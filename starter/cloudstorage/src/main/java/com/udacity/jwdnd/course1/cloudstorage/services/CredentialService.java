package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {

    private CredentialMapper credentialMapper;
    private EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
	this.credentialMapper = credentialMapper;
	this.encryptionService = encryptionService;
    }

    /**
     * Add a credential
     * @param credential The credential object with all its details
     * @return The no of rows updated ; 0 if something went wrong
     */
    public Integer addCredential(Credential credential) {
        if (credential != null) {
            encryptPassword(credential);
            return credentialMapper.insertCredential(credential);
	}
        return 0;
    }

    /**
     * Get all credentials for a user
     * @param userId The id of the user that created the credential
     * @return The list of credentials for a user
     */
    public List<Credential> getAllCredentials(Integer userId) {
        if (userId != null) {
	    return credentialMapper.getAllCredentials(userId);
	}
        return null;
    }

    /**
     * Get credential for credentialId
     * @param credentialId The id of the credential
     * @return The credential that matches the id
     */
    public Credential getCredential(Integer credentialId) {
        if(credentialId != null) {
            return credentialMapper.getCredential(credentialId);
	}
        return null;
    }

    /**
     * Delete a credential given its id
     * @param credentialId The id of the credential
     * @return The no of rows updated ; 0 if something went wrong
     */
    public Integer deleteCredential(Integer credentialId) {
        if (credentialId != null) {
            return credentialMapper.deleteCredential(credentialId);
	}
        return 0;
    }

    /**
     * Update a credential
     * @param credential The credential object to update
     * @return The no of rows updated ; 0 if something went wrong
     */
    public Integer updateCredential(Credential credential) {
	if (credential != null) {
	    encryptPassword(credential);
	    return credentialMapper.updateCredential(credential);
	}
	return 0;
    }

    /**
     * Checks if a credential exists or not
     * @param credentialId The id of the credential
     * @return true if a credential exists that matches the given credentialId ; false if not.
     */
    public boolean doesCredentialExist(Integer credentialId) {
	if (credentialId != null) {
	    return credentialMapper.getCredential(credentialId) != null;
	}
	return false;
    }

    private void encryptPassword(Credential credential) {
	String encodedKey = getEncodedKey();
	String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);
	credential.setKey(encodedKey);
	credential.setPassword(encryptedPassword);
    }

    private String getEncodedKey() {
	SecureRandom random = new SecureRandom();
	byte[] key = new byte[16];
	random.nextBytes(key);
	return Base64.getEncoder().encodeToString(key);
    }

    public List<String> getAllDecryptedPasswords(Integer userId) {
	List<Credential> credentialList = getAllCredentials(userId);
	List<String> unencryptedPasswords = new ArrayList<>();
	if (credentialList != null && !credentialList.isEmpty()) {
	    for (Credential credential : credentialList) {
		String encodedKey = credential.getKey();
		unencryptedPasswords.add(encryptionService.decryptValue(credential.getPassword(), encodedKey));
	    }
	    return unencryptedPasswords;
	}
	return new ArrayList<>();
    }
}
