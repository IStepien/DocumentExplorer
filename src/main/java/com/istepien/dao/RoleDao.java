package com.istepien.dao;

import com.istepien.model.Role;

import java.util.List;
import java.util.Set;

public interface RoleDao {
    public Set<Role> getAllRoles();
    public Role getRoleByName(String rolename);

}
