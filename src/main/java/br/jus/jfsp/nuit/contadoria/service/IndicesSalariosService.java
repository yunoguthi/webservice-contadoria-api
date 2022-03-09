package br.jus.jfsp.nuit.contadoria.service;

import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import br.jus.jfsp.nuit.contadoria.models.AtualizacaoSalario;
import br.jus.jfsp.nuit.contadoria.models.IndicesAtrasados;
import br.jus.jfsp.nuit.contadoria.models.IndicesRes134;
import br.jus.jfsp.nuit.contadoria.models.IndicesSalarios;
import br.jus.jfsp.nuit.contadoria.models.TrMensal;
import br.jus.jfsp.nuit.contadoria.repository.IndicesSalariosRepository;
import br.jus.jfsp.nuit.contadoria.util.ManipulaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

	public void importa() {
		Iterable<AtualizacaoSalario> listAtualizacaoSalario = atualizacaoSalarioService.getAll();
		for (AtualizacaoSalario atualizacaoSalario: listAtualizacaoSalario) {
			BigDecimal indice = new BigDecimal(atualizacaoSalario.getValor());
			if (indice==null || indice.equals(new Double(0.0))) {
				indice = new BigDecimal(atualizacaoSalario.getPercentual());
			}
			if (indice==null || indice.equals(new Double(0.0))) {
				indice = new BigDecimal(1.0);
			}
			try {
				repository.save(new IndicesSalarios(indice, atualizacaoSalario.getObservacao(), atualizacaoSalario.getData()));
			} catch (Exception e) {}
		}
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

	public Page<IndicesSalarios> findAll(Pageable pageable) {
		return repository.findAll(pageable);
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
