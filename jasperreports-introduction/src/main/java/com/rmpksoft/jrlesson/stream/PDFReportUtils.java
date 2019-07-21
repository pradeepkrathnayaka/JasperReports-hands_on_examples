package com.rmpksoft.jrlesson.stream;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

import javax.print.PrintService;
import javax.print.attribute.PrintRequestAttributeSet;

import org.apache.commons.lang3.StringUtils;

import com.rmpksoft.jrlesson.helloworld.ReportUtils;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.engine.fill.JRSubreportRunnerFactory;
import net.sf.jasperreports.engine.util.JRLoader;

public class PDFReportUtils {

	

	static final private Map<String, JasperReport> reportsMap = new ConcurrentHashMap<String, JasperReport>();
	
	
	public static void savePDFReport(JasperPrint jasperPrint,
		      Map<Object, Object> exportParameters, File target) throws JRException {
		
		JasperExportManager.exportReportToPdfFile(jasperPrint, target.getAbsolutePath());
		
	}
	
	public static void savePDFReportToOutputStream(JasperPrint jasperPrint,
		      Map<Object, Object> exportParameters, OutputStream outputStream) throws JRException {
		
	}

	static public boolean exportToPdfFile(String key, Map parameters, ResourceBundle bundle, Collection<?> dataSource,
			String destination) {
		try {
			final JasperPrint jasperPrint = createJasperPrint(key, parameters, bundle, dataSource);
			if (jasperPrint != null) {
				JasperExportManager.exportReportToPdfFile(jasperPrint, destination);
				return true;
			} else {
				return false;
			}
		} catch (JRException e) {
			return false;
		}
	}

	static public byte[] exportToPdfFileAsByteArray(final String key, final Map parameters, final ResourceBundle bundle,
			final Collection dataSource) throws JRException {
		final JasperPrint jasperPrint = createJasperPrint(key, parameters, bundle, dataSource);

		if (jasperPrint != null) {
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			export(new JRPdfExporter(), Collections.singletonList(jasperPrint), baos, (PrintService) null,
					(PrintRequestAttributeSet) null);
			return baos.toByteArray();
		}

		return null;
	}

	static private JasperPrint createJasperPrint(final String key, final Map parameters, final ResourceBundle bundle,
			Collection dataSource) throws JRException {
		JasperReport report = reportsMap.get(key);

		if (report == null) {
			report = (JasperReport) JRLoader.loadObject(ReportUtils.class.getResourceAsStream(key));
			reportsMap.put(key, report);
		}

		if (report != null) {
			if (parameters != null && bundle != null) {
				parameters.put(JRParameter.REPORT_RESOURCE_BUNDLE, bundle);
			}

			if (dataSource == null || dataSource.isEmpty()) {
				// dummy, engine seems to work not very well with empty data
				// sources
				dataSource = Collections.singletonList(StringUtils.EMPTY);
			}

			return null;
		}

		return null;
	}

	static private void export(final JRAbstractExporter<?,?,?,?> exporter, final List<JasperPrint> prints,
			final ByteArrayOutputStream stream, final PrintService printService,
			final PrintRequestAttributeSet printRequestAttributeSet) throws JRException {
		
		 Map<Object, Object> exportParameters = new HashMap<Object, Object>();
		// parameters.putAll(expType.getExportParameters());
		    
		if (prints.size() == 1) {
			//exporter.setParameter(JRExporterParameter.JASPER_PRINT, prints.iterator().next());
		} else {
			//exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, prints);
		}

		if (stream != null) {
			//exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, stream);
		}

		if (printService != null) {
			//exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, printService);
		}

		if (printRequestAttributeSet != null) {
			/*exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET,
					printRequestAttributeSet);*/
		}

		exporter.exportReport();
		return;
	}

}
