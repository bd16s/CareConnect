package cis573.carecoor;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import cis573.carecoor.bean.Contact;
import cis573.carecoor.data.DataCenter;
import cis573.carecoor.data.MedicineCenter;
import cis573.carecoor.database.DatabaseOperations;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

public class CoorApplication extends Application {

	public static final String TAG = "CoorApplication";

	@Override
	public void onCreate() {
		super.onCreate();
		
		DatabaseOperations.dbInit(this);
		
		initUsefulContacts();
		MedicineCenter.init(this);
	}

	private void initUsefulContacts() {
		List<Contact> list = DataCenter.getUsefulContacts(this);
		if (list == null) {
			list = new ArrayList<Contact>();
			list.add(new Contact("Penn Nursing", "215-898-5074"));
			list.add(new Contact("University of Pennsylvania", "215-898-5000"));
			DataCenter.setUsefulContacts(this, list);
		}
	}
}
