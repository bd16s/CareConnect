package cis573.carecoor.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class CommonAdapter<T> extends BaseAdapter {

	protected Context mContext;
	protected List<T> mData;
	
	public CommonAdapter(Context context) {
		this.mContext = context;
	}

	public void setData(List<T> data) {
		this.mData = data;
	}

	@Override
	public int getCount() {
		return mData != null ? mData.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return mData != null ? mData.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}
}
