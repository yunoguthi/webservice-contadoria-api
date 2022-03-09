package br.jus.jfsp.nuit.contadoria.service;

import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import br.jus.jfsp.nuit.contadoria.models.AtualizacaoJudicial;
import br.jus.jfsp.nuit.contadoria.models.IndicesAtrasados;
import br.jus.jfsp.nuit.contadoria.models.IndicesRes134;
import br.jus.jfsp.nuit.contadoria.models.TrMensal;
import br.jus.jfsp.nuit.contadoria.repository.IndicesRes134Repository;
import br.jus.jfsp.nuit.contadoria.util.ManipulaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
public class IndicesRes134Service {
	
	@Autowired
	private IndicesRes134Repository repository;
	
	@Autowired
	private JsonReader jsonReader;

	@Autowired
	private UrlReaderService urlReader;

	@Autowired
	private IndicesAtrasadosService indicesAtrasadosService;

	@Autowired
	private TrMensalService trMensalService;

	public void importa() {
		// Importação dos índices anteriores a julho de 2009, vindos da índices atrasados
		Iterable<IndicesAtrasados> listIndicesAtrasados = indicesAtrasadosService.getAll();
		for (IndicesAtrasados indicesAtrasados: listIndicesAtrasados) {
			BigDecimal indice = indicesAtrasados.getIndice();
			Calendar julho2009 = ManipulaData.getCalendar("2009-07-01", ManipulaData.ANO_MES_DIA);
			if (indicesAtrasados.getData().compareTo(julho2009) == -1) {
				try {
					repository.save(new IndicesRes134(indice, indicesAtrasados.getDescricao(), indicesAtrasados.getData()));
				} catch (Exception e) {}
			}
		}

		// Importação dos índices posteriores a julho de 2009, vindos da TR

		Iterable<TrMensal> listTrMensal = trMensalService.getAll();
		for (TrMensal trMensal: listTrMensal) {
			BigDecimal indice = new BigDecimal(trMensal.getValor());
			Calendar julho2009 = ManipulaData.getCalendar("2009-07-01", ManipulaData.ANO_MES_DIA);
			if (trMensal.getData().compareTo(julho2009) != -1) {
				try {
					repository.save(new IndicesRes134(indice, trMensal.getObservacao(), trMensal.getData()));
				} catch (Exception e) {}
			}
		}
	}

	public IndicesRes134 create(IndicesRes134 indicesRes134) {
		return repository.save(indicesRes134);
	}
	
	public IndicesRes134 save(IndicesRes134 indicesRes134) {
		return repository.save(indicesRes134);
	}
	
	public void delete(Long id) {
		repository.deleteById(id);
	}

	public IndicesRes134 update(IndicesRes134 indicesRes134) throws RecordNotFoundException {
		findByIdOrThrowException(indicesRes134.getId());
		return repository.save(indicesRes134);
	}

	public Iterable<IndicesRes134> getAll(){
		return repository.findAll();
	}

	public Page<IndicesRes134> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public IndicesRes134 read(Long id) throws RecordNotFoundException {
		return findByIdOrThrowException(id);
	}

	public Optional<IndicesRes134> findById(Long id) {
		return repository.findById(id);
	}

	public Page<IndicesRes134> findLike(Pageable pageable, String like) throws RecordNotFoundException {
		Page<IndicesRes134> retorno = repository.findLikePage(pageable, like);
		if (retorno.getTotalElements()==0) {
			throw new RecordNotFoundException("Valor não encontado");
		}
		return retorno;
	}

	private IndicesRes134 findByIdOrThrowException(Long id) throws RecordNotFoundException{
		return repository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Registro não encontrado com o id " + id));
	}

	public Optional<IndicesRes134> findByData(Calendar data) {
		return repository.findByData(data);
	}

	public Iterable<IndicesRes134> findByDataBetween(Calendar data1, Calendar data2) {
		return repository.findAllByDataLessThanEqualAndDataGreaterThanEqual(data2, data1);
	}



}
