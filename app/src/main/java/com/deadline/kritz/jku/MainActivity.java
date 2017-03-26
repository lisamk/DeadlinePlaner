package com.deadline.kritz.jku;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.deadline.kritz.jku.kusss.Term;
import com.deadline.kritz.jku.planer.Planer;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static Dialog groupDialog;
    private List<View> views = new ArrayList<>();
    private static int currentView = R.id.content_deadline;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new AddView(MainActivity.this);
                dialog.show();
            }
        });
/*
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();*/

        /*NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/

        View main = findViewById(currentView);
        main.setVisibility(View.VISIBLE);
       // navigationView.getMenu().getItem(currentView==R.id.content_deadline ? 0 : 1).setChecked(true);

        // add all the other views
        views.add(findViewById(R.id.content_deadline));

        // setup all views
        DeadlineView.setup(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_grous) {
            manageGroups(MainActivity.this);
            Toast.makeText(MainActivity.this, "GROUPS", Toast.LENGTH_LONG).show();
            return true;
        }
        else if (id == R.id.action_kusss) {
            DeadlineView.login(MainActivity.this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void changeView(int id) {
        if(currentView==id) return;
        for(View v : views) {
            if(v.getId()==id) v.setVisibility(View.VISIBLE);
            else v.setVisibility(View.GONE);
        }
        currentView = id;
    }

    public static void manageGroups(final Activity activity) {
        // Create Object of Dialog class
        groupDialog = new Dialog(activity);
        // Set GUI of login screen
        groupDialog.setContentView(R.layout.groups);
        groupDialog.setTitle("Manage Groups");

        // Init button of login GUI
        final ListView groups = (ListView) groupDialog.findViewById(R.id.listViewGroups);

        final Spinner terms = (Spinner) groupDialog.findViewById(R.id.spinner_groups);
        List<String> termsStr = Planer.getInstance().getTerms();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_dropdown_item,
                termsStr.toArray(new String[termsStr.size()]));
        terms.setAdapter(adapter);
        terms.setSelection(terms.getCount()-1);
        terms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                GroupsAdapter adapterLV = new GroupsAdapter(
                        activity, Planer.getInstance().getGroupsOfTerm(terms.getSelectedItem().toString()));
                groups.setAdapter(adapterLV);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        GroupsAdapter adapterLV = new GroupsAdapter(
                activity, Planer.getInstance().getGroupsOfTerm(terms.getSelectedItem().toString()));
        groups.setAdapter(adapterLV);

        Switch enable = (Switch) groupDialog.findViewById(R.id.switchEnable);

      //  Button btnSave = (Button) groupDialog.findViewById(R.id.btnSave);
      //  Button btnCancel = (Button) groupDialog.findViewById(R.id.btnCancel);

        // Attached listener for login GUI button
      /*  btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtUsername.getText()!=null && txtPassword.getText()!=null) {
                    login.dismiss();
                    dialog = ProgressDialog.show(context, "Loading", "Please wait...", true);
                    new DeadlineView.LoginTask().execute("k"+txtUsername.getText().toString(), txtPassword.getText().toString());
                }
                else {
                    Toast.makeText(activity, "Please enter Username and Password", Toast.LENGTH_LONG).show();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.dismiss();
            }
        });*/

        // Make dialog box visible.
        groupDialog.show();
    }
}
