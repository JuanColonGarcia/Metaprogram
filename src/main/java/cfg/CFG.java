package cfg;

import java.util.ArrayList;
import java.util.List;

public class CFG {
	
	public List<ArcoCFG> arcos = new ArrayList<ArcoCFG>();
	public List<NodoCFG> nodosAnteriores = new ArrayList<>();
	
	public int idActual = 0;
	public NodoCFG nodoAnterior = null;
	public NodoCFG nodoActual = null;


////////////////////////CREAR NODOS//////////////////////////////////////
	
	// Al crear un CFG se crea un nodo "start" y un arco desde ese nodo que apunta a un nodo null
	public CFG(){
		nodoAnterior = new NodoCFG(idActual,"Start");
	}

	
	public void crearNodo(Object objeto)
	{
		idActual++; //Contador de nodos, indicando el ID actual
		
		//Crea un nuevo nodo usando el ID actual
		nodoActual = new NodoCFG(idActual,quitarComillas(objeto.toString()));
		crearArcos();
		
		//Limpia lista de nodos anteriores
		nodosAnteriores.clear();
		
		//Se añade el nodo recien creado
		nodosAnteriores.add(nodoActual);
		
		//Actualiza la referencia del nodo anterior
		nodoAnterior = nodoActual;		
	}
	
	
	public void añadirNodoFinal() {
	    idActual++;
	    NodoCFG nodofinal = new NodoCFG(idActual, "Stop");
	    //Recorre sobre todos los nodos en la lista nodosAnteriores
	    for (NodoCFG nodo : nodosAnteriores) {
	        ArcoCFG arcofinal = new ArcoCFG(nodo, nodofinal); //Crea un arco (arcofinal) desde cada nodo en nodosAnteriores al nodo final (nodofinal).
	        arcos.add(arcofinal);
	    }
	}
			
	// Sustituye " por \" en un string: Sirve para eliminar comillas.
	private String quitarComillas(String texto)
	{
	    return texto.replace("\"", "\\\"");
	}
	
//////////////////////METODOS PARA CREAR ARCOS////////////////////////////////////////////	
	
	// Crear arcos
	private void crearArcos()
	{
			añadirArcoSecuencialCFG();
	}

	
	// Añade un arco desde el último nodo hasta el nodo actual (se le pasa como parametro)
	public void añadirArcoSecuencialCFG()
	{	
		//Recorre cada nodo en la lista nodosAnteriores.
	    for (NodoCFG nodo : nodosAnteriores) {
	    	
	    	//Genera arco nodoAnteriores --> nodoActual
			ArcoCFG arco = new ArcoCFG(nodo ,nodoActual);
			arcos.add(arco);
			
		}
	}	
	
	// Crea un arco desde el último nodo al nodo especificado
	public void crearArcoDesdeUltimoNodo(NodoCFG nodoDestino) {
		
    	//Genera arco nodoAnterior --> nodoDestino
		ArcoCFG arco = new ArcoCFG(nodoAnterior, nodoDestino);
		arcos.add(arco);
		
	}
	
	// Crea un arco desde el último nodo al nodo especificado
	public void crearArcoDesdePrimerNodo(NodoCFG nodoDestino) {
		
    	//Genera arco nodoActual --> nodoDestino
		ArcoCFG arco = new ArcoCFG(nodoActual, nodoDestino);
		arcos.add(arco);
		
	}
	
////////////////////////////METODOS PARA GESTION DE NODOS////////////////////////////////////////////	
	
	// Devuelve el 'nodoActual'
	public NodoCFG getNodoActual() {
		return nodoActual;
	}
	
	
	// Establece el nodo anterior con el nodo proporcionado y lo añade a la lista de nodos anteriores
	public void setNodoAnterior(NodoCFG nodo) {
	    this.nodoAnterior = nodo;
		this.nodosAnteriores.add(nodo);

	}
	
	
	// Devuelve el último nodo almacenado en 'nodoAnterior'
	public List<NodoCFG> getNodoAnterior() {
		    List<NodoCFG> nodos = new ArrayList<>();
		        nodos.add(nodoAnterior);
	    return nodos;
	}

	//añadan nuevos nodos a la lista nodosAnteriores si no lo están
	public void añadirListaNodosAnteriores(List<NodoCFG> nodos) {
	    for (NodoCFG nodo : nodos) { //Itera sobre cada nodo en la lista
	        if (!this.nodosAnteriores.contains(nodo)) { //Verifica si el nodo actual no está ya contenido en la lista
	            this.nodosAnteriores.add(nodo);
	        }
	    }
	}
		
//////////////////////VISUALIZACIÓN////////////////////////////////////////////	

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