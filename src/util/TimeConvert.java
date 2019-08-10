package util;

public class TimeConvert {
    public static String convertToMinute(double milliSeconds, String dateFormat) {
        long aux = (long) milliSeconds;
        long minutes = (aux / 1000) / 60;
        long seconds = (aux / 1000) % 60;
        return minutes + ":" + seconds;
    }
}
