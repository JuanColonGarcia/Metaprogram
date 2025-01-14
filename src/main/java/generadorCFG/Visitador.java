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
			NodoCFG nodoIF = cfg.nodoActual; // ALAMCENA REFERENCIA DEL NODO ACTUAL

		}
		else {
			cfg.crearNodo(es); //REPRESENTA NODO IF
			cfg.añadirArcoSecuencialCFG(); // REPRESENTA ARCO QUE VA DE NODO ANTERIOR AL IF
			NodoCFG nodoIF = cfg.nodoActual; // ALAMCENA REFERENCIA DEL NODO ACTUAL
			es.getThenStmt().accept(this, cfg);
			NodoCFG nodoFinalThen = cfg.nodoActual; // CREA EL ÚLTIMO NODO DEL THEN
			cfg.añadirArcoDirigidoCFG(cfg.nodoActual, cfg.nodoSiguiente); //AÑADE ARCO DIRIGIDO DESDE EL NODO IF AL NODO SIGUIENTE
			es.getElseStmt().get().accept(this, cfg); //
			cfg.añadirArcoDirigidoCFG(nodoFinalThen, cfg.nodoSiguiente); //ARCO DEL THEN AL NODO SIGUIENTE
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
