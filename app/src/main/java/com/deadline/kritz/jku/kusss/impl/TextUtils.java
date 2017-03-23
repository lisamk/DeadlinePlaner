package com.deadline.kritz.jku.kusss.impl;

import com.deadline.kritz.jku.kusss.Term;

public class TextUtils {
	
	public static  boolean isEmpty(String s) {
		return s == null || s.length() == 0;
	}
	
	
	public static  boolean isEmpty(int i) {
		return i < 1;
	}
	
	
	public static  boolean isEmpty(Term t) {	
		return (t == null || isEmpty(t.getYear()) || t.getType() == null);
	}
}