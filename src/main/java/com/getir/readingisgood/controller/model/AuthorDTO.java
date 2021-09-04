package com.getir.readingisgood.controller.model;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthorDTO {

    @ApiModelProperty(notes = "Id")
    private Long id;

    @ApiModelProperty(notes = "Name", required = true)
    @NotBlank(message = "Name is mandatory")
    private String name;

    @ApiModelProperty(notes = "Last name", required = true)
    @NotBlank(message = "Last name is mandatory")
    private String lastName;
}
