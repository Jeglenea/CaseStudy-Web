package com.beymen.utils;

import java.io.FileWriter;
import java.io.IOException;

public class FileWriterUtility {

    public static void writeToFile(String filePath, String content) throws IOException {
        FileWriter writer = new FileWriter(filePath);
        writer.write(content);
        writer.close();
    }
}