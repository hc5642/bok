package com.bok.ohc.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.bok.ohc.util.CompareUGMappingUtil;

public class ReSortUsageGuideMain {
	
	static final String BASE_DIR = "src/main/resources/static";
	static final String UG_PATH = BASE_DIR + "/input/20231004/";
	static final String OUTPUT_FILE_PATH = BASE_DIR + "/output/20231004";
	

	public static void main(String [] args) throws IOException {
		
		String outputFilePath = OUTPUT_FILE_PATH + "/BOK_Phase1_CorePayment_v_1_2_FullView_통합본_" + (new SimpleDateFormat("yyyyMMddHmm").format(new Date())) +".xlsx";
		System.out.println(outputFilePath);
		FileOutputStream fos = null;
		
		try {
			
			/* 파일데이터 수집*/
			Map<String, String> fileMap = getFileList(UG_PATH);
			
			boolean isFirstFile = true;
			for ( String msgCd : fileMap.keySet() ) {
				
				XSSFWorkbook outputWorkbook = null;
				OPCPackage outputPkg = null;
				/* 저장햇던 output workbook 을 다시 열어 */
				if ( isFirstFile ) {
					outputWorkbook = new XSSFWorkbook();
					isFirstFile = false;
				} else {
					outputPkg = OPCPackage.open(new File(outputFilePath));
					outputWorkbook = new XSSFWorkbook(outputPkg);
				}
				System.out.println(msgCd);
				XSSFSheet outputSheet = outputWorkbook.createSheet(msgCd);
				
				/* (반복부) 실제 UG 값을 가져온다 */
				OPCPackage ugPkg = OPCPackage.open(new File(UG_PATH + fileMap.get(msgCd)));
				XSSFWorkbook UGworkbook = new XSSFWorkbook(ugPkg);
				XSSFSheet FullViewsheet = UGworkbook.getSheet("Full_View");
				XSSFCell Ugcell = null;
				String Ugvalue = null;
				int rowNo = 0;
				int rows = FullViewsheet.getPhysicalNumberOfRows();
				
				for (rowNo = 0; rowNo < rows; rowNo++) {
					
					XSSFRow ugRow = FullViewsheet.getRow(rowNo);
					XSSFRow outputRow = outputSheet.createRow(rowNo);
					
					if (ugRow != null) {
						
						int cells = ugRow.getPhysicalNumberOfCells(); // 해당 Row에 입력된 셀의 수를 가져온다
						
						for (int cellNo = 0; cellNo < cells; cellNo++) {
							
							Ugcell = ugRow.getCell(cellNo); // 셀의 값을 가져온다
							Ugvalue = CompareUGMappingUtil.getCellValue(Ugcell);
							
							XSSFCell outputCell = outputRow.createCell(cellNo);
							
							System.out.println(rowNo + "," + cellNo);
							
							outputCell = outputRow.createCell(cellNo);
							outputCell.setCellValue(Ugvalue);	
							outputCell.setCellStyle(CompareUGMappingUtil.getCommStyle(outputWorkbook));
							
						}
					}
				}
				/* 작성된 결과물을 엑셀파일에 저장한다 (2차 저장) */
				fos = new FileOutputStream(new File(outputFilePath));
				outputWorkbook.write(fos); 
				fos.close();
				if ( outputPkg != null ) outputPkg.close();
				ugPkg.close();
				
			}
			
		} catch ( Exception e ) {
			e.printStackTrace();
		} finally {
			if ( fos!= null ) {
				fos.close();
			}
		}
	}
	
	
	/**
	 * UG 파일들을 전문이름(8):파일명 으로 수집
	 * @param path
	 * @return
	 */
	public static Map<String, String> getFileList(String path) {
		
		Map<String, String> retValue = new TreeMap<String, String>();
		
		File [] files = new File(path).listFiles();
		
		String key;
		
		for ( File f : files ) {
			if ( !f.getName().startsWith("BOK") )
				continue;
			if ( !f.getName().endsWith("xlsx"))
				continue;
			// BOK_Phase1_CorePayment_v_1_2_BOK_acmt_023_001_03_IdentificationVerificationRequest_20231004_1307.xlsx
			
			String [] words = f.getName().split("_");
			
			key = words[7] + "." + words[8];
			if ( !words[12].startsWith("20") ) {
				key += "_" + words[12];
			}
			retValue.put(key, f.getName());
		}
		
		
		return retValue;
	}
}
