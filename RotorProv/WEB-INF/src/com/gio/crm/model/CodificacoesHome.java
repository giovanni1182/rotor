package com.gio.crm.model;

public interface CodificacoesHome
{
	CodificacaoPlano obterCodificacaoPlano(String numeroPlano) throws Exception;
	CodificacaoCobertura obterCodificacaoCobertura(String numeroCobertura, CodificacaoPlano plano) throws Exception;
	CodificacaoRisco obterCodificacaoRisco(String numeroRisco, CodificacaoCobertura cobertura) throws Exception;
	CodificacaoDetalhe obterCodificacaoDetalhe(String numeroDetalhe, CodificacaoRisco risco) throws Exception;
}
