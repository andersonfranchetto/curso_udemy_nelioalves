package com.nelioalves.cursomc.resources;

import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.dto.ClienteDTO;
import com.nelioalves.cursomc.repositories.ClienteRepository;
import com.nelioalves.cursomc.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value="/clientes")
public class ClienteResource {

	@Autowired
	ClienteService service;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Cliente> find(@PathVariable Integer id){
		Cliente cliente = service.find(id);
		return ResponseEntity.ok().body(cliente);
	}

	@RequestMapping(value = "/email", method = RequestMethod.GET)
	public ResponseEntity<Cliente> find(@RequestParam(value = "value") String email){
		Cliente cliente = service.getByEmail(email);
		return ResponseEntity.ok().body(cliente);
	}

	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody ClienteDTO clienteDTO){
		Cliente cliente = service.fromDTO(clienteDTO);
		cliente = service.insert(cliente);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(cliente.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

	@RequestMapping(value = "/picture", method=RequestMethod.POST)
	public ResponseEntity<Void> uploadProfilePicture(@RequestParam(name = "file")MultipartFile multipartFile){
		URI uri = service.uploadProfilePicture(multipartFile);
		return ResponseEntity.created(uri).build();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> update(@PathVariable Integer id, @Valid @RequestBody ClienteDTO clienteDTO){
		Cliente cliente = service.fromDTO(clienteDTO);
		cliente.setId(id);
		cliente = service.update(cliente);
		return ResponseEntity.noContent().build();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Void> delete(@PathVariable Integer id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@RequestMapping(method = RequestMethod.GET)
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<List<ClienteDTO>> findAll(){
		List<Cliente> clientes = service.findAll();
		List<ClienteDTO> clientesDTO = clientes.stream().map(objeto -> new ClienteDTO(objeto)).collect(Collectors.toList());
		return ResponseEntity.ok().body(clientesDTO);
	}

	//PAGINAÇÃO TOP...
	@RequestMapping(value = "/page" ,method = RequestMethod.GET)
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Page<ClienteDTO>> findAll(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
			@RequestParam(value = "orderBy", defaultValue = "nome") String orderBy,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction){
		Page<Cliente> clientes = service.findPage(page, linesPerPage, orderBy, direction);
		Page<ClienteDTO> clientesDTO = clientes.map(objeto -> new ClienteDTO(objeto));
		return ResponseEntity.ok().body(clientesDTO);
	}
}
