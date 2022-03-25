package br.jus.jfsp.nuit.contadoria.service;

import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import br.jus.jfsp.nuit.contadoria.models.MultiplicadorMoeda;
import br.jus.jfsp.nuit.contadoria.repository.MultiplicadorMoedaRepository;
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
public class MultiplicadorMoedaService extends SgsBacenService {
	
	@Autowired
	private MultiplicadorMoedaRepository repository;
	
	@Autowired
	private JsonReader jsonReader;

	@Autowired
	private UrlReaderService urlReader;
	
	public void importa() {
			
		Calendar dataInicial = repository.findMaxData();
		
		String conteudoUrl = "";
		try {
			conteudoUrl = urlReader.getConteudo(getUrl(BTN_MENSAL, ManipulaData.toDate(dataInicial)));
			
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
				MultiplicadorMoeda bm = new MultiplicadorMoeda();
				bm.setData(ManipulaData.toCalendar(data));
				bm.setValor(valor);
				bm.setFonte(Consts.SGS_BACEN);
				if (!repository.existsByData(ManipulaData.toCalendar(data))) {
					repository.save(bm);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
			
	}

	public MultiplicadorMoeda create(MultiplicadorMoeda multiplicadorMoeda) {
		return repository.save(multiplicadorMoeda);
	}
	
	public MultiplicadorMoeda save(MultiplicadorMoeda multiplicadorMoeda) {
		return repository.save(multiplicadorMoeda);
	}
	
	public void delete(Long id) {
		repository.deleteById(id);
	}

	public MultiplicadorMoeda update(MultiplicadorMoeda multiplicadorMoeda) throws RecordNotFoundException {
		findByIdOrThrowException(multiplicadorMoeda.getId());
		return repository.save(multiplicadorMoeda);
	}

	public Iterable<MultiplicadorMoeda> getAll(){
		return repository.findAll();
	}

	public Iterable<MultiplicadorMoeda> getAll(Sort sort){
		return repository.findAll(sort);
	}

	public Page<MultiplicadorMoeda> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public MultiplicadorMoeda read(Long id) throws RecordNotFoundException {
		return findByIdOrThrowException(id);
	}

	public Optional<MultiplicadorMoeda> findById(Long id) {
		return repository.findById(id);
	}

	public Page<MultiplicadorMoeda> findLike(Pageable pageable, String like) throws RecordNotFoundException {
		Page<MultiplicadorMoeda> retorno = repository.findLikePage(pageable, like);
		if (retorno.getTotalElements()==0) {
			throw new RecordNotFoundException("Valor não encontado");
		}
		return retorno;
	}

	private MultiplicadorMoeda findByIdOrThrowException(Long id) throws RecordNotFoundException{
		return repository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Registro não encontrado com o id " + id));
	}

	public Optional<MultiplicadorMoeda> findByData(Calendar data) {
		return repository.findByData(data);
	}

	public Iterable<MultiplicadorMoeda> findByDataBetween(Calendar data1, Calendar data2) {
		return repository.findAllByDataLessThanEqualAndDataGreaterThanEqual(data2, data1);
	}



}
