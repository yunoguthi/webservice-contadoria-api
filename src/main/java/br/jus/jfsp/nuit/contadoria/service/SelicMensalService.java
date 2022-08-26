package br.jus.jfsp.nuit.contadoria.service;

import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import br.jus.jfsp.nuit.contadoria.models.SelicMensal;
import br.jus.jfsp.nuit.contadoria.models.SelicMensal;
import br.jus.jfsp.nuit.contadoria.repository.SelicMensalRepository;
import br.jus.jfsp.nuit.contadoria.util.ManipulaData;
import br.jus.jfsp.nuit.contadoria.util.consts.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;


@Service
public class SelicMensalService extends SgsBacenService {
	
	@Autowired
	private JsonReader jsonReader;

	@Autowired
	private UrlReaderService urlReaderService;

	@Autowired
	private SelicMensalRepository repository;

	public void importa(){

		Calendar dataInicial = repository.findMaxData();
		if (dataInicial == null) {
			dataInicial = new GregorianCalendar(1900,0,31);
		}
		System.out.println("selic: " + dataInicial);

		String conteudoUrl = "";

		try{
			conteudoUrl = urlReaderService.getConteudo(getUrl(SELIC_MENSAL, ManipulaData.toDate(dataInicial)));
		}catch (IOException e){
			e.printStackTrace();
		}
		try{
			Object[] map = jsonReader.getJsonArray(conteudoUrl);
			for (int i = 0; i < map.length; i++) {
				LinkedHashMap lMap = (LinkedHashMap) map[i];
				Date data;
				try{
					data = ManipulaData.stringToDateDiaMesAno(lMap.get("data")+"");
				}catch (Exception e){
					e.printStackTrace();
					continue;
				}
				Double valor = new Double(lMap.get("valor")+"");
				SelicMensal selicMensal = new SelicMensal();
				selicMensal.setData(ManipulaData.toCalendar(data));
				selicMensal.setValor(valor/100);
				selicMensal.setUltimaAtualizacao(ManipulaData.getHoje());
				selicMensal.setFonte(Consts.SGS_BACEN);
				int mesAtual = ManipulaData.getMes(ManipulaData.getHoje());
				int anoAtual = ManipulaData.getAno(ManipulaData.getHoje());
				if(!repository.existsByData(ManipulaData.toCalendar(data)) &&
						!(ManipulaData.getMes(data) == ManipulaData.getMes(ManipulaData.getHoje()) &&
						ManipulaData.getAno(data) == ManipulaData.getAno(ManipulaData.getHoje())) ) {
					repository.save(selicMensal);
				}
			}
		}catch (IOException e){
			e.printStackTrace();
		}
	}

	public SelicMensal create(SelicMensal selicMensal) {
		return repository.save(selicMensal);
	}

	public SelicMensal save(SelicMensal selicMensal) {
		return repository.save(selicMensal);
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public SelicMensal update(SelicMensal selicMensal) throws RecordNotFoundException {
		findByIdOrThrowException(selicMensal.getId());
		return repository.save(selicMensal);
	}

	public Iterable<SelicMensal> getAll(){
		return repository.findAll();
	}

	public Iterable<SelicMensal> getAll(Sort sort){
		return repository.findAll(sort);
	}

	public Page<SelicMensal> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public Iterable<SelicMensal> findAll() {
		return repository.findAll();
	}

	public SelicMensal read(Long id) throws RecordNotFoundException {
		return findByIdOrThrowException(id);
	}

	public Optional<SelicMensal> findById(Long id) {
		return repository.findById(id);
	}

	public Page<SelicMensal> findLike(Pageable pageable, String like) throws RecordNotFoundException {
		Page<SelicMensal> retorno = repository.findLikePage(pageable, like);
		if (retorno.getTotalElements()==0) {
			throw new RecordNotFoundException("Valor não encontado");
		}
		return retorno;
	}

	private SelicMensal findByIdOrThrowException(Long id) throws RecordNotFoundException{
		return repository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Registro não encontrado com o id " + id));
	}

	public Optional<SelicMensal> findByData(Calendar data) {
		return repository.findByData(data);
	}

	public Iterable<SelicMensal> findByDataBetween(Calendar data1, Calendar data2) {
		return repository.findAllByDataLessThanEqualAndDataGreaterThanEqual(data2, data1);
	}

}
