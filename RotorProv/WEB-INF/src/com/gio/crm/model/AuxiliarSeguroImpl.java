// FrontEnd Plus GUI for JAD
// DeCompiled : AuxiliarSeguroImpl.class

package com.gio.crm.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

// Referenced classes of package com.gio.crm.model:
//            EntidadeImpl, AuxiliarSeguro, Aseguradora, EntidadeHome

public class AuxiliarSeguroImpl extends EntidadeImpl
    implements AuxiliarSeguro
{
    public class RamoImpl
        implements AuxiliarSeguro.Ramo
    {

        private AuxiliarSeguroImpl auxiliar;
        private int seq;
        private String ramo;

        public AuxiliarSeguro obterAuxiliarSeguro()
            throws Exception
        {
            return auxiliar;
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

        public RamoImpl(AuxiliarSeguroImpl auxiliar, int seq, String ramo)
            throws Exception
        {
            this.auxiliar = auxiliar;
            this.seq = seq;
            this.ramo = ramo;
        }
    }


    private Map ramos;

    public AuxiliarSeguroImpl()
    {
    }

    public void atualizarAseguradora(Aseguradora aseguradora)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update auxiliar_seguro set aseguradora=? where id=?");
        update.addLong(aseguradora.obterId());
        update.addLong(obterId());
        update.execute();
    }

    public Aseguradora obterAseguradora()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select aseguradora from auxiliar_seguro where id=?");
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

    public Collection obterAseguradoras()
        throws Exception
    {
        Collection aseguradoras = new ArrayList();
        return aseguradoras;
    }

    public void adicionarNovoRamo(String ramo)
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select MAX(id) as MX from auxiliar_seguro_ramo where entidade=?");
        query.addLong(obterId());
        int id = query.executeAndGetFirstRow().getInt("MX") + 1;
        SQLUpdate insert = getModelManager().createSQLUpdate("crm", "insert into auxiliar_seguro_ramo(entidade, id, nome) values (?, ?, ?)");
        insert.addLong(obterId());
        insert.addInt(id);
        insert.addString(ramo);
        insert.execute();
    }

    public void incluir()
        throws Exception
    {
        super.incluir();
        SQLUpdate insert = getModelManager().createSQLUpdate("crm", "insert into auxiliar_seguro(id) values(?)");
        insert.addLong(obterId());
        insert.execute();
    }

    public Collection obterNomeRamos()
        throws Exception
    {
        Collection nomeRamos = new ArrayList();
        SQLQuery query = getModelManager().createSQLQuery("crm", "select nome from auxiliar_seguro_ramo group by nome");
        SQLRow rows[] = query.execute();
        for(int i = 0; i < rows.length; i++)
            nomeRamos.add(rows[i].getString("nome"));

        return nomeRamos;
    }

    public AuxiliarSeguro.Ramo obterRamo(int seq)
        throws Exception
    {
        obterRamos();
        return (AuxiliarSeguro.Ramo)ramos.get(new Integer(seq));
    }

    public Collection obterRamos()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select * from auxiliar_seguro_ramo where entidade=?");
        query.addLong(obterId());
        SQLRow rows[] = query.execute();
        ramos = new TreeMap();
        for(int i = 0; i < rows.length; i++)
        {
            int seq = rows[i].getInt("id");
            String ramo = rows[i].getString("nome");
            ramos.put(new Integer(seq), new RamoImpl(this, seq, ramo));
        }

        return ramos.values();
    }

    public void excluirRamo(AuxiliarSeguro.Ramo ramo)
        throws Exception
    {
        SQLUpdate delete = getModelManager().createSQLUpdate("crm", "delete from auxiliar_seguro_ramo where entidade=? and id=?");
        delete.addLong(obterId());
        delete.addInt(ramo.obterSeq());
        delete.execute();
    }
}
