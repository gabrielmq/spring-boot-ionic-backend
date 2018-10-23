package br.com.cursomc.sbinc.resources;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.cursomc.sbinc.domain.Cliente;
import br.com.cursomc.sbinc.domain.dto.ClienteDTO;
import br.com.cursomc.sbinc.domain.dto.ClienteNewDTO;
import br.com.cursomc.sbinc.services.ClienteService;

@RestController
@RequestMapping(value = "/clientes")
public class ClienteResource {

	@Autowired
	private ClienteService service;
	
	@RequestMapping(value = "/{clienteId}", method = RequestMethod.GET)
	public ResponseEntity<Cliente> findById(@PathVariable Integer clienteId) {
		return ResponseEntity.ok().body(service.findById(clienteId));
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody ClienteNewDTO clienteDto) {
		Cliente cliente = service.fromDTO(clienteDto);
		cliente = service.insert(cliente);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
					.path("/{clienteId}").buildAndExpand(cliente.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@RequestMapping(value = "/{clienteId}", method = RequestMethod.PUT)
	public ResponseEntity<Void> update(@PathVariable Integer clienteId, @Valid @RequestBody ClienteDTO clienteDto) {
		Cliente cliente = service.fromDTO(clienteDto);
		cliente.setId(clienteId);
		cliente = service.update(cliente);
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value = "/{clienteId}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Integer clienteId) {
		service.delete(clienteId);
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<ClienteDTO>> findAll() {
		return ResponseEntity.ok().body(service.findAll());
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value="/page", method=RequestMethod.GET)
	public ResponseEntity<Page<ClienteDTO>> findPage(@RequestParam(value="page", defaultValue="0") Integer page, 
													 @RequestParam(value="linesPerPage", defaultValue="24") Integer linesPerPage, 
													 @RequestParam(value="orderBy", defaultValue="nome") String orderBy, 
													 @RequestParam(value="direction", defaultValue="ASC") String direction) {
		return ResponseEntity.ok().body(service.findPage(page, linesPerPage, orderBy, direction));
	}
	
	@RequestMapping(value = "/picture", method = RequestMethod.POST)
	public ResponseEntity<Void> uploadProfilePicture(@RequestParam(name="file") MultipartFile file) {
		URI uri = service.uploadProfilePicture(file);
		return ResponseEntity.created(uri).build();
	}
}
