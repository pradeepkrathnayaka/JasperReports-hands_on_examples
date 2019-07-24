package com.rmpksoft.jrlesson.styles;

public class User {
	
	public String id;
	public String name;
	public String gender;
	
	
	public User() {
	}
	public User(String id, String name, String gender) {
		this.id = id;
		this.name = name;
		this.gender = gender;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	

}
