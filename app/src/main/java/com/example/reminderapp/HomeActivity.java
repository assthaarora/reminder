package com.example.reminderapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class HomeActivity extends AppCompatActivity {

    private ImageView noReminder;

    private FloatingActionButton addReminder;

    private RecyclerView remindersRecyclerView;

    ReminderAdapter reminderAdapter;

    int count = 0;

    private List<Reminder> reminderList = new ArrayList<>();

    private AppDatabase appDatabase;

    String dateTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        appDatabase = AppDatabase.geAppdatabase(this);

        init();
    }

    private void init() {

        noReminder = findViewById(R.id.no_reminder);

        addReminder = findViewById(R.id.add_reminder);

        remindersRecyclerView = findViewById(R.id.reminders);

        reminderAdapter = new ReminderAdapter(reminderList, this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        remindersRecyclerView.setLayoutManager(mLayoutManager);

        remindersRecyclerView.setItemAnimator(new DefaultItemAnimator());

        remindersRecyclerView.setAdapter(reminderAdapter);

        if (reminderList.size() == 0) {

            remindersRecyclerView.setVisibility(View.GONE);

            noReminder.setVisibility(View.VISIBLE);

        } else {

            remindersRecyclerView.setVisibility(View.VISIBLE);

            noReminder.setVisibility(View.GONE);

        }

        setItemsInRecyclerView();
        //addReminder.setOnClickListener(addingReminder);

        addReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AddReminder().add(HomeActivity.this, v, appDatabase,
                        reminderList, remindersRecyclerView, reminderAdapter, noReminder);
                //setItemsInRecyclerView();
            }
        });
    }

    public void setItemsInRecyclerView() {

        RoomDAO dao = appDatabase.getRoomDAO();

        reminderList = dao.getAll();

        Log.e("ID chahiye", reminderList.size() + "");

        if (reminderList.size() > 0) {

            noReminder.setVisibility(View.INVISIBLE);

            remindersRecyclerView.setVisibility(View.VISIBLE);
        }

        reminderAdapter = new ReminderAdapter(reminderList, HomeActivity.this);

        remindersRecyclerView.setAdapter(reminderAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if (getIntent()!=null) {

            String emailTO = intent.getStringExtra("To");

            String emailBody = intent.getStringExtra("body");

            String emailSUbject = intent.getStringExtra("subject");

            if (emailTO != null) {

                Intent emailIntent = new Intent(Intent.ACTION_SEND);

                emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                emailIntent.setData(Uri.parse("mailto:"));

                emailIntent.setType("text/plain");

                emailIntent.putExtra(Intent.EXTRA_EMAIL, emailTO);

                emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailBody);

                emailIntent.putExtra(Intent.EXTRA_TEXT, emailSUbject);

                try {
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                    //finish();
                    Log.i("Finished email...", "");
                } catch (android.content.ActivityNotFoundException ex) {

                    Toast.makeText(HomeActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
