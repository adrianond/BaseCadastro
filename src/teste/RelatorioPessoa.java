package teste;

public class RelatorioPessoa extends RelatorioService<RetornoPessoa, EntradaPessoa> {
	private RetornoPessoa retorno;
	

	public RelatorioPessoa(RetornoPessoa retorno, EntradaPessoa entrada) {
		super(retorno, entrada);
		this.retorno = retorno;
	}

	@Override
	public RetornoPessoa pesquisar(EntradaPessoa entrada) {
		retorno.setNome(entrada.getNome());
		
		return retorno;
	}

}
