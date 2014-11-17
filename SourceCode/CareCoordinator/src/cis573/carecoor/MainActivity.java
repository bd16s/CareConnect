package cis573.carecoor;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import cis573.carecoor.utils.Logger;
import cis573.carecoor.utils.PreferenceUtil;

/**
 * Modified by:
 * Naicheng Zhang on 11/10/14
 * Yucong Li on 12/10/14
 * 
 */

public class MainActivity extends BannerActivity {

	public static final String TAG = "MainActivity";

	private ViewPager mViewPager;

	private MainPagerAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Logger.setDebug(true);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		setBannerTitle(R.string.app_name);
		showBackButton(false);
		String pin = PreferenceUtil.get(getApplicationContext(), 0);
		if (pin != null & !pin.equals("")) {
			Intent intent = new Intent(getApplicationContext(),
					PinLockActivity.class);
			startActivityForResult(intent, 0);
		}
		initViews();
	}

	private void initViews() {
		mViewPager = (ViewPager) findViewById(R.id.main_pager);
		mAdapter = new MainPagerAdapter(getSupportFragmentManager(),
				MainActivity.this);
		mViewPager.setAdapter(mAdapter);

		// spinner to change to a certain page;
		Spinner spinner = (Spinner) findViewById(R.id.pager_spinner);
		String[] tmpStrArray = this.getResources().getStringArray(R.array.main_page_titles);
		ArrayList<String> strList= new ArrayList<String>();
		for(int i = 0; i < tmpStrArray.length; ++i) {
			strList.add(tmpStrArray[i]);
		}
		ArrayAdapter<String> pageSpinnerAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, strList);
		// Specify the layout to use when the list of choices appears
		pageSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(pageSpinnerAdapter);
		OnItemSelectedListener onSpinnerItemSelected = new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
				((TextView) parent.getChildAt(0)).setText("To:");
				mViewPager.setCurrentItem(position);
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {	
			}
		};
		spinner.setOnItemSelectedListener(onSpinnerItemSelected);
	}

	public static class MainPagerAdapter extends FragmentPagerAdapter {

		private String[] mPageTitles;

		public MainPagerAdapter(FragmentManager fm, Context context) {
			super(fm);
			mPageTitles = context.getResources().getStringArray(
					R.array.main_page_titles);
		}

		@Override
		public Fragment getItem(int arg0) {
			Fragment f = null;
			if (arg0 == 0) { // History
				f = new HistoryFragment();
			} else if (arg0 == 1) { // Contact
				f = new ContactFragment();
			} else if (arg0 == 2) { // Reminder
				f = new MedScheduleFragment();
			} else if (arg0 == 3) { // Appointment
				f = new AppointmentFragment();
			} else if (arg0 == 4) { // Alert
				f = new AlertConfFragment();
			} else if (arg0 == 5) { // Friends
				f = new FriendsFragment();
			}
//			} else if (arg0 == 6) { // Games
//				f = new GameFragment();
//			}
			/*
			 * { f = DummyFragment.newInstance(mPageTitles[arg0]); }
			 */
			return f;
		}

		@Override
		public int getCount() {
			return mPageTitles.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return mPageTitles[position];
		}
	}

	public static class DummyFragment extends Fragment {

		private String mTitle;

		public static DummyFragment newInstance(String title) {
			DummyFragment f = new DummyFragment();
			Bundle args = new Bundle();
			args.putString("title", title);
			f.setArguments(args);
			return f;
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			Bundle args = getArguments();
			if (args != null) {
				mTitle = args.getString("title");
			}
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.dummy_fragment, container,
					false);
			TextView tv = (TextView) view;
			if (mTitle != null) {
				tv.setText(mTitle);
			}
			return view;
		}

	}
}
