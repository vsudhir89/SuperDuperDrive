package com.udacity.jwdnd.course1.cloudstorage.signup;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SignupPage {

    public static final String USERNAME_ALREADY_EXISTS = "The username already exists, please try another username";

    @FindBy(id = "inputFirstName")
    private WebElement firstNameField;

    @FindBy(id = "inputLastName")
    private WebElement lastNameField;

    @FindBy(id = "inputUsername")
    private WebElement usernameField;

    @FindBy(id = "inputPassword")
    private WebElement passwordField;

    @FindBy(id = "submit-button")
    private WebElement submitButton;

    @FindBy(id = "success-msg")
    private WebElement signupSuccessText;

    @FindBy(id = "error-msg")
    private WebElement signupErrorText;

    @FindBy(id = "login-link")
    private WebElement loginButton;

    public SignupPage(WebDriver driver) {
	PageFactory.initElements(driver, this);
    }

    public boolean isFirstNameFieldVisible() {
	return firstNameField.isDisplayed();
    }

    public SignupPage signupUser(String firstName, String lastName, String username, String password) {
	this.firstNameField.sendKeys(firstName);
	this.lastNameField.sendKeys(lastName);
	this.usernameField.sendKeys(username);
	this.passwordField.sendKeys(password);
	this.submitButton.click();
	return this;
    }

    public boolean isSignupSuccessful() {
	return signupSuccessText.isDisplayed();
    }

    public boolean isUsernameAlreadyExistsErrorMessageShown() {
	return signupErrorText.getText().equals(USERNAME_ALREADY_EXISTS);
    }

    public void clickLoginButton() {
	loginButton.click();
    }

    public SignupPage signupWithDefaultUser() {
	return signupUser("Sudhir",
		"Vaidya",
		"sudhirv89",
		"sudhir");
    }
}
