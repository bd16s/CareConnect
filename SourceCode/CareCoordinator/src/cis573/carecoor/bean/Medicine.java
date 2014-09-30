package cis573.carecoor.bean;

import java.io.File;
import java.io.Serializable;

import android.graphics.Bitmap;

public class Medicine implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String TAG = "Medicine";
	
	private int id = 0;
	private String name= null;
	private String detailedName= null;
	private String instructions= null;
	private String capacity= null;
	private int dose= 0;
	private int times= 0;
	private int interval= 0;
	private int duration= 0;
	private int rank= 0;
	//private Bitmap bitmap = null;
	private File photo = null;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDetailedName() {
		return detailedName;
	}

	public void setDetailedName(String detailedName) {
		this.detailedName = detailedName;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public String getCapacity() {
		return capacity;
	}

	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}

	public int getDose() {
		return dose;
	}

	public void setDose(int dose) {
		this.dose = dose;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	public int getRank() {
		return rank;
	}
	
	public void setRank (int rank){
		this.rank = rank;
	}

	public File getPhotoPath() {
		return photo;
	}
	
	public void setPhotoPath(File path) {
		this.photo = path;
	}
	
	/*
	public Bitmap getBitmap() {
		return bitmap;
	}
	
	public void setBitmap(Bitmap map) {
		this.bitmap = map;
	}*/
	
}
