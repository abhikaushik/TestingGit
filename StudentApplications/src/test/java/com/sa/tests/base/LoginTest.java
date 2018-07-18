package com.sa.tests.base;

import org.testng.annotations.Test;

import com.sa.pages.common.LandingPage;

public class LoginTest extends BaseTest{
	@Test
	public void Testlogin() {

		landingpage = new LandingPage();
		loginpage = landingpage.clickCustomerloginBtn();
		loginpage.login("sunil.dalvi", "Weld@123");
	}
}
