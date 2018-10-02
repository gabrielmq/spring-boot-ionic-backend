package br.com.cursomc.sbinc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cursomc.sbinc.domain.Categoria;
import br.com.cursomc.sbinc.repositories.CategoriaRepository;
import br.com.cursomc.sbinc.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository repository;
	
	public Categoria findById(Integer categoriaId) {
		return repository.findById(categoriaId)
				.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " +categoriaId
						+ ", Tipo: "+Categoria.class.getName()));
	}
	
	public Categoria insert(Categoria categoria) {
		categoria.setId(null);
		return repository.save(categoria);
	}
}
