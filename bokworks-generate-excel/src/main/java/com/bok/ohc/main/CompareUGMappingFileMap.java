package com.bok.ohc.main;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.bok.ohc.util.CompareUGMappingUtil;

/**
 * TX_CODE와 UG 파일명을 매핑한다.
 * @author ohhyonchul
 *
 */
public class CompareUGMappingFileMap {
	
	private Map<String, String> TX_MAP;
	
	public CompareUGMappingFileMap(String filePath) {
		FileInputStream file = null;
		try {
			// 경로에 있는 파일을 읽는다, "files/(붙임)한은금융망 서버접속 전문설명서_v1.3.7_표준전문개발반송부용_매핑포함.xlsx"
			file = new FileInputStream(filePath);
			XSSFWorkbook workbook = new XSSFWorkbook(file);

			int rowNo = 0;
			XSSFSheet sheet = workbook.getSheet("목록");
			int rows = 83;
			for (rowNo = 0; rowNo < rows; rowNo++) {
				XSSFRow row = sheet.getRow(rowNo);
				if (row != null) {
					XSSFCell cell1 = row.getCell(4); // 셀의 값을 가져온다
					XSSFCell cell2 = row.getCell(10); // 셀의 값을 가져온다
					if (cell1 == null || cell2 == null)
						continue;
					this.TX_MAP.put(CompareUGMappingUtil.getCellValue(cell1), CompareUGMappingUtil.getCellValue(cell2));
//					if (XSSFUtil.getCellValue(cell1).equals(txCode))
//						retValue = XSSFUtil.getCellValue(cell2);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (file != null)
					file.close();
			} catch (Exception e) {
				//
			}
		}
	}
	
	public CompareUGMappingFileMap() {
		TX_MAP = new HashMap<String, String>();
		TX_MAP.put("BKS10B011", "pacs.009");
		TX_MAP.put("BKS20F030", "");
		TX_MAP.put("BKS20F040", "");
		TX_MAP.put("BKS10F060", "");
		TX_MAP.put("BKS10E070", "");
		TX_MAP.put("BKS20E090", "");
	}
	
	public Set<String> keySet() {
		return this.TX_MAP.keySet();
	}
	
	public String getFileName(String TX_CODE) {
		return this.TX_MAP.get(TX_CODE);
	}
	
	

}
