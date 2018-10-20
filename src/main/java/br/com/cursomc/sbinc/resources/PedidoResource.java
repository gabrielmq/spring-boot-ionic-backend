package br.com.cursomc.sbinc.resources;

import java.net.URI;

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

import br.com.cursomc.sbinc.domain.Pedido;
import br.com.cursomc.sbinc.services.PedidoService;

@RestController
@RequestMapping(value = "/pedidos")
public class PedidoResource {

	@Autowired
	private PedidoService service;
	
	@RequestMapping(value = "/{pedidoId}", method = RequestMethod.GET)
	public ResponseEntity<Pedido> findById(@PathVariable Integer pedidoId) {
		return ResponseEntity.ok().body(service.finById(pedidoId));
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody Pedido pedido) {
		pedido = service.insert(pedido);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
					.path("/{pedidoId}").buildAndExpand(pedido.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Page<Pedido>> findPage(@RequestParam(name="page", defaultValue="0") Integer page,
												 @RequestParam(name="linesPerPage", defaultValue="24") Integer linesPerPage, 
												 @RequestParam(name="orderBy", defaultValue="instante") String orderBy, 
												 @RequestParam(name="direction", defaultValue="DESC") String direction) {
		return ResponseEntity.ok().body(service.findPage(page, linesPerPage, orderBy, direction));
	}
}
