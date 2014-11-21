package cis573.carecoor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FamilyViewFragment extends Fragment {
	public static final String TAG = "FamilyViewFragment";
	private ListView mListView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Toast toast = Toast.makeText(getActivity(),
				"Tap the orange\t?\tbutton above for instructions.",
				Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,
				0, 0);
		toast.show();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.family_view_fragment, container,
				false);
		mListView = (ListView) view.findViewById(R.id.history_list);
		TextView tvEmpty = (TextView) view.findViewById(R.id.history_empty);
		mListView.setEmptyView(tvEmpty);
		return view;
	}
}
