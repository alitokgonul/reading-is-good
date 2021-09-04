package com.getir.readingisgood.controller.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerDTO {

    @ApiModelProperty(notes = "Customer name", required = true)
    @NotBlank(message = "Name is mandatory")
    private String name;

    @ApiModelProperty(notes = "Customer last name", required = true)
    @NotBlank(message = "Last name is mandatory")
    private String lastName;

    @ApiModelProperty(notes = "Customer username", required = true)
    @NotBlank(message = "Username is mandatory")
    private String username;

    @ApiModelProperty(notes = "Customer password", required = true)
    @NotBlank(message = "Password is mandatory")
    private String password;

    @ApiModelProperty(notes = "Customer email", example = "username@mail.com", required = true)
    @Email(message = "Email should be valid")
    private String email;

    @ApiModelProperty(notes = "Customer phone number", example = "5555555555", required = true)
    @Pattern(regexp="(^$|[0-9]{10})", message = "It should be 10 character number")
    private String phone;
}
