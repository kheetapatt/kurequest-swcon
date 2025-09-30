package ku.cs.models;

public class Request {

    private String date;
    private String status;
    private String id;
    private String academicYear;
    private String semester;
    private String from;
    private String to;
    private String reason;
    private String requestFor;
    private String type;
    private String pdfPath;
    private String phone;
    private String faculty;
    private String major;
    private String advisor;
    private String statusFromApprover;
    private String approveBy;


    public Request(String date, String status, String statusFromApprover, String id, String phone, String faculty, String major, String advisor, String academicYear, String semester, String from, String to, String reason, String requestFor, String approveBy, String pdfPath, String type){
        this.date = date;
        this.status = status;
        this.statusFromApprover = statusFromApprover;
        this.id = id;
        this.phone = phone;
        this.faculty = faculty;
        this.major = major;
        this.advisor = advisor;
        this.academicYear = academicYear;
        this.semester = semester;
        this.from = from;
        this.to = to;
        this.reason = reason;
        this.requestFor = requestFor;
        this.approveBy = approveBy;
        this.pdfPath = pdfPath;
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public String getId() {
        return id;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public String getSemester() {
        return semester;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getReason() {
        return reason;
    }


    public String getRequestFor() {
        return requestFor;
    }

    public String getType() {
        return type;
    }

    public String getPdfPath() {
        return pdfPath;
    }

    public String getPhone() {
        return phone;
    }

    public String getStatusFromApprover() {
        return statusFromApprover;
    }


    public String getAdvisor() {
        return advisor;
    }

    public String getMajor() {
        return major;
    }

    public String getFaculty() {
        return faculty;
    }

    public String getApproveBy() {return approveBy;}

    public void setDate(String date) {
        this.date = date;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setId(String id) {
        this.id = id;
    }



    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }


    public void setRequestFor(String requestFor) {
        this.requestFor = requestFor;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
    }
    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }
    public void setMajor(String major) {
        this.major = major;
    }
    public void setAdvisor(String advisor) {
        this.advisor = advisor;
    }
    public void setStatusFromApprover(String statusFromApprover) {
        this.statusFromApprover = statusFromApprover;
    }
    public void setApproveBy(String approveBy) {this.approveBy = approveBy;}
}

