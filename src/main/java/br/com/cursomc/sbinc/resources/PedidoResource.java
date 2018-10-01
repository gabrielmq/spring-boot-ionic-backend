package br.com.cursomc.sbinc.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
}
