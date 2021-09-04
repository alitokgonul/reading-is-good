package com.getir.readingisgood.controller.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    @ApiModelProperty(notes = "User name")
    private String username;

    @ApiModelProperty(notes = "Authenticated user token")
    private String token;
}
