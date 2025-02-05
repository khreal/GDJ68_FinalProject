package com.ham.len;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ham.len.humanresource.HumanResourceDAO;
import com.ham.len.humanresource.HumanResourceService;
import com.ham.len.humanresource.HumanResourceVO;
import com.ham.len.transfer.TransferDAO;
import com.ham.len.transfer.TransferVO;
import com.ham.len.util.SMTP;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class Gdj68FinalProjectApplicationTests {
	@Autowired
	TransferDAO transferDAO;
	
	@Autowired
	HumanResourceService humanResourceService;
	
	@Autowired
	HumanResourceDAO humanResourceDAO;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	// @Test
	void contextLoads() {
		
	}
	
	// @Test
	void setTransfer() {
		Calendar cal = Calendar.getInstance(Locale.KOREA);
		cal.set(Calendar.YEAR, 2023);
		cal.set(Calendar.MONDAY, 0);
		
		for(int i = 1; i <= 100; i++) {
			cal.set(Calendar.DATE, i);
			
			TransferVO transferVO = new TransferVO();
			transferVO.setEmployeeId("2023001");
			transferVO.setName("테스트" + i);
			transferVO.setTransferDate(new Date(cal.getTimeInMillis()));
			transferVO.setTransferTypeCd("U013");
			transferVO.setBeforePositionCd("U004");
			transferVO.setTransferPositionCd("U003");
			transferVO.setBeforeDepartmentCd("D002");
			transferVO.setTransferDepartmentCd("D006");
			transferDAO.setTransfer(transferVO);
		}
	}
	
	@Test
	void setRegistrationHumanresource() throws Exception {
		for(int i = 1; i <= 100; i++) {
			HumanResourceVO humanResourceVO = new HumanResourceVO();
			humanResourceVO.setPassword(passwordEncoder.encode("1234"));
			humanResourceVO.setJoinDate(Date.valueOf("2023-11-07"));
			humanResourceVO.setJoinType((byte)0);
			humanResourceVO.setName("테스트" + i);
			humanResourceVO.setBirth(Date.valueOf("1995-07-28"));
			humanResourceVO.setDepartmentCd("D001");
			humanResourceVO.setPositionCd("U001");
			humanResourceVO.setYearsOfService(0);
			humanResourceVO.setPhone("010-0000-0000");
			humanResourceVO.setEmail("dngu_icdi@naver.com");
			humanResourceVO.setAddress("가나다라마바사");
			humanResourceVO.setBank("신한");
			humanResourceVO.setAccountNumber("123456789");
			humanResourceVO.setAccountHolder("김민진");
			humanResourceService.setRegistration(humanResourceVO, null);
			// humanResourceDAO.setRegistration(humanResourceVO);
		}
		
		/*
			log.info("before : {}", humanResourceVO.getEmployeeID());
			int result = humanResourceDAO.setRegistration(humanResourceVO);
			humanResourceVO.setEmployeeID(humanResourceDAO.getLatestEmployeeID());
			log.info("after : {}", humanResourceVO.getEmployeeID());
			
			new SMTP().send_mail(humanResourceVO);
			
			assertTrue(result > 0);
		*/
	}
	
	// @Test
	void generateTemporaryPassword() {
		String temporaryPassword = "";
		
		Random rand = new Random();
		for(int i = 0; i < 8; i++) {
			if(rand.nextInt(2) == 0) {
				temporaryPassword += rand.nextInt(10); // 0 ~ 9
			}else {
				temporaryPassword += (char)(rand.nextInt(26) + 97); // 97(a) ~ 122(z)
			}
		}
        
        log.info("temporaryPassword : {}" , temporaryPassword);
	}
}