package generadorCFG;
	
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;

import cfg.CFG;
import cfg.NodoCFG;



public class Visitador extends ModifierVisitor<CFG>
{	
	/********************************************************/
	/********************** Atributos ***********************/
	/********************************************************/
	
	/********************************************************/
	/*********************** Metodos ************************/
	/********************************************************/

	// Visitador de métodos
	// Este visitador añade el nodo final al CFG	
	@Override	
	public Visitable visit(MethodDeclaration methodDeclaration, CFG cfg) //metodo
	{
	    // Visitamos el método objeto
		Visitable v = super.visit(methodDeclaration, cfg);
		
		// Añadimos el nodo final al CFG
		cfg.añadirNodoFinal();
		
		return v;
	}
	
	// Visitador de expresiones
	// Cada expresión encontrada genera un nodo en el CFG	
	@Override
	public Visitable visit(ExpressionStmt es, CFG cfg)
	{
		// Creamos el nodo actual
		cfg.crearNodo(es); 
		System.out.println(es);
		
		return super.visit(es, cfg);
	}
	
	// Visitador de expresiones IF	
	@Override
	public Visitable visit(IfStmt es, CFG cfg)
	{
		//Crea un nodo en el grafo para el if
		cfg.crearNodo("If (" + es.getCondition() + ")");
		
		// Se obtiene la condicion
		NodoCFG nodoIF = cfg.getNodoAnterior().get(0); 
		
		//Se explora el camino del then
		es.getThenStmt().accept(this, cfg); 

		//Obtiene ultimo nodo del camino del then
		List <NodoCFG> nodoFinalUnificacion = new ArrayList<>(cfg.getNodoAnterior());
		
	    if (!cfg.getNodoAnterior().containsAll(nodoFinalUnificacion)) {
	        cfg.añadirListaNodosAnteriores(nodoFinalUnificacion);
	    }
		// Se explora la rama del else si la hay
		if (es.hasElseBlock()) {
	        // Reinicia la lista de nodos anteriores para iniciar desde el nodo "if"
			cfg.nodosAnteriores.clear();
			//Arco que une el if con el else
			cfg.nodosAnteriores.add(nodoIF);
			
	        // Visita el bloque "else" y crea los nodos correspondientes
			es.getElseStmt().get().accept(this, cfg);
	        
	        // Combina los nodos finales de las rama "else" con la salida
	        nodoFinalUnificacion.addAll(cfg.nodosAnteriores);
		}else {
	        // Si no hay rama "else", conecta el nodo "if" directamente a los nodos finales del "then"
			nodoFinalUnificacion.add(nodoIF);
		}
		
        // Combina los nodos finales de las rama "then" con la salida
		cfg.nodosAnteriores = nodoFinalUnificacion;
		return es;

	}

	
	
	
	// Visitador de expresiones WHILE	
	@Override
	public Visitable visit(WhileStmt es, CFG cfg)
	{
		
	    // Crear un nodo para el ciclo while con su condición
	    cfg.crearNodo("While (" + es.getCondition() + ")");
	    
	    // Obtener el nodo anterior del CFG
	    NodoCFG nodoInicialWhile = cfg.getNodoAnterior().get(0);
	    
	    // Primero, visitamos el cuerpo del while
	    es.getBody().accept(this, cfg);
	    
		//Obtiene ultimo nodo del camino del while
	    List <NodoCFG> nodoFinalWhile = new ArrayList<>(cfg.getNodoAnterior());

	    if (!cfg.getNodoAnterior().containsAll(nodoFinalWhile)) {
	        cfg.añadirListaNodosAnteriores(nodoFinalWhile);
	    }
	    
	    //crea el arco desde el último nodo hasta el nodoWhile
		cfg.crearArcoDesdeUltimoNodo(nodoInicialWhile);

        // Reinicia la lista de nodos anteriores para iniciar desde el nodo "while"
	    cfg.nodosAnteriores.clear();

	    // Une el while con la salida del while
	    cfg.setNodoAnterior(nodoInicialWhile);

	    
	    return es;
	}
	
	@Override
	public Visitable visit(DoStmt es, CFG cfg)
	{
	    // Crear un nodo indicar el inicio del do
		cfg.crearNodo("Do");
		
	    // Obtener el nodo anterior del CFG 
		NodoCFG nodoDoWhile = cfg.getNodoActual();

	    // Primero, visitamos el cuerpo del Dowhile
 	    es.getBody().accept(this, cfg);
	    
		//Obtiene ultimo nodo del camino del Dowhile
	    List <NodoCFG> nodoFinalDoWhile = new ArrayList<>(cfg.getNodoAnterior());
	    
	    if (!cfg.getNodoAnterior().containsAll(nodoFinalDoWhile)) {
	        cfg.añadirListaNodosAnteriores(nodoFinalDoWhile);
	    }
	    
	    // Crear un nodo para el ciclo Dowhile con su condición
		cfg.crearNodo("While(" + es.getCondition()+ ")");

	    //crea el arco desde el final del Dowhile al incio
		cfg.crearArcoDesdePrimerNodo(nodoDoWhile);

		return es;

	}
	
	public Visitable visit(ForStmt es, CFG cfg) {
		
		//Crear un nodo para la inicializacion del for
		cfg.crearNodo("(For) " + es.getInitialization().get(0).toString());
		//Crear un nodo para condicion del for
		cfg.crearNodo("(For) "+ es.getCompare().get().toString());

	    // Obtener el nodo anterior del for 
	    NodoCFG nodoInicialFor = cfg.getNodoAnterior().get(0);

	    // Primero, visitamos el cuerpo del for
	    es.getBody().accept(this, cfg);

		//Obtiene ultimo nodo del camino del For
	    List <NodoCFG> nodoFinalFor = new ArrayList<>(cfg.getNodoAnterior());

	    if (!cfg.getNodoAnterior().containsAll(nodoFinalFor)) {
	        cfg.añadirListaNodosAnteriores(nodoFinalFor);
	    }
	    //Genera un nodo con la actualización del for 
		cfg.crearNodo("(For) " + es.getUpdate().get(0).toString());	

        // Reinicia la lista de nodos anteriores para iniciar desde el nodo "while"
	    cfg.nodosAnteriores.clear();

	    //crea el arco desde el final del for al inicio
		cfg.crearArcoDesdePrimerNodo(nodoInicialFor);
		
		//Une el for con el resto del grafo
	    cfg.setNodoAnterior(nodoInicialFor);


		return es;

	}
	

	
	
}