package br.jus.jfsp.nuit.contadoria.service;

import br.jus.jfsp.nuit.contadoria.exception.DataInvalidaException;
import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import br.jus.jfsp.nuit.contadoria.models.TetoBeneficio;
import br.jus.jfsp.nuit.contadoria.repository.TetoBeneficioRepository;
import br.jus.jfsp.nuit.contadoria.util.ManipulaMoeda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

@Service
public class TetoBeneficioService {
	
	@Autowired
	private TetoBeneficioRepository repository;
	
	@Autowired
	private JsonReader jsonReader;

	@Autowired
	private UrlReaderService urlReader;

	public TetoBeneficio create(TetoBeneficio tetoBeneficio) {
		return repository.save(tetoBeneficio);
	}

	public TetoBeneficio save(TetoBeneficio tetoBeneficio) {
		return repository.save(tetoBeneficio);
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public TetoBeneficio update(TetoBeneficio tetoBeneficio) throws RecordNotFoundException {
		findByIdOrThrowException(tetoBeneficio.getId());
		return repository.save(tetoBeneficio);
	}

	public Iterable<TetoBeneficio> getAll(){
		return repository.findAll();
	}

	public Iterable<TetoBeneficio> getAll(Sort sort){
		return repository.findAll(sort);
	}

	public Page<TetoBeneficio> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public TetoBeneficio read(Long id) throws RecordNotFoundException {
		return findByIdOrThrowException(id);
	}

	public Optional<TetoBeneficio> findById(Long id) {
		return repository.findById(id);
	}

	public Page<TetoBeneficio> findLike(Pageable pageable, String like) throws RecordNotFoundException {
		Page<TetoBeneficio> retorno = repository.findLikePage(pageable, like);
		if (retorno.getTotalElements()==0) {
			throw new RecordNotFoundException("Valor não encontado");
		}
		return retorno;
	}

	private TetoBeneficio findByIdOrThrowException(Long id) throws RecordNotFoundException{
		return repository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Registro não encontrado com o id " + id));
	}
	public Optional<TetoBeneficio> findByData(Calendar data) {
		return repository.findByData(data);
	}

	public Iterable<TetoBeneficio> findByDataBetween(Calendar data1, Calendar data2) {
		return repository.findAllByDataLessThanEqualAndDataGreaterThanEqual(data2, data1);
	}

	public void updateMoedas() {
		Iterable<TetoBeneficio> listTetoBeneficio = repository.findByMoedaIsNull();
		for (TetoBeneficio tetoBeneficio: listTetoBeneficio) {
			System.out.println(tetoBeneficio.getData());
			try {
				tetoBeneficio.setMoeda(ManipulaMoeda.getMoedaCorrente(tetoBeneficio.getData()));
				update(tetoBeneficio);
			} catch (DataInvalidaException e) {
				e.printStackTrace();
			} catch (RecordNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

}
