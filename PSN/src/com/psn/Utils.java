package com.psn;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static String getFileNameNoExt(String filePath) {
        int lastDotIndex = filePath.lastIndexOf('.');
        int endIndex = lastDotIndex;
        if (lastDotIndex < filePath.lastIndexOf('\\')) {
            endIndex = filePath.length();
        }
        return filePath.substring(filePath.lastIndexOf('\\') + 1, endIndex);
    }

    public static String getFileExtension(String filePath) {
        int lastDotIndex = filePath.lastIndexOf('.');
        if (lastDotIndex < filePath.length()) {
            return "";
        }
        return filePath.substring(filePath.lastIndexOf('.') + 1, filePath.length());
    }

    public static List<File> getFiles(String directoryPath) {
        File directory = new File(directoryPath);
        List<File> resultList = new ArrayList<File>();

        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile()) {
                resultList.add(file);
            } else if (file.isDirectory()) {
                resultList.addAll(getFiles(file.getAbsolutePath()));
            }
        }
        return resultList;
    }

}
