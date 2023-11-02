package org.click.carservice.db.enums;

/**
 * 角色枚举
 */
public enum UserRole {
    /**
     * 角色：超级管理员
     */
    Role_Super_Admin("1","超级管理员","所有模块的权限"),
    /**
     * 角色：商户
     */
    Role_commercialTenant("2", "商户", "商户权限"),
    /**
     * 角色：管理员
     */
    Role_Admin("1697243432151552000","管理员","后端管理员");

    private String id;
    private String name;
    private String depict;

    UserRole(String id,String name,String depict){
        this.id=id;
        this.name=name;
        this.depict=depict;
    }

    public String getId() {
        return id;
    }

    public String getName(){
        return this.name;
    }

    public String getDepict(){
        return this.depict;
    }
}
