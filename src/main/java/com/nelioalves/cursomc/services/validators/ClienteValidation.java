package com.nelioalves.cursomc.services.validators;

import com.nelioalves.cursomc.domain.enums.TipoCliente;
import com.nelioalves.cursomc.dto.ClienteDTO;
import com.nelioalves.cursomc.resources.exceptions.FieldMessage;
import com.nelioalves.cursomc.services.ClienteService;
import com.nelioalves.cursomc.services.validators.util.BR;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class ClienteValidation implements ConstraintValidator<ClienteValidator, ClienteDTO> {

    @Autowired
    ClienteService service;

    @Override
    public void initialize(ClienteValidator ann){}

    @Override
    public boolean isValid(ClienteDTO clienteDTO, ConstraintValidatorContext context){
        List<FieldMessage> list = new ArrayList<>();

        if(clienteDTO.getTipo().equals(TipoCliente.PESSOAFISICA.getCodigo()) && !BR.isValidCPF(clienteDTO.getCpfOuCnpj()))
            list.add(new FieldMessage("cpfOuCnpj", "CPF digitado é inválido"));

        if(clienteDTO.getTipo().equals(TipoCliente.PESSOAJURIDICA.getCodigo()) && !BR.isValidCNPJ(clienteDTO.getCpfOuCnpj()))
            list.add(new FieldMessage("cpfOuCnpj", "CNPJ digitado é inválido"));

        if(service.findByEmail(clienteDTO.getEmail()) != null)
            list.add(new FieldMessage("email", "Email já cadastrado, digite outro."));

        list.forEach(add->{
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(add.getMessage())
                   .addPropertyNode(add.getFieldName())
                   .addConstraintViolation();
        });
        return list.isEmpty();
    }
}
