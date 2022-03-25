package br.jus.jfsp.nuit.contadoria.service;

import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import br.jus.jfsp.nuit.contadoria.models.ReajusteBeneficio;
import br.jus.jfsp.nuit.contadoria.repository.ReajusteBeneficioRepository;
import br.jus.jfsp.nuit.contadoria.util.ManipulaData;
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
public class ReajusteBeneficioService extends SgsBacenService {
	
	@Autowired
	private ReajusteBeneficioRepository repository;
	
	@Autowired
	private JsonReader jsonReader;

	@Autowired
	private UrlReaderService urlReader;

	public ReajusteBeneficio create(ReajusteBeneficio reajusteBeneficio) {
		return repository.save(reajusteBeneficio);
	}
	
	public ReajusteBeneficio save(ReajusteBeneficio reajusteBeneficio) {
		return repository.save(reajusteBeneficio);
	}
	
	public void delete(Long id) {
		repository.deleteById(id);
	}

	public ReajusteBeneficio update(ReajusteBeneficio reajusteBeneficio) throws RecordNotFoundException {
		findByIdOrThrowException(reajusteBeneficio.getId());
		return repository.save(reajusteBeneficio);
	}

	public Iterable<ReajusteBeneficio> getAll(){
		return repository.findAll();
	}

	public Iterable<ReajusteBeneficio> getAll(Sort sort){
		return repository.findAll(sort);
	}

	public Page<ReajusteBeneficio> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public ReajusteBeneficio read(Long id) throws RecordNotFoundException {
		return findByIdOrThrowException(id);
	}

	public Optional<ReajusteBeneficio> findById(Long id) {
		return repository.findById(id);
	}

	public Page<ReajusteBeneficio> findLike(Pageable pageable, String like) throws RecordNotFoundException {
		Page<ReajusteBeneficio> retorno = repository.findLikePage(pageable, like);
		if (retorno.getTotalElements()==0) {
			throw new RecordNotFoundException("Valor não encontado");
		}
		return retorno;
	}

	private ReajusteBeneficio findByIdOrThrowException(Long id) throws RecordNotFoundException{
		return repository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Registro não encontrado com o id " + id));
	}

	public Optional<ReajusteBeneficio> findByData(Calendar data) {
		return repository.findByData(data);
	}

	public Iterable<ReajusteBeneficio> findByDataBetween(Calendar data1, Calendar data2) {
		return repository.findAllByDataLessThanEqualAndDataGreaterThanEqual(data2, data1);
	}



}
