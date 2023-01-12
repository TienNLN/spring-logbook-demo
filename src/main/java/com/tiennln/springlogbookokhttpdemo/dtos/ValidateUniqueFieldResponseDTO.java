package com.tiennln.springlogbookokhttpdemo.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * @author TienNLN
 * on 8/14/2022
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ValidateUniqueFieldResponseDTO extends BaseResponseDTO implements Serializable {

    private static final long serialVersionUID = -2495470802750268987L;

    private ResultValidateUniqueDTO result;


    @Getter
    @Setter
    public static class ResultValidateUniqueDTO implements Serializable {
        private static final long serialVersionUID = -566560572593034585L;
        @JsonProperty(value = "isValid")
        private boolean isValid;

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