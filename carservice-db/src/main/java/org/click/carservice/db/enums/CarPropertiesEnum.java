package org.click.carservice.db.enums;

/**
 * @title: CarPropertiesEnum
 * @Author HuangYan
 * @Date: 2023/8/12 19:09
 * @Version 1.0
 * @Description: 车辆性质
 */
public enum CarPropertiesEnum {

    OPERATION("0", "运营"),

    NON_OPERATION("1", "非运营");


    /**
     * 编码
     */
    private final String code;

    /**
     * 描述
     */
    private final String depict;


    CarPropertiesEnum(String code, String depict) {
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
    public String parseValue(String code) {
        if (code != null) {
            for (CarPropertiesEnum item : values()) {
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
            for (CarPropertiesEnum item : values()) {
                if (item.depict.equals(depict)) {
                    return item.code;
                }
            }
        }
        throw new IllegalStateException("depict不支持");
    }

}
