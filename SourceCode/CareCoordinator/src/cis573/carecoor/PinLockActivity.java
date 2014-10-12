package cis573.carecoor;

import cis573.carecoor.utils.PreferenceUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Modified
 * Created by dblank on 4/24/14.
 */
public class PinLockActivity extends BannerActivity {

	private String code;
	private StringBuilder testCode;
	private int count;
	private TextView pin;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.pin_lock_activity);
		setBannerTitle(R.string.pin_lock_name);
		code = PreferenceUtil.get(getApplicationContext(), 0);
		pin = (TextView) findViewById(R.id.pin);
		showBackButton(false);
		testCode = new StringBuilder();
		count = 0;
	}

	public void changeView() {
		StringBuilder temp = new StringBuilder();
		for (int i = 0; i < 4; i++) {
			if (i < count) {
				temp.append("*");
				temp.append(" ");
			} else {
				temp.append("_");
				temp.append(" ");
			}
		}
		pin.setText(temp.toString());
	}

	public void onPinButtonClick(View v) {
		Button pressedButton = (Button) v;
		String buttonText = pressedButton.getText().toString();
		testCode.append(buttonText);
		count++;
		changeView();
		if (count == 4) {
			testPin();
		}
	}

	private void testPin() {
		String testCodeStr = testCode.toString();
		if (testCodeStr.equals(code)) {
			success();
		} else {
			reset();
		}
	}

	private void success() {
		setResult(RESULT_OK);
		finish();
	}

	private void reset() {
		testCode = new StringBuilder();
		count = 0;
		pin.setText("");
	}
	
	@Override
	public void onBackPressed(){
	}
}
