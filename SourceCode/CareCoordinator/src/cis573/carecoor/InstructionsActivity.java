package cis573.carecoor;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * Modified
 * 
 * @author xuxu
 */

public class InstructionsActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inst_activity);
	}

	public void quit(View view) {
		finish();
	}
}
