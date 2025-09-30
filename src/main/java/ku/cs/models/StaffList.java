package ku.cs.models;

import java.util.ArrayList;

public class StaffList {
    private ArrayList<Staff> staffs;

    public StaffList() {
        staffs = new ArrayList<>();
    }

    public void addStaff(String username, String password, String defaultPassword, String name, String id,String image,
                         String faculty, String major, String date, String role) {
        if (!username.equals("") && !password.equals("") && !role.equals("")) {
            Staff exist = findStaffByUsername(username);
            if (exist == null) {
                staffs.add(new Staff(username, password, defaultPassword, name, id, image, faculty, major, date, role));
            }
        }
    }

    public void addFacultyStaff(String username, String defaultPassword, String name, String faculty) throws Exception {
            Staff exist = findStaffByUsername(username);
            if (exist == null) {
                staffs.add(new Staff(username, defaultPassword, name, faculty));
            }
    }

    public void addMajorStaff(String username, String defaultPassword, String name, String faculty, String major) throws Exception {
            Staff exist = findStaffByUsername(username);
            if (exist == null) {
                staffs.add(new Staff(username, defaultPassword, name, faculty, major));
            }
    }

    public void addAdvisor(String username, String defaultPassword, String name, String faculty, String major, String id) throws Exception {
        if (!username.equals("") && !defaultPassword.equals("") && !name.equals("") && !faculty.equals("") && !major.equals("") && !id.equals("")) {
            Staff exist = findStaffByUsername(username);
            if (exist == null) {
                staffs.add(new Staff(username, defaultPassword, name, faculty, major, id));
            }
        }
    }

    public void editFacultyStaff(String oldUsername, String newUsername, String defaultPassword, String name, String faculty) throws Exception {
        Staff exist = findStaffByUsername(oldUsername);
        if (exist != null) {
            exist.setFaculty(newUsername);
            exist.setDefaultPassword(defaultPassword);
            exist.setName(name);
            exist.setFaculty(faculty);
        }
    }

    public void editMajorStaff(String oldUsername, String newUsername, String defaultPassword, String name, String faculty, String major) throws Exception {
        Staff exist = findStaffByUsername(oldUsername);
        if (exist != null) {
            exist.setFaculty(newUsername);
            exist.setDefaultPassword(defaultPassword);
            exist.setName(name);
            exist.setMajor(major);
            exist.setFaculty(faculty);
        }
    }

    public void editAdvisor(String oldUsername, String newUsername, String defaultPassword, String name, String faculty, String major, String id) throws Exception {
        Staff exist = findStaffByUsername(oldUsername);
        if (exist != null) {
            exist.setFaculty(newUsername);
            exist.setDefaultPassword(defaultPassword);
            exist.setName(name);
            exist.setId(id);
            exist.setMajor(major);
            exist.setFaculty(faculty);
        }
    }

    public void setImage(String username, String imagePath) {
        Staff exist = findStaffByUsername(username);
        if (exist != null) {
            exist.setImage(imagePath);
        }
    }

    public boolean setPassword(String username, String oldPassword, String newPassword) throws Exception {
        Staff exist = findStaffByUsername(username);
        if (exist.validatePassword(oldPassword)) {
            exist.setPassword(newPassword);
            return true;
        }
        return false;
    }

    public void setDate(String username, String date) {
        Staff exist = findStaffByUsername(username);
        if (exist != null) {
            exist.setDate(date);
        }
    }

    public Staff findStaffByUsername(String username) {
        for (Staff staff : staffs) {
            if (staff.getUsername().equals(username)) {
                return staff;
            }
        }
        return null;
    }

    public ArrayList<Staff> getStaffs() {
        return staffs;
    }
}
