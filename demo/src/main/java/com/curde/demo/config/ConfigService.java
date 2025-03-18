package com.curde.demo.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;


/**
 * @author Md Toufique Husein
 * @since 17 March 2025
 * */

@Getter
@Setter
@ToString
@Configuration
public class ConfigService {

    //@Value("${default.error.message.en}")
    private String defaultErrorMessageEn;

    //@Value("${dynamo.url}")
    private String dynamoUrl;
    //@Value("${aws.region}")
    private String region;
}
