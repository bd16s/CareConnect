package cis573.carecoor;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import cis573.carecoor.adapter.ContactAdapter;
import cis573.carecoor.bean.Contact;
import cis573.carecoor.data.DataCenter;
import cis573.carecoor.utils.Const;

public class PickFromContactsActivity extends BannerActivity {

	public static final String TAG = "PickFromContactsActivity";
	
	private ExpandableListView mListView;
	private ContactAdapter mAdapter;
	
	private List<Contact> mUsefulContacts;
	private List<Contact> mUserContacts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pick_from_contacts_activity);
		setBannerTitle(R.string.title_contacts);
		
		initViews();
		initContacts();
	}

	private void initViews() {
		mListView = (ExpandableListView) findViewById(R.id.contact_listview);
		mListView.setOnGroupClickListener(new OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				return true;
			}
		});
		mListView.setOnChildClickListener(onChildClick);
		mAdapter = new ContactAdapter(PickFromContactsActivity.this);
		mListView.setAdapter(mAdapter);
	}

	private void initContacts() {
		mUsefulContacts = DataCenter.getUsefulContacts(PickFromContactsActivity.this);
		mUserContacts = DataCenter.getUserContacts(PickFromContactsActivity.this);
		mAdapter.setContactList1(mUsefulContacts);
		mAdapter.setContactList2(mUserContacts);
		mAdapter.notifyDataSetChanged();
		for(int i = 0; i < mAdapter.getGroupCount(); i++) {
			mListView.expandGroup(i);
		}
	}
	
	private OnChildClickListener onChildClick = new OnChildClickListener() {
		@Override
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			Contact contact = (Contact) mAdapter.getChild(groupPosition, childPosition);
			Intent intent = new Intent();
			intent.putExtra(Const.EXTRA_CONTACT, contact);
			setResult(RESULT_OK, intent);
			finish();
			return false;
		}
	};
}
