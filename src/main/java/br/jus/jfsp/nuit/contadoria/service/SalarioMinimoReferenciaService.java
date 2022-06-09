package br.jus.jfsp.nuit.contadoria.service;

import br.jus.jfsp.nuit.contadoria.exception.DataInvalidaException;
import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import br.jus.jfsp.nuit.contadoria.models.SalarioMinimo;
import br.jus.jfsp.nuit.contadoria.models.SalarioMinimoReferencia;
import br.jus.jfsp.nuit.contadoria.repository.SalarioMinimoReferenciaRepository;
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
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;

@Service
public class SalarioMinimoReferenciaService {
	
	
	@Autowired
	private SalarioMinimoReferenciaRepository repository;
	
	@Autowired
	private JsonReader jsonReader;

	@Autowired
	private UrlReaderService urlReader;

	public void mostraCSV(String[] indice) {
		ArrayList<SalarioMinimoReferencia> list = (ArrayList<SalarioMinimoReferencia>) repository.findAll(Sort.by("data"));
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
			ManipulaArquivo.geraArquivo("teste_sm_ref.csv", csv);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void testando() {
		System.out.println("Início comparação de salario minimo ref");
		String[] normalizados = ManipulaArquivo.normalizar(ManipulaArquivo.getColuna(2));
		//teste(normalizados);
		mostraCSV(normalizados);
		System.out.println("Fim comparação de salario minimo ref");
	}

	public SalarioMinimoReferencia create(SalarioMinimoReferencia salarioMinimo) {
		return repository.save(salarioMinimo);
	}

	public SalarioMinimoReferencia save(SalarioMinimoReferencia salarioMinimo) {
		return repository.save(salarioMinimo);
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public SalarioMinimoReferencia update(SalarioMinimoReferencia salarioMinimo) throws RecordNotFoundException {
		findByIdOrThrowException(salarioMinimo.getId());
		return repository.save(salarioMinimo);
	}

	public Iterable<SalarioMinimoReferencia> getAll(){
		return repository.findAll();
	}

	public Iterable<SalarioMinimoReferencia> getAll(Sort sort){
		return repository.findAll(sort);
	}

	public Page<SalarioMinimoReferencia> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public Iterable<SalarioMinimoReferencia> findAll() {
		return repository.findAll();
	}

	public SalarioMinimoReferencia read(Long id) throws RecordNotFoundException {
		return findByIdOrThrowException(id);
	}

	public Optional<SalarioMinimoReferencia> findById(Long id) {
		return repository.findById(id);
	}

	public Page<SalarioMinimoReferencia> findLike(Pageable pageable, String like) throws RecordNotFoundException {
		Page<SalarioMinimoReferencia> retorno = repository.findLikePage(pageable, like);
		if (retorno.getTotalElements()==0) {
			throw new RecordNotFoundException("Valor não encontado");
		}
		return retorno;
	}

	private SalarioMinimoReferencia findByIdOrThrowException(Long id) throws RecordNotFoundException{
		return repository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Registro não encontrado com o id " + id));
	}
	public Optional<SalarioMinimoReferencia> findByData(Calendar data) {
		return repository.findByData(data);
	}

	public Iterable<SalarioMinimoReferencia> findByDataBetween(Calendar data1, Calendar data2) {
		return repository.findAllByDataLessThanEqualAndDataGreaterThanEqual(data2, data1);
	}

}
