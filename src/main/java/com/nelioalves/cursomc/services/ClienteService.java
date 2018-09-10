package com.nelioalves.cursomc.services;

import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.domain.Endereco;
import com.nelioalves.cursomc.domain.enums.PerfilCliente;
import com.nelioalves.cursomc.domain.enums.TipoCliente;
import com.nelioalves.cursomc.dto.ClienteDTO;
import com.nelioalves.cursomc.repositories.ClienteRepository;
import com.nelioalves.cursomc.security.domain.User;
import com.nelioalves.cursomc.security.exceptions.AuthorizationException;
import com.nelioalves.cursomc.security.services.UserService;
import com.nelioalves.cursomc.services.exceptions.DataIntegrityException;
import com.nelioalves.cursomc.services.exceptions.ObjectNotFoundException;
import com.nelioalves.cursomc.services.validators.HasRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    ClienteRepository repository;

    @Autowired
    EnderecoService enderecoService;

    public Cliente find(Integer id){

        if(HasRole.hasRoleUser(id))
            throw new AuthorizationException("Acesso negado.");

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
        Cliente cliente = new Cliente(clienteDTO.getId(), clienteDTO.getNome(), clienteDTO.getEmail(), clienteDTO.getCpfOuCnpj(), TipoCliente.toEnum(clienteDTO.getTipo()), bCryptPasswordEncoder.encode(clienteDTO.getSenha()));
        cliente.getEnderecos().addAll(clienteDTO.getEnderecos());
        cliente.getTelefones().addAll(clienteDTO.getTelefones());
        return cliente;
    }

    public Cliente getByEmail(String email) {
        User user = UserService.authenticated();
        if(user ==  null || !user.hasRole(PerfilCliente.ADMIN) && !email.equals(user.getUsername())) {
            throw new AuthorizationException("Access Denied");
        }

        Cliente cliente = this.findByEmail(email);
        if(cliente == null) {
            throw new ObjectNotFoundException("Objeto não encontrado! Id: " + user.getId() + ", Tipo: " + Cliente.class.getName());
        }

        return cliente;
    }

    public Cliente findByEmail(String email){
        return repository.findByEmail(email);
    }

    private Cliente merge(Cliente cliente){
        Cliente newCliente = repository.save(cliente);
        for(Endereco newEndereco : cliente.getEnderecos()){
            newEndereco.setCliente(cliente);
            enderecoService.insert(newEndereco);
        }
        return newCliente;
    }
}