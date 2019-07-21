package com.rmpksoft.jrlesson.simple;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

public class SimpleReport {
	
	private final ClassLoader classLoader = getClass().getClassLoader();

	public SimpleReport() {
		System.out.println("Start SimpleReport");
	}

	public JasperReport compileReportIfNeeded(JasperDesign jasperDesign, final File sourcePath, final File outputPath,
			String reportName) throws Throwable {
		JasperReport report;
		if (checkIfReportNeedsCompile(sourcePath, outputPath, reportName)) {
			report = compileReport(jasperDesign, sourcePath, outputPath, reportName);
		} else {
			File outputReport = new File(outputPath.getAbsolutePath() + "/" + reportName + ".jasper");

			System.out.println("Report " + outputReport.getName() + " is up to date");
			report = (JasperReport) JRLoader.loadObject(outputReport);

		}
		return report;
	}

	public boolean checkIfReportNeedsCompile(final File sourcePath, final File outputPath, String reportName) {
		File sourceReport = new File(sourcePath.getAbsoluteFile() + "/" + reportName + ".jrxml");
		File outputReport = new File(outputPath.getAbsolutePath() + "/" + reportName + ".jasper");
		return !outputReport.exists() || sourceReport.lastModified() > outputReport.lastModified();
	}

	public JasperReport compileReport(JasperDesign jasperDesign, final File sourcePath, final File outputPath,
			String reportName) throws Throwable {
		JasperReport jasperReport = null;
		File outputReport = new File(outputPath.getAbsolutePath() + "/" + reportName + ".jasper");
		jasperReport = JasperCompileManager.compileReport(jasperDesign);
		JRSaver.saveObject(jasperReport, outputReport);
		System.out.println("Saving compiled report : " + outputReport.getAbsolutePath());

		// Compile sub reports
		// JRElementsVisitor.visitReport(jasperReport, createVisitor(sourcePath,
		// outputPath));

		/*
		 * if (subReportException != null) throw new
		 * RuntimeException(subReportException);
		 */
		return jasperReport;
	}

	public JasperDesign getJasperDesignFile(final File sourcePath, String reportName) throws Throwable {
		String name = reportName;
		if (!name.contains(".jrxml")) {
			name = name + ".jrxml";
		}
		File sourceReport = new File(sourcePath.getAbsoluteFile() + "/" + name);
		return JRXmlLoader.load(sourceReport);

	}

	private File getFile(String fileName) {
		ClassLoader classLoader = getClass().getClassLoader();
		URL resource = classLoader.getResource(fileName);
		if (resource == null) {
			throw new IllegalArgumentException("File cannot be found!");
		} else {
			return new File(resource.getFile());
		}

	}

	public void generatePDF(JasperReport jasperReport) throws Exception {
		try {
			
			// Read report logo
			InputStream imageStream =classLoader.getResourceAsStream("com/rmpksoft/jrintro/simple/broccoli.jpg");
						
			// Parameters
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			// Read image from the location
			BufferedImage image = ImageIO.read(getClass().getResource("/com/rmpksoft/jrintro/simple/broccoli.jpg"));
			parameters.put("logo", image );
			
			//parameters.put("REPORT_Logo", imageStream);
			parameters.put("REPORT_TITLE", "Report");
			
			// Resource bundle
			ResourceBundle resourceBundle = ResourceBundle.getBundle("com.rmpksoft.jrintro.simple.JasperReportResources");
			parameters.put(JRParameter.REPORT_RESOURCE_BUNDLE, resourceBundle);
			parameters.put(JRParameter.REPORT_LOCALE, new Locale("fr","BE"));

			// filling report
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
			
			// exporting report as a pdf
			JRPdfExporter exporter = new JRPdfExporter();
			SimpleOutputStreamExporterOutput outputReport = new SimpleOutputStreamExporterOutput(
					new FileOutputStream("src/main/java/com/rmpksoft/jrintro/simple/SimpleReport.pdf"));
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(outputReport);
			exporter.exportReport();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}

	}

	public JasperReport reportAsStream() throws JRException, Exception {
		JasperReport jr = null;

		URL jasperFilePath = classLoader.getResource("src/main/java/com/rmpksoft/jrintro/simple/SimpleReport.jasper");
		System.out.println(jasperFilePath);
		if (jasperFilePath != null) {
			File f = new File(jasperFilePath.getFile());

			if (f != null || f.exists()) {
				jr = (JasperReport) JRLoader.loadObject(f);
				System.out.println(f.getAbsolutePath());
			}
		} else {
			InputStream inputStream = classLoader.getResourceAsStream("com/rmpksoft/jrintro/simple/SimpleReport.jrxml");
			jr = JasperCompileManager.compileReport(inputStream);
			
			JRSaver.saveObject(jr, "src/main/java/com/rmpksoft/jrintro/simple/SimpleReport.jasper");
			System.out.println("created");
		}

		return jr;
	}

	public static void main(String[] args) {
		try {
			SimpleReport sr = new SimpleReport();
			JasperReport report = sr.reportAsStream();
			sr.generatePDF(report);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
