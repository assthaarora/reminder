package com.example.reminderapp;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static android.content.Context.ALARM_SERVICE;

public class AddReminder {
    String label = "", date = "";

    public AddReminder() {
    }


    public void add(final Context context, View v, final AppDatabase appDatabase,
                    final List<Reminder> reminderList, final RecyclerView remreycle
            , final ReminderAdapter adapter, final ImageView norem) {

        Rect displayRectangle = new Rect();

        Window window = ((Activity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DialogTheme);

        ViewGroup viewGroup = v.findViewById(android.R.id.content);

        View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.card_set_reminder, viewGroup, false);

        dialogView.setMinimumWidth((int) (displayRectangle.width() * 1f));

        dialogView.setMinimumHeight((int) (displayRectangle.height() * 1f));

        builder.setView(dialogView);

        final AlertDialog alertDialog = builder.create();

        ImageView ok = dialogView.findViewById(R.id.ok);

        ImageView cancel = dialogView.findViewById(R.id.cancel);

        final EditText editText = dialogView.findViewById(R.id.label_text);

        final TextView dateTime = dialogView.findViewById(R.id.date);

        final Button selectDateTime = dialogView.findViewById(R.id.selectDate);

        final EditText emailTo = dialogView.findViewById(R.id.recepient);

        final EditText emailSubject = dialogView.findViewById(R.id.subject);

        final EditText emailBody = dialogView.findViewById(R.id.body);

        final LinearLayout repeatLay = dialogView.findViewById(R.id.repeatLay);

        final TextView repeatetype = dialogView.findViewById(R.id.repeat_type);

        repeatLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Choose an animal");
                String[] animals = {"Once", "Daily", "Mon to Fri", "Custom"};
                builder.setItems(animals, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: // Once
                                repeatetype.setText("Once");
                                break;
                            case 1: // Daily
                                repeatetype.setText("Daily");
                                break;
                            case 2: // Mon to Fri
                                repeatetype.setText("Mon to Fri");
                                break;
                            case 3: // Custom
                                final AlertDialog.Builder custombuilder = new AlertDialog.Builder(context);
                                final String[] customDay = {"Mon", "Tue", "Wed", "Thr",
                                        "Fri", "Sat", "Sun"};
                                final boolean[] checkedSites = new boolean[]{false, false, false, false, false, false, false};
                                custombuilder.setMultiChoiceItems(customDay, checkedSites, new DialogInterface.OnMultiChoiceClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which
                                            , boolean isChecked) {
                                        // Update the current item's checked status
                                        checkedSites[which] = isChecked;
                                    }
                                });
                                custombuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //customdialog.dismiss();
                                    }
                                });
                                custombuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        for (int i = 0; i < checkedSites.length; i++) {
                                            boolean checked = checkedSites[i];
                                            if (checked) {

                                                repeatetype.setText(repeatetype.getText() + " " + customDay[i]);
                                            }
                                        }
                                    }
                                });
                                final AlertDialog customdialog = custombuilder.create();

                                customdialog.show();
                                break;

                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        selectDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectingDateTime(dateTime, context);
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = settingReminder(editText, dateTime, appDatabase, context
                        , reminderList, remreycle, adapter, norem
                        , emailBody, emailSubject, emailTo, repeatetype);
                if (flag)
                    alertDialog.dismiss();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void selectingDateTime(final TextView textView, final Context context) {

        final Calendar newCalender = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {

                final Calendar newDate = Calendar.getInstance();
                Calendar newTime = Calendar.getInstance();
                TimePickerDialog time = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        newDate.set(year, month, dayOfMonth, hourOfDay, minute, 0);
                        Calendar tem = Calendar.getInstance();
                        Log.w("TIME", System.currentTimeMillis() + "");
                        if (newDate.getTimeInMillis() - tem.getTimeInMillis() > 0)
                            textView.setText(newDate.getTime().toString());
                        else
                            Toast.makeText(context, "Invalid time", Toast.LENGTH_SHORT).show();

                    }
                }, newTime.get(Calendar.HOUR_OF_DAY), newTime.get(Calendar.MINUTE), true);
                time.show();

            }
        }, newCalender.get(Calendar.YEAR), newCalender.get(Calendar.MONTH), newCalender.get(Calendar.DAY_OF_MONTH));

        dialog.getDatePicker().setMinDate(System.currentTimeMillis());
        dialog.show();


    }

    private boolean settingReminder(EditText message, TextView textView, AppDatabase appDatabase
            , Context context, final List<Reminder> reminderList, final RecyclerView remreycle
            , final ReminderAdapter adapter, final ImageView norem,
                                    final EditText body, final EditText sub
            , final EditText emailto, final TextView repeatType) {

        boolean flag = true;
        if (body.getText().toString().equalsIgnoreCase("") || sub.getText().toString().equalsIgnoreCase("")
                || emailto.getText().toString().equalsIgnoreCase("")) {
            flag = false;
            Toast.makeText(context, "Please enter the details", Toast.LENGTH_LONG).show();
        } else {
            flag = true;
            RoomDAO roomDAO = appDatabase.getRoomDAO();
            Reminder reminders = new Reminder();
            reminders.setTitle(message.getText().toString().trim());
            Date remind = new Date(textView.getText().toString().trim());
            reminders.setEmailBody(body.getText().toString());
            reminders.setEmailAddress(emailto.getText().toString());
            reminders.setRepeat(repeatType.getText().toString());
            reminders.setEmailSubject(sub.getText().toString());
            reminders.setRemindedate(remind);
            roomDAO.Insert(reminders);
            List<Reminder> l = roomDAO.getAll();
            Log.e("ID chahiye", l.size() + "))");
            reminders = l.get(l.size() - 1);
            Log.e("ID chahiye", reminders.getId() + "");

            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
            calendar.setTime(remind);
            calendar.set(Calendar.SECOND, 0);
            Intent intent = new Intent(context, NotifierAlarm.class);
            intent.putExtra("Message", reminders.getTitle());
            intent.putExtra("RemindDate", reminders.getRemindedate().toString());
            intent.putExtra("id", reminders.getId());
            intent.putExtra("emailTo", reminders.getEmailAddress());
            intent.putExtra("emailBody", reminders.getEmailBody());
            intent.putExtra("subject", reminders.getEmailSubject());
            intent.putExtra("repeatType", reminders.getRepeat());

            PendingIntent intent1 = PendingIntent.getBroadcast(context, reminders.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            //alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), intent1);
            if (reminders.getRepeat().equalsIgnoreCase("Once")) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), intent1);
            } else if (reminders.getRepeat().equalsIgnoreCase("Daily")) {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, intent1);
            } else if (reminders.getRepeat().equalsIgnoreCase("Mon to Fri")) {
                for (int i = 2; i <= 6; i++) {
                    calendar.set(Calendar.DAY_OF_WEEK, i);
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                            calendar.getTimeInMillis(), 24 * 7 * 60 * 60 * 1000, intent1);
                }
            } else {
                String customDay[] = reminders.getRepeat().split(" ");
                for (int i = 0; i < customDay.length; i++) {
                    if (customDay[i].equalsIgnoreCase("Sun"))
                        calendar.set(Calendar.DAY_OF_WEEK, 1);
                    else if (customDay[i].equalsIgnoreCase("Mon"))
                        calendar.set(Calendar.DAY_OF_WEEK, 2);
                    else if (customDay[i].equalsIgnoreCase("Tue"))
                        calendar.set(Calendar.DAY_OF_WEEK, 3);
                    else if (customDay[i].equalsIgnoreCase("Wed"))
                        calendar.set(Calendar.DAY_OF_WEEK, 4);
                    else if (customDay[i].equalsIgnoreCase("Thr"))
                        calendar.set(Calendar.DAY_OF_WEEK, 5);
                    else if (customDay[i].equalsIgnoreCase("Fri"))
                        calendar.set(Calendar.DAY_OF_WEEK, 6);
                    if (customDay[i].equalsIgnoreCase("Sat"))
                        calendar.set(Calendar.DAY_OF_WEEK, 7);
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                            calendar.getTimeInMillis(), 24 * 7 * 60 * 60 * 1000, intent1);
                }
            }
            Toast.makeText(context, "Inserted Successfully", Toast.LENGTH_SHORT).show();
//        setItemsInRecyclerView();
            AppDatabase.destroyInstance();
//        dialog.dismiss();

            Reminder reminder = new Reminder(reminders.getTitle(),
                    new Date(textView.getText().toString().trim()), reminders.getRepeat()
                    , reminders.getEmailAddress(), reminders.getEmailSubject(), reminders.getEmailBody());
            reminderList.add(reminder);
            remreycle.setVisibility(View.VISIBLE);
            norem.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }
        return flag;
    }

}
