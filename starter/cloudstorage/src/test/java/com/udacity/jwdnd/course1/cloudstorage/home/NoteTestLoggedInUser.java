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
public class NoteTestLoggedInUser {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private LoginPage loginPage;
    private SignupPage signupPage;
    private NoteTabPage noteTabPage;
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
	noteTabPage = new NoteTabPage(driver);
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
    public void testCreateNoteShown() {
	setupUserAndLandOnNotesTab();
        waitTwoSeconds();

        createNote();

	noteTabPage.clickNotesTab();
	waitTwoSeconds();

	waitAndRetrieveWebElement("note-title");

	assertEquals(noteTabPage.getNoteTitleText().getText(), "First Note");
    }

    @Test
    @Order(2)
    public void testCreatedNoteEditTitleShown() {
	waitTwoSeconds();
	waitAndRetrieveWebElement("backToLoginLink").click();
	loginWithDefaultUser();

	waitAndRetrieveWebElement("nav-notes-tab").click();
	waitTwoSeconds();
        assertTrue(waitAndRetrieveWebElement("noteEditButton").isDisplayed());

        waitAndRetrieveWebElement("noteEditButton").click();
        waitTwoSeconds();

	waitAndRetrieveWebElement("note-title").clear();
	waitTwoSeconds();
	waitAndRetrieveWebElement("note-title").sendKeys("First Note Edit");
	waitTwoSeconds();

	noteTabPage.clickNoteSaveChangesButton();
	waitTwoSeconds();

	waitAndRetrieveWebElement("nav-notes-tab").click();
	waitTwoSeconds();

	waitAndRetrieveWebElement("note-title");

	assertEquals(noteTabPage.getNoteTitleText().getText(), "First Note Edit");

    }

    @Test
    @Order(3)
    public void testDeleteOperationNoteNotShown() {
        waitTwoSeconds();
	waitAndRetrieveWebElement("backToLoginLink").click();
	loginWithDefaultUser();

	assertNotNull(noteTabPage.getNoteTitleText().getText());

	waitAndRetrieveWebElement("noteDeleteButton").click();

	waitTwoSeconds();

	waitAndRetrieveWebElement("noteModalCloseButton").click();
	waitTwoSeconds();

	waitAndRetrieveWebElement("nav-notes-tab").click();
	waitTwoSeconds();

	assertThrows(NoSuchElementException.class, () -> noteTabPage.getNoteTitleText().isDisplayed());
    }

    private WebElement waitAndRetrieveWebElement(String s) {
	return webDriverWait.until(webDriver -> webDriver.findElement(By.id(s)));
    }

    private void createNote() {
	noteTabPage.clickAddNoteButton();
	waitTwoSeconds();

	waitAndRetrieveWebElement("note-title").sendKeys("First Note");
	waitTwoSeconds();

	waitAndRetrieveWebElement("note-description").sendKeys("First Note description");
	noteTabPage.clickNoteSaveChangesButton();
	waitTwoSeconds();

	waitAndRetrieveWebElement("noteModalCloseButton").click();
	waitTwoSeconds();
    }

    private void setupUserAndLandOnNotesTab() {
	waitTwoSeconds();

	signupPage.signupWithDefaultUser();
	waitTwoSeconds();

	waitAndRetrieveWebElement("login-link").click();
	waitTwoSeconds();

	loginWithDefaultUser();
    }

    private void loginWithDefaultUser() {
        waitTwoSeconds();
	waitAndRetrieveWebElement("inputUsername").sendKeys("sudhirv89");

	waitTwoSeconds();

	waitAndRetrieveWebElement("inputPassword").sendKeys("sudhir");

	loginPage.getSubmitButton().click();
	waitTwoSeconds();

	waitAndRetrieveWebElement("nav-notes-tab").click();
	waitTwoSeconds();
    }

}
