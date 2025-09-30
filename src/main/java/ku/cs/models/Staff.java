package ku.cs.models;

public class Staff extends User {
    private String defaultPassword;

    public Staff(String username, String password, String defaultPassword, String name, String id, String image,
                 String faculty, String major, String date, String role) {
        super(username, password, name, id, image, faculty, major, date, role);
        this.defaultPassword = defaultPassword;
    }

    public Staff(String username, String defaultPassword, String name, String faculty) throws Exception {
        super(username, defaultPassword, name, "", faculty, "", "faculty staff");
        setDefaultPassword(defaultPassword);
    }

    public Staff(String username, String defaultPassword, String name, String faculty, String major) throws Exception {
        super(username, defaultPassword, name, "", faculty, major, "major staff");
        setDefaultPassword(defaultPassword);
    }

    public Staff(String username, String defaultPassword, String name, String faculty, String major, String id) throws Exception {
        super(username, defaultPassword, name, id, faculty, major, "advisor");
        setDefaultPassword(defaultPassword);
    }

    public String getDefaultPassword() {return defaultPassword;}

    public void setDefaultPassword(String defaultPassword) throws Exception {
        this.defaultPassword = defaultPassword;
        setPassword(defaultPassword);
    }

    public boolean isDefaultPassword() throws Exception {
        return validatePassword(defaultPassword);
    }

    @Override
    public String getUsername() {return super.getUsername();}

    @Override
    public String getPassword() {return super.getPassword();}

    @Override
    public String getName() {return super.getName();}

    @Override
    public String getId() {return super.getId();}

    @Override
    public String getImage() {return super.getImage();}

    @Override
    public String getFaculty() {return super.getFaculty();}

    @Override
    public String getMajor() {return super.getMajor();}

    @Override
    public String getDate() {return super.getDate();}

    @Override
    public String getRole() {return super.getRole();}

    @Override
    public void setUsername(String username) {super.setUsername(username);}

    @Override
    public void setPassword(String password) throws Exception {
        super.setPassword(password);
    }

    @Override
    public boolean validatePassword(String password) throws Exception {
        return super.validatePassword(password);
    }

    @Override
    public void setName(String name) {super.setName(name);}

    @Override
    public void setId(String id) {super.setId(id);}

    @Override
    public void setImage(String image) {super.setImage(image);}

    @Override
    public void setFaculty(String faculty) {super.setFaculty(faculty);}

    @Override
    public void setMajor(String major) {super.setMajor(major);}

    @Override
    public void setDate(String date) {super.setDate(date);}

    @Override
    public void setRole(String role) {super.setRole(role);}
}
