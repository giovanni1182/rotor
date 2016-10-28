// FrontEnd Plus GUI for JAD
// DeCompiled : RegistroAnulacaoImpl.class

package com.gio.crm.model;

import java.util.Date;

import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

// Referenced classes of package com.gio.crm.model:
//            EventoImpl, RegistroAnulacao, Reaseguradora, Entidade, 
//            EntidadeHome, Aseguradora, Apolice, ClassificacaoContas, 
//            EventoHome, Evento

public class RegistroAnulacaoImpl extends EventoImpl
    implements RegistroAnulacao
{

    private Entidade reaseguradora;
    private String tipoContrato;
    private Date dataAnulacao;
    private double capitalGs;
    private String tipoMoedaCapitalGs;
    private double capitalMe;
    private int diasCorridos;
    private double primaGs;
    private String tipoMoedaPrimaGs;
    private double primaMe;
    private double comissaoGs;
    private String tipoMoedaComissaoGs;
    private double comissaoMe;
    private String tipoInstrumento;
    private double numeroEndoso;
    private double certificado;

    public RegistroAnulacaoImpl()
    {
    }

    public void atribuirReaeguradora(Entidade reaseguradora)
        throws Exception
    {
        this.reaseguradora = reaseguradora;
    }

    public void atribuirTipoContrato(String tipo)
        throws Exception
    {
        tipoContrato = tipo;
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

    public void atualizarReaeguradora(Reaseguradora reaseguradora)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update registro_anulacao set reaseguradora = ? where id = ?");
        update.addLong(reaseguradora.obterId());
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarTipoContrato(String tipo)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update registro_anulacao set tipo_contrato = ? where id = ?");
        update.addString(tipo);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarDataAnulacao(Date data)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update registro_anulacao set data_anulacao = ? where id = ?");
        update.addLong(data.getTime());
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarCapitalGs(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update registro_anulacao set capital_gs = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarTipoMoedaCapitalGs(String tipo)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update registro_anulacao set tipo_moeda_capital_gs = ? where id = ?");
        update.addString(tipo);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarCapitalMe(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update registro_anulacao set capital_me = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarDiasCorridos(int dias)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update registro_anulacao set dias = ? where id = ?");
        update.addInt(dias);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarPrimaGs(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update registro_anulacao set prima_gs = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarTipoMoedaPrimaGs(String tipo)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update registro_anulacao set tipo_moeda_prima_gs = ? where id = ?");
        update.addString(tipo);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarPrimaMe(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update registro_anulacao set prima_me = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarComissaoGs(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update registro_anulacao set comissao_gs = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarTipoMoedaComissaoGs(String tipo)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update registro_anulacao set tipo_moeda_comissao_gs = ? where id = ?");
        update.addString(tipo);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarComissaoMe(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update registro_anulacao set comissao_me = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public synchronized void incluir()
        throws Exception
    {
    	 super.incluir();
         
         SQLUpdate insert = this.getModelManager().createSQLUpdate("crm", "EXEC incluirRegistroAnulacao ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?");
         insert.addLong(this.obterId());
         if(reaseguradora != null)
             insert.addLong(reaseguradora.obterId());
         else
             insert.addLong(0L);
         insert.addString(tipoContrato);
         if(dataAnulacao != null)
             insert.addLong(dataAnulacao.getTime());
         else
             insert.addLong(0L);
         insert.addDouble(capitalGs);
         insert.addString(tipoMoedaCapitalGs);
         insert.addDouble(capitalMe);
         insert.addInt(diasCorridos);
         insert.addDouble(primaGs);
         insert.addString(tipoMoedaPrimaGs);
         insert.addDouble(primaMe);
         insert.addDouble(comissaoGs);
         insert.addString(tipoMoedaComissaoGs);
         insert.addDouble(comissaoMe);
         insert.addString(tipoInstrumento);
         insert.addDouble(numeroEndoso);
         insert.addDouble(certificado);
         insert.execute();
    }

    public Entidade obterReaeguradora()
        throws Exception
    {
        if(reaseguradora == null)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select reaseguradora from registro_anulacao where id = ?");
            query.addLong(obterId());
            long id = query.executeAndGetFirstRow().getLong("reaseguradora");
            if(id > 0L)
            {
                EntidadeHome home = (EntidadeHome)getModelManager().getHome("EntidadeHome");
                reaseguradora = (Reaseguradora)home.obterEntidadePorId(id);
            }
        }
        return reaseguradora;
    }

    public String obterTipoContrato() throws Exception
    {
        if(this.tipoContrato == null)
        {
	    	SQLQuery query = getModelManager().createSQLQuery("crm", "select tipo_contrato from registro_anulacao where id = ?");
	        query.addLong(obterId());
	        this.tipoContrato =  query.executeAndGetFirstRow().getString("tipo_contrato");
        }
       	return this.tipoContrato;
    }

    public Date obterDataAnulacao() throws Exception
    {
        if(this.dataAnulacao == null)
        {
	    	SQLQuery query = getModelManager().createSQLQuery("crm", "select data_anulacao from registro_anulacao where id = ?");
	        query.addLong(obterId());
	        long dataLong = query.executeAndGetFirstRow().getLong("data_anulacao");
	        if(dataLong > 0L)
	            this.dataAnulacao = new Date(dataLong);
        }
        return this.dataAnulacao;
    }

    public double obterCapitalGs()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select capital_gs from registro_anulacao where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("capital_gs");
    }

    public String obterTipoMoedaCapitalGs()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select tipo_moeda_capital_gs from registro_anulacao where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("tipo_moeda_capital_gs");
    }

    public double obterCapitalMe()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select capital_me from registro_anulacao where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("capital_me");
    }

    public int obterDiasCorridos()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select dias from registro_anulacao where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getInt("dias");
    }

    public double obterPrimaGs()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select prima_gs from registro_anulacao where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("prima_gs");
    }

    public String obterTipoMoedaPrimaGs()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select tipo_moeda_prima_gs from registro_anulacao where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("tipo_moeda_prima_gs");
    }

    public double obterPrimaMe()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select prima_me from registro_anulacao where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("prima_me");
    }

    public double obterComissaoGs()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select comissao_gs from registro_anulacao where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("comissao_gs");
    }

    public String obterTipoMoedaComissaoGs()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select tipo_moeda_comissao_gs from registro_anulacao where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("tipo_moeda_comissao_gs");
    }

    public double obterComissaoMe()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select comissao_me from registro_anulacao where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("comissao_me");
    }

    public String obterTipoInstrumento() throws Exception
    {
        if(this.tipoInstrumento == null)
        {
	    	SQLQuery query = getModelManager().createSQLQuery("crm", "select tipo_instrumento from registro_anulacao where id = ?");
	        query.addLong(obterId());
	        this.tipoInstrumento = query.executeAndGetFirstRow().getString("tipo_instrumento");
        }
        return this.tipoInstrumento;
    }

    public double obterNumeroEndoso()
        throws Exception
    {
        if(numeroEndoso == 0.0D)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select numero_endoso from registro_anulacao where id = ?");
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
            SQLQuery query = getModelManager().createSQLQuery("crm", "select certificado from registro_anulacao where id = ?");
            query.addLong(obterId());
            certificado = query.executeAndGetFirstRow().getDouble("certificado");
        }
        return certificado;
    }

    public void verificarDuplicidade(Aseguradora aseguradora, Apolice apolice, DadosReaseguro dados, ClassificacaoContas cContas, Entidade reaseguradora, String tipo, double numeroEndoso, double certificado)
        throws Exception
    {
        //SQLQuery query = getModelManager().createSQLQuery("crm", "select evento.id from evento,registro_anulacao,apolice where evento.id = registro_anulacao.id and superior = apolice.id and origem=? and superior = ? and status_apolice = ? and secao = ? and reaseguradora = ? and tipo = ? and registro_anulacao.numero_endoso = ? and registro_anulacao.certificado = ? group by evento.id");
    	SQLQuery query = getModelManager().createSQLQuery("crm", "select evento.id from evento,registro_anulacao,dados_reaseguro where evento.id = registro_anulacao.id and superior = dados_reaseguro.id and origem=? and superior = ? and registro_anulacao.reaseguradora = ? and tipo = ? and registro_anulacao.numero_endoso = ? and registro_anulacao.certificado = ? and registro_anulacao.tipo_contrato = ? and registro_anulacao.data_anulacao = ? and registro_anulacao.tipo_instrumento = ? group by evento.id");
        query.addLong(aseguradora.obterId());
        query.addLong(dados.obterId());
        query.addLong(reaseguradora.obterId());
        query.addString(tipo);
        query.addDouble(numeroEndoso);
        query.addDouble(certificado);
        query.addString(this.obterTipoContrato());
        query.addLong(this.obterDataAnulacao().getTime());
        query.addString(this.obterTipoInstrumento());
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
