package br.jus.jfsp.nuit.contadoria.service;

import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import br.jus.jfsp.nuit.contadoria.models.IndicesConsolidados;
import br.jus.jfsp.nuit.contadoria.repository.IndicesConsolidadosRepository;
import br.jus.jfsp.nuit.contadoria.util.ManipulaData;
import br.jus.jfsp.nuit.contadoria.util.consts.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Optional;

@Service
public class IndicesConsolidadosService extends SidraIbgeService {
	
	@Autowired
	private JsonReader jsonReader;
	
	@Autowired
	private UrlReaderService urlReaderService;
	
	@Autowired
	private IndicesConsolidadosRepository repository;
	
	public void importa() {

	}

	public IndicesConsolidados create(IndicesConsolidados indicesConsolidados) {
		return repository.save(indicesConsolidados);
	}

	public IndicesConsolidados save(IndicesConsolidados indicesConsolidados) {
		System.out.println(indicesConsolidados.toString());
		return repository.save(indicesConsolidados);
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public IndicesConsolidados update(IndicesConsolidados indicesConsolidados) throws RecordNotFoundException {
		findByIdOrThrowException(indicesConsolidados.getId());
		System.out.println(indicesConsolidados.getId());
		return repository.save(indicesConsolidados);
	}

	public Iterable<IndicesConsolidados> getAll(){
		return repository.findAll();
	}

	public Page<IndicesConsolidados> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public IndicesConsolidados read(Long id) throws RecordNotFoundException {
		return findByIdOrThrowException(id);
	}

	public Optional<IndicesConsolidados> findById(Long id) {
		return repository.findById(id);
	}

	public Optional<IndicesConsolidados> findByData(Calendar data) {
		return repository.findByData(data);
	}

	public Page<IndicesConsolidados> findLike(Pageable pageable, String like) throws RecordNotFoundException {
		Page<IndicesConsolidados> retorno = repository.findLikePage(pageable, like);
		if (retorno.getTotalElements()==0) {
			return Page.empty();
			//throw new RecordNotFoundException("Valor não encontado");
		}
		return retorno;
	}

	private IndicesConsolidados findByIdOrThrowException(Long id) throws RecordNotFoundException{
		return repository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Registro não encontrado com o id " + id));
	}

}