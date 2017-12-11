package com.ubsoft.framework.bi.util;

import java.awt.print.PrinterJob;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.Map;

import javax.print.PrintService;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.base.JRBaseReport;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.JRXmlExporter;

import com.ubsoft.framework.core.support.util.StringUtil;

public class JasperUtil {
	public static final String PRINT_TYPE = "print";
	public static final String PDF_TYPE = "pdf";
	public static final String EXCEL_TYPE = "excel";
	public static final String HTML_TYPE = "html";
	public static final String WORD_TYPE = "word";

	/*
	 * 如果导出的是excel， 则需要去掉周围的margin
	 */
	private static void prepareReport(JasperReport jasperReport) {
		try {
			Field margin = JRBaseReport.class.getDeclaredField("leftMargin");
			margin.setAccessible(true);
			margin.setInt(jasperReport, 0);
			margin = JRBaseReport.class.getDeclaredField("topMargin");
			margin.setAccessible(true);
			margin.setInt(jasperReport, 0);
			margin = JRBaseReport.class.getDeclaredField("bottomMargin");
			margin.setAccessible(true);
			margin.setInt(jasperReport, 0);
			Field pageHeight = JRBaseReport.class.getDeclaredField("pageHeight");
			pageHeight.setAccessible(true);
			pageHeight.setInt(jasperReport, 2147483647);
		} catch (Exception exception) {
		}
	}

	/**
	 * 
	 * @Title: exportExcel
	 * @Description: 导出excel
	 * @author chenkf
	 * @date 2017-2-27 上午10:01:31
	 * @param reportfile
	 * @param exportFileName
	 * @param request
	 * @param response
	 * @param parameters
	 * @param bean
	 * @throws IOException
	 * @throws JRException
	 */
	public static void exportExcel(String reportfile, String exportFileName, HttpServletRequest request,
			HttpServletResponse response, Map parameters, JRDataSource bean) throws IOException, JRException {
		response.setContentType("application/vnd.ms-excel");
		// JasperReport jasperReport = (JasperReport) JRLoader.load
		// prepareReport(jasperReport);

		// JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
		// parameters, conn);
		JasperPrint jasperPrint = JasperFillManager.fillReport(reportfile, parameters, bean);
		if (StringUtil.isEmpty(exportFileName)) {
			exportFileName = "export.xls";
		}
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ URLEncoder.encode(exportFileName, "UTF-8") + "\"");
		ServletOutputStream ouputStream = response.getOutputStream();
		JRXlsExporter exporter = new JRXlsExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);

		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, ouputStream);
		exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE); // 删除记录最下面的空行

		exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);// 删除多余的ColumnHeader

		exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);// 显示边框
		exporter.exportReport();

		ouputStream.flush();
		ouputStream.close();
	}

	public static void exportExcel(String reportfile, String exportFileName, HttpServletRequest request,
			HttpServletResponse response, Map parameters, Connection conn) throws IOException, JRException {
		response.setContentType("application/vnd.ms-excel");
		// JasperReport jasperReport = (JasperReport) JRLoader.load
		// prepareReport(jasperReport);

		// JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
		// parameters, conn);
		JasperPrint jasperPrint = JasperFillManager.fillReport(reportfile, parameters, conn);
		if (StringUtil.isEmpty(exportFileName)) {
			exportFileName = "export.xls";
		}
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ URLEncoder.encode(exportFileName, "UTF-8") + "\"");
		ServletOutputStream ouputStream = response.getOutputStream();
		JRXlsExporter exporter = new JRXlsExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);

		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, ouputStream);
		exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE); // 删除记录最下面的空行

		exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);// 删除多余的ColumnHeader

		exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);// 显示边框
		exporter.exportReport();

		ouputStream.flush();
		ouputStream.close();
	}

	public static enum DocType {
		PDF, HTML, XLS, XML, RTF
	}

	public static JRAbstractExporter getJRExporter(DocType docType) {
		JRAbstractExporter exporter = null;
		switch (docType) {
		case PDF:
			exporter = new JRPdfExporter();
			break;
		case HTML:
			exporter = new JRHtmlExporter();
			break;
		case XLS:
			exporter = new JExcelApiExporter();
			break;
		case XML:
			exporter = new JRXmlExporter();
			break;
		case RTF:
			exporter = new JRRtfExporter();
			break;
		}
		return exporter;
	}

	/**
	 * 
	 * @Title: exportPdf
	 * @Description: 导出PDF
	 * @author chenkf
	 * @date 2017-2-27 上午09:39:06
	 * @param reportfile
	 * @param exportFileName
	 * @param request
	 * @param response
	 * @param parameters
	 * @param bean
	 * @throws IOException
	 * @throws JRException
	 */
	public static void exportPdf(String reportfile, String exportFileName, HttpServletRequest request,
			HttpServletResponse response, Map parameters, JRDataSource bean) throws IOException, JRException {
		response.setContentType("application/pdf");
		JasperPrint jasperPrint = JasperFillManager.fillReport(reportfile, parameters, bean);
		if (StringUtil.isEmpty(exportFileName)) {
			exportFileName = "export.pdf";
		}
		response.setHeader("Content-disposition", "attachment; filename=" + exportFileName);
		ServletOutputStream ouputStream = response.getOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrint, ouputStream);
		ouputStream.flush();
		ouputStream.close();
	}

	/**
	 * 
	 * @Title: exportPdf
	 * @Description: 导出PDF
	 * @author chenkf
	 * @date 2017-2-27 上午09:39:06
	 * @param reportfile
	 * @param exportFileName
	 * @param request
	 * @param response
	 * @param parameters
	 * @param conn
	 * @throws IOException
	 * @throws JRException
	 */
	public static void exportPdf(String reportfile, String exportFileName, HttpServletRequest request,
			HttpServletResponse response, Map parameters, Connection conn) throws IOException, JRException {
		response.setContentType("application/pdf");
		JasperPrint jasperPrint = JasperFillManager.fillReport(reportfile, parameters, conn);
		if (StringUtil.isEmpty(exportFileName)) {
			exportFileName = "export.pdf";
		}
		response.setHeader("Content-disposition", "attachment; filename=" + exportFileName);
		ServletOutputStream ouputStream = response.getOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrint, ouputStream);
		ouputStream.flush();
		ouputStream.close();
	}

	/**
	 * 
	 * @Title: exportHtml
	 * @Description: 导出HTML
	 * @author chenkf
	 * @date 2017-2-27 上午09:36:29
	 * @param reportfile
	 * @param exportFileName
	 * @param request
	 * @param response
	 * @param parameters
	 * @param bean
	 * @throws IOException
	 * @throws JRException
	 */
	public static void exportHtml(String reportfile, String exportFileName, HttpServletRequest request,
			HttpServletResponse response, Map parameters, JRDataSource bean) throws IOException, JRException {
		response.setContentType("text/html");
		ServletOutputStream ouputStream = response.getOutputStream();
		JasperPrint jasperPrint = JasperFillManager.fillReport(reportfile, parameters, bean);
		JRHtmlExporter exporter = new JRHtmlExporter();
		exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, Boolean.FALSE);
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, ouputStream);
		exporter.exportReport();
		ouputStream.flush();
		ouputStream.close();
	}

	/**
	 * 
	 * @Title: exportHtml
	 * @Description: 导出HTML
	 * @author chenkf
	 * @date 2017-2-27 上午09:37:29
	 * @param reportfile
	 * @param exportFileName
	 * @param request
	 * @param response
	 * @param parameters
	 * @param conn
	 * @throws IOException
	 * @throws JRException
	 */
	public static void exportHtml(String reportfile, String exportFileName, HttpServletRequest request,
			HttpServletResponse response, Map parameters, Connection conn) throws IOException, JRException {
		response.setContentType("text/html");
		ServletOutputStream ouputStream = response.getOutputStream();
		JasperPrint jasperPrint = JasperFillManager.fillReport(reportfile, parameters, conn);
		JRHtmlExporter exporter = new JRHtmlExporter();
		exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, Boolean.FALSE);
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, ouputStream);
		exporter.exportReport();
		ouputStream.flush();
		ouputStream.close();
	}

	/**
	 * 
	 * @Title: exportWord
	 * @Description: 导出word
	 * @author chenkf
	 * @date 2017-2-27 上午09:34:13
	 * @param reportfile
	 * @param exportFileName
	 * @param request
	 * @param response
	 * @param parameters
	 * @param bean
	 * @throws JRException
	 * @throws IOException
	 */
	public static void exportWord(String reportfile, String exportFileName, HttpServletRequest request,
			HttpServletResponse response, Map parameters, JRDataSource bean) throws JRException, IOException {
		response.setContentType("application/msword;charset=utf-8");
		String defaultname = null;
		JasperPrint jasperPrint = JasperFillManager.fillReport(reportfile, parameters, bean);
		if (StringUtil.isEmpty(exportFileName)) {
			exportFileName = "export.doc";
		}
		response.setHeader("Content-disposition", "attachment; filename=" + exportFileName);
		JRExporter exporter = new JRRtfExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
		exporter.exportReport();

	}

	/**
	 * 
	 * @Title: exportWord
	 * @Description: 导出word
	 * @author chenkf
	 * @date 2017-2-27 上午09:34:53
	 * @param reportfile
	 * @param exportFileName
	 * @param request
	 * @param response
	 * @param parameters
	 * @param bean
	 * @throws JRException
	 * @throws IOException
	 */
	public static void exportWord(String reportfile, String exportFileName, HttpServletRequest request,
			HttpServletResponse response, Map parameters, Connection conn) throws JRException, IOException {
		response.setContentType("application/msword;charset=utf-8");
		String defaultname = null;
		JasperPrint jasperPrint = JasperFillManager.fillReport(reportfile, parameters, conn);
		if (StringUtil.isEmpty(exportFileName)) {
			exportFileName = "export.doc";
		}
		response.setHeader("Content-disposition", "attachment; filename=" + exportFileName);
		JRExporter exporter = new JRRtfExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
		exporter.exportReport();
	}

	/**
	 * 
	 * @Title: showHtml
	 * @Description: 显示成html格式，需要传入图片路径
	 * @author chenkf
	 * @date 2017-2-27 上午09:25:05
	 * @param reportfile
	 * @param imgDir
	 * @param imgUrl
	 * @param request
	 * @param response
	 * @param parameters
	 * @param conn
	 * @throws JRException
	 * @throws IOException
	 */
	public static void showHtml(String reportfile, String imgDir, String imgUrl, HttpServletRequest request,
			HttpServletResponse response, Map parameters, JRDataSource conn) throws JRException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		JRAbstractExporter exporter = getJRExporter(DocType.HTML);
		JasperPrint jasperPrint = JasperFillManager.fillReport(reportfile, parameters, conn);
		PrintWriter out = response.getWriter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_WRITER, out);
		// 图片目录的绝对路径
		exporter.setParameter(JRHtmlExporterParameter.IMAGES_DIR_NAME, imgDir);
		// 通过Web访问时图片的URI
		exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, imgUrl);
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
		exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, true);
		// 是否输出图片到目录
		exporter.setParameter(JRHtmlExporterParameter.IS_OUTPUT_IMAGES_TO_DIR, true);
		exporter.exportReport();
		out.flush();

	}

	/**
	 * 
	 * @Title: showHtml
	 * @Description: TODO
	 * @author chenkf
	 * @date 2017-2-27 上午09:25:45
	 * @param reportfile
	 * @param imgDir
	 * @param imgUrl
	 * @param request
	 * @param response
	 * @param parameters
	 * @param conn
	 * @throws JRException
	 * @throws IOException
	 */
	public static void showHtml(String reportfile, String imgDir, String imgUrl, HttpServletRequest request,
			HttpServletResponse response, Map parameters, Connection conn) throws JRException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		JRAbstractExporter exporter = getJRExporter(DocType.HTML);
		JasperPrint jasperPrint = JasperFillManager.fillReport(reportfile, parameters, conn);
		PrintWriter out = response.getWriter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_WRITER, out);
		// 图片目录的绝对路径
		exporter.setParameter(JRHtmlExporterParameter.IMAGES_DIR_NAME, imgDir);
		// 通过Web访问时图片的URI
		exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, imgUrl);
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
		exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, true);
		// 是否输出图片到目录
		exporter.setParameter(JRHtmlExporterParameter.IS_OUTPUT_IMAGES_TO_DIR, true);
		
		exporter.exportReport();
		out.flush();

	}

	/**
	 * 
	 * @Title: showPdf
	 * @Description: 界面上显示PDF
	 * @author chenkf
	 * @date 2017-2-27 上午09:17:49
	 * @param reportfile
	 * @param request
	 * @param response
	 * @param parameters
	 * @param conn
	 * @throws JRException
	 * @throws IOException
	 */
	public static void showPdf(String reportfile, HttpServletRequest request, HttpServletResponse response,
			Map parameters, Connection conn) throws JRException, IOException {
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		response.setContentType("application/pdf");
		JasperPrint jasperPrint = JasperFillManager.fillReport(reportfile, parameters, conn);
		JRAbstractExporter exporter = getJRExporter(DocType.PDF);
		PrintWriter out = response.getWriter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_WRITER, out);
		exporter.exportReport();
		out.flush();
	}

	/**
	 * 
	 * @Title: showPdf
	 * @Description: 界面上显示PDF传递javaBean
	 * @author chenkf
	 * @date 2017-2-27 上午09:20:21
	 * @param reportfile
	 * @param request
	 * @param response
	 * @param parameters
	 * @param bean
	 * @throws JRException
	 * @throws IOException
	 */
	public static void showPdf(String reportfile, HttpServletRequest request, HttpServletResponse response,
			Map parameters, JRDataSource bean) throws JRException, IOException {
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		response.setContentType("application/pdf");
		JasperPrint jasperPrint = JasperFillManager.fillReport(reportfile, parameters, bean);
		JRAbstractExporter exporter = getJRExporter(DocType.PDF);
		PrintWriter out = response.getWriter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_WRITER, out);
		exporter.exportReport();
		out.flush();
	}

	/**
	 * 
	 * @Title: print
	 * @Description: 打印
	 * @author chenkf
	 * @date 2017-2-27 上午10:05:45
	 * @param reportfile
	 * @param printerName
	 * @param request
	 * @param response
	 * @param parameters
	 * @param bean
	 * @throws JRException
	 * @throws IOException
	 */
	public static void print(String reportfile, String printerName, HttpServletRequest request,
			HttpServletResponse response, Map parameters, JRDataSource bean) throws JRException, IOException {

		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");

		PrintService[] pss = PrinterJob.lookupPrintServices();
		PrintService printService = null;
		for (PrintService prs : pss) {
			if (printerName.equals(prs.getName())) {
				printService = prs;
				break;
			}

		}
		JasperPrint jasperPrint = JasperFillManager.fillReport(reportfile, parameters, bean);
		JRAbstractExporter je = new JRPrintServiceExporter();
		je.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		je.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, printService);
		je.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, false);
		je.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, false);
		je.exportReport();

	}

	/**
	 * 
	 * @Title: print
	 * @Description: 打印
	 * @author chenkf
	 * @date 2017-2-27 上午10:06:01
	 * @param reportfile
	 * @param printerName
	 * @param request
	 * @param response
	 * @param parameters
	 * @param bean
	 * @throws JRException
	 * @throws IOException
	 */
	public static void print(String reportfile, String printerName, HttpServletRequest request,
			HttpServletResponse response, Map parameters, Connection conn) throws JRException, IOException {

		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");

		PrintService[] pss = PrinterJob.lookupPrintServices();
		PrintService printService = null;
		for (PrintService prs : pss) {
			if (printerName.equals(prs.getName())) {
				printService = prs;
				break;
			}

		}
		JasperPrint jasperPrint = JasperFillManager.fillReport(reportfile, parameters, conn);
		JRAbstractExporter je = new JRPrintServiceExporter();
		je.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		je.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, printService);
		je.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, false);
		je.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, false);
		je.exportReport();

	}

}