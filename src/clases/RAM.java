package clases;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/*
 * Clase para modelar la tabla de paginas de la memoria real RAM
 * Por simplicidad la clase se nombró RAM
 */

public class RAM {

	/*
	 * Atributos
	 * marcosPagina Numero de marcos de pagina en memoria real
	 * memoria Cola donde estarán almacenadas las paginas, TP
	 * nFallos Numero de fallos de pagina
	 * monitor Monitor para manejar la sincronización 
	 */

	private int marcosPagina; // Numero de marcos de pagina de la TP en RAM 
	Queue<Pagina> memoria;
	private int nFallos;

	/*
	 * Metodo constructor
	 */
	public RAM(int nMarcosPagina) 
	{
		this.marcosPagina = nMarcosPagina;
		memoria = new LinkedList<Pagina>();
		this.nFallos = 0; // Al comienzo no hay nada cargado i.e no hay fallos cuando se inicializa
	}

	/*
	 * Metodo para cargar paginas en memoria real
	 * Implicitamente todas las referencias de la secuencia son marcadas como 'no en memoria'
	 * @param p Pagina que se desea cargar
	 */
	public void cargarPagina(Pagina p) 
	{
		synchronized(this) 
		{
			Pagina entrante = p;
			/*
			 * Caso 1: La pagina no está cargada y la TP aun no está llena:
			 */
			if(estaCargada(entrante) == false && memoria.size() < marcosPagina) 
			{
				nFallos++;
				imprimirFalloDePagina(entrante);
				memoria.add(entrante);

			}
			/*
			 * Caso 2: La pagina no está cargada y la TP está llena
			 */
			else if(estaCargada(entrante) == false && memoria.size() == marcosPagina) 
			{
				nFallos++;
				NRUReplace(entrante);
			}
			/*
			 * Caso 3: Reemplaza el bit m de la pagina que ya está cargada en caso de ser diferente
			 */
			else if(estaCargada(entrante) == true) 
			{
				if(entrante.getBitM() != getPagina(entrante.getNumPag()).getBitM()) 
				{
					getPagina(entrante.getNumPag()).setBitM(entrante.getBitM());
				}
				
			}

		}

	}

	/*
	 * NRU Pagement Replacement Algorithm - Not Recently Used
	 * Atencion: Este método solo tiene sentido usarlo cuando la TP está llena
	 */
	public void NRUReplace(Pagina p) 
	{
		// 1. Tomar todas las paginas y agruparlas por clases.
		// 	  Las clases estarán agrupadas usando ArrayLists

		ArrayList<Pagina> clase0 = new ArrayList<>(); // R: 0, M: 0
		ArrayList<Pagina> clase1 = new ArrayList<>(); // R: 0, M: 1
		ArrayList<Pagina> clase2 = new ArrayList<>(); // R: 1, M: 0
		ArrayList<Pagina> clase3 = new ArrayList<>(); // R: 1, M: 1

		Iterator<Pagina> it = memoria.iterator();
		while(it.hasNext()) 
		{
			Pagina i = it.next();
			if(i.getClase() == 0) 
			{
				clase0.add(i);
			}
			else if(i.getClase() == 1) 
			{
				clase1.add(i);
			}
			else if(i.getClase() == 2) 
			{
				clase2.add(i);
			}
			else if(i.getClase() == 3) 
			{
				clase3.add(i);
			}
		}

		// 2. Sacar una pagina de la menor clase 
		while(true) 
		{
			if(clase0.size() > 0) 
			{
				System.out.println("Pagina "+ clase0.get(0).getNumPag() +" reemplazada");
				memoria.remove(clase0.get(0));
				break;
			}
			if(clase1.size() > 0) 
			{
				System.out.println("Pagina "+ clase1.get(0).getNumPag() +" reemplazada");
				memoria.remove(clase1.get(0));
				break;
			}
			if(clase2.size() > 0) 
			{
				System.out.println("Pagina "+ clase2.get(0).getNumPag() +" reemplazada");
				memoria.remove(clase2.get(0));
				break;
			}
			if(clase3.size() > 0) 
			{
				System.out.println("Pagina "+ clase3.get(0).getNumPag() +" reemplazada");
				memoria.remove(clase3.get(0));
				break;
			}
			else 
			{
				break; // Revisar
			}

		}

		memoria.add(p); // Carga la pagina despues de que aquella de menor clase haya sido removida. 
		imprimirCargadoDePagina(p);
	}

	/*
	 * Metodo para poner el bit R de todas las paginas en memoria en 0
	 * Tanenbaum dice: "Periodically (e.g., on each clock interrupt), the R bit is cleared"
	 * Atencion: Este metodo se usa unicamente cada periodo definido, preguntar a los monitores
	 */
	public void clearRBits()
	{
		synchronized(this) 
		{
			Iterator<Pagina> it = memoria.iterator();
			while(it.hasNext()) 
			{
				Pagina i = it.next();
				i.setBitR(false);
			}
		}



	}

	/*
	 * Metodo para saber si una pagina ya se encuentra cargada en memoria
	 * @param p Pagina que se desea saber si está cargada
	 * @return true si sí está cargada, false de lo contrario
	 */
	public boolean estaCargada(Pagina p) 
	{
		Iterator<Pagina> it = memoria.iterator();
		boolean existe = false; 
		while(it.hasNext() && !existe) 
		{
			Pagina i = it.next();
			if(i.getNumPag() == p.getNumPag()) // Caso 2.1: La pagina ya se encuentra cargada 
			{
				existe = true; 
			}
		}

		return existe;
	}

	/*
	 * Metodo para obtener una pagina 
	 * @param n numero de la pagina que se desea buscar
	 * @return Pagina buscada 
	 */
	public Pagina getPagina(int n) 
	{
		Pagina pagRetorno = null; 
		Iterator<Pagina> it = memoria.iterator();

		while(it.hasNext()) 
		{
			Pagina i = it.next();
			if(i.getNumPag() == n) 
			{
				pagRetorno = i; 
				break; // No pueden haber dos mismas paginas cargadas en memoria no tiene sentido 
			}
		}

		return pagRetorno;
	}

	/*
	 * Metodo para imprimir la cargada de una pagina
	 * @param p Pagina que se desea imprimir
	 * Usar solo cuando una pagina sea cargada
	 */
	public void imprimirCargadoDePagina(Pagina p) 
	{
		String bR = "0";
		String bM = "0";

		if(p.getBitR() == true) 
		{
			bR = "1";
		}
		if(p.getBitM() == true) 
		{
			bM = "1";
		}

		System.out.println("[*] Se ha cargado una pagina. Pagina: "+p.getNumPag());
		System.out.println("	> Bit R: " + bR);
		System.out.println("	> Bit M: " + bM);
	}

	/*
	 * Imprime de forma atractiva el fallo de pagina de una pagina
	 * @param p Pagina a imprimir fallo de pagina
	 */
	public void imprimirFalloDePagina(Pagina p) 
	{
		System.out.println("[*] Se ha generado un fallo de pagina. Pagina: "+p.getNumPag());
	}

	/*
	 * Retorna el numero de fallos de pagina que ha habido
	 * Atencion: Solo se usa al final de la ejecucion del programa general
	 * @return nFallos numero de fallos de pagina 
	 */
	public int getnFallos() {
		return nFallos;
	}


}
