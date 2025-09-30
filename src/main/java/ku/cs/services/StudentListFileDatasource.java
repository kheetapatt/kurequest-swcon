package ku.cs.services;

import ku.cs.models.Student;
import ku.cs.models.StudentList;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class StudentListFileDatasource implements Datasource<StudentList> {
    private String directoryName;
    private String fileName;

    public StudentListFileDatasource(String directoryName, String fileName) {
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
    public StudentList readData() {
        StudentList students = new StudentList();
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
                String name = data[2];
                String id = data[3];
                String image = data[4];
                String faculty = data[5];
                String major = data[6];
                String date = data[7];
                String advisor = data[8];
                int year = Integer.parseInt(data[9]);
                String email = data[10];
                String role = data[11];

                students.addStudent(username, password, name, id, image, faculty, major, year, email, advisor, date, role);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return students;
    }

    @Override
    public void writeData(StudentList data) {
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
            for (Student student : data.getStudents()) {
                String line = student.getUsername() + "," + student.getPassword() + "," +
                        student.getName() + "," + student.getId() + "," + student.getImage() + "," +
                        student.getFaculty() + "," + student.getMajor() + "," + student.getDate() + "," +
                        student.getAdvisor() + "," + student.getYear() + "," + student.getEmail() +
                        "," + student.getRole();
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
