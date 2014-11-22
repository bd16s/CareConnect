package cis573.carecoor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class GroupActivity extends BannerActivity {

	Button mBtnConfirm;
	GroupActivity self;

	String enteredGroupName;
	Boolean isNewGroup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		self = this;

		setContentView(R.layout.group_activity);
		setBannerTitle(R.string.track);

		if (savedInstanceState == null) {
			Bundle extras = getIntent().getExtras();
			if (extras == null) {
				enteredGroupName = null;
				isNewGroup = false;
			} else {
				enteredGroupName = extras.getString("GROUP_NAME");
				isNewGroup = extras.getBoolean("NEW_OR_ADD");
			}
		} else {
			enteredGroupName = (String) savedInstanceState
					.getSerializable("GROUP_NAME");
			isNewGroup = Boolean.valueOf((String) savedInstanceState
					.getSerializable("NEW_OR_ADD"));
		}

		mBtnConfirm = (Button) this
				.findViewById(R.id.family_group_cloud_confirm);
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

		if (isNewGroup) {
			// add a group to Group table
			ParseObject newGroup = new ParseObject("Group");
			
			ParseACL aclSetting = new ParseACL(ParseUser.getCurrentUser());
			aclSetting.setPublicReadAccess(true);
			aclSetting.setPublicWriteAccess(true);
			newGroup.setACL(aclSetting);
			
			newGroup.put("groupName", enteredGroupName);
			newGroup
					.put("adminUser", ParseUser.getCurrentUser().getUsername());
			ArrayList<String> tmpList = new ArrayList<String>();
			tmpList.add(ParseUser.getCurrentUser().getUsername());
			newGroup.put("usersList", tmpList);
			newGroup.saveInBackground();

			ParseUser user = ParseUser.getCurrentUser();
			user.put("group", enteredGroupName);
			try {
				user.save();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			ParseQuery<ParseObject> query = ParseQuery.getQuery("Group");
			query.whereEqualTo("groupName", enteredGroupName);
			try {
				List<ParseObject> groups = query.find();
				System.out.println("before if " + groups.size());
				if (groups.size() == 1) {
					// List<String> a = groups.get(0).getList("usersList");
					// a.add(enteredGroupName);
					// groups.get(0).put("usersList", a);
					// groups.get(0).saveInBackground();
					String strId = groups.get(0).getObjectId();
					System.out.println("o id " + strId);
					ParseQuery<ParseObject> x = ParseQuery.getQuery("Group");

					// Retrieve the object by id
					x.getInBackground(strId, new GetCallback<ParseObject>() {
						public void done(ParseObject gameScore0,
								ParseException e) {
							if (e == null) {
								// Now let's update it with some new data. In
								// this case, only cheatMode and score
								// will get sent to the Parse Cloud. playerName
								// hasn't changed.
								List<String> b = new ArrayList<String>();
								b.add("what?!");
								System.out.println(gameScore0.getList(
										"usersList").get(0));
								String str = ParseUser.getCurrentUser().getUsername();
								gameScore0.addAllUnique("usersList",
										Arrays.asList(str));
								gameScore0.saveInBackground();
							}
						}
					});

				}
			} catch (ParseException e) {
				e.printStackTrace();
			}

			ParseUser user = ParseUser.getCurrentUser();
			user.put("group", enteredGroupName);
			try {
				user.save();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
