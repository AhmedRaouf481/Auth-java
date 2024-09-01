package com.araouf.server.domain.request;

import java.time.LocalDate;

import org.hibernate.validator.constraints.Range;

import com.araouf.server.domain.db.enums.Gender;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    
    @NotBlank
    @Email
    private String email;
    
    @NotBlank
    private String password;
    
    @NotBlank
    private String fname;
    
    @NotBlank
    private String lname;
    
    @NotNull
    private LocalDate birthDate;
    
    @NotBlank
    private String phone;
    
    @NotNull
    private Gender gender;
    
    @NotNull
    @Range(min = 1, max =3)
    private Integer roleId;

}
