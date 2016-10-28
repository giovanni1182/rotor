// FrontEnd Plus GUI for JAD
// DeCompiled : RatioTresAnosImpl.class

package com.gio.crm.model;

import infra.sql.SQLQuery;
import infra.sql.SQLUpdate;

// Referenced classes of package com.gio.crm.model:
//            EventoImpl, RatioTresAnos

public class RatioTresAnosImpl extends EventoImpl
    implements RatioTresAnos
{

    private double sinistrosPagos;
    private double gastosSinistros;
    private double sinistrosRecuperados;
    private double gastosRecuperados;
    private double recuperoSinistros;
    private double provisoes;

    public RatioTresAnosImpl()
    {
    }

    public void atribuirSinistrosPagos(double valor)
        throws Exception
    {
        sinistrosPagos = valor;
    }

    public void atribuirGastosSinistros(double valor)
        throws Exception
    {
        gastosSinistros = valor;
    }

    public void atribuirSinistrosRecuperados(double valor)
        throws Exception
    {
        sinistrosRecuperados = valor;
    }

    public void atribuirGastosRecuperados(double valor)
        throws Exception
    {
        gastosRecuperados = valor;
    }

    public void atribuirRecuperoSinistros(double valor)
        throws Exception
    {
        recuperoSinistros = valor;
    }

    public void atribuirProvisoes(double valor)
        throws Exception
    {
        provisoes = valor;
    }

    public void atualizarSinistrosPagos(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("update ratio_tres_anos set sinistros_pagos = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarGastosSinistros(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("update ratio_tres_anos set gastos_sinistros = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarSinistrosRecuperados(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("update ratio_tres_anos set sinistros_recuperados = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarGastosRecuperados(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("update ratio_tres_anos set gastos_recuperados = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarRecuperoSinistros(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("update ratio_tres_anos set recuperado_sinistro = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarProvisoes(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("update ratio_tres_anos set provisoes = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void incluir()
        throws Exception
    {
        super.incluir();
        SQLUpdate insert = getModelManager().createSQLUpdate("insert into ratio_tres_anos values(?, ?, ?, ?, ?, ?, ?)");
        insert.addLong(obterId());
        insert.addDouble(sinistrosPagos);
        insert.addDouble(gastosSinistros);
        insert.addDouble(sinistrosRecuperados);
        insert.addDouble(gastosRecuperados);
        insert.addDouble(recuperoSinistros);
        insert.addDouble(provisoes);
        insert.execute();
    }

    public double obterSinistrosPagos()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("select sinistros_pagos from ratio_tres_anos where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("sinistros_pagos");
    }

    public double obterGastosSinistros()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("select gastos_sinistros from ratio_tres_anos where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("gastos_sinistros");
    }

    public double obterSinistrosRecuperados()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("select sinistros_recuperados from ratio_tres_anos where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("sinistros_recuperados");
    }

    public double obterGastosRecuperados()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("select gastos_recuperados from ratio_tres_anos where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("gastos_recuperados");
    }

    public double obterRecuperoSinistros()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("select recuperado_sinistro from ratio_tres_anos where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("recuperado_sinistro");
    }

    public double obterProvisoes()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("select provisoes from ratio_tres_anos where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("provisoes");
    }
}
