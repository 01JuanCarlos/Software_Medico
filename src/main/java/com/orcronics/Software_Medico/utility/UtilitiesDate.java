package com.orcronics.Software_Medico.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class UtilitiesDate {
	

	public static long obtenerDiferenciaDeDias(Date fechaParametro) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
			//Parseamos.
			Date fechaActual = sdf.parse(sdf.format(new Date()));
			fechaParametro = sdf.parse(sdf.format(fechaParametro));
			//Obtenemos la Diferencia.
			long diff = fechaActual.getTime() - fechaParametro.getTime();
			//Conversion de milisegundos al tiempo.
			TimeUnit time = TimeUnit.DAYS;
			long diference = time.convert(diff, TimeUnit.MILLISECONDS);
			System.out.println("Method obtenerDiferneciaDeDias: "+diference);
			return diference;
		} catch (Exception e) {
			System.out.println("Error parseando en method obtenerDiferneciaDeDias.");
			return 0;
		}
	}
	
	
	/**
	 * Sumar o Restar días a una fecha:
     * Suma o Resta una cantidad de dias en positivo (+) o negativo(-) a una fecha dada (DATE).
     * @param fecha Date
     * @param dias int
     * @return String
     */
    public static String sumarRestarDiasAFecha(Date fecha, int dias) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha); // Configuramos la fecha que se recibe
        calendar.add(Calendar.DAY_OF_YEAR, dias);  // numero de días a añadir, o restar en caso de días<0
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(calendar.getTime());
    }
    
    public static String sumarRestarHorasAFecha(Date fecha, int horas) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha); // Configuramos la fecha que se recibe
        calendar.add(Calendar.HOUR, horas);  // numero de horas a añadir, o restar en caso de horas<0
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(calendar.getTime());
    }
    
    public static String sumarRestarMinutosAFecha(Date fecha, int minutos) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha); // Configuramos la fecha que se recibe
        calendar.add(Calendar.MINUTE, minutos);  // numero de horas a añadir, o restar en caso de horas<0
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(calendar.getTime());
    }
    
    
    public static String obtenerPrimerDia(Date fecha) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha); // Configuramos la fecha que se recibe
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        //calendar.add(Calendar.DAY_OF_YEAR, dias);  // numero de días a añadir, o restar en caso de días<0
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(calendar.getTime());
    }
	
    
    public static String obtenerUltimoDia(Date fecha) {
    	Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha); // Configuramos la fecha que se recibe
        calendar.set(Calendar.DAY_OF_MONTH, 1); //1er dia de la fecha dada.
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)+1); //agregamos 1 mes.
        //Damos formato y restamos 1 día, para obtener el último día del anterior.
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date ff = null;
		try {
			ff = sdf.parse(sdf.format(calendar.getTime()));
		} catch (ParseException e) {
			System.out.println(e);
			e.printStackTrace();
		}
        return sumarRestarDiasAFecha(ff, -1);
    }
    

}
