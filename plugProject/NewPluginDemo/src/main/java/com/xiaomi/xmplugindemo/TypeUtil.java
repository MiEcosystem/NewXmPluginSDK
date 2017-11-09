package com.xiaomi.xmplugindemo;

class TypeUtil {

    static Boolean toBoolean(Object value) {
        if(value==null){
            return false;
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        } else if (value instanceof String) {
            String stringValue = (String) value;
            if ("true".equalsIgnoreCase(stringValue)) {
                return true;
            } else if ("false".equalsIgnoreCase(stringValue)) {
                return false;
            }
        }
        return null;
    }

    static Double toDouble(Object value) {
        if(value==null){
            return 0.0;
        }
        if (value instanceof Double) {
            return (Double) value;
        } else if (value instanceof Number) {
            return ((Number) value).doubleValue();
        } else if (value instanceof String) {
            try {
                return Double.valueOf((String) value);
            } catch (NumberFormatException ignored) {
            }
        }
        return null;
    }

    static Integer toInteger(Object value) {
        if(value==null){
            return 0;
        }
        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof Number) {
            return ((Number) value).intValue();
        } else if (value instanceof String) {
            try {
                return (int) Double.parseDouble((String) value);
            } catch (NumberFormatException ignored) {
            }
        }
        return null;
    }

    static Long toLong(Object value) {
        if(value==null){
            return 0l;
        }
        if (value instanceof Long) {
            return (Long) value;
        } else if (value instanceof Number) {
            return ((Number) value).longValue();
        } else if (value instanceof String) {
            try {
                return (long) Double.parseDouble((String) value);
            } catch (NumberFormatException ignored) {
            }
        }
        return null;
    }

    static String toString(Object value) {
        if(value==null){
            return "";
        }
        if (value instanceof String) {
            return (String) value;
        } else if (value != null) {
            return String.valueOf(value);
        }
        return null;
    }
}
