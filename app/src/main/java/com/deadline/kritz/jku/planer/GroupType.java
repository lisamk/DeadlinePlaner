package com.deadline.kritz.jku.planer;

abstract class GroupType {
	
	public Object value;

	public GroupType(Object value) {
		this.value = value;
	}
	
	public abstract String getTitle();
}
