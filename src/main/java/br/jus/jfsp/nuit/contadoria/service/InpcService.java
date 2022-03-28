package br.jus.jfsp.nuit.contadoria.service;

import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import br.jus.jfsp.nuit.contadoria.models.Inpc;
import br.jus.jfsp.nuit.contadoria.models.Inpc;
import br.jus.jfsp.nuit.contadoria.models.Inpc;
import br.jus.jfsp.nuit.contadoria.repository.InpcRepository;
import br.jus.jfsp.nuit.contadoria.util.ManipulaData;
import br.jus.jfsp.nuit.contadoria.util.consts.Consts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class InpcService extends SidraIbgeService {
	
	protected static final String 
		INPC = "1736",
		INPC_NUMERO_INDICE = "2289",
		INPC_PERCENTUAL_MES = "44",
	    PRECISAO_NUMERO_INDICE = "13",
	    PRECISAO_PERCENTUAL_MES = "2"
	    ;
	
	@Autowired
	private JsonReader jsonReader;
	
	@Autowired
	private UrlReaderService urlReaderService;
	
	@Autowired
	private InpcRepository repository;

	Logger logger = LoggerFactory.getLogger(InpcService.class);

	public void importa() {

		logger.info("Iniciando importação INPC");

		Calendar dataInicial = repository.findMaxData();
		String[] indices = {INPC_NUMERO_INDICE,INPC_PERCENTUAL_MES};
		String[] precisoes = {PRECISAO_NUMERO_INDICE, PRECISAO_PERCENTUAL_MES};

		String conteudoUrl = null;
		try {
			conteudoUrl = urlReaderService.getConteudo(getUrl(INPC, ManipulaData.calendarToStringAnoMes(dataInicial), ManipulaData.dateToStringAnoMes(ManipulaData.getHoje()), indices, precisoes));
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			Object[] map = jsonReader.getJsonArray(conteudoUrl);
			for (int i = 0; i < map.length; i++) {
				LinkedHashMap lMap = (LinkedHashMap) map[i];				
				Inpc inpc = new Inpc();
				String ano = "";
				String mes = "";
				Date data = new Date();
				String dataStr = "";
				Double valor = null;
				Float variacaoMensal = null;
				
				try {
					dataStr = (lMap.get(DATA)+"");
					if (!dataStr.matches("[0-9]+")) {
						continue;
					}
					if (dataStr != null && !data.equals("") && dataStr.length() == 6) {
						ano = dataStr.substring(0, 4);
						mes = dataStr.substring(4, 6);
					}
					if (lMap.get(CODIGO_VARIAVEL).equals(INPC_NUMERO_INDICE)) {
						valor = new Double(lMap.get(VALOR)+"");	
						inpc.setValor(valor);
					} else if (lMap.get(CODIGO_VARIAVEL).equals(INPC_PERCENTUAL_MES)) {
						try {
							variacaoMensal = new Float(lMap.get(VALOR)+"");	
							inpc.setVariacaoMensal(variacaoMensal);
						} catch (Exception e) {
							logger.error("Erro na importação do INPC :" + e.getMessage());
						}
					}
					if (findByDataStr(dataStr).isPresent()) {
						Inpc in = findByDataStr(dataStr).get();
						if (findByDataStr(dataStr).isPresent()) {
							inpc.setId(in.getId());
						}
						if (in.getValor()!=null) {
							inpc.setValor(in.getValor());
						}
						in.setDataStr(dataStr);
						in.setData(ManipulaData.toCalendar(ManipulaData.getData(dataStr + 01, ManipulaData.ANO_MES_DIA_SEM_TRACO)));
						in.setAno(ano);
						in.setMes(mes);
						in.setUltimaAtualizacao(ManipulaData.getHoje());
						in.setVariacaoMensal(variacaoMensal);
						in.setFonte(Consts.SGS_BACEN);
						save(in);
						continue;
					}
					inpc.setDataStr(dataStr);
					inpc.setData(ManipulaData.toCalendar(ManipulaData.getData(dataStr + 01, ManipulaData.ANO_MES_DIA_SEM_TRACO)));
					inpc.setAno(ano);
					inpc.setMes(mes);
					inpc.setUltimaAtualizacao(ManipulaData.getHoje());
					save(inpc);
				} catch (Exception e) {
					logger.error("Erro na importação do INPC :" + e.getMessage());
					continue;
				}
			}
		} catch (IOException e) {
			logger.error("Erro na importação do INPC :" + e.getMessage());
		}

		logger.info("Encerrando importação BTN Mensal");

	}

	public Inpc create(Inpc inpc) {
		return repository.save(inpc);
	}

	public Inpc save(Inpc inpc) {
		return repository.save(inpc);
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public Inpc update(Inpc inpc) throws RecordNotFoundException {
		findByIdOrThrowException(inpc.getId());
		return repository.save(inpc);
	}

	public Iterable<Inpc> getAll(){
		return repository.findAll();
	}

	public Page<Inpc> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public Inpc read(Long id) throws RecordNotFoundException {
		return findByIdOrThrowException(id);
	}

	public Optional<Inpc> findById(Long id) {
		return repository.findById(id);
	}

	public Optional<Inpc> findByData(Calendar data) {
		return repository.findByData(data);
	}

	public Optional<Inpc> findByDataStr(String dataStr) {
		return repository.findByDataStr(dataStr);
	}

//	public Iterable<Inpc> findByDataBetween(Calendar data1, Calendar data2) {
//		return repository.findAllByDataLessThanEqualAndDataGreaterThanEqual(data2, data1);
//	}

	public Page<Inpc> findLike(Pageable pageable, String like) throws RecordNotFoundException {
		Page<Inpc> retorno = repository.findLikePage(pageable, like);
		if (retorno.getTotalElements()==0) {
			return Page.empty();
			//throw new RecordNotFoundException("Valor não encontado");
		}
		return retorno;
	}

	private Inpc findByIdOrThrowException(Long id) throws RecordNotFoundException{
		return repository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Registro não encontrado com o id " + id));
	}
	
//	public Inpc save(Inpc inpc) {
//		if (!repository.existsByData(inpc.getData())) {
//			return repository.save(inpc);
//		} else {
//			Optional<Inpc> i = repository.findByData(inpc.getData());
//			inpc.setId(i.get().getId());
//			inpc.setAno(i.get().getAno());
//			inpc.setMes(i.get().getMes());
//			if (inpc.getValor()==null) {
//				inpc.setValor(i.get().getValor());
//			}
//			if (inpc.getVariacaoMensal()==null) {
//				inpc.setVariacaoMensal(i.get().getVariacaoMensal());
//			}
//			return repository.save(inpc);
//		}
//	}
//
//	public List<Inpc> findAll() {
//		return repository.findAll();
//	}
//
//	public Optional<Inpc> findByData(String data) {
//		return repository.findByData(data);
//	}
//
//	public Iterable<Inpc> findByDataBetween(Calendar data1, Calendar data2) {
//		return repository.findAllByDataLessThanEqualAndDataGreaterThanEqual(data2, data1);
//	}

}