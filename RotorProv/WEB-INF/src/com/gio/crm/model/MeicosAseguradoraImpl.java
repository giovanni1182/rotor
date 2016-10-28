// FrontEnd Plus GUI for JAD
// DeCompiled : MeicosAseguradoraImpl.class

package com.gio.crm.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

// Referenced classes of package com.gio.crm.model:
//            EventoImpl, MeicosAseguradora, Evento, MeicosCalculo

public class MeicosAseguradoraImpl extends EventoImpl
    implements MeicosAseguradora
{
    public class IndicadorImpl
        implements MeicosAseguradora.Indicador
    {

        private MeicosAseguradoraImpl meicos;
        private int seq;
        private String descricao;
        private int peso;
        private boolean marcado;
        private boolean excludente;

        public void atualizar(String marcado)
            throws Exception
        {
            SQLUpdate update = meicos.getModelManager().createSQLUpdate("crm", "update aseguradora_indicadores set marcado = ? where id = ? and seq = ?");
            update.addString(marcado);
            update.addLong(meicos.obterId());
            update.addInt(seq);
            update.execute();
        }

        public MeicosAseguradora obterMeicosAseguradora()
            throws Exception
        {
            return meicos;
        }

        public int obterSequencial()
            throws Exception
        {
            return seq;
        }

        public String obterDescricao()
            throws Exception
        {
            return descricao;
        }

        public int obterPeso()
            throws Exception
        {
            return peso;
        }

        public boolean estaMarcado()
            throws Exception
        {
            return marcado;
        }

        public boolean eExcludente()
            throws Exception
        {
            return excludente;
        }

        public IndicadorImpl(MeicosAseguradoraImpl meicos, int seq, String descricao, int peso, boolean marcado, boolean excludente)
            throws Exception
        {
            this.meicos = meicos;
            this.seq = seq;
            this.descricao = descricao;
            this.peso = peso;
            this.marcado = marcado;
            this.excludente = excludente;
        }
    }

    public class ControleDocumentoImpl
        implements MeicosAseguradora.ControleDocumento
    {

        private MeicosAseguradoraImpl meicos;
        private int seq;
        private String descricao;
        private Date dataEntrega;
        private Date dataLimite;

        public void atualizar(Date data)
            throws Exception
        {
            SQLUpdate update = meicos.getModelManager().createSQLUpdate("crm", "update aseguradora_documentos set data_entrega = ? where id = ? and seq = ?");
            update.addLong(data.getTime());
            update.addLong(meicos.obterId());
            update.addInt(seq);
            update.execute();
        }

        public MeicosAseguradora obterMeicosAseguradora()
            throws Exception
        {
            return meicos;
        }

        public int obterSequencial()
            throws Exception
        {
            return seq;
        }

        public String obterDescricao()
            throws Exception
        {
            return descricao;
        }

        public Date obterDataEntrega()
            throws Exception
        {
            return dataEntrega;
        }

        public Date obterDataLimite()
            throws Exception
        {
            return dataLimite;
        }

        public ControleDocumentoImpl(MeicosAseguradoraImpl meicos, int seq, String descricao, Date dataEntrega, Date dataLimite)
            throws Exception
        {
            this.meicos = meicos;
            this.seq = seq;
            this.descricao = descricao;
            this.dataEntrega = dataEntrega;
            this.dataLimite = dataLimite;
        }
    }


    public MeicosAseguradoraImpl()
    {
    }

    public Map obterIndicadores()
        throws Exception
    {
        Map indicadores = new TreeMap();
        SQLQuery query = getModelManager().createSQLQuery("crm", "select * from aseguradora_indicadores where id = ?");
        query.addLong(obterId());
        SQLRow rows[] = query.execute();
        for(int i = 0; i < rows.length; i++)
        {
            int seq = rows[i].getInt("seq");
            String descricao = rows[i].getString("descricao");
            int peso = rows[i].getInt("peso");
            boolean marcado = false;
            boolean excludente = false;
            String marcado2 = rows[i].getString("marcado");
            if(marcado2 != null && marcado2.equals("Sim"))
                marcado = true;
            if(rows[i].getString("excludente") != null && rows[i].getString("excludente").equals("Sim"))
                excludente = true;
            indicadores.put(new Integer(seq), new IndicadorImpl(this, seq, descricao, peso, marcado, excludente));
        }

        return indicadores;
    }

    public MeicosAseguradora.Indicador obterIndicador(int seq)
        throws Exception
    {
        return (MeicosAseguradora.Indicador)obterIndicadores().get(new Integer(seq));
    }

    public void adicionarIndicador(String descricao, int peso, boolean excludente)
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("select max(seq) as MX from aseguradora_indicadores where id = ?");
        query.addLong(obterId());
        long seq = query.executeAndGetFirstRow().getLong("MX") + 1L;
        SQLUpdate insert = getModelManager().createSQLUpdate("insert into aseguradora_indicadores (id, seq, descricao, peso, excludente) values (?, ?, ?, ?, ?)");
        insert.addLong(obterId());
        insert.addLong(seq);
        insert.addString(descricao);
        insert.addInt(peso);
        if(excludente)
            insert.addString("Sim");
        else
            insert.addString("N\343o");
        insert.execute();
    }

    public Map obterDocumentos()
        throws Exception
    {
        Map documentos = new TreeMap();
        SQLQuery query = getModelManager().createSQLQuery("crm", "select * from aseguradora_documentos where id = ?");
        query.addLong(obterId());
        SQLRow rows[] = query.execute();
        for(int i = 0; i < rows.length; i++)
        {
            int seq = rows[i].getInt("seq");
            String descricao = rows[i].getString("descricao");
            Date dataEntrega = null;
            Date dataLimite = null;
            long dataEntregaLong = rows[i].getLong("data_entrega");
            long dataLimiteLong = rows[i].getLong("data_limite");
            if(dataEntregaLong != 0L)
                dataEntrega = new Date(dataEntregaLong);
            if(dataLimiteLong != 0L)
                dataLimite = new Date(dataLimiteLong);
            documentos.put(new Integer(seq), new ControleDocumentoImpl(this, seq, descricao, dataEntrega, dataLimite));
        }

        return documentos;
    }

    public MeicosAseguradora.ControleDocumento obterDocumento(int seq)
        throws Exception
    {
        return (MeicosAseguradora.ControleDocumento)obterDocumentos().get(new Integer(seq));
    }

    public void adicionarDocumento(String descricao, Date dataLimite)
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("select max(seq) as MX from aseguradora_documentos where id = ?");
        query.addLong(obterId());
        long seq = query.executeAndGetFirstRow().getLong("MX") + 1L;
        SQLUpdate insert = getModelManager().createSQLUpdate("insert into aseguradora_documentos (id, seq, descricao, data_limite) values (?, ?, ?, ?)");
        insert.addLong(obterId());
        insert.addLong(seq);
        insert.addString(descricao);
        insert.addLong(dataLimite.getTime());
        insert.execute();
    }

    public Collection obterMeicosCalculos()
        throws Exception
    {
        Collection calculos = new ArrayList();
        for(Iterator i = obterInferiores().iterator(); i.hasNext();)
        {
            Evento e = (Evento)i.next();
            if(e instanceof MeicosCalculo)
                calculos.add(e);
        }

        return calculos;
    }
}
