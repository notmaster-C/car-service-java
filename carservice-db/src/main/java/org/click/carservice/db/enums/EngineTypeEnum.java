package org.click.carservice.db.enums;

/**
 * 动力类型
 */
public enum EngineTypeEnum {

    FUEL("0", "燃油车"),

    NEW_ENERGY("1", "新能源车");


    /**
     * 编码
     */
    private final String code;

    /**
     * 描述
     */
    private final String depict;


    EngineTypeEnum(String code, String depict) {
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
            for (EngineTypeEnum item : values()) {
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
            for (EngineTypeEnum item : values()) {
                if (item.depict.equals(depict)) {
                    return item.code;
                }
            }
        }
        throw new IllegalStateException("depict不支持");
    }
}