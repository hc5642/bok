package com.bok.test.main;

import java.io.File;

public class FileInfoMain {
	
//	final static String ROOT_DIR = "./src/main/resources/static/output/bokwire/convert_MAX";
	final static String ROOT_DIR = "./src/main/resources/static/output/bokwire/convert_MIN";

	public static void main(String[] args) {
		
		File dir = new File(ROOT_DIR);
		File [] xmls = dir.listFiles(); 
		
		String fileName = "";
		for ( File xml : xmls ) {
			fileName = xml.getName();
//			System.out.println("--- DEBUG : " + fileName);
			fileName = fileName.replaceAll("Fedwire_Funds_Service_Release_2025_", "Fedwire_");
			fileName = fileName.replaceAll("_20230831_2310_iso15enriched.xsd.txt", ".xml");
			fileName = fileName.replaceAll("BOK_Phase1_CorePayment_v_1_1_BOK_", "BOK_");
			fileName = fileName.replaceAll("_20230829_0121_iso15enriched.xsd.txt", ".xml");
			fileName = fileName.replaceAll("_20230830_0857.xsd.txt", ".xml");
			System.out.println(fileName + "\t" + xml.length());
		}

	}

}
