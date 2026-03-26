package com.projetoweb.oficinamecanica.dto;

import com.projetoweb.oficinamecanica.entities.Cliente;

public class ClienteResponseDto {

    private Long id;
    private String nome;
    private String telefone;
    private String email;
    private String doc;

    public ClienteResponseDto() {
    }

    public ClienteResponseDto(Cliente cliente) {
        this.id = cliente.getId();
        this.nome = cliente.getNome();
        this.telefone = cliente.getTelefone();
        this.email = cliente.getEmail();
        this.doc = cliente.getDoc();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEmail() {
        return email;
    }

    public String getDoc() {
        return doc;
    }
}
