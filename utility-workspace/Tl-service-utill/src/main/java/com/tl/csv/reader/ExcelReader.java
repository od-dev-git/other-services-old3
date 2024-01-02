package com.tl.csv.reader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.tl.billing.slab.TradeTypeBillingSlab;
import com.tl.csv.trade.ApplicationType;
import com.tl.csv.trade.CsvTrade;
import com.tl.csv.trade.LicenseType;
import com.tl.csv.trade.TradeTypeUtill;
import com.tl.csv.trade.UOMUnit;
import com.tl.csv.trade.type.TradeType;

public class ExcelReader {
	private static String SHEET_NAME = "Sheet8";
//	private static String SHEET_PATH = "C:\\Users\\mdrezak918\\Desktop\\Odisa\\tl-service\\Odisha TL Types & Rates_Update12012022.xlsx";
//	private static String SHEET_PATH = "C:\\Users\\vmodisauser07\\Utility Workspace\\TL_tradetype\\MDMS_Update_27092022.xlsx";
	private static String SHEET_PATH = "C:\\Users\\vmodisauser07\\Digit\\other-services\\utility-workspace\\TL_tradetype\\MDMS_Update_18122023.xlsx";
	private static List<CsvTrade> csvTrades;

	public static void main(String[] args) throws Exception {
		System.out.println("Total record read from csv: " + csvTrades.size());
		TradeType.createTreadType(csvTrades);
		// TradeTypeBillingSlab.createTreadType(csvTrades);
	}

	static {
		try {
			csvTrades = new ArrayList<>();
			Workbook workbook = WorkbookFactory.create(new File(SHEET_PATH));
			Sheet sheet = workbook.getSheetAt(workbook.getSheetIndex(SHEET_NAME));
			DataFormatter dataFormatter = new DataFormatter();
			for (Row row : sheet) {
				if (row.getRowNum() == 0)
					continue;
				try {
					char key = 65;
					CsvTrade csvTrade = new CsvTrade();
					for (Cell cell : row) {
						String cellValue = dataFormatter.formatCellValue(cell);
						if (cellValue != null)
							cellValue = cellValue.trim();
						switch (key) {
						case 'A':
							csvTrade.setUlb(cellValue);
							break;
						case 'B':
							setTradeSubType(cellValue, csvTrade);
							break;
						case 'C':
							setTradeSubTypeCode(cellValue, csvTrade);
							break;
						case 'D':
							setLicenseType(cellValue, csvTrade);
							break;
						case 'E':
							setApplicationType(cellValue, csvTrade);
							break;
						case 'F':
							setUomType(cellValue, csvTrade);
							break;
						case 'G':
							setUomFrom(cellValue, csvTrade);
							break;
						case 'H':
							setUomTo(cellValue, csvTrade);
							break;
						case 'I':
							setLicenseFee(cellValue, csvTrade);
							break;

						}
						key++;
					}

					csvTrades.add(csvTrade);
				} catch (Exception e) {
					// e.printStackTrace();
					System.out.println("Error in row no " + (row.getRowNum() + 1) + " error " + e.getMessage());
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void setTradeSubType(String cellValue, CsvTrade csvTrade) {
		if (cellValue.contains("\n"))
			cellValue = cellValue.replace("\n", "");
		String treadSubtypeCode = TradeTypeUtill.getTradeSubTypeCode(cellValue);
		if (treadSubtypeCode == null) {
			throw new RuntimeException("No code found for trade-" + cellValue);
		}
		csvTrade.setTradeSubType(cellValue);
		csvTrade.setTradeSubTypeCode(cellValue);
	}

	private static void setTradeSubTypeCode(String cellValue, CsvTrade csvTrade) {

		csvTrade.setTradeSubTypeCode(cellValue);

	}

	private static void setLicenseType(String cellValue, CsvTrade csvTrade) {
		csvTrade.setLicenseType(LicenseType.valueOf(cellValue.toUpperCase()));
	}

	private static void setApplicationType(String cellValue, CsvTrade csvTrade) {
		csvTrade.setApplicationType(ApplicationType.valueOf(cellValue.toUpperCase()));
	}

	private static void setUomType(String cellValue, CsvTrade csvTrade) {
		csvTrade.setUmo(UOMUnit.valueOf(cellValue));
	}

	private static void setUomFrom(String cellValue, CsvTrade csvTrade) {
		if ("NA".equals(cellValue)) {
			csvTrade.setUmoFrom(null);
		} else {
			csvTrade.setUmoFrom(Float.parseFloat(cellValue));
		}

	}

	private static void setUomTo(String cellValue, CsvTrade csvTrade) {
		if ("NA".equals(cellValue)) {
			csvTrade.setUmoTo(null);
		} else {
			csvTrade.setUmoTo(Float.parseFloat(cellValue));
		}

	}

	private static void setLicenseFee(String cellValue, CsvTrade csvTrade) {
		if (cellValue != null)
			csvTrade.setLicenseFee(cellValue.equals("") ? 0 : Float.parseFloat(cellValue));
	}
}
