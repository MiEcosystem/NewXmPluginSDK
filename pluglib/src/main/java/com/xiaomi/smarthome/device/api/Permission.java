package com.xiaomi.smarthome.device.api;

public class Permission {
    private static final String READ_CALENDAR = "android.permission.READ_CALENDAR";
    private static final String WRITE_CALENDAR = "android.permission.WRITE_CALENDAR";

    public static final String CAMERA = "android.permission.CAMERA";

    public static final String READ_CONTACTS = "android.permission.READ_CONTACTS";
    private static final String WRITE_CONTACTS = "android.permission.WRITE_CONTACTS";
    public static final String GET_ACCOUNTS = "android.permission.GET_ACCOUNTS";

    public static final String ACCESS_FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION";
    public static final String ACCESS_COARSE_LOCATION = "android.permission.ACCESS_COARSE_LOCATION";

    public static final String RECORD_AUDIO = "android.permission.RECORD_AUDIO";

    public static final String READ_PHONE_STATE = "android.permission.READ_PHONE_STATE";
    @Deprecated
    public static final String CALL_PHONE = "android.permission.CALL_PHONE";
    private static final String READ_CALL_LOG = "android.permission.READ_CALL_LOG";
    private static final String WRITE_CALL_LOG = "android.permission.WRITE_CALL_LOG";
    private static final String ADD_VOICEMAIL = "com.android.voicemail.permission.ADD_VOICEMAIL";
    private static final String USE_SIP = "android.permission.USE_SIP";
    private static final String PROCESS_OUTGOING_CALLS = "android.permission.PROCESS_OUTGOING_CALLS";

    private static final String BODY_SENSORS = "android.permission.BODY_SENSORS";

    @Deprecated
    public static final String SEND_SMS = "android.permission.SEND_SMS";
    @Deprecated
    public static final String RECEIVE_SMS = "android.permission.RECEIVE_SMS";
    private static final String READ_SMS = "android.permission.READ_SMS";
    private static final String RECEIVE_WAP_PUSH = "android.permission.RECEIVE_WAP_PUSH";
    private static final String RECEIVE_MMS = "android.permission.RECEIVE_MMS";

    public static final String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";
    public static final String WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";

    public static final class Group {
        public static final String[] CAMERA = new String[]{Permission.CAMERA};

        public static final String[] CONTACTS = new String[]{
                Permission.READ_CONTACTS,
                Permission.GET_ACCOUNTS};

        public static final String[] LOCATION = new String[]{
                Permission.ACCESS_FINE_LOCATION,
                Permission.ACCESS_COARSE_LOCATION};

        public static final String[] MICROPHONE = new String[]{Permission.RECORD_AUDIO};

        public static final String[] PHONE = new String[]{
                Permission.READ_PHONE_STATE,
                Permission.CALL_PHONE};

//        public static final String[] SENSORS = new String[]{Permission.BODY_SENSORS};

        @Deprecated
        public static final String[] SMS = new String[]{
                Permission.SEND_SMS,
                Permission.RECEIVE_SMS};

        public static final String[] STORAGE = new String[]{
                Permission.READ_EXTERNAL_STORAGE,
                Permission.WRITE_EXTERNAL_STORAGE};
    }
}
