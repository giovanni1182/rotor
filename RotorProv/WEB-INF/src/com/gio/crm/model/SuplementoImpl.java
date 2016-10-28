// FrontEnd Plus GUI for JAD
// DeCompiled : SuplementoImpl.class

package com.gio.crm.model;

import java.util.Date;

import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

// Referenced classes of package com.gio.crm.model:
//            EventoImpl, Suplemento, Aseguradora, Apolice, 
//            ClassificacaoContas, EventoHome, Evento

public class SuplementoImpl extends EventoImpl
    implements Suplemento
{

    private String numero;
    private Date dataEmissao;
    private String razao;
    private double primaGs;
    private String tipoMoedaPrimaGs;
    private double primaMe;
    private String tipoInstrumento;
    private double numeroEndoso;
    private double certificado;

    public SuplementoImpl()
    {
    }

    public void atribuirNumero(String numero)
        throws Exception
    {
        this.numero = numero;
    }

    public void atribuirDataEmissao(Date data)
        throws Exception
    {
        dataEmissao = data;
    }

    public void atribuirRazao(String numero)
        throws Exception
    {
        razao = numero;
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
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update suplemento set numero = ? where id = ?");
        update.addString(numero);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarDataEmissao(Date data)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update suplemento set data_emissao = ? where id = ?");
        update.addLong(data.getTime());
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarRazao(String razao)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update suplemento set razao = ? where id = ?");
        update.addString(razao);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarPrimaGs(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update suplemento set prima_gs = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarTipoMoedaPrimaGs(String tipo)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update suplemento set tipo_moeda_prima_gs = ? where id = ?");
        update.addString(tipo);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarPrimaMe(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update suplemento set prima_me = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public synchronized void incluir() throws Exception
    {
    	super.incluir();
        
        SQLUpdate insert = this.getModelManager().createSQLUpdate("crm", "EXEC incluirSuplemento ?, ?, ?, ?, ?, ?, ?, ?, ?, ?");
        insert.addLong(this.obterId());
        insert.addString(numero);
        if(dataEmissao != null)
            insert.addLong(dataEmissao.getTime());
        else
            insert.addLong(0L);
        insert.addString(razao);
        insert.addDouble(primaGs);
        insert.addString(tipoMoedaPrimaGs);
        insert.addDouble(primaMe);
        insert.addString(tipoInstrumento);
        insert.addDouble(numeroEndoso);
        insert.addDouble(certificado);
        insert.execute();
    }

    public String obterNumero()
        throws Exception
    {
        if(numero == null)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select numero from suplemento where id = ?");
            query.addLong(obterId());
            numero = query.executeAndGetFirstRow().getString("numero");
        }
        return numero;
    }

    public Date obterDataEmissao() throws Exception
    {
    	if(this.dataEmissao == null)
    	{
	        SQLQuery query = getModelManager().createSQLQuery("crm", "select data_emissao from suplemento where id = ?");
	        query.addLong(obterId());
	        long dataLong = query.executeAndGetFirstRow().getLong("data_emissao");
	        if(dataLong > 0L)
	            this.dataEmissao = new Date(dataLong);
    	}
        return this.dataEmissao;
    }

    public String obterRazao()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select razao from suplemento where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("razao");
    }

    public double obterPrimaGs()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select prima_gs from suplemento where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("prima_gs");
    }

    public String obterTipoMoedaPrimaGs()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select tipo_moeda_prima_gs from suplemento where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("tipo_moeda_prima_gs");
    }

    public double obterPrimaMe()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select prima_me from suplemento where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("prima_me");
    }

    public String obterTipoInstrumento()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select tipo_instrumento from suplemento where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("tipo_instrumento");
    }

    public double obterNumeroEndoso()
        throws Exception
    {
        if(numeroEndoso == 0.0D)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select numero_endoso from suplemento where id = ?");
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
            SQLQuery query = getModelManager().createSQLQuery("crm", "select certificado from suplemento where id = ?");
            query.addLong(obterId());
            certificado = query.executeAndGetFirstRow().getDouble("certificado");
        }
        return certificado;
    }

    public void verificarDuplicidade(Aseguradora aseguradora, Apolice apolice, ClassificacaoContas cContas, double numeroEndoso, double certificado)
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select evento.id from evento,suplemento,apolice where evento.id = suplemento.id and superior = apolice.id and origem=? and superior = ? and status_apolice = ? and secao = ? and suplemento.numero_endoso = ? and suplemento.certificado = ? group by evento.id");
        query.addLong(aseguradora.obterId());
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
}
