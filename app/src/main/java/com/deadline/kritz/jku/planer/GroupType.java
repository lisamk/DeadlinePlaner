package com.deadline.kritz.jku.planer;

import com.deadline.kritz.jku.kusss.Term;

abstract class GroupType {

	private final Term term;
	private final boolean hidden;
	public Object value;
	public String gid;

	public GroupType(Object value, String gid, Term term, boolean hidden) {
		this.value = value;
		this.gid = gid;
		this.term = term;
		this.hidden = hidden;
	}
	
	public abstract String getTitle();

	public Term getTerm() {
		return term;
	}

	public boolean isHidden() {
		return hidden;
	}
}
