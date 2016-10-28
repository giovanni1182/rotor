// FrontEnd Plus GUI for JAD
// DeCompiled : AnulacaoInstrumentoImpl.class

package com.gio.crm.model;

import java.util.Date;

import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

// Referenced classes of package com.gio.crm.model:
//            EventoImpl, AnulacaoInstrumento, Aseguradora, Apolice, 
//            ClassificacaoContas, EventoHome, Evento

public class AnulacaoInstrumentoImpl extends EventoImpl
    implements AnulacaoInstrumento
{

    private Date dataAnulacao;
    private double capitalGs;
    private String tipoMoedaCapitalGs;
    private double capitalMe;
    private String solicitadoPor;
    private int diasCorridos;
    private double primaGs;
    private String tipoMoedaPrimaGs;
    private double primaMe;
    private double comissaoGs;
    private String tipoMoedaComissaoGs;
    private double comissaoMe;
    private double comissaoRecuperarGs;
    private String tipoMoedaComissaoRecuperarGs;
    private double comissaoRecuperarMe;
    private double saldoAnulacaoGs;
    private String tipoMoedaSaldoAnulacaoGs;
    private double saldoAnulacaoMe;
    private String destinoSaldoAnulacao;
    private String tipoInstrumento;
    private double numeroEndoso;
    private double certificado;

    public AnulacaoInstrumentoImpl()
    {
    }

    public void atribuirDataAnulacao(Date data)
        throws Exception
    {
        dataAnulacao = data;
    }

    public void atribuirCapitalGs(double valor)
        throws Exception
    {
        capitalGs = valor;
    }

    public void atribuirTipoMoedaCapitalGs(String tipo)
        throws Exception
    {
        tipoMoedaCapitalGs = tipo;
    }

    public void atribuirCapitalMe(double valor)
        throws Exception
    {
        capitalMe = valor;
    }

    public void atribuirSolicitadoPor(String valor)
        throws Exception
    {
        solicitadoPor = valor;
    }

    public void atribuirDiasCorridos(int dias)
        throws Exception
    {
        diasCorridos = dias;
    }

    public void atribuirPrimaGs(double valor)
        throws Exception
    {
        primaGs = valor;
    }

    public void atribuirTipoMoedaPrimaGs(String tipo)
        throws Exception
    {
        tipoMoedaPrimaGs = tipo;
    }

    public void atribuirPrimaMe(double valor)
        throws Exception
    {
        primaMe = valor;
    }

    public void atribuirComissaoGs(double valor)
        throws Exception
    {
        comissaoGs = valor;
    }

    public void atribuirTipoMoedaComissaoGs(String tipo)
        throws Exception
    {
        tipoMoedaComissaoGs = tipo;
    }

    public void atribuirComissaoMe(double valor)
        throws Exception
    {
        comissaoMe = valor;
    }

    public void atribuirComissaoRecuperarGs(double valor)
        throws Exception
    {
        comissaoRecuperarGs = valor;
    }

    public void atribuirTipoMoedaComissaoRecuperarGs(String tipo)
        throws Exception
    {
        tipoMoedaComissaoRecuperarGs = tipo;
    }

    public void atribuirComissaoRecuperarMe(double valor)
        throws Exception
    {
        comissaoRecuperarMe = valor;
    }

    public void atribuirSaldoAnulacaoGs(double valor)
        throws Exception
    {
        saldoAnulacaoGs = valor;
    }

    public void atribuirTipoMoedaSaldoAnulacaoGs(String tipo)
        throws Exception
    {
        tipoMoedaSaldoAnulacaoGs = tipo;
    }

    public void atribuirSaldoAnulacaoMe(double valor)
        throws Exception
    {
        saldoAnulacaoMe = valor;
    }

    public void atribuirDestinoSaldoAnulacao(String destino)
        throws Exception
    {
        destinoSaldoAnulacao = destino;
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

    public void atualizarDataAnulacao(Date data)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update anulacao_instrumento set data_anulacao = ? where id = ?");
        update.addLong(data.getTime());
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarCapitalGs(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update anulacao_instrumento set capital_gs = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarTipoMoedaCapitalGs(String tipo)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update anulacao_instrumento set tipo_moeda_capital_gs = ? where id = ?");
        update.addString(tipo);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarCapitalMe(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update anulacao_instrumento set capital_me = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarSolicitadoPor(String valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update anulacao_instrumento set solicitado_por = ? where id = ?");
        update.addString(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarDiasCorridos(int dias)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update anulacao_instrumento set dias_corridos = ? where id = ?");
        update.addInt(dias);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarPrimaGs(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update anulacao_instrumento set prima_gs = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarTipoMoedaPrimaGs(String tipo)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update anulacao_instrumento set tipo_moeda_prima_gs = ? where id = ?");
        update.addString(tipo);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarPrimaMe(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update anulacao_instrumento set prima_me = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarComissaoGs(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update anulacao_instrumento set comissao_gs = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarTipoMoedaComissaoGs(String tipo)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update anulacao_instrumento set tipo_moeda_comissao_gs = ? where id = ?");
        update.addString(tipo);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarComissaoMe(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update anulacao_instrumento set comissao_me = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarComissaoRecuperarGs(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update anulacao_instrumento set comissao_recuperar_gs = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarTipoMoedaComissaoRecuperarGs(String tipo)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update anulacao_instrumento set tipo_moeda_comissao_recuperar_gs = ? where id = ?");
        update.addString(tipo);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarComissaoRecuperarMe(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update anulacao_instrumento set comissao_recuperar_me = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarSaldoAnulacaoGs(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update anulacao_instrumento set saldo_anulacao_gs = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarTipoMoedaSaldoAnulacaoGs(String tipo)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update anulacao_instrumento set tipo_moeda_saldo_anulacao_gs = ? where id = ?");
        update.addString(tipo);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarSaldoAnulacaoMe(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update anulacao_instrumento set saldo_anulacao_me = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarDestinoSaldoAnulacao(String destino)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update anulacao_instrumento set destino_saldo_anulacao = ? where id = ?");
        update.addString(destino);
        update.addLong(obterId());
        update.execute();
    }

    public synchronized void incluir() throws Exception
    {
    	super.incluir();
        
        SQLUpdate insert = this.getModelManager().createSQLUpdate("crm", "EXEC incluirAnulacaoInstrumento ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?");
        insert.addLong(this.obterId());
        if(dataAnulacao != null)
            insert.addLong(dataAnulacao.getTime());
        else
            insert.addLong(0L);
        insert.addDouble(capitalGs);
        insert.addString(tipoMoedaCapitalGs);
        insert.addDouble(capitalMe);
        insert.addString(solicitadoPor);
        insert.addInt(diasCorridos);
        insert.addDouble(primaGs);
        insert.addString(tipoMoedaPrimaGs);
        insert.addDouble(primaMe);
        insert.addDouble(comissaoGs);
        insert.addString(tipoMoedaComissaoGs);
        insert.addDouble(comissaoMe);
        insert.addDouble(comissaoRecuperarGs);
        insert.addString(tipoMoedaComissaoRecuperarGs);
        insert.addDouble(comissaoRecuperarMe);
        insert.addDouble(saldoAnulacaoGs);
        insert.addString(tipoMoedaSaldoAnulacaoGs);
        insert.addDouble(saldoAnulacaoMe);
        insert.addString(destinoSaldoAnulacao);
        insert.addString(tipoInstrumento);
        insert.addDouble(numeroEndoso);
        insert.addDouble(certificado);
        insert.execute();
    }

    public Date obterDataAnulacao() throws Exception
    {
        if(dataAnulacao == null)
        {
            /*SQLQuery query = getModelManager().createSQLQuery("crm", "select data_anulacao from anulacao_instrumento WITH(NOLOCK) where id = ?");
            query.addLong(obterId());*/
        	
        	SQLQuery query = getModelManager().createSQLQuery("crm", "EXEC obterDataAnulacao "+ obterId());
        	
            long dataLong = query.executeAndGetFirstRow().getLong("data_anulacao");
            if(dataLong > 0L)
                dataAnulacao = new Date(dataLong);
        }
        return dataAnulacao;
    }

    public double obterCapitalGs()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select capital_gs from anulacao_instrumento where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("capital_gs");
    }

    public String obterTipoMoedaCapitalGs()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select tipo_moeda_capital_gs from anulacao_instrumento where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("tipo_moeda_capital_gs");
    }

    public double obterCapitalMe()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select capital_me from anulacao_instrumento where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("capital_me");
    }

    public String obterSolicitadoPor()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select solicitado_por from anulacao_instrumento where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("solicitado_por");
    }

    public int obterDiasCorridos()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select dias_corridos from anulacao_instrumento where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getInt("dias_corridos");
    }

    public double obterPrimaGs()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select prima_gs from anulacao_instrumento where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("prima_gs");
    }

    public String obterTipoMoedaPrimaGs()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select tipo_moeda_prima_gs from anulacao_instrumento where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("tipo_moeda_prima_gs");
    }

    public double obterPrimaMe()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select prima_me from anulacao_instrumento where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("prima_me");
    }

    public double obterComissaoGs()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select comissao_gs from anulacao_instrumento where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("comissao_gs");
    }

    public String obterTipoMoedaComissaoGs()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select tipo_moeda_comissao_gs from anulacao_instrumento where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("tipo_moeda_comissao_gs");
    }

    public double obterComissaoMe()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select comissao_me from anulacao_instrumento where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("comissao_me");
    }

    public double obterComissaoRecuperarGs()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select comissao_recuperar_gs from anulacao_instrumento where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("comissao_recuperar_gs");
    }

    public String obterTipoMoedaComissaoRecuperarGs()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select tipo_moeda_comissao_recuperar_gs from anulacao_instrumento where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("tipo_moeda_comissao_recuperar_gs");
    }

    public double obterComissaoRecuperarMe()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select comissao_recuperar_me from anulacao_instrumento where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("comissao_recuperar_me");
    }

    public double obterSaldoAnulacaoGs()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select saldo_anulacao_gs from anulacao_instrumento where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("saldo_anulacao_gs");
    }

    public String obterTipoMoedaSaldoAnulacaoGs()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select tipo_moeda_saldo_anulacao_gs from anulacao_instrumento where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("tipo_moeda_saldo_anulacao_gs");
    }

    public double obterSaldoAnulacaoMe()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select saldo_anulacao_me from anulacao_instrumento where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("saldo_anulacao_me");
    }

    public String obterDestinoSaldoAnulacao()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select destino_saldo_anulacao from anulacao_instrumento where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("destino_saldo_anulacao");
    }

    public String obterTipoInstrumento()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select tipo_instrumento from anulacao_instrumento where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("tipo_instrumento");
    }
    
    public double obterNumeroEndoso()
        throws Exception
    {
        if(numeroEndoso == 0.0D)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select numero_endoso from anulacao_instrumento where id = ?");
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
            SQLQuery query = getModelManager().createSQLQuery("crm", "select certificado from anulacao_instrumento where id = ?");
            query.addLong(obterId());
            certificado = query.executeAndGetFirstRow().getDouble("certificado");
        }
        return certificado;
    }

    public void verificarDuplicidade(Aseguradora aseguradora, Apolice apolice, ClassificacaoContas cContas, Date dataAnulacao, double numeroEndoso, double certificado)
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select evento.id from evento,anulacao_instrumento,apolice where evento.id = anulacao_instrumento.id and superior = apolice.id and origem = ? and superior = ? and status_apolice = ? and secao = ? and data_anulacao = ? and anulacao_instrumento.numero_endoso = ? and anulacao_instrumento.certificado = ? group by evento.id");
        query.addLong(aseguradora.obterId());
        query.addLong(apolice.obterId());
        query.addString(apolice.obterStatusApolice());
        query.addLong(cContas.obterId());
        query.addLong(dataAnulacao.getTime());
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
