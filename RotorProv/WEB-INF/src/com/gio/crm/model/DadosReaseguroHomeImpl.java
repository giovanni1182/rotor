// FrontEnd Plus GUI for JAD
// DeCompiled : DadosReaseguroHomeImpl.class

package com.gio.crm.model;

import infra.model.Home;
import infra.sql.SQLQuery;

// Referenced classes of package com.gio.crm.model:
//            DadosReaseguroHome, Entidade, Apolice, ClassificacaoContas, 
//            EventoHome, DadosReaseguro

public class DadosReaseguroHomeImpl extends Home implements DadosReaseguroHome
{

    public DadosReaseguroHomeImpl()
    {
    	
    }

    public DadosReaseguro obterDadosReaseguro(Entidade aseguradora, ClassificacaoContas cContas, Apolice apolice, Entidade reaseguradora, String tipoContrato) throws Exception
    {
    	/*SQLQuery query = getModelManager().createSQLQuery("crm", "select TOP 1 evento.id from evento WITH(NOLOCK),dados_reaseguro WITH(NOLOCK),apolice WITH(NOLOCK) where evento.id = dados_reaseguro.id and superior = apolice.id and origem = ? and superior = ? and apolice.numero_apolice = ? and secao = ? and reaseguradora = ? and tipo_contrato = ? group by evento.id");
        query.addLong(aseguradora.obterId());
        query.addLong(apolice.obterId());
        query.addString(apolice.obterNumeroApolice());
        query.addLong(cContas.obterId());
        if(reaseguradora != null)
            query.addLong(reaseguradora.obterId());
        else
            query.addLong(0L);
        query.addString(tipoContrato);*/
    	
    	/*if(reaseguradora != null)
    		System.out.println("select TOP 1 evento.id from evento WITH(NOLOCK),dados_reaseguro WITH(NOLOCK),apolice WITH(NOLOCK) where evento.id = dados_reaseguro.id and superior = apolice.id and origem = "+aseguradora.obterId()+" and superior = "+apolice.obterId()+" and apolice.numero_apolice = '"+apolice.obterNumeroApolice()+"' and secao = "+cContas.obterId()+" and reaseguradora = "+reaseguradora.obterId()+" and tipo_contrato = '"+tipoContrato+"'");
    	else
    		System.out.println("select TOP 1 evento.id from evento WITH(NOLOCK),dados_reaseguro WITH(NOLOCK),apolice WITH(NOLOCK) where evento.id = dados_reaseguro.id and superior = apolice.id and origem = "+aseguradora.obterId()+" and superior = "+apolice.obterId()+" and apolice.numero_apolice = '"+apolice.obterNumeroApolice()+"' and secao = "+cContas.obterId()+" and reaseguradora = 0 and tipo_contrato = '"+tipoContrato+"'");*/
    	
    	SQLQuery query = getModelManager().createSQLQuery("crm","EXEC obterDadosReaseguro ?,?,?,?,?,?");
    	query.addLong(aseguradora.obterId());
    	query.addLong(cContas.obterId());
    	query.addLong(apolice.obterId());
        query.addString(apolice.obterNumeroApolice());
        query.addString(tipoContrato);
        if(reaseguradora != null)
        	query.addLong(reaseguradora.obterId());
        else
        	query.addLong(0L);
        
        DadosReaseguro dados = null;
        long id = query.executeAndGetFirstRow().getLong("id");
        	
        if(id > 0)
        {
            EventoHome home = (EventoHome)getModelManager().getHome("EventoHome");
            dados = (DadosReaseguro)home.obterEventoPorId(id);
        }
        
        return dados;
    }
}
