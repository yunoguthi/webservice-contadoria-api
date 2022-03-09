package br.jus.jfsp.nuit.contadoria.service;

import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import br.jus.jfsp.nuit.contadoria.models.BtnMensal;
import br.jus.jfsp.nuit.contadoria.repository.BtnMensalRepository;
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
public class BtnMensalService extends SgsBacenService {
	
	@Autowired
	private BtnMensalRepository repository;
	
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
				BtnMensal bm = new BtnMensal();
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

	public BtnMensal create(BtnMensal btnMensal) {
		return repository.save(btnMensal);
	}
	
	public BtnMensal save(BtnMensal btnMensal) {
		return repository.save(btnMensal);
	}
	
	public void delete(Long id) {
		repository.deleteById(id);
	}

	public BtnMensal update(BtnMensal btnMensal) throws RecordNotFoundException {
		findByIdOrThrowException(btnMensal.getId());
		return repository.save(btnMensal);
	}

	public Iterable<BtnMensal> getAll(){
		return repository.findAll();
	}

	public Page<BtnMensal> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public BtnMensal read(Long id) throws RecordNotFoundException {
		return findByIdOrThrowException(id);
	}

	public Optional<BtnMensal> findById(Long id) {
		return repository.findById(id);
	}

	public Page<BtnMensal> findLike(Pageable pageable, String like) throws RecordNotFoundException {
		Page<BtnMensal> retorno = repository.findLikePage(pageable, like);
		if (retorno.getTotalElements()==0) {
			throw new RecordNotFoundException("Valor não encontado");
		}
		return retorno;
	}

	private BtnMensal findByIdOrThrowException(Long id) throws RecordNotFoundException{
		return repository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Registro não encontrado com o id " + id));
	}

	public Optional<BtnMensal> findByData(Calendar data) {
		return repository.findByData(data);
	}

	public Iterable<BtnMensal> findByDataBetween(Calendar data1, Calendar data2) {
		return repository.findAllByDataLessThanEqualAndDataGreaterThanEqual(data2, data1);
	}



}
