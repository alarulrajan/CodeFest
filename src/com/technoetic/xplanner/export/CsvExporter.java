package com.technoetic.xplanner.export;

import java.io.ByteArrayOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.classic.Session;

public class CsvExporter implements Exporter {

	private String encoding = "UTF-8";

	public void setEncoding(final String encoding) {
		this.encoding = encoding;
	}

	public void setDelimiter(final String delimiter) {
	}

	public String getFileExtension() {
		return "csv";
	}

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

	@Override
	public void initializeHeaders(final HttpServletResponse response) {
		response.setContentType("text/csv; charset=" + this.encoding);
		// response.setCharacterEncoding(encoding);
	}
}