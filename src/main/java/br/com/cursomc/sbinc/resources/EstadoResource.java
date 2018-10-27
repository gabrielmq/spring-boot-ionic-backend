package br.com.cursomc.sbinc.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.cursomc.sbinc.domain.dto.CidadeDTO;
import br.com.cursomc.sbinc.domain.dto.EstadoDTO;
import br.com.cursomc.sbinc.services.CidadeService;
import br.com.cursomc.sbinc.services.EstadoService;

@RestController
@RequestMapping(value = "/estados")
public class EstadoResource {

	@Autowired
	private EstadoService service;
	
	@Autowired
	private CidadeService cidadeService;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<EstadoDTO>> findAllByOrderByNome() {
		return ResponseEntity.ok().body(service.findAllByOrderByNome());
	}
	
	@RequestMapping(value = "/{estadoId}/cidades", method = RequestMethod.GET)
	public ResponseEntity<List<CidadeDTO>> findCidades(@PathVariable Integer estadoId) {
		return ResponseEntity.ok().body(cidadeService.findCidades(estadoId));
	}
}
