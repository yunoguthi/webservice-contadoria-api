package br.jus.jfsp.nuit.contadoria.controllers;

import br.jus.jfsp.nuit.contadoria.service.BtnMensalService;
import br.jus.jfsp.nuit.contadoria.service.InpcService;
import br.jus.jfsp.nuit.contadoria.service.Ipca15Service;
import br.jus.jfsp.nuit.contadoria.service.IpcaEService;
import br.jus.jfsp.nuit.contadoria.service.IpcaService;
import br.jus.jfsp.nuit.contadoria.service.IrsmService;
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

@Component
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
	
	//@Scheduled(cron = "0 0 8 ? * *")

	@Scheduled(cron = "0 0/1 * 1/1 * ?")

	public void importaTudo() throws Exception {
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
		System.out.println("Fim " + ManipulaData.getHoje());

		
	}

}
