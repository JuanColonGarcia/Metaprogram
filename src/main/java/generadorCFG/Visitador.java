package generadorCFG;
	
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.ExpressionStmt;
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
		// Deberiamos hacer algo al encontrar un IF
		if(es.hasElseBranch() == false) {
			cfg.crearNodo(es); 
			System.out.println(es);
			NodoCFG nodoFinalThen = cfg.nodoActual; // Guarda una referencia al último nodo del bloque then.
	        cfg.añadirArcoDirigidoCFG(nodoFinalThen, cfg.nodoSiguiente);

		}
		else {
			cfg.crearNodo(es); //Crea un nodo para la condición del if.
			cfg.añadirArcoSecuencialCFG(); // Añade un arco desde el nodo anterior al nodo if.
			NodoCFG nodoIF = cfg.nodoActual; // Guarda una referencia al nodo if actual.
			es.getThenStmt().accept(this, cfg); // Visita el bloque then del if y crea nodos correspondientes.
			
			NodoCFG nodoFinalThen = cfg.nodoActual; // Guarda una referencia al último nodo del bloque then.
			cfg.añadirArcoDirigidoCFG(cfg.nodoActual, cfg.nodoSiguiente); //Añade un arco dirigido desde el nodo if al siguiente nodo.
			es.getElseStmt().get().accept(this, cfg); //Visita el bloque else del if y crea nodos correspondientes.
			cfg.añadirArcoDirigidoCFG(nodoFinalThen, cfg.nodoSiguiente); //Añade un arco dirigido desde el último nodo del bloque then al siguiente nodo.
	        NodoCFG nodoFinalElse = cfg.nodoActual; // Nodo final del Else
	        // Conectar Then y Else al siguiente nodo
	        cfg.añadirArcoDirigidoCFG(nodoFinalThen, cfg.nodoSiguiente);
	        cfg.añadirArcoDirigidoCFG(nodoFinalElse, cfg.nodoSiguiente);
		}
		
		return super.visit(es, cfg);
	}

	// Visitador de expresiones WHILE	
	@Override
	public Visitable visit(WhileStmt es, CFG cfg)
	{
		// Deberiamos hacer algo al encontrar un WHILE
		
		return super.visit(es, cfg);
	}
	
}
