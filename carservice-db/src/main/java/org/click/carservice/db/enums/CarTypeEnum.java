package org.click.carservice.db.enums;

/**
 * 轿车类型
 */
public enum CarTypeEnum {

    SEDAN("0", "轿车"),

    SUV("1", "SUV"),

    MPV("2", "MPV"),

    OTHER("3", "其他");


    /**
     * 编码
     */
    private final String code;

    /**
     * 描述
     */
    private final String depict;


    CarTypeEnum(String code, String depict) {
        this.code = code;
        this.depict = depict;
    }

    public String getCode() {
        return code;
    }

    public String getDepict() {
        return depict;
    }

    /**
     * 根据code获取描述
     * @param code
     * @return
     */
    public static String parseValue(String code) {
        if (code != null) {
            for (CarTypeEnum item : values()) {
                if (item.code.equals(code)) {
                    return item.depict;
                }
            }
        }
        throw new IllegalStateException("code不支持");
    }

    /**
     * 根据描述获取code
     * @param depict
     * @return
     */
    public static String parseCode(String depict) {
        if (depict != null) {
            for (CarTypeEnum item : values()) {
                if (item.depict.equals(depict)) {
                    return item.code;
                }
            }
        }
        throw new IllegalStateException("depict不支持");
    }
}