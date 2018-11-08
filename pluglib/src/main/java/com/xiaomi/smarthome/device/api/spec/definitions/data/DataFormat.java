package com.xiaomi.smarthome.device.api.spec.definitions.data;

import com.xiaomi.smarthome.device.api.spec.definitions.data.value.Vbool;
import com.xiaomi.smarthome.device.api.spec.definitions.data.value.Vfloat;
import com.xiaomi.smarthome.device.api.spec.definitions.data.value.Vint16;
import com.xiaomi.smarthome.device.api.spec.definitions.data.value.Vint32;
import com.xiaomi.smarthome.device.api.spec.definitions.data.value.Vint64;
import com.xiaomi.smarthome.device.api.spec.definitions.data.value.Vint8;
import com.xiaomi.smarthome.device.api.spec.definitions.data.value.Vstring;
import com.xiaomi.smarthome.device.api.spec.definitions.data.value.Vuint16;
import com.xiaomi.smarthome.device.api.spec.definitions.data.value.Vuint32;
import com.xiaomi.smarthome.device.api.spec.definitions.data.value.Vuint8;

import java.util.Locale;

public enum DataFormat {

    UNKNOWN("unknown"),
    BOOL("bool"),
    UINT8("uint8"),
    UINT16("uint16"),
    UINT32("uint32"),
    INT8("int8"),
    INT16("int16"),
    INT32("int32"),
    INT64("int64"),
    FLOAT("float"),
    STRING("string");

    private String string;

    DataFormat(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }

    public static DataFormat from(String s) throws IllegalArgumentException {
        for (DataFormat T : values()) {
            if (T.toString().equals(s)) {
                return T;
            }
        }

        throw new IllegalArgumentException("DataFormat invalid: " + s);
    }

    public boolean check(DataValue value) {
        switch (this) {
            case BOOL:
                return (value.getClass() == Vbool.class);

            case UINT8:
                return (value.getClass() == Vuint8.class);

            case UINT16:
                return (value.getClass() == Vuint16.class);

            case UINT32:
                return (value.getClass() == Vuint32.class);

            case INT8:
                return (value.getClass() == Vint8.class);

            case INT16:
                return (value.getClass() == Vint16.class);

            case INT32:
                return (value.getClass() == Vint32.class);

            case INT64:
                return (value.getClass() == Vint64.class);

            case FLOAT:
                return (value.getClass() == Vfloat.class);

            case STRING:
                return (value.getClass() == Vstring.class);

            default:
                break;
        }

        return false;
    }

    public boolean check(DataValue min, DataValue max, DataValue step) {
        return min.lessEquals(max);
    }

    public boolean validate(DataValue value, DataValue min, DataValue max, DataValue step) {
        return (step == null) ? value.validate(min, max) : value.validate(min, max, step);
    }

    public String getFormatJavaBase() {
        switch (this) {
            case BOOL:
                return "boolean";

            case UINT8:
                return "int";

            case UINT16:
                return "int";

            case UINT32:
                return "int";

            case INT8:
                return "int";

            case INT16:
                return "int";

            case INT32:
                return "int";

            case INT64:
                return "long";

            case FLOAT:
                return "float";

            case STRING:
                return "String";

            default:
                return "unknown";
        }
    }

    public Class<?> getJavaClass() throws RuntimeException {
        switch (this) {
            case BOOL:
                return Vbool.class;

            case UINT8:
                return Vuint8.class;

            case UINT16:
                return Vuint16.class;

            case UINT32:
                return Vuint32.class;

            case INT8:
                return Vint8.class;

            case INT16:
                return Vint16.class;

            case INT32:
                return Vint32.class;

            case INT64:
                return Vint64.class;

            case FLOAT:
                return Vfloat.class;

            case STRING:
                return Vstring.class;

            default:
                throw new RuntimeException("DataFormat invalid");
        }
    }

    public Object createObjectValue(String string) throws IllegalArgumentException {
        Object v = null;

        switch (this) {
            case BOOL:
                v = DataFormat.BooleanValueOf(string);
                break;

            case UINT8:
                v = Integer.valueOf(string);
                break;

            case UINT16:
                v = Integer.valueOf(string);
                break;

            case UINT32:
                v = Integer.valueOf(string);
                break;

            case INT8:
                v = Integer.valueOf(string);
                break;

            case INT16:
                v = Integer.valueOf(string);
                break;

            case INT32:
                v = Integer.valueOf(string);
                break;

            case INT64:
                v = Long.valueOf(string);
                break;

            case FLOAT:
                if (string.equals("0")) {
                    string = "0.0f";
                }
                v = Float.valueOf(string);
                break;

            case STRING:
                v = string;
                break;

            default:
                throw new IllegalArgumentException("createObjectValue failed, invalid type");
        }

        return v;
    }

    public DataValue createDefaultValue() {
        DataValue v = null;

        switch (this) {
            case BOOL:
                v = new Vbool();
                break;

            case UINT8:
                v = new Vuint8();
                break;

            case UINT16:
                v = new Vuint16();
                break;

            case UINT32:
                v = new Vuint32();
                break;

            case INT8:
                v = new Vint8();
                break;

            case INT16:
                v = new Vint16();
                break;

            case INT32:
                v = new Vint32();
                break;

            case INT64:
                v = new Vint64();
                break;

            case FLOAT:
                v = new Vfloat();
                break;

            case STRING:
                v = new Vstring();
                break;

            default:
                throw new IllegalArgumentException("createObjectValue failed, invalid type");
        }

        return v;
    }

    public DataValue createValue(Object value) {
        DataValue v = null;

        switch (this) {
            case BOOL:
                v = Vbool.valueOf(value);
                break;

            case UINT8:
                v = Vuint8.valueOf(value);
                break;

            case UINT16:
                v = Vuint16.valueOf(value);
                break;

            case UINT32:
                v = Vuint32.valueOf(value);
                break;

            case INT8:
                v = Vint8.valueOf(value);
                break;

            case INT16:
                v = Vint16.valueOf(value);
                break;

            case INT32:
                v = Vint32.valueOf(value);
                break;

            case INT64:
                v = Vint64.valueOf(value);
                break;

            case FLOAT:
                v = Vfloat.valueOf(value);
                break;

            case STRING:
                v = Vstring.valueOf(value);
                break;

            default:
                throw new IllegalArgumentException("createValue failed, invalid type");
        }

        return v;
    }

    private static Boolean BooleanValueOf(String string) {
        if (string == null) {
            return false;
        }

        String v = string.toUpperCase(Locale.ENGLISH);

        if (v.equals("1") || v.equals("YES") || v.equals("TRUE")) {
            return true;
        }

        if (v.equals("0") || v.equals("NO") || v.equals("FALSE")) {
            return false;
        }

        throw new IllegalArgumentException("BooleanValueOf failed: " + string);
    }
}