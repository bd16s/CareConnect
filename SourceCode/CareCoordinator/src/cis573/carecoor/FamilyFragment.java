package cis573.carecoor;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class FamilyFragment extends Fragment {
	public static final String TAG = "FamilyFragment";
	private ListView mListView;
	private Fragment self;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.family_view_fragment, container,
				false);
		mListView = (ListView) view.findViewById(R.id.cloud_med_history_list);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		self = this;
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("MedicineHistory");
		query.whereEqualTo("userName", ParseUser.getCurrentUser().getUsername());
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> historyList, ParseException e) {
		        if (e == null) {
		        	ArrayList<String> strList = new ArrayList<String>();
		        	for (int i = 0; i < historyList.size(); i++) {
		        		Date tmpD = historyList.get(i).getUpdatedAt();
		        		String tmpT = new SimpleDateFormat("yyyy-MM-dd kk:mm").format(tmpD);
		        		String tmpStr = tmpT + " - " + historyList.get(i).getString("medicineName");
		        		strList.add(tmpStr);
		        	}
					ArrayAdapter<String> familyMedAdapter = new ArrayAdapter<String>(
							self.getActivity(), android.R.layout.simple_spinner_item, strList);
					mListView.setAdapter(familyMedAdapter);
		        } else {
		            
		        }
		    }
		});
		
	}
	
//	public static class FamilyMedAdapter extends BaseAdapter {
//
//		@Override
//		public int getCount() {
//			// TODO Auto-generated method stub
//			return 0;
//		}
//
//		@Override
//		public Object getItem(int position) {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public long getItemId(int position) {
//			// TODO Auto-generated method stub
//			return 0;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			// TODO Auto-generated method stub
//			return null;
//		}
//		
//	}
	
}
