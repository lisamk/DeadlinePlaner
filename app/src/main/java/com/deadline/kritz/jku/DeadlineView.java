package com.deadline.kritz.jku;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.deadline.kritz.jku.planer.Deadline;
import com.deadline.kritz.jku.planer.Planer;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.deadline.kritz.jku.planer.Planer.SDF;

class DeadlineView {

    private static Planer planer;
    private static ListView listview;
    private static Activity context;
    private static Dialog login;
    private static ProgressDialog dialog;

    public static void setup(final Activity activity) {
        context = activity;

        planer = Planer.getInstance(activity);
        planer.setDeadlinesFromDB();

        if(planer.getGroups().size()==0) {
            login(activity);
        }

        listview = (ListView) activity.findViewById(R.id.listview_deadline);
        Deadline[] deadlinesArray = new Deadline[planer.getDeadlines().size()];
        int couterDeadlineArray = 0;
        for(Deadline d : planer.getDeadlines()){
            deadlinesArray[couterDeadlineArray] = d;
            couterDeadlineArray++;
        }


        DeadlineAdapter adapter = new DeadlineAdapter(context, R.layout.deadline_item, deadlinesArray);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Deadline deadlineItem = (Deadline) listview.getItemAtPosition(i);
                AlertDialog.Builder builder = new AlertDialog.Builder(DeadlineView.context);
                builder.setMessage(deadlineItem.getDescription());
                builder.setTitle(deadlineItem.getGroupName());


                builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        planer.deleteDeadline(deadlineItem);
                        update();
                    }
                });

                builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private static void login(final Activity activity) {
        // Create Object of Dialog class
        login = new Dialog(activity);
        // Set GUI of login screen
        login.setContentView(R.layout.login);
        login.setTitle("Login to KUSSS");

        // Init button of login GUI
        Button btnLogin = (Button) login.findViewById(R.id.btnLogin);
        Button btnCancel = (Button) login.findViewById(R.id.btnCancel);
        final EditText txtUsername = (EditText)login.findViewById(R.id.txtUsername);
        final EditText txtPassword = (EditText)login.findViewById(R.id.txtPassword);

        // Attached listener for login GUI button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtUsername.getText()!=null && txtPassword.getText()!=null) {
                    login.dismiss();
                    dialog = ProgressDialog.show(context, "Loading", "Please wait...", true);
                    new LoginTask().execute(txtUsername.getText().toString(), txtPassword.getText().toString());
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
        });

        // Make dialog box visible.
        login.show();
    }

    public static void update() {
        Deadline[] deadlinesArray = new Deadline[planer.getDeadlines().size()];
        int couterDeadlineArray = 0;
        for(Deadline d : planer.getDeadlines()){
            deadlinesArray[couterDeadlineArray] = d;
            couterDeadlineArray++;
        }


        DeadlineAdapter adapter = new DeadlineAdapter(context, R.layout.deadline_item, deadlinesArray);
        listview.setAdapter(adapter);
    }

    private static class LoginTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String[] params) {
            return planer.login(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            dialog.dismiss();
            if(success) {
                Toast.makeText(context, "Login successful.", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(context, "Wrong Userdata.", Toast.LENGTH_LONG).show();
                login.show();
            }
        }
    }
}



