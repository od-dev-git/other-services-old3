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
import org.postgresql.util.PGobject;

import net.minidev.json.JSONArray;

public class FSMDataMartExcelGenerator {
	
	private List<Map<String, Object>> paymentDetailsList;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public FSMDataMartExcelGenerator(List<Map<String, Object>> dataMartList) {
        this.paymentDetailsList = dataMartList;
        this.workbook = new XSSFWorkbook();
        this.sheet = workbook.createSheet("Data Mart Report");
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
        createCell(row, 2, "Application Status", style);
        createCell(row, 3, "No. of Trips", style);
        createCell(row, 4, "Property Type", style);
        createCell(row, 5, "Property Sub Type", style);
        createCell(row, 6, "On Site Sanitation Type", style);
        createCell(row, 7, "Door Number", style);
        createCell(row, 8, "Street Name", style);
        createCell(row, 9, "City", style);
        createCell(row, 10, "Pin Code", style);
        createCell(row, 11, "Urban/Rural", style);
        createCell(row, 12, "Locality", style);
        createCell(row, 13, "Gram Panchayat", style);
        createCell(row, 14, "Village", style);
        createCell(row, 15, "District", style);
        createCell(row, 16, "State", style);
        createCell(row, 17, "Slum Name", style);
        createCell(row, 18, "Application Source", style);
        createCell(row, 19, "Desludging Entity", style);
        createCell(row, 20, "Longitude", style);
        createCell(row, 21, "Latitude", style);
        createCell(row, 22, "Geo Location Provided", style);
        createCell(row, 23, "Desludging Vehicle Number", style);
        createCell(row, 24, "Vehicle Type", style);
        createCell(row, 25, "Vehicle Capacity", style);
        createCell(row, 26, "Trip Amount", style);
        createCell(row, 27, "Advance Amount", style);
        createCell(row, 28, "Payment Amount", style);
        createCell(row, 29, "Payment Source", style);
        createCell(row, 30, "Payment Instrument Type", style);
        createCell(row, 31, "Application Sumbit Date", style);
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
        } else if (valueOfCell instanceof Double) {
            cell.setCellValue((Double)valueOfCell);
        } else if (valueOfCell==null) {
        	cell.setCellValue(String.valueOf(""));
        } else if (valueOfCell instanceof PGobject) {
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
        for (Map<String, Object> record: paymentDetailsList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, record.get("ulb"), style);
            createCell(row, columnCount++, record.get("applicationid"), style);
            createCell(row, columnCount++, record.get("applicationstatus"), style);
            createCell(row, columnCount++, record.get("nooftrips"), style);
            createCell(row, columnCount++, record.get("propertytype"), style);
            createCell(row, columnCount++, record.get("propertysubtype"), style);
            createCell(row, columnCount++, record.get("onsitesanitationtype"), style);
            createCell(row, columnCount++, record.get("doornumber"), style);
            createCell(row, columnCount++, record.get("streetname"), style);
            createCell(row, columnCount++, record.get("city"), style);
            createCell(row, columnCount++, record.get("pincode"), style);
            createCell(row, columnCount++, record.get("urbanruralflag"), style);
            createCell(row, columnCount++, record.get("locality"), style);
            createCell(row, columnCount++, record.get("gp"), style);
            createCell(row, columnCount++, record.get("village"), style);
            createCell(row, columnCount++, record.get("district"), style);
            createCell(row, columnCount++, record.get("state"), style);
            createCell(row, columnCount++, record.get("slumname"), style);
            createCell(row, columnCount++, record.get("applicationsource"), style);
            createCell(row, columnCount++, record.get("desludgingentity"), style);
            createCell(row, columnCount++, record.get("longitude"), style);
            createCell(row, columnCount++, record.get("latitude"), style);
            createCell(row, columnCount++, record.get("geolocationprovided"), style);
            createCell(row, columnCount++, record.get("desludgingvehiclenumber"), style);
            createCell(row, columnCount++, record.get("vehicletype"), style);
            createCell(row, columnCount++, record.get("vehiclecapacity"), style);
            createCell(row, columnCount++, record.get("tripamount"), style);
            createCell(row, columnCount++, record.get("advanceamount"), style);
            createCell(row, columnCount++, record.get("paymentamount"), style);
            createCell(row, columnCount++, record.get("paymentsource"), style);
            createCell(row, columnCount++, record.get("paymentinstrumenttype"), style);
            createCell(row, columnCount++, record.get("applicationsumbitdate"), style);
        }
    }


}
