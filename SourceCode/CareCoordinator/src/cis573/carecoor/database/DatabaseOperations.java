package cis573.carecoor.database;

import java.util.List;

import android.content.Context;

import cis573.carecoor.R;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class DatabaseOperations {

	public static void dbInit(Context context) {
		Parse.initialize(context, context.getString(R.string.parse_app_id),
				context.getString(R.string.parse_client_key));
		
		ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
 
        // If you would like all objects to be private by default, remove this
        // line.
        defaultACL.setPublicReadAccess(true);
 
        ParseACL.setDefaultACL(defaultACL, true);
	}
	
	public static ParseUser getCurrentUser() {
		return ParseUser.getCurrentUser();
	}
	
	public static String getCurrentUsername() {
		return ParseUser.getCurrentUser().getUsername();
	}
	
	public static List<ParseObject> simpleFindQuery(String tableName, String colName, String value) {
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(tableName);
		query.whereEqualTo(colName, value);
		List<ParseObject> result = null;
		try {
			result = query.find();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static boolean simpleUserUpdate(String colName, String value) {
		ParseUser user = getCurrentUser();
		user.put(colName, value);
		try {
			user.save();
			return true;
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
