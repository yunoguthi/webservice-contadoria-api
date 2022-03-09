package br.jus.jfsp.nuit.contadoria.service;

import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import br.jus.jfsp.nuit.contadoria.models.AtualizacaoJudicial;
import br.jus.jfsp.nuit.contadoria.models.IndicesAtrasados;
import br.jus.jfsp.nuit.contadoria.models.MultiplicadorMoeda;
import br.jus.jfsp.nuit.contadoria.repository.IndicesAtrasadosRepository;
import br.jus.jfsp.nuit.contadoria.util.ManipulaArquivo;
import br.jus.jfsp.nuit.contadoria.util.ManipulaData;
import br.jus.jfsp.nuit.contadoria.util.ManipulaMath;
import br.jus.jfsp.nuit.contadoria.util.consts.Consts;
import lombok.val;
import org.modelmapper.internal.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Optional;
@Service
public class IndicesAtrasadosService {
	
	@Autowired
	private IndicesAtrasadosRepository repository;
	
	@Autowired
	private JsonReader jsonReader;

	@Autowired
	private UrlReaderService urlReader;

	@Autowired
	private AtualizacaoJudicialService atualizacaoJudicialService;

	@Autowired
	private MultiplicadorMoedaService multiplicadorMoedaService;

	@Autowired
	private AjusteMoedaService ajusteMoedaService;

	public void calculaAcumulados() throws RecordNotFoundException {
		ArrayList<IndicesAtrasados> listIndicesAtrasados = (ArrayList<IndicesAtrasados>) repository.findAll(Sort.by("data"));
		BigDecimal acumulado = new BigDecimal(1.0);
		for (int i = listIndicesAtrasados.size()-1; i>=0; i--) {
			IndicesAtrasados indicesAtrasados = listIndicesAtrasados.get(i);
			if (!Double.isInfinite(indicesAtrasados.getIndice().doubleValue()) && !indicesAtrasados.getIndice().equals(new BigDecimal(1.0))) {
				acumulado = acumulado.multiply(indicesAtrasados.getIndice());
				//System.out.println(ManipulaData.calendarToStringAnoMes(indicesAtrasados.getData()) + " - " + indicesAtrasados.getIndice() + " - " + acumulado);

				//acumulado = ManipulaMath.round(acumulado);

			}

//			acumulado = round(acumulado);

//			acumulado =  acumulado.multiply(new BigDecimal(ajusteMoedaService.findByData(indicesAtrasados.getData()).get().getValor()));
			acumulado = acumulado.setScale(12, BigDecimal.ROUND_HALF_UP);
			indicesAtrasados.setIndiceAtrasado(acumulado.multiply(new BigDecimal(ajusteMoedaService.findByData(indicesAtrasados.getData()).get().getValor())));
			update(indicesAtrasados);
		}
	}

//	public static BigDecimal round(BigDecimal value, int places) {
//		if (places < 0) throw new IllegalArgumentException();
//
//		BigDecimal bd = new BigDecimal(Double.toString(value));
//		bd = bd.setScale(places, RoundingMode.HALF_UP);
//		return bd.doubleValue();
//	}

//	public static double round(double value) {
//		return value;
//	}

	public void testeIndiceAtrasado(String[] coluna) {
		ArrayList<IndicesAtrasados> listIndicesAtrasados = (ArrayList<IndicesAtrasados>) repository.findAll(Sort.by("data"));
		BigDecimal erro = new BigDecimal(0.0);
		BigDecimal maiorErro = new BigDecimal(0.0);
		Calendar dataMaiorErro = null;

		for(int i=0; i<listIndicesAtrasados.size(); i++) {
			String valorFormatado = new DecimalFormat("#,##0.00000000000000").format(listIndicesAtrasados.get(i).getIndiceAtrasado());
			boolean igual = valorFormatado.equals(coluna[i]);
			String resultado = igual ? "OK" : valorFormatado + " - " + coluna[i];
			if (!igual) {
				if (listIndicesAtrasados.get(i).getIndiceAtrasado().compareTo(BigDecimal.valueOf(Double.valueOf(coluna[i].replaceAll(",", ".")))) > 0) {
					erro = listIndicesAtrasados.get(i).getIndiceAtrasado().subtract(BigDecimal.valueOf(Double.valueOf(coluna[i].replaceAll(",", "."))));
				} else {
					erro = BigDecimal.valueOf(Double.valueOf(coluna[i].replaceAll(",", "."))).subtract(listIndicesAtrasados.get(i).getIndiceAtrasado());
				}
				if (erro.compareTo(maiorErro) > 0) {
					maiorErro = erro;
					dataMaiorErro = listIndicesAtrasados.get(i).getData();
					//System.out.println(ManipulaData.calendarToStringAnoMes(dataMaiorErro) + " Maior erro: " + maiorErro);

				}
				//System.out.println(ManipulaData.calendarToStringAnoMes(listIndicesAtrasados.get(i).getData()) + " ; " + erro);

//				System.out.println(ManipulaData.calendarToStringAnoMes(listIndicesAtrasados.get(i).getData()) + " - " + "Erro: " + erro);
			}
			//System.out.println(ManipulaData.calendarToStringAnoMes(listIndicesAtrasados.get(i).getData()) + " - " + resultado);
		}
		//System.out.println(ManipulaData.calendarToStringAnoMes(dataMaiorErro) + " Maior erro: " + maiorErro);

	}

	public void testeIndice(String[] coluna) {
		ArrayList<IndicesAtrasados> listIndicesAtrasados = (ArrayList<IndicesAtrasados>) repository.findAll(Sort.by("data"));
		for(int i=0; i<listIndicesAtrasados.size(); i++) {
			String valorFormatado = new DecimalFormat("#,##0.00000000000000").format(listIndicesAtrasados.get(i).getIndice());
			boolean igual = valorFormatado.equals(coluna[i]);
			String resultado = igual ? "OK" : valorFormatado + " - " + coluna[i];
			System.out.println(ManipulaData.calendarToStringAnoMes(listIndicesAtrasados.get(i).getData()) + " - " + resultado);
		}
	}

	public void testando() {
		System.out.println("Início comparação de índices atrasados");
		String[] normalizadosIndicesAtrasados = ManipulaArquivo.normalizar(ManipulaArquivo.getColuna(11));
		testeIndiceAtrasado(normalizadosIndicesAtrasados);
		System.out.println("Fim comparação de índices atrasados");

		//		System.out.println("Início comparação de índices");
//		String[] normalizadosIndices = ManipulaArquivo.normalizar(ManipulaArquivo.getColuna(10));
//		testeIndice(normalizadosIndices);
//		System.out.println("Fim comparação de índices");
	}

	public void importa() {
		ArrayList<IndicesAtrasados> listIndicesAtrasados = new ArrayList<IndicesAtrasados>();
		Iterable<AtualizacaoJudicial> listAtualizacaoJudicial = atualizacaoJudicialService.getAll(Sort.by("data").descending());
//		Iterable<MultiplicadorMoeda> listMultiplicadorMoeda = multiplicadorMoedaService.getAll(Sort.by("data"));

		BigDecimal indiceAnterior = new BigDecimal(1.0);
		Double valorAnterior = new Double(0.0);

		for (AtualizacaoJudicial atualizacaoJudicial: listAtualizacaoJudicial) {
			Double percentual = atualizacaoJudicial.getPercentual();
			Double valor = atualizacaoJudicial.getValor();
			BigDecimal indice = new BigDecimal(0.0);
			if (percentual !=null && !percentual.equals(0.0)) {
				indice = new BigDecimal((percentual / 100) + 1);
			} else if (valor !=null && !valor.equals(0.0)) {
				//TODO verificar essa regra de negócio
				// indice = (valor + 1) / indiceAnterior;
				if (valorAnterior.equals(0.0)) {
					indice = new BigDecimal(1.0);
				} else {
					indice = new BigDecimal(valorAnterior / valor);
				}
			} else {
				indice = new BigDecimal(1.0);
			}
			//indice =  indice * multiplicadorMoedaService.findByData(atualizacaoJudicial.getData()).get().getValor();

//			String valorFormatado = new DecimalFormat("####0.00000000000000").format(indice);
//			indice = new Double(valorFormatado.replaceAll(",", "."));

			indice = indice.setScale(9, BigDecimal.ROUND_HALF_UP);

			indiceAnterior = indice;
			valorAnterior = valor;
			//System.out.println(ManipulaData.calendarToStringAnoMes(atualizacaoJudicial.getData()) + " - " + indiceAnterior);
			try {
				repository.save(new IndicesAtrasados(indice, atualizacaoJudicial.getDescricao(), atualizacaoJudicial.getData()));
			} catch (Exception e) {}
		}
	}

	public IndicesAtrasados create(IndicesAtrasados indicesAtrasados) {
		return repository.save(indicesAtrasados);
	}
	
	public IndicesAtrasados save(IndicesAtrasados indicesAtrasados) {
		return repository.save(indicesAtrasados);
	}
	
	public void delete(Long id) {
		repository.deleteById(id);
	}

	public IndicesAtrasados update(IndicesAtrasados indicesAtrasados) throws RecordNotFoundException {
		findByIdOrThrowException(indicesAtrasados.getId());
		return repository.save(indicesAtrasados);
	}

	public Iterable<IndicesAtrasados> getAll(){
		return repository.findAll();
	}

	public Page<IndicesAtrasados> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public IndicesAtrasados read(Long id) throws RecordNotFoundException {
		return findByIdOrThrowException(id);
	}

	public Optional<IndicesAtrasados> findById(Long id) {
		return repository.findById(id);
	}

	public Page<IndicesAtrasados> findLike(Pageable pageable, String like) throws RecordNotFoundException {
		Page<IndicesAtrasados> retorno = repository.findLikePage(pageable, like);
		if (retorno.getTotalElements()==0) {
			throw new RecordNotFoundException("Valor não encontado");
		}
		return retorno;
	}

	private IndicesAtrasados findByIdOrThrowException(Long id) throws RecordNotFoundException{
		return repository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Registro não encontrado com o id " + id));
	}

	public Optional<IndicesAtrasados> findByData(Calendar data) {
		return repository.findByData(data);
	}

	public Iterable<IndicesAtrasados> findByDataBetween(Calendar data1, Calendar data2) {
		return repository.findAllByDataLessThanEqualAndDataGreaterThanEqual(data2, data1);
	}



}
