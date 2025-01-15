package generadorCFG;
	
import java.util.ArrayList;
import java.util.List;

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