package cis573.carecoor.database;

import com.parse.ParseUser;

public class DatabaseOperations {

	public static ParseUser getCurrentUser() {
		return ParseUser.getCurrentUser();
	}
	
	public static String getCurrentUsername() {
		return ParseUser.getCurrentUser().getUsername();
	}
}
