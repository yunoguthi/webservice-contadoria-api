package br.jus.jfsp.nuit.contadoria.service;

import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import br.jus.jfsp.nuit.contadoria.models.Urv;
import br.jus.jfsp.nuit.contadoria.models.Urv;
import br.jus.jfsp.nuit.contadoria.repository.UrvRepository;
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
import java.util.List;
import java.util.Optional;

@Service
public class UrvService extends SgsBacenService {

	@Autowired
	private UrvRepository repository;
	

	@Autowired
	private JsonReader jsonReader;
	
	@Autowired
	private UrlReaderService urlReaderService;
	
public void importa() {
		
		Calendar dataInicial = repository.findMaxData();
		
		String conteudoUrl = "";
		try {
			conteudoUrl = urlReaderService.getConteudo(getUrl(URV, ManipulaData.toDate(dataInicial)));
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
				Urv urv = new Urv();
				urv.setData(ManipulaData.toCalendar(data));
				urv.setValor(valor);
				urv.setFonte(Consts.SGS_BACEN);
				if (!repository.existsByData(ManipulaData.toCalendar(data))) {
					repository.save(urv);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Urv create(Urv urv) {
		return repository.save(urv);
	}

	public Urv save(Urv urv) {
		return repository.save(urv);
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public Urv update(Urv urv) throws RecordNotFoundException {
		findByIdOrThrowException(urv.getId());
		return repository.save(urv);
	}

	public Iterable<Urv> getAll(){
		return repository.findAll();
	}

	public Page<Urv> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public Urv read(Long id) throws RecordNotFoundException {
		return findByIdOrThrowException(id);
	}

	public Optional<Urv> findById(Long id) {
		return repository.findById(id);
	}

	public Page<Urv> findLike(Pageable pageable, String like) throws RecordNotFoundException {
		Page<Urv> retorno = repository.findLikePage(pageable, like);
		if (retorno.getTotalElements()==0) {
			throw new RecordNotFoundException("Valor não encontado");
		}
		return retorno;
	}

	private Urv findByIdOrThrowException(Long id) throws RecordNotFoundException{
		return repository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Registro não encontrado com o id " + id));
	}
	public Optional<Urv> findByData(Calendar data) {
		return repository.findByData(data);
	}

	public Iterable<Urv> findByDataBetween(Calendar data1, Calendar data2) {
		return repository.findAllByDataLessThanEqualAndDataGreaterThanEqual(data2, data1);
	}
	
}
