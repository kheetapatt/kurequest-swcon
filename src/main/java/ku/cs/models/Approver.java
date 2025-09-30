package ku.cs.models;

public class Approver {
    private String name;
    private String position;
    private String faculty;
    private String major;

    public Approver(String name, String position, String faculty, String major) {
        this.name = name;
        this.position = position;
        this.faculty = faculty;
        this.major = major;
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public String getPositionWithFacultyOrMajor() {
        if (major.isEmpty()) {
            return position + "คณะ" + faculty;
        } else {
            return position + major;
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }
}
