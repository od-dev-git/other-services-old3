package org.egov.report.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.egov.report.model.DCBReportModel;

public class DCBReportExcelGenerator {

	private Map<String, DCBReportModel> dcbReportMap;
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	
	public DCBReportExcelGenerator(Map<String, DCBReportModel> dcbReportMap) {
        this.dcbReportMap = dcbReportMap;
        this.workbook = new XSSFWorkbook();
        this.sheet = workbook.createSheet("DCB Report");
    }
	
	public void generateExcelFile(File temporaryfile) throws IOException {
		writeHeader();
		write();
		// writing the workbook into the temporary file...
		FileOutputStream outputStream = new FileOutputStream(temporaryfile);
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
	}
	
	private void writeHeader() {
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(12);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        
        createCell(row, 0, "Sujog Property Id", style);
        createCell(row, 1, "Old Property Id", style);
        createCell(row, 2, "Legacy Id", style);
        createCell(row, 3, "Tax Ward No.", style);
        createCell(row, 4, "Current Demand.", style);
        createCell(row, 5, "Arrear Demand", style);
        createCell(row, 6, "Total Demand", style);
        createCell(row, 7, "Current Payment", style);
        createCell(row, 8, "Arrear Demand Collection", style);
        createCell(row, 9, "Current Demand Collection", style);
    }
	
	private void createCell(Row row, int columnCount, Object valueOfCell, CellStyle style) {
        Cell cell = row.createCell(columnCount);
        if (valueOfCell instanceof Integer) {
            cell.setCellValue((Integer) valueOfCell);
        } else if (valueOfCell instanceof Long) {
            cell.setCellValue((Long) valueOfCell);
        } else if (valueOfCell instanceof String) {
            cell.setCellValue((String) valueOfCell);
        } else if (valueOfCell instanceof BigDecimal) {
            cell.setCellValue(String.valueOf(valueOfCell));
        } else if (valueOfCell==null) {
        	cell.setCellValue(String.valueOf(""));
        }
        else {
        	cell.setCellValue(Boolean.valueOf(String.valueOf(valueOfCell)));
        }
        cell.setCellStyle(style);
    }
	
	private void write() {
        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(11);
        style.setFont(font);
        for (Map.Entry<String, DCBReportModel> entry : dcbReportMap.entrySet()) {
        	
        	String key = entry.getKey();
			DCBReportModel record = entry.getValue();
        	
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, record.getPropertyId(), style);
            createCell(row, columnCount++, record.getOldPropertyId(), style);
            createCell(row, columnCount++, record.getLegacyId(), style);
            createCell(row, columnCount++, record.getWard(), style);
            createCell(row, columnCount++, record.getCurrentDemand(), style);
            createCell(row, columnCount++, record.getArrearDemand(), style);
            createCell(row, columnCount++, record.getTotalDemand(), style);
            createCell(row, columnCount++, record.getCurrentPayment(), style);
            createCell(row, columnCount++, record.getArrearCollection(), style);
            createCell(row, columnCount++, record.getCurrentCollection(), style);
        }
    }
}
