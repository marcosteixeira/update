package br.com.nomus.controller;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.nomus.modelo.Atualizacao;
import br.com.nomus.service.AtualizacaoService;

@Resource
public class AtualizacaoController {

	private final Result result;

	public AtualizacaoController(Result result) {
		this.result = result;
	}

	@Path("/atualizacao/verificarAtualizacao/{contexto.contexto}/{contexto.ultimoScriptRodado}/{contexto.tagAtual}")
	public void verificarAtualizacao(Atualizacao contexto) {
		//Baixa xml e verifica se existe atualizacoes
	
		AtualizacaoService servico = new AtualizacaoService ();
		if(!servico.fazDownloadXmlEVerificaAtualizacao(contexto)){
			result.redirectTo(this).naoHaAtualizacao();
		}
	}

	public void naoHaAtualizacao() {

	}

}
