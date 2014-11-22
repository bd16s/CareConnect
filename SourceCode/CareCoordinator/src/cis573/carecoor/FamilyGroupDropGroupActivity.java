package cis573.carecoor;

import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

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

	String groupId;
	String groupName;
	List<String> newUsersList;
	String currentUsername;
	boolean isAdminUser;
	int listLen;

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
				groupName = null;
			} else {
				groupName = extras.getString("GROUP_NAME");
			}
		} else {
			groupName = (String) savedInstanceState
					.getSerializable("GROUP_NAME");
		}

		// initialize confirm button
		mBtnConfirm = (Button) this
				.findViewById(R.id.family_group_drop_group_confirm_btn);
		mBtnConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
						"Group");
				query.whereEqualTo("groupName", groupName);
				try {
					List<ParseObject> groups = query.find();
					if (groups.size() == 1) {
						// prepare the group ID and users list before query the
						// object
						groupId = groups.get(0).getObjectId();
						currentUsername = ParseUser.getCurrentUser()
								.getUsername();
						isAdminUser = currentUsername.equals(groups.get(0).getString("adminUser"));
						listLen = groups.get(0).getList("usersList").size();
						// query exact one group object back and update the
						// usersList field
						ParseQuery<ParseObject> query2 = ParseQuery
								.getQuery("Group");
						query2.getInBackground(groupId,
								new GetCallback<ParseObject>() {
									public void done(ParseObject group,
											ParseException e) {
										if (e == null) {
											try {
												// delete group from current user
												ParseUser currentUser = ParseUser.getCurrentUser();
												currentUser.remove("group");
												currentUser.save();
											} catch (ParseException e2) {
												e2.printStackTrace();
											}
											if (listLen > 1) {
												try {
													// delete current user from group
													group.removeAll("usersList", Arrays
															.asList(currentUsername));
													group.save();
													// assign group administrator to another user
													ParseQuery<ParseObject> query = ParseQuery.getQuery("Group");
													query.getInBackground(groupId, new GetCallback<ParseObject>() {
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
													    }
													  }
													});
												} catch (ParseException e1) {
													e1.printStackTrace();
												}
											} else {
												try {
													group.delete();
													group.save();
												} catch (ParseException e1) {
													e1.printStackTrace();
												}
											}
										} else {
											// something went wrong
										}
									}
								});
					}

				} catch (ParseException e) {
					e.printStackTrace();
				}

				Intent intent = new Intent(self, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});

	}
}
