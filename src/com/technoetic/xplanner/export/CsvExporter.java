package com.technoetic.xplanner.export;

import java.io.ByteArrayOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.classic.Session;

/**
 * The Class CsvExporter.
 */
public class CsvExporter implements Exporter {

	/** The encoding. */
	private String encoding = "UTF-8";

	/**
     * Sets the encoding.
     *
     * @param encoding
     *            the new encoding
     */
	public void setEncoding(final String encoding) {
		this.encoding = encoding;
	}

	/**
     * Sets the delimiter.
     *
     * @param delimiter
     *            the new delimiter
     */
	public void setDelimiter(final String delimiter) {
	}

	/**
     * Gets the file extension.
     *
     * @return the file extension
     */
	public String getFileExtension() {
		return "csv";
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.export.Exporter#export(org.hibernate.classic.Session, java.lang.Object)
	 */
	@Override
	public byte[] export(final Session session, final Object object)
			throws ExportException {
		final ByteArrayOutputStream data = new ByteArrayOutputStream();
		return data.toByteArray();
		// for (Object item : items) {
		// BeanWrapperImpl wrapper = new BeanWrapperImpl(item);
		// String[] properties = {"", ""};
		// for (String property : properties ) {
		// Object value = wrapper.getPropertyValue(property);
		// if (value instanceof Collection) {
		// Iterator<?> it = ((Collection<?>) value).iterator();
		// while (it.hasNext()) {
		// out.print(it.next());
		// out.print(' ');
		// }
		// }
		// else if (value != null) {
		// out.print(value);
		// }
		// out.print(delimiter);
		// }
		// out.println();
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.export.Exporter#initializeHeaders(javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void initializeHeaders(final HttpServletResponse response) {
		response.setContentType("text/csv; charset=" + this.encoding);
		// response.setCharacterEncoding(encoding);
	}
}