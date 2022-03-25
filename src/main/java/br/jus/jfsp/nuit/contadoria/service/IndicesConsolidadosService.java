package br.jus.jfsp.nuit.contadoria.service;

import br.jus.jfsp.nuit.contadoria.exception.RecordNotFoundException;
import br.jus.jfsp.nuit.contadoria.models.AjusteMoeda;
import br.jus.jfsp.nuit.contadoria.models.IndicesAtrasados;
import br.jus.jfsp.nuit.contadoria.models.IndicesCond;
import br.jus.jfsp.nuit.contadoria.models.IndicesConsolidados;
import br.jus.jfsp.nuit.contadoria.models.IndicesRes134;
import br.jus.jfsp.nuit.contadoria.models.IndicesSalarios;
import br.jus.jfsp.nuit.contadoria.models.Ipca;
import br.jus.jfsp.nuit.contadoria.models.IpcaE;
import br.jus.jfsp.nuit.contadoria.models.Juros;
import br.jus.jfsp.nuit.contadoria.models.JurosAlt;
import br.jus.jfsp.nuit.contadoria.models.MultiplicadorMoeda;
import br.jus.jfsp.nuit.contadoria.models.ReajusteBeneficio;
import br.jus.jfsp.nuit.contadoria.models.SalarioMinimo;
import br.jus.jfsp.nuit.contadoria.models.SalarioMinimoReferencia;
import br.jus.jfsp.nuit.contadoria.models.SelicMensal;
import br.jus.jfsp.nuit.contadoria.models.TetoBeneficio;
import br.jus.jfsp.nuit.contadoria.models.TetoContribuicao;
import br.jus.jfsp.nuit.contadoria.repository.IndicesConsolidadosRepository;
import br.jus.jfsp.nuit.contadoria.util.ManipulaArquivo;
import br.jus.jfsp.nuit.contadoria.util.ManipulaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;

@Service
public class IndicesConsolidadosService extends SidraIbgeService {

	@Autowired
	private JsonReader jsonReader;

	@Autowired
	private UrlReaderService urlReaderService;

	@Autowired
	private IndicesConsolidadosService indicesConsolidadosService;

	@Autowired
	private SalarioMinimoService salarioMinimoService;

	@Autowired
	private SalarioMinimoReferenciaService salarioMinimoReferenciaService;

	@Autowired
	private TetoContribuicaoService tetoContribuicaoService;

	@Autowired
	private TetoBeneficioService tetoBeneficioService;

	@Autowired
	private ReajusteBeneficioService reajusteBeneficioService;

	@Autowired
	private MultiplicadorMoedaService multiplicadorMoedaService;

	@Autowired
	private AjusteMoedaService ajusteMoedaService;

	@Autowired
	private IndicesAtrasadosService indicesAtrasadosService;

	@Autowired
	private IndicesRes134Service indicesRes134Service;

	@Autowired
	private IndicesSalariosService indicesSalariosService;

	@Autowired
	private IpcaService ipcaService;

	@Autowired
	private IpcaEService ipcaEService;

	@Autowired
	private IndicesCondService indicesCondService;

	@Autowired
	private SelicMensalService selicMensalService;

	@Autowired
	private JurosService jurosService;

	@Autowired
	private JurosAltService jurosAltService;

	@Autowired
	private IndicesConsolidadosRepository repository;

	public void importa() {

		// SALÁRIO MÍNIMO

		Iterable<SalarioMinimo> listSalarioMinimo = salarioMinimoService.getAll(Sort.by("data").descending());
		for (SalarioMinimo salarioMinimo: listSalarioMinimo) {
			IndicesConsolidados indicesConsolidados = new IndicesConsolidados();
			if (repository.existsByData(salarioMinimo.getData())) {
				indicesConsolidados = findByData(salarioMinimo.getData()).get();
			} else {
				indicesConsolidados.setData(salarioMinimo.getData());
			}
			indicesConsolidados.setSalarioMinimo(salarioMinimo.getValor());
			try {
				repository.save(indicesConsolidados);
			} catch (Exception e) {}
		}

		// SALÁRIO MÍNIMO REF

		Iterable<SalarioMinimoReferencia> listSalarioMinimoReferencia = salarioMinimoReferenciaService.getAll(Sort.by("data").descending());
		for (SalarioMinimoReferencia salarioMinimoReferencia: listSalarioMinimoReferencia) {
			IndicesConsolidados indicesConsolidados = new IndicesConsolidados();
			if (repository.existsByData(salarioMinimoReferencia.getData())) {
				indicesConsolidados = findByData(salarioMinimoReferencia.getData()).get();
			} else {
				indicesConsolidados.setData(salarioMinimoReferencia.getData());
			}
			indicesConsolidados.setSalarioMinimoReferencia(salarioMinimoReferencia.getValor());
			try {
				repository.save(indicesConsolidados);
			} catch (Exception e) {}
		}

		// TETO CONTRIBUIÇÃO

		Iterable<TetoContribuicao> listTetoContribuicao = tetoContribuicaoService.getAll(Sort.by("data").descending());
		for (TetoContribuicao tetoContribuicao: listTetoContribuicao) {
			IndicesConsolidados indicesConsolidados = new IndicesConsolidados();
			if (repository.existsByData(tetoContribuicao.getData())) {
				indicesConsolidados = findByData(tetoContribuicao.getData()).get();
			} else {
				indicesConsolidados.setData(tetoContribuicao.getData());
			}
			indicesConsolidados.setTetoContribuicao(tetoContribuicao.getValor());
			try {
				repository.save(indicesConsolidados);
			} catch (Exception e) {}
		}

		// TETO BENEFÍCIO

		Iterable<TetoBeneficio> listTetoBeneficio = tetoBeneficioService.getAll(Sort.by("data").descending());
		for (TetoBeneficio tetoBeneficio: listTetoBeneficio) {
			IndicesConsolidados indicesConsolidados = new IndicesConsolidados();
			if (repository.existsByData(tetoBeneficio.getData())) {
				indicesConsolidados = findByData(tetoBeneficio.getData()).get();
			} else {
				indicesConsolidados.setData(tetoBeneficio.getData());
			}
			indicesConsolidados.setTetoBeneficio(tetoBeneficio.getValor());
			try {
				repository.save(indicesConsolidados);
			} catch (Exception e) {}
		}

		// REAJUSTE BENEFÍCIOS

		Iterable<ReajusteBeneficio> listReajusteBeneficio = reajusteBeneficioService.getAll(Sort.by("data").descending());
		for (ReajusteBeneficio reajusteBeneficio: listReajusteBeneficio) {
			IndicesConsolidados indicesConsolidados = new IndicesConsolidados();
			if (repository.existsByData(reajusteBeneficio.getData())) {
				indicesConsolidados = findByData(reajusteBeneficio.getData()).get();
			} else {
				indicesConsolidados.setData(reajusteBeneficio.getData());
			}
			indicesConsolidados.setIntegral(reajusteBeneficio.getIntegral());
			indicesConsolidados.setProporcional(reajusteBeneficio.getProporcional());
			try {
				repository.save(indicesConsolidados);
			} catch (Exception e) {}
		}

		// MULTIPLICADOR MOEDA

		Iterable<MultiplicadorMoeda> listMultiplicadorMoeda = multiplicadorMoedaService.getAll(Sort.by("data").descending());
		for (MultiplicadorMoeda multiplicadorMoeda: listMultiplicadorMoeda) {
			IndicesConsolidados indicesConsolidados = new IndicesConsolidados();
			if (repository.existsByData(multiplicadorMoeda.getData())) {
				indicesConsolidados = findByData(multiplicadorMoeda.getData()).get();
			} else {
				indicesConsolidados.setData(multiplicadorMoeda.getData());
			}
			indicesConsolidados.setMultiplicadorMoeda(multiplicadorMoeda.getValor());
			try {
				repository.save(indicesConsolidados);
			} catch (Exception e) {}
		}

		// AJUSTE MOEDA

		Iterable<AjusteMoeda> listAjusteMoeda = ajusteMoedaService.getAll(Sort.by("data").descending());
		for (AjusteMoeda ajusteMoeda: listAjusteMoeda) {
			IndicesConsolidados indicesConsolidados = new IndicesConsolidados();
			if (repository.existsByData(ajusteMoeda.getData())) {
				indicesConsolidados = findByData(ajusteMoeda.getData()).get();
			} else {
				indicesConsolidados.setData(ajusteMoeda.getData());
			}
			indicesConsolidados.setAjusteMoeda(ajusteMoeda.getValor());
			try {
				repository.save(indicesConsolidados);
			} catch (Exception e) {}
		}

		// ÍNDICES ATRASADOS

		Iterable<IndicesAtrasados> listIndicesAtrasados = indicesAtrasadosService.getAll(Sort.by("data").descending());
		for (IndicesAtrasados indicesAtrasados: listIndicesAtrasados) {
			IndicesConsolidados indicesConsolidados = new IndicesConsolidados();
			if (repository.existsByData(indicesAtrasados.getData())) {
				indicesConsolidados = findByData(indicesAtrasados.getData()).get();
			} else {
				indicesConsolidados.setData(indicesAtrasados.getData());
			}
			indicesConsolidados.setIndiceAcumulado(indicesAtrasados.getIndiceAtrasado());
			indicesConsolidados.setIndiceAtualizado(indicesAtrasados.getIndice());
			try {
				repository.save(indicesConsolidados);
			} catch (Exception e) {}
		}

		// ÍNDICES RES 134

		Iterable<IndicesRes134> listIndicesRes134 = indicesRes134Service.getAll(Sort.by("data").descending());
		for (IndicesRes134 indicesRes134: listIndicesRes134) {
			IndicesConsolidados indicesConsolidados = new IndicesConsolidados();
			if (repository.existsByData(indicesRes134.getData())) {
				indicesConsolidados = findByData(indicesRes134.getData()).get();
			} else {
				indicesConsolidados.setData(indicesRes134.getData());
			}
			indicesConsolidados.setIndiceRes134(indicesRes134.getIndice());
			indicesConsolidados.setIndiceRes134Acumulado(indicesRes134.getIndiceAtrasado());
			try {
				repository.save(indicesConsolidados);
			} catch (Exception e) {}
		}

		// ÍNDICES SALÁRIOS

		Iterable<IndicesSalarios> listIndicesSalarios = indicesSalariosService.getAll(Sort.by("data").descending());
		for (IndicesSalarios indicesSalarios: listIndicesSalarios) {
			IndicesConsolidados indicesConsolidados = new IndicesConsolidados();
			if (repository.existsByData(indicesSalarios.getData())) {
				indicesConsolidados = findByData(indicesSalarios.getData()).get();
			} else {
				indicesConsolidados.setData(indicesSalarios.getData());
			}
			indicesConsolidados.setIndiceSalarios(indicesSalarios.getIndice());
			indicesConsolidados.setIndiceSalariosAcumulado(indicesSalarios.getIndiceAtrasado());
			try {
				repository.save(indicesConsolidados);
			} catch (Exception e) {}
		}

		// IPCA

		Iterable<Ipca> listIpca = ipcaService.getAll(Sort.by("data").descending());
		for (Ipca ipca: listIpca) {
			IndicesConsolidados indicesConsolidados = new IndicesConsolidados();
			if (repository.existsByData(ipca.getData())) {
				indicesConsolidados = findByData(ipca.getData()).get();
			} else {
				indicesConsolidados.setData(ipca.getData());
			}
			indicesConsolidados.setIpca(ipca.getValor());
			try {
				repository.save(indicesConsolidados);
			} catch (Exception e) {}
		}

		// IPCA E

		Iterable<IpcaE> listIpcaE = ipcaEService.getAll(Sort.by("data").descending());
		for (IpcaE ipcaE: listIpcaE) {
			IndicesConsolidados indicesConsolidados = new IndicesConsolidados();
			if (repository.existsByData(ipcaE.getData())) {
				indicesConsolidados = findByData(ipcaE.getData()).get();
			} else {
				indicesConsolidados.setData(ipcaE.getData());
			}
			indicesConsolidados.setIpca(ipcaE.getValor());
			try {
				repository.save(indicesConsolidados);
			} catch (Exception e) {}
		}

		// ÍNDICES COND

		Iterable<IndicesCond> listIndicesCond = indicesCondService.getAll(Sort.by("data").descending());
		for (IndicesCond indicesCond: listIndicesCond) {
			IndicesConsolidados indicesConsolidados = new IndicesConsolidados();
			if (repository.existsByData(indicesCond.getData())) {
				indicesConsolidados = findByData(indicesCond.getData()).get();
			} else {
				indicesConsolidados.setData(indicesCond.getData());
			}
			indicesConsolidados.setIndiceCondenatorias(indicesCond.getIndice());
			indicesConsolidados.setIndiceCondenatoriasAcumulado(indicesCond.getIndiceAtrasado());
			try {
				repository.save(indicesConsolidados);
			} catch (Exception e) {}
		}

		// SELIC

		Iterable<SelicMensal> listSelicMensal = selicMensalService.getAll(Sort.by("data").descending());
		for (SelicMensal selicMensal: listSelicMensal) {
			IndicesConsolidados indicesConsolidados = new IndicesConsolidados();
			if (repository.existsByData(selicMensal.getData())) {
				indicesConsolidados = findByData(selicMensal.getData()).get();
			} else {
				indicesConsolidados.setData(selicMensal.getData());
			}
			indicesConsolidados.setSelic(selicMensal.getValor());
			try {
				repository.save(indicesConsolidados);
			} catch (Exception e) {}
		}

		// JUROS

		Iterable<Juros> listJuros = jurosService.getAll(Sort.by("data").descending());
		for (Juros juros: listJuros) {
			IndicesConsolidados indicesConsolidados = new IndicesConsolidados();
			if (repository.existsByData(juros.getData())) {
				indicesConsolidados = findByData(juros.getData()).get();
			} else {
				indicesConsolidados.setData(juros.getData());
			}
			indicesConsolidados.setJuros(juros.getValor());
			try {
				repository.save(indicesConsolidados);
			} catch (Exception e) {}
		}

		// JUROS ALT

		Iterable<JurosAlt> listJurosAlt = jurosAltService.getAll(Sort.by("data").descending());
		for (JurosAlt jurosAlt: listJurosAlt) {
			IndicesConsolidados indicesConsolidados = new IndicesConsolidados();
			if (repository.existsByData(jurosAlt.getData())) {
				indicesConsolidados = findByData(jurosAlt.getData()).get();
			} else {
				indicesConsolidados.setData(jurosAlt.getData());
			}
			indicesConsolidados.setJurosAlt(jurosAlt.getValor());
			try {
				repository.save(indicesConsolidados);
			} catch (Exception e) {}
		}

	}

	public void mostraCSV() {
		ArrayList<IndicesConsolidados> listIndicesConsolidados = (ArrayList<IndicesConsolidados>) repository.findAll(Sort.by("data"));
		String[] csv = new String[listIndicesConsolidados.size()+1];
		csv[0] = "data;" +
				"salarioMinimo;" +
				"salarioMinimoReferencia;" +
				"tetoContribuicao;" +
				"tetoBeneficio;" +
				"integral;" +
				"proporcional;" +
				"multiplicadorMoeda;" +
				"ajusteMoeda;" +
				"indiceAtualizado;" +
				"indiceAcumulado;" +
				"indiceRes134;" +
				"indiceRes134Acumulado;" +
				"indiceSalarios;" +
				"indiceSalariosAcumulado;" +
				"ipca;" +
				"ipcaE;" +
				"indiceCondenatorias;" +
				"indiceCondenatoriasAcumulado;" +
				"selic;" +
				"juros;" +
				"jurosAlt";
		for(int i=0; i<listIndicesConsolidados.size(); i++) {
			csv[i+1] = listIndicesConsolidados.get(i).toString();
		}
		try {
			ManipulaArquivo.geraArquivo("consolidados.csv", csv);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public IndicesConsolidados create(IndicesConsolidados indicesConsolidados) {
		return repository.save(indicesConsolidados);
	}

	public IndicesConsolidados save(IndicesConsolidados indicesConsolidados) {
		return repository.save(indicesConsolidados);
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public IndicesConsolidados update(IndicesConsolidados indicesConsolidados) throws RecordNotFoundException {
		findByIdOrThrowException(indicesConsolidados.getId());
		return repository.save(indicesConsolidados);
	}

	public Iterable<IndicesConsolidados> getAll(){
		return repository.findAll();
	}

	public Iterable<IndicesConsolidados> getAll(Sort sort){
		return repository.findAll(sort);
	}

	public Page<IndicesConsolidados> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public IndicesConsolidados read(Long id) throws RecordNotFoundException {
		return findByIdOrThrowException(id);
	}

	public Optional<IndicesConsolidados> findById(Long id) {
		return repository.findById(id);
	}

	public Optional<IndicesConsolidados> findByData(Calendar data) {
		return repository.findByData(data);
	}

	public Page<IndicesConsolidados> findLike(Pageable pageable, String like) throws RecordNotFoundException {
		Page<IndicesConsolidados> retorno = repository.findLikePage(pageable, like);
		if (retorno.getTotalElements()==0) {
			return Page.empty();
			//throw new RecordNotFoundException("Valor não encontado");
		}
		return retorno;
	}

	private IndicesConsolidados findByIdOrThrowException(Long id) throws RecordNotFoundException{
		return repository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Registro não encontrado com o id " + id));
	}

}