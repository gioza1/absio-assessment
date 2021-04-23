package com.sample.dto.user;

import com.sample.dto.common.validator.annotation.Base64Encoded;
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
public class ChangePasswordDto {
    @Base64Encoded
    @ApiModelProperty(value = "The hash of the user's new password.", required = true, example = "$2a$10$nF.Ua32ZfkowXhn/pzcxBuGDkeCJY77icb8w4apxn714rxmdK06a.")
    private String newPassword;
    @ApiModelProperty(value = "The user's name who is authenticating.", required = true, example = "user@email.com")
    private String username;
}
