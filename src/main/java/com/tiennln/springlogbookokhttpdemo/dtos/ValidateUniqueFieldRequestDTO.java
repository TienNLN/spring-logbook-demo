package com.tiennln.springlogbookokhttpdemo.dtos;

import lombok.*;

import java.io.Serializable;

/**
 * @author TienNLN
 * on 8/14/2022
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ValidateUniqueFieldRequestDTO implements Serializable {

    private static final long serialVersionUID = 421541390293526107L;

    private String field;
    private String value;
    private String crmMemberID;
}