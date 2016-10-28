// FrontEnd Plus GUI for JAD
// DeCompiled : SinistroImpl.class

package com.gio.crm.model;

import java.util.Date;

import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

// Referenced classes of package com.gio.crm.model:
//            EventoImpl, Sinistro, AuxiliarSeguro, EntidadeHome, 
//            Aseguradora, Apolice, ClassificacaoContas, EventoHome, 
//            Evento

public class SinistroImpl extends EventoImpl
    implements Sinistro
{

    private String numero;
    private Date dataSinistro;
    private Date dataDenuncia;
    private AuxiliarSeguro auxiliar;
    private double montanteGs;
    private String tipoMoedaMontanteGs;
    private double montanteMe;
    private String situacao;
    private Date dataPagamento;
    private Date dataRecuperacao;
    private double valorRecuperacao;
    private double valorRecuperacaoTerceiro;
    private double participacao;
    private String tipoInstrumento;
    private double numeroEndoso;
    private double certificado;
    private CodificacaoPlano plano;
    private CodificacaoCobertura cobertura;
    private CodificacaoRisco risco;
    private CodificacaoDetalhe detalhe;

    public SinistroImpl()
    {
    }

    public void atribuirNumero(String numero)
        throws Exception
    {
        this.numero = numero;
    }

    public void atribuirDataSinistro(Date data)
        throws Exception
    {
        dataSinistro = data;
    }

    public void atribuirDataDenuncia(Date data)
        throws Exception
    {
        dataDenuncia = data;
    }

    public void atribuirAgente(AuxiliarSeguro auxiliar)
        throws Exception
    {
        this.auxiliar = auxiliar;
    }

    public void atribuirMontanteGs(double valor)
        throws Exception
    {
        montanteGs = valor;
    }

    public void atribuirTipoMoedaMontanteGs(String tipo)
        throws Exception
    {
        tipoMoedaMontanteGs = tipo;
    }

    public void atribuirMontanteMe(double valor)
        throws Exception
    {
        montanteMe = valor;
    }

    public void atribuirSituacao(String situacao)
        throws Exception
    {
        this.situacao = situacao;
    }

    public void atribuirDataPagamento(Date data)
        throws Exception
    {
        dataPagamento = data;
    }

    public void atribuirDataRecuperacao(Date data)
        throws Exception
    {
        dataRecuperacao = data;
    }

    public void atribuirValorRecuperacao(double valor)
        throws Exception
    {
        valorRecuperacao = valor;
    }

    public void atribuirValorRecuperacaoTerceiro(double valor)
        throws Exception
    {
        valorRecuperacaoTerceiro = valor;
    }

    public void atribuirParticipacao(double valor)
        throws Exception
    {
        participacao = valor;
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

    public void atualizarNumero(String numero)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update sinistro set numero = ? where id = ?");
        update.addString(numero);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarDataSinistro(Date data)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update sinistro set data_sinistro = ? where id = ?");
        update.addLong(data.getTime());
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarDataDenuncia(Date data)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update sinistro set data_denuncia = ? where id = ?");
        update.addLong(data.getTime());
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarAgente(AuxiliarSeguro auxiliar)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update sinistro set agente = ? where id = ?");
        update.addLong(auxiliar.obterId());
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarMontanteGs(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update sinistro set montante_gs = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarTipoMoedaMontanteGs(String tipo)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update sinistro set tipo_moeda_montante_gs = ? where id = ?");
        update.addString(tipo);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarMontanteMe(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update sinistro set montante_me = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarSituacao(String situacao)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update sinistro set situacao = ? where id = ?");
        update.addString(situacao);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarDataPagamento(Date data)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update sinistro set data_pagamento = ? where id = ?");
        update.addLong(data.getTime());
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarDataRecuperacao(Date data)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update sinistro set data_recuperacao = ? where id = ?");
        update.addLong(data.getTime());
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarValorRecuperacao(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update sinistro set valor_recuperacao = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarValorRecuperacaoTerceiro(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update sinistro set valor_recuperacao_terceiro = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarParticipacao(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update sinistro set participacao = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public synchronized void incluir() throws Exception
    {
    	super.incluir();
        
        SQLUpdate insert = this.getModelManager().createSQLUpdate("crm", "EXEC incluirSinistro ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?");
        insert.addLong(this.obterId());
        insert.addString(numero);
        if(dataSinistro != null)
            insert.addLong(dataSinistro.getTime());
        else
            insert.addLong(0L);
        if(dataDenuncia != null)
            insert.addLong(dataDenuncia.getTime());
        else
            insert.addLong(0L);
        if(auxiliar != null)
            insert.addLong(auxiliar.obterId());
        else
            insert.addLong(0L);
        insert.addDouble(montanteGs);
        insert.addString(tipoMoedaMontanteGs);
        insert.addDouble(montanteMe);
        insert.addString(situacao);
        if(dataPagamento != null)
            insert.addLong(dataPagamento.getTime());
        else
            insert.addLong(0L);
        if(dataRecuperacao != null)
            insert.addLong(dataRecuperacao.getTime());
        else
            insert.addLong(0L);
        insert.addDouble(valorRecuperacao);
        insert.addDouble(valorRecuperacaoTerceiro);
        insert.addDouble(participacao);
        insert.addString(tipoInstrumento);
        insert.addDouble(numeroEndoso);
        insert.addDouble(certificado);
        if(this.plano!=null)
        	insert.addLong(plano.obterId());
        else
        	insert.addLong(0);
        if(this.cobertura!=null)
        	insert.addLong(cobertura.obterId());
        else
        	insert.addLong(0);
        if(this.risco!=null)
        	insert.addLong(risco.obterId());
        else
        	insert.addLong(0);
        if(this.detalhe!=null)
        	insert.addLong(detalhe.obterId());
        else
        	insert.addLong(0);
        
        insert.execute();
    }

    public String obterNumero()
        throws Exception
    {
        if(numero == null)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select numero from sinistro where id = ?");
            query.addLong(obterId());
            numero = query.executeAndGetFirstRow().getString("numero");
        }
        return numero;
    }

    public Date obterDataSinistro()
        throws Exception
    {
        if(dataSinistro == null)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select data_sinistro from sinistro where id = ?");
            query.addLong(obterId());
            long dataLong = query.executeAndGetFirstRow().getLong("data_sinistro");
            if(dataLong != 0L)
                dataSinistro = new Date(dataLong);
        }
        return dataSinistro;
    }

    public Date obterDataDenuncia()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select data_denuncia from sinistro where id = ?");
        query.addLong(obterId());
        Date data = null;
        long dataLong = query.executeAndGetFirstRow().getLong("data_denuncia");
        if(dataLong > 0L)
            data = new Date(dataLong);
        return data;
    }

    public AuxiliarSeguro obterAuxiliar()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select agente from sinistro where id = ?");
        query.addLong(obterId());
        long id = query.executeAndGetFirstRow().getLong("agente");
        AuxiliarSeguro auxiliar = null;
        if(id > 0L)
        {
            EntidadeHome home = (EntidadeHome)getModelManager().getHome("EntidadeHome");
            auxiliar = (AuxiliarSeguro)home.obterEntidadePorId(id);
        }
        return auxiliar;
    }

    public double obterMontanteGs()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select montante_gs from sinistro where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("montante_gs");
    }

    public String obterTipoMoedaMontanteGs()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select tipo_moeda_montante_gs from sinistro where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("tipo_moeda_montante_gs");
    }

    public double obterMontanteMe()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select montante_me from sinistro where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("montante_me");
    }

    public String obterSituacao()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select situacao from sinistro where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("situacao");
    }

    public Date obterDataPagamento()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select data_pagamento from sinistro where id = ?");
        query.addLong(obterId());
        Date data = null;
        long dataLong = query.executeAndGetFirstRow().getLong("data_pagamento");
        if(dataLong > 0L)
            data = new Date(dataLong);
        return data;
    }

    public Date obterDataRecuperacao()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select data_recuperacao from sinistro where id = ?");
        query.addLong(obterId());
        Date data = null;
        long dataLong = query.executeAndGetFirstRow().getLong("data_recuperacao");
        if(dataLong > 0L)
            data = new Date(dataLong);
        return data;
    }

    public double obterValorRecuperacao()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select valor_recuperacao from sinistro where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("valor_recuperacao");
    }

    public double obterValorRecuperacaoTerceiro()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select valor_recuperacao_terceiro from sinistro where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("valor_recuperacao");
    }

    public double obterParticipacao()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select participacao from sinistro where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("participacao");
    }

    public void verificarDuplicidade(Entidade aseguradora, Apolice apolice, ClassificacaoContas cContas, String numeroSinistro, double numeroEndoso, double certificado, Date dataSinistro)
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select evento.id from evento,sinistro,apolice where evento.id = sinistro.id and superior = apolice.id and origem = ? and superior = ? and status_apolice = ? and secao = ? and numero = ? and sinistro.numero_endoso = ? and sinistro.certificado = ? and sinistro.data_sinistro = ? group by evento.id");
        query.addLong(aseguradora.obterId());
        query.addLong(apolice.obterId());
        query.addString(apolice.obterStatusApolice());
        query.addLong(cContas.obterId());
        query.addString(numeroSinistro);
        query.addDouble(numeroEndoso);
        query.addDouble(certificado);
        if(dataSinistro != null)
            query.addLong(dataSinistro.getTime());
        else
            query.addLong(0L);
        
        SQLRow rows[] = query.execute();
        
        for(int i = 0; i < rows.length; i++)
        {
            long id = rows[i].getLong("id");
            if(id!=this.obterId())
            {
	            EventoHome home = (EventoHome)getModelManager().getHome("EventoHome");
	            Evento e = home.obterEventoPorId(id);
	            
	            SQLUpdate update = this.getModelManager().createSQLUpdate("crm","update evento set superior = ? where superior = ?");
	            update.addLong(this.obterId());
	            update.addLong(e.obterId());
	            
	            update.execute();
	            
	            e.excluir();
            }
        }

    }

    public String obterTipoInstrumento()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select tipo_instrumento from sinistro where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("tipo_instrumento");
    }

    public double obterNumeroEndoso()
        throws Exception
    {
        if(numeroEndoso == 0.0D)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select numero_endoso from sinistro where id = ?");
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
            SQLQuery query = getModelManager().createSQLQuery("crm", "select certificado from sinistro where id = ?");
            query.addLong(obterId());
            certificado = query.executeAndGetFirstRow().getDouble("certificado");
        }
        return certificado;
    }

	public void atribuirCodificacaoCobertura(CodificacaoCobertura cobertura) throws Exception
	{
		this.cobertura = cobertura;
	}

	public void atribuirCodificacaoDetalhe(CodificacaoDetalhe detalhe) throws Exception
	{
		this.detalhe = detalhe;
	}

	public void atribuirCodificacaoPlano(CodificacaoPlano plano) throws Exception
	{
		this.plano = plano;
	}

	public void atribuirCodificacaoRisco(CodificacaoRisco risco) throws Exception
	{
		this.risco = risco;
	}
}
