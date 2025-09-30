package ku.cs.services;

import ku.cs.models.Staff;
import ku.cs.models.StaffList;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class StaffListFileDatasource implements Datasource<StaffList> {
    private String directoryName;
    private String fileName;

    public StaffListFileDatasource(String directoryName, String fileName) {
        this.directoryName = directoryName;
        this.fileName = fileName;
        checkFileIsExisted();
    }

    private void checkFileIsExisted() {
        File file = new File(directoryName);
        if (!file.exists()) {
            file.mkdirs();
        }
        String filePath = directoryName + File.separator + fileName;
        file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public StaffList readData() {
        StaffList staffs = new StaffList();
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        InputStreamReader inputStreamReader = new InputStreamReader(
                fileInputStream,
                StandardCharsets.UTF_8
        );
        BufferedReader buffer = new BufferedReader(inputStreamReader);

        String line = "";
        try {
            while ((line = buffer.readLine()) != null) {
                if (line.equals("")) continue;

                String[] data = line.split(",");

                String username = data[0];
                String password = data[1];
                String defaultPassword = data[2];
                String name = data[3];
                String id = data[4];
                String image = data[5];
                String faculty = data[6];
                String major = data[7];
                String date = data[8];
                String role = data[9];

                staffs.addStaff(username, password, defaultPassword, name, id, image, faculty, major, date, role);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return staffs;
    }

    @Override
    public void writeData(StaffList data) {
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
                fileOutputStream,
                StandardCharsets.UTF_8
        );
        BufferedWriter buffer = new BufferedWriter(outputStreamWriter);

        try {
            for (Staff staff : data.getStaffs()) {
                String line = staff.getUsername() + "," + staff.getPassword() + "," +
                        staff.getDefaultPassword() + "," + staff.getName() + "," + staff.getId() + "," +
                        staff.getImage() + "," + staff.getFaculty() + "," + staff.getMajor() + "," +
                        staff.getDate() + "," + staff.getRole();
                buffer.append(line);
                buffer.append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                buffer.flush();
                buffer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
