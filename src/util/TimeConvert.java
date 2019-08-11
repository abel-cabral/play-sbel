package util;

import java.text.SimpleDateFormat;

public class TimeConvert {
    public static String convertToMinute(double milliSeconds, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        long aux = (long) milliSeconds;
        return sdf.format(milliSeconds);
    }
}
