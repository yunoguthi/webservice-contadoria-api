package br.jus.jfsp.nuit.contadoria.service;

import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import br.jus.jfsp.nuit.contadoria.models.AjusteMoeda;
import br.jus.jfsp.nuit.contadoria.repository.AjusteMoedaRepository;
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
import java.util.LinkedHashMap;
import java.util.Optional;

@Service
public class AjusteMoedaService extends SgsBacenService {
	
	@Autowired
	private AjusteMoedaRepository repository;
	
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
				AjusteMoeda bm = new AjusteMoeda();
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

	public AjusteMoeda create(AjusteMoeda ajusteMoeda) {
		return repository.save(ajusteMoeda);
	}
	
	public AjusteMoeda save(AjusteMoeda ajusteMoeda) {
		return repository.save(ajusteMoeda);
	}
	
	public void delete(Long id) {
		repository.deleteById(id);
	}

	public AjusteMoeda update(AjusteMoeda ajusteMoeda) throws RecordNotFoundException {
		findByIdOrThrowException(ajusteMoeda.getId());
		return repository.save(ajusteMoeda);
	}

	public Iterable<AjusteMoeda> getAll(){
		return repository.findAll();
	}

	public Page<AjusteMoeda> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public AjusteMoeda read(Long id) throws RecordNotFoundException {
		return findByIdOrThrowException(id);
	}

	public Optional<AjusteMoeda> findById(Long id) {
		return repository.findById(id);
	}

	public Page<AjusteMoeda> findLike(Pageable pageable, String like) throws RecordNotFoundException {
		Page<AjusteMoeda> retorno = repository.findLikePage(pageable, like);
		if (retorno.getTotalElements()==0) {
			throw new RecordNotFoundException("Valor não encontado");
		}
		return retorno;
	}

	private AjusteMoeda findByIdOrThrowException(Long id) throws RecordNotFoundException{
		return repository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Registro não encontrado com o id " + id));
	}

	public Optional<AjusteMoeda> findByData(Calendar data) {
		return repository.findByData(data);
	}

	public Iterable<AjusteMoeda> findByDataBetween(Calendar data1, Calendar data2) {
		return repository.findAllByDataLessThanEqualAndDataGreaterThanEqual(data2, data1);
	}



}
