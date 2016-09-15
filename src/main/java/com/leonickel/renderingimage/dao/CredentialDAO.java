package com.leonickel.renderingimage.dao;

import com.leonickel.renderingimage.model.CredentialResponseDTO;

public interface CredentialDAO {

	CredentialResponseDTO getCredential() throws Exception;
}
