package com.udacity.jwdnd.course1.cloudstorage.home;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {

    @FindBy(id = "upload-button")
    private WebElement uploadButton;

    public HomePage(WebDriver driver) {
	PageFactory.initElements(driver, this);
    }

    public boolean isUploadButtonShown() {
        return uploadButton.isDisplayed();
    }
}
