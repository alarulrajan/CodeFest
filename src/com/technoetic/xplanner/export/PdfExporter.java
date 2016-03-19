package com.technoetic.xplanner.export;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

import org.apache.commons.lang.StringUtils;
import org.hibernate.classic.Session;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * The Class PdfExporter.
 */
public class PdfExporter implements Exporter {
	
	/** The Constant SCALE_FACTOR. */
	public static final int SCALE_FACTOR = 2;
	
	/** The Constant NORMAL_TEXT_FONT. */
	public static final Font NORMAL_TEXT_FONT = new Font(Font.HELVETICA,
			14 * PdfExporter.SCALE_FACTOR, Font.NORMAL);
	
	/** The Constant BOLD_TEXT_FONT. */
	public static final Font BOLD_TEXT_FONT = new Font(Font.HELVETICA,
			14 * PdfExporter.SCALE_FACTOR, Font.BOLD);
	
	/** The Constant TITLE_FONT. */
	public static final Font TITLE_FONT = new Font(Font.HELVETICA,
			24 * PdfExporter.SCALE_FACTOR, Font.BOLD);

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.export.Exporter#initializeHeaders(javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void initializeHeaders(final HttpServletResponse response) {
		response.setHeader("Content-type", "application/pdf");
		response.setHeader("Content-disposition", "inline; filename=export.pdf");
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.export.Exporter#export(org.hibernate.classic.Session, java.lang.Object)
	 */
	@Override
	public byte[] export(final Session session, final Object object)
			throws ExportException {
		ByteArrayOutputStream output = null;
		try {
			output = new ByteArrayOutputStream();
			this.exportToPdf(object, output);
			return output.toByteArray();
		} catch (final DocumentException e) {
			throw new ExportException(e);
		} finally {
			if (output != null) {
				output.reset();
			}
		}
	}

	/**
     * Export to pdf.
     *
     * @param object
     *            the object
     * @param output
     *            the output
     * @throws DocumentException
     *             the document exception
     */
	public void exportToPdf(final Object object, final OutputStream output)
			throws DocumentException {

		PdfWriter docWriter = null;

		final Rectangle pageRectangle = PageSize.LETTER.rotate();
		final Document document = new Document(pageRectangle, 10, 10,
				22 * PdfExporter.SCALE_FACTOR, 10);
		try {
			docWriter = PdfWriter.getInstance(document, output);
			if (object instanceof Iteration) {
				this.write((Iteration) object, document, pageRectangle,
						docWriter);
			} else if (object instanceof UserStory) {
				this.write((UserStory) object, document, pageRectangle,
						docWriter);
			} else if (object instanceof Task) {
				this.write((Task) object, document, pageRectangle, docWriter);
			}
		} finally {
			if (document.isOpen()) {
				document.close();
			}
			if (docWriter != null) {
				docWriter.close();
			}
		}
	}

	/**
     * Write.
     *
     * @param iteration
     *            the iteration
     * @param document
     *            the document
     * @param pageRectangle
     *            the page rectangle
     * @param docWriter
     *            the doc writer
     * @throws DocumentException
     *             the document exception
     */
	private void write(final Iteration iteration, final Document document,
			final Rectangle pageRectangle, final PdfWriter docWriter)
			throws DocumentException {
		for (final Iterator<UserStory> iterator = iteration.getUserStories()
				.iterator(); iterator.hasNext();) {
			final UserStory userStory = iterator.next();
			this.write(userStory, document, pageRectangle, docWriter);
		}
	}

	/**
     * Write.
     *
     * @param story
     *            the story
     * @param document
     *            the document
     * @param pageRectangle
     *            the page rectangle
     * @param docWriter
     *            the doc writer
     * @throws DocumentException
     *             the document exception
     */
	private void write(final UserStory story, final Document document,
			final Rectangle pageRectangle, final PdfWriter docWriter)
			throws DocumentException {

		final Person customer = story.getCustomer();
		String customerName = "";
		if (customer != null) {
			customerName = customer.getName();
		}
		this.writeCard(
				document,
				pageRectangle,
				docWriter,
				story.getName(),
				story.getDescription(),
				new Field[] {
						new Field("Customer", customerName),
						new Field("Estimate", Double.toString(story
								.getEstimatedHours())) }, null);
		final Collection<Task> tasks = story.getTasks();
		for (final Iterator<Task> it = tasks.iterator(); it.hasNext();) {
			final Task task = it.next();
			this.write(task, document, pageRectangle, docWriter);
		}
	}

	/**
     * Write.
     *
     * @param task
     *            the task
     * @param document
     *            the document
     * @param pageRectangle
     *            the page rectangle
     * @param docWriter
     *            the doc writer
     * @throws DocumentException
     *             the document exception
     */
	private void write(final Task task, final Document document,
			final Rectangle pageRectangle, final PdfWriter docWriter)
			throws DocumentException {

		this.writeCard(
				document,
				pageRectangle,
				docWriter,
				task.getName(),
				task.getDescription(),
				new Field[] {
						new Field("Acceptor", ""),
						new Field("Estimate", Double.toString(task
								.getEstimatedHours())) }, task.getUserStory()
						.getName());
	}

	/**
     * Write card.
     *
     * @param document
     *            the document
     * @param pageRectangle
     *            the page rectangle
     * @param docWriter
     *            the doc writer
     * @param title
     *            the title
     * @param description
     *            the description
     * @param fields
     *            the fields
     * @param containerTitle
     *            the container title
     * @throws DocumentException
     *             the document exception
     */
	private void writeCard(final Document document,
			final Rectangle pageRectangle, final PdfWriter docWriter,
			final String title, final String description, final Field[] fields,
			final String containerTitle) throws DocumentException {
		document.resetHeader();

		// Phrase titlePhrase = new Phrase(title, new Font(Font.HELVETICA, 24 *
		// SCALE_FACTOR, Font.BOLD));
		// HeaderFooter header = new HeaderFooter(titlePhrase, false);
		// document.setHeader(header);

		if (!document.isOpen()) {
			document.open();
		}

		document.newPage();

		final PdfPTable headerTable = this.newTable(pageRectangle, 1);
		if (containerTitle != null) {
			headerTable.getDefaultCell().setPaddingBottom(-5);
			headerTable.addCell(new Paragraph(containerTitle, new Font(
					Font.HELVETICA, 6 * PdfExporter.SCALE_FACTOR, Font.BOLD)));
		}
		headerTable.getDefaultCell().setBorderWidth(2);
		headerTable.getDefaultCell().setBorder(Rectangle.BOTTOM);
		headerTable.getDefaultCell().setPaddingBottom(15);
		headerTable.addCell(new Paragraph(title, PdfExporter.TITLE_FONT));
		headerTable.getDefaultCell().setBorderWidth(0);
		headerTable.getDefaultCell().setPaddingBottom(0);
		headerTable.addCell(new Paragraph(StringUtils
				.defaultString(description), PdfExporter.NORMAL_TEXT_FONT));
		headerTable.writeSelectedRows(0, -1, 10, pageRectangle.height(),
				docWriter.getDirectContent());

		final PdfPTable table = this.newTable(pageRectangle, 2);

		for (int i = 0; i < fields.length; i++) {
			this.addFieldCell(table, fields[i]);
		}
		table.writeSelectedRows(0, -1, 10f, 10f + table.getRowHeight(0),
				docWriter.getDirectContent());
	}

	/**
     * New table.
     *
     * @param pageRectangle
     *            the page rectangle
     * @param columns
     *            the columns
     * @return the pdf p table
     */
	private PdfPTable newTable(final Rectangle pageRectangle, final int columns) {
		final PdfPTable table = new PdfPTable(columns);
		table.getDefaultCell().setBorderWidth(0);
		table.setTotalWidth(pageRectangle.width() - 20);
		return table;
	}

	/**
     * The Class Field.
     */
	class Field {
		
		/** The title. */
		private final String title;
		
		/** The value. */
		private final String value;

		/**
         * Instantiates a new field.
         *
         * @param title
         *            the title
         * @param value
         *            the value
         */
		public Field(final String title, final String value) {
			this.title = StringUtils.defaultString(title);
			this.value = StringUtils.defaultString(value);
		}

		/**
         * Gets the title.
         *
         * @return the title
         */
		public String getTitle() {
			return this.title;
		}

		/**
         * Gets the value.
         *
         * @return the value
         */
		public String getValue() {
			return this.value;
		}
	}

	/**
     * Adds the field cell.
     *
     * @param table
     *            the table
     * @param field
     *            the field
     */
	private void addFieldCell(final PdfPTable table, final Field field) {
		final Phrase cell = new Phrase(new Chunk("" + field.getTitle() + ": ",
				PdfExporter.BOLD_TEXT_FONT));
		cell.add(new Chunk(field.getValue(), PdfExporter.NORMAL_TEXT_FONT));
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);
	}

	/**
     * The main method.
     *
     * @param args
     *            the arguments
     * @throws Exception
     *             the exception
     */
	public static void main(final String[] args) throws Exception {
		final FileOutputStream stream = new FileOutputStream(
				"XPlannerStories.pdf");
		final PdfExporter exporter = new PdfExporter();
		final Iteration iteration = new Iteration();
		final List<UserStory> stories = new ArrayList<UserStory>();

		final List<Task> tasks = new ArrayList<Task>();
		tasks.add(PdfExporter.newTask("Task 1", "Description of Task 1", 2.0));
		tasks.add(PdfExporter.newTask("Task 2", "Description of Task 2", 3.0));
		stories.add(PdfExporter.newStory("Story 1", new Person(), 1.0, tasks));
		iteration.setUserStories(stories);
		exporter.exportToPdf(iteration, stream);
		stream.close();

	}

	/**
     * New story.
     *
     * @param name
     *            the name
     * @param customer
     *            the customer
     * @param estimatedHours
     *            the estimated hours
     * @param tasks
     *            the tasks
     * @return the user story
     */
	private static UserStory newStory(final String name, final Person customer,
			final double estimatedHours, final List<Task> tasks) {
		final UserStory story = new UserStory();
		story.setName(name);
		story.setCustomer(customer);
		story.setEstimatedHoursField(estimatedHours);
		story.setTasks(tasks);
		return story;
	}

	/**
     * New task.
     *
     * @param name
     *            the name
     * @param description
     *            the description
     * @param estimatedHours
     *            the estimated hours
     * @return the task
     */
	private static Task newTask(final String name, final String description,
			final double estimatedHours) {
		final Task task = new Task();
		task.setName(name);
		task.setDescription(description);
		task.setEstimatedHours(estimatedHours);
		return task;
	}
}