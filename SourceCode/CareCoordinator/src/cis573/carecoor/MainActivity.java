package cis573.carecoor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cis573.carecoor.utils.Logger;
import cis573.carecoor.utils.PreferenceUtil;

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
			// switch(arg0){
			// case 0:
			// f = new AlertConfFragment();
			// case 1:
			// f = new ContactFragment();
			// case 2:
			// f = new MedScheduleFragment();
			// case 3:
			// f = new HistoryFragment();
			// case 4:
			// f = new AppointmentFragment();
			// case 5:
			// f = new FriendsFragment();
			// case 6:
			// f = new GameFragment();
			// default:
			//
			// }
			if (arg0 == 0) { // Alert
				f = new AlertConfFragment();
			} else if (arg0 == 1) { // Contact
				f = new ContactFragment();
			} else if (arg0 == 2) { // Reminder
				f = new MedScheduleFragment();
			} else if (arg0 == 3) { // History
				f = new HistoryFragment();
			} else if (arg0 == 4) { // Appointment
				f = new AppointmentFragment();
			} else if (arg0 == 5) { // Friends
				f = new FriendsFragment();
			} else if (arg0 == 6) { // Games
				f = new GameFragment();
			}/*
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
