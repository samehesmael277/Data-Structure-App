package com.example.employeemanagementsystem.datastructure;

import com.example.employeemanagementsystem.model.Employee;

import java.util.ArrayList;
import java.util.Collections;

public class EmployeeLinkedList {

    private Node head;

    Node sorted = null;

    private static class Node {
        private Employee employee;
        private Node next;

        public Node(Employee employee) {
            this.employee = employee;
            this.next = null;
        }
    }

    public void insert(Employee employee) {
        Node new_node = new Node(employee);
        if (head == null)
            insertAtBeg(employee);
        else
            insertAtEnd(employee);
    }

    public void insertAtBeg(Employee employee) {
        Node new_node = new Node(employee);
        new_node.next = head;
        head = new_node;
    }

    public void insertAtEnd(Employee employee) {
        Node new_node = new Node(employee);
        if (head == null) {
            head = new_node;
            return;
        }
        Node ptr = head;
        while (ptr.next != null)
            ptr = ptr.next;

        ptr.next = new_node;
    }

    public void insertAfterPos(Employee employee, int position) {
        Node new_node = new Node(employee);
        Node ptr = head;
        if (position == 1) {
            new_node.next = head;
            head.next = new_node;
            return;
        }
        position--;
        while (position > 0) {
            ptr = ptr.next;
            position--;
        }
        new_node.next = ptr.next;
        ptr.next = new_node;
    }

    public void insertBeforePos(Employee employee, int position) {
        Node new_node = new Node(employee);
        if (position == 1) {
            insertAtBeg(employee);
            return;
        } else {
            Node ptr = head, temp = head;
            while (position != 1) {
                temp = ptr;
                ptr = ptr.next;
                position--;
            }
            temp.next = new_node;
            new_node.next = ptr;
        }
    }

    public void shuffle() {
        // Convert the linked list to an ArrayList
        ArrayList<Employee> list = new ArrayList<Employee>();
        Node current = head;
        while (current != null) {
            list.add(current.employee);
            current = current.next;
        }
        // Shuffle the ArrayList
        Collections.shuffle(list);
        // Convert the ArrayList back to a linked list
        current = head;
        for (Employee element : list) {
            current.employee = element;
            current = current.next;
        }
    }

    public int linearSearch(int emp_no) {
        if (head == null)
            return (-1);
        Node ptr = head;
        int index = 0;
        while (ptr != null) {
            if (ptr.employee.getEmpNo() == emp_no)
                return (index);
            index++;
            ptr = ptr.next;
        }
        return (-1);
    }

    //insertion sort
    public void insertionSort() {
        if (length() <= 1) {
            return;
        }
        Node sorted = head;
        Node current = head.next;
        sorted.next = null;
        while (current != null) {
            Node next = current.next;
            if (current.employee.getEmpNo() < sorted.employee.getEmpNo()) {
                current.next = sorted;
                sorted = current;
            } else {
                Node temp = sorted;
                while (temp.next != null && current.employee.getEmpNo() > temp.next.employee.getEmpNo()) {
                    temp = temp.next;
                }
                current.next = temp.next;
                temp.next = current;
            }
            current = next;
        }
        head = sorted;
    }

    //sortedInsert
    void sortedInsert(Node new_node) {
        if (sorted == null || sorted.employee.getEmpNo() >= new_node.employee.getEmpNo()) {
            new_node.next = sorted;
            sorted = new_node;
        } else {
            Node current = sorted;
            while (current.next != null && current.next.employee.getEmpNo() < new_node.employee.getEmpNo()) {
                current = current.next;
            }
            new_node.next = current.next;
            current.next = new_node;
        }
    }

    public void heapify(Employee[] arr, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        if (left < n && arr[left].getEmpNo() > arr[largest].getEmpNo())
            largest = left;
        if (right < n && arr[right].getEmpNo() > arr[largest].getEmpNo())
            largest = right;
        if (largest != i) {
            Employee temp = arr[i];
            arr[i] = arr[largest];
            arr[largest] = temp;
            heapify(arr, n, largest);
        }
    }

    public void heapSort() {
        // Convert linked list to array
        int n = 0;
        Node current = head;
        while (current != null) {
            current = current.next;
            n++;
        }
        Employee[] arr = new Employee[n];
        current = head;
        for (int i = 0; i < n; i++) {
            arr[i] = current.employee;
            current = current.next;
        }
        // Build heap
        for (int i = n / 2 - 1; i >= 0; i--)
            heapify(arr, n, i);
        // Extract elements from heap one by one
        for (int i = n - 1; i >= 0; i--) {
            Employee temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;
            int j = 0;
            while (true) {
                int left = 2 * j + 1;
                int right = 2 * j + 2;
                if (left >= i) break;
                if (right >= i || arr[left].getEmpNo() > arr[right].getEmpNo()) {
                    if (arr[left].getEmpNo() > arr[j].getEmpNo()) {
                        temp = arr[left];
                        arr[left] = arr[j];
                        arr[j] = temp;
                    }
                    j = left;
                } else {
                    if (arr[right].getEmpNo() > arr[j].getEmpNo()) {
                        temp = arr[right];
                        arr[right] = arr[j];
                        arr[j] = temp;
                    }
                    j = right;
                }
            }
        }
        // Convert array back to linked list
        head = null;
        for (int i = n - 1; i >= 0; i--)
            insertAtBeg(arr[i]);
    }

    // get an employee by index
    public Employee getEmployeeByIndex(int index) {
        if (index < 0 || index >= length()) {
            return null;
        }
        Node current = head;
        for (int i = 0; i < index; i++)
            current = current.next;
        return current.employee;
    }

    public ArrayList<Employee> getEmployees() {
        ArrayList<Employee> employeeList = new ArrayList<>();
        Node current = head;
        while (current != null) {
            employeeList.add(current.employee);
            current = current.next;
        }
        return employeeList;
    }

    //length of linked list
    public int length() {
        int count = 0;
        Node current = head;
        while (current != null) {
            count++;
            current = current.next;
        }
        return count;
    }

    public int binarySearch(int empNum) {
        int low = 0;
        int high = length() - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            Employee midEmployee = getEmployeeByIndex(mid);
            if (midEmployee.getEmpNo() == empNum) {
                return mid;
            } else if (midEmployee.getEmpNo() < empNum) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1;
    }

    private Node getMiddle(Node start, Node end) {
        if (start == null)
            return null;
        Node slow = start;
        Node fast = start.next;
        while (fast != end) {
            fast = fast.next;
            if (fast != end) {
                slow = slow.next;
                fast = fast.next;
            }
        }
        return slow;
    }

    //remove at position
    public void removeAt(int position) {
        if (head == null)
            return;
        Node temp = head;
        if (position == 0) {
            head = temp.next;
            return;
        }
        for (int i = 0; temp != null && i < position - 1; i++)
            temp = temp.next;
        if (temp == null || temp.next == null)
            return;
        Node next = temp.next.next;
        temp.next = next;
    }

    public void displayList() {
        if (head == null) {
            System.out.println("List is empty");
            return;
        }
        Node ptr = head;
        while (ptr != null) {
            System.out.print(ptr.employee.getEmpNo() + " " + ptr.employee.getEmpSalary() + " " + ptr.employee.getEmpDept() + " " +
                    ptr.employee.getEmpDob() + " " + ptr.employee.getEmpName() + " " + '\n');
            ptr = ptr.next;
        }
        System.out.println("null");
    }
}
