package com.example.reminderapp;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "reminder")
public class Reminder {
    private String title,time,repeat,emailAddress,emailSubject,emailBody;
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;

    Date remindedate;
    public Reminder() {
    }

    public Reminder(String title, Date remindedate, String repeat) {
        this.title = title;
        this.remindedate = remindedate;
        this.repeat = repeat;
    }

    public Reminder(String title, Date remindedate, String repeat,
                    String emailAddress, String emailSubject, String emailBody) {
        this.title = title;
        this.remindedate = remindedate;
        this.repeat = repeat;
        this.emailAddress = emailAddress;
        this.emailSubject = emailSubject;
        this.emailBody = emailBody;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getRemindedate() {
        return remindedate;
    }

    public void setRemindedate(Date remindedate) {
        this.remindedate = remindedate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    public String getEmailBody() {
        return emailBody;
    }

    public void setEmailBody(String emailBody) {
        this.emailBody = emailBody;
    }
}
