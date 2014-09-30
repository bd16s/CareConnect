package cis573.carecoor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.hardware.Camera.CameraInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;
import cis573.carecoor.bean.Medicine;
import cis573.carecoor.bean.Schedule;
import cis573.carecoor.bean.Schedule.Time;
import cis573.carecoor.data.DataCenter;
import cis573.carecoor.data.MedicineCenter;
import cis573.carecoor.reminder.ReminderCenter;
import cis573.carecoor.utils.Const;
import cis573.carecoor.utils.Utils;

public class SetScheduleActivity extends BannerActivity {

	public static final String TAG = "SetScheduleActivity";
	
	private TextView mTvMedName;
	private Button mIvMedImage;
	private ImageView mIvPillImage;
	private EditText mTvMedInfo1;
	private EditText mTvMedInfo2;
	private TextView mTvTakeTimeSugg;
	private GridLayout mGlTakeTime;
	private TextView mTvTakeDaysSugg;
	private RadioGroup mRgDayIntv;
	private GridLayout mGlPickDays;
	private TextView mTvDurationSugg;
	private RadioGroup mRgDuration;
	private View mDurationSelector;
	private Button mBtnDurationDown;
	private Button mBtnDurationUp;
	private EditText mEtDuration;
	public File mCurrentPhoto;
	//private Camera camera;
	//private int cameraId = 0;
	
	private Medicine mMedicine = null;
	private List<Time> mTakeTimes = new ArrayList<Time>();
	private List<Integer> mTakeDays = new ArrayList<Integer>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMedicine = (Medicine) getIntent().getSerializableExtra(Const.EXTRA_MEDICINE);
		setContentView(R.layout.set_schedule_activity);
		setBannerTitle(R.string.title_new_schedule);
		 
		initViews();
		initTakeTimeGrid();
		initPickDaysGrid();
		
		showMedicineInfo();
	}

	private void initViews() {
		mTvMedName = (TextView) findViewById(R.id.set_schedule_medname);
		mIvMedImage = (Button) findViewById(R.id.set_schedule_medimage);
		//mIvPillImage = (ImageView) findViewById(R.id.set_schedule_medpill);
		//mIvPillImage.setVisibility(View.INVISIBLE);
		mTvMedInfo1 = (EditText) findViewById(R.id.set_schedule_medinfo1);
		mTvMedInfo2 = (EditText) findViewById(R.id.set_schedule_medinfo2);
		mGlTakeTime = (GridLayout) findViewById(R.id.set_schedule_taketime_grid);
		mTvTakeDaysSugg = (TextView) findViewById(R.id.set_schedule_takedays_suggest);
		mRgDayIntv = (RadioGroup) findViewById(R.id.set_schedule_day_intv);
		mRgDayIntv.setOnCheckedChangeListener(onDayIntvChanged);
		mGlPickDays = (GridLayout) findViewById(R.id.set_schedule_pickdays_grid);
		mTvDurationSugg = (TextView) findViewById(R.id.set_schedule_duration_suggest);
		mRgDuration = (RadioGroup) findViewById(R.id.set_schedule_duration);
		mRgDuration.setOnCheckedChangeListener(onDurationChanged);
		mDurationSelector = findViewById(R.id.set_schedule_duration_selector);
		mBtnDurationDown = (Button) findViewById(R.id.number_selector_minus);
		mBtnDurationDown.setOnClickListener(onDurationBtnClick);
		mBtnDurationUp = (Button) findViewById(R.id.number_selector_add);
		mBtnDurationUp.setOnClickListener(onDurationBtnClick);
		mEtDuration = (EditText) findViewById(R.id.number_selector_edittext);
		mEtDuration.addTextChangedListener(onDurationWatcher);
		mEtDuration.setText("1");
	}
	
	private void showMedicineInfo() {
		if(mMedicine == null) {
			return;
		}
		mTvMedName.setText(mMedicine.getName());
		//mIvMedImage.setImageResource(MedicineCenter.getMedicineImageRes(SetScheduleActivity.this, mMedicine));
		//mTvMedInfo1.setText(mMedicine.getDetailedName() + " - " + mMedicine.getCapacity());
		//mTvMedInfo2.setText(mMedicine.getInstructions());
		//mTvTakeTimeSugg.setText(getString(R.string.set_schedule_take_time_suggest, mMedicine.getTimes()));
		
		if(mMedicine.getInterval() == 0) {	// Every day
			mTvTakeDaysSugg.setText(R.string.set_schedule_take_days_everyday);
			mRgDayIntv.check(R.id.set_schedule_every_day);
		} else {	// x times per week
			mTvTakeDaysSugg.setText(getString(R.string.set_schedule_take_days_week, mMedicine.getInterval()));
			mRgDayIntv.check(R.id.set_schedule_pick_days);
		}
		
		if(mMedicine.getDuration() == 0) {	// Continuous
			mTvDurationSugg.setText(R.string.set_schedule_duration_sugg_continuous);
			mRgDuration.check(R.id.set_schedule_continuous);
		} else {
			int duration = mMedicine.getDuration();
			mTvDurationSugg.setText(getString(R.string.set_schedule_duration_sugg_days, duration));
			mRgDuration.check(R.id.set_schedule_setduration);
			mEtDuration.setText(String.valueOf(duration));
		}
	}
	
	private void initTakeTimeGrid() {
		ToggleButton tb;
		Calendar calendar = Calendar.getInstance(Locale.US);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		String text;
		for(int i = 0; i < 24; i++) {
			tb = (ToggleButton) View.inflate(SetScheduleActivity.this, R.layout.my_toggle_button, null);
			text = getTakeTimeText(calendar);
			tb.setTag(new Time(calendar.get(Calendar.HOUR_OF_DAY), 0));
			tb.setText(text);
			tb.setTextOff(text);
			tb.setTextOn(text);
			tb.setOnCheckedChangeListener(onTakeTimeBtnClicked);
			tb.setOnLongClickListener(onTakeTimeBtnLongClick);
			mGlTakeTime.addView(tb);
			
			calendar.add(Calendar.HOUR_OF_DAY, 1);
		}
	}
	
	private void initPickDaysGrid() {
		ToggleButton tb;
		Calendar calendar = Calendar.getInstance(Locale.US);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		String text;
		for(int i = 0; i < 7; i++) {
			tb = (ToggleButton) View.inflate(SetScheduleActivity.this, R.layout.my_toggle_button, null);
			tb.setTag(calendar.get(Calendar.DAY_OF_WEEK));
			text = Utils.getWeekNameShort(calendar.get(Calendar.DAY_OF_WEEK));
			tb.setText(text);
			tb.setTextOff(text);
			tb.setTextOn(text);
			tb.setOnCheckedChangeListener(onTakeDaysBtnClicked);
			mGlPickDays.addView(tb);
			calendar.add(Calendar.DAY_OF_WEEK, 1);
		}
	}
	
	private OnCheckedChangeListener onTakeTimeBtnClicked = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//			int hour = (Integer) buttonView.getTag();
			Time time = (Time) buttonView.getTag();
			if(isChecked) {
				mTakeTimes.add(time);
			} else {
				mTakeTimes.remove(time);
			}
		}
	};
	
	private OnLongClickListener onTakeTimeBtnLongClick = new OnLongClickListener() {
		@Override
		public boolean onLongClick(View v) {
			final ToggleButton btn = (ToggleButton) v;
			final Time time = (Time) btn.getTag();
			new TimePickerDialog(SetScheduleActivity.this, new OnTimeSetListener() {
				@Override
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					Time newTime = new Time(hourOfDay, minute);
					btn.setTag(newTime);
					String text = Utils.get12ClockTimeString(newTime).replace(' ', '\n');
					btn.setText(text);
					btn.setTextOn(text);
					btn.setTextOff(text);
					if(btn.isChecked()) {
						mTakeTimes.remove(time);
						mTakeTimes.add(newTime);
					} else {
						btn.setChecked(true);
					}
				}
			}, time.hour, time.minute, false).show();
			return false;
		}
	};

	private android.widget.RadioGroup.OnCheckedChangeListener onDayIntvChanged =
			new android.widget.RadioGroup.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch(checkedId) {
			case R.id.set_schedule_every_day:
				mGlPickDays.setVisibility(View.GONE);
				break;
			case R.id.set_schedule_pick_days:
				mGlPickDays.setVisibility(View.VISIBLE);
				break;
			}
		}
	};
	
	private OnCheckedChangeListener onTakeDaysBtnClicked = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			int week = (Integer) buttonView.getTag();
			if(isChecked) {
				mTakeDays.add(week);
			} else {
				mTakeDays.remove(mTakeDays.indexOf(week));
			}
		}
	};
	
	private android.widget.RadioGroup.OnCheckedChangeListener onDurationChanged =
			new android.widget.RadioGroup.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch(checkedId) {
			case R.id.set_schedule_continuous:
				mDurationSelector.setVisibility(View.GONE);
				break;
			case R.id.set_schedule_setduration:
				mDurationSelector.setVisibility(View.VISIBLE);
				break;
			}
		}
	};
	
	private OnClickListener onDurationBtnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int duration = Integer.parseInt(mEtDuration.getText().toString());
			switch(v.getId()) {
			case R.id.number_selector_minus:
				if(duration > 1) {
					mEtDuration.setText(String.valueOf(duration - 1));
				}
				break;
			case R.id.number_selector_add:
				mEtDuration.setText(String.valueOf(duration + 1));
				break;
			}
		}
	};
	
	private TextWatcher onDurationWatcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			if(s.length() == 0) {
				mEtDuration.setText("1");
			}
		}
	};
	
	private static String getTakeTimeText(Calendar calendar) {
		if(calendar == null) {
			return "";
		}
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int ampm = calendar.get(Calendar.AM_PM);
		return String.format(Locale.US, "%d:00\n%s", hour > 12 ? hour - 12: hour,
				ampm == Calendar.AM ? "AM" : "PM");
	}
	
	static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;

	public void onPhotoClick(View v) {
		Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
	        // Create the File where the photo should go
	        File photoFile = null;
	        try {
	            photoFile = createImageFile();
	        } catch (IOException ex) {
	        }
	        if (photoFile != null) {
	            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
	                    Uri.fromFile(photoFile));
	            startActivityForResult(takePictureIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	        }
	    }
	}
	
	String mCurrentPhotoPath;

	private File createImageFile() throws IOException {
	    // Create an image file name
		String imageFileName = "drugpic_" + mMedicine.getName().substring(0, 3).toLowerCase(Locale.US);
	    File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
	    File image = File.createTempFile(
	        imageFileName,  /* prefix */
	        ".jpg",         /* suffix */
	        storageDir      /* directory */
	    );
	    mMedicine.setPhotoPath(image);
	    mCurrentPhotoPath = "file:" + image.getAbsolutePath();
	    return image;
	}
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
	        //Bundle extras = data.getExtras();
	        //Bitmap imageBitmap = (Bitmap) extras.get("data");
	    	mIvMedImage.setVisibility(View.INVISIBLE);
	        //mIvPillImage.setVisibility(View.VISIBLE);      
	        //mIvPillImage.setImageBitmap(imageBitmap);
	    }
	}
	

	public void onOkClick(View v) {
		if(mTakeTimes.size() == 0) {
			showErrorDialog(getString(R.string.msg_no_take_hours));
			return;
		}
		
		if(mRgDayIntv.getCheckedRadioButtonId() == R.id.set_schedule_pick_days
				&& mTakeDays.size() == 0) {
			showErrorDialog(getString(R.string.msg_no_take_days));
			return;
		}
		if (mTvMedInfo1.getText() != null){
			mMedicine.setDetailedName(mTvMedInfo1.getText().toString());
		}
		if (mTvMedInfo2.getText() != null){
			mMedicine.setInstructions(mTvMedInfo2.getText().toString());
		}
		if (mCurrentPhoto != null){
			mMedicine.setPhotoPath(mCurrentPhoto);
		}
		Schedule schedule = new Schedule(new Date());
		schedule.setMedicine(mMedicine);
		Collections.sort(mTakeTimes);
		schedule.setTimes(mTakeTimes);
		if(mRgDayIntv.getCheckedRadioButtonId() == R.id.set_schedule_every_day) {
			schedule.setDays(null);
		} else if(mRgDayIntv.getCheckedRadioButtonId() == R.id.set_schedule_pick_days) {
			Collections.sort(mTakeDays);
			schedule.setDays(mTakeDays);
		}
		if(mRgDuration.getCheckedRadioButtonId() == R.id.set_schedule_continuous) {
			schedule.setDuration(0);
		} else if(mRgDuration.getCheckedRadioButtonId() == R.id.set_schedule_setduration) {
			schedule.setDuration(Integer.parseInt(mEtDuration.getText().toString()));
		}
		schedule.setTracking(true);
		
		DataCenter.addSchedule(SetScheduleActivity.this, schedule);
		ReminderCenter.addNextReminder(SetScheduleActivity.this, schedule);
		
		setResult(RESULT_OK);
		finish();
	}
	
	private void showErrorDialog(String msg) {
		new AlertDialog.Builder(SetScheduleActivity.this)
		.setTitle(R.string.dialog_title_error)
		.setMessage(msg)
		.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		}).show();
	}
}
