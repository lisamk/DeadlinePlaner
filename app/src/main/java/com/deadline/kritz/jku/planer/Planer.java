package com.deadline.kritz.jku.planer;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.deadline.kritz.jku.kusss.Course;
import com.deadline.kritz.jku.kusss.KusssHandler;
import com.deadline.kritz.jku.kusss.KusssHandlers;
import com.deadline.kritz.jku.kusss.Term;
import com.deadline.kritz.jku.kusss.Term.TermType;
import com.deadline.kritz.jku.planer.database.DataSource;

public class Planer {

	public static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	public static SimpleDateFormat DATE = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat TIME = new SimpleDateFormat("HH:mm");

	private final DataSource datasource;
	private List<Deadline> deadlines;
	private List<Group> groups = new ArrayList<>();
	private static Planer p;

	public Planer(Context context) {
		datasource = new DataSource(context);
		datasource.open();
		groups = datasource.getAllGroups();
		datasource.close();
	}
	
	public void addDeadline(Group group, Deadline deadline) {
		group.addDeadline(deadline);
		Collections.sort(groups, new Comparator<Group>() {
			@Override
			public int compare(Group g1, Group g2) {
				return g1.compareTo(g2);
			}
		});
		deadlines.add(deadline);
		datasource.open();
		datasource.createDeadline(deadline);
		datasource.close();
	}

	public List<Deadline> getDeadlines() {
		Collections.sort(deadlines, new Comparator<Deadline>() {
			@Override
			public int compare(Deadline d1, Deadline d2) {
				return d1.getDate().compareTo(d2.getDate());
			}
		});
		return deadlines;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setDeadlinesFromDB() {
		datasource.open();
		deadlines = datasource.getAllDeadlines();
		datasource.close();
	}

	public static Planer getInstance(Context context) {
		if(p==null) p = new Planer(context);
		return p;
	}

	public static Planer getInstance() {
		return p;
	}

	public void deleteDeadline(Deadline deadlineItem) {
		deadlines.remove(deadlineItem);
		deadlineItem.getGroup().deleteDeadline(deadlineItem);
		datasource.open();
		datasource.createDeadline(deadlineItem);
		datasource.close();
	}

	public boolean login(String id, String pw) {
		KusssHandler kusss = KusssHandlers.getInstance();
		if(!kusss.login(id, pw)) return false;

		List<Term> l = new ArrayList<>();
		l.add(new Term(2017, TermType.SUMMER));

		datasource.open();
		for (final Course c : kusss.getCourses(l)) {
			groups.add(datasource.createGroup(new Group(new GroupType(c) {
				@Override
				public String getTitle() {
					return c.getTitle();
				}
			})));
		}
		datasource.close();
		return true;
	}
}