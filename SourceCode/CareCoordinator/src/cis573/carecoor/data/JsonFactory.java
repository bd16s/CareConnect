package cis573.carecoor.data;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import cis573.carecoor.bean.Game;
import cis573.carecoor.bean.Medicine;

public class JsonFactory {

	public static final String TAG = "JsonFactory";
	
	public static Medicine parseMedicine(JSONObject json) {
		if(json == null) {
			return null;
		}
		Medicine item = new Medicine();
		item.setId(json.optInt("id"));
		item.setName(json.optString("name"));
		item.setDetailedName(json.optString("detailed_name"));
		item.setInstructions(json.optString("instructions"));
		item.setCapacity(json.optString("capacity"));
		item.setDose(json.optInt("dose"));
		item.setTimes(json.optInt("times"));
		item.setInterval(json.optInt("interval"));
		item.setDuration(json.optInt("duration"));
		return item;
	}
	
	public static List<Medicine> parseMedicineList(JSONArray array) {
		if(array == null) {
			return null;
		}
		final int size = array.length();
		List<Medicine> list = new ArrayList<Medicine>(size);
		Medicine item;
		for(int i = 0; i < size; i++) {
			item = parseMedicine(array.optJSONObject(i));
			if(item != null) {
				list.add(item);
			}
		}
		return list;
	}
	
	public static Game parseGame(JSONObject json) {
		if(json == null) {
			return null;
		}
		Game item = new Game();
		item.setName(json.optString("name"));
		JSONArray pkgs = json.optJSONArray("pkg_name");
		if(pkgs != null) {
			final int size = pkgs.length();
			String[] pkgNames = new String[size];
			for(int i = 0; i < size; i++) {
				pkgNames[i] = pkgs.optString(i);
			}
			item.setPackageNames(pkgNames);
		}
		item.setIcon(json.optString("icon"));
		return item;
	}
	
	public static List<Game> parseGameList(JSONArray array) {
		if(array == null) {
			return null;
		}
		final int size = array.length();
		List<Game> list = new ArrayList<Game>(size);
		Game item;
		for(int i = 0; i < size; i++) {
			item = parseGame(array.optJSONObject(i));
			if(item != null) {
				list.add(item);
			}
		}
		return list;
	}
}
