package com.projetoweb.oficinamecanica.entities.enums;

public enum OrderStatus {

    RECEBIDO(1),
    EM_DIAGNOSTICO(2),
    AGUARDANDO_APROVACAO(3),
    EM_EXECUCAO(4),
    FINALIZADO(5),
    ENTREGUE(6);

    private int code;

    private  OrderStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static OrderStatus valueOf(int code) {
        for (OrderStatus value : OrderStatus.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid OrderStatus code!");
    }
}
