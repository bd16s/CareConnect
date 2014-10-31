package cis573.carecoor;

import cis573.carecoor.bean.Contact;
import cis573.carecoor.email.GMailSender;
import cis573.carecoor.utils.Const;
import cis573.carecoor.utils.Logger;
import cis573.carecoor.utils.PreferenceUtil;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Modified by: yucongli on 10/12/14. yucongli on 10/14/14. yucongli on
 * 10/15/14.
 */
public class AlertConfFragment extends Fragment implements
		OnSettingsChangedListener {

	/****************************************************************
	 *************************** GLOBALS ****************************
	 ****************************************************************/

	public static final String TAG = "AlertConfFragment";

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

	// Associated views and dialogs
	private View[] views;
	private TextView[] textViews;
	private SettingsDialogFragment2[] dialogs;

	private CheckBox mCbCall;
	private CheckBox mCbText;

	private RadioGroup mRgGender;
	private RadioGroup mRgEthnicity;

	private Button mBtnExport;

	private static String[] settingStrings = { "pin", "primary", "secondary",
			"user", "password", "provider", "name", "dob", "city", "state",
			"height", "weight", "allergies", "insurance" };

	/****************************************************************
	 ********************** INITIALIZATION ***********************
	 ****************************************************************/

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Create the dialogs
		dialogs = new SettingsDialogFragment2[NUM_DIALOGS];

		for (int i = 0; i < NUM_DIALOGS; i++) {
			dialogs[i] = SettingsDialogFragment2.newInstance(i);
		}
		Toast toast = Toast.makeText(getActivity(),
				"Tap the orange\t?\tbutton above for instructions.",
				Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,
				0, 0);
		toast.show();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.alert_conf_fragment, container,
				false);

		// Create the views and hook them up to click handlers
		views = new View[NUM_DIALOGS];
		for (int i = 0; i < NUM_DIALOGS; i++) {
			views[i] = view.findViewById(getViewID(i));
			views[i].setOnClickListener(mOnTextClick);
		}

		textViews = new TextView[NUM_DIALOGS];
		for (int i = 0; i < NUM_DIALOGS; i++) {
			textViews[i] = (TextView) view.findViewById(getTextViewID(i));
			setDisplayedAlertText(i, "");
		}

		initCheckBoxes(view);
		initRadioGroups(view);

		mBtnExport = (Button) view.findViewById(R.id.settings_export_btn);
		mBtnExport.setOnClickListener(mOnExportClick);

		return view;
	}

	private void initCheckBoxes(View view) {
		// Configure the checkboxes
		mCbCall = (CheckBox) view.findViewById(R.id.alert_call_cb);
		mCbCall.setChecked(PreferenceUtil.getEnableAlertCall(getActivity()));
		mCbCall.setOnCheckedChangeListener(mOnCheckedChanged);
		mCbText = (CheckBox) view.findViewById(R.id.alert_text_cb);
		mCbText.setChecked(PreferenceUtil.getEnableAlertText(getActivity()));
		mCbText.setOnCheckedChangeListener(mOnCheckedChanged);
	}

	private void initRadioGroups(View view) {
		initGenderGroup(view);
		initEthnicityGroup(view);
	}

	private void initGenderGroup(View view) {
		mRgGender = (RadioGroup) view.findViewById(R.id.settings_gender);
		mRgGender.setOnCheckedChangeListener(mOnGenderRadioChanged);

		String gender = PreferenceUtil.getUserGender(getActivity());
		if (gender == null)
			return;
		else if (gender.equals(getString(R.string.settings_male)))
			mRgGender.check(R.id.settings_gender_male);
		else if (gender.equals(getString(R.string.settings_female)))
			mRgGender.check(R.id.settings_gender_female);
		else if (gender.equals(getString(R.string.settings_other)))
			mRgGender.check(R.id.settings_gender_other);
	}

	private void initEthnicityGroup(View view) {
		mRgEthnicity = (RadioGroup) view.findViewById(R.id.settings_ethnicity);
		mRgEthnicity.setOnCheckedChangeListener(mOnEthnicityRadioChanged);

		String race = PreferenceUtil.getUserEthnicity(getActivity());
		if (race == null)
			return;
		else if (race.equals(getString(R.string.settings_white)))
			mRgEthnicity.check(R.id.settings_ethnicity_white);
		else if (race.equals(getString(R.string.settings_black)))
			mRgEthnicity.check(R.id.settings_ethnicity_black);
		else if (race.equals(getString(R.string.settings_latino)))
			mRgEthnicity.check(R.id.settings_ethnicity_latino);
		else if (race.equals(getString(R.string.settings_asian)))
			mRgEthnicity.check(R.id.settings_ethnicity_asian);
		else if (race.equals(getString(R.string.settings_native_american)))
			mRgEthnicity.check(R.id.settings_ethnicity_native_american);
		else if (race.equals(getString(R.string.settings_other)))
			mRgEthnicity.check(R.id.settings_ethnicity_other);
	}

	/****************************************************************
	 ********************* ACTION HANDLERS **********************
	 ****************************************************************/

	// Launch the input dialogues on when Views are clicked
	private OnClickListener mOnTextClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int code = getCode(v.getId());
			switch (code) {
			case PIN:
				Intent intent = new Intent(getActivity()
						.getApplicationContext(), PinSetActivity.class);
				startActivityForResult(intent, 0);
				textViews[code].setText("****");
				break;
			case DOB:
				DatePickerFragment datePickFrag = new DatePickerFragment(
						textViews[code]);
				datePickFrag.show(getFragmentManager(), settingStrings[code]);
				break;
			default:
				dialogs[code].setTargetFragment(AlertConfFragment.this, 0);
				dialogs[code].show(getFragmentManager(), settingStrings[code]);
				break;
			}
		}
	};

	private OnCheckedChangeListener mOnCheckedChanged = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			switch (buttonView.getId()) {
			case R.id.alert_call_cb:
				PreferenceUtil.saveEnableAlertCall(getActivity(), isChecked);
				break;
			case R.id.alert_text_cb:
				PreferenceUtil.saveEnableAlertText(getActivity(), isChecked);
				break;
			}
		}
	};

	private RadioGroup.OnCheckedChangeListener mOnGenderRadioChanged = new RadioGroup.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// Check which radio button was clicked
			switch (checkedId) {
			case R.id.settings_gender_male:
				PreferenceUtil.saveUserGender(getActivity(),
						getString(R.string.settings_male));
				break;
			case R.id.settings_gender_female:
				PreferenceUtil.saveUserGender(getActivity(),
						getString(R.string.settings_female));
				break;
			case R.id.settings_gender_other:
				PreferenceUtil.saveUserGender(getActivity(),
						getString(R.string.settings_other));
				break;
			default:
				return;
			}
		}
	};

	private RadioGroup.OnCheckedChangeListener mOnEthnicityRadioChanged = new RadioGroup.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// Check which radio button was clicked
			switch (checkedId) {
			case R.id.settings_ethnicity_white:
				PreferenceUtil.saveUserEthnicity(getActivity(),
						getString(R.string.settings_white));
				break;
			case R.id.settings_ethnicity_black:
				PreferenceUtil.saveUserEthnicity(getActivity(),
						getString(R.string.settings_black));
				break;
			case R.id.settings_ethnicity_latino:
				PreferenceUtil.saveUserEthnicity(getActivity(),
						getString(R.string.settings_latino));
				break;
			case R.id.settings_ethnicity_asian:
				PreferenceUtil.saveUserEthnicity(getActivity(),
						getString(R.string.settings_asian));
				break;
			case R.id.settings_ethnicity_native_american:
				PreferenceUtil.saveUserEthnicity(getActivity(),
						getString(R.string.settings_native_american));
				break;
			case R.id.settings_ethnicity_other:
				PreferenceUtil.saveUserEthnicity(getActivity(),
						getString(R.string.settings_other));
				break;
			default:
				return;
			}
		}
	};

	private OnClickListener mOnExportClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			final String user = PreferenceUtil.get(getActivity(), USER_EMAIL);
			String password = PreferenceUtil.get(getActivity(), EMAIL_PASSWORD);
			final String provider = PreferenceUtil.get(getActivity(),
					PROVIDER_EMAIL);

			if (user == null || password == null || provider == null) {
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(getActivity(),
								getString(R.string.msg_export_failed),
								Toast.LENGTH_SHORT).show();
					}
				});
			} else if ("".equals(user) || "".equals(password)
					|| "".equals(provider)) {
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(getActivity(),
								getString(R.string.msg_export_failed),
								Toast.LENGTH_SHORT).show();
					}
				});
			} else {
				final String body = getFormattedData();
				final GMailSender sender = new GMailSender(user, password);
				new AsyncTask<Void, Void, Void>() {
					@Override
					public Void doInBackground(Void... arg) {
						try {
							sender.sendMail(
									getString(R.string.msg_export_subject),
									body, user, provider);
							getActivity().runOnUiThread(new Runnable() {
								public void run() {
									Toast.makeText(
											getActivity(),
											getString(R.string.msg_export_success),
											Toast.LENGTH_SHORT).show();
								}
							});
						} catch (Exception e) {
							Log.e("SendMail", e.getMessage(), e);
							// getActivity().runOnUiThread(new Runnable() {
							// public void run() {
							// Toast.makeText(getActivity(),
							// getString(R.string.msg_export_failed),
							// Toast.LENGTH_SHORT).show();
							// }
							// });
						}
						return null;
					}
				}.execute();
			}
		}
	};

	private String getFormattedData() {
		Activity context = getActivity();
		String data = null;
		String formattedData = "{";

		if ((data = PreferenceUtil.get(context, NAME)) != null)
			formattedData += "Name : " + data + "\n ";
		if ((data = PreferenceUtil.get(context, DOB)) != null)
			formattedData += "Date of Birth : " + data + "\n ";
		if ((data = PreferenceUtil.getUserGender(context)) != null)
			formattedData += "Gender : " + data + "\n ";
		if ((data = PreferenceUtil.getUserEthnicity(context)) != null)
			formattedData += "Ethnicity : " + data + "\n ";
		if ((data = PreferenceUtil.get(context, CITY)) != null)
			formattedData += "City : " + data + "\n ";
		if ((data = PreferenceUtil.get(context, STATE)) != null)
			formattedData += "State : " + data + "\n ";
		if ((data = PreferenceUtil.get(context, HEIGHT)) != null)
			formattedData += "Height : " + data + "feet\n ";
		if ((data = PreferenceUtil.get(context, WEIGHT)) != null)
			formattedData += "Weight : " + data + "lb\n ";
		// TODO: Allergies should be a list...
		if ((data = PreferenceUtil.get(context, ALLERGIES)) != null)
			formattedData += "Allergies : " + data + "\n ";
		if ((data = PreferenceUtil.get(context, INSURANCE)) != null)
			formattedData += "Insurance : " + data + "\n ";
		formattedData += "}";

		return formattedData;
	}

	// Called when returning from importing a contact
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Logger.i(TAG, String.format(
				"Back from pick contact requestCode=%d, resultCode=%d",
				requestCode, resultCode));
		if (resultCode == Activity.RESULT_OK) {
			Contact contact = null;
			if (data != null) {
				contact = (Contact) data
						.getSerializableExtra(Const.EXTRA_CONTACT);
			}
			String setting = null;
			if (contact != null) {
				switch (requestCode) {
				case USER_EMAIL:
					// TODO: modify contact for email
					// setting = contact.getEmail();
					break;
				case NAME:
					setting = contact.getName();
					break;
				case DOB:
					// TODO: modify contact for DOB
					// setting = contact.getDOB();
					break;
				case PRIMARY_PHONE:
					setting = contact.getPhone();
					break;
				case SECONDARY_PHONE:
					setting = contact.getPhone();
					break;
				case PROVIDER_EMAIL:
					// setting = contact.getEmail();
					break;
				default:
					return;
				}
				onSettingsChanged(setting, requestCode);
			}
		}
	}

	public void onSettingsChanged(String setting, int code) {
		if (!checkSettings(code, setting))
			return;

		PreferenceUtil.save(getActivity(), code, setting);
		if (code == PIN) {
			textViews[code].setText("****");
		} else {
			textViews[code].setText(setting);
		}
	}

	private boolean checkSettings(int code, String setting) {
		switch (code) {
		default:
			return true;
		}
	}

	@Override
	public void onImportFromContact(int type) {
		Intent intent = new Intent(getActivity(),
				PickFromContactsActivity.class);
		startActivityForResult(intent, type);
	}

	//
	// //
	// **************************************************************************
	// // ************************** DIALOG FRAGMENTS **************************
	// //
	// **************************************************************************
	//
	// public static class SettingsDialogFragment extends DialogFragment {
	//
	// private EditText mEtInfo;
	// private Button mBtnImport;
	// private int mType;
	//
	// public static SettingsDialogFragment newInstance(int type) {
	// SettingsDialogFragment f = new SettingsDialogFragment();
	// Bundle args = new Bundle();
	// args.putInt("type", type);
	// f.setArguments(args);
	// return f;
	// }
	//
	// @Override
	// public void onCreate(Bundle savedInstanceState) {
	// super.onCreate(savedInstanceState);
	// Bundle args = getArguments();
	// if (args != null) {
	// mType = args.getInt("type");
	// }
	// }
	//
	// @Override
	// public Dialog onCreateDialog(Bundle savedInstanceState) {
	// View view = View
	// .inflate(getActivity(), R.layout.dialog_email, null);
	// mEtInfo = (EditText) view
	// .findViewById(R.id.dialog_add_info_edittext);
	// setInputType(view);
	// mBtnImport = (Button) view
	// .findViewById(R.id.dialog_add_email_import_btn);
	// mBtnImport.setOnClickListener(onImportClick);
	//
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
	// }
	//
	// private OnClickListener onImportClick = new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// OnSettingsChangedListener listener = (OnSettingsChangedListener)
	// getTargetFragment();
	// if (listener != null) {
	// listener.onImportFromContact(mType);
	// }
	// getDialog().dismiss();
	// }
	// };
	//
	// private void setInputType(View view) {
	// TextView label = (TextView) view
	// .findViewById(R.id.dialog_add_info_label);
	// if (mType == USER_EMAIL || mType == PROVIDER_EMAIL) {
	// mEtInfo.setInputType(InputType.TYPE_CLASS_TEXT
	// | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
	// label.setText(getString(R.string.dialog_add_email_label));
	// } else if (mType == PRIMARY_PHONE || mType == SECONDARY_PHONE) {
	// mEtInfo.setInputType(InputType.TYPE_CLASS_PHONE);
	// label.setText(getString(R.string.dialog_add_phone_label));
	// } else if (mType == NAME) {
	// mEtInfo.setInputType(InputType.TYPE_CLASS_TEXT);
	// label.setText(getString(R.string.dialog_add_name_label));
	// } else if (mType == PIN) {
	// mEtInfo.setInputType(InputType.TYPE_CLASS_TEXT
	// | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
	// label.setText(getString(R.string.dialog_add_password_label));
	// } else if (mType == DOB) {
	// mEtInfo.setInputType(InputType.TYPE_CLASS_DATETIME
	// | InputType.TYPE_DATETIME_VARIATION_DATE);
	// label.setText(getString(R.string.dialog_add_dob_label));
	// }
	// }
	//
	// private String getTitle() {
	// switch (mType) {
	// case PIN:
	// return getActivity().getString(R.string.settings_pin_label);
	// case PRIMARY_PHONE:
	// return getActivity().getString(R.string.alertconf_primary_num);
	// case SECONDARY_PHONE:
	// return getActivity()
	// .getString(R.string.alertconf_secondary_num);
	// case USER_EMAIL:
	// return getActivity().getString(
	// R.string.settings_user_email_label);
	// case EMAIL_PASSWORD:
	// return getActivity().getString(
	// R.string.settings_user_email_password_label);
	// case PROVIDER_EMAIL:
	// return getActivity().getString(
	// R.string.settings_provider_email_label);
	// case NAME:
	// return getActivity().getString(
	// R.string.settings_user_name_label);
	// case DOB:
	// return getActivity()
	// .getString(R.string.settings_user_dob_label);
	// case HEIGHT:
	// return getActivity().getString(R.string.settings_height_label);
	// case WEIGHT:
	// return getActivity().getString(R.string.settings_weight_label);
	// case CITY:
	// return getActivity().getString(R.string.settings_city_label);
	// case STATE:
	// return getActivity().getString(R.string.settings_state_label);
	// case ALLERGIES:
	// return getActivity().getString(
	// R.string.settings_allergies_label);
	// case INSURANCE:
	// return getActivity().getString(
	// R.string.settings_insurance_label);
	// }
	// return null;
	// }
	// }

	/*****************************************************************
	 ******************** HELPER FUNCTIONS ***********************
	 *****************************************************************/

	// Set the text in the input boxes to the value passed in as text
	// or the saved value if none was provided
	private void setDisplayedAlertText(int code, String text) {
		TextView textView = textViews[code];

		if (text == null || "".equals(text)) {
			text = PreferenceUtil.get(getActivity(), code);
		}

		if (text != null)
			textView.setText(text);

		if (code == PIN && text != null && !"".equals(text)) {
			textView.setText("****");
		}
	}

	private static int getCode(int id) {
		switch (id) {
		case R.id.settings_pin:
			return PIN;
		case R.id.alert_conf_primary:
			return PRIMARY_PHONE;
		case R.id.alert_conf_secondary:
			return SECONDARY_PHONE;
		case R.id.settings_user_email:
			return USER_EMAIL;
		case R.id.settings_user_email_password:
			return EMAIL_PASSWORD;
		case R.id.settings_provider_email:
			return PROVIDER_EMAIL;
		case R.id.settings_user_name:
			return NAME;
		case R.id.settings_user_dob:
			return DOB;
		case R.id.settings_city:
			return CITY;
		case R.id.settings_state:
			return STATE;
		case R.id.settings_height:
			return HEIGHT;
		case R.id.settings_weight:
			return WEIGHT;
		case R.id.settings_allergies:
			return ALLERGIES;
		case R.id.settings_insurance:
			return INSURANCE;
		default:
			return -1;
		}
	}

	private static int getViewID(int code) {
		switch (code) {
		case PIN:
			return R.id.settings_pin;
		case PRIMARY_PHONE:
			return R.id.alert_conf_primary;
		case SECONDARY_PHONE:
			return R.id.alert_conf_secondary;
		case USER_EMAIL:
			return R.id.settings_user_email;
		case EMAIL_PASSWORD:
			return R.id.settings_user_email_password;
		case PROVIDER_EMAIL:
			return R.id.settings_provider_email;
		case NAME:
			return R.id.settings_user_name;
		case DOB:
			return R.id.settings_user_dob;
		case CITY:
			return R.id.settings_city;
		case STATE:
			return R.id.settings_state;
		case HEIGHT:
			return R.id.settings_height;
		case WEIGHT:
			return R.id.settings_weight;
		case ALLERGIES:
			return R.id.settings_allergies;
		case INSURANCE:
			return R.id.settings_insurance;
		default:
			return -1;
		}
	}

	private static int getTextViewID(int code) {
		switch (code) {
		case PIN:
			return R.id.settings_pin_text;
		case PRIMARY_PHONE:
			return R.id.alert_conf_primary_text;
		case SECONDARY_PHONE:
			return R.id.alert_conf_secondary_text;
		case USER_EMAIL:
			return R.id.settings_user_email_text;
		case EMAIL_PASSWORD:
			return R.id.settings_user_email_password_text;
		case PROVIDER_EMAIL:
			return R.id.settings_provider_email_text;
		case NAME:
			return R.id.settings_user_name_text;
		case DOB:
			return R.id.settings_user_dob_text;
		case HEIGHT:
			return R.id.settings_height_text;
		case WEIGHT:
			return R.id.settings_weight_text;
		case CITY:
			return R.id.settings_city_text;
		case STATE:
			return R.id.settings_state_text;
		case ALLERGIES:
			return R.id.settings_allergies_text;
		case INSURANCE:
			return R.id.settings_insurance_text;
		default:
			return -1;
		}
	}
}
