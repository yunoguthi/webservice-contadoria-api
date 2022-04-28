package br.jus.jfsp.nuit.contadoria.service;

import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import br.jus.jfsp.nuit.contadoria.models.AtualizacaoJudicial;
import br.jus.jfsp.nuit.contadoria.models.AtualizacaoJudicial;
import br.jus.jfsp.nuit.contadoria.models.IndicesAtrasados;
import br.jus.jfsp.nuit.contadoria.models.Juros;
import br.jus.jfsp.nuit.contadoria.models.IndicesRes134;
import br.jus.jfsp.nuit.contadoria.models.Juros;
import br.jus.jfsp.nuit.contadoria.models.SelicMetaCopom;
import br.jus.jfsp.nuit.contadoria.models.TrMensal;
import br.jus.jfsp.nuit.contadoria.repository.JurosRepository;
import br.jus.jfsp.nuit.contadoria.util.ManipulaArquivo;
import br.jus.jfsp.nuit.contadoria.util.ManipulaData;
import br.jus.jfsp.nuit.contadoria.util.consts.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Optional;

@Service
public class JurosService extends SgsBacenService {
	
	@Autowired
	private JurosRepository repository;
	
	@Autowired
	private JsonReader jsonReader;

	@Autowired
	private UrlReaderService urlReader;

	@Autowired
	private AtualizacaoJudicialService atualizacaoJudicialService;

	@Autowired
	private SelicMetaCopomService selicMetaCopomService;

	public void importa() {

		Iterable<AtualizacaoJudicial> listAtualizacaoJudicial = atualizacaoJudicialService.getAll(Sort.by("data").descending());
		Double anterior = 0.0;
		for (AtualizacaoJudicial atualizacaoJudicial: listAtualizacaoJudicial) {
			Double valor = new Double(0.0);
			Calendar junho2009 = ManipulaData.getCalendar("2009-06-01", ManipulaData.ANO_MES_DIA);
			Calendar julho2009 = ManipulaData.getCalendar("2009-07-01", ManipulaData.ANO_MES_DIA);
			Calendar maio2012 = ManipulaData.getCalendar("2012-05-01", ManipulaData.ANO_MES_DIA);
			Calendar junho2012 = ManipulaData.getCalendar("2012-06-01", ManipulaData.ANO_MES_DIA);
			Double raiz = 1.0 / 12.0;

			if (atualizacaoJudicial.getData().compareTo(junho2009) <= 0) {
				valor = new Double(0.01);
				try {
					repository.save(new Juros(valor, atualizacaoJudicial.getData()));
				} catch (Exception e) {}
			}
			if (atualizacaoJudicial.getData().compareTo(junho2009) > 0 && atualizacaoJudicial.getData().compareTo(maio2012) <= 0) {
				valor = new Double(0.005);
				try {
					repository.save(new Juros(valor, atualizacaoJudicial.getData()));
				} catch (Exception e) {}
			}
			if (atualizacaoJudicial.getData().compareTo(maio2012) > 0) {
				SelicMetaCopom selicMetaCopom = selicMetaCopomService.findByData(atualizacaoJudicial.getData()).get();
				if (selicMetaCopom.getValor().compareTo(new Double(8.5)) > 0) {
					valor = new Double(0.005);
					Juros juros = new Juros();
					juros.setData(selicMetaCopom.getData());
					juros.setValor(valor);
					repository.save(juros);
				} else {
					BigDecimal val = new BigDecimal(selicMetaCopom.getValor()).multiply(new BigDecimal(0.7)).divide(new BigDecimal(100.0));
					val = val.plus();
					val = new BigDecimal(Math.pow(valor.doubleValue(), raiz));
					val.add(new BigDecimal(-1.0));
					//valor = (new Double(selicMetaCopom.getValor()*0.7) / 100);
					//valor = valor + 1;
					//valor = Math.pow(valor.doubleValue(), raiz);
					//valor = valor -1;
					Juros juros = new Juros();
					juros.setData(selicMetaCopom.getData());
					juros.setValor(selicMetaCopom.getValor()*0.7);
					repository.save(juros);
				}
			}
		}
	}

	public void mostraCSV(String[] indice) {
		ArrayList<Juros> listJuros = (ArrayList<Juros>) repository.findAll(Sort.by("data"));
		Calendar dataMaiorErro = null;
		String[] csv = new String[indice.length+1];
		csv[0] = "COMPETENCIA;VALOR;VALOR_GOOGLE";
		for(int i=0; i<listJuros.size(); i++) {
			String valorFormatado = new DecimalFormat("#,##0.00000000000000").format(listJuros.get(i).getValor());
			boolean igual = valorFormatado.equals(indice[i]);
			String resultado = igual ? "OK" : valorFormatado + " - " + indice[i];
			csv[i+1] = ManipulaData.dateToStringDiaMesAno(ManipulaData.toDate(listJuros.get(i).getData())) + ";" +
					(listJuros.get(i).getValor() + ";").replaceAll("\\.", ",") +
					indice[i] + ";";
		}
		try {
			ManipulaArquivo.geraArquivo("teste_juros.csv", csv);
		} catch (IOException e) {}
	}

	public void testeIndice(String[] coluna) {
		ArrayList<Juros> listJuros = (ArrayList<Juros>) repository.findAll(Sort.by("data"));
		for(int i=0; i<listJuros.size(); i++) {
			String valorFormatado = new DecimalFormat("#,##0.00000000000000").format(listJuros.get(i).getValor());
			boolean igual = valorFormatado.equals(coluna[i]);
			String resultado = igual ? "OK" : valorFormatado + " - " + coluna[i];
		}
	}

	public void testando() {

		String[] normalizadosIndices = ManipulaArquivo.normalizar(ManipulaArquivo.getColuna(21));
//		String[] normalizadosIndicesAcumulados = ManipulaArquivo.normalizar(ManipulaArquivo.getColuna(19));

//		System.out.println("Fim comparação de índices");

		mostraCSV(normalizadosIndices);

	}

	public Juros create(Juros juros) {
		return repository.save(juros);
	}
	
	public Juros save(Juros juros) {
		return repository.save(juros);
	}
	
	public void delete(Long id) {
		repository.deleteById(id);
	}

	public Juros update(Juros juros) throws RecordNotFoundException {
		findByIdOrThrowException(juros.getId());
		return repository.save(juros);
	}

	public Iterable<Juros> getAll(){
		return repository.findAll();
	}

	public Iterable<Juros> getAll(Sort sort){
		return repository.findAll(sort);
	}

	public Page<Juros> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public Iterable<Juros> findAll() {
		return repository.findAll();
	}

	public Juros read(Long id) throws RecordNotFoundException {
		return findByIdOrThrowException(id);
	}

	public Optional<Juros> findById(Long id) {
		return repository.findById(id);
	}

	public Page<Juros> findLike(Pageable pageable, String like) throws RecordNotFoundException {
		Page<Juros> retorno = repository.findLikePage(pageable, like);
		if (retorno.getTotalElements()==0) {
			throw new RecordNotFoundException("Valor não encontado");
		}
		return retorno;
	}

	private Juros findByIdOrThrowException(Long id) throws RecordNotFoundException{
		return repository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Registro não encontrado com o id " + id));
	}

	public Optional<Juros> findByData(Calendar data) {
		return repository.findByData(data);
	}

	public Iterable<Juros> findByDataBetween(Calendar data1, Calendar data2) {
		return repository.findAllByDataLessThanEqualAndDataGreaterThanEqual(data2, data1);
	}



}
