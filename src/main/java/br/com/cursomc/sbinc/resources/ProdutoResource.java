package br.com.cursomc.sbinc.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.cursomc.sbinc.domain.Produto;
import br.com.cursomc.sbinc.domain.dto.ProdutoDTO;
import static br.com.cursomc.sbinc.resources.utils.URLUtils.*;
import br.com.cursomc.sbinc.services.ProdutoService;

@RestController
@RequestMapping(value = "/produtos")
public class ProdutoResource {

	@Autowired
	private ProdutoService service;
	
	@RequestMapping(value = "/{produtoId}", method = RequestMethod.GET)
	public ResponseEntity<Produto> findById(@PathVariable Integer produtoId) {
		return ResponseEntity.ok().body(service.finById(produtoId));
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Page<ProdutoDTO>> search(@RequestParam(name="nome", defaultValue="") String nome,
												   @RequestParam(name="categorias", defaultValue="") String categorias,
												   @RequestParam(name="page", defaultValue="0") Integer page,
												   @RequestParam(name="linesPerPage", defaultValue="24") Integer linesPerPage, 
												   @RequestParam(name="orderBy", defaultValue="nome") String orderBy, 
												   @RequestParam(name="direction", defaultValue="ASC") String direction) {
		return ResponseEntity.ok().body(service.search(decodeParam(nome), decodeIntList(categorias), page, linesPerPage, orderBy, direction));
	}
}
