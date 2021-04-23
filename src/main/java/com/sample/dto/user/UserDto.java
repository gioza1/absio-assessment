package com.sample.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class UserDto {
    @ApiModelProperty(value = "User's database id", example = "1")
    private Integer id;
    @NotNull
    @Size(min = 1)
    @ApiModelProperty(value = "Username is email address", required = true, example = "john.doe@absio.com")
    private String username;
    @ApiModelProperty(value = "User's first name", example = "John")
    private String first_name;
    @ApiModelProperty(value = "User's last name", example = "Doe")
    private String last_name;
}
