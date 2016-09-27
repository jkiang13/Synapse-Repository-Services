package org.sagebionetworks.repo.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.net.util.Base64;
import org.junit.Test;
import org.sagebionetworks.repo.model.docker.RegistryEventAction;

public class DockerTokenUtilTest {
	
	@Test
	public void testKeyId() throws Exception {
		X509Certificate certificate = DockerTokenUtil.readCertificate();
		String keyId = DockerTokenUtil.computeKeyId(certificate.getPublicKey());
		String expectedKeyId = "FWOZ:6JNY:OUZ5:BHLA:YWJI:PKL4:G6QR:XCMK:3BU4:EIXW:L3Q7:VMIR";
		assertEquals(expectedKeyId, keyId);
	}

	@Test
	public void testTokenGeneration() throws Exception {
		String userName = "userName";
		String type = "repository";
		String registry = "docker.synapse.org";
		String repository1 = "syn12345/myrepo";
		String repository2 = "syn12345/myotherrepo";
		Set<RegistryEventAction> pushAndPullActions = new HashSet<RegistryEventAction>(Arrays.asList(new RegistryEventAction[] {RegistryEventAction.push, RegistryEventAction.pull}));
		Set<RegistryEventAction> pullActionOnly = new HashSet<RegistryEventAction>(Arrays.asList(new RegistryEventAction[] {RegistryEventAction.pull}));
		
		
		

		long now = 1465768785754L;
		String uuid = "8b263df7-dd04-4afe-8366-64f882e0942d";
		
		List<DockerScopePermission> permissions = new ArrayList<DockerScopePermission>();
		permissions.add(new DockerScopePermission(type, repository1, pushAndPullActions));
		permissions.add(new DockerScopePermission(type, repository2, pullActionOnly));

		
		String token = DockerTokenUtil.createToken(userName, registry, permissions, now, uuid);

		// the 'expected' token was verified to work with the Docker registry
		// note: the token is dependent on the credentials in stack.properties
		// if those credentials are changed, then this test string must be regenerated
		String[] pieces = token.split("\\.");
		assertEquals(3, pieces.length);
		
		String expectedHeaderString = "{\"typ\":\"JWT\",\"kid\":\""+DockerTokenUtil.PUBLIC_KEY_ID+"\",\"alg\":\"ES256\"}";
		String expectedHeadersBase64 = Base64.encodeBase64URLSafeString( expectedHeaderString.getBytes());
		assertEquals(expectedHeadersBase64, pieces[0]);
		
		String expectedClaimSetString = "{\"iss\":\"www.synapse.org\",\"aud\":\"docker.synapse.org\",\"exp\":1465768905,\"nbf\":1465768665,\"iat\":1465768785,\"jti\":\"8b263df7-dd04-4afe-8366-64f882e0942d\",\"sub\":\"userName\",\"access\":[{\"name\":\"syn12345/myrepo\",\"type\":\"repository\",\"actions\":[\"push\",\"pull\"]},{\"name\":\"syn12345/myotherrepo\",\"type\":\"repository\",\"actions\":[\"pull\"]}]}";
		String expectedClaimSetBase64 = Base64.encodeBase64URLSafeString( expectedClaimSetString.getBytes());
		assertEquals(expectedClaimSetBase64, pieces[1]);
		assertTrue(pieces[2].length()>0); // since signature changes every time, we can't hard code an 'expected' value
	}

}
