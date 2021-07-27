package br.com.jfsp.nuit.contadoria.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.jfsp.nuit.contadoria.services.InpcService;
import br.com.jfsp.nuit.contadoria.services.Ipca15Service;
import br.com.jfsp.nuit.contadoria.services.IpcaEService;
import br.com.jfsp.nuit.contadoria.services.IpcaService;
import br.com.jfsp.nuit.contadoria.services.IrsmService;
import br.com.jfsp.nuit.contadoria.services.SalarioMinimoService;
import br.com.jfsp.nuit.contadoria.services.SelicMetaCopomService;
import br.com.jfsp.nuit.contadoria.services.TrdService;
import br.com.jfsp.nuit.contadoria.services.UfirService;
import br.com.jfsp.nuit.contadoria.services.UrvService;
import br.com.jfsp.nuit.contadoria.util.ManipulaData;
import org.springframework.scheduling.annotation.EnableScheduling;

@Component
@EnableScheduling
public class CronController {
	
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
	private TrdService trdService;
	
	@Autowired
	private UfirService ufirService;
	
	@Autowired
	private UrvService urvService;
	
	//@Scheduled(cron = "0 0 8 ? * *")
	@Scheduled(cron = "0 0/1 * 1/1 * ?")
	public void importaTudo() throws Exception {
		System.out.println("Começo " + ManipulaData.getHoje());
		inpcService.importa();
		ipca15Service.importa();
		ipcaService.importa();
		ipcaEService.importa();
		irsmService.importa();
		salarioMinimoService.importa();
		selicMetaCopomService.importa();
		trdService.importa();
		ufirService.importa();
		urvService.importa();
		System.out.println("Fim " + ManipulaData.getHoje());

		
	}

}
