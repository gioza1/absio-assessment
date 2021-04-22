package com.sample.dto;

import com.sample.dto.validator.annotation.Base64Encoded;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class AuthenticationCredentialsDto {
    @Base64Encoded
    @ApiModelProperty(value = "The hash of the user's password.", required = true, example = "$2a$10$nF.Ua32ZfkowXhn/pzcxBuGDkeCJY77icb8w4apxn714rxmdK06a.")
    private String password;
    @ApiModelProperty(value = "The user's name who is authenticating.", required = true, example = "user@email.com")
    private String username;
}
