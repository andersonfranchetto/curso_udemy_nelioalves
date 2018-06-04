package com.nelioalves.cursomc.services.validators;

import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.domain.enums.TipoCliente;
import com.nelioalves.cursomc.dto.ClienteDTO;
import com.nelioalves.cursomc.resources.exceptions.FieldMessage;
import com.nelioalves.cursomc.services.ClienteService;
import com.nelioalves.cursomc.services.validators.util.BR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClienteValidation implements ConstraintValidator<ClienteValidator, ClienteDTO> {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private ClienteService service;

    @Override
    public void initialize(ClienteValidator ann){}

    @Override
    public boolean isValid(ClienteDTO clienteDTO, ConstraintValidatorContext context){

        Map<String, String> map = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Integer uriId = Integer.parseInt(map.get("id") == null ? "0" : map.get("id"));
        List<FieldMessage> list;

        System.out.println("uriId --> "+uriId);

        if(uriId == 0){
            list = toInsert(clienteDTO, uriId);
        } else {
            list = toUpdate(clienteDTO, uriId);
        }

        list.forEach(add->{
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(add.getMessage())
                   .addPropertyNode(add.getFieldName())
                   .addConstraintViolation();
        });
        return list.isEmpty();
    }

    private List<FieldMessage> toInsert(ClienteDTO clienteDTO, Integer uriId){
        List<FieldMessage> list = new ArrayList<>();
        if (clienteDTO.getTipo().equals(TipoCliente.PESSOAFISICA.getCodigo()) && !BR.isValidCPF(clienteDTO.getCpfOuCnpj())){
            list.add(new FieldMessage("cpfOuCnpj", "CPF digitado é inválido"));
        }

        if (clienteDTO.getTipo().equals(TipoCliente.PESSOAJURIDICA.getCodigo()) && !BR.isValidCNPJ(clienteDTO.getCpfOuCnpj())) {
            list.add(new FieldMessage("cpfOuCnpj", "CNPJ digitado é inválido"));
        }

        if (service.findByEmail(clienteDTO.getEmail()) != null) {
            list.add(new FieldMessage("email", "INSERT - Email já cadastrado, digite outro."));
        }
        return list;
    }

    private List<FieldMessage> toUpdate(ClienteDTO clienteDTO, Integer uriId){
        List<FieldMessage> list = new ArrayList<>();
        Cliente cliente = service.findByEmail(clienteDTO.getEmail());
        if(cliente != null && !cliente.getId().equals(uriId)){
           list.add(new FieldMessage("email", "PUT - Email já cadastrado, digite outro."));
        }
        return list;
    }
}
