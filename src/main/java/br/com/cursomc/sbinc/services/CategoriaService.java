package br.com.cursomc.sbinc.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
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
		Categoria c = findById(categoria.getId());
		updateData(c, categoria);
		return repository.save(c);
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
	
	public Page<CategoriaDTO> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repository.findAll(pageRequest).map( obj -> new CategoriaDTO(obj) );
	}
	
	public Categoria fromDTO(CategoriaDTO obj) {
		return new Categoria(obj.getId(), obj.getNome());
	}
	
	private void updateData(Categoria newObj, Categoria obj) {
		newObj.setNome(obj.getNome());
	}
}
