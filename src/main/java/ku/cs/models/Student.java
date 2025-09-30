package ku.cs.models;

public class Student extends User {
    private int year;
    private String email;
    private String advisor;
    public Student(String username, String password, String name, String id, String image, String faculty,
                   String major, int year, String email, String advisor, String date, String role) {
        super(username, password, name, id, image, faculty, major, date, role);
        this.year = year;
        this.email = email;
        this.advisor = advisor;
    }

    public Student(String name, String id, String faculty, String major, String email, String advisor) throws Exception {
        super("", "", name, id, faculty, major, "student");
        this.email = email;
        this.advisor = advisor;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdvisor() {
        return advisor;
    }

    public void setAdvisor(String advisor) {
        this.advisor = advisor;
    }

    @Override
    public String getUsername() {
        return super.getUsername();
    }

    @Override
    public void setUsername(String username) {
        super.setUsername(username);
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public void setPassword(String password) throws Exception {
        super.setPassword(password);
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }

    @Override
    public String getId() {
        return super.getId();
    }

    @Override
    public void setId(String id) {
        super.setId(id);
    }

    @Override
    public String getImage() {
        return super.getImage();
    }

    @Override
    public void setImage(String image) {
        super.setImage(image);
    }

    @Override
    public String getFaculty() {
        return super.getFaculty();
    }

    @Override
    public void setFaculty(String faculty) {
        super.setFaculty(faculty);
    }

    @Override
    public String getMajor() {
        return super.getMajor();
    }

    @Override
    public void setMajor(String major) {
        super.setMajor(major);
    }

    @Override
    public String getDate() {
        return super.getDate();
    }

    @Override
    public void setDate(String date) {
        super.setDate(date);
    }

    @Override
    public String getRole() {
        return super.getRole();
    }

    @Override
    public void setRole(String role) {
        super.setRole(role);
    }
}
