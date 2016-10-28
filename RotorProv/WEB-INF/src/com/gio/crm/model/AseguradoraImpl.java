// FrontEnd Plus GUI for JAD
// DeCompiled : AseguradoraImpl.class

package com.gio.crm.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

// Referenced classes of package com.gio.crm.model:
//            EntidadeImpl, Aseguradora, Entidade, EntidadeHome, 
//            EventoHome, RatioPermanente, Evento

public class AseguradoraImpl extends EntidadeImpl
    implements Aseguradora
{
    public class ReaseguradoraImpl
        implements Aseguradora.Reaseguradora
    {

        private AseguradoraImpl aseguradora;
        private int id;
        private Entidade reaseguradora;
        private Entidade corretora;
        private String tipoContrato;
        private Date dataVencimento;
        private int participacao;
        private String observacao;

        public void atualizar(Entidade reaseguradora, Entidade corretora, String tipoContrato, Date dataVencimento, int participacao, String observacao)
            throws Exception
        {
            SQLUpdate update = aseguradora.getModelManager().createSQLUpdate("update aseguradora_reaseguradora set reaseguradora=?, corretora=?, tipo_contrato=?, data_vencimento=?, participacao=?, observacao=? where entidade=? and id=?");
            update.addLong(reaseguradora.obterId());
            update.addLong(corretora.obterId());
            update.addString(tipoContrato);
            update.addLong(dataVencimento.getTime());
            update.addInt(participacao);
            update.addString(observacao);
            update.addLong(aseguradora.obterId());
            update.addInt(id);
            update.execute();
        }
        
        public Aseguradora obterAseguradora()
            throws Exception
        {
            return aseguradora;
        }

        public int obterId()
            throws Exception
        {
            return id;
        }

        public Entidade obterReaseguradora()
            throws Exception
        {
            return reaseguradora;
        }

        public Entidade obterCorretora()
            throws Exception
        {
            return corretora;
        }

        public String obterTipoContrato()
            throws Exception
        {
            return tipoContrato;
        }

        public Date obterDataVencimento()
            throws Exception
        {
            return dataVencimento;
        }

        public int obterParticipacao()
            throws Exception
        {
            return participacao;
        }

        public String obterObservacao()
            throws Exception
        {
            return observacao;
        }

        ReaseguradoraImpl(AseguradoraImpl aseguradora, int id, Entidade reaseguradora, Entidade corretora, String tipoContrato, Date dataVencimento, 
                int participacao, String observacao)
        {
            this.aseguradora = aseguradora;
            this.id = id;
            this.reaseguradora = reaseguradora;
            this.corretora = corretora;
            this.tipoContrato = tipoContrato;
            this.dataVencimento = dataVencimento;
            this.participacao = participacao;
            this.observacao = observacao;
        }
    }

    public class AcionistaImpl
        implements Aseguradora.Acionista
    {

        private AseguradoraImpl aseguradora;
        private int id;
        private String acionista;
        private int quantidade;
        private String tipo;

        public void atualizar(String acionista, int quantidade, String tipo)
            throws Exception
        {
            SQLUpdate update = aseguradora.getModelManager().createSQLUpdate("update aseguradora_acionista set acionista=?, quantidade=?, tipo=? where entidade=? and id=?");
            update.addString(acionista);
            update.addInt(quantidade);
            update.addString(tipo);
            update.addLong(aseguradora.obterId());
            update.addInt(id);
            update.execute();
        }

        public Aseguradora obterAseguradora()
            throws Exception
        {
            return aseguradora;
        }

        public int obterId()
            throws Exception
        {
            return id;
        }

        public String obterAcionista()
            throws Exception
        {
            return acionista;
        }

        public int obterquantidade()
            throws Exception
        {
            return quantidade;
        }

        public String obtertipo()
            throws Exception
        {
            return tipo;
        }

        AcionistaImpl(AseguradoraImpl aseguradora, int id, String acionista, int quantidade, String tipo)
            throws Exception
        {
            this.aseguradora = aseguradora;
            this.id = id;
            this.acionista = acionista;
            this.quantidade = quantidade;
            this.tipo = tipo;
        }
    }

    public class FilialImpl
        implements Aseguradora.Filial
    {

        private AseguradoraImpl aseguradora;
        private int id;
        private String filial;
        private String tipo;
        private String telefone;
        private String cidade;
        private String endereco;
        private String email;

        public void atualizar(String filial, String tipo, String telefone, String cidade, String endereco, String email)
            throws Exception
        {
            SQLUpdate update = aseguradora.getModelManager().createSQLUpdate("update aseguradora_filial set filial=?, tipo=?, telefone=?, cidade=?, endereco=?, email=? where entidade=? and id=?");
            update.addString(filial);
            update.addString(tipo);
            update.addString(telefone);
            update.addString(cidade);
            update.addString(endereco);
            update.addString(email);
            update.addLong(aseguradora.obterId());
            update.addInt(id);
            update.execute();
        }

        public Aseguradora obterAseguradora()
            throws Exception
        {
            return aseguradora;
        }

        public int obterId()
            throws Exception
        {
            return id;
        }

        public String obterFilial()
            throws Exception
        {
            return filial;
        }

        public String obterTipo()
            throws Exception
        {
            return tipo;
        }

        public String obterTelefone()
            throws Exception
        {
            return telefone;
        }

        public String obterCidade()
            throws Exception
        {
            return cidade;
        }

        public String obterEndereco()
            throws Exception
        {
            return endereco;
        }

        public String obterEmail()
            throws Exception
        {
            return email;
        }

        FilialImpl(AseguradoraImpl aseguradora, int id, String filial, String tipo, String telefone, String cidade, 
                String endereco, String email)
            throws Exception
        {
            this.aseguradora = aseguradora;
            this.id = id;
            this.filial = filial;
            this.tipo = tipo;
            this.telefone = telefone;
            this.cidade = cidade;
            this.endereco = endereco;
            this.email = email;
        }
    }

    public class FusaoImpl
        implements Aseguradora.Fusao
    {

        private AseguradoraImpl aseguradora;
        private int id;
        private String empresa;
        private Date dataFusao;

        public void atualizar(String empresa, Date data)
            throws Exception
        {
            SQLUpdate update = aseguradora.getModelManager().createSQLUpdate("update aseguradora_fusao set empresa=?, data=? where entidade=? and id=?");
            update.addString(empresa);
            update.addLong(data.getTime());
            update.addLong(aseguradora.obterId());
            update.addInt(id);
            update.execute();
        }

        public Aseguradora obterAseguradora()
            throws Exception
        {
            return aseguradora;
        }

        public int obterId()
            throws Exception
        {
            return id;
        }

        public String obterEmpresa()
            throws Exception
        {
            return empresa;
        }

        public Date obterDatausao()
            throws Exception
        {
            return dataFusao;
        }

        FusaoImpl(AseguradoraImpl aseguradora, int id, String empresa, Date dataFusao)
            throws Exception
        {
            this.aseguradora = aseguradora;
            this.id = id;
            this.empresa = empresa;
            this.dataFusao = dataFusao;
        }
    }

    public class CoaseguradorImpl
        implements Aseguradora.Coasegurador
    {

        private AseguradoraImpl aseguradora;
        private int id;
        private String codigo;

        public void atualizar(String codigo)
            throws Exception
        {
            SQLUpdate update = aseguradora.getModelManager().createSQLUpdate("update aseguradora_coasegurador set codigo=? where entidade=? and id=?");
            update.addString(codigo);
            update.addLong(aseguradora.obterId());
            update.addInt(id);
            update.execute();
        }

        public Aseguradora obterAseguradora()
            throws Exception
        {
            return aseguradora;
        }

        public int obterId()
            throws Exception
        {
            return id;
        }

        public String obterCodigo()
            throws Exception
        {
            return codigo;
        }

        CoaseguradorImpl(AseguradoraImpl aseguradora, int id, String codigo)
            throws Exception
        {
            this.aseguradora = aseguradora;
            this.id = id;
            this.codigo = codigo;
        }
    }


    private Map reaseguradoras;
    private Map acionistas;
    private Map filiais;
    private Map fusoes;
    private Map planos;
    private Map coaseguradores;

    public AseguradoraImpl()
    {
    }

    public boolean existeAgendaNoPeriodo(int mes, int ano, String tipo) throws Exception
	{
	    SQLQuery query = getModelManager().createSQLQuery("crm", "select movimento_mes, movimento_ano from agenda_movimentacao,evento,fase where evento.id=agenda_movimentacao.id and origem=? and movimento_mes=? and movimento_ano=? and tipo=? and evento.id=fase.id and codigo='concluido' and termino = 0");
	    query.addLong(this.obterId());
	    query.addInt(mes);
	    query.addInt(ano);
	    query.addString(tipo);
	    SQLRow rows[] = query.execute();
	    if(rows.length > 0)
	        return true;
	    else
	    	return false;
	}
    
    public void adicionarReaseguradora(Entidade reaseguradora, Entidade corretora, String tipoContrato, Date dataVencimento, int participacao, String observacao)
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("select max(id) from aseguradora_reaseguradora where entidade=?");
        query.addLong(obterId());
        long id = query.executeAndGetFirstRow().getLong("max(id)") + 1L;
        SQLUpdate insert = getModelManager().createSQLUpdate("insert into aseguradora_reaseguradora (entidade, id, reaseguradora, corretora, tipo_contrato, data_vencimento, participacao, observacao) values (?, ?, ?, ?, ?, ?, ?, ?)");
        insert.addLong(obterId());
        insert.addLong(id);
        insert.addLong(reaseguradora.obterId());
        insert.addLong(corretora.obterId());
        insert.addString(tipoContrato);
        insert.addLong(dataVencimento.getTime());
        insert.addInt(participacao);
        insert.addString(observacao);
        insert.execute();
    }

    public Aseguradora.Reaseguradora obterReaseguradora(int id)
        throws Exception
    {
        obterReaseguradoras();
        return (Aseguradora.Reaseguradora)reaseguradoras.get(Integer.toString(id));
    }

    public Collection obterReaseguradoras()
        throws Exception
    {
        reaseguradoras = new TreeMap();
        SQLQuery query = getModelManager().createSQLQuery("crm", "select * from aseguradora_reaseguradora where entidade=?");
        query.addLong(obterId());
        SQLRow rows[] = query.execute();
        for(int i = 0; i < rows.length; i++)
        {
            int id = rows[i].getInt("id");
            EntidadeHome home = (EntidadeHome)getModelManager().getHome("EntidadeHome");
            long reaseguradoraId = rows[i].getLong("reaseguradora");
            long corretoraId = rows[i].getLong("corretora");
            Entidade reaseguradora = home.obterEntidadePorId(reaseguradoraId);
            Entidade corretora = home.obterEntidadePorId(corretoraId);
            reaseguradoras.put(Integer.toString(id), new ReaseguradoraImpl(this, id, reaseguradora, corretora, rows[i].getString("tipo_contrato"), new Date(rows[i].getLong("data_vencimento")), rows[i].getInt("participacao"), rows[i].getString("observacao")));
        }

        return reaseguradoras.values();
    }

    public void removerReaseguradora(Aseguradora.Reaseguradora reaseguradora)
        throws Exception
    {
        SQLUpdate delete = getModelManager().createSQLUpdate("delete from aseguradora_reaseguradora where entidade=? and id=?");
        delete.addLong(obterId());
        delete.addInt(reaseguradora.obterId());
        delete.execute();
        if(reaseguradoras != null)
            reaseguradoras.remove(Integer.toString(reaseguradora.obterId()));
    }

    public Collection obterCalculosMeicos()
        throws Exception
    {
        Map calculos = new TreeMap();
        EventoHome home = (EventoHome)getModelManager().getHome("EventoHome");
        SQLQuery query = getModelManager().createSQLQuery("crm", "select evento.id from evento,meicos_calculo,fase where evento.id = meicos_calculo.id and origem = ? and meicos_calculo.id = fase.id and codigo='pendente' and termino = 0");
        query.addLong(obterId());
        SQLRow rows[] = query.execute();
        for(int i = 0; i < rows.length; i++)
        {
            long l = rows[i].getLong("id");
        }

        return calculos.values();
    }

    public Collection obterAgentes()
        throws Exception
    {
        Map agentes = new TreeMap();
        EntidadeHome home = (EntidadeHome)getModelManager().getHome("EntidadeHome");
        SQLQuery query = getModelManager().createSQLQuery("crm", "select agente from evento,apolice where origem = ? and evento.id = apolice.id");
        query.addLong(obterId());
        SQLRow rows[] = query.execute();
        for(int i = 0; i < rows.length; i++)
        {
            long id = rows[i].getLong("agente");
            Entidade agente = home.obterEntidadePorId(id);
            agentes.put(new Long(agente.obterId()), agente);
        }

        return agentes.values();
    }

    public Collection obterMeicos(String tipo)
        throws Exception
    {
        Map meicos = new TreeMap();
        EventoHome home = (EventoHome)getModelManager().getHome("EventoHome");
        SQLQuery query = getModelManager().createSQLQuery("crm", "select id from evento where classe = 'MeicosAseguradora' and origem = ? and tipo = ?");
        query.addLong(obterId());
        query.addString(tipo);
        SQLRow rows[] = query.execute();
        for(int i = 0; i < rows.length; i++)
        {
            long l = rows[i].getLong("id");
        }

        return meicos.values();
    }

    public void adicionarAcionista(String acionista, int quantidade, String tipo)
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("select max(id) as MX from aseguradora_acionista where entidade=?");
        query.addLong(obterId());
        long id = query.executeAndGetFirstRow().getLong("MX") + 1L;
        SQLUpdate insert = getModelManager().createSQLUpdate("insert into aseguradora_acionista (entidade, id, acionista, quantidade, tipo) values (?, ?, ?, ?, ?)");
        insert.addLong(obterId());
        insert.addLong(id);
        insert.addString(acionista);
        insert.addInt(quantidade);
        insert.addString(tipo);
        insert.execute();
    }

    public Aseguradora.Acionista obterAcionista(int id)
        throws Exception
    {
        obterAcionistas();
        return (Aseguradora.Acionista)acionistas.get(Integer.toString(id));
    }

    public Collection obterAcionistas()
        throws Exception
    {
        acionistas = new TreeMap();
        SQLQuery query = getModelManager().createSQLQuery("crm", "select * from aseguradora_acionista where entidade=?");
        query.addLong(obterId());
        SQLRow rows[] = query.execute();
        for(int i = 0; i < rows.length; i++)
        {
            int id = rows[i].getInt("id");
            EntidadeHome home = (EntidadeHome)getModelManager().getHome("EntidadeHome");
            acionistas.put(Integer.toString(id), new AcionistaImpl(this, id, rows[i].getString("acionista"), rows[i].getInt("quantidade"), rows[i].getString("tipo")));
        }

        return acionistas.values();
    }

    public void removerAcionista(Aseguradora.Acionista acionista)
        throws Exception
    {
        SQLUpdate delete = getModelManager().createSQLUpdate("delete from aseguradora_acionista where entidade=? and id=?");
        delete.addLong(obterId());
        delete.addInt(acionista.obterId());
        delete.execute();
        if(acionistas != null)
            acionistas.remove(Integer.toString(acionista.obterId()));
    }

    public void adicionarFilial(String filial, String tipo, String telefone, String cidade, String endereco, String email)
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("select max(id) as MX from aseguradora_filial where entidade=?");
        query.addLong(obterId());
        long id = query.executeAndGetFirstRow().getLong("MX") + 1L;
        SQLUpdate insert = getModelManager().createSQLUpdate("insert into aseguradora_filial (entidade, id, filial, tipo, telefone, cidade, endereco, email) values (?, ?, ?, ?, ?, ?, ?, ?)");
        insert.addLong(obterId());
        insert.addLong(id);
        insert.addString(filial);
        insert.addString(tipo);
        insert.addString(telefone);
        insert.addString(cidade);
        insert.addString(endereco);
        insert.addString(email);
        insert.execute();
    }

    public Aseguradora.Filial obterFilial(int id)
        throws Exception
    {
        obterFiliais();
        return (Aseguradora.Filial)filiais.get(Integer.toString(id));
    }

    public Collection obterFiliais()
        throws Exception
    {
        filiais = new TreeMap();
        SQLQuery query = getModelManager().createSQLQuery("crm", "select * from aseguradora_filial where entidade=?");
        query.addLong(obterId());
        SQLRow rows[] = query.execute();
        for(int i = 0; i < rows.length; i++)
        {
            int id = rows[i].getInt("id");
            filiais.put(Integer.toString(id), new FilialImpl(this, id, rows[i].getString("filial"), rows[i].getString("tipo"), rows[i].getString("telefone"), rows[i].getString("cidade"), rows[i].getString("endereco"), rows[i].getString("email")));
        }

        return filiais.values();
    }

    public void removerFilial(Aseguradora.Filial filial)
        throws Exception
    {
        SQLUpdate delete = getModelManager().createSQLUpdate("delete from aseguradora_filial where entidade=? and id=?");
        delete.addLong(obterId());
        delete.addInt(filial.obterId());
        delete.execute();
        if(filiais != null)
            filiais.remove(Integer.toString(filial.obterId()));
    }

    public void adicionarFusao(String empresa, Date dataFusao)
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("select max(id) as MX from aseguradora_fusao where entidade=?");
        query.addLong(obterId());
        long id = query.executeAndGetFirstRow().getLong("MX") + 1L;
        SQLUpdate insert = getModelManager().createSQLUpdate("insert into aseguradora_fusao (entidade, id, empresa, data) values (?, ?, ?, ?)");
        insert.addLong(obterId());
        insert.addLong(id);
        insert.addString(empresa);
        insert.addLong(dataFusao.getTime());
        insert.execute();
    }

    public Aseguradora.Fusao obterFusao(int id)
        throws Exception
    {
        obterFusoes();
        return (Aseguradora.Fusao)fusoes.get(Integer.toString(id));
    }

    public Collection obterFusoes()
        throws Exception
    {
        fusoes = new TreeMap();
        SQLQuery query = getModelManager().createSQLQuery("crm", "select * from aseguradora_fusao where entidade=?");
        query.addLong(obterId());
        SQLRow rows[] = query.execute();
        for(int i = 0; i < rows.length; i++)
        {
            int id = rows[i].getInt("id");
            fusoes.put(Integer.toString(id), new FusaoImpl(this, id, rows[i].getString("empresa"), new Date(rows[i].getLong("data"))));
        }

        return fusoes.values();
    }

    public void removerFusao(Aseguradora.Fusao fusao)
        throws Exception
    {
        SQLUpdate delete = getModelManager().createSQLUpdate("delete from aseguradora_fusao where entidade=? and id=?");
        delete.addLong(obterId());
        delete.addInt(fusao.obterId());
        delete.execute();
        if(fusoes != null)
            fusoes.remove(Integer.toString(fusao.obterId()));
    }

    public void adicionarPlano(String ramo, String secao, String plano, Evento resolucao, Date data, String situacao, String descricao)
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("select max(id) as MX from aseguradora_plano where entidade=?");
        query.addLong(obterId());
        long id = query.executeAndGetFirstRow().getLong("MX") + 1L;
        SQLUpdate insert = getModelManager().createSQLUpdate("insert into aseguradora_plano (entidade, id, ramo, secao, plano, resolucao, data, situacao, descricao) values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
        insert.addLong(obterId());
        insert.addLong(id);
        insert.addString(ramo);
        insert.addString(secao);
        insert.addString(plano);
        insert.addLong(null);
        insert.addLong(data.getTime());
        insert.addString(situacao);
        insert.addString(descricao);
        insert.execute();
    }

    public RatioPermanente obterRatioPermanente()
        throws Exception
    {
        RatioPermanente ratio = null;
        SQLQuery query = getModelManager().createSQLQuery("select evento.id from evento,ratio_permanente,fase where evento.id = ratio_permanente.id and evento.id = fase.id and origem = ? and fase.codigo='pendente' and termino = 0");
        query.addLong(obterId());
        long id = query.executeAndGetFirstRow().getLong("id");
        if(id > 0L)
        {
            EventoHome home = (EventoHome)getModelManager().getHome("EventoHome");
            ratio = (RatioPermanente)home.obterEventoPorId(id);
        }
        return ratio;
    }

    public Collection obterRatiosPermanentes()
        throws Exception
    {
        Collection ratios = new ArrayList();
        SQLQuery query = getModelManager().createSQLQuery("crm", "select evento.id from evento,ratio_permanente where evento.id = ratio_permanente.id and origem = ?");
        query.addLong(obterId());
        SQLRow rows[] = query.execute();
        EventoHome home = (EventoHome)getModelManager().getHome("EventoHome");
        for(int i = 0; i < rows.length; i++)
        {
            long id = rows[i].getLong("id");
            RatioPermanente ratio = (RatioPermanente)home.obterEventoPorId(id);
            ratios.add(ratio);
        }

        return ratios;
    }

    public Collection obterRatiosUmAno()
        throws Exception
    {
        Collection ratios = new ArrayList();
        SQLQuery query = getModelManager().createSQLQuery("crm", "select evento.id from evento,ratio_um_ano where evento.id = ratio_um_ano.id and origem = ?");
        query.addLong(obterId());
        SQLRow rows[] = query.execute();
        EventoHome home = (EventoHome)getModelManager().getHome("EventoHome");
        for(int i = 0; i < rows.length; i++)
        {
            long id = rows[i].getLong("id");
            RatioPermanente ratio = (RatioPermanente)home.obterEventoPorId(id);
            ratios.add(ratio);
        }

        return ratios;
    }

    public Collection obterRatiosTresAnos()
        throws Exception
    {
        Collection ratios = new ArrayList();
        SQLQuery query = getModelManager().createSQLQuery("crm", "select evento.id from evento,ratio_tres_anos where evento.id = ratio_tres_anos.id and origem = ?");
        query.addLong(obterId());
        SQLRow rows[] = query.execute();
        EventoHome home = (EventoHome)getModelManager().getHome("EventoHome");
        for(int i = 0; i < rows.length; i++)
        {
            long id = rows[i].getLong("id");
            RatioPermanente ratio = (RatioPermanente)home.obterEventoPorId(id);
            ratios.add(ratio);
        }

        return ratios;
    }

    public Collection obterPlanos()
        throws Exception
    {
        planos = new TreeMap();
        return planos.values();
    }

    public void adicionarCoasegurador(String codigo)
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("select max(id) as MX from aseguradora_coasegurador where entidade=?");
        query.addLong(obterId());
        long id = query.executeAndGetFirstRow().getLong("MX") + 1L;
        SQLUpdate insert = getModelManager().createSQLUpdate("insert into aseguradora_coasegurador (entidade, id, codigo) values (?, ?, ?)");
        insert.addLong(obterId());
        insert.addLong(id);
        insert.addString(codigo);
        insert.execute();
    }

    public Aseguradora.Coasegurador obterCoasegurador(int id)
        throws Exception
    {
        obterCoaseguradores();
        return (Aseguradora.Coasegurador)coaseguradores.get(Integer.toString(id));
    }

    public Collection obterCoaseguradores()
        throws Exception
    {
        coaseguradores = new TreeMap();
        SQLQuery query = getModelManager().createSQLQuery("crm", "select * from aseguradora_coasegurador where entidade=?");
        query.addLong(obterId());
        SQLRow rows[] = query.execute();
        for(int i = 0; i < rows.length; i++)
        {
            int id = rows[i].getInt("id");
            coaseguradores.put(Integer.toString(id), new CoaseguradorImpl(this, id, rows[i].getString("codigo")));
        }

        return coaseguradores.values();
    }

    public void removerCoasegurador(Aseguradora.Coasegurador coasegurador)
        throws Exception
    {
        SQLUpdate delete = getModelManager().createSQLUpdate("delete from aseguradora_coasegurador where entidade=? and id=?");
        delete.addLong(obterId());
        delete.addInt(coasegurador.obterId());
        delete.execute();
        if(coaseguradores != null)
            coaseguradores.remove(Integer.toString(coasegurador.obterId()));
    }

    public Collection obterCoaseguradorasPorGrupo(String codigo)
        throws Exception
    {
        Collection lista = new ArrayList();
        SQLQuery query = getModelManager().createSQLQuery("crm", "select entidade.id from entidade, aseguradora_coasegurador where entidade.id = aseguradora_coasegurador.entidade and codigo=?");
        query.addString(codigo);
        SQLRow rows[] = query.execute();
        for(int i = 0; i < rows.length; i++)
        {
            EntidadeHome home = (EntidadeHome)getModelManager().getHome("EntidadeHome");
            lista.add(home.obterEntidadePorId(rows[i].getLong("id")));
        }

        return lista;
    }
    
    public Collection obterAgendas() throws Exception
	{
		Collection agendas = new ArrayList();
		
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		if(Integer.parseInt(this.obterSigla()) < 80) 
		{
			SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,agenda_movimentacao where evento.id = agenda_movimentacao.id " +
					"and evento.origem = ? and classe='AgendaMovimentacao' order by agenda_movimentacao.movimento_mes, agenda_movimentacao.movimento_ano desc ");
				
			query.addLong(this.obterId());
		
			SQLRow[] filas =  query.execute();
		
			for(int i = 0 ; i < filas.length;i++) 
			{
			
				long id = filas[i].getLong("id");
				
				AgendaMovimentacao agenda = (AgendaMovimentacao) home.obterEventoPorId(id);
				
				agendas.add(agenda);
			
				
			}
				
		}
		return agendas; 
	}

	public Collection obterAgendasIntrumentoPendentes() throws Exception
	{
		Collection agendas = new ArrayList();
		
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		if(Integer.parseInt(this.obterSigla()) < 80) 
		{
			SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,agenda_movimentacao,fase where evento.id = agenda_movimentacao.id and agenda_movimentacao.id = fase.id " +
					"and evento.origem = ? and classe='AgendaMovimentacao' and tipo = 'Instrumento' and codigo='pendente' and termino = 0 order by agenda_movimentacao.movimento_mes, agenda_movimentacao.movimento_ano desc ");
				
			query.addLong(this.obterId());
		
			SQLRow[] filas =  query.execute();
		
			for(int i = 0 ; i < filas.length;i++) 
			{
			
				long id = filas[i].getLong("id");
				
				AgendaMovimentacao agenda = (AgendaMovimentacao) home.obterEventoPorId(id);
				
				agendas.add(agenda);
			
				
			}
				
		}
		return agendas; 
	}
    
	public AgendaMovimentacao obterUltimaAgendaMCO() throws Exception
	{
		AgendaMovimentacao agenda = null;
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.YEAR, -2);
		
		Date dataLimite = c.getTime();
		c.setTime(new Date());
		
		while(c.getTime().compareTo(dataLimite)>0)
		{
			String mes = new SimpleDateFormat("MM").format(c.getTime());
			String ano = new SimpleDateFormat("yyyy").format(c.getTime());
			
			//SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,agenda_movimentacao where evento.id = agenda_movimentacao.id and movimento_mes = ? and movimento_ano = ? and origem = ?");
			SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,agenda_movimentacao,fase where evento.id = agenda_movimentacao.id and agenda_movimentacao.id = fase.id and tipo = 'Contabil' and origem = ? and movimento_mes = ? and movimento_ano = ? and codigo = 'concluido' and termino = 0");
			query.addLong(this.obterId());
			query.addInt(Integer.parseInt(mes));
			query.addInt(Integer.parseInt(ano));
			
			long id = query.executeAndGetFirstRow().getLong("id");
			
			if(id > 0)
			{
				AgendaMovimentacao agenda2 = (AgendaMovimentacao) home.obterEventoPorId(id);
				{
					agenda = agenda2;
					break;
				}
			}
			c.add(Calendar.MONTH, -1);
		}
		
		return  agenda;
	}
	
	public AgendaMovimentacao obterUltimaAgendaMCI() throws Exception
	{
		AgendaMovimentacao agenda = null;
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.YEAR, -2);
		
		Date dataLimite = c.getTime();
		c.setTime(new Date());
		
		while(c.getTime().compareTo(dataLimite)>0)
		{
			String mes = new SimpleDateFormat("MM").format(c.getTime());
			String ano = new SimpleDateFormat("yyyy").format(c.getTime());
			
			SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,agenda_movimentacao,fase where evento.id = agenda_movimentacao.id and agenda_movimentacao.id = fase.id and origem = ? and movimento_mes = ? and movimento_ano = ? and tipo = 'Instrumento' and codigo = 'concluido' and termino = 0");
			query.addLong(this.obterId());
			query.addInt(Integer.parseInt(mes));
			query.addInt(Integer.parseInt(ano));
			
			long id = query.executeAndGetFirstRow().getLong("id");
			
			if(id > 0)
			{
				AgendaMovimentacao agenda2 = (AgendaMovimentacao) home.obterEventoPorId(id);
				agenda = agenda2;
				break;
			}
			
			c.add(Calendar.MONTH, -1);
		}
		
		return  agenda;
	}
}