package br.jus.jfsp.nuit.contadoria.service;

import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import br.jus.jfsp.nuit.contadoria.models.AtualizacaoJudicial;
import br.jus.jfsp.nuit.contadoria.models.AtualizacaoSalario;
import br.jus.jfsp.nuit.contadoria.models.IndicesAtrasados;
import br.jus.jfsp.nuit.contadoria.models.IndicesCond;
import br.jus.jfsp.nuit.contadoria.models.IndicesRes134;
import br.jus.jfsp.nuit.contadoria.models.IndicesCond;
import br.jus.jfsp.nuit.contadoria.models.IndicesSalarios;
import br.jus.jfsp.nuit.contadoria.models.IpcaE;
import br.jus.jfsp.nuit.contadoria.models.TrMensal;
import br.jus.jfsp.nuit.contadoria.models.Ufir;
import br.jus.jfsp.nuit.contadoria.repository.IndicesCondRepository;
import br.jus.jfsp.nuit.contadoria.util.ManipulaArquivo;
import br.jus.jfsp.nuit.contadoria.util.ManipulaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;

@Service
public class IndicesCondService {
	
	@Autowired
	private IndicesCondRepository repository;
	
	@Autowired
	private JsonReader jsonReader;

	@Autowired
	private UrlReaderService urlReader;

	@Autowired
	private IpcaEService ipcaEService;

	@Autowired
	private UfirService ufirService;

	@Autowired
	private AjusteMoedaService ajusteMoedaService;

	@Autowired
	private AtualizacaoJudicialService atualizacaoJudicialService;

	@Autowired
	private IndicesSalariosService indicesSalariosService;

	public void importa() {

		Calendar dezembro1991 = ManipulaData.getCalendar("1991-12-01", ManipulaData.ANO_MES_DIA);
		Calendar dezembro2000 = ManipulaData.getCalendar("2000-12-01", ManipulaData.ANO_MES_DIA);
		Calendar janeiro1992 = ManipulaData.getCalendar("1992-01-01", ManipulaData.ANO_MES_DIA);
		Calendar novembro2000 = ManipulaData.getCalendar("2000-11-01", ManipulaData.ANO_MES_DIA);

		Calendar janeiro2000 = ManipulaData.getCalendar("2000-11-01", ManipulaData.ANO_MES_DIA);
		Calendar fevereiro2000 = ManipulaData.getCalendar("2000-11-01", ManipulaData.ANO_MES_DIA);
		Calendar marco2000 = ManipulaData.getCalendar("2000-11-01", ManipulaData.ANO_MES_DIA);
		Calendar abril2000 = ManipulaData.getCalendar("2000-11-01", ManipulaData.ANO_MES_DIA);
		Calendar maio2000 = ManipulaData.getCalendar("2000-11-01", ManipulaData.ANO_MES_DIA);
		Calendar junho2000 = ManipulaData.getCalendar("2000-11-01", ManipulaData.ANO_MES_DIA);
		Calendar julho2000 = ManipulaData.getCalendar("2000-11-01", ManipulaData.ANO_MES_DIA);
		Calendar agosto2000 = ManipulaData.getCalendar("2000-11-01", ManipulaData.ANO_MES_DIA);
		Calendar setembro2000 = ManipulaData.getCalendar("2000-11-01", ManipulaData.ANO_MES_DIA);
		Calendar outubro2000 = ManipulaData.getCalendar("2000-11-01", ManipulaData.ANO_MES_DIA);


		// IPCA_E

		BigDecimal indiceAnterior = null;
		Iterable<IpcaE> listIpcaE = ipcaEService.getAll(Sort.by("data").descending());
		for (IpcaE ipcaE: listIpcaE) {
			BigDecimal indice = new BigDecimal(ipcaE.getValor()).divide(new BigDecimal(100.0));
			indice = indice.add(new BigDecimal(1.0));
			if (ipcaE.getData().compareTo(dezembro1991) == 0 ||
					ipcaE.getData().compareTo(dezembro2000) > 0) {
				try {
					if (indiceAnterior != null) {
						repository.save(new IndicesCond(indice, ipcaE.getObservacao(), ipcaE.getData()));
					}
				} catch (Exception e) {}
			}
			if (ipcaE.getData().compareTo(dezembro2000) == 0) {
				BigDecimal dezembro = new BigDecimal(1.06035557011);
				repository.save(new IndicesCond(dezembro, "", ipcaE.getData()));
			}
			indiceAnterior = indice;
		}

		// UFIR

//		Calendar janeiro1992 = ManipulaData.getCalendar("1992-01-01", ManipulaData.ANO_MES_DIA);
//		Calendar novembro2000 = ManipulaData.getCalendar("2000-11-01", ManipulaData.ANO_MES_DIA);

		Iterable<Ufir> listUfir = ufirService.getAll(Sort.by("data").descending());
		indiceAnterior = null;
		for (Ufir ufir: listUfir) {
			BigDecimal indice = indiceAnterior!=null ? indiceAnterior.divide(new BigDecimal(ufir.getValor()), 14, RoundingMode.HALF_UP) : new BigDecimal(1.0);
			System.out.println("JANEIRO/1992 " + ManipulaData.calendarToStringAnoMes(janeiro1992));
			System.out.println("NOVEMBRO/2000 " + ManipulaData.calendarToStringAnoMes(novembro2000));
			if (ufir.getData().compareTo(janeiro1992) >= 0 && ufir.getData().compareTo(novembro2000) <= 0) {
				System.out.println(ManipulaData.calendarToStringAnoMes(ufir.getData()));
				try {
					repository.save(new IndicesCond(indice, ufir.getObservacao(), ufir.getData()));
				} catch (Exception e) {}
			}
			indiceAnterior = new BigDecimal(ufir.getValor());
		}

		// Atualizacao Judicial

		Iterable<AtualizacaoJudicial> listAtualizacaoJudicial = atualizacaoJudicialService.getAll(Sort.by("data").descending());
		indiceAnterior = new BigDecimal(1.0);
		Double valorAnterior = new Double(0.0);

		for (AtualizacaoJudicial atualizacaoJudicial: listAtualizacaoJudicial) {
			Double percentual = atualizacaoJudicial.getPercentual();
			Double valor = atualizacaoJudicial.getValor();
			BigDecimal indice = new BigDecimal(atualizacaoJudicial.getValor());
			if (percentual !=null && !percentual.equals(0.0)) {
				indice = new BigDecimal((percentual / 100) + 1);
			} else if (valor !=null && !valor.equals(0.0)) {
				if (valorAnterior.equals(0.0)) {
					indice = new BigDecimal(1.0);
				} else {
					indice = new BigDecimal(valorAnterior / valor);
				}
			} else {
				indice = new BigDecimal(1.0);
			}
			indice = indice.setScale(10, BigDecimal.ROUND_HALF_UP);
			indiceAnterior = indice;
			valorAnterior = valor;
			if (atualizacaoJudicial.getData().compareTo(dezembro1991) < 0) {
				try {
					repository.save(new IndicesCond(indice, atualizacaoJudicial.getObservacao(), atualizacaoJudicial.getData()));
				} catch (Exception e) {}
			}
		}

	}

	public void calculaAcumulados() throws RecordNotFoundException {
		ArrayList<IndicesCond> listIndicesCond = (ArrayList<IndicesCond>) repository.findAll(Sort.by("data"));
		BigDecimal acumulado = new BigDecimal(1.0);
		for (int i = listIndicesCond.size()-1; i>=0; i--) {
			IndicesCond indicesCond = listIndicesCond.get(i);
			if (!Double.isInfinite(indicesCond.getIndice().doubleValue()) && !indicesCond.getIndice().equals(new BigDecimal(1.0))) {
				acumulado = acumulado.multiply(indicesCond.getIndice());
			}
			acumulado = acumulado.setScale(14, BigDecimal.ROUND_HALF_UP);
			indicesCond.setIndiceAtrasado(acumulado.multiply(new BigDecimal(ajusteMoedaService.findByData(indicesCond.getData()).get().getValor())).setScale(14, BigDecimal.ROUND_HALF_UP));
			update(indicesCond);
		}
	}

	public void testeIndiceAtrasado(String[] coluna) {
		ArrayList<IndicesCond> listIndicesCond = (ArrayList<IndicesCond>) repository.findAll(Sort.by("data"));
		BigDecimal erro = new BigDecimal(0.0);
		BigDecimal maiorErro = new BigDecimal(0.0);
		Calendar dataMaiorErro = null;

		for(int i=0; i<listIndicesCond.size(); i++) {
			String valorFormatado = new DecimalFormat("#,##0.00000000000000").format(listIndicesCond.get(i).getIndiceAtrasado());
			boolean igual = valorFormatado.equals(coluna[i]);
			String resultado = igual ? "OK" : valorFormatado + " - " + coluna[i];
			if (!igual) {
				if (listIndicesCond.get(i).getIndiceAtrasado().compareTo(BigDecimal.valueOf(Double.valueOf(coluna[i].replaceAll(",", ".")))) > 0) {
					erro = listIndicesCond.get(i).getIndiceAtrasado().subtract(BigDecimal.valueOf(Double.valueOf(coluna[i].replaceAll(",", "."))));
				} else {
					erro = BigDecimal.valueOf(Double.valueOf(coluna[i].replaceAll(",", "."))).subtract(listIndicesCond.get(i).getIndiceAtrasado());
				}
				if (erro.compareTo(maiorErro) > 0) {
					maiorErro = erro;
					dataMaiorErro = listIndicesCond.get(i).getData();
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
		System.out.println("indice " + indice[0]);
		System.out.println("indiceAcumulaod " + indiceAcumulado[0]);
		ArrayList<IndicesCond> listIndicesCond = (ArrayList<IndicesCond>) repository.findAll(Sort.by("data"));
		Calendar dataMaiorErro = null;
		String[] csv = new String[indice.length+1];
		csv[0] = "COMPETENCIA;INDICE_CALCULADO;INDICE_GOOGLE;ACUMULADO_CALCULADO;ACUMULADO_GOOGLE";
		for(int i=0; i<listIndicesCond.size(); i++) {
			String valorFormatado = new DecimalFormat("#,##0.00000000000000").format(listIndicesCond.get(i).getIndiceAtrasado());
			boolean igual = valorFormatado.equals(indice[i]);
			String resultado = igual ? "OK" : valorFormatado + " - " + indice[i];
			csv[i+1] = ManipulaData.dateToStringDiaMesAno(ManipulaData.toDate(listIndicesCond.get(i).getData())) + ";" +
					(listIndicesCond.get(i).getIndice() + ";").replaceAll("\\.", ",") +
					indice[i] + ";" +
					(listIndicesCond.get(i).getIndiceAtrasado() + ";").replaceAll("\\.", ",") +
					indiceAcumulado[i];
			//csv[i+1] = csv[i+1].replaceAll("\\.", ",");
//			System.out.println(csv[i+1]);
		}
		try {
			ManipulaArquivo.geraArquivo("teste_indices_cond.csv", csv);
		} catch (IOException e) {}
	}

	public void testeIndice(String[] coluna) {
		ArrayList<IndicesCond> listIndicesCond = (ArrayList<IndicesCond>) repository.findAll(Sort.by("data"));
		for(int i=0; i<listIndicesCond.size(); i++) {
			String valorFormatado = new DecimalFormat("#,##0.00000000000000").format(listIndicesCond.get(i).getIndice());
			boolean igual = valorFormatado.equals(coluna[i]);
			String resultado = igual ? "OK" : valorFormatado + " - " + coluna[i];
			//System.out.println(ManipulaData.calendarToStringAnoMes(listIndicesCond.get(i).getData()) + " - " + resultado);
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


		String[] normalizadosIndices = ManipulaArquivo.normalizar(ManipulaArquivo.getColuna(18));
		String[] normalizadosIndicesAcumulados = ManipulaArquivo.normalizar(ManipulaArquivo.getColuna(19));

//		System.out.println("Fim comparação de índices");

		mostraCSV(normalizadosIndices, normalizadosIndicesAcumulados);



	}

	public IndicesCond create(IndicesCond indicesRes134) {
		return repository.save(indicesRes134);
	}
	
	public IndicesCond save(IndicesCond indicesRes134) {
		return repository.save(indicesRes134);
	}
	
	public void delete(Long id) {
		repository.deleteById(id);
	}

	public IndicesCond update(IndicesCond indicesRes134) throws RecordNotFoundException {
		findByIdOrThrowException(indicesRes134.getId());
		return repository.save(indicesRes134);
	}

	public Iterable<IndicesCond> getAll(){
		return repository.findAll();
	}

	public Iterable<IndicesCond> getAll(Sort sort){
		return repository.findAll(sort);
	}


	public Page<IndicesCond> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public Iterable<IndicesCond> findAll() {
		return repository.findAll(Sort.by("data"));
	}

	public IndicesCond read(Long id) throws RecordNotFoundException {
		return findByIdOrThrowException(id);
	}

	public Optional<IndicesCond> findById(Long id) {
		return repository.findById(id);
	}

	public Page<IndicesCond> findLike(Pageable pageable, String like) throws RecordNotFoundException {
		Page<IndicesCond> retorno = repository.findLikePage(pageable, like);
		if (retorno.getTotalElements()==0) {
			throw new RecordNotFoundException("Valor não encontado");
		}
		return retorno;
	}

	private IndicesCond findByIdOrThrowException(Long id) throws RecordNotFoundException{
		return repository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Registro não encontrado com o id " + id));
	}

	public Optional<IndicesCond> findByData(Calendar data) {
		return repository.findByData(data);
	}

	public Iterable<IndicesCond> findByDataBetween(Calendar data1, Calendar data2) {
		return repository.findAllByDataLessThanEqualAndDataGreaterThanEqual(data2, data1);
	}



}
