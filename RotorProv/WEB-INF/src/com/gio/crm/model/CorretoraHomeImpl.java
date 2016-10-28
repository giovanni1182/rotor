// FrontEnd Plus GUI for JAD
// DeCompiled : CorretoraHomeImpl.class

package com.gio.crm.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import infra.model.Home;
import infra.sql.SQLQuery;
import infra.sql.SQLRow;

// Referenced classes of package com.gio.crm.model:
//            CorretoraHome, EventoHome, Inscricao, Corretora

public class CorretoraHomeImpl extends Home implements CorretoraHome
{

    public CorretoraHomeImpl()
    {
    }

    public Corretora obterCorretoraPorInscricao(String inscricao, String mesAno) throws Exception
    {
        /*SQLQuery query = getModelManager().createSQLQuery("crm", "select evento.id from evento,inscricao where evento.id = inscricao.id and inscricao = ?");
        query.addString(inscricao);*/
        
    	EventoHome home = (EventoHome)getModelManager().getHome("EventoHome");
    	
    	SQLQuery query = getModelManager().createSQLQuery("crm", "select MAX(data_validade) as data,evento.id from evento,inscricao where evento.id = inscricao.id and inscricao = ? group by evento.id");
        query.addString(inscricao);
        
        Corretora corredor = null;
        SQLRow rows[] = query.execute();
        for(int i = 0; i < rows.length; i++)
        {
            long id = rows[i].getLong("id");
            Date dataVencimento = new Date(rows[i].getLong("data"));
            
            Inscricao inscricao2 = (Inscricao)home.obterEventoPorId(id);
            if(inscricao2.obterSituacao().equals("Vigente") && (inscricao2.obterOrigem() instanceof Corretora))
            {
                corredor = (Corretora)inscricao2.obterOrigem();
                break;
            }
            else
            {
            	Date mesAnoMovimentoAgenda = new SimpleDateFormat("MM/yyyy").parse(mesAno);
                String mesAnoVencimentoStr = new SimpleDateFormat("MM/yyyy").format(dataVencimento);
                Date mesAnoMovimentoInscricao = new SimpleDateFormat("MM/yyyy").parse(mesAnoVencimentoStr);
                
                if(mesAnoMovimentoAgenda.compareTo(mesAnoMovimentoInscricao)<=0)
                {
                	corredor = (Corretora)inscricao2.obterOrigem();
                	break;
                }
            }
        }

        return corredor;
    }
}
