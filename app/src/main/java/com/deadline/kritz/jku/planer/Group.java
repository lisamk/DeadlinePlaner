package com.deadline.kritz.jku.planer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.deadline.kritz.jku.kusss.Course;
import com.deadline.kritz.jku.kusss.CourseType;
import com.deadline.kritz.jku.kusss.Term;

public class Group implements Comparable<Group> {
	private List<Deadline> deadlines = new ArrayList<>();
	private GroupType type;
	private long id;
	private String gid;
	private String title;
	private boolean hidden;
	private Term term;

	public Group(long id, String gid, String title, boolean hidden, Term term) {
		this.id = id;
		this.gid = gid;
		this.title = title;
		this.hidden = hidden;
		this.term = term;
	}

	public Group(String gid, String title, boolean hidden, Term term) {
		this.gid = gid;
		this.title = title;
		this.hidden = hidden;
		this.term = term;
	}

	public Group(GroupType type) {
		this(0, type.gid, type.getTitle(), type.isHidden(), type.getTerm());
	}
	
	private boolean isCourse() {
		return !(type instanceof Course);
	}
	
	public Deadline[] getDeadlines() {
		return deadlines.toArray(new Deadline[deadlines.size()]);
	}
	
	public String getTitle() {
		return title;
	}
	
	public CourseType getCourseType() {
		if(!isCourse()) return null;
		Course c = (Course) type;
		return c.getCourseType();
	}

	public long getId() {
		return id;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public void addDeadline(Deadline deadline) {
		deadlines.add(deadline);
		deadline.setGroup(this);
		Collections.sort(deadlines, new Comparator<Deadline>() {
			@Override
			public int compare(Deadline d1, Deadline d2) {
				return d1.getDate().compareTo(d2.getDate());
			}
		});
	}

	@Override
	public int compareTo(Group group) {
		if(deadlines.size()==0 && group.deadlines.size()==0) return 0;
		else if(deadlines.size()==0 && group.deadlines.size()!=0) return 1;
		else if(deadlines.size()!=0 && group.deadlines.size()==0) return -1;
		else return deadlines.get(0).getDate().compareTo(group.deadlines.get(0).getDate());
	}

    public void deleteDeadline(Deadline deadlineItem) {
		deadlines.remove(deadlineItem);
    }

	public String getGid() {
		return gid;
	}

	public Term getTerm() {
		return term;
	}
}
