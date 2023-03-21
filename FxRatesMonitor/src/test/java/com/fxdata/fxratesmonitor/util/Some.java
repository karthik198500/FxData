package com.fxdata.fxratesmonitor.util;

import lombok.experimental.UtilityClass;
import lombok.val;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.io.File;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@UtilityClass
public class Some {

    private static final AtomicInteger COUNTER = new AtomicInteger(1);

    private static final String DEFAULT_OUTPUT_DIR = "build/test/test-files/";

    public static  String email() {
        return stringWithRandomSuffix("email", 10, 25)+"@"+stringWithRandomSuffix(".com.au", 10, 25);
    }

    public static  String timeStamp() {
        return new Long(RandomUtils.nextLong()).toString();
    }

    public static String subject() {
        return stringWithRandomSuffix("code", 10, 25);
    }
    public static String code() {
        return stringWithRandomSuffix("code", 10, 25);
    }

    public File someFile() {
        return newRandomFile("test-file");
    }

    private static File newRandomFile(String name) {
        return Paths.get(DEFAULT_OUTPUT_DIR, name + "-" + UUID.randomUUID() + ".zip").toFile();
    }

    public static String longVal(int minInclusive, int maxInclusive) {
        return new Long(RandomUtils.nextLong(minInclusive, maxInclusive + 1)).toString();
    }

    private static String stringWithRandomSuffix(String label, int minLength, int maxLength) {
        val prefix = newPrefix(label);
        val minRandomLength = Math.max(1, minLength - prefix.length());
        val maxRandomLength = Math.max(1, maxLength - prefix.length());

        return prefix + RandomStringUtils.randomAlphanumeric(minRandomLength, maxRandomLength);
    }

    private static String newPrefix(String label) {
        return label + "-" + (COUNTER.getAndIncrement()) + "-";
    }
}
