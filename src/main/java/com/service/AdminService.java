package com.service;

import com.model.Admin;

import java.util.List;

public interface AdminService {
    public Admin addAndUpdateAdmin(Admin admin);
    public List<Admin> getAdmins();
    public Admin findAdminById(int id);

    public void deleteAdmin(int id);
    public void initializeAdmin();
}
