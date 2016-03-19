package com.technoetic.xplanner.tags;

/**
 * The Class AccessKeyTransformer.
 */
public class AccessKeyTransformer {
	
	/**
     * Removes the mnemonic markers.
     *
     * @param text
     *            the text
     * @return the string
     */
	public static String removeMnemonicMarkers(final String text) {
		if (text == null) {
			return null;
		}
		if (AccessKeyTransformer.getAccessKey(text) != 0) {
			return text.replaceFirst("&(.)",
					"<span class=\"mnemonic\">$1</span>");
		}
		return text.replaceAll("&&", "&");
	}

	/**
     * Gets the html.
     *
     * @param text
     *            the text
     * @return the html
     */
	public static String getHtml(final String text) {
		final char mnemonic = AccessKeyTransformer.getAccessKey(text);
		if (mnemonic == 0) {
			return "";
		}
		String properties = " id=\"aK" + mnemonic + "\"" + " accesskey=\""
				+ mnemonic + "\"";
		properties += " title= \"ALT+" + mnemonic + "\"";
		return properties;
	}

	/**
     * Gets the access key.
     *
     * @param text
     *            the text
     * @return the access key
     */
	public static char getAccessKey(final String text) {
		if (text == null) {
			return 0;
		}
		final int pos = text.indexOf('&');
		if (pos == -1 || pos == text.length() - 1) {
			return 0;
		}
		final char mnemonic = Character.toUpperCase(text.charAt(pos + 1));
		if (!Character.isLetterOrDigit(mnemonic)) {
			return 0;
		}
		return mnemonic;
	}
}