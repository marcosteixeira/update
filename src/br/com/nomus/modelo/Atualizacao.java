package br.com.nomus.modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import br.com.nomus.hibernate.Entidade;

@Entity
@Table(name = "atualizacao")
@Root
public class Atualizacao implements Entidade {
	
	@Id
	@GeneratedValue
	private Integer id ;
	@Element
	private String contexto ;
	private Integer ultimoScriptRodado;
	@Element
	private Integer tagAtual ;
	private String caminhoDoArquivo;

	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCaminhoDoArquivo() {
		return caminhoDoArquivo;
	}
	public void setCaminhoDoArquivo(String caminhoDoArquivo) {
		this.caminhoDoArquivo = caminhoDoArquivo;
	}
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