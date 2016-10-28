package com.gio.crm.model;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import org.apache.tika.Tika;

import infra.config.InfraProperties;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;

public class Uteis 
{
	private Map<String, String> nomeArquivos()
	{
		Map<String,String> nomeLivros = new TreeMap<String,String>();
		
		nomeLivros.put("SISLIBA10", "SEGUROS DIRECTOS SISLIBA10");
		nomeLivros.put("SISLIBA20", "REASEGUROS ACEPTADOS SISLIBA20");
		nomeLivros.put("SISLIBA30", "REASEGUROS CEDIDOS/RETROCEDIDOS SISLIBA30");
		nomeLivros.put("SISLIBB00", "EXTRACTO DEL LIBRO DE SINIESTROS SISLIBB00");
		nomeLivros.put("SISLIBC00", "EXTRACTO DEL LIBRO DE ACTUACIONES JUDICIALES SISLIBC00");
		nomeLivros.put("SISLIBD10", "DETALLE DE PROVISIONES Y RESERVAS-PATRIMONIALES SISLIBD10");
		nomeLivros.put("SISLIBD20", "DETALLE DE PROVISIONES Y RESERVAS-VIDA SISLIBD20");
		nomeLivros.put("SISLIBD30", "DETALLE DE PROVISIONES SINIESTROS PENDIENTES-TRIMESTRAL SISLIBD30");
		nomeLivros.put("SISLIBD40", "DETALLE DE PROVISIONES SINIESTROS PENDIENTES-VIDA SISLIBD40");
		nomeLivros.put("SISLIBD51", "RESUMEN DE LA COMPOSICION DE LA CARTERA DE INVERSION SISLIBD51");
		nomeLivros.put("SISLIBD52", "RESUMEN DEL ESTADO DE REPRESENTATIVIDAD SEGUROS DE VIDA SISLIBD52");
		nomeLivros.put("SISLIBD53", "RESUMEN DEL ESTADO DE REPRESENTATIVIDAD SEGUROS PATRIMONIALES SISLIBD53");
		nomeLivros.put("SISLIBD54", "RESUMEN DEL ESTADO DE LIQUIDEZ SISLIBD54");
		nomeLivros.put("SISLIBE00", "ROBO DE VEHÍCULOS SISLIBE00");
		nomeLivros.put("SISLIBF01", "Patrimonio Propio no comprometido o Patrimonio Técnico SISLIBF01".toUpperCase());
		nomeLivros.put("SISLIBF02", "Cálculo del Factor de Retención SISLIBF02".toUpperCase());
		nomeLivros.put("SISLIBF03" , "Margen de Solvencia requerido en función de las primas SISLIBF03".toUpperCase());
		nomeLivros.put("SISLIBF04", "Margen de Solvencia requerido en función de los siniestros SISLIBF04".toUpperCase());
		nomeLivros.put("SISLIBF05", "Seg. Vida - Patr. prop. no comprom. o patr. téc. SISLIBF05".toUpperCase());
		nomeLivros.put("SISLIBF06", "Seg. Vida - Margen de solvencia - Res. matem. - Cap. en riesgo SISLIBF06".toUpperCase());
		nomeLivros.put("SISLIBF07", "Cia operam ambas ramas - Patr. prop no comprom. o patr. téc. SISLIBF07".toUpperCase());
		nomeLivros.put("SISLIBF08", "Fondo de Garantía SISLIBF08".toUpperCase());
		nomeLivros.put("SISLIBF21", "Cálculo del factor de retención - seccción automóviles SISLIBF21".toUpperCase());
		nomeLivros.put("SISLIBF22", "Cálculo del factor de retención - seccción de incendio SISLIBF22".toUpperCase());
		nomeLivros.put("SISLIBF23", "Cálculo del factor de retención - seccción riesgos varios SISLIBF23".toUpperCase());
		nomeLivros.put("SISLIBF24", "Cálculo del factor de retención - seccción personales a corto plazo SISLIBF24".toUpperCase());
		nomeLivros.put("SISLIBF25", "Cálculo del factor de retención - seccción varias SISLIBF25".toUpperCase());
		nomeLivros.put("SISLIBF31", "Margen de solvencia requerido en función de las primas - seccción automóviles SISLIBF31".toUpperCase());
		nomeLivros.put("SISLIBF32", "Margen de solvencia requerido en función de las primas - seccción de incendio SISLIBF32".toUpperCase());
		nomeLivros.put("SISLIBF33", "Margen de solvencia requerido en función de las primas - seccción riesgos varios SISLIBF33".toUpperCase());
		nomeLivros.put("SISLIBF34", "Margen de solvencia requerido en función de las primas - seccción personales a corto plazo SISLIBF34".toUpperCase());
		nomeLivros.put("SISLIBF35", "Margen de solvencia requerido en función de las primas - seccción varias SISLIBF35".toUpperCase());
		nomeLivros.put("SISLIBF41", "Margen de solvencia requerido en función de los siniestros - seccción automóviles SISLIBF41".toUpperCase());
		nomeLivros.put("SISLIBF42", "Margen de solvencia requerido en función de los siniestros - seccción de incendio SISLIBF42".toUpperCase());
		nomeLivros.put("SISLIBF43", "Margen de solvencia requerido en función de los siniestros - seccción riesgos varios SISLIBF43".toUpperCase());
		nomeLivros.put("SISLIBF44", "Margen de solvencia requerido en función de los siniestros - seccción personales a corto plazo SISLIBF44".toUpperCase());
		nomeLivros.put("SISLIBF45", "Margen de solvencia requerido en función de los siniestros - seccción varias SISLIBF45".toUpperCase());
		nomeLivros.put("SISLIBA40","Libro de Producción por Agentes y Corredores de Seguros SISLIBA40".toUpperCase());
		
		return nomeLivros;
	}
	
	public String obterNomeArquivo(String tipo)
	{
		if(this.nomeArquivos().containsKey(tipo))
			return this.nomeArquivos().get(tipo);
		else
			return null;
	}
	
	public Collection<String> obterMimeTypes()
	{
		Collection<String> mimeTypes = new ArrayList<String>();
		
		mimeTypes.addAll(this.obterMimeTypesPDF());
		mimeTypes.addAll(this.obterMimeTypesWord());
		mimeTypes.addAll(this.obterMimeTypesExcel());
		
		return mimeTypes;
	}
	
	public Collection<String> obterMimeTypesPDF()
	{
		Collection<String> mimeTypes = new ArrayList<String>();
		mimeTypes.add("application/pdf");
		mimeTypes.add("application/x-pdf");
		mimeTypes.add("application/acrobat");
		mimeTypes.add("applications/vnd.pdf");
		mimeTypes.add("text/pdf");
		mimeTypes.add("text/x-pdf");
		
		return mimeTypes;
	}
	
	public Collection<String> obterMimeTypesWord()
	{
		Collection<String> mimeTypes = new ArrayList<String>();
		mimeTypes.add("application/msword");
		mimeTypes.add("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		mimeTypes.add("application/vnd.openxmlformats-officedocument.wordprocessingml.template");
		mimeTypes.add("application/vnd.ms-word.document.macroEnabled.12");
		mimeTypes.add("application/vnd.ms-word.template.macroEnabled.12");
		
		return mimeTypes;
	}
	
	public Collection<String> obterMimeTypesExcel()
	{
		Collection<String> mimeTypes = new ArrayList<String>();
		mimeTypes.add("application/vnd.ms-excel");
		mimeTypes.add("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		mimeTypes.add("application/vnd.openxmlformats-officedocument.spreadsheetml.template");
		mimeTypes.add("application/vnd.ms-excel.sheet.macroEnabled.12");
		mimeTypes.add("application/vnd.ms-excel.template.macroEnabled.12");
		mimeTypes.add("application/vnd.ms-excel.addin.macroEnabled.12");
		mimeTypes.add("application/vnd.ms-excel.sheet.binary.macroEnabled.12");
		
		return mimeTypes;
	}
	
	//private FileWriter writer;
	private SimpleDateFormat formataData;
	private AgendaBat bat;
	private Map<Long,Aseguradora> aseguradoras;
	private InputStream input;
	
	public Collection<Aseguradora> copiarArquivos(SampleModelManager mm, Usuario usuarioAtual, Processamento processamentoContabil, Processamento processamentoLivro, Processamento processamentoInstrumento, Map<String,SmbFile> arquivos, NtlmPasswordAuthentication auth) throws Exception
	{
		//this.writer = writer;
		
		infra = InfraProperties.getInstance();
		String dir = infra.getProperty("arquivos.url");
		boolean enviaEmail = dir.indexOf("Laptopsala") == -1;
		
		aseguradoras = new TreeMap<>();
		
		String DIRETORIO_TXT = "C:/Aseguradoras/Archivos/";
		String DIRETORIO_LIVROS = "C:/Aseguradoras/libros";
		String diretorioFinal = "";
		Collection<String> tipos = this.obterMimeTypes();
		
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");
		AgendaMovimentacaoHome agendaHome = (AgendaMovimentacaoHome) mm.getHome("AgendaMovimentacaoHome");
		Entidade destino = (Entidade) home.obterEntidadePorApelido("SuperSeg");//BCP
		LivroHome livroHome = (LivroHome) mm.getHome("LivroHome");
		UploadedFileHome upHome = (UploadedFileHome) mm.getHome("UploadedFileHome");
		
		Tika tika;
		boolean podeCopiar,contabil,instrumento,livroBollean,agendaConcluida,agendaPendente;
		String nomeArquivo,primeiraLetra,mimeType,extensao,siglaArquivo,anoArquivo,mesArquivo,erro,texto,mesAno,titulo,descricao,codLivro,sigla,tipoLivro,mesStr;
		Aseguradora aseguradora;
		Entidade origem;
		AgendaMovimentacao agendaMovimentacao;
		Collection<AgendaMovimentacao> agendas;
		int ano,mes;
		Livro livro;
		Collection<UploadedFile> filesGravadas;
		Collection<UploadedFile> filesExcluir;
		Collection<SmbFile> arquivosExcluir = new ArrayList<>();
		formataData = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		for(SmbFile arquivo : arquivos.values())
		{
			//System.out.println(arquivos.size());
			
			erro = "";
			descricao = "";
			
			//writer.write("***** Arquivo: " + arquivo.getName() + " - " + formataData.format(new Date()) + " *****\r\n");
			
			diretorioFinal = "";
			podeCopiar = false;
			contabil = false;
			instrumento = false;
			livroBollean = false;
			
			nomeArquivo = arquivo.getName();
			primeiraLetra = nomeArquivo.substring(0,1);
			tika = new Tika();
			try
			{
				input = arquivo.getInputStream();
				mimeType = tika.detect(input);
				input.close();
			}
			catch(Exception e)
			{
				mimeType = "oct";
				input.close();
			}
		    
			try
			{
				if(primeiraLetra.equals("0"))
				{
					//System.out.println("txt");
					
					if(mimeType.equals("text/plain"))
					{
						diretorioFinal = DIRETORIO_TXT;
						//System.out.println("txt");
						podeCopiar = true;
					}
					
					contabil = true;
				}
				else if(primeiraLetra.toLowerCase().equals("a"))
				{
					if(mimeType.equals("text/plain"))
					{
						diretorioFinal = DIRETORIO_TXT;
						//System.out.println("txt");
						podeCopiar = true;
					}
					
					instrumento = true;
				}
				else if(primeiraLetra.toLowerCase().equals("l"))
				{
					diretorioFinal = DIRETORIO_LIVROS;
					//System.out.println("livro");
					
					if(mimeType == null || mimeType.indexOf("oct")>-1 || mimeType.indexOf("tika")>-1)
					{
						extensao = nomeArquivo.substring(20,nomeArquivo.length());
						
						if(extensao.startsWith("xl"))
							mimeType = "application/vnd.ms-excel";
	            		else if(extensao.startsWith("pd"))
	            			mimeType = "application/pdf";
	            		else if(extensao.startsWith("do"))
	            			mimeType = "application/msword";
					}
					
					podeCopiar = tipos.contains(mimeType);
					livroBollean = podeCopiar;
				}
				
				if(podeCopiar)
				{
					if(primeiraLetra.toLowerCase().equals("a"))
					{
						String nomeCopia = nomeArquivo.replace("A", "B");
						
						if(arquivos.containsKey(nomeCopia))
						{
							SmbFile arquivoB = arquivos.get(nomeCopia);
							if(arquivo.exists() && arquivoB.exists())
							{
								System.out.println("Copiando Archivo: " + arquivo.getName());
								
								input = arquivo.getInputStream();
								
								Files.copy(input, new File(diretorioFinal+"/"+nomeArquivo).toPath(), StandardCopyOption.REPLACE_EXISTING);
								arquivosExcluir.add(arquivo);
								input.close();
								
								System.out.println("Copiando Archivo: " + arquivoB.getName());
								input = arquivoB.getInputStream();
								Files.copy(input, new File(diretorioFinal+"/"+nomeCopia).toPath(), StandardCopyOption.REPLACE_EXISTING);
								arquivosExcluir.add(arquivoB);
								input.close();
							}
						}
					}
					else if(!primeiraLetra.toLowerCase().equals("b"))
					{
						if(arquivo.exists())
						{
							System.out.println("Copiando Archivo: " + arquivo.getName());
							//writer.write("Copiando Arquivo: " + arquivo.getName() + " - " + formataData.format(new Date()) + "\r\n");
							
							input = arquivo.getInputStream();
							
							Files.copy(input, new File(diretorioFinal+"/"+nomeArquivo).toPath(), StandardCopyOption.REPLACE_EXISTING);
							arquivosExcluir.add(arquivo);
							input.close();
						}
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			try
			{
				processamentoContabil.atualizarProcessando(nomeArquivo);
				
				if(contabil || instrumento)
				{
					siglaArquivo = "";
					String tipoAgenda = "";
					if(contabil)
						tipoAgenda = "Contabil";
    				else
    					tipoAgenda = "Instrumento";
					
					if(contabil)
						siglaArquivo = nomeArquivo.substring(0, 3);
					else
						siglaArquivo = nomeArquivo.substring(1, 4);
					
					aseguradora = (Aseguradora) home.obterEntidadePorSigla(siglaArquivo);
		    		agendaConcluida = false;
		    		
		    		if(aseguradora != null)
		    		{
		    			if((instrumento && primeiraLetra.toLowerCase().equals("a") || contabil))
		    			{
		    				anoArquivo = "";
			    			mesArquivo = "";
			    			if(contabil)
			    			{
			    				anoArquivo = nomeArquivo.substring(3,7);
			    				mesArquivo = nomeArquivo.substring(7,9);
			    			}
			    			else
			    			{
			    				anoArquivo = nomeArquivo.substring(4,8);
			    				mesArquivo = nomeArquivo.substring(8,10);
			    			}
			    			
			    			agendaMovimentacao = null;
			    			if(contabil)
			    				agendaMovimentacao = agendaHome.obterAgendaNoPeriodo(Integer.valueOf(mesArquivo), Integer.valueOf(anoArquivo), aseguradora, "Contabil");
			    			else if(instrumento)
			    				agendaMovimentacao = agendaHome.obterAgendaNoPeriodo(Integer.valueOf(mesArquivo), Integer.valueOf(anoArquivo), aseguradora, "Instrumento");
			    			
			    			if(agendaMovimentacao == null)
			    			{
			    				agendaMovimentacao = (AgendaMovimentacao) mm.getEntity("AgendaMovimentacao");
			    				agendaMovimentacao.atribuirOrigem(aseguradora);
		    					agendaMovimentacao.atribuirTipo(tipoAgenda);
				    			agendaMovimentacao.atribuirResponsavel(usuarioAtual);
		    					agendaMovimentacao.atribuirDescricao("Archivo carpeta Archivos Generales - " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
		    					agendaMovimentacao.atribuirDestino(destino);
		    					agendaMovimentacao.atribuirTitulo(mesArquivo +" - " + anoArquivo);
		    					agendaMovimentacao.atribuirDataPrevistaInicio(new Date());
		    					agendaMovimentacao.atribuirAnoMovimento(Integer.valueOf(anoArquivo));
		    					agendaMovimentacao.atribuirMesMovimento(Integer.valueOf(mesArquivo));
		    					
			    			}
			    			else if(agendaMovimentacao.obterFase().obterCodigo().equals(AgendaMovimentacao.EVENTO_CONCLUIDO))
			    			{
			    				erro="Agenda " + agendaMovimentacao.obterMesMovimento() + "/" + agendaMovimentacao.obterAnoMovimento() + " concluida, no se procesó - " + new SimpleDateFormat("HH:mm:ss").format(new Date());
			    				if(contabil)
			    					processamentoContabil.addAgenda(nomeArquivo, erro, agendaMovimentacao,1);
			    				else
			    					processamentoInstrumento.addAgenda(nomeArquivo, erro, agendaMovimentacao,1);
			    				
			    				agendaConcluida = true;
			    			}
			    			
			    			if(!agendaConcluida)
			    			{
			    				agendaPendente = false;
			    				
			    				agendas = aseguradora.obterAgendasPendentes(tipoAgenda);
		    					
		    					for(AgendaMovimentacao agenda : agendas)
		    					{
									if(agenda.obterId()!=agendaMovimentacao.obterId())
									{
										erro+="Existe Agenda Pendiente " + agenda.obterMesMovimento() + "/" + agenda.obterAnoMovimento() + " - " + new SimpleDateFormat("HH:mm:ss").format(new Date());
										agendaPendente = true;
										break;
									}
		    					}
		    					
		    					if(!agendaPendente)
		    					{
			    					Calendar c = Calendar.getInstance();
			    					Date dataAgendaAtual = new SimpleDateFormat("dd/MM/yyyy").parse("01/"+agendaMovimentacao.obterMesMovimento()+"/"+agendaMovimentacao.obterAnoMovimento());
			    					c.setTime(dataAgendaAtual);
			    					Date dataMesAnterior;
			    					int mesAnterior;
			    					int anoAnterior;
			    					
			    					for(int i = 1 ; i<=12 ; i++)
			    					{
				    					c.add(Calendar.MONTH, -i);
				    					
				    					dataMesAnterior = c.getTime();
				    					mesAnterior = Integer.valueOf(new SimpleDateFormat("MM").format(dataMesAnterior));
				    					anoAnterior = Integer.valueOf(new SimpleDateFormat("yyyy").format(dataMesAnterior));
				    					
				    					if(agendaHome.obterAgendaNoPeriodo(mesAnterior, anoAnterior, aseguradora, tipoAgenda) == null)
				    					{
				    						erro+="Existe Agenda Pendiente " + mesAnterior + "/" + anoAnterior + " - " + new SimpleDateFormat("HH:mm:ss").format(new Date());
											agendaPendente = true;
											break;
				    					}
			    					}
		    					}
		    					
		    					if(!agendaPendente)
		    					{
		    						if(agendaMovimentacao.obterId() == 0)
		    						{
		    							agendaMovimentacao.incluir();
		    	    					agendaMovimentacao.atualizarValidacao("Total");
		    	    					if(!contabil)
				    					{
				    						agendaMovimentacao.atualizarValidacao("Total");
				    						agendaMovimentacao.atualizarEspecial("Sim");
				    						agendaMovimentacao.atualizarInscricaoEspecial("Sim");
				    						agendaMovimentacao.atualizarSuplementosEspecial("Sim");
				    						agendaMovimentacao.atualizarCapitalEspecial("Sim");
				    						agendaMovimentacao.atualizarDataEspecial("Sim");
				    						if(enviaEmail)
				    							agendaMovimentacao.atualizarDocumentoEspecial("Não");
				    						else
				    							agendaMovimentacao.atualizarDocumentoEspecial("Sim");
				    						agendaMovimentacao.atualizarApAnteriorEspecial("Sim");
				    						agendaMovimentacao.atualizarEndosoApolice("Sim");
				    					}
		    						}
		    						else
		    						{
		    							if(!contabil)
				    					{
				    						agendaMovimentacao.atualizarValidacao("Total");
				    						agendaMovimentacao.atualizarEspecial("Sim");
				    						agendaMovimentacao.atualizarInscricaoEspecial("Sim");
				    						agendaMovimentacao.atualizarSuplementosEspecial("Sim");
				    						agendaMovimentacao.atualizarCapitalEspecial("Sim");
				    						agendaMovimentacao.atualizarDataEspecial("Sim");
				    						if(enviaEmail)
				    							agendaMovimentacao.atualizarDocumentoEspecial("Não");
				    						else
				    							agendaMovimentacao.atualizarDocumentoEspecial("Sim");
				    						agendaMovimentacao.atualizarApAnteriorEspecial("Sim");
				    						agendaMovimentacao.atualizarEndosoApolice("Sim");
				    					}
		    						}
		    						
		    						if(contabil)
		    							this.validarArquivosContabeis(agendaMovimentacao, home, nomeArquivo, mm, processamentoContabil);
		    						else
		    						{
		    							//agendaMovimentacao.atualizarFase2(AgendaMovimentacao.AGENDADA);
		    							
		    							bat = new AgendaBat();
		    							bat.verificarAgendasInstrumento(agendaMovimentacao, processamentoInstrumento, nomeArquivo);
		    							aseguradoras.put(aseguradora.obterId(),aseguradora);
		    						}
		    					}
		    					else
		    					{
		    						origem = agendaMovimentacao.obterOrigem();
		    						mesAno = agendaMovimentacao.obterMesMovimento() + "/"+agendaMovimentacao.obterAnoMovimento();
		    						titulo = origem.obterNome() + " - " + mesAno;
		    						if(contabil)
		    						{
			    						processamentoContabil.addAgenda(nomeArquivo, erro, agendaMovimentacao,1);
			    						descricao = "AAl validar el archivo del Módulo Contable del mes de "+mesAno+" ocurrió el siguiente error:\n"+erro;
		    						}
		    						else
		    						{
		    							processamentoInstrumento.addAgenda(nomeArquivo, erro, agendaMovimentacao,1);
			    						descricao = "Al validar los archivos del Módulo Central de Información del mes de "+mesAno+" ocurrió el siguiente error:\n"+erro;
		    						}
		    						
		    						this.enviarEmail(titulo, descricao, origem);
		    					}
			    			}
		    			}
		    		}
				}
				else if(livroBollean)
				{
					codLivro = nomeArquivo.substring(4,13);
	            	
	            	ano = Integer.parseInt(nomeArquivo.substring(13,17));
	            	
	            	mes = Integer.parseInt(nomeArquivo.substring(17,19));
	            	
	            	sigla = nomeArquivo.substring(1,4);
	            	
	            	aseguradora = (Aseguradora) home.obterEntidadePorSigla(sigla);
	            	
	            	if(aseguradora!=null)
	            	{
	            		tipoLivro = this.obterNomeArquivo(codLivro);
	            		if(tipoLivro!=null)
	            		{
		            		livro = livroHome.obterLivro(aseguradora, tipoLivro, mes, ano);
		    				if(livro == null)
		    				{
			            		livro = (Livro) mm.getEntity("Livro");
			            		livro.atribuirTitulo(tipoLivro);
			            		livro.atribuirTipo(tipoLivro);
			    				livro.atribuirMes(mes);
			    				livro.atribuirAno(ano);
			    				livro.atribuirOrigem(aseguradora);
			    				livro.incluir();
			    				
			    				livro.atualizarFase2(Evento.EVENTO_CONCLUIDO);
		    				}
		    				
		    				filesGravadas = upHome.getAllUploadedFiles(livro);
		    				filesExcluir = new ArrayList<>();
		    				
		    				for(UploadedFile up : filesGravadas)
		    				{
		    					if(up.getName().equals(nomeArquivo))
		    						filesExcluir.add(up);
		    				}
		    				
		    				for(UploadedFile up : filesExcluir)
		    					upHome.removeUploadedFile(up.getId());
		    				
							input = arquivo.getInputStream();
							upHome.addUploadedFile(livro, input, nomeArquivo, mimeType, input.available(), 0);
							input.close();
							
							mesStr = "";
							if(new Integer(mes).toString().length() == 1)
								mesStr = "0" + mes;
							
							descricao = "Notificación de Recibimiento - " + new SimpleDateFormat("HH:mm:ss").format(new Date());
							
							processamentoLivro.addAgenda(nomeArquivo, descricao, livro, 0);
							
							this.enviarEmail("Libro " + nomeArquivo, descricao, livro.obterOrigem());
	            		}
	            		else
	            		{
	            			livro = (Livro) mm.getEntity("Livro");
		    				livro.atribuirMes(mes);
		    				livro.atribuirAno(ano);
		    				livro.atribuirOrigem(aseguradora);
		    				
	            			descricao = "Archivo del Módulo Libro Electrónico "+nomeArquivo+" código del Libro no fue encontrado - " + new SimpleDateFormat("HH:mm:ss").format(new Date());
							
							processamentoLivro.addAgenda(nomeArquivo, descricao, livro, 1);
							
							this.enviarEmail("Libro " + nomeArquivo, descricao, aseguradora);
	            		}
	            	}
				}
			}
			catch(Exception e)
			{
				if(input!=null)
					input.close();
				this.enviarEmail("Error Interno Validación", "Archivo copiado en " + diretorioFinal + "\nError: "+e.getMessage(), null);
				System.out.println("Archivo copiado en " + diretorioFinal + "\n\n"+e.getMessage());
			}
			
			//writer.write("***** Fim - "+formataData.format(new Date())+" *****\r\n\r\n");
			
			processamentoContabil.excluirProcessando(arquivo.getName());
		}
		
		System.gc();
		
		Thread.sleep(5000);//This part gives the Bufferedreaders and the InputStreams time to close Completely
		
		//writer.write("***** EXCLUSÃO DENTO DO UTEIS *****\r\n");
		for(SmbFile arquivo : arquivosExcluir)
		{
			if(arquivo.exists())
			{
				try
				{
					arquivo.delete();
					//System.out.println("Exclusao TRUE 1 = " + arquivo.getName());
					//writer.write(arquivo.getName() + " Excluido\r\n");
					
				}
				catch(Exception e)
				{
					//writer.write(arquivo.getName() + " Não conseguiu Excluir\r\n");
					//System.out.println("Exclusao FALSE 1 = " + arquivo.getName());
					e.printStackTrace();
				}
			}
		}
		//writer.write("***** FIM EXCLUSÃO DENTO DO UTEIS *****\r\n\r\n");
		
		//writer.close();
		
		return aseguradoras.values();
	}
	
	/*private static void copyFileUsingFileChannel(String pathIn, String pathOut) throws IOException 
	{
		final int DEFAULT_BUFFER_SIZE = 1024 * 8;
        FileChannel source = null;
        FileChannel destination = null;
         
        try {
            source = new FileInputStream(new File(inputStream).getChannel();
            destination = new FileOutputStream(new File(pathOut)).getChannel();
             
            //This fails with Map Failed exception on large files
            //destination.transferFrom(source, 0, source.size());
             
            ByteBuffer buf = ByteBuffer.allocateDirect(DEFAULT_BUFFER_SIZE);
            while((source.read(buf)) != -1)
            {
            	buf.flip();
            	destination.write(buf);
            	buf.clear();
            }
        } 
        finally 
        {
            if (source != null) 
            {
                source.close();
            }
            if (destination != null) 
            {
                destination.close();
            }
        }
    }*/
	
	private InfraProperties infra;
	
	private void enviarEmail(String titulo, String descricao, Entidade entidade)
	{
		try
		{
			infra = InfraProperties.getInstance();
			String dir = infra.getProperty("arquivos.url");
			boolean enviaEmail = dir.indexOf("Laptopsala") == -1;
			
			Collection<String> emailsExternos = new ArrayList<>();
			
			//writer.write("Enviou Email = "+titulo + " - " + descricao + " - Hora Atual: "+formataData.format(new Date())+"\r\n");
			
			Email email = new SimpleEmail();
			email.setHostName("mail.bcp.gov.py");
			email.setSmtpPort(25);
			email.setFrom("sisvalidacion@bcp.gov.py");
			email.setSubject(titulo);
			email.setMsg(descricao);
			email.addTo("gbrawer@bcp.gov.py");
			if(!titulo.startsWith("Libro"))
			{
				//email.addCc("nfernan@bcp.gov.py");
				email.addCc("prodriguez@bcp.gov.py");
				email.addCc("cferrac@bcp.gov.py");
			}
			
			if(entidade!=null)
			{
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
	
	private void copyFileUsingBufferedReader(InputStream inputStream, String pathDest) throws IOException 
	{
		final int DEFAULT_BUFFER_SIZE = 1024 * 8;
        BufferedInputStream source = new BufferedInputStream(inputStream);
        BufferedOutputStream destination = new BufferedOutputStream(new FileOutputStream(new File(pathDest)));
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
 
        try
        {
            int n = 0;
            while (-1 != (n = source.read(buffer))) 
                destination.write(buffer, 0, n);
            
            destination.flush();
        }
        catch(Exception e)
        {
        	System.out.println("Erro na copia do arquivo " + pathDest);
        	e.printStackTrace();
        }
        finally 
        {
            if (source != null) 
                source.close(); 
            if (destination != null) 
                destination.close();                
        }
    }
 
	/*private void enviarEmail(String titulo, String descricao)
	{
		try
		{
			String to = "cavaquiolo1105@gmail.com";
			String cc = "gbrawer@bcp.gov.py";
			String from = "giovanni@gdsd.com.br";
			
			MailManager mail = new MailManager();
            MailServer server = new MailServer();
            //mail.send(server, to, "cc", "", from, titulo, descricao);
            mail.send(server, to, "", "", from, titulo, descricao);
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}*/
	
	public Processamento criarProcessamento(String tipo, String tipo2, Usuario usuario) throws Exception
    {
		SampleModelManager mm = new SampleModelManager();
    	 
    	Processamento processamento = (Processamento) mm.getEntity("Processamento");
    	processamento.atribuirResponsavel(usuario);
    	processamento.atribuirTitulo("Validación " + tipo2 + " - " + new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
    	processamento.atribuirTipo(tipo);
    	processamento.atribuirData(new Date());
    	processamento.atribuirDescricao("");
    	processamento.atribuirDataPrevistaInicio(new Date());
    	processamento.atribuirDataPrevistaConclusao(new Date());
    	processamento.incluir();
    	
    	processamento.atualizarFase2(Processamento.EVENTO_CONCLUIDO);
    	
    	return processamento;
    }
	
	private void validarArquivosContabeis(AgendaMovimentacao agendaMovimentacao, EntidadeHome entidadeHome, String nomeArquivo, SampleModelManager mm, Processamento processamentoContabil) throws Exception
    {
    	Collection<String> validacaoErros;
    	
    	try
    	{
			String sigla = agendaMovimentacao.obterOrigem().obterSigla();
			Entidade origem = agendaMovimentacao.obterOrigem();
			
    		String ano2 = new Integer(agendaMovimentacao.obterAnoMovimento()).toString();
    		String mes2 = new Integer(agendaMovimentacao.obterMesMovimento()).toString();

			if (mes2.length() == 1)
				mes2 = "0" + mes2;
	
			validacaoErros = agendaMovimentacao.validaArquivo(sigla.trim()+ ano2 + mes2);
			
			if (validacaoErros.size() > 0) 
			{
				Entidade destino = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("planodecontas");
				String msgErros = "";

				System.out.println("Erros: " + validacaoErros.size());

				for (String msgAux : validacaoErros)
					msgErros += msgAux + "\n";

				System.out.println("Erros2: ");

				Parametro parametro = (Parametro) entidadeHome.obterEntidadePorApelido("parametros");
				String descricao = parametro.obterAtributo("notificacaocritica").obterValor();

				Notificacao notificacao = (Notificacao) mm.getEntity("Notificacao");
				notificacao.atribuirSuperior(agendaMovimentacao);
				notificacao.atribuirOrigem(agendaMovimentacao.obterOrigem());
				notificacao.atribuirDestino(destino);
				notificacao.atribuirResponsavel(agendaMovimentacao.obterResponsavel());
				notificacao.atribuirTipo("Notificación de Error de Validación");
				notificacao.atribuirTitulo("Notificación de Error de Validación");
				notificacao.atribuirDescricao(descricao + "\n" + msgErros);
				notificacao.incluir();
				
				for (Evento e : agendaMovimentacao.obterInferiores())
				{
					if (e instanceof Notificacao && e.obterId() != notificacao.obterId())
					{
						if (e.obterTipo().equals("Notificación de Error de Validación"))
							e.atualizarFase2(Evento.EVENTO_CONCLUIDO);
					}
				}

				agendaMovimentacao.atualizarFase2(Evento.EVENTO_PENDENTE);
				
				String texto="Notificación de Error de Validación - " + new SimpleDateFormat("HH:mm:ss").format(new Date());
				processamentoContabil.addAgenda(nomeArquivo, texto, agendaMovimentacao, 1);
				
				String mesAno = agendaMovimentacao.obterMesMovimento() + "/"+agendaMovimentacao.obterAnoMovimento();
				String titulo = origem.obterNome() + " - " + mesAno;
				
				String descricao2 = "Este correo fue enviado de forma automática, favor no responder al mismo\n\nAl validar el archivo del Módulo Contable del mes de "+mesAno+" ocurrió el siguiente error:\n"+msgErros;
				
				this.enviarEmail(titulo, descricao2, agendaMovimentacao.obterOrigem());
			} 
			else
			{
				Parametro parametro = (Parametro) entidadeHome.obterEntidadePorApelido("parametros");
				String descricao = parametro.obterAtributo("notificacaorecebimento").obterValor();

				Entidade destino = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido("planodecontas");
				Notificacao notificacao = (Notificacao) mm.getEntity("Notificacao");
				notificacao.atribuirSuperior(agendaMovimentacao);
				notificacao.atribuirOrigem(agendaMovimentacao.obterOrigem());
				notificacao.atribuirDestino(destino);
				notificacao.atribuirResponsavel(agendaMovimentacao.obterResponsavel());
				notificacao.atribuirTipo("Notificación de Recibimiento");
				notificacao.atribuirTitulo("Notificación de Recibimiento");
				notificacao.atribuirDescricao(descricao);
				notificacao.incluir();
				
				for (Evento e : agendaMovimentacao.obterInferiores())
					e.atualizarFase2(Evento.EVENTO_CONCLUIDO);

				agendaMovimentacao.atualizarFase2(Evento.EVENTO_CONCLUIDO);
				
				agendaMovimentacao.atualizaUltimaAgenda("Contabil");

				/*AgendaMovimentacao novaAgenda = (AgendaMovimentacao) mm.getEntity("AgendaMovimentacao");
				novaAgenda.atribuirOrigem(agendaMovimentacao.obterOrigem());
				novaAgenda.atribuirDestino(agendaMovimentacao.obterDestino());
				novaAgenda.atribuirResponsavel(agendaMovimentacao
						.obterResponsavel());
				novaAgenda.atribuirTitulo(" (Consistencia del archivo)");

				Calendar mesSeguinte = Calendar.getInstance();
				mesSeguinte.setTime(new Date());
				mesSeguinte.add(Calendar.MONTH, 1);

				String dia = parametro.obterAtributo("diaagenda").obterValor();

				String mes = new SimpleDateFormat("MM").format(mesSeguinte.getTime());
				String ano = new SimpleDateFormat("yyyy").format(mesSeguinte.getTime());

				String data = dia + "/" + mes + "/" + ano;

				Date dataModificada = new SimpleDateFormat("dd/MM/yyyy").parse(data);

				int mesMovimento = agendaMovimentacao.obterMesMovimento() + 1;
				int anoMovimento = agendaMovimentacao.obterAnoMovimento();

				if (mesMovimento > 12)
				{
					mesMovimento = 1;
					anoMovimento += 1;
				}

				novaAgenda.atribuirDataPrevistaInicio(dataModificada);
				novaAgenda.atribuirTipo(agendaMovimentacao.obterTipo());
				novaAgenda.atribuirMesMovimento(mesMovimento);
				novaAgenda.atribuirAnoMovimento(anoMovimento);
				novaAgenda.incluir();

				String mesModificado = "";

				if (new Integer(mesMovimento).toString().length() == 1)
					mesModificado = "0" + mesMovimento;
				else
					mesModificado = new Integer(mesMovimento).toString();

				novaAgenda.atualizarTitulo2(mesModificado + " - " + anoMovimento	+ " (Consistencia del archivo)");*/
				
				String texto="Notificación de Recibimiento - " + new SimpleDateFormat("HH:mm:ss").format(new Date());
				processamentoContabil.addAgenda(nomeArquivo, texto, agendaMovimentacao, 0);
				
				String mesAno = agendaMovimentacao.obterMesMovimento() + "/"+agendaMovimentacao.obterAnoMovimento();
				String titulo = origem.obterNome() + " - " + mesAno;
				
				String descricao2 = "Este correo fue enviado de forma automática, favor no responder al mismo\n\nEl archivo del Módulo Contable del mes de "+mesAno+" fue incluido satisfactoriamente.";
				
				this.enviarEmail(titulo, descricao2, agendaMovimentacao.obterOrigem());
				
				//this.enviarEmail("Validación " +nomeArquivo, descricao, agendaMovimentacao.obterOrigem());
			}
    	}
    	catch (Exception exception)
		{
			System.out.println(exception.getMessage());
		}
    }
	
	public boolean validaData(String dataStr)
    {
        final String DATE_PATTERN = "(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)";
        Pattern pattern = Pattern.compile(DATE_PATTERN);;
        Matcher matcher  = pattern.matcher(dataStr);

        if(matcher.matches())
        {
            matcher.reset();

            if(matcher.find())
            {
                String day = matcher.group(1);
                String month = matcher.group(2);
                int year = Integer.parseInt(matcher.group(3));

                if (day.equals("31") && (month.equals("4") || month .equals("6") || month.equals("9") || month.equals("11") || month.equals("04") || month .equals("06") || month.equals("09")))
                {
                    return false; // only 1,3,5,7,8,10,12 has 31 days
                }
                else if (month.equals("2") || month.equals("02"))
                {
                    //leap year
                    if(year % 4==0)
                    {
                        if(day.equals("30") || day.equals("31"))
                        {
                            return false;
                        }
                        else
                        {
                            return true;
                        }
                    }
                    else
                    {
                        if(day.equals("29")||day.equals("30")||day.equals("31"))
                        {
                            return false;
                        }
                        else
                        {
                            return true;
                        }
                    }
                }
                else
                {
                    return true;
                }
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }
}
