package com.tiennln.springlogbookokhttpdemo.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * @author TienNLN
 * on 8/14/2022
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseResponseDTO implements Serializable {
    private static final long serialVersionUID = -3505274814963047680L;

    private String targetUrl;
    private boolean success;
    private ErrorDTO error;
    private boolean unAuthorizedRequest;
    private boolean __abp;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class ErrorDTO implements Serializable {
        private int code;
        private String message;
        private String details;
        private String validationErrors;

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}