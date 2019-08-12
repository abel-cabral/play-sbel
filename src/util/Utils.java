package util;

public class Utils {
    public static String rmExtension(String name) {
        String[] auxName = name.split(".");
        System.out.printf(auxName[0]);
        return auxName[0];
    }
}
