package br.jus.jfsp.nuit.contadoria.controllers;

import br.jus.jfsp.nuit.contadoria.models.IndicesCond;
import br.jus.jfsp.nuit.contadoria.service.BtnMensalService;
import br.jus.jfsp.nuit.contadoria.service.IndicesAtrasadosService;
import br.jus.jfsp.nuit.contadoria.service.IndicesCondService;
import br.jus.jfsp.nuit.contadoria.service.IndicesConsolidadosService;
import br.jus.jfsp.nuit.contadoria.service.IndicesRes134Service;
import br.jus.jfsp.nuit.contadoria.service.IndicesSalariosService;
import br.jus.jfsp.nuit.contadoria.service.InpcService;
import br.jus.jfsp.nuit.contadoria.service.Ipca15Service;
import br.jus.jfsp.nuit.contadoria.service.IpcaEService;
import br.jus.jfsp.nuit.contadoria.service.IpcaService;
import br.jus.jfsp.nuit.contadoria.service.IrsmService;
import br.jus.jfsp.nuit.contadoria.service.JurosAltService;
import br.jus.jfsp.nuit.contadoria.service.JurosService;
import br.jus.jfsp.nuit.contadoria.service.SalarioMinimoService;
import br.jus.jfsp.nuit.contadoria.service.SelicMensalService;
import br.jus.jfsp.nuit.contadoria.service.SelicMetaCopomService;
import br.jus.jfsp.nuit.contadoria.service.TrMensalService;
import br.jus.jfsp.nuit.contadoria.service.TrdService;
import br.jus.jfsp.nuit.contadoria.service.UfirService;
import br.jus.jfsp.nuit.contadoria.service.UrvService;
import br.jus.jfsp.nuit.contadoria.util.ManipulaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Component
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/importa")
@EnableScheduling
public class CronController {

	@Autowired
	private BtnMensalService btnMensalService;

	@Autowired
	private InpcService inpcService;
	
	@Autowired
	private Ipca15Service ipca15Service;
	
	@Autowired
	private IpcaService ipcaService;
	
	@Autowired
	private IpcaEService ipcaEService;
	
	@Autowired
	private IrsmService irsmService;
	
	@Autowired
	private SalarioMinimoService salarioMinimoService;
	
	@Autowired
	private SelicMetaCopomService selicMetaCopomService;

	@Autowired
	private SelicMensalService selicMensalService;

	@Autowired
	private TrdService trdService;

	@Autowired
	private TrMensalService trMensalService;

	@Autowired
	private UfirService ufirService;
	
	@Autowired
	private UrvService urvService;

	@Autowired
	private IndicesAtrasadosService indicesAtrasadosService;

	@Autowired
	private IndicesRes134Service indicesRes134Service;

	@Autowired
	private IndicesCondService indicesCondService;

	@Autowired
	private IndicesSalariosService indicesSalariosService;

	@Autowired
	private JurosService jurosService;

	@Autowired
	private JurosAltService jurosAltService;

	@Autowired
	private IndicesConsolidadosService indicesConsolidadosService;


	//@Scheduled(cron = "0 0 8 ? * *")

//	@Scheduled(cron = "0 0/1 * 1/1 * ?")

	//@Scheduled(cron = "0/10 0/1 * 1/1 * ?")

	@GetMapping("/tudo")
	public void importaTudo() {
		try {
			System.out.println("Começo " + ManipulaData.getHoje());
			btnMensalService.importa();
			inpcService.importa();
			ipca15Service.importa();
			ipcaService.importa();
			ipcaEService.importa();
			irsmService.importa();
			salarioMinimoService.updateMoedas();
			salarioMinimoService.importa();
			selicMetaCopomService.importa();
			selicMensalService.importa();
			trdService.importa();
			trMensalService.importa();
			ufirService.importa();
			urvService.importa();
			indicesAtrasadosService.importa();
			indicesRes134Service.importa();
			indicesRes134Service.calculaAcumulados();
//		indicesRes134Service.testando();
			indicesSalariosService.importa();
			indicesSalariosService.calculaAcumulados();
//		indicesSalariosService.testando();
			indicesCondService.importa();
			indicesCondService.calculaAcumulados();
//		indicesCondService.testando();

			jurosService.importa();
//		jurosService.testando();
			jurosAltService.importa();

			indicesConsolidadosService.importa();
			//indicesConsolidadosService.mostraCSV();

			System.out.println("Fim " + ManipulaData.getHoje());
		} catch (Exception e) {

		}


	}

}