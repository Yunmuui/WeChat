package com.jsut.wechat.Converter;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jsut.wechat.Entity.Chat;
import com.jsut.wechat.Entity.Chat.OneChat;

import java.lang.reflect.Type;
import java.util.List;

public class ChatTypeConverters {
    private static Gson gson = new Gson();

    @TypeConverter
    public static List<OneChat> fromJson(String json) {
        Type type = new TypeToken<List<OneChat>>() {}.getType();
        return gson.fromJson(json, type);
    }

    @TypeConverter
    public static String toJson(List<OneChat> list) {
        return gson.toJson(list);
    }
}
