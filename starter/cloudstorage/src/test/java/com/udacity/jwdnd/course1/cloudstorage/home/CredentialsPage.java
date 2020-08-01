package com.udacity.jwdnd.course1.cloudstorage.home;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CredentialsPage {

    @FindBy(id = "nav-credentials-tab")
    private WebElement credentialsTab;

    @FindBy(id = "addNewCredentialButton")
    private WebElement addNewCredentialButton;

    @FindBy(id = "editCredentialButton")
    private WebElement editCredentialButton;

    @FindBy(id = "deleteCredentialButton")
    private WebElement deleteCredentialButton;
    // -----------------------------------------------------------------------------------------------
    @FindBy(id = "credentialUrlText")
    private WebElement credentialUrlText;
    @FindBy(id = "credentialUsernameText")
    private WebElement credentialUsernameText;
    @FindBy(id = "credentialPasswordText")
    private WebElement credentialPasswordText;
    //------------------------------------------- ------------------------------------------------------
    @FindBy(id = "credential-url")
    private WebElement editCredentialUrlTextField;
    @FindBy(id = "credential-username")
    private WebElement editCredentialUsernameTextField;
    @FindBy(id = "credential-edit-password")
    private WebElement editCredentialPasswordTextField;
    // -----------------------------------------------------------------------------------------------------
    @FindBy(id = "credentialEditDismissButton")
    private WebElement credentialEditModalDismissButton;
    @FindBy(id = "credentialEditSaveButton")
    private WebElement credentialEditModalSaveButton;
    // ---------------------------------------------------------------------------------------------------
    @FindBy(id = "credentialOperationModalCloseButton")
    private WebElement credentialOperationModalCloseButton;

    public CredentialsPage(WebDriver webDriver) {
	PageFactory.initElements(webDriver, this);
    }

    public WebElement getEditCredentialPasswordTextField() {
	return editCredentialPasswordTextField;
    }

    public WebElement getCredentialUrlText() {
	return credentialUrlText;
    }

    public WebElement getCredentialUsernameText() {
	return credentialUsernameText;
    }

    public WebElement getCredentialPasswordText() {
	return credentialPasswordText;
    }

    // --------------------- Button Clicks --------------------------------------------------
    public void clickAddCredentialButton() {
	addNewCredentialButton.click();
    }

    public void clickCredentialSaveChangesButton() {
	credentialEditModalSaveButton.click();
    }

    public void clickCredentialsTab() {
	credentialsTab.click();
    }
}
