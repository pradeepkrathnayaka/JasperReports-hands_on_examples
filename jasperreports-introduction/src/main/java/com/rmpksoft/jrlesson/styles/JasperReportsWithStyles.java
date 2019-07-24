package com.rmpksoft.jrlesson.styles;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

public class JasperReportsWithStyles {
	
	private final ClassLoader classLoader = getClass().getClassLoader();
	
	public JasperReport reportAsStream() throws JRException, Exception {
		JasperReport jr = null;

		URL jasperFilePath = classLoader.getResource("src/main/java/com/rmpksoft/jrlesson/styles/report_styles.jasper");
		System.out.println(jasperFilePath);
		if (jasperFilePath != null) {
			File f = new File(jasperFilePath.getFile());

			if (f != null || f.exists()) {
				jr = (JasperReport) JRLoader.loadObject(f);
				System.out.println(f.getAbsolutePath());
			}
		} else {
			InputStream inputStream = classLoader.getResourceAsStream("com/rmpksoft/jrlesson/styles/report_styles.jrxml");
			jr = JasperCompileManager.compileReport(inputStream);
			
			JRSaver.saveObject(jr, "src/main/java/com/rmpksoft/jrlesson/styles/report_styles.jasper");
			System.out.println("created");
		}

		return jr;
	}
	
	public void generatePDF(JasperReport jasperReport) throws Exception {
		try {
			// Parameters
			Map<String, Object> parameters = new HashMap<String, Object>();
			
		
			//parameters.put("REPORT_Logo", imageStream);
			parameters.put("REPORT_TITLE", "Report");
			
			List<User> list = new ArrayList<>();
			list.add(new User("Id1","name1","Gender1"));
			list.add(new User("Id2","name2","Gender2"));
			list.add(new User("Id3","name3","Gender3"));
			list.add(new User("Id4","name4","Gender4"));
			list.add(new User("Id5","name5","Gender5"));
			
			JRBeanCollectionDataSource jr = new JRBeanCollectionDataSource(list);
			
			parameters.put("dataList", jr);
			
			// Resource bundle
		/*	ResourceBundle resourceBundle = ResourceBundle.getBundle("com.rmpksoft.jrintro.simple.JasperReportResources");
			parameters.put(JRParameter.REPORT_RESOURCE_BUNDLE, resourceBundle);
			parameters.put(JRParameter.REPORT_LOCALE, new Locale("fr","BE"));
*/
			// filling report
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
			
			// exporting report as a pdf
			JRPdfExporter exporter = new JRPdfExporter();
			SimpleOutputStreamExporterOutput outputReport = new SimpleOutputStreamExporterOutput(
					new FileOutputStream("src/main/java/com/rmpksoft/jrlesson/styles/report_style.pdf"));
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(outputReport);
			exporter.exportReport();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	public static void main(String[] args) {
		try {
			JasperReportsWithStyles sr = new JasperReportsWithStyles();
			JasperReport report = sr.reportAsStream();
			sr.generatePDF(report);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
