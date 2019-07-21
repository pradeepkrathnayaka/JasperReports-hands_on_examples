package com.rmpksoft.jrlesson.stream;

import java.util.Locale;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReportsContext;

public class PDFReportException extends JRException {

	public PDFReportException(String messageKey, Object[] args, Throwable t) {
		super(messageKey, args, t);
	}

	public PDFReportException(String messageKey, Object[] args) {
		super(messageKey, args);
	}

	public PDFReportException(String message, Throwable t) {
		super(message, t);
	}

	public PDFReportException(Throwable t) {
		super(t);
	}

	public PDFReportException(String message) {
		super(message);
	}

	private static final long serialVersionUID = 1L;
	

}
