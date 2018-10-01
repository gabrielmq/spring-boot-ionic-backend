package br.com.cursomc.sbinc.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.cursomc.sbinc.domain.Cliente;
import br.com.cursomc.sbinc.services.ClienteService;

@RestController
@RequestMapping(value = "clientes")
public class ClienteResource {

	@Autowired
	private ClienteService service;
	
	@RequestMapping(value = "/{clienteId}", method = RequestMethod.GET)
	public ResponseEntity<Cliente> findById(@PathVariable Integer clienteId) {
		return ResponseEntity.ok().body(service.findById(clienteId));
	}
}
