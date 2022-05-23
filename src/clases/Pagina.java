package clases;

public class Pagina {
	
	/**
	 * Atributos
	 * numeroID Numero en el marco de paginas de la memoria virtual
	 * bitR Bit R de leido
	 * bitM Bit M de modificado
	 * bitR & BitM son booleanos true = 1 & false = 0
	 */
	
	private int numeroID; 
	private boolean bitR;
	private boolean bitM;
	private char bitRM;
	
	public Pagina(int numId, char bit) 
	{
		numeroID = numId;
		bitR = false;
		bitM = false; 
		bitRM = bit;
		
		if(bitRM == 'r') 
		{
			this.bitR = true; 
		}
		if(bitRM == 'm') 
		{
			this.bitM = true;
		}
		
	}
	
	
	/*
	 * Métodos
	 */
	
	/*
	 * Metodo set para el bit R
	 * @param r boolean
	 */
	public void setBitR(boolean r) 
	{
		bitR = r;
		
	}
	
	/*
	 * Metodo set para el bit M
	 * @param m boolean
	 */
	public void setBitM(boolean m)
	{
		bitM = m;
	}
	
	/*
	 * Metodo get para el bit M
	 */
	public boolean getBitM() 
	{
		return bitM;
	}
	
	/*
	 * Metodo get para el bit R
	 */
	public boolean getBitR() 
	{
		return bitR;
	}
	
	/*
	 * Metodo get para el numero de pagina referenciada
	 */
	public int getNumPag() 
	{
		return numeroID; 
	}
	
	/*
	 * Metodo para imprimir atributos
	 */
	public String getAtts() 
	{
		String bR = "0";
		String bM = "0";
		
		if(getBitR() == true) 
		{
			bR = "1";
		}
		if(getBitM() == true) 
		{
			bM = "1";
		}
		return "Pagina: " +Integer.toString(numeroID) + " | " + "Bit R: " + bR + " | " + "Bit M: " + bM;
		// return "Pagina: " +Integer.toString(numeroID) + " | " + bitRM;
		
	}
	
	/*
	 * Metodo para obtener la clase de la pagina
	 * Auxiliar para el algoritmo NRU 
	 * @return Clase de la pagina
	 */
	public int getClase() 
	{
		int clase = 0;
		
		/*
		 * Clase 0
		 * bit R -> 0
		 * bit M -> 0 
		 */
		if(bitR == false && bitM == false) 
		{
			clase = 0;
		}
		/*
		 * Clase 1
		 * bit R -> 0
		 * bit M -> 1 
		 */
		else if(bitR == false && bitM == true) 
		{
			clase = 1;
		}
		/*
		 * Clase 2
		 * bit R -> 1
		 * bit M -> 0 
		 */
		else if(bitR == true && bitM == false) 
		{
			clase = 2;
		}
		/*
		 * Clase 3
		 * bit R -> 1
		 * bit M -> 1 
		 */
		else if(bitR == true && bitM == true) 
		{
			clase = 3;
		}
		
		
		return clase; 
	}
	
	

}
