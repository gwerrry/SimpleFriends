package com.gwerry.utils;

import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class UuidUtil {
    public static UUID getUuid(String name) {
        String url = "https://api.mojang.com/users/profiles/minecraft/" + name;
        try {
            InputStream inputStream = new URI(url).toURL().openStream();
            String UUIDJson = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            if (UUIDJson.isEmpty()) return null;
            JSONObject UUIDObject = (JSONObject) JSONValue.parseWithException(UUIDJson);
            return UUID.fromString(insertDashUUID(UUIDObject.get("id").toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String insertDashUUID(String uuid) {
        StringBuilder sb = new StringBuilder(uuid);
        sb.insert(20, "-");
        sb.insert(16, "-");
        sb.insert(12, "-");
        sb.insert(8, "-");
        return sb.toString();
    }
}
