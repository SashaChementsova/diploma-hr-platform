package com.service.serviceImpl;

import com.model.Role;
import com.repository.RoleRepository;
import com.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }
    @Override
    public Role addAndUpdateRole(Role role){
        return roleRepository.save(role);
    }
    @Override
    public List<Role> getRoles(){
        return roleRepository.findAll();
    }
    @Override
    public Role findRoleById(int id){

        return roleRepository.findById(id).orElse(null);
    }
    @Override
    public void deleteRole(int id){
        roleRepository.deleteById(id);
    }

    @Override
    public Role checkAndFindHrRole(){
        Role role=roleRepository.findByNameRole("ROLE_HR");
        if(role==null){
            role=new Role();
            role.setNameRole("ROLE_HR");
            role= addAndUpdateRole(role);
        }
        System.out.println(role.getNameRole()+" service");
        return role;
    }
}
