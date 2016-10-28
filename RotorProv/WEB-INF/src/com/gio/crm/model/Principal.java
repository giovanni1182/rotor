// FrontEnd Plus GUI for JAD
// DeCompiled : Principal.class

package com.gio.crm.model;

import java.net.ServerSocket;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import infra.config.InfraProperties;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;

public class Principal
{
	private static Processamento processamentoContabil,processamentoLibros, processamentoInstrumento;
	private Uteis uteis;
    private SampleModelManager mm;
    private	ProcessamentoHome home;
    private EntidadeHome entidadeHome;
    private Usuario usuario;
    private String tipo,tipo2;
    private EventoHome eventoHome;
    private ManutencaoHome manutencao;
    private Collection<Aseguradora> aseguradoras;
    
    public Principal(Map<String,SmbFile> processarArquivos, NtlmPasswordAuthentication auth)
    {
    	try
    	{
	    	if(uteis == null)
	    	{
	    		uteis = new Uteis();
	    		mm = new SampleModelManager();
	     		home = (ProcessamentoHome) mm.getHome("ProcessamentoHome");
	     		entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
	     		usuario = (Usuario) entidadeHome.obterEntidadePorApelido("admin");
	     		eventoHome = (EventoHome) mm.getHome("EventoHome");
	     		manutencao = (ManutencaoHome) mm.getHome("ManutencaoHome");
	    	}
	    	
	 		tipo = "Contabil";
	 		tipo2 = "Contable";
	 		processamentoContabil = home.obterProcessamentoDoDia(tipo);
	 		if(processamentoContabil == null)
	 			processamentoContabil = uteis.criarProcessamento(tipo, tipo2, usuario);
	 		
	 		tipo = "Instrumento";
	 		tipo2 = "Instrumento";
	 		processamentoInstrumento = home.obterProcessamentoDoDia(tipo);
	 		if(processamentoInstrumento == null)
	 			processamentoInstrumento = uteis.criarProcessamento(tipo, tipo2, usuario);
	 		
	 		tipo = "Livro";
	 		tipo2 = "Libro";
	 		processamentoLibros = home.obterProcessamentoDoDia(tipo);
	 		if(processamentoLibros == null)
	 			processamentoLibros = uteis.criarProcessamento(tipo, tipo2, usuario);
	 		
	 		for(SmbFile f : processarArquivos.values())
	 			processamentoContabil.addProcessando(f.getName(), f.getLastModified());
	 		
	 		System.out.println("Verificando Inscricoes Vencidas....");
	 		eventoHome.validarTodasAsIncricoes();
	 		System.out.println("Verificando Apolices Vencidas....");
	 		eventoHome.atualizarNoVigenteApolicesVenciadas();
	 		
	 		aseguradoras = uteis.copiarArquivos(mm, usuario, processamentoContabil, processamentoLibros, processamentoInstrumento, processarArquivos, auth);
	        
	 		System.out.println("Executando Manutencao na Base de dados... Isso pode demorar alguns minutos !");
	 		manutencao.verificarDuplicidades(this.aseguradoras);
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
    
    public static void main(String args[]) throws Exception
    {
    	boolean podeExecutar = true;
    	ServerSocket s = null;
    	
    	try
    	{
    	    s = new ServerSocket(6676);
    	}
    	catch (Exception e) 
    	{
    		podeExecutar = false;
    		System.out.println("RotorProv ja esta em execucao");
    	}
    	
    	if(podeExecutar)
    	{
	    	SmbFile arquivo2;
	    	SmbFile[] arquivos = null;
	    	Map<String,SmbFile> processarArquivos = new TreeMap<>();
	    	
	    	String nomeArquivo;
	    	String primeiraLetra;
	    	
	    	InfraProperties infra = InfraProperties.getInstance();
	    	String dir = "smb:"+infra.getProperty("arquivos.url");
	    	boolean enviaEmail = dir.indexOf("Laptopsala") == -1;
	    	NtlmPasswordAuthentication auth;
	    	if(enviaEmail)
	    		auth = new NtlmPasswordAuthentication("bcp.gov.py", "sisvalidacion", "Validacion2015");
	    	else
	    		auth = new NtlmPasswordAuthentication(null, "teste", "teste");
	    	SmbFile diretorio;
	    	boolean podeProcessar;
	    	
	    	try
	    	{
	    		diretorio = new SmbFile(dir, auth, SmbFile.FILE_SHARE_READ | SmbFile.FILE_SHARE_DELETE);
	    		Map<String,SmbFile> arquivoAux;
	    		
	    		while(true)
		    	{
	    			arquivoAux = new TreeMap<>();
		    		System.out.println(dir + " - " + new SimpleDateFormat("HH:mm:ss").format(new Date()));
		    		
		    		processarArquivos = new TreeMap<>();
		    		arquivos = diretorio.listFiles();
		        	
		        	for(int i = 0 ; i < arquivos.length ; i++)
		    		{
		        		arquivo2 = arquivos[i];
		        		
		        		arquivoAux.put(arquivo2.getName(), arquivo2);
		    		}
		        	
		        	for(SmbFile arquivo : arquivoAux.values())
		    		{
		    			podeProcessar = true;
		    			
		        		nomeArquivo = arquivo.getName();
		        		
	        			primeiraLetra = nomeArquivo.substring(0, 1).toLowerCase();
	        			
		    			if(primeiraLetra.equals("a"))
		    			{
		    				String nomeArquivoB = nomeArquivo.replace("A", "B");
		    				if(!arquivoAux.containsKey(nomeArquivoB))
	    						podeProcessar = false;
		    				else
		    				{
		    					if(!arquivoAux.get(nomeArquivoB).exists())
		    						podeProcessar = false;
		    				}
		    			}
		    			else if(primeiraLetra.equals("b"))
		    			{
		    				String nomeArquivoA = nomeArquivo.replace("B", "A");
		    				if(!arquivoAux.containsKey(nomeArquivoA))
	    						podeProcessar = false;
		    				else
		    				{
		    					if(!arquivoAux.get(nomeArquivoA).exists())
		    						podeProcessar = false;
		    				}
		    			}
		    			else if(!primeiraLetra.equals("0") && !primeiraLetra.equals("l"))
		    				podeProcessar = false;
		    			
		    			if(podeProcessar)
		    				processarArquivos.put(nomeArquivo, arquivo);
		    		}
		        	
		        	//System.out.println("processarArquivos : " + processarArquivos.toString());
		        	if(processarArquivos.size() > 0)
		        		new Principal(processarArquivos, auth);
		        	
		        	System.gc();
		        	Thread.sleep(5000);//This part gives the Bufferedreaders and the InputStreams time to close Completely
		        	for(SmbFile arquivo : processarArquivos.values())
		    		{
		        		if(arquivo.exists())
		        		{
		         			try
		     				{
		         				arquivo.delete();
		         				//System.out.println("Exclusao TRUE 2 = " + arquivo.getName());
		     				}
		     				catch(Exception e)
		     				{
		     					//System.out.println("Exclusao FALSE 2 = " + arquivo.getName());
		    					//e.printStackTrace();
		     				}
		 				}
		    		}
		        	
			    	//1 minuto
					Thread.sleep(60000/3);
					//15 minutos
		    		//Thread.sleep(900000);
		    	}
	    	}
	    	catch(Exception e)
	    	{
	    		if(s!=null)
	    			s.close();
	    		
	    		e.printStackTrace();
	    		main(args);
	    	}
	    }
    }
}
