package com.luizalabs.whitelist.integration;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.luizalabs.whitelist.enumeration.IntegrationResponseEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IntegrationResponse {

    private IntegrationResponseEnum status;
    @JsonInclude(Include.NON_NULL)
    private Map<String, String> erros;	
}
