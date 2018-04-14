package com.landbay.mortgage.utils;

import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;

public class TestUtils {

    public static void setField(Object object, String key, Object value) throws Exception{
        Field field = object.getClass().getDeclaredField(key);
        field.setAccessible(true);
        field.set(object, value);
    }
}
