package cis573.carecoor.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;

public class FileKit {
	
	public static final String TAG = "FileKit";
	
	public static void saveObject(Context context, String filename, Object odj) {
		ObjectOutputStream oos = null;
		try {
			FileOutputStream fos = context.openFileOutput(filename,
					Context.MODE_PRIVATE);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(odj);
		} catch (Exception e) {
			Logger.e(TAG, e.getMessage(), e);
		} finally {
			try {
				if(oos != null) {
					oos.close();
				}
			} catch (IOException e) {
				Logger.e(TAG, e.getMessage(), e);
			}
		}
	}
	
	public static Object readObject(Context context, String filename) {
		Object obj = null;
		ObjectInputStream ois = null;
		File file = context.getFileStreamPath(filename);
		if(file != null && file.exists()) {
			try {
				FileInputStream fis = new FileInputStream(file);
				ois = new ObjectInputStream(fis);
				obj = ois.readObject();
			} catch (Exception e) {
				Logger.e(TAG, e.getMessage(), e);
			} finally {
				try {
					if(ois != null) {
						ois.close();
					}
				} catch (IOException e) {
					Logger.e(TAG, e.getMessage(), e);
				}
			}
		}
		return obj;
	}
}
