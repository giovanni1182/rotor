package com.gio.crm.model;

import infra.model.Home;
import infra.sql.SQLQuery;

public class ClassificacaoContasHomeImpl extends Home implements ClassificacaoContasHome
{

	public ClassificacaoContas obterClassificacaoContasPorApelido(String apelido) throws Exception
	{
		EntidadeHome home = (EntidadeHome)getModelManager().getHome("EntidadeHome");
    	
    	SQLQuery query = this.getModelManager().createSQLQuery("crm","select entidade.id,classe from entidade,classificacao_contas where entidade.id = classificacao_contas.id and apelido = ?");
    	query.addString(apelido);
    	
    	long id = query.executeAndGetFirstRow().getLong("id");
    	String classe = query.executeAndGetFirstRow().getString("classe");
    	
    	return (ClassificacaoContas) home.instanciarEntidade(id, classe);
	}

}
