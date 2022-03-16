package br.jus.jfsp.nuit.contadoria.service;

import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import br.jus.jfsp.nuit.contadoria.models.AtualizacaoJudicial;
import br.jus.jfsp.nuit.contadoria.models.IndicesAtrasados;
import br.jus.jfsp.nuit.contadoria.models.IndicesRes134;
import br.jus.jfsp.nuit.contadoria.models.IndicesRes134;
import br.jus.jfsp.nuit.contadoria.models.TrMensal;
import br.jus.jfsp.nuit.contadoria.repository.IndicesRes134Repository;
import br.jus.jfsp.nuit.contadoria.util.ManipulaArquivo;
import br.jus.jfsp.nuit.contadoria.util.ManipulaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
public class IndicesRes134Service {
	
	@Autowired
	private IndicesRes134Repository repository;
	
	@Autowired
	private JsonReader jsonReader;

	@Autowired
	private UrlReaderService urlReader;

	@Autowired
	private IndicesAtrasadosService indicesAtrasadosService;

	@Autowired
	private TrMensalService trMensalService;

	@Autowired
	private AjusteMoedaService ajusteMoedaService;

	public void importa() {
		// Importação dos índices anteriores a julho de 2009, vindos da índices atrasados
		Iterable<IndicesAtrasados> listIndicesAtrasados = indicesAtrasadosService.getAll();
		for (IndicesAtrasados indicesAtrasados: listIndicesAtrasados) {
			BigDecimal indice = indicesAtrasados.getIndice();
			Calendar julho2009 = ManipulaData.getCalendar("2009-07-01", ManipulaData.ANO_MES_DIA);
			if (indicesAtrasados.getData().compareTo(julho2009) == -1) {
				try {
					repository.save(new IndicesRes134(indice, indicesAtrasados.getDescricao(), indicesAtrasados.getData()));
				} catch (Exception e) {}
			}
		}

		// Importação dos índices posteriores a julho de 2009, vindos da TR

		Iterable<TrMensal> listTrMensal = trMensalService.getAll();
		for (TrMensal trMensal: listTrMensal) {
			BigDecimal indice = new BigDecimal(trMensal.getValor()).divide(new BigDecimal(100.0));
			Calendar julho2009 = ManipulaData.getCalendar("2009-07-01", ManipulaData.ANO_MES_DIA);
			if (trMensal.getData().compareTo(julho2009) != -1) {
				try {
					repository.save(new IndicesRes134(indice.add(new BigDecimal(1.0)), trMensal.getObservacao(), trMensal.getData()));
				} catch (Exception e) {}
			}
		}
	}

	public void calculaAcumulados() throws RecordNotFoundException {
		ArrayList<IndicesRes134> listIndicesRes134 = (ArrayList<IndicesRes134>) repository.findAll(Sort.by("data"));
		BigDecimal acumulado = new BigDecimal(1.0);
		for (int i = listIndicesRes134.size()-1; i>=0; i--) {
			IndicesRes134 indicesRes134 = listIndicesRes134.get(i);
			if (!Double.isInfinite(indicesRes134.getIndice().doubleValue()) && !indicesRes134.getIndice().equals(new BigDecimal(1.0))) {
				acumulado = acumulado.multiply(indicesRes134.getIndice());
			}
			acumulado = acumulado.setScale(14, BigDecimal.ROUND_HALF_UP);
			indicesRes134.setIndiceAtrasado(acumulado.multiply(new BigDecimal(ajusteMoedaService.findByData(indicesRes134.getData()).get().getValor())).setScale(14, BigDecimal.ROUND_HALF_UP));
			update(indicesRes134);
		}
	}

	public void testeIndiceAtrasado(String[] coluna) {
		ArrayList<IndicesRes134> listIndicesRes134 = (ArrayList<IndicesRes134>) repository.findAll(Sort.by("data"));
		BigDecimal erro = new BigDecimal(0.0);
		BigDecimal maiorErro = new BigDecimal(0.0);
		Calendar dataMaiorErro = null;

		for(int i=0; i<listIndicesRes134.size(); i++) {
			String valorFormatado = new DecimalFormat("#,##0.00000000000000").format(listIndicesRes134.get(i).getIndiceAtrasado());
			boolean igual = valorFormatado.equals(coluna[i]);
			String resultado = igual ? "OK" : valorFormatado + " - " + coluna[i];
			if (!igual) {
				if (listIndicesRes134.get(i).getIndiceAtrasado().compareTo(BigDecimal.valueOf(Double.valueOf(coluna[i].replaceAll(",", ".")))) > 0) {
					erro = listIndicesRes134.get(i).getIndiceAtrasado().subtract(BigDecimal.valueOf(Double.valueOf(coluna[i].replaceAll(",", "."))));
				} else {
					erro = BigDecimal.valueOf(Double.valueOf(coluna[i].replaceAll(",", "."))).subtract(listIndicesRes134.get(i).getIndiceAtrasado());
				}
				if (erro.compareTo(maiorErro) > 0) {
					maiorErro = erro;
					dataMaiorErro = listIndicesRes134.get(i).getData();
					//System.out.println(ManipulaData.calendarToStringAnoMes(dataMaiorErro) + " Maior erro: " + maiorErro);

				}
				//System.out.println(ManipulaData.calendarToStringAnoMes(listIndicesAtrasados.get(i).getData()) + " ; " + erro);

//				System.out.println(ManipulaData.calendarToStringAnoMes(listIndicesAtrasados.get(i).getData()) + " - " + "Erro: " + erro);
			}
			//System.out.println(ManipulaData.calendarToStringAnoMes(listIndicesAtrasados.get(i).getData()) + " - " + resultado);
		}
		//System.out.println(ManipulaData.calendarToStringAnoMes(dataMaiorErro) + " Maior erro: " + maiorErro);

	}

	public void mostraCSV(String[] indice, String[] indiceAcumulado) {
		ArrayList<IndicesRes134> listIndicesRes134 = (ArrayList<IndicesRes134>) repository.findAll(Sort.by("data"));
		Calendar dataMaiorErro = null;
		String[] csv = new String[indice.length+1];
		csv[0] = "COMPETENCIA;INDICE_CALCULADO;INDICE_GOOGLE;ACUMULADO_CALCULADO;ACUMULADO_GOOGLE";
		for(int i=0; i<listIndicesRes134.size(); i++) {
			String valorFormatado = new DecimalFormat("#,##0.00000000000000").format(listIndicesRes134.get(i).getIndiceAtrasado());
			boolean igual = false;
			try {
				igual = valorFormatado.equals(indice[i]);
			} catch (java.lang.ArrayIndexOutOfBoundsException e) {
				break;
			}
			String resultado = igual ? "OK" : valorFormatado + " - " + indice[i];
			csv[i+1] = ManipulaData.dateToStringDiaMesAno(ManipulaData.toDate(listIndicesRes134.get(i).getData())) + ";" +
					(listIndicesRes134.get(i).getIndice() + ";").replaceAll("\\.", ",") +
					indice[i] + ";" +
					(listIndicesRes134.get(i).getIndiceAtrasado() + ";").replaceAll("\\.", ",") +
					indiceAcumulado[i];
			//csv[i+1] = csv[i+1].replaceAll("\\.", ",");
			System.out.println(csv[i+1]);
		}
		try {
			ManipulaArquivo.geraArquivo("teste_indices_res134.csv", csv);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void testeIndice(String[] coluna) {
		ArrayList<IndicesRes134> listIndicesRes134 = (ArrayList<IndicesRes134>) repository.findAll(Sort.by("data"));
		for(int i=0; i<listIndicesRes134.size(); i++) {
			String valorFormatado = new DecimalFormat("#,##0.00000000000000").format(listIndicesRes134.get(i).getIndice());
			boolean igual = valorFormatado.equals(coluna[i]);
			String resultado = igual ? "OK" : valorFormatado + " - " + coluna[i];
			System.out.println(ManipulaData.calendarToStringAnoMes(listIndicesRes134.get(i).getData()) + " - " + resultado);
		}
	}

	public void testando() {


//		System.out.println("Início comparação de índices atrasados");
//		String[] normalizadosIndicesAtrasados = ManipulaArquivo.normalizar(ManipulaArquivo.getColuna(11));
//		testeIndiceAtrasado(normalizadosIndicesAtrasados);
//		System.out.println("Fim comparação de índices atrasados");

		//		System.out.println("Início comparação de índices");
//		String[] normalizadosIndices = ManipulaArquivo.normalizar(ManipulaArquivo.getColuna(10));
//		testeIndice(normalizadosIndices);


		String[] normalizadosIndices = ManipulaArquivo.normalizar(ManipulaArquivo.getColuna(12));
		String[] normalizadosIndicesAcumulados = ManipulaArquivo.normalizar(ManipulaArquivo.getColuna(13));

//		System.out.println("Fim comparação de índices");

		mostraCSV(normalizadosIndices, normalizadosIndicesAcumulados);



	}

	public IndicesRes134 create(IndicesRes134 indicesRes134) {
		return repository.save(indicesRes134);
	}
	
	public IndicesRes134 save(IndicesRes134 indicesRes134) {
		return repository.save(indicesRes134);
	}
	
	public void delete(Long id) {
		repository.deleteById(id);
	}

	public IndicesRes134 update(IndicesRes134 indicesRes134) throws RecordNotFoundException {
		findByIdOrThrowException(indicesRes134.getId());
		return repository.save(indicesRes134);
	}

	public Iterable<IndicesRes134> getAll(){
		return repository.findAll();
	}

	public Page<IndicesRes134> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public IndicesRes134 read(Long id) throws RecordNotFoundException {
		return findByIdOrThrowException(id);
	}

	public Optional<IndicesRes134> findById(Long id) {
		return repository.findById(id);
	}

	public Page<IndicesRes134> findLike(Pageable pageable, String like) throws RecordNotFoundException {
		Page<IndicesRes134> retorno = repository.findLikePage(pageable, like);
		if (retorno.getTotalElements()==0) {
			throw new RecordNotFoundException("Valor não encontado");
		}
		return retorno;
	}

	private IndicesRes134 findByIdOrThrowException(Long id) throws RecordNotFoundException{
		return repository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Registro não encontrado com o id " + id));
	}

	public Optional<IndicesRes134> findByData(Calendar data) {
		return repository.findByData(data);
	}

	public Iterable<IndicesRes134> findByDataBetween(Calendar data1, Calendar data2) {
		return repository.findAllByDataLessThanEqualAndDataGreaterThanEqual(data2, data1);
	}



}
