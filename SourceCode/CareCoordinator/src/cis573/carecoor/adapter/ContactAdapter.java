package cis573.carecoor.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import cis573.carecoor.R;
import cis573.carecoor.bean.Contact;

public class ContactAdapter extends BaseExpandableListAdapter {

	private Context mContext;
	private List<Contact> mContactList1 = null;
	private List<Contact> mContactList2 = null;
	
	public ContactAdapter(Context context) {
		this.mContext = context;
	}

	public void setContactList1(List<Contact> list) {
		this.mContactList1 = list;
	}
	
	public void setContactList2(List<Contact> list) {
		this.mContactList2 = list;
	}
	
	private List<Contact> getList(int position) {
		List<Contact> list = null;
		if(position == 0) {
			list = mContactList1;
		} else if(position == 1) {
			list = mContactList2;
		}
		return list;
	}
	
	@Override
	public int getGroupCount() {
		return 2;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		int count = 0;
		List<Contact> list = getList(groupPosition);
		if(list != null) {
			count = list.size();
		}
		return count;
	}

	@Override
	public Object getGroup(int groupPosition) {
		String item = null;
		String[] items = mContext.getResources().getStringArray(R.array.contact_group_items);
		if(items != null) {
			item = items[groupPosition];
		}
		return item;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		Contact item = null;
		List<Contact> list = getList(groupPosition);
		if(list != null) {
			item = list.get(childPosition);
		}
		return item;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return groupPosition * 100 + childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		GroupViewHolder vh;
		if(convertView == null) {
			convertView = View.inflate(mContext, R.layout.contact_group_view, null);
			vh = new GroupViewHolder();
			vh.text = (TextView) convertView;
			convertView.setTag(vh);
		} else {
			vh = (GroupViewHolder) convertView.getTag();
		}
		String item = (String) getGroup(groupPosition);
		if(item != null) {
			vh.text.setText(item);
		}
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ChildViewHolder vh;
		if(convertView == null) {
			convertView = View.inflate(mContext, R.layout.contact_child_view, null);
			vh = new ChildViewHolder();
			vh.name = (TextView) convertView.findViewById(R.id.contact_child_name);
			vh.phone = (TextView) convertView.findViewById(R.id.contact_child_phone);
			convertView.setTag(vh);
		} else {
			vh = (ChildViewHolder) convertView.getTag();
		}
		Contact item = (Contact) getChild(groupPosition, childPosition);
		if(item != null) {
			vh.name.setText(item.getName());
			vh.phone.setText(item.getPhone());
		}
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	static class GroupViewHolder {
		TextView text;
	}
	
	static class ChildViewHolder {
		TextView name;
		TextView phone;
	}
}
