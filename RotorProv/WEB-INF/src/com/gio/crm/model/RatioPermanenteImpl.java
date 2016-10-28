// FrontEnd Plus GUI for JAD
// DeCompiled : RatioPermanenteImpl.class

package com.gio.crm.model;

import infra.sql.SQLQuery;
import infra.sql.SQLUpdate;

// Referenced classes of package com.gio.crm.model:
//            EventoImpl, RatioPermanente

public class RatioPermanenteImpl extends EventoImpl
    implements RatioPermanente
{

    private double ativoCorrente;
    private double passivoCorrente;
    private double inversao;
    private double deudas;
    private double uso;
    private double venda;
    private double leasing;
    private double resultados;

    public RatioPermanenteImpl()
    {
    }

    public void atribuirAtivoCorrente(double valor)
        throws Exception
    {
        ativoCorrente = valor;
    }

    public void atribuirPassivoCorrente(double valor)
        throws Exception
    {
        passivoCorrente = valor;
    }

    public void atribuirInversao(double valor)
        throws Exception
    {
        inversao = valor;
    }

    public void atribuirDeudas(double valor)
        throws Exception
    {
        deudas = valor;
    }

    public void atribuirUso(double valor)
        throws Exception
    {
        uso = valor;
    }

    public void atribuirVenda(double valor)
        throws Exception
    {
        venda = valor;
    }

    public void atribuirLeasing(double valor)
        throws Exception
    {
        leasing = valor;
    }

    public void atribuirResultados(double valor)
        throws Exception
    {
        resultados = valor;
    }

    public void atualizarAtivoCorrente(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update ratio_permanente set ativo_corrente = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarPassivoCorrente(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update ratio_permanente set passivo_corrente = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarInversao(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update ratio_permanente set inversao = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarDeudas(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update ratio_permanente set deudas = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarUso(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update ratio_permanente set uso = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarVenda(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update ratio_permanente set venda = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarLeasing(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update ratio_permanente set leasing = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarResultados(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update ratio_permanente set resultado = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void incluir()
        throws Exception
    {
        super.incluir();
        SQLUpdate insert = getModelManager().createSQLUpdate("crm", "insert ratio_permanente values(?, ?, ?, ?, ?, ?, ?, ?, ?)");
        insert.addLong(obterId());
        insert.addDouble(ativoCorrente);
        insert.addDouble(passivoCorrente);
        insert.addDouble(inversao);
        insert.addDouble(deudas);
        insert.addDouble(uso);
        insert.addDouble(venda);
        insert.addDouble(leasing);
        insert.addDouble(resultados);
        insert.execute();
    }

    public double obterAtivoCorrente()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select ativo_corrente from ratio_permanente where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("ativo_corrente");
    }

    public double obterPassivoCorrente()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select passivo_corrente from ratio_permanente where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("passivo_corrente");
    }

    public double obterInversao()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select inversao from ratio_permanente where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("inversao");
    }

    public double obterDeudas()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select deudas from ratio_permanente where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("deudas");
    }

    public double obterUso()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select uso from ratio_permanente where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("uso");
    }

    public double obterVenda()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select venda from ratio_permanente where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("venda");
    }

    public double obterLeasing()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select leasing from ratio_permanente where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("leasing");
    }

    public double obterResultados()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select resultado from ratio_permanente where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("resultado");
    }
}
