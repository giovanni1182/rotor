// FrontEnd Plus GUI for JAD
// DeCompiled : RefinacaoImpl.class

package com.gio.crm.model;

import java.util.Date;

import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

// Referenced classes of package com.gio.crm.model:
//            EventoImpl, Refinacao, Aseguradora, Apolice, 
//            ClassificacaoContas, EventoHome, Evento

public class RefinacaoImpl extends EventoImpl
    implements Refinacao
{

    private double financiamentoGs;
    private String tipoMoedaFinanciamentoGs;
    private double financiamentoMe;
    private int qtdeParcelas;
    private String tipoInstrumento;
    private double numeroEndoso;
    private double certificado;

    public RefinacaoImpl()
    {
    }

    public void atribuirFinanciamentoGs(double valor)
        throws Exception
    {
        financiamentoGs = valor;
    }

    public void atribuirTipoMoedaFinanciamentoGs(String tipo)
        throws Exception
    {
        tipoMoedaFinanciamentoGs = tipo;
    }

    public void atribuirFinanciamentoMe(double valor)
        throws Exception
    {
        financiamentoMe = valor;
    }

    public void atribuirQtdeParcelas(int qtde)
        throws Exception
    {
        qtdeParcelas = qtde;
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

    public void atualizarFinanciamentoGs(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update refinacao set financiamente_gs = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarTipoMoedaFinanciamentoGs(String tipo)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update refinacao set tipo_moeda_financiamente_gs = ? where id = ?");
        update.addString(tipo);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarFinanciamentoMe(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update refinacao set financiamente_me = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarQtdeParcelas(int qtde)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update refinacao set parcelas = ? where id = ?");
        update.addInt(qtde);
        update.addLong(obterId());
        update.execute();
    }

    public synchronized void incluir() throws Exception
    {
    	 super.incluir();
         
         SQLUpdate insert = this.getModelManager().createSQLUpdate("crm", "EXEC incluirRefinacao ?, ?, ?, ?, ?, ?, ?, ?");
         insert.addLong(this.obterId());
         insert.addDouble(financiamentoGs);
         insert.addString(tipoMoedaFinanciamentoGs);
         insert.addDouble(financiamentoMe);
         insert.addInt(qtdeParcelas);
         insert.addString(tipoInstrumento);
         insert.addDouble(numeroEndoso);
         insert.addDouble(certificado);
         insert.execute();
    }

    public double obterFinanciamentoGs()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select financiamente_gs from refinacao where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("financiamente_gs");
    }

    public String obterTipoMoedaFinanciamentoGs()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select tipo_moeda_financiamente_gs from refinacao where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("tipo_moeda_financiamente_gs");
    }

    public double obterFinanciamentoMe()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select financiamente_me from refinacao where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("financiamente_me");
    }

    public int obterQtdeParcelas()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select parcelas from refinacao where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getInt("parcelas");
    }

    public String obterTipoInstrumento()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select tipo_instrumento from refinacao where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("tipo_instrumento");
    }

    public double obterNumeroEndoso()
        throws Exception
    {
        if(numeroEndoso == 0.0D)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select numero_endoso from refinacao where id = ?");
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
            SQLQuery query = getModelManager().createSQLQuery("crm", "select certificado from refinacao where id = ?");
            query.addLong(obterId());
            certificado = query.executeAndGetFirstRow().getDouble("certificado");
        }
        return certificado;
    }

    public void verificarDuplicidade(Aseguradora aseguradora, Apolice apolice, ClassificacaoContas cContas, double numeroEndoso, double certificado, 
            Date dataInicio)
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select evento.id from evento,apolice,refinacao where evento.id = refinacao.id and superior = apolice.id and origem=? and superior = ? and status_apolice = ? and secao = ? and classe = ? and apolice.numero_endoso = ? and apolice.certificado = ? and data_prevista_inicio = ? group by evento.id");
        query.addLong(aseguradora.obterId());
        query.addLong(apolice.obterId());
        query.addString(apolice.obterStatusApolice());
        query.addLong(cContas.obterId());
        query.addString(obterClasse());
        query.addDouble(numeroEndoso);
        query.addDouble(certificado);
        query.addLong(dataInicio.getTime());
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
