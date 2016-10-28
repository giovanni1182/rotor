// FrontEnd Plus GUI for JAD
// DeCompiled : DadosCoaseguroImpl.class

package com.gio.crm.model;

import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

// Referenced classes of package com.gio.crm.model:
//            EventoImpl, DadosCoaseguro, Entidade, EntidadeHome, 
//            Aseguradora, Apolice, ClassificacaoContas, EventoHome, 
//            Evento

public class DadosCoaseguroImpl extends EventoImpl
    implements DadosCoaseguro
{

    private Entidade aseguradora;
    private double capitalGs;
    private String tipoMoedaCapitalGs;
    private double capitalMe;
    private double participacao;
    private double primaGs;
    private String tipoMoedaPrimaGs;
    private double primaMe;
    private String grupo;
    private String tipoInstrumento;
    private double numeroEndoso;
    private double certificado;

    public DadosCoaseguroImpl()
    {
    }

    public void atribuirAseguradora(Entidade aseguradora)
        throws Exception
    {
        this.aseguradora = aseguradora;
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

    public void atribuirParticipacao(double valor)
        throws Exception
    {
        participacao = valor;
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

    public void atribuirGrupo(String grupo)
        throws Exception
    {
        this.grupo = grupo;
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

    public void atualizarAseguradora(Entidade aseguradora)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update dados_coaseguro set aseguradora = ? where id = ?");
        update.addLong(aseguradora.obterId());
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarCapitalGs(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update dados_coaseguro set capital_gs = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarTipoMoedaCapitalGs(String tipo)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update dados_coaseguro set tipo_moeda_capital_gs = ? where id = ?");
        update.addString(tipo);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarCapitalMe(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update dados_coaseguro set capital_me = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarParticipacao(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update dados_coaseguro set participacao = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarPrimaGs(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update dados_coaseguro set prima_gs = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarTipoMoedaPrimaGs(String tipo)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update dados_coaseguro set tipo_moeda_prima_gs = ? where id = ?");
        update.addString(tipo);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarGrupo(String grupo)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update dados_coaseguro set grupo = ? where id = ?");
        update.addString(grupo);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarPrimaMe(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update dados_coaseguro set prima_me = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public synchronized void incluir() throws Exception
    {
        super.incluir();
        
        SQLUpdate insert = this.getModelManager().createSQLUpdate("crm", "EXEC incluirDadosCoaseguro ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?");
        insert.addLong(this.obterId());
        insert.addLong(aseguradora.obterId());
        insert.addDouble(capitalGs);
        insert.addString(tipoMoedaCapitalGs);
        insert.addDouble(capitalMe);
        insert.addDouble(participacao);
        insert.addDouble(primaGs);
        insert.addString(tipoMoedaPrimaGs);
        insert.addDouble(primaMe);
        insert.addString(grupo);
        insert.addString(tipoInstrumento);
        insert.addDouble(numeroEndoso);
        insert.addDouble(certificado);
        insert.execute();
    }

    public Entidade obterAseguradora()
        throws Exception
    {
        if(aseguradora == null)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select aseguradora from dados_coaseguro where id = ?");
            query.addLong(obterId());
            long id = query.executeAndGetFirstRow().getLong("aseguradora");
            if(id > 0L)
            {
                EntidadeHome home = (EntidadeHome)getModelManager().getHome("EntidadeHome");
                aseguradora = home.obterEntidadePorId(id);
            }
        }
        return aseguradora;
    }

    public double obterCapitalGs()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select capital_gs from dados_coaseguro where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("capital_gs");
    }

    public String obterTipoMoedaCapitalGs()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select tipo_moeda_capital_gs from dados_coaseguro where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("tipo_moeda_capital_gs");
    }

    public double obterCapitalMe()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select capital_me from dados_coaseguro where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("capital_me");
    }

    public double obterParticipacao()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select participacao from dados_coaseguro where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("participacao");
    }

    public double obterPrimaGs()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select prima_gs from dados_coaseguro where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("prima_gs");
    }

    public String obterTipoMoedaPrimaGs()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select tipo_moeda_prima_gs from dados_coaseguro where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("tipo_moeda_prima_gs");
    }

    public double obterPrimaMe()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select prima_me from dados_coaseguro where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("prima_me");
    }

    public String obterGrupo()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select grupo from dados_coaseguro where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("grupo");
    }

    public void verificarDuplicidade(Aseguradora aseguradora, Entidade aseguradora2, Apolice apolice, ClassificacaoContas cContas, double numeroEndoso, double certificado)
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select evento.id from evento,dados_coaseguro,apolice where evento.id = dados_coaseguro.id and superior = apolice.id and origem=? and aseguradora = ? and superior = ? and status_apolice = ? and secao = ? and dados_coaseguro.numero_endoso = ? and dados_coaseguro.certificado = ? group by evento.id");
        query.addLong(aseguradora.obterId());
        query.addLong(aseguradora2.obterId());
        query.addLong(apolice.obterId());
        query.addString(apolice.obterStatusApolice());
        query.addLong(cContas.obterId());
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

    public String obterTipoInstrumento()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select tipo_instrumento from dados_coaseguro where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("tipo_instrumento");
    }

    public double obterNumeroEndoso()
        throws Exception
    {
        if(numeroEndoso == 0.0D)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select numero_endoso from dados_coaseguro where id = ?");
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
            SQLQuery query = getModelManager().createSQLQuery("crm", "select certificado from dados_coaseguro where id = ?");
            query.addLong(obterId());
            certificado = query.executeAndGetFirstRow().getDouble("certificado");
        }
        return certificado;
    }
}
