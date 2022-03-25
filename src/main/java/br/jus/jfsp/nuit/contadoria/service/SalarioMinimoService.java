package br.jus.jfsp.nuit.contadoria.service;

import br.jus.jfsp.nuit.contadoria.exception.DataInvalidaException;
import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import br.jus.jfsp.nuit.contadoria.models.EMoeda;
import br.jus.jfsp.nuit.contadoria.models.SalarioMinimo;
import br.jus.jfsp.nuit.contadoria.models.SalarioMinimo;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

@Service
public class SalarioMinimoService extends SgsBacenService {
	
	
	@Autowired
	private SalarioMinimoRepository repository;
	
	@Autowired
	private JsonReader jsonReader;

	@Autowired
	private UrlReaderService urlReader;
	
	public void importa() {
			
		Calendar dataInicial = repository.findMaxData();
		
		String conteudoUrl = "";
		try {
			conteudoUrl = urlReader.getConteudo(getUrl(SALARIO_MINIMO, ManipulaData.toDate(dataInicial)));
			
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		try {
			Object[] map = jsonReader.getJsonArray(conteudoUrl);
			for (int i = 0; i < map.length; i++) {
				LinkedHashMap lMap = (LinkedHashMap) map[i];
				Date data;

				try {
					data = ManipulaData.stringToDateDiaMesAno(lMap.get("data")+"");
				} catch (ParseException e) {
					e.printStackTrace();
					continue;
				}
				Double valor = new Double(lMap.get("valor")+"");
				SalarioMinimo sm = new SalarioMinimo();
				sm.setData(ManipulaData.toCalendar(data));
				sm.setValor(valor);
				sm.setFonte(Consts.SGS_BACEN);
				try {
					sm.setMoeda(ManipulaMoeda.getMoedaCorrente(ManipulaData.toCalendar(data)));
				} catch (DataInvalidaException e1) {
					e1.printStackTrace();
				}
				if (!repository.existsByData(ManipulaData.toCalendar(data))) {
					repository.save(sm);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}

	public SalarioMinimo create(SalarioMinimo salarioMinimo) {
		return repository.save(salarioMinimo);
	}

	public SalarioMinimo save(SalarioMinimo salarioMinimo) {
		return repository.save(salarioMinimo);
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public SalarioMinimo update(SalarioMinimo salarioMinimo) throws RecordNotFoundException {
		findByIdOrThrowException(salarioMinimo.getId());
		return repository.save(salarioMinimo);
	}

	public Iterable<SalarioMinimo> getAll(){
		return repository.findAll();
	}

	public Iterable<SalarioMinimo> getAll(Sort sort){
		return repository.findAll(sort);
	}

	public Page<SalarioMinimo> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public SalarioMinimo read(Long id) throws RecordNotFoundException {
		return findByIdOrThrowException(id);
	}

	public Optional<SalarioMinimo> findById(Long id) {
		return repository.findById(id);
	}

	public Page<SalarioMinimo> findLike(Pageable pageable, String like) throws RecordNotFoundException {
		Page<SalarioMinimo> retorno = repository.findLikePage(pageable, like);
		if (retorno.getTotalElements()==0) {
			throw new RecordNotFoundException("Valor não encontado");
		}
		return retorno;
	}

	private SalarioMinimo findByIdOrThrowException(Long id) throws RecordNotFoundException{
		return repository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Registro não encontrado com o id " + id));
	}
	public Optional<SalarioMinimo> findByData(Calendar data) {
		return repository.findByData(data);
	}

	public Iterable<SalarioMinimo> findByDataBetween(Calendar data1, Calendar data2) {
		return repository.findAllByDataLessThanEqualAndDataGreaterThanEqual(data2, data1);
	}

	public void updateMoedas() {
		Iterable<SalarioMinimo> listSalarioMinimo = repository.findByMoedaIsNull();
		for (SalarioMinimo salarioMinimo: listSalarioMinimo) {
			System.out.println(salarioMinimo.getData());
			try {
				salarioMinimo.setMoeda(ManipulaMoeda.getMoedaCorrente(salarioMinimo.getData()));
				update(salarioMinimo);
			} catch (DataInvalidaException e) {
				e.printStackTrace();
			} catch (RecordNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

}
