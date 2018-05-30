package com.example.may.myapplication.dal.room.converters;

import android.arch.persistence.room.TypeConverter;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by May on 4/18/2018.
 */

public class StringArrayConverter {

    static String ARRAY_DIVIDER = "#";

    @TypeConverter
    public static List<String> toList(String value) {
        return (value == null || value.equals("")) ? new ArrayList<String>() : Arrays.asList(value.split(ARRAY_DIVIDER));
    }

    @TypeConverter
    public static String toString(List<String> value) {
        return value == null ? null : TextUtils.join(ARRAY_DIVIDER, value);
    }
}
