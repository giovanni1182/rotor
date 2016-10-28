package com.gio.crm.model;

import java.util.ArrayList;
import java.util.Collection;

import infra.model.Home;
import infra.sql.SQLQuery;
import infra.sql.SQLRow;

public class RatioUmAnoHomeImpl extends Home implements RatioUmAnoHome 
{
	public Collection obterRatiosUmAno(Aseguradora aseguradora) throws Exception 
	{
		Collection ratios = new ArrayList();
		
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,ratio_um_ano,fase where evento.id = ratio_um_ano.id and evento.id = fase.id and origem = ? and codigo='pendente' and termino=0");
		query.addLong(aseguradora.obterId());
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			
			RatioUmAno ratio = (RatioUmAno) home.obterEventoPorId(id);
			
			ratios.add(ratio);
		}
		
		return ratios;
	}
}