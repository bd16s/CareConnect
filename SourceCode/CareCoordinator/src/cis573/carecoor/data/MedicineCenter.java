package cis573.carecoor.data;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import junit.framework.Assert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.AssetManager;
import cis573.carecoor.R;
import cis573.carecoor.bean.Medicine;
import cis573.carecoor.utils.Logger;
import cis573.carecoor.utils.ResourceKit;

public class MedicineCenter {

	public static final String TAG = "MedicineCenter";
	private static List<Medicine> mMedList = null;
	//public static String searchQuery = "Asprin";
	public final static String apiURL = "http://rxnav.nlm.nih.gov/REST/approximateTerm?term=+";
	public final static String maxEntries = "&maxEntries=15";
	public final static String options = "&option=";

	/*public static void init(Context context) {
		String urlString = apiURL + searchQuery;
		try  {
			new APIFactory().execute(urlString);
			
		} catch (Exception e) {
			Logger.e(TAG, e.getMessage(), e);
		}
	}*/
	
	public static void init(Context context) {
		String json = ResourceKit.readAsString(context, R.raw.medicine_list);
		try {
			mMedList = JsonFactory.parseMedicineList(new JSONArray(json));
		} catch (JSONException e) {
			Logger.e(TAG, e.getMessage(), e);
		}
	}
	
	public ArrayList<String> autoComplete(String input) {
		ArrayList<String> resultList = null;
		HttpURLConnection conn = null;
		StringBuilder results = new StringBuilder(); 
		try {
			StringBuilder sb = new StringBuilder(apiURL);
			sb.append(URLEncoder.encode(input, "utf8"));
			sb.append(maxEntries);
			
			URL url = new URL(sb.toString());
			conn = (HttpURLConnection) url.openConnection();
			InputStreamReader in = new InputStreamReader(conn.getInputStream());
			
			int read;
			char[] buff = new char[1024];
			while ((read = in.read(buff)) != -1) {
				results.append(buff,0,read);
			}
		} catch (MalformedURLException e) {
	        Logger.e(TAG, "Error processing Places API URL", e);
	        return resultList;
	    } catch (IOException e) {
	        Logger.e(TAG, "Error connecting to Places API", e);
	        return resultList;
	    } finally {
	        if (conn != null) {
	            conn.disconnect();
	        }
	    }
		
		try {
	        // Create a JSON object hierarchy from the results
	        JSONObject jsonObj = new JSONObject(results.toString());
	        JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

	        // Extract the Place descriptions from the results
	        resultList = new ArrayList<String>(predsJsonArray.length());
	        for (int i = 0; i < predsJsonArray.length(); i++) {
	            resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
	        }
	    } catch (JSONException e) {
	        Logger.e(TAG, "Cannot process JSON results", e);
	    }

	    return resultList;
	}
	
	
	public static List<Medicine> getMedicineList(Context context) {
		if(mMedList == null) {
			init(context);
		}
		return mMedList;
	}
	
	public static int getMedicineImageRes(Context context, Medicine medicine) {
		int id = 0;
		if(medicine != null) {
			id = getMedicineImageResByName(context, medicine.getName());
		}
		return id;
	}
	
	public static int getMedicineImageResByName(Context context, String name) {
		return context.getResources().getIdentifier("drugpic_" + name.substring(0, 3).toLowerCase(Locale.US),
				"drawable", context.getPackageName());
	}
}
