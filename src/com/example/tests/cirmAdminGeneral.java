package com.example.tests;


import static org.junit.Assert.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import javax.management.RuntimeErrorException;

import org.junit.internal.MethodSorter;
import junit.framework.Assert;
import sun.org.mozilla.javascript.internal.ast.ThrowStatement;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.server.RemoteControlConfiguration;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.Wait;


@SuppressWarnings("deprecation")
public class cirmAdminGeneral {
	//Variables for Post deploymnet tests
	//can be used across methods.
	private Selenium selenium;
//	private String site = "https://s0020269/";
//	private String site = "https://s0020284/html/startup.html#";
	private String site = "https://311hub.miamidade.gov/#";
//	private String site = "https://s0144654/html/startup.html";
//	private String site = "https://cirm.miamidade.gov/html/startup.html";
//	private String site = "https://cirm.311.miamidade.gov/html/login.html";
	private String loginUserID = "c203036";
	private String pass = "hahahaha147";
	private String longPwd = "something"; 
	private String recipients = "rajiv@miamidade.gov";
	private void ln (Object test){
		System.out.println(test);
	}
		
	//private String longPwd = "password";
	//private String pageLoadTime= "50000";
	public class SimpleOnFailed extends TestWatcher {
	    @Override
	    protected void failed(Throwable e, Description description) {
	    	ln("failed");
	     }
	}

	
	
	public static boolean isMyServerUp(){
		try {
			URL uri = new URL ("http://localhost:4444/wd/hub/status");
			HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
//			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept-Charset", "UTF-8");
			connection.getInputStream();
			int HttpResult = connection.getResponseCode();
		    if(HttpResult == HttpURLConnection.HTTP_OK) return true;
		    else return false;
		  
		}catch (Exception e){
			return false;
		} 		
	}
	
	private Thread myThread = new Thread() {
	    public void run() {
	        try {
	        	Process P = Runtime.getRuntime().exec("cmd /c start javaw -jar C:\\users\\angel.martin.MIAMIDADE\\Downloads\\selenium-server-standalone-2.53.1.jar -trustAllSSLCertificates");
	        	P.waitFor();
				System.out.println("Sucessfully started selenium server");
	        	
	        } catch(Exception e) {
	            System.out.println(e);
	        }
	    }  
	};
	 
	
	@Before
	public void startServer () throws Exception {
		myThread.start();

		int c = 0;
		do {
			Thread.sleep(1000);
			c++;
			} while (!isMyServerUp() && c < 10);
		
		if (c>10) throw new RuntimeException("Failure to contact selenium sever after ten attempts");
	 
		RemoteControlConfiguration settings = new RemoteControlConfiguration();
		settings.setTrustAllSSLCertificates(true);
		settings.setAvoidProxy(true);
		
		
		
		selenium = new DefaultSelenium("localhost", 4444, "*googlechrome C:/Program Files (x86)/Google/Chrome/Application/chrome.exe" , site);              
         
		
        selenium.start();
	}
	@Test
	public void login() throws Exception {
		try{
			selenium.open(site);
			selenium.type("id=iUsername", loginUserID);
			selenium.type("id=iPassword", pass);
			selenium.click("id=btnLogin");
			Thread.sleep(8000);
			selenium.isTextPresent("Popular Searches");
		}catch (Exception e){
            System.out.println(e);
            SendEmail.send("angel.martin@miamidade.gov", "Login has failed", "**login test has failed**<br><br>Screen shot on failure can be found at File:///C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/Login.png<br><br><br>To manually test this follow the steps below<br>* Open Chorme and navigate to the CiRM application<br>* Fill in the User and Password boxes<br>* Then click the Login button and wait 8-10 seconds for the application to load if the page loads the test has passed<br><br>"+e.getMessage());
            selenium.captureScreenshot("C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/Login.png");
            Assert.fail();
        }}
	@Test
	public void ValidateAddress() throws Exception {
		try{
		login();
		selenium.click("//input[@value='']");
		selenium.type("//input[@value='']", "9920 sw 73rd st");
		selenium.click("//input[@value='Search']");
		Thread.sleep(4000);
		selenium.isVisible("css=#answer_hub > div:nth-child(1) > span > input.ic_valid.button_icon.visibility_visible");
		}catch (Exception e){
            System.out.println(e);
            selenium.captureScreenshot("C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/Validateaddress.png");
            SendEmail.send("angel.martin@miamidade.gov", "Validate address has failed", "**Validate address test has failed**<br><br>Screen shot on failure can be found at File:///C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/Validateaddress.png<br><br><br>To manually test this follow the steps below<br>* Open Chorme and navigate to the CiRM application<br>* Fill in the User and Password boxes<br>* Then click the Login button and wait 8-10 seconds for the application to load<br>* Once you enter the application type a valid address in the address box<br>* To submit address for validation click the magnifiying glass<br>* Once you click the magnifiying glass there should be a green box that appears with a check-mark this signifies that the address has been validated and the test has passed<br><br>"+e.getMessage());
            Assert.fail();}}
    

	@Test
	public void OpenSRInAnswerHub() throws Exception {
		try{
		ValidateAddress();
		selenium.type("xpath=(//input[@type='text'])[6]", "Bulky");
		selenium.click("css=#answer_hub > div:nth-child(5) > span > input.submit.h32.button.blue");
		String Address = selenium.getText("css=#answer_hub > div:nth-child(1) > span > input.ic_field.h24.address_reset.color_green");
		selenium.click("link=BULKY TRASH REQUEST");
		Thread.sleep(3000);
		selenium.click("css=body > div:nth-child(13) > div.ui-dialog-buttonpane.ui-widget-content.ui-helper-clearfix > div > button:nth-child(1) > span");
		selenium.isTextPresent(Address);
		}catch (Exception e){
            System.out.println(e);
            selenium.captureScreenshot("C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/OpenSrInAnswerHub.png");
            SendEmail.send("angel.martin@miamidade.gov", "Open SR In AnswerHub test failed", "**Open SR In AnswerHub test has failed**<br><br>Screen shot on failure can be found at File:///C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/Validateaddress.png<br><br><br>To manually test this follow the steps below<br>* Open Chorme and navigate to the CiRM application<br>* Fill in the User and Password boxes<br>* Then click the Login button and wait 8-10 seconds for the application to load<br>* Once you enter the application type a valid address in the address box, then click the magnifiying glass to validate address<br>* Then in key words section typ the name of a service request and search it by clicking magnifiying glass on right<br>* When results load look to the right and click one of the service request listed<br>* Once you click the service request you hould be taken to service hub and the address should carry over aswell if this all is correct test has passed<br><br>"+e.getMessage());
            Assert.fail();
        }}	
	@Test
	public void ValidateinWCS() throws Exception {
		try{
		login();
		selenium.type("css=#answer_hub > div:nth-child(1) > span > input.ic_field.h24.address_reset", "9910 sw 73rd st");
		selenium.click("css=#answer_hub > div:nth-child(1) > span > input.submit.h32.button.blue");
		Thread.sleep(5000);
		selenium.click("css=#wcsTabLi > a");
		selenium.click("css=#wcs_right > input:nth-child(1)");
		Thread.sleep(1000);
		selenium.isTextPresent("11558262");
		}catch (Exception e){
			System.out.println(e);
			selenium.captureScreenshot("C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/ValidateWCS.png");
			SendEmail.send("angel.martin@miamidade.gov", "Validate WCS Failed", "**Validate WCS Failed**<br><br><br><br>Screen shot on failure can be found at File:///C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/Validatewcs.png<br><br><b>* To manually test this follow the steps below</b><br>* Login into the application<br>* In the address box validate an address and wait for the green box to appear<br>* Then click the WCS tab above<br>* When you get taken to the WCS section the validated address should have been transfered over <br>* Next click the Validate in wcs button<br>* Once u click the validate button assure WCS account information is pulled up if this is true test has passed<br><br> "+e.getMessage());
			Assert.fail();
		}}	
	@Test
	public void MasterClr() throws Exception {
		try{
		OpenSRInAnswerHub();
		selenium.click("css=textarea.tooltip");
		selenium.type("css=textarea.tooltip", "test");
		selenium.click("css=div.grid_3.alpha > input");
		selenium.type("css=div.grid_3.alpha > input", "tester");
		selenium.type("css=div.grid_3.omega > input", "last tester");
		selenium.click("//div[@id='sr_details']/span/div[7]/input");
		selenium.type("//div[@id='sr_details']/span/div[7]/input", "test@test.com");
		selenium.click("css=span > div.grid_2 > input..error");
		selenium.type("css=span > div.grid_2 > input", "3051111111");
		selenium.click("link=WCS");
		selenium.click("css=#wcs_right > input.button.blue");
		Thread.sleep(8000);
		selenium.click("id=sendToSRButtonID");
		selenium.click("css=select..error");
		selenium.select("css=div.input_margins > div.grid_5.alpha > select", "label=No");
		selenium.click("xpath=(//button[@type='button'])[3]");
		selenium.click("//div[@id='sr_details']/div[23]/div/div[10]/select");
		selenium.select("//div[@id='sr_details']/div[23]/div/div[10]/select", "label=No");
		selenium.click("//div[@id='sr_details']/div[23]/div/div[14]/select");
		selenium.select("//div[@id='sr_details']/div[23]/div/div[14]/select", "label=Yes");
		selenium.click("//div[@id='sr_details']/div[23]/div/div[18]/select");
		selenium.select("//div[@id='sr_details']/div[23]/div/div[18]/select", "label=Yes");
		selenium.select("//div[@id='sr_details']/div[23]/div/div[22]/select", "label=FR (Front of)");
		selenium.select("//div[@id='sr_details']/div[23]/div/div[26]/select", "label=Yes");
		selenium.click("xpath=(//button[@type='button'])[3]");
		selenium.click("//div[@id='sr_details']/div[23]/div/div[26]/select");
		selenium.click("//div[@id='sr_details']/div[23]/div/div[30]/select");
		selenium.select("//div[@id='sr_details']/div[23]/div/div[30]/select", "label=No");
		selenium.click("css=input.tooltip.error");
		selenium.type("css=input.tooltip", "3331111111");
		selenium.click("//div[@id='sr_details']/div[23]/div/div[38]/select");
		selenium.select("//div[@id='sr_details']/div[23]/div/div[38]/select", "label=Yes");
		selenium.click("//input[contains(@id,'dp')]");
		selenium.type("//input[contains(@id,'dp')]", "04/15/2015");
		selenium.addSelection("css=select[title=\"To select multiple options - press CTRL and select the options you wish to.\"]", "label=Tree Cuttings");
		selenium.click("css=option[value=\"http://www.miamidade.gov/cirm/legacy#BULKYTRA_WHATTYPE_BTPTYPE_BTPTREE\"]");
		selenium.click("xpath=(//button[@type='button'])[3]");
		selenium.click("//div[@id='sr_details']/div[23]/div/div[50]/input");
		selenium.type("//div[@id='sr_details']/div[23]/div/div[50]/input", "3305111234");
		selenium.click("css=#sr_details > div.grid_2.omega > input.button.blue");
		selenium.click("xpath=(//button[@type='button'])[5]");
		selenium.click("css=#sr_details > div:nth-child(2) > input");
		selenium.click("css=body > div:nth-child(15) > div.ui-dialog-buttonpane.ui-widget-content.ui-helper-clearfix > div > button:nth-child(1) > span");
		assertFalse(selenium.isTextPresent("Contact Infromation"));
		}catch (Exception e){
            System.out.println(e);
            selenium.captureScreenshot("C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/MasterCLR.png");
            SendEmail.send("angel.martin@miamidade.gov", "Master CLR Failed", "**Master CLR**<br><br>"+e.getMessage());
            Assert.fail();
        }}
 	@Test	
 	public void SaveSr() throws Exception {
//		try{
 		OpenSRInAnswerHub();
		selenium.click("css=textarea.tooltip");
		selenium.type("css=textarea.tooltip", "test");
		selenium.click("css=div.grid_3.alpha > input");
		selenium.type("css=div.grid_3.alpha > input", "tester");
		selenium.type("css=div.grid_3.omega > input", "last tester");
		selenium.click("//div[@id='sr_details']/span/div[7]/input");
		selenium.type("//div[@id='sr_details']/span/div[7]/input", "test@test.com");
		selenium.click("css=span > div.grid_2 > input..error");
		selenium.type("css=span > div.grid_2 > input", "3051111111");
		selenium.click("link=WCS");
		selenium.click("css=#wcs_right > input.button.blue");
		Thread.sleep(8000);
		selenium.click("id=sendToSRButtonID");
		selenium.click("css=select..error");
		selenium.select("css=div.input_margins > div.grid_5.alpha > select", "label=No");
		selenium.click("xpath=(//button[@type='button'])[3]");
		selenium.click("//div[@id='sr_details']/div[23]/div/div[10]/select");
		selenium.select("//div[@id='sr_details']/div[23]/div/div[10]/select", "label=No");
		selenium.click("//div[@id='sr_details']/div[23]/div/div[14]/select");
		selenium.select("//div[@id='sr_details']/div[23]/div/div[14]/select", "label=Yes");
		selenium.click("//div[@id='sr_details']/div[23]/div/div[18]/select");
		selenium.select("//div[@id='sr_details']/div[23]/div/div[18]/select", "label=Yes");
		selenium.select("//div[@id='sr_details']/div[23]/div/div[22]/select", "label=FR (Front of)");
		selenium.select("//div[@id='sr_details']/div[23]/div/div[26]/select", "label=Yes");
		selenium.click("xpath=(//button[@type='button'])[3]");
		selenium.click("//div[@id='sr_details']/div[23]/div/div[26]/select");
		selenium.click("//div[@id='sr_details']/div[23]/div/div[30]/select");
		selenium.select("//div[@id='sr_details']/div[23]/div/div[30]/select", "label=No");
		selenium.click("css=input.tooltip.error");
		selenium.type("css=input.tooltip", "3331111111");
		selenium.click("//div[@id='sr_details']/div[23]/div/div[38]/select");
		selenium.select("//div[@id='sr_details']/div[23]/div/div[38]/select", "label=Yes");
		selenium.click("//input[contains(@id,'dp')]");
		selenium.type("//input[contains(@id,'dp')]", "04/15/2015");
		selenium.addSelection("css=select[title=\"To select multiple options - press CTRL and select the options you wish to.\"]", "label=Tree Cuttings");
		selenium.click("css=option[value=\"http://www.miamidade.gov/cirm/legacy#BULKYTRA_WHATTYPE_BTPTYPE_BTPTREE\"]");
		selenium.click("xpath=(//button[@type='button'])[3]");
		selenium.click("//div[@id='sr_details']/div[23]/div/div[50]/input");
		selenium.type("//div[@id='sr_details']/div[23]/div/div[50]/input", "3305111234");
		selenium.click("css=#save");
		selenium.getValue("css=#editorDiv > div.app_container > div.right_column.grid_2 > span > input.search");
		System.out.println(selenium.getValue("css=#editorDiv > div.app_container > div.right_column.grid_2 > span > input.search"));
		selenium.getValue("css=#editorDiv > div.app_container > div.right_column.grid_2 > span > input.search");
//		}catch (Exception e){
//            System.out.println(e);
//            selenium.captureScreenshot("C://Users/angel.martin.MIAMIDADE/Desktop/failed test/SaveSr.png");
//            SendEmail.send("angel.martin@miamidade.gov", "test", e.getMessage());
//            Assert.fail();
//        }
		} 	
	@Test	
 	public void OpenSrBasicSearch() throws Exception {
 		try{
 		login();
 		selenium.click("link=Basic Search");
 		selenium.click("name=ServiceRequestType");
		selenium.type("name=ServiceRequestType", "BULKY TRASH REQUEST - MD");
		selenium.click("id=createdStartDate");
		selenium.type("id=createdStartDate", "-6");
		selenium.click("css=#advSearch_right > input[name=\"search\"]");
		Thread.sleep(6000);
		selenium.click("css=#advSearchResults > table > tbody > tr:nth-child(1) > td:nth-child(1) > a");
		selenium.click("css=body > div:nth-child(11) > div.ui-dialog-buttonpane.ui-widget-content.ui-helper-clearfix > div > button:nth-child(1) > span");
		Thread.sleep(5000);
		System.out.println(selenium.getValue("css=#editorDiv > div.app_container > div.right_column.grid_2 > span > input.search"));
		selenium.getValue("css=#editorDiv > div.app_container > div.right_column.grid_2 > span > input.search");
 		}catch (Exception e){
            System.out.println(e);
            selenium.captureScreenshot("C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/OpenSrBasicSearch.png");
            SendEmail.send("angel.martin@miamidade.gov", "Open Sr Basic Search Failed", "**Open Sr Basic Search Has Failed**<br><br><br><br>Screen shot on failure can be found at File://W203-MARTIN1/Users/angel.martin.MIAMIDADE/Desktop/Failedtest<br><br><b>To manually test this follow the steps below</b><br>* Login into the application<br>* Next click the basic search tab above<br>* Then select a Sr type and a date range<br>* Next click search button you should have some cases return if not extend date range<br>* To open a case click the Sr Id you will be prompted with a pop titled Select one option click view activites<br>* Where you should be taken into the service case assure that the sr id is the same one you clicked if this comes back true the test has passed<br><br> "+e.getMessage());
            Assert.fail();
        }}
 	@Test
	public void FeildSort() throws Exception {
		try{
		selenium.open(site);
		Thread.sleep(8000);
		selenium.type("id=iUsername", "c203036");
		selenium.type("id=iPassword", pass);
		selenium.click("id=btnLogin");
		Thread.sleep(8000);
		selenium.click("css=body > div.container_12 > div.banner.grid_12 > ul > li:nth-child(6) > a");
		selenium.type("name=ServiceRequestType", "POTHOLE - MD");
		selenium.click("id=showMoreFieldsId");
		selenium.select("css=#advsearch_moreDetails > div:nth-child(3) > select", "Locked");
		selenium.type("id=createdStartDate", "-1");
		selenium.click("css=#advSearch_right > input:nth-child(1)");
		Thread.sleep(8000);
		String sr1 = selenium.getText("css=#advSearchResults > table > tbody > tr:nth-child(1) > td:nth-child(1) > a");
		String[] parts = sr1.split("-");
		ln("Sr1= " + parts[1]);
		int sr01 = Integer.parseInt(parts[1]);
		selenium.click("css=#advSearchResults > table > thead > tr > th:nth-child(1) > img");
		String sr2 = selenium.getText("css=#advSearchResults > table > tbody > tr:nth-child(1) > td:nth-child(1) > a");
		parts = sr2.split("-");
		int sr02 = Integer.parseInt(parts[1]);
		ln("Sr2= "+sr02);
		int test= sr01-sr02; 
		if(test >0 ) {
						ln("SUCCESSFUL SORT IS OPERATIONAL.");
		}
		else{ 
			ln("FAIL!!! SORT DID NOT WORK");
	        throw new RuntimeException("ERROR: Sort functionality did not work");
			} 
		ln("FeildSort= Done");	
		}catch (Exception e){
          System.out.println(e);
          selenium.captureScreenshot("C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/FeildSort.png");
          SendEmail.send("angel.martin@miamidade.gov", "Feild Sort Failed", "**Feild Sort Has Failed**<br><br><br><br>Screen shot on failure can be found at File://W203-MARTIN1/Users/angel.martin.MIAMIDADE/Desktop/Failedtest/OpenSrBasicSearch.png<br><br><b>To manually test this follow the steps below</b><br>* Login into the application<br>* Next click the basic search tab above<br>* Then select a Sr type and a date range<br>* Next click search button you should have some cases return if not extend date range<br>* Now to test sort we will test the sort Sr ID to do this click the sort icon next to the Sr ID title if numbers change to ascending then the test has passed<br><br> "+e.getMessage());
          Assert.fail();
        }}
    @Test
 	public void ViewReport() throws Exception {
 		try{
 		login();
 		selenium.click("link=Basic Search");
		selenium.click("name=ServiceRequestType");
		selenium.type("name=ServiceRequestType", "BULKY TRASH REQUEST - MD");
		selenium.click("id=createdStartDate");
		selenium.type("id=createdStartDate", "-1");
		selenium.click("id=showMoreFieldsId");
		selenium.select("css=#advsearch_moreDetails > div:nth-child(3) > select", "Locked");
		selenium.click("css=#advSearch_right > input[name=\"search\"]");
		Thread.sleep(3000);
		selenium.click("css=#advSearchResults > table > tbody > tr:nth-child(1) > td:nth-child(1) > a");
		selenium.click("css=body > div:nth-child(11) > div.ui-dialog-buttonpane.ui-widget-content.ui-helper-clearfix > div > button:nth-child(2) > span");
		Thread.sleep(2000);
		String[] names = selenium.getAllWindowNames();
		String windowName = null;
			windowName = names[1];
			selenium.selectWindow(names[1]);
			(windowName).equalsIgnoreCase("frompop_1461961429332");
		} catch (Exception e){
			System.out.println(e);
			selenium.captureScreenshot("C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/ViewReport.png");
			SendEmail.send("angel.martin@miamidade.gov", "Open Sr Basic Search Failed", "**Open Sr Basic Search Has Failed**<br><br><br><br>Screen shot on failure can be found at File://W203-MARTIN1/Users/angel.martin.MIAMIDADE/Desktop/Failedtest<br><br><b>To manually test this follow the steps below</b><br>* Login into the application<br>* Next click the basic search tab above<br>* Then select a Sr type and a date range<br>* Next click search button you should have some cases return if not extend date range<br>* To open a report click the Sr Id you will be prompted with a pop-up titled Select one option click view report<br>* Then you should be taken to a new pop-up titled printview once you make it here the test has passed<br><br> "+e.getMessage());
			Assert.fail();
		}}

 	@Test
 	public void Duplicate() throws Exception {
 		try{
 		login();
 		selenium.click("css=body > div.container_12 > div.banner.grid_12 > ul > li:nth-child(6) > a");
 		Thread.sleep(250);
 		selenium.type("css=#srTypeListAdvSearch > input", "BULKY TRASH REQUEST - MD");
 		selenium.type("id=createdStartDate", "-2");
 		selenium.click("css=#advSearch_right > input:nth-child(1)");
 		Thread.sleep(4500);
 		String Address = selenium.getText("css=#advSearchResults > table > tbody > tr:nth-child(1) > td:nth-child(3)");
 		selenium.click("css=body > div.container_12 > div.banner.grid_12 > ul > li:nth-child(2) > a");
 		ln(Address);
 		Thread.sleep(500);
 		selenium.type("id=srTypeID", "BULKY TRASH REQUEST - MD");
 		selenium.click("css=#srTypeList > span > input.submit.h23_submit.button.blue");
 		selenium.click("css=body > div:nth-child(11) > div.ui-dialog-buttonpane.ui-widget-content.ui-helper-clearfix > div > button:nth-child(1)");
 		selenium.type("css=#sr_details > div:nth-child(8) > span > input.ic_field.h24.error", Address);
 		selenium.click("css=#sr_details > div:nth-child(8) > span > input.submit.h32.button.blue");
 		Thread.sleep(8000);
 		if (selenium.isVisible("css=#sr_details_right > input.red_button.button.blue")==true){
		ln("nice");
 		} else {
 			throw new RuntimeException("ERROR: Related SRs Button not found during test");
 				}
 		}catch (Exception e){
            System.out.println(e);
            selenium.captureScreenshot("C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/Duplicate.png");
            SendEmail.send("angel.martin@miamidade.gov", "Duplicate Failed", "**Duplicate Has Failed**<br><br><br><br>Screen shot on failure can be found at File://W203-MARTIN1/Users/angel.martin.MIAMIDADE/Desktop/Failedtest<br><br><b>To manually test this follow the steps below</b><br>* Login into the application<br>* Next click the basic search tab above<br>* Then select a Sr type and a date range<br>* Next click search button you should have some cases return if not extend date range<br>* When u get Service Request cases back write sown the address and SR type<br>* Next click the Service hub tab above<br>* Next take the service request type and address you noted down earlier and imput it into the sr feilds<br>* When you click to validate address the Related SRs button should appear above save button<br>* If this is true pass has passed<br><br> "+e.getMessage());
            Assert.fail();
        }}
 	@Test
 	public void OutofServiceArea() throws Exception {
 		try{
 		login();
 		selenium.click("link=Service Hub");
		selenium.click("id=srTypeID");
		selenium.type("id=srTypeID", "BOAT STORAGE - MD");
		selenium.click("css=#srTypeList > span > input.submit.h23_submit.button.blue");
		selenium.type("css=#sr_details > div.grid_5.alpha > span.input_clear > input.ic_field.h24", "1022 adams drive");
		selenium.click("xpath=(//input[@value='Search'])[6]");
		Thread.sleep(8000);
		selenium.getText("css=#ui-dialog-title-sh_dialog_type_invalid");
		String out = selenium.getText("css=#ui-dialog-title-sh_dialog_type_invalid");
		ln(out);
//		assertTrue(out = "Outside Service Area.", false);
		
		if (selenium.isVisible("css=#sh_dialog_type_invalid"))
		{
		ln("nice everything worked");	
		} else {
			throw new RuntimeException("ERROR: After 8sec's of address validation out of service area did not prompt");
		}
		}catch (Exception e){
            System.out.println(e);
            selenium.captureScreenshot("C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/OutofServiceArea.png");
            SendEmail.send("angel.martin@miamidade.gov", "Out of Service Area Failed", "**Out Service Area Has Failed**<br><br><br><br>Screen shot on failure can be found at File://W203-MARTIN1/Users/angel.martin.MIAMIDADE/Desktop/Failedtest<br><br><b>To manually test this follow the steps below</b><br>* Login into the application<br>* Next click the Service Hub tab above<br>* In Service Hub pick service request type Boat Storage - MD & fill in address feild with 1022 adams drive then validate<br>* You should now be prompted by a pop-up window titled Outside Service Area<br>"+e.getMessage());
            Assert.fail();
        }} 	
 	
	@Test
 	public void apporvalProcess() throws Exception {
 		try{
 		selenium.open(site);
 		selenium.type("id=iUsername", "c203036");
		selenium.type("id=iPassword", pass);
		selenium.click("id=btnLogin");
		Thread.sleep(8000);
 		selenium.click("link=Basic Search");
 		Thread.sleep(3000);
 		selenium.click("id=showMoreFieldsId");
 		selenium.click("css=#advsearch_moreDetails > div.grid_2 > select.f_left");
 		selenium.type("id=createdStartDate", "-5");
		selenium.select("css=#advsearch_moreDetails > div.grid_2 > select.f_left", "label=Pending");
		selenium.click("css=#advSearch_right > input:nth-child(1)");
		Thread.sleep(5500);
		selenium.click("css=#advSearchResults > table > tbody > tr:nth-child(3) > td:nth-child(1) > a");
		selenium.click("css=body > div:nth-child(11) > div.ui-dialog-buttonpane.ui-widget-content.ui-helper-clearfix > div > button:nth-child(1) > span");
		Thread.sleep(5000);
		selenium.isTextPresent("The following Service Request has been identified as a self-service request and is 'Pending Approval'. Please review the request and make appropriate changes. When complete, set the status to 'Open' then save.");
 		}catch (Exception e){
            System.out.println(e);
            selenium.captureScreenshot("C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/ApprovalProcess.png");
            SendEmail.send("angel.martin@miamidade.gov", "Approval Process", "**Aprroval Process Has Failed**<br><br><br><br>Screen shot on failure can be found at File://W203-MARTIN1/Users/angel.martin.MIAMIDADE/Desktop/Failedtest<br><br><b>To manually test this follow the steps below</b><br>* Login into the application<br>* Next click the basic search tab above<br>* Then click SHOW Advanced Search Fields and select the status as pending<br>* Then click search and wait for cases to returnwhen they do click on the SR ID and select view activites<br>* when SR loads you should be prommpted with a service request alert stating the case is still pending approval if this happens test has passed<br><br> "+e.getMessage());
            Assert.fail();
        }}
// 	@Test
	public void message() throws Exception {
		SendEmail.send("nijat@miamidade.gov","Test", "Test");
	}
	@After
	public void tearDown() throws Exception {
//	 selenium.stop();
//	 selenium.shutDownSeleniumServer();
//	 ln("server successfully shut down.");
	
	}
}
