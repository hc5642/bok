package com.bok.iso20022.mngr.svc;

import java.util.Calendar;
import java.util.Map;

public interface BokwireManagerSvc {
	
	public int [][] getCalendarTable(Calendar calendar, int year, int month) ;
	
	public Map<String, String> loadMap(String filePath) ;
	
	public int saveMap(String filePath, String key, String value);

}
