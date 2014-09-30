package cis573.carecoor.bean;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Schedule implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3425227164092067303L;

	public static final String TAG = "Schedule";
	
	private Date createDate;
	private Medicine medicine;
//	private List<Integer> hours;
	private List<Time> times;
	private List<Integer> days;
	private int duration;
	private boolean tracking;
	
	public Schedule(Date createDate) {
		this.createDate = createDate;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Medicine getMedicine() {
		return medicine;
	}
	public void setMedicine(Medicine medicine) {
		this.medicine = medicine;
	}
	public List<Time> getTimes() {
		return times;
	}
	public void setTimes(List<Time> times) {
		this.times = times;
	}
	//	public List<Integer> getHours() {
//		return hours;
//	}
//	public void setHours(List<Integer> hours) {
//		this.hours = hours;
//	}
	public List<Integer> getDays() {
		return days;
	}
	public void setDays(List<Integer> days) {
		this.days = days;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public boolean isTracking() {
		return tracking;
	}
	public void setTracking(boolean tracking) {
		this.tracking = tracking;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Schedule) {
			Schedule s = (Schedule) o;
			return createDate.getTime() == s.getCreateDate().getTime()
					&& medicine.getId() == s.getMedicine().getId();
		}
		return false;
	}
	
	public static class Time implements Comparable<Time>, Serializable {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -8800508215876351137L;
		public final int hour;
		public final int minute;
		
		public Time(int hour, int minute) {
			this.hour = hour;
			this.minute = minute;
		}
		
		@Override
		public int compareTo(Time another) {
			int result = hour - another.hour;
			if(result == 0) {
				result = minute - another.minute;
			}
			return result;
		}

		@Override
		public boolean equals(Object o) {
			if(o instanceof Time) {
				Time t = (Time) o;
				return hour == t.hour && minute == t.minute;
			} else if(o instanceof Date) {
				Calendar cal = Calendar.getInstance(Locale.US);
				cal.setTime((Date) o);
				return hour == cal.get(Calendar.HOUR_OF_DAY) && minute == cal.get(Calendar.MINUTE);
			} else if(o instanceof Calendar) {
				Calendar c = (Calendar) o;
				return hour == c.get(Calendar.HOUR_OF_DAY) && minute == c.get(Calendar.MINUTE);
			}
			return false;
		}
	}
}
