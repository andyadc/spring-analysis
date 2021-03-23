package com.andyadc.bms.auth.entity;

import org.springframework.data.relational.core.mapping.Table;

@Table("auth_role_user")
public class AuthRoleMenu {

    private Long id;
    private Long roleId;
    private Long menuId;

    public AuthRoleMenu() {
    }

    public AuthRoleMenu(Long roleId, Long menuId) {
        this.roleId = roleId;
        this.menuId = menuId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }
}
