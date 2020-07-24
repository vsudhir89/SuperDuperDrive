package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {

    // ADD NOTE
    @Insert("INSERT INTO NOTES (notetitle, notedescription, userid)" +
    		" VALUES ( #{noteTitle}, #{noteDescription}, #{userid})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    Integer insertNote(Note note);

    // VIEW NOTE
    @Select("SELECT * FROM NOTES where noteid = #{noteId}")
    Note getNote(Integer noteId);

    // VIEW ALL NOTES
    @Select("SELECT * FROM NOTES where userid = #{userId}")
    List<Note> getAllNotes(Integer userId);

    // DELETE NOTE
    @Delete("DELETE FROM NOTES where noteid = #{noteId}")
    Integer deleteNote(Integer noteId);


    @Update("UPDATE NOTES SET notetitle=#{noteTitle}, notedescription=#{noteDescription}, userid=#{userid}" +
            " where noteid = #{noteId}")
    Integer updateNote(Note note);
}
