package ku.cs.services;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class RequestStatusDatesFileDatasource {
    private String directoryName;
    private String fileName;

    public RequestStatusDatesFileDatasource(String directoryName, String fileName) {
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

    public HashMap<String, String[]> readData() {
        HashMap<String, String[]> requestStatusDates = new HashMap<>();
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
            while ( (line = buffer.readLine()) != null ){
                if (line.equals("")) continue;

                String[] data = line.split(",");

                String request = data[0].trim();
                String declineReason = data[1].trim();
                String newRequestDate = data[2].trim();
                String dateFromAdvisor = data[3].trim();
                String dateFromMajor = data[4].trim();
                String dateFromFaculty = data[5].trim();

                String[] statusDates = {declineReason, newRequestDate, dateFromAdvisor, dateFromMajor, dateFromFaculty};
                requestStatusDates.put(request, statusDates);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return requestStatusDates;
    }

    public void writeData(HashMap<String, String[]> data) {
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
            for (String key : data.keySet()) {
                String line = key;
                String[] statusDates = data.get(key);
                for (String statusDate : statusDates) {
                    line += "," + statusDate;
                }
                buffer.append(line);
                buffer.append("\n");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                buffer.flush();
                buffer.close();
            }
            catch (IOException e){
                throw new RuntimeException(e);
            }
        }
    }
}
