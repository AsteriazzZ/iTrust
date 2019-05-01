package edu.ncsu.csc.itrust.model.user.patient;

import java.io.Serializable;
import edu.ncsu.csc.itrust.model.user.User;

/**
 * Empty class for future use
 * Needed Patient Data Access/Controllers but
 * Did not need any information generally available
 * outside the information available in the user class
 *
 */
public class Patient extends User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1509544768333457536L;
	private String bloodType;

	public void setBloodType(String bloodType) {
		this.bloodType = bloodType;
	}

	public String getBloodType() {
		return bloodType;
	}

	// add data when appropriate
}
