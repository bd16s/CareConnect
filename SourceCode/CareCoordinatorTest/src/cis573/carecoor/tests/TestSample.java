package cis573.carecoor.tests;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;
import cis573.carecoor.MainActivity;
import cis573.carecoor.R;

public class TestSample extends ActivityInstrumentationTestCase2<MainActivity> {

	MainActivity mMainActivity;
	TextView mFirstTestText;
	
	public TestSample() {
	    super(MainActivity.class);
	}
	
	public TestSample(Class<MainActivity> activityClass) {
		super(activityClass);
	}

	protected void setUp() throws Exception {
		super.setUp();
		mMainActivity = getActivity();
		mFirstTestText = (TextView) mMainActivity.findViewById(R.id.banner_title_text);
	}
	
	public void testPreconditions() {
	    assertNotNull("mFirstTestActivity is null", mMainActivity);
	    assertNotNull("mFirstTestText is null", mFirstTestText);
	}

	public void testMyFirstTestTextView_labelText() {
	    final String expected =
	            mMainActivity.getString(R.string.app_name);
	    final String actual = mFirstTestText.getText().toString();
	    assertEquals(expected, actual);
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}

}
