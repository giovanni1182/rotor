// FrontEnd Plus GUI for JAD
// DeCompiled : PlanoImpl.class

package com.gio.crm.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

// Referenced classes of package com.gio.crm.model:
//            EventoImpl, Plano, EventoHome

public class PlanoImpl extends EventoImpl
    implements Plano
{

    public PlanoImpl()
    {
    }

    public void atualizarRamo(String ramo)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update plano set ramo=? where id=?");
        update.addString(ramo);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarSecao(String secao)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update plano set secao=? where id=?");
        update.addString(secao);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarPlano(String plano)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update plano set plano=? where id=?");
        update.addString(plano);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarIdentificador(String identificador)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update plano set identificador=? where id=?");
        update.addString(identificador);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarResolucao(String resolucao)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update plano set resolucao=? where id=?");
        update.addString(resolucao);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarDataResolucao(Date data)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update plano set data_resolucao=? where id=?");
        update.addLong(data.getTime());
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarSituacao(String situacao)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update plano set situacao=? where id=?");
        update.addString(situacao);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarDenominacao(String denominacao)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update plano set denominacao=? where id=?");
        update.addString(denominacao);
        update.addLong(obterId());
        update.execute();
    }

    public void incluir()
        throws Exception
    {
        super.incluir();
        SQLUpdate insert = getModelManager().createSQLUpdate("crm", "insert into plano(id) values(?)");
        insert.addLong(obterId());
        insert.execute();
    }

    public String obterRamo()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select ramo from plano where id=?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("ramo");
    }

    public String obterSecao()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select secao from plano where id=?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("secao");
    }

    public String obterPlano()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select plano from plano where id=?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("plano");
    }

    public String obterDenominacao()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select denominacao from plano where id=?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("denominacao");
    }

    public String obterIdentificador()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select identificador from plano where id=?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("identificador");
    }

    public String obterResolucao()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select resolucao from plano where id=?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("resolucao");
    }

    public Date obterDataResolucao()  throws Exception
    {
        /*SQLQuery query = getModelManager().createSQLQuery("crm", "select data_resolucao from plano where id=?");
        query.addLong(obterId());*/
        
    	SQLQuery query = getModelManager().createSQLQuery("crm", "EXEC obterDataResolucaoPlano "+this.obterId());
        
        long dataLong = query.executeAndGetFirstRow().getLong("data_resolucao");
        Date data = null;
        if(dataLong > 0L)
            data = new Date(dataLong);
        return data;
    }

    public String obterSituacao()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select situacao from plano where id=?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("situacao");
    }

    public void adicionarNovoRamo(String ramo)
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select MAX(seq) as MX from plano_ramos where id=?");
        query.addLong(obterId());
        int id = query.executeAndGetFirstRow().getInt("MX") + 1;
        SQLUpdate insert = getModelManager().createSQLUpdate("crm", "insert into plano_ramos(id, seq, nome) values (?, ?, ?)");
        insert.addLong(obterId());
        insert.addInt(id);
        insert.addString(ramo);
        insert.execute();
    }

    public Collection obterNomeRamos()
        throws Exception
    {
        Collection nomeRamos = new ArrayList();
        SQLQuery query = getModelManager().createSQLQuery("crm", "select nome from plano_ramos group by nome");
        SQLRow rows[] = query.execute();
        for(int i = 0; i < rows.length; i++)
            nomeRamos.add(rows[i].getString("nome"));

        return nomeRamos;
    }

    public void adicionarNovaSecao(String secao)
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select MAX(seq) as MX from plano_secao where id=?");
        query.addLong(obterId());
        int id = query.executeAndGetFirstRow().getInt("MX") + 1;
        SQLUpdate insert = getModelManager().createSQLUpdate("crm", "insert into plano_secao(id, seq, nome) values (?, ?, ?)");
        insert.addLong(obterId());
        insert.addInt(id);
        insert.addString(secao);
        insert.execute();
    }

    public Collection obterNomeSecoes()
        throws Exception
    {
        Collection secoes = new ArrayList();
        SQLQuery query = getModelManager().createSQLQuery("crm", "select nome from plano_secao group by nome");
        SQLRow rows[] = query.execute();
        for(int i = 0; i < rows.length; i++)
            secoes.add(rows[i].getString("nome"));

        return secoes;
    }

    public void adicionarNovoPlano(String plano)
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select MAX(seq) as MX from plano_planos where id=?");
        query.addLong(obterId());
        int id = query.executeAndGetFirstRow().getInt("MX") + 1;
        SQLUpdate insert = getModelManager().createSQLUpdate("crm", "insert into plano_planos(id, seq, nome) values (?, ?, ?)");
        insert.addLong(obterId());
        insert.addInt(id);
        insert.addString(plano);
        insert.execute();
    }

    public Collection obterNomePlanos()
        throws Exception
    {
        Collection modalidades = new ArrayList();
        SQLQuery query = getModelManager().createSQLQuery("crm", "select nome from plano_planos group by nome");
        SQLRow rows[] = query.execute();
        for(int i = 0; i < rows.length; i++)
            modalidades.add(rows[i].getString("nome"));

        return modalidades;
    }

    public void verificarPlano(String plano)
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select identificador from plano where identificador=? and id<>?");
        query.addString(plano);
        query.addLong(obterId());
        SQLRow rows[] = query.execute();
        if(rows.length > 0)
            throw new Exception("O Plano N\272 " + plano + " j\341 est\341 sendo usado");
        else
            return;
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
            return true;
        }
    }
}
