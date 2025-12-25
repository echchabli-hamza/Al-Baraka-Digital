package com.AlBaraka.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NonNull
    private String email;
    @NonNull
    private String password;
    private Boolean active ;


    public boolean isActive(){
        return active;
    }
}