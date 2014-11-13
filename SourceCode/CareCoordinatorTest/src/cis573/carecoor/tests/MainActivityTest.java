package cis573.carecoor.tests;

import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import cis573.carecoor.MainActivity;
import cis573.carecoor.R;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

	MainActivity mMainActivity;
	Spinner mSpinner;
	SpinnerAdapter mAdapter;
	public static final int ADAPTER_COUNT = 7;
	public static final int INITIAL_POSITION = 0;
	public static final int TEST_POSITION = 5;
	private String mSelection;
	private int mPos;
	  
	public MainActivityTest() {
	    super(MainActivity.class);
	}
	
	public MainActivityTest(Class<MainActivity> activityClass) {
		super(activityClass);
	}

	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(false);
		mMainActivity = getActivity();
		mSpinner = (Spinner) mMainActivity.findViewById(R.id.pager_spinner);
		mAdapter = mSpinner.getAdapter();
	}
	
	public void testPreconditions() {
	    assertNotNull("mMainActivity is null", mMainActivity);
	    assertTrue(mSpinner.getOnItemSelectedListener() != null);
	    assertTrue(mAdapter != null);
	    assertEquals(mAdapter.getCount(), ADAPTER_COUNT);
	}
	
	public void testSpinnerUIFifthSelection() {	
		mMainActivity.runOnUiThread(
	      new Runnable() {
	        public void run() {
	          mSpinner.requestFocus();
	          mSpinner.setSelection(INITIAL_POSITION);
	        } // end of run() method definition
	      } // end of anonymous Runnable object instantiation
	    ); // end of invocation of runOnUiThread
		
		this.sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);
	    for (int i = 1; i <= TEST_POSITION; i++) {
	      this.sendKeys(KeyEvent.KEYCODE_DPAD_DOWN);
	    } // end of for loop

	    this.sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);
	    
	    mPos = mSpinner.getSelectedItemPosition();
	    mSelection = (String) mSpinner.getItemAtPosition(mPos);
	    
//	    TextView resultView =
//	      (TextView) mMainActivity.findViewById(R.id.SpinnerResult);

//	    String resultText = (String) resultView.getText();
	    String resultText = "Friends";
	    
	    assertEquals(resultText, mSelection);

	  } // end of testSpinnerUI() method definition
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}

}
