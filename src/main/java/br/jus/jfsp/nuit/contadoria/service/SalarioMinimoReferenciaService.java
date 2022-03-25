package br.jus.jfsp.nuit.contadoria.service;

import br.jus.jfsp.nuit.contadoria.exception.DataInvalidaException;
import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import br.jus.jfsp.nuit.contadoria.models.SalarioMinimo;
import br.jus.jfsp.nuit.contadoria.models.SalarioMinimoReferencia;
import br.jus.jfsp.nuit.contadoria.repository.SalarioMinimoReferenciaRepository;
import br.jus.jfsp.nuit.contadoria.repository.SalarioMinimoRepository;
import br.jus.jfsp.nuit.contadoria.util.ManipulaData;
import br.jus.jfsp.nuit.contadoria.util.ManipulaMoeda;
import br.jus.jfsp.nuit.contadoria.util.consts.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Optional;

@Service
public class SalarioMinimoReferenciaService {
	
	
	@Autowired
	private SalarioMinimoReferenciaRepository repository;
	
	@Autowired
	private JsonReader jsonReader;

	@Autowired
	private UrlReaderService urlReader;

	public SalarioMinimoReferencia create(SalarioMinimoReferencia salarioMinimo) {
		return repository.save(salarioMinimo);
	}

	public SalarioMinimoReferencia save(SalarioMinimoReferencia salarioMinimo) {
		return repository.save(salarioMinimo);
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public SalarioMinimoReferencia update(SalarioMinimoReferencia salarioMinimo) throws RecordNotFoundException {
		findByIdOrThrowException(salarioMinimo.getId());
		return repository.save(salarioMinimo);
	}

	public Iterable<SalarioMinimoReferencia> getAll(){
		return repository.findAll();
	}

	public Iterable<SalarioMinimoReferencia> getAll(Sort sort){
		return repository.findAll(sort);
	}

	public Page<SalarioMinimoReferencia> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public SalarioMinimoReferencia read(Long id) throws RecordNotFoundException {
		return findByIdOrThrowException(id);
	}

	public Optional<SalarioMinimoReferencia> findById(Long id) {
		return repository.findById(id);
	}

	public Page<SalarioMinimoReferencia> findLike(Pageable pageable, String like) throws RecordNotFoundException {
		Page<SalarioMinimoReferencia> retorno = repository.findLikePage(pageable, like);
		if (retorno.getTotalElements()==0) {
			throw new RecordNotFoundException("Valor não encontado");
		}
		return retorno;
	}

	private SalarioMinimoReferencia findByIdOrThrowException(Long id) throws RecordNotFoundException{
		return repository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Registro não encontrado com o id " + id));
	}
	public Optional<SalarioMinimoReferencia> findByData(Calendar data) {
		return repository.findByData(data);
	}

	public Iterable<SalarioMinimoReferencia> findByDataBetween(Calendar data1, Calendar data2) {
		return repository.findAllByDataLessThanEqualAndDataGreaterThanEqual(data2, data1);
	}

}
