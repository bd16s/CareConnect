package cis573.carecoor.reminder;

import java.util.Date;
import java.util.List;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import cis573.carecoor.bean.Appointment;
import cis573.carecoor.bean.Schedule;
import cis573.carecoor.data.DataCenter;
import cis573.carecoor.data.ScheduleCenter;
import cis573.carecoor.utils.Const;
import cis573.carecoor.utils.Logger;
import cis573.carecoor.utils.Utils;

public class ReminderCenter {

	public static final String TAG = "ReminderCenter";
	
	public static void addNextReminder(Context context, Schedule schedule) {
		if(schedule == null) {
			return;
		}
		Date next = ScheduleCenter.getNextUntakeScheduledTime(context, schedule);
		if(next != null) {
			Intent intent = getReminderIntent(context, schedule);
			if(intent != null) {
				AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
				PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_NO_CREATE);
				if(pIntent != null) {	// Cancel previous alarm
					alarm.cancel(pIntent);
				}
				alarm.set(AlarmManager.RTC_WAKEUP, next.getTime(),
						PendingIntent.getBroadcast(context, 0, intent, 0));
				Logger.i(TAG, "Alarm set, ring time: " + next.toString());
			}
		}
	}
	
	public static void addReminder(Context context, Appointment appointment) {
		if(appointment == null) {
			return;
		}
		Date next = Utils.getAppointmentRemindDate(appointment);
		if(next != null) {
			if(next.before(new Date())) {
				return;
			}
			Intent intent = getReminderIntent(context, appointment);
			if(intent != null) {
				AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
				PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_NO_CREATE);
				if(pIntent != null) {	// Cancel previous alarm
					alarm.cancel(pIntent);
				}
				alarm.set(AlarmManager.RTC_WAKEUP, next.getTime(),
						PendingIntent.getBroadcast(context, 0, intent, 0));
				Logger.i(TAG, "Alarm set, ring time: " + next.toString());
			}
		}
	}
	
	public static void cancelAlarm(Context context, Schedule schedule) {
		if(schedule == null) {
			return;
		}
		Intent intent = getReminderIntent(context, schedule);
		if(intent != null) {
			PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_NO_CREATE);
			if(pIntent != null) {
				AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
				alarm.cancel(pIntent);
			}
		}
	}
	
	public static void cancelAlarm(Context context, Appointment appointment) {
		if(appointment == null) {
			return;
		}
		Intent intent = getReminderIntent(context, appointment);
		if(intent != null) {
			PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_NO_CREATE);
			if(pIntent != null) {
				AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
				alarm.cancel(pIntent);
			}
		}
	}
	
	public static void resetAllReminders(Context context) {
		List<Schedule> schedules = DataCenter.getSchedules(context);
		if(schedules != null) {
			for(Schedule schedule : schedules) {
				addNextReminder(context, schedule);
			}
		}
		
		List<Appointment> appointments = DataCenter.getAppointments(context, null);
		if(appointments != null) {
			for(Appointment appointment : appointments) {
				addReminder(context, appointment);
			}
		}
	}
	
	private static Intent getReminderIntent(Context context, Schedule schedule) {
		if(schedule == null) {
			return null;
		}
		Intent intent = new Intent(Const.ACTION_REMINDER_ALARM);
		intent.setPackage(context.getPackageName());
		intent.setData(Uri.parse(getReminderId(schedule)));
		intent.putExtra(Const.EXTRA_SCHEDULE, schedule);
		return intent;
	}
	
	private static Intent getReminderIntent(Context context, Appointment appointment) {
		if(appointment == null) {
			return null;
		}
		Intent intent = new Intent(Const.ACTION_REMINDER_ALARM);
		intent.setPackage(context.getPackageName());
		intent.setData(Uri.parse(getReminderId(appointment)));
		intent.putExtra(Const.EXTRA_APPOINTMENT, appointment);
		return intent;
	}
	
	public static String getReminderId(Schedule schedule) {
		return String.format("reminder://schedule#create=%d", schedule.getCreateDate().getTime());
	}
	
	public static String getReminderId(Appointment appointment) {
		return String.format("reminder://appointment#create=%d", appointment.getDate().getTime());
	}
}
