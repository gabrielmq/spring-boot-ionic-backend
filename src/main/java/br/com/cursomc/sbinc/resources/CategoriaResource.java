package br.com.cursomc.sbinc.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.cursomc.sbinc.domain.Categoria;
import br.com.cursomc.sbinc.services.CategoriaService;

@RestController
@RequestMapping(value = "/categorias")
public class CategoriaResource {

	@Autowired
	private CategoriaService service;
	
	@RequestMapping(value = "/{categoriaId}", method = RequestMethod.GET)
	public ResponseEntity<Categoria> findById(@PathVariable Integer categoriaId) {
		return ResponseEntity.ok().body(service.findById(categoriaId));
	}
}
