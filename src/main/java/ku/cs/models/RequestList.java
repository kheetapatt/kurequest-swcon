package ku.cs.models;

import ku.cs.services.RequestDateComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RequestList {
    private ArrayList<Request> requests;
    public RequestList() { requests = new ArrayList<>(); }
    public ArrayList<Request> getRequests() { return requests; }

    public void addRequest(String date, String status,String statusFromApprover,String id,String phone,String faculty,String major,String adivsor,
                           String academicYear,String semester,String from,String to,String reason,String requestFor,String approveBy,String pdfPath,String type){
        requests.add(new Request(date,status,statusFromApprover,id,phone,faculty,major,adivsor,academicYear,semester,from,to,reason,requestFor,approveBy,pdfPath,type));
    }

    public List<Request> getRequestsById(String id) {
        return requests.stream().filter(request -> request.getId().equals(id)).toList();
    }

    public void approveRequest(String id, String date, String status, String statusFromApprover, String approveBy) {
        Request request = getSpecificRequest(id, date);
        if (request != null) {
            request.setApproveBy(approveBy);
            request.setStatus(status);
            request.setStatusFromApprover(statusFromApprover);
            request.setDate(date);
        }
    }


    public void setPdfPath(String id, String date, String pdfPath) {
        Request request = getSpecificRequest(id, date);
        if (request != null) {
            request.setPdfPath(pdfPath);
        }
    }

    public void setDate(String id, String oldDate, String newDate) {
        Request request = getSpecificRequest(id, oldDate);
        if (request != null) {
            request.setDate(newDate);
        }
    }

    public void sort() {
        Collections.sort(requests, new RequestDateComparator());
    }


    public List<Request> getRequestsByAdvisorAndStatus(String advisor, String status) {
        return requests.stream().filter(request -> (request.getAdvisor().equals(advisor)&&request.getStatus().equals(status))).toList();
    }

    public List<Request> findRequestByFacultyAndStatus(String faculty, String status) {
        return requests.stream()
                .filter(request -> (request.getFaculty().equals(faculty)&&request.getStatus().equals(status))).toList();
    }


    public List<Request> findRequestByMajorAndStatus(String major, String status )
    {
        return requests.stream()
                .filter(request -> (request.getMajor().equals(major)&&request.getStatus().equals(status))).toList();
    }

    public List<Request> getRequestsByIdWithTypeOrStatus(String id, String searchText) {
        return requests.stream().filter(request -> request.getId().equals(id))
                                .filter(request -> request.getType().contains(searchText) ||
                                        request.getStatus().contains(searchText) ||
                                        request.getStatusFromApprover().contains(searchText)).toList();
    }

    public Request getSpecificRequest(String id, String date) {
        for (Request request : requests) {
            if (request.getId().equals(id) && request.getDate().equals(date)) {
                return request;
            }
        }
        return null;
    }

}
