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

public class ApplicationsReportExcelGenerator {

    private List<Map<String, Object>> applicationsDetailsList;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public ApplicationsReportExcelGenerator(List<Map<String, Object>> applicationsDetailsList) {
        this.applicationsDetailsList = applicationsDetailsList;
        this.workbook = new XSSFWorkbook();
        this.sheet = workbook.createSheet("Applications Details");
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
        createCell(row, 1, "Application No.", style);
        createCell(row, 2, "Status", style);
        createCell(row, 3, "Applicant Name", style);
        createCell(row, 4, "Approval Name", style);
        createCell(row, 5, "Permit No.", style);
        createCell(row, 6, "Applicant Mobile", style);
        createCell(row, 7, "Mauza", style);
        createCell(row, 8, "Risk Type", style);
        createCell(row, 9, "Service Type", style);
        createCell(row, 10, "Plot No", style);
        createCell(row, 11, "Workflow", style);
        createCell(row, 12, "Is BUA Above 500", style);
        createCell(row, 13, "Architect Name", style);
        createCell(row, 14, "Architect Mobile", style);
        createCell(row, 15, "Current Status", style);
        createCell(row, 16, "Pending Days Since Current Status", style);
        createCell(row, 17, "Officer Name At Pending", style);
        createCell(row, 18, "Submission Date", style);
        createCell(row, 19, "Pending Days Since Submission", style);
        createCell(row, 20, "Approval Date", style);
        createCell(row, 21, "Total Built-up Area", style);
        createCell(row, 22, "Total Plot Area", style);
        createCell(row, 23, "Max Building Height", style);
        createCell(row, 24, "Alteration Sub-Service", style);
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
            cell.setCellValue((Boolean) valueOfCell);
        }
        cell.setCellStyle(style);
    }
    
    
    private void write() {
        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(11);
        style.setFont(font);
        for (Map<String, Object> record: applicationsDetailsList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, record.get("ulb"), style);
            createCell(row, columnCount++, record.get("applicationno"), style);
            createCell(row, columnCount++, record.get("status"), style);
            createCell(row, columnCount++, record.get("applicantname"), style);
            createCell(row, columnCount++, record.get("approvalname"), style);
            createCell(row, columnCount++, record.get("permitnumber"), style);
            createCell(row, columnCount++, record.get("applicantmobilenumber"), style);
            createCell(row, columnCount++, record.get("mauza"), style);
            createCell(row, columnCount++, record.get("risktype"), style);
            createCell(row, columnCount++, record.get("servicetype"), style);
            createCell(row, columnCount++, record.get("plotnumber"), style);
            createCell(row, columnCount++, record.get("workflow"), style);
            createCell(row, columnCount++, record.get("isbuaabove500"), style);
            createCell(row, columnCount++, record.get("architectname"), style);
            createCell(row, columnCount++, record.get("architectmobilenumber"), style);
            createCell(row, columnCount++, record.get("currentstatus"), style);
            createCell(row, columnCount++, record.get("pendingdayssincecurrentstatus"), style);
            createCell(row, columnCount++, record.get("officernameatpending"), style);
            createCell(row, columnCount++, record.get("submissiondate"), style);
            createCell(row, columnCount++, record.get("pendingdayssincesubmission"), style);
            createCell(row, columnCount++, record.get("approvaldate"), style);
            createCell(row, columnCount++, record.get("totalbuiltuparea"), style);
            createCell(row, columnCount++, record.get("totalplotarea"), style);
            createCell(row, columnCount++, record.get("maxbuildingheight"), style);
            createCell(row, columnCount++, record.get("alterationsubservice"), style);
            
        }
    }
    
}
