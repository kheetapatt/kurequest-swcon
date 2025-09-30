package ku.cs.models;

import java.util.ArrayList;
import java.util.List;

public class MajorList {
    private ArrayList<Major> majors;

    public MajorList() {
        majors = new ArrayList<>();
    }

    public void addMajor(String name, String id, String faculty) {
        if (!name.equals("") && !id.equals("") && !faculty.equals("")) {
            Major exist = findMajorById(id);
            if (exist == null) {
                majors.add(new Major(name, id, faculty));
            }
        }
    }

    public Major findMajorById(String id) {
        for (Major major : majors) {
            if (major.getId().equals(id)) {
                return major;
            }
        }
        return null;
    }

    public void editName(String id, String name) {
        Major exist = findMajorById(id);
        if (exist != null) {
            exist.setName(name);
        }
    }

    public void editId(String oldId, String newId) {
        Major exist = findMajorById(oldId);
        if (exist != null) {
            exist.setId(newId);
        }
    }

    public void editFaculty(String id, String faculty) {
        Major exist = findMajorById(id);
        if (exist != null) {
            exist.setFaculty(faculty);
        }
    }

    public ArrayList<Major> getMajors() {
        return majors;
    }

    public List<Major> getMajorsByFaculty(String faculty) {
        return majors.stream().filter(major -> major.getFaculty().equals(faculty)).toList();
    }

    public String[] getMajorNamesByFaculty(String faculty) {
        List<Major> majorNameList = getMajorsByFaculty(faculty);
        return majorNameList.stream().map(Major::getName).toArray(String[]::new);
    }
}
