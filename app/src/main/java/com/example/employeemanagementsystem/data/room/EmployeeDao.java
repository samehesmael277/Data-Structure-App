package com.example.employeemanagementsystem.data.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.employeemanagementsystem.model.Employee;

import java.util.List;

@Dao
public interface EmployeeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Employee employee);

    @Update
    void update(Employee employee);

    @Delete
    void delete(Employee employee);

    @Query("DELETE FROM employees")
    void deleteAll();

    @Query("SELECT * FROM employees")
    List<Employee> getAllEmployees();

    @Query("SELECT * FROM employees WHERE empName LIKE :empName")
    List<Employee> searchOnEmployees(String empName);
}

