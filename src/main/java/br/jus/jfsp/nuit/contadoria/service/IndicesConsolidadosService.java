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
		try {
			// SALÁRIO MÍNIMO
			System.out.println("importa consolidado");
			Iterable<SalarioMinimo> listSalarioMinimo = salarioMinimoService.getAll(Sort.by("data").descending());
			for (SalarioMinimo salarioMinimo: listSalarioMinimo) {
				IndicesConsolidados indicesConsolidados = new IndicesConsolidados();
				String ano = ManipulaData.getAno(ManipulaData.toDate(salarioMinimo.getData())) + "";
				String mes = ManipulaData.getMesAlteradoStr(ManipulaData.toDate(salarioMinimo.getData())) + "";
				if (repository.existsByMesAndAno(mes, ano)) {
					System.out.println("EXISTS");
					indicesConsolidados = findByMesAndAno(mes, ano).get();
					System.out.println(indicesConsolidados.getAno() + "-" + indicesConsolidados.getMes());
				} else {
					indicesConsolidados.setData(salarioMinimo.getData());
					indicesConsolidados.setMes(mes);
					indicesConsolidados.setAno(ano);
				}
				indicesConsolidados.setSalarioMinimo(salarioMinimo.getValor());
				try {
					//if (!repository.existsByData(indicesConsolidados.getData())) {
					//System.out.println("indicesConsolidados " + indicesConsolidados.toString());
					System.out.println(indicesConsolidados.toString());
					repository.save(indicesConsolidados);
					//}
				} catch (Exception e) {}
			}
//
			// SALÁRIO MÍNIMO REF

			Iterable<SalarioMinimoReferencia> listSalarioMinimoReferencia = salarioMinimoReferenciaService.getAll(Sort.by("data").descending());
			for (SalarioMinimoReferencia salarioMinimoReferencia: listSalarioMinimoReferencia) {
				IndicesConsolidados indicesConsolidados = new IndicesConsolidados();
				String ano = ManipulaData.getAno(ManipulaData.toDate(salarioMinimoReferencia.getData())) + "";
				String mes = ManipulaData.getMesAlteradoStr(ManipulaData.toDate(salarioMinimoReferencia.getData())) + "";
				if (repository.existsByMesAndAno(mes, ano)) {
					indicesConsolidados = findByMesAndAno(mes, ano).get();
				} else {
					indicesConsolidados.setData(salarioMinimoReferencia.getData());
					indicesConsolidados.setMes(mes);
					indicesConsolidados.setAno(ano);
				}
				indicesConsolidados.setSalarioMinimoReferencia(salarioMinimoReferencia.getValor());
				try {
					//if (!repository.existsByData(indicesConsolidados.getData())) {
						repository.save(indicesConsolidados);
					//}
				} catch (Exception e) {}
			}
			System.out.println("fim importacao salario minimo ref");

			// TETO CONTRIBUIÇÃO

			Iterable<TetoContribuicao> listTetoContribuicao = tetoContribuicaoService.getAll(Sort.by("data").descending());
			for (TetoContribuicao tetoContribuicao: listTetoContribuicao) {
				IndicesConsolidados indicesConsolidados = new IndicesConsolidados();
				String ano = ManipulaData.getAno(ManipulaData.toDate(tetoContribuicao.getData())) + "";
				String mes = ManipulaData.getMesAlteradoStr(ManipulaData.toDate(tetoContribuicao.getData())) + "";
				if (repository.existsByMesAndAno(mes, ano)) {
					indicesConsolidados = findByMesAndAno(mes, ano).get();
				} else {
					indicesConsolidados.setData(tetoContribuicao.getData());
					indicesConsolidados.setMes(mes);
					indicesConsolidados.setAno(ano);
				}
				indicesConsolidados.setTetoContribuicao(tetoContribuicao.getValor());
				try {
					//if (!repository.existsByData(indicesConsolidados.getData())) {
						repository.save(indicesConsolidados);
					//}
				} catch (Exception e) {}
			}
			System.out.println("fim importacao teto contribuicao");

			// TETO BENEFÍCIO

			Iterable<TetoBeneficio> listTetoBeneficio = tetoBeneficioService.getAll(Sort.by("data").descending());
			for (TetoBeneficio tetoBeneficio: listTetoBeneficio) {
				IndicesConsolidados indicesConsolidados = new IndicesConsolidados();
				String ano = ManipulaData.getAno(ManipulaData.toDate(tetoBeneficio.getData())) + "";
				String mes = ManipulaData.getMesAlteradoStr(ManipulaData.toDate(tetoBeneficio.getData())) + "";
				if (repository.existsByMesAndAno(mes, ano)) {
					indicesConsolidados = findByMesAndAno(mes, ano).get();
				} else {
					indicesConsolidados.setData(tetoBeneficio.getData());
					indicesConsolidados.setMes(mes);
					indicesConsolidados.setAno(ano);
				}
				indicesConsolidados.setTetoBeneficio(tetoBeneficio.getValor());
				try {
					//if (!repository.existsByData(indicesConsolidados.getData())) {
						repository.save(indicesConsolidados);
					//}
				} catch (Exception e) {}
			}
			System.out.println("fim importacao teto beneficio");
			// REAJUSTE BENEFÍCIOS

			Iterable<ReajusteBeneficio> listReajusteBeneficio = reajusteBeneficioService.getAll(Sort.by("data").descending());
			for (ReajusteBeneficio reajusteBeneficio: listReajusteBeneficio) {
				IndicesConsolidados indicesConsolidados = new IndicesConsolidados();
				String ano = ManipulaData.getAno(ManipulaData.toDate(reajusteBeneficio.getData())) + "";
				String mes = ManipulaData.getMesAlteradoStr(ManipulaData.toDate(reajusteBeneficio.getData())) + "";
				if (repository.existsByMesAndAno(mes, ano)) {
					indicesConsolidados = findByMesAndAno(mes, ano).get();
				} else {
					indicesConsolidados.setData(reajusteBeneficio.getData());
					indicesConsolidados.setMes(mes);
					indicesConsolidados.setAno(ano);
				}
				indicesConsolidados.setIntegral(reajusteBeneficio.getIntegral());
				indicesConsolidados.setProporcional(reajusteBeneficio.getProporcional());
				try {
					//if (!repository.existsByData(indicesConsolidados.getData())) {
						repository.save(indicesConsolidados);
					//}
				} catch (Exception e) {}
			}
			System.out.println("fim importacao reajuste beneficios");
			// MULTIPLICADOR MOEDA

			Iterable<MultiplicadorMoeda> listMultiplicadorMoeda = multiplicadorMoedaService.getAll(Sort.by("data").descending());
			for (MultiplicadorMoeda multiplicadorMoeda: listMultiplicadorMoeda) {
				IndicesConsolidados indicesConsolidados = new IndicesConsolidados();
				String ano = ManipulaData.getAno(ManipulaData.toDate(multiplicadorMoeda.getData())) + "";
				String mes = ManipulaData.getMesAlteradoStr(ManipulaData.toDate(multiplicadorMoeda.getData())) + "";
				if (repository.existsByMesAndAno(mes, ano)) {
					indicesConsolidados = findByMesAndAno(mes, ano).get();
				} else {
					indicesConsolidados.setData(multiplicadorMoeda.getData());
					indicesConsolidados.setMes(mes);
					indicesConsolidados.setAno(ano);
				}
				indicesConsolidados.setMultiplicadorMoeda(multiplicadorMoeda.getValor());
				try {
					//if (!repository.existsByData(indicesConsolidados.getData())) {
						repository.save(indicesConsolidados);
					//}
				} catch (Exception e) {}
			}
			System.out.println("fim importacao multiplicador moeda ");

			// AJUSTE MOEDA

			Iterable<AjusteMoeda> listAjusteMoeda = ajusteMoedaService.getAll(Sort.by("data").descending());
			for (AjusteMoeda ajusteMoeda: listAjusteMoeda) {
				IndicesConsolidados indicesConsolidados = new IndicesConsolidados();
				String ano = ManipulaData.getAno(ManipulaData.toDate(ajusteMoeda.getData())) + "";
				String mes = ManipulaData.getMesAlteradoStr(ManipulaData.toDate(ajusteMoeda.getData())) + "";
				if (repository.existsByMesAndAno(mes, ano)) {
					indicesConsolidados = findByMesAndAno(mes, ano).get();
				} else {
					indicesConsolidados.setData(ajusteMoeda.getData());
					indicesConsolidados.setMes(mes);
					indicesConsolidados.setAno(ano);
				}
				indicesConsolidados.setAjusteMoeda(ajusteMoeda.getValor());
				try {
					//if (!repository.existsByData(indicesConsolidados.getData())) {
						repository.save(indicesConsolidados);
					//}
				} catch (Exception e) {}
			}
			System.out.println("fim importacao ajuste moeda");

			// ÍNDICES ATRASADOS

			Iterable<IndicesAtrasados> listIndicesAtrasados = indicesAtrasadosService.getAll(Sort.by("data").descending());
			for (IndicesAtrasados indicesAtrasados: listIndicesAtrasados) {
				IndicesConsolidados indicesConsolidados = new IndicesConsolidados();
				String ano = ManipulaData.getAno(ManipulaData.toDate(indicesAtrasados.getData())) + "";
				String mes = ManipulaData.getMesAlteradoStr(ManipulaData.toDate(indicesAtrasados.getData())) + "";
				if (repository.existsByMesAndAno(mes, ano)) {
					indicesConsolidados = findByMesAndAno(mes, ano).get();
				} else {
					indicesConsolidados.setData(indicesAtrasados.getData());
					indicesConsolidados.setMes(mes);
					indicesConsolidados.setAno(ano);
				}
				indicesConsolidados.setIndiceAcumulado(indicesAtrasados.getIndiceAtrasado());
				indicesConsolidados.setIndiceAtualizado(indicesAtrasados.getIndice());
				try {
					//if (!repository.existsByData(indicesConsolidados.getData())) {
						repository.save(indicesConsolidados);
					//}
				} catch (Exception e) {}
			}
			System.out.println("fim importacao indices Atrasados");

			// ÍNDICES RES 134

			Iterable<IndicesRes134> listIndicesRes134 = indicesRes134Service.getAll(Sort.by("data").descending());
			for (IndicesRes134 indicesRes134: listIndicesRes134) {
				IndicesConsolidados indicesConsolidados = new IndicesConsolidados();
				String ano = ManipulaData.getAno(ManipulaData.toDate(indicesRes134.getData())) + "";
				String mes = ManipulaData.getMesAlteradoStr(ManipulaData.toDate(indicesRes134.getData())) + "";
				if (repository.existsByMesAndAno(mes, ano)) {
					indicesConsolidados = findByMesAndAno(mes, ano).get();
				} else {
					indicesConsolidados.setData(indicesRes134.getData());
					indicesConsolidados.setMes(mes);
					indicesConsolidados.setAno(ano);
				}
				indicesConsolidados.setIndiceRes134(indicesRes134.getIndice());
				indicesConsolidados.setIndiceRes134Acumulado(indicesRes134.getIndiceAtrasado());
				try {
					//if (!repository.existsByData(indicesConsolidados.getData())) {
						repository.save(indicesConsolidados);
					//}
				} catch (Exception e) {}
			}
			System.out.println("fim importacao res 134");

			// ÍNDICES SALÁRIOS

			Iterable<IndicesSalarios> listIndicesSalarios = indicesSalariosService.getAll(Sort.by("data").descending());
			for (IndicesSalarios indicesSalarios: listIndicesSalarios) {
				IndicesConsolidados indicesConsolidados = new IndicesConsolidados();
				String ano = ManipulaData.getAno(ManipulaData.toDate(indicesSalarios.getData())) + "";
				String mes = ManipulaData.getMesAlteradoStr(ManipulaData.toDate(indicesSalarios.getData())) + "";
				if (repository.existsByMesAndAno(mes, ano)) {
					indicesConsolidados = findByMesAndAno(mes, ano).get();
				} else {
					indicesConsolidados.setData(indicesSalarios.getData());
					indicesConsolidados.setMes(mes);
					indicesConsolidados.setAno(ano);
				}
				indicesConsolidados.setIndiceSalarios(indicesSalarios.getIndice());
				indicesConsolidados.setIndiceSalariosAcumulado(indicesSalarios.getIndiceAtrasado());
				try {
					//if (!repository.existsByData(indicesConsolidados.getData())) {
						repository.save(indicesConsolidados);
					//}
				} catch (Exception e) {}
			}
			System.out.println("fim importacao indices salarios");

			// IPCA E

			Iterable<IpcaE> listIpcaE = ipcaEService.getAll(Sort.by("data").descending());
			for (IpcaE ipcaE: listIpcaE) {
				IndicesConsolidados indicesConsolidados = new IndicesConsolidados();
				String ano = ManipulaData.getAno(ManipulaData.toDate(ipcaE.getData())) + "";
				String mes = ManipulaData.getMesAlteradoStr(ManipulaData.toDate(ipcaE.getData())) + "";
				if (repository.existsByMesAndAno(mes, ano)) {
					indicesConsolidados = findByMesAndAno(mes, ano).get();
				} else {
					indicesConsolidados.setData(ipcaE.getData());
					indicesConsolidados.setMes(mes);
					indicesConsolidados.setAno(ano);
				}
				indicesConsolidados.setIpcaE(ipcaE.getValor());
				try {
					//if (!repository.existsByData(indicesConsolidados.getData())) {
						repository.save(indicesConsolidados);
					//}
				} catch (Exception e) {}
			}
			System.out.println("fim importacao ipca e");
			// ÍNDICES COND

			Iterable<IndicesCond> listIndicesCond = indicesCondService.getAll(Sort.by("data").descending());
			for (IndicesCond indicesCond: listIndicesCond) {
				IndicesConsolidados indicesConsolidados = new IndicesConsolidados();
				String ano = ManipulaData.getAno(ManipulaData.toDate(indicesCond.getData())) + "";
				String mes = ManipulaData.getMesAlteradoStr(ManipulaData.toDate(indicesCond.getData())) + "";
				if (repository.existsByMesAndAno(mes, ano)) {
					indicesConsolidados = findByMesAndAno(mes, ano).get();
				} else {
					indicesConsolidados.setData(indicesCond.getData());
					indicesConsolidados.setMes(mes);
					indicesConsolidados.setAno(ano);
				}
				indicesConsolidados.setIndiceCondenatorias(indicesCond.getIndice());
				indicesConsolidados.setIndiceCondenatoriasAcumulado(indicesCond.getIndiceAtrasado());
				try {
					//if (!repository.existsByData(indicesConsolidados.getData())) {
						repository.save(indicesConsolidados);
					//}
				} catch (Exception e) {}
			}
			System.out.println("fim importacao indices cond ");
			// SELIC

			Iterable<SelicMensal> listSelicMensal = selicMensalService.getAll(Sort.by("data").descending());
			for (SelicMensal selicMensal: listSelicMensal) {
				IndicesConsolidados indicesConsolidados = new IndicesConsolidados();
				String ano = ManipulaData.getAno(ManipulaData.toDate(selicMensal.getData())) + "";
				String mes = ManipulaData.getMesAlteradoStr(ManipulaData.toDate(selicMensal.getData())) + "";
				if (repository.existsByMesAndAno(mes, ano)) {
					indicesConsolidados = findByMesAndAno(mes, ano).get();
				} else {
					indicesConsolidados.setData(selicMensal.getData());
					indicesConsolidados.setMes(mes);
					indicesConsolidados.setAno(ano);
				}
				indicesConsolidados.setSelic(selicMensal.getValor());
				try {
					//if (!repository.existsByData(indicesConsolidados.getData())) {
						repository.save(indicesConsolidados);
					//}
				} catch (Exception e) {}
			}
			System.out.println("fim importacao selic ");
			// JUROS

			Iterable<Juros> listJuros = jurosService.getAll(Sort.by("data").descending());
			for (Juros juros: listJuros) {
				IndicesConsolidados indicesConsolidados = new IndicesConsolidados();
				String ano = ManipulaData.getAno(ManipulaData.toDate(juros.getData())) + "";
				String mes = ManipulaData.getMesAlteradoStr(ManipulaData.toDate(juros.getData())) + "";
				if (repository.existsByMesAndAno(mes, ano)) {
					indicesConsolidados = findByMesAndAno(mes, ano).get();
				} else {
					indicesConsolidados.setData(juros.getData());
					indicesConsolidados.setMes(mes);
					indicesConsolidados.setAno(ano);
				}
				indicesConsolidados.setJuros(juros.getValor());
				try {
					//if (!repository.existsByData(indicesConsolidados.getData())) {
						repository.save(indicesConsolidados);
					//}
				} catch (Exception e) {}
			}
			System.out.println("fim importacao juros ");
			// JUROS ALT

			Iterable<JurosAlt> listJurosAlt = jurosAltService.getAll(Sort.by("data").descending());
			for (JurosAlt jurosAlt: listJurosAlt) {
				IndicesConsolidados indicesConsolidados = new IndicesConsolidados();
				String ano = ManipulaData.getAno(ManipulaData.toDate(jurosAlt.getData())) + "";
				String mes = ManipulaData.getMesAlteradoStr(ManipulaData.toDate(jurosAlt.getData())) + "";
				if (repository.existsByMesAndAno(mes, ano)) {
					indicesConsolidados = findByMesAndAno(mes, ano).get();
				} else {
					indicesConsolidados.setData(jurosAlt.getData());
					indicesConsolidados.setMes(mes);
					indicesConsolidados.setAno(ano);
				}
				indicesConsolidados.setJurosAlt(jurosAlt.getValor());
				try {
					//if (!repository.existsByData(indicesConsolidados.getData())) {
						repository.save(indicesConsolidados);
					//}
				} catch (Exception e) {}
			}
			System.out.println("fim importacao juros alt");

			// IPCA

			Iterable<Ipca> listIpca = ipcaService.getAll(Sort.by("data").descending());
			for (Ipca ipca: listIpca) {
				IndicesConsolidados indicesConsolidados = new IndicesConsolidados();
				String ano = ManipulaData.getAno(ManipulaData.toDate(ipca.getData())) + "";
				String mes = ManipulaData.getMesAlteradoStr(ManipulaData.toDate(ipca.getData())) + "";
				if (repository.existsByMesAndAno(mes, ano)) {
					indicesConsolidados = findByMesAndAno(mes, ano).get();
				} else {
					indicesConsolidados.setData(ipca.getData());
				}
				indicesConsolidados.setIpca(ipca.getVariacaoMensal());
				try {
					//if (!repository.existsByData(indicesConsolidados.getData())) {
					repository.save(indicesConsolidados);
					//}
				} catch (Exception e) {}
			}
			System.out.println("fim importacao ipca");


			System.out.println("mostraCSV");
			mostraCSV();
			System.out.println("fim");

		} catch (Exception e) {}
	}

	public void mostraCSV() {
		ArrayList<IndicesConsolidados> listIndicesConsolidados = (ArrayList<IndicesConsolidados>) repository.findAll(Sort.by("data"));
		String[] csv = new String[listIndicesConsolidados.size()+1];
		csv[0] = "Competencia;" +
				"DataBase;" +
				"salarioMinimo;" +
				"salarioMinimoReferencia;" +
				"tetoContribuicao;" +
				"tetoBeneficio;" +
				"integral;" +
				"proporcional;" +
				"multiplicadorMoeda;" +
				"ajusteMoeda;" +
				"indiceAtualizacao;" +
				"indiceAcumulado;" +
				"indiceRes134;" +
				"indiceRes134Acumulado;" +
				"indiceSalarios;" +
				"indiceSalariosAcumulado;" +
				"IPCA;" +
				"IPCAE;" +
				"indiceCondenatorias;" +
				"indiceCondenatoriasAcumulado;" +
				"Selic;" +
				"Juros;" +
				"JurosAlt";
		for(int i=0; i<listIndicesConsolidados.size(); i++) {
			csv[i+1] = listIndicesConsolidados.get(i).toString();
		}
		try {
			ManipulaArquivo.geraArquivo("consolidados.csv", csv);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void mostraCSVTeste() {

		ArrayList<IndicesConsolidados> listIndicesConsolidados = (ArrayList<IndicesConsolidados>) repository.findAll(Sort.by("data"));
		String[] csv = new String[listIndicesConsolidados.size()+1];
		csv[0] = "competencia;" +
				"dataBase;" +
				"dataBase_google;" +

				"salarioMinimo;" +
				"salarioMinimo_google;" +

				"salarioMinimoReferencia;" +
				"salarioMinimoReferencia_google;" +

				"tetoContribuicao;" +
				"tetoContribuicao_google;" +

				"tetoBeneficio;" +
				"tetoBeneficio_google;" +

				"integral;" +
				"integral_google;" +

				"proporcional;" +
				"proporcional_google;" +

				"multiplicadorMoeda;" +
				"multiplicadorMoeda_google;" +

				"ajusteMoeda;" +
				"ajusteMoeda_google;" +

				"indiceAtualizado;" +
				"indiceAtualizado_google;" +

				"indiceAcumulado;" +
				"indiceAcumulado_google;" +

				"indiceRes134;" +
				"indiceRes134_google;" +

				"indiceRes134Acumulado;" +
				"indiceRes134Acumulado_google;" +

				"indiceSalarios;" +
				"indiceSalarios_google;" +

				"indiceSalariosAcumulado;" +
				"indiceSalariosAcumulado_google;" +

				"ipca;" +
				"ipca_google;" +

				"ipcaE;" +
				"ipcaE_google;" +

				"indiceCondenatorias;" +
				"indiceCondenatorias_google;" +

				"indiceCondenatoriasAcumulado;" +
				"indiceCondenatoriasAcumulado_google;" +

				"selic;" +
				"selic_google;" +

				"juros;" +
				"juros_google;" +

				"jurosAlt" +
				"jurosAlt_google";


		String linha = "";

		String[] salarioMinimo = ManipulaArquivo.normalizar(ManipulaArquivo.getColuna(1));
		String[] salarioMinimoRef = ManipulaArquivo.normalizar(ManipulaArquivo.getColuna(2));
		String[] tetoContribuicao = ManipulaArquivo.normalizar(ManipulaArquivo.getColuna(3));
		String[] tetoBeneficio = ManipulaArquivo.normalizar(ManipulaArquivo.getColuna(4));
		String[] dataBase = ManipulaArquivo.normalizar(ManipulaArquivo.getColuna(5));
		String[] integral = ManipulaArquivo.normalizar(ManipulaArquivo.getColuna(6));
		String[] proporcional = ManipulaArquivo.normalizar(ManipulaArquivo.getColuna(7));
		String[] multiplicadorMoeda = ManipulaArquivo.normalizar(ManipulaArquivo.getColuna(8));
		String[] ajusteMoeda = ManipulaArquivo.normalizar(ManipulaArquivo.getColuna(9));
		String[] indiceAtrasado = ManipulaArquivo.normalizar(ManipulaArquivo.getColuna(10));
		String[] indiceAcumulado = ManipulaArquivo.normalizar(ManipulaArquivo.getColuna(11));
		String[] res134 = ManipulaArquivo.normalizar(ManipulaArquivo.getColuna(12));
		String[] rest134Acumulado = ManipulaArquivo.normalizar(ManipulaArquivo.getColuna(13));
		String[] indiceSalario = ManipulaArquivo.normalizar(ManipulaArquivo.getColuna(14));
		String[] indiceSalarioAcumulado = ManipulaArquivo.normalizar(ManipulaArquivo.getColuna(15));
		String[] ipca = ManipulaArquivo.normalizar(ManipulaArquivo.getColuna(16));
		String[] ipcaE = ManipulaArquivo.normalizar(ManipulaArquivo.getColuna(17));
		String[] indiceCond = ManipulaArquivo.normalizar(ManipulaArquivo.getColuna(18));
		String[] indiceCondAcumulado = ManipulaArquivo.normalizar(ManipulaArquivo.getColuna(19));
		String[] selic = ManipulaArquivo.normalizar(ManipulaArquivo.getColuna(20));
		String[] juros = ManipulaArquivo.normalizar(ManipulaArquivo.getColuna(21));


		DecimalFormat df = new DecimalFormat("#,##0.00000000000000");

		for(int i=0; i<listIndicesConsolidados.size(); i++) {
			//System.out.println(listIndicesConsolidados.get(i).getTetoContribuicao());
			try {

				String proporcionalStr = listIndicesConsolidados.get(i).getProporcional()!=null?df.format(listIndicesConsolidados.get(i).getProporcional()):listIndicesConsolidados.get(i).getProporcional() + "";
				String integralStr = listIndicesConsolidados.get(i).getIntegral()!=null?df.format(listIndicesConsolidados.get(i).getIntegral()):listIndicesConsolidados.get(i).getIntegral() + "";
				String jurosStr = listIndicesConsolidados.get(i).getJuros()!=null?df.format(listIndicesConsolidados.get(i).getJuros()):listIndicesConsolidados.get(i).getJuros() + "";
				String jurosAltStr = listIndicesConsolidados.get(i).getJurosAlt()!=null?df.format(listIndicesConsolidados.get(i).getJuros()):listIndicesConsolidados.get(i).getJurosAlt() + "";

				System.out.println(ManipulaData.dateToStringAnoMes(ManipulaData.toDate(listIndicesConsolidados.get(i).getData())) + " " + integralStr);

				linha = ManipulaData.dateToStringAnoMes(ManipulaData.toDate(listIndicesConsolidados.get(i).getData()))  + ";" +
						listIndicesConsolidados.get(i).getDataBase() + ";" +
						dataBase[i] + ";" +

						listIndicesConsolidados.get(i).getSalarioMinimo() + ";" +
						salarioMinimo[i].replaceAll(",", ".") + ";" +

						listIndicesConsolidados.get(i).getSalarioMinimoReferencia()+ ";" +
						salarioMinimoRef[i].replaceAll(",", ".") + ";" +

						listIndicesConsolidados.get(i).getTetoContribuicao() + ";" +
						tetoContribuicao[i].replaceAll(",", ".") + ";" +

						listIndicesConsolidados.get(i).getTetoBeneficio() + ";" +
						tetoBeneficio[i].replaceAll(",", ".") + ";" +

						integralStr.replaceAll(",", ".") + ";" +
						integral[i].replaceAll(",", ".") + ";" +

						proporcionalStr.replaceAll(",", ".") + ";" +
						proporcional[i].replaceAll(",", ".") + ";" +

						listIndicesConsolidados.get(i).getMultiplicadorMoeda() + ";" +
						multiplicadorMoeda[i].replaceAll(",", ".") + ";" +

						listIndicesConsolidados.get(i).getAjusteMoeda() + ";" +
						ajusteMoeda[i].replaceAll(",", ".") + ";" +

						listIndicesConsolidados.get(i).getIndiceAtualizado() + ";" +
						indiceAtrasado[i].replaceAll(",", ".") + ";" +

						listIndicesConsolidados.get(i).getIndiceAcumulado() + ";" +
						indiceAcumulado[i].replaceAll(",", ".") + ";" +

						listIndicesConsolidados.get(i).getIndiceRes134() + ";" +
						res134[i].replaceAll(",", ".") + ";" +


						listIndicesConsolidados.get(i).getIndiceRes134Acumulado() + ";" +
						rest134Acumulado[i].replaceAll(",", ".") + ";" +

						listIndicesConsolidados.get(i).getIndiceSalarios() + ";" +
						indiceSalario[i].replaceAll(",", ".") + ";" +

						listIndicesConsolidados.get(i).getIndiceSalariosAcumulado() + ";" +
						indiceSalarioAcumulado[i].replaceAll(",", ".") + ";" +

						listIndicesConsolidados.get(i).getIpca() + ";" +
						ipca[i].replaceAll(",", ".") + ";" +

						listIndicesConsolidados.get(i).getIpcaE() + ";" +
						ipcaE[i].replaceAll(",", ".") + ";" +

						listIndicesConsolidados.get(i).getIndiceCondenatorias() + ";" +
						indiceCond[i].replaceAll(",", ".") + ";" +

						listIndicesConsolidados.get(i).getIndiceCondenatoriasAcumulado() + ";" +
						indiceCondAcumulado[i].replaceAll(",", ".") + ";" +

						listIndicesConsolidados.get(i).getSelic() + ";" +
						selic[i].replaceAll(",", ".") + ";" +

						jurosStr.replaceAll(",", ".") + ";" +
						juros[i].replaceAll(",", ".") + ";"

				//jurosAltStr.replaceAll(",", ".") + ";" +
				//jurosAlt[i].replaceAll(",", ".") + ";"
				;

				//csv[i+1] = listIndicesConsolidados.get(i).toString();
				csv[i+1] = linha;
			} catch (Exception e) {
				System.out.println(i);
				//System.out.println(listIndicesConsolidados.get(i).getDataBase() + " - " + listIndicesConsolidados.get(i).getIntegral());
			}

		}
		try {
			ManipulaArquivo.geraArquivo("consolidados_teste_comp.csv", csv);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("fim teste");
	}

	public void testando() {
		System.out.println("Início comparação de salario minimo ref");
		String[] normalizados = ManipulaArquivo.normalizar(ManipulaArquivo.getColuna(2));
		//teste(normalizados);
		//mostraCSV(normalizados);
		System.out.println("Fim comparação de salario minimo ref");
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

	public Iterable<IndicesConsolidados> findAll() {
		return repository.findAll(Sort.by("data"));
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

	public Optional<IndicesConsolidados> findByMesAndAno(String mes, String ano) {
		return repository.findByMesAndAno(mes, ano);
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