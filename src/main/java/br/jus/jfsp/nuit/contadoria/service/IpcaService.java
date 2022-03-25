package br.jus.jfsp.nuit.contadoria.service;

import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import br.jus.jfsp.nuit.contadoria.models.Ipca;
import br.jus.jfsp.nuit.contadoria.models.Ipca;
import br.jus.jfsp.nuit.contadoria.repository.IpcaRepository;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

@Service
public class IpcaService extends SidraIbgeService {
	
	protected static final String 
		IPCA =  "1737",
	    IPCA_NUMERO_INDICE =  "2266",
	    IPCA_PERCENTUAL_MES =  "63",
	    PRECISAO_NUMERO_INDICE = "13",
	    PRECISAO_PERCENTUAL_MES = "2"
	    ;
	
	@Autowired
	private JsonReader jsonReader;
	
	@Autowired
	private UrlReaderService urlReaderService;
	
	@Autowired
	private IpcaRepository repository;
	
	public void importa() {
		
		String dataInicial = repository.findMaxDataStr();

		String conteudoUrl = "";
		try {
			String[] indices = {IPCA_NUMERO_INDICE,IPCA_PERCENTUAL_MES};
			String[] precisoes = {PRECISAO_NUMERO_INDICE, PRECISAO_PERCENTUAL_MES};
			conteudoUrl = urlReaderService.getConteudo(getUrl(IPCA, dataInicial, ManipulaData.dateToStringAnoMes(ManipulaData.getHoje()), indices, precisoes));
			System.out.println("*** " + conteudoUrl);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		try {
			Object[] map = jsonReader.getJsonArray(conteudoUrl);
			for (int i = 0; i < map.length; i++) {
				LinkedHashMap lMap = (LinkedHashMap) map[i];				
				Ipca ipca = new Ipca();
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
					if (repository.existsByDataStr(data)) {
						ipca = repository.findByDataStr(data).get();
					}
					if (lMap.get(CODIGO_VARIAVEL).equals(IPCA_NUMERO_INDICE)) {
						numeroIndice = new Double(lMap.get(VALOR)+"");	
						ipca.setValor(numeroIndice);
					} else if (lMap.get(CODIGO_VARIAVEL).equals(IPCA_PERCENTUAL_MES)) {
						try {
							variacaoMensal = new Float(lMap.get(VALOR)+"");	
							ipca.setVariacaoMensal(variacaoMensal);
						} catch (Exception e) {
							//System.out.println(lMap.get(VALOR)+"");
						}
					}

//					if (repository.existsByData(ipca.getData())) {
					if (ipca!=null && ipca.getData()!=null && repository.findByData(ipca.getData())!=null) {
					ipca.setId(findByData(ipca.getData()).get().getId());
						update(ipca);
					} else {
						ipca.setData(ManipulaData.toCalendar(ManipulaData.getData(data + "01", ManipulaData.ANO_MES_DIA_SEM_TRACO)));
						ipca.setDataStr(data);
						ipca.setAno(ano);
						ipca.setMes(mes);
						ipca.setUltimaAtualizacao(ManipulaData.getHoje());
						ipca.setFonte(Consts.SIDRA_IBGE);
						save(ipca);
					}
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Ipca create(Ipca ipca) {
		return repository.save(ipca);
	}

	public Ipca save(Ipca ipca) {
		return repository.save(ipca);
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public Ipca update(Ipca ipca) throws RecordNotFoundException {
		findByIdOrThrowException(ipca.getId());
		return repository.save(ipca);
	}

	public Iterable<Ipca> getAll(){
		return repository.findAll();
	}

	public Iterable<Ipca> getAll(Sort sort){
		return repository.findAll(sort);
	}

	public Page<Ipca> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public Ipca read(Long id) throws RecordNotFoundException {
		return findByIdOrThrowException(id);
	}

	public Optional<Ipca> findById(Long id) {
		return repository.findById(id);
	}

	public Page<Ipca> findLike(Pageable pageable, String like) throws RecordNotFoundException {
		Page<Ipca> retorno = repository.findLikePage(pageable, like);
		if (retorno.getTotalElements()==0) {
			throw new RecordNotFoundException("Valor não encontado");
		}
		return retorno;
	}

	private Ipca findByIdOrThrowException(Long id) throws RecordNotFoundException{
		return repository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Registro não encontrado com o id " + id));
	}
	
	public Optional<Ipca> findByData(Calendar data) {
		return repository.findByData(data);
	}

	public Iterable<Ipca> findByDataBetween(Calendar data1, Calendar data2) {
		return repository.findAllByDataLessThanEqualAndDataGreaterThanEqual(data2, data1);
	}

}