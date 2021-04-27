package com.sample.dto.address;

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
public class AddressDto {
    @NotNull
    @ApiModelProperty(value = "Address's database id", example = "1")
    private Integer id;
    @NotNull
    @ApiModelProperty(value = "Foreign key of user", required = true, example = "1")
    private Integer userId;
    @ApiModelProperty(value = "Street name", example = "2435 Fair Oaks Blvd")
    private String street;
    @ApiModelProperty(value = "State address", example = "California")
    private String state;
    @ApiModelProperty(value = "Zip code", example = "92024-2852")
    private String zip;
}
