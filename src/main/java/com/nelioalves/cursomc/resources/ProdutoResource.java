package com.nelioalves.cursomc.resources;

import com.nelioalves.cursomc.domain.Produto;
import com.nelioalves.cursomc.dto.ProdutoDTO;
import com.nelioalves.cursomc.resources.utils.URL;
import com.nelioalves.cursomc.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="/produtos")
public class ProdutoResource {

	@Autowired
	ProdutoService service;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Produto> find(@PathVariable Integer id){
		Produto produto = service.find(id);
		return ResponseEntity.ok().body(produto);
	}

	@RequestMapping(value = "/page" ,method = RequestMethod.GET)
	public ResponseEntity<Page<ProdutoDTO>> findAll(
			@RequestParam(value = "nome", defaultValue = "") String nome,
			@RequestParam(value = "categorias", defaultValue = "") String categorias,
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
			@RequestParam(value = "orderBy", defaultValue = "nome") String orderBy,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction){

		Page<Produto> produtos = service.search(URL.decodeStringParam(nome), URL.stringToIntegerList(categorias), page, linesPerPage, orderBy, direction);
		Page<ProdutoDTO> produtosDTO = produtos.map(objeto -> new ProdutoDTO(objeto));
		return ResponseEntity.ok().body(produtosDTO);
	}
}
