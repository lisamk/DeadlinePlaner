package com.deadline.kritz.jku.planer;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Deadline  implements Serializable  {
	private static final long serialVersionUID = 1L;

	private long id;
	private Date date;
	private String title;
	private String description;
	private Group group;

	public Deadline(String title, String description, Group group, Date date) {
		this.date = date;
		this.title = title;
		this.description = description;
		this.group = group;
	}

	public Deadline(long id, String title, String description, String group, Date date) {
		this.id = id;
		this.date = date;
		this.title = title;
		this.description = description;
		for(Group g : Planer.getInstance().getGroups()) if(g.getTitle().equals(group)) this.group = g;
	}

	public Date getDate() {
		return date;
	}

	public String getTitle() {
		return title;
	}
	
	public void setGroup(Group group) {
		this.group = group;
	}

	public Group getGroup() {
		return group;
	}

	public String getGroupName() {
		return group.getTitle();
	}

	public long getId() {
		return id;
	}
	
	public String getDescription() {
		return description;
	}
	
	@Override
	public String toString() {
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");
		if(group==null) return df.format(date) + " - " + title + " " + description;
		return df.format(date) + " - " + group.getTitle();
	}


}
