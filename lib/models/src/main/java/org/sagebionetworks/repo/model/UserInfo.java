package org.sagebionetworks.repo.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 *  Contains both a user and the groups to which she belongs.
 */
public class UserInfo {

	// ALL the groups the user belongs to, except "Public",
	// which everyone implicitly belongs to, and "Administrators",
	// which is encoded in the 'isAdmin' field
	private Set<Long> groups;
	
	private final boolean isAdmin;

	public UserInfo(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	
	@Deprecated
	public UserInfo(boolean isAdmin, String id){
		this(isAdmin, Long.parseLong(id));
	}
	
	/**
	 * Helper to create a UserInfo
	 * @param isAdmin
	 * @param id
	 */
	public UserInfo(boolean isAdmin, Long id){
		this.isAdmin = isAdmin;
		this.groups = new HashSet<Long>();
		this.groups.add(this.id);
	}

	public Set<Long> getGroups() {
		return groups;
	}
	
	private Long id;
	private String etag;
	private Date creationDate;
	private boolean agreesToTermsOfUse;

	public void setGroups(Set<Long> groups) {
		this.groups = groups;
	}

	/**
	 * Is the passed userInfo object valid?
	 */
	public static void validateUserInfo(UserInfo info) throws UserNotFoundException {

		if (info == null) throw new IllegalArgumentException("UserInfo cannot be null");
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEtag() {
		return etag;
	}

	public void setEtag(String etag) {
		this.etag = etag;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public boolean isAgreesToTermsOfUse() {
		return agreesToTermsOfUse;
	}

	public void setAgreesToTermsOfUse(boolean agreesToTermsOfUse) {
		this.agreesToTermsOfUse = agreesToTermsOfUse;
	}

	public boolean isAdmin() {
		return isAdmin;
	}
	
	
}
