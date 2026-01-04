package com.neit.hoccode.utils;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class MergeObject {
    public static <T> void mergeIgnoreNull(T source, T target) {
        if (source == null || target == null) return;
        Field[] fields = source.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object value = field.get(source);
                if (value != null) {
                    field.set(target, value);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
