package org.example.formgenerator.utils;

/**
 * 雪花算法工具类（生成全局唯一64位ID）
 * 适配分布式/高并发，无需配置，直接使用
 */
public class SnowFlakeUtil {
    // 起始的时间戳（2026-01-29，可自定义）
    private final static long START_TIMESTAMP = 1738108800000L;

    // 每一部分占用的位数
    private final static long SEQUENCE_BIT = 12; // 序列号占用的位数
    private final static long MACHINE_BIT = 5;   // 机器标识占用的位数
    private final static long DATA_CENTER_BIT = 5;// 数据中心占用的位数

    // 每一部分的最大值
    private final static long MAX_DATA_CENTER_NUM = -1L ^ (-1L << DATA_CENTER_BIT);
    private final static long MAX_MACHINE_NUM = -1L ^ (-1L << MACHINE_BIT);
    private final static long MAX_SEQUENCE = -1L ^ (-1L << SEQUENCE_BIT);

    // 每一部分向左的位移
    private final static long MACHINE_LEFT = SEQUENCE_BIT;
    private final static long DATA_CENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    private final static long TIMESTAMP_LEFT = DATA_CENTER_LEFT + DATA_CENTER_BIT;

    private long dataCenterId = 1;  // 数据中心ID（自定义，0-31）
    private long machineId = 1;     // 机器ID（自定义，0-31）
    private long sequence = 0L;     // 序列号（0-4095）
    private long lastTimestamp = -1L;// 上一次的时间戳

    // 单例模式（避免重复创建对象）
    private static class SnowFlakeHolder {
        private static final SnowFlakeUtil INSTANCE = new SnowFlakeUtil();
    }

    public static SnowFlakeUtil getInstance() {
        return SnowFlakeHolder.INSTANCE;
    }

    // 私有化构造器
    private SnowFlakeUtil() {}

    /**
     * 生成下一个ID
     */
    public synchronized long nextId() {
        long currTimestamp = getCurrTimestamp();
        // 如果当前时间小于上一次生成ID的时间，说明系统时钟回退，抛出异常
        if (currTimestamp < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards. Refusing to generate id.");
        }

        // 如果是同一时间生成的，则进行序列号自增
        if (currTimestamp == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            // 序列号达到最大值，等待下一个毫秒
            if (sequence == 0L) {
                currTimestamp = getNextMill();
            }
        } else {
            // 不同时间生成，序列号重置为0
            sequence = 0L;
        }

        lastTimestamp = currTimestamp;

        // 拼接各部分生成ID
        return (currTimestamp - START_TIMESTAMP) << TIMESTAMP_LEFT
                | dataCenterId << DATA_CENTER_LEFT
                | machineId << MACHINE_LEFT
                | sequence;
    }

    // 获取下一个毫秒数
    private long getNextMill() {
        long mill = getCurrTimestamp();
        while (mill <= lastTimestamp) {
            mill = getCurrTimestamp();
        }
        return mill;
    }

    // 获取当前时间戳（毫秒）
    private long getCurrTimestamp() {
        return System.currentTimeMillis();
    }

    /**
     * 生成带前缀的唯一编号（适配表单编号格式：FORM_+雪花ID）
     * @param prefix 前缀（如FORM_）
     * @return 唯一编号（如FORM_1738108800000123456）
     */
    public String generateUniqueNo(String prefix) {
        return prefix + nextId();
    }
}