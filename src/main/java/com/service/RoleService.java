package com.service;

import com.model.Role;

import java.util.List;

public interface RoleService {
    public Role addAndUpdateRole(Role role);
    public List<Role> getRoles();

    public Role findRoleById(int id);
    public void deleteRole(int id);
    public Role checkAndFindHrRole();
}
