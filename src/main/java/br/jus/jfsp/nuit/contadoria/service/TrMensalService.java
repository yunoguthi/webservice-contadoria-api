package br.jus.jfsp.nuit.contadoria.service;

import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import br.jus.jfsp.nuit.contadoria.models.TrMensal;
import br.jus.jfsp.nuit.contadoria.models.TrMensal;
import br.jus.jfsp.nuit.contadoria.repository.TrMensalRepository;
import br.jus.jfsp.nuit.contadoria.util.ManipulaData;
import br.jus.jfsp.nuit.contadoria.util.consts.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

@Service
public class TrMensalService extends SgsBacenService {
	
	@Autowired
	private JsonReader jsonReader;
	
	@Autowired
	private UrlReaderService urlReaderService;
	
	@Autowired
	private TrMensalRepository repository;
	
	public void importa() {
		
		Calendar dataInicial = repository.findMaxData();
		if (dataInicial == null) {
			dataInicial = new GregorianCalendar(1900,0,31);
		}
		System.out.println("tr: " + dataInicial);


		String conteudoUrl = "";
		
		try {
			conteudoUrl = urlReaderService.getConteudo(getUrl(TR_MENSAL_PRIMEIRO_DIA, ManipulaData.toDate(dataInicial)));
		} catch (IOException e) {
			// TODO: handle exception
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
					// TODO: handle exception
					e.printStackTrace();
					continue;
				}
				Double valor = new Double(lMap.get("valor")+"");
				TrMensal trMensal = new TrMensal();
				trMensal.setData(ManipulaData.toCalendar(data));
				trMensal.setValor(valor);
				trMensal.setUltimaAtualizacao(ManipulaData.getHoje());
				trMensal.setFonte(Consts.SGS_BACEN);
				if(!repository.existsByData(ManipulaData.toCalendar(data))) {
					repository.save(trMensal);
				}
			}
			
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
		
	}

	public TrMensal create(TrMensal trMensal) {
		return repository.save(trMensal);
	}

	public TrMensal save(TrMensal trMensal) {
		return repository.save(trMensal);
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public TrMensal update(TrMensal trMensal) throws RecordNotFoundException {
		findByIdOrThrowException(trMensal.getId());
		return repository.save(trMensal);
	}

	public Iterable<TrMensal> getAll(){
		return repository.findAll();
	}

	public Page<TrMensal> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public TrMensal read(Long id) throws RecordNotFoundException {
		return findByIdOrThrowException(id);
	}

	public Optional<TrMensal> findById(Long id) {
		return repository.findById(id);
	}

	public Page<TrMensal> findLike(Pageable pageable, String like) throws RecordNotFoundException {
		Page<TrMensal> retorno = repository.findLikePage(pageable, like);
		if (retorno.getTotalElements()==0) {
			throw new RecordNotFoundException("Valor não encontado");
		}
		return retorno;
	}

	private TrMensal findByIdOrThrowException(Long id) throws RecordNotFoundException{
		return repository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Registro não encontrado com o id " + id));
	}
	public Optional<TrMensal> findByData(Calendar data) {
		return repository.findByData(data);
	}

	public Iterable<TrMensal> findByDataBetween(Calendar data1, Calendar data2) {
		return repository.findAllByDataLessThanEqualAndDataGreaterThanEqual(data2, data1);
	}

}
