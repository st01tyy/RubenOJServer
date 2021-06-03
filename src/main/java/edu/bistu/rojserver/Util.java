package edu.bistu.rojserver;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Util
{
    public static boolean nowBeforeStart(String time)
    {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dateTime = LocalDateTime.parse(time, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return now.isBefore(dateTime);
    }

    public static String getInContestID(int index)
    {
        int val = 'A';
        char ch = (char) (val + index);
        return String.valueOf(ch);
    }
}
