package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.utils.Constants;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Controller
public class HomeController {

    private NoteService noteService;
    private CredentialService credentialService;
    private FileService fileService;

    public HomeController(NoteService noteService, CredentialService credentialService, FileService fileService) {
	this.noteService = noteService;
	this.credentialService = credentialService;
	this.fileService = fileService;
    }

    @GetMapping("/home")
    public String getAllNotes(@AuthenticationPrincipal User user, Model model) {
	int userId = user.getUserid();
	populateHomePageData(model, userId);
	return "/home";
    }

    private void populateHomePageData(Model model, int userId) {
	List<Note> allNotes = noteService.getAllNotes(userId);
	model.addAttribute("allNotes", allNotes);

	List<Credential> credentialList = credentialService.getAllCredentials(userId);
	model.addAttribute("credentials", credentialList);

	List<File> filesList = fileService.getAllFilesForUser(userId);
	model.addAttribute("files", filesList);
    }

    // ------------------------------------- Note -----------------------------------------------------------------

    @PostMapping("/home/addOrUpdateNote")
    public String createOrUpdateNote(Note note, @AuthenticationPrincipal User user, Model model) {
	int userId = user.getUserid();
	if (note.getNoteTitle() != null) {
	    if (noteService.doesNoteExist(note.getNoteId())) {
		// we have the note in the system, update it.
		tryUpdateNote(note, user, model, userId);
	    } else {
		tryCreateNote(note, model, userId);
	    }
	}
	return "/home";
    }

    private void tryCreateNote(Note note, Model model, int userId) {
	note.setUserid(userId);
	int rowsAdded = noteService.createNote(note);
	if (rowsAdded > 0) {
	    model.addAttribute("noteOperationSuccess", true);
	    model.addAttribute("noteOperationMessage", Constants.NOTE_ADDED_SUCCESSFULLY);
	    populateHomePageData(model, userId);
	}
    }

    private void tryUpdateNote(Note note, @AuthenticationPrincipal User user, Model model, int userId) {
	Note noteToUpdate = noteService.getNote(note.getNoteId());
	if (noteToUpdate != null) {
	    noteToUpdate.setNoteTitle(note.getNoteTitle());
	    noteToUpdate.setNoteDescription(note.getNoteDescription());
	    noteToUpdate.setUserid(user.getUserid());
	    int rowsUpdated = noteService.updateNote(noteToUpdate);
	    if (rowsUpdated > 0) {
		// Good to go, Send a success message to UI
		populateHomePageData(model, userId);
	    } else {
		// Something went wrong trying to update DB. Send error message
		model.addAttribute("noteUpdateError", Constants.SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN_LATER);
	    }
	} else {
	    // Something went wrong trying to lookup DB. Send error message
	    model.addAttribute("noteUpdateError", Constants.SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN_LATER);
	}
    }

    @GetMapping("/home/deleteNote")
    public String deleteNote(Note note, @AuthenticationPrincipal User user, Model model) {
	int userId = user.getUserid();
	if (note != null) {
	    if (noteService.doesNoteExist(note.getNoteId())) {
		int rowsDeleted = noteService.deleteNote(note.getNoteId());
		if (rowsDeleted == 1) {
		    model.addAttribute("noteOperationSuccess", true);
		    model.addAttribute("noteOperationMessage", Constants.NOTE_DELETED_SUCCESSFULLY);
		    populateHomePageData(model, userId);
		}
	    } else {
		// The note doesn't exist or is already deleted. Send a message to the user
		model.addAttribute("noteOperationMessage", Constants.NOTE_DOES_NOT_EXIST_ERROR);
	    }
	} else {
	    // Note error populate
	    model.addAttribute("noteOperationMessage", Constants.SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN_LATER);
	}
	return "/home";
    }

    // ----------------------------------------- Credentials ------------------------------------------------------

    @PostMapping("/home/addOrUpdateCredential")
    public String addOrUpdateCredential(Credential credential,
					@AuthenticationPrincipal User user,
					Model model) {
	int userId = user.getUserid();
	if (credential.getUrl() != null) {
	    if (credentialService.doesCredentialExist(credential.getCredentialId())) {
		// we have a credential already, just update it
		tryUpdateCredential(credential, model, userId);
	    } else {
		// create a new credential
		tryAddCredential(credential, model, userId);
	    }
	}
	return "/home";
    }

    public void tryAddCredential(Credential credential, Model model, int userId) {
	String unencryptedPwd = credential.getPassword();
	credential.setUserId(userId);
	int rowsAffected = credentialService.addCredential(credential);
	if (rowsAffected > 0) {
	    populateHomePageData(model, userId);
	    model.addAttribute("credentialOperationSuccess", true);
	    model.addAttribute("credentialOperationMessage", Constants.CREDENTIAL_ADDED_SUCCESSFULLY);
	    model.addAttribute("unencryptedPwd", unencryptedPwd);
	} else {
	    model.addAttribute("credentialError", Constants.SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN_LATER);
	}
    }

    public void tryUpdateCredential(Credential credential, Model model, int userId) {
	String unencryptedPwd = credential.getPassword();
	Credential credentialToUpdate = credentialService.getCredential(credential.getCredentialId());
	if (credentialToUpdate != null) {
	    credentialToUpdate.setUrl(credential.getUrl());
	    credentialToUpdate.setPassword(credential.getPassword());
	    credentialToUpdate.setUsername(credential.getUsername());
	    int rowsAffected = credentialService.updateCredential(credentialToUpdate);
	    if (rowsAffected == 1) {
		// Good to go
		populateHomePageData(model, userId);
		model.addAttribute("unencryptedPwd", unencryptedPwd);
	    } else {
		model.addAttribute("credentialError", Constants.SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN_LATER);
	    }
	} else {
	    model.addAttribute("credentialError", Constants.SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN_LATER);
	}
    }

    @GetMapping("/home/deleteCredential")
    public String deleteCredential(Credential credential, @AuthenticationPrincipal User user, Model model) {
	int userId = user.getUserid();
	if (credentialService.doesCredentialExist(credential.getCredentialId())) {
	    int rowsAffected = credentialService.deleteCredential(credential.getCredentialId());
	    if (rowsAffected > 0) {
		model.addAttribute("credentialOperationSuccess", true);
		model.addAttribute("credentialOperationMessage", Constants.CREDENTIAL_DELETED_SUCCESSFULLY);
		populateHomePageData(model, userId);
	    } else {
		model.addAttribute("credentialError", Constants.SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN_LATER);
	    }
	} else {
	    model.addAttribute("credentialError", Constants.SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN_LATER);
	}
	return "/home";
    }


    // ------------------------------------- Files -----------------------------------------------------------

    @PostMapping("/home/files/upload")
    public String uploadFile(@RequestParam("fileUpload") MultipartFile file, @AuthenticationPrincipal User user, Model model) {
	int userId = user.getUserid();
	File fileToUpload = null;
	if (file != null && !file.isEmpty()) {
	    if (fileService.isFileNameUnique(file.getOriginalFilename())) {
		try {
		    fileToUpload = new File(null, file.getOriginalFilename(), file.getContentType(), String.valueOf(file.getSize()), userId, file.getBytes());
		} catch (IOException e) {
		    model.addAttribute("fileError", Constants.SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN_LATER);
		    e.printStackTrace();
		}
		int rowsAffected = fileService.insertFile(fileToUpload);
		if (rowsAffected > 0) {
		    populateHomePageData(model, userId);
		    model.addAttribute("fileOperationSuccess", true);
		    model.addAttribute("fileOperationMessage", Constants.FILE_UPLOAD_SUCCESSFUL);
		} else {
		    model.addAttribute("fileUploadError", Constants.SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN_LATER);
		}
	    } else {
		// Duplicate file name.
		populateHomePageData(model, userId);
		model.addAttribute("duplicateFileName", true);
		model.addAttribute("duplicateFileOperationMessage", Constants.A_FILE_WITH_THAT_NAME_ALREADY_EXISTS_PLEASE_TRY_ANOTHER_FILE_OR_CHANGE_THE_FILENAME);
	    }
	} else {
	    // File is empty. Send an error to the user
	    model.addAttribute("fileInvalidError", Constants.FILE_EMPTY_ERROR_MESSAGE);
	}
	return "/home";
    }

    @GetMapping("/home/downloadFile/")
    public ResponseEntity<Resource> downloadFile(File file, @AuthenticationPrincipal User user, Model model) {
	File fileToDownload = fileService.loadFileFromDb(file.getFileName());
	if (fileToDownload != null) {
	    ByteArrayResource resource = new ByteArrayResource(fileToDownload.getFileData());
	    return ResponseEntity.ok()
		    .header(HttpHeaders.CONTENT_DISPOSITION,
			    "attachment; filename=" + fileToDownload.getFileName())
		    .contentLength(resource.contentLength())
		    .body(resource);
	} else {
	    model.addAttribute("fileDownloadFailed", "File could not be downloaded");
	    populateHomePageData(model, user.getUserid());
	}
	return null;
    }

    @GetMapping("/home/deleteFile")
    public String deleteFile(File file, @AuthenticationPrincipal User user, Model model) {
	int userId = user.getUserid();
	int idOfFileToDelete = file.getFileId();
	if (file.getFileId() != null && fileService.doesFileExist(idOfFileToDelete)) {
	    int rowsAffected = fileService.deleteFile(idOfFileToDelete);
	    if (rowsAffected == 1) {
		populateHomePageData(model, userId);
		model.addAttribute("fileOperationSuccess", true);
		model.addAttribute("fileOperationMessage", Constants.FILE_DELETED_SUCCESSFULLY);
	    } else {
		model.addAttribute("fileError", Constants.SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN_LATER);
	    }
	} else {
	    model.addAttribute("fileDoesNotExistError", Constants.THE_FILE_YOU_REQUESTED_TO_DELETE_DOES_NOT_EXIST);
	}
	return "/home";
    }
}
