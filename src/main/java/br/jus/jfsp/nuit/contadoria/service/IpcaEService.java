package br.jus.jfsp.nuit.contadoria.service;

import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import br.jus.jfsp.nuit.contadoria.models.IpcaE;
import br.jus.jfsp.nuit.contadoria.models.IpcaE;
import br.jus.jfsp.nuit.contadoria.repository.IpcaERepository;
import br.jus.jfsp.nuit.contadoria.util.ManipulaData;
import br.jus.jfsp.nuit.contadoria.util.consts.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

@Service
public class IpcaEService extends SgsBacenService {
	
	@Autowired
	private IpcaERepository repository;
	
	@Autowired
	private JsonReader jsonReader;

	@Autowired
	private UrlReaderService urlReader;
	
	public void importa() {

		Calendar dataInicial = repository.findMaxData();

		String conteudoUrl = "";
		try {
			conteudoUrl = urlReader.getConteudo(getUrl(IPCA_E, ManipulaData.toDate(dataInicial)));

		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		try {
			Object[] map = jsonReader.getJsonArray(conteudoUrl);
			for (int i = 0; i < map.length; i++) {
				LinkedHashMap lMap = (LinkedHashMap) map[i];
				Date data;

				try {
					data = ManipulaData.stringToDateDiaMesAno(lMap.get("data") + "");
				} catch (ParseException e) {
					e.printStackTrace();
					continue;
				}
				Double valor = new Double(lMap.get("valor") + "");
				IpcaE ipcae = new IpcaE();
				ipcae.setData(ManipulaData.toCalendar(data));
				ipcae.setValor(valor);
				ipcae.setFonte(Consts.SGS_BACEN);
				if (!repository.existsByData(ManipulaData.toCalendar(data))) {
					repository.save(ipcae);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public IpcaE create(IpcaE ipcaE) {
		return repository.save(ipcaE);
	}

	public IpcaE save(IpcaE ipcaE) {
		return repository.save(ipcaE);
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public IpcaE update(IpcaE ipcaE) throws RecordNotFoundException {
		findByIdOrThrowException(ipcaE.getId());
		return repository.save(ipcaE);
	}

	public Iterable<IpcaE> getAll(){
		return repository.findAll();
	}

	public Iterable<IpcaE> getAll(Sort sort){
		return repository.findAll(sort);
	}

	public Page<IpcaE> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public Iterable<IpcaE> findAll() {
		return repository.findAll();
	}

	public IpcaE read(Long id) throws RecordNotFoundException {
		return findByIdOrThrowException(id);
	}

	public Optional<IpcaE> findById(Long id) {
		return repository.findById(id);
	}

	public Page<IpcaE> findLike(Pageable pageable, String like) throws RecordNotFoundException {
		Page<IpcaE> retorno = repository.findLikePage(pageable, like);
		if (retorno.getTotalElements()==0) {
			throw new RecordNotFoundException("Valor não encontado");
		}
		return retorno;
	}

	private IpcaE findByIdOrThrowException(Long id) throws RecordNotFoundException{
		return repository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Registro não encontrado com o id " + id));
	}
	
	public Optional<IpcaE> findByData(Calendar data) {
		return repository.findByData(data);
	}

	public Iterable<IpcaE> findByDataBetween(Calendar data1, Calendar data2) {
		return repository.findAllByDataLessThanEqualAndDataGreaterThanEqual(data2, data1);
	}



}
