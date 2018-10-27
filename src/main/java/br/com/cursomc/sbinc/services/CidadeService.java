package br.com.cursomc.sbinc.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cursomc.sbinc.domain.dto.CidadeDTO;
import br.com.cursomc.sbinc.repositories.CidadeRepository;

@Service
public class CidadeService {

	@Autowired
	private CidadeRepository repository;
	
	public List<CidadeDTO> findCidades(Integer estadoId) {
		return repository.findCidades(estadoId)
				.stream().map(c -> new CidadeDTO(c))
				.collect(Collectors.toList());
	}
}
