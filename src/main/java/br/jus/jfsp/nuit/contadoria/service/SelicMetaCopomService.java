package br.jus.jfsp.nuit.contadoria.service;

import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import br.jus.jfsp.nuit.contadoria.models.SelicMetaCopom;
import br.jus.jfsp.nuit.contadoria.models.SelicMetaCopom;
import br.jus.jfsp.nuit.contadoria.repository.SelicMetaCopomRepository;
import br.jus.jfsp.nuit.contadoria.util.ManipulaData;
import br.jus.jfsp.nuit.contadoria.util.consts.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

@Service
public class SelicMetaCopomService extends SgsBacenService {
	
	@Autowired
	private JsonReader jsonReader;
	
	@Autowired
	private UrlReaderService urlReaderService;
	
	@Autowired
	private SelicMetaCopomRepository repository;
	
	public void importa() {
		
		Calendar dataInicial = repository.findMaxData();
		if (dataInicial == null) {
			dataInicial = new GregorianCalendar(1900,0,31);
		}

		String conteudoUrl = "";
		try {
			conteudoUrl = urlReaderService.getConteudo(getUrl(SELIC_META_COPOM, ManipulaData.toDate(dataInicial)));
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		try {
			Object[] map = jsonReader.getJsonArray(conteudoUrl);
			for (int i = 0; i < map.length; i++) {
				LinkedHashMap lMap = (LinkedHashMap) map[i];
				Date data;
				try {
					data = ManipulaData.stringToDateDiaMesAno(lMap.get("data")+"");
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					continue;
				}
				Double valor = new Double(lMap.get("valor")+"");
				SelicMetaCopom selicMetaCopom = new SelicMetaCopom();
				selicMetaCopom.setData(ManipulaData.toCalendar(data));
				selicMetaCopom.setValor(valor);
				selicMetaCopom.setUltimaAtualizacao(ManipulaData.getHoje());
				selicMetaCopom.setFonte(Consts.SGS_BACEN);
				if (!repository.existsByData(ManipulaData.toCalendar(data))) {
					repository.save(selicMetaCopom);
				}
			}
			
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}

	public SelicMetaCopom create(SelicMetaCopom selicMetaCopom) {
		return repository.save(selicMetaCopom);
	}

	public SelicMetaCopom save(SelicMetaCopom selicMetaCopom) {
		return repository.save(selicMetaCopom);
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public SelicMetaCopom update(SelicMetaCopom selicMetaCopom) throws RecordNotFoundException {
		findByIdOrThrowException(selicMetaCopom.getId());
		return repository.save(selicMetaCopom);
	}

	public Iterable<SelicMetaCopom> getAll(){
		return repository.findAll();
	}

	public Page<SelicMetaCopom> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public SelicMetaCopom read(Long id) throws RecordNotFoundException {
		return findByIdOrThrowException(id);
	}

	public Optional<SelicMetaCopom> findById(Long id) {
		return repository.findById(id);
	}

	public Page<SelicMetaCopom> findLike(Pageable pageable, String like) throws RecordNotFoundException {
		Page<SelicMetaCopom> retorno = repository.findLikePage(pageable, like);
		if (retorno.getTotalElements()==0) {
			throw new RecordNotFoundException("Valor não encontado");
		}
		return retorno;
	}

	private SelicMetaCopom findByIdOrThrowException(Long id) throws RecordNotFoundException{
		return repository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Registro não encontrado com o id " + id));
	}
	public Optional<SelicMetaCopom> findByData(Calendar data) {
		return repository.findByData(data);
	}

	public Iterable<SelicMetaCopom> findByDataBetween(Calendar data1, Calendar data2) {
		return repository.findAllByDataLessThanEqualAndDataGreaterThanEqual(data2, data1);
	}

}
