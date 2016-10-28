// FrontEnd Plus GUI for JAD
// DeCompiled : RenovacaoImpl.class

package com.gio.crm.model;

import infra.sql.SQLQuery;
import infra.sql.SQLUpdate;

// Referenced classes of package com.gio.crm.model:
//            EventoImpl, Renovacao

public class RenovacaoImpl extends EventoImpl
    implements Renovacao
{

    public RenovacaoImpl()
    {
    }

    public void atualizarMatriculaAnterior(int arg)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update renovacao set matricula_anterior=? where id=?");
        update.addInt(arg);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarCertificadoAntecedentes(int arg)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update renovacao set certificado_antecedentes=? where id=?");
        update.addInt(arg);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarCertificadoJudicial(int arg)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update renovacao set cetificado_judicial=? where id=?");
        update.addInt(arg);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarCertificadoTributario(int arg)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update renovacao set certificado_tributario=? where id=?");
        update.addInt(arg);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarDeclaracao(int arg)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update renovacao set declaracao=? where id=?");
        update.addInt(arg);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarComprovanteMatricula(int arg)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update renovacao set comprovante_matricula=? where id=?");
        update.addInt(arg);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarApoliceSeguro(int arg)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update renovacao set apolice_seguro=? where id=?");
        update.addInt(arg);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarLivro(int arg)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update renovacao set livro=? where id=?");
        update.addInt(arg);
        update.addLong(obterId());
        update.execute();
    }

    public void incluir()
        throws Exception
    {
        super.incluir();
        SQLUpdate insert = getModelManager().createSQLUpdate("crm", "insert into renovacao(id) values(?)");
        insert.addLong(obterId());
        insert.execute();
    }

    public boolean obterMatriculaAnterior()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select matricula_anterior from renovacao where id=?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getInt("matricula_anterior") != 0;
    }

    public boolean obterCertificadoAntecedentes()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select certificado_antecedentes from renovacao where id=?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getInt("certificado_antecedentes") != 0;
    }

    public boolean obterCertificadoJudicial()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select cetificado_judicial from renovacao where id=?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getInt("cetificado_judicial") != 0;
    }

    public boolean obterCertificadoTributario()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select certificado_tributario from renovacao where id=?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getInt("certificado_tributario") != 0;
    }

    public boolean obterDeclaracao()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select declaracao from renovacao where id=?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getInt("declaracao") != 0;
    }

    public boolean obterComprovanteMatricula()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select comprovante_matricula from renovacao where id=?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getInt("comprovante_matricula") != 0;
    }

    public boolean obterApoliceSeguro()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select apolice_seguro from renovacao where id=?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getInt("apolice_seguro") != 0;
    }

    public boolean obterLivro()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select livro from renovacao where id=?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getInt("livro") != 0;
    }
}
