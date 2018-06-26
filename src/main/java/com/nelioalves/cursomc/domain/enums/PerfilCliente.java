package com.nelioalves.cursomc.domain.enums;

public enum PerfilCliente {

    ADMIN (1, "ROLE_ADMIN"),
    CLIENTE(2, "ROLE_CLIENTE");

    private int codigo;
    private String descricao;

    private PerfilCliente(int codigo, String descricao){
        this.codigo=codigo;
        this.descricao=descricao;
    };

    public int getCodigo(){
        return codigo;
    }

    public String getDescricao(){
        return descricao;
    }

    public static PerfilCliente toEnum(Integer codigo){
        if(codigo == null) {
            return null;
        }

        for(PerfilCliente x : PerfilCliente.values()){
            if(codigo.equals(x.getCodigo())) {
                return x;
            }
        }

        throw new IllegalArgumentException("Id inválido: " + codigo);
    }
}
