package cis573.carecoor.bean;

import java.io.Serializable;

public class Contact implements Serializable {
	private static final long serialVersionUID = 5240658907134977572L;
	private String name;
	private String phone;
	
	public Contact() { }
	
	public Contact(String name, String phone) {
		this.name = name;
		this.phone = phone;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
}
