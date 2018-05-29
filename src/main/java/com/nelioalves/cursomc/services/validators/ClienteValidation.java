package com.nelioalves.cursomc.services.validators;

import com.nelioalves.cursomc.domain.enums.TipoCliente;
import com.nelioalves.cursomc.dto.ClienteDTO;
import com.nelioalves.cursomc.resources.exceptions.FieldMessage;
import com.nelioalves.cursomc.services.validators.util.BR;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class ClienteValidation implements ConstraintValidator<ClienteValidator, ClienteDTO> {

    @Override
    public void initialize(ClienteValidator ann){}

    @Override
    public boolean isValid(ClienteDTO tipo, ConstraintValidatorContext context){
        List<FieldMessage> list = new ArrayList<>();

        if(tipo.getTipo().equals(TipoCliente.PESSOAFISICA.getCodigo()) && !BR.isValidCPF(tipo.getCpfOuCnpj()))
            list.add(new FieldMessage("cpfOuCnpj", "CPF digitado é inválido"));

        if(tipo.getTipo().equals(TipoCliente.PESSOAJURIDICA.getCodigo()) && !BR.isValidCNPJ(tipo.getCpfOuCnpj()))
            list.add(new FieldMessage("cpfOuCnpj", "CNPJ digitado é inválido"));

        list.forEach(add->{
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(add.getMessage())
                   .addPropertyNode(add.getFieldName())
                   .addConstraintViolation();
        });
        return list.isEmpty();
    }
}
