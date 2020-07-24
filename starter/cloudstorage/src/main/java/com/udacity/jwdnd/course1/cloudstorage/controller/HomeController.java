package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.boot.Banner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {

    private NoteService noteService;

    public HomeController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/home")
    public String getAllNotes(@AuthenticationPrincipal User user, Model model) {
        int userId = user.getUserid();
        List<Note> allNotes = noteService.getAllNotes(userId);
        model.addAttribute("allNotes", allNotes);
        // TODO: get files and credentials and add them to the model here and return /home
        return "/home";
    }

    @PostMapping("/home/addOrUpdateNote")
    public String createOrUpdateNote(Note note, @AuthenticationPrincipal User user, Model model) {
        String noteError;
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
            List<Note> allNotes = noteService.getAllNotes(userId);
            model.addAttribute("allNotes", allNotes);
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
                List<Note> allNotes = noteService.getAllNotes(userId);
                model.addAttribute("allNotes", allNotes);
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
                    List<Note> allNotes = noteService.getAllNotes(userId);
                    model.addAttribute("allNotes", allNotes);
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
}
