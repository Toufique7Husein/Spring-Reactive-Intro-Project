package com.curde.demo.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Profile {
    DEV("dev"),
    SIT("sit"),
    LT("lt"),
    UAT("uat"),
    PROD("prod");

    private final String value;
}
