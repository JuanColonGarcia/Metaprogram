package cfg;

import java.util.ArrayList;
import java.util.List;

public class CFG {
	
	private List<ArcoCFG> arcos = new ArrayList<ArcoCFG>();
	private List<NodoCFG> nodosAnteriores = new ArrayList<>();
	
	public int idActual = 0;
	private NodoCFG nodoAnterior = null;
	public NodoCFG nodoActual = null;

	

	
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
	
	//JUANNNNNNN//
	
	// Devuelve el último nodo almacenado en 'nodoAnterior'
	public List<NodoCFG> getNodoAnterior() {
		    List<NodoCFG> nodos = new ArrayList<>();
		        nodos.add(nodoAnterior);
	    return nodos;
	}

	
	// Establece el nodo anterior con el nodo proporcionado
	public void setNodoAnterior(NodoCFG nodo) {
	    this.nodoAnterior = nodo;
	}
	
	public void addListaNodosAnteriores(List<NodoCFG> nodos) {
	    for (NodoCFG nodo : nodos) {
	        if (!this.nodosAnteriores.contains(nodo)) { // Verifica si el nodo ya existe
	            this.nodosAnteriores.add(nodo);
	        }
	    }
	}
	// Crea un arco desde el último nodo al nodo especificado
	public void crearArcoDesdeUltimoNodo(NodoCFG nodoDestino) {
		ArcoCFG arco = new ArcoCFG(nodoAnterior, nodoDestino);
		arcos.add(arco);
	}
	
	// Crea un arco desde el último nodo al nodo especificado
	public void crearArcoDesdePrimerNodo(NodoCFG nodoDestino) {
		ArcoCFG arco = new ArcoCFG(nodoAnterior, nodoDestino);
		arcos.add(arco);
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
}