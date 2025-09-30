package ku.cs.models;

import java.util.ArrayList;
import java.util.List;

public class ApproverList {
    private ArrayList<Approver> approvers;

    public ApproverList() {
        approvers = new ArrayList<>();
    }

    public void addApprover(String name, String position, String faculty, String major) {
        approvers.add(new Approver(name, position, faculty, major));
    }

    public Approver findApproverByName(String name) {
        for (Approver approver : approvers) {
            if (approver.getName().equals(name)) {
                return approver;
            }
        }
        return null;
    }

    public void editName(String oldName, String newName) {
        Approver exist = findApproverByName(oldName);
        if (exist != null) {
            exist.setName(newName);
        }
    }

    public void editPosition(String name, String newPosition) {
        Approver exist = findApproverByName(name);
        if (exist != null) {
            exist.setPosition(newPosition);
        }
    }

    public List<Approver> getApproversByFaculty(String faculty) {
        List<Approver> approversByFaculty = new ArrayList<>();
        for (Approver approver : approvers) {
            if (approver.getFaculty().equals(faculty) && approver.getMajor().equals("")) {
                approversByFaculty.add(approver);
            }
        }
        return approversByFaculty;
    }

    public List<Approver> getApproversByMajor(String major) {
        List<Approver> approverByMajor = new ArrayList<>();
        for (Approver approver : approvers) {
            if (approver.getMajor().equals(major)) {
                approverByMajor.add(approver);
            }
        }
        return approverByMajor;
    }

    public ArrayList<Approver> getApprovers() {
        return approvers;
    }

    public String[] getApproversPositionWithNameByFaculty(String faculty) {
        List<Approver> approversByFaculty = getApproversByFaculty(faculty);
        String[] approverPositionWithName = new String[approversByFaculty.size()];
        for (int i = 0; i < approversByFaculty.size(); i++) {
            approverPositionWithName[i] = approversByFaculty.get(i).getPosition() + "คณะ" + approversByFaculty.get(i).getFaculty() +
                    " " + approversByFaculty.get(i).getName();
        }
        return approverPositionWithName;
    }

    public String[] getApproversPositionWithNameByMajor(String major) {
        List<Approver> approversByMajor = getApproversByMajor(major);
        String[] approverPositionWithName = new String[approversByMajor.size()];
        for (int i = 0; i < approversByMajor.size(); i++) {
            approverPositionWithName[i] = approversByMajor.get(i).getPosition() + approversByMajor.get(i).getMajor() + " " + approversByMajor.get(i).getName();
        }
        return approverPositionWithName;
    }
}
