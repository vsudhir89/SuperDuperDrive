package com.udacity.jwdnd.course1.cloudstorage.signup;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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

    public void signupWithDefaultUser() {
	signupPage.signupUser("Sudhir",
		"Vaidya",
		"sudhirv89",
		"sudhir");
    }

    @Test
    public void getSignupPage() {
	assertEquals("Sign Up", driver.getTitle());
    }

    @Test
    public void checkFirstNameFieldIsShown() {
	assertTrue(signupPage.isFirstNameFieldVisible());
    }

    @Test
    public void testUserSignupSuccessful() {
        signupWithDefaultUser();
	assertTrue(signupPage.isSignupSuccessful());
    }

    @Test
    public void testUsernameAlreadyExistsErrorMessageShown() {
	signupWithDefaultUser();
	assertTrue(signupPage.isSignupSuccessful());

	signupPage.signupUser("asdad",
		"Vaiadasddya",
		"sudhirv89",
		"sudadasdhir");
	assertTrue(signupPage.isUsernameAlreadyExistsErrorMessageShown());
    }
}
