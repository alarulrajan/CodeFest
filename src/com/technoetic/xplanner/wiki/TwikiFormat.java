package com.technoetic.xplanner.wiki;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.oro.text.perl.MalformedPerl5PatternException;
import org.apache.oro.text.perl.Perl5Util;
import org.apache.oro.text.regex.PatternMatcherInput;

import com.technoetic.xplanner.XPlannerProperties;

public class TwikiFormat implements WikiFormat {
	private final Logger log = Logger.getLogger(this.getClass());
	private final Perl5Util perl = new Perl5Util();
	private final ArrayList codeStack = new ArrayList();
	private static final String mailSubstitution = "s/([\\s\\(])(?:mailto\\:)*([a-zA-Z0-9\\-\\_\\.\\+]+)\\@"
			+ "([a-zA-Z0-9\\-\\_\\.]+)\\.([a-zA-Z0-9\\-\\_]+)(?=[\\s\\.\\,\\;\\:\\!\\?\\)])/"
			+ "$1<a href=\"mailto:$2@$3.$4\">$2@$3.$4<\\/a>/go";
	private static final String fancyHr = "s@^([a-zA-Z0-9]+)----*@<table width=\"100%\"><tr><td valign=\"bottom\"><h2>$1</h2></td>"
			+ "<td width=\"98%\" valign=\"middle\"><hr /></td></tr></table>@o";
	private static final String escapeRegexp = "s@([\\*\\?\\.\\[\\](\\)])@\\\\$1@g";
	private static final String urlPattern = "m@(^|[-*\\W])((\\w+):([\\w\\$\\-_\\@\\&\\;\\.&\\+\\?/:#%~=]+))(\\[([^\\]]+)\\]|)@";
	private static final String headerPatternDa = "^---+(\\++|\\#+)\\s+(.+)\\s*$"; // '---++
																					// Header',
																					// '---##
																					// Header'
	private static final String headerPatternSp = "^\\t(\\++|\\#+)\\s+(.+)\\s*$"; // '
																					// ++
																					// Header',
																					// '
																					// +
																					// Header'
	private static final String headerPatternHt = "^<h([1-6])>\\s*(.+?)\\s*</h[1-6]>"; // '<h6>Header</h6>
	private static final String wikiWordPattern = "(^|[^\\w:/])(\\w+\\.|)([A-Z][a-z]\\w*[A-Z][a-z]\\w*)(\\b|$)";
	private static final String wikiWordMatch = "m/"
			+ TwikiFormat.wikiWordPattern + "/";
	private static Map schemeHandlers;
	private ExternalWikiAdapter externalWikiAdapter = null;
	private MalformedPerl5PatternException malformedPattern = null;
	private Properties properties = XPlannerProperties.getProperties();

	public TwikiFormat() {
		this(new HashMap());
	}

	public TwikiFormat(final Map schemeTranslations) {
		TwikiFormat.schemeHandlers = schemeTranslations;
		if (this.properties.getProperty("twiki.wikiadapter") != null) {
			try {
				this.externalWikiAdapter = (ExternalWikiAdapter) Class.forName(
						this.properties.getProperty("twiki.wikiadapter"))
						.newInstance();
			} catch (final Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public String format(final String text) {
		boolean inPreformattedSection = false;
		boolean inVerbatimSection = false;
		boolean inList = false;
		boolean inTable = false;
		final PatternMatcherInput patternMatcherInput = new PatternMatcherInput(
				"");
		final BufferedReader reader = new BufferedReader(new StringReader(text));
		final StringBuffer outputText = new StringBuffer();
		try {
			String line = reader.readLine();
			while (line != null) {
				try {
					if (this.perl.match("m|<pre>|i", line)) {
						inPreformattedSection = true;
					}
					if (this.perl.match("m|</pre>|i", line)) {
						inPreformattedSection = false;
					}
					if (this.perl.match("m|<verbatim>|", line)) {
						line = this.perl.substitute("s|<verbatim>|<pre>|goi",
								line);
						inVerbatimSection = true;

					}
					if (this.perl.match("m|</verbatim>|", line)) {
						line = this.perl.substitute("s|</verbatim>|</pre>|goi",
								line);
						inVerbatimSection = false;
					}
					final boolean escapeBrackets = new Boolean(
							this.properties.getProperty(
									WikiFormat.ESCAPE_BRACKETS_KEY, "true"))
							.booleanValue();
					if (inPreformattedSection || inVerbatimSection) {
						if (inVerbatimSection) {
							line = this.perl.substitute("s/&/&amp;/go", line);
							if (escapeBrackets) {
								line = this.perl
										.substitute("s/</&lt;/go", line);
								line = this.perl
										.substitute("s/>/&gt;/go", line);
							}
							line = this.perl.substitute(
									"s/&lt;pre&gt;/<pre>/go", line);
						}
					} else {
						// Blockquote
						line = this.perl.substitute(
								"s|^>(.*?)$|> <cite> $1 </cite><br>|go", line);

						// Embedded HTML - \263 is a special translation token
						// -- Allow standalone "<!--"
						line = this.perl.substitute("s/<(!--)/\\\\263$1/go",
								line);
						// -- Allow standalone "-->"
						line = this.perl.substitute("s/(--)>/$1\\\\263/go",
								line);
						line = this.perl.substitute(
								"s/<(\\S.*?)>/\\\\263$1$\\\\263/g", line);
						if (escapeBrackets) {
							line = this.perl.substitute("s/</&lt;/go", line);
							line = this.perl.substitute("s/>/&gt;/go", line);
						}
						line = this.perl.substitute(
								"s/\\\\263(\\S.*?)\\\\263/<$1>/g", line);
						line = this.perl.substitute("s/(--)\\\\263/$1>/go",
								line);
						line = this.perl.substitute("s/\\\\263(!--)/<$1/go",
								line);

						// Entities
						line = this.perl.substitute("s/&(\\w+?);/\\\\236$1;/g",
								line); // "&abc;"
						line = this.perl.substitute(
								"s/&(#[0-9]+);/\\\\236$1;/g", line); // "&#123;"
						line = this.perl.substitute("s/&/&amp;/go", line); // escape
																			// standalone
																			// "&"
						line = this.perl.substitute("s/\\\\236/&/go", line);

						// Headings
						// -- '<h6>...</h6>' HTML rule
						patternMatcherInput.setInput(line);
						while (this.perl.match("m|"
								+ TwikiFormat.headerPatternHt + "|",
								patternMatcherInput)) {
							line = this.perl.substitute(
									"s@"
											+ TwikiFormat.headerPatternHt
											+ "@"
											+ this.makeAnchorHeading(this.perl
													.group(2), Integer
													.parseInt(this.perl
															.group(1)))
											+ "@goi", line);
						}
						// -- '\t+++++++' rule
						patternMatcherInput.setInput(line);
						while (this.perl.match("m|"
								+ TwikiFormat.headerPatternSp + "|",
								patternMatcherInput)) {
							line = this.perl
									.substitute(
											"s@"
													+ TwikiFormat.headerPatternSp
													+ "@"
													+ this.makeAnchorHeading(
															this.perl.group(2),
															this.perl.group(1)
																	.length())
													+ "@goi", line);
						}
						// -- '---+++++++' rule
						patternMatcherInput.setInput(line);
						while (this.perl.match("m|"
								+ TwikiFormat.headerPatternDa + "|",
								patternMatcherInput)) {
							line = this.perl
									.substitute(
											"s@"
													+ TwikiFormat.headerPatternDa
													+ "@"
													+ this.makeAnchorHeading(
															this.perl.group(2),
															this.perl.group(1)
																	.length())
													+ "@goi", line);
						}

						// Lists etc.
						// -- TWiki seems to be looking for tabs at the
						// beginning of the line.
						// However, the formatting clearly uses 3 spaces, so
						// ???.
						// The following substitutes tabs for groups of 3 spaces
						// at the begininng of
						// the line.
						while (this.perl.match("m/^(\t*)   /", line)) {
							line = this.perl.substitute("s/^(\t*)   /$1\t/o",
									line);
						}
						if (this.perl.match("m/^\\s*$/", line)) {
							line = this.perl.substitute("s/^\\s*$/<p\\/>/o",
									line);
							inList = false;
						}
						if (this.perl.match("m/^(\\S+?)/", line)) {
							inList = false;
						}
						if (this.perl.match("m/^(\\t+)(\\S+?):\\s/", line)) {
							line = this.perl
									.substitute(
											"s/^(\\t+)(\\S+?):\\s/<dt> $2<dd> /o",
											line);
							this.emitCode(outputText, "dl", this.perl.group(1)
									.length());
							inList = true;
						}
						if (this.perl.match("m/^(\\t+)\\* /", line)) {
							line = this.perl.substitute(
									"s/^(\\t+)\\* /<li> /o", line);
							this.emitCode(outputText, "ul", this.perl.group(1)
									.length());
							inList = true;
						}
						if (this.perl.match("m/^(\\t+)\\d+\\.?/", line)) {
							line = this.perl.substitute(
									"s/^(\\t+)\\d+\\.? /<li> /o", line);
							this.emitCode(outputText, "ol", this.perl.group(1)
									.length());
							inList = true;
						}

						if (inList == false) {
							this.emitCode(outputText, "", 0);
						}

						// Table
						if (this.perl.match("m/^(\\s*)\\|(.*)/", line)) {
							line = this.perl
									.substitute(
											"s/^(\\s*)\\|(.*)/"
													+ this.emitTableRow("",
															this.perl.group(2),
															inTable) + "/",
											line);
							inTable = true;
						} else if (inTable) {
							outputText.append("</table>");
							inTable = false;
						}

						// Emphasizing
						if (this.perl
								.match("m/([\\s\\(]*)==([^\\s]+?|[^\\s].*?[^\\s])==([\\s,.;:!?<)]|$)/",
										line)) {
							line = this.perl
									.substitute(
											"s/([\\s\\(]*)==([^\\s]+?|[^\\s].*?[^\\s])==([\\s,.;:!?)<]|$)/"
													+ "$1<code><b>$2<\\/b><\\/code>$3/g",
											line);
						}

						if (this.perl
								.match("m/([\\s\\(]*)__([^\\s]+?|[^\\s].*?[^\\s])__([\\s,.;:!?)<]|$)/",
										line)) {
							line = this.perl
									.substitute(
											"s/([\\s\\(]*)__([^\\s]+?|[^\\s].*?[^\\s])__([\\s,.;:!?)<]|$)/"
													+ "$1<strong><em>$2<\\/em><\\/strong>$3/g",
											line);
						}

						if (this.perl
								.match("m/([\\s\\(]*)\\*([^\\s]+?|[^\\s].*?[^\\s])\\*([\\s,.;:!?)<]|$)/",
										line)) {
							line = this.perl.substitute(
									"s/([\\s\\(]*)\\*([^\\s]+?|[^\\s].*?[^\\s])\\*([\\s,.;:!?)<]|$)/"
											+ "$1<strong>$2<\\/strong>$3/g",
									line);
						}

						if (this.perl
								.match("m/([\\s\\(]*)_([^\\s]+?|[^\\s].*?[^\\s])_([\\s,.;:!?)<]|$)/",
										line)) {
							line = this.perl.substitute(
									"s/([\\s\\(]*)_([^\\s]+?|[^\\s].*?[^\\s])_([\\s,.;:!?)<]|$)/"
											+ "$1<em>$2<\\/em>$3/g", line);
						}

						if (this.perl
								.match("m/([\\s\\(]*)=([^\\s]+?|[^\\s].*?[^\\s])=([\\s,.;:!?)<]|$)/",
										line)) {
							line = this.perl.substitute(
									"s/([\\s\\(]*)=([^\\s]+?|[^\\s].*?[^\\s])=([\\s,.;:!?)<]|$)/"
											+ "$1<code>$2<\\/code>$3/g", line);
						}

						// Mailto
						line = this.perl.substitute(
								TwikiFormat.mailSubstitution, line);

						// # Horizontal rule
						line = this.perl.substitute("s/^---+/<hr\\/>/o", line);
						line = this.perl.substitute(TwikiFormat.fancyHr, line);

						// patternMatcherInput.setInput(line);
						// while (perl.match("m|" + headerPatternHt + "|",
						// patternMatcherInput)) {
						// line = perl.substitute("s@" + headerPatternHt + "@" +
						// makeAnchorHeading(perl.group(2),
						// Integer.parseInt(perl.group(1))) + "@goi", line);
						// }

						// WikiWord
						if (this.externalWikiAdapter != null) {
							patternMatcherInput.setInput(line);
							while (this.perl.match(TwikiFormat.wikiWordMatch,
									patternMatcherInput)) {
								final String wikiWord = this.perl.group(2)
										+ this.perl.group(3);
								line = this.perl
										.substitute(
												"s\0"
														+ wikiWord
														+ "\0"
														+
														/* perl.group(1)+ */
														this.externalWikiAdapter
																.formatWikiWord(wikiWord)
														+ this.perl.group(4)
														+ "\0", line);
							}
						}

						// Handle embedded URLs
						patternMatcherInput.setInput(line);
						while (this.perl.match(TwikiFormat.urlPattern,
								patternMatcherInput)) {
							String link = this.perl.group(0);
							final String previousText = this.perl.group(1);
							final String scheme = this.perl.group(3);
							final String location = this.perl.group(4);
							final String linkText = this.perl.group(6);
							String formattedLink = this.formatLink(
									previousText, scheme, location, linkText);
							if (formattedLink != null) {
								link = this.perl.substitute(
										TwikiFormat.escapeRegexp, link);
								formattedLink = formattedLink.replaceAll("@",
										"\\\\@");
								line = this.perl.substitute("s@" + link + "@"
										+ formattedLink + "@go", line);
							}
						}

					}
				} catch (final MalformedPerl5PatternException ex) {
					// just continue, set flag for testing purposes
					this.malformedPattern = ex;
				}
				outputText.append(line);
				outputText.append("\n");
				line = reader.readLine();
			}
			this.emitCode(outputText, "", 0);
			if (inTable) {
				outputText.append("</table>");
			}
			if (inPreformattedSection || inVerbatimSection) {
				outputText.append("</pre>");
			}
		} catch (final Exception ex) {
			this.log.error("error during formatting", ex);
			outputText.setLength(0);
			outputText.append("[Error during formatting]");
		}
		return outputText.toString();
	}

	@Override
	public void setProperties(final Properties properties) {
		this.properties = properties;
	}

	private String formatLink(final String previousText, final String scheme,
			final String location, String linkText) {
		if (scheme.equals("mailto")) {
			return null;
		}

		final SchemeHandler handler = (SchemeHandler) TwikiFormat.schemeHandlers
				.get(scheme);
		if (handler != null) {
			return previousText
					+ handler.translate(this.properties, scheme, location,
							linkText);
		}
		final String url = scheme + ":" + location;
		if (this.perl.match("m/http|ftp|gopher|news|file|https/", scheme)) {
			if (linkText == null) {
				linkText = url;
			}
			if (this.perl.match("m|\\.(gif|jpg|jpeg|png)(#|$)|i", url)) {
				return previousText + "<img border=\"0\" src=\"" + url + "\"/>";
			} else {
				return previousText + "<a href=\"" + url
						+ "\" target=\"_top\">" + linkText + "</a>";
			}
		}
		String returnFormatLink = "";
		if (linkText != null) {
			returnFormatLink = previousText + url + "[" + linkText + "]";
		} else {
			returnFormatLink = previousText + url;
		}

		return returnFormatLink;
	}

	private String makeAnchorName(String text) {
		text = this.perl.substitute("s/^[\\s\\#\\_]*//o", text); // no leading
																	// space nor
																	// '#', '_'
		text = this.perl.substitute("s/[\\s\\_]*$//o", text); // no trailing
																// space, nor
																// '_'
		text = this.perl.substitute("s/<\\w[^>]*>//goi", text); // remove HTML
																// tags
		text = this.perl.substitute("s/[^a-zA-Z0-9]/_/go", text); // only
																	// allowed
																	// chars
		text = this.perl.substitute("s/__+/_/go", text); // remove excessive '_'
		text = this.perl.substitute("s/^(.{32})(.*)$/$1/o", text); // limit to
																	// 32 chars
		return text;
	}

	private String makeAnchorHeading(String text, final int level) {
		// - Need to build '<nop><h1><a name="atext"> text </a></h1>'
		// type markup.
		// - Initial '<nop>' is needed to prevent subsequent matches.
		// - Need to make sure that <a> tags are not nested, i.e. in
		// case heading has a WikiName that gets linked
		final String anchorName = this.makeAnchorName(text);
		final boolean hasAnchor = this.perl.match("m/<a /i", text)
				|| this.perl.match("m/\\[\\[/", text)
				|| this.perl.match("m/(^|[\\*\\s][\\-\\*\\s]*)([A-Z]{3,})/",
						text)
				|| this.perl
						.match("m/(^|[\\*\\s][\\(\\-\\*\\s]*)([A-Z]+[a-z0-9]*)\\.([A-Z]+[a-z]+[A-Z]+[a-zA-Z0-9]*)/",
								text)
				|| this.perl
						.match("m/(^|[\\*\\s][\\(\\-\\*\\s]*)([A-Z]+[a-z]+[A-Z]+[a-zA-Z0-9]*)/",
								text);
		if (hasAnchor) {
			// # (From TWiki) FIXME: '<h1><a name="atext"></a></h1> WikiName'
			// has an
			// empty <a> tag, which is not HTML conform
			text = "<nop><h" + level + "><a name=\"" + anchorName + "\"> </a> "
					+ text + "</h" + level + ">";
		} else {
			text = "<nop><h" + level + "><a name=\"" + anchorName + "\"> "
					+ text + " </a></h" + level + ">";
		}
		return text;
	}

	public void emitCode(final StringBuffer result, final String code,
			final int depth) {
		while (this.codeStack.size() > depth) {
			final String c = (String) this.codeStack.remove(this.codeStack
					.size() - 1);
			result.append("</").append(c).append(">\n");
		}
		while (this.codeStack.size() < depth) {
			this.codeStack.add(code);
			result.append("<").append(code).append(">\n");
		}

		// if( ( $#code > -1 ) && ( $code[$#code] ne $code ) ) {
		if (!this.codeStack.isEmpty()
				&& !this.codeStack.get(this.codeStack.size() - 1).equals(code)) {
			result.append("</")
					.append(this.codeStack.get(this.codeStack.size() - 1))
					.append("><").append(code).append(">\n");
			this.codeStack.set(this.codeStack.size() - 1, code);
		}
	}

	public String emitTableRow(final String previousText, String row,
			final boolean inTable) {
		final StringBuffer result = new StringBuffer();
		if (inTable) {
			result.append(previousText).append("<tr class=\"twiki\">");
		} else {
			result.append(previousText);
			result.append("<table class=\"twiki\" border=\"1\" cellspacing=\"0\" cellpadding=\"1\">");
			result.append("<tr class=\"twiki\">");
		}
		row = this.perl.substitute("s/\\t/    /go", row); // change tab to
															// spaces
		row = this.perl.substitute("s/\\s*$//o", row); // remove trailing white
														// space
		while (this.perl.match("m/(\\|\\|+)/", row)) {
			// calc COLSPAN
			row = this.perl.substitute(
					"s/(\\|\\|+)/\\\\236" + this.perl.group(1).length()
							+ "\\|/go", row);
		}

		final ArrayList cells = new ArrayList();
		this.perl.split(cells, "/\\|/", row);
		for (int i = 0, n = cells.size() - 1; i < n; i++) {
			String cell = (String) cells.get(i);
			// TODO 3/21/05 JM Added during merge from the Sabre codebase.
			// Verify it was added in the Sabre codebase otherwise remove it
			cell = this.perl.substitute("s/\\//-/go", cell);
			String attribute = "";
			if (this.perl.match("m/\\\\236([0-9]+)/", cell)) {
				cell = this.perl.substitute("s/\\\\236([0-9]+)//", cell);
				attribute = " colspan=\""
						+ Integer.parseInt(this.perl.group(1)) + "\"";
			}
			cell = this.perl.substitute("s/^\\s+$/ &nbsp; /o", cell);
			this.perl.match("m/^(\\s*).*?(\\s*)$/", cell);
			final String left = this.perl.group(1);
			final String right = this.perl.group(2);
			if (left.length() > right.length()) {
				if (right.length() <= 1) {
					attribute += " align=\"right\"";
				} else {
					attribute += " align=\"center\"";
				}
			}
			if (this.perl.match("m/^\\s*(\\*.*\\*)\\s*$/", cell)) {
				result.append("<th").append(attribute)
						.append(" class=\"twiki\" bgcolor=\"#99CCCC\">")
						.append(this.perl.group(1)).append("<\\/th>");
			} else {
				result.append("<td").append(attribute)
						.append(" class=\"twiki\">").append(cell)
						.append("<\\/td>");
			}
		}
		result.append("<\\/tr>");
		return result.toString();
	}

	public void setExternalWikiAdapter(
			final ExternalWikiAdapter wikiWordFormatter) {
		this.externalWikiAdapter = wikiWordFormatter;
	}

	public MalformedPerl5PatternException getMalformedPatternException() {
		return this.malformedPattern;
	}
}
