package com.example.hydrogen.util;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class IdGeneratorUtil {

    private IdGeneratorUtil() {}

    public static Supplier<String> generateRequestId  = () -> "hydrogen" + generateUuid();

    private static String generateUuid() { return UUID.randomUUID().toString(); }
}
