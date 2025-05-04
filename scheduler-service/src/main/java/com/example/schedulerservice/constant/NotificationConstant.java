package com.example.schedulerservice.constant;

public class NotificationConstant {
    public static String BEARER = "Bearer";
    public static int BEARER_LENGTH = 6;
    public static final String TOKEN_BLACK_LIST = "TOKEN_BLACK_LIST";
    public static final String USER_CODE = "userCode";

    //
    // WebSocket Constant
    //
    public static final String WS_APP_PREFIX = "/ws-app";
    public static final String WS_TOPIC_PREFIX = "/ws-topic";
    public static final String WS_ENDPOINT = "/ws-endpoint";
    public static final String WS_BROADCAST_TOPIC = "/ws-topic/broadcast";
    public static final String WS_SPECIFIC_TOPIC = "/ws-topic/specific";
    public static final String WS_ALARM_TOPIC = "/ws-topic/specific/alarm";
    //
    // Notification Type Constant
    //
    public static final String WS_NOTIFICATION = "web_socket";
    public static final String DB_NOTIFICATION = "db";
    public static final String EMAIL_NOTIFICATION = "email";

    public static final String TELE_NOTIFICATION = "telegram";

    public static final Byte UN_READ = 0;
    public static final Byte READED = 1;

    public static final Byte DB_TYPE = 1;
    public static final Byte EMAIL_TYPE = 2;
    public static final Byte WS_TYPE = 3;
    public static final Byte TELE_TYPE = 4;
    public static final Byte UNKNOWN_TYPE = 0;

    public static final Boolean BROADCAST = Boolean.TRUE;
    public static final Boolean SPECIFIC = Boolean.FALSE;
    //
    //OTP
    //
    public static long OTP_TIMEOUT = 10l;
    public static int OTP_LENGTH = 6;
    public static String OTP_KEY = "495189262794738883448337";

    public static final String OTP_BYPASS_KEY_REDIS = "otp_bypass_15611299";
    public static final String OTP_BYPASS = "136868";
    public static final byte OTP_EXPIRED_DAYS = 7;

    public static final String OTP_PRE = "OTP";
    public static final String DISABLE_LABEL = "DISABLE";
    public static final String ENABLE_LABEL = "ENABLE";
    public static final String UNDERSCORE = "_";
    public static final String DASH = "-";

    //
    //Security
    //
    public static final String SECURITY_KEY_REDIS = "vnpt_idg_redis_kafka_mysql_1234567890";
    public static final String SECURITY_KEY_KAFKA = "vnpt_idg_redis_kafka_mysql_1234567890";
    public static final String SECURITY_KEY_MYSQL = "vnpt_idg_redis_kafka_mysql_1234567890";

    public static final String NOTI_OK = "OK";
    public static final String NOTI_ERROR = "ERROR";
    public static final String OTP_COUNTER_SUFFIX = "-COUNTER";

    public static final int IDGFULL_ROLE_ID = 52;               // ID CUA ROLE IDG-FULL

    public static final String UI_CACHE = "UI_CACHE_";               // TIEN TO LUU THONG TIN USER, vd UI_CACHE_userCode

    public static final String CACHE_CA = "CACHE_CA_";

    public static final String EMAIL_QUEUE_REDIS_KEY = "EMAIL_QUEUE";

    public static final String VALIDATED_OTP = "Nhập mã xác thực thành công!";

    private NotificationConstant() {}

    public static final String ENCODED_INSTANCE = "desede/CBC/PKCS5Padding";
    public static final String USERNAME_PATTERN = "^[a-zA-Z0-9._-]{2,}$";
    public static final String U_PATTERN = "[^A-Za-z0-9]";
    public static final String TEMP_EMAIL_PREFIX = "email_attachments";
    public static final String TEMP_LOG_SUFFIX = ".txt";
    public static final String TEMP_LOG_PREFIX = "log_";
    public static final String RESOLVED_STT= "resolved";
}
