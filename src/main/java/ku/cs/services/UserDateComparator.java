package ku.cs.services;

import ku.cs.models.User;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;

public class UserDateComparator implements Comparator<User> {
    @Override
    public int compare(User u1, User u2) {
        DateFormat dfm = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            return dfm.parse(u2.getDate()).compareTo(dfm.parse(u1.getDate()));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
