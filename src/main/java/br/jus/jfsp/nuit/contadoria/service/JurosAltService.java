package br.jus.jfsp.nuit.contadoria.service;

import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import br.jus.jfsp.nuit.contadoria.models.BtnMensal;
import br.jus.jfsp.nuit.contadoria.models.JurosAlt;
import br.jus.jfsp.nuit.contadoria.models.SelicMetaCopom;
import br.jus.jfsp.nuit.contadoria.repository.JurosAltRepository;
import br.jus.jfsp.nuit.contadoria.util.ManipulaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Optional;

@Service
public class JurosAltService extends SgsBacenService {
	
	@Autowired
	private JurosAltRepository repository;
	
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
			BigDecimal indice = new BigDecimal(0.0);
			Calendar dezembro2002 = ManipulaData.getCalendar("2002-12-01", ManipulaData.ANO_MES_DIA);
			Calendar janeiro2003 = ManipulaData.getCalendar("2003-01-01", ManipulaData.ANO_MES_DIA);
			Calendar junho2009 = ManipulaData.getCalendar("2009-06-01", ManipulaData.ANO_MES_DIA);
			Calendar maio2012 = ManipulaData.getCalendar("2012-05-01", ManipulaData.ANO_MES_DIA);

			if (btnMensal.getData().compareTo(dezembro2002) <= 0) {
				indice = new BigDecimal(0.005);
				try {
					repository.save(new JurosAlt(indice, btnMensal.getData()));
				} catch (Exception e) {}
			}
			if (btnMensal.getData().compareTo(janeiro2003) >= 0 && btnMensal.getData().compareTo(junho2009) <= 0) {
				indice = new BigDecimal(0.01);
				try {
					repository.save(new JurosAlt(indice, btnMensal.getData()));
				} catch (Exception e) {}
			}
			if (btnMensal.getData().compareTo(junho2009) > 0 && btnMensal.getData().compareTo(maio2012) <= 0) {
				indice = new BigDecimal(0.005);
				try {
					repository.save(new JurosAlt(indice, btnMensal.getData()));
				} catch (Exception e) {}
			}
			if (btnMensal.getData().compareTo(maio2012) > 0) {
				SelicMetaCopom selicMetaCopom = selicMetaCopomService.findByData(btnMensal.getData()).get();
				if (selicMetaCopom.getValor().compareTo(new Double(0.085)) > 0) {
					indice = new BigDecimal(0.005);
				} else {
					indice = new BigDecimal(0.7).multiply(new BigDecimal(selicMetaCopom.getValor()));
				}
				try {
					repository.save(new JurosAlt(indice, btnMensal.getData()));
				} catch (Exception e) {}
			}
		}

			
	}

	public JurosAlt create(JurosAlt juros) {
		return repository.save(juros);
	}
	
	public JurosAlt save(JurosAlt juros) {
		return repository.save(juros);
	}
	
	public void delete(Long id) {
		repository.deleteById(id);
	}

	public JurosAlt update(JurosAlt juros) throws RecordNotFoundException {
		findByIdOrThrowException(juros.getId());
		return repository.save(juros);
	}

	public Iterable<JurosAlt> getAll(){
		return repository.findAll();
	}

	public Page<JurosAlt> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public JurosAlt read(Long id) throws RecordNotFoundException {
		return findByIdOrThrowException(id);
	}

	public Optional<JurosAlt> findById(Long id) {
		return repository.findById(id);
	}

	public Page<JurosAlt> findLike(Pageable pageable, String like) throws RecordNotFoundException {
		Page<JurosAlt> retorno = repository.findLikePage(pageable, like);
		if (retorno.getTotalElements()==0) {
			throw new RecordNotFoundException("Valor não encontado");
		}
		return retorno;
	}

	private JurosAlt findByIdOrThrowException(Long id) throws RecordNotFoundException{
		return repository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Registro não encontrado com o id " + id));
	}

	public Optional<JurosAlt> findByData(Calendar data) {
		return repository.findByData(data);
	}

	public Iterable<JurosAlt> findByDataBetween(Calendar data1, Calendar data2) {
		return repository.findAllByDataLessThanEqualAndDataGreaterThanEqual(data2, data1);
	}



}
