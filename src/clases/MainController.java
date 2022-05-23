package clases;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class MainController {
	
	/*
	 * Archivos de prueba
	 */
	public final static String ARCHIVO_R8_16_75 = "./data/referencias8_16_75.txt";
	public final static String ARCHIVO_R8_32_75 = "./data/referencias8_32_75.txt";
	public final static String ARCHIVO_R8_64_75 = "./data/referencias8_64_75.txt";
	public final static String ARCHIVO_R8_128_75 = "./data/referencias8_128_75.txt";
	/*
	 * Atributos
	 * numMarcosP Numero de marcos de pagina en memoria RAM
	 * numPagsProc Numero de paginas del proceso
	 * numReferencias Numero de referencias del archivo
	 * secuencia Entradas de la tabla de pagina del proceso
	 */
	private int numMarcosP;	
	private int numPagsProc; 
	private int numReferencias; 
	private Queue<Pagina> secuencia;
	// private RAM memoriaReal;
	
	public MainController() 
	{
		secuencia = new LinkedList<Pagina>();
		// memoriaReal = new RAM(numMarcosP);
	}
	
	public static void main(String[] args) 
	{
		MainController m = new MainController();
		m.readFile(); 
		m.inicio();
		
		
	}
	
	public void readFile() 
	{
		try 
		{
			/*
			 * Archivos:
			 * 	ARCHIVO_R8_16_75
			 * 	ARCHIVO_R8_32_75
			 * 	ARCHIVO_R8_64_75
			 * 	ARCHIVO_R8_128_75
			 */
			File f = new File(ARCHIVO_R8_128_75);
			Scanner r  = new Scanner(f);
			int c = 0;
			
			while(r.hasNextLine()) 
			{
				String dato = r.nextLine();
				if(c == 0) 
				{
					numMarcosP = Integer.parseInt(dato);
					c++;
				}
				else if(c == 1) 
				{
					numPagsProc = Integer.parseInt(dato);
					c++;
				}
				else if(c == 2) 
				{
					numReferencias = Integer.parseInt(dato);
					c++;
				}
				else if(c >= 3) 
				{
					int numeroPagina = Integer.parseInt(dato.split(",")[0]);
					char bit = dato.split(",")[1].charAt(0);
					Pagina p = new Pagina(numeroPagina, bit);
					
					
					secuencia.add(p);
					/*
					 * Las paginas de las referencias son cargadas como 'no en memoria' para despues ser cargadas a la TP de la RAM (Ver documentación en RAM.java)
					 */
				}
			}
			
			r.close();
			
			System.out.println("[*] Del Archivo se obtiene que:");
			System.out.println("");
			System.out.println("");
			System.out.println("------------------------------");
			
			System.out.println("[*] Numero de Marcos de página en memoria RAM: " + numMarcosP);
			System.out.println("[*] Numero de paginas del proceso: " + numPagsProc);
			System.out.println("[*] Numero de referencias del archivo: " + numReferencias);
			
			System.out.println("-----");
			System.out.println("[*] Primeros 5 elementos de la secuencia: ");
			System.out.println("");
			
			Iterator<Pagina> it = secuencia.iterator();
			
			int i = 0; 
			while(it.hasNext() && i < 5) 
			{
				System.out.println("	> " + it.next().getAtts());
				// System.out.println("	> " + it.next().getNumPag() + it.next().getBitR() + it.next().getBitM());
				i++; 
			}
			
			System.out.println("");
			System.out.println("");
			System.out.println("------------------------------");
			
			
		}
		catch(FileNotFoundException e) 
		{
			System.out.println("Error: "); 
			e.printStackTrace();
		}
	}
	
	public void inicio() 
	{
		// Un thread debe estar agregando marcos de pagina, los debe cargar cada milisegundo
		RAM memoriaReal = new RAM(numMarcosP);
		
		Actualizador t1 = new Actualizador(memoriaReal, secuencia);
		NRUpdater t2 = new NRUpdater(memoriaReal); // El segundo thread se encargará de ejecutar el algoritmo NRU, debe cargar cada 20 milisegundos
		
		t1.start();
		t2.start();
		
		try 
		{
			t1.join();
			t2.join();
		}
		
		catch(Exception e) 
		{
			e.printStackTrace();
		}
		
		
	}

}
