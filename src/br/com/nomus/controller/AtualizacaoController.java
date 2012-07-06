package br.com.nomus.controller;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.nomus.modelo.Atualizacao;

@Resource
public class AtualizacaoController {

	private final Result result;

	public AtualizacaoController(Result result) {
		this.result = result;
	}

	@Path("/atualizacao/verificarAtualizacao/{contexto.contexto}/{contexto.ultimoScriptRodado}/{contexto.tagAtual}")
	public void verificarAtualizacao(Atualizacao contexto) {
		result.redirectTo(IndexController.class).index();
	}

}
