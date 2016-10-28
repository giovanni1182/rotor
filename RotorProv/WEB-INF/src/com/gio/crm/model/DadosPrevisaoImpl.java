// FrontEnd Plus GUI for JAD
// DeCompiled : DadosPrevisaoImpl.class

package com.gio.crm.model;

import java.util.Date;

import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

// Referenced classes of package com.gio.crm.model:
//            EventoImpl, DadosPrevisao, Aseguradora, Apolice, 
//            ClassificacaoContas, EventoHome, Evento

public class DadosPrevisaoImpl extends EventoImpl
    implements DadosPrevisao
{

    private Date dataCorte;
    private double curso;
    private double pendente;
    private double reservas;
    private double fundos;
    private double premios;
    private String tipoInstrumento;
    private double numeroEndoso;
    private double certificado;

    public DadosPrevisaoImpl()
    {
    }

    public void atribuirDataCorte(Date dataCorte)
        throws Exception
    {
        this.dataCorte = dataCorte;
    }

    public void atribuirCurso(double curso)
        throws Exception
    {
        this.curso = curso;
    }

    public void atribuirSinistroPendente(double pendente)
        throws Exception
    {
        this.pendente = pendente;
    }

    public void atribuirReservasMatematicas(double reservas)
        throws Exception
    {
        this.reservas = reservas;
    }

    public void atribuirFundosAcumulados(double fundos)
        throws Exception
    {
        this.fundos = fundos;
    }

    public void atribuirPremios(double premios)
        throws Exception
    {
        this.premios = premios;
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

    public void atualizarDataCorte(Date dataCorte)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update dados_previsao set data_corte = ? where id = ?");
        update.addLong(dataCorte.getTime());
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarCurso(double curso)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update dados_previsao set curso = ? where id = ?");
        update.addDouble(curso);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarSinistroPendente(double pendente)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update dados_previsao set pendentes = ? where id = ?");
        update.addDouble(pendente);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarReservasMatematicas(double reservas)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update dados_previsao set reservas = ? where id = ?");
        update.addDouble(reservas);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarFundosAcumulados(double fundos)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update dados_previsao set fundos = ? where id = ?");
        update.addDouble(fundos);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarPremios(double premios)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update dados_previsao set premios = ? where id = ?");
        update.addDouble(premios);
        update.addLong(obterId());
        update.execute();
    }

    public synchronized void incluir() throws Exception
    {
        super.incluir();
        
        SQLUpdate insert = this.getModelManager().createSQLUpdate("crm", "EXEC incluirDadosPrevisao ?,?,?,?,?,?,?,?,?,?");
        insert.addLong(this.obterId());
        if(dataCorte != null)
            insert.addLong(dataCorte.getTime());
        else
            insert.addLong(0L);
        insert.addDouble(curso);
        insert.addDouble(pendente);
        insert.addDouble(reservas);
        insert.addDouble(fundos);
        insert.addDouble(premios);
        insert.addString(tipoInstrumento);
        insert.addDouble(numeroEndoso);
        insert.addDouble(certificado);
        insert.execute();
        
      /*  String sql = "EXEC incluirDadosPrevisao "+this.obterId();
        if(dataCorte != null)
            sql+=","+dataCorte.getTime();
        else
        	sql+=",0";
        
        sql+=","+curso+","+pendente+","+reservas+","+fundos+","+premios+",'"+tipoInstrumento+"',"+numeroEndoso+","+certificado;
        
        SQLUpdate insert = this.getModelManager().createSQLUpdate("crm", sql);
        insert.execute();*/
    }

    public Date obterDataCorte()
        throws Exception
    {
        if(dataCorte == null)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select data_corte from dados_previsao where id = ?");
            query.addLong(obterId());
            long datalong = query.executeAndGetFirstRow().getLong("data_corte");
            if(datalong > 0L)
                dataCorte = new Date(datalong);
        }
        return dataCorte;
    }

    public double obterCurso()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select curso from dados_previsao where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("curso");
    }

    public double obterSinistroPendente()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select pendentes from dados_previsao where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("pendentes");
    }

    public double obterReservasMatematicas()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select reservas from dados_previsao where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("reservas");
    }

    public double obterFundosAcumulados()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select fundos from dados_previsao where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("fundos");
    }

    public double obterPremios()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select premios from dados_previsao where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("premios");
    }

    public void verificarDuplicidade(Aseguradora aseguradora, Apolice apolice, ClassificacaoContas cContas, Date dataCorte, double numeroEndoso, double certificado) throws Exception
    {
     	SQLQuery query = getModelManager().createSQLQuery("crm", "select evento.id from evento,dados_previsao,apolice where evento.id = dados_previsao.id and superior = apolice.id and origem =? and status_apolice = ? and superior = ? and secao = ? and data_corte = ? and dados_previsao.numero_endoso = ? and dados_previsao.certificado = ? group by evento.id");
        query.addLong(aseguradora.obterId());
        query.addString(apolice.obterStatusApolice());
        query.addLong(apolice.obterId());
        query.addLong(cContas.obterId());
        query.addLong(dataCorte.getTime());
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
        SQLQuery query = getModelManager().createSQLQuery("crm", "select tipo_instrumento from dados_previsao where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("tipo_instrumento");
    }

    public double obterNumeroEndoso()
        throws Exception
    {
        if(numeroEndoso == 0.0D)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select numero_endoso from dados_previsao where id = ?");
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
            SQLQuery query = getModelManager().createSQLQuery("crm", "select certificado from dados_previsao where id = ?");
            query.addLong(obterId());
            certificado = query.executeAndGetFirstRow().getDouble("certificado");
        }
        return certificado;
    }
}
