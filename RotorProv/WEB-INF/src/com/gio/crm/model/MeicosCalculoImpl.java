// FrontEnd Plus GUI for JAD
// DeCompiled : MeicosCalculoImpl.class

package com.gio.crm.model;

import infra.sql.SQLQuery;
import infra.sql.SQLUpdate;

// Referenced classes of package com.gio.crm.model:
//            EventoImpl, MeicosCalculo

public class MeicosCalculoImpl extends EventoImpl
    implements MeicosCalculo
{

    private double valorIndicador;

    public MeicosCalculoImpl()
    {
    }

    public void atribuirValor(double valor)
        throws Exception
    {
        valorIndicador = valor;
    }

    public void atualizarValorIndicador(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("update meicos_calculo set valor = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void incluir()
        throws Exception
    {
        super.incluir();
        SQLUpdate insert = getModelManager().createSQLUpdate("insert into meicos_calculo(id, valor) values(?, ?)");
        insert.addLong(obterId());
        insert.addDouble(valorIndicador);
        insert.execute();
    }

    public double obterValorIndicador()
        throws Exception
    {
        double valor = 0.0D;
        SQLQuery query = getModelManager().createSQLQuery("crm", "select valor from evento,meicos_calculo where evento.id = meicos_calculo.id and meicos_calculo.id = ?");
        query.addLong(obterId());
        valor = query.executeAndGetFirstRow().getDouble("valor");
        return valor;
    }
}
