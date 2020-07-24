package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class HomeController {

    private NoteService noteService;
    private CredentialService credentialService;

    public HomeController(NoteService noteService, CredentialService credentialService) {
	this.noteService = noteService;
	this.credentialService = credentialService;
    }

    @GetMapping("/home")
    public String getAllNotes(@AuthenticationPrincipal User user, Model model) {
	int userId = user.getUserid();
	populateHomePageData(model, userId);
	// TODO: get files and credentials and add them to the model here and return /home
	return "/home";
    }

    private void populateHomePageData(Model model, int userId) {
	List<Note> allNotes = noteService.getAllNotes(userId);
	model.addAttribute("allNotes", allNotes);

	List<Credential> credentialList = credentialService.getAllCredentials(userId);
	model.addAttribute("credentials", credentialList);
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
	    populateHomePageData(model, userId);
	}
    }

    private void tryUpdateNote(Note note, @AuthenticationPrincipal User user, Model model, int userId) {
	String noteError;
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
		noteError = "Something went wrong. Please try again";
		model.addAttribute("noteUpdateError", noteError);
	    }
	} else {
	    // Something went wrong trying to lookup DB. Send error message
	    noteError = "Something went wrong. Please try again";
	    model.addAttribute("noteUpdateError", noteError);
	}
    }

    @GetMapping("/home/deleteNote")
    public String deleteNote(Note note, @AuthenticationPrincipal User user, Model model) {
	String noteDeleteError;
	int userId = user.getUserid();
	if (note != null) {
	    if (noteService.doesNoteExist(note.getNoteId())) {
		int rowsDeleted = noteService.deleteNote(note.getNoteId());
		if (rowsDeleted == 1) {
		    populateHomePageData(model, userId);
		}
	    } else {
		// The note doesn't exist or is already deleted. Send a message to the user
		noteDeleteError = "Looks like that note does not exist. Try deleting a valid note";
		model.addAttribute("noteDeleteError", noteDeleteError);
	    }
	} else {
	    // Note error populate
	    noteDeleteError = "Something went wrong. Please try again";
	    model.addAttribute("noteDeleteError", noteDeleteError);
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
	    model.addAttribute("unencryptedPwd", unencryptedPwd);
	} else {
	    model.addAttribute("credentialError", "Something went wrong. Please try again");
	}
    }

    public void tryUpdateCredential(Credential credential, Model model, int userId) {
	String credentialError;
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
		credentialError = "Something went wrong. Please try again later";
		model.addAttribute("credentialError", credentialError);
	    }
	} else {
	    credentialError = "Something went wrong. Please try again later";
	    model.addAttribute("credentialError", credentialError);
	}
    }

    @GetMapping("/home/deleteCredential")
    public String deleteCredential(Credential credential, @AuthenticationPrincipal User user, Model model) {
	String credentialError;
	int userId = user.getUserid();
	if (credentialService.doesCredentialExist(credential.getCredentialId())) {
	    int rowsAffected = credentialService.deleteCredential(credential.getCredentialId());
	    if (rowsAffected > 0) {
		populateHomePageData(model, userId);
	    } else {
		credentialError = "Something went wrong. Please try again later";
		model.addAttribute("credentialError", credentialError);
	    }
	} else {
	    credentialError = "Something went wrong. Please try again later";
	    model.addAttribute("credentialError", credentialError);
	}
	return "/home";
    }

}
