package com.youcode.myaftas.dto.authDTO;

import com.youcode.myaftas.entities.Role;
import com.youcode.myaftas.enums.IdentityDocumentType;
import com.youcode.myaftas.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
    @NotNull(message = "name should be not Empty")
    private String name;

    @NotNull(message = "family Name should be not Empty")
    private String familyName;

    @NotNull(message = "accession Date should not be null")
    private LocalDate accessionDate;

    @NotNull(message = "nationality should not be null")
    private String nationality;

    @NotEmpty(message = "Identity Document Type should not be empty")
    private IdentityDocumentType identityDocumentType;

    @NotNull(message = "Identity number should not be null")
    private String identityNumber;

    @NotBlank(message = "username should be not Empty")
    private String username;

    @NotBlank(message = "password should be not Empty")
    private String password;

    private Role role;
}
