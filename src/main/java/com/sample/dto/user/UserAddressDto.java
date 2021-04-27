package com.sample.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class UserAddressDto {
    @NotNull
    @ApiModelProperty(value = "Address's database id", example = "1")
    private Integer id;
    @NotNull
    @ApiModelProperty(hidden = true)
    private Integer user_id;
    @ApiModelProperty(value = "Street name", example = "2435 Fair Oaks Blvd")
    private String street;
    @ApiModelProperty(value = "State address", example = "California")
    private String state;
    @ApiModelProperty(value = "Zip code", example = "92024-2852")
    private String zip;
}
