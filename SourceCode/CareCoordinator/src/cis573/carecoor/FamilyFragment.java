package cis573.carecoor;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class FamilyFragment extends Fragment {
	public static final String TAG = "FamilyFragment";
	private ListView mListView;
	private ExpandableListView mExListView;
	private Fragment self;
	
	private boolean hasGroup;
	private String groupName;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		fragmentSelector();
		if (hasGroup) {
			View view = inflater.inflate(R.layout.family_view_fragment, container,
					false);
			mExListView = (ExpandableListView) view.findViewById(R.id.cloud_med_history_list);
			TextView tv = (TextView) view.findViewById(R.id.cloud_group_text_content);
			tv.setText(groupName);
			return view;
		} else {
			return null;
		}		
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		self = this;
		
		List<String> listDataHeader = new ArrayList<String>();
		HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();
 
        // Adding child data
        listDataHeader.add("Top 250");
        listDataHeader.add("Now Showing");
        listDataHeader.add("Coming Soon..");
 
        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("The Shawshank Redemption");
        top250.add("The Godfather");
        top250.add("The Godfather: Part II");
        top250.add("Pulp Fiction");
        top250.add("The Good, the Bad and the Ugly");
        top250.add("The Dark Knight");
        top250.add("12 Angry Men");
 
        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("The Conjuring");
        nowShowing.add("Despicable Me 2");
        nowShowing.add("Turbo");
        nowShowing.add("Grown Ups 2");
        nowShowing.add("Red 2");
        nowShowing.add("The Wolverine");
 
        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("2 Guns");
        comingSoon.add("The Smurfs 2");
        comingSoon.add("The Spectacular Now");
        comingSoon.add("The Canyons");
        comingSoon.add("Europa Report");
 
        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
        
		ExpandableListAdapter adp = new ExpandableListAdapter(self.getActivity(), listDataHeader, listDataChild);
		mExListView.setAdapter(adp);
//		ParseQuery<ParseObject> query = ParseQuery.getQuery("MedicineHistory");
//		query.whereEqualTo("userName", ParseUser.getCurrentUser().getUsername());
//		query.findInBackground(new FindCallback<ParseObject>() {
//		    public void done(List<ParseObject> historyList, ParseException e) {
//		        if (e == null) {
//		        	ArrayList<String> strList = new ArrayList<String>();
//		        	for (int i = 0; i < historyList.size(); i++) {
//		        		Date tmpD = historyList.get(i).getUpdatedAt();
//		        		String tmpT = new SimpleDateFormat("yyyy-MM-dd kk:mm").format(tmpD);
//		        		String tmpStr = tmpT + " - " + historyList.get(i).getString("medicineName");
//		        		strList.add(tmpStr);
//		        	}
//					ArrayAdapter<String> familyMedAdapter = new ArrayAdapter<String>(
//							self.getActivity(), android.R.layout.simple_spinner_item, strList);
//					mListView.setAdapter(familyMedAdapter);
//		        } else {
//		            
//		        }
//		    }
//		});
		
	}
	
	public void fragmentSelector() {
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
		try {
			List<ParseUser> users = query.find();
	    	if (null != users && users.size() == 1 && users.get(0).getString("group") != null) {
        		hasGroup = true;
        		groupName = users.get(0).getString("group");
        	} else {
        		System.out.println("no exception");
        	}
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public class ExpandableListAdapter extends BaseExpandableListAdapter {
		 
	    private Context _context;
	    private List<String> _listDataHeader; // header titles
	    // child data in format of header title, child title
	    private HashMap<String, List<String>> _listDataChild;
	 
	    public ExpandableListAdapter(Context context, List<String> listDataHeader,
	            HashMap<String, List<String>> listChildData) {
	        this._context = context;
	        this._listDataHeader = listDataHeader;
	        this._listDataChild = listChildData;
	    }
	 
	    @Override
	    public Object getChild(int groupPosition, int childPosititon) {
	        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
	                .get(childPosititon);
	    }
	 
	    @Override
	    public long getChildId(int groupPosition, int childPosition) {
	        return childPosition;
	    }
	 
	    @Override
	    public View getChildView(int groupPosition, final int childPosition,
	            boolean isLastChild, View convertView, ViewGroup parent) {
	 
	        final String childText = (String) getChild(groupPosition, childPosition);
	 
	        if (convertView == null) {
	            LayoutInflater infalInflater = (LayoutInflater) this._context
	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            convertView = infalInflater.inflate(R.layout.family_list_item, null);
	        }
	 
	        TextView txtListChild = (TextView) convertView
	                .findViewById(R.id.lblListItem);
	 
	        txtListChild.setText(childText);
	        return convertView;
	    }
	 
	    @Override
	    public int getChildrenCount(int groupPosition) {
	        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
	                .size();
	    }
	 
	    @Override
	    public Object getGroup(int groupPosition) {
	        return this._listDataHeader.get(groupPosition);
	    }
	 
	    @Override
	    public int getGroupCount() {
	        return this._listDataHeader.size();
	    }
	 
	    @Override
	    public long getGroupId(int groupPosition) {
	        return groupPosition;
	    }
	 
	    @Override
	    public View getGroupView(int groupPosition, boolean isExpanded,
	            View convertView, ViewGroup parent) {
	        String headerTitle = (String) getGroup(groupPosition);
	        if (convertView == null) {
	            LayoutInflater infalInflater = (LayoutInflater) this._context
	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            convertView = infalInflater.inflate(R.layout.family_list_group, null);
	        }
	 
	        TextView lblListHeader = (TextView) convertView
	                .findViewById(R.id.lblListHeader);
	        lblListHeader.setTypeface(null, Typeface.BOLD);
	        lblListHeader.setText(headerTitle);
	 
	        return convertView;
	    }
	 
	    @Override
	    public boolean hasStableIds() {
	        return false;
	    }
	 
	    @Override
	    public boolean isChildSelectable(int groupPosition, int childPosition) {
	        return true;
	    }
	}
	
}
