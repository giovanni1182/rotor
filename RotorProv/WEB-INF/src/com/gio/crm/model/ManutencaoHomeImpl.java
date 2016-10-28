package com.gio.crm.model;

import java.util.Collection;
import java.util.Iterator;

import infra.model.Home;
import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

public class ManutencaoHomeImpl extends Home implements ManutencaoHome
{

	public ManutencaoHomeImpl()
	{
	}
	
	public void manutDadosPrevisao() throws Exception
	{
		try
		{
			EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
			EntidadeHome entidadehome = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
			ApoliceHome apolicehome = (ApoliceHome) this.getModelManager().getHome("ApoliceHome");
			
			SQLQuery query = this.getModelManager().createSQLQuery("crm", "SELECT evento.id,titulo,origem FROM dados_previsao,evento where evento.id=dados_previsao.id and superior=0");
			
			SQLRow[] rows = query.execute();
			
			int achou = 0;
			int naoAchou = 0;
			
			for(int i = 0 ; i < rows.length ; i++)
			{
				long id = rows[i].getLong("id");
				
				DadosPrevisao dados = (DadosPrevisao) home.obterEventoPorId(id);
				
				String titulo = rows[i].getString("titulo");
				
				String[] numero = titulo.split(":");
				
				String numero2 = numero[1].trim();
				
				//System.out.println(numero2);
				
				long origem = rows[i].getLong("origem");
				
				Entidade e = entidadehome.obterEntidadePorId(origem);
				
				Apolice ap = apolicehome.obterApolice(e, numero2);
				
				if(ap!=null)
				{
					dados.atualizarSuperior2(ap);
					achou++;
				}
				else
				{
					dados.excluir2();
					naoAchou++;
				}
			}
			
			rows = null;
			
			query = null;
			
			System.out.println("manutDadosPrevisao, Encontrou: " + achou + " - Nao Encontrou: " + naoAchou);
		}
		catch (Exception e)
		{
			System.out.println("Erro Interno Manut Dados Previsao: " + e.toString());
		}
			
	}
	
	public void manutFaturaSinistro() throws Exception
	{
		String str = "";
		
		try
		{
			System.out.println("Inicio Manut Fatura Sinistro");
			
			EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
			
			SQLQuery query = this.getModelManager().createSQLQuery("crm", "SELECT evento.id, superior FROM evento,fatura_sinistro where evento.id = fatura_sinistro.id and superior > 0 and evento.id > 0 and secao_apolice is null");
			
			SQLRow[] rows = query.execute();
			
			for(int i = 0 ; i < rows.length ; i++)
			{
				long id = rows[i].getLong("id");
				long superiorId = rows[i].getLong("superior");
				
				FaturaSinistro fatura = (FaturaSinistro) home.obterEventoPorId(id);
				str="Achou fatura";
				Sinistro sinistro = (Sinistro) home.obterEventoPorId(superiorId);
				str+="\nAchou sinistro";
				if(sinistro!=null)
				{
					Apolice apolice = (Apolice) sinistro.obterSuperior();
					str+="\nAchou apolice";
					if(apolice!=null)
					{
						Plano plano = apolice.obterPlano();
						str+="\nAchou Plano";
						if(plano!=null)
						{
							System.out.println("Atualizou " + i);
							//fatura.atualizarSecao(plano.obterSecao());
						}
					}
				}
			}
			
			System.out.println("Fim Manut Fatura Sinistro");
		}
		catch (Exception e)
		{
			System.out.println("Erro Interno Manut Fatura Sinistro: " + e.toString());
			System.out.println(str);
		}
			
	}
	
	public void manutUltimaAgenda() throws Exception
	{
		System.out.println("Inicio Manut Ultima Agenda");
		
		try
		{
			AseguradoraHome aseguradoraHome = (AseguradoraHome) this.getModelManager().getHome("AseguradoraHome");
			Collection aseguradoras = aseguradoraHome.obterAseguradoras();
			
			for(Iterator i = aseguradoras.iterator() ; i.hasNext() ; )
			{
				Aseguradora aseguradora = (Aseguradora) i.next();
				
				AgendaMovimentacao agMCO = aseguradora.obterUltimaAgendaMCO();
				if(agMCO!=null)
					agMCO.atualizaUltimaAgenda("Contabil");
				
				AgendaMovimentacao agMCI = aseguradora.obterUltimaAgendaMCI();
				if(agMCI!=null)
					agMCI.atualizaUltimaAgenda("Instrumento");
			}
			
			System.out.println("Fim Manut Ultima Agenda");
		}
		catch (Exception e)
		{
			System.out.println("Erro Interno Manut Ultima Agenda: " + e.toString());
		}
			
	}
	
	/*public void manutDadosReaseguro() throws Exception
	{
		try
		{
			EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
			EntidadeHome entidadehome = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
			ApoliceHome apolicehome = (ApoliceHome) this.getModelManager().getHome("ApoliceHome");
			
			SQLQuery query = this.getModelManager().createSQLQuery("crm", "SELECT TOP 1000 evento.id,titulo,origem FROM dados_reaseguro,evento where evento.id=dados_reaseguro.id and superior=0");
			
			SQLRow[] rows = query.execute();
			
			int achou = 0;
			int naoAchou = 0;
			
			for(int i = 0 ; i < rows.length ; i++)
			{
				long id = rows[i].getLong("id");
				
				DadosReaseguro dados = (DadosReaseguro) home.obterEventoPorId(id);
				
				String titulo = rows[i].getString("titulo");
				
				String[] numero = titulo.split(":");
				
				String numero2 = numero[1].trim();
				
				//System.out.println(numero2);
				
				long origem = rows[i].getLong("origem");
				
				Entidade e = entidadehome.obterEntidadePorId(origem);
				
				Apolice ap = apolicehome.obterApolice(e, numero2);
				
				if(ap!=null)
				{
					dados.atualizarSuperior2(ap);
					achou++;
				}
				else
					naoAchou++;
			}
			
			rows = null;
			
			query = null;
			
			System.out.println("manutDadosReaseguro, Encontrou: " + achou + " - Nao Encontrou: " + naoAchou);
		}
		catch (Exception e)
		{
			System.out.println("Erro Interno Manut Dados Reaseguro: " + e.toString());
		}
		
	}
	
	public void manutDadosCoaseguro() throws Exception
	{
		try
		{
			EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
			EntidadeHome entidadehome = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
			ApoliceHome apolicehome = (ApoliceHome) this.getModelManager().getHome("ApoliceHome");
			
			SQLQuery query = this.getModelManager().createSQLQuery("crm", "SELECT TOP 1000 evento.id,titulo,origem FROM dados_coaseguro,evento where evento.id=dados_coaseguro.id and superior=0");
			
			SQLRow[] rows = query.execute();
			
			int achou = 0;
			int naoAchou = 0;
			
			for(int i = 0 ; i < rows.length ; i++)
			{
				long id = rows[i].getLong("id");
				
				DadosCoaseguro dados = (DadosCoaseguro) home.obterEventoPorId(id);
				
				String titulo = rows[i].getString("titulo");
				
				String[] numero = titulo.split(":");
				
				String numero2 = numero[1].trim();
				
				//System.out.println(numero2);
				
				long origem = rows[i].getLong("origem");
				
				Entidade e = entidadehome.obterEntidadePorId(origem);
				
				Apolice ap = apolicehome.obterApolice(e, numero2);
				
				if(ap!=null)
				{
					dados.atualizarSuperior2(ap);
					achou++;
				}
				else
					naoAchou++;
			}
			
			rows = null;
			
			query = null;
			
			System.out.println("manutDadosCoaseguro, Encontrou: " + achou + " - Nao Encontrou: " + naoAchou);
		}
		catch (Exception e)
		{
			System.out.println("Erro Interno Manut Dados Coaseguro: " + e.toString());
		}
		
	}
	
	public void manutSinistro() throws Exception
	{
		try
		{
			EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
			EntidadeHome entidadehome = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
			ApoliceHome apolicehome = (ApoliceHome) this.getModelManager().getHome("ApoliceHome");
			
			SQLQuery query = this.getModelManager().createSQLQuery("crm", "SELECT TOP 1000 evento.id,titulo,origem FROM sinistro,evento where evento.id=sinistro.id and superior=0");
			
			SQLRow[] rows = query.execute();
			
			int achou = 0;
			int naoAchou = 0;
			
			for(int i = 0 ; i < rows.length ; i++)
			{
				long id = rows[i].getLong("id");
				
				Sinistro dados = (Sinistro) home.obterEventoPorId(id);
				
				String titulo = rows[i].getString("titulo");
				
				String[] numero = titulo.split(":");
				
				String numero2 = numero[1].trim();
				
				//System.out.println(numero2);
				
				long origem = rows[i].getLong("origem");
				
				Entidade e = entidadehome.obterEntidadePorId(origem);
				
				Apolice ap = apolicehome.obterApolice(e, numero2);
				
				if(ap!=null)
				{
					dados.atualizarSuperior2(ap);
					achou++;
				}
				else
					naoAchou++;
			}
			
			rows = null;
			
			query = null;
			
			System.out.println("manutSinistro, Encontrou: " + achou + " - Nao Encontrou: " + naoAchou);
		}
		catch (Exception e)
		{
			System.out.println("Erro Interno Manut Sinistro: " + e.toString());
		}
		
	}
	
	public void manutAnulacaoInstrumento() throws Exception
	{
		try
		{
			EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
			EntidadeHome entidadehome = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
			ApoliceHome apolicehome = (ApoliceHome) this.getModelManager().getHome("ApoliceHome");
			
			SQLQuery query = this.getModelManager().createSQLQuery("crm", "SELECT TOP 1000 evento.id,titulo,origem FROM anulacao_instrumento,evento where evento.id=anulacao_instrumento.id and superior=0");
			
			SQLRow[] rows = query.execute();
			
			int achou = 0;
			int naoAchou = 0;
			
			for(int i = 0 ; i < rows.length ; i++)
			{
				long id = rows[i].getLong("id");
				
				AnulacaoInstrumento dados = (AnulacaoInstrumento) home.obterEventoPorId(id);
				
				String titulo = rows[i].getString("titulo");
				
				String[] numero = titulo.split(":");
				
				String numero2 = numero[1].trim();
				
				//System.out.println(numero2);
				
				long origem = rows[i].getLong("origem");
				
				Entidade e = entidadehome.obterEntidadePorId(origem);
				
				Apolice ap = apolicehome.obterApolice(e, numero2);
				
				if(ap!=null)
				{
					dados.atualizarSuperior2(ap);
					achou++;
				}
				else
					naoAchou++;
			}
			
			rows = null;
			
			query = null;
			
			System.out.println("manutAnulacaoInstrumento, Encontrou: " + achou + " - Nao Encontrou: " + naoAchou);
		}
		catch (Exception e)
		{
			System.out.println("Erro Interno Manut Anulacao Instrumento: " + e.toString());
		}
		
	}
	
	public void manutRegistroCobranca() throws Exception
	{
		try
		{
			EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
			EntidadeHome entidadehome = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
			ApoliceHome apolicehome = (ApoliceHome) this.getModelManager().getHome("ApoliceHome");
			
			SQLQuery query = this.getModelManager().createSQLQuery("crm", "SELECT TOP 1000 evento.id,titulo,origem FROM registro_cobranca,evento where evento.id=registro_cobranca.id and superior=0");
			
			SQLRow[] rows = query.execute();
			
			int achou = 0;
			int naoAchou = 0;
			
			for(int i = 0 ; i < rows.length ; i++)
			{
				long id = rows[i].getLong("id");
				
				RegistroCobranca dados = (RegistroCobranca) home.obterEventoPorId(id);
				
				String titulo = rows[i].getString("titulo");
				
				String[] numero = titulo.split(":");
				
				String numero2 = numero[1].trim();
				
				//System.out.println(numero2);
				
				long origem = rows[i].getLong("origem");
				
				Entidade e = entidadehome.obterEntidadePorId(origem);
				
				Apolice ap = apolicehome.obterApolice(e, numero2);
				
				if(ap!=null)
				{
					dados.atualizarSuperior2(ap);
					achou++;
				}
				else
					naoAchou++;
			}
			
			rows = null;
			
			query = null;
			
			System.out.println("manutRegistroCobranca, Encontrou: " + achou + " - Nao Encontrou: " + naoAchou);
		}
		catch (Exception e)
		{
			System.out.println("Erro Interno Manut Registro Cobranca: " + e.toString());
		}
	}
	
	public void manutSuplemento() throws Exception
	{
		try
		{
			EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
			EntidadeHome entidadehome = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
			ApoliceHome apolicehome = (ApoliceHome) this.getModelManager().getHome("ApoliceHome");
			
			SQLQuery query = this.getModelManager().createSQLQuery("crm", "SELECT TOP 1000 evento.id,titulo,origem FROM suplemento,evento where evento.id=suplemento.id and superior=0");
			
			SQLRow[] rows = query.execute();
			
			int achou = 0;
			int naoAchou = 0;
			
			for(int i = 0 ; i < rows.length ; i++)
			{
				long id = rows[i].getLong("id");
				
				Suplemento dados = (Suplemento) home.obterEventoPorId(id);
				
				String titulo = rows[i].getString("titulo");
				
				String[] numero = titulo.split(":");
				
				String numero2 = numero[1].trim();
				
				//System.out.println(numero2);
				
				long origem = rows[i].getLong("origem");
				
				Entidade e = entidadehome.obterEntidadePorId(origem);
				
				Apolice ap = apolicehome.obterApolice(e, numero2);
				
				if(ap!=null)
				{
					dados.atualizarSuperior2(ap);
					achou++;
				}
				else
					naoAchou++;
			}
			
			rows = null;
			
			query = null;
			
			System.out.println("manutSuplemento, Encontrou: " + achou + " - Nao Encontrou: " + naoAchou);
		}
		catch (Exception e)
		{
			System.out.println("Erro Interno Manut Suplemento: " + e.toString());
		}
	}
	
	public void manutMorosidade() throws Exception
	{
		try
		{
			EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
			EntidadeHome entidadehome = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
			ApoliceHome apolicehome = (ApoliceHome) this.getModelManager().getHome("ApoliceHome");
			
			SQLQuery query = this.getModelManager().createSQLQuery("crm", "SELECT TOP 1000 evento.id,titulo,origem FROM morosidade,evento where evento.id=morosidade.id and superior=0");
			
			SQLRow[] rows = query.execute();
			
			int achou = 0;
			int naoAchou = 0;
			
			for(int i = 0 ; i < rows.length ; i++)
			{
				long id = rows[i].getLong("id");
				
				Morosidade dados = (Morosidade) home.obterEventoPorId(id);
				
				String titulo = rows[i].getString("titulo");
				
				String[] numero = titulo.split(":");
				
				String numero2 = numero[1].trim();
				
				//System.out.println(numero2);
				
				long origem = rows[i].getLong("origem");
				
				Entidade e = entidadehome.obterEntidadePorId(origem);
				
				Apolice ap = apolicehome.obterApolice(e, numero2);
				
				if(ap!=null)
				{
					dados.atualizarSuperior2(ap);
					achou++;
				}
				else
					naoAchou++;
			}
			
			rows = null;
			
			query = null;
			
			System.out.println("manutMorosidade, Encontrou: " + achou + " - Nao Encontrou: " + naoAchou);
		}
		catch (Exception e)
		{
			System.out.println("Erro Interno Manut Morosidade: " + e.toString());
		}
	}
	
	public void limparCache() throws Exception
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm","DBCC DROPCLEANBUFFERS");
		update.execute();
		
		SQLUpdate update2 = this.getModelManager().createSQLUpdate("crm","DBCC FREEPROCCACHE");
		update2.execute();
		
		SQLUpdate update3 = this.getModelManager().createSQLUpdate("crm","DBCC FREESYSTEMCACHE ( 'ALL' )");
		update3.execute();
		
	}*/
	
	private Collection<Aseguradora> aseguradoras;
	private SampleModelManager mm;
	private EventoHome home;
	
	 public void verificarDuplicidades(Collection<Aseguradora> aseguradoras) throws Exception
	 {
		 mm = new SampleModelManager();
		 home = (EventoHome) mm.getHome("EventoHome");
		 
		 this.aseguradoras = aseguradoras;
		 
		 this.duplicidadeApolices();
		 this.duplicidadeDadosPrevisao();
		 this.duplicidadeDadosReaseguro();
		 this.duplicidadeDadosCoaseguro();
		 this.duplicidadeSinistro();
		 this.duplicidadeFaturaSinistro();
		 this.duplicidadeAnulacao();
		 this.duplicidadeCobranca();
		 this.duplicidadeAspectos();
		 this.duplicidadeSuplementos();
		 this.duplicidadeRefinacao();
		 this.duplicidadeRegistroGastos();
		 this.duplicidadeRegistroAnulacao();
		 this.duplicidadeMorosidade();
	 }
	 
	 public void datasReaseguro() throws Exception
	 {
		 mm = new SampleModelManager();
		 home = (EventoHome) mm.getHome("EventoHome");
		 
		 SQLQuery query = mm.createSQLQuery("crm","select COUNT(*) as qtde from evento,dados_reaseguro,apolice where evento.id = dados_reaseguro.id and superior = apolice.id");
		 int total = query.executeAndGetFirstRow().getInt("qtde");
		 
		 int inicio = 509950;
		 int fim = inicio + 10000;
		 int totalAchado = 0;
		 
		 while(totalAchado<=total)
		 {
			 query = mm.createSQLQuery("crm","select evento.id, superior from evento, dados_reaseguro where evento.id = dados_reaseguro.id and superior > 0 and evento.id BETWEEN "+inicio+" AND "+fim);
			 
			 SQLRow[] rows = query.execute();
			 
			 for(int i = 0 ; i < rows.length ; i++)
			 {
				 long id = rows[i].getLong("id");
				 long superior = rows[i].getLong("superior");
				 
				 DadosReaseguro dados = (DadosReaseguro) home.obterEventoPorId(id);
				 
				 SQLQuery query2 = mm.createSQLQuery("crm","select count(*) as qtde from evento where id = " + superior);
				 if(query2.executeAndGetFirstRow().getInt("qtde") > 0)
				 {
					 Apolice apolice = (Apolice) home.obterEventoPorId(superior);
					 
					 //dados.atualizarDataIniApolice(apolice.obterDataPrevistaInicio());
					 //dados.atualizarDataFimApolice(apolice.obterDataPrevistaConclusao());
					 
					 totalAchado++;
					 
					 System.out.println("Total " + totalAchado);
				 }
			 }
			 
			 inicio+=10001;
			 fim+=10001;
		 }
		 
		 System.out.println("Terminou");
	 }
	 
	 public void datasReaseguro2() throws Exception
	 {
		 System.out.println("Executando Reaseguro 2");
		 
		 mm = new SampleModelManager();
		 home = (EventoHome) mm.getHome("EventoHome");
		 
		 SQLQuery query = mm.createSQLQuery("crm","select count(*) as qtde from evento,dados_reaseguro where evento.id = dados_reaseguro.id and data_ini_apo is null");
		 int qtde  =  query.executeAndGetFirstRow().getInt("qtde");
		 System.out.println("Qtde " + qtde);
		 
		 query = mm.createSQLQuery("crm","select evento.id, superior from evento,dados_reaseguro where evento.id = dados_reaseguro.id and data_ini_apo is null");
			 
		 SQLRow[] rows = query.execute();
		 
		 for(int i = 0 ; i < rows.length ; i++)
		 {
			 long id = rows[i].getLong("id");
			 long superior = rows[i].getLong("superior");
			 
			 DadosReaseguro dados = (DadosReaseguro) home.obterEventoPorId(id);
			 
			 SQLQuery query2 = mm.createSQLQuery("crm","select count(*) as qtde from evento where id = " + superior);
			 if(query2.executeAndGetFirstRow().getInt("qtde") > 0)
			 {
				 Apolice apolice = (Apolice) home.obterEventoPorId(superior);
				 
				 //dados.atualizarDataIniApolice(apolice.obterDataPrevistaInicio());
				 //dados.atualizarDataFimApolice(apolice.obterDataPrevistaConclusao());
			 }
		 }
		 
		 System.out.println("Terminou");
	 }
	    
    private void duplicidadeApolices() throws Exception
	{
		try
		{
			System.out.println("Verificando duplicidade apolice");
			
			int cont = 1;
			SQLQuery query;
			SQLRow[] rows,rows2;
			int qtde;
			long origem,secao,id;
			String numero,status_apolice;
			double numero_endoso,certificado;
			Apolice apoliceSuperior,apolice;
			SQLUpdate update;
			
			for(Aseguradora aseg2 : aseguradoras)
			{
				/*SQLQuery query = getModelManager().createSQLQuery("crm", "select count(*) as qtde, origem, numero_apolice, status_apolice, secao, numero_endoso, certificado from evento,apolice where evento.id = apolice.id and origem = ? group by origem, numero_apolice, status_apolice, secao, numero_endoso, certificado having count(*) > 1");
				query.addLong(aseg2.obterId());*/
				
				query = getModelManager().createSQLQuery("crm", "EXEC duplicidadeApolice ?");
				query.addLong(aseg2.obterId());
				
				rows = query.execute();
				
				for(int i = 0 ; i < rows.length ; i++)
				{
					qtde = rows[i].getInt("qtde");
					origem = rows[i].getLong("origem");
					numero = rows[i].getString("numero_apolice");
					status_apolice = rows[i].getString("status_apolice");
					secao = rows[i].getLong("secao");
					numero_endoso = rows[i].getDouble("numero_endoso");
					certificado = rows[i].getDouble("certificado");
					
					/*System.out.println("Duplicada");
					System.out.println(qtde + " - " + aseg2.obterNome() + " - " + numero + " - " + status_apolice + " - " + secao + " - " + numero_endoso + " - " + certificado);*/
					
					//query = getModelManager().createSQLQuery("crm", "select evento.id from evento,apolice where evento.id = apolice.id and origem = ? and status_apolice = ? and secao = ? and numero_endoso = ? and certificado = ? and numero_apolice = ? order by criacao desc");
					query = getModelManager().createSQLQuery("crm", "EXEC duplicidadeApolice2 ?,?,?,?,?,?");
			        query.addLong(origem);
			        query.addString(status_apolice);
			        query.addLong(secao);
			        query.addDouble(numero_endoso);
			        query.addDouble(certificado);
			        query.addString(numero);
					
			        rows2 = query.execute();
			        
			        apoliceSuperior = null;
			        
			        for(int k = 0; k < rows2.length; k++)
			        {
			            id = rows2[k].getLong("id");
			            apolice = (Apolice) home.obterEventoPorId(id);
			            //System.out.println(id + " - " + new SimpleDateFormat("dd/MM/yyyy").format(apolice.obterCriacao()));
			            if(apolice!=null)
			            {
				            if(k == 0)
				            	apoliceSuperior = apolice;
				            else
				            {
				            	if(apoliceSuperior!=null)
				            	{
					            	//SQLUpdate update = getModelManager().createSQLUpdate("crm","update evento set superior = ? where superior = ?");
				            		update = getModelManager().createSQLUpdate("crm","EXEC atualizarSuperior ?,?");
						            update.addLong(apoliceSuperior.obterId());
						            update.addLong(apolice.obterId());
				            		
						            update.execute();
						            
						            apolice.excluir();
				            	}
				            }
			            }
			        }
					
					//System.out.println(qtde + " - " + aseg2.obterNome() + " - " + numero + " - " + status_apolice + " - " + secao + " - " + numero_endoso + " - " + certificado);
				}
				
				/*cont++;
				if(cont == 6)
					break;*/
			}
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			System.out.println("Erro na duplicidade apolice".toUpperCase());
		}
	}
	
	private void duplicidadeDadosPrevisao() throws Exception
	{
		try
		{
			System.out.println("Verificando duplicidade dados previsao");
			
			int cont = 1;
			SQLQuery query;
			SQLRow[] rows;
			SQLRow[] rows2;
			int qtde;
			long origem,id;
			String status_apolice;
			long superior,secao,dataCorte;
			double numero_endoso,certificado;
			DadosPrevisao dados;
			
			for(Aseguradora aseg2 : aseguradoras)
			{
				//SQLQuery query = getModelManager().createSQLQuery("crm", "select count(*) as qtde, origem, status_apolice, superior, secao, data_corte, dados_previsao.numero_endoso, dados_previsao.certificado from evento,dados_previsao,apolice where evento.id = dados_previsao.id and superior = apolice.id and origem = ? group by origem, status_apolice,superior, secao, data_corte, dados_previsao.numero_endoso, dados_previsao.certificado having count(*) > 1");
				query = getModelManager().createSQLQuery("crm", "EXEC duplicidadeDadosPrevisao ?");
				query.addLong(aseg2.obterId());
				
				rows = query.execute();
				
				for(int i = 0 ; i < rows.length ; i++)
				{
					qtde = rows[i].getInt("qtde");
					origem = rows[i].getLong("origem");
					status_apolice = rows[i].getString("status_apolice");
					superior = rows[i].getLong("superior");
					secao = rows[i].getLong("secao");
					dataCorte = rows[i].getLong("data_corte");
					numero_endoso = rows[i].getDouble("numero_endoso");
					certificado = rows[i].getDouble("certificado");
					
					/*System.out.println("Duplicada");
					System.out.println(qtde + " - " + aseg2.obterNome() + " - " + dataCorte + " - " + status_apolice + " - " + secao + " - " + numero_endoso + " - " + certificado + " - "+ superior);*/
					
					//query = getModelManager().createSQLQuery("crm", "select evento.id from evento,dados_previsao,apolice where evento.id = dados_previsao.id and superior = apolice.id and origem =? and status_apolice = ? and superior = ? and secao = ? and data_corte = ? and dados_previsao.numero_endoso = ? and dados_previsao.certificado = ? order by criacao desc");
					query = getModelManager().createSQLQuery("crm", "EXEC duplicidadeDadosPrevisao2 ?,?,?,?,?,?,?");
			        query.addLong(aseg2.obterId());
			        query.addString(status_apolice);
			        query.addLong(superior);
			        query.addLong(secao);
			        query.addLong(dataCorte);
			        query.addDouble(numero_endoso);
			        query.addDouble(certificado);
					
			        rows2 = query.execute();
			        
			        for(int k = 0; k < rows2.length; k++)
			        {
			            id = rows2[k].getLong("id");
			            dados = (DadosPrevisao) home.obterEventoPorId(id);
			            if(dados!=null)
			            {
				            //System.out.println(id + " - " + new SimpleDateFormat("dd/MM/yyyy").format(dados.obterCriacao()));
				            if(k>0)
				            	dados.excluir();
			            }
			        }
				}
				
				/*cont++;
				if(cont == 4)
					break;*/
			}
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			System.out.println("Erro na duplicidade dados previsao".toUpperCase());
		}
	}
	
	private void duplicidadeDadosReaseguro() throws Exception
	{
		try
		{
			System.out.println("Verificando duplicidade dados reaseguro");
			
			int cont = 1;
			SQLQuery query;
			SQLRow[] rows,rows2;
			int qtde;
			long origem,superior,secao,reaseguradora,id;
			String status_apolice,tipoContrato;
			double numero_endoso,certificado;
			DadosReaseguro dadosSuperior,dados;
			SQLUpdate update;
			
			for(Aseguradora aseg2 : aseguradoras)
			{
				//query = getModelManager().createSQLQuery("crm", "select count(*) as qtde, origem, superior, status_apolice, secao, reaseguradora, tipo_contrato, dados_reaseguro.valor_endoso, dados_reaseguro.certificado from evento,dados_reaseguro,apolice where evento.id = dados_reaseguro.id and superior = apolice.id and origem = ? group by origem, superior, status_apolice, secao, reaseguradora, tipo_contrato, dados_reaseguro.valor_endoso, dados_reaseguro.certificado having count(*) > 1");
				query = getModelManager().createSQLQuery("crm", "EXEC duplicidadeDadosReaseguro ?");
				query.addLong(aseg2.obterId());
				
				rows = query.execute();
				
				for(int i = 0 ; i < rows.length ; i++)
				{
					qtde = rows[i].getInt("qtde");
					origem = rows[i].getLong("origem");
					status_apolice = rows[i].getString("status_apolice");
					superior = rows[i].getLong("superior");
					secao = rows[i].getLong("secao");
					numero_endoso = rows[i].getDouble("valor_endoso");
					certificado = rows[i].getDouble("certificado");
					tipoContrato = rows[i].getString("tipo_contrato");
					reaseguradora = rows[i].getLong("reaseguradora");
					
					/*System.out.println("Duplicada");
					System.out.println(qtde + " - " + aseg2.obterNome() + " - " + reaseguradora + " - " + status_apolice + " - " + secao + " - " + numero_endoso + " - " + certificado + " - "+ superior + " - " + tipoContrato);*/
					
					//query = getModelManager().createSQLQuery("crm", "select evento.id from evento,dados_reaseguro,apolice where evento.id = dados_reaseguro.id and superior = apolice.id and origem = ? and superior = ? and status_apolice = ? and secao = ? and reaseguradora = ? and tipo_contrato = ? and dados_reaseguro.valor_endoso = ? and dados_reaseguro.certificado = ? order by criacao desc");
					query = getModelManager().createSQLQuery("crm", "EXEC duplicidadeDadosReaseguro2 ?,?,?,?,?,?,?,?");
					query.addLong(aseg2.obterId());
				    query.addLong(superior);
				    query.addString(status_apolice);
				    query.addLong(secao);
				    query.addLong(reaseguradora);
				    query.addString(tipoContrato);
				    query.addDouble(numero_endoso);
				    query.addDouble(certificado);
					
			        rows2 = query.execute();
			        
			        dadosSuperior = null;
			        
			        for(int k = 0; k < rows2.length; k++)
			        {
			            id = rows2[k].getLong("id");
			            dados = (DadosReaseguro) home.obterEventoPorId(id);
			            if(dados!=null)
			            {
				            //System.out.println(id + " - " + new SimpleDateFormat("dd/MM/yyyy").format(dados.obterCriacao()));
				            if(k == 0)
				            	dadosSuperior = dados;
				            else
				            {
				            	update = getModelManager().createSQLUpdate("crm","EXEC atualizarSuperior "+ dadosSuperior.obterId()+","+dados.obterId());
					            
					            update.execute();
					            
					            dados.excluir();
				            }
			            }
			        }
				}
				
				/*cont++;
				if(cont == 2)
					break;*/
			}
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			System.out.println("Erro na duplicidade dados reaseguro".toUpperCase());
		}
	}
	
	private void duplicidadeDadosCoaseguro() throws Exception
	{
		try
		{
			System.out.println("Verificando duplicidade dados coaseguro");
			
			int cont = 1;
			SQLQuery query;
			SQLRow[] rows,rows2;
			int qtde;
			long origem,superior,secao,aseguradora,id;
			String status_apolice;
			double numero_endoso,certificado;
			DadosCoaseguro dados;
			
			for(Aseguradora aseg2 : aseguradoras)
			{
				//query = getModelManager().createSQLQuery("crm", "select count(*) as qtde, origem, aseguradora, superior, status_apolice, secao, dados_coaseguro.numero_endoso, dados_coaseguro.certificado from evento,dados_coaseguro,apolice where evento.id = dados_coaseguro.id and superior = apolice.id and origem=? group by origem, aseguradora, superior, status_apolice, secao, dados_coaseguro.numero_endoso, dados_coaseguro.certificado having count(*) > 1");
				query = getModelManager().createSQLQuery("crm", "EXEC duplicidadeDadosCoaseguro ?");
				query.addLong(aseg2.obterId());
				
				rows = query.execute();
				
				for(int i = 0 ; i < rows.length ; i++)
				{
					qtde = rows[i].getInt("qtde");
					origem = rows[i].getLong("origem");
					status_apolice = rows[i].getString("status_apolice");
					superior = rows[i].getLong("superior");
					secao = rows[i].getLong("secao");
					numero_endoso = rows[i].getDouble("numero_endoso");
					certificado = rows[i].getDouble("certificado");
					aseguradora = rows[i].getLong("aseguradora");
					
					/*System.out.println("Duplicada");
					System.out.println(qtde + " - " + aseg2.obterNome() + " - " + aseguradora + " - " + status_apolice + " - " + secao + " - " + numero_endoso + " - " + certificado + " - "+ superior);*/
					
					//query = getModelManager().createSQLQuery("crm", "select evento.id from evento,dados_coaseguro,apolice where evento.id = dados_coaseguro.id and superior = apolice.id and origem=? and aseguradora = ? and superior = ? and status_apolice = ? and secao = ? and dados_coaseguro.numero_endoso = ? and dados_coaseguro.certificado = ? order by criacao desc");
					query = getModelManager().createSQLQuery("crm", "EXEC duplicidadeDadosCoaseguro2 ?,?,?,?,?,?,?");
			        query.addLong(aseg2.obterId());
			        query.addLong(aseguradora);
			        query.addLong(superior);
			        query.addString(status_apolice);
			        query.addLong(secao);
			        query.addDouble(numero_endoso);
			        query.addDouble(certificado);
					
					//query = getModelManager().createSQLQuery("crm", "EXEC duplicidadeDadosCoaseguro2 " + origem+","+aseguradora+","+superior+",'"+status_apolice+"',"+secao+","+numero_endoso+","+certificado);
			        
			        rows2 = query.execute();
			        
			        for(int k = 0; k < rows2.length; k++)
			        {
			            id = rows2[k].getLong("id");
			            dados = (DadosCoaseguro) home.obterEventoPorId(id);
			            if(dados!=null)
			            {
				            //System.out.println(id + " - " + new SimpleDateFormat("dd/MM/yyyy").format(dados.obterCriacao()));
				            if(k>0)
				            	dados.excluir();
			            }
			        }
				}
				
				/*cont++;
				if(cont == 8)
					break;*/
			}
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			System.out.println("Erro na duplicidade dados coaseguro".toUpperCase());
		}
	}
	
	private void duplicidadeSinistro() throws Exception
	{
		try
		{
			System.out.println("Verificando duplicidade sinistro");
			
			int cont = 1;
			SQLQuery query;
			SQLRow[] rows,rows2;
			int qtde;
			String status_apolice,numeroSinistro;
			long superior,secao,id,dataSinistro;
			double numero_endoso,certificado;
			Sinistro sinistroSuperior,sinistro;
			SQLUpdate update;
			
			for(Aseguradora aseg2 : aseguradoras)
			{
				/*query = getModelManager().createSQLQuery("crm", "select count(*) as qtde, origem, superior, status_apolice, secao, numero, sinistro.numero_endoso, sinistro.certificado, sinistro.data_sinistro from evento,sinistro,apolice where evento.id = sinistro.id and superior = apolice.id and origem = ? group by origem, superior, status_apolice, secao, numero, sinistro.numero_endoso, sinistro.certificado, sinistro.data_sinistro having count(*) > 1");
				query.addLong(aseg2.obterId());*/
				
				query = getModelManager().createSQLQuery("crm", "EXEC duplicidadeSinistro " + aseg2.obterId());
				
				rows = query.execute();
				
				for(int i = 0 ; i < rows.length ; i++)
				{
					qtde = rows[i].getInt("qtde");
					status_apolice = rows[i].getString("status_apolice");
					superior = rows[i].getLong("superior");
					secao = rows[i].getLong("secao");
					numero_endoso = rows[i].getDouble("numero_endoso");
					certificado = rows[i].getDouble("certificado");
					numeroSinistro = rows[i].getString("numero");
					dataSinistro = rows[i].getLong("data_sinistro");
					
					//System.out.println(qtde + " - " + aseg2.obterNome() + " - " + numeroSinistro + " - " + status_apolice + " - " + secao + " - " + numero_endoso + " - " + certificado + " - "+ superior + " - " + dataSinistro);
					
					query = getModelManager().createSQLQuery("crm", "EXEC duplicidadeSinistro2 ?,?,?,?,?,?,?,?");
			        query.addLong(aseg2.obterId());
			        query.addLong(superior);
			        query.addString(status_apolice);
			        query.addLong(secao);
			        query.addString(numeroSinistro);
			        query.addDouble(numero_endoso);
			        query.addDouble(certificado);
		            query.addLong(dataSinistro);
					
					//query = getModelManager().createSQLQuery("crm", "EXEC duplicidadeSinistro2 "+aseg2.obterId()+","+superior+",'"+status_apolice+"',"+secao+",'"+numeroSinistro+"',"+numero_endoso+","+certificado+","+dataSinistro);
			        
			        rows2 = query.execute();
			        
			        sinistroSuperior = null;
			        
			        for(int k = 0; k < rows2.length; k++)
			        {
			            id = rows2[k].getLong("id");
			            sinistro = (Sinistro) home.obterEventoPorId(id);
			            if(sinistro!=null)
			            {
				            //System.out.println(id + " - " + new SimpleDateFormat("dd/MM/yyyy").format(sinistro.obterCriacao()));
				            if(k == 0)
				            	sinistroSuperior = sinistro;
				            else
				            {
				            	/*update = getModelManager().createSQLUpdate("crm","update evento set superior = ? where superior = ?");
					            update.addLong(sinistroSuperior.obterId());
					            update.addLong(sinistro.obterId());*/
				            	
				            	update = getModelManager().createSQLUpdate("crm","EXEC atualizarSuperior "+ sinistroSuperior.obterId()+","+sinistro.obterId());
					            
					            update.execute();
					            
					            sinistro.excluir();
				            }
			            }
			        }
				}
				
				/*cont++;
				if(cont == 2)
					break;*/
			}
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			System.out.println("Erro na duplicidade sinistro".toUpperCase());
		}
	}
	
	private void duplicidadeFaturaSinistro() throws Exception
	{
		try
		{
			System.out.println("Verificando duplicidade fatura sinistro");
			
			int cont = 1;
			SQLQuery query;
			SQLRow[] rows,rows2;
			int qtde;
			long superior,data_pagamento,dataSinistro,id;
			double numero_endoso,certificado;
			String tipo,numeroDoc,fatura,ruc_provedor;
			FaturaSinistro faturaSinistro;
			
			for(Aseguradora aseg2 : aseguradoras)
			{
				/*query = getModelManager().createSQLQuery("crm", "select count(*) as qtde, origem, superior, tipo, numero_documento, ruc_provedor, fatura_sinistro.numero_endoso, fatura_sinistro.data_pagamento, fatura_sinistro.certificado, fatura_sinistro.numero_fatura, sinistro.data_sinistro from evento,fatura_sinistro,sinistro where evento.id = fatura_sinistro.id and superior = sinistro.id and origem = ? group by origem, superior, tipo, numero_documento, ruc_provedor, fatura_sinistro.numero_endoso, fatura_sinistro.data_pagamento, fatura_sinistro.certificado, fatura_sinistro.numero_fatura, sinistro.data_sinistro having count(*) > 1");
				query.addLong(aseg2.obterId());*/
				
				query = getModelManager().createSQLQuery("crm", "EXEC duplicidadeFaturaSinistro " + aseg2.obterId());
				
				rows = query.execute();
				
				for(int i = 0 ; i < rows.length ; i++)
				{
					qtde = rows[i].getInt("qtde");
					superior = rows[i].getLong("superior");
					numero_endoso = rows[i].getDouble("numero_endoso");
					certificado = rows[i].getDouble("certificado");
					tipo = rows[i].getString("tipo");
					numeroDoc = rows[i].getString("numero_documento");
					fatura = rows[i].getString("numero_fatura");
					ruc_provedor = rows[i].getString("ruc_provedor");
					data_pagamento = rows[i].getLong("data_pagamento");
					dataSinistro= rows[i].getLong("data_sinistro");
					
					/*System.out.println("Duplicada");
					System.out.println(qtde + " - " + aseg2.obterNome() + " - " + numeroDoc + " - " + data_pagamento + " - " + secao + " - " + numero_endoso + " - " + certificado + " - "+ superior + " - " + dataSinistro);*/
					
					query = getModelManager().createSQLQuery("crm", "EXEC duplicidadeFaturaSinistro2 ?,?,?,?,?,?,?,?,?,?");
			        query.addLong(aseg2.obterId());
			        query.addLong(superior);
			        query.addString(tipo);
			        query.addString(numeroDoc);
			        query.addString(ruc_provedor);
			        query.addDouble(numero_endoso);
			        query.addLong(data_pagamento);
			        query.addDouble(certificado);
			        query.addString(fatura);
		            query.addLong(dataSinistro);
					
			        rows2 = query.execute();
			        
			        for(int k = 0; k < rows2.length; k++)
			        {
			            id = rows2[k].getLong("id");
			            faturaSinistro = (FaturaSinistro) home.obterEventoPorId(id);
			            if(faturaSinistro!=null)
			            {
				            //System.out.println(id + " - " + new SimpleDateFormat("dd/MM/yyyy").format(faturaSinistro.obterCriacao()));
				            if(k>0)
				            	faturaSinistro.excluir();
			            }
			        }
				}
				
				/*cont++;
				if(cont == 3)
					break;*/
			}
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			System.out.println("Erro na duplicidade fatura sinistro".toUpperCase());
		}
	}
	
	private void duplicidadeAnulacao() throws Exception
	{
		try
		{
			System.out.println("Verificando duplicidade anulacao");
			
			int cont = 1;
			SQLQuery query;
			SQLRow[] rows,rows2;
			int qtde;
			long superior,secao,data_anulacao,id;
			String status;
			double numero_endoso,certificado;
			 AnulacaoInstrumento anulacao;
			 
			for(Aseguradora aseg2 : aseguradoras)
			{
				/*query = getModelManager().createSQLQuery("crm", "select count(*) as qtde, origem, superior, status_apolice, secao, data_anulacao, anulacao_instrumento.numero_endoso, anulacao_instrumento.certificado from evento,anulacao_instrumento,apolice where evento.id = anulacao_instrumento.id and superior = apolice.id and origem = ? group by origem, superior, status_apolice, secao, data_anulacao, anulacao_instrumento.numero_endoso, anulacao_instrumento.certificado having count(*) > 1");
				query.addLong(aseg2.obterId());*/
				
				query = getModelManager().createSQLQuery("crm", "EXEC duplicidadeAnulacao " + aseg2.obterId());
				
				rows = query.execute();
				
				for(int i = 0 ; i < rows.length ; i++)
				{
					qtde = rows[i].getInt("qtde");
					superior = rows[i].getLong("superior");
					secao = rows[i].getLong("secao");
					status = rows[i].getString("status_apolice");
					numero_endoso = rows[i].getDouble("numero_endoso");
					certificado = rows[i].getDouble("certificado");
					data_anulacao= rows[i].getLong("data_anulacao");
					
					/*System.out.println("Duplicada");
					System.out.println(qtde + " - " + aseg2.obterNome() + " - " + data_anulacao + " - " + secao + " - " + numero_endoso + " - " + certificado + " - "+ superior);*/
					
					query = getModelManager().createSQLQuery("crm", "EXEC duplicidadeAnulacao2 ?,?,?,?,?,?,?");
			        query.addLong(aseg2.obterId());
			        query.addLong(superior);
			        query.addString(status);
			        query.addLong(secao);
			        query.addLong(data_anulacao);
			        query.addDouble(numero_endoso);
			        query.addDouble(certificado);
					
			        rows2 = query.execute();
			        
			        for(int k = 0; k < rows2.length; k++)
			        {
			            id = rows2[k].getLong("id");
			            anulacao = (AnulacaoInstrumento) home.obterEventoPorId(id);
			            if(anulacao!=null)
			            {
				            //System.out.println(id + " - " + new SimpleDateFormat("dd/MM/yyyy").format(anulacao.obterCriacao()));
				            if(k>0)
				            	anulacao.excluir();
			            }
			        }
				}
				
				/*cont++;
				if(cont == 3)
					break;*/
			}
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			System.out.println("Erro na duplicidade anulacao".toUpperCase());
		}
	}
	
	private void duplicidadeCobranca() throws Exception
	{
		try
		{
			System.out.println("Verificando duplicidade registro cobranca");
			
			int cont = 1;
			SQLQuery query;
			SQLRow[] rows,rows2;
			int qtde,numeroParcela;
			long superior,secao,data_cobranca,id;
			String status;
			double numero_endoso,certificado;
			RegistroCobranca cobranca;
			
			for(Aseguradora aseg2 : aseguradoras)
			{
				/*query = getModelManager().createSQLQuery("crm", "select count(*) as qtde, origem, superior, status_apolice, secao, data_cobranca, numero_parcela, registro_cobranca.numero_endoso, registro_cobranca.certificado from evento,registro_cobranca,apolice where evento.id = registro_cobranca.id and superior = apolice.id and origem=? group by origem, superior, status_apolice, secao, data_cobranca, numero_parcela, registro_cobranca.numero_endoso, registro_cobranca.certificado having count(*) > 1");
				query.addLong(aseg2.obterId());*/
				
				query = getModelManager().createSQLQuery("crm", "EXEC duplicidadeCobranca " + aseg2.obterId());
				
				rows = query.execute();
				
				for(int i = 0 ; i < rows.length ; i++)
				{
					qtde = rows[i].getInt("qtde");
					superior = rows[i].getLong("superior");
					secao = rows[i].getLong("secao");
					status = rows[i].getString("status_apolice");
					numero_endoso = rows[i].getDouble("numero_endoso");
					certificado = rows[i].getDouble("certificado");
					data_cobranca= rows[i].getLong("data_cobranca");
					numeroParcela = rows[i].getInt("numero_parcela");
					
					/*System.out.println("Duplicada");
					System.out.println(qtde + " - " + aseg2.obterNome() + " - " + data_cobranca + " - " + secao + " - " + numero_endoso + " - " + certificado + " - "+ superior + " - "+ numeroParcela);*/
					
					query = getModelManager().createSQLQuery("crm", "EXEC duplicidadeCobranca2 ?,?,?,?,?,?,?,?");
			        query.addLong(aseg2.obterId());
			        query.addLong(superior);
			        query.addString(status);
			        query.addLong(secao);
			        query.addLong(data_cobranca);
			        query.addInt(numeroParcela);
			        query.addDouble(numero_endoso);
			        query.addDouble(certificado);
					
			        rows2 = query.execute();
			        
			        for(int k = 0; k < rows2.length; k++)
			        {
			            id = rows2[k].getLong("id");
			            cobranca = (RegistroCobranca) home.obterEventoPorId(id);
			            if(cobranca!=null)
			            {
				            //System.out.println(id + " - " + new SimpleDateFormat("dd/MM/yyyy").format(cobranca.obterCriacao()));
				            if(k>0)
				            	cobranca.excluir();
			            }
			        }
				}
				
				/*cont++;
				if(cont == 2)
					break;*/
			}
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			System.out.println("Erro na duplicidade registro cobranca".toUpperCase());
		}
	}
	
	private void duplicidadeAspectos() throws Exception
	{
		try
		{
			System.out.println("Verificando duplicidade aspectos legais");
			
			int cont = 1;
			SQLQuery query;
			SQLRow[] rows,rows2;
			int qtde;
			long superior,secao,id;
			String status,numeroOrdem;
			double numero_endoso,certificado;
			AspectosLegais aspecto;
			
			for(Aseguradora aseg2 : aseguradoras)
			{
				/*query = getModelManager().createSQLQuery("crm", "select count(*) as qtde, origem, superior, status_apolice, secao, numero_ordem, aspectos_legais.numero_endoso, aspectos_legais.certificado from evento,aspectos_legais,apolice where evento.id = aspectos_legais.id and superior = apolice.id and origem=? group by origem, superior, status_apolice, secao, numero_ordem, aspectos_legais.numero_endoso, aspectos_legais.certificado having count(*) > 1");
				query.addLong(aseg2.obterId());*/
				
				query = getModelManager().createSQLQuery("crm", "EXEC duplicidadeAspectos " + aseg2.obterId());
				
				rows = query.execute();
				
				for(int i = 0 ; i < rows.length ; i++)
				{
					qtde = rows[i].getInt("qtde");
					superior = rows[i].getLong("superior");
					secao = rows[i].getLong("secao");
					status = rows[i].getString("status_apolice");
					numeroOrdem = rows[i].getString("numero_ordem");
					numero_endoso = rows[i].getDouble("numero_endoso");
					certificado = rows[i].getDouble("certificado");
					
					/*System.out.println("Duplicada");
					System.out.println(qtde + " - " + aseg2.obterNome() + " - " + numeroOrdem + " - " + secao + " - " + numero_endoso + " - " + certificado + " - "+ superior);*/
					
					query = getModelManager().createSQLQuery("crm", "EXEC duplicidadeAspectos2 ?,?,?,?,?,?,?");
			        query.addLong(aseg2.obterId());
			        query.addLong(superior);
			        query.addString(status);
			        query.addLong(secao);
			        query.addString(numeroOrdem);
			        query.addDouble(numero_endoso);
			        query.addDouble(certificado);
					
			        rows2 = query.execute();
			        
			        for(int k = 0; k < rows2.length; k++)
			        {
			            id = rows2[k].getLong("id");
			            aspecto = (AspectosLegais) home.obterEventoPorId(id);
			            if(aspecto!=null)
			            {
				            //System.out.println(id + " - " + new SimpleDateFormat("dd/MM/yyyy").format(aspecto.obterCriacao()));
				            if(k>0)
				            	aspecto.excluir();
			            }
			        }
				}
				
				/*cont++;
				if(cont == 2)
					break;*/
			}
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			System.out.println("Erro na duplicidade aspectos legais".toUpperCase());
		}
	}
	
	private void duplicidadeSuplementos() throws Exception
	{
		try
		{
			System.out.println("Verificando duplicidade suplementos");
			
			int cont = 1;
			SQLQuery query;
			SQLRow[] rows,rows2;
			int qtde;
			long superior,secao,id;
			String status;
			double numero_endoso,certificado;
			 Suplemento suplemento;
			 
			for(Aseguradora aseg2 : aseguradoras)
			{
				/*query = getModelManager().createSQLQuery("crm", "select count(*) as qtde, origem, superior, status_apolice, secao, suplemento.numero_endoso, suplemento.certificado from evento,suplemento,apolice where evento.id = suplemento.id and superior = apolice.id and origem = ? group by origem, superior, status_apolice, secao, suplemento.numero_endoso, suplemento.certificado having count(*) > 1");
				query.addLong(aseg2.obterId());*/
				
				query = getModelManager().createSQLQuery("crm", "EXEC duplicidadeSuplementos " + aseg2.obterId());
				
				rows = query.execute();
				
				for(int i = 0 ; i < rows.length ; i++)
				{
					qtde = rows[i].getInt("qtde");
					superior = rows[i].getLong("superior");
					secao = rows[i].getLong("secao");
					status = rows[i].getString("status_apolice");
					numero_endoso = rows[i].getDouble("numero_endoso");
					certificado = rows[i].getDouble("certificado");
					
					/*System.out.println("Duplicada");
					System.out.println(qtde + " - " + aseg2.obterNome() + " - " + secao + " - " + numero_endoso + " - " + certificado + " - "+ superior);*/
					
					query = getModelManager().createSQLQuery("crm", "EXEC duplicidadeSuplementos2 ?,?,?,?,?,?");
				    query.addLong(aseg2.obterId());
				    query.addLong(superior);
				    query.addString(status);
				    query.addLong(secao);
				    query.addDouble(numero_endoso);
				    query.addDouble(certificado);
					
			        rows2 = query.execute();
			        
			        for(int k = 0; k < rows2.length; k++)
			        {
			            id = rows2[k].getLong("id");
			            suplemento = (Suplemento) home.obterEventoPorId(id);
			            if(suplemento!=null)
			            {
				            //System.out.println(id + " - " + new SimpleDateFormat("dd/MM/yyyy").format(suplemento.obterCriacao()));
				            if(k>0)
				            	suplemento.excluir();
			            }
			        }
				}
				
				/*cont++;
				if(cont == 6)
					break;*/
			}
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			System.out.println("Erro na duplicidade suplementos".toUpperCase());
		}
	}
	
	private void duplicidadeRefinacao() throws Exception
	{
		try
		{
			System.out.println("Verificando duplicidade refinacao");
			
			int cont = 1;
			SQLQuery query;
			SQLRow[] rows,rows2;
			int qtde;
			long superior,secao,data,id;
			String status;
			double numero_endoso,certificado;
			Refinacao ref;
			
			for(Aseguradora aseg2 : aseguradoras)
			{
				/*query = getModelManager().createSQLQuery("crm", "select count(*) as qtde, origem, superior, status_apolice, secao, classe, apolice.numero_endoso, apolice.certificado, data_prevista_inicio from evento,apolice,refinacao where evento.id = refinacao.id and superior = apolice.id and origem=? group by origem, superior, status_apolice, secao, classe, apolice.numero_endoso, apolice.certificado, data_prevista_inicio having count(*) > 1");
				query.addLong(aseg2.obterId());*/
				
				query = getModelManager().createSQLQuery("crm", "EXEC duplicidadeRefinacao " + aseg2.obterId());
				
				rows = query.execute();
				
				for(int i = 0 ; i < rows.length ; i++)
				{
					qtde = rows[i].getInt("qtde");
					superior = rows[i].getLong("superior");
					secao = rows[i].getLong("secao");
					status = rows[i].getString("status_apolice");
					numero_endoso = rows[i].getDouble("numero_endoso");
					certificado = rows[i].getDouble("certificado");
					data = rows[i].getLong("data_prevista_inicio");
					
					/*System.out.println("Duplicada");
					System.out.println(qtde + " - " + aseg2.obterNome() + " - " + secao + " - " + numero_endoso + " - " + certificado + " - "+ superior + " - " + data);*/
					
					query = getModelManager().createSQLQuery("crm", "EXEC duplicidadeRefinacao2 ?,?,?,?,?,?,?");
			        query.addLong(aseg2.obterId());
			        query.addLong(superior);
			        query.addString(status);
			        query.addLong(secao);
			        query.addDouble(numero_endoso);
			        query.addDouble(certificado);
			        query.addLong(data);
					
			        rows2 = query.execute();
			        
			        for(int k = 0; k < rows2.length; k++)
			        {
			            id = rows2[k].getLong("id");
			            ref = (Refinacao) home.obterEventoPorId(id);
			            if(ref!=null)
			            {
				            //System.out.println(id + " - " + new SimpleDateFormat("dd/MM/yyyy").format(ref.obterCriacao()));
				            if(k>0)
				            	ref.excluir();
			            }
			        }
				}
				
				/*cont++;
				if(cont == 4)
					break;*/
			}
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			System.out.println("Erro na duplicidade refinacao".toUpperCase());
		}
	}
	
	private void duplicidadeRegistroGastos() throws Exception
	{
		try
		{
			System.out.println("Verificando duplicidade registro de gastos");
			
			int cont = 1;
			SQLQuery query;
			SQLRow[] rows,rows2;
			int qtde;
			long superior,id;
			String tipoInstrumento,tipo,cheque;
			double numero_endoso,certificado;
			long dataPagamento,dataSinistro;
			RegistroGastos gastos;
			
			for(Aseguradora aseg2 : aseguradoras)
			{
				/*query = getModelManager().createSQLQuery("crm", "select count(*) as qtde, origem, superior, evento.tipo, registro_gastos.data_pagamento, registro_gastos.data_sinistro, registro_gastos.numero_endoso, registro_gastos.certificado, numero_cheque, registro_gastos.tipo_instrumento from evento,registro_gastos,sinistro where evento.id = registro_gastos.id and superior = sinistro.id and origem=? group by origem, superior, evento.tipo, registro_gastos.data_pagamento, registro_gastos.data_sinistro, registro_gastos.numero_endoso, registro_gastos.certificado, numero_cheque, registro_gastos.tipo_instrumento having count(*) > 1");
				query.addLong(aseg2.obterId());*/
				
				query = getModelManager().createSQLQuery("crm", "EXEC duplicidadeRegistroGastos " + aseg2.obterId());
				
				rows = query.execute();
				
				for(int i = 0 ; i < rows.length ; i++)
				{
					qtde = rows[i].getInt("qtde");
					superior = rows[i].getLong("superior");
					tipoInstrumento = rows[i].getString("tipo_instrumento");
					tipo = rows[i].getString("tipo");
					cheque = rows[i].getString("numero_cheque");
					numero_endoso = rows[i].getDouble("numero_endoso");
					certificado = rows[i].getDouble("certificado");
					dataPagamento = rows[i].getLong("data_pagamento");
					dataSinistro = rows[i].getLong("data_sinistro");
					
					/*System.out.println("Duplicada");
					System.out.println(qtde + " - " + aseg2.obterNome() + " - " + secao + " - " + numero_endoso + " - " + certificado + " - "+ superior + " - " + dataSinistro + " - " + tipo);*/
					
					query = getModelManager().createSQLQuery("crm", "EXEC duplicidadeRegistroGastos2 ?,?,?,?,?,?,?,?,?");
			        query.addLong(aseg2.obterId());
			        query.addLong(superior);
			        query.addString(tipo);
			        query.addLong(dataPagamento);
			        query.addLong(dataSinistro);
			        query.addDouble(numero_endoso);
			        query.addDouble(certificado);
			        query.addString(cheque);
			        query.addString(tipoInstrumento);
					
			        rows2 = query.execute();
			        
			        for(int k = 0; k < rows2.length; k++)
			        {
			            id = rows2[k].getLong("id");
			            gastos = (RegistroGastos) home.obterEventoPorId(id);
			            if(gastos!=null)
			            {
				            //System.out.println(id + " - " + new SimpleDateFormat("dd/MM/yyyy").format(gastos.obterCriacao()));
				            if(k>0)
				            	gastos.excluir();
			            }
			        }
				}
				
				/*cont++;
				if(cont == 3)
					break;*/
			}
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			System.out.println("Erro na duplicidade registro de gastos".toUpperCase());
		}
	}
	
	private void duplicidadeRegistroAnulacao() throws Exception
	{
		try
		{
			System.out.println("Verificando duplicidade registro de anulacao");
			
			int cont = 1;
			SQLQuery query;
			SQLRow[] rows,rows2;
			int qtde;
			long superior,reaseguradora,data_anulacao,id;
			String tipo,tipoInstrumento,tipoContrato;
			double numero_endoso,certificado;
			RegistroAnulacao anulacao;
			
			for(Aseguradora aseg2 : aseguradoras)
			{
				/*query = getModelManager().createSQLQuery("crm", "select count(*) as qtde, origem, superior, registro_anulacao.reaseguradora, tipo, registro_anulacao.numero_endoso, registro_anulacao.certificado, registro_anulacao.tipo_contrato, registro_anulacao.data_anulacao, registro_anulacao.tipo_instrumento from evento,registro_anulacao,dados_reaseguro where evento.id = registro_anulacao.id and superior = dados_reaseguro.id and origem=? group by origem, superior, registro_anulacao.reaseguradora, tipo, registro_anulacao.numero_endoso, registro_anulacao.certificado, registro_anulacao.tipo_contrato, registro_anulacao.data_anulacao, registro_anulacao.tipo_instrumento having count(*) > 1");
				query.addLong(aseg2.obterId());*/
				query = getModelManager().createSQLQuery("crm", "EXEC duplicidadeRegistroAnulacao ?");
				query.addLong(aseg2.obterId());
				
				rows = query.execute();
				
				for(int i = 0 ; i < rows.length ; i++)
				{
					qtde = rows[i].getInt("qtde");
					superior = rows[i].getLong("superior");
					reaseguradora = rows[i].getLong("reaseguradora");
					tipo = rows[i].getString("tipo");
					tipoInstrumento = rows[i].getString("tipo_instrumento");
					tipoContrato = rows[i].getString("tipo_contrato");
					numero_endoso = rows[i].getDouble("numero_endoso");
					certificado = rows[i].getDouble("certificado");
					data_anulacao = rows[i].getLong("data_anulacao");
					
					/*System.out.println("Duplicada");
					System.out.println(qtde + " - " + aseg2.obterNome() + " - " + secao + " - " + numero_endoso + " - " + certificado + " - "+ superior + " - " + data_anulacao + " - " + tipoContrato);*/
					
					//query = getModelManager().createSQLQuery("crm", "select evento.id from evento,registro_anulacao,dados_reaseguro where evento.id = registro_anulacao.id and superior = dados_reaseguro.id and origem=? and superior = ? and registro_anulacao.reaseguradora = ? and tipo = ? and registro_anulacao.numero_endoso = ? and registro_anulacao.certificado = ? and registro_anulacao.tipo_contrato = ? and registro_anulacao.data_anulacao = ? and registro_anulacao.tipo_instrumento = ? order by criacao desc");
					query = getModelManager().createSQLQuery("crm", "EXEC duplicidadeRegistroAnulacao2 ?,?,?,?,?,?,?,?,?");
			        query.addLong(aseg2.obterId());
			        query.addLong(superior);
			        query.addLong(reaseguradora);
			        query.addString(tipo);
			        query.addDouble(numero_endoso);
			        query.addDouble(certificado);
			        query.addString(tipoContrato);
			        query.addLong(data_anulacao);
			        query.addString(tipoInstrumento);
					
					//query = getModelManager().createSQLQuery("crm", "EXEC duplicidadeRegistroAnulacao2"+aseg2.obterId()+","+superior+","+reaseguradora+",'"+tipo+"',"+numero_endoso+","+certificado+",'"+tipoContrato+"',"+data_anulacao+",'"+tipoInstrumento+"'");
			        
			        rows2 = query.execute();
			        
			        for(int k = 0; k < rows2.length; k++)
			        {
			            id = rows2[k].getLong("id");
			            anulacao = (RegistroAnulacao) home.obterEventoPorId(id);
			            if(anulacao!=null)
			            {
				            //System.out.println(id + " - " + new SimpleDateFormat("dd/MM/yyyy").format(anulacao.obterCriacao()));
				            if(k>0)
				            	anulacao.excluir();
			            }
			        }
				}
				
				/*cont++;
				if(cont == 3)
					break;*/
			}
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			System.out.println("Erro na duplicidade registro de anulacao".toUpperCase());
		}
	}
	
	private void duplicidadeMorosidade() throws Exception
	{
		try
		{
			System.out.println("Verificando duplicidade morosidade");
			
			int cont = 1;
			SQLQuery query;
			SQLRow[] rows,rows2;
			int qtde;
			long superior,secao,data_corte,id;
			int numero_parcela;
			double numero_endoso,certificado;
			Morosidade morosidade;
			String status_apolice;
			
			for(Aseguradora aseg2 : aseguradoras)
			{
				/*query = getModelManager().createSQLQuery("crm", "select count(*) as qtde, origem, superior, status_apolice, secao, data_corte, morosidade.numero_endoso, morosidade.certificado, numero_parcela from evento,morosidade,apolice where evento.id = morosidade.id and superior = apolice.id and origem=? group by origem, superior, status_apolice, secao, data_corte, morosidade.numero_endoso, morosidade.certificado, numero_parcela having count(*) > 1");
				query.addLong(aseg2.obterId());*/
				
				query = getModelManager().createSQLQuery("crm", "EXEC duplicidadeMorosidade " + aseg2.obterId());
				
				rows = query.execute();
				
				for(int i = 0 ; i < rows.length ; i++)
				{
					qtde = rows[i].getInt("qtde");
					superior = rows[i].getLong("superior");
					secao = rows[i].getLong("secao");
					numero_parcela = rows[i].getInt("numero_parcela");
					status_apolice = rows[i].getString("status_apolice");
					numero_endoso = rows[i].getDouble("numero_endoso");
					certificado = rows[i].getDouble("certificado");
					data_corte = rows[i].getLong("data_corte");
					
					/*System.out.println("Duplicada");
					System.out.println(qtde + " - " + aseg2.obterNome() + " - " + secao + " - " + numero_endoso + " - " + certificado + " - "+ superior + " - " + data_corte + " - " + status_apolice);*/
					
					query = getModelManager().createSQLQuery("crm", "EXEC duplicidadeMorosidade2 ?,?,?,?,?,?,?,?");
			        query.addLong(aseg2.obterId());
			        query.addLong(superior);
			        query.addString(status_apolice);
			        query.addLong(secao);
			        query.addLong(data_corte);
			        query.addDouble(numero_endoso);
			        query.addDouble(certificado);
			        query.addInt(numero_parcela);
					
			        rows2 = query.execute();
			        
			        for(int k = 0; k < rows2.length; k++)
			        {
			            id = rows2[k].getLong("id");
			            morosidade = (Morosidade) home.obterEventoPorId(id);
			            if(morosidade!=null)
			            {
				            //System.out.println(id + " - " + new SimpleDateFormat("dd/MM/yyyy").format(morosidade.obterCriacao()));
				            if(k>0)
				            	morosidade.excluir();
			            }
			        }
				}
				
				/*cont++;
				if(cont == 5)
					break;*/
			}
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			System.out.println("Erro na duplicidade registro morosidade".toUpperCase());
		}
	}
}