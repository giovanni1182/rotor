package com.gio.crm.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;

import infra.config.InfraProperties;
import infra.sql.SQLUpdate;

public class AgendaBat
{

    private String nomeArquivo;
    //private Map arquivos = new TreeMap();
    //private Map arquivosProblema = new TreeMap();
    private Map arquivosValidados = new TreeMap();
    private FileWriter fileLog = null;
    private Map<String,String> arquivosErroSFTP;
    private Map<String,String> arquivosSTFP;
    
    
    public AgendaBat() throws Exception
    {
    	
    }
    
    
   
    
   /* public void verificarArquivosInstrumento() throws Exception
    {
    	SampleModelManager mm = new SampleModelManager();
    	EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");
    	EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
    	Usuario usuario = (Usuario) home.obterEntidadePorApelido("admin");
    	Collection erros = new ArrayList();
    	AgendaMovimentacao agenda2 = null;
    	File arquivoErro = null; 
    	
    	//mm.beginTransaction();
    	try
    	{
	    	String diretorioStr = "C:/tmp/";
	    	
	    	File diretorio = new File(diretorioStr);
	    	
	    	File[] arquivosSemFiltro = diretorio.listFiles();
	    	
	    	for(int i = 0 ; i < arquivosSemFiltro.length ; i++)
	    	{
	    		File arquivo = arquivosSemFiltro[i];
	    		
	    		String nome = arquivo.getName();
	    		
	    		String tipo = nome.substring(0, 1);
	    		
	    		if(tipo.equals("A"))
	    			this.arquivos.put(nome, arquivo);
	    		
	    	}
	    	
	    	fileLog.write("\r\n");
	    	
	    	//System.out.println(this.arquivos.size()+ " archivos encontrados");
	    	
	    	fileLog.write(this.arquivos.size() + " Arquivos A encontrados no tmp" + "\r\n");
	    	
	    	//System.out.println(this.arquivosProblema.size()+ " archivos com prolemas encontrados");
	    	
	    	fileLog.write(this.arquivosProblema.size() + " Arquivos com problemas encontrados no tmp" + "\r\n");
	    	
	    	//System.out.println(this.arquivosValidados.size()+ " archivos validados encontrados");
	    	
	    	fileLog.write(this.arquivosValidados.size() + " Arquivos validados no tmp" + "\r\n");
	    	
	    	this.arquivos.values().removeAll(this.arquivosProblema.values());
	    	this.arquivos.values().removeAll(this.arquivosValidados.values());
	    	
	    	for(Iterator i = this.arquivos.values().iterator() ; i.hasNext() ; )
	    	{
	    		File arquivoA = (File) i.next();
	    		
    			arquivoErro = arquivoA;
	    		
	    		erros = new ArrayList();
	    		
	    		String nome = arquivoA.getName();
	    		
	    		String sigla = nome.substring(1, 4);
	    			
	    		Aseguradora aseguradora = (Aseguradora) home.obterEntidadePorSigla(sigla);
	    			
		    	String anoStr = nome.substring(4, 8);
		    		
	    		String mesStr = nome.substring(8, 10);
	    		
	    		//System.out.println(nome + " : " + tipo + " - "+ sigla + " - " + anoStr + " - " + mesStr);
	    		
	    		int mes = Integer.parseInt(mesStr);
	    		int ano = Integer.parseInt(anoStr);
	    		
	    		boolean validar = true;
	    		
	    		AgendaMovimentacao agenda = eventoHome.obterAgendaInstrumento(aseguradora, mes, ano);
	    		
	    		agenda2 = agenda;
	    		
	    		if(agenda == null)
	    		{
	    			//System.out.println("Agenda está concluida: " + arquivoA.getName());
	    			validar = false;
	    		}
	    		else
	    		{
	    			if(agenda.obterId() == 0)
		    		{
	    				mm.beginTransaction();
	    				
	    				agenda.atribuirOrigem(aseguradora);
		    			agenda.atribuirTitulo(mesStr + " - " + anoStr);
		    			agenda.atribuirResponsavel(usuario);
		    			agenda.atribuirTipo("Instrumento");
		    			agenda.atribuirAnoMovimento(ano);
		    			agenda.atribuirMesMovimento(mes);
		    			agenda.atribuirDataPrevistaInicio(new Date());
		    			agenda.atribuirDataPrevistaConclusao(new Date());
		    			
		    			agenda.incluir();
		    			
		    			agenda.atualizarValidacao("Total");
		    			
		    			mm.commitTransaction();
		    			
		    			//System.out.println("Agenda não existia, foi criada automaticamente: " + arquivoA.getName());
		    			fileLog.write("Agenda não existia, foi criada automaticamente: " + arquivoA.getName() + "\r\n");
		    		}
		    		else
		    		{
		    			//System.out.println("Agenda estava pendente: " + arquivoA.getName());
		    			fileLog.write("Agenda estava pendente: " + arquivoA.getName() + "\r\n");
		    		}
		    		
		    		//validar = false;
	    		}
	    		
	    		String mensagem = "";
	    		
	    		for(Iterator j = aseguradora.obterAgendasIntrumentoPendentes().iterator() ; j.hasNext() ; )
				{
					AgendaMovimentacao agendaP = (AgendaMovimentacao) j.next();
							
					if(agendaP.obterId()!=agenda.obterId())
					{
						mensagem += "Agenda " + agendaP.obterMesMovimento() + " - " + agendaP.obterAnoMovimento() + " pendiente" + "\r\n";
						validar = false;
					}
				
				}
	    		
	    		if(!validar && agenda!=null)
	    		{
	    			//agenda.atualizarDescricao2(mensagem);
	    			
	    			mm.beginTransaction();
	    			agenda.concluirNotificacoesInferiores();
	    			mm.commitTransaction();
	    			
	    			Parametro parametro = (Parametro)home.obterEntidadePorApelido("parametros");
                    String descricao = parametro.obterAtributo("notificacaocritica").obterValor();
                    Notificacao notificacao = (Notificacao)mm.getEntity("Notificacao");
                    notificacao.atribuirSuperior(agenda);
                    notificacao.atribuirOrigem(agenda.obterOrigem());
                    notificacao.atribuirResponsavel(agenda.obterResponsavel());
                    notificacao.atribuirTipo("Notificaci\363n de Error de Validaci\363n");
                    notificacao.atribuirTitulo("Notificaci\363n de Error de Validaci\363n");
                    notificacao.atribuirDescricao(descricao + "\n" + mensagem);
                    notificacao.incluir();
	    		}
	    			
	    		String nomeArquivoB = arquivoA.getName().replace('A', 'B');
	    		
	    		File arquivoB = new File(diretorioStr + nomeArquivoB);
	    		
	    		if(validar)
	    		{
		    		this.arquivosValidados.put(arquivoA.getName(), arquivoA);
		    		
	    			mm.beginTransaction();
		    		
		    		//System.out.println("Comecou a validacao, arquivo: " + arquivoA.getName());
		    		
		    		fileLog.write("Comecou a validacao, arquivo: " + arquivoA.getName()+ "\r\n");
		    		
		    		agenda.adicionarComentario("Consistencia", "Inicio " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
		    		
		    		agenda.instanciar();
		    		
		    		if(!arquivoB.exists())
		    			erros.add("Error: 02 - El Archivo B " + arquivoB + " no fue encontrado (Datos del Asegurado)");
		    		else
		    			erros.addAll(agenda.verificarApolice(arquivoA, arquivoB));
		    		
		    		if(erros.size() > 0)
	                {
		    			//mm.beginTransaction();
		    			
		    			agenda.adicionarComentario("Consistencia", "Termino " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
	                	
	                	//mm.commitTransaction();
	                	
	                	//System.out.println("Gravou comentário de término que deu erro (validacaoErros.size() > 0)");
	                	
	                	String msgErros = "";
	                    for(Iterator j = erros.iterator(); j.hasNext();)
	                    {
	                        String msgAux = (String)j.next();
	                        msgErros = msgErros + msgAux + "\n";
	                    }
	
	                   // mm.beginTransaction();
	                    
	                   // System.out.println("Concluindo notificacoes pendentes");
	                    
	                    agenda.concluirNotificacoesInferiores();
	                    
	                    Parametro parametro = (Parametro)home.obterEntidadePorApelido("parametros");
	                    String descricao = parametro.obterAtributo("notificacaocritica").obterValor();
	                    Notificacao notificacao = (Notificacao)mm.getEntity("Notificacao");
	                    notificacao.atribuirSuperior(agenda);
	                    notificacao.atribuirOrigem(agenda.obterOrigem());
	                    notificacao.atribuirResponsavel(agenda.obterResponsavel());
	                    notificacao.atribuirTipo("Notificaci\363n de Error de Validaci\363n");
	                    notificacao.atribuirTitulo("Notificaci\363n de Error de Validaci\363n");
	                    notificacao.atribuirDescricao(descricao + "\n" + msgErros);
	                    notificacao.incluir();
	                    
	                    //System.out.println("Gravou a notificação de erro dentro do try");
	                    
	                    //System.out.println("ID: " + agenda.obterId());
	                    
	                    agenda.atualizarFase2("pendente");
	                   // System.out.println("Colocou a agenda com erro na fase pendente (validacaoErros.size() > 0)");
	                    
	                    //this.gravarArquivoSFTP(arquivoA, descricao + "\n" + msgErros);
	                    
	                    mm.commitTransaction();
	                } 
	                else
	                {
	                	agenda.adicionarComentario("Consistencia", "Termino " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
	                	//System.out.println("Gravou comentário de término sem erro");
	                	
	                	Parametro parametro = (Parametro)home.obterEntidadePorApelido("parametros");
	                    String descricao = parametro.obterAtributo("notificacaorecebimento").obterValor();
	                    Notificacao notificacao = (Notificacao)mm.getEntity("Notificacao");
	                    notificacao.atribuirSuperior(agenda);
	                    notificacao.atribuirOrigem(agenda.obterOrigem());
	                    notificacao.atribuirResponsavel(agenda.obterResponsavel());
	                    notificacao.atribuirTipo("Notificaci\363n de Recibimiento");
	                    notificacao.atribuirTitulo("Notificaci\363n de Recibimiento");
	                    notificacao.atribuirDescricao(descricao);
	                    notificacao.incluir();
	                    
	                   // notificacoes.add(notificacao);
	                    //System.out.println("Gravou a notificação de recebimento dentro do try");
	                    for(Iterator j = agenda.obterInferiores().iterator(); j.hasNext();)
	                    {
	                    	Evento e = (Evento) j.next();
	                    	if(!(e instanceof RatioPermanente) &&  !(e instanceof RatioUmAno) &&  !(e instanceof RatioTresAnos))
	                    		e.atualizarFase("concluido");
	                        
	                    }
	                    
	                    agenda.atualizarFase("concluido");
	                    //System.out.println("Colocou a agenda sem erro na fase concluido");
	                    
	                    Calendar mesSeguinte = Calendar.getInstance();
	                    mesSeguinte.setTime(new Date());
	                    mesSeguinte.add(2, 1);
	                    String dia = parametro.obterAtributo("diaagenda").obterValor();
	                    String mes2 = (new SimpleDateFormat("MM")).format(mesSeguinte.getTime());
	                    String ano2 = (new SimpleDateFormat("yyyy")).format(mesSeguinte.getTime());
	                    String data = dia + "/" + mes + "/" + ano;
	                    Date dataModificada = (new SimpleDateFormat("dd/MM/yyyy")).parse(data);
	                    int mesMovimento = agenda.obterMesMovimento() + 1;
	                    int anoMovimento = agenda.obterAnoMovimento();
	                    if(mesMovimento > 12)
	                    {
	                        mesMovimento = 1;
	                        anoMovimento++;
	                    }
	                    if(!agenda.existeAgendaNoPeriodo(mesMovimento, anoMovimento, agenda.obterOrigem(), agenda.obterTipo()))
	                    {
	                        AgendaMovimentacao novaAgenda = (AgendaMovimentacao)mm.getEntity("AgendaMovimentacao");
	                        novaAgenda.atribuirOrigem(agenda.obterOrigem());
	                        novaAgenda.atribuirResponsavel(agenda.obterResponsavel());
	                        novaAgenda.atribuirDataPrevistaInicio(dataModificada);
	                        novaAgenda.atribuirTipo(agenda.obterTipo());
	                        novaAgenda.atribuirMesMovimento(mesMovimento);
	                        novaAgenda.atribuirAnoMovimento(anoMovimento);
	                        String mesModificado = "";
	                        if((new Integer(mesMovimento)).toString().length() == 1)
	                            mesModificado = "0" + mesMovimento;
	                        else
	                            mesModificado = (new Integer(mesMovimento)).toString();
	                        novaAgenda.atribuirTitulo(mesModificado + " - " + anoMovimento + " (Consistencia del archivo)");
	                        novaAgenda.incluir();
	                        
	                        //System.out.println("Gravou agenda do mês seguinte");
	                        
	                    }
	                    
	                    //this.gravarArquivoSFTP(arquivoA, descricao + "\n");
	                }
	    		}
	    		
	    		//COPIANDO ARQUIVOS PRA PASTA DE BACKUP, E EXCLUINDO OS ARQUIVOS DO TMP 
	    		InputStream is = new FileInputStream(arquivoA);
                 OutputStream os = new FileOutputStream("C:/Aseguradoras/Backup/" + arquivoA.getName());
                 byte buffer[] = new byte[is.available()];
                 is.read(buffer);
                 os.write(buffer);
                 
                 fileLog.write("Copiou arquivo para pasta backup: " + arquivoA.getName()+ "\r\n");
                 
                 is.close();
                 os.close();
                 
                 InputStream is2 = new FileInputStream(arquivoB);
                 OutputStream os2 = new FileOutputStream("C:/Aseguradoras/Backup/" + arquivoB.getName());
                 byte buffer2[] = new byte[is2.available()];
                 is2.read(buffer2);
                 os2.write(buffer2);
                 
                 fileLog.write("Copiou arquivo para pasta backup: " + arquivoB.getName()+ "\r\n");
                 
                 is2.close();
                 os2.close();
                 
                arquivoA.setWritable(true, true);
                arquivoB.setWritable(true, true);
                arquivoA.deleteOnExit();
                fileLog.write("Excluiu arquivo do tmp: " + arquivoA.getName()+ "\r\n");
                arquivoB.deleteOnExit();
                fileLog.write("Excluiu arquivo do tmp: " + arquivoB.getName()+ "\r\n");
                fileLog.write("\r\n");
	    	}
	    	
	    	//mm.beginTransaction();
	    	
	    	//mm.commitTransaction();
	    	
	    	 fileLog.close();
    	}
    	catch (Exception e) 
    	{
    		//System.out.println("Erro:" + e.getMessage());
    		fileLog.write("Erro:" + e.getMessage() + "\r\n");
    		fileLog.write("\r\n");
        	//mm.rollbackTransaction();
            if(agenda2 != null)
            {
            	agenda2.concluirNotificacoesInferiores();
            	
            	//mm.beginTransaction();
                Notificacao notificacao = (Notificacao)mm.getEntity("Notificacao");
                notificacao.atribuirSuperior(agenda2);
                notificacao.atribuirOrigem(agenda2.obterOrigem());
                notificacao.atribuirResponsavel(agenda2.obterResponsavel());
                notificacao.atribuirTipo("Notificaci\363n de Error de Validaci\363n");
                notificacao.atribuirTitulo("Notificaci\363n de Error de Validaci\363n");
                
                String msgErros = e.getMessage() + "\n";
                
                for(Iterator j = erros.iterator(); j.hasNext();)
                {
                    String msgAux = (String)j.next();
                    msgErros +=  msgAux + "\n";
                }
                
                notificacao.atribuirDescricao(msgErros);
                
                notificacao.incluir();
                
                //System.out.println("Gravou a notificação de erro no catch");
                
                agenda2.atualizarFase("pendente");
                
                //System.out.println("Colocou a agenda com erro no catch na fase pendente");
                
                mm.commitTransaction();
                
                this.arquivosProblema.put(arquivoErro.getName(), arquivoErro);
                this.verificarArquivosInstrumento();
            }
    	}
    }*/
    
   /* private void gravarArquivoSFTP(File arquivo, String msg)throws Exception
    {
    	String pasta = this.arquivosSTFP.get(arquivo.getName()).toString();
    	
    	FileWriter fileW = new FileWriter("C:/tmp/Notificacion_" + arquivo.getName());
    	
    	fileW.write(msg);
    	
    	fileW.close();
    	
    	File file = new File("C:/tmp/Notificacion_" + arquivo.getName());
    	
    	try
    	{
    		System.out.println("Conectando ao SFTP 200.1.201.54 para gravar arquivo de Notificacao");
    		fileLog.write("Conectando ao SFTP 200.1.201.54 para gravar o arquivo de Notificacao " + file.getName() + "\r\n");
    		
    		SFTPClient sftp = new SFTPClient();
	    	sftp.setPort(2345);
	    	sftp.setServerName("200.1.201.54");
	    	sftp.setUsername("supseg");
	    	sftp.setPassword("supseg");
	    	
	    	int status = sftp.sendFile(file, pasta);
	    	
	    	if(FileTransferStatus.SUCCESS == status)
				fileLog.write(arquivo.getName() + " Sucesso no upload para pasta " + pasta + "\r\n");
			else if(FileTransferStatus.FAILURE == status)
				fileLog.write(arquivo.getName() + " Falha no upload para pasta " + pasta+ "\r\n");
	    	
	    	 arquivo.setWritable(true, true);
             
             arquivo.deleteOnExit();
             
             fileLog.write("\r\n");
    	}
    	catch (FileTransferException e)
    	{
    		System.out.println(e.toString());
    		fileLog.write("Erro interno no upload:" + e.getMessage() + "\r\n");
    		fileLog.close();
		}
    }*/
    
    public void atualizarNoVigenteApolicesVenciadas() throws Exception
	{
    	 SampleModelManager mm = new SampleModelManager();
    	 ApoliceHome apoliceHome = (ApoliceHome)mm.getHome("ApoliceHome");
    	 
    	 mm.beginTransaction();
		 {
		 	try
	        {
		 		apoliceHome.atualizarNoVigenteApolicesVenciadas();
		 		
		 		mm.commitTransaction();
		 		
	        }
		 	catch(Exception exception)
	        {
	            System.out.println("Erro na Atualizacao de Apolices Vencidas: " + Util.translateException(exception));
	            mm.rollbackTransaction();
	        }
		 }
	}
    
   
    
    private boolean gerouGEE;
    
    public void relGEE() throws Exception
    {
    	SampleModelManager mm = new SampleModelManager();
    	AseguradoraHome home = (AseguradoraHome) mm.getHome("AseguradoraHome");
    	Collection aseguradoras = home.obterAseguradorasPorMenor80OrdenadoPorNome();
    	
    	Calendar c = Calendar.getInstance();
    	
    	for(int i = 1 ; i <= 3 ; i++)
    	{
	    	if(!this.gerouGEE)
	    	{
	    		c.setTime(new Date());
	    		c.add(Calendar.MONTH, i*-1);
	    		
	    		this.gerarGEE(c.getTime(), aseguradoras);
	    	}
    	}
    }
    
    private void gerarGEE(Date data, Collection aseguradoras) throws Exception
    {
    	SampleModelManager mm = new SampleModelManager();
    	AseguradoraHome home = (AseguradoraHome) mm.getHome("AseguradoraHome");
    	
    	String mesStr = new SimpleDateFormat("MM").format(data);
    	String anoStr = new SimpleDateFormat("yyyy").format(data);
    	
    	int mes = Integer.parseInt(mesStr);
    	int ano = Integer.parseInt(anoStr);
    	
    	/*String mesStr = "02";
    	String anoStr = "2011";
    	
    	int mes = Integer.parseInt(mesStr);
    	int ano = Integer.parseInt(anoStr);*/
    	
    	boolean gerarGEE = true;
    	
    	System.out.println(mesStr+"/"+anoStr);
    	
    	for(Iterator i = aseguradoras.iterator() ; i.hasNext() ; )
    	{
    		Aseguradora aseg = (Aseguradora) i.next();
    		
    		if(aseg.obterId()!=5228 && aseg.obterId()!=5205)
    		{
	    		//System.out.println(aseg.obterNome());
	    		gerarGEE = aseg.existeAgendaNoPeriodo(mes, ano, "Contabil");
	    		if(!gerarGEE)
	    			break;
    		}
    	}
    	
    	if(gerarGEE)
    	{
    		String mesAno = home.obterMesAnoGEE();
    		
    		if(mesAno==null || !mesAno.equals(mesStr+anoStr))
    		{
	    		System.out.println("Gerando Relatorio GEE");
	    		String sql = "Exec sp_plancuentas '"+mesStr+anoStr+"'";
	    		
	    		System.out.println(sql);
	    		
	    		SQLUpdate update = mm.createSQLUpdate("crm",sql);
	    		update.execute();
	    		
	    		mm.beginTransaction();
	    		
	    		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
	    		Usuario usuario = usuarioHome.obterUsuarioPorChave("norma");
	    		Mensagem mensagem = (Mensagem) mm.getEntity("Mensagem");
	            mensagem.atribuirResponsavel(usuario);
	            mensagem.atribuirPrioridade(1);
	            mensagem.atribuirTitulo("Información GEE");
	            mensagem.atribuirDescricao("Fue generada la tabla GEE del mes " + mesStr+"/"+anoStr);
	            mensagem.atribuirTipo("Notificación");
	            mensagem.incluir();
	            
	            mm.commitTransaction();
    		}
    		else
    			System.out.println(mesStr+anoStr + " Ja esta gerado no BD");
    	}
    	else
    		System.out.println("Nao foi possivel gerar Relatorio GEE");
    	
    	this.gerouGEE = gerarGEE;
    }
    
    private Map<Long, Aseguradora> aseguradoras = new TreeMap<Long, Aseguradora>();
    //private AgendaMovimentacao ultimaConcluida = null;

    public void verificarAgendasInstrumento(AgendaMovimentacao agendaMovimentacao, Processamento processamentoInstrumento, String nomeArquivo) throws Exception
    {
        SampleModelManager mm = new SampleModelManager();
        EntidadeHome entidadeHome = (EntidadeHome)mm.getHome("EntidadeHome");
        //EventoHome eventoHome = (EventoHome)mm.getHome("EventoHome");
        //Collection agendas = new ArrayList();
        Collection<String> validacaoErros = new ArrayList<String>();
        AgendaMovimentacao agendaMovimentacao2 = null;
        Entidade destino = (ClassificacaoContas)entidadeHome.obterEntidadePorApelido("planodecontas");
        UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
        mm.beginTransaction();
        try
        {
        	//agendas = eventoHome.obterAgendasInstrumento();
        	
        	//if(agendas.size() > 0)
        	//{
        		/*boolean podeValidar = true;
        		AgendaMovimentacao agendaMovimentacao = eventoHome.obterAgendaInstrumentoParaValidacao();
        		if(ultimaConcluida!=null)
        		{
        			if(ultimaConcluida.obterId()==agendaMovimentacao.obterId())
        				podeValidar = false;
        		}*/
                
        		//if(podeValidar)
        		//{
	        		agendaMovimentacao2 = agendaMovimentacao;
	                String ano2 = (new Integer(agendaMovimentacao.obterAnoMovimento())).toString();
	                String mes2 = (new Integer(agendaMovimentacao.obterMesMovimento())).toString();
	                if(mes2.length() == 1)
	                    mes2 = "0" + mes2;
	                
	                Entidade origem = agendaMovimentacao.obterOrigem();
	                String sigla = origem.obterSigla();
	                
	                if(agendaMovimentacao.obterTipo() != null)
	                {
	                	//String descricaoFTP = "";
	                	
	                    if(agendaMovimentacao.obterTipo().equals("Instrumento"))
	                    {
	                    	agendaMovimentacao.adicionarComentario("Consistencia", "Inicio " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
	                    	//System.out.println("Gravou comentário de inicio");
	                    	agendaMovimentacao.instanciar();
	                    	validacaoErros = agendaMovimentacao.verificarApolice(sigla.trim() + ano2 + mes2, false);
	                    	//agendaMovimentacao.contagemRegistros(sigla.trim() + ano2 + mes2);
	                    }
	                    
	                    if(validacaoErros.size() > 0)
	                    {
	                    	//agendaMovimentacao.adicionarComentario("Consistencia", "Termino " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
	                    	
	                    	//System.out.println("Gravou comentário de término que deu erro (validacaoErros.size() > 0)");
	                    	
	                    	String msgErros = "";
	                    	String errosAgente = "";
	                    	String errosReaseguradoras = "";
	                    	String erroData = "";
	                    	
	                        for( String msgAux : validacaoErros)
	                        {
	                            if(msgAux.startsWith("Error: 09") || msgAux.startsWith("Error: 84"))
	                            	errosAgente+= msgAux + "\n";
	                            else if(msgAux.startsWith("Error: 10") || msgAux.startsWith("Error: 11 - Inscripci\363n (Corretora o Reaseguradora"))
	                            	errosReaseguradoras+= msgAux + "\n";
	                            else if(msgAux.startsWith("Error: 141"))
	                            	erroData+= msgAux + "\n";
	                            
	                            msgErros += msgAux + "\n";
	                        }
	                        
	                        if(!errosAgente.equals(""))
	                        {
	                        	Usuario usuarioAgente = usuarioHome.obterUsuarioPorChave("asilver");
	                        	Usuario usuarioAgente2 = usuarioHome.obterUsuarioPorNivel("Intendente ICORAS");
	                        	 
	                        	Mensagem mensagem = (Mensagem) mm.getEntity("Mensagem");
	                            mensagem.atribuirResponsavel(usuarioAgente);
	                            mensagem.atribuirPrioridade(1);
	                            mensagem.atribuirTitulo("Error Validación");
	                            mensagem.atribuirDescricao("Aseguradora:" + agendaMovimentacao.obterOrigem().obterNome() +"\n\n" + errosAgente);
	                            mensagem.atribuirTipo("Notificación");
	                            mensagem.incluir();
	                            
	                            Mensagem mensagem2 = (Mensagem) mm.getEntity("Mensagem");
	                            mensagem2.atribuirResponsavel(usuarioAgente2);
	                            mensagem2.atribuirPrioridade(1);
	                            mensagem2.atribuirTitulo("Error Validación");
	                            mensagem2.atribuirDescricao("Aseguradora:" + agendaMovimentacao.obterOrigem().obterNome() +"\n\n" + errosAgente);
	                            mensagem2.atribuirTipo("Notificación");
	                            mensagem2.incluir();
	                        }
	                        
	                        if(!errosReaseguradoras.equals(""))
	                        {
	                        	Usuario usuarioAgente = usuarioHome.obterUsuarioPorNivel("Intendente ICORAS");
	                        	Usuario usuarioAgente2 = usuarioHome.obterUsuarioPorNivel("División de Control de Reaseguros");
	                        	 
	                        	Mensagem mensagem = (Mensagem) mm.getEntity("Mensagem");
	                            mensagem.atribuirResponsavel(usuarioAgente);
	                            mensagem.atribuirPrioridade(1);
	                            mensagem.atribuirTitulo("Error Validación");
	                            mensagem.atribuirDescricao("Aseguradora:" + agendaMovimentacao.obterOrigem().obterNome() +"\n\n" + errosReaseguradoras);
	                            mensagem.atribuirTipo("Notificación");
	                            mensagem.incluir();
	                            
	                            Mensagem mensagem2 = (Mensagem) mm.getEntity("Mensagem");
	                            mensagem2.atribuirResponsavel(usuarioAgente2);
	                            mensagem2.atribuirPrioridade(1);
	                            mensagem2.atribuirTitulo("Error Validación");
	                            mensagem2.atribuirDescricao("Aseguradora:" + agendaMovimentacao.obterOrigem().obterNome() +"\n\n" + errosReaseguradoras);
	                            mensagem2.atribuirTipo("Notificación");
	                            mensagem2.incluir();
	                        }
	                        
	                        if(!erroData.equals(""))
	                        {
	                        	Collection<String> pessoas = new ArrayList<String>();
	                        	pessoas.add("pgonza");
	                        	pessoas.add("craggio");
	                        	pessoas.add("garriola");
	                        	pessoas.add("jburgos");
	                        	pessoas.add("mmorat");
	                        	
	                        	Usuario usuarioAgente;;
	                        	
	                        	for(String chaveAcesso : pessoas)
	                        	{
	                        		usuarioAgente = usuarioHome.obterUsuarioPorChave(chaveAcesso);
	                        		
	                           	 	if(usuarioAgente!=null)
	                           	 	{
		                            	Mensagem mensagem = (Mensagem) mm.getEntity("Mensagem");
		                                mensagem.atribuirResponsavel(usuarioAgente);
		                                mensagem.atribuirPrioridade(1);
		                                mensagem.atribuirTitulo("Error Validación");
		                                mensagem.atribuirDescricao("Aseguradora:" + agendaMovimentacao.obterOrigem().obterNome() +"\n\n" + erroData);
		                                mensagem.atribuirTipo("Notificación");
		                                mensagem.incluir();
	                           	 	}
	                        	}
	                        }
	                        
	                        agendaMovimentacao.concluirNotificacoesInferiores();
	
	                        Parametro parametro = (Parametro)entidadeHome.obterEntidadePorApelido("parametros");
	                        String descricao = parametro.obterAtributo("notificacaocritica").obterValor();
	                        Notificacao notificacao = (Notificacao)mm.getEntity("Notificacao");
	                        notificacao.atribuirSuperior(agendaMovimentacao);
	                        notificacao.atribuirOrigem(agendaMovimentacao.obterOrigem());
	                        notificacao.atribuirDestino(destino);
	                        notificacao.atribuirResponsavel(agendaMovimentacao.obterResponsavel());
	                        notificacao.atribuirTipo("Notificaci\363n de Error de Validaci\363n");
	                        notificacao.atribuirTitulo("Notificaci\363n de Error de Validaci\363n");
	                        notificacao.atribuirDescricao(descricao + "\n" + msgErros);
	                        notificacao.incluir();
	                        //System.out.println("Gravou a notificação de erro dentro do try");
	                        
	                        //descricaoFTP = descricao + "\r\n" + msgErros;
	
	                        agendaMovimentacao.atualizarFase2(Evento.EVENTO_PENDENTE);
	                        //System.out.println("Colocou a agenda com erro na fase pendente (validacaoErros.size() > 0)");
	                        
	                        String mesAno = agendaMovimentacao.obterMesMovimento() + "/"+agendaMovimentacao.obterAnoMovimento();
							String titulo = origem.obterNome() + " - " + mesAno;
							
							String descricao2 = "Este correo fue enviado de forma automática, favor no responder al mismo\n\nAl validar los archivos del Módulo Central de Información del mes de "+mesAno+" ocurrió el siguiente error:\n"+msgErros;
							
							processamentoInstrumento.addAgenda(nomeArquivo, "Errores enviados por email - " + new SimpleDateFormat("HH:mm:ss").format(new Date()), agendaMovimentacao,1);
							
							this.enviarEmail(titulo, descricao2, origem);
	                    } 
	                    else
	                    {
	                    	Aseguradora aseg = (Aseguradora) agendaMovimentacao.obterOrigem();
	                    	aseguradoras.put(aseg.obterId(), aseg);
	                    	
	                    	//agendaMovimentacao.adicionarComentario("Consistencia", "Termino " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
	                    	//System.out.println("Gravou comentário de término sem erro");
	                    	
	                    	Parametro parametro = (Parametro)entidadeHome.obterEntidadePorApelido("parametros");
	                        String descricao = parametro.obterAtributo("notificacaorecebimento").obterValor();
	                        Notificacao notificacao = (Notificacao)mm.getEntity("Notificacao");
	                        notificacao.atribuirSuperior(agendaMovimentacao);
	                        notificacao.atribuirOrigem(agendaMovimentacao.obterOrigem());
	                        notificacao.atribuirDestino(destino);
	                        notificacao.atribuirResponsavel(agendaMovimentacao.obterResponsavel());
	                        notificacao.atribuirTipo("Notificaci\363n de Recibimiento");
	                        notificacao.atribuirTitulo("Notificaci\363n de Recibimiento");
	                        notificacao.atribuirDescricao(descricao);
	                        notificacao.incluir();
	                        
	                        notificacao.atualizarFase2(Evento.EVENTO_CONCLUIDO);
	                        
	                        //System.out.println("Gravou a notificação de recebimento dentro do try");
	                        
	                        //descricaoFTP = descricao;
	
	                        agendaMovimentacao.atualizarFase2(Evento.EVENTO_CONCLUIDO);
	                        //ultimaConcluida = agendaMovimentacao;
	                        //System.out.println("Colocou a agenda sem erro na fase concluido");
	                        
	                        /*Calendar mesSeguinte = Calendar.getInstance();
	                        mesSeguinte.setTime(new Date());
	                        mesSeguinte.add(2, 1);
	                        String dia = parametro.obterAtributo("diaagenda").obterValor();
	                        String mes = (new SimpleDateFormat("MM")).format(mesSeguinte.getTime());
	                        String ano = (new SimpleDateFormat("yyyy")).format(mesSeguinte.getTime());
	                        String data = dia + "/" + mes + "/" + ano;
	                        Date dataModificada = (new SimpleDateFormat("dd/MM/yyyy")).parse(data);
	                        int mesMovimento = agendaMovimentacao.obterMesMovimento() + 1;
	                        int anoMovimento = agendaMovimentacao.obterAnoMovimento();
	                        if(mesMovimento > 12)
	                        {
	                            mesMovimento = 1;
	                            anoMovimento++;
	                        }
	                        
	                        if(!agendaMovimentacao.existeAgendaNoPeriodo(mesMovimento, anoMovimento, agendaMovimentacao.obterOrigem(), agendaMovimentacao.obterTipo()))
	                        {
	                            AgendaMovimentacao novaAgenda = (AgendaMovimentacao)mm.getEntity("AgendaMovimentacao");
	                            novaAgenda.atribuirOrigem(agendaMovimentacao.obterOrigem());
	                            novaAgenda.atribuirDestino(agendaMovimentacao.obterDestino());
	                            novaAgenda.atribuirResponsavel(agendaMovimentacao.obterResponsavel());
	                            novaAgenda.atribuirDataPrevistaInicio(dataModificada);
	                            novaAgenda.atribuirTipo(agendaMovimentacao.obterTipo());
	                            novaAgenda.atribuirMesMovimento(mesMovimento);
	                            novaAgenda.atribuirAnoMovimento(anoMovimento);
	                            String mesModificado = "";
	                            if((new Integer(mesMovimento)).toString().length() == 1)
	                                mesModificado = "0" + mesMovimento;
	                            else
	                                mesModificado = (new Integer(mesMovimento)).toString();
	                            novaAgenda.atribuirTitulo(mesModificado + " - " + anoMovimento + " (Consistencia del archivo)");
	                            novaAgenda.incluir();
	                            
	                            novaAgenda.atualizarValidacao("Total");
	                            novaAgenda.atualizarEspecial("Sim");
	                            novaAgenda.atualizarInscricaoEspecial("Sim");
	                            novaAgenda.atualizarSuplementosEspecial("Sim");
	                            novaAgenda.atualizarCapitalEspecial("Sim");
	                            novaAgenda.atualizarDataEspecial("Sim");
	                            //novaAgenda.atualizarDocumentoEspecial("Não");
	                            novaAgenda.atualizarDocumentoEspecial("Sim");
	                            novaAgenda.atualizarApAnteriorEspecial("Sim");
	                            novaAgenda.atualizarEndosoApolice("Sim");
	                            
	                            //System.out.println("Gravou agenda do mês seguinte");
	                        }*/
	                        
	                        agendaMovimentacao.atualizaUltimaAgenda("Instrumento");
	                        
	                        String mesAno = agendaMovimentacao.obterMesMovimento() + "/"+agendaMovimentacao.obterAnoMovimento();
							String titulo = origem.obterNome() + " - " + mesAno;
							
							processamentoInstrumento.addAgenda(nomeArquivo, "Notificación de Recibimiento - " + new SimpleDateFormat("HH:mm:ss").format(new Date()), agendaMovimentacao,0);
							
							String descricao2 = "Este correo fue enviado de forma automática, favor no responder al mismo\n\nEl archivo del Módulo Central de Información del mes de "+mesAno+" fue incluido satisfactoriamente.";
							this.enviarEmail(titulo, descricao2, origem);
	                    }
	                    
	                    //this.gravaLogFTP(agendaMovimentacao, descricaoFTP);
	                    
	                    /*System.out.println("Copiando tabelas temp");
	            		SQLUpdate update = mm.createSQLUpdate("EXEC copiarTabelasTmp");
	            		update.execute();*/
	            		
	           			agendaMovimentacao.adicionarComentario("Consistencia", "Termino " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
	           			
	           			String nomeArquivoA = "A" + sigla.trim() + ano2 + mes2 + ".txt";
	           			String nomeArquivoB = "B" + sigla.trim() + ano2 + mes2 + ".txt";
	           			
	           			File arquivo = new File("C:/Aseguradoras/Archivos/"+nomeArquivoA);
	           			if(arquivo.exists())
	           			{
		           			arquivo.delete();
		           			//System.out.println("Delete "+nomeArquivoA+" = " + arquivo.delete());
	           			}
	           			
	           			arquivo = new File("C:/Aseguradoras/Archivos/"+nomeArquivoB);
	           			if(arquivo.exists())
	           			{
		           			arquivo.delete();
		           			//System.out.println("Delete "+nomeArquivoB+" = " + arquivo.delete());
	           			}
	                }
        		//}
                
                mm.commitTransaction();
                
                //this.verificarAgendasInstrumento();
            //}
        }
        catch(Exception exception)
        {
         	//System.out.println(exception.getMessage());
        	exception.printStackTrace();
        	mm.rollbackTransaction();
            if(agendaMovimentacao2 != null)
            {
            	mm.beginTransaction();
            	
            	agendaMovimentacao2.concluirNotificacoesInferiores();
            	
                Notificacao notificacao = (Notificacao)mm.getEntity("Notificacao");
                notificacao.atribuirSuperior(agendaMovimentacao2);
                notificacao.atribuirOrigem(agendaMovimentacao2.obterOrigem());
                notificacao.atribuirDestino(destino);
                notificacao.atribuirResponsavel(agendaMovimentacao2.obterResponsavel());
                notificacao.atribuirTipo("Notificaci\363n de Error de Validaci\363n");
                notificacao.atribuirTitulo("Notificaci\363n de Error de Validaci\363n");
                
                String msgErros = exception.getMessage() + "\n";
                
                for(String msgAux : validacaoErros)
                    msgErros +=  msgAux + "\r\n";
                
                notificacao.atribuirDescricao(msgErros);
                
                notificacao.incluir();
                
                //this.gravaLogFTP(agendaMovimentacao2, msgErros);
                
                System.out.println("Gravou a notificação de erro no catch");
                
                agendaMovimentacao2.atualizarFase2("pendente");
                
                System.out.println("Colocou a agenda com erro no catch na fase pendente");
                
                //System.out.println("Gravou agendas no catch");
                Entidade origem = agendaMovimentacao2.obterOrigem();
                
                String mesAno = agendaMovimentacao2.obterMesMovimento() + "/"+agendaMovimentacao2.obterAnoMovimento();
				String titulo = origem.obterNome() + " - " + mesAno;
				
				String descricao2 = "Al validar los archivos de la Central de Información (Módulo Instrumento) del mes de "+mesAno+" ocurrió el siguiente error:\n"+msgErros;
				this.enviarEmail(titulo, descricao2, origem);
                
				/* System.out.println("Copiar tabelas temp");
				 SQLUpdate update = mm.createSQLUpdate("crm","EXEC copiarTabelasTmp");
				 update.execute();*/
	        		
                mm.commitTransaction();
                //this.verificarAgendasInstrumento();
            }
        }
    }
    
    private InfraProperties infra;
    
    private void enviarEmail(String titulo, String descricao, Entidade entidade)
	{
		try
		{
			infra = InfraProperties.getInstance();
			String dir = infra.getProperty("arquivos.url");
			boolean enviaEmail = dir.indexOf("Laptopsala") == -1;
			
			Email email = new SimpleEmail();
			email.setHostName("mail.bcp.gov.py");
			email.setSmtpPort(25);
			email.setFrom("sisvalidacion@bcp.gov.py");
			email.setSubject(titulo);
			email.setMsg(descricao);
			email.addTo("gbrawer@bcp.gov.py");
			email.addCc("cferrac@bcp.gov.py");
			email.addCc("prodriguez@bcp.gov.py");
			
			Collection<String> emailsExternos = new ArrayList<>();
			
			Collection<Entidade.Contato> contatos = entidade.obterContatos();
			for(Entidade.Contato contato : contatos)
			{
				String nome = contato.obterNome().toLowerCase();
				if(nome.startsWith("email") || nome.startsWith("e-mail"))
				{
					String valor = contato.obterValor();
					if(valor.indexOf("@")>-1)
						emailsExternos.add(valor);
				}
			}
			
			if(descricao.indexOf("SQLServerException") == -1 && descricao.indexOf("infra.connection") == -1)
			{
				for(String emailStr : emailsExternos)
					email.addCc(emailStr);
				
				if(enviaEmail)
					email.send();
			}
			else
			{
				//Manda com a descrição do erro para os admins
				if(enviaEmail)
					email.send();
				
				//Manda com a descrição amigavel para todos
				email = new SimpleEmail();
				email.setHostName("mail.bcp.gov.py");
				email.setSmtpPort(25);
				email.setFrom("sisvalidacion@bcp.gov.py");
				email.setSubject(titulo);
				email.addTo("gbrawer@bcp.gov.py");
				email.addCc("cferrac@bcp.gov.py");
				email.addCc("prodriguez@bcp.gov.py");
				email.setMsg("Validación interrumpida volver a grabar los archivos");
				
				for(String emailStr : emailsExternos)
					email.addCc(emailStr);
				
				if(enviaEmail)
					email.send();
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
    
    public void verificarDuplicidades() throws Exception
    {
    	 SampleModelManager mm = new SampleModelManager();
         ManutencaoHome manutencao = (ManutencaoHome) mm.getHome("ManutencaoHome");
         System.out.println("Executando Manutencao na Base de dados... Isso pode demorar alguns minutos !");
         
         //EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");
         //Aseguradora aa = (Aseguradora) home.obterEntidadePorId(5211);
         //this.aseguradoras.put(aa.obterId(), aa);
         if(this.aseguradoras.size() > 0)
        	 manutencao.verificarDuplicidades(this.aseguradoras.values());
    }
    
    public void arrumarDatasReaseguro() throws Exception
    {
    	 SampleModelManager mm = new SampleModelManager();
         ManutencaoHome manutencao = (ManutencaoHome) mm.getHome("ManutencaoHome");
         System.out.println("Executando Manutencao na Base de dados... Reaseguros !");
         
         //manutencao.datasReaseguro();
         manutencao.datasReaseguro2();
    }

    public void verificarAgendas()
        throws Exception
    {
        SampleModelManager mm = new SampleModelManager();
        UsuarioHome usuarioHome = (UsuarioHome)mm.getHome("UsuarioHome");
        Usuario usuario = usuarioHome.obterUsuarioPorChave("admin");
        EntidadeHome entidadeHome = (EntidadeHome)mm.getHome("EntidadeHome");
        EventoHome eventoHome = (EventoHome)mm.getHome("EventoHome");
        mm.beginTransaction();
        try
        {
            Parametro parametro = (Parametro)entidadeHome.obterEntidadePorApelido("parametros");
            for(Iterator i = usuario.verificarAgendas().iterator(); i.hasNext();)
            {
                AgendaMovimentacao ag = (AgendaMovimentacao)i.next();
                if((new Date()).after(ag.obterDataPrevistaInicio()))
                {
                    Calendar dia = Calendar.getInstance();
                    dia.setTime(ag.obterDataPrevistaInicio());
                    dia.add(5, Integer.parseInt(parametro.obterAtributo("diaatraso").obterValor()));
                    if(ag.obterDataPrevistaInicio().getDay() != 0 && ag.obterDataPrevistaInicio().getDay() != 6 && !parametro.obterFeriados().contains(ag.obterDataPrevistaInicio()))
                    {
                        if(dia.getTime().getDay() != 0 && dia.getTime().getDay() != 6 && !parametro.obterFeriados().contains(dia.getTime()) && (new Date()).getDate() - dia.getTime().getDate() > Integer.parseInt(parametro.obterAtributo("diaatraso").obterValor()))
                            enviarEmail(ag);
                    } else
                    {
                        for(boolean diaUtil = false; !diaUtil;)
                        {
                            dia.add(5, 1);
                            if(dia.getTime().getDay() != 0 && dia.getTime().getDay() != 6 && !parametro.obterFeriados().contains(dia.getTime()))
                                diaUtil = true;
                        }

                        if((new Date()).getDate() - dia.getTime().getDate() > Integer.parseInt(parametro.obterAtributo("diaatraso").obterValor()))
                            enviarEmail(ag);
                    }
                }
            }

            mm.commitTransaction();
        }
        catch(Exception exception)
        {
            System.out.println("Erro 1: " + Util.translateException(exception));
            mm.rollbackTransaction();
        }
    }

    private void enviarEmail(AgendaMovimentacao agenda)
        throws Exception
    {
        SampleModelManager mm = new SampleModelManager();
        EntidadeHome entidadeHome = (EntidadeHome)mm.getHome("EntidadeHome");
        
        mm.beginTransaction();
        try
        {
            Notificacao notificacao = (Notificacao)mm.getEntity("Notificacao");
            notificacao.atribuirSuperior(agenda);
            notificacao.atribuirOrigem(agenda.obterOrigem());
            notificacao.atribuirDestino(agenda.obterDestino());
            notificacao.atribuirResponsavel(agenda.obterResponsavel());
            notificacao.atribuirTipo("Notificaci\363n de Atraso");
            notificacao.atribuirTitulo("Notificaci\363n de Atraso");
            Parametro parametro = (Parametro)entidadeHome.obterEntidadePorApelido("parametros");
            String descricao = parametro.obterAtributo("notificacaoatraso").obterValor();
            notificacao.atribuirDescricao(descricao);
            notificacao.incluir();
            MailManager mail = new MailManager();
            MailServer server = new MailServer();
            mail.send(server, "giovanni@horst.com.br", "", "", "giovanni@horst.com.br", "Notificaci\363n de Atraso", descricao);
            mm.commitTransaction();
        }
        catch(Exception exception)
        {
            System.out.println("Erro 2: " + Util.translateException(exception));
            mm.rollbackTransaction();
        }
    }

    public void verificarLivros()  throws Exception
    {
        SampleModelManager mm = new SampleModelManager();
        //EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
        EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
        LivroHome livroHome = (LivroHome) mm.getHome("LivroHome");
        UploadedFileHome upHome = (UploadedFileHome) mm.getHome("UploadedFileHome");
        mm.beginTransaction();
        try
        {
            File dir = new File("C:/Aseguradoras/libros");
            
            File[] arquivos = dir.listFiles();
            
            Uteis uteis = new Uteis();
            
            for(int i = 0 ; i < arquivos.length ; i++)
            {
            	File arquivo = arquivos[i];
            	
            	String nomeArquivo = arquivo.getName();
            	
            	String tipo = nomeArquivo.substring(0,1);
            	
            	if(tipo.toLowerCase().equals("l"))
            	{
	            	String sigla = nomeArquivo.substring(1,4); 
	            	
	            	String codLivro = nomeArquivo.substring(4,13);
	            	
	            	int ano = Integer.parseInt(nomeArquivo.substring(13,17));
	            	
	            	int mes = Integer.parseInt(nomeArquivo.substring(17,19));
	            	
	            	String extensao = nomeArquivo.substring(20,nomeArquivo.length());
	            	
	            	String mimeType = Files.probeContentType(arquivo.toPath());
	            	
	            	if(mimeType == null)
	            	{
	            		if(extensao.startsWith("xl"))
	            			mimeType = "application/vnd.ms-excel";
	            		else if(extensao.startsWith("pd"))
	            			mimeType = "application/pdf";
	            		else if(extensao.startsWith("do"))
	            			mimeType = "application/msword";
	            	}
	            	
	            	System.out.println(sigla + " " + codLivro + " " + ano + " " + mes + " " + extensao + " " + mimeType);
	            	
	            	Aseguradora aseg = (Aseguradora) entidadeHome.obterEntidadePorSigla(sigla);
	            	if(aseg!=null)
	            	{
	            		//System.out.println(aseg.obterNome());
	            		String tipoLivro = uteis.obterNomeArquivo(codLivro);
	            		
	            		Livro livro = livroHome.obterLivro(aseg, tipoLivro, mes, ano);
	    				if(livro == null)
	    				{
		            		livro = (Livro) mm.getEntity("Livro");
		            		livro.atribuirTitulo(tipoLivro);
		            		livro.atribuirTipo(tipoLivro);
		    				livro.atribuirMes(mes);
		    				livro.atribuirAno(ano);
		    				livro.atribuirOrigem(aseg);
		    				livro.incluir();
		    				
		    				livro.atualizarFase(Evento.EVENTO_CONCLUIDO);
	    				}
	    				
	    				Collection<UploadedFile> files = upHome.getAllUploadedFiles(livro);
	    				Collection<UploadedFile> filesExcluir = new ArrayList<UploadedFile>();
	    				
	    				for(Iterator<UploadedFile> j = files.iterator() ; j.hasNext() ; )
	    				{
	    					UploadedFile up = j.next();
	    					
	    					if(up.getName().equals(nomeArquivo))
	    						filesExcluir.add(up);
	    				}
	    				
	    				for(Iterator<UploadedFile> j = filesExcluir.iterator() ; j.hasNext() ; )
	    				{
	    					UploadedFile up = j.next();
	    					
	    					upHome.removeUploadedFile(up.getId());
	    				}
	    				
    					InputStream input = new FileInputStream(arquivo);
    					upHome.addUploadedFile(livro, input, nomeArquivo, mimeType, input.available(), 0);
    					input.close();
	            	}
	            	
	            	 InputStream is = new FileInputStream(arquivo);
	                 OutputStream os = new FileOutputStream("C:/Aseguradoras/backup_libros/" + nomeArquivo);
	                 byte buffer[] = new byte[is.available()];
	                 is.read(buffer);
	                 os.write(buffer);
	                 
	                 if(is != null)
	                     is.close();
	                 if(os != null)
	                     os.close();
	            	
	            	arquivo.delete();
            	}
            }
            
        	mm.commitTransaction();
        }
        catch(Exception exception)
        {
            System.out.println("Erro na Verificacao dos Livros: " + Util.translateException(exception));
            mm.rollbackTransaction();
        }
    }
    
    private void copiarArquivo() throws Exception
    {
        InputStream is = null;
        OutputStream os = null;
        boolean success = true;
        try
        {
            is = new FileInputStream("C:/Aseguradoras/Archivos/" + nomeArquivo + ".txt");
            os = new FileOutputStream("C:/Aseguradoras/Backup/" + nomeArquivo + "_Backup.txt");
            byte buffer[] = new byte[is.available()];
            is.read(buffer);
            os.write(buffer);
        }
        catch(IOException e)
        {
            success = false;
        }
        catch(OutOfMemoryError e)
        {
            success = false;
        }
        finally
        {
            try
            {
                if(is != null)
                    is.close();
                if(os != null)
                    os.close();
            }
            catch(IOException e)
            {
                System.out.println(e.getMessage());
            }
        }
        File arquivo = new File("C:/Aseguradoras/Archivos/" + nomeArquivo + ".txt");
        arquivo.delete();
        System.out.println("arquivo.delete(): " + arquivo.delete());
        return;
    }
    
    public void verificarInscricoes()  throws Exception
    {
        SampleModelManager mm = new SampleModelManager();
        EventoHome eventoHome = (EventoHome)mm.getHome("EventoHome");
        mm.beginTransaction();
        try
        {
            eventoHome.validarTodasAsIncricoes();
            
        	mm.commitTransaction();
        }
        catch(Exception exception)
        {
            System.out.println("Erro na Verificacao das Inscricoes: " + Util.translateException(exception));
            mm.rollbackTransaction();
        }
    }
    
   
}
