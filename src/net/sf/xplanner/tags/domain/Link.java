package net.sf.xplanner.tags.domain;

/**
 * XplannerPlus, agile planning software.
 *
 * @author Maksym. Copyright (C) 2009 Maksym Chyrkov This program is free
 *         software: you can redistribute it and/or modify it under the terms of
 *         the GNU General Public License as published by the Free Software
 *         Foundation, either version 3 of the License, or (at your option) any
 *         later version.
 * 
 *         This program is distributed in the hope that it will be useful, but
 *         WITHOUT ANY WARRANTY; without even the implied warranty of
 *         MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *         General Public License for more details.
 * 
 *         You should have received a copy of the GNU General Public License
 *         along with this program. If not, see <http://www.gnu.org/licenses/>
 */
public class Link {
	
	/** The text. */
	private String text;
	
	/** The id. */
	private String id;
	
	/** The url. */
	private String url;
	
	/** The access key. */
	private String accessKey;

	/**
     * Gets the text.
     *
     * @return the text
     */
	public String getText() {
		return this.text;
	}

	/**
     * Sets the text.
     *
     * @param text
     *            the new text
     */
	public void setText(final String text) {
		this.text = text;
	}

	/**
     * Gets the url.
     *
     * @return the url
     */
	public String getUrl() {
		return this.url;
	}

	/**
     * Sets the url.
     *
     * @param url
     *            the new url
     */
	public void setUrl(final String url) {
		this.url = url;
	}

	/**
     * Gets the access key.
     *
     * @return the access key
     */
	public String getAccessKey() {
		return this.accessKey;
	}

	/**
     * Sets the access key.
     *
     * @param accessKey
     *            the new access key
     */
	public void setAccessKey(final String accessKey) {
		this.accessKey = accessKey;
	}

	/**
     * Gets the id.
     *
     * @return the id
     */
	public String getId() {
		return this.id;
	}

	/**
     * Sets the id.
     *
     * @param id
     *            the new id
     */
	public void setId(final String id) {
		this.id = id;
	}

}
