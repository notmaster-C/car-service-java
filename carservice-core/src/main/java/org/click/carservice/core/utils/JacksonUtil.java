package org.click.carservice.core.utils;
/**
 * Copyright (c) [click] [927069313@qq.com]
 * [carservice-plus] is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 * http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * json转换通用工具类
 */
@Slf4j
public class JacksonUtil {

    /**
     * 获取json字符串中某个字段
     * @param body  json字符串
     * @param field 字段名
     */
    public static String parseString(String body, String field) {
        ObjectMapper mapper = objectMapper();
        JsonNode node;
        try {
            node = mapper.readTree(body);
            JsonNode leaf = node.get(field);
            if (leaf != null) {
                return leaf.asText();
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }


    /**
     * 获取json字符串中某个字段
     * @param body  json字符串
     * @param field 字段名
     */
    public static List<String> parseStringList(String body, String field) {
        ObjectMapper mapper = objectMapper();
        JsonNode node;
        try {
            node = mapper.readTree(body);
            JsonNode leaf = node.get(field);
            if (leaf != null) {
                return mapper.convertValue(leaf, new TypeReference<List<String>>() {
                });
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 获取json字符串中某个字段
     * @param body  json字符串
     * @param field 字段名
     */
    public static Integer parseInteger(String body, String field) {
        ObjectMapper mapper = objectMapper();
        JsonNode node;
        try {
            node = mapper.readTree(body);
            JsonNode leaf = node.get(field);
            if (leaf != null) {
                return leaf.asInt();
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 获取json字符串中某个字段
     * @param body  json字符串
     * @param field 字段名
     */
    public static List<Integer> parseIntegerList(String body, String field) {
        ObjectMapper mapper = objectMapper();
        JsonNode node;
        try {
            node = mapper.readTree(body);
            JsonNode leaf = node.get(field);
            if (leaf != null) {
                return mapper.convertValue(leaf, new TypeReference<List<Integer>>() {
                });
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }


    /**
     * 获取json字符串中某个字段
     * @param body  json字符串
     * @param field 字段名
     */
    public static Boolean parseBoolean(String body, String field) {
        ObjectMapper mapper = objectMapper();
        JsonNode node;
        try {
            node = mapper.readTree(body);
            JsonNode leaf = node.get(field);
            if (leaf != null) {
                return leaf.asBoolean();
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 获取json字符串中某个字段
     * @param body  json字符串
     * @param field 字段名
     */
    public static Short parseShort(String body, String field) {
        ObjectMapper mapper = objectMapper();
        JsonNode node;
        try {
            node = mapper.readTree(body);
            JsonNode leaf = node.get(field);
            if (leaf != null) {
                int value = leaf.asInt();
                return (short) value;
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 获取json字符串中某个字段
     * @param body  json字符串
     * @param field 字段名
     */
    public static Byte parseByte(String body, String field) {
        ObjectMapper mapper = objectMapper();
        JsonNode node;
        try {
            node = mapper.readTree(body);
            JsonNode leaf = node.get(field);
            if (leaf != null) {
                int value = leaf.asInt();
                return (byte) value;
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 获取json字符串中某个字段并转换成指定类型
     * @param body  json字符串
     * @param field 字段名
     * @param clazz 数据类型
     */
    public static <T> T parseObject(String body, String field, Class<T> clazz) {
        ObjectMapper mapper = objectMapper();
        JsonNode node;
        try {
            node = mapper.readTree(body);
            node = node.get(field);
            return mapper.treeToValue(node, clazz);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 获取json字符串并转换成指定类型
     * @param body  json字符串
     * @param clazz 数据类型
     */
    public static <T> T parseObject(String body, Class<T> clazz) {
        ObjectMapper mapper = objectMapper();
        try {
            return mapper.readValue(body, clazz);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }


    /**
     * 将字符串转成JsonNode
     * @param json json字符串
     */
    public static JsonNode toNode(String json) {
        if (json == null) {
            return null;
        }
        ObjectMapper mapper = objectMapper();
        try {
            return mapper.readTree(json);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 将json转成map
     * @param data json字符串
     */
    public static Map<String, String> toMap(String data) {
        ObjectMapper mapper = objectMapper();
        try {
            return mapper.readValue(data, new TypeReference<Map<String, String>>() {
            });
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 将对象转为json字符串
     * @param data  对象
     */
    public static String toJson(Object data) {
        ObjectMapper mapper = objectMapper();
        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 通用json对象
     */
    private static ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        JavaTimeModule module = new JavaTimeModule();
        //日期序列化、
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        module.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        module.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        //反序列化
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        module.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        module.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        mapper.registerModule(module);
        return mapper;
    }

}