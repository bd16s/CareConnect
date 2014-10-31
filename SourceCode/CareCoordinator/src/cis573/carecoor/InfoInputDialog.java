package cis573.carecoor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import cis573.carecoor.utils.MyToast;

/**
 * Created by: yucongli on 10/14/14.
 * 
 * Modified by: yucongli on 10/15/14.
 */
public class InfoInputDialog extends AlertDialog {

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

	Context mContext = null;
	View mView = null;
	CharSequence mTitle = null;
	int mType = 0;
	EditText mEtInfo = null;
	Fragment mFragment = null;
	AlertDialog mDialog = null;

	protected InfoInputDialog(Context context, View view, CharSequence title,
			int type, EditText mEtInfo, Fragment fragment) {
		super(context);
		mContext = context;
		mView = view;
		mTitle = title;
		mType = type;
		this.mEtInfo = mEtInfo;
		mFragment = fragment;
		init();
	}

	protected void init() {
		mDialog = new Builder(mContext).setView(mView).setTitle(mTitle)
				.setPositiveButton(android.R.string.ok, null)
				.setNegativeButton(android.R.string.cancel, null).create();

		mDialog.setOnShowListener(new DialogInterface.OnShowListener() {

			@Override
			public void onShow(final DialogInterface dialog) {

				Button b = ((AlertDialog) dialog)
						.getButton(AlertDialog.BUTTON_POSITIVE);
				b.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						String text = mEtInfo.getText().toString();
						if (!validityCheck(text)) {
							MyToast.show(mContext, R.string.invalid_input);
						} else {
							OnSettingsChangedListener listener = (OnSettingsChangedListener) mFragment;
							if (listener != null) {
								listener.onSettingsChanged(text, mType);
							}
							dialog.dismiss();
						}
					}

					private boolean validityCheck(String text) {
						boolean b = true;
						switch (mType) {
						case PRIMARY_PHONE:
						case SECONDARY_PHONE:
							b = text.matches("^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$");
							break;
						case USER_EMAIL:
						case PROVIDER_EMAIL:
							b = text.matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");
							break;
						case NAME:
						case CITY:
						case STATE:
							b = text.matches("^[a-zA-Z ]+$");
							break;
						case HEIGHT:
						case WEIGHT:
							b = text.matches("^\\d+$|^\\d+\\.\\d+$");
							break;
						}
						return b;
					}
				});

				b = ((AlertDialog) dialog)
						.getButton(AlertDialog.BUTTON_NEGATIVE);
				b.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						dialog.dismiss();
					}
				});
			}
		});
	}

	protected AlertDialog getDialog() {
		return mDialog;
	}
}
