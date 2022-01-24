package br.jus.jfsp.nuit.contadoria.service;

import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import br.jus.jfsp.nuit.contadoria.models.BtnMensal;
import br.jus.jfsp.nuit.contadoria.models.IndicesAtrasados;
import br.jus.jfsp.nuit.contadoria.models.IndicesCond;
import br.jus.jfsp.nuit.contadoria.models.IndicesRes134;
import br.jus.jfsp.nuit.contadoria.models.Juros;
import br.jus.jfsp.nuit.contadoria.models.SelicMetaCopom;
import br.jus.jfsp.nuit.contadoria.models.TrMensal;
import br.jus.jfsp.nuit.contadoria.repository.JurosRepository;
import br.jus.jfsp.nuit.contadoria.util.ManipulaData;
import br.jus.jfsp.nuit.contadoria.util.consts.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
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
	private BtnMensalService btnMensalService;

	@Autowired
	private SelicMetaCopomService selicMetaCopomService;

	public void importa() {

		Iterable<BtnMensal> listBtnMensal = btnMensalService.getAll();
		for (BtnMensal btnMensal: listBtnMensal) {
			Double indice = new Double(0.0);
			Calendar junho2009 = ManipulaData.getCalendar("2009-06-01", ManipulaData.ANO_MES_DIA);
			Calendar julho2009 = ManipulaData.getCalendar("2009-07-01", ManipulaData.ANO_MES_DIA);
			Calendar maio2012 = ManipulaData.getCalendar("2012-05-01", ManipulaData.ANO_MES_DIA);
			Calendar junho2012 = ManipulaData.getCalendar("2012-06-01", ManipulaData.ANO_MES_DIA);

			if (btnMensal.getData().compareTo(junho2009) <= 0) {
				indice = new Double(0.01);
				try {
					repository.save(new Juros(indice, btnMensal.getData()));
				} catch (Exception e) {}
			}
			if (btnMensal.getData().compareTo(junho2009) > 0 && btnMensal.getData().compareTo(maio2012) <= 0) {
				indice = new Double(0.005);
				try {
					repository.save(new Juros(indice, btnMensal.getData()));
				} catch (Exception e) {}
			}
			if (btnMensal.getData().compareTo(maio2012) > 0) {
				SelicMetaCopom selicMetaCopom = selicMetaCopomService.findByData(btnMensal.getData()).get();
				if (selicMetaCopom.getValor().compareTo(new Double(0.085)) > 0) {
					indice = new Double(0.005);
				} else {
					indice = selicMetaCopom.getValor() * 0.7;
				}
				try {
					repository.save(new Juros(indice, btnMensal.getData()));
				} catch (Exception e) {}
			}
		}

			
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

	public Page<Juros> findAll(Pageable pageable) {
		return repository.findAll(pageable);
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
