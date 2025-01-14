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

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PTDDNDetailsReportExcelGenerator {

    private List<Map<String, Object>> ptDDNList;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public PTDDNDetailsReportExcelGenerator(List<Map<String, Object>> ptDDNList) {
        this.ptDDNList = ptDDNList;
        this.workbook = new XSSFWorkbook();
        this.sheet = workbook.createSheet("PT DDN Details");
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

        createCell(row, 0, "ULB", style);
        createCell(row, 1, "Property ID", style);
        createCell(row, 2, "Name", style);
        createCell(row, 3, "DDN No.", style);
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
        log.info("Starting to write data rows to the Excel sheet...");

        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(11);
        style.setFont(font);

        // Loop through each record in the ptDDNList and write data to the Excel sheet
        for (Map<String, Object> record : ptDDNList) {
            // Log the record before processing
            log.info("Processing record #{}: {}", rowCount, record);

            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            // Log the columns being written
            log.info("Creating cells for the following columns for record #{}: ULB, Property ID, Name, DDN No.", rowCount);

            createCell(row, columnCount++, record.get("ulb"), style);
            createCell(row, columnCount++, record.get("propertyid"), style);
            createCell(row, columnCount++, record.get("names"), style);
            createCell(row, columnCount++, record.get("ddnno"), style);

            // Log after creating cells for the row
            log.info("Row #{} processed successfully.", rowCount - 1);
        }

        log.info("Data rows written successfully. Total rows: {}", rowCount - 1);
    }

}