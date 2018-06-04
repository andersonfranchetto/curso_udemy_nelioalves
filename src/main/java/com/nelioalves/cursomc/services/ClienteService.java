package com.nelioalves.cursomc.services;

import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.domain.Endereco;
import com.nelioalves.cursomc.domain.enums.TipoCliente;
import com.nelioalves.cursomc.dto.ClienteDTO;
import com.nelioalves.cursomc.repositories.ClienteRepository;
import com.nelioalves.cursomc.services.exceptions.DataIntegrityException;
import com.nelioalves.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    ClienteRepository repository;

    @Autowired
    EnderecoService enderecoService;

    public Cliente find(Integer id){
        Cliente cliente = repository.findOne(id);
        if(cliente == null){
            throw new ObjectNotFoundException("Objeto não encontrado! Id: " + id
                    + ", Tipo: " + Cliente.class.getName());
        }
        return cliente;
    }

    public Cliente insert(Cliente cliente){
        return this.merge(cliente);
    }

    public Cliente update(Cliente cliente){
        find(cliente.getId());//VERIFICA SE ID EXISTE.
        return this.merge(cliente);
    }

    public void delete(Integer id){
        find(id);
        try {
            repository.delete(id);
        } catch (DataIntegrityViolationException e){
            throw new DataIntegrityException("Não é possível excluir um cliente relacionado a um ou mais pedidos.");
        }
    }

    public List<Cliente> findAll(){
        return repository.findAll();
    }

    public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
        PageRequest pageRequest = new PageRequest(page, linesPerPage, Sort.Direction.valueOf(direction),orderBy);
        return repository.findAll(pageRequest);
    }

    public Cliente fromDTO(ClienteDTO clienteDTO){
        Cliente cliente = new Cliente(clienteDTO.getId(), clienteDTO.getNome(), clienteDTO.getEmail(), clienteDTO.getCpfOuCnpj(), TipoCliente.toEnum(clienteDTO.getTipo()));
        cliente.getEnderecos().addAll(clienteDTO.getEnderecos());
        cliente.getTelefones().addAll(clienteDTO.getTelefones());
        return cliente;
    }

    public Cliente findByEmail(String email){
        return repository.findByEmail(email);
    }

    public Cliente merge(Cliente cliente){
        Cliente newCliente = repository.save(cliente);
        for(Endereco newEndereco : cliente.getEnderecos()){
            newEndereco.setCliente(cliente);
            enderecoService.insert(newEndereco);
        }
        return newCliente;
    }
}