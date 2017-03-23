package com.deadline.kritz.jku.kusss.impl;

import java.util.Date;

import com.deadline.kritz.jku.kusss.Assessment;
import com.deadline.kritz.jku.kusss.AssessmentType;
import com.deadline.kritz.jku.kusss.Course;
import com.deadline.kritz.jku.kusss.CourseType;
import com.deadline.kritz.jku.kusss.Curricula;
import com.deadline.kritz.jku.kusss.Exam;
import com.deadline.kritz.jku.kusss.Grade;
import com.deadline.kritz.jku.kusss.KusssFactory;
import com.deadline.kritz.jku.kusss.Term;

public class KusssFactoryImpl implements KusssFactory {

	@Override
	public  Course getCourse(Term term, String courseId, String title, int cid,
			String lecturer, double ects, double sws, CourseType courseType,
			String classCode) {
		return new CourseImpl(term, courseId, title, cid, lecturer, ects, sws,
				courseType, classCode);
	}

	@Override
	public  Curricula getCurricula(int cid, String title, String uni,
			Date dtStart, Date dtEnd, boolean isStandard, boolean steopDone,
			boolean active) {
		return new CurriculaImpl(cid, title, uni, dtStart, dtEnd, isStandard,
				steopDone, active);
	}

	@Override
	public  Assessment getAssessment(Date date, String title, Term term,
			String courseId, Grade grade, int cid,
			AssessmentType assessmentType, String classCode, double ects,
			double sws, CourseType courseType) {
		return new AssessmentImpl(date, title, term, courseId, grade, cid,
				assessmentType, classCode, ects, sws, courseType);
	}

	@Override
	public  Exam getExam(String courseId, Term term, Date dtStart, Date dtEnd,
			String location, String title, int cid, String description,
			String info, boolean isRegistered, int maxParticipants,
			int participants, Date registrationDtStart, Date registrationDtEnd,
			Date unregistrationDt) {
		return new ExamImpl(courseId, term, dtStart, dtEnd, location, title,
				cid, description, info, isRegistered, maxParticipants,
				participants, registrationDtStart, registrationDtEnd,
				unregistrationDt);

	}

}