package teste;

public abstract class RelatorioService<R, E> {
	
	private R retorno;
	private E entrada;
	
	public RelatorioService(R retorno, E entrada ) {
		this.retorno = retorno;
		this.entrada = entrada;
	}
	
	
	public R executar(String classe) {
		
		System.out.println("Executando com a classe " + classe);
		
		return pesquisar(entrada);
		
	}
	
	
		
	public abstract R pesquisar(E entrada);
	
	

}
