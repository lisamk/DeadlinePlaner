package com.deadline.kritz.jku.planer;

import android.content.Context;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.deadline.kritz.jku.kusss.Course;
import com.deadline.kritz.jku.kusss.EventType;
import com.deadline.kritz.jku.kusss.Exam;
import com.deadline.kritz.jku.kusss.KusssHandler;
import com.deadline.kritz.jku.kusss.KusssHandlers;
import com.deadline.kritz.jku.kusss.Term;
import com.deadline.kritz.jku.kusss.Term.TermType;
import com.deadline.kritz.jku.planer.database.DataSource;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Property;

public class Planer {

	public static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	public static SimpleDateFormat DATE = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat TIME = new SimpleDateFormat("HH:mm");

	private final DataSource datasource;
	private List<Deadline> deadlines;
	private List<String> terms = new ArrayList<>();
	private List<Group> groups = new ArrayList<>();
	private static Planer p;
	public static long ID = 0;

	public Planer(Context context) {
		datasource = new DataSource(context);
		datasource.open();
		groups = datasource.getAllGroups();
		for(Group g : groups) if(!terms.contains(g.getTerm().toString())) {
			System.out.println(g.getTerm().toString());
			terms.add(g.getTerm().toString());
		}
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
		List<Group> gList = new ArrayList<>();
		for(Group g : groups) if(!g.isHidden()) {
			gList.add(g);
		}
		return gList;
	}

	public List<Group> getGroupsOfTerm(String term) {
		List<Group> gList = new ArrayList<>();
		for(Group g : groups) if(g.getTerm().toString().equals(term)) {
			gList.add(g);
		}
		return gList;
	}

	public List<Group> getAllGroups() {
		return groups;
	}

	public void setDeadlinesFromDB() {
		datasource.open();
		deadlines = datasource.getAllDeadlines();
		for(Deadline d : deadlines) {
			if(d.getId()>ID) ID = d.getId()+1;
		}
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
		datasource.deleteDeadline(deadlineItem);
		datasource.close();
	}

	public boolean login(String id, String pw) {
		KusssHandler kusss = KusssHandlers.getInstance();
		if(!kusss.login(id, pw)) return false;

		return true;
	}

	public void setGroupsFromKusss() {
		int month = Calendar.getInstance().get(Calendar.MONTH);
		TermType type = (month>=Calendar.MARCH && month<Calendar.OCTOBER) ? TermType.SUMMER : TermType.WINTER;
		int year = Calendar.getInstance().get(Calendar.YEAR);
		List<Term> l = new ArrayList<>();
		for(TermType t : new TermType[] {TermType.SUMMER, TermType.WINTER}) {
			for (int i = Calendar.getInstance().get(Calendar.YEAR) - 1; i <= Calendar.getInstance().get(Calendar.YEAR); i++) {
				l.clear();
				l.add(new Term(i, t));
				List<Course> courses = KusssHandlers.getInstance().getCourses(l);
				if(courses.size()>0) {
					if(!terms.contains(l.get(0).toString())) terms.add(l.get(0).toString());
					datasource.open();
					for (final Course c : courses) {
						boolean add = true;
						for (Group g : groups)
							if (g.getGid().equals(c.getCourseId())) {
								add = false;
								break;
							}
						if (!add) continue;
						groups.add(datasource.createGroup(new Group(new GroupType(c, c.getCourseId(), l.get(0),
								!(type.equals(l.get(0).getType()) && year == l.get(0).getYear())) {
							@Override
							public String getTitle() {
								return c.getTitle();
							}
						})));
					}
					datasource.close();
				}
			}
		}
	}

	public void setDeadlinesFromKusss() {
		for (final Exam e : KusssHandlers.getInstance().getExams()) {
			boolean add = true;
			Group group = null;
			for(Deadline d : deadlines) {
				if (d.getDate().equals(e.getDtStart()) && e.getTitle().startsWith(d.getGroupName())) {
					add = false; break;
				}
			}
			if(!add) continue;
			for(Group g : groups) if(g.getGid().equals(e.getCourseId())) {
					group = g; add = false; break;
			}
			/*if(add) {
				group = new Group(e.getCourseId(), e.getTitle(), true, e);
				datasource.open();
				groups.add(datasource.createGroup(group));
				datasource.close();
			}*/
			if(!add) addDeadline(group, new Deadline(ID++, "Exam", e.getDescription(), e.getCourseId(), e.getDtStart()));
		}
	}

	public List<String> getTerms() {
		Collections.sort(terms);
		return terms;
	}

	public void replaceGroup(Group group, boolean checked) {
		datasource.open();
		groups.remove(group);
		datasource.deleteGroup(group);
		groups.add(datasource.createGroup(new Group(group.getGid(), group.getTitle(), checked, group.getTerm())));
		datasource.close();
	}
}
