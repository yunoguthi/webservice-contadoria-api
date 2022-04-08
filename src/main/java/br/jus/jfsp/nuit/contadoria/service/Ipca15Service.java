package br.jus.jfsp.nuit.contadoria.service;

import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import br.jus.jfsp.nuit.contadoria.models.Ipca15;
import br.jus.jfsp.nuit.contadoria.models.Ipca15;
import br.jus.jfsp.nuit.contadoria.repository.Ipca15Repository;
import br.jus.jfsp.nuit.contadoria.util.ManipulaData;
import br.jus.jfsp.nuit.contadoria.util.consts.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

@Service
public class Ipca15Service extends SidraIbgeService {
	
	protected static final String 	
		IPCA15 =  "1705",
		//IPCA15_PERCENTUAL_MES =  "355/c315/7169",
		IPCA15_PERCENTUAL_MES =  "355",
	    PRECISAO_PERCENTUAL_MES = "2",
	    COMPLEMENTO = "/c315/7169"
	    ;
	
	@Autowired
	private JsonReader jsonReader;
	
	@Autowired
	private UrlReaderService urlReaderService;
	
	@Autowired
	private Ipca15Repository repository;
	
	@Override
	protected String getUrl(String codigo, String dataInicial, String dataFinal, String[] variaveis, String[] precisoes) {
		return urlInicio + codigo + url2 
				+ montaVariavel(variaveis) + url3 + adicionaComplementos(dataInicial, dataFinal) +
				montaPrecisao(variaveis, precisoes) + COMPLEMENTO + url6;
	}
	
	public void importa() {
		
		Calendar dataInicial = repository.findMaxData();
		
		String conteudoUrl = "";
		try {
			String[] indices = {IPCA15_PERCENTUAL_MES};
			String[] precisoes = {PRECISAO_PERCENTUAL_MES};
			conteudoUrl = urlReaderService.getConteudo(getUrl(IPCA15, ManipulaData.calendarToStringAnoMes(dataInicial) , ManipulaData.dateToStringAnoMes(ManipulaData.getHoje()), indices, precisoes));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		try {
			Object[] map = jsonReader.getJsonArray(conteudoUrl);
			for (int i = 0; i < map.length; i++) {
				LinkedHashMap lMap = (LinkedHashMap) map[i];				
				Ipca15 ipca15 = new Ipca15();
				String ano = "";
				String mes = "";
				String dataStr = "";
				Float variacaoMensal = null;
				
				try {
					dataStr = (lMap.get(DATA)+"");
					if (!dataStr.matches("[0-9]+")) {
						continue;
					}
					if (dataStr != null && !dataStr.equals("") && dataStr.length() == 6) {
						ano = dataStr.substring(0, 4);
						mes = dataStr.substring(4, 6);
					}
					if (lMap.get(CODIGO_VARIAVEL).equals(IPCA15_PERCENTUAL_MES)) {
						try {
							variacaoMensal = new Float(lMap.get(VALOR)+"");	
							ipca15.setVariacaoMensal(variacaoMensal);
							ipca15.setValor(variacaoMensal.doubleValue());
						} catch (Exception e) {
							System.out.println(lMap.get(VALOR)+"");
						}
					}
					ipca15.setData(ManipulaData.toCalendar(ManipulaData.getData(dataStr + "01", ManipulaData.ANO_MES_DIA_SEM_TRACO)));
					ipca15.setAno(ano);
					ipca15.setMes(mes);
					ipca15.setUltimaAtualizacao(ManipulaData.getHoje());
					ipca15.setFonte(Consts.SIDRA_IBGE);
					save(ipca15);
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Ipca15 create(Ipca15 ipca15) {
		return repository.save(ipca15);
	}

	public Ipca15 save(Ipca15 ipca15) {
		return repository.save(ipca15);
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public Ipca15 update(Ipca15 ipca15) throws RecordNotFoundException {
		findByIdOrThrowException(ipca15.getId());
		return repository.save(ipca15);
	}

	public Iterable<Ipca15> getAll(){
		return repository.findAll();
	}

	public Page<Ipca15> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public Iterable<Ipca15> findAll() {
		return repository.findAll();
	}

	public Ipca15 read(Long id) throws RecordNotFoundException {
		return findByIdOrThrowException(id);
	}

	public Optional<Ipca15> findById(Long id) {
		return repository.findById(id);
	}

	public Page<Ipca15> findLike(Pageable pageable, String like) throws RecordNotFoundException {
		Page<Ipca15> retorno = repository.findLikePage(pageable, like);
		if (retorno.getTotalElements()==0) {
			throw new RecordNotFoundException("Valor não encontado");
		}
		return retorno;
	}

	private Ipca15 findByIdOrThrowException(Long id) throws RecordNotFoundException{
		return repository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Registro não encontrado com o id " + id));
	}

	public Optional<Ipca15> findByData(String data) {
		return repository.findByData(data);
	}

	public Iterable<Ipca15> findByDataBetween(Calendar data1, Calendar data2) {
		return repository.findAllByDataLessThanEqualAndDataGreaterThanEqual(data2, data1);
	}

}