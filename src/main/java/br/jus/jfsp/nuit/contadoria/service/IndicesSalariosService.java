package br.jus.jfsp.nuit.contadoria.service;

import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import br.jus.jfsp.nuit.contadoria.models.AtualizacaoSalario;
import br.jus.jfsp.nuit.contadoria.models.IndicesAtrasados;
import br.jus.jfsp.nuit.contadoria.models.IndicesRes134;
import br.jus.jfsp.nuit.contadoria.models.IndicesSalarios;
import br.jus.jfsp.nuit.contadoria.models.TrMensal;
import br.jus.jfsp.nuit.contadoria.repository.IndicesSalariosRepository;
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
import java.util.Optional;

@Service
public class IndicesSalariosService {
	
	@Autowired
	private IndicesSalariosRepository repository;
	
	@Autowired
	private JsonReader jsonReader;

	@Autowired
	private UrlReaderService urlReader;

	@Autowired
	private AtualizacaoSalarioService atualizacaoSalarioService;

	@Autowired
	private AjusteMoedaService ajusteMoedaService;

	public void importa() {
		Iterable<AtualizacaoSalario> listAtualizacaoSalario = atualizacaoSalarioService.getAll(Sort.by("data").descending());
		BigDecimal indiceAnterior = new BigDecimal(1.0);
		Double valorAnterior = new Double(0.0);
		for (AtualizacaoSalario atualizacaoSalario: listAtualizacaoSalario) {
			Double percentual = atualizacaoSalario.getPercentual();
			Double valor = atualizacaoSalario.getValor();
			BigDecimal indice = new BigDecimal(1.0);
			if (percentual == null && valor == null) {
				indice = new BigDecimal(0.0);

			} else if (percentual !=null && !percentual.equals(0.0)) {
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
			try {
				IndicesSalarios indiceSalarios = new IndicesSalarios();
				if (repository.existsByData(atualizacaoSalario.getData())) {
					indiceSalarios = repository.findByData(atualizacaoSalario.getData()).get();
				} else {
					indiceSalarios.setData(atualizacaoSalario.getData());
				}
				indiceSalarios.setIndice(indice);
				indiceSalarios.setObservacao(atualizacaoSalario.getObservacao());
				repository.save(indiceSalarios);
			} catch (Exception e) {}
		}
	}

	public void calculaAcumulados() throws RecordNotFoundException {
		ArrayList<IndicesSalarios> listIndicesSalarios = (ArrayList<IndicesSalarios>) repository.findAll(Sort.by("data"));
		BigDecimal acumulado = new BigDecimal(1.0);
		for (int i = listIndicesSalarios.size()-1; i>=0; i--) {
			IndicesSalarios indicesSalarios = listIndicesSalarios.get(i);
			if (!Double.isInfinite(indicesSalarios.getIndice().doubleValue()) && indicesSalarios.getIndice().abs().compareTo(new BigDecimal(0.0))!=0) {
				acumulado = acumulado.multiply(indicesSalarios.getIndice());
			}
			acumulado = acumulado.setScale(14, BigDecimal.ROUND_HALF_UP);
			indicesSalarios.setIndiceAtrasado(acumulado.multiply(new BigDecimal(ajusteMoedaService.findByData(indicesSalarios.getData()).get().getValor())).setScale(14, BigDecimal.ROUND_HALF_UP));
			update(indicesSalarios);
		}
	}

	public void testeIndiceAtrasado(String[] coluna) {
		ArrayList<IndicesSalarios> listIndicesSalarios = (ArrayList<IndicesSalarios>) repository.findAll(Sort.by("data"));
		BigDecimal erro = new BigDecimal(0.0);
		BigDecimal maiorErro = new BigDecimal(0.0);
		Calendar dataMaiorErro = null;

		for(int i=0; i<listIndicesSalarios.size(); i++) {
			String valorFormatado = new DecimalFormat("#,##0.00000000000000").format(listIndicesSalarios.get(i).getIndiceAtrasado());
			boolean igual = valorFormatado.equals(coluna[i]);
			String resultado = igual ? "OK" : valorFormatado + " - " + coluna[i];
			if (!igual) {
				if (listIndicesSalarios.get(i).getIndiceAtrasado().compareTo(BigDecimal.valueOf(Double.valueOf(coluna[i].replaceAll(",", ".")))) > 0) {
					erro = listIndicesSalarios.get(i).getIndiceAtrasado().subtract(BigDecimal.valueOf(Double.valueOf(coluna[i].replaceAll(",", "."))));
				} else {
					erro = BigDecimal.valueOf(Double.valueOf(coluna[i].replaceAll(",", "."))).subtract(listIndicesSalarios.get(i).getIndiceAtrasado());
				}
				if (erro.compareTo(maiorErro) > 0) {
					maiorErro = erro;
					dataMaiorErro = listIndicesSalarios.get(i).getData();
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
		ArrayList<IndicesSalarios> listIndicesSalarios = (ArrayList<IndicesSalarios>) repository.findAll(Sort.by("data"));
		Calendar dataMaiorErro = null;
		String[] csv = new String[indice.length+1];
		csv[0] = "COMPETENCIA;INDICE_CALCULADO;INDICE_GOOGLE;ACUMULADO_CALCULADO;ACUMULADO_GOOGLE";
		for(int i=0; i<listIndicesSalarios.size(); i++) {
			String valorFormatado = new DecimalFormat("#,##0.00000000000000").format(listIndicesSalarios.get(i).getIndiceAtrasado());
			boolean igual = valorFormatado.equals(indice[i]);
			String resultado = igual ? "OK" : valorFormatado + " - " + indice[i];
			csv[i+1] = ManipulaData.dateToStringDiaMesAno(ManipulaData.toDate(listIndicesSalarios.get(i).getData())) + ";" +
					(listIndicesSalarios.get(i).getIndice() + ";").replaceAll("\\.", ",") +
					indice[i] + ";" +
					(listIndicesSalarios.get(i).getIndiceAtrasado() + ";").replaceAll("\\.", ",") +
					indiceAcumulado[i];
			//csv[i+1] = csv[i+1].replaceAll("\\.", ",");
			System.out.println(csv[i+1]);
		}
		try {
			ManipulaArquivo.geraArquivo("teste_indices_salarios.csv", csv);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void testeIndice(String[] coluna) {
		ArrayList<IndicesSalarios> listIndicesSalarios = (ArrayList<IndicesSalarios>) repository.findAll(Sort.by("data"));
		for(int i=0; i<listIndicesSalarios.size(); i++) {
			String valorFormatado = new DecimalFormat("#,##0.00000000000000").format(listIndicesSalarios.get(i).getIndice());
			boolean igual = valorFormatado.equals(coluna[i]);
			String resultado = igual ? "OK" : valorFormatado + " - " + coluna[i];
			System.out.println(ManipulaData.calendarToStringAnoMes(listIndicesSalarios.get(i).getData()) + " - " + resultado);
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


		String[] normalizadosIndices = ManipulaArquivo.normalizar(ManipulaArquivo.getColuna(14));
		String[] normalizadosIndicesAcumulados = ManipulaArquivo.normalizar(ManipulaArquivo.getColuna(15));

//		System.out.println("Fim comparação de índices");

		mostraCSV(normalizadosIndices, normalizadosIndicesAcumulados);



	}

	public IndicesSalarios create(IndicesSalarios indicesRes134) {
		return repository.save(indicesRes134);
	}
	
	public IndicesSalarios save(IndicesSalarios indicesRes134) {
		return repository.save(indicesRes134);
	}
	
	public void delete(Long id) {
		repository.deleteById(id);
	}

	public IndicesSalarios update(IndicesSalarios indicesRes134) throws RecordNotFoundException {
		findByIdOrThrowException(indicesRes134.getId());
		return repository.save(indicesRes134);
	}

	public Iterable<IndicesSalarios> getAll(){
		return repository.findAll();
	}

	public Iterable<IndicesSalarios> getAll(Sort sort){
		return repository.findAll(sort);
	}

	public Page<IndicesSalarios> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public Iterable<IndicesSalarios> findAll() {
		return repository.findAll(Sort.by("data"));
	}

	public IndicesSalarios read(Long id) throws RecordNotFoundException {
		return findByIdOrThrowException(id);
	}

	public Optional<IndicesSalarios> findById(Long id) {
		return repository.findById(id);
	}

	public Page<IndicesSalarios> findLike(Pageable pageable, String like) throws RecordNotFoundException {
		Page<IndicesSalarios> retorno = repository.findLikePage(pageable, like);
		if (retorno.getTotalElements()==0) {
			throw new RecordNotFoundException("Valor não encontado");
		}
		return retorno;
	}

	private IndicesSalarios findByIdOrThrowException(Long id) throws RecordNotFoundException{
		return repository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Registro não encontrado com o id " + id));
	}

	public Optional<IndicesSalarios> findByData(Calendar data) {
		return repository.findByData(data);
	}

	public Iterable<IndicesSalarios> findByDataBetween(Calendar data1, Calendar data2) {
		return repository.findAllByDataLessThanEqualAndDataGreaterThanEqual(data2, data1);
	}



}
