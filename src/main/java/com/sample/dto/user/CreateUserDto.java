package com.sample.dto.user;

import com.sample.domain.Address;
import com.sample.dto.common.validator.annotation.Base64Encoded;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class CreateUserDto {
    @NotNull
    @Size(min = 1)
    @ApiModelProperty(value = "Username is email address", required = true, example = "john.doe@absio.com")
    private String username;
    @NotNull
    @Base64Encoded
    @ApiModelProperty(value = "The hash of the user's password.", required = true, example = "$2a$10$nF.Ua32ZfkowXhn/pzcxBuGDkeCJY77icb8w4apxn714rxmdK06a.")
    private String password;
    @ApiModelProperty(value = "User's first name", example = "John")
    private String first_name;
    @ApiModelProperty(value = "User's last name", example = "Doe")
    private String last_name;
    @NotNull
    @Size(min = 1)
    @ApiModelProperty(value = "{\n'street':'234 Spruce Avenue Belleville',\n'state':'NJ'\n},\n{\n'zip':'07109'\n}")
    private List<Address> addresses;

}
