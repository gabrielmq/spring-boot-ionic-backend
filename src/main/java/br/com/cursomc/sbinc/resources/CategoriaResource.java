package br.com.cursomc.sbinc.resources;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.cursomc.sbinc.domain.Categoria;
import br.com.cursomc.sbinc.domain.dto.CategoriaDTO;
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
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody CategoriaDTO categoriaDto) {
		Categoria categoria = service.fromDTO(categoriaDto);
		categoria = service.insert(categoria);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
					.path("/{categoriaId}").buildAndExpand(categoria.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@RequestMapping(value = "/{categoriaId}" , method = RequestMethod.PUT)
	public ResponseEntity<Void> update(@PathVariable Integer categoriaId, @Valid @RequestBody CategoriaDTO categoriaDto) {
		Categoria categoria = service.fromDTO(categoriaDto);
		categoria.setId(categoriaId);
		categoria = service.update(categoria);
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(value = "/{categoriaId}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Integer categoriaId) {
		service.delete(categoriaId);
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<CategoriaDTO>> findAll() {
		return ResponseEntity.ok().body(service.findAll());
	}
	
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public ResponseEntity<Page<CategoriaDTO>> findPage(@RequestParam(name="page", defaultValue="0") Integer page,
													   @RequestParam(name="linesPerPage", defaultValue="24") Integer linesPerPage, 
													   @RequestParam(name="orderBy", defaultValue="nome") String orderBy, 
													   @RequestParam(name="direction", defaultValue="ASC") String direction) {
		return ResponseEntity.ok().body(service.findPage(page, linesPerPage, orderBy, direction));
	}
}
