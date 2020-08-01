package com.udacity.jwdnd.course1.cloudstorage.login;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

    @FindBy(id = "inputUsername")
    private WebElement usernameField;

    @FindBy(id = "inputPassword")
    private WebElement passwordField;

    @FindBy(id = "submit-button")
    private WebElement submitButton;

    @FindBy(id = "error-msg")
    private WebElement errorMessage;

    @FindBy(id = "signup-link")
    private WebElement signupLink;

    public WebElement getPasswordField() {
	return passwordField;
    }

    public WebElement getSubmitButton() {
	return submitButton;
    }

    public WebElement getErrorMessage() {
	return errorMessage;
    }

    public LoginPage(WebDriver driver) {
	PageFactory.initElements(driver, this);
    }

    public boolean isLoginPageUsernameDisplayed() {
	return usernameField.isDisplayed();
    }

    public LoginPage loginUser(String username, String password) {
	this.usernameField.sendKeys(username);
	this.passwordField.sendKeys(password);
	submitButton.click();
	return this;
    }

    public boolean isLoginErrorShown() {
	return errorMessage.isDisplayed();
    }

    public LoginPage clickSignupLink() {
	signupLink.click();
	return this;
    }

    public LoginPage loginWithDefaultUser() {
	return loginUser("sudhirv89", "sudhir");
    }
}
