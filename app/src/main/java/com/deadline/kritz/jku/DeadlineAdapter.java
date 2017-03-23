package com.deadline.kritz.jku;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.deadline.kritz.jku.planer.Deadline;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by danie on 18.03.2017.
 */

public class DeadlineAdapter extends ArrayAdapter<Deadline> {

    private Context context;
    private int layoutResourceId;
    private List<Deadline> data = null;

    public DeadlineAdapter(Context context, int layoutResourceId, List<Deadline> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        DeadlineHolder holder = null;

        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new DeadlineHolder();
            holder.group = (TextView) row.findViewById(R.id.tvDeadlineGroup);
            holder.title = (TextView) row.findViewById(R.id.tvDeadlineTitle);
            holder.date = (TextView) row.findViewById(R.id.tvDeadlineDate);
            holder.time = (TextView) row.findViewById(R.id.tvDeadlineTime);

            row.setTag(holder);
        }
        else {
            holder = (DeadlineHolder) row.getTag();
        }

        Deadline deadline = data.get(position);

        /* titel = Exercise, Exam
         * description = on click
         * Group = Kurs
         */

        String groupName = deadline.getGroupName();
        if(groupName.length() > 25) {
            groupName = groupName.substring(0, 25);
            groupName += "...";
        }

        holder.group.setText(groupName);
        holder.title.setText(deadline.getTitle());

        DateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat dfTime = new SimpleDateFormat(("HH:mm"));

        holder.date.setText(dfDate.format(deadline.getDate()));
        holder.time.setText(dfTime.format(deadline.getDate()));

        return row;
    }

    private static class DeadlineHolder {
        TextView group;
        TextView title;
        TextView date;
        TextView time;
    }

}
