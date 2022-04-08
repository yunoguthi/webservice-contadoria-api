package br.jus.jfsp.nuit.contadoria.service;

import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import br.jus.jfsp.nuit.contadoria.models.Irsm;
import br.jus.jfsp.nuit.contadoria.models.Irsm;
import br.jus.jfsp.nuit.contadoria.repository.IrsmRepository;
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
public class IrsmService extends SidraIbgeService {
	
	protected static final String 	
		IRSM = "90",
		//IRSM_PERCENTUAL_MES = "64/C73/2639",
		IRSM_PERCENTUAL_MES = "64",
	    PRECISAO_PERCENTUAL_MES = "2",
	    COMPLEMENTO = "/C73/2639"
	    ;
	
	@Autowired
	private JsonReader jsonReader;
	
	@Autowired
	private UrlReaderService urlReaderService;
	
	@Autowired
	private IrsmRepository repository;
	
	@Override
	protected String getUrl(String codigo, String dataInicial, String dataFinal, String[] variaveis, String[] precisoes) {
		System.out.println(urlInicio + codigo + url2 
				+ montaVariavel(variaveis) + url3 + adicionaComplementos(dataInicial, dataFinal) +
				montaPrecisao(variaveis, precisoes) + url6);
		return urlInicio + codigo + url2 
				+ montaVariavel(variaveis) + url3 + adicionaComplementos(dataInicial, dataFinal) +
				montaPrecisao(variaveis, precisoes) + COMPLEMENTO + url6;
	}
	
	public void importa() {
		
		String dataInicial = repository.findMaxDataStr();
		
		String conteudoUrl = "";
		try {
			String[] indices = {IRSM_PERCENTUAL_MES};
			String[] precisoes = {PRECISAO_PERCENTUAL_MES};
			conteudoUrl = urlReaderService.getConteudo(getUrl(IRSM, dataInicial, ManipulaData.dateToStringAnoMes(ManipulaData.getHoje()), indices, precisoes));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		try {
			Object[] map = jsonReader.getJsonArray(conteudoUrl);
			for (int i = 0; i < map.length; i++) {
				LinkedHashMap lMap = (LinkedHashMap) map[i];				
				Irsm irsm = new Irsm();
				String ano = "";
				String mes = "";
				String data = "";
				Double numeroIndice = null;
				Float variacaoMensal = null;
				
				try {
					data = (lMap.get(DATA)+"");
					if (!data.matches("[0-9]+")) {
						continue;
					}
					if (data != null && !data.equals("") && data.length() == 6) {
						ano = data.substring(0, 4);
						mes = data.substring(4, 6);
					}
					if (lMap.get(CODIGO_VARIAVEL).equals(IRSM_PERCENTUAL_MES)) {
						try {
							variacaoMensal = new Float(lMap.get(VALOR)+"");	
							irsm.setVariacaoMensal(variacaoMensal);
						} catch (Exception e) {
							System.out.println(lMap.get(VALOR)+"");
						}
					}
					irsm.setDataStr(data);
					irsm.setData(ManipulaData.toCalendar(ManipulaData.getData(data + "01", ManipulaData.ANO_MES_DIA_SEM_TRACO)));
					irsm.setAno(ano);
					irsm.setMes(mes);
					irsm.setUltimaAtualizacao(ManipulaData.getHoje());
					irsm.setFonte(Consts.SIDRA_IBGE);
					save(irsm);
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Irsm create(Irsm irsm) {
		return repository.save(irsm);
	}

	public Irsm save(Irsm irsm) {
		return repository.save(irsm);
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public Irsm update(Irsm irsm) throws RecordNotFoundException {
		findByIdOrThrowException(irsm.getId());
		return repository.save(irsm);
	}

	public Iterable<Irsm> getAll(){
		return repository.findAll();
	}

	public Page<Irsm> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public Iterable<Irsm> findAll() {
		return repository.findAll();
	}

	public Irsm read(Long id) throws RecordNotFoundException {
		return findByIdOrThrowException(id);
	}

	public Optional<Irsm> findById(Long id) {
		return repository.findById(id);
	}

	public Page<Irsm> findLike(Pageable pageable, String like) throws RecordNotFoundException {
		Page<Irsm> retorno = repository.findLikePage(pageable, like);
		if (retorno.getTotalElements()==0) {
			throw new RecordNotFoundException("Valor não encontado");
		}
		return retorno;
	}

	private Irsm findByIdOrThrowException(Long id) throws RecordNotFoundException{
		return repository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Registro não encontrado com o id " + id));
	}
	public Optional<Irsm> findByData(String data) {
		return repository.findByData(data);
	}

	public Iterable<Irsm> findByDataBetween(Calendar data1, Calendar data2) {
		return repository.findAllByDataLessThanEqualAndDataGreaterThanEqual(data2, data1);
	}
		
}