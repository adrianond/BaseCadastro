package teste;

public class Cliente {
	
	public static void main(String[] args) {
		
		RetornoPessoa retornoPessoa = new RetornoPessoa(); 
		EntradaPessoa entradaPessoa = new EntradaPessoa();
		entradaPessoa.setNome("adriano");
		
		RelatorioService<RetornoPessoa, EntradaPessoa> relatorioService = 
				new RelatorioPessoa(retornoPessoa, entradaPessoa);
		
		RetornoPessoa retono = relatorioService.executar("RelatorioPessoa");
		System.out.println(retono.getNome());
		
	}

}
