package com.tl.csv.trade;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tl.csv.reader.Constant;

public class TradeTypeUtill {
	
	private static Properties tlCode=new Properties();
	
	public static String getTradeSubTypeCode(String treadSubtype) {
		treadSubtype=treadSubtype.intern();
		if(tlCode.isEmpty()) {
			/*
			 * this code for making TradeType properties json from mdms
			 */
			//readFromMDMSAndWrite(Constant.MDMS_DATA_PATH, Constant.MDMS_DATA_FILENAME);
			loadTlCode();
		}
		String result = null;
		for (Map.Entry entry : tlCode.entrySet()) {
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();
			if (treadSubtype.equalsIgnoreCase(value)) {
				result = key;
				break;
			}
		}
		return result;
	}
	
	private static void loadTlCode() {
		try (InputStream inputStream = new FileInputStream(Constant.TRADE_CODE_PROPERTIES_PATH)) {
			tlCode.load(inputStream);
		} catch (IOException io) {
			io.printStackTrace();
		}
	}
	
	private static void readFromMDMSAndWrite(String fileUrl,String fileName) {
		File folder = new File(fileUrl);
		File[] listOfFiles = folder.listFiles();
		List<File> list=new ArrayList<>();
		for (int i = 0; i < listOfFiles.length; i++) {
			File file=listOfFiles[i];
			getFile(file, fileName, list);
		}
		readAllTradeType(list);
		write();
	}
	
	public static void write() {
		Properties properties=new Properties();
		properties.putAll(tlCode);
		try (OutputStream output = new FileOutputStream(Constant.TRADE_CODE_PROPERTIES_PATH)) {
			properties.store(output, null);
		}catch (IOException io) {
            io.printStackTrace();
        }
	}
	
	private static void getFile(File file,String fileName,List<File> list) {
		if (file.isFile()) {
			if(fileName.equals(file.getName())) {
				list.add(file);
			}
		} else if (file.isDirectory()) {
			File[] listOfFiles = file.listFiles();
			for (int i = 0; i < listOfFiles.length; i++) {
				File f=listOfFiles[i];
				getFile(f, fileName,list);
			}
		}
	}
	
	private static void readAllTradeType(List<File> list) {
		ObjectMapper mapper=new ObjectMapper();
		for(File file:list) {
			try {
				Map<String,ArrayList> map=mapper.readValue(file, Map.class);
//				System.out.println(map.get("TradeType").get(0));
				for(LinkedHashMap lh:(ArrayList<LinkedHashMap>)map.get("TradeType")) {
					String code=(String) lh.get("code");
					String name=(String) lh.get("name");
					if(code!=null && name !=null)
						tlCode.put(code, name);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}

}
