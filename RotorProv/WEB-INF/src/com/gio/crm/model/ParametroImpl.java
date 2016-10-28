// FrontEnd Plus GUI for JAD
// DeCompiled : ParametroImpl.class

package com.gio.crm.model;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

// Referenced classes of package com.gio.crm.model:
//            EntidadeImpl, Parametro, Entidade

public class ParametroImpl extends EntidadeImpl
    implements Parametro
{
    public class FeriadoImpl
        implements Parametro.Feriado
    {

        private EntidadeImpl entidade;
        private int id;
        private String descricao;
        private Date data;

        public void atualizarValor(String descricao, Date data)
            throws Exception
        {
            SQLUpdate update = entidade.getModelManager().createSQLUpdate("update feriado set descricao=?, data=? where entidade=? and id=?");
            update.addString(descricao);
            update.addLong(data.getTime());
            update.addLong(entidade.obterId());
            update.addInt(id);
            update.execute();
        }

        public String obterDescricaoFeriado()
            throws Exception
        {
            return descricao;
        }

        public Date obterDataFeriado()
            throws Exception
        {
            return data;
        }

        public Entidade obterEntidade()
            throws Exception
        {
            return entidade;
        }

        public int obterId()
            throws Exception
        {
            return id;
        }

        FeriadoImpl(EntidadeImpl entidade, int id, String descricao, Date data)
        {
            this.entidade = entidade;
            this.id = id;
            this.descricao = descricao;
            this.data = data;
        }
    }

    public class ConsistenciaImpl
        implements Parametro.Consistencia
    {

        private EntidadeImpl entidade;
        private int sequencial;
        private String operando1;
        private String operando2;
        private String operador;
        private String mensagem;
        private int regra;

        public void atualizarValor(String operando1, String operador, String operando2, String mensagem, int regra)
            throws Exception
        {
            SQLUpdate update = entidade.getModelManager().createSQLUpdate("update consistencia set operando1=?, operador=?, operando2=?, mensagem=?, regra=? where entidade=? and sequencial=? and regra=?");
            update.addString(operando1);
            update.addString(operador);
            update.addString(operando2);
            update.addString(mensagem);
            update.addInt(regra);
            update.addLong(entidade.obterId());
            update.addLong(obterSequencial());
            update.addLong(regra);
            update.execute();
        }

        public Entidade obterEntidade()
            throws Exception
        {
            return entidade;
        }

        public int obterSequencial()
            throws Exception
        {
            return sequencial;
        }

        public String obterOperando1()
            throws Exception
        {
            return operando1;
        }

        public String obterOperando2()
            throws Exception
        {
            return operando2;
        }

        public String obterOperador()
            throws Exception
        {
            return operador;
        }

        public String obterMensagem()
            throws Exception
        {
            return mensagem;
        }

        public int obterRegra()
            throws Exception
        {
            return regra;
        }

        ConsistenciaImpl(EntidadeImpl entidade, int sequencial, String operando1, String operador, String operando2, String mensagem, 
                int regra)
        {
            this.entidade = entidade;
            this.sequencial = sequencial;
            this.operando1 = operando1;
            this.operando2 = operando2;
            this.operador = operador;
            this.mensagem = mensagem;
            this.regra = regra;
        }
    }


    private Map dias;
    private Map consistencias;

    public ParametroImpl()
    {
        dias = new TreeMap();
        consistencias = new TreeMap();
    }

    public void adicionarFeriado(String descricao, Date data)
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("select max(id) from feriado where entidade=?");
        query.addLong(obterId());
        long id = query.executeAndGetFirstRow().getLong("max(id)") + 1L;
        SQLUpdate insert = getModelManager().createSQLUpdate("insert into feriado (id, entidade, descricao, data) values (?, ?, ?, ?)");
        insert.addLong(id);
        insert.addLong(obterId());
        insert.addString(descricao);
        insert.addLong(data.getTime());
        insert.execute();
    }

    public void adicionarConsistencia(String operando1, String operador, String operando2, String mensagem, int regra)
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("select max(sequencial) as maximo from consistencia where entidade=?");
        query.addLong(obterId());
        long id = query.executeAndGetFirstRow().getLong("maximo") + 1L;
        SQLUpdate insert = getModelManager().createSQLUpdate("insert into consistencia (entidade, sequencial, operando1, operador, operando2, mensagem, regra) values (?, ?, ?, ?, ?, ?, ?)");
        insert.addLong(obterId());
        insert.addLong(id);
        insert.addString(operando1);
        insert.addString(operador);
        insert.addString(operando2);
        insert.addString(mensagem);
        if(regra == 0)
            regra = 1;
        insert.addInt(regra);
        insert.execute();
    }

    public Parametro.Feriado obterFeriado(int id)
        throws Exception
    {
        obterFeriados();
        return (Parametro.Feriado)dias.get(Integer.toString(id));
    }

    public Parametro.Consistencia obterConsistencia(int id, int regra)
        throws Exception
    {
        obterConsistencias();
        return (Parametro.Consistencia)consistencias.get(new Integer(id) + Integer.toString(regra));
    }

    public Collection obterFeriados()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select id, descricao, entidade, data from feriado where entidade = ?");
        query.addLong(obterId());
        SQLRow rows[] = query.execute();
        for(int i = 0; i < rows.length; i++)
        {
            int id = rows[i].getInt("id");
            dias.put(Integer.toString(id), new FeriadoImpl(this, id, rows[i].getString("descricao"), new Date(rows[i].getLong("data"))));
        }

        return dias.values();
    }

    public Collection<Consistencia> obterConsistencias() throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select entidade, sequencial, operando1, operador, operando2, mensagem, regra  from consistencia where entidade = ? and regra=? order by sequencial");
        query.addLong(obterId());
        query.addInt(1);
        SQLRow rows[] = query.execute();
        for(int i = 0; i < rows.length; i++)
        {
            int id = rows[i].getInt("sequencial");
            int regra = rows[i].getInt("regra");
            consistencias.put(new Integer(id) + Integer.toString(regra), new ConsistenciaImpl(this, id, rows[i].getString("operando1"), rows[i].getString("operador"), rows[i].getString("operando2"), rows[i].getString("mensagem"), rows[i].getInt("regra")));
        }

        return consistencias.values();
    }

    public void removerFeriado(Parametro.Feriado feriado)
        throws Exception
    {
        SQLUpdate delete = getModelManager().createSQLUpdate("delete from feriado where entidade=? and id=?");
        delete.addLong(obterId());
        delete.addInt(feriado.obterId());
        delete.execute();
        if(dias != null)
            dias.remove(Integer.toString(feriado.obterId()));
    }

    public void removerConsistencia(Parametro.Consistencia consistencia, int regra)
        throws Exception
    {
        SQLUpdate delete = getModelManager().createSQLUpdate("delete from consistencia where entidade=? and sequencial=? and regra=?");
        delete.addLong(obterId());
        delete.addInt(consistencia.obterSequencial());
        delete.addInt(regra);
        delete.execute();
        if(consistencias != null)
            consistencias.remove(Integer.toString(consistencia.obterSequencial()));
    }
}
