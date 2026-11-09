package com.axin.errand.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "PhoneAuthDTO")
public class PhoneAuthDTO {
    @Schema(title = "手机号授权code")
    private String code;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}

