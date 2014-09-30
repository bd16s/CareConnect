/*
 *	Copyright (c) 2012, Yulong Information Technologies
 *	All rights reserved.
 *  
 *  @Project: aCityTongli
 *  @author: Robot	
 */
package cis573.carecoor.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Convenient class to show a toast
 * @author Belmen
 *
 */
public class MyToast {
	private static Toast toast = null;
	
	public static void show(Context context, String message) {
		if(context == null) {
			return;
		}
		if(toast == null) {
			toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
		}
		if(message != null && message.length() > 20) {
			toast.setDuration(Toast.LENGTH_LONG);
		} else {
			toast.setDuration(Toast.LENGTH_SHORT);
		}
		toast.setText(message);
		toast.show();
	}
	
	public static void show(Context context, int stringId) {
		show(context, context.getString(stringId));
	}
}
