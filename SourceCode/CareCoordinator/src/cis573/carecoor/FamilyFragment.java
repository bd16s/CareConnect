package cis573.carecoor;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class FamilyFragment extends Fragment {
	public static final String TAG = "FamilyFragment";
	private ListView mListView;
	private Fragment self;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Toast toast = Toast.makeText(getActivity(),
		// "Tap the orange\t?\tbutton above for instructions.",
		// Toast.LENGTH_LONG);
		// toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,
		// 0, 0);
		// toast.show();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.family_view_fragment, container,
				false);
		 mListView = (ListView) view;
		// TextView tvEmpty = (TextView) view.findViewById(R.id.history_empty);
		// mListView.setEmptyView(tvEmpty);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		self = this;
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("MedicineHistory");
		query.getInBackground("Y5OhRw0OJu", new GetCallback<ParseObject>() {
			public void done(ParseObject object, ParseException e) {
				if (e == null) {
					// object will be your game score
					String medStr = object.getString("medicineHistory");
					String dateStr = object.getUpdatedAt().toString();
					Toast toast = Toast
							.makeText(
									getActivity(),
									dateStr,
									Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER_HORIZONTAL
							| Gravity.CENTER_VERTICAL, 0, 0);
					toast.show();
					
					ArrayList<String> strList = new ArrayList<String>();
					strList.add(dateStr + ": " + medStr);
					ArrayAdapter<String> familyMedAdapter = new ArrayAdapter<String>(
							self.getActivity(), android.R.layout.simple_spinner_item, strList);
					mListView.setAdapter(familyMedAdapter);
					
				} else {
					// something went wrong
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
