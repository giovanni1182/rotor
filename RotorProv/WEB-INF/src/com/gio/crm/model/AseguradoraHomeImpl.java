// FrontEnd Plus GUI for JAD
// DeCompiled : AseguradoraHomeImpl.class

package com.gio.crm.model;

import java.util.ArrayList;
import java.util.Collection;

import infra.model.Home;
import infra.sql.SQLQuery;
import infra.sql.SQLRow;

// Referenced classes of package com.gio.crm.model:
//            AseguradoraHome, EntidadeHome, Aseguradora

public class AseguradoraHomeImpl extends Home implements AseguradoraHome
{

    public AseguradoraHomeImpl()
    {
    	
    }

    public Collection obterAseguradoras() throws Exception
    {
    	SampleModelManager mm = new SampleModelManager();
    	
    	Collection aseguradoras = new ArrayList();
        EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");
        SQLQuery query = mm.createSQLQuery("crm", "select id from entidade where classe = 'Aseguradora' order by nome");
        SQLRow rows[] = query.execute();
        
        for(int i = 0; i < rows.length; i++)
        {
            long id = rows[i].getLong("id");
            Aseguradora aseguradora = (Aseguradora)home.obterEntidadePorId(id);
            aseguradoras.add(aseguradora);
        }

        return aseguradoras;
    }
    
    public Collection obterAseguradorasPorMenor80OrdenadoPorNome()throws Exception 
	{
		Collection aseguradoras = new ArrayList();
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select origem,nome from evento,inscricao,entidade where evento.id = inscricao.id and evento.origem = entidade.id and situacao='Vigente' and CAST(inscricao AS INT)<=80 group by origem,nome order by nome");
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("origem");
			
			Entidade e = home.obterEntidadePorId(id);
			
			if(e instanceof Aseguradora)
				aseguradoras.add(e);
		}

		return aseguradoras;
	}
    
    public String obterMesAnoGEE() throws Exception
    {
         SQLQuery query = getModelManager().createSQLQuery("crm", "select nome from gee where codigo='00_MES_ANO'");
         
         return query.executeAndGetFirstRow().getString("nome");
    }
    
    /*public Entidade obterAseguradoraPorSigla(String sigla) throws Exception 
    {
    	EntidadeHome home = (EntidadeHome)getModelManager().getHome("EntidadeHome");
    	
    	SQLQuery query = this.getModelManager().createSQLQuery("crm","select entidade.id,classe from entidade,aseguradora where entidade.id = aseguradora.id and sigla = ?");
    	query.addString(sigla);
    	
    	long id = query.executeAndGetFirstRow().getLong("id");
    	String classe = query.executeAndGetFirstRow().getString("classe");
    	
    	return home.instanciarEntidade(id, classe);
    }*/

}
