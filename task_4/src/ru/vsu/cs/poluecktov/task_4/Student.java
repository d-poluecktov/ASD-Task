package ru.vsu.cs.poluecktov.task_4;

/*Отсортировать студентов так, чтобы сначала сортировка шла по курсу, затем по группе и
 только потом по фамилии (для этого необходимо соответствующим образом реализовать
 интерфейс Comparable<Student> для класса Student)*/

public class Student implements Comparable<Student> {
    private String surname;
    private int group;
    private int course;

    public Student() {
    }

    public Student(String surname, int group, int course) {
        this.surname = surname;
        this.group = group;
        this.course = course;
    }

    public String getSurname() {
        return surname;
    }

    public int getGroup() {
        return group;
    }

    public int getCourse() {
        return course;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public void setCourse(int course) {
        this.course = course;
    }


    @Override
    public int compareTo(Student o) {
        int result = this.course - o.course;

        if(result == 0) {
            result = this.group - o.group;

            if(result == 0) {
                result = this.surname.compareTo(o.surname);
            }

        }

        return result;
    }

    @Override
    public String toString() {
        return "Student{" +
                "surname='" + surname + '\'' +
                ", group=" + group +
                ", course=" + course +
                '}';
    }
}
