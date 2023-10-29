package org.click.carservice.db.enums;

public enum UserType {
    /**
     * 用户类型（type）：普通用户
     */
    TYPE_USER(0,"用户","普通用户"),
    /**
     * 用户类型：商户
     */
    TYPE_commercialTenant(1, "商户", "商户");

    private int type;
    private String name;
    private String depict;

    UserType(int type,String name,String depict){
        this.type=type;
        this.name=name;
        this.depict=depict;
    }

    public int getType() {
        return type;
    }

    public String getName(){
        return this.name;
    }

    public String getDepict(){
        return this.depict;
    }
}
