package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
	this.noteMapper = noteMapper;
    }

    public Integer createNote(Note note) {
        return noteMapper.insertNote(note);
    }

    public List<Note> getAllNotes(Integer userId) {
        return noteMapper.getAllNotes(userId);
    }

    public boolean doesNoteExist(Integer noteId) {
        if (noteId != null) {
            return noteMapper.getNote(noteId) != null;
        }
        return false;
    }

    public Note getNote(Integer noteId) {
        if (noteId != null) {
            return noteMapper.getNote(noteId);
        }
        return null;
    }

    public Integer updateNote(Note noteToUpdate) {
        return noteMapper.updateNote(noteToUpdate);
    }

    public Integer deleteNote(Integer noteId) {
        return noteMapper.deleteNote(noteId);
    }
}
