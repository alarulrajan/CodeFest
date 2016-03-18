package com.technoetic.xplanner.tags;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import com.technoetic.xplanner.format.DecimalFormat;

public class ProgressBarImage extends BufferedImage {
	private final double value;
	private final double maxValue;
	private Color valueColor = Color.BLUE;
	private Color maxValueColor = Color.RED;
	private final DecimalFormat format;
	private final double captionValue;

	public static void main(final String[] args) throws IOException {
		final JFrame jFrame = new JFrame("Test");
		jFrame.setSize(800, 700);
		final ProgressBarImage image = new ProgressBarImage(100, 20, 12.9,
				Color.BLUE, 16.8, Color.RED, new DecimalFormat(
						Locale.getDefault(), ""));
		final FileOutputStream fos = new FileOutputStream("c:\\temp\\pic.jpeg");
		// BufferedOutputStream bos = new BufferedOutputStream( fos );
		image.encodeJPEG(fos, (float) 1.0);
		fos.flush();
		fos.close();

		jFrame.getContentPane().add(
				new JLabel(new ImageIcon("c:\\temp\\pic.jpeg")));
		jFrame.pack();
		jFrame.show();
	}

	public static class Model {
		public int width;
		public int height;
		public double value;
		public Color valueColor;
		public double maxValue;
		public Color maxValueColor;
		public DecimalFormat format;
		private final double captionValue;

		public Model(final int width, final int height, final double value,
				final Color valueColor, final double maxValue,
				final Color maxValueColor, final DecimalFormat format,
				final double captionValue) {
			this.width = width;
			this.height = height;
			this.value = value;
			this.valueColor = valueColor;
			this.maxValue = maxValue;
			this.maxValueColor = maxValueColor;
			this.format = format;
			this.captionValue = captionValue;
		}
	}

	public ProgressBarImage(final int width, final int height,
			final double value, final Color valueColor, final double maxValue,
			final Color maxValueColor, final DecimalFormat format) {
		this(new Model(width, height, value, valueColor, maxValue,
				maxValueColor, format, value));
	}

	public ProgressBarImage(final Model model) {
		super(model.width, model.height /* model.height */,
				BufferedImage.TYPE_INT_RGB);
		this.value = model.value;
		this.valueColor = model.valueColor;
		this.maxValue = model.maxValue;
		this.maxValueColor = model.maxValueColor;
		this.format = model.format;
		this.captionValue = model.captionValue;
		this.init();
	}

	private void init() {
		final Graphics2D drawGraphics = this.createGraphics();
		final Graphics2D textGraphics = this.getTextGraphics();

		final int rightMarginInPixels = 0; // getValueTextWidth(textGraphics,
											// maxValue);
		final int valueInPixels = this.getPixelsForValue();

		this.drawRectangle(drawGraphics, 0, 0, this.getWidth(),
				this.getHeight(), Color.WHITE);
		this.drawBar(drawGraphics, 0, valueInPixels, this.getHeight(),
				this.valueColor);
		if (valueInPixels < this.getWidth()) {
			this.drawBar(drawGraphics, valueInPixels, this.getWidth()
					- valueInPixels - rightMarginInPixels, this.getHeight(),
					this.maxValueColor);
		}

		this.drawValue(textGraphics, valueInPixels, this.getHeight() - 2);
		// if (valueInPixels < getWidth())
		// drawValue(textGraphics, getWidth()-rightMarginInPixels, getHeight() -
		// 2, maxValue, Color.BLACK);
	}

	private void drawValue(final Graphics2D g, final int x, final int y) {
		final int textWidth = this.getValueTextWidth(g);
		int xText = x;
		Color bgColor = this.maxValueColor;
		if (textWidth < x) {
			xText = x - textWidth;
			bgColor = this.valueColor;
		}
		// g.drawString(text, xText, y);
		this.drawValue(g, xText, y, this.getTextColor(bgColor));
	}

	private void drawValue(final Graphics2D g, final int x, final int y,
			final Color color) {
		g.setColor(color);
		final Font oldFont = g.getFont();
		// if (!color.equals(Color.BLACK)) {
		// g.setFont(g.getFont().deriveFont(Font.BOLD));
		// }
		final TextLayout tl = new TextLayout(this.getCaption(), g.getFont(),
				g.getFontRenderContext());
		tl.draw(g, x, y);
		g.setFont(oldFont);
	}

	private String getCaption() {
		return this.format.format(this.captionValue);
	}

	private int getValueTextWidth(final Graphics2D g) {
		return SwingUtilities.computeStringWidth(g.getFontMetrics(),
				this.getCaption());
	}

	private Graphics2D getTextGraphics() {
		final Graphics2D g = this.createGraphics();
		g.setFont(new Font("SansSerif", Font.BOLD, this.getFontSize()));
		// g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
		// RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setPaintMode();
		// g.setXORMode(Color.BLACK);
		return g;
	}

	private int getFontSize() {
		return (int) Math.round(this.getHeight() * 0.91);
	}

	private Color getTextColor(final Color bgColor) {
		Color color = null;
		if (bgColor.equals(Color.RED) || bgColor.equals(Color.BLUE)) {
			color = Color.WHITE;
		} else {
			color = Color.BLACK;
		}
		return color;
	}

	private void drawBar(final Graphics2D g, final int x, final int width,
			final int height, final Color color) {
		this.drawRectangle(g, x, 0, width, height, color);
	}

	private void drawRectangle(final Graphics2D g, final int x, final int y,
			final int width, final int height, final Color color) {
		g.setColor(color);
		g.fillRect(x, y, width, height);
	}

	private int getPixelsForValue() {
		return (int) Math.round(this.getWidth() / this.maxValue * this.value);
	}

	public void encodeJPEG(final OutputStream outputStream,
			final float outputQuality) throws java.io.IOException {
		final int outputWidth = this.getWidth(null);
		if (outputWidth < 1) {
			throw new IllegalArgumentException("output image width "
					+ outputWidth + " is out of range");
		}
		final int outputHeight = this.getHeight(null);
		if (outputHeight < 1) {
			throw new IllegalArgumentException("output image height "
					+ outputHeight + " is out of range");
		}

		// Get a buffered image from the image.
		final BufferedImage bi = new BufferedImage(outputWidth, outputHeight,
				BufferedImage.TYPE_INT_RGB);
		final Graphics2D biContext = bi.createGraphics();
		biContext.drawImage(this, 0, 0, null);
		// Note that additional drawing such as watermarks or logos can be
		// placed here.

		// com.sun.image.codec.jpeg package is included in sun and ibm sdk 1.3
		/*
		 * JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder( outputStream
		 * ); // The default quality is 0.75. JPEGEncodeParam jep =
		 * JPEGCodec.getDefaultJPEGEncodeParam( bi ); jep.setQuality(
		 * outputQuality, true ); encoder.encode( bi, jep );
		 */
		// encoder.encode( bi );
		outputStream.flush();
	} // encodeImage

}
