package ku.cs.services;

import ku.cs.models.Request;
import ku.cs.models.RequestList;

import java.io.*;
import java.nio.charset.StandardCharsets;


public class RequestListFileDatasource implements Datasource<RequestList> {
    private String directoryName;
    private String fileName;

    public RequestListFileDatasource(String directoryName, String fileName) {
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
    public RequestList readData() {
        RequestList requestList = new RequestList();
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

                String date = data[0];
                String status = data[1];
                String statusFromApprover = data[2];
                String id = data[3];
                String phone = data[4];
                String faculty = data[5];
                String major = data[6];
                String adivsor = data[7];
                String academicYear = data[8];
                String semester = data[9];
                String from = data[10];
                String to = data[11];
                String reason = data[12];
                String requestFor = data[13];
                String approveBy = data[14];
                String pdfPath = data[15];
                String type = data[16];
                requestList.addRequest(date,status,statusFromApprover,id,phone,faculty,major,adivsor,academicYear,semester,from,to,reason,requestFor,approveBy,pdfPath,type);

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return requestList;
    }


    @Override
    public void writeData(RequestList data) {
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
            for (Request request : data.getRequests()) {
                String line = request.getDate() + ","
                        + request.getStatus() + ","
                        +request.getStatusFromApprover()+","
                        + request.getId() + ","
                        + request.getPhone()+","
                        +request.getFaculty()+","
                        + request.getMajor()+","
                        +request.getAdvisor()+","
                        + request.getAcademicYear() + ","
                        + request.getSemester() + ","
                        + request.getFrom() + ","
                        + request.getTo() + ","
                        + request.getReason() + ","
                        + request.getRequestFor() + ","
                        + request.getApproveBy() + ","
                        + request.getPdfPath() + ","
                        + request.getType();
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

