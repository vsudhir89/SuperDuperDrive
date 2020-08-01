package com.udacity.jwdnd.course1.cloudstorage.home;

import com.udacity.jwdnd.course1.cloudstorage.login.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.signup.SignupPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CredentialsTest {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private LoginPage loginPage;
    private SignupPage signupPage;
    private CredentialsPage credentialsPage;
    private WebDriverWait webDriverWait;

    @BeforeAll
    static void beforeAll() {
	WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
	this.driver = new ChromeDriver();
	driver.get("http://localhost:" + this.port + "/signup");
	signupPage = new SignupPage(driver);
	loginPage = new LoginPage(driver);
	credentialsPage = new CredentialsPage(driver);
	webDriverWait = new WebDriverWait(driver, 5);
    }

    @AfterEach
    public void afterEach() {
	if (driver != null) {
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
    public void testCreateCredentialAndCredentialShownSuccessfully() {
	setupUserAndLandOnCredentialsTab();
	waitTwoSeconds();

	createCredential();

	credentialsPage.clickCredentialsTab();
	waitTwoSeconds();

	assertEquals("sudhir", credentialsPage.getCredentialUsernameText().getText());

	assertEquals("https://gmail.com", credentialsPage.getCredentialUrlText().getText());

	assertNotEquals("java web developer nanodegree", credentialsPage.getCredentialPasswordText().getText());
    }

    @Test
    @Order(3)
    public void testEditCredentialAndVerifyEditedSuccessful() {
	waitTwoSeconds();
	waitAndRetrieveWebElement("backToLoginLink").click();
	loginWithDefaultUserAndLandOnCredentialsTab();
	waitTwoSeconds();

	assertTrue(waitAndRetrieveWebElement("editCredentialButton").isDisplayed());

	waitAndRetrieveWebElement("editCredentialButton").click();
	waitTwoSeconds();

	waitAndRetrieveWebElement("credential-url").clear();
	waitAndRetrieveWebElement("credential-url").sendKeys("https://yahoo.com");

	credentialsPage.clickCredentialSaveChangesButton();
	waitTwoSeconds();

	waitAndRetrieveWebElement("nav-credentials-tab").click();
	waitTwoSeconds();

	assertEquals("https://yahoo.com", credentialsPage.getCredentialUrlText().getText());
    }

    @Test
    @Order(4)
    public void testDeleteCredentialAndVerifyItsNotShown() {
	waitTwoSeconds();
	waitAndRetrieveWebElement("backToLoginLink").click();

	loginWithDefaultUserAndLandOnCredentialsTab();
	waitTwoSeconds();

	assertTrue(waitAndRetrieveWebElement("deleteCredentialButton").isDisplayed());

	waitAndRetrieveWebElement("deleteCredentialButton").click();
	waitTwoSeconds();

	assertThrows(NoSuchElementException.class, () -> credentialsPage.getCredentialUrlText().isDisplayed());
    }


    private WebElement waitAndRetrieveWebElement(String s) {
	return webDriverWait.until(webDriver -> webDriver.findElement(By.id(s)));
    }

    private void setupUserAndLandOnCredentialsTab() {
	waitTwoSeconds();

	signupPage.signupWithDefaultUser();
	waitTwoSeconds();

	waitAndRetrieveWebElement("login-link").click();
	waitTwoSeconds();

	loginWithDefaultUserAndLandOnCredentialsTab();
    }

    private void loginWithDefaultUserAndLandOnCredentialsTab() {
	waitTwoSeconds();
	waitAndRetrieveWebElement("inputUsername").sendKeys("sudhirv89");

	waitTwoSeconds();

	waitAndRetrieveWebElement("inputPassword").sendKeys("sudhir");

	loginPage.getSubmitButton().click();
	waitTwoSeconds();

	waitAndRetrieveWebElement("nav-credentials-tab").click();
	waitTwoSeconds();
    }

    public void createCredential() {
	credentialsPage.clickAddCredentialButton();
	waitTwoSeconds();

	waitAndRetrieveWebElement("credential-url").sendKeys("https://gmail.com");
	waitTwoSeconds();

	waitAndRetrieveWebElement("credential-username").sendKeys("sudhir");
	waitTwoSeconds();

	waitAndRetrieveWebElement("credential-edit-password").sendKeys("java web developer nanodegree");
	waitTwoSeconds();

	credentialsPage.clickCredentialSaveChangesButton();
	waitTwoSeconds();

	waitAndRetrieveWebElement("credentialOperationModalCloseButton").click();
	waitTwoSeconds();
    }

}
