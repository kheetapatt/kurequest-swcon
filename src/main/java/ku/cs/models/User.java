package ku.cs.models;

import ku.cs.services.PasswordEncoder;

import java.text.SimpleDateFormat;
import java.util.Date;

public class User {
    private String username;
    private String password;
    private String name;
    private String id;
    private String image;
    private String faculty;
    private String major;
    private String date;
    private String role;


    public User(String username, String password, String name, String id,String image,
                String faculty, String major, String date, String role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.id = id;
        this.image = image;
        this.faculty = faculty;
        this.major = major;
        this.date = date;
        this.role = role;
    }

    public User(String username, String password, String name, String id,
                 String faculty, String major, String role) throws Exception {
        String dateNow = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
        this.username = username;
        setPassword(password);
        this.name = name;
        this.id = id;
        this.image = "default-profile-image.png";
        this.faculty = faculty;
        this.major = major;
        this.date = dateNow;
        this.role = role;
    }


    public String getUsername() {return username;}
    public String getPassword() {return password;}
    public String getName() {return name;}
    public String getId() {return id;}
    public String getImage() {return image;}
    public String getFaculty() {return faculty;}
    public String getMajor() {return major;}
    public String getDate() {return date;}
    public String getRole() {return role;}
    public void setUsername(String username) {this.username = username;}
    public void setName(String name) {this.name = name;}
    public void setId(String id) {this.id = id;}
    public void setImage(String image) {this.image = image;}
    public void setFaculty(String faculty) {this.faculty = faculty;}
    public void setMajor(String major) {this.major = major;}
    public void setDate(String date) {this.date = date;}
    public void setRole(String role) {this.role = role;}

    public void setPassword(String password) throws Exception {
        PasswordEncoder passwordEncoder = new PasswordEncoder(16, 65536, 256);
        this.password = passwordEncoder.hash(password);
    }

    public boolean validatePassword(String password) throws Exception {
        PasswordEncoder passwordEncoder = new PasswordEncoder(16, 65536, 256);
        return passwordEncoder.verify(password, this.password);
    }

}
