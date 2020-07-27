package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {

    private FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
	this.fileMapper = fileMapper;
    }

    /**
     * Insert the file into the database
     * @param file The file to be inserted
     * @return The no of rows affected after the insert operation is performed.
     */
    public Integer insertFile(File file) {
        if (file != null) {
            return fileMapper.insertFile(file);
	}
        return 0;
    }

    /**
     * Get all the files associated with the user
     * @param userId The user Id of the current user
     * @return List of files uploaded by the user
     */
    public List<File> getAllFilesForUser(Integer userId) {
        if (userId != null) {
            return fileMapper.getAllFiles(userId);
	}
        return null;
    }

    /**
     * Get File for a given fileId
     * @param fileId The id of the file
     * @return The File that matches the given id
     */
    public File getFile(Integer fileId) {
        if (fileId != null) {
            return fileMapper.getFile(fileId);
	}
        return null;
    }

    /**
     * Delete the File associated with the given fileId
     * @param fileId The fileId of the file to be deleted
     * @return The no of rows affected after the delete operation is performed.
     */
    public Integer deleteFile(Integer fileId) {
        if (fileId != null) {
            return fileMapper.deleteFile(fileId);
	}
        return 0;
    }

    /**
     * Helper method to check if a file exists in the database
     * @param fileId The id of the file to be checked
     * @return True if the file exists; false otherwise
     */
    public boolean doesFileExist(Integer fileId) {
        if (fileId != null) {
            return fileMapper.getFile(fileId) != null;
	}
        return false;
    }

    /**
     * Get file by its filename
     * @param fileName The name of the file that exists in the system
     * @return Thf file associated with the filename
     */
    public File loadFileFromDb(String fileName) {
        if (fileName != null && !fileName.isEmpty()) {
            return fileMapper.getFileByNameIfExists(fileName);
        }
        return null;
    }
}
