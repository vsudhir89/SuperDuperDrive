package com.udacity.jwdnd.course1.cloudstorage.home;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {

    @FindBy(id = "upload-button")
    private WebElement uploadButton;

    @FindBy(id = "logout-Button")
    private WebElement logoutButton;

    @FindBy(id = "nav-files-tab")
    private WebElement filesTab;

    @FindBy(id = "nav-notes-tab")
    private WebElement notesTab;

    @FindBy(id = "nav-credentials-tab")
    private WebElement credentialsTab;

    @FindBy(id = "addNoteButton")
    private WebElement addNoteButton;

    @FindBy(id = "noteTitleText")
    private WebElement noteTitleText;

    @FindBy(id = "noteDescriptionText")
    private WebElement noteDescriptionText;

    @FindBy(id = "noteEditButton")
    private WebElement noteEditButton;

    @FindBy(id = "noteDeleteButton")
    private WebElement noteDeleteButton;

    @FindBy(id = "noteChangesSaveButton")
    private WebElement noteChangesSaveButton;

    @FindBy(id = "noteOperationMessageString")
    private WebElement noteOperationModalMessageString;

    @FindBy(id = "noteModalCloseButton")
    private WebElement noteModalCloseButton;

    @FindBy(id = "note-title")
    private WebElement noteInputTitleText;

    @FindBy(id = "note-description")
    private WebElement noteInputDescriptionText;

    public HomePage(WebDriver driver) {
	PageFactory.initElements(driver, this);
    }


    public boolean isUploadButtonShown() {
	return uploadButton.isDisplayed();
    }

    public HomePage clickLogoutButton() {
	logoutButton.click();
	return this;
    }
}
