package cis573.carecoor;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;
import cis573.carecoor.utils.PreferenceUtil;

/**
 * Created by: yucongli on 10/14/14
 * 
 * Modified by: yucongli on 10/15/14
 */
public class DatePickerFragment extends DialogFragment implements
		DatePickerDialog.OnDateSetListener {

	public static final int DOB = AlertConfFragment.DOB;
	TextView mEt;

	DatePickerFragment(TextView et) {
		super();
		mEt = et;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	public void onDateSet(DatePicker view, int year, int month, int day) {
		// Do something with the date chosen by the user
		String text = "" + ++month + "/" + day + "/" + year;
		PreferenceUtil.save(getActivity(), DOB, text);
		mEt.setText(PreferenceUtil.get(getActivity(), DOB));
	}
}
