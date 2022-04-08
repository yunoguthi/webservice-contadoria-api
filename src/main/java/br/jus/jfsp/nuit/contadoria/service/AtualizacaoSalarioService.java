package br.jus.jfsp.nuit.contadoria.service;

import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import br.jus.jfsp.nuit.contadoria.models.AtualizacaoSalario;
import br.jus.jfsp.nuit.contadoria.repository.AtualizacaoSalarioRepository;
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
public class AtualizacaoSalarioService extends SgsBacenService {
	
	@Autowired
	private AtualizacaoSalarioRepository repository;
	
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
				AtualizacaoSalario bm = new AtualizacaoSalario();
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

	public AtualizacaoSalario create(AtualizacaoSalario btnMensal) {
		return repository.save(btnMensal);
	}
	
	public AtualizacaoSalario save(AtualizacaoSalario btnMensal) {
		return repository.save(btnMensal);
	}
	
	public void delete(Long id) {
		repository.deleteById(id);
	}

	public AtualizacaoSalario update(AtualizacaoSalario btnMensal) throws RecordNotFoundException {
		findByIdOrThrowException(btnMensal.getId());
		return repository.save(btnMensal);
	}

	public Iterable<AtualizacaoSalario> getAll(){
		return repository.findAll();
	}

	public Iterable<AtualizacaoSalario> getAll(Sort sort){
		return repository.findAll(sort);
	}

	public Page<AtualizacaoSalario> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public Iterable<AtualizacaoSalario> findAll() {
		return repository.findAll();
	}

	public AtualizacaoSalario read(Long id) throws RecordNotFoundException {
		return findByIdOrThrowException(id);
	}

	public Optional<AtualizacaoSalario> findById(Long id) {
		return repository.findById(id);
	}

	public Page<AtualizacaoSalario> findLike(Pageable pageable, String like) throws RecordNotFoundException {
		Page<AtualizacaoSalario> retorno = repository.findLikePage(pageable, like);
		if (retorno.getTotalElements()==0) {
			throw new RecordNotFoundException("Valor não encontado");
		}
		return retorno;
	}

	private AtualizacaoSalario findByIdOrThrowException(Long id) throws RecordNotFoundException{
		return repository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Registro não encontrado com o id " + id));
	}

	public Optional<AtualizacaoSalario> findByData(Calendar data) {
		return repository.findByData(data);
	}

	public Iterable<AtualizacaoSalario> findByDataBetween(Calendar data1, Calendar data2) {
		return repository.findAllByDataLessThanEqualAndDataGreaterThanEqual(data2, data1);
	}



}
