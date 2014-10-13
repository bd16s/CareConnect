package cis573.carecoor;

import cis573.carecoor.utils.PreferenceUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by:
 * yucongli on 10/12/14.
 */
public class PinSetActivity extends BannerActivity {

	private StringBuilder code;
	private int count;
	private TextView pin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pin_lock_activity);
		setBannerTitle(R.string.pin_lock_name);
		pin = (TextView) findViewById(R.id.pin);
		showBackButton(false);
		code = new StringBuilder();
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
		
		if("cancel".equals(buttonText)){
			delete();
			return;
		}
		
		code.append(buttonText);
		count++;
		changeView();
		if (count == 4) {
			success();
		}
	}

	private void delete(){
		int length = code.length();
		if(length <= 0){
			return;
		}else{
			code.setLength(length - 1);
			count--;
			changeView();
		}
	}

	private void success() {
		PreferenceUtil.save(getApplicationContext(), 0, String.valueOf(code));
//		setContentView(R.layout.alert_conf_fragment);
//		TextView pinSettingText = (TextView) findViewById(R.id.settings_pin_text);
//		pinSettingText.setText(code.toString());
		
		
		setResult(RESULT_OK);
		finish();
	}
	
	@Override
	public void onBackPressed(){
	}
}
