package com.bok.ohc.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelWorkMain {

//	public static String TX_CODE[] = { "BKS20B070" };
	public static String TX_CODE[] = {"BKS20F030","BKS20F040","BKS10F060","BKS10E070","BKS20E090","BKS20E110","BKS10E150","BKS20E190","BKS10A011","BKS20A020","BKS20A030","BKS20A040","BKS10B011","BKS20B020","BKS20B030","BKS20B040","BKS20B050","BKS20B060","BKS20B070","BKS20B080","BKS10B021","BKS10B031","BKS20B360","BKS20B370","BKS10B091","BKS20B100","BKS20B110","BKS20B120","BKS20B130","BKS20B140","BKS10B081","BKS20B150","BKS20B160","BKS20B170","BKS10E300","BKS20E300","BKS10E310","BKS10E060","BKS20E130","BKS10B170","BKS20B180","BKS20B190","BKS20B200","BKS20E220","BKS10E120","BKF101011","BKS20G010","BKS20G020","BKF101021","BKS20G210","BKS20G220"};
	public static Map<String, String> COL_MAP = null;
	public static Map<String, String> PATH_MAP = null;
	public static Map<String, String> ADDINFO_MAP = null;

	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		XSSFWorkbook workbook = null;
		XSSFSheet sheet = null;
		XSSFRow row = null;
		XSSFCell cell = null;
		FileOutputStream fos = null;
		
		try {
			
			workbook = new XSSFWorkbook();
			
			Font commFont = workbook.createFont();
			commFont.setFontName("한컴 고딕");
			commFont.setFontHeight((short)200);
			
			Font headFont = workbook.createFont();
			headFont.setFontName("한컴 고딕");
			headFont.setFontHeight((short)200);
			headFont.setBoldweight((short)700);
			
			Font titleFont = workbook.createFont();
			titleFont.setFontName("한컴 고딕");
			titleFont.setFontHeight((short)250);
			titleFont.setBoldweight((short)700);
			        
			CellStyle commStyle = workbook.createCellStyle();
			commStyle.setFont(commFont);
			CellStyle headStyle = workbook.createCellStyle();
			headStyle.setFont(headFont);
			CellStyle titleStyle = workbook.createCellStyle();
			titleStyle.setFont(titleFont);
			
			String fileName = "";
			
			XSSFSheet headerSheet = workbook.createSheet("목차");
			headerSheet.setColumnWidth(0, 4000);
			headerSheet.setColumnWidth(1, 6000);
			
			XSSFRow headerRow = null;
			int headerRowIndex = 0;
			XSSFCell headerCell = null;
			
			headerRow = headerSheet.createRow(headerRowIndex++);
			headerCell = headerRow.createCell(0); headerCell.setCellValue("거래구분코드"); headerCell.setCellStyle(titleStyle);
			headerCell = headerRow.createCell(1); headerCell.setCellValue("UG파일명"); headerCell.setCellStyle(titleStyle);
			CreationHelper createHelper = workbook.getCreationHelper();
			Hyperlink link = createHelper.createHyperlink(Hyperlink.LINK_DOCUMENT);
			
			
			for ( String txCode : TX_CODE ) {
				
				COL_MAP = new LinkedHashMap<String, String>();
				PATH_MAP = new TreeMap<String, String>();	
				ADDINFO_MAP = new TreeMap<String, String>();
				
				fileName = getFileName(txCode).replaceAll("BOK_Phase1_CorePayment_", "BOK_Phase1_CorePayment_v_1_1_").replaceAll("_20230526_0443", "_20230809_0448.xlsx");
				
				headerRow = headerSheet.createRow(headerRowIndex++);
				
				headerCell = headerRow.createCell(0); headerCell.setCellValue(txCode); //headerCell.setCellStyle(commStyle);
							 link.setAddress("'"+txCode+"'!A1");		headerCell.setHyperlink(link);
				headerCell = headerRow.createCell(1); headerCell.setCellValue(fileName); headerCell.setCellStyle(commStyle);
				
				getPath(fileName, txCode);
				
				sheet = workbook.createSheet(txCode);
				
				sheet.setColumnWidth(0, 4000);
				sheet.setColumnWidth(1, 6000);
				sheet.setColumnWidth(3, 10000);
				sheet.setColumnWidth(5, 10000);
				
				row = sheet.createRow(0);
				cell = row.createCell(0);	cell.setCellValue(txCode);		cell.setCellStyle(titleStyle);
				cell = row.createCell(1);	cell.setCellValue(fileName);	cell.setCellStyle(titleStyle);
				System.out.println(txCode + "\t" + fileName);
				
				row = sheet.createRow(1);
				cell = row.createCell(0);	cell.setCellValue("순서");				cell.setCellStyle(headStyle);
				cell = row.createCell(1);	cell.setCellValue("한은망전문항목");	cell.setCellStyle(headStyle);
				cell = row.createCell(2);	cell.setCellValue("필수");				cell.setCellStyle(headStyle);
				cell = row.createCell(3);	cell.setCellValue("UG_매핑현황");		cell.setCellStyle(headStyle);
				cell = row.createCell(4);	cell.setCellValue("UG_MinMand");		cell.setCellStyle(headStyle);
				cell = row.createCell(5);	cell.setCellValue("UG매핑패스");		cell.setCellStyle(headStyle);
				
				int colIndex = 1, rowIndex=2, excelColIndex=0, loopCnt = 0;
				for ( String key : COL_MAP.keySet() ) {
					
					if ( loopCnt == 0 ) {
						row = sheet.createRow(rowIndex++);
					}
					loopCnt = 0;
					excelColIndex = 0;
					cell = row.createCell(excelColIndex++);	cell.setCellValue(colIndex);			cell.setCellStyle(commStyle);
					cell = row.createCell(excelColIndex++);	cell.setCellValue(key);					cell.setCellStyle(commStyle);
					cell = row.createCell(excelColIndex++);	cell.setCellValue(COL_MAP.get(key));	cell.setCellStyle(commStyle);
					
					int startCellIndex = 0;
					

					if ( key.indexOf("(") > 0)
						key = key.substring(0, key.indexOf("("));
					
					for ( String path : PATH_MAP.keySet() ) {
						
						startCellIndex = excelColIndex;
						
						if ( path.contains(colIndex+key) ) {
							
							String b = path.substring(path.indexOf(colIndex+key) + (colIndex+key).length());
							
							if (  !isNumeric(b.substring(0,1)) )
								continue;
							
							cell = row.createCell(startCellIndex++);	cell.setCellValue(path);	cell.setCellStyle(commStyle);
							String temp = PATH_MAP.get(path);
							String mm = temp.substring(0, temp.indexOf(";"));
							cell = row.createCell(startCellIndex++);	cell.setCellValue(mm);		cell.setCellStyle(commStyle);
							cell = row.createCell(startCellIndex++);	cell.setCellValue(temp.substring(temp.indexOf(";")+1));	cell.setCellStyle(commStyle);
							
							row = sheet.createRow(rowIndex++);	loopCnt++;
						}
						
					}
					colIndex++;
				}
				row = sheet.createRow(rowIndex++);
				row = sheet.createRow(rowIndex++);
				cell = row.createCell(0);	cell.setCellValue("<참고> UG에서 조사된 값의 목록(전체)");	cell.setCellStyle(headStyle);
				cell = row.createCell(3);	cell.setCellValue("Annotation Field No");					cell.setCellStyle(headStyle);
				cell = row.createCell(4);	cell.setCellValue("Min Mand");								cell.setCellStyle(headStyle);
				cell = row.createCell(5);	cell.setCellValue("PATH");									cell.setCellStyle(headStyle);
				cell = row.createCell(6);	cell.setCellValue("Annotation AddtionalInfomation");		cell.setCellStyle(headStyle);
				
				for ( String path : PATH_MAP.keySet() ) {
					row = sheet.createRow(rowIndex++);
					excelColIndex = 3;
					cell = row.createCell(excelColIndex++);	cell.setCellValue(path);					cell.setCellStyle(commStyle);
					String temp = PATH_MAP.get(path);
					cell = row.createCell(excelColIndex++);	cell.setCellValue(temp.substring(0, temp.indexOf(";")));	cell.setCellStyle(commStyle);
					cell = row.createCell(excelColIndex++);	cell.setCellValue(temp.substring(temp.indexOf(";")+1));		cell.setCellStyle(commStyle);
					cell = row.createCell(excelColIndex++); cell.setCellValue(ADDINFO_MAP.get(path));					cell.setCellStyle(commStyle);
				}
			}
			File file = new File("files/BOK_Phase1_CorePayment_BOK_매핑현황(20230808))_"+(new Date().getTime())+".xlsx");
			 fos = new FileOutputStream(file);
			workbook.write(fos);
		} catch ( Exception e ) {
			System.err.println(e.getMessage());
		} finally {
			if ( fos!= null ) fos.close();
		}
	}

	public static void getPath(String fileName, String txCode) {
		
		int minMandIndex = 0;
		int pathIndex = 0;
		FileInputStream file = null;
		try {
			// 경로에 있는 파일을 읽
			file = new FileInputStream("files/" + fileName);
			XSSFWorkbook workbook = new XSSFWorkbook(file);

			int rowNo = 0;
			int cellIndex = 0;
			XSSFSheet sheet = workbook.getSheet("Full_View");
			int rows = sheet.getPhysicalNumberOfRows();
			int annotationFieldNo = 0;
			int annotationAddInfo = 0;
			for (rowNo = 0; rowNo < rows; rowNo++) {
				XSSFRow row = sheet.getRow(rowNo);
				if (row != null) {
					int cells = row.getPhysicalNumberOfCells(); // 해당 Row에 사용자가 입력한 셀의 수를 가져온다
					if ( rowNo == 0 ) {
						for (cellIndex = 0; cellIndex <= cells; cellIndex++) {
							XSSFCell cell = row.getCell(cellIndex); // 셀의 값을 가져온다
							String value = getCellValue(cell);
							if ( value.contains(txCode)) {
								if ( value.contains("Field") ) {
									annotationFieldNo = cellIndex;
								} else if ( value.contains("Additional")) {
									annotationAddInfo = cellIndex;
								}
							} else if ( value.equals("Min Mand")) {
								minMandIndex = cellIndex;
							} else if ( value.contains("Path")) {
								pathIndex = cellIndex;
							}
						}
					} else {
						XSSFCell annoFieldCell = row.getCell(annotationFieldNo); // 셀의 값을 가져온다
						XSSFCell annoAddCell = row.getCell(annotationAddInfo); // /셀의 값을 가져온다
						XSSFCell pathCell = row.getCell(pathIndex); // path 가져오기
						XSSFCell minMandCell = row.getCell(minMandIndex); //MIN_MAND 가져오기
						String annoFieldCellStr = getCellValue(annoFieldCell);
						String annoAddCellStr = getCellValue(annoAddCell);
						String pathCellStr = getCellValue(pathCell);
						String minMandStr = getCellValue(minMandCell);
						if ( minMandStr.equals("false") ) minMandStr = "";
						if ( annoFieldCellStr != null && annoFieldCellStr.startsWith("false"))
							continue;
						PATH_MAP.put(getOnlyHangle(annoFieldCellStr)+"_"+rowNo, minMandStr +";"+pathCellStr);
						ADDINFO_MAP.put(getOnlyHangle(annoFieldCellStr)+"_"+rowNo, annoAddCellStr);
					}
				}
			}

		} catch (Exception e) {
			System.err.println(e.getMessage());
		} finally {
			try {
				if (file != null)
					file.close();
			} catch (Exception e) {
				//
			}
		}
	}

	public static String getFileName(String txCode) {
		FileInputStream file = null;
		String retValue = "";
		try {
			// 경로에 있는 파일을 읽
			file = new FileInputStream("files/(붙임)한은금융망 서버접속 전문설명서_v1.3.7_표준전문개발반송부용_매핑포함.xlsx");
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
//					System.out.println("파일 탐색 중 : " + getCellValue(cell1) + " : " + getCellValue(cell2));
					if (getCellValue(cell1).equals(txCode))
						retValue = getCellValue(cell2);
				}
			}

			XSSFSheet sheetTxCode = workbook.getSheet(txCode);
			rows = sheetTxCode.getPhysicalNumberOfRows();
			for (rowNo = 0; rowNo < rows; rowNo++) {
				XSSFRow row = sheetTxCode.getRow(rowNo);
				if (row != null) {
					XSSFCell cell1 = row.getCell(1); // 셀의 값을 가져온다
					XSSFCell cell2 = row.getCell(6); // 셀의 값을 가져온다
					if (cell1 == null || cell2 == null)
						continue;
					String colName = cell1.toString().trim();
					if (colName.length() == 0)
						continue;
					if (colName.equals("항목") || colName.equals("공통부"))
						continue;
					//System.out.println(colName + "\t:\t" + cell2);
					COL_MAP.put(getOnlyHangle(colName), cell2.toString());
				}
			}

		} catch (Exception e) {
			retValue = e.getMessage();
		} finally {
			try {
				if (file != null)
					file.close();
			} catch (Exception e) {
				//
			}
		}
		return retValue;
	}

	public static String getCellValue(XSSFCell cell) {
		String value = "";
		if (cell == null) { // 빈 셀 체크
			return "";
		} else {
			// 타입 별로 내용을 읽는다
			switch (cell.getCellType()) {
			case XSSFCell.CELL_TYPE_FORMULA:
				value = cell.getCellFormula();
				break;
			case XSSFCell.CELL_TYPE_NUMERIC:
				value = cell.getNumericCellValue() + "";
				break;
			case XSSFCell.CELL_TYPE_STRING:
				value = cell.getStringCellValue() + "";
				break;
			case XSSFCell.CELL_TYPE_BLANK:
				value = cell.getBooleanCellValue() + "";
				break;
			case XSSFCell.CELL_TYPE_ERROR:
				value = cell.getErrorCellValue() + "";
				break;
			}
		}
		return value;
	}
	
	/**
	 * 한글만 추출
	 * @param str
	 * @return
	 */
	public static String getOnlyHangle(String str){

		StringBuffer sb=new StringBuffer();
		if(str!=null && str.length()!=0){
			Pattern p = Pattern.compile("[가-힣|0-9|()]");
			Matcher m = p.matcher(str);
			while(m.find()){
				sb.append(m.group());
			}
		}
		return sb.toString();

	}
	
	/**
	 * 패턴 매칭
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String in){

		if(in!=null && in.length()!=0){
			Pattern p = Pattern.compile("[_|0-9]");
			Matcher m = p.matcher(in);
			while(m.find()){
				return true;
			}
		}
		return false;

	}
	
	
}