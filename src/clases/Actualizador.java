package clases;

import java.util.Iterator;
import java.util.Queue;

/*
 * Clase para modelar el primer Thread
 * Este thread se encargará de ir actualizando el estado de la tabla de paginas y los marcos de pagina en RAM 
 * de acuerdo a las referencias generadas por el proceso. 
 * 
 * Especificaciones:
 * 
 * * Este thread debe cargar una nueva referencia cada milisegundo
 */
public class Actualizador extends Thread{

	/*
	 * Atributos
	 * memoriaReal Memoria real bajo la cual el thread cargará los marcos de pagina
	 * secuencia Secuencia de marcos de pagina desde donde se cargará la información
	 */
	private RAM memoriaReal;
	private Queue<Pagina> secuencia;

	public Actualizador(RAM r, Queue<Pagina> sec) 
	{
		this.memoriaReal = r;
		this.secuencia = sec; 
	}

	@Override
	public void run() 
	{
		try 
		{
			// Iterar sobre la secuencia e ir cargando cada una de las paginas
			Iterator<Pagina> it = secuencia.iterator();
			while(it.hasNext()) 
			{
				Pagina i = it.next();
				Thread.sleep(1); // Este thread debe cargar una referencia cada milisegundo
				memoriaReal.cargarPagina(i);
			}
			
			System.out.println("El numero de fallos de pagina es de: " + memoriaReal.getnFallos());
		}
		catch(Exception e) 
		{
			System.out.println("Error: \n >");
			e.printStackTrace();
		}

		// Cargar cada pagina de ella
		// Cargar una referencia cada milisegundo
	}

}
