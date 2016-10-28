package com.gio.crm.model;


public class MensagemImpl extends EventoImpl implements Mensagem
{
	public String obterIcone() throws Exception
	{
		return "message.gif";
	}

	public boolean permiteExcluir() throws Exception
	{
		return this.obterUsuarioAtual().equals(this.obterCriador());
	}
}