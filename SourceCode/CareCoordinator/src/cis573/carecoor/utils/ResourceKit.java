package cis573.carecoor.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;

public class ResourceKit {

	public static final String TAG = "ResourceKit";
	
	public static String readAsString(Context context, int resId) {
		InputStream is = null;
		StringBuilder sb = new StringBuilder();
		try {
			is = context.getResources().openRawResource(resId);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line;
			while((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			Logger.e(TAG, e.getMessage(), e);
		} finally {
			try {
				if(is != null) {
					is.close();
				}
			} catch (IOException e) {
				Logger.e(TAG, e.getMessage(), e);
			}
		}
		return sb.toString();
	}
	
	public static int getDrawableResIdByName(Context context, String name) {
		return context.getResources().getIdentifier(name, "drawable", context.getPackageName());
	}
}
