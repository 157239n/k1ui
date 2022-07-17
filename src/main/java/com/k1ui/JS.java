package com.k1ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * JSON Utils.
 */
public class JS {
    private static List<String> indentsCache = null;

    private static void initIndents() {
        if (indentsCache == null) {
            indentsCache = new ArrayList<>();
            String currentStr = "";
            for (int i = 0; i < 100; i++) {
                indentsCache.add(currentStr);
                currentStr += "  ";
            }
        }
    }

    /**
     * Doesn't include fields that starts with "_", or hibernate's internal fields
     */
    @SuppressWarnings("StringConcatenationInsideStringBufferAppend")
    private static <T> void format(StringBuilder answer, String endl, String fieldName, T o, int indent) {
        if (o == null) {
            answer.append(indentsCache.get(indent) + fieldName + ": null").append(endl);
            return;
        }
        Class<?> _class = o.getClass();
        if (_class.toString().contains("org.hibernate")) return;
        if (fieldName.startsWith("_")) return;
        if (o instanceof List) {
            List<?> a = (List<?>) o;
            answer.append(indentsCache.get(indent) + fieldName + ": Array").append(endl);
            for (int i = 0; i < a.size(); i++)
                format(answer, endl, "" + i, a.get(i), indent + 1);
        } else if (o instanceof Map) {
            //noinspection unchecked
            Map<String, ?> m = (Map<String, ?>) o;
            answer.append(indentsCache.get(indent) + fieldName + ": Map").append(endl);
            m.forEach((key, value) -> format(answer, endl, key, value, indent + 1));
        } else if (o instanceof JsonNode) {
            JsonNode n = (JsonNode) o;
            if (n.isNumber() || n.isBoolean()) {
                answer.append(indentsCache.get(indent) + fieldName + ": " + n.asText()).append(endl);
            } else if (n.isArray()) {
                answer.append(indentsCache.get(indent) + fieldName + ": Array (JsonNode)").append(endl);
                for (int i = 0; i < n.size(); i++)
                    format(answer, endl, "" + i, n.get(i), indent + 1);
            } else {
                answer.append(indentsCache.get(indent) + fieldName + ": Map (JsonNode)").append(endl);
                for (Iterator<String> it = n.fieldNames(); it.hasNext(); ) {
                    String field = it.next();
                    format(answer, endl, field, n.get(field), indent + 1);
                }
            }
        } else if (_class == String.class || _class == Integer.class || _class == Float.class || _class == Long.class || _class == Double.class || _class == Date.class || _class == Boolean.class) {
            answer.append(indentsCache.get(indent) + fieldName + ": " + o).append(endl);
        } else if (_class.isEnum()) {
            answer.append(indentsCache.get(indent) + fieldName + ": Enum " + ((Enum<?>) o).name()).append(endl);
        } else {
            answer.append(indentsCache.get(indent) + fieldName + ": Object (" + _class + ")").append(endl);
            for (Field field : o.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    format(answer, endl, field.getName(), field.get(o), indent + 1);
                } catch (IllegalAccessException e) {
                    System.out.println("Illegal: " + field.getName());
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Formats an object into a String nicely, with nice tree views and whatnot, for debugging purposes.
     * Can print out pretty much any object, not just JSON-related ones, so very useful in general.
     */
    private static <T> String format(T o, String endl) {
        initIndents();
        StringBuilder answer = new StringBuilder();
        format(answer, endl, "root", o, 0);
        return answer.toString();
    }

    public static <T> String formatCli(T o) {
        return format(o, "\n");
    }

    public static<T> String formatHtml(T o) {
        return "<pre>\n" + format(o, "<br>\n") + "\n</pre>";
    }

    private static ObjectMapper _mapper = null;

    public static ObjectMapper mapper() {
        if (_mapper == null) {
            _mapper = new ObjectMapper();
            _mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        }
        return _mapper;
    }

    /**
     * Maps json string to JSONObject. This way you can add this to JS.obj() and JS.arr() builders.
     */
    public static JSONObject map(String json) {
        return new JSONObject(json);
    }

    /**
     * Maps json string to a class.
     *
     * Btw, if there's a field within your custom class that you can't know ahead of time, you can
     * pass in JsonNode instead. See test case JSTest.dynamic() for an example.
     */
    @Nullable
    public static <T> T map(String json, Class<T> _class) {
        try {
            return mapper().readValue(json, _class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nonnull
    public static <T> T mapNonNull(String json, Class<T> _class) {
        return Objects.requireNonNull(map(json, _class));
    }

    public static <T> T print(T o) {
        System.out.println(format(o, "\n"));
        return o;
    }

    /**
     * Maps and prints out
     */
    public static <T> T mapPrint(String json, Class<T> _class) {
        return print(map(json, _class));
    }

    /**
     * Object to JSON String. Intuitively, reads as "json.of(obj)"
     */
    public static String of(Object o) {
        try {
            return mapper().writeValueAsString(o);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }

    /**
     * Object to JSON String, but with parameter for over-arching label (Mostly for Otter DTO to json) -RF
     */
    public static String of(Object o, String header){
        return "{\"" + header + "\"" + ":" + of(o)+"}";
    }

    public static String of(JSONObject o) {
        if (o == null) return "";
        return o.toString();
    }

    public static String of(JSONArray a) {
        return a.toString();
    }

    /**
     * This method, together with arr(), forms quite a nice way to build custom json objects on the go. So this...
     *
     *     JS.obj("b", "something", "c", JS.arr(5, "d"))
     *
     * ...will produce this json:
     *
     *     {
     *       "b": "something",
     *       "c": [ 5, "d"]
     *     }
     *
     * Again, to get the actual string, use JS.of() on the output. There's a gotcha tho. You can't really mix
     * your custom classes with [JSONObject, JSONArray]. To do that, follow the test case in
     * JSTest.addExistingCustomClassToBuilder() instead.
     */
    public static JSONObject obj(Object... objs) {
        if (objs.length % 2 != 0) throw new IllegalArgumentException("Have odd numbers of input!");
        JSONObject answer = new JSONObject();
        for (int i = 0; i < objs.length / 2; i++)
            answer.put((String) objs[2 * i], objs[2 * i + 1]);
        return answer;
    }

    public static JSONArray arr(Object... objs) {
        return new JSONArray(Arrays.asList(objs));
    }

    public static <T> JSONArray arr(List<T> objs) {
        return arr(objs.toArray());
    }

    /**
     * Joins multiple JSONArray together. Example::
     *
     *     # returns JS.arr(2, 3, 4, 5)
     *     JS.join(JS.arr(2, 3), JS.arr(4, 5))
     */
    public static JSONArray join(JSONArray... arrays) {
        List<Object> l = new ArrayList<>();
        for (JSONArray arr : arrays) {
            for (int i = 0; i < arr.length(); i++)
                l.add(arr.get(i));
        }
        return arr(l);
    }

    /**
     * Joins multiple JSONObject together. Example::
     *
     *     # returns JS.obj("a", 3, "b", 4, "c", 5)
     *     JS.join(JS.obj("a", 3, "b", 4), JS.obj("c", 5))
     */
    public static JSONObject join(JSONObject... objs) {
        List<Object> l = new ArrayList<>();
        for (JSONObject obj : objs) {
            obj.toMap().forEach((key, value) -> {
                l.add(key);
                l.add(value);
            });
        }
        return obj(l.toArray());
    }
}
