// FrontEnd Plus GUI for JAD
// DeCompiled : DadosReaseguroImpl.class

package com.gio.crm.model;

import java.util.Date;

import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

// Referenced classes of package com.gio.crm.model:
//            EventoImpl, DadosReaseguro, Entidade, EntidadeHome, 
//            Aseguradora, Apolice, ClassificacaoContas, EventoHome, 
//            Evento

public class DadosReaseguroImpl extends EventoImpl
    implements DadosReaseguro
{

    private Entidade reaseguradora;
    private String tipoContrato;
    private Entidade corredora;
    private double capitalGs;
    private String tipoMoedaCapitalGs;
    private double capitalMe;
    private double primaGs;
    private String tipoMoedaPrimaGs;
    private double primaMe;
    private double comissaoGs;
    private String tipoMoedaComissaoGs;
    private double comissaoMe;
    private String situacao;
    private double valorEndoso;
    private String tipoInstrumento;
    private double certificado;
    private Date dataIniApo, dataFimApo;

    public DadosReaseguroImpl()
    {
    }

    public void atribuirReaseguradora(Entidade reaseguradora)
        throws Exception
    {
        this.reaseguradora = reaseguradora;
    }

    public void atribuirTipoContrato(String tipo)
        throws Exception
    {
        tipoContrato = tipo;
    }

    public void atribuirCorredora(Entidade corredora)
        throws Exception
    {
        this.corredora = corredora;
    }
    
    public void atribuirDataIniApo(Date data) throws Exception
        {
            this.dataIniApo = data;
        }
    
    public void atribuirDataFimApo(Date data) throws Exception
    {
        this.dataFimApo = data;
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

    public void atribuirSituacao(String situacao)
        throws Exception
    {
        this.situacao = situacao;
    }

    public void atribuirValorEndoso(double valor)
        throws Exception
    {
        valorEndoso = valor;
    }

    public void atribuirTipoInstrumento(String tipo)
        throws Exception
    {
        tipoInstrumento = tipo;
    }

    public void atribuirCertificado(double certificado)
        throws Exception
    {
        this.certificado = certificado;
    }

    public void atualizarReaseguradora(Entidade reaseguradora)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update dados_reaseguro set reaseguradora = ? where id = ?");
        update.addLong(reaseguradora.obterId());
        update.addLong(obterId());
        update.execute();
    }
    
	public void atualizarTipoContrato(String tipo)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update dados_reaseguro set tipo_contrato = ? where id = ?");
        update.addString(tipo);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarCorredora(Entidade corredora)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update dados_reaseguro set corretora = ? where id = ?");
        update.addLong(corredora.obterId());
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarCapitalGs(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update dados_reaseguro set caiptal_gs = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarTipoMoedaCapitalGs(String tipo)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update dados_reaseguro set tipo_moeda_capital_gs = ? where id = ?");
        update.addString(tipo);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarCapitalMe(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update dados_reaseguro set capital_me = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarPrimaGs(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update dados_reaseguro set prima_gs = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarTipoMoedaPrimaGs(String tipo)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update dados_reaseguro set tipo_moeda_prima_gs = ? where id = ?");
        update.addString(tipo);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarPrimaMe(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update dados_reaseguro set prima_me = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarComissaoGs(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update dados_reaseguro set comissao_gs = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarTipoMoedaComissaoGs(String tipo)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update dados_reaseguro set tipo_moeda_comissao_gs = ? where id = ?");
        update.addString(tipo);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarComissaoMe(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update dados_reaseguro set comissao_me = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarSituacao(String situacao) throws Exception
    {
    	SQLUpdate update = this.getModelManager().createSQLUpdate("crm", "EXEC atualizarSituacaoDadoReaseguro ?,?");
    	update.addString(situacao);
        update.addLong(this.obterId());
        update.execute();
    }

    public synchronized void incluir() throws Exception
    {
        super.incluir();
        
        SQLUpdate insert = this.getModelManager().createSQLUpdate("crm", "EXEC incluirDadosReaseguro ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?");
        insert.addLong(this.obterId());
        if(reaseguradora != null)
            insert.addLong(reaseguradora.obterId());
        else
            insert.addLong(0L);
        insert.addString(tipoContrato);
        if(corredora != null)
            insert.addLong(corredora.obterId());
        else
            insert.addLong(0L);
        insert.addDouble(capitalGs);
        insert.addString(tipoMoedaCapitalGs);
        insert.addDouble(capitalMe);
        insert.addDouble(primaGs);
        insert.addString(tipoMoedaPrimaGs);
        insert.addDouble(primaMe);
        insert.addDouble(comissaoGs);
        insert.addString(tipoMoedaComissaoGs);
        insert.addDouble(comissaoMe);
        insert.addString(situacao);
        insert.addDouble(valorEndoso);
        insert.addString(tipoInstrumento);
        insert.addDouble(certificado);
        insert.addLong(dataIniApo.getTime());
        insert.addLong(dataFimApo.getTime());
        insert.execute();
    }

    public Entidade obterReaseguradora()
        throws Exception
    {
        if(reaseguradora == null)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select reaseguradora from dados_reaseguro where id = ?");
            query.addLong(obterId());
            long id = query.executeAndGetFirstRow().getLong("reaseguradora");
            if(id > 0L)
            {
                EntidadeHome home = (EntidadeHome)getModelManager().getHome("EntidadeHome");
                reaseguradora = home.obterEntidadePorId(id);
            }
        }
        return reaseguradora;
    }

    public String obterTipoContrato()
        throws Exception
    {
        if(tipoContrato == null)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select tipo_contrato from dados_reaseguro where id = ?");
            query.addLong(obterId());
            tipoContrato = query.executeAndGetFirstRow().getString("tipo_contrato");
        }
        return tipoContrato;
    }

    public Entidade obterCorredora()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select corretora from dados_reaseguro where id = ?");
        query.addLong(obterId());
        Entidade corretora = null;
        long id = query.executeAndGetFirstRow().getLong("corretora");
        if(id > 0L)
        {
            EntidadeHome home = (EntidadeHome)getModelManager().getHome("EntidadeHome");
            corretora = home.obterEntidadePorId(id);
        }
        return corretora;
    }

    public double obterCapitalGs()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select caiptal_gs from dados_reaseguro where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("caiptal_gs");
    }

    public String obterTipoMoedaCapitalGs()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select tipo_moeda_capital_gs from dados_reaseguro where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("tipo_moeda_capital_gs");
    }

    public double obterCapitalMe()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select capital_me from dados_reaseguro where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("capital_me");
    }

    public double obterPrimaGs()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select prima_gs from dados_reaseguro where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("prima_gs");
    }

    public String obterTipoMoedaPrimaGs()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select tipo_moeda_prima_gs from dados_reaseguro where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("tipo_moeda_prima_gs");
    }

    public double obterPrimaMe()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select prima_me from dados_reaseguro where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("prima_me");
    }

    public double obterComissaoGs()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select comissao_gs from dados_reaseguro where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("comissao_gs");
    }

    public String obterTipoMoedaComissaoGs()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select tipo_moeda_comissao_gs from dados_reaseguro where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("tipo_moeda_comissao_gs");
    }

    public double obterComissaoMe()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select comissao_me from dados_reaseguro where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("comissao_me");
    }

    public String obterSituacao()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select situacao from dados_reaseguro where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("situacao");
    }

    public void verificarDuplicidade(Aseguradora aseguradora, Apolice apolice, ClassificacaoContas cContas, Entidade reaseguradora, String tipoContrato, double valorEndoso, 
            double certificado)
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select evento.id from evento,dados_reaseguro,apolice where evento.id = dados_reaseguro.id and superior = apolice.id and origem = ? and superior = ? and status_apolice = ? and secao = ? and reaseguradora = ? and tipo_contrato = ? and dados_reaseguro.valor_endoso = ? and dados_reaseguro.certificado = ? group by evento.id");
        query.addLong(aseguradora.obterId());
        query.addLong(apolice.obterId());
        query.addString(apolice.obterStatusApolice());
        query.addLong(cContas.obterId());
        query.addLong(reaseguradora.obterId());
        query.addString(tipoContrato);
        query.addDouble(valorEndoso);
        query.addDouble(certificado);
        
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

    public double obterValorEndoso()
        throws Exception
    {
        if(valorEndoso == 0.0D)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select valor_endoso from dados_reaseguro where id = ?");
            query.addLong(obterId());
            valorEndoso = query.executeAndGetFirstRow().getDouble("valor_endoso");
        }
        return valorEndoso;
    }

    public String obterTipoInstrumento()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select tipo_instrumento from dados_reaseguro where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("tipo_instrumento");
    }

    public double obterCertificado()
        throws Exception
    {
        if(certificado == 0.0D)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select certificado from dados_reaseguro where id = ?");
            query.addLong(obterId());
            certificado = query.executeAndGetFirstRow().getDouble("certificado");
        }
        return certificado;
    }
}
