package com.orcronics.Software_Medico.service.scheduled;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.orcronics.Software_Medico.service.CitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@EnableScheduling
public class TaskScheduledService {

	
private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	
	/** 
	 * Formato del scheduled para ejecución en segundo plano con asyn.
	 * @Scheduled(cron = "[Seconds] [Minutes] [Hours] [Day of month] [Month] [Day of week]")
	 */
		
	//@Scheduled (cron = "0 0 */24 * * *") //Cada 24 horas
	//@Scheduled (cron = "0 */1 * * * *") //Cada 1 minuto.
		
	@Autowired
	private CitaService citaService;


	/**
	 * Se cambia el valor del campo "cerrado" de false a true, siendo así que no se aceptan modificaciones a la cita.
	 * Las modificaciones serán permitidas dutante el mismo hasta las 11PM.
	 * @throws ParseException 
	 */
	@Transactional
	@Async("asyncExecutor")
	@Scheduled (cron = "0 0 23 * * ?") //A las 11:00:00 PM - Todos los días.
	public void cerrarModificacionDeCitas() throws ParseException {
		Date hoy = new Date();
		try {
			citaService.cerrarModificacionDeCitas(hoy);
			System.out.println("Cerrando la modificación de citas.");
		} catch (Exception e) {
			System.out.println("Ocurrió un error cerrando la modificación de citas.");
			System.out.println(e);
		}
	}
	
	
	
}
