// FrontEnd Plus GUI for JAD
// DeCompiled : AspectosLegaisImpl.class

package com.gio.crm.model;

import java.util.Date;

import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

// Referenced classes of package com.gio.crm.model:
//            EventoImpl, AspectosLegais, Aseguradora, Apolice, 
//            ClassificacaoContas, EventoHome, Evento

public class AspectosLegaisImpl extends EventoImpl
    implements AspectosLegais
{

    private String numeroOrdem;
    private Date dataNotificacao;
    private String assunto;
    private String demandante;
    private String demandado;
    private String julgado;
    private String turno;
    private String juiz;
    private String secretaria;
    private String advogado;
    private String circunscricao;
    private String forum;
    private Date dataDemanda;
    private double montanteDemandado;
    private double montanteSentenca;
    private Date dataCancelamento;
    private double responsabilidadeMaxima;
    private double sinistroPendente;
    private String objetoCausa;
    private String tipoInstrumento;
    private double numeroEndoso;
    private double certificado;

    public AspectosLegaisImpl()
    {
    }

    public void atribuirNumeroOrdem(String numero)
        throws Exception
    {
        numeroOrdem = numero;
    }

    public void atribuirDataNotificacao(Date data)
        throws Exception
    {
        dataNotificacao = data;
    }

    public void atribuirAssunto(String assunto)
        throws Exception
    {
        this.assunto = assunto;
    }

    public void atribuirDemandante(String demandante)
        throws Exception
    {
        this.demandante = demandante;
    }

    public void atribuirDemandado(String demandado)
        throws Exception
    {
        this.demandado = demandado;
    }

    public void atribuirJulgado(String julgado)
        throws Exception
    {
        this.julgado = julgado;
    }

    public void atribuirTurno(String turno)
        throws Exception
    {
        this.turno = turno;
    }

    public void atribuirJuiz(String juiz)
        throws Exception
    {
        this.juiz = juiz;
    }

    public void atribuirSecretaria(String secretaria)
        throws Exception
    {
        this.secretaria = secretaria;
    }

    public void atribuirAdvogado(String advogado)
        throws Exception
    {
        this.advogado = advogado;
    }

    public void atribuirCircunscricao(String circunscricao)
        throws Exception
    {
        this.circunscricao = circunscricao;
    }

    public void atribuirForum(String forum)
        throws Exception
    {
        this.forum = forum;
    }

    public void atribuirDataDemanda(Date data)
        throws Exception
    {
        dataDemanda = data;
    }

    public void atribuirMontanteDemandado(double valor)
        throws Exception
    {
        montanteDemandado = valor;
    }

    public void atribuirMontanteSentenca(double valor)
        throws Exception
    {
        montanteSentenca = valor;
    }

    public void atribuirDataCancelamento(Date data)
        throws Exception
    {
        dataCancelamento = data;
    }

    public void atribuirResponsabilidadeMaxima(double valor)
        throws Exception
    {
        responsabilidadeMaxima = valor;
    }

    public void atribuirSinistroPendente(double valor)
        throws Exception
    {
        sinistroPendente = valor;
    }

    public void atribuirObjetoCausa(String objeto)
        throws Exception
    {
        objetoCausa = objeto;
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

    public void atualizarNumeroOrdem(String numero)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update aspectos_legais set numero_ordem = ? where id = ?");
        update.addString(numero);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarDataNotificacao(Date data)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update aspectos_legais set data_notificacao = ? where id = ?");
        update.addLong(data.getTime());
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarAssunto(String assunto)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update aspectos_legais set assunto = ? where id = ?");
        update.addString(assunto);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarDemandante(String demandante)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update aspectos_legais set demandante = ? where id = ?");
        update.addString(demandante);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarDemandado(String demandado)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update aspectos_legais set demandado = ? where id = ?");
        update.addString(demandado);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarJulgado(String julgado)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update aspectos_legais set julgado = ? where id = ?");
        update.addString(julgado);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarTurno(String turno)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update aspectos_legais set turno = ? where id = ?");
        update.addString(turno);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarJuiz(String juiz)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update aspectos_legais set juiz = ? where id = ?");
        update.addString(juiz);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarSecretaria(String secretaria)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update aspectos_legais set secretaria = ? where id = ?");
        update.addString(secretaria);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarAdvogado(String advogado)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update aspectos_legais set advogado = ? where id = ?");
        update.addString(advogado);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarCircunscricao(String circunscricao)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update aspectos_legais set circunscricao = ? where id = ?");
        update.addString(circunscricao);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarForum(String forum)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update aspectos_legais set forum = ? where id = ?");
        update.addString(forum);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarDataDemanda(Date data)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update aspectos_legais set data_demandada = ? where id = ?");
        update.addLong(data.getTime());
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarMontanteDemandado(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update aspectos_legais set montante_demandado = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarMontanteSentenca(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update aspectos_legais set montante_sentenca = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarDataCancelamento(Date data)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update aspectos_legais set data_cancelamento = ? where id = ?");
        update.addLong(data.getTime());
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarResponsabilidadeMaxima(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update aspectos_legais set resp_maxima = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarSinistroPendente(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update aspectos_legais set sinistro = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarObjetoCausa(String objeto)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update aspectos_legais set objeto = ? where id = ?");
        update.addString(objeto);
        update.addLong(obterId());
        update.execute();
    }

    public synchronized void incluir() throws Exception
    {
    	super.incluir();
        
        SQLUpdate insert = this.getModelManager().createSQLUpdate("crm", "EXEC incluirAspectosLegais ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?");
        insert.addLong(this.obterId());
        insert.addString(numeroOrdem);
        if(dataNotificacao != null)
            insert.addLong(dataNotificacao.getTime());
        else
            insert.addLong(0L);
        insert.addString(assunto);
        insert.addString(demandante);
        insert.addString(demandado);
        insert.addString(julgado);
        insert.addString(turno);
        insert.addString(juiz);
        insert.addString(secretaria);
        insert.addString(advogado);
        insert.addString(circunscricao);
        insert.addString(forum);
        if(dataDemanda != null)
            insert.addLong(dataDemanda.getTime());
        else
            insert.addLong(0L);
        insert.addDouble(montanteDemandado);
        insert.addDouble(montanteSentenca);
        if(dataCancelamento != null)
            insert.addLong(dataCancelamento.getTime());
        else
            insert.addLong(0L);
        insert.addDouble(responsabilidadeMaxima);
        insert.addDouble(sinistroPendente);
        insert.addString(objetoCausa);
        insert.addString(tipoInstrumento);
        insert.addDouble(numeroEndoso);
        insert.addDouble(certificado);
        insert.execute();
    }

    public String obterNumeroOrdem()
        throws Exception
    {
        if(numeroOrdem == null)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select numero_ordem from aspectos_legais where id = ?");
            query.addLong(obterId());
            numeroOrdem = query.executeAndGetFirstRow().getString("numero_ordem");
        }
        return numeroOrdem;
    }

    public Date obterDataNotificacao()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select data_notificacao from aspectos_legais where id = ?");
        query.addLong(obterId());
        Date data = null;
        long dataLong = query.executeAndGetFirstRow().getLong("data_notificacao");
        if(dataLong > 0L)
            data = new Date(dataLong);
        return data;
    }

    public String obterAssunto()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select assunto from aspectos_legais where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("assunto");
    }

    public String obterDemandante()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select demandante from aspectos_legais where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("demandante");
    }

    public String obterDemandado()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select demandado from aspectos_legais where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("demandado");
    }

    public String obterJulgado()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select julgado from aspectos_legais where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("julgado");
    }

    public String obterTurno()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select turno from aspectos_legais where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("turno");
    }

    public String obterJuiz()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select juiz from aspectos_legais where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("juiz");
    }

    public String obterSecretaria()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select secretaria from aspectos_legais where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("secretaria");
    }

    public String obterAdvogado()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select advogado from aspectos_legais where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("advogado");
    }

    public String obterCircunscricao()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select circunscricao from aspectos_legais where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("circunscricao");
    }

    public String obterForum()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select forum from aspectos_legais where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("forum");
    }

    public Date obterDataDemanda()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select data_demandada from aspectos_legais where id = ?");
        query.addLong(obterId());
        Date data = null;
        long dataLong = query.executeAndGetFirstRow().getLong("data_demandada");
        if(dataLong > 0L)
            data = new Date(dataLong);
        return data;
    }

    public double obterMontanteDemandado()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select montante_demandado from aspectos_legais where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("montante_demandado");
    }

    public double obterMontanteSentenca()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select montante_sentenca from aspectos_legais where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("montante_sentenca");
    }

    public Date obterDataCancelamento()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select data_cancelamento from aspectos_legais where id = ?");
        query.addLong(obterId());
        Date data = null;
        long dataLong = query.executeAndGetFirstRow().getLong("data_cancelamento");
        if(dataLong > 0L)
            data = new Date(dataLong);
        return data;
    }

    public double obterResponsabilidadeMaxima()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select resp_maxima from aspectos_legais where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("resp_maxima");
    }

    public double obterSinistroPendente()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select sinistro from aspectos_legais where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("sinistro");
    }

    public String obterObjetoCausa()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select objeto from aspectos_legais where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("objeto");
    }

    public String obterTipoInstrumento()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select tipo_instrumento from aspectos_legais where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("tipo_instrumento");
    }

    public double obterNumeroEndoso()
        throws Exception
    {
        if(numeroEndoso == 0.0D)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select numero_endoso from aspectos_legais where id = ?");
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
            SQLQuery query = getModelManager().createSQLQuery("crm", "select certificado from aspectos_legais where id = ?");
            query.addLong(obterId());
            certificado = query.executeAndGetFirstRow().getDouble("certificado");
        }
        return certificado;
    }

    public void verificarDuplicidade(Aseguradora aseguradora, Apolice apolice, ClassificacaoContas cContas, String numeroOrdem, double numeroEndoso, double certificado)
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select evento.id from evento,aspectos_legais,apolice where evento.id = aspectos_legais.id and superior = apolice.id and origem=? and superior = ? and status_apolice = ? and secao = ? and numero_ordem = ? and aspectos_legais.numero_endoso = ? and aspectos_legais.certificado = ? group by evento.id");
        query.addLong(aseguradora.obterId());
        query.addLong(apolice.obterId());
        query.addString(apolice.obterStatusApolice());
        query.addLong(cContas.obterId());
        query.addString(numeroOrdem);
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
