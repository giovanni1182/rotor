package com.gio.crm.model;

import java.util.Collection;
import java.util.Date;

public interface RucCiHome 
{
	String obterPessoaPorDoc(String tipoDoc, String numeroDoc) throws Exception;
	Collection<String> obterPessoaPorNome(String tipoPessoa,String nome, String sobreNome, Date dataNasc) throws Exception;
}
