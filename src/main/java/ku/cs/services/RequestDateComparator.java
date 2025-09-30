package ku.cs.services;

import ku.cs.models.Request;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;

public class RequestDateComparator implements Comparator<Request> {
    @Override
    public int compare(Request r1, Request r2) {
        DateFormat dfm = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            return dfm.parse(r2.getDate()).compareTo(dfm.parse(r1.getDate()));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
