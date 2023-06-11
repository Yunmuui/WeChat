package com.jsut.wechat.Converter;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jsut.wechat.Entity.Chat;
import com.jsut.wechat.Entity.Chat.OneMsg;

import java.lang.reflect.Type;
import java.util.List;

public class ChatTypeConverters {
    private static Gson gson = new Gson();

    @TypeConverter
    public static List<OneMsg> fromJson(String json) {
        Type type = new TypeToken<List<OneMsg>>() {}.getType();
        return gson.fromJson(json, type);
    }

    @TypeConverter
    public static String toJson(List<Chat.OneMsg> list) {
        return gson.toJson(list);
    }
}
