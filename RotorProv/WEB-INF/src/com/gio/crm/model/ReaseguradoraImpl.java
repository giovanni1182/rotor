// FrontEnd Plus GUI for JAD
// DeCompiled : ReaseguradoraImpl.class

package com.gio.crm.model;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

// Referenced classes of package com.gio.crm.model:
//            EntidadeImpl, Reaseguradora

public class ReaseguradoraImpl extends EntidadeImpl
    implements Reaseguradora
{
    public class ClassificacaoImpl
        implements Reaseguradora.Classificacao
    {

        private ReaseguradoraImpl reaseguradora;
        private int id;
        private String classificacao;
        private String nivel;
        private String codigo;
        private String qualificacao;
        private Date data;

        public void atualizar(String classificacao, String nivel, String codigo, String qualificacao, Date data)
            throws Exception
        {
            SQLUpdate update = reaseguradora.getModelManager().createSQLUpdate("update reaseguradora_classificacao set classificacao=?, nivel=?, codigo = ?, qualificacao=?, data=? where entidade=? and id=?");
            update.addString(classificacao);
            update.addString(nivel);
            update.addString(codigo);
            update.addString(qualificacao);
            update.addLong(data.getTime());
            update.addLong(reaseguradora.obterId());
            update.addInt(id);
            update.execute();
        }

        public Reaseguradora obterReaseguradora()
            throws Exception
        {
            return reaseguradora;
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

        public String obterClassificacao()
            throws Exception
        {
            return classificacao;
        }

        public String obterNivel()
            throws Exception
        {
            return nivel;
        }

        public String obterQualificacao()
            throws Exception
        {
            return qualificacao;
        }

        public Date obterData()
            throws Exception
        {
            return data;
        }

        ClassificacaoImpl(ReaseguradoraImpl reaseguradora, int id, String classificacao, String nivel, String codigo, String qualificacao, 
                Date data)
            throws Exception
        {
            this.reaseguradora = reaseguradora;
            this.id = id;
            this.classificacao = classificacao;
            this.nivel = nivel;
            this.codigo = codigo;
            this.qualificacao = qualificacao;
            this.data = data;
        }
    }


    private Map classificacoes;

    public ReaseguradoraImpl()
    {
    }

    public void adicionarClassificacao(String classificacao, String nivel, String codigo, String qualificacao, Date data)
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("select max(id) as MX from reaseguradora_classificacao where entidade=?");
        query.addLong(obterId());
        long id = query.executeAndGetFirstRow().getLong("MX") + 1L;
        SQLUpdate insert = getModelManager().createSQLUpdate("insert into reaseguradora_classificacao (entidade, id, classificacao, nivel, codigo, qualificacao, data) values (?, ?, ?, ?, ?, ?, ?)");
        insert.addLong(obterId());
        insert.addLong(id);
        insert.addString(classificacao);
        insert.addString(nivel);
        insert.addString(codigo);
        insert.addString(qualificacao);
        insert.addLong(data.getTime());
        insert.execute();
    }

    public void adicionarClassificacaoNivel(String nivel)
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select max(seq) as MX from reaseguradora_classificacao_nivel where entidade=?");
        query.addLong(obterId());
        long id = query.executeAndGetFirstRow().getLong("MX") + 1L;
        SQLUpdate insert = getModelManager().createSQLUpdate("crm", "insert into reaseguradora_classificacao_nivel(entidade, seq, nivel) values (?, ?, ?)");
        insert.addLong(obterId());
        insert.addLong(id);
        insert.addString(nivel);
        insert.execute();
    }

    public Collection obterNiveis()
        throws Exception
    {
        Map niveis = new TreeMap();
        SQLQuery query = getModelManager().createSQLQuery("crm", "select nivel from reaseguradora_classificacao_nivel group by nivel");
        SQLRow rows[] = query.execute();
        for(int i = 0; i < rows.length; i++)
        {
            String nivel = rows[i].getString("nivel");
            niveis.put(nivel, nivel);
        }

        return niveis.values();
    }

    public Reaseguradora.Classificacao obterClassificacao(int id)
        throws Exception
    {
        obterClassificacoes();
        return (Reaseguradora.Classificacao)classificacoes.get(Integer.toString(id));
    }

    public Collection obterClassificacoes()
        throws Exception
    {
        classificacoes = new TreeMap();
        SQLQuery query = getModelManager().createSQLQuery("crm", "select * from reaseguradora_classificacao where entidade=?");
        query.addLong(obterId());
        SQLRow rows[] = query.execute();
        for(int i = 0; i < rows.length; i++)
        {
            int id = rows[i].getInt("id");
            classificacoes.put(Integer.toString(id), new ClassificacaoImpl(this, id, rows[i].getString("classificacao"), rows[i].getString("nivel"), rows[i].getString("codigo"), rows[i].getString("qualificacao"), new Date(rows[i].getLong("data"))));
        }

        return classificacoes.values();
    }

    public void removerClassificacao(Reaseguradora.Classificacao classificacao)
        throws Exception
    {
        SQLUpdate delete = getModelManager().createSQLUpdate("delete from reaseguradora_classificacao where entidade=? and id=?");
        delete.addLong(obterId());
        delete.addInt(classificacao.obterId());
        delete.execute();
        if(classificacoes != null)
            classificacoes.remove(Integer.toString(classificacao.obterId()));
        SQLUpdate delete2 = getModelManager().createSQLUpdate("delete from reaseguradora_classificacao_nivel where entidade=? and nivel=?");
        delete2.addLong(obterId());
        delete2.addString(classificacao.obterNivel());
        delete2.execute();
    }
}
