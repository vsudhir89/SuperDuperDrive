package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    // INSERT FILE
    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) " +
    		" VALUES ( #{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    Integer insertFile(File file);

    // GET ALL FILES
    @Select("SELECT * FROM FILES where userid = #{userId}")
    List<File> getAllFiles(Integer userId);

    // VIEW FILE
    @Select("SELECT * FROM FILES where fileId = #{fileId}")
    File getFile(Integer fileId);

    // DELETE FILE
    @Delete("DELETE FROM FILES where fileId = #{fileId}")
    Integer deleteFile(Integer fileId);


    @Select("SELECT * FROM FILES where filename = #{fileName}")
    File getFileByNameIfExists(String fileName);

}
