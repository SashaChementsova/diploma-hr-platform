package com.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto
{
    private int id;
    //@NotEmpty(message = "Поле Email должно быть заполнено.")
    //@Email
    private String email;
    //@NotEmpty(message = "Поле Пароль должно быть заполнено.")
    private String password;
    //@NotEmpty(message = "Поле Имя должно быть заполнено.")
    private String name;
    private String patronymic;
    //@NotEmpty(message = "Поле Фамилия должно быть заполнено.")
    private String surname;
   // @NotEmpty(message = "Поле Номер телефона должно быть заполнено.")
    private String phone;
    //@NotEmpty(message = "Поле Дата рождения должно быть заполнено.")
    private Date birthday;
    private String info;

    private MultipartFile file1;
}
