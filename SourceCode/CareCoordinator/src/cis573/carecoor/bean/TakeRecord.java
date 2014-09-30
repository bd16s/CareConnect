package cis573.carecoor.bean;

import java.io.Serializable;
import java.util.Date;

public class TakeRecord implements Serializable {

 	/**
	 * 
	 */
	private static final long serialVersionUID = -3389156036735869960L;

	public static final String TAG = "TakeRecord";
	
	private Date schCreateTime;
	private Medicine medicine;
	private Date takeTime;
	private Date plannedTime;

	public TakeRecord(Schedule schedule, Date takeTime, Date plannedTime) {
		this.schCreateTime = schedule.getCreateDate();
		this.medicine = schedule.getMedicine();
		this.takeTime = takeTime;
		this.plannedTime = plannedTime;
	}

	public boolean belongsTo(Schedule schedule) {
		return this.schCreateTime.getTime() == schedule.getCreateDate().getTime();
	}

	public Medicine getMedicine() {
		return medicine;
	}

	public void setMedicine(Medicine medicine) {
		this.medicine = medicine;
	}

	public Date getTakeTime() {
		return takeTime;
	}
	public void setTakeTime(Date takeTime) {
		this.takeTime = takeTime;
	}

	public Date getPlannedTime() {
		return plannedTime;
	}

	public void setPlannedTime(Date plannedTime) {
		this.plannedTime = plannedTime;
	}

	public int getDelayedMinutes() {
		if(takeTime == null || plannedTime == null) {
			return 0;
		}
		return (int) ((takeTime.getTime() - plannedTime.getTime()) / 1000 / 60);
	}

}
