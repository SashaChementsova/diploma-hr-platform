package com.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Password {
    private int idUser;
    private String oldPassword;
    private String newPassword;
    private String newPasswordRepeat;


}
