package com.deadline.kritz.jku.planer;

abstract class GroupType {
	
	public Object value;
	public String gid;

	public GroupType(Object value, String gid) {
		this.value = value;
		this.gid = gid;
	}
	
	public abstract String getTitle();
}
