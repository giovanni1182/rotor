// FrontEnd Plus GUI for JAD
// DeCompiled : InscricaoImpl.class

package com.gio.crm.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

// Referenced classes of package com.gio.crm.model:
//            EventoImpl, Inscricao, Aseguradora, Entidade, 
//            EventoHome, Apolice, EntidadeHome

public class InscricaoImpl extends EventoImpl
    implements Inscricao
{
    public class RamoImpl
        implements Inscricao.Ramo
    {

        private InscricaoImpl inscricao;
        private int seq;
        private String ramo;

        public Inscricao obterEvento()
            throws Exception
        {
            return inscricao;
        }

        public int obterSeq()
            throws Exception
        {
            return seq;
        }

        public String obterRamo()
            throws Exception
        {
            return ramo;
        }

        public RamoImpl(InscricaoImpl inscricao, int seq, String ramo)
            throws Exception
        {
            this.inscricao = inscricao;
            this.seq = seq;
            this.ramo = ramo;
        }
    }


    private Map ramos;

    public InscricaoImpl()
    {
    }

    public void atualizarAseguradora(Aseguradora aseguradora)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update inscricao set aseguradora=? where id=?");
        update.addLong(aseguradora.obterId());
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarAgente(Entidade agente)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update inscricao set agente=? where id=?");
        update.addLong(agente.obterId());
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarInscricao(String inscricao)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update inscricao set inscricao=? where id=?");
        update.addString(inscricao);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarNumeroApolice(String numero)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update inscricao set numero_apolice=? where id=?");
        update.addString(numero);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarNumeroSecao(String numero)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update inscricao set numero_secao=? where id=?");
        update.addString(numero);
        update.addLong(obterId());
        update.execute();
    }

    public boolean verificarInscricao(String inscricao)
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select inscricao from inscricao where inscricao=? and id<>?");
        query.addString(inscricao);
        query.addLong(obterId());
        return query.execute().length > 0;
    }

    public boolean validarResolucao(String resolucaoStr)
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select evento.id from evento,documento_produto where evento.id = documento_produto.id and numero=?");
        query.addString(resolucaoStr);
        if(query.execute().length == 0)
        {
            return true;
        } else
        {
            boolean renovar = true;
            long id = query.executeAndGetFirstRow().getLong("id");
            EventoHome home = (EventoHome)getModelManager().getHome("EventoHome");
            return renovar;
        }
    }

    public void atualizarNumeroResolucao(String resolucao)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update inscricao set resolucao=? where id=?");
        update.addString(resolucao);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarDataResolucao(Date dataResolucao)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update inscricao set data_resolucao=? where id=?");
        update.addLong(dataResolucao.getTime());
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarDataValidade(Date dataValidade)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update inscricao set data_validade=? where id=?");
        update.addLong(dataValidade.getTime());
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarDataEmissao(Date data)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update inscricao set data_emissao=? where id=?");
        update.addLong(data.getTime());
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarDataVencimento(Date data)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update inscricao set data_vencimento=? where id=?");
        update.addLong(data.getTime());
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarSituacao(String situacao)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update inscricao set situacao=? where id=?");
        update.addString(situacao);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarApolice(Apolice apolice)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update inscricao set apolice=? where id=?");
        update.addLong(apolice.obterId());
        update.addLong(obterId());
        update.execute();
    }

    public void incluir()
        throws Exception
    {
        super.incluir();
        SQLUpdate insert = getModelManager().createSQLUpdate("crm", "insert into inscricao(id) values(?)");
        insert.addLong(obterId());
        insert.execute();
    }

    public Aseguradora obterAseguradora()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select aseguradora from inscricao where id=?");
        query.addLong(obterId());
        Aseguradora aseguradora = null;
        if(query.executeAndGetFirstRow().getLong("aseguradora") > 0L)
        {
            EntidadeHome home = (EntidadeHome)getModelManager().getHome("EntidadeHome");
            aseguradora = (Aseguradora)home.obterEntidadePorId(query.executeAndGetFirstRow().getLong("aseguradora"));
        }
        return aseguradora;
    }

    public Entidade obterAgente()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select agente from inscricao where id=?");
        query.addLong(obterId());
        Entidade agente = null;
        if(query.executeAndGetFirstRow().getLong("agente") > 0L)
        {
            EntidadeHome home = (EntidadeHome)getModelManager().getHome("EntidadeHome");
            agente = home.obterEntidadePorId(query.executeAndGetFirstRow().getLong("agente"));
        }
        return agente;
    }

    public String obterInscricao()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select inscricao from inscricao where id=?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("inscricao");
    }

    public String obterNumeroApolice()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select numero_apolice from inscricao where id=?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("numero_apolice");
    }

    public String obterNumeroSecao()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select numero_secao from inscricao where id=?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("numero_secao");
    }

    public String obterNumeroResolucao()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select resolucao from inscricao where id=?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("resolucao");
    }

    public Date obterDataResolucao()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select data_resolucao from inscricao where id=?");
        query.addLong(obterId());
        Date data = null;
        if(query.executeAndGetFirstRow().getLong("data_resolucao") != 0L)
            data = new Date(query.executeAndGetFirstRow().getLong("data_resolucao"));
        return data;
    }

    public Date obterDataValidade()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select data_validade from inscricao where id=?");
        query.addLong(obterId());
        Date data = null;
        if(query.executeAndGetFirstRow().getLong("data_validade") != 0L)
            data = new Date(query.executeAndGetFirstRow().getLong("data_validade"));
        return data;
    }

    public Collection obterDocumentosVinculados()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select sub_evento from evento_entidades where id=?");
        query.addLong(obterId());
        Collection inscricoes = new ArrayList();
        SQLRow rows[] = query.execute();
        for(int i = 0; i < rows.length; i++)
        {
            EventoHome home = (EventoHome)getModelManager().getHome("EventoHome");
            inscricoes.add(home.obterEventoPorId(rows[i].getLong("sub_evento")));
        }

        return inscricoes;
    }

    public String obterSituacao()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select situacao from inscricao where id=?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("situacao");
    }

    public Date obterDataEmissao()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select data_emissao from inscricao where id=?");
        query.addLong(obterId());
        Date data = null;
        long dataLong = query.executeAndGetFirstRow().getLong("data_emissao");
        if(dataLong > 0L)
            data = new Date(dataLong);
        return data;
    }

    public Date obterDataVencimento()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select data_vencimento from inscricao where id=?");
        query.addLong(obterId());
        Date data = null;
        long dataLong = query.executeAndGetFirstRow().getLong("data_vencimento");
        if(dataLong > 0L)
            data = new Date(dataLong);
        return data;
    }

    public Apolice obterApolice()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select apolice from inscricao,evento,apolice where evento.id=inscricao.apolice and inscricao.apolice=apolice.id and inscricao.id = ?");
        query.addLong(obterId());
        long id = query.executeAndGetFirstRow().getLong("apolice");
        Apolice apolice = null;
        if(id > 0L)
        {
            EventoHome home = (EventoHome)getModelManager().getHome("EventoHome");
            apolice = (Apolice)home.obterEventoPorId(id);
        }
        return apolice;
    }

    public void adicionarNovoRamo(String ramo)
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select MAX(seq) as MX from inscricao_ramo where id=?");
        query.addLong(obterId());
        int id = query.executeAndGetFirstRow().getInt("MX") + 1;
        SQLUpdate insert = getModelManager().createSQLUpdate("crm", "insert into inscricao_ramo(id, seq, nome) values (?, ?, ?)");
        insert.addLong(obterId());
        insert.addInt(id);
        insert.addString(ramo);
        insert.execute();
    }

    public void atualizarRamo(String ramo)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update inscricao set ramo=? where id=?");
        update.addString(ramo);
        update.addLong(obterId());
        update.execute();
    }

    public String obterRamo()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select ramo from inscricao where id=?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("ramo");
    }

    public Inscricao.Ramo obterRamo(int seq)
        throws Exception
    {
        obterRamos();
        return (Inscricao.Ramo)ramos.get(new Integer(seq));
    }

    public Collection obterRamos()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select * from inscricao_ramo where id=?");
        query.addLong(obterId());
        SQLRow rows[] = query.execute();
        ramos = new TreeMap();
        for(int i = 0; i < rows.length; i++)
        {
            int seq = rows[i].getInt("seq");
            String ramo = rows[i].getString("nome");
            ramos.put(new Integer(seq), new RamoImpl(this, seq, ramo));
        }

        return ramos.values();
    }

    public void excluirRamo(Inscricao.Ramo ramo)
        throws Exception
    {
        SQLUpdate delete = getModelManager().createSQLUpdate("crm", "delete from inscricao_ramo where id=? and seq=?");
        delete.addLong(obterId());
        delete.addInt(ramo.obterSeq());
        delete.execute();
    }

    public Collection obterNomeRamos()
        throws Exception
    {
        Collection nomeRamos = new ArrayList();
        SQLQuery query = getModelManager().createSQLQuery("crm", "select nome from inscricao_ramo group by nome");
        SQLRow rows[] = query.execute();
        for(int i = 0; i < rows.length; i++)
            nomeRamos.add(rows[i].getString("nome"));

        return nomeRamos;
    }
}
