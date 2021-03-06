package com.guosh.security.core.validate.code.sms;

import com.guosh.security.core.properties.SecurityProperties;
import com.guosh.security.core.validate.code.ValidateCode;
import com.guosh.security.core.validate.code.ValidateCodeGenerator;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;


@Component("smsValidateCodeGenerator")
public class SmsCodeGenerator implements ValidateCodeGenerator{

    @Autowired
    private SecurityProperties securityProperties;

    /**
     * 生成验证码
     * @param request
     * @return
     */
    @Override
    public ValidateCode generate(ServletWebRequest request) {
        String code= RandomStringUtils.randomNumeric(securityProperties.getCode().getSms().getLength());

        return new ValidateCode(code,securityProperties.getCode().getSms().getExpreIn());
    }


}
