package br.jus.jfsp.nuit.contadoria.service;

import br.jus.jfsp.nuit.contadoria.exception.DataInvalidaException;
import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import br.jus.jfsp.nuit.contadoria.models.EMoeda;
import br.jus.jfsp.nuit.contadoria.models.IndicesAtrasados;
import br.jus.jfsp.nuit.contadoria.models.SalarioMinimo;
import br.jus.jfsp.nuit.contadoria.models.SalarioMinimo;
import br.jus.jfsp.nuit.contadoria.repository.SalarioMinimoRepository;
import br.jus.jfsp.nuit.contadoria.util.ManipulaArquivo;
import br.jus.jfsp.nuit.contadoria.util.ManipulaData;
import br.jus.jfsp.nuit.contadoria.util.ManipulaMoeda;
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
import java.util.List;
import java.util.Optional;

@Service
public class SalarioMinimoService extends SgsBacenService {
	
	
	@Autowired
	private SalarioMinimoRepository repository;
	
	@Autowired
	private JsonReader jsonReader;

	@Autowired
	private UrlReaderService urlReader;

	public void teste(String[] coluna) {
		ArrayList<SalarioMinimo> list = (ArrayList<SalarioMinimo>) repository.findAll(Sort.by("data"));
		BigDecimal erro = new BigDecimal(0.0);
		BigDecimal maiorErro = new BigDecimal(0.0);
		Calendar dataMaiorErro = null;

		for(int i=0; i<list.size(); i++) {
			//System.out.println("GOOGLE: " + coluna[i] + " CALCULADO: " + list.get(i).getValor());

			String valorFormatado = new DecimalFormat("#,##0.00000000000000").format(list.get(i).getValor());

			boolean igual = valorFormatado.equals(coluna[i]);
			String resultado = igual ? "OK" : valorFormatado + " - " + coluna[i];
			coluna[i] = coluna[i].replaceAll(".", "");
			if (!igual) {
				if (list.get(i).getValor().compareTo(BigDecimal.valueOf(Double.valueOf(coluna[i].replaceAll(",", "."))).doubleValue()) > 0) {
					erro = new BigDecimal(list.get(i).getValor() - Double.valueOf(coluna[i].replaceAll(",", ".")));
				} else {
					erro = new BigDecimal(Double.valueOf(coluna[i].replaceAll(",", ".")) - list.get(i).getValor());
				}
				if (erro.compareTo(maiorErro) > 0) {
					maiorErro = erro;
					dataMaiorErro = list.get(i).getData();
				}
				//System.out.println(ManipulaData.calendarToStringAnoMes(list.get(i).getData()) + " ; " + erro);

				//System.out.println(ManipulaData.calendarToStringAnoMes(list.get(i).getData()) + " - " + "Erro: " + erro);
			}
			//System.out.println(ManipulaData.calendarToStringAnoMes(list.get(i).getData()) + " - " + resultado);
		}
		//System.out.println(ManipulaData.calendarToStringAnoMes(dataMaiorErro) + " Maior erro: " + maiorErro);

	}

	public void mostraCSV(String[] indice) {
		ArrayList<SalarioMinimo> list = (ArrayList<SalarioMinimo>) repository.findAll(Sort.by("data"));
		Calendar dataMaiorErro = null;
		String[] csv = new String[indice.length+1];
		csv[0] = "COMPETENCIA;VALOR_CALCULADO;VALOR_GOOGLE";
		for(int i=0; i<list.size(); i++) {
			System.out.println(indice[i]);

			String valorFormatado = new DecimalFormat("#,##0.00000000000000").format(list.get(i).getValor());
			boolean igual = valorFormatado.equals(indice[i]);
			String resultado = igual ? "OK" : valorFormatado + " - " + indice[i];
			csv[i+1] = ManipulaData.dateToStringDiaMesAno(ManipulaData.toDate(list.get(i).getData())) + ";" +
					list.get(i).getValor() + ";" +
					indice[i];
			System.out.println(csv[i+1]);
		}
		try {
			ManipulaArquivo.geraArquivo("teste_sm.csv", csv);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void testeIndice(String[] coluna) {
		ArrayList<SalarioMinimo> list = (ArrayList<SalarioMinimo>) repository.findAll(Sort.by("data"));
		for(int i=0; i<list.size(); i++) {
			String valorFormatado = new DecimalFormat("#,##0.00000000000000").format(list.get(i).getValor());
			boolean igual = valorFormatado.equals(coluna[i]);
			String resultado = igual ? "OK" : valorFormatado + " - " + coluna[i];
			System.out.println(ManipulaData.calendarToStringAnoMes(list.get(i).getData()) + " - " + resultado);
		}
	}

	public void testando() {


		System.out.println("Início comparação de salario minimo");
		String[] normalizados = ManipulaArquivo.normalizar(ManipulaArquivo.getColuna(1));
		//teste(normalizados);
		System.out.println("Fim comparação de salario minimo");

		mostraCSV(normalizados);



	}

	public void updateMoeda() {
		Iterable<SalarioMinimo> listSalarioMinimo = findAll();
		for (SalarioMinimo sm : listSalarioMinimo) {
			try {
				sm.setMoeda(ManipulaMoeda.getMoedaCorrente(sm.getData()));
			} catch (DataInvalidaException e1) {
				e1.printStackTrace();
			}
			repository.save(sm);
		}
	}
	
	public void importa() {

		Calendar dataInicial = repository.findMaxData();
		
		String conteudoUrl = "";
		try {
			conteudoUrl = urlReader.getConteudo(getUrl(SALARIO_MINIMO, ManipulaData.toDate(dataInicial)));
			
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
				SalarioMinimo sm = new SalarioMinimo();
				sm.setData(ManipulaData.toCalendar(data));
				sm.setValor(valor);
				sm.setFonte(Consts.SGS_BACEN);
				try {
					sm.setMoeda(ManipulaMoeda.getMoedaCorrente(ManipulaData.toCalendar(data)));
				} catch (DataInvalidaException e1) {
					e1.printStackTrace();
				}
				if (!repository.existsByData(ManipulaData.toCalendar(data))) {
					repository.save(sm);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}

	public SalarioMinimo create(SalarioMinimo salarioMinimo) {
		return repository.save(salarioMinimo);
	}

	public SalarioMinimo save(SalarioMinimo salarioMinimo) {
		return repository.save(salarioMinimo);
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public SalarioMinimo update(SalarioMinimo salarioMinimo) throws RecordNotFoundException {
		findByIdOrThrowException(salarioMinimo.getId());
		return repository.save(salarioMinimo);
	}

	public Iterable<SalarioMinimo> getAll(){
		return repository.findAll();
	}

	public Iterable<SalarioMinimo> getAll(Sort sort){
		return repository.findAll(sort);
	}

	public Page<SalarioMinimo> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public Iterable<SalarioMinimo> findAll() {
		return repository.findAll();
	}

	public SalarioMinimo read(Long id) throws RecordNotFoundException {
		return findByIdOrThrowException(id);
	}

	public Optional<SalarioMinimo> findById(Long id) {
		return repository.findById(id);
	}

	public Page<SalarioMinimo> findLike(Pageable pageable, String like) throws RecordNotFoundException {
		Page<SalarioMinimo> retorno = repository.findLikePage(pageable, like);
		if (retorno.getTotalElements()==0) {
			throw new RecordNotFoundException("Valor não encontado");
		}
		return retorno;
	}

	private SalarioMinimo findByIdOrThrowException(Long id) throws RecordNotFoundException{
		return repository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Registro não encontrado com o id " + id));
	}
	public Optional<SalarioMinimo> findByData(Calendar data) {
		return repository.findByData(data);
	}

	public Iterable<SalarioMinimo> findByDataBetween(Calendar data1, Calendar data2) {
		return repository.findAllByDataLessThanEqualAndDataGreaterThanEqual(data2, data1);
	}

	public void updateMoedas() {
		Iterable<SalarioMinimo> listSalarioMinimo = repository.findByMoedaIsNull();
		for (SalarioMinimo salarioMinimo: listSalarioMinimo) {
			System.out.println(salarioMinimo.getData());
			try {
				salarioMinimo.setMoeda(ManipulaMoeda.getMoedaCorrente(salarioMinimo.getData()));
				update(salarioMinimo);
			} catch (DataInvalidaException e) {
				e.printStackTrace();
			} catch (RecordNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

}
