package com.technoetic.xplanner.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import com.opensymphony.util.TextUtils;

public class StringUtilities {
	public static String computeSuffix(final String description,
			final String boundaryText, final int maxSuffixLength) {
		final int index = description.toLowerCase().indexOf(
				boundaryText.toLowerCase());
		final String wholeSuffix = description.substring(index
				+ boundaryText.length());
		String matchSuffix = StringUtilities.getShortSuffix(wholeSuffix,
				maxSuffixLength);
		if (matchSuffix.length() < wholeSuffix.length()) {
			matchSuffix += "...";
		}
		return matchSuffix;
	}

	public static String computePrefix(final String description,
			final String boundaryText, final int maxSuffixLength) {
		final int index = description.toLowerCase().indexOf(
				boundaryText.toLowerCase());
		final String wholePrefix = description.substring(0, index);
		String matchPrefix = StringUtilities.getShortPrefix(wholePrefix,
				maxSuffixLength);
		if (matchPrefix.length() < wholePrefix.length()) {
			matchPrefix = "..." + matchPrefix;
		}
		return matchPrefix;
	}

	public static String replaceQuotationMarks(final String text) {
		if (text == null) {
			return "";
		}
		String newText = text.replaceAll("\"", "''");
		newText = newText.replaceAll("'", "\\\\'");
		return newText;
	}

	public static String getShortSuffix(final String wholeSuffix,
			final int maxSuffixLength) {
		int count = 0;
		int pos = 0;
		while (pos < wholeSuffix.length() && count < maxSuffixLength) {
			count = StringUtilities.adjustCountBasedOnChar(
					wholeSuffix.charAt(pos), count);
			pos++;
		}
		return wholeSuffix.substring(0, pos);
	}

	private static int adjustCountBasedOnChar(final char currentChar, int count) {
		if (currentChar != ' ' && currentChar != '\n') {
			count++;
		}
		return count;
	}

	public static String getShortPrefix(final String wholePrefix,
			final int maxPrefixLength) {
		int pos = wholePrefix.length() - 1;
		int count = 0;
		while (pos >= 0 && count < maxPrefixLength) {
			count = StringUtilities.adjustCountBasedOnChar(
					wholePrefix.charAt(pos), count);
			pos--;
		}
		return wholePrefix.substring(pos + 1);
	}

	public static String getFirstNLines(final String text,
			final int desiredLineCount) {
		final BufferedReader bufReader = new BufferedReader(new StringReader(
				text));
		final StringBuffer stringBuffer = new StringBuffer();
		int i = 1;
		try {
			String str = bufReader.readLine();
			while (str != null && i <= desiredLineCount) {
				stringBuffer.append(str);
				if (i < desiredLineCount) {
					stringBuffer.append(" ");
				}
				i++;
				str = bufReader.readLine();
			}
		} catch (final IOException e) {
			// Don't care
		}
		return stringBuffer.toString();
	}

	public static String htmlEncode(final String s) {
		if (!TextUtils.stringSet(s)) {
			return "";
		}

		final StringBuffer str = new StringBuffer();

		for (int j = 0; j < s.length(); j++) {
			str.append(StringUtilities.htmlEncode(s.charAt(j)));
		}

		return str.toString();
	}

	public static String htmlEncode(final char c) {
		switch (c) {
		case '"':
			return "&quot;";
		case '&':
			return "&amp;";
		case '<':
			return "&lt;";
		case '>':
			return "&gt;";
		default:
			return "" + c;
		}
	}

}
