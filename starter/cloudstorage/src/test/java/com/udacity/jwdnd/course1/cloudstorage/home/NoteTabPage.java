package com.udacity.jwdnd.course1.cloudstorage.home;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class NoteTabPage {

    @FindBy(id = "logout-Button")
    private WebElement logoutButton;

    @FindBy(id = "nav-notes-tab")
    private WebElement notesTab;

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

    public NoteTabPage(WebDriver driver) {
	PageFactory.initElements(driver, this);
    }

    public WebElement getNoteTitleText() {
	return noteTitleText;
    }

    public boolean isAddNoteButtonShown() {
	return addNoteButton.isDisplayed();
    }


    public NoteTabPage clickNotesTab() {
	notesTab.click();
	return this;
    }

    public NoteTabPage clickAddNoteButton() {
	addNoteButton.click();
	return this;
    }

    public NoteTabPage clickNoteSaveChangesButton() {
	noteChangesSaveButton.click();
	return this;
    }

    public boolean isEditButtonShown() {
	return noteEditButton.isDisplayed();
    }

}
