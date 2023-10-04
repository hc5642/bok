package com.bok.ohc.test;

import java.io.BufferedReader;
import java.io.FileReader;

public class GzipBase64Main {

	public static void main(String[] args) {

		FileReader fr = null;
		BufferedReader br = null;

		StringBuffer sb = new StringBuffer();

		try {
			fr = new FileReader("C:\\dev\\workspace-sts\\SimpleTestProject\\src\\com\\bok\\ohc\\test\\sample.xml");
			br = new BufferedReader(fr);

			String temp = "";
			while ((temp = br.readLine()) != null) {
				sb.append(temp);
			}
		} catch (Exception e) {
			System.err.println(e);
		} finally {
			try {
				if (br != null)
					br.close();
				if (fr != null)
					fr.close();
			} catch (Exception e) {
			}
		}

		System.out.println(sb.toString());

	}

}
