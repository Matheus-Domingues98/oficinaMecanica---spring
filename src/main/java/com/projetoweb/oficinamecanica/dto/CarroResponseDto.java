package com.projetoweb.oficinamecanica.dto;

import com.projetoweb.oficinamecanica.entities.Carro;

public class CarroResponseDto {
    
    private Long id;
    private String modelo;
    private String placa;
    private String cor;
    private Integer anoFabricacao;
    private String marca;
    
    public CarroResponseDto() {
    }
    
    public CarroResponseDto(Carro carro) {
        this.id = carro.getId();
        this.modelo = carro.getModelo();
        this.placa = carro.getPlaca();
        this.cor = carro.getCor();
        this.anoFabricacao = carro.getAnoFabricacao();
        this.marca = carro.getMarca();
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getModelo() {
        return modelo;
    }
    
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }
    
    public String getPlaca() {
        return placa;
    }
    
    public void setPlaca(String placa) {
        this.placa = placa;
    }
    
    public String getCor() {
        return cor;
    }
    
    public void setCor(String cor) {
        this.cor = cor;
    }
    
    public Integer getAnoFabricacao() {
        return anoFabricacao;
    }
    
    public void setAnoFabricacao(Integer anoFabricacao) {
        this.anoFabricacao = anoFabricacao;
    }
    
    public String getMarca() {
        return marca;
    }
    
    public void setMarca(String marca) {
        this.marca = marca;
    }
}
