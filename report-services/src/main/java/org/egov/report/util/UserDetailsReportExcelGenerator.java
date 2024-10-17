package org.egov.report.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class UserDetailsReportExcelGenerator {

    private List<Map<String, Object>> userDetailsList;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public UserDetailsReportExcelGenerator(List<Map<String, Object>> userDetailsList) {
        this.userDetailsList = userDetailsList;
        this.workbook = new XSSFWorkbook();
        this.sheet = workbook.createSheet("User Details");
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
        
        createCell(row, 0, "Name", style);
        createCell(row, 1, "Mobile Number", style);
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
        } else {
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
        for (Map<String, Object> record: userDetailsList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, record.get("uuid"), style);
            createCell(row, columnCount++, record.get("mobilenumber"), style);
        }
    }
}
