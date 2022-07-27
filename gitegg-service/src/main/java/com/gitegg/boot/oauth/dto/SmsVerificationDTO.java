package com.gitegg.boot.oauth.dto;

import lombok.Data;

@Data
public class SmsVerificationDTO {

    private String smsCode;

    private String phoneNumber;

    private String code;
}
