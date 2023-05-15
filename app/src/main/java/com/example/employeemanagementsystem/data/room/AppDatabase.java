package com.example.employeemanagementsystem.data.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.employeemanagementsystem.model.Employee;

@Database(entities = {Employee.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    // Define a singleton instance of the database
    private static AppDatabase instance;

    // Define an abstract method for each DAO
    public abstract EmployeeDao employeeDao();

    // Create the database
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "emp_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}

