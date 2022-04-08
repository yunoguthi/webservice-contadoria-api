package br.jus.jfsp.nuit.contadoria.service;

import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import br.jus.jfsp.nuit.contadoria.models.Trd;
import br.jus.jfsp.nuit.contadoria.models.Trd;
import br.jus.jfsp.nuit.contadoria.repository.TrdRepository;
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
public class TrdService extends SgsBacenService {
	
	//private static final String url = "https://api.bcb.gov.br/dados/serie/bcdata.sgs.4174/dados?formato=json";
	//private static final String urlCsv = "https://www3.bcb.gov.br/sgspub/pefi300/consultarTaxasJuros.do?method=downloadUC47&RN=UC47.RN01";

	@Autowired
	private JsonReader jsonReader;
	
	@Autowired
	private UrlReaderService urlReaderService;
	
	@Autowired
	private TrdRepository repository;
	
	public void importa() {
		
		Calendar dataInicial = repository.findMaxData();
		
		String conteudoUrl = "";
		try {
			conteudoUrl = urlReaderService.getConteudo(getUrl(TRD, ManipulaData.toDate(dataInicial)));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		try {
			Object[] map = jsonReader.getJsonArray(conteudoUrl);
			for (int i = 0; i < map.length; i++) {
				LinkedHashMap lMap = (LinkedHashMap) map[i];
				Date data;
				Date dataFim;

				try {
					data = ManipulaData.stringToDateDiaMesAno(lMap.get("data")+"");
					dataFim = ManipulaData.stringToDateDiaMesAno(lMap.get("datafim")+"");

				} catch (ParseException e) {
					e.printStackTrace();
					continue;
				}
				Double valor = new Double(lMap.get("valor")+"");
				Trd tr = new Trd();
				tr.setData(ManipulaData.toCalendar(data));
				tr.setDataFim(ManipulaData.toCalendar(dataFim));
				tr.setValor(valor);
				tr.setUltimaAtualizacao(ManipulaData.getHoje());
				tr.setFonte(Consts.SGS_BACEN);
				if (!repository.existsByData(ManipulaData.toCalendar(data))) {
					repository.save(tr);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Trd create(Trd trd) {
		return repository.save(trd);
	}

	public Trd save(Trd trd) {
		return repository.save(trd);
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public Trd update(Trd trd) throws RecordNotFoundException {
		findByIdOrThrowException(trd.getId());
		return repository.save(trd);
	}

	public Iterable<Trd> getAll(){
		return repository.findAll();
	}

	public Page<Trd> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public Iterable<Trd> findAll() {
		return repository.findAll();
	}

	public Trd read(Long id) throws RecordNotFoundException {
		return findByIdOrThrowException(id);
	}

	public Optional<Trd> findById(Long id) {
		return repository.findById(id);
	}

	public Page<Trd> findLike(Pageable pageable, String like) throws RecordNotFoundException {
		Page<Trd> retorno = repository.findLikePage(pageable, like);
		if (retorno.getTotalElements()==0) {
			throw new RecordNotFoundException("Valor não encontado");
		}
		return retorno;
	}

	private Trd findByIdOrThrowException(Long id) throws RecordNotFoundException{
		return repository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Registro não encontrado com o id " + id));
	}
	public Optional<Trd> findByData(Calendar data) {
		return repository.findByData(data);
	}

	public Iterable<Trd> findByDataBetween(Calendar data1, Calendar data2) {
		return repository.findAllByDataLessThanEqualAndDataGreaterThanEqual(data2, data1);
	}

}