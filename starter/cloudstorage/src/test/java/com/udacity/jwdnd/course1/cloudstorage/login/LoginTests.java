package com.udacity.jwdnd.course1.cloudstorage.login;

import com.udacity.jwdnd.course1.cloudstorage.home.HomePage;
import com.udacity.jwdnd.course1.cloudstorage.signup.SignupPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginTests {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private LoginPage loginPage;
    private SignupPage signupPage;
    private HomePage homePage;

    @BeforeAll
    static void beforeAll() {
	WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
	this.driver = new ChromeDriver();
	driver.get("http://localhost:" + this.port + "/login");
	loginPage = new LoginPage(driver);
	signupPage = new SignupPage(driver);
	homePage = new HomePage(driver);
    }

    @AfterEach
    public void afterEach() {
	if (this.driver != null) {
	    driver.quit();
	}
    }

    private void waitTwoSeconds() {
	try {
	    Thread.sleep(2000);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
    }

    @Test
    public void testLoginSuccessfulPostSignup() {
        loginPage.clickSignupPage();
        waitTwoSeconds();
        assertTrue(signupPage.isFirstNameFieldVisible());

	signupPage.signupUser("Sudhir",
		"Vaidya",
		"sudhirv89",
		"sudhir");
	waitTwoSeconds();
	signupPage.clickLoginButton();
	assertTrue(loginPage.isLoginPageUsernameDisplayed());

	loginPage.loginWithDefaultUser();
	waitTwoSeconds();

	assertTrue(homePage.isUploadButtonShown());
    }

    @Test
    public void testLoginUnsuccessfulPostSignup() {
	loginPage.clickSignupPage();
	waitTwoSeconds();
	signupPage.signupUser("Sudhir",
		"Vaidya",
		"sudhirv89",
		"sudhir");
	waitTwoSeconds();
	signupPage.clickLoginButton();
	loginPage.loginUser("sudhirv89", "sudh");
	waitTwoSeconds();

	assertTrue(loginPage.isLoginErrorShown());
    }


}
