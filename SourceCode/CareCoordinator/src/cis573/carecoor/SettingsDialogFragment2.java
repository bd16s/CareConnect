package cis573.carecoor;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by: yucongli on 10/14/14.
 * 
 */

// **************************************************************************
// ************************** DIALOG FRAGMENTS **************************
// **************************************************************************

public class SettingsDialogFragment2 extends DialogFragment {

	// Info codes (for arrays, etc...)
	public static final int PIN = 0;

	public static final int PRIMARY_PHONE = 1;
	public static final int SECONDARY_PHONE = 2;

	// Alert Options...
	public static final int USER_EMAIL = 3;
	public static final int EMAIL_PASSWORD = 4;
	public static final int PROVIDER_EMAIL = 5;

	public static final int NAME = 6;
	public static final int DOB = 7;
	// private static final int GENDER = 4;
	// private static final int ETHNICITY = 5;
	public static final int CITY = 8;
	public static final int STATE = 9;
	public static final int HEIGHT = 10;
	public static final int WEIGHT = 11;
	public static final int ALLERGIES = 12;
	public static final int INSURANCE = 13;
	public static final int NUM_DIALOGS = 14;

	private EditText mEtInfo;
	private Button mBtnImport;
	private int mType;

	public static SettingsDialogFragment2 newInstance(int type) {
		SettingsDialogFragment2 f = new SettingsDialogFragment2();
		Bundle args = new Bundle();
		args.putInt("type", type);
		f.setArguments(args);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		if (args != null) {
			mType = args.getInt("type");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		View view = View.inflate(getActivity(), R.layout.dialog_email, null);
		mEtInfo = (EditText) view.findViewById(R.id.dialog_add_info_edittext);
		setInputType(view);
		mBtnImport = (Button) view
				.findViewById(R.id.dialog_add_email_import_btn);
		mBtnImport.setOnClickListener(onImportClick);

		InfoInputDialog infoD = new InfoInputDialog(getActivity(), view,
				getTitle(), mType, mEtInfo, getTargetFragment());
		return infoD.getDialog();

		// return new AlertDialog.Builder(getActivity())
		// .setView(view)
		// .setTitle(getTitle())
		// .setPositiveButton(android.R.string.ok,
		// new DialogInterface.OnClickListener() {
		// @Override
		// public void onClick(DialogInterface dialog,
		// int which) {
		// String text = mEtInfo.getText().toString();
		//
		// if (!validityCheck(text)) {
		// MyToast.show(getActivity().getApplicationContext(),
		// String.valueOf(mType));
		// } else {
		// OnSettingsChangedListener listener = (OnSettingsChangedListener)
		// getTargetFragment();
		// if (listener != null) {
		// listener.onSettingsChanged(text, mType);
		// }
		// }
		// }
		//
		// private boolean validityCheck (String text) {
		// switch (mType) {
		// case 3:
		// return text.matches("^\\d+$");
		// default:
		// return true;
		// }
		// }
		// })
		// .setNegativeButton(android.R.string.cancel,
		// new DialogInterface.OnClickListener() {
		// @Override
		// public void onClick(DialogInterface dialog,
		// int which) {
		// return;
		// }
		// }).create();
	}

	private OnClickListener onImportClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			OnSettingsChangedListener listener = (OnSettingsChangedListener) getTargetFragment();
			if (listener != null) {
				listener.onImportFromContact(mType);
			}
			getDialog().dismiss();
		}
	};

	private void setInputType(View view) {
		TextView label = (TextView) view
				.findViewById(R.id.dialog_add_info_label);
		if (mType == USER_EMAIL || mType == PROVIDER_EMAIL) {
			mEtInfo.setInputType(InputType.TYPE_CLASS_TEXT
					| InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
			label.setText(getString(R.string.dialog_add_email_label));
		} else if (mType == PRIMARY_PHONE || mType == SECONDARY_PHONE) {
			mEtInfo.setInputType(InputType.TYPE_CLASS_PHONE);
			label.setText(getString(R.string.dialog_add_phone_label));
		} else if (mType == NAME) {
			mEtInfo.setInputType(InputType.TYPE_CLASS_TEXT);
			label.setText(getString(R.string.dialog_add_name_label));
		} else if (mType == PIN) {
			mEtInfo.setInputType(InputType.TYPE_CLASS_TEXT
					| InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
			label.setText(getString(R.string.dialog_add_password_label));
		} else if (mType == DOB) {
			mEtInfo.setInputType(InputType.TYPE_CLASS_DATETIME
					| InputType.TYPE_DATETIME_VARIATION_DATE);
			label.setText(getString(R.string.dialog_add_dob_label));
		}
	}

	private String getTitle() {
		switch (mType) {
		case PIN:
			return getActivity().getString(R.string.settings_pin_label);
		case PRIMARY_PHONE:
			return getActivity().getString(R.string.alertconf_primary_num);
		case SECONDARY_PHONE:
			return getActivity().getString(R.string.alertconf_secondary_num);
		case USER_EMAIL:
			return getActivity().getString(R.string.settings_user_email_label);
		case EMAIL_PASSWORD:
			return getActivity().getString(
					R.string.settings_user_email_password_label);
		case PROVIDER_EMAIL:
			return getActivity().getString(
					R.string.settings_provider_email_label);
		case NAME:
			return getActivity().getString(R.string.settings_user_name_label);
		case DOB:
			return getActivity().getString(R.string.settings_user_dob_label);
		case HEIGHT:
			return getActivity().getString(R.string.settings_height_label);
		case WEIGHT:
			return getActivity().getString(R.string.settings_weight_label);
		case CITY:
			return getActivity().getString(R.string.settings_city_label);
		case STATE:
			return getActivity().getString(R.string.settings_state_label);
		case ALLERGIES:
			return getActivity().getString(R.string.settings_allergies_label);
		case INSURANCE:
			return getActivity().getString(R.string.settings_insurance_label);
		}
		return null;
	}
}
