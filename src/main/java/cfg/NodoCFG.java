package cfg;

public class NodoCFG {
	int id;
	String texto = "NEW NODE";
	
	public NodoCFG(int id, String texto){
		this.id=id;
		this.texto=texto;
	}
	
	String imprimir() {
		return "("+id+") "+texto; 
	}
}

