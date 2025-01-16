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
		// Obetenemos la condicion
		NodoCFG nodoIF = cfg.getNodoAnterior().get(0); 
		
		//Se explora el camino del then
		es.getThenStmt().accept(this, cfg); // Visita el bloque then del if y crea nodos correspondientes.

		//Obtiene ultimo nodo del camino del then
		List <NodoCFG> nodoFinalThen = new ArrayList<>(cfg.getNodoAnterior());
			
		//Volvemos al if
		cfg.setNodoAnterior(nodoIF);
		
		// Se explora la rama del else si la ha
		if (es.hasElseBlock()) {
	        es.getElseStmt().ifPresent(stmt -> stmt.accept(this, cfg));
		}
		cfg.addListaNodosAnteriores(nodoFinalThen);
		return es;
	}

	// Visitador de expresiones WHILE	
	@Override
	public Visitable visit(WhileStmt es, CFG cfg)
	{
		
	    // Crear un nodo para el ciclo while con su condición
	    cfg.crearNodo("While (" + es.getCondition() + ")");
	    
	    // Obtener el nodo anterior del CFG
	    NodoCFG nodoWhile = cfg.getNodoAnterior().get(0);
	    
	    // Primero, visitamos el cuerpo del while
	    es.getBody().accept(this, cfg);
	    
	    List <NodoCFG> nodoFinalWhile = new ArrayList<>(cfg.getNodoAnterior());

	    if (!cfg.getNodoAnterior().containsAll(nodoFinalWhile)) {
	        cfg.addListaNodosAnteriores(nodoFinalWhile);
	    }
	    
		cfg.crearArcoDesdeUltimoNodo(nodoWhile);

	    // Ahora, guardamos el último nodo del camino del while
	    
	    // Volvemos al nodo del while
	    cfg.setNodoAnterior(nodoWhile);

	    
	    return es;
	}
	
	@Override
	public Visitable visit(DoStmt es, CFG cfg)
	{
	    NodoCFG nodoDoWhile = cfg.getNodoAnterior().get(0);

	    es.getBody().accept(this, cfg);
	    
	    List <NodoCFG> nodoFinalWhile = new ArrayList<>(cfg.getNodoAnterior());
	    
	    if (!cfg.getNodoAnterior().containsAll(nodoFinalWhile)) {
	        cfg.addListaNodosAnteriores(nodoFinalWhile);
	    }
	    

		cfg.crearNodo("Do While(" + es.getCondition()+ ")");

		cfg.crearArcoDesdePrimerNodo(nodoDoWhile);

		return es;

	}
	
	public Visitable visit(ForStmt es, CFG cfg) {
		
		cfg.crearNodo("(For) " + es.getInitialization().get(0).toString());
		
		cfg.crearNodo("(For) "+ es.getCompare().get().toString());
		
	    NodoCFG nodoFor = cfg.getNodoAnterior().get(0);
	    
	    es.getBody().accept(this, cfg);

	    List <NodoCFG> nodoFinalFor = new ArrayList<>(cfg.getNodoAnterior());

	    if (!cfg.getNodoAnterior().containsAll(nodoFinalFor)) {
	        cfg.addListaNodosAnteriores(nodoFinalFor);
	    }
	    
		cfg.crearNodo("(For) " + es.getUpdate().get(0).toString());	

		cfg.crearArcoDesdePrimerNodo(nodoFor);
	    cfg.setNodoAnterior(nodoFor);


		return es;

	}
	

	
	
}