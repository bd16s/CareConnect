package cis573.carecoor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;
import cis573.carecoor.utils.MyToast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Created by: Chunxiao Mu on 11/20/14 Modified by: Yucong Li on 11/21/14
 */
public class FamilyFragment extends Fragment {
	public static final String TAG = "FamilyFragment";
	private FamilyFragment self;

	// for "has group" view
	private ExpandableListView mExListView;
	private Button mDropBtn;

	// for "no group" view
	private Button mBtnNew;
	private Button mBtnAdd;
	private EditText mEtGroupName;

	// for selector
	private boolean mHasGroup;
	private String mGroupName;

	// for group list
	List<String> mListDataHeader;
	HashMap<String, List<String>> mListDataChild;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// pop up help instruction
		// Toast toast = Toast.makeText(getActivity(),
		// "Tap the orange\t?\tbutton above for instructions.",
		// Toast.LENGTH_LONG);
		// toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,
		// 0, 0);
		// toast.show();

		fragmentSelector();
		if (mHasGroup) {
			View view = inflater.inflate(R.layout.family_view_fragment, container, false);
			mExListView = (ExpandableListView) view.findViewById(R.id.cloud_med_history_list);
			TextView tv = (TextView) view.findViewById(R.id.cloud_group_text_content);
			tv.setText(mGroupName);
			mDropBtn = (Button) view.findViewById(R.id.family_group_delete_button);
			mDropBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(self.getActivity(), FamilyGroupDropGroupActivity.class);
					intent.putExtra("GROUP_NAME", mGroupName);
					startActivityForResult(intent, 0);
				}
			});
			return view;
		} else {
			System.out.println("in else");
			View view = inflater.inflate(R.layout.family_group_no_group_fragment, container, false);
			mBtnNew = (Button) view.findViewById(R.id.new_cloud_group);
			mBtnAdd = (Button) view.findViewById(R.id.add_to_cloud_group);
			mEtGroupName = (EditText) view.findViewById(R.id.cloud_no_group_text_content);
			return view;
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		self = this;

		if (mHasGroup) {
			mListDataHeader = new ArrayList<String>();
			mListDataChild = new HashMap<String, List<String>>();

			ParseQuery<ParseObject> query = ParseQuery.getQuery("Group");
			query.whereEqualTo("groupName", mGroupName);
			try {
				// get groups in Group table
				List<ParseObject> groups = query.find();

				if (null != groups && groups.size() == 1) {
					List<String> userList = groups.get(0).getList("usersList");

					// iterate over users in the group
					for (int i = 0; i < userList.size(); ++i) {
						mListDataHeader.add(userList.get(i));
						ParseQuery<ParseObject> query2 = ParseQuery.getQuery("MedicineHistory");
						query2.whereEqualTo("userName", userList.get(i));
						List<String> medList = new ArrayList<String>();
						List<ParseObject> historyList = query2.find();

						// iterate over a user's med history
						for (int j = 0; j < historyList.size(); j++) {
							Date tmpD = historyList.get(j).getUpdatedAt();
							String tmpT = new SimpleDateFormat("MM/dd-kk:mm").format(tmpD);
							String tmpStr = "On " + tmpT + " took " + historyList.get(j).getString("medicineName");

							medList.add(tmpStr);
						}
						mListDataChild.put(userList.get(i), medList);
					}
				} else {
					MyToast.show(self.getActivity(), "Dirty data in database, get medicin history failed.");
				}
			} catch (ParseException e1) {
				MyToast.show(self.getActivity(), "Get group medicine hisotory from cloud failed.");
				e1.printStackTrace();
			}

			ExpandableListAdapter adp = new ExpandableListAdapter(self.getActivity(), mListDataHeader, mListDataChild);
			mExListView.setAdapter(adp);
		} else {
			mBtnNew.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String tmpStr = mEtGroupName.getText().toString();
					if ("".equals(tmpStr)) {
						MyToast.show(self.getActivity(), "Group name can't be empty.");
					} else if (tmpStr.length() > 20){
						MyToast.show(self.getActivity(), "Group name can't be more than 20 characters.");
					} else if (isGroupExist(tmpStr)) {
						MyToast.show(self.getActivity(), "Group already exists.");
					} else {
						jumpToConfirmationActivity(true);
					}
				}
			});

			mBtnAdd.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String tmpStr = mEtGroupName.getText().toString();
					if ("".equals(tmpStr)) {
						MyToast.show(self.getActivity(), "Group name can't be empty.");
					} else if (tmpStr.length() > 20){
						MyToast.show(self.getActivity(), "Group name can't be more than 20 characters.");
					} else if (!isGroupExist(tmpStr)) {
						MyToast.show(self.getActivity(), "Group doesn't exist.");
					} else {
						jumpToConfirmationActivity(false);
					}
				}
			});
		}
	}

	private void jumpToConfirmationActivity(boolean isNewGroup) {
		Intent intent = new Intent(self.getActivity(), FamilyGroupCreateOrAddActivity.class);
		intent.putExtra("GROUP_NAME", mEtGroupName.getText().toString());
		intent.putExtra("NEW_OR_ADD", isNewGroup);
		startActivityForResult(intent, 0);
	}

	public boolean isGroupExist(String typedGroupName) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Group");
		query.whereEqualTo("groupName", typedGroupName);
		try {
			List<ParseObject> groups = query.find();
			if (groups.size() > 0) {
				return true;
			}
		} catch (ParseException e) {
			MyToast.show(self.getActivity(), "Get group data from cloud failed.");
			e.printStackTrace();
		}
		return false;
	}

	public void fragmentSelector() {
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
		try {
			List<ParseUser> users = query.find();
			if (null != users && users.size() == 1 && users.get(0).getString("group") != null) {
				mHasGroup = true;
				mGroupName = users.get(0).getString("group");
			} else {
				mHasGroup = false;
			}
		} catch (ParseException e1) {
			MyToast.show(self.getActivity(), "Get user data from cloud failed.");
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
			return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosititon);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView,
				ViewGroup parent) {

			final String childText = (String) getChild(groupPosition, childPosition);

			if (convertView == null) {
				LayoutInflater infalInflater = (LayoutInflater) this._context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = infalInflater.inflate(R.layout.family_list_item, null);
			}

			TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);

			txtListChild.setText(childText);
			return convertView;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
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
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
			String headerTitle = (String) getGroup(groupPosition);
			if (convertView == null) {
				LayoutInflater infalInflater = (LayoutInflater) this._context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = infalInflater.inflate(R.layout.family_list_group, null);
			}

			TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
			// lblListHeader.setTypeface(null, Typeface.BOLD);
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
