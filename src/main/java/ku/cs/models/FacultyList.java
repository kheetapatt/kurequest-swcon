package ku.cs.models;

import java.util.ArrayList;

public class FacultyList {
    private ArrayList<Faculty> faculties;

    public FacultyList() {
        faculties = new ArrayList<>();
    }

    public void addFaculty(String name, String id) {
        if (!name.equals("") && !id.equals("")) {
            Faculty exist = findFacultyById(name);
            if (exist == null) {
                faculties.add(new Faculty(name, id));
            }
        }
    }

    public Faculty findFacultyById(String id) {
        for (Faculty faculty : faculties) {
            if (faculty.getId().equals(id)) {
                return faculty;
            }
        }
        return null;
    }

    public void editName(String id, String name) {
        Faculty exist = findFacultyById(id);
        if (exist != null) {
            exist.setName(name);
        }
    }

    public void editId(String oldId, String newId) {
        Faculty exist = findFacultyById(oldId);
        if (exist != null) {
            exist.setId(newId);
        }
    }

    public ArrayList<Faculty> getFaculties() {
        return faculties;
    }

    public String[] getFacultyNames() {
        String[] facultiesNames = new String[faculties.size()];
        for (Faculty faculty : faculties) {
            facultiesNames[faculties.indexOf(faculty)] = faculty.getName();
        }
        return facultiesNames;
    }
}
