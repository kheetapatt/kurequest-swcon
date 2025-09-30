package ku.cs.models;

import java.util.ArrayList;
import java.util.List;

public class StudentList {
    private ArrayList<Student> students;

    public StudentList() {
        students = new ArrayList<>();
    }

    public void addStudent(String username, String password, String name, String id, String image, String faculty,
                           String major, int year, String email, String advisor, String date, String role) {
            Student exist = findStudentByID(id);
            if (exist == null) {
                students.add(new Student(username, password, name, id, image, faculty, major, year, email, advisor, date, role));
            }
    }

    public void addStudent(String name, String id, String faculty, String major, String email, String advisor) throws Exception {
            Student exist = findStudentByID(id);
            if (exist == null) {
                students.add(new Student(name, id, faculty, major, email, advisor));
            }
    }

    public Student findStudentByUsername(String username) {
        for (Student student : students) {
            if (student.getUsername().equals(username)) {
                return student;
            }
        }
        return null;
    }

    public Student findStudentByID(String id) {
        for (Student student : students) {
            if (student.getId().equals(id)) {
                return student;
            }
        }
        return null;
    }

    public void editStudentInfo(String oldId, String newId, String name, String email, String advisor) {
        Student exist = findStudentByID(oldId);
        boolean otherIdExists = false;
        if (findStudentByID(newId) != null && !newId.equals(oldId)) {
            otherIdExists = true;
        }
        if (exist != null && !otherIdExists) {
            exist.setId(newId);
            exist.setEmail(email);
            exist.setName(name);
            exist.setAdvisor(advisor);
        }
    }

    public void setImage(String username, String imagePath) {
        Student exist = findStudentByUsername(username);
        if (exist != null) {
            exist.setImage(imagePath);
        }
    }

    public boolean setPassword(String username, String oldPassword, String newPassword) throws Exception {
        Student exist = findStudentByUsername(username);
        if (exist.validatePassword(oldPassword)) {
            exist.setPassword(newPassword);
            return true;
        }
        return false;
    }

    public void setDate(String username, String date) {
        Student exist = findStudentByUsername(username);
        if (exist != null) {
            exist.setDate(date);
        }
    }

    public List<Student> getStudentsByAdvisor(String advisorName) {
        return students.stream().filter(student -> student.getAdvisor().equals(advisorName)).toList();
    }

    public List<Student> getStudentsByMajor(String major) {
        return students.stream().filter(student -> student.getMajor().equals(major)).toList();
    }

    public List<Student> searchStudentMajorByID(String id,String major) {
        return students.stream()
                .filter(student -> student.getId().contains(id))  // กรองตามรหัสนิสิต
                .filter(student -> student.getMajor().equalsIgnoreCase(major))  // กรองตามชื่อที่ปรึกษา
                .toList();
    }

    public List<Student> searchStudenMajorByName(String name,String major) {

        return students.stream()
                .filter(student -> student.getName().contains(name))
                .filter(student -> student.getMajor().equalsIgnoreCase(major))
                .toList();
    }

    public List<Student> searchStudentUnderAvisorByName(String name,String staff) {

        return students.stream()
                .filter(student -> student.getName().contains(name))
                .filter(student -> student.getAdvisor().equalsIgnoreCase(staff))
                .toList();
    }

    public List<Student> searchStudentUnderAvisorByID(String id,String staff) {
        return students.stream()
                .filter(student -> student.getId().contains(id))
                .filter(student -> student.getAdvisor().equalsIgnoreCase(staff))
                .toList();
    }

    public ArrayList<Student> getStudents() {
        return students;
    }
}
