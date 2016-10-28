package com.gio.crm.model;

import java.util.Collection;

public interface ManutencaoHome
{
	void verificarDuplicidades(Collection<Aseguradora> aseguradoras) throws Exception;
	void datasReaseguro() throws Exception;
	void datasReaseguro2() throws Exception;
	void manutFaturaSinistro() throws Exception;
	void manutUltimaAgenda() throws Exception;
	
	/*void manutDadosPrevisao() throws Exception;
	void manutMorosidade() throws Exception;
	void manutSuplemento() throws Exception;
	void manutRegistroCobranca() throws Exception;
	void manutAnulacaoInstrumento() throws Exception;
	void manutSinistro() throws Exception;
	void manutDadosCoaseguro() throws Exception;
	void manutDadosReaseguro() throws Exception;
	
	 void limparCache() throws Exception;*/
	
}
