package br.com.nomus.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import br.com.nomus.modelo.Atualizacao;

public class AtualizacaoService {
	
	public static final String CATALINA_HOME = System.getProperty("catalina.home");
	public static final String PASTA_UPDATE = CATALINA_HOME+"/update/";
	
	public Boolean fazDownloadXmlEVerificaAtualizacao (Atualizacao contexto) {
		
		String nomeCliente = contexto.getContexto();
		
		String linkDoXML = montaLinkXML(nomeCliente);
		String caminhoXML = PASTA_UPDATE + nomeCliente + ".xml";
		
		Boolean retorno = null;
		
		if(download(caminhoXML, linkDoXML)){
			
			if(verificaAtualizacaoDisponivel(caminhoXML, contexto)){
				retorno = Boolean.TRUE;
			}else {
				retorno = Boolean.FALSE;
			}
		
		}else{
			//faz validação sobre falha no download
		}
		
		return retorno ;
		
	}

	private Boolean verificaAtualizacaoDisponivel(String caminhoXML, Atualizacao contexto) {
		Boolean retorno = null;

		try{

			Serializer serializer = new Persister();
			File result = new File(caminhoXML);
			
			Atualizacao update = serializer.read(Atualizacao.class, result);
			
			
			if(update.getTagAtual() > contexto.getTagAtual()){
				retorno = Boolean.TRUE;
				
			}else {
				retorno = Boolean.FALSE;
			}
			
		}catch (Exception e) {
			// indica falha na leitura e solicita contato com o suporte
		}
		
		return retorno ;

		
	}

	private String montaLinkXML(String contexto) {
		
		return "http://apps.nomus.com.br/downloads/update/automatizados/"+contexto+".xml";
	}
	
	public Boolean download(String caminhoCompletoSalvamentoArquivo, String linkDoDownload){
		try {
			File file = new File(caminhoCompletoSalvamentoArquivo);
			OutputStream out = new FileOutputStream(file, false);
            URL url = new URL(linkDoDownload);  
            URLConnection conn = url.openConnection();  
                                      
            InputStream in = conn.getInputStream();  
              
            int i=0;  
            while ((i = in.read()) != -1){  
                out.write(i);  
            }  
            in.close();  
            out.close();  
            return Boolean.TRUE;
			
		} catch (Exception e) {
			return Boolean.FALSE;
		}
		
	}
	
	
}
