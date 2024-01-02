package com.tl.csv.trade.type;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.tl.csv.reader.Constant;
import com.tl.csv.trade.CsvTrade;
import com.tl.csv.trade.LicenseType;

public class TradeType {

	public static void createTreadType(List<CsvTrade> csvTrades) throws IOException {

		Map<String, List<CsvTrade>> map = csvTrades.stream().collect(Collectors.groupingBy(CsvTrade::getUlb));
		for (Map.Entry<String, List<CsvTrade>> entry : map.entrySet()) {
			prepareTradTypeJson(entry.getKey(), entry.getValue());
		}
	}

	private static void prepareTradTypeJson(String ulbName, List<CsvTrade> csvTrades) throws IOException {

		Map<LicenseType, List<CsvTrade>> map = csvTrades.stream()
				.collect(Collectors.groupingBy(CsvTrade::getLicenseType));

		// PERMANENT
		if(map.get(LicenseType.PERMANENT)!=null && !map.get(LicenseType.PERMANENT).isEmpty())
			prepareTradTypeJsonPermanent(ulbName, map.get(LicenseType.PERMANENT));
		// TEMPORARY
		if(map.get(LicenseType.TEMPORARY)!=null && !map.get(LicenseType.TEMPORARY).isEmpty())
			prepareTradTypeJsonTemporary(ulbName, map.get(LicenseType.TEMPORARY));
	}

	private static void prepareTradTypeJsonPermanent(String ulbName, List<CsvTrade> csvTrades) throws IOException {
		Set<Tread> treads = new HashSet();

		for (CsvTrade csvTrade : csvTrades) {
			Tread tread = new Tread();

			tread.setCode(csvTrade.getTradeSubTypeCode());
			tread.setName(csvTrade.getTradeSubType());
			if(csvTrade.getUmo().getValue()==null) {
				tread.setUom(csvTrade.getUmo().getValue());
				tread.setUomDescription(csvTrade.getUmo().getValue());
			}else {
				tread.setUom(csvTrade.getUmo().toString());
				tread.setUomDescription(csvTrade.getUmo().getValue());
			}
			tread.setApplicationDocument(getApplicationDocuments());

			treads.add(tread);

		}

		writeJson(ulbName +"-" +LicenseType.PERMANENT, treads);
	}

	private static void prepareTradTypeJsonTemporary(String ulbName, List<CsvTrade> csvTrades) throws IOException {

		Set<Tread> treads = new HashSet();

		for (CsvTrade csvTrade : csvTrades) {
			Tread tread = new Tread();

			tread.setCode(csvTrade.getTradeSubTypeCode());
			tread.setName(csvTrade.getTradeSubType());
			if(csvTrade.getUmo().getValue()==null) {
				tread.setUom(csvTrade.getUmo().getValue());
				tread.setUomDescription(csvTrade.getUmo().getValue());
			}else {
				tread.setUom(csvTrade.getUmo().toString());
				tread.setUomDescription(csvTrade.getUmo().getValue());
			}
			tread.setApplicationDocument(getApplicationDocuments());

			treads.add(tread);

		}

		writeJson(ulbName +"-" +LicenseType.TEMPORARY, treads);
	

	}
	
	public static List<ApplicationDocument> getApplicationDocuments() {
		ApplicationDocument NEW = new ApplicationDocument();
		NEW.setApplicationType("NEW");
		NEW.setDocumentList(Arrays.asList(new String[] { "OWNERIDPROOF", "OWNERSHIPPROOF", "OWNERPHOTO" }));
		ApplicationDocument RENEWAL = new ApplicationDocument();
		RENEWAL.setApplicationType("RENEWAL");
		RENEWAL.setDocumentList(
				Arrays.asList(new String[] { "OWNERIDPROOF", "OWNERSHIPPROOF", "OWNERPHOTO", "OLDLICENCENO" }));
		return Arrays.asList(new ApplicationDocument[] { NEW, RENEWAL });
	}

	private static void writeJson(String ulbName, Set<Tread> treads) throws IOException {
		FileOutputStream fos = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

			mapper.writeValue(baos, treads);
			final byte[] data = baos.toByteArray();

			fos = new FileOutputStream(new File(Constant.WORK_DIR+"//TradeLicense//" + ulbName + ".json"));
			baos.writeTo(fos);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			fos.close();
		}
	}
}
