package cfg;

import java.util.ArrayList;
import java.util.List;

public class CFG {
	
	private List<ArcoCFG> arcos = new ArrayList<ArcoCFG>();
	
	private int idActual = 0;
	private NodoCFG nodoAnterior = null;
	public NodoCFG nodoActual = null;
	public NodoCFG nodoSiguiente = null;

	

	
	// Al crear un CFG se crea un nodo "start" y un arco desde ese nodo que apunta a un nodo null
	public CFG(){
		nodoAnterior = new NodoCFG(idActual,"Start");
	}
	
	// Crear nodo
	// Añade un arco desde el nodo actual hasta el último control
	public void crearNodo(Object objeto)
	{
		idActual++; //Contador de nodos, indicando el ID actual
		nodoActual = new NodoCFG(idActual,quitarComillas(objeto.toString()));
		// OPCIONAL: Imprimir los nodos cada vez que se crean
		//System.out.println("NODO: " + nodoActual.imprimir());
		crearArcos();
		nodoAnterior = nodoActual;
		
	}
		
	// Sustituye " por \" en un string: Sirve para eliminar comillas.
	private String quitarComillas(String texto)
	{
	    return texto.replace("\"", "\\\"");
	}
	
	// Crear arcos
	private void crearArcos()
	{
			añadirArcoSecuencialCFG();
	}

	
	// Añade un arco desde el último nodo hasta el nodo actual (se le pasa como parametro)
	public void añadirArcoSecuencialCFG()
	{	
		ArcoCFG arco = new ArcoCFG(nodoAnterior,nodoActual);
		arcos.add(arco);
	}	
	
		
	public void añadirNodoFinal() {
		idActual++;
		NodoCFG nodofinal = new NodoCFG(idActual,"Stop");
		ArcoCFG arcofinal = new ArcoCFG(nodoAnterior,nodofinal);

		arcos.add(arcofinal);
	}	
	


	
	// Obtiene el grafo en formato DOT (String)
	public String obtenerGrafo()
	{
		String dotInfo="";
		for(ArcoCFG arco:arcos) {
			dotInfo += arco.imprimir();	
		}
		return dotInfo;
	}
	
	// Imprime el grafo en la pantalla
	public void imprimirGrafo()
	{
		System.out.println("\nARCOS del CFG:");

		String dotInfo="";
		for(ArcoCFG arco:arcos) {
			dotInfo += arco.imprimir();	
			System.out.println("ARCO: "+arco.imprimir());
		}
		System.out.println("\nCFG completo:");
		System.out.println(dotInfo);
	}
	
	
	
	////JUANNNNNNNN

	
	//Creo un nodo que es el siguiente
	public void añadirArcoDirigidoCFG(NodoCFG nodoAnterior, NodoCFG nodoActual)
	{
	    nodoSiguiente = new NodoCFG(idActual + 1, "Next Node");
		ArcoCFG arco = new ArcoCFG(nodoAnterior,nodoActual);
		ArcoCFG arcoDirigido = new ArcoCFG(nodoActual,nodoSiguiente);
		nodoActual = nodoSiguiente;
	    idActual++; 

		
		
		// OPCIONAL: Imprimir los nodos cada vez que se crean
		//System.out.println("NODO: " + nodoActual.imprimir());
		arcos.add(arco);
		arcos.add(arcoDirigido);


	}
	
	
	public void añadirNodoFinalThen() {
		idActual++;
		NodoCFG nodofinalThen = new NodoCFG(idActual,"End then");
		ArcoCFG arcofinalThen = new ArcoCFG(nodoAnterior,nodofinalThen);

		arcos.add(arcofinalThen);
	}
	

}
