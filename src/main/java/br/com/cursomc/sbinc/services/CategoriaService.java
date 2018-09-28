package br.com.cursomc.sbinc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cursomc.sbinc.domain.Categoria;
import br.com.cursomc.sbinc.repositories.CategoriaRepository;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository repository;
	
	public Categoria findById(Integer categoriaId) {
		return repository.findById(categoriaId).orElse(null);
	}
}
