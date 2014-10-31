package cis573.carecoor;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import cis573.carecoor.bean.Medicine;
import cis573.carecoor.bean.TakeRecord;
import cis573.carecoor.data.DataCenter;
import cis573.carecoor.utils.Utils;

public class HistoryFragment extends Fragment {

	public static final String TAG = "HistoryFragment";

	public static final int ONTIME_OFFSET = 10;

	private ListView mListView;
	private HistoryAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAdapter = new HistoryAdapter(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.history_fragment, container,
				false);
		mListView = (ListView) view.findViewById(R.id.history_list);
		TextView tvEmpty = (TextView) view.findViewById(R.id.history_empty);
		mListView.setEmptyView(tvEmpty);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		mAdapter.setTakeRecordList(DataCenter.getTakeRecords(getActivity()));
		mListView.setAdapter(mAdapter);
	}

	public static class HistoryAdapter extends BaseAdapter {

		private Context mContext;
		private List<TakeRecord> mTakeRecordList;

		public HistoryAdapter(Context context) {
			this.mContext = context;
		}

		public void setTakeRecordList(List<TakeRecord> takeRecordList) {
			this.mTakeRecordList = takeRecordList;
		}

		@Override
		public int getCount() {
			return mTakeRecordList != null ? mTakeRecordList.size() : 0;
		}

		@Override
		public Object getItem(int position) {
			return mTakeRecordList != null ? mTakeRecordList.get(position)
					: null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder vh;
			if (convertView == null) {
				convertView = View.inflate(mContext, R.layout.history_item,
						null);
				vh = new ViewHolder();
				vh.text1 = (TextView) convertView
						.findViewById(R.id.history_text1);
				vh.text2 = (TextView) convertView
						.findViewById(R.id.history_text2);
				vh.delay = (TextView) convertView
						.findViewById(R.id.history_delay);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}

			TakeRecord item = (TakeRecord) getItem(position);
			if (item != null) {
				Medicine med = item.getMedicine();
				vh.text1.setText(med.getName());
				StringBuilder sb = new StringBuilder();
				sb.append(
						mContext.getString(R.string.history_taken_at,
								Utils.getDateTimeString(item.getTakeTime())))
						.append('\n')
						.append(mContext.getString(R.string.history_planned,
								Utils.getTimeString(item.getPlannedTime())));
				vh.text2.setText(sb.toString());

				int delay = item.getDelayedMinutes();
				if (delay <= ONTIME_OFFSET) {
					vh.delay.setText(R.string.history_ontime);
					vh.delay.setTextColor(mContext.getResources().getColor(
							R.color.green_text));
					vh.delay.setCompoundDrawablesWithIntrinsicBounds(
							R.drawable.icon_check, 0, 0, 0);
				} else {
					String delayStr = "";
					if (delay >= 60) {
						delayStr = "" + (delay / 60) + " hour(s)";
					} else {
						delayStr = "" + delay + " minutes";
					}
					vh.delay.setText(mContext.getString(R.string.history_late,
							delayStr));
					vh.delay.setTextColor(mContext.getResources().getColor(
							R.color.orange_light_text));
					vh.delay.setCompoundDrawablesWithIntrinsicBounds(
							R.drawable.icon_exclaimation, 0, 0, 0);
				}
			}
			return convertView;
		}

		private static class ViewHolder {
			TextView text1;
			TextView text2;
			TextView delay;
		}
	}
}
