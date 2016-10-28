// FrontEnd Plus GUI for JAD
// DeCompiled : EventoImpl.class

package com.gio.crm.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import infra.config.InfraProperties;
import infra.model.Entity;
import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

// Referenced classes of package com.gio.crm.model:
//            Evento, Entidade, Usuario, UsuarioHome, 
//            EntidadeHome, EventoHome

public abstract class EventoImpl extends Entity
    implements Evento
{
    public class ComentarioImpl
        implements Evento.Comentario
    {

        private String comentario;
        private Date criacao;
        private EventoImpl evento;
        private String titulo;

        public String obterComentario()
        {
            return comentario;
        }

        public Date obterCriacao()
        {
            return criacao;
        }

        public String obterTitulo()
        {
            return titulo;
        }

        public ComentarioImpl(EventoImpl evento, Date criacao, String titulo, String comentario)
        {
            this.evento = evento;
            this.criacao = criacao;
            this.titulo = titulo;
            this.comentario = comentario;
        }
    }

    public class FaseImpl
        implements Evento.Fase
    {

        private String codigo;
        private EventoImpl evento;
        private Date inicio;
        private String nome;
        private Date termino;

        public boolean equals(Object object)
        {
            if(object instanceof Evento.Fase)
                return obterCodigo().equals(((Evento.Fase)object).obterCodigo());
            if(object instanceof String)
                return obterCodigo().equals((String)object);
            else
                return false;
        }

        public String obterCodigo()
        {
            return codigo.trim();
        }

        public Collection obterFasesAnteriores()
            throws Exception
        {
            SQLQuery query = evento.getModelManager().createSQLQuery("crm", "select * from fase where id=? and termino>0");
            query.addLong(evento.obterId());
            SQLRow rows[] = query.execute();
            ArrayList fases = new ArrayList();
            for(int i = 0; i < rows.length; i++)
                fases.add(new FaseImpl(evento, rows[i].getString("codigo"), new Date(rows[i].getLong("inicio")), new Date(rows[i].getLong("termino"))));

            return fases;
        }

        public Date obterInicio()
        {
            return inicio;
        }

        public String obterNome()
            throws Exception
        {
            return InfraProperties.getInstance().getProperty("fase." + codigo.trim() + ".nome");
        }

        public Collection obterProximasFases()
            throws Exception
        {
            InfraProperties ip = InfraProperties.getInstance();
            StringTokenizer st = new StringTokenizer(ip.getProperty(evento.obterClasse() + "." + nome.toLowerCase()), ",");
            ArrayList fases = new ArrayList();
            for(; st.hasMoreTokens(); fases.add(st.nextToken()));
            return fases;
        }

        public Date obterTermino()
        {
            return termino;
        }

        public FaseImpl(EventoImpl evento, String codigo)
        {
            this.evento = evento;
            this.codigo = codigo;
            inicio = new Date();
        }

        public FaseImpl(EventoImpl evento, String codigo, Date inicio, Date termino)
        {
            this.evento = evento;
            this.codigo = codigo;
            this.inicio = inicio;
            this.termino = termino;
        }
    }


    private Date atualizacao;
    private String classe;
    private String classeDescricao;
    private Collection comentarios;
    private Date criacao;
    private Usuario criador;
    private Date dataPrevistaConclusao;
    private Date dataPrevistaInicio;
    private String descricao;
    private Entidade destino;
    private Long duracao;
    private Evento.Fase fase;
    private long id;
    private Boolean lido;
    private Entidade origem;
    private Integer prioridade;
    private Integer quantidadeComentarios;
    private Entidade responsavel;
    private Entidade responsavelAnterior;
    private Evento superior;
    private Collection superiores;
    private String tipo;
    private String titulo;
    private long ordem;
    private Collection classesOrdem;

    public EventoImpl()
    {
    }

    public void adicionarComentario(String titulo, String comentario)
        throws Exception
    {
        SQLUpdate update1 = getModelManager().createSQLUpdate("crm","insert into comentario (id, criacao, titulo, comentario) values (?, ?, ?, ?)");
        update1.addLong(obterId());
        update1.addLong((new Date()).getTime());
        update1.addString(titulo);
        update1.addString(comentario);
        update1.execute();
        SQLUpdate update2 = getModelManager().createSQLUpdate("crm","update evento set atualizacao=? where id=?");
        update2.addLong((new Date()).getTime());
        update2.addLong(obterId());
        update2.execute();
    }

    public void atribuirDataPrevistaConclusao(Date dataPrevistaConclusao)
        throws Exception
    {
        this.dataPrevistaConclusao = dataPrevistaConclusao;
    }

    public void atribuirDataPrevistaInicio(Date dataPrevistaInicio) throws Exception
    {
        this.dataPrevistaInicio = dataPrevistaInicio;
    }

    public void atribuirDescricao(String descricao)
        throws Exception
    {
        this.descricao = descricao;
    }

    public void atribuirDestino(Entidade destino)
        throws Exception
    {
        this.destino = destino;
    }

    public void atribuirDuracao(Long duracao)
        throws Exception
    {
        this.duracao = duracao;
    }

    public void atribuirId(long id)
        throws Exception
    {
        this.id = id;
    }

    public void atribuirOrigem(Entidade origem)
        throws Exception
    {
        this.origem = origem;
    }

    public void atribuirPrioridade(int prioridade)
        throws Exception
    {
        this.prioridade = new Integer(prioridade);
    }

    public void atribuirResponsavel(Entidade responsavel)
        throws Exception
    {
        this.responsavel = responsavel;
    }

    public void atribuirSuperior(Evento superior)
        throws Exception
    {
        this.superior = superior;
    }

    public void atribuirTipo(String tipo)
        throws Exception
    {
        this.tipo = tipo;
    }

    public void atribuirTitulo(String titulo)
        throws Exception
    {
        this.titulo = titulo;
    }

    public void atualizarComoLido()
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("update evento set lido=1 where id=?");
        update.addLong(obterId());
        update.execute();
        lido = Boolean.TRUE;
    }

    public void atualizarComoNaoLido()
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("update evento set lido=0 where id=?");
        update.addLong(obterId());
        update.execute();
        lido = Boolean.FALSE;
    }

    public void atualizarDataPrevistaConclusao(Date dataPrevistaConclusao)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("update evento set data_prevista_conclusao=? where id=?");
        if(dataPrevistaConclusao == null)
            update.addLong(null);
        else
            update.addLong(dataPrevistaConclusao.getTime());
        update.addLong(id);
        update.execute();
        this.dataPrevistaConclusao = dataPrevistaConclusao;
    }
    
    public void atualizarDataPrevistaConclusao2(Date dataPrevistaConclusao)
    throws Exception
{
    SQLUpdate update = getModelManager().createSQLUpdate("crm", "update evento set data_prevista_conclusao=? where id=?");
    if(dataPrevistaConclusao == null)
        update.addLong(null);
    else
        update.addLong(dataPrevistaConclusao.getTime());
    update.addLong(id);
    update.execute();
    this.dataPrevistaConclusao = dataPrevistaConclusao;
}

    public void atualizarDataPrevistaInicio(Date dataPrevistaInicio) throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("update evento set data_prevista_inicio=? where id=?");
        if(dataPrevistaInicio == null)
            update.addLong(null);
        else
            update.addLong(dataPrevistaInicio.getTime());
        update.addLong(id);
        update.execute();
        this.dataPrevistaInicio = dataPrevistaInicio;
    }
    
    public void atualizarDataPrevistaInicio2(Date dataPrevistaInicio) throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update evento set data_prevista_inicio=? where id=?");
        if(dataPrevistaInicio == null)
            update.addLong(null);
        else
            update.addLong(dataPrevistaInicio.getTime());
        update.addLong(id);
        update.execute();
        this.dataPrevistaInicio = dataPrevistaInicio;
    }

    public void atualizarDescricao(String descricao)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("update evento set descricao=? where id=?");
        update.addString(descricao);
        update.addLong(id);
        update.execute();
        this.descricao = descricao;
    }
    
    public void atualizarDescricao2(String descricao)
    throws Exception
{
    SQLUpdate update = getModelManager().createSQLUpdate("crm", "update evento set descricao=? where id=?");
    update.addString(descricao);
    update.addLong(id);
    update.execute();
    this.descricao = descricao;
}
    

    public void atualizarDestino(Entidade destino)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("update evento set destino=? where id=?");
        update.addLong(destino.obterId());
        update.addLong(id);
        update.execute();
        this.destino = destino;
    }

    public void atualizarDuracao(Long duracao)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("update evento set duracao=? where id=?");
        update.addLong(duracao);
        update.addLong(id);
        update.execute();
        this.duracao = duracao;
    }

    public void atualizarFase(String codigo)
        throws Exception
    {
        Date data = new Date();
        SQLUpdate update1 = this.getModelManager().createSQLUpdate("update fase set termino=? where id=? and termino=0");
        update1.addLong(data.getTime());
        update1.addLong(this.obterId());
        update1.execute();
        
        SQLUpdate update2 = this.getModelManager().createSQLUpdate("insert into fase (id, codigo, inicio, termino) values (?, ?, ?, 0)");
        update2.addLong(this.obterId());
        update2.addString(codigo);
        update2.addLong(data.getTime());
        update2.execute();
    }
    
    public void atualizarFase2(String codigo)
    throws Exception
{
    Date data = new Date();
    SQLUpdate update1 = getModelManager().createSQLUpdate("crm", "update fase set termino=? where id=? and termino=0");
    update1.addLong(data.getTime());
    update1.addLong(obterId());
    update1.execute();
    SQLUpdate update2 = getModelManager().createSQLUpdate("crm", "insert into fase (id, codigo, inicio, termino) values (?, ?, ?, 0)");
    update2.addLong(obterId());
    update2.addString(codigo);
    update2.addLong(data.getTime());
    update2.execute();
}

    public void atualizarOrigem(Entidade origem)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("update evento set origem=? where id=?");
        update.addLong(origem.obterId());
        update.addLong(id);
        update.execute();
        this.origem = origem;
    }
    
    public void atualizarOrigem2(Entidade origem)
    throws Exception
{
    SQLUpdate update = getModelManager().createSQLUpdate("crm", "update evento set origem=? where id=?");
    update.addLong(origem.obterId());
    update.addLong(id);
    update.execute();
    this.origem = origem;
}

    public void atualizarPrioridade(int prioridade)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("update evento set prioridade=? where id=?");
        update.addInt(prioridade);
        update.addLong(id);
        update.execute();
        this.prioridade = new Integer(prioridade);
    }
    
    public void atualizarPrioridade2(int prioridade)
    throws Exception
{
    SQLUpdate update = getModelManager().createSQLUpdate("crm", "update evento set prioridade=? where id=?");
    update.addInt(prioridade);
    update.addLong(id);
    update.execute();
    this.prioridade = new Integer(prioridade);
}

    public void atualizarResponsavel(Entidade responsavel)
        throws Exception
    {
        Entidade responsavelAtual = obterResponsavel();
        if(responsavelAtual != null)
            if(responsavelAtual instanceof Usuario)
            {
                SQLUpdate update = getModelManager().createSQLUpdate("update evento set responsavel_anterior=?, responsavel=? where id=?");
                update.addLong(responsavelAtual.obterId());
                update.addLong(responsavel.obterId());
                update.addLong(id);
                update.execute();
                atualizarComoNaoLido();
                this.responsavel = responsavel;
                responsavelAnterior = responsavelAtual;
            } else
            {
                SQLUpdate update = getModelManager().createSQLUpdate("update evento set responsavel=? where id=?");
                update.addLong(responsavel.obterId());
                update.addLong(id);
                update.execute();
                this.responsavel = responsavel;
            }
    }
    
    public void atualizarResponsavel2(Entidade responsavel)
    throws Exception
{
    Entidade responsavelAtual = obterResponsavel();
    if(responsavelAtual != null)
        if(responsavelAtual instanceof Usuario)
        {
            SQLUpdate update = getModelManager().createSQLUpdate("crm", "update evento set responsavel_anterior=?, responsavel=? where id=?");
            update.addLong(responsavelAtual.obterId());
            update.addLong(responsavel.obterId());
            update.addLong(id);
            update.execute();
            atualizarComoNaoLido();
            this.responsavel = responsavel;
            responsavelAnterior = responsavelAtual;
        } else
        {
            SQLUpdate update = getModelManager().createSQLUpdate("crm", "update evento set responsavel=? where id=?");
            update.addLong(responsavel.obterId());
            update.addLong(id);
            update.execute();
            this.responsavel = responsavel;
        }
}

    public void atualizarSuperior(Evento superior)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("update evento set superior=? where id=?");
        if(superior == null)
            update.addLong(null);
        else
            update.addLong(superior.obterId());
        update.addLong(id);
        update.execute();
        this.superior = superior;
    }
    
    public void atualizarSuperior2(Evento superior)throws Exception
    {
	    SQLUpdate update = getModelManager().createSQLUpdate("crm", "update evento set superior=? where id=?");
	    if(superior == null)
	        update.addLong(null);
	    else
	        update.addLong(superior.obterId());
	    update.addLong(id);
	    update.execute();
	    this.superior = superior;
    }

    public void atualizarTipo(String tipo)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("update evento set tipo=? where id=?");
        update.addString(tipo);
        update.addLong(id);
        update.execute();
        this.tipo = tipo;
    }
    
    public void atualizarTipo2(String tipo)
    throws Exception
{
    SQLUpdate update = getModelManager().createSQLUpdate("crm", "update evento set tipo=? where id=?");
    update.addString(tipo);
    update.addLong(id);
    update.execute();
    this.tipo = tipo;
}

    public void atualizarTitulo(String titulo)
        throws Exception
    {
        if(titulo == null || titulo.equals(""))
        {
            throw new Exception("O t\355tulo deve ser preenchido");
        } else
        {
            SQLUpdate update = getModelManager().createSQLUpdate("update evento set titulo=? where id=?");
            update.addString(titulo);
            update.addLong(id);
            update.execute();
            this.titulo = titulo;
            return;
        }
    }
    
    public void atualizarTitulo2(String titulo)
    throws Exception
{
    if(titulo == null || titulo.equals(""))
    {
        throw new Exception("O t\355tulo deve ser preenchido");
    } else
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update evento set titulo=? where id=?");
        update.addString(titulo);
        update.addLong(id);
        update.execute();
        this.titulo = titulo;
        return;
    }
}

    public void calcularPrevisoes()
        throws Exception
    {
    }

    public void concluir(String comentario)
        throws Exception
    {
        if(comentario != null)
            adicionarComentario("Concluido por " + obterUsuarioAtual().obterNome(), comentario);
        if(obterSuperior() == null)
        {
            if(obterUsuarioAtual().equals(obterCriador()))
                atualizarFase("concluido");
            else
                atualizarResponsavel(obterCriador());
        } else
        {
            atualizarFase("concluido");
            boolean concluirSuperior = true;
            for(Iterator i = obterSuperior().obterInferiores().iterator(); i.hasNext();)
            {
                Evento e = (Evento)i.next();
                if(!e.obterFase().obterCodigo().equals("concluido"))
                {
                    concluirSuperior = false;
                    break;
                }
            }

            if(concluirSuperior)
                obterSuperior().concluir("Concluido automaticamente");
        }
    }

    public void encaminhar(Entidade responsavel, String comentario)
        throws Exception
    {
        adicionarComentario("Encaminado por " + obterUsuarioAtual().obterNome() + " para " + responsavel.obterNome(), comentario);
        atualizarResponsavel(responsavel);
    }

    public boolean equals(Object object)
    {
        if(object instanceof Evento)
            return ((Evento)object).obterId() == id;
        else
            return false;
    }

    public void excluir()throws Exception
    {
    	SampleModelManager mm = new SampleModelManager();
    	
    	for(Evento e : this.obterInferiores())
    		e.excluir();
    	
    	StringTokenizer tabelas = new StringTokenizer(InfraProperties.getInstance().getProperty("eventos.excluir"), ",");
    	SQLUpdate update;
    	String nomeTabela;
    	
		while (tabelas.hasMoreTokens()) 
		{
			nomeTabela = tabelas.nextToken();
			//SQLUpdate update1 = mm.createSQLUpdate("crm","delete from " + nomeTabela + " where id=?");
			update = mm.createSQLUpdate("crm","EXEC excluirEvento ?,?");
			update.addString(nomeTabela);
			update.addLong(this.obterId());
			
			update.execute();
		}

		update = mm.createSQLUpdate("crm","delete from uploaded_file where id_evento=?");
		update.addLong(this.obterId());
		update.execute();

		update = mm.createSQLUpdate("crm","delete from uploaded_file_content where id_evento=?");
		update.addLong(this.obterId());
		update.execute();

		update = mm.createSQLUpdate("crm","delete from evento_entidades where sub_evento=?");
		update.addLong(this.obterId());
		update.execute();

		//update = mm.createSQLUpdate("crm","delete from comentario where id=?");
		update = mm.createSQLUpdate("crm","EXEC excluirEvento ?,?");
		update.addString("comentario");
		update.addLong(this.obterId());
		update.execute();

		//update = mm.createSQLUpdate("crm","delete from fase where id=?");
		update = mm.createSQLUpdate("crm","EXEC excluirEvento ?,?");
		update.addString("fase");
		update.addLong(this.obterId());
		update.execute();
		
		//update = mm.createSQLUpdate("crm","delete from evento where id=?");
		update = mm.createSQLUpdate("crm","EXEC excluirEvento ?,?");
		update.addString("evento");
		update.addLong(this.obterId());
		update.execute();
		
		
    }
    
    public void excluir2()throws Exception
    {
        for(Iterator i = obterInferiores().iterator(); i.hasNext(); ((Evento)i.next()).excluir());
        SQLUpdate update1;
        for(StringTokenizer tabelas = new StringTokenizer(InfraProperties.getInstance().getProperty("eventos.excluir"), ","); tabelas.hasMoreTokens(); update1.execute())
        {
            String nomeTabela = tabelas.nextToken();
            update1 = getModelManager().createSQLUpdate("crm","delete from " + nomeTabela + " where id=?");
            update1.addLong(obterId());
        }

        SQLUpdate update = getModelManager().createSQLUpdate("crm","delete from uploaded_file where id_evento=?");
        update.addLong(obterId());
        update.execute();
        SQLUpdate update2 = getModelManager().createSQLUpdate("crm","delete from uploaded_file_content where id_evento=?");
        update2.addLong(obterId());
        update2.execute();
    }

    public boolean foiLido()
        throws Exception
    {
        if(this.lido == null)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select lido from evento where id=?");
            query.addLong(obterId());
            String lido = query.executeAndGetFirstRow().getString("lido");
            if(lido != null)
                this.lido = new Boolean(lido.equals("1"));
            else
                this.lido = Boolean.FALSE;
        }
        return this.lido.booleanValue();
    }

    private UsuarioHome usuarioHome;
    private Usuario usuarioAtual;
    
    public void incluir() throws Exception
    {
    	synchronized(this)
    	{
    		if(titulo == null || titulo.equals(""))
    			throw new Exception("O t\355tulo do evento deve ser preenchido");
    		if(prioridade == null)
    			prioridade = new Integer(2);
    	        
    	        /*SQLQuery query = getModelManager().createSQLQuery("crm","select max(id) as maximo from evento");
    	        atribuirId(query.executeAndGetFirstRow().getLong("maximo") + 1L);*/
    	        
    		SQLQuery query = getModelManager().createSQLQuery("crm","EXEC obterMaxIdEvento");
    		atribuirId(query.executeAndGetFirstRow().getLong("maximo") + 1);
    	        
    		if(usuarioAtual == null)
    		{
    			usuarioHome = (UsuarioHome)getModelManager().getHome("UsuarioHome");
    			usuarioAtual = usuarioHome.obterUsuarioPorChave("admin");
    		}
    	        
    		SQLUpdate insert = getModelManager().createSQLUpdate("crm","EXEC incluirEvento ?,?,?,?,?,?,?,?,?,?,?,?,?");
    		insert.addLong(obterId());
    		insert.addString(getClassAlias());
    		insert.addLong(usuarioAtual.obterId());
    		insert.addLong((new Date()).getTime());
    		insert.addString(titulo);
    		if(this.superior!=null)
    			insert.addLong(this.superior.obterId());
    		else
    			insert.addLong(0);
    		if(dataPrevistaInicio != null)
    			insert.addLong(dataPrevistaInicio.getTime());
    		else
    			insert.addLong(0);
    		if(dataPrevistaConclusao != null)
    			insert.addLong(dataPrevistaConclusao.getTime());
    		else
    			insert.addLong(0);
    		insert.addString(descricao);
    		if(destino!=null)
    			insert.addLong(destino.obterId());
    		else
    			insert.addLong(0);
    		if(origem!=null)
    			insert.addLong(origem.obterId());
    		else
    			insert.addLong(0);
    		insert.addString(tipo);
    	        
    		if(this instanceof AgendaMovimentacao)
    			insert.addLong(1);
    		else
    			insert.addLong(0);
    	        
    		insert.execute();
	        
	        /*if(dataPrevistaInicio != null)
	            atualizarDataPrevistaInicio(dataPrevistaInicio);
	        if(dataPrevistaConclusao != null)
	            atualizarDataPrevistaConclusao(dataPrevistaConclusao);
	        if(descricao != null)
	            atualizarDescricao(descricao);
	        if(destino != null)
	            atualizarDestino(destino);
	        if(duracao != null)
	            atualizarDuracao(duracao);
	        if(origem != null)
	            atualizarOrigem(origem);
	        if(prioridade != null)
	            atualizarPrioridade(prioridade.intValue());
	        if(tipo != null)
	            atualizarTipo(tipo);
	        
	        atualizarFase("pendente");*/
	        
	        // Usar o atualizar2 para o rotor que gera a agenda automaticamente
	        
	       /* if(dataPrevistaInicio != null)
	            atualizarDataPrevistaInicio2(dataPrevistaInicio);
	        if(dataPrevistaConclusao != null)
	            atualizarDataPrevistaConclusao2(dataPrevistaConclusao);
	        if(descricao != null)
	            atualizarDescricao2(descricao);
	        if(origem != null)
	            atualizarOrigem2(origem);
	        if(prioridade != null)
	            atualizarPrioridade2(prioridade.intValue());
	        if(tipo != null)
	            atualizarTipo2(tipo);
	        
	        atualizarFase2("pendente");*/
    	}
    }

    public Date obterAtualizacao()
        throws Exception
    {
        if(atualizacao == null)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select atualizacao from evento where id=?");
            query.addLong(obterId());
            atualizacao = new Date(query.executeAndGetFirstRow().getLong("atualizacao"));
        }
        return atualizacao;
    }

    public String obterClasse()
        throws Exception
    {
        return getClassAlias().toLowerCase().trim();
    }

    public String obterClasseDescricao()
        throws Exception
    {
        if(classeDescricao == null)
            classeDescricao = InfraProperties.getInstance().getProperty(obterClasse() + ".descricao");
        return classeDescricao;
    }

    public Collection obterComentarios()
        throws Exception
    {
        if(comentarios == null)
        {
            EntidadeHome entidadeHome = (EntidadeHome)getModelManager().getHome("EntidadeHome");
            SQLQuery query = getModelManager().createSQLQuery("crm", "select criacao, titulo, comentario from comentario where id=? order by criacao");
            query.addLong(obterId());
            SQLRow rows[] = query.execute();
            comentarios = new ArrayList();
            for(int i = 0; i < rows.length; i++)
            {
                Date criacao = new Date(rows[i].getLong("criacao"));
                comentarios.add(new ComentarioImpl(this, criacao, rows[i].getString("titulo"), rows[i].getString("comentario")));
            }

        }
        return comentarios;
    }

    public Date obterCriacao()
        throws Exception
    {
        if(criacao == null)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select criacao from evento where id=?");
            query.addLong(obterId());
            criacao = new Date(query.executeAndGetFirstRow().getLong("criacao"));
        }
        return criacao;
    }

    public Usuario obterCriador()
        throws Exception
    {
        if(criador == null)
        {
            EntidadeHome entidadeHome = (EntidadeHome)getModelManager().getHome("EntidadeHome");
            SQLQuery query = getModelManager().createSQLQuery("crm", "select criador from evento where id=?");
            query.addLong(obterId());
            criador = (Usuario)entidadeHome.obterEntidadePorId(query.executeAndGetFirstRow().getLong("criador"));
        }
        return criador;
    }

    public Date obterDataPrevistaConclusao() throws Exception
    {
        if(dataPrevistaConclusao == null)
        {
        	SQLQuery query = getModelManager().createSQLQuery("crm", "EXEC obterDataPrevistaConclusao "+this.obterId());
            
            long data = query.executeAndGetFirstRow().getLong("data_prevista_conclusao");
            if(data != 0)
                 dataPrevistaConclusao = new Date(data);
        }
        return dataPrevistaConclusao;
    }

    public Date obterDataPrevistaInicio() throws Exception
    {
        if(dataPrevistaInicio == null)
        {
            /*SQLQuery query = getModelManager().createSQLQuery("crm", "select data_prevista_inicio from evento where id=?");
            query.addLong(obterId());*/
        	
        	SQLQuery query = getModelManager().createSQLQuery("crm", "EXEC obterDataPrevistaInicio "+this.obterId());
        	
            long data = query.executeAndGetFirstRow().getLong("data_prevista_inicio");
            if(data != 0)
                dataPrevistaInicio = new Date(data);
            
        }
        return dataPrevistaInicio;
    }

    public String obterDescricao()
        throws Exception
    {
        if(descricao == null)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select descricao from evento where id=?");
            query.addLong(obterId());
            descricao = query.executeAndGetFirstRow().getString("descricao");
        }
        return descricao;
    }

    public Entidade obterDestino()
        throws Exception
    {
        if(destino == null)
        {
            EntidadeHome entidadeHome = (EntidadeHome)getModelManager().getHome("EntidadeHome");
            SQLQuery query = getModelManager().createSQLQuery("crm", "select destino from evento where id=?");
            query.addLong(obterId());
            destino = entidadeHome.obterEntidadePorId(query.executeAndGetFirstRow().getLong("destino"));
        }
        return destino;
    }

    public long obterDuracao()
        throws Exception
    {
        if(duracao == null && obterId() > 0L)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select duracao from evento where id=?");
            query.addLong(obterId());
            SQLRow rows[] = query.execute();
            if(rows.length == 1)
                duracao = new Long(rows[0].getLong("duracao"));
        }
        if(duracao != null)
            return duracao.longValue();
        else
            return 0L;
    }

    public Evento.Fase obterFase()
        throws Exception
    {
        if(fase == null)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select codigo, inicio from fase where id=? and termino=0");
            query.addLong(obterId());
            SQLRow row = query.executeAndGetFirstRow();
            fase = new FaseImpl(this, row.getString("codigo"), new Date(row.getLong("inicio")), null);
        }
        return fase;
    }

    public Evento.Fase obterFaseAnterior()
        throws Exception
    {
        if(fase == null)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select codigo, inicio from fase where id=? and termino<>0 order by termino DESC");
            query.addLong(obterId());
            SQLRow row = query.executeAndGetFirstRow();
            fase = new FaseImpl(this, row.getString("codigo"), new Date(row.getLong("inicio")), new Date(row.getLong("termino")));
        }
        return fase;
    }

    public String obterIcone()
        throws Exception
    {
        return "document.gif";
    }

    public long obterId()
    {
        return id;
    }

    public Collection<Evento> obterInferiores()
        throws Exception
    {
        EventoHome eventoHome = (EventoHome)getModelManager().getHome("EventoHome");
        return eventoHome.obterEventosInferiores(this);
    }

    public Entidade obterOrigem()
        throws Exception
    {
        if(origem == null)
        {
            EntidadeHome entidadeHome = (EntidadeHome)getModelManager().getHome("EntidadeHome");
            SQLQuery query = getModelManager().createSQLQuery("crm", "select origem from evento where id=?");
            query.addLong(obterId());
            origem = entidadeHome.obterEntidadePorId(query.executeAndGetFirstRow().getLong("origem"));
        }
        return origem;
    }

    public int obterPrioridade()
        throws Exception
    {
        if(prioridade == null)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select prioridade from evento where id=?");
            query.addLong(obterId());
            prioridade = new Integer(query.executeAndGetFirstRow().getInt("prioridade"));
        }
        return prioridade.intValue();
    }

    public int obterQuantidadeComentarios()
        throws Exception
    {
        if(quantidadeComentarios == null)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select count(*) from comentario where id=?");
            query.addLong(obterId());
            quantidadeComentarios = new Integer(query.executeAndGetFirstRow().getInt("count(*)"));
        }
        return quantidadeComentarios.intValue();
    }

    public Entidade obterResponsavel()
        throws Exception
    {
        if(responsavel == null)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select responsavel from evento where id=?");
            query.addLong(obterId());
            SQLRow rows[] = query.execute();
            if(rows.length == 1)
            {
                long responsavelId = rows[0].getLong("responsavel");
                if(responsavelId > 0L)
                {
                    EntidadeHome entidadeHome = (EntidadeHome)getModelManager().getHome("EntidadeHome");
                    responsavel = entidadeHome.obterEntidadePorId(responsavelId);
                }
            }
        }
        return responsavel;
    }

    public Entidade obterResponsavelAnterior()
        throws Exception
    {
        if(responsavelAnterior == null && obterId() > 0L)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select responsavel_anterior from evento where id=?");
            query.addLong(obterId());
            SQLRow rows[] = query.execute();
            if(rows.length == 1)
            {
                long responsavelAnteriorId = rows[0].getLong("responsavel_anterior");
                if(responsavelAnteriorId > 0L)
                {
                    EntidadeHome entidadeHome = (EntidadeHome)getModelManager().getHome("EntidadeHome");
                    responsavelAnterior = entidadeHome.obterEntidadePorId(responsavelAnteriorId);
                }
            }
        }
        return responsavelAnterior;
    }

    public Evento obterSuperior() throws Exception
    {
        if(superior == null)
        {
            EventoHome eventoHome = (EventoHome)getModelManager().getHome("EventoHome");
            superior = eventoHome.obterEventoSuperior(this);
        }
        return superior;
    }

    public Collection obterSuperiores()
        throws Exception
    {
        if(superiores == null)
        {
            EventoHome eventoHome = (EventoHome)getModelManager().getHome("EventoHome");
            superiores = eventoHome.obterEventosSuperiores(this);
        }
        return superiores;
    }

    public String obterTipo() throws Exception
    {
        if(tipo == null)
        {
        	SQLQuery query = getModelManager().createSQLQuery("crm", "EXEC obterTipoEvento ?");
        	query.addLong(obterId());
        	
            tipo = query.executeAndGetFirstRow().getString("tipo");
        }
        return tipo;
    }

    public String obterTitulo()
        throws Exception
    {
        if(titulo == null)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select titulo from evento where id=?");
            query.addLong(obterId());
            titulo = query.executeAndGetFirstRow().getString("titulo");
        }
        return titulo;
    }

    protected Usuario obterUsuarioAtual()
        throws Exception
    {
        UsuarioHome usuarioHome = (UsuarioHome)getModelManager().getHome("UsuarioHome");
        return usuarioHome.obterUsuarioPorUser(getModelManager().getUser());
    }

    public boolean permiteAdicionarComentario()
        throws Exception
    {
        return !obterFase().equals("concluido");
    }

    public boolean permiteAtualizar()
        throws Exception
    {
        if(obterId() == 0L)
            return true;
        return obterUsuarioAtual().equals(obterResponsavel()) && !obterFase().equals("concluido");
    }

    public boolean permiteConcluir()
        throws Exception
    {
        return obterUsuarioAtual().equals(obterResponsavel()) && !obterFase().equals("concluido");
    }

    public boolean permiteDevolver()
        throws Exception
    {
        return obterUsuarioAtual().equals(obterResponsavel()) && !obterFase().equals("concluido");
    }

    public boolean permiteEncaminhar()
        throws Exception
    {
        return (obterUsuarioAtual().equals(obterResponsavel()) || obterUsuarioAtual().equals(obterCriador())) && !obterFase().equals("concluido");
    }

    public boolean permiteExcluir()
        throws Exception
    {
        return obterUsuarioAtual().equals(obterOrigem().obterResponsavel()) && obterFase().equals("concluido");
    }

    public boolean permiteIncluirEventoInferior()
        throws Exception
    {
        String classes = InfraProperties.getInstance().getProperty(obterClasse() + ".inferiores");
        if(classes.equals(""))
            return false;
        else
            return permiteAtualizar();
    }

    public boolean permiteResponder()
        throws Exception
    {
        return obterUsuarioAtual().equals(obterResponsavel()) && !obterFase().equals("concluido");
    }

    public void responder(String comentario)
        throws Exception
    {
        adicionarComentario("Respondido por " + obterUsuarioAtual().obterNome() + " para " + obterResponsavelAnterior().obterNome(), comentario);
        atualizarResponsavel(obterResponsavelAnterior());
    }

    public void ordenar()
        throws Exception
    {
        if(permiteAutoOrdenar())
        {
            Evento e;
            for(Iterator i = obterEventosOrdenar().iterator(); i.hasNext(); e.atualizarOrdem(e.obterCriacao().getTime()))
                e = (Evento)i.next();

        } else
        {
            Evento e;
            for(Iterator i = obterEventoSemOrdem().iterator(); i.hasNext(); e.atualizarOrdem(e.obterCriacao().getTime()))
                e = (Evento)i.next();

        }
    }

    private Collection obterEventosOrdenar()
        throws Exception
    {
        Collection retornar = new ArrayList();
        for(Iterator i = obterInferiores().iterator(); i.hasNext();)
        {
            Evento e = (Evento)i.next();
            boolean aceita = false;
            for(Iterator i2 = classesOrdem.iterator(); i2.hasNext();)
            {
                String nomeClasse = (String)i2.next();
                if(nomeClasse.toLowerCase().equals(e.obterClasse().toLowerCase()))
                    aceita = true;
            }

            if(aceita)
                retornar.add(e);
        }

        return retornar;
    }

    private Collection obterEventoSemOrdem()
        throws Exception
    {
        Collection semOrdem = new ArrayList();
        for(Iterator i = obterEventosOrdenar().iterator(); i.hasNext();)
        {
            Evento e = (Evento)i.next();
            if(e.obterOrdem() == 0L)
                semOrdem.add(e);
        }

        return semOrdem;
    }

    private boolean permiteAutoOrdenar()
        throws Exception
    {
        boolean retorno = true;
        for(Iterator i = obterEventosOrdenar().iterator(); i.hasNext();)
        {
            Evento e = (Evento)i.next();
            if(e.obterOrdem() != 0L)
                retorno = false;
        }

        return retorno;
    }

    private HashMap obterListaPorOrdem()
        throws Exception
    {
        HashMap ordenados = new HashMap();
        Evento e;
        for(Iterator i = obterEventosOrdenar().iterator(); i.hasNext(); ordenados.put(new Long(e.obterOrdem()), e))
            e = (Evento)i.next();

        return ordenados;
    }

    public void ordenarParaCima(long ordem)
        throws Exception
    {
        HashMap ordenados = obterListaPorOrdem();
        long ordemUm = 0L;
        long ordemDois = 0L;
        Evento eventoUm = (Evento)ordenados.get(new Long(ordem));
        if(eventoUm != null)
            ordemUm = eventoUm.obterOrdem();
        Evento eventoDois = obterEventoACima(ordem);
        if(eventoDois != null)
            ordemDois = eventoDois.obterOrdem();
        if(ordemUm != 0L && ordemDois != 0L)
        {
            eventoUm.atualizarOrdem(ordemDois);
            eventoDois.atualizarOrdem(ordemUm);
        } else
        {
            throw new Exception("N\343o pode ser ordenado para cima");
        }
    }

    public void ordenarParaBaixo(long ordem)
        throws Exception
    {
        HashMap ordenados = obterListaPorOrdem();
        long ordemUm = 0L;
        long ordemDois = 0L;
        Evento eventoUm = (Evento)ordenados.get(new Long(ordem));
        if(eventoUm != null)
            ordemUm = eventoUm.obterOrdem();
        Evento eventoDois = obterEventoABaixo(ordem);
        if(eventoDois != null)
            ordemDois = eventoDois.obterOrdem();
        if(ordemUm != 0L && ordemDois != 0L)
        {
            eventoUm.atualizarOrdem(ordemDois);
            eventoDois.atualizarOrdem(ordemUm);
        } else
        {
            throw new Exception("N\343o pode ser ordenado para baixo");
        }
    }

    private Evento obterEventoACima(long ordem)
        throws Exception
    {
        long retorno = 0L;
        Evento retorna = null;
        for(Iterator i = obterEventosOrdenar().iterator(); i.hasNext();)
        {
            Evento e = (Evento)i.next();
            if(e.obterOrdem() < ordem && retorno < e.obterOrdem())
            {
                retorno = e.obterOrdem();
                retorna = e;
            }
        }

        return retorna;
    }

    private Evento obterEventoABaixo(long ordem)
        throws Exception
    {
        long retorno = 0L;
        Evento retorna = null;
        for(Iterator i = obterEventosOrdenar().iterator(); i.hasNext();)
        {
            Evento e = (Evento)i.next();
            if(e.obterOrdem() > ordem && (retorno > e.obterOrdem() || retorno == 0L))
            {
                retorno = e.obterOrdem();
                retorna = e;
            }
        }

        return retorna;
    }

    public void atualizarOrdem(long ordem)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("update evento set ordem=? where id=?");
        update.addLong(ordem);
        update.addLong(id);
        update.execute();
        this.ordem = ordem;
    }

    public long obterOrdem()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select ordem from evento where id=?");
        query.addLong(obterId());
        ordem = query.executeAndGetFirstRow().getLong("ordem");
        return ordem;
    }

    public void atribuirClassesParaOrdenar(Collection classes)
        throws Exception
    {
        classesOrdem = classes;
    }
}
