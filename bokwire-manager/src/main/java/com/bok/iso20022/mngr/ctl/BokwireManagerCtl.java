package com.bok.iso20022.mngr.ctl;

import java.util.Calendar;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bok.iso20022.mngr.svc.BokwireManagerSvc;

@Controller
@RequestMapping("/manager")
public class BokwireManagerCtl {
	
	@Autowired
	private BokwireManagerSvc svc;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());	
	
	private final String calendarPath = "./calendar.";
	
	/**
	 * 캘린더 새로운 버전
	 * @param name
	 * @param year
	 * @param month
	 * @param model
	 * @return
	 */
	@GetMapping("/calendar/{name}")
	public String calelndar2(
			@PathVariable(value="name") String name,
			@RequestParam(value="year", required=false) String year,
			@RequestParam(value="month", required=false) String month,
			@RequestParam(value="startDay", required=false, defaultValue="0") int startDay,
			@RequestParam(value="filterKey", required=false) String filterKey,
			HttpServletRequest request, HttpServletResponse response, 
			Model model) {
		String remoteIp = request.getRemoteAddr();
		logger.info("---------------------------------------");
		logger.info("--- APP NAME : /calendar/" + name);
		logger.info("--- DEFAULT PARAM [year] = ["+year+"]");
		logger.info("--- DEFAULT PARAM [month] = ["+month+"]");
		logger.info("--- DEFAULT PARAM [startDay] = ["+startDay+"]");
		logger.info("--- INPUT PARAM : [filterKey]=["+filterKey+"]");
		logger.info("--- ACCESS IP : " + remoteIp);
		Calendar cal = Calendar.getInstance();
		int yearInt = cal.get(Calendar.YEAR);
		int monthInt = cal.get(Calendar.MONTH)+1;
		int dayInt = cal.get(Calendar.DAY_OF_MONTH);
		if ( year != null && month != null )  {
			yearInt = Integer.parseInt(year);
			monthInt = Integer.parseInt(month);
			if ( monthInt == 13 )  {
				yearInt += 1;
				monthInt = 1;
			} else if ( monthInt == 0 )  {
				yearInt -= 1;
				monthInt = 12;
			}
		}
		model.addAttribute("yearInt", yearInt);
		model.addAttribute("monthInt", monthInt);
		model.addAttribute("dayInt", dayInt);
		model.addAttribute("name", name);
		model.addAttribute("dayTable", svc.getCalendarTable(cal, yearInt, monthInt));
		logger.info("--- calendar path : " + calendarPath + name+"."+yearInt+".dat");
		Map<String, String> result = svc.loadMap(calendarPath + name+"."+yearInt+".dat");
		if ( filterKey != null && filterKey.trim().length() > 0 ) {
	 		String temp = "";
			String [] lines = null;
			StringBuffer tempResult = null;
			for ( String key : result.keySet() ) {
				temp = result.get(key);
				
				temp = temp.replaceAll("<br>", "\n");
				temp = temp.replaceAll("<br/>", "\n");
				temp = temp.replaceAll("<br />", "\n");
				temp = temp.replaceAll("\t", "");
				temp = temp.replaceAll("<div", "\n<div");
				temp = temp.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
				temp = temp.replaceAll("\n\n", "\n");
				
				lines = temp.split("\n");
				tempResult = new StringBuffer();
				for ( String line : lines ) {
					if ( line.trim().length() == 0 ) continue;
					if ( line.contains(filterKey) ) {
						tempResult.append("<span style='background-color: #ffeedd;'>" + line + "</span><br/>");
					} else {
						tempResult.append("<span style='color: #CCCCCC;'>" + line + "</span><br/>");
					}
				}
				result.put(key, tempResult.toString());
			}
		}
		model.addAttribute("contents", result);
		model.addAttribute("startDay", startDay);
		model.addAttribute("filterKey", filterKey);
		logger.info("---------------------------------------");
		return "calendar/calendar";
	}
	
	/**
	 * 캘린더 새로운 버전
	 * @param name
	 * @param year
	 * @param month
	 * @param model
	 * @return
	 */
	@PostMapping("/calendar/{name}")
	public String calelndarPost2(
			@PathVariable(value="name") String name,
			@RequestParam(value="year") String year,
			@RequestParam(value="month") String month,
			@RequestParam(value="key") String key,
			@RequestParam(value="value") String value,
			@RequestParam(value="startDay", required=false) String startDay,
			HttpServletRequest request, HttpServletResponse response, 
			Model model) {
		String remoteIp = request.getRemoteAddr();
		logger.info("---------------------------------------");
		logger.info("--- APP NAME : /calendar/" + name);
		logger.info("--- DEFAULT PARAM [year] = ["+year+"]");
		logger.info("--- DEFAULT PARAM [month] = ["+month+"]");
		logger.info("--- INPUT PARAM : key=["+key+"], value=["+value.trim()+"]");
		logger.info("--- ACCESS IP : " + remoteIp);
		Calendar cal = Calendar.getInstance();
		int yearInt = Integer.parseInt(year);
		int monthInt = Integer.parseInt(month);
		int dayInt = cal.get(Calendar.DAY_OF_MONTH);
		String filePath = calendarPath+name+"."+yearInt+".dat";
		logger.info("--- SAVE RESULT : " + svc.saveMap(filePath, key, value));
		model.addAttribute("yearInt", yearInt);
		model.addAttribute("monthInt", monthInt);
		model.addAttribute("dayInt", dayInt);
		model.addAttribute("name", name);
		model.addAttribute("dayTable", svc.getCalendarTable(cal, yearInt, monthInt));
		model.addAttribute("contents", svc.loadMap(filePath));
		model.addAttribute("startDay", startDay);
		logger.info("---------------------------------------");
		return "calendar/calendar";
	}
	
	@RequestMapping("/hello")
	public String hello() {
		return "index";
	} 
	

}
