package br.com.nomus.modelo;

public class Atualizacao {
	
	private String contexto ;
	private Integer ultimoScriptRodado;
	private Integer tagAtual ;
	
	public String getContexto() {
		return contexto;
	}
	public void setContexto(String contexto) {
		this.contexto = contexto;
	}
	public Integer getUltimoScriptRodado() {
		return ultimoScriptRodado;
	}
	public void setUltimoScriptRodado(Integer ultimoScriptRodado) {
		this.ultimoScriptRodado = ultimoScriptRodado;
	}
	public Integer getTagAtual() {
		return tagAtual;
	}
	public void setTagAtual(Integer tagAtual) {
		this.tagAtual = tagAtual;
	}
	
	
	
	
}