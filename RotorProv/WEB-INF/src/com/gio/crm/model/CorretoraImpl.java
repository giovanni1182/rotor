// FrontEnd Plus GUI for JAD
// DeCompiled : CorretoraImpl.class

package com.gio.crm.model;

import java.util.ArrayList;
import java.util.Collection;

import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

// Referenced classes of package com.gio.crm.model:
//            EntidadeImpl, Corretora, Aseguradora, EntidadeHome

public class CorretoraImpl extends EntidadeImpl
    implements Corretora
{

    public CorretoraImpl()
    {
    }

    public void atualizarAseguradora(Aseguradora aseguradora)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update corretora set aseguradora=? where id=?");
        update.addLong(aseguradora.obterId());
        update.addLong(obterId());
        update.execute();
    }

    public Aseguradora obterAseguradora()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select aseguradora from corretora where id=?");
        query.addLong(obterId());
        Aseguradora aseguradora = null;
        long id = query.executeAndGetFirstRow().getLong("aseguradora");
        if(id > 0L)
        {
            EntidadeHome home = (EntidadeHome)getModelManager().getHome("EntidadeHome");
            aseguradora = (Aseguradora)home.obterEntidadePorId(id);
        }
        return aseguradora;
    }

    public void adicionarNovoRamo(String ramo)
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select MAX(id) as MX from corretora_ramo where entidade=?");
        query.addLong(obterId());
        int id = query.executeAndGetFirstRow().getInt("MX") + 1;
        SQLUpdate insert = getModelManager().createSQLUpdate("crm", "insert into corretora_ramo(entidade, id, nome) values (?, ?, ?)");
        insert.addLong(obterId());
        insert.addInt(id);
        insert.addString(ramo);
        insert.execute();
    }

    public void atualizarRamo(String ramo)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update corretora set ramo=? where id=?");
        update.addString(ramo);
        update.addLong(obterId());
        update.execute();
    }

    public String obterRamo()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select ramo from corretora where id=?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("ramo");
    }

    public void incluir()
        throws Exception
    {
        super.incluir();
        SQLUpdate insert = getModelManager().createSQLUpdate("crm", "insert into corretora(id) values(?)");
        insert.addLong(obterId());
        insert.execute();
    }

    public Collection obterNomeRamos()
        throws Exception
    {
        Collection nomeRamos = new ArrayList();
        SQLQuery query = getModelManager().createSQLQuery("crm", "select nome from corretora_ramo group by nome");
        SQLRow rows[] = query.execute();
        for(int i = 0; i < rows.length; i++)
            nomeRamos.add(rows[i].getString("nome"));

        return nomeRamos;
    }
}
