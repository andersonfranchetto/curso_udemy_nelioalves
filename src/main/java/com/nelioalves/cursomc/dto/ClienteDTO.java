package com.nelioalves.cursomc.dto;

import com.nelioalves.cursomc.domain.Cliente;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class ClienteDTO {

    private Integer id;
    @NotEmpty(message = "Preenchimento obrigatório")
    @Length(min = 5, max = 120, message = "O Nome deve conter entre 5 e 120 caracteres")
    private String nome;
    @NotEmpty(message = "Preenchimento obrigatório")
    @Email(message = "Email inválido")
    private String email;

    public ClienteDTO(){}

    public ClienteDTO(Cliente cliente){
        this.id = cliente.getId();
        this.nome = cliente.getNome();
        this.email = cliente.getEmail();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
