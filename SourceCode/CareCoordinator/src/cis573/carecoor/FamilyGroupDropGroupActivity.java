package cis573.carecoor;

import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import cis573.carecoor.utils.MyToast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Created by: Yucong Li on 11/21/14
 */
public class FamilyGroupDropGroupActivity extends BannerActivity {

	Button mBtnConfirm;
	FamilyGroupDropGroupActivity self;

	String mGroupId;
	String mGroupName;
	List<String> mNewUsersList;
	String mCurrentUsername;
	boolean mIsAdminUser;
	int mListLen;
	ParseUser currentUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.family_group_drop_group_activity);
		setBannerTitle(R.string.confirmation_label);
		self = this;

		// get group name from intent
		if (savedInstanceState == null) {
			Bundle extras = getIntent().getExtras();
			if (extras == null) {
				mGroupName = null;
			} else {
				mGroupName = extras.getString("GROUP_NAME");
			}
		} else {
			mGroupName = (String) savedInstanceState.getSerializable("GROUP_NAME");
		}

		// initialize confirm button
		mBtnConfirm = (Button) this.findViewById(R.id.family_group_drop_group_confirm_btn);
		mBtnConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					try {
						// delete group from current user
						currentUser = ParseUser.getCurrentUser();
						currentUser.remove("group");
						currentUser.save();
					} catch (ParseException e2) {
						MyToast.show(self, "Delete group from current user failed.");
						e2.printStackTrace();
					}
					
					ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Group");
					query.whereEqualTo("groupName", mGroupName);
					List<ParseObject> groups = query.find();
					
					if (groups.size() == 1) {
						// prepare the group ID and users list before query the object
						ParseObject group = groups.get(0);
						mGroupId = group.getObjectId();
						mCurrentUsername = ParseUser.getCurrentUser().getUsername();
						mIsAdminUser = mCurrentUsername.equals(group.getString("adminUser"));
						mListLen = group.getList("usersList").size();
						
						// query exact one group object back and update the usersList field		
						if (mListLen > 1) {
							try {
								// delete current user from group
								group.removeAll("usersList", Arrays.asList(mCurrentUsername));
								group.save();
								String nextAdmin = (String) group.getList("usersList").get(0);
								group.put("adminUser", nextAdmin);
								group.save();
							} catch (ParseException e1) {
								MyToast.show(self, "Delete group from current user failed.");
								e1.printStackTrace();
							}
						} else if (mListLen == 1
								&& mCurrentUsername.equals(group.getList("usersList").get(0).toString())) {
							try {
								group.delete();
								// group.save();
							} catch (ParseException e1) {
								MyToast.show(self, "Delete whole group failed.");
								e1.printStackTrace();
							}
						} else {
							MyToast.show(self, "Leave group failed.");
						}
					}
				} catch (ParseException e) {
					MyToast.show(self, "Dirty data in table, leave group failed.");
					e.printStackTrace();
				}

				Intent intent = new Intent(self, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
	}
}
