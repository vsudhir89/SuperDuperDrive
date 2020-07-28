package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.utils.Constants;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class FileUploadExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleFileUploadSizeTooLargeException(MaxUploadSizeExceededException maxUploadSizeExceededException, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("fileSizeToUploadTooLarge", true);
	redirectAttributes.addFlashAttribute("fileTooLargeMessage", Constants.FILE_TOO_LARGE_ERROR_MESSAGE);
	return "redirect:/home";
    }
}
