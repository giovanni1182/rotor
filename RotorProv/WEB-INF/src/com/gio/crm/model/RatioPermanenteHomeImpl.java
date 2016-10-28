package com.gio.crm.model;

import java.util.ArrayList;
import java.util.Collection;

import infra.model.Home;
import infra.sql.SQLQuery;
import infra.sql.SQLRow;

public class RatioPermanenteHomeImpl extends Home implements RatioPermanenteHome 
{
	public Collection obterRatiosPermanentes(Aseguradora aseguradora) throws Exception 
	{
		Collection ratios = new ArrayList();
		
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,ratio_permanente,fase where evento.id = ratio_permanente.id and evento.id = fase.id and origem = ? and codigo='pendente' and termino=0");
		query.addLong(aseguradora.obterId());
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			
			RatioPermanente ratio = (RatioPermanente) home.obterEventoPorId(id);
			
			ratios.add(ratio);
		}
		
		return ratios;
	}
}