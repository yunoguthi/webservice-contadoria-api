package br.jus.jfsp.nuit.contadoria.service;

import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import br.jus.jfsp.nuit.contadoria.models.AtualizacaoJudicial;
import br.jus.jfsp.nuit.contadoria.models.Juros;
import br.jus.jfsp.nuit.contadoria.models.JurosAlt;
import br.jus.jfsp.nuit.contadoria.models.SelicMetaCopom;
import br.jus.jfsp.nuit.contadoria.repository.JurosAltRepository;
import br.jus.jfsp.nuit.contadoria.util.ManipulaData;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
	private AtualizacaoJudicialService atualizacaoJudicialService;

	@Autowired
	private SelicMetaCopomService selicMetaCopomService;

	public void importa() {

//		Iterable<AtualizacaoJudicial> listAtualizacaoJudicial = atualizacaoJudicialService.getAll();
//		for (AtualizacaoJudicial atualizacaoJudicial: listAtualizacaoJudicial) {
//			Double valor = new Double(0.0);
//			Calendar dezembro2002 = ManipulaData.getCalendar("2002-12-01", ManipulaData.ANO_MES_DIA);
//			Calendar janeiro2003 = ManipulaData.getCalendar("2003-01-01", ManipulaData.ANO_MES_DIA);
//			Calendar junho2009 = ManipulaData.getCalendar("2009-06-01", ManipulaData.ANO_MES_DIA);
//			Calendar maio2012 = ManipulaData.getCalendar("2012-05-01", ManipulaData.ANO_MES_DIA);
//
//			if (atualizacaoJudicial.getData().compareTo(dezembro2002) <= 0) {
//				valor = new Double(0.005);
//				try {
//					repository.save(new JurosAlt(valor, atualizacaoJudicial.getData()));
//				} catch (Exception e) {}
//			}
//			if (atualizacaoJudicial.getData().compareTo(janeiro2003) >= 0 && atualizacaoJudicial.getData().compareTo(junho2009) <= 0) {
//				valor = new Double(0.01);
//				try {
//					repository.save(new JurosAlt(valor, atualizacaoJudicial.getData()));
//				} catch (Exception e) {}
//			}
//			if (atualizacaoJudicial.getData().compareTo(junho2009) > 0 && atualizacaoJudicial.getData().compareTo(maio2012) <= 0) {
//				valor = new Double(0.005);
//				try {
//					repository.save(new JurosAlt(valor, atualizacaoJudicial.getData()));
//				} catch (Exception e) {}
//			}
//			if (atualizacaoJudicial.getData().compareTo(maio2012) > 0) {
//				SelicMetaCopom selicMetaCopom = selicMetaCopomService.findByData(atualizacaoJudicial.getData()).get();
//				if (selicMetaCopom.getValor().compareTo(new Double(0.085)) > 0) {
//					valor = new Double(0.005);
//				} else {
//					valor = selicMetaCopom.getValor() * 0.7;
//				}
//				try {
//					repository.save(new JurosAlt(valor, atualizacaoJudicial.getData()));
//				} catch (Exception e) {}
//			}
//		}
		try {
			Iterable<AtualizacaoJudicial> listAtualizacaoJudicial = atualizacaoJudicialService.getAll(Sort.by("data").descending());
			Double anterior = 0.0;
			for (AtualizacaoJudicial atualizacaoJudicial: listAtualizacaoJudicial) {
				Double valor = new Double(0.0);
				//Calendar junho2009 = ManipulaData.getCalendar("2009-06-01", ManipulaData.ANO_MES_DIA);
				//Calendar julho2009 = ManipulaData.getCalendar("2009-07-01", ManipulaData.ANO_MES_DIA);
				//Calendar maio2012 = ManipulaData.getCalendar("2012-05-01", ManipulaData.ANO_MES_DIA);
				//Calendar junho2012 = ManipulaData.getCalendar("2012-06-01", ManipulaData.ANO_MES_DIA);
				Calendar janeiro2002 = ManipulaData.getCalendar("2002-01-01", ManipulaData.ANO_MES_DIA);

				Double raiz = 1.0 / 12.0;

				if (atualizacaoJudicial.getData().compareTo(janeiro2002) <= 0) {
					valor = new Double(0.005);
					try {
						JurosAlt jurosAlt = new JurosAlt();
						jurosAlt.setData(atualizacaoJudicial.getData());
						jurosAlt.setValor(valor);
						if (!repository.existsByData(atualizacaoJudicial.getData())) {
							repository.save(jurosAlt);
						}
					} catch (Exception e) {
					}
				} else {
					try {
						SelicMetaCopom selicMetaCopom = selicMetaCopomService.findByData(atualizacaoJudicial.getData()).get();
						if (selicMetaCopom.getValor().compareTo(new Double(8.5)) > 0) {
							valor = new Double(0.005);

							JurosAlt jurosAlt = new JurosAlt();
							jurosAlt.setData(atualizacaoJudicial.getData());
							jurosAlt.setValor(valor);
							if (!repository.existsByData(atualizacaoJudicial.getData())) {
								repository.save(jurosAlt);
							}
						} else {
							BigDecimal val = new BigDecimal(selicMetaCopom.getValor()).multiply(new BigDecimal(0.7)).divide(new BigDecimal(100.0));
							val = val.plus();
							val = new BigDecimal(Math.pow(valor.doubleValue(), raiz));
							val.add(new BigDecimal(-1.0));
							valor = (new Double(selicMetaCopom.getValor()*0.7) / 100);
							valor = valor + 1;
							valor = Math.pow(valor.doubleValue(), raiz);
							valor = valor -1;
							try {
								JurosAlt jurosAlt = new JurosAlt();
								jurosAlt.setData(atualizacaoJudicial.getData());
								jurosAlt.setValor(valor);
								if (!repository.existsByData(atualizacaoJudicial.getData())) {
									repository.save(jurosAlt);
								}
							} catch (Exception e) {
							}
						}
					} catch (Exception e) {}
				}
			}
		} catch (Exception e) {}

	}

	public JurosAlt create(JurosAlt jurosAlt) {
		return repository.save(jurosAlt);
	}
	
	public JurosAlt save(JurosAlt jurosAlt) {
		return repository.save(jurosAlt);
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

	public Iterable<JurosAlt> getAll(Sort sort){
		return repository.findAll(sort);
	}

	public Page<JurosAlt> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public Iterable<JurosAlt> findAll() {
		return repository.findAll();
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
