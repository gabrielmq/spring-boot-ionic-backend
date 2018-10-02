package br.com.cursomc.sbinc.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.com.cursomc.sbinc.domain.Categoria;
import br.com.cursomc.sbinc.domain.dto.CategoriaDTO;
import br.com.cursomc.sbinc.repositories.CategoriaRepository;
import br.com.cursomc.sbinc.services.exceptions.DataIntegrityException;
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
	
	public Categoria update(Categoria categoria) {
		findById(categoria.getId());
		return repository.save(categoria);
	}
	
	public void delete(Integer categoriaId) {
		findById(categoriaId);
		try {
			repository.deleteById(categoriaId);
		}
		catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir uma categoria que possui produtos.");
		}
	}
	
	public List<CategoriaDTO> findAll() {
		return repository.findAll()
				.stream().map( obj -> new CategoriaDTO(obj) )
				.collect(Collectors.toList());
	}
}
