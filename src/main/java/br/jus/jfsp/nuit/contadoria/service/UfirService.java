package br.jus.jfsp.nuit.contadoria.service;

import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import br.jus.jfsp.nuit.contadoria.models.Ufir;
import br.jus.jfsp.nuit.contadoria.models.Ufir;
import br.jus.jfsp.nuit.contadoria.repository.UfirRepository;
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
public class UfirService extends SgsBacenService {
	@Autowired
	private UfirRepository repository;
	
	@Autowired
	private JsonReader jsonReader;

	@Autowired
	private UrlReaderService urlReader;
	
	public void importa() {
			
		Calendar dataInicial = repository.findMaxData();
		
		String conteudoUrl = "";
		try {
			conteudoUrl = urlReader.getConteudo(getUrl(UFIR, ManipulaData.toDate(dataInicial)));
			
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
				Ufir ufir = new Ufir();
				ufir.setData(ManipulaData.toCalendar(data));
				ufir.setValor(valor);
				ufir.setFonte(Consts.SGS_BACEN);
				if (!repository.existsByData(ManipulaData.toCalendar(data))) {
					repository.save(ufir);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}

	public Ufir create(Ufir ufir) {
		return repository.save(ufir);
	}

	public Ufir save(Ufir ufir) {
		return repository.save(ufir);
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public Ufir update(Ufir ufir) throws RecordNotFoundException {
		findByIdOrThrowException(ufir.getId());
		return repository.save(ufir);
	}

	public Iterable<Ufir> getAll(){
		return repository.findAll();
	}

	public Page<Ufir> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public Ufir read(Long id) throws RecordNotFoundException {
		return findByIdOrThrowException(id);
	}

	public Optional<Ufir> findById(Long id) {
		return repository.findById(id);
	}

	public Page<Ufir> findLike(Pageable pageable, String like) throws RecordNotFoundException {
		Page<Ufir> retorno = repository.findLikePage(pageable, like);
		if (retorno.getTotalElements()==0) {
			throw new RecordNotFoundException("Valor não encontado");
		}
		return retorno;
	}

	private Ufir findByIdOrThrowException(Long id) throws RecordNotFoundException{
		return repository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Registro não encontrado com o id " + id));
	}
	public Optional<Ufir> findByData(Calendar data) {
		return repository.findByData(data);
	}

	public Iterable<Ufir> findByDataBetween(Calendar data1, Calendar data2) {
		return repository.findAllByDataLessThanEqualAndDataGreaterThanEqual(data2, data1);
	}

}
