package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {

    // ADD CREDENTIAL
    @Insert("INSERT INTO CREDENTIALS (url, username, key, password, userid)" +
    		" VALUES (#{url}, #{username}, #{key}, #{password}, #{userId} )")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    Integer insertCredential(Credential credential);

    // VIEW CREDENTIAL
    @Select("SELECT * FROM CREDENTIALS where credentialid = #{credentialId}")
    Credential getCredential(Integer credentialId);

    // GET ALL CREDENTIALS
    @Select("SELECT * FROM CREDENTIALS where userid = #{userId}")
    List<Credential> getAllCredentials(Integer userId);

    // DELETE CREDENTIAL
    @Delete("DELETE FROM CREDENTIALS where credentialid = #{credentialId}")
    Integer deleteCredential(Integer credentialId);

    // UPDATE CREDENTIAL
    @Update("UPDATE CREDENTIALS SET url = #{url}, username = #{username}, key = #{key}, password = #{password}, userid = #{userId}" +
    		" where credentialid = #{credentialId}")
    Integer updateCredential(Credential credential);
}
