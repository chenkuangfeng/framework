package com.ubsoft.framework.core.support.file;

import com.ubsoft.framework.core.exception.ComException;
import com.ubsoft.framework.core.support.util.StringUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExcelUtil {
    /**
     * 根据列和数据，生成数据流
     *
     * @param em
     * @param out
     */
    public static boolean exportExcel(ExcelModel em, OutputStream out){
        List<Map> listData = em.getData();
        List<ExcelColumn> columns = em.getColumns();
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        Sheet sheet = workbook.createSheet(em.getSheetName());
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth((short) 15);
        createHeader(workbook, sheet, columns);

        createRow(workbook, sheet, columns, listData);
        try {
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ComException(0, "生成EXCEL异常：" + e.getMessage());
        }
        return true;
    }

    public static boolean exportExcel(ExcelModel em){
        List<Map> listData = em.getData();
        List<ExcelColumn> columns = em.getColumns();
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        Sheet sheet = workbook.createSheet(em.getSheetName());
        // 设置表格默认列宽度为15个字节
        //sheet.setDefaultColumnWidth((short) 15);
        for (int i = 0; i < columns.size(); i++) {
            ExcelColumn col = columns.get(i);
            //
            if (col.getWidth() > 0) {
                sheet.setColumnWidth(i, col.getWidth() * 256 / 7);
            } else {
                sheet.autoSizeColumn(i, true);
            }

        }
        createHeader(workbook, sheet, columns);

        createRow(workbook, sheet, columns, listData);

        try {
            // 新建文件输出流
            FileOutputStream fOut = new FileOutputStream(em.getPath());
            // 将数据写入Excel
            workbook.write(fOut);
            fOut.flush();
            fOut.close();
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            throw new ComException(0, "生成EXCEL异常：" + e.getMessage());
        }
    }

    public static void createHeader(SXSSFWorkbook workbook, Sheet sheet, List<ExcelColumn> columns){
        // 生成一个样式
        CellStyle headerStyle = workbook.createCellStyle();
        // 设置头部样式
        headerStyle.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
        headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(CellStyle.BORDER_THIN);
        headerStyle.setBorderLeft(CellStyle.BORDER_THIN);
        headerStyle.setBorderRight(CellStyle.BORDER_THIN);
        headerStyle.setBorderTop(CellStyle.BORDER_THIN);
        headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
        // 生成一个字体
        Font font = workbook.createFont();
        font.setColor(HSSFColor.WHITE.index);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        headerStyle.setFont(font);
        Row headerRow = sheet.createRow(0);
        int i = 0;
        for (ExcelColumn col : columns) {
            // sheet.setColumnWidth(i, col.getWidth());
            Cell cell = headerRow.createCell(i);

            cell.setCellStyle(headerStyle);

            // org.apache.poi.ss.usermodel.Comment
            // com=patriarch.createCellComment(new ClientAnchor(0, 0, 0, 0,
            // (short) 4, 2, (short) 6, 5);
            // cell.setCellComment(arg0)
            cell.setCellValue(col.getTitle());
            i++;
        }
    }

    private static void createCell(Cell cell, Object value, SXSSFWorkbook workbook){
        CellStyle dateCellStyle = getDateCellStyle(workbook);
        CellStyle numCellStyle = getNumberCellStyle(workbook);
        CellStyle stringCellStyle = getStringCellStyle(workbook);

        if (value != null) {
            if (value instanceof Date || value instanceof Timestamp) {
                Date date = (Date) value;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String text = sdf.format(date);
                cell.setCellStyle(dateCellStyle);

                cell.setCellValue(text);
            } else if (value instanceof Number) {
                cell.setCellValue(Double.parseDouble(value.toString()));
                cell.setCellStyle(numCellStyle);

                // 定义单元格为字符串类型
                cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
                cell.setCellValue(Double.parseDouble(value + ""));
            }
            // else if (value instanceof byte[]) {
            // // 有图片时，设置行高为60px;
            // row.setHeightInPoints(60);
            // // 设置图片所在列宽度为80px,注意这里单位的一个换算
            // sheet.setColumnWidth(i, (short) (35.7 * 80));
            // // sheet.autoSizeColumn(i);
            // byte[] bsValue = (byte[]) value;
            // HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0,
            // 1023, 255, (short) 6, index, (short) 6, index);
            // anchor.setAnchorType(2);
            // patriarch.createPicture(anchor,
            // workbook.addPicture(bsValue,
            // HSSFWorkbook.PICTURE_TYPE_JPEG));
            // }
            else {
                cell.setCellStyle(stringCellStyle);
                cell.setCellValue(value + "");
            }

        } else {
            cell.setCellStyle(stringCellStyle);

            cell.setCellValue("");
        }
    }

    private static CellStyle getDateCellStyle(SXSSFWorkbook workbook){
        CellStyle cellStyle = getCellStyle(workbook);
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        //cellStyle.setWrapText(true);

        return cellStyle;
    }

    private static CellStyle getNumberCellStyle(SXSSFWorkbook workbook){
        CellStyle cellStyle = getCellStyle(workbook);
        cellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        //cellStyle.setWrapText(true);

        return cellStyle;
    }

    private static CellStyle getStringCellStyle(SXSSFWorkbook workbook){
        CellStyle cellStyle = getCellStyle(workbook);
        cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
        // cellStyle.setWrapText(true);

        return cellStyle;
    }

    private static CellStyle getCellStyle(SXSSFWorkbook workbook){
        // 生成单元格样式
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.WHITE.index);
        cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        cellStyle.setBorderRight(CellStyle.BORDER_THIN);
        cellStyle.setBorderTop(CellStyle.BORDER_THIN);
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        //cellStyle.setWrapText(true);

        // 生成另一个字体
        Font font = workbook.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        cellStyle.setFont(font);
        return cellStyle;
    }

    private static void createRow(SXSSFWorkbook workbook, Sheet sheet, List<ExcelColumn> columns, List<Map> listData){
        CellStyle cellStyle = getCellStyle(workbook);
        // 声明一个画图的顶级管理器
        org.apache.poi.ss.usermodel.Drawing patriarch = sheet.createDrawingPatriarch();
        int j = 1;
        for (Map map : listData) {
            Row row = sheet.createRow(j);
            int k = 0;
            for (ExcelColumn header : columns) {
                Cell cell = row.createCell(k);
                Object value = map.get(header.getField());
                createCell(cell, value, workbook);
                k++;
            }
            j++;
        }
    }

    public static void createRow(SXSSFWorkbook workbook, Sheet sheet, List<ExcelColumn> columns, ResultSet rs) throws SQLException{

        // 声明一个画图的顶级管理器
        org.apache.poi.ss.usermodel.Drawing patriarch = sheet.createDrawingPatriarch();
        int j = 1;
        while (rs.next()) {
            Row row = sheet.createRow(j);
            int k = 0;
            for (ExcelColumn header : columns) {
                Cell cell = row.createCell(k);
                Object value = rs.getObject(k + 1);
                createCell(cell, value, workbook);
                k++;
            }

            j++;
        }

    }

    public static void createRow(SXSSFWorkbook workbook, Sheet sheet, List<ExcelColumn> columns, ResultSet rs, int i) throws SQLException{

        // 声明一个画图的顶级管理器
        org.apache.poi.ss.usermodel.Drawing patriarch = sheet.createDrawingPatriarch();

        Row row = sheet.createRow(i + 1);
        int k = 0;
        for (ExcelColumn header : columns) {
            Cell cell = row.createCell(k);
            Object value = rs.getObject(k + 1);
            createCell(cell, value, workbook);
            k++;
        }


    }

//	public static void createRow(SXSSFWorkbook workbook, Sheet sheet, List<ExcelColumn> columns, ScrollableResults rs) throws SQLException {
//
//		// 声明一个画图的顶级管理器
//		org.apache.poi.ss.usermodel.Drawing patriarch = sheet.createDrawingPatriarch();
//		int j = 1;
//		while (rs.next()) {
//			Object[] rso = rs.get();			
//			Row row = sheet.createRow(j);
//			int k = 0;
//			for (ExcelColumn header : columns) {
//				Cell cell = row.createCell(k);
//				Object value = rso[k];
//				createCell(cell, value, workbook);
//				k++;
//			}
//
//			j++;
//		}
//
//	}

    /**
     * 从流中获取Excel数据
     *
     * @param is
     * @param fileName
     * @param sheetNum
     * @param columnKeys
     * @return
     * @throws IOException
     */
    public static List<Map> getExcelData(InputStream is, String fileName, int sheetNum, String[] columnKeys) throws IOException{
        Workbook wb;
        try {
            if (fileName.endsWith(".xls")) {
                wb = new HSSFWorkbook(is);
            } else if (fileName.endsWith(".xlsx")) {
                wb = new XSSFWorkbook(is);
            } else {
                throw new ComException(100, "文件扩展名必须是xls或xlsx");
            }
        } catch (Exception e) {

            throw new ComException(1, e);
        } finally {
            // is.close();
        }

        Sheet sheet = wb.getSheetAt(sheetNum);
        if (sheet.getLastRowNum() < 1 || sheet.getRow(1).getLastCellNum() < 1) {
            throw new ComException(1000, "此文件为空。");
        }

        // 格式化数字，禁止科学计数法
        DecimalFormat df = new DecimalFormat("#.#########");
        // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        List<Map> listData = new ArrayList<Map>();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            Map item = new HashMap();
            for (int j = 0; j < row.getLastCellNum(); j++) {
                Cell cell = row.getCell(j);

                Object cellValue = null;
                if (cell == null) {
                    if (columnKeys != null) {
                        item.put(columnKeys[j], null);
                    } else {
                        item.put(j, null);
                    }
                    continue;
                }
                switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_BLANK:
                        cellValue = null;
                        break;
                    case Cell.CELL_TYPE_BOOLEAN:
                        cellValue = cell.getBooleanCellValue();

                        break;
                    case Cell.CELL_TYPE_STRING:
                        cellValue = StringUtil.isEmpty(cell.getStringCellValue()) ? null : cell.getStringCellValue();
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        if (DateUtil.isCellDateFormatted(cell)) {// poi提供的Date类型判断方法
                            Date date = cell.getDateCellValue();
                            cellValue = date;
                        } else {
                            cellValue = df.format(cell.getNumericCellValue());
                        }

                        break;

                    case Cell.CELL_TYPE_FORMULA:
                        try {
                            cellValue = df.format(cell.getNumericCellValue());
                        } catch (Exception e) {
                            // TODO: handle exception
                            cellValue = cell.getStringCellValue();
                        }
                        break;
                    default:
                        cellValue = cell.toString();
                        break;
                }
                if (columnKeys != null) {
                    item.put(columnKeys[j], cellValue);
                } else {
                    item.put(j, cellValue);
                }
            }
            listData.add(item);

        }

        return listData;
    }

}
