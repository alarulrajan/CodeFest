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

/**
 * The Class ProgressBarImage.
 */
public class ProgressBarImage extends BufferedImage {
    
    /** The value. */
    private final double value;
    
    /** The max value. */
    private final double maxValue;
    
    /** The value color. */
    private Color valueColor = Color.BLUE;
    
    /** The max value color. */
    private Color maxValueColor = Color.RED;
    
    /** The format. */
    private final DecimalFormat format;
    
    /** The caption value. */
    private final double captionValue;

    /**
     * The main method.
     *
     * @param args
     *            the arguments
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
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

    /**
     * The Class Model.
     */
    public static class Model {
        
        /** The width. */
        public int width;
        
        /** The height. */
        public int height;
        
        /** The value. */
        public double value;
        
        /** The value color. */
        public Color valueColor;
        
        /** The max value. */
        public double maxValue;
        
        /** The max value color. */
        public Color maxValueColor;
        
        /** The format. */
        public DecimalFormat format;
        
        /** The caption value. */
        private final double captionValue;

        /**
         * Instantiates a new model.
         *
         * @param width
         *            the width
         * @param height
         *            the height
         * @param value
         *            the value
         * @param valueColor
         *            the value color
         * @param maxValue
         *            the max value
         * @param maxValueColor
         *            the max value color
         * @param format
         *            the format
         * @param captionValue
         *            the caption value
         */
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

    /**
     * Instantiates a new progress bar image.
     *
     * @param width
     *            the width
     * @param height
     *            the height
     * @param value
     *            the value
     * @param valueColor
     *            the value color
     * @param maxValue
     *            the max value
     * @param maxValueColor
     *            the max value color
     * @param format
     *            the format
     */
    public ProgressBarImage(final int width, final int height,
            final double value, final Color valueColor, final double maxValue,
            final Color maxValueColor, final DecimalFormat format) {
        this(new Model(width, height, value, valueColor, maxValue,
                maxValueColor, format, value));
    }

    /**
     * Instantiates a new progress bar image.
     *
     * @param model
     *            the model
     */
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

    /**
     * Inits the.
     */
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

    /**
     * Draw value.
     *
     * @param g
     *            the g
     * @param x
     *            the x
     * @param y
     *            the y
     */
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

    /**
     * Draw value.
     *
     * @param g
     *            the g
     * @param x
     *            the x
     * @param y
     *            the y
     * @param color
     *            the color
     */
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

    /**
     * Gets the caption.
     *
     * @return the caption
     */
    private String getCaption() {
        return this.format.format(this.captionValue);
    }

    /**
     * Gets the value text width.
     *
     * @param g
     *            the g
     * @return the value text width
     */
    private int getValueTextWidth(final Graphics2D g) {
        return SwingUtilities.computeStringWidth(g.getFontMetrics(),
                this.getCaption());
    }

    /**
     * Gets the text graphics.
     *
     * @return the text graphics
     */
    private Graphics2D getTextGraphics() {
        final Graphics2D g = this.createGraphics();
        g.setFont(new Font("SansSerif", Font.BOLD, this.getFontSize()));
        // g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
        // RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setPaintMode();
        // g.setXORMode(Color.BLACK);
        return g;
    }

    /**
     * Gets the font size.
     *
     * @return the font size
     */
    private int getFontSize() {
        return (int) Math.round(this.getHeight() * 0.91);
    }

    /**
     * Gets the text color.
     *
     * @param bgColor
     *            the bg color
     * @return the text color
     */
    private Color getTextColor(final Color bgColor) {
        Color color = null;
        if (bgColor.equals(Color.RED) || bgColor.equals(Color.BLUE)) {
            color = Color.WHITE;
        } else {
            color = Color.BLACK;
        }
        return color;
    }

    /**
     * Draw bar.
     *
     * @param g
     *            the g
     * @param x
     *            the x
     * @param width
     *            the width
     * @param height
     *            the height
     * @param color
     *            the color
     */
    private void drawBar(final Graphics2D g, final int x, final int width,
            final int height, final Color color) {
        this.drawRectangle(g, x, 0, width, height, color);
    }

    /**
     * Draw rectangle.
     *
     * @param g
     *            the g
     * @param x
     *            the x
     * @param y
     *            the y
     * @param width
     *            the width
     * @param height
     *            the height
     * @param color
     *            the color
     */
    private void drawRectangle(final Graphics2D g, final int x, final int y,
            final int width, final int height, final Color color) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }

    /**
     * Gets the pixels for value.
     *
     * @return the pixels for value
     */
    private int getPixelsForValue() {
        return (int) Math.round(this.getWidth() / this.maxValue * this.value);
    }

    /**
     * Encode jpeg.
     *
     * @param outputStream
     *            the output stream
     * @param outputQuality
     *            the output quality
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
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
