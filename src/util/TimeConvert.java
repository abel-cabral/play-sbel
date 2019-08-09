package util;

import java.text.SimpleDateFormat;

public class TimeConvert {
    public static String convertToMinute(Double milliSeconds, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(milliSeconds);
    }
}
