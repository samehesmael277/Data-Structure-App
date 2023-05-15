package com.example.employeemanagementsystem.datastructure;

import com.example.employeemanagementsystem.model.Employee;

import java.util.Random;

public class EmployeeArray {

    private static final int MAX_SIZE = 1000;

    private final Employee[] employees;

    private int size;

    public EmployeeArray() {
        this.employees = new Employee[MAX_SIZE];
        this.size = 0;
    }

    public void add(Employee emp) {
        if (size < MAX_SIZE) {
            employees[size] = emp;
            size++;
        }
    }

    public Employee get(int index) {
        if (index < size) {
            return employees[index];
        } else {
            return null;
        }
    }

    public int getSize() {
        return size;
    }

    public void insert(Employee emp, int index) {
        if (index < size && size < MAX_SIZE) {
            for (int i = size; i > index; i--) {
                employees[i] = employees[i - 1];
            }
            employees[index] = emp;
            size++;
        }
    }

    public void delete(int index) {
        if (index < size) {
            for (int i = index; i < size - 1; i++) {
                employees[i] = employees[i + 1];
            }
            employees[size - 1] = null;
            size--;
        }
    }

    public void deleteAll() {
        for (int i = 0; i < size; i++) {
            employees[i] = null;
        }
        size = 0;
    }

    public void shuffle() {
        Random rnd = new Random();
        for (int i = size - 1; i > 0; i--) {
            int j = rnd.nextInt(i + 1);
            Employee temp = employees[i];
            employees[i] = employees[j];
            employees[j] = temp;
        }
    }

    public int linearSearch(int empNumber) {
        for (int i = 0; i < size; i++) {
            if (employees[i].getEmpNo() == empNumber) {
                return i;
            }
        }
        return -1;
    }

    public int binarySearch(int empNumber) {
        insertionSort();
        int low = 0;
        int high = size - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            if (employees[mid].getEmpNo() == empNumber) {
                return mid;
            } else if (employees[mid].getEmpNo() < empNumber) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1;
    }

    public void insertionSort() {
        int n = size;
        for (int i = 1; i < n; i++) {
            Employee key = employees[i];
            int j = i - 1;
            while (j >= 0 && employees[j].getEmpNo() > key.getEmpNo()) {
                employees[j + 1] = employees[j];
                j--;
            }
            employees[j + 1] = key;
        }
    }

    public void bubbleSort() {
        boolean swapped = true;
        int n = size;
        while (swapped) {
            swapped = false;
            for (int i = 1; i < n; i++) {
                if (employees[i - 1].getEmpNo() > employees[i].getEmpNo()) {
                    Employee temp = employees[i];
                    employees[i] = employees[i - 1];
                    employees[i - 1] = temp;
                    swapped = true;
                }
            }
            n--;
        }
    }

    public void heapSort() {
        int n = size;
        // Build heap (rearrange array)
        for (int i = n / 2 - 1; i >= 0; i--)
            heapify(n, i);
        // One by one extract an element from heap
        for (int i = n - 1; i > 0; i--) {
            // Move current root to end
            Employee temp = employees[0];
            employees[0] = employees[i];
            employees[i] = temp;
            // call max heapify on the reduced heap
            heapify(i, 0);
        }
    }

    void heapify(int n, int i) {
        int largest = i;  // Initialize largest as root
        int l = 2 * i + 1;  // left = 2*i + 1
        int r = 2 * i + 2;  // right = 2*i + 2
        // If left child is larger than root
        if (l < n && employees[l].getEmpNo() > employees[largest].getEmpNo())
            largest = l;
        // If right child is larger than largest so far
        if (r < n && employees[r].getEmpNo() > employees[largest].getEmpNo())
            largest = r;
        // If largest is not root
        if (largest != i) {
            Employee swap = employees[i];
            employees[i] = employees[largest];
            employees[largest] = swap;
            // Recursively heapify the affected sub-tree
            heapify(n, largest);
        }
    }

    public Employee[] getAllEmp() {
        return employees;
    }
}