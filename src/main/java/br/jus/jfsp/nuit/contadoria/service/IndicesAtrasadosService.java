package br.jus.jfsp.nuit.contadoria.service;

import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import br.jus.jfsp.nuit.contadoria.models.AtualizacaoJudicial;
import br.jus.jfsp.nuit.contadoria.models.IndicesAtrasados;
import br.jus.jfsp.nuit.contadoria.repository.IndicesAtrasadosRepository;
import br.jus.jfsp.nuit.contadoria.util.ManipulaData;
import br.jus.jfsp.nuit.contadoria.util.consts.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Optional;

@Service
public class IndicesAtrasadosService {
	
	@Autowired
	private IndicesAtrasadosRepository repository;
	
	@Autowired
	private JsonReader jsonReader;

	@Autowired
	private UrlReaderService urlReader;

	@Autowired
	private AtualizacaoJudicialService atualizacaoJudicialService;

	public void calculaAcumulados() throws RecordNotFoundException {
		ArrayList<IndicesAtrasados> listIndicesAtrasados = new ArrayList<IndicesAtrasados>();
		Double acumulado = new Double(1.0);
		for (int i = listIndicesAtrasados.size()-1; i>=0; i--) {
			IndicesAtrasados indicesAtrasados = listIndicesAtrasados.get(i);
			if (!indicesAtrasados.getIndice().equals(new Double(1.0))) {
				acumulado = acumulado * indicesAtrasados.getIndice();
			}
			indicesAtrasados.setIndiceAtrasado(acumulado);
			update(indicesAtrasados);
		}
	}

	public void importa() {
		ArrayList<IndicesAtrasados> listIndicesAtrasados = new ArrayList<IndicesAtrasados>();
		Iterable<AtualizacaoJudicial> listAtualizacaoJudicial = atualizacaoJudicialService.getAll();
		for (AtualizacaoJudicial atualizacaoJudicial: listAtualizacaoJudicial) {
			Double percentual = atualizacaoJudicial.getPercentual();
			Double valor = atualizacaoJudicial.getValor();
			Double indice = new Double(0.0);
			if (percentual !=null && !percentual.equals(0.0)) {
				indice = (percentual / 100) + 1;
			} else {
				indice = valor;
			}
			repository.save(new IndicesAtrasados(indice, atualizacaoJudicial.getDescricao(), atualizacaoJudicial.getData()));
		}
	}

	public IndicesAtrasados create(IndicesAtrasados indicesAtrasados) {
		return repository.save(indicesAtrasados);
	}
	
	public IndicesAtrasados save(IndicesAtrasados indicesAtrasados) {
		return repository.save(indicesAtrasados);
	}
	
	public void delete(Long id) {
		repository.deleteById(id);
	}

	public IndicesAtrasados update(IndicesAtrasados indicesAtrasados) throws RecordNotFoundException {
		findByIdOrThrowException(indicesAtrasados.getId());
		return repository.save(indicesAtrasados);
	}

	public Iterable<IndicesAtrasados> getAll(){
		return repository.findAll();
	}

	public Page<IndicesAtrasados> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public IndicesAtrasados read(Long id) throws RecordNotFoundException {
		return findByIdOrThrowException(id);
	}

	public Optional<IndicesAtrasados> findById(Long id) {
		return repository.findById(id);
	}

	public Page<IndicesAtrasados> findLike(Pageable pageable, String like) throws RecordNotFoundException {
		Page<IndicesAtrasados> retorno = repository.findLikePage(pageable, like);
		if (retorno.getTotalElements()==0) {
			throw new RecordNotFoundException("Valor não encontado");
		}
		return retorno;
	}

	private IndicesAtrasados findByIdOrThrowException(Long id) throws RecordNotFoundException{
		return repository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Registro não encontrado com o id " + id));
	}

	public Optional<IndicesAtrasados> findByData(Calendar data) {
		return repository.findByData(data);
	}

	public Iterable<IndicesAtrasados> findByDataBetween(Calendar data1, Calendar data2) {
		return repository.findAllByDataLessThanEqualAndDataGreaterThanEqual(data2, data1);
	}



}
