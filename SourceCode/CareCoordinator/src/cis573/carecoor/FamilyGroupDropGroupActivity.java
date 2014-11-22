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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.family_group_drop_group_activity);
		setBannerTitle(R.string.track);
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
				ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Group");
				query.whereEqualTo("groupName", mGroupName);
				try {
					List<ParseObject> groups = query.find();
					if (groups.size() == 1) {
						// prepare the group ID and users list before query the object
						mGroupId = groups.get(0).getObjectId();
						mCurrentUsername = ParseUser.getCurrentUser().getUsername();
						mIsAdminUser = mCurrentUsername.equals(groups.get(0).getString("adminUser"));
						mListLen = groups.get(0).getList("usersList").size();
						// query exact one group object back and update the usersList field
						ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Group");
						query2.getInBackground(mGroupId, new GetCallback<ParseObject>() {
							public void done(ParseObject group, ParseException e) {
								if (e == null) {
									try {
										// delete group from current user
										ParseUser currentUser = ParseUser.getCurrentUser();
										currentUser.remove("group");
										currentUser.save();
									} catch (ParseException e2) {
										MyToast.show(self, "Delete group from current user failed.");
										e2.printStackTrace();
									}
									if (mListLen > 1) {
										try {
											// delete current user from group
											group.removeAll("usersList", Arrays.asList(mCurrentUsername));
											group.save();
											// assign group administrator to another user
											ParseQuery<ParseObject> query = ParseQuery.getQuery("Group");
											query.getInBackground(mGroupId, new GetCallback<ParseObject>() {
												public void done(ParseObject group2, ParseException e) {
													if (e == null) {
														String nextAdmin = (String) group2.getList("usersList").get(0);
														group2.put("adminUser", nextAdmin);
														try {
															group2.save();
														} catch (ParseException e1) {
															e1.printStackTrace();
														}
													} else {
														// something went wrong
														MyToast.show(self, "Assign new group administrator failed.");
													}
												}
											});
										} catch (ParseException e1) {
											MyToast.show(self, "Delete group from current user failed.");
											e1.printStackTrace();
										}
									} else if (mListLen == 1
											&& mCurrentUsername.equals(group.getList("usersList").get(0).toString())) {
										try {
											group.delete();
											group.save();
										} catch (ParseException e1) {
											MyToast.show(self, "Delete whole group failed.");
											e1.printStackTrace();
										}
									} else {
										MyToast.show(self, "Leave group failed.");
									}
								} else {
									// something went wrong
									MyToast.show(self, "Leave group failed.");
								}
							}
						});
					}

				} catch (ParseException e) {
					e.printStackTrace();
					MyToast.show(self, "Dirty data in table, leave group failed.");
				}

				Intent intent = new Intent(self, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
	}
}
