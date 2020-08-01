package com.udacity.jwdnd.course1.cloudstorage.signup;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SignupTests {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private SignupPage signupPage;

    @BeforeAll
    static void beforeAll() {
	WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
	this.driver = new ChromeDriver();
	driver.get("http://localhost:" + this.port + "/signup");
	signupPage = new SignupPage(driver);
    }

    @AfterEach
    public void afterEach() {
	if (this.driver != null) {
	    driver.quit();
	}
    }

    @Test
    @Order(1)
    public void getSignupPage() {
	assertEquals("Sign Up", driver.getTitle());
    }

    @Test
    @Order(2)
    public void checkFirstNameFieldIsShown() {
	assertTrue(signupPage.isFirstNameFieldVisible());
    }

    @Test
    @Order(3)
    public void testUserSignupSuccessful() {
	signupPage.signupWithDefaultUser();
	waitTwoSeconds();
	assertTrue(signupPage.isSignupSuccessful());
    }

    @Test
    @Order(4)
    public void testUsernameAlreadyExistsErrorMessageShown() {
	signupPage.signupUser("asdad",
		"Vaiadasddya",
		"sudhirv89",
		"sudadasdhir");
	waitTwoSeconds();
	assertTrue(signupPage.isUsernameAlreadyExistsErrorMessageShown());
    }

    private void waitTwoSeconds() {
	try {
	    Thread.sleep(2000);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
    }
}
