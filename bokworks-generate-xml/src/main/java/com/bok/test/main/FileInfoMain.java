package com.bok.test.main;

import java.io.File;

public class FileInfoMain {
	
//	final static String ROOT_DIR = "./src/main/resources/static/output/bokwire/convert_MAX";
	final static String ROOT_DIR = "./src/main/resources/static/output/bokwire/convert_MIN";

	public static void main(String[] args) {
		
		File dir = new File(ROOT_DIR);
		File [] xmls = dir.listFiles(); 
		
		for ( File xml : xmls ) {
			System.out.println(xml.getName() + "\t" + xml.length());
		}

	}

}
