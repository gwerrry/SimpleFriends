/**
 * @file UuidUtil.java
 * @author gwerry
 * @brief The UuidUtil class is a utility class that provides methods to get UUIDs from names.
 * @version 1.0
 * @date 2024/01/06
 *
 * Copyright 2024 gwerry
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gwerry.utils;

import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;


/**
 * @brief A utility class that provides methods to get UUIDs from names.
 * @author gwerry
 * @version 1.0
 */
public class UuidUtil {

    /**
     * @brief Gets the UUID of a Minecraft player from their name.
     * @param name the name of the Minecraft player
     * @return the UUID of the player, or null if not found
     */
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

    /**
     * @brief Inserts dashes into a UUID string to make it valid.
     * @param uuid the UUID string without dashes
     * @return the UUID string with dashes
     */
    private static String insertDashUUID(String uuid) {
        StringBuilder sb = new StringBuilder(uuid);
        sb.insert(20, "-");
        sb.insert(16, "-");
        sb.insert(12, "-");
        sb.insert(8, "-");
        return sb.toString();
    }
}
