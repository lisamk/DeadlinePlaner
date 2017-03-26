package com.deadline.kritz.jku;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.deadline.kritz.jku.planer.Group;
import com.deadline.kritz.jku.planer.Planer;

import java.util.List;

public class GroupsAdapter extends ArrayAdapter<Group> {
    List<Group> modelItems = null;
    Context context;

    public GroupsAdapter(Context context, List<Group> resource) {
        super(context, R.layout.row, resource);
        this.context = context;
        this.modelItems = resource;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.row, parent, false);
        TextView name = (TextView) convertView.findViewById(R.id.textViewGroup);
        final CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBoxGroup);
        String text = modelItems.get(position).getTitle();
        if(text.length()>30) text = text.substring(0,30)+"...";
        name.setText(text);
        if (modelItems.get(position).isHidden()) cb.setChecked(true);
        else cb.setChecked(false);

        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Planer.getInstance().replaceGroup(modelItems.get(position), cb.isChecked());
                Toast.makeText(context, "Saved.", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }
}