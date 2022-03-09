package br.jus.jfsp.nuit.contadoria.service;

import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import br.jus.jfsp.nuit.contadoria.models.AtualizacaoJudicial;
import br.jus.jfsp.nuit.contadoria.models.AtualizacaoSalario;
import br.jus.jfsp.nuit.contadoria.models.IndicesAtrasados;
import br.jus.jfsp.nuit.contadoria.models.IndicesCond;
import br.jus.jfsp.nuit.contadoria.models.IndicesRes134;
import br.jus.jfsp.nuit.contadoria.models.IndicesSalarios;
import br.jus.jfsp.nuit.contadoria.models.IpcaE;
import br.jus.jfsp.nuit.contadoria.models.TrMensal;
import br.jus.jfsp.nuit.contadoria.models.Ufir;
import br.jus.jfsp.nuit.contadoria.repository.IndicesCondRepository;
import br.jus.jfsp.nuit.contadoria.util.ManipulaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
	private AtualizacaoJudicialService atualizacaoJudicialService;

	public void importa() {

		// IpcaE

		Iterable<IpcaE> listIpcaE = ipcaEService.getAll();
		for (IpcaE ipcaE: listIpcaE) {
			BigDecimal indice = new BigDecimal(ipcaE.getValor());
			Calendar dezembro1991 = ManipulaData.getCalendar("1991-12-01", ManipulaData.ANO_MES_DIA);
			Calendar dezembro2000 = ManipulaData.getCalendar("2000-12-01", ManipulaData.ANO_MES_DIA);
			Calendar janeiro1992 = ManipulaData.getCalendar("1992-01-01", ManipulaData.ANO_MES_DIA);
			Calendar novembro2000 = ManipulaData.getCalendar("2000-11-01", ManipulaData.ANO_MES_DIA);

			if (ipcaE.getData().compareTo(dezembro1991) == 1 || ipcaE.getData().compareTo(dezembro2000) > 1) {
				try {
					repository.save(new IndicesCond(indice, ipcaE.getObservacao(), ipcaE.getData()));
				} catch (Exception e) {}
			}
		}

		// UFIR

		Iterable<Ufir> listUfir = ufirService.getAll();
		for (Ufir ufir: listUfir) {
			BigDecimal indice = new BigDecimal(ufir.getValor());
			Calendar janeiro1992 = ManipulaData.getCalendar("1992-01-01", ManipulaData.ANO_MES_DIA);
			Calendar novembro2000 = ManipulaData.getCalendar("2000-11-01", ManipulaData.ANO_MES_DIA);

			if (ufir.getData().compareTo(janeiro1992) >= 1 || ufir.getData().compareTo(novembro2000) < 1) {
				try {
					repository.save(new IndicesCond(indice, ufir.getObservacao(), ufir.getData()));
				} catch (Exception e) {}
			}
		}

		// Atualizacao Judicial

		Iterable<AtualizacaoJudicial> listAtualizacaoJudicial = atualizacaoJudicialService.getAll();
		for (AtualizacaoJudicial atualizacaoJudicial: listAtualizacaoJudicial) {
			BigDecimal indice = new BigDecimal(atualizacaoJudicial.getValor());
			Calendar novembro2000 = ManipulaData.getCalendar("2000-11-01", ManipulaData.ANO_MES_DIA);

			if (atualizacaoJudicial.getData().compareTo(novembro2000) >= 1) {
				try {
					repository.save(new IndicesCond(indice, atualizacaoJudicial.getObservacao(), atualizacaoJudicial.getData()));
				} catch (Exception e) {}
			}
		}

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

	public Page<IndicesCond> findAll(Pageable pageable) {
		return repository.findAll(pageable);
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
