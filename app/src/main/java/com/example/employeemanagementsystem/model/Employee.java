package com.example.employeemanagementsystem.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "employees")
public class Employee implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int empNo;
    private String empName;
    private String empDept;
    private String empDob;
    private long empSalary;

    public Employee() {
    }

    public Employee(int empNo, String empName, String empDept, String empDob, long empSalary) {
        this.empNo = empNo;
        this.empName = empName;
        this.empDept = empDept;
        this.empDob = empDob;
        this.empSalary = empSalary;
    }

    protected Employee(Parcel in) {
        empNo = in.readInt();
        empName = in.readString();
        empDept = in.readString();
        empDob = in.readString();
        empSalary = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(empNo);
        dest.writeString(empName);
        dest.writeString(empDept);
        dest.writeString(empDob);
        dest.writeLong(empSalary);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Employee> CREATOR = new Creator<Employee>() {
        @Override
        public Employee createFromParcel(Parcel in) {
            return new Employee(in);
        }

        @Override
        public Employee[] newArray(int size) {
            return new Employee[size];
        }
    };

    public int getEmpNo() {
        return empNo;
    }

    public void setEmpNo(int empNo) {
        this.empNo = empNo;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpDept() {
        return empDept;
    }

    public void setEmpDept(String empDept) {
        this.empDept = empDept;
    }

    public String getEmpDob() {
        return empDob;
    }

    public void setEmpDob(String empDob) {
        this.empDob = empDob;
    }

    public long getEmpSalary() {
        return empSalary;
    }

    public void setEmpSalary(long empSalary) {
        this.empSalary = empSalary;
    }
}