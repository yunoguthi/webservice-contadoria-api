package br.jus.jfsp.nuit.contadoria.service;

import br.jus.jfsp.nuit.contadoria.exception.DataInvalidaException;
import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import br.jus.jfsp.nuit.contadoria.models.TetoContribuicao;
import br.jus.jfsp.nuit.contadoria.models.TetoContribuicao;
import br.jus.jfsp.nuit.contadoria.repository.TetoContribuicaoRepository;
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
public class TetoContribuicaoService {
	
	@Autowired
	private TetoContribuicaoRepository repository;
	
	@Autowired
	private JsonReader jsonReader;

	@Autowired
	private UrlReaderService urlReader;

	public TetoContribuicao create(TetoContribuicao tetoContribuicao) {
		return repository.save(tetoContribuicao);
	}

	public TetoContribuicao save(TetoContribuicao tetoContribuicao) {
		return repository.save(tetoContribuicao);
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public TetoContribuicao update(TetoContribuicao tetoContribuicao) throws RecordNotFoundException {
		findByIdOrThrowException(tetoContribuicao.getId());
		return repository.save(tetoContribuicao);
	}

	public Iterable<TetoContribuicao> getAll(){
		return repository.findAll();
	}

	public Iterable<TetoContribuicao> getAll(Sort sort){
		return repository.findAll(sort);
	}

	public Page<TetoContribuicao> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public TetoContribuicao read(Long id) throws RecordNotFoundException {
		return findByIdOrThrowException(id);
	}

	public Optional<TetoContribuicao> findById(Long id) {
		return repository.findById(id);
	}

	public Page<TetoContribuicao> findLike(Pageable pageable, String like) throws RecordNotFoundException {
		Page<TetoContribuicao> retorno = repository.findLikePage(pageable, like);
		if (retorno.getTotalElements()==0) {
			throw new RecordNotFoundException("Valor não encontado");
		}
		return retorno;
	}

	private TetoContribuicao findByIdOrThrowException(Long id) throws RecordNotFoundException{
		return repository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Registro não encontrado com o id " + id));
	}
	public Optional<TetoContribuicao> findByData(Calendar data) {
		return repository.findByData(data);
	}

	public Iterable<TetoContribuicao> findByDataBetween(Calendar data1, Calendar data2) {
		return repository.findAllByDataLessThanEqualAndDataGreaterThanEqual(data2, data1);
	}

	public void updateMoedas() {
		Iterable<TetoContribuicao> listTetoContribuicao = repository.findByMoedaIsNull();
		for (TetoContribuicao tetoContribuicao: listTetoContribuicao) {
			System.out.println(tetoContribuicao.getData());
			try {
				tetoContribuicao.setMoeda(ManipulaMoeda.getMoedaCorrente(tetoContribuicao.getData()));
				update(tetoContribuicao);
			} catch (DataInvalidaException e) {
				e.printStackTrace();
			} catch (RecordNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

}
