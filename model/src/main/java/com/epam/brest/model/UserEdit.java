package com.epam.brest.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserEdit {
	
	private int id;
	
	@NotNull(message="Name is required field!")
	@Size(min=4, message="Name length must be more than four!")
	private String name;
	
	@NotNull(message="Current password is required field!")
	@Size(min=6, message="Current password length must be more than six!")
	private String currentPassword;
	
	@NotNull(message="New password is required field!")
	@Size(min=6, message="New password length must be more than six!")
	private String newPassword;
	
	private boolean isUpdated;
	
	private String role;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public boolean isUpdated() {
		return isUpdated;
	}

	public void setUpdated(boolean isUpdated) {
		this.isUpdated = isUpdated;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	

}
