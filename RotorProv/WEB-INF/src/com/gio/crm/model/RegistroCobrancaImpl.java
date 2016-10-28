// FrontEnd Plus GUI for JAD
// DeCompiled : RegistroCobrancaImpl.class

package com.gio.crm.model;

import java.util.Date;

import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

// Referenced classes of package com.gio.crm.model:
//            EventoImpl, RegistroCobranca, Aseguradora, Apolice, 
//            ClassificacaoContas, EventoHome, Evento

public class RegistroCobrancaImpl extends EventoImpl
    implements RegistroCobranca
{

    private Date dataCobranca;
    private Date dataVencimento;
    private int numeroParcela;
    private double valorCobrancaGs;
    private String tipoMoedaValorCobrancaGs;
    private double valorCobrancaMe;
    private double valorInteres;
    private String tipoInstrumento;
    private double numeroEndoso;
    private double certificado;

    public RegistroCobrancaImpl()
    {
    }

    public void atribuirDataCobranca(Date data)
        throws Exception
    {
        dataCobranca = data;
    }

    public void atribuirDataVencimento(Date data)
        throws Exception
    {
        dataVencimento = data;
    }

    public void atribuirNumeroParcela(int numero)
        throws Exception
    {
        numeroParcela = numero;
    }

    public void atribuirValorCobrancaGs(double valor)
        throws Exception
    {
        valorCobrancaGs = valor;
    }

    public void atribuirTipoMoedaValorCobrancaGs(String tipo)
        throws Exception
    {
        tipoMoedaValorCobrancaGs = tipo;
    }

    public void atribuirValorCobrancaMe(double valor)
        throws Exception
    {
        valorCobrancaMe = valor;
    }

    public void atribuirValorInteres(double valor)
        throws Exception
    {
        valorInteres = valor;
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

    public void atualizarDataCobranca(Date data)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update registro_cobranca set data_cobranca = ? where id = ?");
        update.addLong(data.getTime());
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarDataVencimento(Date data)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update registro_cobranca set data_vencimento = ? where id = ?");
        update.addLong(data.getTime());
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarNumeroParcela(int numero)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update registro_cobranca set numero_parcela = ? where id = ?");
        update.addInt(numero);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarValorCobrancaGs(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update registro_cobranca set valor_cobranca_gs = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarTipoMoedaValorCobrancaGs(String tipo)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update registro_cobranca set tipo_moeda_valor_cobranca_gs = ? where id = ?");
        update.addString(tipo);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarValorCobrancaMe(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update registro_cobranca set valor_cobranca_me = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarValorInteres(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update registro_cobranca set valor_interes = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public synchronized void incluir() throws Exception
    {
    	super.incluir();
        
        SQLUpdate insert = this.getModelManager().createSQLUpdate("crm", "EXEC incluirRegistroCobranca ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?");
        insert.addLong(this.obterId());
        if(dataCobranca != null)
            insert.addLong(dataCobranca.getTime());
        else
            insert.addLong(0L);
        if(dataVencimento != null)
            insert.addLong(dataVencimento.getTime());
        else
            insert.addLong(0L);
        insert.addInt(numeroParcela);
        insert.addDouble(valorCobrancaGs);
        insert.addString(tipoMoedaValorCobrancaGs);
        insert.addDouble(valorCobrancaMe);
        insert.addDouble(valorInteres);
        insert.addString(tipoInstrumento);
        insert.addDouble(numeroEndoso);
        insert.addDouble(certificado);
        insert.execute();
    }

    public Date obterDataCobranca()
        throws Exception
    {
        if(dataCobranca == null)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select data_cobranca from registro_cobranca where id = ?");
            query.addLong(obterId());
            long dataLong = query.executeAndGetFirstRow().getLong("data_cobranca");
            if(dataLong > 0L)
                dataCobranca = new Date(dataLong);
        }
        return dataCobranca;
    }

    public Date obterDataVencimento()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select data_vencimento from registro_cobranca where id = ?");
        query.addLong(obterId());
        Date data = null;
        long dataLong = query.executeAndGetFirstRow().getLong("data_vencimento");
        if(dataLong > 0L)
            data = new Date(dataLong);
        return data;
    }

    public int obterNumeroParcela()
        throws Exception
    {
        if(numeroParcela == 0)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select numero_parcela from registro_cobranca where id = ?");
            query.addLong(obterId());
            numeroParcela = query.executeAndGetFirstRow().getInt("numero_parcela");
        }
        return numeroParcela;
    }

    public double obterValorCobrancaGs()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select valor_cobranca_gs from registro_cobranca where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("valor_cobranca_gs");
    }

    public String obterTipoMoedaValorCobrancaGs()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select tipo_moeda_valor_cobranca_gs from registro_cobranca where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("tipo_moeda_valor_cobranca_gs");
    }

    public double obterValorCobrancaMe()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select valor_cobranca_me from registro_cobranca where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("valor_cobranca_me");
    }

    public double obterValorInteres()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select valor_interes from registro_cobranca where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("valor_interes");
    }

    public String obterTipoInstrumento()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select tipo_instrumento from registro_cobranca where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("tipo_instrumento");
    }

    public double obterNumeroEndoso()
        throws Exception
    {
        if(numeroEndoso == 0.0D)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select numero_endoso from registro_cobranca where id = ?");
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
            SQLQuery query = getModelManager().createSQLQuery("crm", "select certificado from registro_cobranca where id = ?");
            query.addLong(obterId());
            certificado = query.executeAndGetFirstRow().getDouble("certificado");
        }
        return certificado;
    }

    public void verificarDuplicidade(Aseguradora aseguradora, Apolice apolice, ClassificacaoContas cContas, Date dataCobranca, int numeroParcela, double numeroEndoso, 
            double certificado)
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select evento.id from evento,registro_cobranca,apolice where evento.id = registro_cobranca.id and superior = apolice.id and origem=? and superior = ? and status_apolice = ? and secao = ? and data_cobranca = ? and numero_parcela = ? and registro_cobranca.numero_endoso = ? and registro_cobranca.certificado = ? group by evento.id");
        query.addLong(aseguradora.obterId());
        query.addLong(apolice.obterId());
        query.addString(apolice.obterStatusApolice());
        query.addLong(cContas.obterId());
        query.addLong(dataCobranca.getTime());
        query.addInt(numeroParcela);
        query.addDouble(numeroEndoso);
        query.addDouble(certificado);
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
