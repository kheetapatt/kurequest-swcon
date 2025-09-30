package ku.cs.models;

import ku.cs.services.UserDateComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserList {
    private ArrayList<User> users;

    public UserList() {
        users = new ArrayList<>();
    }

    public void addUser(String username, String password, String name, String id,String image,
                        String faculty, String major, String date, String role) {
        User exist = findUserByUsername(username);
        if (exist == null) {
            users.add(new User(username, password, name, id, image, faculty, major, date, role));
        }
    }

    public void addUser(String username, String password, String name, String id,
                        String faculty, String major, String role) throws Exception {
        User exist = findUserByUsername(username);
        if (exist == null) {
            users.add(new User(username, password, name, id, faculty, major, role));
        }
    }

    public void editAllInfo(String oldUsername, String newUsername, String password, String name, String id, String faculty, String major) throws Exception {
        User exist = findUserByUsername(oldUsername);
        if (exist != null) {
            exist.setUsername(newUsername);
            exist.setPassword(password);
            exist.setName(name);
            exist.setId(id);
            exist.setFaculty(faculty);
            exist.setMajor(major);
        }
    }

    public void setImage(String username, String imagePath) {
        User exist = findUserByUsername(username);
        if (exist != null) {
            exist.setImage(imagePath);
        }
    }

    public User findUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public User getAdmin() {
        for (User user : users) {
            if (user.getRole().equals("admin")) {
                return user;
            }
        }
        return null;
    }

    public boolean setPassword(String username, String oldPassword, String newPassword) throws Exception {
        User exist = findUserByUsername(username);
        if (exist != null) {
            if (exist.validatePassword(oldPassword)) {
                exist.setPassword(newPassword);
                return true;
            }
        }
        return false;
    }

    public void setDate(String username, String date) {
        User exist = findUserByUsername(username);
        if (exist != null) {
            exist.setDate(date);
        }
    }

    public void setId(String username, String id) {
        User exist = findUserByUsername(username);
        if (exist != null) {
            exist.setId(id);
        }
    }

    public void setName(String username, String name) {
        User exist = findUserByUsername(username);
        if (exist != null) {
            exist.setName(name);
        }
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public List<User> getUsersByRole(String role) {
        return users.stream().filter(user -> user.getRole().equals(role)).toList();
    }

    public User login(String username, String password) throws Exception {
        User exist = findUserByUsername(username);
        if (exist != null) {
            if (exist.validatePassword(password)) {
                return exist;
            }
        }
        return null;
    }

    public void sort() {
        Collections.sort(users, new UserDateComparator());
    }

    public void removeUser(String username) {
        if (findUserByUsername(username) != null) {
            users.remove(findUserByUsername(username));
        }
    }

}
