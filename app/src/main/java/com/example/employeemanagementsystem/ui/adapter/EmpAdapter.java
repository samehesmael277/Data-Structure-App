package com.example.employeemanagementsystem.ui.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employeemanagementsystem.R;
import com.example.employeemanagementsystem.model.Employee;

import java.util.List;

public class EmpAdapter extends RecyclerView.Adapter<EmpAdapter.ViewHolder> {

    private List<Employee> data;

    private final OnEmpClickListener mListener;

    public EmpAdapter(OnEmpClickListener mListener) {
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_emp, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Employee currentItem = data.get(position);

        if (currentItem != null) {
            String empNo = "Emp_No: " + currentItem.getEmpNo();
            String empName = currentItem.getEmpName();
            String empDept = "Department: " + currentItem.getEmpDept();
            String empDob = "DOB: " + currentItem.getEmpDob();
            String empSalary = currentItem.getEmpSalary() + " P";

            holder.empNo.setText(empNo);
            holder.empName.setText(empName);
            holder.empDept.setText(empDept);
            holder.empDob.setText(empDob);
            holder.empSalary.setText(empSalary);

            holder.root.setOnClickListener(v -> mListener.onEmpClick(currentItem));
        }
    }

    @Override
    public int getItemCount() {
        if (data != null)
            return data.size();
        else
            return 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Employee> newData) {
        data = newData;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView empNo;
        TextView empName;
        TextView empDept;
        TextView empDob;
        TextView empSalary;
        ConstraintLayout root;

        public ViewHolder(View itemView) {
            super(itemView);
            empNo = itemView.findViewById(R.id.tv_emp_no);
            empName = itemView.findViewById(R.id.tv_emp_name);
            empDept = itemView.findViewById(R.id.tv_emp_dept);
            empDob = itemView.findViewById(R.id.tv_emp_dob);
            empSalary = itemView.findViewById(R.id.tv_emp_salary);
            root = itemView.findViewById(R.id.constraint_layout_item_container);
        }
    }
}
