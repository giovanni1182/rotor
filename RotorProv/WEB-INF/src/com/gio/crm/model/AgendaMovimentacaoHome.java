package com.gio.crm.model;

public interface AgendaMovimentacaoHome
{
	AgendaMovimentacao obterAgendaNoPeriodo(int mes, int ano, Entidade asseguradora, String tipo) throws Exception;
}
