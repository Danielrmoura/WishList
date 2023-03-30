package com.luizalabs.whitelist.mongo;

import java.util.Date;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;


@Data
public class Product {

	@Id
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String _id;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Long idProduct;
	@JsonInclude(JsonInclude.Include.NON_NULL)
    private String nome;
	@JsonInclude(JsonInclude.Include.NON_NULL)
    private String descricao;
	@JsonInclude(JsonInclude.Include.NON_NULL)
    private Double perco;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String usuarioReg;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date datahoraReg;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String usuario;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date datahora;
	
}
