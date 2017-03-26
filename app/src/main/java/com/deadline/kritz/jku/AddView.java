package com.deadline.kritz.jku;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.deadline.kritz.jku.planer.Deadline;
import com.deadline.kritz.jku.planer.Group;
import com.deadline.kritz.jku.planer.Planer;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import static com.deadline.kritz.jku.planer.Planer.SDF;
import static com.deadline.kritz.jku.planer.Planer.TIME;
import static com.deadline.kritz.jku.planer.Planer.DATE;

public class AddView extends Dialog {

    private static EditText date;
    private static EditText time;

    public AddView(@NonNull Context context) {
        super(context);

        setContentView(R.layout.add_popup);
        setup(null);
    }

    public AddView(Activity context, Deadline deadlineItem) {
        super(context);
        setContentView(R.layout.add_popup);
        setup(deadlineItem);
    }

    private void setup(final Deadline deadlineItem) {
        final HashMap<String, Group> groups = new HashMap<String, Group>();
        if(deadlineItem!=null) for(Group g : Planer.getInstance().getAllGroups()) groups.put(g.getTitle(), g);
        else for(Group g : Planer.getInstance().getGroups()) groups.put(g.getTitle(), g);

        final Spinner spGroups = (Spinner) findViewById(R.id.spinner_groups);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, groups.keySet().toArray(new String[groups.size()]));
        spGroups.setAdapter(adapter);
        if(deadlineItem!=null) {
            List<String> str = new ArrayList<>(groups.keySet());
            spGroups.setSelection(str.indexOf(deadlineItem.getGroupName()));
        }

        final AutoCompleteTextView title = (AutoCompleteTextView) findViewById(R.id.title);
        adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.select_dialog_singlechoice,
                new String[] {"Exercise", "Project", "Exam", "Birthday"});
        title.setThreshold(1);
        title.setAdapter(adapter);
        title.setImeOptions(EditorInfo.IME_ACTION_DONE);
        if(deadlineItem!=null) title.setText(deadlineItem.getTitle());

        final EditText description = (EditText) findViewById(R.id.description);
        description.setImeOptions(EditorInfo.IME_ACTION_DONE);
        if(deadlineItem!=null) description.setText(deadlineItem.getDescription());

        date = (EditText) findViewById(R.id.date);
        date.setOnClickListener(new DateOnClickListener());
        date.setImeOptions(EditorInfo.IME_ACTION_DONE);
        if(deadlineItem!=null) date.setText(DATE.format(deadlineItem.getDate()));

        time = (EditText) findViewById(R.id.time);
        time.setOnClickListener(new TimeOnClickListener());
        time.setImeOptions(EditorInfo.IME_ACTION_DONE);
        if(deadlineItem!=null) time.setText(TIME.format(deadlineItem.getDate()));

        Button btnDate = (Button) findViewById(R.id.btnDate);
        btnDate.setOnClickListener(new DateOnClickListener());

        Button btnTime = (Button) findViewById(R.id.btnTime);
        btnTime.setOnClickListener(new TimeOnClickListener());

        Button btnSave = (Button) findViewById(R.id.btnSubmit);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(time.getText()==null || date.getText()==null); //TODO print warning
                else {
                    String d = date.getText()+" "+time.getText();
                    try {
                        Group g = groups.get(spGroups.getSelectedItem().toString());
                        if(deadlineItem!=null) Planer.getInstance().deleteDeadline(deadlineItem);
                        Planer.getInstance().addDeadline(g, new Deadline(Planer.ID, title.getText().toString(), description.getText().toString(), g, SDF.parse(d)));
                        DeadlineView.update();
                        AddView.this.dismiss();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        if(deadlineItem!=null) btnSave.setText("Save");
        description.requestFocus();
    }

    private class DateOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            DatePickerDialog.OnDateSetListener dpd = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Calendar c = GregorianCalendar.getInstance();
                    c.set(year, monthOfYear, dayOfMonth);
                    date.setText("" + DATE.format(c.getTime()));
                }
            };
            Calendar calendar = new GregorianCalendar();
            DatePickerDialog d = new DatePickerDialog(AddView.this.getContext(), dpd, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            d.show();
        }
    }

    private class TimeOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            TimePickerDialog.OnTimeSetListener dpd = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    Calendar c = GregorianCalendar.getInstance();
                    c.set(0,0,0,hourOfDay,minute);
                    time.setText("" + TIME.format(c.getTime()));
                }
            };
            TimePickerDialog d = new TimePickerDialog(AddView.this.getContext(), dpd, 0, 0, true);
            d.show();
        }
    }
}
