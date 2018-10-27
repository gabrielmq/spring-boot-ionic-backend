package br.com.cursomc.sbinc.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cursomc.sbinc.domain.dto.EstadoDTO;
import br.com.cursomc.sbinc.repositories.EstadoRepository;

@Service
public class EstadoService {

	@Autowired
	private EstadoRepository repository;
	
	public List<EstadoDTO> findAllByOrderByNome() {
		return repository.findAllByOrderByNome()
				.stream().map(e -> new EstadoDTO(e))
				.collect(Collectors.toList());
	}
}
