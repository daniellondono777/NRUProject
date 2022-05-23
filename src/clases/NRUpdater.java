package clases;

import java.util.Iterator;

/*
 * Clase para modelar el segundo thread
 * El segundo thread se encargará de actualizar los bits R y M
 * con base en el algoritmo de reemplazo de Tanenbaum.
 * 
 * Este Thread debe correr cada 20 milisegundos
 */
public class NRUpdater extends Thread{
	
	/*
	 * Atributos
	 * memoriaReal Memoria real bajo la cual el thread actualizará los marcos de pagina
	 */
	private RAM memoriaReal;
	
	public NRUpdater(RAM memoria) 
	{
		this.memoriaReal = memoria; 
	}
	
	@Override
	public void run() 
	{
		try 
		{
			// Iterar sobre los marcos de pagina de la memoria y settear los bits R a 0
			Thread.sleep(20);
			Iterator<Pagina> it = memoriaReal.memoria.iterator();
			while(it.hasNext()) 
			{
				Pagina i = it.next();
				i.setBitR(false); // false es 0
			}
		}
		catch(Exception e) 
		{
			System.out.println("Error: \n >");
			e.printStackTrace();
		}
	}

}
