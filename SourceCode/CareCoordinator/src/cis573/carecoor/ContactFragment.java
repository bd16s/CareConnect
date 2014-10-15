package cis573.carecoor;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.TextView;
import cis573.carecoor.adapter.ContactAdapter;
import cis573.carecoor.bean.Contact;
import cis573.carecoor.data.DataCenter;

public class ContactFragment extends Fragment {

	public static final String TAG = "ContactFragment";
	
	private static final int REQUEST_IMPORT = 0;
	
	private static final int ADD = 0;
	private static final int EDIT = 1;
	
	private Button mBtnAdd;
	private Button mBtnImport;
	private ExpandableListView mListView;
	private ContactAdapter mAdapter;
	private AlertDialog mAddContactDialog;
	
	private List<Contact> mUsefulContacts;
	private List<Contact> mUserContacts;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mUsefulContacts = DataCenter.getUsefulContacts(getActivity());
		mUserContacts = DataCenter.getUserContacts(getActivity());
		if(mUserContacts == null) {
			mUserContacts = new ArrayList<Contact>();
		}
		initAddContactDialog("", "", ADD, 0);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.contact_fragment, container, false);
		mBtnAdd = (Button) view.findViewById(R.id.contact_add_btn);
		mBtnImport = (Button) view.findViewById(R.id.contact_import_btn);
		mBtnAdd.setOnClickListener(onButtonClick);
		mBtnImport.setOnClickListener(onButtonClick);
		mListView = (ExpandableListView) view.findViewById(R.id.contact_listview);
		mListView.setOnGroupClickListener(new OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				return true;
			}
		});
		mListView.setOnItemLongClickListener(onLongClick);
		mAdapter = new ContactAdapter(getActivity());
		mAdapter.setContactList1(mUsefulContacts);
		mAdapter.setContactList2(mUserContacts);
		mListView.setAdapter(mAdapter);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		for(int i = 0; i < mAdapter.getGroupCount(); i++) {
			mListView.expandGroup(i);
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == REQUEST_IMPORT) {
			if(resultCode == Activity.RESULT_OK && data != null) {
				addImportedContact(data.getData());
			}
		}
	}
	
	private void addImportedContact(Uri uri) {
		@SuppressWarnings("deprecation")
		Cursor c = getActivity().managedQuery(uri, null, null, null, null);
		if (c.moveToFirst()) {
			String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
			String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
			if("1".equals(hasPhone)) {
				Cursor phones = getActivity().getContentResolver()
						.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
				phones.moveToFirst();
				String number = phones.getString(phones
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				String name = phones.getString(phones
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
				mUserContacts.add(new Contact(name, number));
				DataCenter.setUserContacts(getActivity(), mUserContacts);
				mAdapter.setContactList2(mUserContacts);
				mAdapter.notifyDataSetChanged();
			}
		}
	}

	private OnClickListener onButtonClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int id = v.getId();
			if(id == R.id.contact_add_btn) {
				initAddContactDialog("", "", ADD, 0);
				mAddContactDialog.show();
			} else if(id == R.id.contact_import_btn) {
				// Pick from system contacts
				Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
				startActivityForResult(i, REQUEST_IMPORT);
			}
		}
	};
	
	private OnItemLongClickListener onLongClick = new OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			long flatPosition = mListView.getExpandableListPosition(position);
			if(ExpandableListView.getPackedPositionType(flatPosition)
					== ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
				int groupPosition = ExpandableListView.getPackedPositionGroup(flatPosition);
	            if(groupPosition == 1) {
	            	int childPosition = ExpandableListView.getPackedPositionChild(flatPosition);
	            	showDeleteEditContactDialog(childPosition);
	            }
			}
			return true;
		}
	};
	
	private void initAddContactDialog(String name_p, String phone_p, final int type, final int position) {
		View view = View.inflate(getActivity(), R.layout.dialog_add_contact, null);
		final EditText etName = (EditText) view.findViewById(R.id.contact_name_edittext);
		final EditText etPhone = (EditText) view.findViewById(R.id.contact_phone_edittext);
		etName.setText(name_p, TextView.BufferType.EDITABLE);
		etPhone.setText(phone_p, TextView.BufferType.EDITABLE);
		mAddContactDialog = new AlertDialog.Builder(getActivity())
		.setTitle(R.string.dialog_add_contact_title)
		.setView(view)
		.setPositiveButton(android.R.string.ok, null)
		.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		}).create();
		
		mAddContactDialog.setOnShowListener(new DialogInterface.OnShowListener() {

		    @Override
		    public void onShow(DialogInterface dialog) {

		        Button b = mAddContactDialog.getButton(AlertDialog.BUTTON_POSITIVE);
		        b.setOnClickListener(new View.OnClickListener() {

		            @Override
		            public void onClick(View view) {
		            	String name = etName.getText().toString();
        				String phone = etPhone.getText().toString();
        				
        				if("".equals(name) || "".equals(phone)){
        					Toast.makeText(getActivity().getApplicationContext(), R.string.contact_error_msg, Toast.LENGTH_SHORT).show();
        				}else{
        					if(type == ADD){
            					mUserContacts.add(new Contact(name, phone));
            				} else if(type == EDIT){
            					mUserContacts.get(position).setName(name);
            					mUserContacts.get(position).setPhone(phone);
            				}
            				DataCenter.setUserContacts(getActivity(), mUserContacts);
            				mAdapter.setContactList2(mUserContacts);
            				mAdapter.notifyDataSetChanged();
    		            	mAddContactDialog.dismiss();
        				}
		            }
		        });
		    }
		});
	}
	
	private void showDeleteEditContactDialog(final int position) {
		new AlertDialog.Builder(getActivity())
		.setTitle(R.string.dialog_delete_edit_contact_title)
		.setMessage(R.string.dialog_delete_edit_contact_msg)
		.setPositiveButton(R.string.dialog_contact_delete, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {						
				new AlertDialog.Builder(getActivity())
				.setTitle(R.string.dialog_delete_contact_title)
				.setMessage(R.string.dialog_delete_contact_msg)
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						mUserContacts.remove(position);
						DataCenter.setUserContacts(getActivity(), mUserContacts);
						mAdapter.setContactList2(mUserContacts);
						mAdapter.notifyDataSetChanged();
					}
				}).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						return;
					}
				}).show();
			}
		}).setNegativeButton(R.string.dialog_contact_edit, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {				
				String name = mUserContacts.get(position).getName();
				String phone = mUserContacts.get(position).getPhone();
				initAddContactDialog(name, phone, EDIT, position);
				mAddContactDialog.show();
			}
		}).show();
	}
}
