package ru.vsu.cs.poluecktov.task_4;



import java.util.LinkedList;
import java.util.List;


public class Task {

    public static List<Student> getStudentList(String[][] data) {
        List<Student> students = new LinkedList<>();
        for (String[] student : data) {
            students.add(new Student(student[0], Integer.parseInt(student[1]), Integer.parseInt(student[2])));
        }
        return students;
    }

    public static String[][] getStudentArray(List<Student> list) {
        String[][] students = new String[list.size()][3];
        int i = 0;
        for (Student student: list) {
            students[i][0] = student.getSurname();
            students[i][1] = Integer.toString(student.getGroup());
            students[i][2] = Integer.toString(student.getCourse());
            i++;
        }

        return students;
    }




}
