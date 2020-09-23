package com.example.reminderapp;


import android.provider.CalendarContract;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RoomDAO {

//    @Insert
//    public void Insert(UsernamePassword... usernamePasswords);
//
//    @Update
//    public void Update(UsernamePassword... usernamePasswords);
//
//    @Delete
//    public void Delete(UsernamePassword usernamePassword);

//    @Query("Select * from login where usename = :username")
//    public UsernamePassword getUserwithUsername(String username);
//
//    @Query("Select * from login where isloggedIn = 1")
//    public UsernamePassword getLoggedInUser();


    @Insert
    public void Insert(Reminder... reminders);

    @Update
    public void Update(Reminder... reminders);

    @Delete
    public void Delete(Reminder reminders);

    @Query("Select * from reminder order by remindeDate")
    public List<Reminder> orderThetable();

    @Query("Select * from reminder where id=:position")
    public Reminder getRecentEnteredData(int position);

    @Query("Select * from reminder")
    public List<Reminder> getAll();

}
