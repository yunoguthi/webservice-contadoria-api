package br.jus.jfsp.nuit.contadoria.service;

import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import br.jus.jfsp.nuit.contadoria.models.AtualizacaoJudicial;
import br.jus.jfsp.nuit.contadoria.repository.AtualizacaoJudicialRepository;
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
public class AtualizacaoJudicialService extends SgsBacenService {
	
	@Autowired
	private AtualizacaoJudicialRepository repository;
	
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
				AtualizacaoJudicial bm = new AtualizacaoJudicial();
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

	public AtualizacaoJudicial create(AtualizacaoJudicial atualizacaoJudicial) {
		return repository.save(atualizacaoJudicial);
	}
	
	public AtualizacaoJudicial save(AtualizacaoJudicial atualizacaoJudicial) {
		return repository.save(atualizacaoJudicial);
	}
	
	public void delete(Long id) {
		repository.deleteById(id);
	}

	public AtualizacaoJudicial update(AtualizacaoJudicial atualizacaoJudicial) throws RecordNotFoundException {
		findByIdOrThrowException(atualizacaoJudicial.getId());
		return repository.save(atualizacaoJudicial);
	}

	public Iterable<AtualizacaoJudicial> getAll(){
		return repository.findAll();
	}

	public Page<AtualizacaoJudicial> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public AtualizacaoJudicial read(Long id) throws RecordNotFoundException {
		return findByIdOrThrowException(id);
	}

	public Optional<AtualizacaoJudicial> findById(Long id) {
		return repository.findById(id);
	}

	public Page<AtualizacaoJudicial> findLike(Pageable pageable, String like) throws RecordNotFoundException {
		Page<AtualizacaoJudicial> retorno = repository.findLikePage(pageable, like);
		if (retorno.getTotalElements()==0) {
			throw new RecordNotFoundException("Valor não encontado");
		}
		return retorno;
	}

	private AtualizacaoJudicial findByIdOrThrowException(Long id) throws RecordNotFoundException{
		return repository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Registro não encontrado com o id " + id));
	}

	public Optional<AtualizacaoJudicial> findByData(Calendar data) {
		return repository.findByData(data);
	}

	public Iterable<AtualizacaoJudicial> findByDataBetween(Calendar data1, Calendar data2) {
		return repository.findAllByDataLessThanEqualAndDataGreaterThanEqual(data2, data1);
	}



}
