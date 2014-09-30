package cis573.carecoor.bean;

import java.io.Serializable;

public class Game implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String TAG = "Game";
	
	private String name;
	private String[] pkgNames;
	private String icon;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String[] getPackageNames() {
		return pkgNames;
	}
	public void setPackageNames(String[] pkgNames) {
		this.pkgNames = pkgNames;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
}
