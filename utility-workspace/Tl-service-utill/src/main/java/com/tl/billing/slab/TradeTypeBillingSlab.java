package com.tl.billing.slab;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.tl.csv.reader.Constant;
import com.tl.csv.trade.ApplicationType;
import com.tl.csv.trade.CsvTrade;
import com.tl.csv.trade.LicenseType;
import com.tl.csv.trade.UOMUnit;
import com.tl.csv.trade.type.Tread;

public class TradeTypeBillingSlab {
	public static void createTreadType(List<CsvTrade> csvTrades) throws IOException {

		Map<String, List<CsvTrade>> map = csvTrades.stream().collect(Collectors.groupingBy(CsvTrade::getUlb));
		for (Map.Entry<String, List<CsvTrade>> entry : map.entrySet()) {
			prepareTradTypeJson(entry.getKey(), entry.getValue());
		}
	}

	private static void prepareTradTypeJson(String ulbName, List<CsvTrade> csvTrades) throws IOException {

		ulbName = ulbName.toLowerCase();

		Map<LicenseType, List<CsvTrade>> map = csvTrades.stream()
				.collect(Collectors.groupingBy(CsvTrade::getLicenseType));

		// PERMANENT
		List<CsvTrade> permanent = map.get(LicenseType.PERMANENT);

		Map<ApplicationType, List<CsvTrade>> pnew = permanent.stream()
				.collect(Collectors.groupingBy(CsvTrade::getApplicationType));

		writeJson("NEW//" + ulbName, getBillingSlabs(ulbName, pnew.get(ApplicationType.NEW)));
		writeJson("RENEWAL//" + ulbName, getBillingSlabs(ulbName, pnew.get(ApplicationType.RENEWAL)));

		// TEMPORARY
		System.out.println("TEMPORARY pendding");
	}

	private static List<BillingSlab> getBillingSlabs(String ulbName, List<CsvTrade> csvTrades) {
		List<BillingSlab> billingSlabs = new ArrayList<BillingSlab>();

		for (CsvTrade csvTrade : csvTrades) {
			BillingSlab billingSlab = new BillingSlab();
			billingSlab.setApplicationType(csvTrade.getApplicationType().toString());
			billingSlab.setLicenseType(csvTrade.getLicenseType().toString());
			billingSlab.setRate(csvTrade.getLicenseFee());
			billingSlab.setTenantId("od." + ulbName);
			billingSlab.setFromUom(csvTrade.getUmoFrom());
			billingSlab.setToUom(csvTrade.getUmoTo());
			billingSlab.setTradeType(csvTrade.getTradeSubTypeCode());
			if (UOMUnit.NA.equals(csvTrade.getUmo()))
				billingSlab.setUom(null);
			else
				billingSlab.setUom(csvTrade.getUmo().name());
//			if (csvTrade.getUmoFrom() == null && csvTrade.getUmoTo() == null)
//				billingSlab.setType(Type.FLAT);
//			else
//				billingSlab.setType(Type.RATE);
			billingSlab.setType(Type.FLAT);
			
			if (billingSlab.getRate() > 0) {
				billingSlabs.add(billingSlab);
			} else {
				System.out.println("Rejected : " + billingSlab);
			}
		}
		return billingSlabs;
	}

	private static void writeJson(String ulbName, List<BillingSlab> billingSlabs) throws IOException {
		FileOutputStream fos = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

			mapper.writeValue(baos, billingSlabs);
			final byte[] data = baos.toByteArray();

			fos = new FileOutputStream(new File(Constant.WORK_DIR + "//2022//12ulbnew19jan2022//" + ulbName + ".json"));
			baos.writeTo(fos);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			fos.close();
		}
	}
}
