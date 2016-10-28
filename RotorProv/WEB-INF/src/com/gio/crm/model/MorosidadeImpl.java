// FrontEnd Plus GUI for JAD
// DeCompiled : MorosidadeImpl.class

package com.gio.crm.model;

import java.util.Date;

import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

// Referenced classes of package com.gio.crm.model:
//            EventoImpl, Morosidade, Aseguradora, Apolice, 
//            ClassificacaoContas, EventoHome, Evento

public class MorosidadeImpl extends EventoImpl
    implements Morosidade
{

    private Date dataCorte;
    private int numeroParcela;
    private Date dataVencimento;
    private int diasAtraso;
    private double valorGs;
    private String tipoMoedaValorGs;
    private double valorMe;
    private String tipoInstrumento;
    private double numeroEndoso;
    private double certificado;

    public MorosidadeImpl()
    {
    }

    public void atribuirDataCorte(Date data)
        throws Exception
    {
        dataCorte = data;
    }

    public void atribuirNumeroParcela(int numero)
        throws Exception
    {
        numeroParcela = numero;
    }

    public void atribuirDataVencimento(Date data)
        throws Exception
    {
        dataVencimento = data;
    }

    public void atribuirDiasAtraso(int dias)
        throws Exception
    {
        diasAtraso = dias;
    }

    public void atribuirValorGs(double valor)
        throws Exception
    {
        valorGs = valor;
    }

    public void atribuirTipoMoedaValorGs(String tipo)
        throws Exception
    {
        tipoMoedaValorGs = tipo;
    }

    public void atribuirValorMe(double valor)
        throws Exception
    {
        valorMe = valor;
    }

    public void atribuirTipoInstrumento(String tipo)
        throws Exception
    {
        tipoInstrumento = tipo;
    }

    public void atribuirNumeroEndoso(double numeroEndoso)
        throws Exception
    {
        this.numeroEndoso = numeroEndoso;
    }

    public void atribuirCertificado(double certificado)
        throws Exception
    {
        this.certificado = certificado;
    }

    public void atualizarDataCorte(Date data)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update morosidade set data_corte = ? where id = ?");
        update.addLong(data.getTime());
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarNumeroParcela(int numero)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update morosidade set numero_parcela = ? where id = ?");
        update.addInt(numero);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarDataVencimento(Date data)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update morosidade set data_vencimento = ? where id = ?");
        update.addLong(data.getTime());
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarDiasAtraso(int dias)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update morosidade set dias_atraso = ? where id = ?");
        update.addInt(dias);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarValorGs(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update morosidade set valor_gs = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarTipoMoedaValorGs(String tipo)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update morosidade set tipo_moeda_valor_gs = ? where id = ?");
        update.addString(tipo);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarValorMe(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update morosidade set valor_me = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public synchronized void incluir()
        throws Exception
    {
    	 super.incluir();
         
         SQLUpdate insert = this.getModelManager().createSQLUpdate("crm", "EXEC incluirMorosidade ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?");
         insert.addLong(this.obterId());
         if(dataCorte != null)
             insert.addLong(dataCorte.getTime());
         else
             insert.addLong(0L);
         insert.addInt(numeroParcela);
         if(dataVencimento != null)
             insert.addLong(dataVencimento.getTime());
         else
             insert.addLong(0L);
         insert.addInt(diasAtraso);
         insert.addDouble(valorGs);
         insert.addString(tipoMoedaValorGs);
         insert.addDouble(valorMe);
         insert.addString(tipoInstrumento);
         insert.addDouble(numeroEndoso);
         insert.addDouble(certificado);
         insert.execute();
    }

    public Date obterDataCorte()
        throws Exception
    {
        if(dataCorte == null)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select data_corte from morosidade where id = ?");
            query.addLong(obterId());
            long dataLong = query.executeAndGetFirstRow().getLong("data_corte");
            if(dataLong != 0L)
                dataCorte = new Date(dataLong);
        }
        return dataCorte;
    }

    public int obterNumeroParcela()
        throws Exception
    {
        if(numeroParcela == 0)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select numero_parcela from morosidade where id = ?");
            query.addLong(obterId());
            numeroParcela = query.executeAndGetFirstRow().getInt("numero_parcela");
        }
        return numeroParcela;
    }

    public Date obterDataVencimento()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select data_vencimento from morosidade where id = ?");
        query.addLong(obterId());
        Date data = null;
        long dataLong = query.executeAndGetFirstRow().getLong("data_vencimento");
        if(dataLong > 0L)
            data = new Date(dataLong);
        return data;
    }

    public int obterDiasAtraso()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select dias_atraso from morosidade where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getInt("dias_atraso");
    }

    public double obterValorGs()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select valor_gs from morosidade where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("valor_gs");
    }

    public String obterTipoMoedaValorGs()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select tipo_moeda_valor_gs from morosidade where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("tipo_moeda_valor_gs");
    }

    public double obterValorMe()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select valor_me from morosidade where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("valor_me");
    }

    public String obterTipoInstrumento()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select tipo_instrumento from morosidade where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("tipo_instrumento");
    }

    public double obterNumeroEndoso()
        throws Exception
    {
        if(numeroEndoso == 0.0D)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select numero_endoso from morosidade where id = ?");
            query.addLong(obterId());
            numeroEndoso = query.executeAndGetFirstRow().getDouble("numero_endoso");
        }
        return numeroEndoso;
    }

    public double obterCertificado()
        throws Exception
    {
        if(certificado == 0.0D)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select certificado from morosidade where id = ?");
            query.addLong(obterId());
            certificado = query.executeAndGetFirstRow().getDouble("certificado");
        }
        return certificado;
    }

    public void verificarDuplicidade(Aseguradora aseguradora, Apolice apolice, ClassificacaoContas cContas, Date dataCorte, double numeroEndoso, double certificado, int numeroParcela)
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select evento.id from evento,morosidade,apolice where evento.id = morosidade.id and origem=? and superior = apolice.id and superior = ? and  status_apolice = ? and secao = ? and data_corte = ? and morosidade.numero_endoso = ? and morosidade.certificado = ? and numero_parcela = ? group by evento.id");
        query.addLong(aseguradora.obterId());
        query.addLong(apolice.obterId());
        query.addString(apolice.obterStatusApolice());
        query.addLong(cContas.obterId());
        query.addLong(dataCorte.getTime());
        query.addDouble(numeroEndoso);
        query.addDouble(certificado);
        query.addInt(numeroParcela);
        SQLRow rows[] = query.execute();
        for(int i = 0; i < rows.length; i++)
        {
            long id = rows[i].getLong("id");
            EventoHome home = (EventoHome)getModelManager().getHome("EventoHome");
            Evento e = home.obterEventoPorId(id);
            e.excluir();
        }

    }
}
