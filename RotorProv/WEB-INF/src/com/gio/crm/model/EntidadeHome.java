package com.gio.crm.model;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

public interface EntidadeHome {
	EntidadeImpl instanciarEntidade(long id, String classe) throws Exception;
	
	Collection localizarEntidades(String pesquisa) throws Exception;

	Collection localizarEntidades(String pesquisa, String classe)
			throws Exception;

	Collection localizarEntidadesPorDestino(String pesquisa, String classe,
			long fornecedor) throws Exception;

	Collection localizarEntidadesEmpUsuPes(String pesquisa) throws Exception;

	Collection obterAseguradoras() throws Exception;

	Collection obterClassesInferiores(Entidade entidade) throws Exception;

	String obterDescricaoClasse(String classe) throws Exception;

	Entidade obterEntidadePorApelido(String apelido) throws Exception;

	Entidade obterEntidadePorSigla(String sigla) throws Exception;

	Entidade obterEntidadePorId(long id) throws Exception;

	Entidade obterEntidadePorInscricao(String inscricaoStr) throws Exception;

	Collection obterEntidades() throws Exception;

	Collection obterEntidadesInferiores(Entidade entidade) throws Exception;

	Collection obterEntidadesPorRaiz() throws Exception;

	Collection obterEntidadesPorResponsavel(Usuario usuario) throws Exception;

	Collection<Entidade> obterEntidadesSuperiores(Entidade entidade) throws Exception;

	Entidade obterEntidadeSuperior(Entidade entidade) throws Exception;

	long obterNumeroEntidadesInferiores(Entidade entidade) throws Exception;

	Collection obterPossiveisSuperiores(Entidade entidade) throws Exception;

	Collection obterPossiveisSuperioresParaCadastro(Entidade entidade)
			throws Exception;

	Map obterTiposEntidade() throws Exception;

	boolean possuiEntidadesInferiores(Entidade entidade) throws Exception;

	boolean possuiEventosVinculados(Entidade entidade) throws Exception;
	Map<String, String> obterReaseguros(Entidade entidade, Date dataInicio,Date dataFim) throws Exception;

	Map obterAseguradoras(Entidade entidade, Date dataInicio, Date dataFim)
			throws Exception;

	Map obterAseguradorasPorCorretora(Entidade entidade, Date dataInicio,
			Date dataFim) throws Exception;
	
	void gravarCIRUC(String tipoDoc, String numero, String tipoPessoa) throws Exception;
	
	void excluirCIRUC() throws Exception;
	boolean existeDocumento(String tipoDocumento, String numero,String tipoPessoa) throws Exception;
	
	void limparSujeiraBDEntidades()throws Exception;
	
	 void manutBase() throws Exception;
	 Entidade obterEntidadePorInscricao(String inscricaoStr,String mesAno) throws Exception;
	 Entidade obterEntidadePorInscricao(String inscricaoStr, String classe,String mesAno) throws Exception;
	 Entidade obterEntidadePorInscricaoSemValidacao(String inscricao, String tipo) throws Exception;
}