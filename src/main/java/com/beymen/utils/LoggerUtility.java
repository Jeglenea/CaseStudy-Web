package com.beymen.utils;

import org.apache.log4j.Logger;

public class LoggerUtility {
    public static Logger getLogger(Class<?> clazz) {
        return Logger.getLogger(clazz);
    }
}