// FrontEnd Plus GUI for JAD
// DeCompiled : AuxiliarSeguroHomeImpl.class

package com.gio.crm.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import infra.model.Home;
import infra.sql.SQLQuery;
import infra.sql.SQLRow;

// Referenced classes of package com.gio.crm.model:
//            AuxiliarSeguroHome, EventoHome, Inscricao, AuxiliarSeguro

public class AuxiliarSeguroHomeImpl extends Home  implements AuxiliarSeguroHome
{

    public AuxiliarSeguroHomeImpl()
    {
    }

    public AuxiliarSeguro obterAuxiliarPorInscricao(String inscricao) throws Exception
    {
    	EventoHome home = (EventoHome)getModelManager().getHome("EventoHome");
        SQLQuery query = getModelManager().createSQLQuery("crm", "select evento.id from evento,inscricao where evento.id = inscricao.id and inscricao = ?");
        query.addString(inscricao);
        AuxiliarSeguro auxliar = null;
        SQLRow rows[] = query.execute();
        for(int i = 0; i < rows.length; i++)
        {
            long id = rows[i].getLong("id");
            Inscricao inscricao2 = (Inscricao)home.obterEventoPorId(id);
            if(inscricao2.obterSituacao().equals("Vigente") && (inscricao2.obterOrigem() instanceof AuxiliarSeguro))
                auxliar = (AuxiliarSeguro)inscricao2.obterOrigem();
        }

        return auxliar;
    }
    
    public AuxiliarSeguro obterAuxiliarPorInscricao(String inscricao, String tipo) throws Exception
    {
    	EntidadeHome home = (EntidadeHome) getModelManager().getHome("EntidadeHome");
    	AuxiliarSeguro auxliar = null;
        
    	/*SQLQuery query = getModelManager().createSQLQuery("crm", "select top 1 origem from evento,inscricao,entidade,entidade_atributo where evento.id = inscricao.id and origem = entidade.id and origem = entidade_atributo.entidade and inscricao = ? and entidade_atributo.nome = 'atividade' and valor like '%" + tipo + "%' group by origem");
        query.addString(inscricao);*/
    	
    	SQLQuery query = getModelManager().createSQLQuery("crm", "EXEC obterAuxiliarPorInscricao '"+inscricao+"', '"+tipo+"'");
        
        SQLRow rows[] = query.execute();
        
        for(int i = 0; i < rows.length; i++)
        {
        	long id = rows[i].getLong("origem");
                
           	auxliar = (AuxiliarSeguro) home.obterEntidadePorId(id);
        }

        return auxliar;
    }

    public AuxiliarSeguro obterAuxiliarPorInscricaoeTipo(String inscricao, String tipo, String mesAno)throws Exception
    {
        AuxiliarSeguro auxliar = null;
        EventoHome home = (EventoHome)getModelManager().getHome("EventoHome");
        
        /*SQLQuery query = getModelManager().createSQLQuery("crm", "select MAX(data_validade) as data,evento.id from evento,inscricao,entidade_atributo where evento.id = inscricao.id and inscricao = ? and entidade_atributo.entidade = origem and entidade_atributo.nome = 'atividade' and valor like '%" + tipo + "%' group by evento.id order by data desc");
        query.addString(inscricao);*/
        
        SQLQuery query = getModelManager().createSQLQuery("crm", "EXEC obterAuxiliarPorInscricaoeTipo '"+inscricao+"', '"+tipo+"'");
        
        SQLRow rows[] = query.execute();
        boolean vigente = false;
        
        for(int i = 0; i < rows.length; i++)
        {
            long id = rows[i].getLong("id");
            Date dataVencimento = new Date(rows[i].getLong("data"));
            
            Inscricao inscricao2 = (Inscricao)home.obterEventoPorId(id);
            
            if(inscricao2.obterSituacao().equals("Vigente") && (inscricao2.obterOrigem() instanceof AuxiliarSeguro))
            {
                vigente = true;
            	auxliar = (AuxiliarSeguro)inscricao2.obterOrigem();
            	break;
            }
            if(!vigente)
            {
                Date mesAnoMovimentoAgenda = new SimpleDateFormat("MM/yyyy").parse(mesAno);
                String mesAnoVencimentoStr = new SimpleDateFormat("MM/yyyy").format(dataVencimento);
                Date mesAnoMovimentoInscricao = new SimpleDateFormat("MM/yyyy").parse(mesAnoVencimentoStr);
                
                if(mesAnoMovimentoAgenda.compareTo(mesAnoMovimentoInscricao)<=0)
                {
                	auxliar = (AuxiliarSeguro)inscricao2.obterOrigem();
                	break;
                }
            }
        }

        return auxliar;
    }
}
