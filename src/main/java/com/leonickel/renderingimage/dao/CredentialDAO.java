package com.leonickel.renderingimage.dao;

import com.leonickel.renderingimage.model.CredentialResponse;

public interface CredentialDAO {

	CredentialResponse getCredential() throws Exception;
}
