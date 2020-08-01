package com.udacity.jwdnd.course1.cloudstorage.login;

import com.udacity.jwdnd.course1.cloudstorage.home.HomePage;
import com.udacity.jwdnd.course1.cloudstorage.signup.SignupPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginTests {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private LoginPage loginPage;
    private SignupPage signupPage;
    private HomePage homePage;
    private WebDriverWait webDriverWait;

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
	webDriverWait = new WebDriverWait(driver, 5);
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
    @Order(1)
    public void testLoginSuccessfulPostSignup() {
	loginPage.clickSignupLink();
	waitTwoSeconds();
	assertTrue(signupPage.isFirstNameFieldVisible());

	signupPage.signupUser("Sudhir",
		"Vaidya",
		"sudhirv89",
		"sudhir");
	waitTwoSeconds();
	signupPage.clickLoginLink();
	waitTwoSeconds();
	assertTrue(loginPage.isLoginPageUsernameDisplayed());

	loginPage.loginWithDefaultUser();
	waitTwoSeconds();

	assertTrue(homePage.isUploadButtonShown());
    }

    @Test
    @Order(2)
    public void testLoginInvalidUsernameShownPostSignup() {
	loginPage.loginUser("sudhirv89", "sudh");
	waitTwoSeconds();
	assertTrue(loginPage.isLoginErrorShown());
    }

    @Test
    @Order(3)
    public void testSuccessfulLogoutShowsLoginPage() {
	loginPage.loginWithDefaultUser();
	waitTwoSeconds();

	assertTrue(homePage.isUploadButtonShown());

	homePage.clickLogoutButton();
	waitTwoSeconds();

	assertTrue(loginPage.isLoginPageUsernameDisplayed());
    }

    private WebElement waitAndRetrieveWebElement(String s) {
	return webDriverWait.until(webDriver -> webDriver.findElement(By.id(s)));
    }

}
