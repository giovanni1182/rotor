package com.gio.crm.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import infra.config.InfraProperties;
import infra.model.Home;
import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

public class EntidadeHomeImpl extends Home implements EntidadeHome {
	private HashMap entidadesPorId = new HashMap();

	private boolean contemPeloMenosUm(Collection collection1,
			Collection collection2) {
		if (collection1 == null || collection2 == null)
			return false;
		for (Iterator i = collection2.iterator(); i.hasNext();)
			if (collection1.contains(i.next()))
				return true;
		return false;
	}

	protected EntidadeImpl instanciarEntidade(long id) throws Exception {
		return this.instanciarEntidade(id, null);
	}

	 public EntidadeImpl instanciarEntidade(long id, String classe) throws Exception
	 {
		 EntidadeImpl entidade = (EntidadeImpl)entidadesPorId.get(new Long(id));
		 if(entidade == null)
		 {
			 if(classe == null)
			 {
				 /*SQLQuery query = getModelManager().createSQLQuery("crm", "select classe from entidade where id=?");
				 query.addLong(id);*/
				 SQLQuery query = getModelManager().createSQLQuery("crm", "EXEC obterEntidadePorId "+id);
		                
				 classe = query.executeAndGetFirstRow().getString("classe");
			 }
			 if(classe != null)
			 {
				 entidade = (EntidadeImpl)getModelManager().getEntity(classe.trim());
				 entidade.atribuirId(id);
				 entidadesPorId.put(new Long(id), entidade);
			 }
		 }
		 return entidade;
	 }

	protected Collection instanciarEntidades(SQLRow[] rows) throws Exception {
		UsuarioHome usuarioHome = (UsuarioHome) this.getModelManager().getHome(
				"UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this
				.getModelManager().getUser());
		ArrayList entidades = new ArrayList();

		for (int i = 0; i < rows.length; i++)
		{
			EntidadeImpl entidade = this.instanciarEntidade(rows[i].getLong("id"), rows[i].getString("classe"));
			boolean visivel = false;
			boolean regraAseguradora = false;

			// o usuario � o administrador
			if (usuarioAtual.obterId() == 1)
				visivel = true;

			// a entidade � o administrador
			else if (entidade.obterId() == 1)
				visivel = true;

			// a entidade � uma das superiores do usu�rio atual
			else if (usuarioAtual.obterSuperiores().contains(entidade))
				visivel = true;

			// o usu�rio atual � respons�vel pela entidade
			else if (entidade.obterResponsavel().equals(usuarioAtual))
				visivel = true;

			// a entidade � o usu�rio respons�vel pelo usu�rio atual
			else if (entidade.equals(usuarioAtual.obterResponsavel()))
				visivel = true;

			// a empresa do usu�rio � uma das superiores da entidade
			else if (entidade.obterSuperiores().contains(
					usuarioAtual.obterAseguradoraComoEmpresa())) {
				visivel = true;
				regraAseguradora = true;
			}

			else if (!regraAseguradora
					&& entidade.obterSuperiores().contains(
							usuarioAtual.obterEmpresa()))
				visivel = true;

			// a empresa da entidade � uma das superiores da usuario
			/*
			 * else if
			 * (usuarioAtual.obterSuperiores().contains(entidade.obterEmpresa()))
			 * visivel = true;
			 */
			else
				visivel = false;
			
			if(entidade instanceof Usuario)
			{
				Usuario usuario = (Usuario) entidade;
				//visivel = usuario.estaVisivel();
			}

			/*if (entidade.obterApelido() != null) 
			{
				if (entidade.obterApelido().equals("parametros")) 
				{
					Entidade icf = this.obterEntidadePorApelido("intendenteicf");
					Entidade informatica = this.obterEntidadePorApelido("informatica");
					
					if (usuarioAtual.obterId() == 1 || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR) || usuarioAtual.obterSuperiores().contains(icf) || usuarioAtual.obterSuperiores().contains(informatica))
						visivel = true;
					else
						visivel = false;
				}
			}*/

			if (visivel)
				entidades.add(entidade);
		}
		return entidades;
	}

	public Collection localizarEntidades(String pesquisa) throws Exception {
		return this.localizarEntidades(pesquisa, null);
	}

	/*
	 * public Collection localizarEntidades(String pesquisa, String classe)
	 * throws Exception { SQLQuery query; if (classe == null || classe.length() ==
	 * 0) { query = this.getModelManager().createSQLQuery("crm", "select id,
	 * classe from entidade where (nome like ? or apelido like ?)"); } else {
	 * query = this.getModelManager().createSQLQuery("crm", "select id, classe
	 * from entidade where classe=? and (nome like ? or apelido like ?)");
	 * query.addString(classe); } query.addString("%" + pesquisa + "%");
	 * query.addString("%" + pesquisa + "%"); return
	 * this.instanciarEntidades(query.execute()); }
	 */

	public Collection localizarEntidades(String pesquisa, String classe)
			throws Exception {
		SQLQuery query;
		if (classe == null || classe.length() == 0) {
			query = this
					.getModelManager()
					.createSQLQuery(
							"crm",
							"select id, classe from entidade where (nome like ? or apelido like ? or ruc like ? or sigla like ?)");
		} else {
			StringTokenizer st = new StringTokenizer(classe, ",");
			String s = "";
			int i = 0;
			while (st.hasMoreTokens()) {
				String m = st.nextToken();

				if (i == 0)
					s = "'" + m + "'";
				else
					s += " or classe=" + "'" + m + "'";

				i++;
			}

			query = this
					.getModelManager()
					.createSQLQuery(
							"crm",
							"select id, classe from entidade where (classe="
									+ s
									+ ") and (nome like ? or apelido like ? or ruc like ? or sigla like ?)");
		}

		query.addString("%" + pesquisa + "%");
		query.addString("%" + pesquisa + "%");
		query.addString("%" + pesquisa + "%");
		query.addString("%" + pesquisa + "%");

		return this.instanciarEntidades(query.execute());
	}

	public Collection localizarEntidadesEmpUsuPes(String pesquisa)
			throws Exception {
		SQLQuery query;
		query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select id, classe from entidade where (classe='empresa' or classe='usuario' or classe='pessoa') and (nome like ? or apelido like ?)");
		query.addString("%" + pesquisa + "%");
		query.addString("%" + pesquisa + "%");
		return this.instanciarEntidades(query.execute());
	}

	public Collection localizarEntidadesPorDestino(String pesquisa,
			String classe, long fornecedor) throws Exception {
		SQLQuery query;
		if (classe == null || classe.length() == 0) {
			query = this
					.getModelManager()
					.createSQLQuery("crm",
							"select id, classe from entidade where (nome like ? or apelido like ?)");
		} else {
			query = this
					.getModelManager()
					.createSQLQuery(
							"crm",
							"select entidade.id as id, entidade.classe as classe from entidade,produto_fornecedor where entidade.id=produto_fornecedor.id and produto_fornecedor.fornecedor=? and entidade.classe=? and (entidade.nome like ? or entidade.apelido like ?)");
			query.addLong(fornecedor);
			query.addString(classe);

		}
		query.addString("%" + pesquisa + "%");
		query.addString("%" + pesquisa + "%");
		return this.instanciarEntidades(query.execute());
	}

	public Collection obterClassesInferiores(Entidade entidade)
			throws Exception {
		ArrayList inferiores = new ArrayList();
		InfraProperties ip = InfraProperties.getInstance();
		StringTokenizer classes = new StringTokenizer(ip.getProperty(entidade
				.obterClasse().toLowerCase()
				+ ".inferiores"), ",");
		while (classes.hasMoreTokens())
			inferiores.add(classes.nextToken().toLowerCase());
		return inferiores;
	}

	public Entidade obterEntidadePorInscricao(String inscricaoStr)
			throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select evento.id from evento,inscricao where evento.id = inscricao.id and inscricao = ?");
		query.addString(inscricaoStr);

		//System.out.println("select evento.id from evento,inscricao where
		// evento.id = inscricao.id and inscricao = " + inscricaoStr);

		Entidade entidade = null;

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++) {
			long id = rows[i].getLong("id");

			EventoHome home = (EventoHome) this.getModelManager().getHome(
					"EventoHome");

			Inscricao inscricao2 = (Inscricao) home.obterEventoPorId(id);

			if (inscricao2.obterSituacao().equals("Vigente"))
				entidade = inscricao2.obterOrigem();
		}

		return entidade;
	}

	public String obterDescricaoClasse(String classe) throws Exception {
		return InfraProperties.getInstance().getProperty(classe + ".descricao");
	}

	public Collection obterAseguradoras() throws Exception
	{
		Map aseguradoras = new TreeMap();

		//SQLQuery query = this.getModelManager().createSQLQuery("crm","SELECT plano.id, evento.origem  FROM evento,entidade,plano where evento.id = plano.id and evento.origem=entidade.Id and evento.origem is not null");
		SQLQuery query = this.getModelManager().createSQLQuery("crm","SELECT entidade.id  FROM entidade where classe='Aseguradora' order by nome");

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++)
		{
			long id = rows[i].getLong("id");

			Aseguradora aseguradora = (Aseguradora) this.obterEntidadePorId(id);

			aseguradoras.put(aseguradora.obterNome(), aseguradora);
		}

		return aseguradoras.values();
	}

	public Entidade obterEntidadePorApelido(String apelido) throws Exception
	{
		//SQLQuery query = getModelManager().createSQLQuery("crm", "select id, classe from entidade where apelido=?");
		//query.addString(apelido);
		SQLQuery query = getModelManager().createSQLQuery("crm", "EXEC obterEntidadePorApelido '"+apelido+"'");
	        
		SQLRow rows[] = query.execute();
		if(rows.length > 0)
			return instanciarEntidade(rows[0].getLong("id"), rows[0].getString("classe"));
		else
			return null;
	}

	public Entidade obterEntidadePorEvento(Evento evento) throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select objeto from evento where id=?");
		query.addLong(evento.obterId());
		long id = query.executeAndGetFirstRow().getLong("objeto");
		if (id == 0)
			return null;
		else
			return this.obterEntidadePorId(id);
	}

	public Entidade obterEntidadePorId(long id) throws Exception {
		if (id == 0) {
			RaizHome home = (RaizHome) this.getModelManager().getHome(
					"RaizHome");
			return home.obterRaiz();
		} else {
			return this.instanciarEntidade(id);
		}
	}

	 public Entidade obterEntidadePorSigla(String sigla) throws Exception
	 {
		 //SQLQuery query = getModelManager().createSQLQuery("crm", "select id from entidade where sigla=?");
		 SQLQuery query = getModelManager().createSQLQuery("crm", "EXEC obterEntidadePorSigla '" + sigla +"'");
		 long id = query.executeAndGetFirstRow().getLong("id");
		 if(id == 0L)
			 return null;
		 else
			 return obterEntidadePorId(id);
	 }

	public Collection obterEntidades() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select id, classe from entidade order by nome");
		return this.instanciarEntidades(query.execute());
	}

	public Collection obterEntidadesInferiores(Entidade entidade) throws Exception
	{
		if (entidade instanceof Raiz)
		{
			return this.obterEntidadesPorRaiz();
		}
		else
		{
			SQLQuery query = this.getModelManager().createSQLQuery("crm", "select id, classe from entidade where superior=? order by nome");
			query.addLong(entidade.obterId());
			return this.instanciarEntidades(query.execute());
		}
	}

	public Collection obterEntidadesPorRaiz() throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm", "select id, classe from entidade where superior=0");
		return this.instanciarEntidades(query.execute());
	}

	public Collection obterEntidadesPorResponsavel(Usuario usuario)
			throws Exception {
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery("crm",
						"select id, classe from entidade where responsavel=? order by nome");
		query.addLong(usuario.obterId());
		return this.instanciarEntidades(query.execute());
	}

	public Collection<Entidade> obterEntidadesSuperiores(Entidade entidade)	throws Exception 
	{
		ArrayList<Entidade> superiores = new ArrayList<>();
		Entidade s = entidade.obterSuperior();
		while (s != null) 
		{
			superiores.add(s);
			s = s.obterSuperior();
		}
		return superiores;
	}

	public Entidade obterEntidadeSuperior(Entidade entidade) throws Exception {
		if (entidade.obterId() == 0) {
			return null;
		} else {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select superior from entidade where id=?");
			query.addLong(entidade.obterId());
			return this.obterEntidadePorId(query.executeAndGetFirstRow()
					.getLong("superior"));
		}
	}

	public long obterNumeroEntidadesInferiores(Entidade entidade) throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
						"select top 1 entidade.id as quantidade from entidade where superior=?");
		query.addLong(entidade.obterId());
		return query.executeAndGetFirstRow().getLong("quantidade");
	}

	public Collection obterPossiveisSuperiores(Entidade entidade)
			throws Exception {
		ArrayList possiveisSuperiores = new ArrayList();
		UsuarioHome usuarioHome = (UsuarioHome) this.getModelManager().getHome(
				"UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this
				.getModelManager().getUser());
		EntidadeHome entidadeHome = (EntidadeHome) this.getModelManager()
				.getHome("EntidadeHome");
		Collection responsabilidades = entidadeHome
				.obterEntidadesPorResponsavel(usuarioAtual);
		if (usuarioAtual.obterId() == 1) {
			RaizHome raizHome = (RaizHome) this.getModelManager().getHome(
					"RaizHome");
			responsabilidades.add(raizHome.obterRaiz());
		}
		for (Iterator i = responsabilidades.iterator(); i.hasNext();) {
			Entidade e = (Entidade) i.next();
			if (entidade == null) {
				if (!this.obterClassesInferiores(e).isEmpty())
					possiveisSuperiores.add(e);
			} else {
				if (!e.equals(entidade))
					if (this.obterClassesInferiores(e).contains(
							entidade.obterClasse().toLowerCase()))
						if (!e.obterSuperiores().contains(entidade))
							possiveisSuperiores.add(e);
			}
		}
		return possiveisSuperiores;
	}

	public Collection obterPossiveisSuperioresParaCadastro(Entidade entidade)
			throws Exception {
		ArrayList possiveisSuperiores = new ArrayList();
		UsuarioHome usuarioHome = (UsuarioHome) this.getModelManager().getHome(
				"UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this
				.getModelManager().getUser());
		EntidadeHome entidadeHome = (EntidadeHome) this.getModelManager()
				.getHome("EntidadeHome");
		Collection responsabilidades = entidadeHome
				.obterEntidadesPorResponsavel(usuarioAtual);
		if (usuarioAtual.obterId() == 1) {
			RaizHome raizHome = (RaizHome) this.getModelManager().getHome(
					"RaizHome");
			responsabilidades.add(raizHome.obterRaiz());
		}
		for (Iterator i = responsabilidades.iterator(); i.hasNext();) {
			Entidade e = (Entidade) i.next();
			if (e.obterClasse().toLowerCase().equals("empresa")
					|| e.obterClasse().toLowerCase().equals("departamento")) {
				if (entidade == null) {
					if (!this.obterClassesInferiores(e).isEmpty())
						possiveisSuperiores.add(e);
				} else {
					if (!e.equals(entidade))
						if (this.obterClassesInferiores(e).contains(
								entidade.obterClasse().toLowerCase()))
							if (!e.obterSuperiores().contains(entidade))
								possiveisSuperiores.add(e);
				}
			}
		}
		return possiveisSuperiores;
	}

	public Map obterTiposEntidade() throws Exception {
		Map tipos = new HashMap();
		InfraProperties ip = InfraProperties.getInstance();
		StringTokenizer classes = new StringTokenizer(ip
				.getProperty("entidades"), ",");
		while (classes.hasMoreTokens()) {
			String classe = classes.nextToken().trim();
			String descricao = ip.getProperty(classe + ".descricao");
			tipos.put(classe, descricao);
		}
		return tipos;
	}

	public boolean possuiEntidadesInferiores(Entidade entidade)
			throws Exception {
		return this.obterNumeroEntidadesInferiores(entidade) > 0;
	}

	public boolean possuiEventosVinculados(Entidade entidade) throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select TOP 1 evento.id from evento where origem=? or responsavel=? or criador=?");
						//"select count(id) as MX from evento where origem=? or responsavel=? or criador=?");
		query.addLong(entidade.obterId());
		query.addLong(entidade.obterId());
		query.addLong(entidade.obterId());
		long q = query.executeAndGetFirstRow().getLong("id");
		return q > 0;
		
	}

	public Map obterAseguradoras(Entidade entidade, Date dataInicio,Date dataFim) throws Exception 
	{
		Map entidades = new TreeMap();

		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");

		SQLQuery query = this.getModelManager().createSQLQuery("crm",
						"select origem, tipo_contrato from evento, dados_reaseguro where evento.id = dados_reaseguro.id and reaseguradora = ? and data_ini_apo >= ? and data_fim_apo <= ? group by origem, tipo_contrato");
		query.addLong(entidade.obterId());
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());

		SQLRow[] rows = query.execute();
		
		//System.out.println("select origem, reaseguradora, tipo_contrato from evento, dados_reaseguro where evento.id = dados_reaseguro.id and reaseguradora = "+entidade.obterId()+" and data_prevista_inicio >= "+dataInicio.getTime()+" and data_prevista_conclusao <= "+dataFim.getTime()+" group by origem, reaseguradora,tipo_contrato");

		for (int i = 0; i < rows.length; i++) 
		{
			long id = rows[i].getLong("origem");

			String tipoContrato = rows[i].getString("tipo_contrato");

			if (id > 0) 
			{
				Entidade origem = (Entidade) home.obterEntidadePorId(id);
				
				entidades.put(origem.obterNome() + "_" + tipoContrato, origem.obterId() + "_" + tipoContrato);
			}
		}

		return entidades;
	}
	
	public Map<String, String> obterReaseguros(Entidade entidade, Date dataInicio,Date dataFim) throws Exception 
	{
		Map<String, String> entidades = new TreeMap<String, String>();

		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");

		String sql = "select origem, tipo_contrato, SUM(caiptal_gs) as capital, SUM(prima_gs) as prima, SUM(comissao_gs) as comissao from evento, dados_reaseguro where evento.id = dados_reaseguro.id and reaseguradora = "+entidade.obterId()+" and data_ini_apo >= "+dataInicio.getTime()+" and data_fim_apo <= "+dataFim.getTime()+" group by origem, tipo_contrato";
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		
		SQLRow[] rows = query.execute();
		
		for (int i = 0; i < rows.length; i++) 
		{
			long id = rows[i].getLong("origem");
			String tipoContrato = rows[i].getString("tipo_contrato");
			double capital = rows[i].getDouble("capital");
			double prima = rows[i].getDouble("prima");
			double comissao = rows[i].getDouble("comissao");

			if (id > 0) 
			{
				Entidade origem = (Entidade) home.obterEntidadePorId(id);
				
				entidades.put(origem.obterNome() + "_" + tipoContrato, id + "_" + tipoContrato +"_"+capital+"_"+prima+"_"+comissao);
			}
		}

		return entidades;
	}

	public Map obterAseguradorasPorCorretora(Entidade entidade,	Date dataInicio, Date dataFim) throws Exception 
	{
		Map entidades = new TreeMap();

		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");

		SQLQuery query = this.getModelManager().createSQLQuery("crm",
						"select origem, tipo_contrato from evento, dados_reaseguro where evento.id = dados_reaseguro.id and corretora = ? and data_prevista_inicio >= ? and data_prevista_conclusao <= ? group by origem,tipo_contrato");
		query.addLong(entidade.obterId());
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());

		SQLRow[] rows = query.execute();
		
		System.out.println("select origem, tipo_contrato from evento, dados_reaseguro where evento.id = dados_reaseguro.id and corretora = "+entidade.obterId()+" and data_prevista_inicio >= "+dataInicio.getTime()+" and data_prevista_inicio <= "+dataFim.getTime()+" group by origem, tipo_contrato");

		for (int i = 0; i < rows.length; i++) 
		{
			long id = rows[i].getLong("origem");

			String tipoContrato = rows[i].getString("tipo_contrato");

			if (id > 0) 
			{
				Entidade origem = (Entidade) home.obterEntidadePorId(id);

				entidades.put(origem.obterNome() + "_" + tipoContrato, origem.obterId() + "_" + tipoContrato);
			}
		}

		return entidades;
	}
	
	public void gravarCIRUC(String tipoDoc, String numero, String tipoPessoa) throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select MAX(id) as mx from ci_ruc");
		long id = query.executeAndGetFirstRow().getLong("mx") + 1;
		
		SQLUpdate insert = this.getModelManager().createSQLUpdate("crm","insert into ci_ruc values(?,?,?,?)");
		insert.addLong(id);
		insert.addString(tipoDoc);
		insert.addString(numero);
		insert.addString(tipoPessoa);
		
		insert.execute();
	}

	public void excluirCIRUC() throws Exception 
	{
		SQLUpdate delete = this.getModelManager().createSQLUpdate("crm","delete from ci_ruc");
		
		delete.execute();
	}
	
	public boolean existeDocumento(String tipoDocumento, String numero,String tipoPessoa) throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select id from ci_ruc where tipo_doc = ? and numero = ? and tipo_pessoa = ?");
		query.addString(tipoDocumento.trim());
		query.addString(numero.trim());
		query.addString(tipoPessoa.trim());
		
		if(query.execute().length > 0)
			return true;
		else
			return false;
	}

	public void limparSujeiraBDEntidades() throws Exception
	{
		SQLUpdate delete = this.getModelManager().createSQLUpdate("crm","delete from entidade_atributo where entidade = 0");
		delete.execute();
		
		SQLUpdate delete2 = this.getModelManager().createSQLUpdate("crm","delete from entidade_contato where entidade = 0");
		delete2.execute();
		
		SQLUpdate delete3 = this.getModelManager().createSQLUpdate("crm","delete from entidade_endereco where entidade = 0");
		delete3.execute();
	}
	
	public void manutBase() throws Exception
	{
		PlanoHome home = (PlanoHome) getModelManager().getHome("PlanoHome");
		
		home.manutPlano();
	}
	
	 public Entidade obterEntidadePorInscricao(String inscricaoStr,String mesAno) throws Exception
	 {
		 return obterEntidadePorInscricao(inscricaoStr, null,mesAno);
	 }
	 
	 public Entidade obterEntidadePorInscricao(String inscricaoStr, String classe,String mesAno) throws Exception
	 {
		 Entidade entidade = null;
		 if(classe == null)
		 {
			 SQLQuery query = getModelManager().createSQLQuery("crm", "EXEC obterEntidadePorInscricao '"+inscricaoStr+"','"+classe+"'");
	        	
			 if(query.execute().length > 0)
			 {
				 long id = query.executeAndGetFirstRow().getLong("id");
				 if(id > 0L)
				 {
					 EventoHome home = (EventoHome)getModelManager().getHome("EventoHome");
					 Inscricao inscricao2 = (Inscricao)home.obterEventoPorId(id);
					 entidade = inscricao2.obterOrigem();
				 }
			 }
			 else
			 {
				 if(mesAno!=null)
				 {
					 SQLQuery query2 = getModelManager().createSQLQuery("crm", "EXEC obterEntidadePorInscricaoData '"+inscricaoStr+"','"+classe+"'");
	                	
					 SQLRow[] rows = query2.execute();
		                
					 Date mesAnoMovimentoAgenda = new SimpleDateFormat("MM/yyyy").parse(mesAno);
		                
					 for(int i = 0 ; i < rows.length ; i++)
					 {
						 Date dataVencimento = new Date(rows[i].getLong("data"));
						 String mesAnoVencimentoStr = new SimpleDateFormat("MM/yyyy").format(dataVencimento);
						 Date mesAnoMovimentoInscricao = new SimpleDateFormat("MM/yyyy").parse(mesAnoVencimentoStr);
				                
						 if(mesAnoMovimentoAgenda.compareTo(mesAnoMovimentoInscricao)<=0)
						 {
							 EventoHome home = (EventoHome)getModelManager().getHome("EventoHome");
							 Inscricao inscricao2 = (Inscricao)home.obterEventoPorId(rows[i].getLong("id"));
							 entidade = inscricao2.obterOrigem();
							 break;
						 }
					 }
				 }
			 }
		 } 
		 else
		 {
			 SQLQuery query = getModelManager().createSQLQuery("crm", "EXEC obterEntidadePorInscricao '"+inscricaoStr+"','"+classe+"'");
	            
			 if(query.execute().length > 0)
			 {
				 long id = query.executeAndGetFirstRow().getLong("id");
				 if(id > 0L)
				 {
					 EventoHome home = (EventoHome)getModelManager().getHome("EventoHome");
					 Inscricao inscricao2 = (Inscricao)home.obterEventoPorId(id);
					 entidade = inscricao2.obterOrigem();
				 }
			 }
			 else
			 {
				 if(mesAno!=null)
				 {
					 SQLQuery query2 = getModelManager().createSQLQuery("crm", "EXEC obterEntidadePorInscricaoData '"+inscricaoStr+"','"+classe+"'");
		                
					 SQLRow[] rows = query2.execute();
		                
					 Date mesAnoMovimentoAgenda = new SimpleDateFormat("MM/yyyy").parse(mesAno);
		                
					 for(int i = 0 ; i < rows.length ; i++)
					 {
						 Date dataVencimento = new Date(rows[i].getLong("data"));
						 String mesAnoVencimentoStr = new SimpleDateFormat("MM/yyyy").format(dataVencimento);
						 Date mesAnoMovimentoInscricao = new SimpleDateFormat("MM/yyyy").parse(mesAnoVencimentoStr);
				                
						 if(mesAnoMovimentoAgenda.compareTo(mesAnoMovimentoInscricao)<=0)
						 {
							 EventoHome home = (EventoHome)getModelManager().getHome("EventoHome");
							 Inscricao inscricao2 = (Inscricao)home.obterEventoPorId(rows[i].getLong("id"));
							 entidade = inscricao2.obterOrigem();
							 break;
						 }
					 }
				 }
			 }
		 }
		 return entidade;
	 }
	 
	 public Entidade obterEntidadePorInscricaoSemValidacao(String inscricao, String tipo) throws Exception
	 {
		 EntidadeHome home = (EntidadeHome) getModelManager().getHome("EntidadeHome");
		 Entidade auxliar = null;
	        
		 SQLQuery query = getModelManager().createSQLQuery("crm", "EXEC obterEntidadePorInscricaoSemValidacao '"+inscricao+"', '"+tipo+"'");
	        
		 SQLRow rows[] = query.execute();
		 long id;
		 
		 for(int i = 0; i < rows.length; i++)
		 {
			 id = rows[i].getLong("origem");
	                
			 auxliar = home.obterEntidadePorId(id);
		 }
		 return auxliar;
	 }
}