package com.technoetic.xplanner.tags;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import de.laures.cewolf.CewolfException;
import de.laures.cewolf.ChartImage;
import de.laures.cewolf.WebConstants;
import de.laures.cewolf.util.RenderingException;

/**
 * Created by IntelliJ IDEA. User: tkmower Date: Jun 11, 2004 Time: 4:50:56 PM
 */
public class CewolfProgressBarChartImage implements ChartImage {
	private final ProgressBarImage image;
	private byte[] bytes;

	public CewolfProgressBarChartImage(final ProgressBarImage image) {
		this.image = image;
	}

	@Override
	public int getWidth() {
		return this.image.getWidth();
	}

	@Override
	public int getHeight() {
		return this.image.getHeight();
	}

	@Override
	public int getType() {
		return ChartImage.IMG_TYPE_CHART;
	}

	@Override
	public byte[] getBytes() throws CewolfException {
		if (this.bytes == null) {
			this.bytes = this.render();
		}
		return this.bytes;
	}

	private byte[] render() throws RenderingException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final BufferedOutputStream bos = new BufferedOutputStream(baos);
		try {
			this.image.encodeJPEG(bos, 0.75f);
		} catch (final IOException e) {
			throw new RenderingException(e);
		}
		return baos.toByteArray();
	}

	@Override
	public String getMimeType() {
		return WebConstants.MIME_PNG;
	}

	@Override
	public int getSize() throws CewolfException {
		return this.getBytes().length;
	}

	@Override
	public Date getTimeoutTime() {
		final Calendar cal = new GregorianCalendar();
		cal.add(Calendar.SECOND, 30);
		return cal.getTime();
	}
}
