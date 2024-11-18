package com.orcronics.Software_Medico.utility;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

//import jakarta.imageio.ImageIO;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
//import jakarta.xml.bind.DatatypeConverter;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

public class Utilities {
	

	/**
	 * M�todo modificado: Guarda imagen de canvas desde base64String.
	 * @param base64String
	 * @param request
	 * @param nombreFirma
	 * @return
	 */
	/*
	public static String guardarImagen(String base64String, HttpServletRequest request, String nombreFirma) {
		String[] strings = base64String.split(",");
		//convert base64 string to binary data
        byte[] data = DatatypeConverter.parseBase64Binary(strings[1]);
        
        String rutaFinal = request.getServletContext().getRealPath("/resources/multimedia/");
        nombreFirma = nombreFirma+".png";
        String path = rutaFinal+nombreFirma;*/
        /*File file = new File(path);
        try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
            outputStream.write(data);
            return "m1 test_image.png";
        } catch (IOException e) {
        	System.out.println("Error method guardarImagen base64: " + e.getMessage());
            e.printStackTrace();
        }*/
        
      /*  try {
	        BufferedImage image = ImageIO.read(new ByteArrayInputStream(data));
	        // write the image to a file
	        File outputfile = new File(path);
	        ImageIO.write(image, "png", outputfile);
	        return nombreFirma;
        }catch(Exception e) {
            System.out.println("Error no se pudo guardar imagen base64: "+e.getStackTrace());
        }
        
		return null;
	}*/
	
	
	
	
	/**
	 * Metodo para generar una cadena Alfanumerica aleatoria de longitud N
	 * @param count
	 * @return
	 */
	public static String randomAlphaNumeric(int count) {
		String CARACTERES = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		StringBuilder builder = new StringBuilder();
		while (count-- != 0) {
			int character = (int) (Math.random() * CARACTERES.length());
			builder.append(CARACTERES.charAt(character));
		}
		return builder.toString();
	}
	
	
	/**
	 * Metodo para generar una cadena Num�rica aleatorio de longitud N
	 * @param count
	 * @return
	 */
	public static String randomNumeric(int count) {
		String CARACTERES = "0123456789";
		StringBuilder builder = new StringBuilder();
		while (count-- != 0) {
			int character = (int) (Math.random() * CARACTERES.length());
			builder.append(CARACTERES.charAt(character));
		}
		return builder.toString();
	}
	

	/**
	 * Restar FECHA ACTUAL y fecha par�metro, obteniendo diferencia de d�as.
	 * @param fecha variable
	 * @return obtener difernecia de d�as
	 * @throws ParseException 
	 */
	public static long obtenerDiferenciaDeDias(Date fecha) {
		try {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
		//Parseamos.
		Date fechaActual = sdf.parse(sdf.format(new Date()));
		fecha = sdf.parse(sdf.format(fecha));
		//Diferencia.
		long diff = fechaActual.getTime() - fecha.getTime();
		//Conversion de milisegundos al tiempo.
		TimeUnit time = TimeUnit.DAYS;
		long diference = time.convert(diff, TimeUnit.MILLISECONDS);
		System.out.println("Difernecia de d�as: "+diference);
		return diference;
		} catch (Exception e) {
			System.out.println("Error parseando fechas para obtenerDiferenciaDeDias.");
			return 0;
		}
	}
	
	
	/**
	 * Guarda imagen a trav�s de un formulario HTML en el disco duro.
	 * @param multiPart
	 * @param request
	 * @return
	 */
	public static String guardarExcel(MultipartFile multiPart, HttpServletRequest request) {
		// Obtenemos el nombre original del archivo.
		String nombreOriginal = multiPart.getOriginalFilename();
		// Reemplazamos en el nombre del archivo los espacios por guiones.
		//nombreOriginal = nombreOriginal.replace(" ", "-");
		// Agregamos al nombre del archivo 8 caracteres aleatorios para evitar duplicados.
		String nombreFinal = randomAlphaNumeric(8)+nombreOriginal;
		//String nombreFinal = "excel_a_leer";
		// Obtenemos la ruta ABSOLUTA del directorio images.
		// apache-tomcat/webapps/cineapp/resources/images/
		String rutaFinal = request.getServletContext().getRealPath("/resources/excel/");
		//String rutaFinal = "D:\\E-commerce - GoFindYourKey\\test-upload-photo\\";
		try {
			// Formamos el nombre del archivo para guardarlo en el disco duro.
			File imageFile = new File(rutaFinal + nombreFinal);
			System.out.println(imageFile.getAbsolutePath());
			// Aqui se guarda fisicamente el archivo en el disco duro.
			multiPart.transferTo(imageFile);
			return nombreFinal;
		} catch (IOException e) {
			System.out.println("Error method guardarExcel: " + e.getMessage());
			return null;
		}
	}
	
	
	
	/**
	 * Obtener la ruta absoluta de la webapp, pasando el par�metro PATH necesario.
	 * Ejemplo: "/", "/resources/", etc.
	 * @param path
	 * @return
	 */
	public static String getContextWebApp(String path) {
		//Obtener ContextPath de la webapp.
		WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        ServletContext context = webApplicationContext.getServletContext();
        return context.getRealPath(path);
	}
	
	
	/**
     * Sumar una cantidad de dias (int) a una fecha dada (DATE).
     * @param fecha Date
     * @param dias int
     * @return String
     */
    public static String sumarDiasAFecha(Date fecha, int dias) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha); // Configuramos la fecha que se recibe
        calendar.add(Calendar.DAY_OF_YEAR, dias);  // numero de días a añadir, o restar en caso de días<0
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(calendar.getTime());
    }
	
    

	public static String subirArchivo(MultipartFile multiPart, String nombre, String rutaMultimedia) throws IOException {
		// Obtenemos el nombre original del archivo.
		String nombreOriginal = multiPart.getOriginalFilename();
		// Reemplazamos en el nombre del archivo los espacios por guiones.
		nombreOriginal = nombreOriginal.replace(" ", "-");
		// Agregamos al nombre del archivo 8 caracteres aleatorios para evitar duplicados.
		String nombreFinal = nombre+"_"+randomAlphaNumeric(4)+"_"+nombreOriginal;
		try {
			// Formamos el nombre del archivo para guardarlo en el disco duro.
			File file = new File(rutaMultimedia+"/"+ nombreFinal);
			System.out.println(file.getAbsolutePath());
			// Aqui se guarda fisicamente el archivo en el disco duro.
			multiPart.transferTo(file); System.out.println("fichero upload...");
			return nombreFinal;
		} catch (IOException e) {
			System.out.println("Error method subirArchivo multipart: " + e.getMessage());
			return null;
		}
	}
	
	public static String subirArchivoConExtensionPNG(MultipartFile multiPart, String nombre, String rutaMultimedia) throws IOException {
		// Obtenemos el nombre original del archivo.
		String nombreOriginal = multiPart.getOriginalFilename();
		// Reemplazamos en el nombre del archivo los espacios por guiones.
		nombreOriginal = nombreOriginal.replace(" ", "-");
		// Agregamos al nombre del archivo 8 caracteres aleatorios para evitar duplicados.
		String nombreFinal = nombre+"_"+randomAlphaNumeric(4)+"_"+nombreOriginal+".png";
		try {
			// Formamos el nombre del archivo para guardarlo en el disco duro.
			File file = new File(rutaMultimedia+"/"+ nombreFinal);
			System.out.println(file.getAbsolutePath());
			// Aqui se guarda fisicamente el archivo en el disco duro.
			multiPart.transferTo(file); System.out.println("fichero upload...");
			return nombreFinal;
		} catch (IOException e) {
			System.out.println("Error method subirArchivo multipart: " + e.getMessage());
			return null;
		}
	}

	
	/**
	 * Convirte Bytes a formato legible.
	 * @param bytes
	 * @return
	 */
	public static String convertirBytes(long bytes) {
	    int unidad = 1024;
	    if (bytes < unidad) {
	        return bytes + " B";
	    }
	    int exp = (int) (Math.log(bytes) / Math.log(unidad));
	    char prefijo = "KMGTPE".charAt(exp - 1);
	    return String.format("%.1f %sB", bytes / Math.pow(unidad, exp), prefijo);
	}
	
    
}
