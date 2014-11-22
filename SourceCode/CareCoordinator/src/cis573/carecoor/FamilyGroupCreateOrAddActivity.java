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
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Created by: Yucong Li on 11/21/14
 */
public class FamilyGroupCreateOrAddActivity extends BannerActivity {

	Button mBtnConfirm;
	FamilyGroupCreateOrAddActivity self;

	String mEnteredGroupName;
	Boolean mIsNewGroup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		self = this;

		setContentView(R.layout.family_group_new_or_add_activity);
		setBannerTitle(R.string.confirmation_label);

		if (savedInstanceState == null) {
			Bundle extras = getIntent().getExtras();
			if (extras == null) {
				mEnteredGroupName = null;
				mIsNewGroup = false;
			} else {
				mEnteredGroupName = extras.getString("GROUP_NAME");
				mIsNewGroup = extras.getBoolean("NEW_OR_ADD");
			}
		} else {
			mEnteredGroupName = (String) savedInstanceState.getSerializable("GROUP_NAME");
			mIsNewGroup = Boolean.valueOf((String) savedInstanceState.getSerializable("NEW_OR_ADD"));
		}

		mBtnConfirm = (Button) this.findViewById(R.id.family_group_create_add_confirm_btn);
		mBtnConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				storeToCloud();
				Intent intent = new Intent(self, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
	}

	private void storeToCloud() {

		if (mIsNewGroup) {
			// add a group to Group table
			ParseObject newGroup = new ParseObject("Group");

			ParseACL aclSetting = new ParseACL(ParseUser.getCurrentUser());
			aclSetting.setPublicReadAccess(true);
			aclSetting.setPublicWriteAccess(true);
			newGroup.setACL(aclSetting);

			newGroup.put("groupName", mEnteredGroupName);
			newGroup.put("adminUser", ParseUser.getCurrentUser().getUsername());

			newGroup.put("usersList", Arrays.asList(ParseUser.getCurrentUser().getUsername()));
			newGroup.saveInBackground();

			ParseUser user = ParseUser.getCurrentUser();
			user.put("group", mEnteredGroupName);
			try {
				user.save();
			} catch (ParseException e) {
				MyToast.show(self, "User create group failed.");
				e.printStackTrace();
			}
		} else {
			ParseQuery<ParseObject> query = ParseQuery.getQuery("Group");
			query.whereEqualTo("groupName", mEnteredGroupName);
			try {
				List<ParseObject> groups = query.find();
				System.out.println("before if " + groups.size());
				if (groups.size() == 1) {
					String strId = groups.get(0).getObjectId();
					ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Group");

					// Retrieve the object by id
					query2.getInBackground(strId, new GetCallback<ParseObject>() {
						public void done(ParseObject group, ParseException e) {
							if (e == null) {
								// Now let's update it with some new data. In this case, only cheatMode and score will
								// get sent to the Parse Cloud. playerName hasn't changed.
								String str = ParseUser.getCurrentUser().getUsername();
								group.addAllUnique("usersList", Arrays.asList(str));
								group.saveInBackground();
							} else {
								MyToast.show(self, "Add to group failed. (get group by ID)");
							}
						}
					});

				}
			} catch (ParseException e) {
				MyToast.show(self, "Add to group failed.");
				e.printStackTrace();
			}

			try {
				ParseUser user = ParseUser.getCurrentUser();
				user.put("group", mEnteredGroupName);
				user.save();
			} catch (ParseException e) {
				MyToast.show(self, "Save group to user failed.");
				e.printStackTrace();
			}
		}
	}
}
