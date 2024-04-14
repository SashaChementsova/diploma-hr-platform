package com.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


public class UsersDto {
    private List<UserDto> users;
    public void addUserDto(UserDto userDto){
        users.add(userDto);
    }

    public UsersDto(List<UserDto> users) {
        this.users = users;
    }

    public UsersDto() {
        users=new ArrayList<>();
    }

    public List<UserDto> getUsers() {
        return users;
    }

    public void setUsers(List<UserDto> users) {
        this.users = users;
    }
}
