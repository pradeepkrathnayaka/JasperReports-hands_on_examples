package com.rmpksoft.jrlesson.helloworld;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRStaticText;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignImage;
import net.sf.jasperreports.engine.design.JRDesignParameter;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.HtmlResourceHandler;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRExportProgressMonitor;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.fill.FillListener;
import net.sf.jasperreports.engine.fill.JRSwapFileVirtualizer;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.WhenNoDataTypeEnum;
import net.sf.jasperreports.engine.util.JRElementsVisitor;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.engine.util.JRSwapFile;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.CsvReportConfiguration;
import net.sf.jasperreports.export.HtmlReportConfiguration;
import net.sf.jasperreports.export.PdfReportConfiguration;
import net.sf.jasperreports.export.SimpleCsvReportConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleHtmlReportConfiguration;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;
import net.sf.jasperreports.export.SimpleReportExportConfiguration;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import net.sf.jasperreports.web.servlets.AsyncJasperPrintAccessor;
import net.sf.jasperreports.web.servlets.ReportExecutionStatus;
public class SimpleReport {
	
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
	
	public JasperDesign getDesignFile(final File sourcePath, String reportName) throws Throwable {
		String name = reportName;
		if (!name.contains(".jrxml")) {
			name = name + ".jrxml";
		}
		File sourceReport = new File(sourcePath.getAbsoluteFile() + "/" + name);
		return JRXmlLoader.load(sourceReport);

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
		//JRElementsVisitor.visitReport(jasperReport, createVisitor(sourcePath, outputPath));

		/*if (subReportException != null)
			throw new RuntimeException(subReportException);*/
		return jasperReport;
	}

	
	public void execute() throws Exception {
		
		Map<String, Object> parameters;
		
	    try {
	        InputStream relatorioStream = this.getClass().getResourceAsStream("reports/Blank_A4.jrxml");
	        JasperPrint print = JasperFillManager.fillReport(relatorioStream, null, new JREmptyDataSource());
	        boolean b = print.getPages().size() > 0;
	        if (b) {
	        	JRPdfExporter exportador = new JRPdfExporter();
	        	
	        }
	    } catch (Exception e) {
	        throw new Exception("Erro ao executar relatÃ³rio ", e);
	    }
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

	public JasperReport reportFile() throws JRException, Exception {
		JasperReport jr = null;

		ClassLoader classLoader = getClass().getClassLoader();
		URL jasperFilePath = classLoader.getResource("reports/Blank_A4.jasper");
		System.out.println(jasperFilePath);
		if (jasperFilePath != null) {
			File f = new File(jasperFilePath.getFile());

			if (f != null || f.exists()) {
				jr = (JasperReport) JRLoader.loadObject(f);
				System.out.println(f.getAbsolutePath());
			}
		} else {
			jr = JasperCompileManager.compileReport("src/main/resources/reports/Blank_A4.jrxml");
			JRSaver.saveObject(jr, "src/main/resources/reports/Blank_A4.jasper");
			System.out.println("created");
		}

		return jr;
	}

	public void generatePDF(JasperReport jasperReport) throws Exception {
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			 InputStream imageStream = getClass().getClassLoader().getResourceAsStream("/reports/logo.jpg");
			    parameters.put("REPORT_IMAGE", imageStream);
			    parameters.put("REPORT_TITLE", "Report");
			    
			    
			
			JasperPrint p = JasperFillManager.fillReport(jasperReport, null, new JREmptyDataSource());
			JRPdfExporter exporter = new JRPdfExporter();
			SimpleOutputStreamExporterOutput c = new SimpleOutputStreamExporterOutput(
					new FileOutputStream("src/main/resources/reports/Blank_A4.pdf"));
			exporter.setExporterInput(new SimpleExporterInput(p));
			exporter.setExporterOutput(c);
			exporter.exportReport();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}

	}

	public JasperReport reportStream() throws JRException, Exception {
		JasperReport jr = null;

		ClassLoader classLoader = getClass().getClassLoader();
		URL jasperFilePath = classLoader.getResource("reports/Blank_A4.jasper");

		System.out.println(jasperFilePath);
		if (jasperFilePath != null) {
			File f = new File(jasperFilePath.getFile());

			if (f != null || f.exists()) {
				jr = (JasperReport) JRLoader.loadObject(f);
				System.out.println(f.getAbsolutePath());
			}
		} else {
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream("reports/Blank_A4.jrxml");
			jr = JasperCompileManager.compileReport(inputStream);
			JRSaver.saveObject(jr, "src/main/resources/reports/Blank_A4.jasper");
			System.out.println("created");
		}

		return jr;
	}

	public static void main(String[] args) throws JRException {

		try {
			SimpleReport a = new SimpleReport();
			JasperReport report = a.reportStream();
			a.generatePDF(report);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
