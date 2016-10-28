package com.gio.crm.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

public class ApoliceImpl extends EventoImpl implements Apolice {

	public class FinalizacaoImpl implements Finalizacao {
		private ApoliceImpl apolice;

		private Date data;

		public FinalizacaoImpl(ApoliceImpl apolice, Date data) throws Exception {
			this.apolice = apolice;
			this.data = data;
		}

		public Apolice obterApolice() throws Exception {
			return this.apolice;
		}

		public Date obterDataFinalizacao() throws Exception {
			return this.data;
		}
	}

	private ClassificacaoContas cContas;

	private String numeroApolice;

	private String statusApolice;

	private Apolice apoliceAnterior;

	private String afetadoPorSinistro;

	private String apoliceFlutuante;

	private Plano plano;

	private String codigoPlano;

	private String numeroFatura;

	private String modalidadeVenda;

	private int diasCobertura;

	private Date dataEmissao;

	private double capitalGs;

	private String tipoMoedaCapitalGuarani;

	private double capitalMe;

	private double primaGs;

	private String tipoMoedaPrimaGs;

	private double primaMe;

	private double principalGs;

	private String tipoMoedaPrincipalGs;

	private double principalMe;

	private String tipoMoedaIncapacidadeGs;

	private double incapacidadeGs;

	private double incapacidadeMe;

	private double enfermidadeGs;

	private String tipoMoedaEnfermidadeGs;

	private double enfermidadeMe;
	private String apoliceSuspeita;

	private double acidentesGs;

	private String tipoMoedaAcidentesGs;

	private double acidentesMe;

	private double outrosGs;

	private String tipoMoedaOutrosGs;

	private double outrosMe;

	private double financiamentoGs;

	private String tipoMoedaFinanciamentoGs;

	private double financiamentoMe;

	private double premiosGs;

	private String tipoMoedaPremiosGs;

	private double premiosMe;

	private String formaPagamento;

	private int qtdeParcelas;

	private String refinacao;

	private Entidade inscricaoAgente;

	private double comissaoGs;

	private String tipoMoedaComissaoGs;

	private double comissaoMe;

	private String situacaoSeguro;

	private Date dataEncerramento;

	private String nomeAsegurado;

	private String tipoPessoa;

	private String tipoIdentificacao;

	private String numeroIdentificacao;

	private Date dataNascimento;

	private String nomeTomador;

	private Entidade corredor;

	private double numeroEndoso;

	private double certificado;

	private double numeroEndosoAnterior;

	private double certificadoAnterior;

	public void atribuirSecao(ClassificacaoContas cContas) throws Exception {
		this.cContas = cContas;
	}

	public void atribuirNumeroApolice(String numero) throws Exception {
		this.numeroApolice = numero;
	}

	public void atribuirStatusApolice(String status) throws Exception {
		this.statusApolice = status;
	}

	public void atribuirApoliceAnterior(Apolice apolice) throws Exception {
		this.apoliceAnterior = apolice;
	}

	public void atribuirAfetadoPorSinistro(String valor) throws Exception {
		this.afetadoPorSinistro = valor;
	}

	public void atribuirApoliceFlutuante(String valor) throws Exception {
		this.apoliceFlutuante = valor;
	}

	public void atribuirPlano(Plano plano) throws Exception {
		this.plano = plano;
	}

	public void atribuirCodigoPlano(String codigoPlano) throws Exception {
		this.codigoPlano = codigoPlano;
	}

	public void atribuirNumeroFatura(String numero) throws Exception {
		this.numeroFatura = numero;
	}

	public void atribuirModalidadeVenda(String valor) throws Exception {
		this.modalidadeVenda = valor;
	}

	public void atribuirDiasCobertura(int dias) throws Exception {
		this.diasCobertura = dias;
	}

	public void atribuirDataEmissao(Date data) throws Exception {
		this.dataEmissao = data;
	}

	public void atribuirCapitalGs(double valor) throws Exception {
		this.capitalGs = valor;
	}

	public void atribuirTipoMoedaCapitalGuarani(String tipo) throws Exception {
		this.tipoMoedaCapitalGuarani = tipo;
	}

	public void atribuirCapitalMe(double valor) throws Exception {
		this.capitalMe = valor;
	}

	public void atribuirPrimaGs(double valor) throws Exception {
		this.primaGs = valor;
	}

	public void atribuirTipoMoedaPrimaGs(String tipo) throws Exception {
		this.tipoMoedaPrimaGs = tipo;
	}

	public void atribuirPrimaMe(double valor) throws Exception {
		this.primaMe = valor;
	}

	public void atribuirPrincipalGs(double valor) throws Exception {
		this.principalGs = valor;
	}

	public void atribuirTipoMoedaPrincipalGs(String tipo) throws Exception {
		this.tipoMoedaPrincipalGs = tipo;
	}

	public void atribuirPrincipalMe(double valor) throws Exception {
		this.primaMe = valor;
	}

	public void atribuirIncapacidadeGs(double valor) throws Exception {
		this.incapacidadeGs = valor;
	}

	public void atribuirTipoMoedaIncapacidadeGs(String tipo) throws Exception {
		this.tipoMoedaIncapacidadeGs = tipo;
	}

	public void atribuirIncapacidadeMe(double valor) throws Exception {
		this.incapacidadeMe = valor;
	}

	public void atribuirEnfermidadeGs(double valor) throws Exception {
		this.enfermidadeGs = valor;
	}

	public void atribuirTipoMoedaEnfermidadeGs(String tipo) throws Exception {
		this.tipoMoedaEnfermidadeGs = tipo;
	}

	public void atribuirEnfermidadeMe(double valor) throws Exception {
		this.enfermidadeMe = valor;
	}
	
	public void atribuirApoliceSuspeita(String suspeita) throws Exception
	{
		this.apoliceSuspeita = suspeita;
	}

	public void atribuirAcidentesGs(double valor) throws Exception {
		this.acidentesGs = valor;
	}

	public void atribuirTipoMoedaAcidentesGs(String tipo) throws Exception {
		this.tipoMoedaAcidentesGs = tipo;
	}

	public void atribuirAcidentesMe(double valor) throws Exception {
		this.acidentesMe = valor;
	}

	public void atribuirOutrosGs(double valor) throws Exception {
		this.outrosGs = valor;
	}

	public void atribuirTipoMoedaOutrosGs(String tipo) throws Exception {
		this.tipoMoedaOutrosGs = tipo;
	}

	public void atribuirOutrosMe(double valor) throws Exception {
		this.outrosMe = valor;
	}

	public void atribuirFinanciamentoGs(double valor) throws Exception {
		this.financiamentoGs = valor;
	}

	public void atribuirTipoMoedaFinanciamentoGs(String tipo) throws Exception {
		this.tipoMoedaFinanciamentoGs = tipo;
	}

	public void atribuirFinanciamentoMe(double valor) throws Exception {
		this.financiamentoMe = valor;
	}

	public void atribuirPremiosGs(double valor) throws Exception {
		this.premiosGs = valor;
	}

	public void atribuirTipoMoedaPremiosGs(String tipo) throws Exception {
		this.tipoMoedaPremiosGs = tipo;
	}

	public void atribuirPremiosMe(double valor) throws Exception {
		this.premiosMe = valor;
	}

	public void atribuirFormaPagamento(String tipo) throws Exception {
		this.formaPagamento = tipo;
	}

	public void atribuirQtdeParcelas(int qtde) throws Exception {
		this.qtdeParcelas = qtde;
	}

	public void atribuirRefinacao(String refinacao) throws Exception {
		this.refinacao = refinacao;
	}

	public void atribuirInscricaoAgente(Entidade agente) throws Exception {
		this.inscricaoAgente = agente;
	}

	public void atribuirComissaoGs(double valor) throws Exception {
		this.comissaoGs = valor;
	}

	public void atribuirTipoMoedaComissaoGs(String tipo) throws Exception {
		this.tipoMoedaComissaoGs = tipo;
	}

	public void atribuirComissaoMe(double valor) throws Exception {
		this.comissaoMe = valor;
	}

	public void atribuirSituacaoSeguro(String situacao) throws Exception {
		this.situacaoSeguro = situacao;
	}

	public void atribuirDataEncerramento(Date data) throws Exception {
		this.dataEncerramento = data;
	}

	public void atribuirNomeAsegurado(String nome) throws Exception {
		this.nomeAsegurado = nome;
	}

	public void atribuirTipoPessoa(String tipo) throws Exception {
		this.tipoPessoa = tipo;
	}

	public void atribuirTipoIdentificacao(String tipo) throws Exception {
		this.tipoIdentificacao = tipo;
	}

	public void atribuirNumeroIdentificacao(String numero) throws Exception {
		this.numeroIdentificacao = numero;
	}

	public void atribuirDataNascimento(Date data) throws Exception {
		this.dataNascimento = data;
	}

	public void atribuirNomeTomador(String nome) throws Exception {
		this.nomeTomador = nome;
	}

	public void atribuirCorredor(Entidade corredor) throws Exception {
		this.corredor = corredor;
	}

	public void atribuirNumeroEndoso(double numeroEndoso) throws Exception {
		this.numeroEndoso = numeroEndoso;
	}

	public void atribuirCertificado(double certificado) throws Exception {
		this.certificado = certificado;
	}

	public void atribuirNumeroEndosoAnterior(double numeroEndosoAnterior)
			throws Exception {
		this.numeroEndosoAnterior = numeroEndosoAnterior;
	}

	public void atribuirCertificadoAnterior(double certificadoAnterior)
			throws Exception {
		this.certificadoAnterior = certificadoAnterior;
	}

	public Collection obterFinalizacoes() throws Exception {
		Map finalizacoes = new TreeMap();

		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select * from apolice_finalizacao where id = ?");
		query.addLong(this.obterId());

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++) {
			long dataLong = rows[i].getLong("data");

			finalizacoes.put(new Long(dataLong), new FinalizacaoImpl(this,
					new Date(dataLong)));
		}

		return finalizacoes.values();
	}

	public void adicionarFinalizacao(Date data) throws Exception {
		SQLUpdate insert = this.getModelManager().createSQLUpdate("crm",
				"insert into apolice_finalizacao(id, data) values(?, ?)");
		insert.addLong(this.obterId());
		insert.addLong(data.getTime());

		insert.execute();
	}

	public void excluirFinalizacao() throws Exception {
		SQLUpdate delete = this.getModelManager().createSQLUpdate("crm",
				"delete from apolice_finalizacao where id = ?");
		delete.addLong(this.obterId());

		delete.execute();
	}

	public void atualizarSecao(ClassificacaoContas cContas) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update apolice set secao = ? where id = ?");
		update.addLong(cContas.obterId());
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarNumeroApolice(String numero) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update apolice set numero_apolice = ? where id = ?");
		update.addString(numero);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarStatusApolice(String status) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update apolice set status_apolice = ? where id = ?");
		update.addString(status);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarApoliceAnterior(Apolice apolice) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update apolice set apolice_anterior = ? where id = ?");
		update.addLong(apolice.obterId());
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarAfetadoPorSinistro(String valor) throws Exception
    {
    	SQLUpdate update = this.getModelManager().createSQLUpdate("crm","EXEC atualizarAfetadoPorSinistro ?,?");
    	update.addString(valor);
        update.addLong(this.obterId());
        update.execute();
    }

	public void atualizarApoliceFlutuante(String valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update apolice set apolice_flutuante = ? where id = ?");
		update.addString(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarPlano(Plano plano) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update apolice set plano = ? where id = ?");
		update.addLong(plano.obterId());
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarNumeroFatura(String numero) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update apolice set numero_fatura = ? where id = ?");
		update.addString(numero);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarModalidadeVenda(String valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update apolice set modalidade_venda = ? where id = ?");
		update.addString(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarDiasCobertura(int dias) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update apolice set dias_cobertura = ? where id = ?");
		update.addInt(dias);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarDataEmissao(Date data) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update apolice set data_emissao = ? where id = ?");
		update.addLong(data.getTime());
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarCapitalGs(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update apolice set capital_gs = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarTipoMoedaCapitalGuarani(String tipo) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update apolice set tipo_moeda_capital_gs = ? where id = ?");
		update.addString(tipo);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarCapitalMe(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update apolice set capital_me = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarPrimaGs(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update apolice set prima_gs = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarTipoMoedaPrimaGs(String tipo) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update apolice set tipo_moeda_prima_gs = ? where id = ?");
		update.addString(tipo);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarPrimaMe(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update apolice set prima_me = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarPrincipalGs(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update apolice set principal_gs = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarTipoMoedaPrincipalGs(String tipo) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update apolice set tipo_moeda_principal_gs = ? where id = ?");
		update.addString(tipo);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarPrincipalMe(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update apolice set principal_me = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarIncapacidadeGs(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update apolice set incapacidade_gs = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarTipoMoedaIncapacidadeGs(String tipo) throws Exception {
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate("crm",
						"update apolice set tipo_moeda_incapacidade_gs = ? where id = ?");
		update.addString(tipo);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarIncapacidadeMe(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update apolice set incapacidade_me = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarEnfermidadeGs(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update apolice set enfermidade_gs = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarTipoMoedaEnfermidadeGs(String tipo) throws Exception {
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate("crm",
						"update apolice set tipo_moeda_enfermidade_gs = ? where id = ?");
		update.addString(tipo);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarEnfermidadeMe(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update apolice set enfermidade_me = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarAcidentesGs(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update apolice set acidentes_gs = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarTipoMoedaAcidentesGs(String tipo) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update apolice set tipo_moeda_acidentes_gs = ? where id = ?");
		update.addString(tipo);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarAcidentesMe(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update apolice set acidentes_me = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarOutrosGs(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update apolice set outros_gs = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarTipoMoedaOutrosGs(String tipo) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update apolice set tipo_moeda_outros_gs = ? where id = ?");
		update.addString(tipo);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarOutrosMe(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update apolice set outros_me = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarFinanciamentoGs(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update apolice set financiamento_gs = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarTipoMoedaFinanciamentoGs(String tipo) throws Exception {
		SQLUpdate update = this
				.getModelManager()
				.createSQLUpdate("crm",
						"update apolice set tipo_moeda_financiamento_gs = ? where id = ?");
		update.addString(tipo);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarFinanciamentoMe(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update apolice set financiamento_me = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarPremiosGs(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update apolice set premio_gs = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarTipoMoedaPremiosGs(String tipo) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update apolice set tipo_moeda_premio_gs = ? where id = ?");
		update.addString(tipo);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarPremiosMe(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update apolice set premio_me = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarFormaPagamento(String tipo) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update apolice set forma_pagamento = ? where id = ?");
		update.addString(tipo);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarQtdeParcelas(int qtde) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update apolice set parcelas = ? where id = ?");
		update.addInt(qtde);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarRefinacao(String refinacao) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update apolice set refinacao = ? where id = ?");
		update.addString(refinacao);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarInscricaoAgente(Entidade agente) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update apolice set agente = ? where id = ?");
		update.addLong(agente.obterId());
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarComissaoGs(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update apolice set comissao_gs = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarTipoMoedaComissaoGs(String tipo) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update apolice set tipo_moeda_comissao_gs = ? where id = ?");
		update.addString(tipo);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarComissaoMe(double valor) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update apolice set comissao_me = ? where id = ?");
		update.addDouble(valor);
		update.addLong(this.obterId());

		update.execute();
	}

	 public void atualizarSituacaoSeguro(String situacao) throws Exception
	 {
		 SQLUpdate update = this.getModelManager().createSQLUpdate("crm", "EXEC atualizarSituacaoSeguro ?,?");
		 update.addString(situacao);
		 update.addLong(this.obterId());
		 update.execute();
	 }

	public void atualizarNomeAsegurado(String nome) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update apolice set nome_asegurado = ? where id = ?");
		update.addString(nome);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarTipoPessoa(String tipo) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update apolice set tipo_pessoa = ? where id = ?");
		update.addString(tipo);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarDataEncerramento(Date data) throws Exception
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm", "EXEC atualizarDataEncerramentoApolice ?,?");
		update.addLong(data.getTime());
		update.addLong(this.obterId());
	        
		update.execute();
	}

	public void atualizarTipoIdentificacao(String tipo) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update apolice set tipo_indentificacao = ? where id = ?");
		update.addString(tipo);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarNumeroIdentificacao(String numero) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update apolice set numero_identificacao = ? where id = ?");
		update.addString(numero);
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarDataNascimento(Date data) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update apolice set data_nascimento = ? where id = ?");
		update.addLong(data.getTime());
		update.addLong(this.obterId());

		update.execute();
	}

	public void atualizarNomeTomador(String nome) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm",
				"update apolice set nome_tomador = ? where id = ?");
		update.addString(nome);
		update.addLong(this.obterId());

		update.execute();
	}

	public synchronized void incluir() throws Exception 
	{
		super.incluir();
    	
   	 	SQLUpdate insert = this.getModelManager().createSQLUpdate("crm", "EXEC incluirApolice ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?");
        insert.addLong(this.obterId());
        insert.addLong(cContas.obterId());
        insert.addString(numeroApolice);
        insert.addString(statusApolice);
        if(apoliceAnterior != null)
            insert.addLong(apoliceAnterior.obterId());
        else
            insert.addLong(0L);
        insert.addString(afetadoPorSinistro);
        insert.addString(apoliceFlutuante);
        if(plano != null)
            insert.addLong(plano.obterId());
        else
            insert.addLong(0L);
        insert.addString(numeroFatura);
        insert.addString(modalidadeVenda);
        insert.addInt(diasCobertura);
        if(dataEmissao != null)
            insert.addLong(dataEmissao.getTime());
        else
            insert.addLong(0L);
        insert.addDouble(capitalGs);
        insert.addString(tipoMoedaCapitalGuarani);
        insert.addDouble(capitalMe);
        insert.addDouble(primaGs);
        insert.addString(tipoMoedaPrimaGs);
        insert.addDouble(primaMe);
        insert.addDouble(principalGs);
        insert.addDouble(principalMe);
        insert.addDouble(incapacidadeGs);
        insert.addString(tipoMoedaIncapacidadeGs);
        insert.addDouble(incapacidadeMe);
        insert.addDouble(enfermidadeGs);
        insert.addDouble(acidentesGs);
        insert.addString(tipoMoedaAcidentesGs);
        insert.addDouble(acidentesMe);
        insert.addDouble(outrosGs);
        insert.addString(tipoMoedaOutrosGs);
        insert.addDouble(outrosMe);
        insert.addDouble(financiamentoGs);
        insert.addString(tipoMoedaFinanciamentoGs);
        insert.addDouble(financiamentoMe);
        insert.addDouble(premiosGs);
        insert.addString(tipoMoedaPremiosGs);
        insert.addDouble(premiosMe);
        insert.addString(formaPagamento);
        insert.addInt(qtdeParcelas);
        insert.addString(refinacao);
        if(inscricaoAgente != null)
            insert.addLong(inscricaoAgente.obterId());
        else
            insert.addLong(0L);
        insert.addDouble(comissaoGs);
        insert.addString(tipoMoedaComissaoGs);
        insert.addDouble(comissaoMe);
        insert.addString(situacaoSeguro);
        insert.addString(tipoMoedaEnfermidadeGs);
        insert.addDouble(enfermidadeMe);
        insert.addString(codigoPlano);
        insert.addString(nomeAsegurado);
        insert.addString(tipoPessoa);
        insert.addString(tipoIdentificacao);
        insert.addString(numeroIdentificacao);
        if(dataNascimento != null)
            insert.addLong(dataNascimento.getTime());
        else
            insert.addLong(0L);
        insert.addString(nomeTomador);
        if(corredor != null)
            insert.addLong(corredor.obterId());
        else
            insert.addLong(0L);
        insert.addDouble(numeroEndoso);
        insert.addDouble(certificado);
        insert.addDouble(numeroEndosoAnterior);
        insert.addDouble(certificadoAnterior);
        insert.addString(apoliceSuspeita);
        
        insert.execute();
        
        if(this.getAsegurados()!=null)
        {
        	for(EntidadeBCP entidadeBcp : this.getAsegurados())
        	{
        		insert = this.getModelManager().createSQLUpdate("crm", "EXEC incluirAsegurado ?,?,?,?,?,?,?,?");
        		insert.addLong(this.obterId());
        		insert.addString(entidadeBcp.getNome());
        		insert.addString(entidadeBcp.getSobreNome());
        		insert.addString(entidadeBcp.getTipoPessoa());
        		insert.addString(entidadeBcp.getTipoDocumento());
        		insert.addString(entidadeBcp.getNumeroDoc());
        		if(entidadeBcp.getDataNascimento()!=null)
        			insert.addLong(entidadeBcp.getDataNascimento().getTime());
        		else
        			insert.addLong(0);
        		insert.addString(entidadeBcp.getPais());
        		
        		insert.execute();
        	}
        }
        
        if(this.getTomadores()!=null)
        {
        	for(EntidadeBCP entidadeBcp : this.getTomadores())
        	{
        		insert = this.getModelManager().createSQLUpdate("crm", "EXEC incluirTomador ?,?,?,?,?,?,?,?");
        		insert.addLong(this.obterId());
        		insert.addString(entidadeBcp.getNome());
        		insert.addString(entidadeBcp.getSobreNome());
        		insert.addString(entidadeBcp.getTipoPessoa());
        		insert.addString(entidadeBcp.getTipoDocumento());
        		insert.addString(entidadeBcp.getNumeroDoc());
        		if(entidadeBcp.getDataNascimento()!=null)
        			insert.addLong(entidadeBcp.getDataNascimento().getTime());
        		else
        			insert.addLong(0);
        		insert.addString(entidadeBcp.getPais());
        		
        		insert.execute();
        	}
        }
	}

	public ClassificacaoContas obterSecao() throws Exception {
		if (this.cContas == null) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select secao from apolice where id = ?");
			query.addLong(this.obterId());

			long id = query.executeAndGetFirstRow().getLong("secao");

			//ClassificacaoContas cContas = null;

			if (id > 0) {
				EntidadeHome home = (EntidadeHome) this.getModelManager()
						.getHome("EntidadeHome");
				this.cContas = (ClassificacaoContas) home
						.obterEntidadePorId(id);
			}
		}

		return cContas;
	}

	public String obterNumeroApolice() throws Exception
	{
		if(numeroApolice == null)
		{
			/*SQLQuery query = getModelManager().createSQLQuery("crm", "select numero_apolice from apolice where id = ?");
			query.addLong(obterId());*/
	        	
			SQLQuery query = getModelManager().createSQLQuery("crm", "EXEC obterNumeroApolice "+this.obterId());
	            
			numeroApolice = query.executeAndGetFirstRow().getString("numero_apolice");
		}
		return numeroApolice;
	}

	public String obterStatusApolice() throws Exception 
	{
		if(statusApolice == null)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "EXEC obterStatusApolice " + this.obterId());
            
            statusApolice = query.executeAndGetFirstRow().getString("status_apolice");
        }
        return statusApolice;
	}

	public Apolice obterApoliceAnterior() throws Exception
	{
		if(apoliceAnterior == null)
		{
			SQLQuery query = getModelManager().createSQLQuery("crm", "EXEC obterApoliceAnterior ?");
			query.addLong(this.obterId());
	        	
			long id = query.executeAndGetFirstRow().getLong("apolice_anterior");
			if(id > 0)
			{
				EventoHome home = (EventoHome)getModelManager().getHome("EventoHome");
				apoliceAnterior = (Apolice)home.obterEventoPorId(id);
			}
		}
		return apoliceAnterior;
	}

	public String obterAfetadoPorSinistro() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select afetado_sinistro from apolice where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("afetado_sinistro");
	}

	public String obterApoliceFlutuante() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select apolice_flutuante from apolice where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("apolice_flutuante");
	}

	public Plano obterPlano() throws Exception
    {
        //SQLQuery query = getModelManager().createSQLQuery("crm", "select plano from apolice where id = ?");
    	SQLQuery query = getModelManager().createSQLQuery("crm", "EXEC obterPlanoApolice ?");
        query.addLong(obterId());
        long id = query.executeAndGetFirstRow().getLong("plano");
        Plano plano = null;
        if(id > 0L)
        {
            EventoHome home = (EventoHome)getModelManager().getHome("EventoHome");
            plano = (Plano)home.obterEventoPorId(id);
        }
        return plano;
    }

	public String obterCodigoPlano() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select codigo_plano from apolice where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("codigo_plano");
	}

	public String obterNumeroFatura() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select numero_fatura from apolice where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("numero_fatura");
	}

	public String obterModalidadeVenda() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select modalidade_venda from apolice where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("modalidade_venda");
	}

	public int obterDiasCobertura() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select dias_cobertura from apolice where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getInt("dias_cobertura");
	}

	 public Date obterDataEmissao() throws Exception
	 {
		 if(this.dataEmissao == null)
		 {
			 SQLQuery query = getModelManager().createSQLQuery("crm", "EXEC obterDataEmissao "+this.obterId());
		        
			 long dataLong = query.executeAndGetFirstRow().getLong("data_emissao");
			 if(dataLong > 0L)
				 this.dataEmissao = new Date(dataLong);
		 }
		 return this.dataEmissao;
	 }

	public double obterCapitalGs() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select capital_gs from apolice where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("capital_gs");
	}

	public String obterTipoMoedaCapitalGuarani() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select tipo_moeda_capital_gs from apolice where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("tipo_moeda_capital_gs");
	}

	public double obterCapitalMe() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select capital_me from apolice where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("capital_me");
	}

	public double obterPrimaGs() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select prima_gs from apolice where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("prima_gs");
	}

	public String obterTipoMoedaPrimaGs() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select tipo_moeda_prima_gs from apolice where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("tipo_moeda_prima_gs");
	}

	public double obterPrimaMe() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select prima_me from apolice where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("prima_me");
	}

	public double obterPrincipalGs() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select principal_gs from apolice where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("principal_gs");
	}

	public String obterTipoMoedaPrincipalGs() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select tipo_moeda_principal_gs from apolice where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString(
				"tipo_moeda_principal_gs");
	}

	public double obterPrincipalMe() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select principal_me from apolice where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("principal_me");
	}

	public double obterIncapacidadeGs() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select incapacidade_gs from apolice where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("incapacidade_gs");
	}

	public String obterTipoMoedaIncapacidadeGs() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select tipo_moeda_incapacidade_gs from apolice where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString(
				"tipo_moeda_incapacidade_gs");
	}

	public double obterIncapacidadeMe() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select incapacidade_me from apolice where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("incapacidade_me");
	}

	public double obterEnfermidadeGs() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select enfermidade_gs from apolice where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("enfermidade_gs");
	}

	public String obterTipoMoedaEnfermidadeGs() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select tipo_moeda_enfermidade_gs from apolice where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString(
				"tipo_moeda_enfermidade_gs");
	}

	public double obterEnfermidadeMe() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select enfermidade_me from apolice where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("enfermidade_me");
	}

	public double obterAcidentesGs() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select acidentes_gs from apolice where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("acidentes_gs");
	}

	public String obterTipoMoedaAcidentesGs() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select tipo_moeda_acidentes_gs from apolice where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString(
				"tipo_moeda_acidentes_gs");
	}

	public double obterAcidentesMe() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select acidentes_me from apolice where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("acidentes_me");
	}

	public double obterOutrosGs() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select outros_gs from apolice where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("outros_gs");
	}

	public String obterTipoMoedaOutrosGs() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select tipo_moeda_outros_gs from apolice where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("tipo_moeda_outros_gs");
	}

	public double obterOutrosMe() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select outros_me from apolice where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("outros_me");
	}

	public double obterFinanciamentoGs() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select financiamento_gs from apolice where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("financiamento_gs");
	}

	public String obterTipoMoedaFinanciamentoGs() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select tipo_moeda_financiamento_gs from apolice where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString(
				"tipo_moeda_financiamento_gs");
	}

	public double obterFinanciamentoMe() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select financiamento_me from apolice where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("financiamento_me");
	}

	public double obterPremiosGs() throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select premio_gs from apolice where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("premio_gs");
	}

	public String obterTipoMoedaPremiosGs() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select tipo_moeda_premio_gs from apolice where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("tipo_moeda_premio_gs");
	}

	public double obterPremiosMe() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select premio_me from apolice where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("premio_me");
	}

	public String obterFormaPagamento() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select forma_pagamento from apolice where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("forma_pagamento");
	}

	public int obterQtdeParcelas() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select parcelas from apolice where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getInt("parcelas");
	}

	public String obterRefinacao() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select refinacao from apolice where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("refinacao");
	}

	public Entidade obterInscricaoAgente() throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select agente from apolice where id = ?");
		query.addLong(this.obterId());

		Entidade agente = null;
		long id = query.executeAndGetFirstRow().getLong("agente");

		if (id > 0)
		{
			EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
			agente = home.obterEntidadePorId(id);
		}

		return agente;
	}

	public double obterComissaoGs() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select comissao_gs from apolice where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("comissao_gs");
	}

	public String obterTipoMoedaComissaoGs() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select tipo_moeda_comissao_gs from apolice where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow()
				.getString("tipo_moeda_comissao_gs");
	}

	public double obterComissaoMe() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select comissao_me from apolice where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getDouble("comissao_me");
	}

	public String obterSituacaoSeguro() throws Exception
    {
    	if(this.situacaoSeguro == null)
    	{
    		SQLQuery query = getModelManager().createSQLQuery("crm", "EXEC obterSituacaoSeguro ?");
    		query.addLong(obterId());
	        
	        this.situacaoSeguro =  query.executeAndGetFirstRow().getString("situacao_seguro");
    	}
    	
    	return this.situacaoSeguro;
    }

	public Date obterDataEncerramento() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select data_encerramento from apolice where id = ?");
		query.addLong(this.obterId());

		Date data = null;

		long dataLong = query.executeAndGetFirstRow().getLong(
				"data_encerramento");

		//if(dataLong > 0)
		data = new Date(dataLong);

		return data;
	}

	public String obterNomeAsegurado() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select nome_asegurado from apolice where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("nome_asegurado");
	}

	public String obterTipoPessoa() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select tipo_pessoa from apolice where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("tipo_pessoa");
	}

	public String obterTipoIdentificacao() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select tipo_indentificacao from apolice where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("tipo_indentificacao");
	}

	public String obterNumeroIdentificacao() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select numero_identificacao from apolice where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("numero_identificacao");
	}

	public Date obterDataNascimento() throws Exception 
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select data_nascimento from apolice where id = ?");
		query.addLong(this.obterId());

		Date data = null;

		long dataLong = query.executeAndGetFirstRow().getLong("data_nascimento");

		if(dataLong > 0)
			data = new Date(dataLong);
		return data;
	}

	public String obterNomeTomador() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select nome_tomador from apolice where id = ?");
		query.addLong(this.obterId());

		return query.executeAndGetFirstRow().getString("nome_tomador");
	}

	public Entidade obterCorredor() throws Exception
	{
		Entidade corredor = null;

		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");

		SQLQuery query = this.getModelManager().createSQLQuery("crm","select corredor from apolice where id = ?");
		query.addLong(this.obterId());

		long id = query.executeAndGetFirstRow().getLong("corredor");

		if (id > 0)
			corredor = home.obterEntidadePorId(id);

		return corredor;
	}

	public double obterNumeroEndoso() throws Exception {
		if (this.numeroEndoso == 0) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select numero_endoso from apolice where id = ?");
			query.addLong(this.obterId());

			this.numeroEndoso = query.executeAndGetFirstRow().getDouble(
					"numero_endoso");
		}

		return this.numeroEndoso;
	}

	public double obterCertificado() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select certificado from apolice where id = ?");
		query.addLong(this.obterId());

		this.certificado = query.executeAndGetFirstRow().getDouble(
				"certificado");

		return this.certificado;
	}

	public double obterNumeroEndosoAnterior() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select numero_endoso_anterior from apolice where id = ?");
		query.addLong(this.obterId());

		this.numeroEndosoAnterior = query.executeAndGetFirstRow().getDouble(
				"numero_endoso_anterior");

		return this.numeroEndosoAnterior;
	}

	public double obterCertificadoAnterior() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select certificado_anterior from apolice where id = ?");
		query.addLong(this.obterId());

		this.certificadoAnterior = query.executeAndGetFirstRow().getDouble(
				"certificado_anterior");

		return this.certificadoAnterior;
	}
	
	public Collection obterSinistros() throws Exception
	{
		Collection sinistros = new ArrayList();
		
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,sinsitro where evento.id = sinistro.id and superior = ?");
		query.addLong(this.obterId());
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			
			Sinistro sinistro = (Sinistro) home.obterEventoPorId(id);
			
			sinistros.add(sinistro);
		}
		
		return sinistros;
	}
	
	public double obterValorTotalDosSinistros(Date dataInicio, Date dataFim) throws Exception
	{
		double total = 0;
		Map<String, String> dado = new TreeMap<String, String>();
		String sql = "select numero,montante_gs from evento,sinistro where evento.id = sinistro.id and superior = "+this.obterId();
		
		//SQLQuery query = this.getModelManager().createSQLQuery("crm","select SUM(montante_gs) as montante from evento,sinistro,apolice where evento.id = sinistro.id and superior = apolice.id and superior = ? and data_prevista_inicio>= ? and data_prevista_conclusao<= ?");
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			String numero = rows[i].getString("numero");
			double montante = rows[i].getDouble("montante_gs");
			
			if(!dado.containsKey(numero))
			{
				total+=montante;
				dado.put(numero, numero);
			}
		}
		
		return total;
	}
	
	public String obterApoliceSuspeita() throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select apolice_suspeita from apolice where id = ?");
		query.addLong(this.obterId());
		
		if(query.executeAndGetFirstRow().getString("apolice_suspeita").equals("S"))
			return "Sí";
		else
			return "No";
	}
	
	public boolean estaAnulada() throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,anulacao_instrumento where evento.id = anulacao_instrumento.id and superior = ?");
		query.addLong(this.obterId());
		
		if(query.execute().length > 0)
			return true;
		else
			return false;
	}

	public double obterMontanteGsSinistro(String situacao, Date dataInicio, Date dataFim) throws Exception
	{
		double valor = 0;
		
		if(situacao.equals("Vigente Judicializado") || situacao.equals("No Vigente Judicializado"))
		{
			boolean temAspecto = false;
			
			for(Iterator j = this.obterInferiores().iterator() ; j.hasNext() ; )
			{
				Evento e = (Evento) j.next();
				
				if(e instanceof AspectosLegais)
				{
					temAspecto = true;
					break;
				}
			}
			
			if(temAspecto)
			{
				for(Iterator i = this.obterInferiores().iterator() ; i.hasNext() ; )
				{
					Evento e = (Evento) i.next();
					
					if(e instanceof Sinistro)
					{
						Sinistro sinistro = (Sinistro) e;
						
						if(sinistro.obterDataSinistro().compareTo(dataInicio)>=0 && sinistro.obterDataSinistro().compareTo(dataFim)<=0 && sinistro.obterSituacao().equals("Pagado"))
							valor+=sinistro.obterMontanteGs();
					}
				}
			}
		}
		else
		{
			boolean temAspecto = false;
			
			for(Iterator j = this.obterInferiores().iterator() ; j.hasNext() ; )
			{
				Evento e = (Evento) j.next();
				
				if(e instanceof AspectosLegais)
				{
					temAspecto = true;
					break;
				}
			}
			
			if(!temAspecto)
			{
				for(Iterator i = this.obterInferiores().iterator() ; i.hasNext() ; )
				{
					Evento e = (Evento) i.next();
					
					if(e instanceof Sinistro)
					{
						Sinistro sinistro = (Sinistro) e;
						
						if(sinistro.obterDataSinistro().compareTo(dataInicio)>=0 && sinistro.obterDataSinistro().compareTo(dataFim)<=0 && sinistro.obterSituacao().equals("Pagado"))
							valor+=sinistro.obterMontanteGs();
					}
				}
			}
		}
		
		return valor;
	}

	public double obterRecuperadosSinistro(String situacao, Date dataInicio, Date dataFim) throws Exception
	{
		double valor = 0;
		
		if(situacao.equals("Vigente Judicializado") || situacao.equals("No Vigente Judicializado"))
		{
			boolean temAspecto = false;
			
			for(Iterator j = this.obterInferiores().iterator() ; j.hasNext() ; )
			{
				Evento e = (Evento) j.next();
				
				if(e instanceof AspectosLegais)
				{
					temAspecto = true;
					break;
				}
			}
			if(temAspecto)
			{
				for(Iterator i = this.obterInferiores().iterator() ; i.hasNext() ; )
				{
					Evento e = (Evento) i.next();
					
					if(e instanceof Sinistro)
					{
						Sinistro sinistro = (Sinistro) e;
						
						if(sinistro.obterDataSinistro().compareTo(dataInicio)>=0 && sinistro.obterDataSinistro().compareTo(dataFim)<=0 && sinistro.obterSituacao().equals("Pagado"))
						{
							for(Iterator j = sinistro.obterInferiores().iterator() ; j.hasNext() ; )
							{
								Evento e2 = (Evento) j.next();
								
								if(e2 instanceof RegistroGastos)
								{
									RegistroGastos registroGastos = (RegistroGastos) e2;
									valor+=registroGastos.obterAbonoGs();
								}
								else if(e2 instanceof FaturaSinistro)
								{
									FaturaSinistro faturaSinistro = (FaturaSinistro) e2;
									valor+=faturaSinistro.obterMontantePago();
								}
							}
						}
					}
				}
			}
		}
		else
		{
			boolean temAspecto = false;
		
			for(Iterator j = this.obterInferiores().iterator() ; j.hasNext() ; )
			{
				Evento e = (Evento) j.next();
				
				if(e instanceof AspectosLegais)
				{
					temAspecto = true;
					break;
				}
			}
			
			if(!temAspecto)
			{
				for(Iterator i = this.obterInferiores().iterator() ; i.hasNext() ; )
				{
					Evento e = (Evento) i.next();
					
					if(e instanceof Sinistro)
					{
						Sinistro sinistro = (Sinistro) e;
						
						if(sinistro.obterDataSinistro().compareTo(dataInicio)>=0 && sinistro.obterDataSinistro().compareTo(dataFim)<=0 && sinistro.obterSituacao().equals("Pagado"))
						{
							for(Iterator j = sinistro.obterInferiores().iterator() ; j.hasNext() ; )
							{
								Evento e2 = (Evento) j.next();
								
								if(e2 instanceof RegistroGastos)
								{
									RegistroGastos registroGastos = (RegistroGastos) e2;
									valor+=registroGastos.obterAbonoGs();
								}
								else if(e2 instanceof FaturaSinistro)
								{
									FaturaSinistro faturaSinistro = (FaturaSinistro) e2;
									valor+=faturaSinistro.obterMontantePago();
								}
							}
						}
					}
				}
			}
		}
		
		return valor;
	}
	
	public int obterQtdeReaseguros(double valor) throws Exception
	{
		String sql = "select count(*) as qtde from evento,dados_reaseguro where evento.id = dados_reaseguro.id and superior = " + this.obterId();
		if(valor > 0)
			sql+=" and caiptal_gs>="+valor;
		
		//System.out.println(sql);
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm",sql);
		
		return query.executeAndGetFirstRow().getInt("qtde");
	}
	
	public String obterValoresBuscaInstrumento() throws Exception
	{
		double totalCapital = 0;
		double totalPrima = 0;
		double totalComissao = 0;
		
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id,caiptal_gs,prima_gs,comissao_gs from evento,dados_reaseguro where evento.id = dados_reaseguro.id and superior = ? and dados_reaseguro.situacao = 'Vigente'");
		query.addLong(this.obterId());
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			double capital = rows[i].getDouble("caiptal_gs");
			double prima = rows[i].getDouble("prima_gs");
			double comissao = rows[i].getDouble("comissao_gs");
			
			DadosReaseguro dado = (DadosReaseguro) home.obterEventoPorId(id);
			
			Date dataInicioR = dado.obterDataPrevistaInicio();
			Date dataFimR = dado.obterDataPrevistaConclusao();
			Date dataInicioA = this.obterDataPrevistaInicio();
			Date dataFimA = this.obterDataPrevistaConclusao();
			
			if(dataInicioR.compareTo(dataInicioA)>=0 && dataFimR.compareTo(dataFimA)<=0)
			{
				/*String[] valoresSujos = dado.obterValoresParciaisAnulacao().split(";");
				double totalParcialCapital = Double.valueOf(valoresSujos[0]);
				double totalParcialPrima = Double.valueOf(valoresSujos[1]);
				double totalParcialComissao = Double.valueOf(valoresSujos[2]);
				
				totalCapital+=capital - totalParcialCapital;
				totalPrima+= prima - totalParcialPrima;
				totalComissao+=comissao - totalParcialComissao;*/
			}
		}
		
		return totalCapital+";"+totalPrima+";"+totalComissao;
	}
	
	public boolean realmenteVigente() throws Exception
	{
		boolean vigente = false;
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select top 1 data_prevista_inicio,data_prevista_conclusao from evento,dados_reaseguro where evento.id = dados_reaseguro.id and superior = ? and dados_reaseguro.situacao = 'Vigente' order by data_prevista_inicio desc");
		//SQLQuery query = this.getModelManager().createSQLQuery("crm","select top 1 data_prevista_inicio,data_prevista_conclusao from evento,dados_reaseguro where evento.id = dados_reaseguro.id and superior = ? order by data_prevista_inicio desc");
		query.addLong(this.obterId());
		
		if(query.execute().length > 0)
		{
			Date dataInicioR = new Date(query.executeAndGetFirstRow().getLong("data_prevista_inicio"));
			Date dataFimR = new Date(query.executeAndGetFirstRow().getLong("data_prevista_conclusao"));
			Date dataInicioA = this.obterDataPrevistaInicio();
			Date dataFimA = this.obterDataPrevistaConclusao();
			
			if(dataInicioR.compareTo(dataInicioA)>=0 && dataFimR.compareTo(dataFimA)<=0)
				vigente = true;
		}
		
		return vigente;
	}
	
	public Map<Double, Suplemento> obterSuplementos() throws Exception
	{
		Map<Double, Suplemento> suplementos = new TreeMap<Double, Suplemento>();
		EventoHome home = (EventoHome)getModelManager().getHome("EventoHome");
		
		/*SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id,suplemento.numero_endoso from evento,suplemento where evento.id = suplemento.id and superior = ?");
		query.addLong(this.obterId());*/
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","EXEC obterSuplementos "+this.obterId());
		
		SQLRow[] rows = query.execute();
		long id;
		double numeroSuplemento;
		Suplemento s;
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			id = rows[i].getLong("id");
			numeroSuplemento = rows[i].getDouble("numero_endoso");
			
			s = (Suplemento) home.obterEventoPorId(id);
			
			suplementos.put(new Double(numeroSuplemento), s);
		}
		
		return suplementos;
	}
	
	private Collection<EntidadeBCP> asegurados;
    private Collection<EntidadeBCP> tomadores;
	
	public void setAsegurados(Collection<EntidadeBCP> asegurados)
	{
		this.asegurados = asegurados;
	}

	public Collection<EntidadeBCP> getAsegurados()
	{
		return this.asegurados;
	}

	public void setTomadores(Collection<EntidadeBCP> tomadores)
	{
		this.tomadores = tomadores;
	}

	public Collection<EntidadeBCP> getTomadores()
	{
		return this.tomadores;
	}
	
	public void atualizarNumeroEndoso(double numeroEndoso) throws Exception
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm", "EXEC atualizarNumeroEndoso ?,?");
		update.addDouble(numeroEndoso);
		update.addLong(this.obterId());
		update.execute();
	}
	 
	public void atualizarCertificado(double certificado) throws Exception
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm", "EXEC atualizarCertificado ?,?");
		update.addDouble(certificado);
		update.addLong(this.obterId());
		update.execute();
	}
	
	public void atualizarAseguradosETomadores() throws Exception
	{
		try
		{
			SQLUpdate insert;
			boolean formatoNovo = false;
			
			if(this.getAsegurados()!=null)
			{
				formatoNovo = true;
				
				for(EntidadeBCP entidadeBcp : this.getAsegurados())
				{
					insert = this.getModelManager().createSQLUpdate("crm", "EXEC incluirAsegurado ?,?,?,?,?,?,?,?");
					insert.addLong(this.obterId());
					insert.addString(entidadeBcp.getNome());
					insert.addString(entidadeBcp.getSobreNome());
		     		insert.addString(entidadeBcp.getTipoPessoa());
		     		insert.addString(entidadeBcp.getTipoDocumento());
		     		insert.addString(entidadeBcp.getNumeroDoc());
		     		if(entidadeBcp.getDataNascimento()!=null)
		     			insert.addLong(entidadeBcp.getDataNascimento().getTime());
		     		else
		     			insert.addLong(0);
		     		insert.addString(entidadeBcp.getPais());
		     		
		     		insert.execute();
				}
			}
	     
			if(this.getTomadores()!=null)
			{
				formatoNovo = true;
				
				for(EntidadeBCP entidadeBcp : this.getTomadores())
		     	{
					insert = this.getModelManager().createSQLUpdate("crm", "EXEC incluirTomador ?,?,?,?,?,?,?,?");
		     		insert.addLong(this.obterId());
		     		insert.addString(entidadeBcp.getNome());
		     		insert.addString(entidadeBcp.getSobreNome());
		     		insert.addString(entidadeBcp.getTipoPessoa());
		     		insert.addString(entidadeBcp.getTipoDocumento());
		     		insert.addString(entidadeBcp.getNumeroDoc());
		     		if(entidadeBcp.getDataNascimento()!=null)
		     			insert.addLong(entidadeBcp.getDataNascimento().getTime());
		     		else
		     			insert.addLong(0);
		     		insert.addString(entidadeBcp.getPais());
		     		
		     		insert.execute();
		     	}
			}
			
			if(!formatoNovo)
			{
				if(this.obterNomeAsegurado()!=null)
				{
					if(this.obterNomeAsegurado().length() > 0)
						this.atualizarNomeAsegurado(this.obterNomeAsegurado());
				}
				if(this.obterTipoPessoa()!=null)
				{
					if(this.obterTipoPessoa().length() > 0)
						this.atualizarTipoPessoa(this.obterTipoPessoa());
				}
				if(this.obterTipoIdentificacao()!=null)
				{
					if(this.obterTipoIdentificacao().length() > 0)
						this.atualizarTipoIdentificacao(this.obterTipoIdentificacao());
				}
				if(this.obterNumeroIdentificacao()!=null)
				{
					if(this.obterNumeroIdentificacao().length() > 0)
						this.atualizarNumeroIdentificacao(this.obterNumeroIdentificacao());
				}
				
				if(this.obterDataNascimento()!=null)
					this.atualizarDataNascimento(this.obterDataNascimento());
				
				if(this.obterNomeTomador()!=null)
				{
					if(this.obterNomeTomador().length() > 0)
						this.atualizarNomeTomador(this.obterNomeTomador());
				}
				
				/*System.out.println("Apolice: " + this.obterNumeroApolice());
				System.out.println("NomeAsegurado: " + this.obterNomeAsegurado());
				System.out.println("TipoPessoa: " + this.obterTipoPessoa());
				System.out.println("TipoIdentificacao: " + this.obterTipoIdentificacao());
				System.out.println("NumeroIdentificacao: " + this.obterNumeroIdentificacao());
				System.out.println("DataNascimento: " + this.obterDataNascimento().getTime());
				System.out.println("NomeTomador: " + this.obterNomeTomador());*/
			}
		}
		catch(Exception e)
		{
			System.out.println("Erro no metodo atualizarAseguradosETomadores");
			e.printStackTrace();
		}
	}
}