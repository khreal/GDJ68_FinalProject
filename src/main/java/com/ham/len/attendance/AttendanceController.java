package com.ham.len.attendance;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ham.len.humanresource.HumanResourceVO;
import com.ham.len.util.WeekOfMonthInfoCalculator;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/attendance/*")
public class AttendanceController {	
	@Autowired
	private AttendanceService attendanceService;
	
	@GetMapping("getServerDate")
	@ResponseBody
	public Date getServerDate() {
		return new Date();
	}
	
	private Map<String, Boolean> getCommuteWhether(String employeeId) {
		Map<String, Object> params = new HashMap<>();
		params.put("employeeId", employeeId);
		params.put("date", new Date());
		AttendanceVO attendanceVO = attendanceService.getAttendance(params);
		
		Map<String, Boolean> commuteWhether = new HashMap<>();
		if(attendanceVO == null && attendanceService.getLeaveWorkWhether(employeeId) == 0) {
			commuteWhether.put("goWork", false);
			commuteWhether.put("leaveWork", true);
		}else {
			commuteWhether.put("goWork", true);
			if(attendanceVO != null) {
				commuteWhether.put("leaveWork", (attendanceVO.getAttendanceEnd() != null));
			}else {
				commuteWhether.put("leaveWork", false);
			}
		}
		
		return commuteWhether;
	}
	
	@PostMapping("status")
	public String getStatus(@RequestParam(value = "year", defaultValue = "0")int year,
									@RequestParam(value = "month", defaultValue = "0")int month,
									@RequestParam(value = "day", required = false)Integer day,
									@AuthenticationPrincipal HumanResourceVO humanResourceVO, Model model) {
		
		Calendar cal = Calendar.getInstance(Locale.KOREA);
		int weekOfMonth = 0;
		if((year == 0 && month == 0) || (year == cal.get(Calendar.YEAR) && month - 1 == cal.get(Calendar.MONTH))) {
			year = cal.get(Calendar.YEAR);
			month = cal.get(Calendar.MONTH) + 1;
			Map<String, Integer> currentWeekOfMonth = WeekOfMonthInfoCalculator.getCurrentWeekOfMonth(cal.getTime());
			if(currentWeekOfMonth.get("year") == year && currentWeekOfMonth.get("month") == month) {
				weekOfMonth = currentWeekOfMonth.get("weekOfMonth");
			}
		}else if(day != null) {
			cal.set(Calendar.YEAR, year);
			cal.set(Calendar.MONTH, month - 1);
			cal.set(Calendar.DATE, day);
			Map<String, Integer> currentWeekOfMonth = WeekOfMonthInfoCalculator.getCurrentWeekOfMonth(cal.getTime());
			year = currentWeekOfMonth.get("year");
			month = currentWeekOfMonth.get("month");
			weekOfMonth = currentWeekOfMonth.get("weekOfMonth");
		}
		
		String[][] weeksOfMonthInfo = WeekOfMonthInfoCalculator.getWeeksOfMonthInfo(year, month);
		
		Map<String, String> params = new HashMap<>();
		String start_date = weeksOfMonthInfo[1][1].split(" ")[0].replaceAll("[ymd]", "-");
		String end_date = weeksOfMonthInfo[weeksOfMonthInfo.length - 1][7].split(" ")[0].replaceAll("[ymd]", "-");
		start_date = start_date.substring(0, start_date.length() - 1) + " " + "00:00:00";
		end_date = end_date.substring(0, end_date.length() - 1) + " " + "23:59:59";
		params.put("employeeID", humanResourceVO.getEmployeeID());
		params.put("start_date", start_date);
		params.put("end_date", end_date);
		
		List<AttendanceVO> attendances = attendanceService.getStatus(params);
		
		
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		
		Map<String, Integer> date = new HashMap<>();
		date.put("year", year);
		date.put("month", month);
		date.put("weekOfMonth", weekOfMonth);
		
		model.addAttribute("date", date);
		model.addAttribute("weeksOfMonthInfo", weeksOfMonthInfo);
		model.addAttribute("weeksOfMonthInfo_json", gson.toJson(weeksOfMonthInfo));
		model.addAttribute("attendances_json", gson.toJson(attendances));
		model.addAttribute("commuteWhether", getCommuteWhether(humanResourceVO.getEmployeeID()));
		
		return "attendance/status";
	}
	
	@PostMapping("goWork")
	@ResponseBody
	public Timestamp setGoWork(@AuthenticationPrincipal HumanResourceVO humanResourceVO, HttpServletRequest httpServletRequest) throws Exception {
		String employeeID = humanResourceVO.getEmployeeID();
		
		if(getCommuteWhether(employeeID).get("goWork") == false) {
			Timestamp goWorkDate = new Timestamp(new Date().getTime());
			
	        AttendanceVO attendanceVO = new AttendanceVO();
			attendanceVO.setEmployeeId(employeeID);
			attendanceVO.setAttendanceDate(goWorkDate);
			attendanceVO.setAttendanceStart(goWorkDate);
			
			attendanceService.setGoWork(attendanceVO);
			return goWorkDate;
		}
		
		return null;
	}
	
	@PostMapping("leaveWork")
	@ResponseBody
	public Timestamp setLeaveWork(@AuthenticationPrincipal HumanResourceVO humanResourceVO, HttpServletRequest httpServletRequest) throws Exception {
		String employeeID = humanResourceVO.getEmployeeID();
		
		if(getCommuteWhether(employeeID).get("leaveWork") == false) {
			Timestamp leaveWorkDate = new Timestamp(new Date().getTime());
	        
	        AttendanceVO attendanceVO = new AttendanceVO();
			attendanceVO.setEmployeeId(employeeID);
			attendanceVO.setAttendanceEnd(leaveWorkDate);
			
			attendanceService.setLeaveWork(attendanceVO);
			return leaveWorkDate;
		}
		
		return null;
	}
}