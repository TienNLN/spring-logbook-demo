package com.tiennln.springlogbookokhttpdemo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author TienNLN on 10/01/2023
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 45657628522808145L;

    private Object body;
    private String message;
    private int errorCode;

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
