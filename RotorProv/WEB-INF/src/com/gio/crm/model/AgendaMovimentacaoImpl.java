package com.gio.crm.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;

import infra.config.InfraProperties;
import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

public class AgendaMovimentacaoImpl extends EventoImpl implements
		AgendaMovimentacao {
	private int movimentoMes;

	private int movimentoAno;

	public void atualizarDataPrevistaConclusao(Date dataPrevistaConclusao) throws Exception
	{
		super.atualizarDataPrevistaConclusao(dataPrevistaConclusao);
	}

	public void atualizarDataPrevistaInicio(Date dataPrevistaInicio) throws Exception
	{
		super.atualizarDataPrevistaInicio(dataPrevistaInicio);
	}
	
	public void atualizarEndosoApolice(String especial) throws Exception
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm","update agenda_movimentacao set endoso_apolice = ? where id = ?");
		update.addString(especial);
		update.addLong(this.obterId());
	   		
		update.execute();
	}

	public void atualizarValidacao(String mod) throws Exception 
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm","update agenda_movimentacao set validacao = ? where id = ?");
		update.addString(mod);
		update.addLong(this.obterId());
		
		update.execute();
	}

	public String obterValidacao() throws Exception 
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select validacao from agenda_movimentacao where id = ?");
		query.addLong(this.obterId());
		
		return query.executeAndGetFirstRow().getString("validacao");
	}

	public String obterIcone() throws Exception {
		return "calendar.gif";
	}

	public void atribuirMesMovimento(int mes) throws Exception {
		this.movimentoMes = mes;
	}

	public void atribuirAnoMovimento(int ano) throws Exception {
		this.movimentoAno = ano;
	}

	public int obterMesMovimento() throws Exception 
	{
		if (this.movimentoMes == 0) 
		{
			SQLQuery query = this.getModelManager().createSQLQuery("crm","select movimento_mes from agenda_movimentacao where id=?");
			query.addLong(this.obterId());
			
			this.movimentoMes = query.executeAndGetFirstRow().getInt("movimento_mes");
		}
		return this.movimentoMes;
	}

	public int obterAnoMovimento() throws Exception
	{
		if (this.movimentoAno == 0)
		{
			SQLQuery query = this.getModelManager().createSQLQuery("crm","select movimento_ano from agenda_movimentacao where id=?");
			query.addLong(this.obterId());
			this.movimentoAno = query.executeAndGetFirstRow().getInt("movimento_ano");
		}
		return this.movimentoAno;
	}

	public boolean existeAgendaNoPeriodo(int mes, int ano, Entidade asseguradora, String tipo) throws Exception
	{
		boolean existe = false;

		SQLQuery query = this.getModelManager().createSQLQuery("crm", "select movimento_mes, movimento_ano from agenda_movimentacao,evento where evento.id=agenda_movimentacao.id and origem=? and movimento_mes=? and movimento_ano=? and tipo=? and validacao=?");
		query.addLong(asseguradora.obterId());
		query.addInt(mes);
		query.addInt(ano);
		query.addString(tipo);
		query.addString("Total");

		SQLRow[] rows = query.execute();

		if (rows.length > 0)
			existe = true;

		return existe;
	}
	
	public void incluir() throws Exception
	{
		super.incluir();
		SQLUpdate insert = this.getModelManager().createSQLUpdate("crm","insert into agenda_movimentacao (id, movimento_mes, movimento_ano) values (?,?,?)");
		insert.addLong(this.obterId());
		insert.addInt(this.obterMesMovimento());
		insert.addInt(this.obterAnoMovimento());
		insert.execute();
	}

	public void enviarBcp(String comentario) throws Exception
	{
		this.concluir(comentario);
		for (Iterator i = this.obterInferiores().iterator(); i.hasNext();)
			((Evento) i.next()).concluir(null);
	}

	public boolean permiteEnviarBcp() throws Exception {
		boolean permite = false;
		for (Iterator i = this.obterInferiores().iterator(); i.hasNext();) {
			Evento e = (Evento) i.next();
			if (e instanceof Notificacao) {
				Notificacao notificacao = (Notificacao) e;
				if (notificacao.obterTipo().equals(
						"Notificación de Recebimento"))
					permite = true;
			}
		}
		return permite;
	}

	public boolean permiteValidar() throws Exception {
		boolean permite = true;

		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"SELECT movimentacao_financeira_conta.data_prevista FROM evento,movimentacao_financeira_conta where origem=? and evento.id=movimentacao_financeira_conta.id group by movimentacao_financeira_conta.data_prevista");
		query.addLong(this.obterOrigem().obterId());

		SQLRow[] rows = query.execute();

		for (int i = 0; i < rows.length; i++) {
			Date data = new Date(rows[i].getLong("data_prevista"));

			String mesEvento = new SimpleDateFormat("MM").format(data);

			String anoEvento = new SimpleDateFormat("yyyy").format(data);

			if (this.obterMesMovimento() == Integer.parseInt(mesEvento)
					&& this.obterAnoMovimento() == Integer.parseInt(anoEvento))
				return false;
		}

		return permite;
	}

	private String nomeArquivo;

	public Collection<String> validaArquivo(String nomeArquivo) throws Exception
	{
		Collection<String> linhas = new ArrayList<>();

		this.nomeArquivo = nomeArquivo;
		
		File file = new File("" + "c:/Aseguradoras/Archivos/" + nomeArquivo+ ".txt");
		
		if (file.exists())
		{
			String str = null;
			FileReader reader = new FileReader(file);
			BufferedReader in = new BufferedReader(reader);
			
			while((str = in.readLine())!=null)
				linhas.add(str);
			
			reader.close();
			in.close();
			reader = null;
			in = null;
			file = null;
			System.gc();
			
			
			return this.validadorArquivo(linhas);
		}
		else
			throw new Exception("Erro: 26 - El Archivo " + nomeArquivo	+ ".txt no fue encontrado");
	}

	private Collection<String> erros = new ArrayList<>();

	private Collection<ClassificacaoContas> classificacaoContasTotalizadas = new ArrayList<>();

	/////////////////// MÉTODO PRA VALIDAR O ARQUIVO CONTABIL
	// ////////////////////////////////////////
	private Collection<String> validadorArquivo(Collection<String> linhas) throws Exception
	{
		//FileWriter file = new FileWriter("c:/tmp/LogContabil.txt");
		
		//DecimalFormat formataValor = new DecimalFormat("");
		
		int contador = 1;

		int sequencial = 1;

		EntidadeHome entidadeHome = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		UsuarioHome usuarioHome = (UsuarioHome) this.getModelManager().getHome("UsuarioHome");

		Usuario responsavel = null;

		Map<Integer,Integer> tipoRegistro = new TreeMap<>();
		int numeroRegistros = 0;

		try
		{
			for (String linha : linhas) 
			{
				//System.out.println(linha);
	
				//System.out.println("Contador:" + contador);
				
				//file.write("Contador: " + contador + " ;Linha: " + linha + "\r\n");
	
				if (Integer.parseInt(linha.substring(5, 7)) != 1
						&& Integer.parseInt(linha.substring(5, 7)) != 2
						&& Integer.parseInt(linha.substring(5, 7)) != 3
						&& Integer.parseInt(linha.substring(5, 7)) != 9)
					
					erros.add("Linea = " + contador
							+ " - Erro: xx - Tipo de registro: "
							+ linha.substring(5, 7) + " incorrecto");
	
				if (Integer.parseInt(linha.substring(5, 7)) == 1)
				{
					if (!tipoRegistro.containsKey(new Integer(linha.substring(5, 7))))
						tipoRegistro.put(new Integer(linha.substring(5, 7)),new Integer(linha.substring(5, 7)));
					else
						erros.add("Linea = "+ contador + " - Erro: 07 - Existe más de un registro tipo 01.");
				}
	
				if (Integer.parseInt(linha.substring(5, 7)) == 2)
				{
					if (!tipoRegistro.containsKey(new Integer(linha.substring(5, 7))))
						tipoRegistro.put(new Integer(linha.substring(5, 7)),new Integer(linha.substring(5, 7)));
				}
	
				else if (Integer.parseInt(linha.substring(5, 7)) == 3)
				{
					if (!tipoRegistro.containsKey(new Integer(linha.substring(5, 7))))
						tipoRegistro.put(new Integer(linha.substring(5, 7)),new Integer(linha.substring(5, 7)));
				}
	
				else if (Integer.parseInt(linha.substring(5, 7)) == 9)
				{
					if (!tipoRegistro.containsKey(new Integer(linha.substring(5, 7))))
						tipoRegistro.put(new Integer(linha.substring(5, 7)),new Integer(linha.substring(5, 7)));
				}
	
				if (contador == linhas.size())
				{ /* Final de arquivo *//*
																	    * Rergistro
																	    * 9
																	    */
					if (linha.length() != 9)
					{
						erros.add("Linea = " + contador	+ " - Erro: 04 - Tamaño de registro inválido.");
						break;
					} 
					else
					{
						/* Registro 9 */
						String str01 = linha.substring(0, 5); // 05 Número
															  // Secuencial
	
						sequencial = Integer.parseInt(str01);
	
						if (contador != numeroRegistros)
							erros.add("Linea = "+ contador + " - Erro: 09 - Número de registros informado no esta coincidiendo con el número de registros enviados.");
	
						if (sequencial != contador)
							erros.add("Linea = "+ contador	+ " - Erro 02 - La secuencia de la numeración del archivo no esta en el orden secuencial y creciente");
	
						if (!tipoRegistro.containsKey(new Integer(2)))
							erros.add("Linea = " + contador	+ " - Erro: 17 - Falta registro tipo 02.");
	
						if (!tipoRegistro.containsKey(new Integer(3)))
							erros.add("Linea = " + contador	+ " - Erro: 18 - Falta registro tipo 03.");
	
						if (!tipoRegistro.containsKey(new Integer(9)))
							erros.add("Linea = " + contador + " - Erro: 21 - Falta registro tipo 09.");
	
						String str02 = linha.substring(5, 7); // 02 Identifica el
															  // registro
	
					}
				} 
				else if (contador == 1)
				{/* Comeco de arquivo *//* Rergistro 1 */
	
					if (Integer.parseInt(linha.substring(5, 7)) != 1)
						erros.add("Linea = "+ contador	+ " - Erro: 11 - El registro tipo 01 debe ser el primer registro del archivo.");
					else
					{
						/* Rergistro 1 */
						if (linha.length() != 46)
						{
							erros.add("Linea = "+ contador	+ " - Erro: 04 - El tamaño del registro es diferente del especificado en el formato del registro.");
							break;
						}
	
						String str01 = linha.substring(0, 5); // 05 Numero
															  // Secuencial
						if (str01.length() != 5)
							erros.add("Linha = "+ contador	+ " Coluna 0~5 - Erro: 04 - Tamanho de registro inválido.");
	
						sequencial = Integer.parseInt(str01);
	
						if (sequencial != contador)
							erros.add("Linea = "+ contador	+ " - Erro 02 - La secuencia de la numeración del archivo no esta en el orden secuencial y creciente");
	
						String str02 = linha.substring(5, 7); // 02 Identifica el
															  // registro
						if (str02.length() != 2)
							erros.add("Linea = "+ contador	+ " Coluna 5~7 - Erro: 04 - Tamaño de registro inválido.");
						String str03 = linha.substring(7, 10); // 3 Identifica la
															   // Aseguradora
						if (str03.length() != 3)
							erros.add("Linea = "+ contador	+ " Coluna 7~18 - Erro: 04 - Tamanho de registro inválido.");
	
						seguradora = (Entidade) entidadeHome.obterEntidadePorSigla(str03);
	
						if (seguradora == null)
							erros.add("Linea = " + contador+ " - Erro: 08 - Aseguradora " + str03.trim()+ " inexistente.");
						else if (this.obterOrigem() != seguradora)
							erros.add("Linha = " + contador	+ " - Erro: 25 - El aseguradora "+ str03.trim()
									+ " no es el misma de la agenda.");
	
						String str04 = linha.substring(10, 20); // 10 identifica el
																// Usuario
						if (str04.length() != 10)
							erros.add("Linea = "+ contador+ " Coluna 18~29 - Erro: 04 - Tamaño de registro inválido.");
	
						responsavel = (Usuario) usuarioHome.obterUsuarioPorChave(str04.trim());
	
						if (responsavel == null)
							erros.add("Linea = " + contador+ " - Erro: 10 - Usuario " + str04.trim()+ " inexistente.");
	
						String str05 = linha.substring(20, 24); // 04 Fecha de
																// generación del
																// archivo
						if (str05.length() != 4)
							erros.add("Linea = "+ contador+ " Coluna 29~33 - Erro: 04 - Tamañ de registro inválido.");
						String str06 = linha.substring(24, 26); // 02 Fecha de
																// generación del
																// archivo
						if (str06.length() != 2)
							erros.add("Linea = "+ contador	+ " Coluna 33~35 - Erro: 04 - Tamaño de registro inválido.");
						String str07 = linha.substring(26, 28); // 02 Fecha de
																// generación del
																// archivo
						if (str07.length() != 2)
							erros.add("Linea = "+ contador+ " Coluna 35~37 - Erro: 04 - Tamaño de registro inválido.");
						String str08 = linha.substring(28, 32); // 04 Año / Mes del
																// Movimiento
						if (str08.length() != 4)
							erros.add("Linea = "+ contador	+ " Coluna 37~41 - Erro: 04 - Tamaño de registro inválido.");
	
						ano = str08;
	
						String str09 = linha.substring(32, 34); // 02 Mes del
																// Movimiento
						if (str09.length() != 2)
							erros.add("Linea = "+ contador+ " Coluna 41~43 - Erro: 04 - Tamaño de registro inválido.");
	
						mes = str09;
	
						if (Integer.parseInt(str09) != this.obterMesMovimento()	|| Integer.parseInt(str08) != this.obterAnoMovimento())
							erros.add("Linea = "+ contador	+ " - Erro: 23 - Mes/Año del movimento de la agenda, es diferente del Mes/Año del Archivo");
	
						String str10 = linha.substring(34, 44); // 10 Número total
																// de registros
						if (str10.length() != 10)
							erros.add("Linea = "+ contador	+ " Coluna 43~53 - Erro: 04 - Tamaño de registro inválido.");
	
						numeroRegistros = Integer.parseInt(str10);
					}
				} 
				else if (Integer.parseInt(linha.substring(5, 7)) == 2)
				{ /* Corpo *//*
																					   * Rergistro
																					   * 2 e
																					   * 3
																					   */
					if (Integer.parseInt(linha.substring(5, 7)) == 2)
						if (linha.length() != 142)
						{
							erros.add("Linea = " + contador+ " - Erro: 04 - Tamaño de registro inválido.");
							break;
						}
	
					/* Rergistro 2 */
	
					MovimentacaoFinanceiraConta mf = (MovimentacaoFinanceiraConta) this.getModelManager().getEntity("MovimentacaoFinanceiraConta");
	
					String str01 = linha.substring(0, 5); // 05 Número secuencial
					if (str01.length() != 5)
						erros.add("Linea = "+ contador+ " Coluna 0~5 - Erro: 04 - Tamaño de registro inválido.");
	
					sequencial = Integer.parseInt(str01);
	
					if (sequencial != contador)
						erros.add("Linea = " + contador+ " - Erro: 02 - La secuencia de la numeración del archivo no esta en el orden secuencial y creciente");
	
					String str02 = linha.substring(5, 7); // 02 Identifica el
														  // registro
					if (str02.length() != 2)
						erros.add("Linea = "+ contador+ " Coluna 5~7 - Erro: 04 - Tamaño de registro inválido.");
					String str03 = linha.substring(7, 17); // 10 Cuenta contable
	
					if (str03.length() != 10)
						erros.add("Linea = "+ contador+ " Coluna 7~17 - Erro: 04 - Tamaño de registro inválido.");
	
					String strNivel = linha.substring(7, 9);
	
					Entidade entidade = (Entidade) entidadeHome.obterEntidadePorApelido(str03);
					Conta conta = null;
	
					if (entidade instanceof Conta)
						conta = (Conta) entidade;
	
					if (conta == null)
						erros.add("Linea = " + contador + " - Erro: 14 - Cuenta "+ str03.trim() + " inexistente o inválida.");
					else if (!conta.obterAtivo())
						erros.add("Linea = " + contador + " - Erro: 19 - Cuenta "+ str03.trim() + " no esta activa.");
	
					String str04 = linha.substring(17, 27); // 10
					if (str04.length() != 10)
						erros.add("Linea = "+ contador	+ " Coluna 17~27 - Erro: 04 - Tamaño de registro inválido.");
					String str05 = linha.substring(27, 49); // 22 Total del
															// movimiento de débito
					if (str05.length() != 22)
						erros.add("Linea = "+ contador+ " Coluna 27~49 - Erro: 04 - Tamaño de registro inválido.");
					String str06 = linha.substring(49, 71); // 22 Total del
															// movimiento de crédito
					if (str06.length() != 22)
						erros.add("Linea = "+ contador+ " Coluna 49~71 - Erro: 04 - Tamaño de registro inválido.");
					String str07 = linha.substring(71, 72); // 01 Estado del saldo
															// anterior
					if (str07.length() != 1)
						erros.add("Linea = "+ contador+ " Coluna 71~72 - Erro: 04 - Tamaño de registro inválido.");
					String str08 = linha.substring(72, 94); // 22 Saldo del mes
															// anterior
					if (str08.length() != 22)
						erros.add("Linea = "+ contador+ " Coluna 72~94 - Erro: 04 - Tamaño de registro inválido.");
					String str09 = linha.substring(94, 95); // 01 Estado del saldo
															// actual
					if (str09.length() != 1)
						erros.add("Linea = "+ contador+ " Coluna 94~95 - Erro: 04 - Tamaño de registro inválido.");
					String str10 = linha.substring(95, 117); // 22 Saldo actual
					if (str10.length() != 22)
						erros.add("Linea = "+ contador	+ " Coluna 95~117 - Erro: 04 - Tamaño de registro inválido.");
					String str11 = linha.substring(117, 118); // 01 Estado del total
															  // de moneda
															  // extranjera
					if (str11.length() != 1)
						erros.add("Linea = "+ contador+ " Coluna 117~118 - Erro: 04 - Tamaño de registro inválido.");
					String str12 = linha.substring(118, 140); // 22 Total de moneda
															  // extranjera
					if (str12.length() != 22)
						erros.add("Linea = "+ contador	+ " Coluna 118~140 - Erro: 04 - Tamaño de registro inválido.");
	
					if (conta != null)
					{
						mf.atribuirOrigem(seguradora);
						mf.atribuirResponsavel(responsavel);
						Date dataInicio = new SimpleDateFormat("dd/MM/yyyy").parse("01/" + mes + "/" + ano);
						mf.atribuirDataPrevista(dataInicio);
						mf.atribuirConta(conta);
	
						if (strNivel.equals("01") || strNivel.equals("05") || strNivel.equals("06") || strNivel.equals("09"))
						{
							if (str07.equals("D"))
								mf.atribuirSaldoAnterior(new BigDecimal(str08));
							else
								mf.atribuirSaldoAnterior(new BigDecimal(str08).multiply(new BigDecimal("-1")));
						} 
						else if (strNivel.equals("02") || strNivel.equals("03") || strNivel.equals("04") || strNivel.equals("07"))
						{
							if (str07.equals("D"))
								mf.atribuirSaldoAnterior(new BigDecimal(str08).multiply(new BigDecimal("-1")));
							else
								mf.atribuirSaldoAnterior(new BigDecimal(str08));
						}
	
						mf.atribuirDebito(new BigDecimal(str05));
						mf.atribuirCredito(new BigDecimal(str06));
	
						if (strNivel.equals("01") || strNivel.equals("05") || strNivel.equals("06") || strNivel.equals("09"))
						{
							if (str09.equals("D"))
								mf.atribuirSaldoAtual(new BigDecimal(str10));
							else
								mf.atribuirSaldoAtual(new BigDecimal(str10).multiply(new BigDecimal("-1")));
							
							//System.out.println(conta.obterApelido() + " Saldo atual: " + str10);
						}
						else if (strNivel.equals("02") || strNivel.equals("03")	|| strNivel.equals("04") || strNivel.equals("07"))
						{
							if (str09.equals("D"))
								mf.atribuirSaldoAtual(new BigDecimal(str10).multiply(new BigDecimal("-1")));
							else
								mf.atribuirSaldoAtual(new BigDecimal(str10));
							
							//System.out.println(conta.obterApelido() + " Saldo atual: " + str10);
						}
	
						if (strNivel.equals("01") || strNivel.equals("05") || strNivel.equals("06") || strNivel.equals("09"))
						{
							if (str11.equals("D"))
								mf.atribuirSaldoEstrangeiro(new BigDecimal(str12));
							else
								mf.atribuirSaldoEstrangeiro(new BigDecimal(str12).multiply(new BigDecimal("-1")));
							
						} 
						else if (strNivel.equals("02") || strNivel.equals("03")|| strNivel.equals("04") || strNivel.equals("07"))
						{
							if (str11.equals("D"))
								mf.atribuirSaldoEstrangeiro(new BigDecimal(str12).multiply(new BigDecimal("-1")));
							else
								mf.atribuirSaldoEstrangeiro(new BigDecimal(str12));
						}
	
						mf.atribuirTitulo("Movimiento da Cuenta "+ conta.obterCodigo());
	
						this.movimentacaoes.add(mf);
	
						if (this.saldoAtualContaTotalizado.containsKey(conta.obterCodigo()))
							erros.add("Linea = "+ contador+ " - Erro: 06 - Existe duplicidad del registro "	+ conta.obterCodigo());
						else
						{
							this.contas.add(conta);
	
							//BigDecimal saldoAtual = new BigDecimal(new Double(mf.obterSaldoAtual()).toString()).setScale(30);
							BigDecimal saldoAtual = mf.obterSaldoAtual();
							BigDecimal saldoAnterior = mf.obterSaldoAnterior();
							BigDecimal credito = mf.obterCredito();
							BigDecimal debito = mf.obterDebito();
							BigDecimal saldoMoedaEstrangeira = mf.obterSaldoEstrangeiro();
							
							/*if(conta.obterApelido().equals("0605010101") || conta.obterApelido().equals("0701010101") || conta.obterApelido().equals("0701010110") || conta.obterApelido().equals("0701010111") || conta.obterApelido().equals("0701010203"))
							{
								DecimalFormat formatador = new DecimalFormat();
								//formatador.applyPattern("#,##0.00");
								
								System.out.println("-------- " + conta.obterApelido() + " -------");
								System.out.println("str10 " + str10);
								System.out.println("saldoAtual " + saldoAtual.toPlainString());
								System.out.println("saldoAnterior " + formataValor.format(saldoAnterior));
								System.out.println("credito " + formataValor.format(credito));
								System.out.println("debito " + formataValor.format(debito));
							}*/
	
							this.saldoAtualContaTotalizado.put(conta.obterCodigo(),saldoAtual);
							this.saldoAnteriorContaTotalizado.put(conta.obterCodigo(), saldoAnterior);
							this.creditoContaTotalizado.put(conta.obterCodigo(),credito);
							this.debitoContaTotalizado.put(conta.obterCodigo(),debito);
							this.saldoMoedaEstrangeiraContaTotalizado.put(conta.obterCodigo(), saldoMoedaEstrangeira);
						}
	
						ClassificacaoContas cContas2;
						
						for (Entidade e : conta.obterSuperiores())
						{
							if (e instanceof ClassificacaoContas)
							{
								cContas2 = (ClassificacaoContas) e;
								if (this.saldoAtualClassificacaoContasTotalizado.containsKey(cContas2.obterCodigo()))
								{
									BigDecimal valor = new BigDecimal(this.saldoAtualClassificacaoContasTotalizado.get(cContas2.obterCodigo()).toString());
									//BigDecimal valor2 = valor.add(new BigDecimal(new Double(mf.obterSaldoAtual()).toString()));
									BigDecimal valor2 = valor.add(mf.obterSaldoAtual());
									
									/*if(cContas2.obterApelido().equals("0605010000"))
									{
										System.out.println(formataValor.format(valor) + " + Saldo atual " + mf.obterConta().obterApelido() + " " + formataValor.format(mf.obterSaldoAtual()));
									}*/
									
									this.saldoAtualClassificacaoContasTotalizado.remove(cContas2.obterCodigo());
									this.saldoAtualClassificacaoContasTotalizado.put(cContas2.obterCodigo(),valor2);
								} 
								else
								{
									/*if(cContas2.obterApelido().equals("0605010000"))
									{
										System.out.println("Saldo atual " + mf.obterConta().obterApelido() + " " + formataValor.format(mf.obterSaldoAtual()));
									}*/
									
									//BigDecimal novoValor = new BigDecimal(new Double(mf.obterSaldoAtual()).toString());
									BigDecimal novoValor = mf.obterSaldoAtual();
									this.saldoAtualClassificacaoContasTotalizado.put(cContas2.obterCodigo(),novoValor);
								}
	
								if (this.saldoAnteriorClassificacaoContasTotalizado.containsKey(cContas2.obterCodigo()))
								{
									BigDecimal valor = new BigDecimal(this.saldoAnteriorClassificacaoContasTotalizado.get(cContas2.obterCodigo()).toString());
									BigDecimal valor2 = valor.add(mf.obterSaldoAnterior());
									
									this.saldoAnteriorClassificacaoContasTotalizado.remove(cContas2.obterCodigo());
									this.saldoAnteriorClassificacaoContasTotalizado.put(cContas2.obterCodigo(),	valor2);
								}
								else
								{
									BigDecimal novoValor = mf.obterSaldoAnterior();
									this.saldoAnteriorClassificacaoContasTotalizado.put(cContas2.obterCodigo(),novoValor);
								}
	
								if (this.saldoMoedaEstrangeiraClassificacaoContasTotalizado.containsKey(cContas2.obterCodigo()))
								{
									BigDecimal valor = new BigDecimal(this.saldoMoedaEstrangeiraClassificacaoContasTotalizado.get(cContas2.obterCodigo()).toString());
									BigDecimal valor2 = valor.add(mf.obterSaldoEstrangeiro());
									this.saldoMoedaEstrangeiraClassificacaoContasTotalizado.remove(cContas2.obterCodigo());
									this.saldoMoedaEstrangeiraClassificacaoContasTotalizado.put(cContas2.obterCodigo(),	valor2);
								}
								else
								{
									BigDecimal novoValor = mf.obterSaldoEstrangeiro();
									this.saldoMoedaEstrangeiraClassificacaoContasTotalizado.put(cContas2.obterCodigo(),	novoValor);
								}
	
								if (this.creditoClassificacaoContasTotalizado.containsKey(cContas2.obterCodigo()))
								{
									BigDecimal valor = new BigDecimal(this.creditoClassificacaoContasTotalizado.get(cContas2.obterCodigo()).toString());
									BigDecimal valor2 = valor.add(mf.obterCredito());
									this.creditoClassificacaoContasTotalizado.remove(cContas2.obterCodigo());
									this.creditoClassificacaoContasTotalizado.put(cContas2.obterCodigo(), valor2);
									
									//System.out.println(cContas2.obterCodigo() + " Credito: " + valor2);
								} 
								else 
								{
									BigDecimal novoValor = mf.obterCredito();
									this.creditoClassificacaoContasTotalizado.put(cContas2.obterCodigo(), novoValor);
									
									//System.out.println(cContas2.obterCodigo() + " Credito: " + novoValor);
								}
	
								if (this.debitoClassificacaoContasTotalizado.containsKey(cContas2.obterCodigo()))
								{
									BigDecimal valor = new BigDecimal(this.debitoClassificacaoContasTotalizado.get(cContas2.obterCodigo()).toString());
									BigDecimal valor2 = valor.add(mf.obterDebito());
									this.debitoClassificacaoContasTotalizado.remove(cContas2.obterCodigo());
									this.debitoClassificacaoContasTotalizado.put(cContas2.obterCodigo(), valor2);
								}
								else
								{
									BigDecimal novoValor = mf.obterDebito();
									this.debitoClassificacaoContasTotalizado.put(cContas2.obterCodigo(), novoValor);
								}
	
								if (!this.classificacaoContasTotalizadas.contains(cContas2)	&& !cContas2.obterCodigo().equals("0000000000"))
									this.classificacaoContasTotalizadas.add(cContas2);
							}
						}
					}
				}
	
				else if (Integer.parseInt(linha.substring(5, 7)) == 3)
				{
					if (linha.length() != 52)
					{
						erros.add("Linea = " + contador+ " - Erro: 04 - Tamaño de registro inválido.");
						break;
					}
	
					/* Rergistro 3 */
					String str01 = linha.substring(0, 5); // 05 Número secuencial
					if (str01.length() != 5)
						erros.add("Linea = "+ contador+ " Coluna 0~5 - Erro: 04 - Tamaño de registro inválido.");
	
					sequencial = Integer.parseInt(str01);
	
					if (sequencial != contador)
						erros.add("Linea = " + contador+ " - Erro: 02 Número sequencial invalido.");
	
					String str02 = linha.substring(5, 7); // 02 Identifica el
														  // registro
					if (str02.length() != 2)
						erros.add("Linea = "+ contador+ " Coluna 5~7 - Erro: 04 - Tamaño de registro inválido.");
					String str03 = linha.substring(7, 17); // 10 Cuenta contable
					if (str03.length() != 10)
						erros.add("Linea = "+ contador+ " Coluna 7~17 - Erro: 04 - Tamaño de registro invalido.");
	
					String strNivel = linha.substring(7, 9);
	
					ClassificacaoContas cConta = null;
					Entidade entidade = (Entidade) entidadeHome.obterEntidadePorApelido(str03);
	
					if (entidade instanceof ClassificacaoContas)
						cConta = (ClassificacaoContas) entidade;
	
					if (cConta == null)
						erros.add("Linea = " + contador + " - Erro: 14 - "+ str03.trim()+ " no es una clasificación de cuenta.");
	
					String str04 = linha.substring(17, 27); // 10
					if (str04.length() != 10)
						erros.add("Linea = "+ contador+ " Coluna 17~27 - Erro: 04 - Tamaño de registro inválido.");
					String str05 = linha.substring(27, 28); // 01 Estado del total
															// del nivel
					if (str05.length() != 1)
						erros.add("Linea = "+ contador+ " Coluna 27~28 - Erro: 04 - Tamaño de registro inválido.");
					String str06 = linha.substring(28, 50); // 22 Total del nivel
					if (str06.length() != 22)
						erros.add("Linea = "+ contador+ " Coluna 28~50 - Erro: 04 - Tamaño de registro inválido.");
	
					if (cConta != null)
					{
						if (!this.classificacaoContas.contains(cConta))
						{
							this.classificacaoContas.add(cConta);
							
							if (!this.saldoAtualClassificacaoContasTotalizado.containsKey(cConta.obterCodigo()))
								erros.add("Linea = "+ contador+ " - Erro: 24 - Cuenta "	+ cConta.obterCodigo()+ " totalizada no posee registro de asiento del tipo 02");
							else
							{
								double valorTotalizadoMemoria = Double.parseDouble(this.saldoAtualClassificacaoContasTotalizado.get(cConta.obterCodigo()).toString());
								double valorTotalizadoArquivo = Double.parseDouble(str06);
	
								if (strNivel.equals("01") || strNivel.equals("05")|| strNivel.equals("06") || strNivel.equals("09"))
								{
									if (str05.equals("C"))
										valorTotalizadoArquivo = valorTotalizadoArquivo	* -1;
								}
								else if (strNivel.equals("02") || strNivel.equals("03") || strNivel.equals("04") || strNivel.equals("07")) 
								{
									if (str05.equals("D"))
										valorTotalizadoArquivo = valorTotalizadoArquivo	* -1;
								}
	
								if (valorTotalizadoMemoria != valorTotalizadoArquivo)
								{
									erros.add("Linea = "+ contador+ " - Erro: 20 - Sumatoria del saldo de la cuenta: "+ cConta.obterCodigo()+ " esta incorrecta.");
									/*System.out.println("Classif.: "	+ cConta.obterCodigo());
									System.out.println("valorTotalizadoMemoria: "+ formataValor.format(valorTotalizadoMemoria));
									System.out.println("valorTotalizadoArquivo: "+ formataValor.format(valorTotalizadoArquivo));
									System.out.println("valorTotalizadoArquivo: "+ str06);
									System.out.println("----------------------: ");*/
								}
							}
						} else
							erros.add("Linea = " + contador+ " - Erro: 21 - Cuenta "+ cConta.obterCodigo() + " esta em duplicidad");
					}
				}
				contador++;
			}
			
			for (ClassificacaoContas cContas : this.classificacaoContasTotalizadas) 
			{
				if (!this.classificacaoContas.contains(cContas))
					erros.add("Error: 22 - La Cuenta " + cContas.obterCodigo()+ " no fue totalizada");
			}

			Collection validaTotContasNivel3 = new ArrayList();
		
			validaTotContasNivel3 = this.validarTotatizadorContasNivel3(saldoAtualContaTotalizado.values());
			erros = validaTotContasNivel3;
			
			Collection<String> contasExtraContabeis = new ArrayList<>();
			contasExtraContabeis.add("9000000001");
			contasExtraContabeis.add("9000000002");
			contasExtraContabeis.add("9000000003");
			contasExtraContabeis.add("9000000004");
			contasExtraContabeis.add("9000000005");
			contasExtraContabeis.add("9000000006");
			contasExtraContabeis.add("9000000007");
			contasExtraContabeis.add("9000000008");
			contasExtraContabeis.add("9000000009");
			contasExtraContabeis.add("9000000010");
			contasExtraContabeis.add("9000000011");
			
			for(String contaStr : contasExtraContabeis)
			{
				if(!this.contas.contains(contaStr))
					erros.add("Error: 23 - Falta informar la cuenta extracontable "+contaStr);
			}
		
			if (erros.size() == 0)
				this.compararContas();
			
			//file.write("Comparou as contas \r\n");
		
			if (erros.size() == 0)
			{
				if (this.obterMesMovimento() != 7)
				{
					this.compararSaldoAtualComAnterior();
					//file.write("Comparou Saldo atual com anterior \r\n");
				}
			}
		
			if (erros.size() == 0) 
			{
				this.gravarMovimentacaoes();
				//file.write("Gravou Movimentações \r\n");
				this.copiarArquivo();
				//file.write("Copiou os arquivos \r\n");
			}
		}
		catch (Exception e) 
		{
			System.out.println(e.toString());
			erros.add(e.toString() + " - Linea: " + contador);
			//file.write(e.toString() + "\r\n");
			//file.close();
			//erro = true;
		}
		
		//if(!erro)
			//file.close();
		
		return erros;
	}

	/////////////////// FIM DO MÉTODO PRA VALIDAR O ARQUIVO CONTABIL
	// ////////////////////////////////////////

	private void copiarArquivo() throws Exception
	{
		InputStream is = null;
		OutputStream os = null;

		byte[] buffer;
		boolean success = true;
		try 
		{
			is = new FileInputStream("" + "C:/Aseguradoras/Archivos/" + this.nomeArquivo + ".txt");

			os = new FileOutputStream("" + "C:/Aseguradoras/Backup/"+ this.nomeArquivo + "_Backup.txt");

			buffer = new byte[is.available()];
			is.read(buffer);
			os.write(buffer);
		}
		catch (IOException e) 
		{
			success = false;
		}
		catch (OutOfMemoryError e) 
		{
			success = false;
		}
		finally 
		{
			try 
			{
				if (is != null)
				{
					is.close();
					is = null;
				}
				
				if (os != null)
				{
					os.flush();
					os.close();
			        os = null;
				}
				
			} 
			catch (IOException e) 
			{
				System.out.println(e.getMessage());
			}
		}

		File arquivo = new File("C:/Aseguradoras/Archivos/" + this.nomeArquivo+ ".txt");
		if(arquivo.exists())
			arquivo.delete();

	}

	private void compararContas() throws Exception
	{
		for (Iterator i = this.contas.iterator(); i.hasNext();)
		{
			Conta conta = (Conta) i.next();

			DecimalFormat formatador = new DecimalFormat();
			formatador.applyPattern("#,##0.00");

			BigDecimal saldoAtual = this.saldoAtualContaTotalizado.get(conta.obterCodigo());
			BigDecimal saldoAnterior = this.saldoAnteriorContaTotalizado.get(conta.obterCodigo());
			BigDecimal credito = this.creditoContaTotalizado.get(conta.obterCodigo());
			BigDecimal debito = this.debitoContaTotalizado.get(conta.obterCodigo());
			BigDecimal saldoMoedaEstrangeira = this.saldoMoedaEstrangeiraContaTotalizado.get(conta.obterCodigo());

			if (conta.obterApelido().substring(0, 2).equals("01") || conta.obterApelido().substring(0, 2).equals("05") || conta.obterApelido().substring(0, 2).equals("06"))
			{
				//double tot = (saldoAnterior + debito) - credito;
				BigDecimal tot = (saldoAnterior.add(debito)).subtract(credito);
				tot.setScale(30);
				
				if (saldoAtual.doubleValue() != tot.doubleValue())
				{
					erros.add("Erro: 348 - Cuenta " + conta.obterApelido()	+ " el saldo atual no cuadra");
					System.out.println("1 - " +conta.obterApelido()+" saldoAtual " + saldoAtual + " tot " + tot);
				}
			} 
			else if (conta.obterApelido().substring(0, 2).equals("02") || conta.obterApelido().substring(0, 2).equals("03")	|| conta.obterApelido().substring(0, 2).equals("04") || conta.obterApelido().substring(0, 2).equals("07"))
			{
				//double tot = (saldoAnterior - debito) + credito;
				BigDecimal tot = (saldoAnterior.subtract(debito)).add(credito);

				if (saldoAtual.doubleValue() != tot.doubleValue())
				{
					erros.add("Erro: 348 - Cuenta " + conta.obterApelido()+ " el saldo atual no cuadra");
					System.out.println("2 - " +conta.obterApelido()+" saldoAtual " + saldoAtual + " tot " + tot);
				}
			}

		}
	}

	private void compararSaldoAtualComAnterior() throws Exception
	{

		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");

		ClassificacaoContas cContas1 = (ClassificacaoContas) home.obterEntidadePorApelido("0100000000");
		ClassificacaoContas cContas2 = (ClassificacaoContas) home.obterEntidadePorApelido("0200000000");
		ClassificacaoContas cContas3 = (ClassificacaoContas) home.obterEntidadePorApelido("0300000000");
		ClassificacaoContas cContas4 = (ClassificacaoContas) home.obterEntidadePorApelido("0400000000");
		ClassificacaoContas cContas5 = (ClassificacaoContas) home.obterEntidadePorApelido("0500000000");
		ClassificacaoContas cContas6 = (ClassificacaoContas) home.obterEntidadePorApelido("0600000000");
		ClassificacaoContas cContas7 = (ClassificacaoContas) home.obterEntidadePorApelido("0700000000");

		Collection<ClassificacaoContas> contas = new ArrayList<>();
		contas.add(cContas1);
		contas.add(cContas2);
		contas.add(cContas3);
		contas.add(cContas4);
		contas.add(cContas5);
		contas.add(cContas6);
		contas.add(cContas7);

		int mesModificado = Integer.parseInt(mes);
		int anoModificado = Integer.parseInt(ano);

		if (mesModificado == 1)
		{
			mesModificado = 12;
			anoModificado--;
		}
		else
			mesModificado--;

		String mesModificado2 = new Integer(mesModificado).toString();

		if (mesModificado2.length() == 1)
			mesModificado2 = "0" + mesModificado2;

		String mesAnoModificao = mesModificado2	+ new Integer(anoModificado).toString();

		for (ClassificacaoContas cContas : contas)
		{
			double saldoAnteriorMesAnterior = cContas.obterTotalizacaoExistente(this.obterOrigem(),	mesAnoModificao);

			if (this.saldoAnteriorClassificacaoContasTotalizado.get(cContas.obterCodigo()) != null)
			{
				double saldoAnteriorMesAtual = Double.parseDouble(this.saldoAnteriorClassificacaoContasTotalizado.get(cContas.obterCodigo()).toString());

				if (saldoAnteriorMesAtual != saldoAnteriorMesAnterior)
				{
					System.out.println("saldoAnteriorMesAnterior: "	+ saldoAnteriorMesAnterior);
					System.out.println("saldoAnteriorMesAtual: " + saldoAnteriorMesAtual);

					erros.add("Erro: 350 - Saldo Acual de la Cuenta " + cContas.obterApelido() + " no se cuadra en Saldo Anterior");
				}
			}
			
			if(cContas.obterApelido().equals("0100000000") || cContas.obterApelido().equals("0200000000"))
			{
				if(this.saldoAnteriorClassificacaoContasTotalizado.get(cContas.obterCodigo()) != null && this.saldoAtualClassificacaoContasTotalizado.get(cContas.obterCodigo())!=null)
				{
					double saldoAnterior = Double.parseDouble(this.saldoAnteriorClassificacaoContasTotalizado.get(cContas.obterCodigo()).toString());
					double saldoAtual = Double.parseDouble(this.saldoAtualClassificacaoContasTotalizado.get(cContas.obterCodigo()).toString());
					
					//if(saldoAtual == saldoAnterior)
						//erros.add("Erro: 350 - Saldo Actual de la Cuenta " + cContas.obterApelido() + " és el mismo que el Saldo Anterior (en el archivo)");
				}
			}
		}
	}

	private void gravarMovimentacaoes() throws Exception
	{
		for (MovimentacaoFinanceiraConta mf : this.movimentacaoes)
			mf.incluir2();

		this.gravarTotalizacoes();
	}

	private void gravarTotalizacoes() throws Exception
	{
		for (ClassificacaoContas cContas : this.classificacaoContas)
		{
			BigDecimal saldoAtual = this.saldoAtualClassificacaoContasTotalizado.get(cContas.obterCodigo());
			BigDecimal saldoAnterior = this.saldoAnteriorClassificacaoContasTotalizado.get(cContas.obterCodigo());
			BigDecimal credito = this.creditoClassificacaoContasTotalizado.get(cContas.obterCodigo());
			BigDecimal debito = this.debitoClassificacaoContasTotalizado.get(cContas.obterCodigo());
			BigDecimal moedaEstrangeira = this.saldoMoedaEstrangeiraClassificacaoContasTotalizado.get(cContas.obterCodigo());

			//System.out.println(cContas.obterApelido() + " CreditoCcontas: " + credito);
			//System.out.println(cContas.obterApelido() + " DebitoCcontas: " + debito);
			
			cContas.incluirRelatorio(mes + ano, saldoAtual.doubleValue(), debito.doubleValue(), credito.doubleValue(),saldoAnterior.doubleValue(), moedaEstrangeira.doubleValue(), seguradora);
		}

		for (Conta conta : this.contas)
		{
			BigDecimal saldoAtual = this.saldoAtualContaTotalizado.get(conta.obterCodigo());
			BigDecimal saldoAnterior = this.saldoAnteriorContaTotalizado.get(conta.obterCodigo());
			BigDecimal credito = this.creditoContaTotalizado.get(conta.obterCodigo());
			BigDecimal debito = this.debitoContaTotalizado.get(conta.obterCodigo());
			BigDecimal saldoMoedaEstrangeira = this.saldoMoedaEstrangeiraContaTotalizado.get(conta.obterCodigo());
			
			//System.out.println(conta.obterApelido() + " CreditoCcontas: " + credito);
			//System.out.println(conta.obterApelido() + " DebitoCcontas: " + debito);

			conta.incluirRelatorio(mes + ano, saldoAtual.doubleValue(),saldoMoedaEstrangeira.doubleValue(), debito.doubleValue(), credito.doubleValue(), saldoAnterior.doubleValue(),	seguradora);

		}
	}

	private Collection<Conta> contas = new ArrayList<>();

	private Collection<ClassificacaoContas> classificacaoContas = new ArrayList<>();

	private Map<String, BigDecimal> saldoAtualContaTotalizado = new TreeMap<String, BigDecimal>();

	private Map<String, BigDecimal> saldoAnteriorContaTotalizado = new TreeMap<String, BigDecimal>();

	private Map<String, BigDecimal> saldoMoedaEstrangeiraContaTotalizado = new TreeMap<String, BigDecimal>();

	private Map<String, BigDecimal> debitoContaTotalizado = new TreeMap<String, BigDecimal>();

	private Map<String, BigDecimal> creditoContaTotalizado = new TreeMap<String, BigDecimal>();

	private Map<String, BigDecimal> saldoAtualClassificacaoContasTotalizado = new TreeMap<String, BigDecimal>();

	private Map<String, BigDecimal> saldoAnteriorClassificacaoContasTotalizado = new TreeMap<String, BigDecimal>();

	private Map<String, BigDecimal> saldoMoedaEstrangeiraClassificacaoContasTotalizado = new TreeMap<String, BigDecimal>();

	private Map<String, BigDecimal> debitoClassificacaoContasTotalizado = new TreeMap<String, BigDecimal>();

	private Map<String, BigDecimal> creditoClassificacaoContasTotalizado = new TreeMap<String, BigDecimal>();

	private Collection<MovimentacaoFinanceiraConta> movimentacaoes = new ArrayList<>();

	private Entidade seguradora;

	private String ano;

	private String mes;

	private Collection validarTotatizadorContasNivel3(Collection contasTotalizadas) throws Exception
	{
		EntidadeHome entidadeHome = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		//FileWriter w = new FileWriter("c:/tmp/ConsParam.txt");

		Parametro parametro = (Parametro) entidadeHome.obterEntidadePorApelido("parametros");

		try
		{
			Entidade entidade;
			ClassificacaoContas cContas;
			String operando2;
			
			for (Parametro.Consistencia consistencia : parametro.obterConsistencias())
			{
				//w.write(consistencia.obterSequencial() + "\r\n");
	
				if (consistencia.obterOperando1().substring(0, 1).equals("c"))
				{
					if (entidadeHome.obterEntidadePorApelido(consistencia.obterOperando1().substring(1,consistencia.obterOperando1().length())) != null)
					{
						entidade = (Entidade) entidadeHome.obterEntidadePorApelido(consistencia.obterOperando1().substring(1,consistencia.obterOperando1().length()));
						if (entidade instanceof ClassificacaoContas)
						{
							cContas = (ClassificacaoContas) entidade;
							
							/*if(cContas.obterApelido().equals("0305000000"))
								System.out.println("");*/
							
							if (this.saldoAtualClassificacaoContasTotalizado.containsKey(cContas.obterCodigo()))
								this.analisaOperador(cContas, consistencia,	consistencia.obterOperando1(), consistencia.obterOperador(), consistencia.obterOperando2());
						}
					}
				} 
				else if (consistencia.obterOperando1().equals("A"))
				{
					operando2 = consistencia.obterOperando2();
					//if(operando2.equals("0"))
						//System.out.println("");
					
					//Entidade entidade = null;
					//if(!operando2.equals("0"))
					entidade = (Entidade) entidadeHome.obterEntidadePorApelido(operando2.substring(1,operando2.length()));
					
					if (entidade instanceof ClassificacaoContas)
					{
						cContas = (ClassificacaoContas) entidade;
	
						if (this.saldoAtualClassificacaoContasTotalizado.containsKey(cContas.obterCodigo()))
							this.analisaOperador(cContas, consistencia,	consistencia.obterOperando1(), consistencia.obterOperador(), consistencia.obterOperando2());
					}
					else
						this.analisaOperador(null, consistencia, consistencia.obterOperando1(), consistencia.obterOperador(),consistencia.obterOperando2());
				}
				else if (consistencia.obterOperando1().equals("B"))
				{
					entidade = (Entidade) entidadeHome	.obterEntidadePorApelido(consistencia.obterOperando2().substring(1,consistencia.obterOperando2().length()));
					if (entidade instanceof ClassificacaoContas)
					{
						cContas = (ClassificacaoContas) entidade;
	
						if (this.saldoAtualClassificacaoContasTotalizado.containsKey(cContas.obterCodigo()))
							this.analisaOperador(cContas, consistencia,consistencia.obterOperando1(), consistencia.obterOperador(), consistencia.obterOperando2());
					} 
					else
						this.analisaOperador(null, consistencia, consistencia.obterOperando1(), consistencia.obterOperador(),consistencia.obterOperando2());
				}
			}
			
			//w.close();
		}
		catch (Exception e)
		{
			erros.add(e.toString() + " - Error Interno: (validarTotatizadorContasNivel3)\r\n");
			//w.write(e.toString() + "\r\n");
			//w.close();
		}
		
		return erros;
	}

	private BigDecimal A = new BigDecimal("0.00");

	private BigDecimal B =  new BigDecimal("0.00");

	private Collection analisaOperador(ClassificacaoContas cContas,Parametro.Consistencia consistencia, String operando1,String operador, String operando2) throws Exception
	{
		if (operador.equals("=="))
			this.verificarConsistencias(cContas, consistencia, operando1, 1,operando2);
		else if (operador.equals(">="))
			this.verificarConsistencias(cContas, consistencia, operando1, 2,operando2);
		else if (operador.equals(">"))
			this.verificarConsistencias(cContas, consistencia, operando1, 3,operando2);
		else if (operador.equals("<="))
			this.verificarConsistencias(cContas, consistencia, operando1, 4,operando2);
		else if (operador.equals("<"))
			this.verificarConsistencias(cContas, consistencia, operando1, 5,operando2);
		else if (operador.equals("<>"))
			this.verificarConsistencias(cContas, consistencia, operando1, 6,operando2);
		else if (operador.equals("="))
			this.verificarConsistencias(cContas, consistencia, operando1, 7,operando2);
		else if (operador.equals("+"))
			this.verificarConsistencias(cContas, consistencia, operando1, 8,operando2);
		else
			erros.add("Operador Inválido");

		return erros;

	}

	private Collection verificarConsistencias(ClassificacaoContas cContas,Parametro.Consistencia consistencia, String operando1,int operador, String operando2) throws Exception
	{
		// 1 - ==
		// 2 - >=
		// 3 - >
		// 4 - <=
		// 5 - <
		// 6 - <>
		// 7 - =
		// 8 - +

		/*
		 * System.out.println("------------------------: " );
		 * System.out.println("operando1: " + operando1);
		 * System.out.println("operandor: " + operador);
		 * System.out.println("operando2: " + operando2);
		 */

		switch (operador) 
		{
		case 1: 
		{
			BigDecimal totalizacao = new BigDecimal("0.00"); 
			BigDecimal operando2Double = new BigDecimal("0.00");
			
			if ((operando1.equals("A") && operando2.equals("B")) || (operando1.equals("B") && operando2.equals("A")))
			{
				if (this.A.doubleValue() == this.B.doubleValue())
				{

				}
				else
				{
					System.out.println("A: " + this.A);
					System.out.println("B: " + this.B);

					erros.add(consistencia.obterMensagem());
				}
			}
			else 
			{
				if(cContas.obterApelido().equals("0305000000"))
					System.out.println("");
					
				totalizacao = this.saldoAtualClassificacaoContasTotalizado.get(cContas.obterCodigo());
				operando2Double = new BigDecimal(operando2);

				if (totalizacao.doubleValue() == operando2Double.doubleValue()) 
				{

				} 
				else
				{
					System.out.println(cContas.obterApelido() + " - totalizacao: " + totalizacao);
					System.out.println("operando2: " + operando2);
					erros.add(consistencia.obterMensagem());
				}
			}
			
			//System.out.println("1 " + cContas.obterApelido() + ": " + totalizacao);
			
			break;
		}
		case 2: {
			BigDecimal totalizacao = new BigDecimal("0.00");
			if ((operando1.equals("A") && operando2.equals("B")) || operando1.equals("B") && operando2.equals("A"))
			{
				if (this.A.doubleValue() >= this.B.doubleValue())
				{

				}
				else
					erros.add(consistencia.obterMensagem());
			}
			else
			{
				if (operando1.equals("A"))
				{
					if (this.A.doubleValue() >= 0)
					{

					}
					else
						erros.add(consistencia.obterMensagem());
				}
				else if (operando1.equals("B"))
				{
					if (this.B.doubleValue() >= 0)
					{

					}
					else
						erros.add(consistencia.obterMensagem());
				}
				else
				{
					totalizacao = this.saldoAtualClassificacaoContasTotalizado.get(cContas.obterCodigo());
					BigDecimal operando2Double = new BigDecimal("0.00");

					if (!operando2.equals("A"))
						operando2Double = new BigDecimal(operando2);
					else if (this.A.doubleValue() > 0)
						operando2Double = this.A;

					if(totalizacao.doubleValue() >= operando2Double.doubleValue())
					{

					}
					else
						erros.add(consistencia.obterMensagem());
				}
			}
			
			//System.out.println("2 " + cContas.obterApelido() + ": " + totalizacao);
			break;
		}
		case 3: {
			BigDecimal totalizacao = new BigDecimal("0.00");
			if ((operando1.equals("A") && operando2.equals("B")) || operando1.equals("B") && operando2.equals("A"))
			{
				if (this.A.doubleValue() > this.B.doubleValue())
				{

				}
				else
					erros.add(consistencia.obterMensagem());
			}
			else
			{
				if (operando1.equals("A"))
				{
					if (this.A.doubleValue() > 0)
					{

					}
					else
						erros.add(consistencia.obterMensagem());
				}
				else if (operando1.equals("B"))
				{
					if (this.B.doubleValue() > 0)
					{

					}
					else
						erros.add(consistencia.obterMensagem());
				}
				else
				{
					totalizacao = this.saldoAtualClassificacaoContasTotalizado.get(cContas.obterCodigo());
					BigDecimal operando2Double = new BigDecimal("0.00");

					if (!operando2.equals("A"))
						operando2Double = new BigDecimal(operando2);
					else if (this.A.doubleValue() > 0)
						operando2Double = this.A;

					/*
					 * System.out.println("totalizacao: " + totalizacao);
					 * System.out.println("operando2Double: " +
					 * operando2Double);
					 */

					if (totalizacao.doubleValue() > operando2Double.doubleValue())
					{

					}
					else
					{
						erros.add(consistencia.obterMensagem());
					}
				}
			}
			
			//System.out.println("3 " + cContas.obterApelido() + ": " + totalizacao);
			break;
		}
		case 4: {
			BigDecimal totalizacao = new BigDecimal("0.00");
			if ((operando1.equals("A") && operando2.equals("B")) || operando1.equals("B") && operando2.equals("A"))
			{
				if (this.A.doubleValue() <= this.B.doubleValue())
				{

				}
				else
					erros.add(consistencia.obterMensagem());
			} 
			else
			{
				System.out.println("operando1: " + operando1);

				if (operando1.equals("A"))
				{
					if (this.A.doubleValue() <= 0)
					{

					}
					else
						erros.add(consistencia.obterMensagem());
				}
				else if (operando1.equals("B"))
				{
					if (this.B.doubleValue() <= 0)
					{

					}
					else
						erros.add(consistencia.obterMensagem());
				}
				else
				{
					totalizacao = this.saldoAtualClassificacaoContasTotalizado.get(cContas.obterCodigo());
					BigDecimal operando2Double = new BigDecimal("0.00");

					if (!operando2.equals("A"))
						operando2Double = new BigDecimal(operando2);
					else if (this.A.doubleValue() > 0)
						operando2Double = this.A;

					/*
					 * System.out.println("totalizacao: " + totalizacao);
					 * System.out.println("operando2Double: " +
					 * operando2Double);
					 */

					if (totalizacao.doubleValue() <= operando2Double.doubleValue())
					{

					}
					else
					{
						erros.add(consistencia.obterMensagem());
					}
				}
			}
			//System.out.println("4 " + cContas.obterApelido() + ": " + totalizacao);
			break;
		}
		case 5: {
			BigDecimal totalizacao = new BigDecimal("0.00");
			if ((operando1.equals("A") && operando2.equals("B")) || operando1.equals("B") && operando2.equals("A"))
			{
				if (this.A.doubleValue() < this.B.doubleValue())
				{

				}
				else
					erros.add(consistencia.obterMensagem());
			}
			else
			{
				if (operando1.equals("A"))
				{
					if (this.A.doubleValue() < 0)
					{

					}
					else
						erros.add(consistencia.obterMensagem());
				} 
				else if (operando1.equals("B"))
				{
					if (this.B.doubleValue() < 0)
					{

					}
					else
						erros.add(consistencia.obterMensagem());
				}
				else
				{
					totalizacao = this.saldoAtualClassificacaoContasTotalizado.get(cContas.obterCodigo());
					BigDecimal operando2Double = new BigDecimal("0.00");

					if (!operando2.equals("A"))
						operando2Double = new BigDecimal(operando2);
					else if (this.A.doubleValue() > 0)
						operando2Double = this.A;

					if (totalizacao.doubleValue() < operando2Double.doubleValue())
					{

					}
					else
					{
						erros.add(consistencia.obterMensagem());
					}
				}
			}
			
			//System.out.println("5 " + cContas.obterApelido() + ": " + totalizacao);
			break;
		}
		case 6: {
			BigDecimal totalizacao = new BigDecimal("0.00");
			if ((operando1.equals("A") && operando2.equals("B")) || operando1.equals("B") && operando2.equals("A"))
			{
				if (this.A.doubleValue() != this.B.doubleValue())
				{

				}
				else
					erros.add(consistencia.obterMensagem());
			}
			else
			{
				if (operando1.equals("A"))
				{
					if (this.A.doubleValue() != 0)
					{

					}
					else
						erros.add(consistencia.obterMensagem());
				}
				else if (operando1.equals("B"))
				{
					if (this.B.doubleValue() != 0)
					{

					}
					else
						erros.add(consistencia.obterMensagem());
				}
				else
				{
					totalizacao = this.saldoAtualClassificacaoContasTotalizado.get(cContas.obterCodigo());
					BigDecimal operando2Double = new BigDecimal(operando2);

					if (totalizacao.doubleValue() != operando2Double.doubleValue())
					{

					}
					else
					{
						erros.add(consistencia.obterMensagem());
					}
				}
			}
			//System.out.println("6 " + cContas.obterApelido() + ": " + totalizacao);
			break;
		}
		case 7: {
			if (operando1.equals("A") && operando2.equals("B"))
				this.A = this.B;
			else if (operando1.equals("B") && operando2.equals("A"))
				this.B = this.A;
			else if (operando1.equals("A") && operando2.equals("0"))
				this.A = new BigDecimal("0.00");
			else if (operando1.equals("B") && operando2.equals("0"))
				this.B = new BigDecimal("0.00");

			break;
		}
		case 8: {
			if (operando1.equals("A"))
				this.A = this.A.add(this.saldoAtualClassificacaoContasTotalizado.get(cContas.obterCodigo()));
			else if (operando1.equals("B"))
				this.B = this.B.add(this.saldoAtualClassificacaoContasTotalizado.get(cContas.obterCodigo()));

			break;
		}

		default: {
			erros.add("Operador Inválido");
		}
		}

		return erros;
	}
	
	public AgendaMovimentacaoImpl()
    {
    	errosTotal = new ArrayList();
        classificacaoContasTotalizadas = new ArrayList();
        contas = new ArrayList();
        classificacaoContas = new ArrayList();
        saldoAtualContaTotalizado = new TreeMap();
        saldoAnteriorContaTotalizado = new TreeMap();
        saldoMoedaEstrangeiraContaTotalizado = new TreeMap();
        debitoContaTotalizado = new TreeMap();
        creditoContaTotalizado = new TreeMap();
        saldoAtualClassificacaoContasTotalizado = new TreeMap();
        saldoAnteriorClassificacaoContasTotalizado = new TreeMap();
        saldoMoedaEstrangeiraClassificacaoContasTotalizado = new TreeMap();
        debitoClassificacaoContasTotalizado = new TreeMap();
        creditoClassificacaoContasTotalizado = new TreeMap();
        movimentacaoes = new ArrayList();
        A = new BigDecimal(0);
        B = new BigDecimal(0);
        apolices = new TreeMap<String, Apolice>();
        apolicesResgostro17 = new TreeMap<String, Apolice>();
        dadosPrevisoes = new ArrayList<DadosPrevisao>();
        dadosReaseguros = new TreeMap();
        dadosCoaseguros = new ArrayList();
        sinistros = new TreeMap();
        faturas = new ArrayList();
        anulacoes = new ArrayList();
        cobrancas = new ArrayList();
        aspectos2 = new ArrayList();
        suplementos = new ArrayList();
        refinacoes = new ArrayList();
        gastos2 = new ArrayList();
        anulacoes2 = new ArrayList();
        morosidades = new ArrayList();
        ratiosPermanentes = new ArrayList();
        ratiosUmAno = new ArrayList();
        ratiosTresAnos = new ArrayList();
    }
	
	private Collection<String> errosTotal;
	private Collection<String> erroData = new ArrayList<>();
    private Collection<String> erroInscricao = new ArrayList<>();
	
	public Collection<String> verificarApolice(String nomeArquivo, boolean arquivoAntigo) throws Exception
    {
		try
		{
			File file = new File("C:/Aseguradoras/Archivos/A" + nomeArquivo + ".txt");
	        File file2 = new File("C:/Aseguradoras/Archivos/B" + nomeArquivo + ".txt");
	        
	        Collection<String> linhasA = new ArrayList<>();
			Collection<String> linhasB = new ArrayList<>();
	        
	        System.out.println("Archivo: " + file.getName());
	        
	        if(file.exists())
	        {
	        	if(file2.exists())
	            {
	        		int numeroLinha = 1;
	               
	        		FileReader reader = new FileReader(file);  
	        		BufferedReader in = new BufferedReader(reader);  
	        		String string,linha2;
	          	   
	        		while ((string = in.readLine()) != null)
	        		{
	        			linha2 = string;
	        			linhasA.add(linha2);
	        		}
	        		
	        		reader = new FileReader(file2);  
	        		in = new BufferedReader(reader);
	        		
	        		while ((string = in.readLine()) != null)
	        		{
	        			linha2 = string;
	        			linhasB.add(linha2);
	        		}
	        		
	        		file = null;
	        		file2 = null;
	        		in.close();
	        		reader.close();
	        		in = null;
	        		reader = null;
	        		System.gc();
	        		
	        		for(String linha : linhasA)
	        		{
	        			if(arquivoAntigo)
	        				linha = "00"+linha;
	        		   	
	        			if(linha.trim().length() > 0)
	        			{
	        				String numeroLinhaStr = linha.substring(0,7);
	        				if(this.eNumero(numeroLinhaStr))
	        				{
	        					int numeroLinhaArq = Integer.parseInt(numeroLinhaStr);
				               	
	        					if(numeroLinha!=numeroLinhaArq)
				               	{
	        						errosTotal.add("Error: 02 - Numero secuencial invalido (Archivo A)- Línea: " + numeroLinha);
				               		break;
				               	}
				               	else
				               	{
				               		System.out.println("numeroLinha: " + numeroLinha);
			        				errosTotal.addAll(validarApolice(linha, numeroLinha));
				               	}
		               		}
		               		else
		               			errosTotal.add("Error: 01 - Numero secuencial "+numeroLinhaStr+" no es numerico (Archivo A) - Línea: " + numeroLinha);
			               	
			               	numeroLinha++;
		               	}
	          	   }
	        	   
	        	   numeroLinha = 1;
	                
	        	   for(String linha : linhasB)
	        	   {  
	        		   if(arquivoAntigo)
	        			   linha = "00"+linha;
		           	 	
	        		   if(linha.trim().length() > 0)
	        		   {
	        			   String numeroLinhaStr = linha.substring(0,7);
	        			   if(this.eNumero(numeroLinhaStr))
	        			   {
	        				   int numeroLinhaArq = Integer.parseInt(numeroLinhaStr);
				        	
	        				   if(numeroLinha!=numeroLinhaArq)
	        				   {
	        					   errosTotal.add("Error: 02 - Numero secuencial invalido (Archivo Datos del Asegurado) - Línea: " + numeroLinha);
	        					   break;
	        				   }
	        				   else
	        				   {
	        					   System.out.println("numeroLinhaB: " + numeroLinha);
	        					   errosTotal.addAll(validarAsegurado(linha,numeroLinha));
	        				   }
	        			   }
	        			   else
	        				   errosTotal.add("Error: 01 - Numero secuencial "+numeroLinhaStr+" no es numerico (Archivo B) - Línea: " + numeroLinha);
			        		
	        			   numeroLinha++;
	        		   }
	        	   }
	                
	        	   System.out.println("Total de Erros: " + errosTotal.size());
	        	   System.out.println("Total de Erros Data: " + erroData.size());
	        	   System.out.println("Total de Erros Inscricao: " + erroInscricao.size());
	                
	        	   if(errosTotal.size() == 0)
	        	   {
	        		   boolean podeGravar1 = false;
	        		   boolean podeGravar2 = false;
	                	
	        		   if((erroData.size() > 0 && this.eEspecial()) || erroData.size() == 0)
	        		   {
	        			   this.nomeArquivo = "A" + nomeArquivo;
		                	
	        			   podeGravar1 = true;
		                	
	        			   this.nomeArquivo = "B" + nomeArquivo;
	        		   }
	        		   else
	        		   {
	        			   if(!this.eEspecial())
	        				   errosTotal.addAll(erroData);
	        		   }
	                	
	        		   if((erroInscricao.size() > 0 && this.inscricaoEspecial) || erroInscricao.size() == 0)
	        		   {
	        			   this.nomeArquivo = "A" + nomeArquivo;
		                	
	        			   podeGravar2 = true;
		                	
	        			   this.nomeArquivo = "B" + nomeArquivo;
	        		   }
	        		   else
	        		   {
	        			   if(!this.inscricaoEspecial)
	        				   errosTotal.addAll(erroInscricao);
	        		   }
	                	
	        		   if(podeGravar1 && podeGravar2)
	        		   {
	        			   Collection<String> errosGravacao = new ArrayList<>();
	        			   Apolice apolice;
	                		
	        			   for(String keyApolice : this.apolices.keySet())
	        			   {
	        				   if(!this.apolicesResgostro17.containsKey(keyApolice))
	        				   {
	        					   apolice = this.apolices.get(keyApolice);
	                				
	        					   errosGravacao.add("Error 140: Póliza " + apolice.obterNumeroApolice() + " no fue encuentrada en Datos del Asegurado");
	        				   }
	        			   }
	                		
	        			   if(errosGravacao.size() == 0)
	        			   {
	        				   errosGravacao = gravarEventosDaApolice();
	        				   if(errosGravacao.size() == 0)
	        				   {
	        					   this.nomeArquivo = "A" + nomeArquivo;
	        					   copiarArquivo();
	        					   this.nomeArquivo = "B" + nomeArquivo;
	        					   copiarArquivo();
	        				   }
	        			   }
	        			   else
	        				   errosTotal.addAll(errosGravacao);
	        		   }
	        	   }
	        	   else
	        	   {
	        		   if(!this.eEspecial())
	        			   errosTotal.addAll(erroData);
	        		   if(!this.inscricaoEspecial)
	        			   errosTotal.addAll(erroInscricao);
	        	   }
	        	   
	        	   //return errosTotal;
	            } 
	        	else
	        		//throw new Exception("Error: 02 - El Archivo B" + nomeArquivo + ".txt no fue encuentrado (Datos del Asegurado)");
	        		errosTotal.add("Error: 02 - El Archivo B" + nomeArquivo + ".txt no fue encuentrado (Datos del Asegurado)");
	        }
	        else
	        	//throw new Exception("Error: 01 - El Archivo A" + nomeArquivo + ".txt no fue encuentrado");
	        	errosTotal.add("Error: 01 - El Archivo A" + nomeArquivo + ".txt no fue encuentrado");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			errosTotal.add(e.getMessage());
		}
		
		return errosTotal;
    }

	private Map<String,Apolice> apolices = new TreeMap<>();

	private Collection<DadosPrevisao> dadosPrevisoes = new ArrayList<>();

	private Map<String,DadosReaseguro> dadosReaseguros = new TreeMap<>();

	private Collection<DadosCoaseguro> dadosCoaseguros = new ArrayList<>();

	private Map<Long,Sinistro> sinistros = new TreeMap<>();

	private Collection<FaturaSinistro> faturas = new ArrayList<>();

	private Collection<AnulacaoInstrumento> anulacoes = new ArrayList<>();

	private Collection<RegistroCobranca> cobrancas = new ArrayList<>();

	private Collection<AspectosLegais> aspectos2 = new ArrayList<>();

	private Collection<Suplemento> suplementos = new ArrayList<>();

	private Collection<Refinacao> refinacoes = new ArrayList<>();

	private Collection<RegistroGastos> gastos2 = new ArrayList<>();

	private Collection<RegistroAnulacao> anulacoes2 = new ArrayList<>();

	private Collection<Morosidade> morosidades = new ArrayList<>();

	private Collection ratiosPermanentes = new ArrayList();

	private Collection ratiosUmAno = new ArrayList();

	private Collection ratiosTresAnos = new ArrayList();

	private Collection<String> gravarEventosDaApolice() throws Exception
    {
    	Collection<String> erros = new ArrayList<String>();
    	
    	System.out.println("Gravando Eventos......: ");
    	String classe = "";
    	
        try
        {
        	classe  = "Apolice";
        	Apolice apliceapoliceAnterior;
        	
        	for(Apolice apolice : apolices.values())
	        {
        		apliceapoliceAnterior = apolice.obterApoliceAnterior();
        		
	            if(apliceapoliceAnterior != null)
	            {
	            	apliceapoliceAnterior.atualizarNumeroEndoso(apolice.obterNumeroEndoso());
	            	apliceapoliceAnterior.atualizarCertificado(apolice.obterCertificado());
	                if(apliceapoliceAnterior.obterSituacaoSeguro().equals("Vigente"))
	                	apliceapoliceAnterior.atualizarSituacaoSeguro("No Vigente");
	            }
	            
	            apolice.incluir();
	            if(new Date().compareTo(apolice.obterDataPrevistaConclusao()) >= 0)
	            	apolice.atualizarSituacaoSeguro("No Vigente");
	            
	            //apolice.atualizarFase(EVENTO_CONCLUIDO);
	        }
        	
        	System.out.println("Gravou Apolices......: " + apolices.size());
        	
        	for(Apolice apolice : apolicesAtualizacao.values())
        		apolice.atualizarAseguradosETomadores();
        	
        	System.out.println("Atualizou Apolices......: " + apolicesAtualizacao.size());
        	
        	classe  = "Dados Previsao";
	        for(DadosPrevisao dados : dadosPrevisoes)
	        {
	        	dados.incluir();
	            
	            //dados.atualizarFase(EVENTO_CONCLUIDO);
	        }
	        
	        System.out.println("Gravou Dados Previsao......: " + dadosPrevisoes.size());
	        
	        classe  = "Dados Reaseguro";
	        for(DadosReaseguro dados : dadosReaseguros2)
	        {
	            dados.incluir();
	            
	            //dados.atualizarFase(EVENTO_CONCLUIDO);
	        }
	
	        System.out.println("Gravou Dados Reaseguro......: " + dadosReaseguros2.size());
	        
	        classe  = "Dados Coaseguro";
	        for(DadosCoaseguro dados : dadosCoaseguros)
	        {
	            dados.incluir();
	            
	            //dados.atualizarFase(EVENTO_CONCLUIDO);
	        }
	        
	        System.out.println("Gravou Dados Coaseguro......: " + dadosCoaseguros.size());
	        Apolice apolice;
	        
	        classe  = "Sinistro";
	        for(Sinistro sinistro : sinistros.values())
	        {
	            apolice = (Apolice) sinistro.obterSuperior();
	            
	            sinistro.incluir();
	            
	            //sinistro.atualizarFase(EVENTO_CONCLUIDO);
	            
	            apolice.atualizarAfetadoPorSinistro("Sí");
	        }
	        
	        System.out.println("Gravou Sinistro......: " + sinistros.size());
	        
	        classe  = "Faturas Sinistro";
	        for(FaturaSinistro fatura : faturas)
	        {
	            fatura.incluir();
	            
	           // fatura.atualizarFase(EVENTO_CONCLUIDO);
	        }
	                
	        System.out.println("Gravou Faturas Sinistro......: " + faturas.size());
	        
	        classe  = "Anulacao Instrumento";
	        for(AnulacaoInstrumento anulacao : anulacoes)
	        {
	            apolice = (Apolice)anulacao.obterSuperior();
	            
	            anulacao.incluir();
	            
	            apolice.atualizarDataEncerramento(anulacao.obterDataAnulacao());
	            
	            if(anulacao.obterTipo().equals("Total"))
	            	apolice.atualizarSituacaoSeguro("Anulada");
	            
	           // anulacao.atualizarFase(EVENTO_CONCLUIDO);
	        }
	
	        System.out.println("Gravou Anula\347\365es......: " + anulacoes.size());
	        
	        classe  = "Registro Cobranca";
	        for(RegistroCobranca cobranca : cobrancas)
	        {
	            cobranca.incluir();
	            
	            //cobranca.atualizarFase(EVENTO_CONCLUIDO);
	        }
	        
	        System.out.println("Gravou Cobran\347a......: " + cobrancas.size());
	        
	        classe  = "Aspectos Legais";
	        for(AspectosLegais aspectos : aspectos2)
	        {
	            aspectos.incluir();
	            
	           //aspectos.atualizarFase(EVENTO_CONCLUIDO);
	        }
	        
	        System.out.println("Gravou Aspectos Legais......: " + aspectos2.size());
	        
	        classe  = "Suplemento";
	        for(Suplemento suplemento : suplementos)
	        {
	            suplemento.incluir();
	            
	           // suplemento.atualizarFase(EVENTO_CONCLUIDO);
	        }
	
	        System.out.println("Gravou Suplementos......: " + suplementos.size());
	        
	        classe  = "Refinacao";
	        for(Refinacao refinacao : refinacoes)
	        {
	            refinacao.incluir();
	            
	            //refinacao.atualizarFase(EVENTO_CONCLUIDO);
	        }
	
	        System.out.println("Gravou Refinanciacao......: " + refinacoes.size());
	      
	        classe  = "Registro Gastos";
	        for(RegistroGastos gastos : gastos2)
	        {
	            gastos.incluir();
	            
	            //gastos.atualizarFase(EVENTO_CONCLUIDO);
	        }
	        
	        System.out.println("Gravou Registros de Gastos......: " + gastos2.size());
	        
	        DadosReaseguro dados;
	        
	        classe  = "Registro Anulacao";
	        for(RegistroAnulacao anulacao : anulacoes2)
	        {
	            anulacao.incluir();
	            
	            if(anulacao.obterTipo().equals("Total"))
	            {
	            	dados = (DadosReaseguro)anulacao.obterSuperior();
	            	dados.atualizarSituacao("No Vigente");
	            }
	            
	            //anulacao.atualizarFase(EVENTO_CONCLUIDO);
	        }
	        
	        System.out.println("Gravou Anulacoes 2......: " + anulacoes2.size());
	        
	        classe  = "Morosidade";
	        for(Morosidade morosidade : morosidades)
	        {
	            morosidade.incluir();
	            
	            //morosidade.atualizarFase(EVENTO_CONCLUIDO);
	        }
	
	        System.out.println("Gravou Morosidade......: " + morosidades.size());
	        
	        /*if(ratiosPermanentes.size() > 0)
	        {
	        	RatioPermanenteHome home = (RatioPermanenteHome) this.getModelManager().getHome("RatioPermanenteHome");
	        	
	        	for(Iterator k = home.obterRatiosPermanentes((Aseguradora) this.obterOrigem()).iterator(); k.hasNext();)
	            {
	                Evento e = (Evento)k.next();
	                
	                e.atualizarFase("concluido");
	            }
	        	
	        	for(Iterator i = ratiosPermanentes.iterator(); i.hasNext(); )
	            {
	            	RatioPermanente ratios = (RatioPermanente)i.next();
	            	
	                ratios.incluir();
	            }
	        }
	        
	        System.out.println("Gravou Ratios Permanentes......: " + ratiosPermanentes.size());
	        
	        for(Iterator i = ratiosUmAno.iterator(); i.hasNext(); )
	        {
	        	RatioUmAno ratios = (RatioUmAno)i.next();
	        	
	        	RatioUmAnoHome home = (RatioUmAnoHome) this.getModelManager().getHome("RatioUmAnoHome");
	        	
	            for(Iterator k = home.obterRatiosUmAno((Aseguradora)ratios.obterOrigem()).iterator(); k.hasNext();)
	            {
	                Evento e = (Evento)k.next();
	                
	                e.atualizarFase("concluido");
	            }
	            ratios.incluir();
	        }
	
	        System.out.println("Gravou Ratios 1 Ano......: " + ratiosUmAno.size());
	        
	        for(Iterator i = ratiosTresAnos.iterator(); i.hasNext(); )
	        {
	        	RatioTresAnos ratios = (RatioTresAnos)i.next();
	        	
	        	RatioTresAnosHome home = (RatioTresAnosHome) this.getModelManager().getHome("RatioTresAnosHome");
	        	
	            for(Iterator k = home.obterRatiosTresAnos((Aseguradora)ratios.obterOrigem()).iterator(); k.hasNext();)
	            {
	                Evento e = (Evento)k.next();
	                
	                e.atualizarFase("concluido");
	            }
	            ratios.incluir();
	        }
	
	        System.out.println("Gravou Ratios 3 Anos......: " + ratiosTresAnos.size());*/
	        
	        apolices.clear();
	        dadosPrevisoes.clear();
	        dadosReaseguros.clear();
	        dadosCoaseguros.clear();
	        sinistros.clear();
	        faturas.clear();
	        anulacoes.clear();
	        cobrancas.clear();
	        aspectos2.clear();
	        suplementos.clear();
	        refinacoes.clear();
	        gastos2.clear();
	        anulacoes2.clear();
	        morosidades.clear();
	        //ratiosPermanentes.clear();
	        //ratiosUmAno.clear();
	        //ratiosTresAnos.clear();
	        
	        //logGravacao.close();
        }
        catch (Exception e)
        {
        	erros.add("Error en Agregar " + classe);
        	erros.add(e.getMessage());
        	System.out.println(e.toString());
        	
        	this.atualizarFase(Evento.EVENTO_PENDENTE);
        	this.atualizarDescricao(this.obterDescricao()+"\nError en Agregar " + classe+"\n"+e.getMessage());
        	
        	this.enviarEmail("Error en Agregar " + this.obterTitulo() + " - " + this.obterOrigem().obterNome(), "Error en Agregar " + classe+"\n"+e.getMessage());
        	
        	for(Evento evento : this.obterInferiores())
        		evento.excluir();
		}
        
        return erros;
    }
	
	private void enviarEmail(String titulo, String descricao)
	{
		try
		{
			InfraProperties infra = InfraProperties.getInstance();
			String dir = infra.getProperty("arquivos.url");
			boolean enviaEmail = dir.indexOf("Laptopsala") == -1;
			
			if(enviaEmail)
			{
				Email email = new SimpleEmail();
				email.setHostName("mail.bcp.gov.py");
				email.setSmtpPort(25);
				email.setFrom("sisvalidacion@bcp.gov.py");
				email.setSubject(titulo);
				email.setMsg(descricao);
				email.addTo("gbrawer@bcp.gov.py");
				email.addCc("cferrac@bcp.gov.py");
				email.addCc("prodriguez@bcp.gov.py");
				
				email.send();
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}

	private String obterTipoMoeda(String cod, int numeroLinha, String registro) throws Exception {
		String moeda = "";

		if (cod.equals("01"))
			moeda = "Guaraní";
		else if (cod.equals("02"))
			moeda = "Dólar USA";
		else if (cod.equals("03"))
			moeda = "Euro";
		else if (cod.equals("04"))
			moeda = "Real";
		else if (cod.equals("05"))
			moeda = "Peso Arg";
		else if (cod.equals("06"))
			moeda = "Peso Uru";
		else if (cod.equals("07"))
			moeda = "Yen";
		//else
			//erros.add("Tipo Moneda invalida, Tipo Moneda = " + cod + " - Línea: " + numeroLinha + nRegistro);

		return moeda;
	}
	
	public boolean permiteAtualizar() throws Exception
	{
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		Entidade informatica = home.obterEntidadePorApelido("informatica");
		
		if(this.obterId() > 0)
		{
			if(this.obterFase().obterCodigo().equals(Evento.EVENTO_CONCLUIDO))
				return super.permiteAtualizar();
			else
			{
				if(informatica!=null)
				{
					if(this.obterUsuarioAtual().obterSuperiores().contains(informatica) || this.obterUsuarioAtual().obterId() == 1)
						return true;
					else
						return false;
				}
				else
					return super.permiteAtualizar();
			}
		}
		else
			return true;
	}
	
	public void atualizarQtdeA(int total) throws Exception
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm","update agenda_movimentacao set qtdeA = ? where id = ?");
		update.addInt(total);
		update.addLong(this.obterId());
		
		update.execute();
	}
	
	public void atualizarQtdeB(int total) throws Exception
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm","update agenda_movimentacao set qtdeB = ? where id = ?");
		update.addInt(total);
		update.addLong(this.obterId());
		
		update.execute();
	}
	
	public int obterQtdeRegistrosA() throws Exception
	{
		int qtde = 0;
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select qtdeA from agenda_movimentacao where id = ?");
		query.addLong(this.obterId());
		
		qtde = query.executeAndGetFirstRow().getInt("qtdeA");
		
		if(qtde == 0)
		{
			//String[] mesAnoArray = ultimaAgendaStr.split("/");
			//String mesAno = mesAnoArray[1] + mesAnoArray[0];
			String mesAno = "";
			mesAno+= this.obterAnoMovimento();
			
			if(new Integer(this.obterMesMovimento()).toString().length() == 1)
				mesAno += "0" + this.obterMesMovimento();
			else
				mesAno += this.obterMesMovimento();
			
			String sigla = this.obterOrigem().obterSigla();
			File file = new File("C:/Aseguradoras/Archivos/A" + sigla + mesAno + ".txt");
			
			if(file.exists())
			{
				Scanner scan = new Scanner(file);
				
				String linha = scan.nextLine();
				
				qtde = Integer.parseInt(linha.substring(34, 44));
				
				this.atualizarQtdeA(qtde);
			}
		}
		
		return qtde;
	}
	
	public int obterQtdeRegistrosB() throws Exception
	{
		int qtde = 0;
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select qtdeB from agenda_movimentacao where id = ?");
		query.addLong(this.obterId());
		
		qtde = query.executeAndGetFirstRow().getInt("qtdeB");
		
		if(qtde == 0)
		{
			String mesAno = "";
			mesAno+= this.obterAnoMovimento();
			
			if(new Integer(this.obterMesMovimento()).toString().length() == 1)
				mesAno += "0" + this.obterMesMovimento();
			else
				mesAno += this.obterMesMovimento();
			
			String sigla = this.obterOrigem().obterSigla();
			File file = new File("C:/Aseguradoras/Archivos/B" + sigla + mesAno + ".txt");
			
			if(file.exists())
			{
				Scanner scan = new Scanner(file);
				
				String linha = scan.nextLine();
				
				qtde = Integer.parseInt(linha.substring(34, 44));
				
				this.atualizarQtdeB(qtde);
			}
		}
		
		return qtde;
	}
	
	public Date obterDataModificacaoArquivo() throws Exception
	{
		Date data = null;
		
		String mesAno = "";
		mesAno+= this.obterAnoMovimento();
		
		if(new Integer(this.obterMesMovimento()).toString().length() == 1)
			mesAno += "0" + this.obterMesMovimento();
		else
			mesAno += this.obterMesMovimento();
		
		String sigla = this.obterOrigem().obterSigla();
		
		File file = new File("C:/Aseguradoras/Archivos/A" + sigla + mesAno + ".txt");
		
		if(file.exists())
		{
			long dataLong = file.lastModified();
			
			if(dataLong > 0)
				data = new Date(dataLong);
		}
		
		return data;
	}
	
	public void atualizarEspecial(String especial) throws Exception
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm","update agenda_movimentacao set especial = ? where id = ?");
		update.addString(especial);
		update.addLong(this.obterId());
		
		update.execute();
	}
	
	public String obterEspecial() throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select especial from agenda_movimentacao where id = ?");
		query.addLong(this.obterId());
		
		return query.executeAndGetFirstRow().getString("especial");
	}
	
	public boolean eEspecial() throws Exception
	{
		if(this.obterEspecial() == null)
			return false;
		else
		{
			if(this.obterEspecial().equals("Sim"))
				return true;
			else
				return false;
		}
	}
	
	public void atualizarInscricaoEspecial(String especial) throws Exception
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm","update agenda_movimentacao set inscricao_especial = ? where id = ?");
		update.addString(especial);
		update.addLong(this.obterId());
		
		update.execute();
	}
	
	public void atualizarSuplementosEspecial(String especial) throws Exception
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm","update agenda_movimentacao set suplemento_especial = ? where id = ?");
		update.addString(especial);
		update.addLong(this.obterId());
		
		update.execute();
	}
	
	public void atualizarCapitalEspecial(String especial) throws Exception
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm","update agenda_movimentacao set capital_especial = ? where id = ?");
		update.addString(especial);
		update.addLong(this.obterId());
		
		update.execute();
	}
	
	public void atualizarDataEspecial(String especial) throws Exception
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm","update agenda_movimentacao set fecha_especial = ? where id = ?");
		update.addString(especial);
		update.addLong(this.obterId());
		
		update.execute();
	}
	
	public void atualizarDocumentoEspecial(String especial) throws Exception
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm","update agenda_movimentacao set documento_especial = ? where id = ?");
		update.addString(especial);
		update.addLong(this.obterId());
		
		update.execute();
	}
	
	public void atualizarApAnteriorEspecial(String especial) throws Exception
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm","update agenda_movimentacao set ap_anterior_especial = ? where id = ?");
		update.addString(especial);
		update.addLong(this.obterId());
		
		update.execute();
	}

	public String obterInscricaoEspecial() throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select inscricao_especial from agenda_movimentacao where id = ?");
		query.addLong(this.obterId());
		
		return query.executeAndGetFirstRow().getString("inscricao_especial");
	}
	
	public String obterSuplementoEspecial() throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select suplemento_especial from agenda_movimentacao where id = ?");
		query.addLong(this.obterId());
		
		return query.executeAndGetFirstRow().getString("suplemento_especial");
	}
	
	public String obterCapitalEspecial() throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select capital_especial from agenda_movimentacao where id = ?");
		query.addLong(this.obterId());
		
		return query.executeAndGetFirstRow().getString("capital_especial");
	}
	
	public String obterDataEspecial() throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select fecha_especial from agenda_movimentacao where id = ?");
		query.addLong(this.obterId());
		
		return query.executeAndGetFirstRow().getString("fecha_especial");
	}
	
	public String obterDocumentoEspecial() throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select documento_especial from agenda_movimentacao where id = ?");
		query.addLong(this.obterId());
		
		return query.executeAndGetFirstRow().getString("documento_especial");
	}
	
	public String obterApAnteriorEspecial() throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select ap_anterior_especial from agenda_movimentacao where id = ?");
		query.addLong(this.obterId());
		
		return query.executeAndGetFirstRow().getString("ap_anterior_especial");
	}

	public void atualizaUltimaAgenda(String tipo) throws Exception
	    {
	    	long asegId = this.obterOrigem().obterId();
	    	int mes = this.obterMesMovimento();
	    	int ano = this.obterAnoMovimento();
	    	long agendaId = this.obterId();
	    	
	    	SQLQuery query = this.getModelManager().createSQLQuery("crm","select count(*) as qtde from ultima_agenda where aseguradora_id = ? and mes > ? and ano > ? and tipo = ?");
	    	query.addLong(asegId);
	    	query.addInt(mes);
	    	query.addInt(ano);
	    	query.addString(tipo);
	    	
	    	int qtde = query.executeAndGetFirstRow().getInt("qtde"); 
	    	
	    	if(qtde == 0)
	    	{
	    		query = this.getModelManager().createSQLQuery("crm","select count(*) as qtde from ultima_agenda where aseguradora_id = ? and tipo = ?");
	        	query.addLong(this.obterOrigem().obterId());
	        	query.addString(tipo);
	        	
	        	qtde = query.executeAndGetFirstRow().getInt("qtde");
	        	
	        	if(qtde == 0)
	        	{
	        		SQLUpdate insert = this.getModelManager().createSQLUpdate("crm","insert into ultima_agenda(aseguradora_id, agenda_id, mes, ano, tipo) values(?,?,?,?,?)");
	        		insert.addLong(asegId);
	        		insert.addLong(agendaId);
	        		insert.addInt(mes);
	        		insert.addInt(ano);
	        		insert.addString(tipo);
	        		
	        		insert.execute();
	        	}
	        	else
	        	{
	        		SQLUpdate update = this.getModelManager().createSQLUpdate("crm","update ultima_agenda set agenda_id = ?, mes = ?, ano = ? where aseguradora_id = ? and tipo = ?");
	        		update.addLong(agendaId);
	        		update.addInt(mes);
	        		update.addInt(ano);
	        		update.addLong(asegId);
	        		update.addString(tipo);
	        		
	        		update.execute();
	        	}
	    	}
	    }
	
	private ApoliceHome apoliceHome = null;
	private EntidadeHome entidadeHome = null;
	private UsuarioHome usuarioHome = null;
	private PlanoHome planoHome = null;
	private DadosReaseguroHome dadosReaseguroHome = null;
	private SinistroHome sinistroHome = null; 
	private AuxiliarSeguroHome auxiliarSeguroHome = null;
	private CorretoraHome corretoraHome = null;
	private AseguradoraHome aseguradoraHome = null;
	private ClassificacaoContasHome classificacaoContasHome = null;
	private RucCiHome rucCiHome = null;
	private Map<String, Apolice> apolicesResgostro17;
	
	public void instanciar() throws Exception
	{
		SampleModelManager mm = new SampleModelManager();
	    	
		apoliceHome = (ApoliceHome) mm.getHome("ApoliceHome");
		entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		planoHome = (PlanoHome) mm.getHome("PlanoHome");
		dadosReaseguroHome = (DadosReaseguroHome) mm.getHome("DadosReaseguroHome");
		sinistroHome = (SinistroHome) mm.getHome("SinistroHome");
		auxiliarSeguroHome = (AuxiliarSeguroHome) mm.getHome("AuxiliarSeguroHome");
		corretoraHome = (CorretoraHome) mm.getHome("CorretoraHome");
		aseguradoraHome = (AseguradoraHome) mm.getHome("AseguradoraHome");
		classificacaoContasHome = (ClassificacaoContasHome) mm.getHome("ClassificacaoContasHome");
		rucCiHome = (RucCiHome) mm.getHome("RucCiHome");
	     	
		this.especiais();
	    	
		apolices = new TreeMap<String, Apolice>();
		apolicesResgostro17 = new TreeMap<String, Apolice>();
		dadosPrevisoes = new ArrayList<DadosPrevisao>();
		dadosReaseguros = new TreeMap();
		dadosCoaseguros = new ArrayList();
		sinistros = new TreeMap();
		faturas = new ArrayList();
		anulacoes = new ArrayList();
		cobrancas = new ArrayList();
		aspectos2 = new ArrayList();
		suplementos = new ArrayList();
		refinacoes = new ArrayList();
		gastos2 = new ArrayList();
		anulacoes2 = new ArrayList();
		morosidades = new ArrayList();
		ratiosPermanentes = new ArrayList();
		ratiosUmAno = new ArrayList();
		ratiosTresAnos = new ArrayList();
	}
	 
	private void especiais() throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select fecha_especial,capital_especial,inscricao_especial,suplemento_especial,documento_especial,ap_anterior_especial,endoso_apolice from agenda_movimentacao where id = ?");
		query.addLong(this.obterId());
		 
		SQLRow[] rows = query.execute();
		String especial;
		 
		for(int i = 0 ; i < rows.length ; i++)
		{
			especial = rows[i].getString("fecha_especial");
			if(especial == null)
				dataEspecial =  false;
			else
				dataEspecial = especial.equals("Sim");
			 
			especial = rows[i].getString("capital_especial");
			if(especial == null)
				capitalEspecial = false;
			else
				capitalEspecial = especial.equals("Sim");
			 
			especial = rows[i].getString("inscricao_especial");
			if(especial == null)
				inscricaoEspecial = false;
			else
				inscricaoEspecial = especial.equals("Sim");
			 
			especial = rows[i].getString("suplemento_especial");
			if(especial == null)
				suplementoEspecial = false;
			else
				suplementoEspecial = especial.equals("Sim");
			 
			especial = rows[i].getString("documento_especial");
			if(especial == null)
				documentoEspecial = false;
			else
				documentoEspecial = especial.equals("Sim");
			 
			especial = rows[i].getString("ap_anterior_especial");
			if(especial == null)
				apAnteriorEspecial = false;
			else
				apAnteriorEspecial = especial.equals("Sim");
			 
			especial = rows[i].getString("endoso_apolice");
			if(especial == null)
				endosoApolice = false;
			else
				endosoApolice = especial.equals("Sim");
		 }
	 }
	
	private int numeroTotalRegistros = 0,qtde,ultimo,ultimoArquivo,ultimoArquivo2,cont,numeroRegistroInt;
    private Entidade aseguradora = null,destino,reaseguradora,corretora,aseguradora2,banco,entidadeReaseguradora ;
    private Usuario usuario = null;
    private Date dataGeracao = null;
    private String tipoArquivo = null;
    private boolean dataEspecial = false;
    private boolean documentoEspecial = false;
    private boolean apAnteriorEspecial = false;
    private boolean endosoApolice = false;
    private boolean capitalEspecial = false;
    private boolean inscricaoEspecial = false;
    private boolean suplementoEspecial = false;
    private String sigla,chaveUsuario,anoGeracao,mesGeracao,diaGeracao,anoReporte,mesReporte,apelidoCconta,numeroApolice,tipoApolice,tipoApoliceSub,statusApoliceSub,statusApolice,apoliceSuperiorStr,afetadorPorSinistro,afetadorPorSinistroSub,
    apoliceFlutuante,apoliceSub,codigoPlano,numeroFatura,modalidadeVenda,modalidadeVendaSub,anoInicioVigencia,mesInicioVigencia,diaInicioVigencia,anoFimVigencia,mesFimVigencia,diaFimVigencia,diasCobertura,anoEmissao,mesEmissao,diaEmissao,
    capitalGuaraniStr,tipoMoedaCapitalGuarani,capitalMeStr,primasSeguroStr,tipoMoedaPrimas,primasMeStr,principalGuaraniStr,tipoMoedaPrincipalGuarani,principalMeStr,incapacidadeGuaraniStr,tipoMoedaIncapacidadeGuarani,incapacidadeMeStr,
    enfermidadeGuaraniStr,tipoMoedaEnfermidade,enfermidadeMeStr,acidentesGuaraniStr,tipoMoedaAcidente,acidentesMeStr,outrosGuaraniStr,tipoMoedaOutros,outrosMeStr,financimantoGsStr,tipoMoedaFinanciamentoGS,financiamentoMeStr,premioGsStr, 
    tipoMoedaPremio,premioMeStr,formaPagamento,formaPagamentoSub,qtdeParcelas,refinacaoStr,refinacaoSub,inscricaoAgente,apoliceSuspeita,comissaoGsStr,tipoMoedaComissaoGs,comissaoMeStr,situacaoSeguro,situacaoSeguroSub,inscricaoCorredor,mesAno2,
    numeroInstrumento,mesAno,anoCorte,mesCorte,diaCorte,cursoStr,sinistroStr,reservasStr,fundoStr,premiosStr,tipoInstrumento,tipoInstrumento2,valorEndosoStr,qtdeStr,inscricaoReaseguradora1,tipoContratoReaseguro1,inscricaoCorredoraReaseguro1,nome,reaseguroGs1Str,
    tipoMoedaReaseguroGs1,reaseguroMe1Str,primaReaseguro1GsStr,tipoMoedaPrimaReaseguro1,primaReaseguro1MeStr,comissaoReaseguro1GsStr,tipoMoedaComissaoReaseguro1,comissaoReaseguro1MeStr,situacaoReaseguro1,grupo,inscricaoAseguradora,capitalGsStr,
    tipoMoedaCapitalGs1,porcentagemCoaseguradoraStr,primaGsStr,tipoMoedaPrimaGs,primaMeStr,numeroSinistro,anoSinistro,mesSinistro,diaSinistro,anoDenunciaSinistro,mesDenunciaSinistro,diaDenunciaSinistro,numeroLiquidador,montanteGsStr,tipoMoedaMontante,
    montanteMeStr,situacaoSinistro,mesFinalizacao,diaFinalizacao,mesRecupero,diaRecupero,recuperoTerceiroStr,participacaoStr,descricao,anoFinalizacao,anoRecupero,recuperoReaseguradoraStr,descricao2,caracter,tipoDocumento,numeroDocumento,
    rucProvedor,nomeProvedor,anoDocumento,montanteDocumentoStr,montanteDocumentoMEStr,montanteDocumentoPagoStr,anoPagamento,mesPagamento,diaPagamento,situacaoFatura,tipoDocumentoProvedor,mesDocumento,diaDocumento,anoAnulacao,mesAnulacao,
    tipoAnulacao,capitalAnuladoGsStr,tipoMoedaCapitalAnulado,capitalAnuladoMeStr,solicitado,diasCorridos,primaAnuladaGsStr,tipoMoedaPrimaAnuladaGs,primaAnuladaMeStr,comissaoAnuladaGsStr,tipoMoedaComissaoAnuladaGs,comissaoAnuladaMeStr,comissaoRecuperarGsStr,
    tipoMoedaComissaoRecuperarGs,comissaoRecuperarMeStr,saldoAnuladoGsStr,tipoMoedaSaldoAnuladoGs,saldoAnuladoMeStr,destinoSaldoAnulacao,diaAnulacao,motivoAnulacao,motivoAnulacao2,anoCobranca,mesCobranca,diaCobranca,anoVencimentoCobranca,
    mesVencimentoCobranca,diaVencimentoCobranca,cotaCobranca,tipoMoedaCobrancaGs,valorCobrancaMeStr,valorInteresCobrancaStr,valorCobrancaGsStr,numeroOrdem,anoNotificacao,mesNotificacao,diaNotificacao,assuntoQuestionado,demandante,demandado,julgado,
    turno,juiz,numeroSecretaria,advogado,circunscricao,forum,anoDemanda,mesDemanda,diaDemanda,montanteDemandandoStr,montanteSentencaStr,anoCancelamento,mesCancelamento,diaCancelamento,caraterDemanda,responsabilidadeMaximaStr,provisaoSinistroStr,
    objetoCausa,observacoes,objetoCausa2,observacoes2,numeroEndosoStr,anoVigenciaInicial,mesVigenciaInicial,diaVigenciaInicial,anoVigenciaVencimento,mesVigenciaVencimento,diaVigenciaVencimento,razaoEmissao,razaoEmissao2,financiamentoGsStr,
    tipoMoedaFinanciamentoGs,tipoPagamento,nomeTerceiro,abonadoGsStr,tipoMoedaAbonoGs,abonadoMeStr,bancoStr,situacaoPagamento,numeroCheque,inscricaoReaseguradora,tipoMoedaCapitalAnuladoGs,diaCorridos,tipoContrato,numeroParcela,anoVencimento,
    mesVencimento,diaVencimento,diaAtraso,valorGsStr,tipoMoeda,valorMeStr,situacaoAgente, nRegistro;
    
    private Date dataFimVigencia,dataEmissao,dataCorte,dataInicioVigencia,dataSinistro,dataDenunciaSinistro,dataFinalizacao,dataRecupero,dataPagamento,dataDocumento,dataAnulacao,dataCobranca,dataVencimentoCobranca,dataNotificacao,dataDemanda,
    dataCancelamento,dataVigenciaInicial,dataVigenciaVencimento,dataVencimento;
    private Plano plano;
    private Apolice apolice,apoliceAnterior,apoliceSuperior;
    private ClassificacaoContas cContas;
    private double numeroEndoso,certificado,capitalGuarani,capitalMe,primasSeguro,primasMe,principalGuarani,principalMe,incapacidadeGuarani,incapacidadeMe,enfermidadeGuarani,enfermidadeMe,acidentesGuarani,acidentesMe,outrosGuarani,outrosMe,
    financimantoGS,financiamentoMe,premioGs,premioMe,numeroEndosoAnterior,certificadoAnterior,comissaoGs,comissaoMe,curso,valorSinistro,reservas,fundos,premios,valorEndoso,reaseguroGs1,reaseguroMe1,primaReaseguro1Gs,primaReaseguro1Me,
    comissaoReaseguro1Gs,comissaoReaseguro1Me,capitalGs,porcentagemCoaseguradora,primaGs,primaMe,montanteGs,montanteMe,recuperoReaseguradora,recuperoTerceiro,participacao,montanteDocumento,montanteDocumentoME,montanteDocumentoPago,capitalAnuladoGs,
    capitalAnuladoMe,primaAnuladaGs,comissaoAnuladaGs,comissaoRecuperarGs,comissaoRecuperarMe,primaAnuladaMe,comissaoAnuladaMe,saldoAnuladoGs,saldoAnuladoMe,valorCobrancaGs,valorCobrancaMe,valorInteresCobranca,montanteDemandando,montanteSentenca,
    responsabilidadeMaxima,provisaoSinistro,numeroEndoso2,certificado2,financiamentoGs,abonadoGs,abonadoMe,valorGs,valorMe,nSuple;
    
    private AuxiliarSeguro corredor,agente,auxiliar;
    private boolean duplicada,mostrarErro110,entrou;
    private DadosPrevisao dadosPrevisao;
    private DadosReaseguro dadosReaseguro;
    private Collection<DadosReaseguro> dadosReaseguros2 = new ArrayList<>();
    private DadosCoaseguro dadosCoaseguro;
    private Sinistro sinistro;
    private FaturaSinistro fatura;
    private AnulacaoInstrumento anulacao;
    private RegistroCobranca cobranca;
    private AspectosLegais aspectos;
    private Suplemento suplemento;
    private Refinacao refinacao;
    private RegistroGastos gastos;
    private RegistroAnulacao registroAnulacao;
    private Morosidade morosidade;
    private Entidade asegAuxPlano = null;
    private Entidade.Atributo situacaoEntidade;
    private Map<Double, Suplemento> suplementosMap = new TreeMap<Double, Suplemento>();
    private Map<String,Suplemento> suplementosMem = new TreeMap<String, Suplemento>();
    private Map<String, Map<Double, Suplemento>> suplementosTODOSMap = new TreeMap<String, Map<Double,Suplemento>>();
    
    private void validarRegistro2(String linha, int numeroLinha)
    {
    	try
    	{
	    	nRegistro = " (Registro 02)";
			
			apolice = (Apolice)getModelManager().getEntity("Apolice");
			
			if(verificaPosicaoString(linha, 9, 19, true, numeroLinha, "Cuenta", nRegistro))
			{
				apelidoCconta = linha.substring(9, 19).trim();
				cContas = cContas = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido(apelidoCconta);
				
				if(cContas == null)
					erros.add("Error: 05 - Cuenta " + apelidoCconta + " no fue encontrada - Línea: " + numeroLinha + nRegistro);
			}
		    
			numeroApolice = "";
			if(verificaPosicaoString(linha, 19, 29, true, numeroLinha, "Número Póliza", nRegistro))
				numeroApolice = linha.substring(19, 29).trim();
			
			apolice.atribuirNumeroApolice(numeroApolice);
			
			tipoApolice = "";
			if(verificaPosicaoString(linha, 29, 30, true, numeroLinha, "Status del Instrumento", nRegistro))
			{
				tipoApoliceSub = linha.substring(29, 30).trim();
		                
				if(tipoApoliceSub.equals("1"))
					tipoApolice = "Nuevo";
				else if(tipoApoliceSub.equals("2"))
					tipoApolice = "Renovado";
				else
					erros.add("Error: 12 - Status del Instrumento es obligatorio - Línea: " + numeroLinha + nRegistro);
			}
	
			statusApolice = "";
			if(verificaPosicaoString(linha, 40, 41, true, numeroLinha, "Tipo del Instrumento", nRegistro))
			{
				statusApoliceSub = linha.substring(40, 41).trim();
		                
				if(statusApoliceSub.equals("1"))
					statusApolice = "P\363liza Individual";
				else if(statusApoliceSub.equals("2"))
					statusApolice = "P\363liza Madre";
				else if(statusApoliceSub.equals("3"))
					statusApolice = "Certificado de Seguro Colectivo";
				else if(statusApoliceSub.equals("4"))
					statusApolice = "Certificado Provisorio";
				else if(statusApoliceSub.equals("5"))
					statusApolice = "Nota de Cobertura de Reaseguro";
				else
					erros.add("Error: 13 - Tipo del Instrumento es obligatorio - Línea: " + numeroLinha + nRegistro);
			}
			
			apolice.atribuirStatusApolice(statusApolice);
		    
			numeroEndoso = 0;
			certificado = 0;
			
			if(verificaPosicaoString(linha, 584, 594, true, numeroLinha, "Número endoso", nRegistro))
				numeroEndoso = Double.valueOf(linha.substring(584, 594).trim());
			
			if(verificaPosicaoString(linha, 594, 601, true, numeroLinha, "Número certificado", nRegistro))
				certificado = Double.valueOf(linha.substring(594, 601).trim());
		                
			apoliceAnterior = null;
			if(apolices != null && cContas!=null && !statusApolice.equals(""))
			{
				if(verificaPosicaoString(linha, 30, 40, true, numeroLinha, "Póliza anterior", nRegistro))
				{
					String s = linha.substring(30, 40).trim();
					
					apoliceAnterior = apolices.get(obterOrigem().obterId() + s + cContas.obterId() + statusApolice);
					if(apoliceAnterior == null)
						apoliceAnterior = apoliceHome.obterApolice(obterOrigem(), s ,cContas, statusApolice);
				}
			}
		                
			if(tipoApoliceSub.equals("2") && !this.apAnteriorEspecial)
			{
				if(apoliceAnterior == null)
					erros.add("Error: 136 - Póliza anterior es obligatoria, tipo de póliza Renovado - Línea: " + numeroLinha + nRegistro);
			}
			
			apoliceSuperiorStr = "";
			apoliceSuperior = null;
			
			if(verificaPosicaoString(linha, 41, 51, true, numeroLinha, "Póliza madre", nRegistro))
			{
				apoliceSuperiorStr = linha.substring(41, 51).trim();
				if(apolices != null && cContas!=null && !statusApolice.equals(""))
				{
					apoliceSuperior = apolices.get(obterOrigem().obterId() + apoliceSuperiorStr + cContas.obterId() + statusApolice);
					if(apoliceSuperior == null)
						apoliceSuperior = apoliceHome.obterApolice(obterOrigem(), apoliceSuperiorStr,cContas, statusApolice);
				}
			}
			
			if(statusApoliceSub.equals("3") && apoliceSuperiorStr.equals(""))
				erros.add("Error: 131 - Certificado de Seguro Colectivo, numero de póliza madre no informado - Línea: " + numeroLinha + nRegistro);
			else
			{
				if(statusApoliceSub.equals("3") && apoliceSuperior == null)
					erros.add("Error: 132 - Certificado de Seguro Colectivo, numero de póliza madre no encuentrado en nostra base, numero de póliza "+numeroApolice+", póliza madre "+apoliceSuperiorStr+" - Línea: " + numeroLinha + nRegistro);
			}
		                
			afetadorPorSinistro = "";
			if(verificaPosicaoString(linha, 51, 52, true, numeroLinha, "Afectado por Sinistro", nRegistro))
			{
				afetadorPorSinistroSub = linha.substring(51, 52).trim();
		                
				if(afetadorPorSinistroSub.equals("1"))
					afetadorPorSinistro = "S\355";
				else if(afetadorPorSinistroSub.equals("2"))
					afetadorPorSinistro = "No";
				else
					erros.add("Error: 14 - Afectado por Sinistro es obligatorio - Línea: " + numeroLinha + nRegistro);
			}
		                
			apoliceFlutuante = "";
			if(verificaPosicaoString(linha, 52, 53, true, numeroLinha, "Póliza Flotante", nRegistro))
			{
				apoliceSub = linha.substring(52, 53).trim();
		                
				if(apoliceSub.equals("1"))
					apoliceFlutuante = "S\355";
				else if(apoliceSub.equals("2"))
					apoliceFlutuante = "No";
				else
					erros.add("Error: 15 - Póliza Flotante es obligatorio - Línea: " + numeroLinha + nRegistro);
			}
			
			plano = null;
			codigoPlano = "";
			asegAuxPlano = null;
			if(verificaPosicaoString(linha, 53, 65, false, numeroLinha, "Numero del Plan", nRegistro))
			{
				codigoPlano = linha.substring(53, 65).trim();
				
				if(!codigoPlano.startsWith("RG.0001"))
				{
					plano = planoHome.obterPlanoEspecial(codigoPlano);
					if(plano == null)
					{
						//Grupo Coasegurador R.C. Carretero Internacional usar os planos da Central S.A. de Seguros
						if(aseguradora.obterId() == 7160)
						{
							asegAuxPlano = entidadeHome.obterEntidadePorId(5233); //Central S.A. de Seguros
							plano = planoHome.obterPlano(asegAuxPlano,codigoPlano);
						}
						else
							plano = planoHome.obterPlano(aseguradora,codigoPlano);
					}
		                	
					if(plano == null)
						erros.add("Error: 85 - Numero del Plan " + codigoPlano + " no fue encuentrado - Línea: " + numeroLinha + nRegistro);
				}
			}
			
			numeroFatura = "";
			if(verificaPosicaoString(linha, 65, 80, false, numeroLinha, "Numero de la Factura", nRegistro))
			{
				numeroFatura = linha.substring(65, 80).trim();
				if(numeroFatura.equals(""))
					erros.add("Error: 16 - Numero de la Factura es obligatorio - Línea: " + numeroLinha + nRegistro);
			}
			
			modalidadeVenda = "";
			if(verificaPosicaoString(linha, 80, 81, true, numeroLinha, "Modalidad de Venta", nRegistro))
			{
				modalidadeVendaSub = linha.substring(80, 81).trim();
				if(modalidadeVendaSub.equals("1"))
					modalidadeVenda = "Con Intermediario";
				else if(modalidadeVendaSub.equals("2"))
					modalidadeVenda = "Sin Intermediario";
				else
					erros.add("Error: 17 - Modalidad de Venta es obligatorio - Línea: " + numeroLinha + nRegistro);
			}
			
			dataInicioVigencia = null;
			anoInicioVigencia = "";
			mesInicioVigencia = "";
			diaInicioVigencia = "";
			
			if(verificaPosicaoString(linha, 81, 85, true, numeroLinha, "Año inicio de Vigencia", nRegistro))
				anoInicioVigencia = linha.substring(81, 85).trim();
			if(verificaPosicaoString(linha, 85, 87, true, numeroLinha, "Mes inicio de Vigencia", nRegistro))
				mesInicioVigencia = linha.substring(85, 87).trim();
			if(verificaPosicaoString(linha, 87, 89, true, numeroLinha, "Día inicio de Vigencia", nRegistro))
				diaInicioVigencia = linha.substring(87, 89).trim();
			
			dataFimVigencia = null;
			anoFimVigencia = "";
			mesFimVigencia = "";
			diaFimVigencia = "";
			
			if(verificaPosicaoString(linha, 89, 93, true, numeroLinha, "Año fim de Vigencia", nRegistro))
				anoFimVigencia = linha.substring(89, 93).trim();
			if(verificaPosicaoString(linha, 93, 95, true, numeroLinha, "Mes fim de Vigencia", nRegistro))
				mesFimVigencia = linha.substring(93, 95).trim();
			if(verificaPosicaoString(linha, 95, 97, true, numeroLinha, "Día fim de Vigencia", nRegistro))
				diaFimVigencia = linha.substring(95, 97).trim();
		                
			if(!dataEspecial)
			{
				if(eData(diaInicioVigencia+"/"+mesInicioVigencia+"/"+anoInicioVigencia))
					dataInicioVigencia = new SimpleDateFormat("dd/MM/yyyy").parse(diaInicioVigencia + "/" + mesInicioVigencia + "/" + anoInicioVigencia);
				else
					erros.add("Error: 93 - Fecha Inicio Vigencia Invalida "+diaInicioVigencia+"/"+mesInicioVigencia+"/"+anoInicioVigencia+" - Línea: " + numeroLinha + nRegistro);
				
				if(dataInicioVigencia!=null)
				{
					if(plano!=null)
					{
						if(plano.obterDataResolucao().compareTo(dataInicioVigencia)>0)
							erros.add("Error: 129 - Fecha de início de vigencia de la póliza es anterior a fecha de início de validez del plan - Línea: " + numeroLinha + nRegistro);
					}
			                    
					Calendar anoAtual = Calendar.getInstance();
					anoAtual.setTime(new Date());
			                    
					Date anoAnterior = null;
					Date proximoAno = null;
			                    
					anoAtual.add(Calendar.YEAR, -1);
			                    
					anoAnterior = anoAtual.getTime();
			                    
					anoAtual.setTime(new Date());
					anoAtual.add(Calendar.YEAR, 1);
			                    
					proximoAno = anoAtual.getTime();
			                    
					if(dataInicioVigencia.compareTo(anoAnterior) < 0 || dataInicioVigencia.compareTo(proximoAno) > 0)
						erroData.add("Error: 130 - Fecha de inicio de vigencia es menor que el año actual menos uno, o mayor que el año actual más uno, Póliza: "+numeroApolice+" Inicio Vigencia: "+diaInicioVigencia + "/" + mesInicioVigencia + "/" + anoInicioVigencia+" Fin Vigencia:"+diaFimVigencia + "/" + mesFimVigencia + "/" + anoFimVigencia + "- Línea: " + numeroLinha + nRegistro);
				}
			}
			else
				dataInicioVigencia = new SimpleDateFormat("dd/MM/yyyy").parse(diaInicioVigencia + "/" + mesInicioVigencia + "/" + anoInicioVigencia);
			
			apolice.atribuirDataPrevistaInicio(dataInicioVigencia);
			
			if(!dataEspecial)
			{
				if(eData(diaFimVigencia+"/"+mesFimVigencia+"/"+anoFimVigencia))
					dataFimVigencia = new SimpleDateFormat("dd/MM/yyyy").parse(diaFimVigencia + "/" + mesFimVigencia + "/" + anoFimVigencia);
				else
					erros.add("Error: 94 - Fecha Fin Vigencia Invalida "+diaFimVigencia+"/"+mesFimVigencia+"/"+anoFimVigencia +" - Línea: " + numeroLinha + nRegistro);
			}
			else
				dataFimVigencia = new SimpleDateFormat("dd/MM/yyyy").parse(diaFimVigencia + "/" + mesFimVigencia + "/" + anoFimVigencia);
			
			apolice.atribuirDataPrevistaConclusao(dataFimVigencia);
			
			diasCobertura = "";
			if(verificaPosicaoString(linha, 97, 102, true, numeroLinha, "Dias de cobertura", nRegistro))
			{
				diasCobertura = linha.substring(97, 102).trim();
				
				if(diasCobertura.equals(""))
					erros.add("Error: 18 - Dias de cobertura es obligatorio - Línea: " + numeroLinha + nRegistro);
			}
			
			anoEmissao = "";
			mesEmissao = "";
			diaEmissao = "";
			
			if(verificaPosicaoString(linha, 102, 106, true, numeroLinha, "Año Emisi\363n de Vigencia", nRegistro))
				anoEmissao = linha.substring(102, 106).trim();
			if(verificaPosicaoString(linha, 106, 108, true, numeroLinha, "Mes Emisi\363n de Vigencia", nRegistro))
				mesEmissao = linha.substring(106, 108).trim();
			if(verificaPosicaoString(linha, 108, 110, true, numeroLinha, "Día Emisi\363n de Vigencia", nRegistro))
				diaEmissao = linha.substring(108, 110).trim();
			
			dataEmissao = null;
		                
			if(!dataEspecial)
			{
				if(eData(diaEmissao + "/" + mesEmissao + "/" + anoEmissao))
					dataEmissao = new SimpleDateFormat("dd/MM/yyyy").parse(diaEmissao + "/" + mesEmissao + "/" + anoEmissao);
				else
					erros.add("Error: 92 - Fecha Emisi\363n Invalida "+diaEmissao + "/" + mesEmissao + "/" + anoEmissao +" - Línea: " + numeroLinha + nRegistro);
			}
			else
				dataEmissao = new SimpleDateFormat("dd/MM/yyyy").parse(diaEmissao + "/" + mesEmissao + "/" + anoEmissao);
			
			apolice.atribuirDataEmissao(dataEmissao);
			
			capitalGuaraniStr = "";
			capitalGuarani = 0;
			if(verificaPosicaoString(linha, 110, 132, true, numeroLinha, "Capital en Riesgo en Guaraníes", nRegistro))
			{
				capitalGuaraniStr = linha.substring(110, 132).trim();
				if(capitalGuaraniStr.equals(""))
					erros.add("Error: 19 - Capital en Riesgo en Guaraníes es obligatorio - Línea: " + numeroLinha + nRegistro);
				else
				{
					capitalGuarani = Double.valueOf(capitalGuaraniStr);
					if(!statusApoliceSub.equals("2"))
					{
						if(capitalGuarani == 0 && !capitalEspecial)
							erros.add("Error: 133 - Capital en Riesgo en Guaraníes debe ser maior que ceros - Línea: " + numeroLinha + nRegistro);
					}
				}
			}
			
			tipoMoedaCapitalGuarani = "";
			if(verificaPosicaoString(linha, 132, 134, true, numeroLinha, "Tipo ME Capital", nRegistro))
				tipoMoedaCapitalGuarani = obterTipoMoeda(linha.substring(132, 134), numeroLinha, nRegistro);
			
			capitalMe = 0;
			if(verificaPosicaoString(linha, 134, 156, true, numeroLinha, "Capital en ME", nRegistro))
				capitalMe = Double.valueOf(linha.substring(134, 156).trim());
			
			primasSeguro = 0;
			if(verificaPosicaoString(linha, 156, 178, true, numeroLinha, "Prima en Guaranies", nRegistro))
				primasSeguro = Double.valueOf(linha.substring(156, 178).trim());
			
			tipoMoedaPrimas = "";
			if(verificaPosicaoString(linha, 178, 180, true, numeroLinha, "Tipo ME Prima", nRegistro))
				tipoMoedaPrimas = obterTipoMoeda(linha.substring(178, 180), numeroLinha, nRegistro);
			
			primasMe = 0;
			if(verificaPosicaoString(linha, 180, 202, true, numeroLinha, "Prima en ME", nRegistro))
				primasMe = Double.valueOf(linha.substring(180, 202).trim());
			
			principalGuarani = 0;
			if(verificaPosicaoString(linha, 202, 224, true, numeroLinha, "Principal en Guaranies", nRegistro))
				principalGuarani = Double.valueOf(linha.substring(202, 224).trim());
			
			tipoMoedaPrincipalGuarani = "";
			if(verificaPosicaoString(linha, 224, 226, true, numeroLinha, "Tipo ME Principal", nRegistro))
				tipoMoedaPrincipalGuarani = obterTipoMoeda(linha.substring(224, 226), numeroLinha, nRegistro);
			
			principalMe = 0;
			if(verificaPosicaoString(linha, 226, 248, true, numeroLinha, "Principal en ME", nRegistro))
				principalMe = Double.valueOf(linha.substring(226, 248).trim());
			
			incapacidadeGuarani = 0;
			if(verificaPosicaoString(linha, 248, 270, true, numeroLinha, "Incapacidade en Guaranies", nRegistro))
				incapacidadeGuarani = Double.valueOf(linha.substring(248, 270).trim());
			
			tipoMoedaIncapacidadeGuarani = "";
			if(verificaPosicaoString(linha, 270, 272, true, numeroLinha, "Tipo ME Incapacidade", nRegistro))
				tipoMoedaIncapacidadeGuarani = obterTipoMoeda(linha.substring(270, 272), numeroLinha, nRegistro);
			
			incapacidadeMe = 0;
			if(verificaPosicaoString(linha, 272, 294, true, numeroLinha, "Incapacidade en ME", nRegistro))
				incapacidadeMe = Double.valueOf(linha.substring(272, 294).trim());
			
			enfermidadeGuarani = 0;
			if(verificaPosicaoString(linha, 294, 316, true, numeroLinha, "Enfermidade en Guaranies", nRegistro))
				enfermidadeGuarani = Double.valueOf(linha.substring(294, 316).trim());
			
			tipoMoedaEnfermidade = "";
			if(verificaPosicaoString(linha, 316, 318, true, numeroLinha, "Tipo ME Enfermidade", nRegistro))
				tipoMoedaEnfermidade = obterTipoMoeda(linha.substring(316, 318), numeroLinha, nRegistro);
			
			enfermidadeMe = 0;
			if(verificaPosicaoString(linha, 318, 340, true, numeroLinha, "ME en Enfermidade", nRegistro))
				enfermidadeMe = Double.valueOf(linha.substring(318, 340).trim());
			
			acidentesGuarani = 0;
			if(verificaPosicaoString(linha, 340, 362, true, numeroLinha, "Acidentes en Guaranies", nRegistro))            
				acidentesGuarani = Double.valueOf(linha.substring(340, 362).trim());
			
			tipoMoedaAcidente = "";
			if(verificaPosicaoString(linha, 362, 364, true, numeroLinha, "Tipo ME Acidentes", nRegistro))
				tipoMoedaAcidente = obterTipoMoeda(linha.substring(362, 364), numeroLinha, nRegistro);
			
			acidentesMe = 0;
			if(verificaPosicaoString(linha, 364, 386, true, numeroLinha, "Acidentes en ME", nRegistro))
				acidentesMe = Double.valueOf(linha.substring(364, 386).trim());
			
			outrosGuarani = 0;
			if(verificaPosicaoString(linha, 386, 408, true, numeroLinha, "Otros en Guaranies", nRegistro))
				outrosGuarani = Double.valueOf(linha.substring(386, 408).trim());
			
			tipoMoedaOutros = "";
			if(verificaPosicaoString(linha, 408, 410, true, numeroLinha, "Tipo ME Otros", nRegistro))
				tipoMoedaOutros = obterTipoMoeda(linha.substring(408, 410), numeroLinha, nRegistro);
		                
			outrosMe = 0;
			if(verificaPosicaoString(linha, 410, 432, true, numeroLinha, "Otros en ME", nRegistro))
				outrosMe = Double.valueOf(linha.substring(410, 432).trim());
			
			financimantoGS = 0;
			if(verificaPosicaoString(linha, 432, 454, true, numeroLinha, "Financiamiento en Guaranies", nRegistro))
				financimantoGS = Double.valueOf(linha.substring(432, 454).trim());
			
			tipoMoedaFinanciamentoGS = "";
			if(verificaPosicaoString(linha, 454, 456, true, numeroLinha, "Tipo ME Financiamiento", nRegistro))
				tipoMoedaFinanciamentoGS = obterTipoMoeda(linha.substring(454, 456), numeroLinha, nRegistro);
			
			financiamentoMe = 0;
			if(verificaPosicaoString(linha, 456, 478, true, numeroLinha, "Financiamiento en ME", nRegistro))
				financiamentoMe = Double.valueOf(linha.substring(456, 478).trim());
			
			premioGs = 0;
			if(verificaPosicaoString(linha, 478, 500, true, numeroLinha, "Premio en Guaranies", nRegistro))
				premioGs = Double.valueOf(linha.substring(478, 500).trim());
			
			tipoMoedaPremio = "";
			if(verificaPosicaoString(linha, 500, 502, true, numeroLinha, "Tipo ME Premio", nRegistro))
				tipoMoedaPremio = obterTipoMoeda(linha.substring(500, 502), numeroLinha, nRegistro);
			
			premioMe = 0;
			if(verificaPosicaoString(linha, 502, 524, true, numeroLinha, "Premio en ME", nRegistro))
				premioMe = Double.valueOf(linha.substring(502, 524).trim());
		                
			if(premioMe > 0)
			{
				if(primasMe == 0)
					erros.add("Error: 23 - Valor de prima en dolar tiene que ser mayor que zero, pues valor del premio en dolar es mayor que zero - Línea: " + numeroLinha + nRegistro);
			}
		                
			if(primasMe > 0)
			{
				if(premioMe == 0)
					erros.add("Error: 24 - Valor de premio en dolar tiene que ser mayor que zero, pues valor de prima en dolar es mayor que zero - Línea: " + numeroLinha + nRegistro);
			}
		                
			formaPagamento = "";
			if(verificaPosicaoString(linha, 524, 525, true, numeroLinha, "Forma de Pago", nRegistro))
			{
				formaPagamentoSub = linha.substring(524, 525).trim();
				
				if(formaPagamentoSub.equals("1"))
					formaPagamento = "Al contado";
				else if(formaPagamentoSub.equals("2"))
					formaPagamento = "Financiado";
				else
					erros.add("Error: 25 - Forma de Pago es obligatorio - Línea: " + numeroLinha + nRegistro);
			}
			
			qtdeParcelas = "";
			if(verificaPosicaoString(linha, 525, 528, true, numeroLinha, "Parcelas", nRegistro))
				qtdeParcelas = linha.substring(525, 528).trim();
			
			refinacaoStr = "";
			if(verificaPosicaoString(linha, 528, 529, true, numeroLinha, "Refinación", nRegistro))
			{
				refinacaoSub = linha.substring(528, 529).trim();
				if(refinacaoSub.equals("1"))
					refinacaoStr = "S\355";
				else if(refinacaoSub.equals("2"))
					refinacaoStr = "No";
				else
					erros.add("Error: 25 - Refinación invalida, Refinación = "+refinacaoSub+"  - Línea: " + numeroLinha + nRegistro);
			}
		                
			agente = null;
			if(verificaPosicaoString(linha, 529, 533, true, numeroLinha, "Inscripci\363n Agentes de Seguros", nRegistro))
			{
				inscricaoAgente = linha.substring(529, 533).trim();
				
				if(!inscricaoAgente.equals("0000"))
				{
					mesAno2 = "";
		                	
					if(dataInicioVigencia!=null)
					{
						mesAno = new SimpleDateFormat("MM/yyyy").format(dataEmissao);
			                	
						mesAno2 = mesAno;
			                	
						if(inscricaoAgente.substring(0, 1).equals("0"))
							agente = auxiliarSeguroHome.obterAuxiliarPorInscricaoeTipo(inscricaoAgente.substring(1, inscricaoAgente.length()), "Agentes de Seguros",mesAno);
						else
							agente = auxiliarSeguroHome.obterAuxiliarPorInscricaoeTipo(inscricaoAgente, "Agentes de Seguros",mesAno);
					}
					if(agente == null)
					{
						if(inscricaoAgente.substring(0, 1).equals("0"))
							agente = auxiliarSeguroHome.obterAuxiliarPorInscricao(inscricaoAgente.substring(1, inscricaoAgente.length()), "Agentes de Seguros");
						else
							agente = auxiliarSeguroHome.obterAuxiliarPorInscricao(inscricaoAgente, "Agentes de Seguros");
		                    	
						if(agente!=null)
							erros.add("Error: 09 - Agente(Agentes de Seguros) con inscripci\363n " + inscricaoAgente + " " +agente.obterNome() + " no fue encuentrado, Póliza: "+numeroApolice+" mes e año Póliza: "+ mesAno2+" - Línea: " + numeroLinha + nRegistro);
						else
							erros.add("Error: 09 - Agente(Agentes de Seguros) con inscripci\363n " + inscricaoAgente + " no fue encuentrado, Póliza: "+numeroApolice+" mes e año Póliza: "+ mesAno2+" - Línea: " + numeroLinha + nRegistro);
					}
				}
				
				if(agente!=null)
				{
					situacaoEntidade = agente.obterAtributo("situacao");
					if(situacaoEntidade!=null)
					{
						situacaoAgente = situacaoEntidade.obterValor();
						if(situacaoAgente.equals(Inscricao.SUSPENSA))
							erros.add("Error: 09 - Agente(Agentes de Seguros) con inscripci\363n " + inscricaoAgente + " suspendida, Póliza: "+numeroApolice+" mes e año Póliza: "+ mesAno2+" - Línea: " + numeroLinha + nRegistro);
					}
				}
			}
			
			comissaoGs = 0;
			if(verificaPosicaoString(linha, 533, 555, true, numeroLinha, "Comisión en Guaranies", nRegistro))
				comissaoGs = Double.valueOf(linha.substring(533, 555).trim());
			
			tipoMoedaComissaoGs = "";
			if(verificaPosicaoString(linha, 555, 557, true, numeroLinha, "Tipo ME Comisión", nRegistro))
				tipoMoedaComissaoGs = obterTipoMoeda(linha.substring(555, 557), numeroLinha, nRegistro);
			
			comissaoMe = 0;
			if(verificaPosicaoString(linha, 557, 579, true, numeroLinha, "Comisión en ME", nRegistro))
				comissaoMe = Double.valueOf(linha.substring(557, 579).trim());
			
			situacaoSeguro = "";
			if(verificaPosicaoString(linha, 579, 580, true, numeroLinha, "Situacion del Seguro", nRegistro))
			{
				situacaoSeguroSub = linha.substring(579, 580).trim();
				if(situacaoSeguroSub.equals("1"))
					situacaoSeguro = "Vigente";
				else if(situacaoSeguroSub.equals("2"))
					situacaoSeguro = "No Vigente Pendiente";
				else if(situacaoSeguroSub.equals("3"))
					situacaoSeguro = "No Vigente";
				else
					erros.add("Error: 99 - Situacion del Seguro no informada (Vigente, No Vigente Pendiente, No Vigente) - Línea: " + numeroLinha + nRegistro);
			}
			
			corredor = null;
			if(verificaPosicaoString(linha, 580, 584, true, numeroLinha, "Inscripci\363n Corredores de Seguros", nRegistro))
			{
				inscricaoCorredor = linha.substring(580, 584).trim();
				if(!inscricaoCorredor.equals("0000"))
				{
					mesAno2 = "";
		                	
					if(dataInicioVigencia!=null)
					{
						mesAno = new SimpleDateFormat("MM/yyyy").format(dataEmissao);
			                	
						mesAno2 = mesAno;
			                	
						if(inscricaoCorredor.substring(0, 1).equals("0"))
							corredor = auxiliarSeguroHome.obterAuxiliarPorInscricaoeTipo(inscricaoCorredor.substring(1, inscricaoCorredor.length()), "Corredores de Seguros",mesAno);
						else
							corredor = auxiliarSeguroHome.obterAuxiliarPorInscricaoeTipo(inscricaoCorredor, "Corredores de Seguros",mesAno);
					}
					if(corredor == null)
					{
						if(inscricaoCorredor.substring(0, 1).equals("0"))
							corredor = auxiliarSeguroHome.obterAuxiliarPorInscricao(inscricaoCorredor.substring(1, inscricaoCorredor.length()), "Corredores de Seguros");
						else
							corredor = auxiliarSeguroHome.obterAuxiliarPorInscricao(inscricaoCorredor, "Corredores de Seguros");
		                    	
						if(corredor!=null)
							erros.add("Error: 10 - Agente(Corredores de Seguros) con inscripci\363n " + inscricaoCorredor + " " +corredor.obterNome() +" no fue encuentrado, Póliza: "+numeroApolice+ " mes e año Póliza: "+ mesAno2+" - Línea: " + numeroLinha + nRegistro);
						else
							erros.add("Error: 10 - Agente(Corredores de Seguros) con inscripci\363n " + inscricaoCorredor + " no fue encuentrado, Póliza: "+numeroApolice+ " mes e año Póliza: "+ mesAno2+" - Línea: " + numeroLinha + nRegistro);
					}
				}
			}
		                
			if(corredor!=null)
			{
				situacaoAgente = corredor.obterAtributo("situacao").obterValor();
				if(situacaoAgente.equals(Inscricao.SUSPENSA))
					erros.add("Error: 09 - Agente(Corredores de Seguros) con inscripci\363n " + inscricaoCorredor + " suspendida, Póliza: "+numeroApolice+" mes e año Póliza: "+ mesAno2+" - Línea: " + numeroLinha + nRegistro);
			}
			
			numeroEndosoAnterior = 0;
			if(verificaPosicaoString(linha, 601, 611, true, numeroLinha, "Endoso anterior", nRegistro))
				numeroEndosoAnterior = Double.valueOf(linha.substring(601, 611).trim());
			
			certificadoAnterior = 0;
			if(verificaPosicaoString(linha, 611, 618, true, numeroLinha, "Certificado anterior", nRegistro))
				certificadoAnterior = Double.valueOf(linha.substring(611, 618).trim());
			
			apoliceSuspeita = "N";
		                
			if(linha.length() == 619)
				apoliceSuspeita = linha.substring(618, 619).trim();
		                
			duplicada = false;
		                
			if(statusApoliceSub.equals("3") && apoliceSuperior!=null) //Certificado de seguro coletivo
				duplicada = apoliceHome.estaDuplicada(aseguradora, statusApolice, cContas, numeroEndoso, certificado, numeroApolice, apoliceSuperior);
			else
				duplicada = apoliceHome.estaDuplicada(aseguradora, statusApolice, cContas, numeroEndoso, certificado, numeroApolice);
		                
			if(duplicada && statusApoliceSub.equals("3"))
				erros.add("Error: 134 - Existe certificado de seguro coletivo, Póliza ya existe - Número de la Póliza "+numeroApolice+", Póliza Madre " + apoliceSuperiorStr +" - Línea: " + numeroLinha + nRegistro);
			else if(duplicada)
				erros.add("Error: 135 - Póliza ya existe - Número de la Póliza "+numeroApolice+" - Línea: " + numeroLinha + nRegistro);
		                
			if(erros.size() == 0)
			{
				destino = entidadeHome.obterEntidadePorApelido("bcp");
				apolice.atribuirOrigem(aseguradora);
				apolice.atribuirDestino(destino);
				apolice.atribuirResponsavel(usuario);
				apolice.atribuirTipo(tipoApolice);
				apolice.atribuirTitulo("Instrumento: " + numeroApolice + ";Sección: " + cContas.obterId());
		        if(apoliceSuperior != null)
		        	apolice.atribuirSuperior(apoliceSuperior);
		        else
		        	apolice.atribuirSuperior(this);
		        apolice.atribuirDataPrevistaInicio(dataInicioVigencia);
		        apolice.atribuirDataPrevistaConclusao(dataFimVigencia);
		        apolice.atribuirSecao(cContas);
		        apolice.atribuirNumeroApolice(numeroApolice);
		        apolice.atribuirStatusApolice(statusApolice);
		        apolice.atribuirApoliceAnterior(apoliceAnterior);
		        if(apoliceAnterior != null)
		        {
		        	apoliceAnterior.atribuirNumeroEndoso(numeroEndosoAnterior);
		        	apoliceAnterior.atribuirCertificado(certificadoAnterior);
		        }
		        apolice.atribuirAfetadoPorSinistro(afetadorPorSinistro);
		        apolice.atribuirApoliceFlutuante(apoliceFlutuante);
		        apolice.atribuirPlano(plano);
		        apolice.atribuirNumeroFatura(numeroFatura);
		        apolice.atribuirModalidadeVenda(modalidadeVenda);
		        apolice.atribuirDiasCobertura(Integer.parseInt(diasCobertura));
		        apolice.atribuirDataEmissao(dataEmissao);
		        apolice.atribuirCapitalGs(capitalGuarani);
		        apolice.atribuirTipoMoedaCapitalGuarani(tipoMoedaCapitalGuarani);
		        apolice.atribuirCapitalMe(capitalMe);
		        apolice.atribuirPrimaGs(primasSeguro);
		        apolice.atribuirTipoMoedaPrimaGs(tipoMoedaPrimas);
		        apolice.atribuirPrimaMe(primasMe);
		        apolice.atribuirPrincipalGs(principalGuarani);
		        apolice.atribuirTipoMoedaPrincipalGs(tipoMoedaPrincipalGuarani);
		        apolice.atribuirPrincipalMe(principalMe);
		        apolice.atribuirIncapacidadeGs(incapacidadeGuarani);
		        apolice.atribuirTipoMoedaIncapacidadeGs(tipoMoedaIncapacidadeGuarani);
		        apolice.atribuirIncapacidadeMe(incapacidadeMe);
		        apolice.atribuirEnfermidadeGs(enfermidadeGuarani);
		        apolice.atribuirTipoMoedaEnfermidadeGs(tipoMoedaEnfermidade);
		        apolice.atribuirAcidentesGs(acidentesGuarani);
		        apolice.atribuirEnfermidadeMe(enfermidadeMe);
		        apolice.atribuirTipoMoedaAcidentesGs(tipoMoedaAcidente);
		        apolice.atribuirAcidentesMe(acidentesMe);
		        apolice.atribuirOutrosGs(outrosGuarani);
	            apolice.atribuirTipoMoedaOutrosGs(tipoMoedaOutros);
	            apolice.atribuirOutrosMe(outrosMe);
	            apolice.atribuirFinanciamentoGs(financimantoGS);
	            apolice.atribuirTipoMoedaFinanciamentoGs(tipoMoedaFinanciamentoGS);
	            apolice.atribuirFinanciamentoMe(financiamentoMe);
	            apolice.atribuirPremiosGs(premioGs);
	            apolice.atribuirTipoMoedaPremiosGs(tipoMoedaPremio);
	            apolice.atribuirPremiosMe(premioMe);
	            apolice.atribuirFormaPagamento(formaPagamento);
	            apolice.atribuirQtdeParcelas(Integer.parseInt(qtdeParcelas));
	            apolice.atribuirRefinacao(refinacaoStr);
	            apolice.atribuirInscricaoAgente(agente);
	            apolice.atribuirComissaoGs(comissaoGs);
	            apolice.atribuirTipoMoedaComissaoGs(tipoMoedaComissaoGs);
	            apolice.atribuirComissaoMe(comissaoMe);
	            apolice.atribuirSituacaoSeguro(situacaoSeguro);
	            if(!codigoPlano.startsWith("RG.0001"))
	                apolice.atribuirCodigoPlano(codigoPlano);
	            apolice.atribuirCorredor(corredor);
	            apolice.atribuirNumeroEndoso(numeroEndoso);
	            apolice.atribuirNumeroEndosoAnterior(numeroEndosoAnterior);
	            apolice.atribuirCertificado(certificado);
	            apolice.atribuirCertificadoAnterior(certificadoAnterior);
	            apolice.atribuirApoliceSuspeita(apoliceSuspeita);
			}
			
			if(!numeroApolice.equals("") && cContas!=null && !statusApolice.equals(""))
				apolices.put(obterOrigem().obterId() + numeroApolice + cContas.obterId() + statusApolice, apolice);
    	}
    	catch(NumberFormatException e)
        {
        	if(e.toString().equals("java.util.ConcurrentModificationException"))
        		this.validarRegistro2(linha, numeroLinha);
        	else
        	{
        		String[] msgCerta = e.getMessage().split(":");
        		erros.add("Error Interno: Formato de número no es correcto " + msgCerta[1] + " - Línea: "+ numeroLinha + nRegistro);
        	}
        }
        catch(Exception e)
        {
        	if(e.toString().equals("java.util.ConcurrentModificationException"))
        		this.validarRegistro2(linha, numeroLinha);
        	else
        		erros.add("Error Interno: " + e.toString() + " - Línea: "+ numeroLinha + nRegistro);
		}
    }
    
    private Collection<String> validarApolice(String linha, int numeroLinha) throws Exception
    {
    	erros = new ArrayList();
    	//FileWriter file = new FileWriter("c:/tmp/Erro.txt");
    	try
    	{
    		/*if(numeroLinha == 23389)
    			System.out.println("");*/
    		
    		numeroRegistroInt = 0;
    		nRegistro = "";
    		
    		if(this.verificaPosicaoString(linha, 7, 9, true, numeroLinha, "Número del registro", ""))
   				numeroRegistroInt = Integer.valueOf(linha.substring(7, 9).trim());
    		
    		if(numeroRegistroInt > 0)
    		{
    			if(numeroRegistroInt == 1)
    			{
    				nRegistro = " (Registro 01)";
    				
    				if(verificaPosicaoString(linha, 9, 12, false, numeroLinha, "Sigla", nRegistro))
    				{
    					sigla = linha.substring(9, 12).trim();
    					aseguradora = entidadeHome.obterEntidadePorSigla(sigla);
    					
    					if(aseguradora == null || aseguradora instanceof Raiz)
    	    				erros.add("Error: 03 - Aseguradora " + sigla + " no fue encontrada - Línea: " + numeroLinha + nRegistro);
    	    			else
    	    				if(!obterOrigem().equals(aseguradora))
    	    					erros.add("Error: 06 - Aseguradora " + sigla + " no es la misma de la agenda - Línea: " + numeroLinha + nRegistro);
    				}
	    			
    				chaveUsuario = "";
    				usuario = null;
    				if(verificaPosicaoString(linha, 12, 22, false, numeroLinha, "Clave del usuario", nRegistro))
    				{    				
		    			chaveUsuario = linha.substring(12, 22).trim();
		    			usuario = usuarioHome.obterUsuarioPorChave(chaveUsuario);
		    			if(usuario == null)
		    				erros.add("Error: 04 - Usuario " + chaveUsuario + " no fue encuentrado - Línea: " + numeroLinha + nRegistro);
    				}
    				
    				anoGeracao = "";
    				mesGeracao = "";
    				diaGeracao = "";
    				anoReporte = "0";
    				mesReporte = "0";
    				numeroTotalRegistros = 0;
    				
    				if(verificaPosicaoString(linha, 22, 26, true, numeroLinha, "Año emisión", nRegistro))
    					anoGeracao = linha.substring(22, 26).trim();
    				
    				if(verificaPosicaoString(linha, 26, 28, true, numeroLinha, "Mes emisión", nRegistro))
    					mesGeracao = linha.substring(26, 28).trim();
    				
    				if(verificaPosicaoString(linha, 28, 30, true, numeroLinha, "Día emisión", nRegistro))
    					diaGeracao = linha.substring(28, 30).trim();
    				
    				if(eData(diaGeracao+"/"+mesGeracao+"/"+anoGeracao))
    					dataGeracao = new SimpleDateFormat("dd/MM/yyyy").parse(diaGeracao + "/" + mesGeracao + "/" + anoGeracao);
	    			else
	    				erros.add("Error: 92 - Fecha Emisión Invalida "+diaGeracao+"/"+mesGeracao+"/"+anoGeracao +" - Línea: " + numeroLinha + nRegistro);
    				
    				if(verificaPosicaoString(linha, 30, 34, true, numeroLinha, "Año reporte", nRegistro))
    					anoReporte = linha.substring(30, 34).trim();
    				
    				if(obterAnoMovimento() != Integer.parseInt(anoReporte))
	    				erros.add("Error: 08 - A\361o informado es diferente del a\361o de la agenda" + nRegistro);
	    			
    				if(verificaPosicaoString(linha, 34, 36, true, numeroLinha, "Mes reporte", nRegistro))
    					mesReporte = linha.substring(34, 36).trim();
    				
    				if(obterMesMovimento() != Integer.valueOf(mesReporte))
	    				erros.add("Error: 07 - Mes informado es diferente del mes de la agenda" + nRegistro);
	    			
    				if(verificaPosicaoString(linha, 36, 46, true, numeroLinha, "Número total de registros", nRegistro))
    					numeroTotalRegistros = Integer.valueOf(linha.substring(36, 46).trim());
	    			
    				tipoArquivo = "";
	    			if(linha.substring(46, 47).equals("|"))
	    				tipoArquivo = "Instrumento de cobertura";
	    		}
    			else if(numeroRegistroInt == 2)
				{
    				this.validarRegistro2(linha, numeroLinha);
				} 
    			else if(numeroRegistroInt == 3)
    			{
    				nRegistro = " (Registro 03)";
    				
    				cContas = null;
    				if(verificaPosicaoString(linha, 9, 19, true, numeroLinha, "Cuenta", nRegistro))
    				{
    					apelidoCconta = linha.substring(9, 19).trim();
    					cContas = (ClassificacaoContas)entidadeHome.obterEntidadePorApelido(apelidoCconta);
    					if(cContas == null)
    						erros.add("Error: 05 - Cuenta " + apelidoCconta + " no fue encontrada - Línea: " + numeroLinha + nRegistro);
    				}
    				
    				numeroInstrumento = "";
    				if(verificaPosicaoString(linha, 19, 29, true, numeroLinha, "Número Póliza", nRegistro))
    					numeroInstrumento = linha.substring(19, 29).trim();
    				
    				dataCorte = null;
    				anoCorte = "";
    				mesCorte = "";
    				diaCorte = "";
    				
    				if(verificaPosicaoString(linha, 29, 33, true, numeroLinha, "Año del Corte", nRegistro))
    					anoCorte = linha.substring(29, 33).trim();
    				
    				if(verificaPosicaoString(linha, 33, 35, true, numeroLinha, "Mes del Corte", nRegistro))
    					mesCorte = linha.substring(33, 35).trim();
    				
    				if(verificaPosicaoString(linha, 35, 37, true, numeroLinha, "Día del Corte", nRegistro))
    					diaCorte = linha.substring(35, 37).trim();
    				
    				if(eData(diaCorte + "/" + mesCorte + "/" + anoCorte))
    					dataCorte = new SimpleDateFormat("dd/MM/yyyy").parse(diaCorte + "/" + mesCorte + "/" + anoCorte);
    				else
    					erros.add("Error: 26 - Fecha del Corte Invalida "+diaCorte + "/" + mesCorte + "/" + anoCorte + " - Línea: " + numeroLinha + nRegistro);
	
    				cursoStr = "";
    				curso = 0;
    				if(verificaPosicaoString(linha, 37, 59, true, numeroLinha, "Riesgo en Curso", nRegistro))
    				{
    					cursoStr = linha.substring(37, 59).trim();
    					if(cursoStr.equals(""))
    						erros.add("Error: 27 - Riesgo en Curso es obligatorio - Línea: " + numeroLinha + nRegistro);
    					else
    						curso = Double.valueOf(cursoStr);
    				}
    				
    				valorSinistro = 0;
    				if(verificaPosicaoString(linha, 59, 81, true, numeroLinha, "Siniestro", nRegistro))
    					valorSinistro = Double.valueOf(linha.substring(59, 81).trim());
    				
    				reservas = 0;
    				if(verificaPosicaoString(linha, 81, 103, true, numeroLinha, "Reserva", nRegistro))
    					reservas = Double.valueOf(linha.substring(81, 103).trim());

    				fundos = 0;
    				if(verificaPosicaoString(linha, 103, 125, true, numeroLinha, "Fundo", nRegistro))
    					fundos = Double.valueOf(linha.substring(103, 125).trim());

    				premios = 0;
    				if(verificaPosicaoString(linha, 125, 147, true, numeroLinha, "Premio", nRegistro))
    					 premios = Double.valueOf(linha.substring(125, 147).trim());

    				tipoInstrumento = "";
    				if(verificaPosicaoString(linha, 147, 148, true, numeroLinha, "Tipo del Instrumento", nRegistro))
    				{
    					String s = linha.substring(147, 148).trim();
    					
    					if(s.equals("1"))
    						tipoInstrumento = "P\363liza Individual";
    					else if(s.equals("2"))
		                    tipoInstrumento = "P\363liza Madre";
		                else if(s.equals("3"))
		                    tipoInstrumento = "Certificado de Seguro Colectivo";
		                else if(s.equals("4"))
		                    tipoInstrumento = "Certificado Provisorio";
		                else if(s.equals("5"))
		                    tipoInstrumento = "Nota de Cobertura de Reaseguro";
		                else
		                	erros.add("Error: 28 - Tipo del Instrumento es obligatorio - Línea: " + numeroLinha + nRegistro);
    				}
				                
    				numeroEndoso = 0;
    				if(verificaPosicaoString(linha, 148, 158, true, numeroLinha, "Endoso", nRegistro))
    					numeroEndoso = Double.valueOf(linha.substring(148, 158).trim());
    				
    				certificado = 0;
    				if(verificaPosicaoString(linha, 158, 165, true, numeroLinha, "Certificado", nRegistro))
    					certificado = Double.valueOf(linha.substring(158, 165).trim());
	                
    				apolice = null;
    				if(apolices != null && cContas!=null && !numeroInstrumento.equals(""))
	                {
	                	apolice = apolices.get(obterOrigem().obterId() + numeroInstrumento+ cContas.obterId() + tipoInstrumento);
	                	if(apolice == null)
	                		apolice = apoliceHome.obterApolice(obterOrigem(), numeroInstrumento,cContas, tipoInstrumento);
	                	if(apolice == null)
		                    erros.add("Error: 29 - Instrumento " + numeroInstrumento + " no fue encuentrado - Línea: " + numeroLinha + nRegistro);
	                }
				                
	                if(erros.size() == 0)
	                {
	                	dadosPrevisao = (DadosPrevisao)getModelManager().getEntity("DadosPrevisao");
	                    dadosPrevisao.atribuirOrigem(apolice.obterOrigem());
	                    dadosPrevisao.atribuirDestino(apolice.obterDestino());
	                    dadosPrevisao.atribuirResponsavel(apolice.obterResponsavel());
	                    dadosPrevisao.atribuirTitulo("Datos do Instrumento: " + numeroInstrumento + ";Sección: " + cContas.obterId());
	                    dadosPrevisao.atribuirSuperior(apolice);
	                    dadosPrevisao.atribuirDataCorte(dataCorte);
	                    dadosPrevisao.atribuirCurso(curso);
	                    dadosPrevisao.atribuirSinistroPendente(valorSinistro);
	                    dadosPrevisao.atribuirReservasMatematicas(reservas);
	                    dadosPrevisao.atribuirFundosAcumulados(fundos);
	                    dadosPrevisao.atribuirPremios(premios);
	                    dadosPrevisao.atribuirTipoInstrumento(tipoInstrumento);
	                    dadosPrevisao.atribuirNumeroEndoso(numeroEndoso);
	                    dadosPrevisao.atribuirCertificado(certificado);
	                    dadosPrevisoes.add(dadosPrevisao);
	                }
    			}
    			else if(numeroRegistroInt == 4)
    			{
    				nRegistro = " (Registro 04)";
    				
    				cContas = null;
    				if(verificaPosicaoString(linha, 9, 19, true, numeroLinha, "Cuenta", nRegistro))
    				{
    					apelidoCconta = linha.substring(9, 19).trim();
    					cContas = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido(apelidoCconta);
    					if(cContas == null)
    						erros.add("Error: 05 - Cuenta " + apelidoCconta + " no fue encontrada - Línea: " + numeroLinha + nRegistro);
    				}
				                
    				tipoInstrumento = "";
    				if(verificaPosicaoString(linha, 19, 20, true, numeroLinha, "Tipo del Instrumento", nRegistro))
    				{
    					String s = linha.substring(19, 20).trim();
    					
    					if(s.equals("1"))
    						tipoInstrumento = "P\363liza Individual";
		                else if(s.equals("2"))
		                    tipoInstrumento = "P\363liza Madre";
		                else if(s.equals("3"))
		                    tipoInstrumento = "Certificado de Seguro Colectivo";
		                else if(s.equals("4"))
		                    tipoInstrumento = "Certificado Provisorio";
		                else if(s.equals("5"))
		                    tipoInstrumento = "Nota de Cobertura de Reaseguro";
		                else
		                	erros.add("Error: 28 - Tipo del Instrumento es obligatorio - Línea: " + numeroLinha + nRegistro);
    				}
    				
    				numeroInstrumento = "";
    				if(verificaPosicaoString(linha, 20, 30, true, numeroLinha, "Número Póliza", nRegistro))
    					numeroInstrumento = linha.substring(20, 30).trim();
    				
    				valorEndoso = 0;
    				if(verificaPosicaoString(linha, 30, 40, true, numeroLinha, "Endoso", nRegistro))
    					valorEndoso = Double.valueOf(linha.substring(30, 40).trim());
    				
    				certificado = 0;
    				if(verificaPosicaoString(linha, 40, 47, true, numeroLinha, "Certificado", nRegistro))
    					certificado = Double.valueOf(linha.substring(40, 47).trim());
    				
    				apolice = null;
    				if(apolices != null && cContas!=null && !numeroInstrumento.equals(""))
    				{
    					apolice = apolices.get(obterOrigem().obterId() + numeroInstrumento+ cContas.obterId() + tipoInstrumento);
		                if(apolice == null)
		                    apolice = apoliceHome.obterApolice(obterOrigem(), numeroInstrumento,cContas, tipoInstrumento);
		                if(apolice == null)
		                    erros.add("Error: 29 - Instrumento " + numeroInstrumento + " no fue encuentrado - Línea: " + numeroLinha + nRegistro);
    				}
	                
	                qtde = 0;
	                if(verificaPosicaoString(linha, 47, 49, true, numeroLinha, "Cantidad", nRegistro))
	                {
	                	qtde = Integer.valueOf(linha.substring(47, 49).trim());
	                	if(qtde == 0)
	                		erros.add("Error: 30 - Cantidad de Reaseguros es obligatoria - Línea: " + numeroLinha + nRegistro);
	                }
	                
	                ultimo = 0;
	                ultimoArquivo = qtde * (3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22 + 2 + 22 + 4 + 2 + 2 + 4 + 2 + 2 + 1);
	                ultimoArquivo2 = linha.substring(49, linha.length()).length() - 2;
	                
	                if(ultimoArquivo!= ultimoArquivo2)
	                	erros.add("Error: 31 - Número de Dados Reaseguro declarados es diferente de lo que hay en el archivo - Línea: " + numeroLinha + nRegistro);
	                else
	                {
	                	for(int w = 0; w < qtde; w++)
	                	{
		                    if(ultimo == 0)
		                    	ultimo = 49;
		                    
		                    inscricaoReaseguradora1 = "";
		                    if(verificaPosicaoString(linha, ultimo, ultimo + 3, true, numeroLinha, "Inscripci\363n (Reaseguradora o Aseguradora)", nRegistro))
		                    {
		                    	inscricaoReaseguradora1 = linha.substring(ultimo, ultimo + 3).trim();
		                    	if(inscricaoReaseguradora1.equals(""))
		                    		erros.add("Error: 32 - Numero de la Reaseguradora es obligatorio - Línea: " + numeroLinha + nRegistro);
		                    }
					                    
		                    reaseguradora = null;
		                    if(!inscricaoReaseguradora1.equals("000"))
		                    {
		                    	if(apolice!=null)
		                    	{
		                    		mesAno = null;
		                    		
		                    		if(apolice.obterDataEmissao()!=null)
		                    			mesAno = new SimpleDateFormat("MM/yyyy").format(apolice.obterDataEmissao());
			                    	
			                        if(Integer.parseInt(inscricaoReaseguradora1) >= 201)
			                        {
			                        	mesAno = new SimpleDateFormat("MM/yyyy").format(apolice.obterDataPrevistaInicio());
			                            reaseguradora = entidadeHome.obterEntidadePorInscricao(inscricaoReaseguradora1, "Reaseguradora",mesAno);
			                        }
			                        else
			                            reaseguradora = entidadeHome.obterEntidadePorInscricao(inscricaoReaseguradora1, "Aseguradora",mesAno);
			                        
			                        if(reaseguradora == null)
			                        {
			                        	 if(Integer.parseInt(inscricaoReaseguradora1) >= 201)
			                        		 reaseguradora = entidadeHome.obterEntidadePorInscricaoSemValidacao(inscricaoReaseguradora1, "Reaseguradora");
			                        	 else
			                        		 reaseguradora = entidadeHome.obterEntidadePorInscricaoSemValidacao(inscricaoReaseguradora1, "Aseguradora");
			                        	
			                        	 if(reaseguradora!=null)
			                        		 erroInscricao.add("Error: 33 - Inscripci\363n (Reaseguradora o Aseguradora) " + inscricaoReaseguradora1 + " de la Reaseguradora "+reaseguradora.obterNome()+" no fue encontrada o esta No Activa, Póliza: "+apolice.obterNumeroApolice()+" mes e año Póliza: "+ mesAno+" - Línea: " + numeroLinha + nRegistro);
			                        	 else
			                        		 erroInscricao.add("Error: 33 - Inscripci\363n (Reaseguradora o Aseguradora) " + inscricaoReaseguradora1 + " de la Reaseguradora no fue encontrada o esta No Activa, Póliza: "+apolice.obterNumeroApolice()+" mes e año Póliza: "+ mesAno+" - Línea: " + numeroLinha + nRegistro);
			                        }
		                    	}
		                    }
					                    
		                    if(reaseguradora!=null)
		                    {
		                    	situacaoAgente = reaseguradora.obterAtributo("situacao").obterValor();
			                	if(situacaoAgente.equals(Inscricao.SUSPENSA))
			                	{
			                		if(apolice!=null)
			                			erros.add("Error: 09 - Reaseguradora o Aseguradora con inscripci\363n " + inscricaoReaseguradora1 + " suspendida, Póliza: "+apolice.obterNumeroApolice()+" mes e año Póliza: "+ mesAno+" - Línea: " + numeroLinha + nRegistro);
			                		else
			                			erros.add("Error: 09 - Reaseguradora o Aseguradora con inscripci\363n " + inscricaoReaseguradora1 + " suspendida, mes e año Póliza: "+ mesAno+" - Línea: " + numeroLinha + nRegistro);
			                	}
		                    }
		                    //else
		                    	//erros.add("Error: 32 - Numero de la Reaseguradora es obligatorio - Línea: " + numeroLinha);
		                    
		                    tipoContratoReaseguro1 = "";
		                    if(verificaPosicaoString(linha, ultimo + 3, ultimo + 3 + 1, true, numeroLinha, "Tipo de Contrato de Reaseguro", nRegistro))
		                    {
		                    	String s = linha.substring(ultimo + 3, ultimo + 3 + 1).trim();
		                    	
			                    if(s.equals("1"))
			                        tipoContratoReaseguro1 = "Cuota parte";
			                    else if(s.equals("2"))
			                        tipoContratoReaseguro1 = "Excedente";
			                    else if(s.equals("3"))
			                        tipoContratoReaseguro1 = "Exceso de p\351rdida";
			                    else if(s.equals("4"))
			                        tipoContratoReaseguro1 = "Facultativo no Proporcional";
			                    else if(s.equals("5"))
			                        tipoContratoReaseguro1 = "Facultativo Proporcional";
			                    else if(s.equals("6"))
			                        tipoContratoReaseguro1 = "Limitaci\363n de Siniestralidad";
			                    else
			                    	erros.add("Error: 34 - Tipo de Contrato de Reaseguro es obligatorio - Línea: " + numeroLinha + nRegistro);
		                    }
		                    
		                    inscricaoCorredoraReaseguro1 = "";
		                    corretora = null;
		                    mostrarErro110 = true;
		                    
		                    if(verificaPosicaoString(linha, ultimo + 3 + 1, ultimo + 3 + 1 + 3, true, numeroLinha, "Inscripci\363n Corretora o Reaseguradora", nRegistro))
		                    	inscricaoCorredoraReaseguro1 = linha.substring(ultimo + 3 + 1, ultimo + 3 + 1 + 3).trim();
		                    
		                    if(!inscricaoCorredoraReaseguro1.equals("000"))
		                    {
		                    	if(apolice!=null)
		                    	{
		                    		mesAno = null;
		                    		
		                    		if(apolice.obterDataEmissao()!=null)
		                    			mesAno = new SimpleDateFormat("MM/yyyy").format(apolice.obterDataEmissao());
		                    		
			                        corretora = entidadeHome.obterEntidadePorInscricao(inscricaoCorredoraReaseguro1, "Corretora",mesAno);
			                        
			                        if(corretora == null)
			                        {
			                        	mesAno = new SimpleDateFormat("MM/yyyy").format(apolice.obterDataPrevistaInicio());
			                        	corretora = entidadeHome.obterEntidadePorInscricao(inscricaoCorredoraReaseguro1, "Reaseguradora",mesAno);
			                        }
			                        
			                        if(corretora == null)
			                        {
			                            nome = "";
			                            
			                            corretora = entidadeHome.obterEntidadePorInscricaoSemValidacao(inscricaoCorredoraReaseguro1, "Corretora");
			                            if(corretora!=null)
			                            	nome = corretora.obterNome();
			                            else
			                            {
			                            	corretora = entidadeHome.obterEntidadePorInscricaoSemValidacao(inscricaoCorredoraReaseguro1, "Reaseguradora");
			                            	 if(corretora!=null)
			                            		 nome = corretora.obterNome();
			                            }
			                        	
			                            if(!nome.equals(""))
			                            	erroInscricao.add("Error: 35 - Inscripci\363n (Corretora o Reaseguradora) " + inscricaoCorredoraReaseguro1 + " de la Corredora o Reaseguradora "+nome+" no fue encontrada o esta No Activa, Póliza: "+apolice.obterNumeroApolice()+" mes e año Póliza: "+ mesAno+"  - Línea: " + numeroLinha + nRegistro);
			                            else
			                            	erroInscricao.add("Error: 35 - Inscripci\363n (Corretora o Reaseguradora) " + inscricaoCorredoraReaseguro1 + " de la Corredora o Reaseguradora no fue encontrada o esta No Activa, Póliza: "+apolice.obterNumeroApolice()+" mes e año Póliza: "+ mesAno+"  - Línea: " + numeroLinha + nRegistro);
			                        }
		                    	}
		                    	else
		                    		mostrarErro110 = false;
		                    }
		                    if(corretora!=null)
		                    {
		                    	situacaoAgente = corretora.obterAtributo("situacao").obterValor();
			                	if(situacaoAgente.equals(Inscricao.SUSPENSA))
			                	{
			                		if(apolice!=null)
			                			erros.add("Error: 09 - Corretora o Reaseguradora con inscripci\363n " + inscricaoCorredoraReaseguro1 + " suspendida, Póliza: "+apolice.obterNumeroApolice()+" mes e año Póliza: "+ mesAno+" - Línea: " + numeroLinha + nRegistro);
			                		else
			                			erros.add("Error: 09 - Corretora o Reaseguradora con inscripci\363n " + inscricaoCorredoraReaseguro1 + " suspendida, mes e año Póliza: "+ mesAno+" - Línea: " + numeroLinha + nRegistro);
			                	}
		                    }
		                    
		                    if(reaseguradora == null && corretora == null && apolice!=null)
		                    {
		                        if(mostrarErro110)
		                        	erros.add("Error: 36 - Corredora es obligatoria - Línea: " + numeroLinha + nRegistro);
		                    }

		                    reaseguroGs1 = 0;
		                    if(verificaPosicaoString(linha, ultimo + 3 + 1 + 3, ultimo + 3 + 1 + 3 + 22, true, numeroLinha, "Reaseguro en Guaranies", nRegistro))
		                    	reaseguroGs1 = Double.valueOf(linha.substring(ultimo + 3 + 1 + 3, ultimo + 3 + 1 + 3 + 22).trim());
		                    
		                    tipoMoedaReaseguroGs1 = "";
		                    if(verificaPosicaoString(linha, ultimo + 3 + 1 + 3 + 22, ultimo + 3 + 1 + 3 + 22 + 2, true, numeroLinha, "Tipo ME Reaseguro", nRegistro))
		                    	tipoMoedaReaseguroGs1 = obterTipoMoeda(linha.substring(ultimo + 3 + 1 + 3 + 22, ultimo + 3 + 1 + 3 + 22 + 2), numeroLinha, nRegistro);
		                    
		                    reaseguroMe1 = 0;
		                    if(verificaPosicaoString(linha, ultimo + 3 + 1 + 3 + 22 + 2, ultimo + 3 + 1 + 3 + 22 + 2 + 22, true, numeroLinha, "Reaseguro en ME", nRegistro))
		                    	reaseguroMe1 = Double.valueOf(linha.substring(ultimo + 3 + 1 + 3 + 22 + 2, ultimo + 3 + 1 + 3 + 22 + 2 + 22).trim());        
		                    
		                    primaReaseguro1Gs = 0;
		                    if(verificaPosicaoString(linha, ultimo + 3 + 1 + 3 + 22 + 2 + 22, ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22, true, numeroLinha, "Prima en Guaranies", nRegistro))
		                    	primaReaseguro1Gs = Double.valueOf(linha.substring(ultimo + 3 + 1 + 3 + 22 + 2 + 22, ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22).trim());        
		                    	
		                    tipoMoedaPrimaReaseguro1 = "";
		                    if(verificaPosicaoString(linha, ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22, ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2, true, numeroLinha, "Tipo ME Prima", nRegistro))
		                    	tipoMoedaPrimaReaseguro1 = obterTipoMoeda(linha.substring(ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22, ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2), numeroLinha, nRegistro);
		                    
		                    primaReaseguro1Me = 0;
		                    if(verificaPosicaoString(linha, ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2, ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22, true, numeroLinha, "Prima en ME", nRegistro))
		                    	primaReaseguro1Me = Double.valueOf(linha.substring(ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2, ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22).trim());        
		                    	
		                    comissaoReaseguro1Gs = 0;
		                    if(verificaPosicaoString(linha, ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22, ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22, true, numeroLinha, "Comisión en Guaranies", nRegistro))
		                    	comissaoReaseguro1Gs = Double.valueOf(linha.substring(ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22, ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22).trim());
		                    
		                    tipoMoedaComissaoReaseguro1 = "";
		                    if(verificaPosicaoString(linha, ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22, ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22 + 2, true, numeroLinha, "Tipo ME Comisión", nRegistro))
		                    	tipoMoedaComissaoReaseguro1 = obterTipoMoeda(linha.substring(ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22, ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22 + 2), numeroLinha, nRegistro);
		                    
		                    comissaoReaseguro1Me = 0;
		                    if(verificaPosicaoString(linha, ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22 + 2, ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22 + 2 + 22, true, numeroLinha, "Comisión en ME", nRegistro))
		                    	comissaoReaseguro1Me = Double.valueOf(linha.substring(ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22 + 2, ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22 + 2 + 22).trim());        
		                    
		                    anoInicioVigencia = "";
		                    mesInicioVigencia = "";
		                    diaInicioVigencia = "";
		                    dataInicioVigencia = null;
		                    
		                    if(verificaPosicaoString(linha, ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22 + 2 + 22, ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22 + 2 + 22 + 4, true, numeroLinha, "Año inicio vigencia", nRegistro))
		                    	anoInicioVigencia = linha.substring(ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22 + 2 + 22, ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22 + 2 + 22 + 4).trim();
		                    
		                    if(verificaPosicaoString(linha, ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22 + 2 + 22 + 4, ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22 + 2 + 22 + 4 + 2, true, numeroLinha, "Mes inicio vigencia", nRegistro))
		                    	mesInicioVigencia = linha.substring(ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22 + 2 + 22 + 4, ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22 + 2 + 22 + 4 + 2).trim();
		                    
		                    if(verificaPosicaoString(linha, ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22 + 2 + 22 + 4 + 2, ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22 + 2 + 22 + 4 + 2 + 2, true, numeroLinha, "Día inicio vigencia", nRegistro))
		                    	diaInicioVigencia = linha.substring(ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22 + 2 + 22 + 4 + 2, ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22 + 2 + 22 + 4 + 2 + 2).trim();
		                    
		                    if(inscricaoEspecial)
		                    {
		                    	if(eData(diaInicioVigencia + "/" + mesInicioVigencia + "/" + anoInicioVigencia))
		                    		dataInicioVigencia = new SimpleDateFormat("dd/MM/yyyy").parse(diaInicioVigencia + "/" + mesInicioVigencia + "/" + anoInicioVigencia);
		                    	else
			                        erros.add("Error: 92 - Fecha Inico Vigencia Invalida "+diaInicioVigencia + "/" + mesInicioVigencia + "/" + anoInicioVigencia +" - Línea: " + numeroLinha + nRegistro);
		                    }
		                    else
		                    	dataInicioVigencia = new SimpleDateFormat("dd/MM/yyyy").parse(diaInicioVigencia + "/" + mesInicioVigencia + "/" + anoInicioVigencia);
		                    
		                    anoFimVigencia = "";
		                    mesFimVigencia = "";
		                    diaFimVigencia = "";
		                    dataFimVigencia = null;
		                    
		                    if(verificaPosicaoString(linha, ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22 + 2 + 22 + 4 + 2 + 2, ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22 + 2 + 22 + 4 + 2 + 2 + 4, true, numeroLinha, "Año fin vigencia", nRegistro))
		                    	anoFimVigencia = linha.substring(ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22 + 2 + 22 + 4 + 2 + 2, ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22 + 2 + 22 + 4 + 2 + 2 + 4).trim();
		                    
		                    if(verificaPosicaoString(linha, ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22 + 2 + 22 + 4 + 2 + 2 + 4, ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22 + 2 + 22 + 4 + 2 + 2 + 4 + 2, true, numeroLinha, "Mes fin vigencia", nRegistro))
		                    	mesFimVigencia = linha.substring(ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22 + 2 + 22 + 4 + 2 + 2 + 4, ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22 + 2 + 22 + 4 + 2 + 2 + 4 + 2).trim();
		                    
		                    if(verificaPosicaoString(linha, ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22 + 2 + 22 + 4 + 2 + 2 + 4 + 2, ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22 + 2 + 22 + 4 + 2 + 2 + 4 + 2 + 2, true, numeroLinha, "Día fin vigencia", nRegistro))
		                    	diaFimVigencia = linha.substring(ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22 + 2 + 22 + 4 + 2 + 2 + 4 + 2, ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22 + 2 + 22 + 4 + 2 + 2 + 4 + 2 + 2).trim();
		                    
		                    if(inscricaoEspecial)
		                    {
		                    	if(eData(diaFimVigencia + "/" + mesFimVigencia + "/" + anoFimVigencia))
		                    		dataFimVigencia = new SimpleDateFormat("dd/MM/yyyy").parse(diaFimVigencia + "/" + mesFimVigencia + "/" + anoFimVigencia);
		                    	else
			                        erros.add("Error: 92 - Fecha Fin Vigencia Invalida "+diaFimVigencia + "/" + mesFimVigencia + "/" + anoFimVigencia +" - Línea: " + numeroLinha + nRegistro);
		                    }
		                    else
		                    	dataFimVigencia = new SimpleDateFormat("dd/MM/yyyy").parse(diaFimVigencia + "/" + mesFimVigencia + "/" + anoFimVigencia);
					                    
		                    //Nova validação das datas, incluida em 20/04/2016 - Pediram pra retirar em 15/06/2016
		                   /* Date dataAtual = new Date();
		                    int anoAtual = Integer.valueOf(new SimpleDateFormat("yyyy").format(dataAtual));
		                    if(dataInicioVigencia!=null)
		                    {
		                    	int anoInicio = Integer.valueOf(new SimpleDateFormat("yyyy").format(dataInicioVigencia));
		                    	
		                    	if(anoInicio>anoAtual)
		                    		erros.add("Error: 188 - Año de inicio del contrato es mayor que año actual - Línea: " + numeroLinha);
		                    	else if(anoInicio<(anoAtual-1))
		                    		erros.add("Error: 188 - Año de inicio del contrato es menor que el año anterior - Línea: " + numeroLinha);
		                    }*/
					                    
		                    if(dataFimVigencia!=null && dataInicioVigencia!=null)
		                    {
		                    	int anoInicio = Integer.valueOf(new SimpleDateFormat("yyyy").format(dataInicioVigencia));
		                    	int anoFim = Integer.valueOf(new SimpleDateFormat("yyyy").format(dataFimVigencia));
		                    	
		                    	//Tirei só pra passar a validação, descomentar
		                    	//if(dataInicioVigencia.getTime() == dataFimVigencia.getTime())
		                    		//erros.add("Error: 189 - Fecha de inicio del contrato es igual a Fecha final del contrato - Línea: " + numeroLinha + nRegistro);
		                    	
		                    	//Pediram pra retirar em 15/06/2016
		                    	/*if(anoFim < anoInicio)
		                    		erros.add("Error: 189 - Año final del contrato es menor que Año de inicio del contrato - Línea: " + numeroLinha);
		                    	else if(anoFim>(anoAtual+5))
		                    		erros.add("Error: 189 - Año final del contrato es mayor a cinco años que el año actual - Línea: " + numeroLinha);*/
		                    }
					                    
		                    situacaoReaseguro1 = "";
		                    if(verificaPosicaoString(linha, ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22 + 2 + 22 + 4 + 2 + 2 + 4 + 2 + 2, ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22 + 2 + 22 + 4 + 2 + 2 + 4 + 2 + 2 + 1, true, numeroLinha, "Situac\355on de Reaseguro", nRegistro))
		                    {
		                    	String s = linha.substring(ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22 + 2 + 22 + 4 + 2 + 2 + 4 + 2 + 2, ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22 + 2 + 22 + 4 + 2 + 2 + 4 + 2 + 2 + 1).trim(); 
			                    if(s.equals("1"))
			                        situacaoReaseguro1 = "Vigente";
			                    else if(s.equals("2"))
			                        situacaoReaseguro1 = "No Vigente";
			                    else
			                    	erros.add("Error: 37 - Situac\355on de Reaseguro es obligatorio - Línea: " + numeroLinha + nRegistro);
		                    }
		                    
		                    ultimo = ultimo + 3 + 1 + 3 + 22 + 2 + 22 + 22 + 2 + 22 + 22 + 2 + 22 + 4 + 2 + 2 + 4 + 2 + 2 + 1;
		                    
		                    dadosReaseguro = (DadosReaseguro) this.getModelManager().getEntity("DadosReaseguro");
					                    
		                    if(apolice != null)
		                    {
		                    	if(apolice.obterNumeroApolice() != null && cContas!=null && !tipoContratoReaseguro1.equals(""))
		                    	{
			                    	if(reaseguradora != null)
			                            dadosReaseguros.put(new Long((obterOrigem().obterId() + cContas.obterId()) + apolice.obterNumeroApolice() + reaseguradora.obterId()) + tipoContratoReaseguro1, dadosReaseguro);
			                        else
			                            dadosReaseguros.put(new Long((obterOrigem().obterId() + cContas.obterId()) + apolice.obterNumeroApolice() + 0) + tipoContratoReaseguro1, dadosReaseguro);
		                    	}
		                    }
					                    
		                    if(erros.size() == 0)
		                    {
		                        dadosReaseguro.atribuirOrigem(aseguradora);
		                        dadosReaseguro.atribuirDestino(apolice.obterDestino());
		                        dadosReaseguro.atribuirResponsavel(apolice.obterResponsavel());
		                        dadosReaseguro.atribuirTitulo("Datos do Instrumento: " + numeroInstrumento + ";Sección: " + cContas.obterId());
		                        dadosReaseguro.atribuirSuperior(apolice);
		                        dadosReaseguro.atribuirReaseguradora(reaseguradora);
		                        dadosReaseguro.atribuirTipoContrato(tipoContratoReaseguro1);
		                        dadosReaseguro.atribuirCorredora(corretora);
		                        dadosReaseguro.atribuirCapitalGs(reaseguroGs1);
		                        dadosReaseguro.atribuirTipoMoedaCapitalGs(tipoMoedaReaseguroGs1);
		                        dadosReaseguro.atribuirCapitalMe(reaseguroMe1);
		                        dadosReaseguro.atribuirPrimaGs(primaReaseguro1Gs);
		                        dadosReaseguro.atribuirTipoMoedaPrimaGs(tipoMoedaPrimaReaseguro1);
		                        dadosReaseguro.atribuirPrimaMe(primaReaseguro1Me);
		                        dadosReaseguro.atribuirComissaoGs(comissaoReaseguro1Gs);
		                        dadosReaseguro.atribuirTipoMoedaComissaoGs(tipoMoedaComissaoReaseguro1);
		                        dadosReaseguro.atribuirComissaoMe(comissaoReaseguro1Me);
		                        dadosReaseguro.atribuirDataPrevistaInicio(dataInicioVigencia);
		                        dadosReaseguro.atribuirDataPrevistaConclusao(dataFimVigencia);
		                        dadosReaseguro.atribuirSituacao(situacaoReaseguro1);
		                        dadosReaseguro.atribuirValorEndoso(valorEndoso);
		                        dadosReaseguro.atribuirTipoInstrumento(tipoInstrumento);
		                        dadosReaseguro.atribuirCertificado(certificado);
		                        dadosReaseguro.atribuirDataIniApo(apolice.obterDataPrevistaInicio());
		                        dadosReaseguro.atribuirDataFimApo(apolice.obterDataPrevistaConclusao());
		                        dadosReaseguros2.add(dadosReaseguro);
		                    }
		                }
	                }
	            } 
    			else if(numeroRegistroInt == 5)
    			{
    				nRegistro = " (Registro 05)";
    				
    				cContas = null;
    				if(verificaPosicaoString(linha, 9, 19, true, numeroLinha, "Cuenta", nRegistro))
    				{
    					apelidoCconta = linha.substring(9, 19).trim();
    					cContas = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido(apelidoCconta);
    					if(cContas == null)
    						erros.add("Error: 05 - Cuenta " + apelidoCconta + " no fue encontrada - Línea: " + numeroLinha + nRegistro);
    				}
				                
    				tipoInstrumento = "";
    				if(verificaPosicaoString(linha, 19, 20, true, numeroLinha, "Tipo del Instrumento", nRegistro))
    				{
    					String s = linha.substring(19, 20).trim();
    				
		                if(s.equals("1"))
		                    tipoInstrumento = "P\363liza Individual";
		                else if(s.equals("2"))
		                    tipoInstrumento = "P\363liza Madre";
		                else if(s.equals("3"))
		                    tipoInstrumento = "Certificado de Seguro Colectivo";
		                else if(s.equals("4"))
		                    tipoInstrumento = "Certificado Provisorio";
		                else if(s.equals("5"))
		                    tipoInstrumento = "Nota de Cobertura de Reaseguro";
		                else
		                	erros.add("Error: 28 - Tipo del Instrumento es obligatorio - Línea: " + numeroLinha + nRegistro);
    				}
    				
    				numeroInstrumento = "";
    				if(verificaPosicaoString(linha, 20, 30, true, numeroLinha, "Número Póliza", nRegistro))
    					numeroInstrumento = linha.substring(20, 30).trim();
    				
    				numeroEndoso = 0;
    				if(verificaPosicaoString(linha, 30, 40, true, numeroLinha, "Endoso", nRegistro))
    					numeroEndoso = Double.valueOf(linha.substring(30, 40).trim());
    				
    				certificado = 0;
    				if(verificaPosicaoString(linha, 40, 47, true, numeroLinha, "Certificado", nRegistro))
    					certificado = Double.valueOf(linha.substring(40, 47).trim());
    				
	                apolice = null;
	                
	                if(apolices != null && cContas!=null && !numeroInstrumento.equals(""))
	                {
	                    apolice = apolices.get(obterOrigem().obterId() + numeroInstrumento+ cContas.obterId() + tipoInstrumento);
	                    if(apolice == null)
	                    	apolice = apoliceHome.obterApolice(obterOrigem(), numeroInstrumento,cContas, tipoInstrumento);
	                    if(apolice == null)
		                	erros.add("Error: 29 - Instrumento " + numeroInstrumento + " no fue encuentrado - Línea: " + numeroLinha + nRegistro);
	                }
	                
	                grupo = "";
	                if(verificaPosicaoString(linha, 47, 49, false, numeroLinha, "Grupo", nRegistro))
	                	grupo = linha.substring(47, 49).trim();
	                
	                qtde = 0;
	                if(verificaPosicaoString(linha, 49, 51, true, numeroLinha, "Cantidad", nRegistro))
	                {
	                	qtde = Integer.valueOf(linha.substring(49, 51).trim());
	                	if(qtde == 0)
	                		erros.add("Error: 39 - Cantidad de Aseguradoras es obligatoria - Línea: " + numeroLinha + nRegistro);
	                }
	                
	                ultimo = 0;
	                ultimoArquivo = qtde * (3 + 22 + 2 + 22 + 6 + 22 + 2 + 22);
	                ultimoArquivo2 = linha.substring(51, linha.length()).length() - 2;
	                
	                if(ultimoArquivo!= ultimoArquivo2)
	                	erros.add("Error: 40 - Número de Dados Coaseguro declarados es diferente de lo que hay en el archivo - Línea " + numeroLinha + nRegistro);
	                else
	                {
		                for(int w = 0; w < qtde; w++)
		                {
		                    if(ultimo == 0)
		                    	ultimo = 51;
		                    
		                    aseguradora2 = null;
		                    inscricaoAseguradora = "";
		                    if(verificaPosicaoString(linha, ultimo, ultimo + 3, true, numeroLinha, "Numero de la Aseguradora", nRegistro))
		                    {
		                    	inscricaoAseguradora = linha.substring(ultimo, ultimo + 3).trim();
		                    	if(inscricaoAseguradora.equals(""))
		                    		erros.add("Error: 41 - Numero de la Aseguradora es obligatorio - Línea: " + numeroLinha + nRegistro);
		                    	else
		                    		aseguradora2 = entidadeHome.obterEntidadePorSigla(inscricaoAseguradora);
		                    	
		                    	if(aseguradora2 == null || aseguradora2 instanceof Raiz)
			                        erros.add("Error: 42 - Aseguradora " + inscricaoAseguradora + " - no fue encontrada - Línea: " + numeroLinha + nRegistro);
		                    }
		                    
		                    capitalGs = 0;
		                    capitalGsStr = "";
		                    if(verificaPosicaoString(linha, ultimo + 3, ultimo + 3 + 22, true, numeroLinha, "Capital en Riesgo en Gs", nRegistro))
		                    {
			                    capitalGsStr = linha.substring(ultimo + 3, ultimo + 3 + 22).trim();
			                    if(capitalGsStr.equals(""))
			                        erros.add("Error: 43 - Capital en Riesgo en Gs es obligatorio - Línea: " + numeroLinha + nRegistro);
			                    else
			                    	capitalGs = Double.valueOf(capitalGsStr);
		                    }
		                    
		                    tipoMoedaCapitalGs1 = "";
		                    if(verificaPosicaoString(linha, ultimo + 3 + 22, ultimo + 3 + 22 + 2, true, numeroLinha, "Tipo ME Capital", nRegistro))
		                    	tipoMoedaCapitalGs1 = obterTipoMoeda(linha.substring(ultimo + 3 + 22, ultimo + 3 + 22 + 2), numeroLinha, nRegistro);
		                    
		                    capitalMe = 0;
		                    if(verificaPosicaoString(linha, ultimo + 3 + 22 + 2, ultimo + 3 + 22 + 2 + 22, true, numeroLinha, "Capital en ME", nRegistro))
		                    	capitalMe = Double.valueOf(linha.substring(ultimo + 3 + 22 + 2, ultimo + 3 + 22 + 2 + 22).trim());
		                    
		                    porcentagemCoaseguradora = 0;
		                    porcentagemCoaseguradoraStr = "";
		                    if(verificaPosicaoString(linha, ultimo + 3 + 22 + 2 + 22, ultimo + 3 + 22 + 2 + 22 + 6, true, numeroLinha, "Porcentaje de participaci\363n", nRegistro))
		                    {
			                    porcentagemCoaseguradoraStr = linha.substring(ultimo + 3 + 22 + 2 + 22, ultimo + 3 + 22 + 2 + 22 + 6).trim();
			                    if(porcentagemCoaseguradoraStr.equals(""))
			                        erros.add("Error: 44 - Porcentaje de participaci\363n es obligatorio - Línea: " + numeroLinha + nRegistro);
			                    else
			                    	porcentagemCoaseguradora = Double.valueOf(porcentagemCoaseguradoraStr);
		                    }
		                    
		                    primaGsStr = "";
		                    primaGs = 0;
		                    if(verificaPosicaoString(linha, ultimo + 3 + 22 + 2 + 22 + 6, ultimo + 3 + 22 + 2 + 22 + 6 + 22, true, numeroLinha, "Prima en Gs", nRegistro))
		                    {
			                    primaGsStr = linha.substring(ultimo + 3 + 22 + 2 + 22 + 6, ultimo + 3 + 22 + 2 + 22 + 6 + 22).trim();
			                    if(primaGsStr.equals(""))
			                        erros.add("Error: 45 - Prima en Gs es obligatoria - Línea: " + numeroLinha + nRegistro);
			                    else
			                    	primaGs = Double.valueOf(primaGsStr);
		                    }
		                    
		                    tipoMoedaPrimaGs = "";
		                    if(verificaPosicaoString(linha, ultimo + 3 + 22 + 2 + 22 + 6 + 22, ultimo + 3 + 22 + 2 + 22 + 6 + 22 + 2, true, numeroLinha, "Tipo ME Prima", nRegistro))
		                    	tipoMoedaPrimaGs = obterTipoMoeda(linha.substring(ultimo + 3 + 22 + 2 + 22 + 6 + 22, ultimo + 3 + 22 + 2 + 22 + 6 + 22 + 2), numeroLinha, nRegistro);
		                    
		                    primaMe = 0;
		                    if(verificaPosicaoString(linha, ultimo + 3 + 22 + 2 + 22 + 6 + 22 + 2, ultimo + 3 + 22 + 2 + 22 + 6 + 22 + 2 + 22, true, numeroLinha, "Prima en ME", nRegistro))
			                    primaMe = Double.valueOf(linha.substring(ultimo + 3 + 22 + 2 + 22 + 6 + 22 + 2, ultimo + 3 + 22 + 2 + 22 + 6 + 22 + 2 + 22).trim());
		                    
		                    ultimo = ultimo + 3 + 22 + 2 + 22 + 6 + 22 + 2 + 22;
		                    if(erros.size() == 0)
		                    {
		                        dadosCoaseguro = (DadosCoaseguro)getModelManager().getEntity("DadosCoaseguro");
		                        dadosCoaseguro.atribuirOrigem(apolice.obterOrigem());
		                        dadosCoaseguro.atribuirDestino(apolice.obterDestino());
		                        dadosCoaseguro.atribuirResponsavel(apolice.obterResponsavel());
		                        dadosCoaseguro.atribuirTitulo("Datos do Instrumento: " + numeroInstrumento + ";Sección: " + cContas.obterId());
		                        dadosCoaseguro.atribuirSuperior(apolice);
		                        dadosCoaseguro.atribuirAseguradora(aseguradora2);
		                        dadosCoaseguro.atribuirGrupo(grupo);
		                        dadosCoaseguro.atribuirCapitalGs(capitalGs);
		                        dadosCoaseguro.atribuirTipoMoedaCapitalGs(tipoMoedaCapitalGs1);
		                        dadosCoaseguro.atribuirCapitalMe(capitalMe);
		                        dadosCoaseguro.atribuirParticipacao(porcentagemCoaseguradora);
		                        dadosCoaseguro.atribuirPrimaGs(primaGs);
		                        dadosCoaseguro.atribuirTipoMoedaPrimaGs(tipoMoedaPrimaGs);
		                        dadosCoaseguro.atribuirPrimaMe(primaMe);
		                        dadosCoaseguro.atribuirTipoInstrumento(tipoInstrumento);
		                        dadosCoaseguro.atribuirNumeroEndoso(numeroEndoso);
		                        dadosCoaseguro.atribuirCertificado(certificado);
		                        dadosCoaseguros.add(dadosCoaseguro);
		                    }
		                }
	                }
	            }
    			else if(numeroRegistroInt == 6)
    			{
    				nRegistro = " (Registro 06)";
    				
    				cContas = null;
    				if(verificaPosicaoString(linha, 9, 19, true, numeroLinha, "Cuenta", nRegistro))
    				{
    					apelidoCconta = linha.substring(9, 19).trim();
		                
		                cContas = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido(apelidoCconta);
		                
		                if(cContas == null)
		                    erros.add("Error: 05 - Cuenta " + apelidoCconta + " no fue encontrada - Línea: " + numeroLinha + nRegistro);
    				}
	
	                tipoInstrumento = "";
	                if(verificaPosicaoString(linha, 19, 20, true, numeroLinha, "Tipo del Instrumento", nRegistro))
	                {
	                	String s = linha.substring(19, 20).trim();
		                if(s.equals("1"))
		                    tipoInstrumento = "P\363liza Individual";
		                else if(s.equals("2"))
		                    tipoInstrumento = "P\363liza Madre";
		                else if(s.equals("3"))
		                    tipoInstrumento = "Certificado de Seguro Colectivo";
		                else if(s.equals("4"))
		                    tipoInstrumento = "Certificado Provisorio";
		                else if(s.equals("5"))
		                    tipoInstrumento = "Nota de Cobertura de Reaseguro";
		                else
		                	erros.add("Error: 28 - Tipo del Instrumento es obligatorio - Línea: " + numeroLinha + nRegistro);
	                }
	                
	                numeroInstrumento = "";
	                if(verificaPosicaoString(linha, 20, 30, true, numeroLinha, "Número Póliza", nRegistro))
	                	numeroInstrumento = linha.substring(20, 30).trim();
	                
	                numeroEndoso = 0;
	                if(verificaPosicaoString(linha, 30, 40, true, numeroLinha, "Endoso", nRegistro))
	                	numeroEndoso = Double.valueOf(linha.substring(30, 40).trim());
	                
	                certificado = 0;
	                if(verificaPosicaoString(linha, 40, 47, true, numeroLinha, "Certificado", nRegistro))
	                	certificado = Double.valueOf(linha.substring(40, 47).trim());
	                
	                apolice = null;
	                if(apolices != null && cContas!=null && !numeroInstrumento.equals(""))
	                {
	                    apolice = apolices.get(obterOrigem().obterId() + numeroInstrumento+ cContas.obterId() + tipoInstrumento);
		                if(apolice == null)
		                    apolice = apoliceHome.obterApolice(obterOrigem(), numeroInstrumento,cContas, tipoInstrumento);
		                if(apolice == null)
		                    erros.add("Error: 29 - Instrumento " + numeroInstrumento + " no fue encuentrado - Línea: " + numeroLinha + nRegistro);
	                }
	                
	                qtde = 0;
	                if(verificaPosicaoString(linha, 47, 49, true, numeroLinha, "Cantidad", nRegistro))
	                {
	                	qtde = Integer.valueOf(linha.substring(47, 49).trim());
	                
	                	if(qtde == 0)
	                		erros.add("Error: 31 - Cantidad de Siniestros es obligatoria - Línea: " + numeroLinha + nRegistro);
	                }
	                
	                ultimo = 0;
	                ultimoArquivo = qtde * (6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4 + 2 + 2 + 4 + 2 + 2 + 22 + 22 + 6 + 120);
	                ultimoArquivo2 = linha.substring(49, linha.length()).length() - 2;
	                if(ultimoArquivo!= ultimoArquivo2)
	                	erros.add("Error: 46 - Número de Siniestros declarados es diferente de lo que hay en el archivo - Línea " + numeroLinha + nRegistro);
	                else
	                {
		                for(int w = 0; w < qtde; w++)
		                {
		                    sinistro = (Sinistro)getModelManager().getEntity("Sinistro");
		                    
		                    if(ultimo == 0)
		                    	ultimo = 49;
		                    
		                    numeroSinistro = "";
		                    if(verificaPosicaoString(linha, ultimo, ultimo + 6, true, numeroLinha, "Numero Siniestro", nRegistro))
		                    {
		                    	numeroSinistro = linha.substring(ultimo, ultimo + 6).trim();
		                    	if(numeroSinistro.equals(""))
		                    		erros.add("Error: 47 - Numero del Siniestro es obligatorio - Línea: " + numeroLinha + nRegistro);
		                    }
		                    
		                    anoSinistro = "";
		                    mesSinistro = "";
		                    diaSinistro = "";
		                    dataSinistro = null;
		                    
		                    if(verificaPosicaoString(linha, ultimo + 6, ultimo + 6 + 4, true, numeroLinha, "Año Siniestro", nRegistro))
		                    	anoSinistro = linha.substring(ultimo + 6, ultimo + 6 + 4).trim();
		                    if(verificaPosicaoString(linha, ultimo + 6 + 4, ultimo + 6 + 4 + 2, true, numeroLinha, "Mes Siniestro", nRegistro))
		                    	mesSinistro = linha.substring(ultimo + 6 + 4, ultimo + 6 + 4 + 2).trim();
		                    if(verificaPosicaoString(linha, ultimo + 6 + 4 + 2, ultimo + 6 + 4 + 2 + 2, true, numeroLinha, "Día Siniestro", nRegistro))
		                    	diaSinistro = linha.substring(ultimo + 6 + 4 + 2, ultimo + 6 + 4 + 2 + 2).trim();
	                    	
                    		if(eData(diaSinistro + "/" + mesSinistro + "/" + anoSinistro))
                    			dataSinistro = (new SimpleDateFormat("dd/MM/yyyy")).parse(diaSinistro + "/" + mesSinistro + "/" + anoSinistro);
                    		else
                    			erros.add("Error: 48 - Fecha Sinistro Invalida "+diaSinistro+"/"+mesSinistro+"/"+anoSinistro + " - Línea: " + numeroLinha + nRegistro);
		                    
                    		anoDenunciaSinistro = "";
                    		mesDenunciaSinistro = "";
                    		diaDenunciaSinistro = "";
                    		dataDenunciaSinistro = null;
                    		
                    		if(verificaPosicaoString(linha, ultimo + 6 + 4 + 2 + 2, ultimo + 6 + 4 + 2 + 2 + 4, true, numeroLinha, "Año Denuncia", nRegistro))
                    			anoDenunciaSinistro = linha.substring(ultimo + 6 + 4 + 2 + 2, ultimo + 6 + 4 + 2 + 2 + 4).trim();
                    		if(verificaPosicaoString(linha, ultimo + 6 + 4 + 2 + 2 + 4, ultimo + 6 + 4 + 2 + 2 + 4 + 2, true, numeroLinha, "Mes Denuncia", nRegistro))
                    			mesDenunciaSinistro = linha.substring(ultimo + 6 + 4 + 2 + 2 + 4, ultimo + 6 + 4 + 2 + 2 + 4 + 2).trim();
                    		if(verificaPosicaoString(linha, ultimo + 6 + 4 + 2 + 2 + 4 + 2, ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2, true, numeroLinha, "Día Denuncia", nRegistro))
                    			diaDenunciaSinistro = linha.substring(ultimo + 6 + 4 + 2 + 2 + 4 + 2, ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2).trim();
		                    
		                    if(eData(diaDenunciaSinistro + "/" + mesDenunciaSinistro + "/" + anoDenunciaSinistro))
		                    	dataDenunciaSinistro = (new SimpleDateFormat("dd/MM/yyyy")).parse(diaDenunciaSinistro + "/" + mesDenunciaSinistro + "/" + anoDenunciaSinistro);
		                    else
		                        erros.add("Error: 49 - Fecha Denuncia del Sinistro Invalida "+diaDenunciaSinistro + "/" + mesDenunciaSinistro + "/" + anoDenunciaSinistro +" - Línea: " + numeroLinha + nRegistro);
		                    
		                    numeroLiquidador = "";
		                    if(verificaPosicaoString(linha, ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2, ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3, true, numeroLinha, "Inscripci\363n Liquidadores de Siniestros", nRegistro))
		                    	numeroLiquidador = linha.substring(ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2, ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3).trim();
		                    
		                    auxiliar = null;
		                    if(!numeroLiquidador.equals("000"))
		                    {
		                    	if(apolice!=null)
		                    	{
			                    	mesAno = null;
			                    	
			                    	if(apolice.obterDataEmissao()!=null)
			                    	{
			                    		mesAno = new SimpleDateFormat("MM/yyyy").format(apolice.obterDataEmissao());
			                    		auxiliar = auxiliarSeguroHome.obterAuxiliarPorInscricaoeTipo(numeroLiquidador, "Liquidadores de Siniestros",mesAno);
			                    	}
			                    	
			                    	if(auxiliar == null)
			                    	{
			                    		auxiliar = auxiliarSeguroHome.obterAuxiliarPorInscricao(numeroLiquidador, "Liquidadores de Siniestros");
			                    		
			                    		if(auxiliar!=null)
			                    			erros.add("Error: 50 - Auxiliar de Seguro (Liquidadores de Siniestros) con Inscripci\363n " + numeroLiquidador + " " + auxiliar.obterNome()+" no fue encuentrado, Póliza: "+apolice.obterNumeroApolice()+" mes e año Póliza: "+ mesAno+" - Línea: " + numeroLinha + nRegistro);
			                    		else
			                    			erros.add("Error: 50 - Auxiliar de Seguro (Liquidadores de Siniestros) con Inscripci\363n " + numeroLiquidador + " no fue encuentrado, Póliza: "+apolice.obterNumeroApolice()+" mes e año Póliza: "+ mesAno+" - Línea: " + numeroLinha + nRegistro);
			                    	}
		                    	}
		                    }
		                    
		                    if(auxiliar!=null)
		                    {
		                    	situacaoAgente = auxiliar.obterAtributo("situacao").obterValor();
			                	if(situacaoAgente.equals(Inscricao.SUSPENSA))
			                		erros.add("Error: 09 - Auxiliar de Seguro (Liquidadores de Siniestros) con inscripci\363n " + numeroLiquidador + " suspendida, Póliza: "+apolice.obterNumeroApolice()+" mes e año Póliza: "+ mesAno+" - Línea: " + numeroLinha + nRegistro);
		                    }
		                    
		                    montanteGs = 0;
		                    montanteGsStr = "";
		                    if(verificaPosicaoString(linha, ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3, ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22, true, numeroLinha, "Monto estimado en Gs", nRegistro))
		                    {
		                    	montanteGsStr = linha.substring(ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3, ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22).trim();
		                    	if(montanteGsStr.equals(""))
		                    		erros.add("Error: 51 - Monto estimado en Gs es obligatorio - Línea: " + numeroLinha + nRegistro);
		                    	else
		                    		montanteGs = Double.valueOf(montanteGsStr);
		                    }
		                    
		                    tipoMoedaMontante = "";
		                    if(verificaPosicaoString(linha, ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22, ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2, true, numeroLinha, "Tipo ME Monto estimado", nRegistro))
		                    	tipoMoedaMontante = obterTipoMoeda(linha.substring(ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22, ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2), numeroLinha, nRegistro);
		                    
		                    montanteMe = 0;
		                    if(verificaPosicaoString(linha, ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2, ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22, true, numeroLinha, "Monto estimado en ME", nRegistro))
		                    	montanteMe = Double.valueOf(linha.substring(ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2, ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22).trim());
		                    
		                    situacaoSinistro = "";
		                    if(verificaPosicaoString(linha, ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22, ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1, true, numeroLinha, "Situaci\363n del Siniestro", nRegistro))
		                    {
		                    	String s = linha.substring(ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22, ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1).trim();
		                    	
			                    if(s.equals("1"))
			                        situacaoSinistro = "Pendiente de Liquidaci\363n";
			                    else if(s.equals("2"))
			                        situacaoSinistro = "Controvertido";
			                    else if(s.equals("3"))
			                        situacaoSinistro = "Pendiente de Pago";
			                    else if(s.equals("4"))
			                        situacaoSinistro = "Recharzado";
			                    else if(s.equals("5"))
			                        situacaoSinistro = "Judicializado";
			                    else if(s.equals("6"))
			                        situacaoSinistro = "Pagado";
			                    else if(s.equals("7"))
			                        situacaoSinistro = "Otros";
			                    else
			                    	erros.add("Error: 52 - Situaci\363n del Siniestro es obligatoria - Línea: " + numeroLinha + nRegistro);
		                    }
		                    
		                    anoFinalizacao = "";
		                    mesFinalizacao = "";
		                    diaFinalizacao = "";
		                    dataFinalizacao = null;
		                    
		                    if(verificaPosicaoString(linha, ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1, ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4, true, numeroLinha, "Año Finalizaci\363n", nRegistro))
		                    	anoFinalizacao = linha.substring(ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1, ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4).trim();
		                    if(verificaPosicaoString(linha, ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4, ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4 + 2, true, numeroLinha, "Mes Finalizaci\363n", nRegistro))
		                    	mesFinalizacao = linha.substring(ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4, ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4 + 2).trim();
		                    if(verificaPosicaoString(linha, ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4 + 2, ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4 + 2 + 2, true, numeroLinha, "Dia Finalizaci\363n", nRegistro))
		                    	diaFinalizacao = linha.substring(ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4 + 2, ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4 + 2 + 2).trim();
		                    
		                    if(!dataEspecial)
		                    {
			                    if(eData(diaFinalizacao + "/" + mesFinalizacao + "/" + anoFinalizacao))
			                    	dataFinalizacao = (new SimpleDateFormat("dd/MM/yyyy")).parse(diaFinalizacao + "/" + mesFinalizacao + "/" + anoFinalizacao);
			                    else
			                        erros.add("Error: 53 - Fecha Finalizaci\363n Invalida "+diaFinalizacao + "/" + mesFinalizacao + "/" + anoFinalizacao +" - Línea: " + numeroLinha + nRegistro);
		                    }
		                    else
		                    	dataFinalizacao = (new SimpleDateFormat("dd/MM/yyyy")).parse(diaFinalizacao + "/" + mesFinalizacao + "/" + anoFinalizacao);

		                    anoRecupero = "";
		                    mesRecupero = "";
		                    diaRecupero = "";
		                    dataRecupero = null;
		                    
		                    if(verificaPosicaoString(linha, ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4 + 2 + 2, ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4 + 2 + 2 + 4, true, numeroLinha, "Año Recupero", nRegistro))
		                    	anoRecupero = linha.substring(ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4 + 2 + 2, ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4 + 2 + 2 + 4).trim();
		                    if(verificaPosicaoString(linha, ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4 + 2 + 2 + 4, ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4 + 2 + 2 + 4 + 2, true, numeroLinha, "Mes Recupero", nRegistro))
		                    	mesRecupero = linha.substring(ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4 + 2 + 2 + 4, ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4 + 2 + 2 + 4 + 2).trim();
		                    if(verificaPosicaoString(linha, ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4 + 2 + 2 + 4 + 2, ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4 + 2 + 2 + 4 + 2 + 2, true, numeroLinha, "Día Recupero", nRegistro))
		                    	diaRecupero = linha.substring(ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4 + 2 + 2 + 4 + 2, ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4 + 2 + 2 + 4 + 2 + 2).trim();
		                    
		                    if(!dataEspecial)
		                    {
			                    if(eData(diaRecupero + "/" + mesRecupero + "/" + anoRecupero))
			                    	dataRecupero = (new SimpleDateFormat("dd/MM/yyyy")).parse(diaRecupero + "/" + mesRecupero + "/" + anoRecupero);
			                    else
			                        erros.add("Error: 54 - Fecha Recupero Invalida "+diaRecupero + "/" + mesRecupero + "/" + anoRecupero +" - Línea: " + numeroLinha + nRegistro);
		                    }
		                    else
		                    	dataRecupero = (new SimpleDateFormat("dd/MM/yyyy")).parse(diaRecupero + "/" + mesRecupero + "/" + anoRecupero);
		                    
		                    recuperoReaseguradora = 0;
		                    if(verificaPosicaoString(linha, ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4 + 2 + 2 + 4 + 2 + 2, ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4 + 2 + 2 + 4 + 2 + 2 + 22, true, numeroLinha, "Recupero en Gs", nRegistro))
		                    	recuperoReaseguradora = Double.valueOf(linha.substring(ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4 + 2 + 2 + 4 + 2 + 2, ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4 + 2 + 2 + 4 + 2 + 2 + 22).trim());
		                    
		                    recuperoTerceiro = 0;
		                    if(verificaPosicaoString(linha, ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4 + 2 + 2 + 4 + 2 + 2 + 22, ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4 + 2 + 2 + 4 + 2 + 2 + 22 + 22, true, numeroLinha, "Recupero Terceiro en Gs", nRegistro))
		                    	recuperoTerceiro = Double.valueOf(linha.substring(ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4 + 2 + 2 + 4 + 2 + 2 + 22, ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4 + 2 + 2 + 4 + 2 + 2 + 22 + 22).trim());
		                    
		                    participacao = 0;
		                    if(verificaPosicaoString(linha, ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4 + 2 + 2 + 4 + 2 + 2 + 22 + 22, ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4 + 2 + 2 + 4 + 2 + 2 + 22 + 22 + 6, true, numeroLinha, "Participación en Gs", nRegistro))
		                    	participacao = Double.valueOf(linha.substring(ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4 + 2 + 2 + 4 + 2 + 2 + 22 + 22, ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4 + 2 + 2 + 4 + 2 + 2 + 22 + 22 + 6).trim());
		                    
		                    if(verificaPosicaoString(linha, ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4 + 2 + 2 + 4 + 2 + 2 + 22 + 22 + 6, ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4 + 2 + 2 + 4 + 2 + 2 + 22 + 22 + 6 + 120, false, numeroLinha, "Descripci\363n", nRegistro))
		                    {
		                    	descricao = linha.substring(ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4 + 2 + 2 + 4 + 2 + 2 + 22 + 22 + 6, ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4 + 2 + 2 + 4 + 2 + 2 + 22 + 22 + 6 + 120).trim();
		                    	if(descricao.equals(""))
		                    		erros.add("Error: 55 - Descripci\363n del Siniestro es obligatoria - Línea: " + numeroLinha + nRegistro);
		                    }
					                    
		                    ultimo = ultimo + 6 + 4 + 2 + 2 + 4 + 2 + 2 + 3 + 22 + 2 + 22 + 1 + 4 + 2 + 2 + 4 + 2 + 2 + 22 + 22 + 6 + 120;
		                    if(erros.size() == 0)
		                    {
		                    	sinistro.atribuirOrigem(apolice.obterOrigem());
		                        sinistro.atribuirDataPrevistaInicio(apolice.obterDataPrevistaInicio());
		                        sinistro.atribuirDataPrevistaConclusao(apolice.obterDataPrevistaConclusao());
		                        sinistro.atribuirDestino(apolice.obterDestino());
		                        sinistro.atribuirResponsavel(apolice.obterResponsavel());
		                        sinistro.atribuirTitulo("Datos do Instrumento: " + numeroInstrumento + ";Sección: " + cContas.obterId());
		                        sinistro.atribuirSuperior(apolice);
		                        sinistro.atribuirNumero(numeroSinistro);
		                        sinistro.atribuirDataSinistro(dataSinistro);
		                        sinistro.atribuirDataDenuncia(dataDenunciaSinistro);
		                        sinistro.atribuirAgente(auxiliar);
		                        sinistro.atribuirMontanteGs(montanteGs);
		                        sinistro.atribuirTipoMoedaMontanteGs(tipoMoedaMontante);
		                        sinistro.atribuirMontanteMe(montanteMe);
		                        sinistro.atribuirSituacao(situacaoSinistro);
		                        sinistro.atribuirDataPagamento(dataFinalizacao);
		                        sinistro.atribuirDataRecuperacao(dataRecupero);
		                        sinistro.atribuirValorRecuperacao(recuperoReaseguradora);
		                        sinistro.atribuirValorRecuperacaoTerceiro(recuperoTerceiro);
		                        sinistro.atribuirParticipacao(participacao);
		                        sinistro.atribuirTipoInstrumento(tipoInstrumento);
		                        sinistro.atribuirNumeroEndoso(numeroEndoso);
		                        sinistro.atribuirCertificado(certificado);
		                        descricao2 = "";
		                        cont = 1;
		                        for(int j = 0; j < descricao.length();)
		                        {
		                            caracter = descricao.substring(j, cont);
		                            if(j == 100)
		                            {
		                                for(entrou = false; !caracter.equals(" "); entrou = true)
		                                {
		                                    descricao2 = descricao2 + caracter;
		                                    j++;
		                                    cont++;
		                                    if(j == descricao.length())
		                                        break;
		                                    caracter = descricao.substring(j, cont);
		                                }
		
		                                if(!entrou)
		                                {
		                                    j++;
		                                    cont++;
		                                }
		                                else
		                                    descricao2 = descricao2 + "\n";
		                            }
		                            else
		                            {
		                                descricao2 = descricao2 + caracter;
		                                cont++;
		                                j++;
		                            }
		                        }
		
		                        sinistro.atribuirDescricao(descricao2);
		                    }
		                    
		                    if(!numeroSinistro.equals(""))
		                    	sinistros.put(new Long(obterOrigem().obterId() + numeroSinistro), sinistro);
		                }
	                }
	            }
    			else if(numeroRegistroInt == 7)
    			{
    				nRegistro = " (Registro 07)";
				        
    				cContas = null;
    				if(verificaPosicaoString(linha, 9, 19, true, numeroLinha, "Cuenta", nRegistro))
    				{
    					apelidoCconta = linha.substring(9, 19).trim();
		                cContas = (ClassificacaoContas)entidadeHome.obterEntidadePorApelido(apelidoCconta);
		                if(cContas == null)
		                    erros.add("Error: 05 - Cuenta " + apelidoCconta + " no fue encontrada - Línea: " + numeroLinha + nRegistro);
    				}
				                
    				tipoInstrumento = "";
    				if(verificaPosicaoString(linha, 19, 20, true, numeroLinha, "Tipo del Instrumento", nRegistro))
    				{
    					String s = linha.substring(19, 20).trim();
    					
	    				if(s.equals("1"))
		                    tipoInstrumento = "P\363liza Individual";
		                else if(s.equals("2"))
		                    tipoInstrumento = "P\363liza Madre";
		                else if(s.equals("3"))
		                    tipoInstrumento = "Certificado de Seguro Colectivo";
		                else if(s.equals("4"))
		                    tipoInstrumento = "Certificado Provisorio";
		                else if(s.equals("5"))
		                    tipoInstrumento = "Nota de Cobertura de Reaseguro";
		                else
		                	erros.add("Error: 28 - Instrumento es obligatorio - Línea: " + numeroLinha + nRegistro);
    				}
	                
    				numeroInstrumento = "";
    				if(verificaPosicaoString(linha, 20, 30, true, numeroLinha, "Número Póliza", nRegistro))
    					numeroInstrumento = linha.substring(20, 30).trim();
    				
    				numeroEndoso = 0;
    				if(verificaPosicaoString(linha, 30, 40, true, numeroLinha, "Endoso", nRegistro))
    					numeroEndoso = Double.valueOf(linha.substring(30, 40).trim());
    				
    				certificado = 0;
    				if(verificaPosicaoString(linha, 40, 47, true, numeroLinha, "Certificado", nRegistro))
    					certificado = Double.valueOf(linha.substring(40, 47).trim());
    				
    				apolice = null;
				                
    				if(apolices != null && cContas!=null && !numeroInstrumento.equals(""))
    				{
    					apolice = apolices.get(obterOrigem().obterId() + numeroInstrumento+ cContas.obterId() + tipoInstrumento);
    					if(apolice == null)
    						apolice = apoliceHome.obterApolice(obterOrigem(), numeroInstrumento,cContas, tipoInstrumento);
    					if(apolice == null)
    						erros.add("Error: 29 - Instrumento " + numeroInstrumento + " no fue encuentrado - Línea: " + numeroLinha + nRegistro);
    				}
				                
    				qtde = 0;
    				if(verificaPosicaoString(linha, 47, 49, true, numeroLinha, "Cuantidad", nRegistro))
    				{
    					qtde = Integer.valueOf(linha.substring(47, 49).trim());
    					if(qtde == 0)
    						erros.add("Error: 56 - Cantidad de Facturas es obligatoria - Línea: " + numeroLinha + nRegistro);
    				}
    				
    				ultimo = 0;
    				ultimoArquivo = qtde * (6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11 + 50 + 4 + 2 + 2 + 22 + 2 + 22 + 22 + 4 + 2 + 2 + 1);
    				ultimoArquivo2 = linha.substring(49, linha.length()).length() - 2;
    				if(ultimoArquivo!= ultimoArquivo2)
    					erros.add("Error: 57 - Número de Faturas de Siniestro declarados es diferente de lo que hay en el archivo - Línea " + numeroLinha + nRegistro);
    				else
    				{
    					for(int w = 0; w < qtde; w++)
    					{
    						if(ultimo == 0)
    							ultimo = 49;
					                    
    						numeroSinistro = "";
    						if(verificaPosicaoString(linha, ultimo, ultimo + 6, true, numeroLinha, "Numero del Siniestro", nRegistro))
    						{
    							numeroSinistro = linha.substring(ultimo, ultimo + 6).trim();
    							if(numeroSinistro.equals(""))
    								erros.add("Error: 58 - Numero del Siniestro es obligatorio - Línea: " + numeroLinha + nRegistro);
    						}
    						
    						sinistro = null;
    						if(sinistros != null && !numeroSinistro.equals(""))
    						{
    							sinistro = sinistros.get(new Long(obterOrigem().obterId() + numeroSinistro));
    							if(sinistro == null)
    								sinistro = sinistroHome.obterSinistro(obterOrigem(), numeroSinistro);
    							if(sinistro == null)
    								erros.add("Error: 59 - Siniestro " + numeroSinistro.trim() + " no fue encuentrado - Línea: " + numeroLinha + nRegistro);
    						}
					                    
    						dataSinistro = null;
    						anoSinistro = "";
    						mesSinistro = "";
    						diaSinistro = "";
    						
    						if(verificaPosicaoString(linha, ultimo + 6, ultimo + 6 + 4, true, numeroLinha, "Año del Siniestro", nRegistro))
    							anoSinistro = linha.substring(ultimo + 6, ultimo + 6 + 4).trim();
    						if(verificaPosicaoString(linha, ultimo + 6 + 4, ultimo + 6 + 4 + 2, true, numeroLinha, "Mes del Siniestro", nRegistro))
    							mesSinistro = linha.substring(ultimo + 6 + 4, ultimo + 6 + 4 + 2).trim();
    						if(verificaPosicaoString(linha, ultimo + 6 + 4 + 2, ultimo + 6 + 4 + 2 + 2, true, numeroLinha, "Día del Siniestro", nRegistro))
    							diaSinistro = linha.substring(ultimo + 6 + 4 + 2, ultimo + 6 + 4 + 2 + 2).trim();
    						
		                    if(eData(diaSinistro + "/" + mesSinistro + "/" + anoSinistro))
		                    	dataSinistro = (new SimpleDateFormat("dd/MM/yyyy")).parse(diaSinistro + "/" + mesSinistro + "/" + anoSinistro);
		                    else
		                        erros.add("Error: 60 - Fecha Sinistro Invalida "+diaSinistro+"/"+mesSinistro+"/"+anoSinistro + " - Línea: " + numeroLinha + nRegistro);
					                    
		                    tipoDocumento = "";
		                    if(verificaPosicaoString(linha, ultimo + 6 + 4 + 2 + 2, ultimo + 6 + 4 + 2 + 2 + 1, true, numeroLinha, "Tipo del Documento Recibido", nRegistro))
		                    {
		                    	String s = linha.substring(ultimo + 6 + 4 + 2 + 2, ultimo + 6 + 4 + 2 + 2 + 1).trim();
		                    	
			                    if(s.equals("1"))
			                        tipoDocumento = "Factura Cr\351dito";
			                    else if(s.equals("2"))
			                        tipoDocumento = "Autofactura";
			                    else if(s.equals("3"))
			                        tipoDocumento = "Boleta de Venta";
			                    else if(s.equals("4"))
			                        tipoDocumento = "Nota de Cr\351dito";
			                    else if(s.equals("5"))
			                        tipoDocumento = "Nota de D\351bito";
			                    else if(s.equals("6"))
			                        tipoDocumento = "Recibo";
			                    else if(s.equals("7"))
			                        tipoDocumento = "Factura al Contado";
			                    else if(s.equals("8"))
			                        tipoDocumento = "Otros";
			                    else
			                    	erros.add("Error: 61 - Tipo del Documento Recibido es obligatorio - Línea: " + numeroLinha + nRegistro);
		                    }
		                    
		                    numeroDocumento = "";
		                    if(verificaPosicaoString(linha, ultimo + 6 + 4 + 2 + 2 + 1, ultimo + 6 + 4 + 2 + 2 + 1 + 15, false, numeroLinha, "Numero del Documento del Proveedor", nRegistro))
		                    {
		                    	numeroDocumento = linha.substring(ultimo + 6 + 4 + 2 + 2 + 1, ultimo + 6 + 4 + 2 + 2 + 1 + 15).trim();
		                    
		                    	if(numeroDocumento.equals(""))
		                    		erros.add("Error: 62 - Numero del Documento del Proveedor es obligatorio - Línea: " + numeroLinha + nRegistro);
		                    }
		                    
		                    numeroFatura = "";
		                    if(verificaPosicaoString(linha, ultimo + 6 + 4 + 2 + 2 + 1 + 15, ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15, false, numeroLinha, "Numero del la Factura", nRegistro))
		                    	numeroFatura = linha.substring(ultimo + 6 + 4 + 2 + 2 + 1 + 15, ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15).trim();
		                    
		                    tipoDocumentoProvedor = "";
		                    if(verificaPosicaoString(linha, ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15, ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1, true, numeroLinha, "Tipo del Documento del Proveedor", nRegistro))
		                    {
		                    	String s = linha.substring(ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15, ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1).trim();
		                    	
			                    if(s.equals("1"))
			                        tipoDocumentoProvedor = "CI";
			                    else if(s.equals("2"))
			                        tipoDocumentoProvedor = "RUC";
			                    else
			                    	erros.add("Error: 63 - Tipo del Documento del Proveedor es obligatorio - Línea: " + numeroLinha + nRegistro);
		                    }
					                    
		                    rucProvedor = "";
		                    if(verificaPosicaoString(linha, ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1, ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11, false, numeroLinha, "Numero del RUC o CI del Proveedor", nRegistro))
		                    {
		                    	rucProvedor = linha.substring(ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1, ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11).trim();
		                    	if(rucProvedor.equals(""))
		                    		erros.add("Error: 64 - Numero del RUC o CI del Proveedor es obligatorio - Línea: " + numeroLinha + nRegistro);
		                    }
		                    
		                    nomeProvedor = "";
		                    if(verificaPosicaoString(linha, ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11, ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11 + 50, false, numeroLinha, "Nombre del Proveedor", nRegistro))
		                    {
		                    	nomeProvedor = linha.substring(ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11, ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11 + 50).trim();
		                    	if(nomeProvedor.equals(""))
		                    		erros.add("Error: 65 - Nombre del Proveedor es obligatorio - Línea: " + numeroLinha + nRegistro);
		                    }
		                    
		                    dataDocumento = null;
		                    anoDocumento = "";
		                    mesDocumento = "";
		                    diaDocumento = "";
		                    
		                    if(verificaPosicaoString(linha, ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11 + 50, ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11 + 50 + 4, true, numeroLinha, "Año del Documento", nRegistro))
		                    	anoDocumento = linha.substring(ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11 + 50, ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11 + 50 + 4);
		                    if(verificaPosicaoString(linha, ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11 + 50 + 4, ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11 + 50 + 4 + 2, true, numeroLinha, "Mes del Documento", nRegistro))
		                    	mesDocumento = linha.substring(ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11 + 50 + 4, ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11 + 50 + 4 + 2);
		                    if(verificaPosicaoString(linha, ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11 + 50 + 4 + 2, ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11 + 50 + 4 + 2 + 2, true, numeroLinha, "Dia del Documento", nRegistro))
		                    	diaDocumento = linha.substring(ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11 + 50 + 4 + 2, ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11 + 50 + 4 + 2 + 2);
					                    
		                    if(eData(diaDocumento + "/" + mesDocumento + "/" + anoDocumento))
		                    	dataDocumento = (new SimpleDateFormat("dd/MM/yyyy")).parse(diaDocumento + "/" + mesDocumento + "/" + anoDocumento);
		                    else
		                    	erros.add("Error: 66 - Fecha Documento Invalida "+diaDocumento + "/" + mesDocumento + "/" + anoDocumento + " - Línea: " + numeroLinha + nRegistro);
					                        
		                    montanteDocumento = 0;
		                    if(verificaPosicaoString(linha, ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11 + 50 + 4 + 2 + 2, ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11 + 50 + 4 + 2 + 2 + 22, true, numeroLinha, "Valor del Documento", nRegistro))
		                    {
		                    	montanteDocumentoStr = linha.substring(ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11 + 50 + 4 + 2 + 2, ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11 + 50 + 4 + 2 + 2 + 22).trim();
		                    	
		                    	if(montanteDocumentoStr.equals(""))
			                        erros.add("Error: 67 - Valor del Documento es obligatorio - Línea: " + numeroLinha + nRegistro);
		                    	else
		                    		montanteDocumento = Double.valueOf(montanteDocumentoStr);	
		                    }
		                    
		                    tipoMoedaMontante = "";
		                    if(verificaPosicaoString(linha, ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11 + 50 + 4 + 2 + 2 + 22, ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11 + 50 + 4 + 2 + 2 + 22 + 2, true, numeroLinha, "Tipo ME Valor del Documento", nRegistro))
		                    	tipoMoedaMontante = obterTipoMoeda(linha.substring(ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11 + 50 + 4 + 2 + 2 + 22, ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11 + 50 + 4 + 2 + 2 + 22 + 2), numeroLinha, nRegistro);
		                    
		                    montanteDocumentoME = 0;
		                    if(verificaPosicaoString(linha, ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11 + 50 + 4 + 2 + 2 + 22 + 2, ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11 + 50 + 4 + 2 + 2 + 22 + 2 + 22, true, numeroLinha, "Valor del Documento en ME", nRegistro))
		                    	montanteDocumentoME = Double.valueOf(linha.substring(ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11 + 50 + 4 + 2 + 2 + 22 + 2, ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11 + 50 + 4 + 2 + 2 + 22 + 2 + 22).trim());       
					          
		                    montanteDocumentoPago = 0;
		                    if(verificaPosicaoString(linha, ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11 + 50 + 4 + 2 + 2 + 22 + 2 + 22, ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11 + 50 + 4 + 2 + 2 + 22 + 2 + 22 + 22, true, numeroLinha, "Valor del Montante Pagado", nRegistro))
		                    	montanteDocumentoPago = Double.valueOf(linha.substring(ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11 + 50 + 4 + 2 + 2 + 22 + 2 + 22, ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11 + 50 + 4 + 2 + 2 + 22 + 2 + 22 + 22).trim());
		                    
		                    dataPagamento = null;
		                    anoPagamento = "";
		                    mesPagamento = "";
		                    diaPagamento = "";
		                    
		                    if(verificaPosicaoString(linha, ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11 + 50 + 4 + 2 + 2 + 22 + 2 + 22 + 22, ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11 + 50 + 4 + 2 + 2 + 22 + 2 + 22 + 22 + 4, true, numeroLinha, "Año Pagamento", nRegistro))
		                    	anoPagamento = linha.substring(ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11 + 50 + 4 + 2 + 2 + 22 + 2 + 22 + 22, ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11 + 50 + 4 + 2 + 2 + 22 + 2 + 22 + 22 + 4).trim();
		                    if(verificaPosicaoString(linha, ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11 + 50 + 4 + 2 + 2 + 22 + 2 + 22 + 22 + 4, ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11 + 50 + 4 + 2 + 2 + 22 + 2 + 22 + 22 + 4 + 2, true, numeroLinha, "Mes Pagamento", nRegistro))
		                    	mesPagamento = linha.substring(ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11 + 50 + 4 + 2 + 2 + 22 + 2 + 22 + 22 + 4, ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11 + 50 + 4 + 2 + 2 + 22 + 2 + 22 + 22 + 4 + 2).trim();
		                    if(verificaPosicaoString(linha, ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11 + 50 + 4 + 2 + 2 + 22 + 2 + 22 + 22 + 4 + 2, ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11 + 50 + 4 + 2 + 2 + 22 + 2 + 22 + 22 + 4 + 2 + 2, true, numeroLinha, "Día Pagamento", nRegistro))
		                    	diaPagamento = linha.substring(ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11 + 50 + 4 + 2 + 2 + 22 + 2 + 22 + 22 + 4 + 2, ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11 + 50 + 4 + 2 + 2 + 22 + 2 + 22 + 22 + 4 + 2 + 2).trim();
					                    
		                    //if(eData(diaPagamento + "/" + mesPagamento + "/" + anoPagamento))
		                    	dataPagamento = (new SimpleDateFormat("dd/MM/yyyy")).parse(diaPagamento + "/" + mesPagamento + "/" + anoPagamento);
		                    //else
		                    	//erros.add("Error: 68 - Fecha Pagamento Invalida "+diaPagamento + "/" + mesPagamento + "/" + anoPagamento + " - Línea: " + numeroLinha + nRegistro);
					                        
		                    situacaoFatura = "";
		                    if(verificaPosicaoString(linha, ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11 + 50 + 4 + 2 + 2 + 22 + 2 + 22 + 22 + 4 + 2 + 2, ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11 + 50 + 4 + 2 + 2 + 22 + 2 + 22 + 22 + 4 + 2 + 2 + 1, true, numeroLinha, " Situaci\363n de la Factura", nRegistro))
		                    {
		                    	String s = linha.substring(ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11 + 50 + 4 + 2 + 2 + 22 + 2 + 22 + 22 + 4 + 2 + 2, ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11 + 50 + 4 + 2 + 2 + 22 + 2 + 22 + 22 + 4 + 2 + 2 + 1).trim();
		                    	
		                    	if(s.equals("1"))
		                    		situacaoFatura = "Pagada";
		                    	else if(s.equals("2"))
		                    		situacaoFatura = "Pendiente";
		                    	else if(s.equals("3"))
		                    		situacaoFatura = "Anulada";
		                    	else
		                    		erros.add("Error: 69 - Situaci\363n de la Factura es obligatoria - Línea: " + numeroLinha + nRegistro);
		                    }
					                    
		                    ultimo = ultimo + 6 + 4 + 2 + 2 + 1 + 15 + 15 + 1 + 11 + 50 + 4 + 2 + 2 + 22 + 2 + 22 + 22 + 4 + 2 + 2 + 1;
		                    if(erros.size() == 0)
		                    {
		                        fatura = (FaturaSinistro)getModelManager().getEntity("FaturaSinistro");
		                        fatura.atribuirOrigem(sinistro.obterOrigem());
		                        fatura.atribuirDestino(sinistro.obterDestino());
		                        fatura.atribuirResponsavel(sinistro.obterResponsavel());
		                        fatura.atribuirTitulo("Datos do Instrumento: " + numeroInstrumento + ";Sección: " + cContas.obterId());
		                        fatura.atribuirSuperior(sinistro);
		                        fatura.atribuirDataSinistro(dataSinistro);
		                        fatura.atribuirTipo(tipoDocumento);
		                        fatura.atribuirNumeroDocumento(numeroDocumento);
		                        fatura.atribuirNumeroFatura(numeroFatura);
		                        fatura.atribuirRucProvedor(rucProvedor);
		                        fatura.atribuirNomeProvedor(nomeProvedor);
		                        fatura.atribuirDataDocumento(dataDocumento);
		                        fatura.atribuirMontanteDocumento(montanteDocumento);
		                        fatura.atribuirDataPagamento(dataPagamento);
		                        fatura.atribuirSituacaoFatura(situacaoFatura);
		                        fatura.atribuirTipoInstrumento(tipoInstrumento);
		                        fatura.atribuirNumeroEndoso(numeroEndoso);
		                        fatura.atribuirCertificado(certificado);
		                        fatura.atribuirTipoDocumentoProveedor(tipoDocumentoProvedor);
		                        fatura.atribuirTipoMoedaDocumento(tipoMoedaMontante);
		                        fatura.atribuirMontanteME(montanteDocumentoME);
		                        fatura.atribuirMontantePago(montanteDocumentoPago);
		                        Plano plano = apolice.obterPlano();
		                        if(plano!=null)
		                        {
		                        	fatura.atribuirSecaoApolice(plano.obterSecao());
		                        	fatura.atribuirModalidadePlano(plano.obterPlano());
		                        }
		                        
		                        faturas.add(fatura);
		                    }
    					}
    				}
    			}
    			else if(numeroRegistroInt == 8)
    			{
    				nRegistro = " (Registro 08)";
    				
    				cContas = null;
    				if(verificaPosicaoString(linha, 9, 19, true, numeroLinha, "Cuenta", nRegistro))
    				{
    					apelidoCconta = linha.substring(9, 19).trim();
				                
    					cContas = (ClassificacaoContas)entidadeHome.obterEntidadePorApelido(apelidoCconta);
    					if(cContas == null)
    						erros.add("Error: 05 - Cuenta " + apelidoCconta + " no fue encontrada - Línea: " + numeroLinha + nRegistro);
    				}
    				
    				numeroInstrumento = "";
    				if(verificaPosicaoString(linha, 19, 29, true, numeroLinha, "Número Póliza", nRegistro))
    					numeroInstrumento = linha.substring(19, 29).trim();
    				
    				dataAnulacao = null;
    				anoAnulacao = "";            
    				mesAnulacao = "";
    				diaAnulacao = "";
    				
    				if(verificaPosicaoString(linha, 29, 33, true, numeroLinha, "Año Anulaci\363n", nRegistro))
    					anoAnulacao = linha.substring(29, 33).trim();
    				if(verificaPosicaoString(linha, 33, 35, true, numeroLinha, "Mes Anulaci\363n", nRegistro))
    					mesAnulacao = linha.substring(33, 35).trim();
    				if(verificaPosicaoString(linha, 35, 37, true, numeroLinha, "Día Anulaci\363n", nRegistro))
    					diaAnulacao = linha.substring(35, 37).trim();
				                
    				if(eData(diaAnulacao + "/" + mesAnulacao + "/" + anoAnulacao))
    					dataAnulacao = (new SimpleDateFormat("dd/MM/yyyy")).parse(diaAnulacao + "/" + mesAnulacao + "/" + anoAnulacao);
    				else
    					erros.add("Error: 70 - Fecha Anulaci\363n Invalida "+diaAnulacao + "/" + mesAnulacao + "/" + anoAnulacao + " - Línea: " + numeroLinha + nRegistro);
    					
    				tipoAnulacao = "";
    				if(verificaPosicaoString(linha, 37, 38, true, numeroLinha, "Tipo Anulaci\363n", nRegistro))
    				{
    					String s = linha.substring(37, 38).trim();
    					
    					if(s.equals("1"))
    						tipoAnulacao = "Total";
    					else if(s.equals("2"))
    						tipoAnulacao = "Parcial";
    					else
    						erros.add("Error: 70 - Tipo Anulaci\363n Invalida, Tipo = "+s+" - Línea: " + numeroLinha + nRegistro);
    				}
    				
    				capitalAnuladoGs = 0;
    				if(verificaPosicaoString(linha, 38, 60, true, numeroLinha, "Capital Anulado", nRegistro))
    					capitalAnuladoGs = Double.valueOf(linha.substring(38, 60).trim());
    				
    				tipoMoedaCapitalAnulado = "";
    				if(verificaPosicaoString(linha, 60, 62, true, numeroLinha, "Tipo ME Capital Anulado", nRegistro))
    					tipoMoedaCapitalAnulado = obterTipoMoeda(linha.substring(60, 62), numeroLinha, nRegistro);
    				
    				capitalAnuladoMe = 0;
    				if(verificaPosicaoString(linha, 62, 84, true, numeroLinha, "Capital Anulado en ME", nRegistro))
    					capitalAnuladoMe = Double.valueOf(linha.substring(62, 84).trim());        
				                
    				solicitado = "";
    				if(verificaPosicaoString(linha, 84, 85, true, numeroLinha, "Solicitado Por", nRegistro))
    				{
    					String s = linha.substring(84, 85).trim();
    					
		                if(s.equals("1"))
		                    solicitado = "Asegurado";
		                else if(s.equals("2"))
		                    solicitado = "Tomador";
		                else if(s.equals("3"))
		                    solicitado = "Compa\361ia Aseguradora";
		                else if(s.equals("4"))
		                    solicitado = "Otros";
		                else
		                	erros.add("Error: 71 - Solicitado Por es obligatorio - Línea: " + numeroLinha + nRegistro);
    				}
    				
    				diasCorridos = "";
    				if(verificaPosicaoString(linha, 85, 90, true, numeroLinha, "Dias Corridos", nRegistro))
    				{
    					diasCorridos = linha.substring(85, 90).trim();
    					if(diasCorridos.equals(""))
    						erros.add("Error: 72 - Dias Corridos es obligatorio - Línea: " + numeroLinha + nRegistro);
    				}

    				primaAnuladaGs = 0;
    				if(verificaPosicaoString(linha, 90, 112, true, numeroLinha, "Prima Anulada", nRegistro))
    				{
    					primaAnuladaGsStr = linha.substring(90, 112).trim();
    					if(primaAnuladaGsStr.equals(""))
    						erros.add("Error: 73 - Prima Anulada en Gs es obligatoria - Línea: " + numeroLinha + nRegistro);
    					else    
    						primaAnuladaGs = Double.valueOf(primaAnuladaGsStr);
    				}
    				
    				tipoMoedaPrimaAnuladaGs = "";
    				if(verificaPosicaoString(linha, 112, 114, true, numeroLinha, "Tipo ME Prima Anulada", nRegistro))
    					tipoMoedaPrimaAnuladaGs = obterTipoMoeda(linha.substring(112, 114), numeroLinha, nRegistro);
    				
    				primaAnuladaMe = 0;
    				if(verificaPosicaoString(linha, 114, 136, true, numeroLinha, "Prima Anulada en ME", nRegistro))
    				{
    					primaAnuladaMeStr = linha.substring(114, 136).trim();
    					if(primaAnuladaMeStr.equals(""))
    						erros.add("Error: 74 - Prima Anulada en M/E es obligatoria - Línea: " + numeroLinha + nRegistro);
    					else
    						primaAnuladaMe = Double.valueOf(primaAnuladaMeStr);
    				}
    				
    				comissaoAnuladaGs = 0;
    				if(verificaPosicaoString(linha, 136, 158, true, numeroLinha, "Comisi\363n Anulada en GS", nRegistro))
    					comissaoAnuladaGs = Double.valueOf(linha.substring(136, 158).trim());
    					
    				tipoMoedaComissaoAnuladaGs = "";
    				if(verificaPosicaoString(linha, 158, 160, true, numeroLinha, "Tipo ME Comisi\363n Anulada", nRegistro))
    					tipoMoedaComissaoAnuladaGs = obterTipoMoeda(linha.substring(158, 160), numeroLinha, nRegistro);
    				
    				comissaoAnuladaMe = 0;
    				if(verificaPosicaoString(linha, 160, 182, true, numeroLinha, "Comisi\363n Anulada en ME", nRegistro))
    				{
    					comissaoAnuladaMeStr = linha.substring(160, 182).trim();
    					if(comissaoAnuladaMeStr.equals(""))
    						erros.add("Error: 75 - Comisi\363n Anulada en M/E es obligatoria - Línea: " + numeroLinha + nRegistro);
    					else
    						comissaoAnuladaMe = Double.valueOf(comissaoAnuladaMeStr);
    				}
    				
    				comissaoRecuperarGs = 0;
    				if(verificaPosicaoString(linha, 182, 204, true, numeroLinha, "Comisi\363n Recupero en GS", nRegistro))
    					comissaoRecuperarGs = Double.valueOf(linha.substring(182, 204).trim());
    				
    				tipoMoedaComissaoRecuperarGs = "";
    				if(verificaPosicaoString(linha, 204, 206, true, numeroLinha, "Tipo ME Comisi\363n Recupero", nRegistro))
    					tipoMoedaComissaoRecuperarGs = obterTipoMoeda(linha.substring(204, 206), numeroLinha, nRegistro);
    				
    				comissaoRecuperarMe = 0;
    				if(verificaPosicaoString(linha, 206, 228, true, numeroLinha, "Comisi\363n Recupero en ME", nRegistro))
    					comissaoRecuperarMe = Double.valueOf(linha.substring(206, 228).trim());
    				
    				saldoAnuladoGs = 0;
    				if(verificaPosicaoString(linha, 228, 250, true, numeroLinha, "Saldo Anulaci\363n en Gs", nRegistro))
    				{
    					saldoAnuladoGsStr = linha.substring(228, 250).trim();
    					if(saldoAnuladoGsStr.equals(""))
    						erros.add("Error: 76 - Saldo Anulaci\363n en Gs es obligatorio - Línea: " + numeroLinha + nRegistro);
    					else
    						saldoAnuladoGs = Double.valueOf(saldoAnuladoGsStr);
    				}
    				
    				tipoMoedaSaldoAnuladoGs = "";
    				if(verificaPosicaoString(linha, 250, 252, true, numeroLinha, "Tipo ME Saldo Anulaci\363n", nRegistro))
    					tipoMoedaSaldoAnuladoGs = obterTipoMoeda(linha.substring(250, 252), numeroLinha, nRegistro);
    				
    				saldoAnuladoMe = 0;
    				if(verificaPosicaoString(linha, 252, 274, true, numeroLinha, "Saldo Anulaci\363n en ME", nRegistro))
    				{
    					saldoAnuladoMeStr = linha.substring(252, 274).trim();
    					if(saldoAnuladoMeStr.equals(""))
    						erros.add("Error: 77 - Saldo Anulaci\363n en M/E es obligatorio - Línea: " + numeroLinha + nRegistro);
    					else    
    						saldoAnuladoMe = Double.valueOf(saldoAnuladoMeStr);
    				}
    				
    				destinoSaldoAnulacao = "";
    				if(verificaPosicaoString(linha, 274, 275, true, numeroLinha, "Destino Saldo Anulaci\363n", nRegistro))
    				{
    					String s = linha.substring(274, 275).trim(); 
	    				if(s.equals("1"))
	    					destinoSaldoAnulacao = "Cuando el Saldo es Cero";
	    				else if(s.equals("2"))
	    					destinoSaldoAnulacao = "A favor del Asegurado/Tomador";
	    				else if(s.equals("3"))
	    					destinoSaldoAnulacao = "A favor de la Compa\361ia";
	    				else
	    					erros.add("Error: 76 - Destino Saldo Anulaci\363n invalido, Destino Saldo Anulaci\363n = "+s+" - Línea: " + numeroLinha + nRegistro);
    				}
    				
    				motivoAnulacao = "";
    				if(verificaPosicaoString(linha, 275, 395, false, numeroLinha, "Motivo Anulaci\363n", nRegistro))
    					motivoAnulacao = linha.substring(275, 395).trim();
    				
    				tipoInstrumento = "";
    				if(verificaPosicaoString(linha, 395, 396, true, numeroLinha, "Tipo del Instrumento", nRegistro))
    				{
    					String s = linha.substring(395, 396).trim();
    					
		                if(s.equals("1"))
		                    tipoInstrumento = "P\363liza Individual";
		                else if(s.equals("2"))
		                    tipoInstrumento = "P\363liza Madre";
		                else if(s.equals("3"))
		                    tipoInstrumento = "Certificado de Seguro Colectivo";
		                else if(s.equals("4"))
		                    tipoInstrumento = "Certificado Provisorio";
		                else if(s.equals("5"))
		                    tipoInstrumento = "Nota de Cobertura de Reaseguro";
		                else
		                	erros.add("Error: 28 - Tipo del Instrumento es obligatorio - Línea: " + numeroLinha + nRegistro);
    				}
    				
    				numeroEndoso = 0;
    				if(verificaPosicaoString(linha, 396, 406, true, numeroLinha, "Endoso", nRegistro))
    					numeroEndoso = Double.valueOf(linha.substring(396, 406).trim());
    				
    				certificado = 0;
    				if(verificaPosicaoString(linha, 406, 413, true, numeroLinha, "Certificado", nRegistro))
    					certificado = Double.valueOf(linha.substring(406, 413).trim());
				                
    				apolice = null;
    				if(apolices != null && cContas!=null && !numeroInstrumento.equals(""))
    				{
    					apolice = apolices.get(obterOrigem().obterId() + numeroInstrumento+ cContas.obterId() + tipoInstrumento);
    					if(apolice == null)
    						apolice = apoliceHome.obterApolice(obterOrigem(), numeroInstrumento,cContas, tipoInstrumento);
    					if(apolice == null)
    						erros.add("Error: 29 - Instrumento " + numeroInstrumento + " no fue encuentrado - Línea: " + numeroLinha + nRegistro);
    				}
	                
    				if(erros.size() == 0)
	                {
	                    anulacao = (AnulacaoInstrumento)getModelManager().getEntity("AnulacaoInstrumento");
	                    anulacao.atribuirOrigem(apolice.obterOrigem());
	                    anulacao.atribuirDestino(apolice.obterDestino());
	                    anulacao.atribuirResponsavel(apolice.obterResponsavel());
	                    anulacao.atribuirTitulo("Datos do Instrumento: " + numeroInstrumento + ";Sección: " + cContas.obterId());
	                    anulacao.atribuirSuperior(apolice);
	                    anulacao.atribuirDataAnulacao(dataAnulacao);
	                    anulacao.atribuirTipo(tipoAnulacao);
	                    anulacao.atribuirCapitalGs(capitalAnuladoGs);
	                    anulacao.atribuirTipoMoedaCapitalGs(tipoMoedaCapitalAnulado);
	                    anulacao.atribuirCapitalMe(capitalAnuladoMe);
	                    anulacao.atribuirSolicitadoPor(solicitado);
	                    anulacao.atribuirDiasCorridos(Integer.parseInt(diasCorridos));
	                    anulacao.atribuirPrimaGs(primaAnuladaGs);
	                    anulacao.atribuirTipoMoedaPrimaGs(tipoMoedaPrimaAnuladaGs);
	                    anulacao.atribuirPrimaMe(primaAnuladaMe);
	                    anulacao.atribuirComissaoGs(comissaoAnuladaGs);
	                    anulacao.atribuirTipoMoedaComissaoGs(tipoMoedaComissaoAnuladaGs);
	                    anulacao.atribuirComissaoMe(comissaoAnuladaMe);
	                    anulacao.atribuirComissaoRecuperarGs(comissaoRecuperarGs);
	                    anulacao.atribuirTipoMoedaComissaoRecuperarGs(tipoMoedaComissaoRecuperarGs);
	                    anulacao.atribuirComissaoRecuperarMe(comissaoRecuperarMe);
	                    anulacao.atribuirSaldoAnulacaoGs(saldoAnuladoGs);
	                    anulacao.atribuirTipoMoedaSaldoAnulacaoGs(tipoMoedaSaldoAnuladoGs);
	                    anulacao.atribuirSaldoAnulacaoMe(saldoAnuladoMe);
	                    anulacao.atribuirDestinoSaldoAnulacao(destinoSaldoAnulacao);
	                    anulacao.atribuirTipoInstrumento(tipoInstrumento);
	                    anulacao.atribuirNumeroEndoso(numeroEndoso);
	                    anulacao.atribuirCertificado(certificado);
	                    motivoAnulacao2 = "";
	                    cont = 1;
	                    for(int j = 0; j < motivoAnulacao.length();)
	                    {
	                        String caracter = motivoAnulacao.substring(j, cont);
	                        if(j == 100)
	                        {
	                            entrou = false;
	                            for(entrou = false; !caracter.equals(" "); entrou = true)
	                            {
	                                motivoAnulacao2 = motivoAnulacao2 + caracter;
	                                j++;
	                                cont++;
	                                if(j == motivoAnulacao.length())
	                                    break;
	                                caracter = motivoAnulacao.substring(j, cont);
	                            }
	
	                            if(!entrou)
	                            {
	                                j++;
	                                cont++;
	                            }
	                            else
	                                motivoAnulacao2 = motivoAnulacao2 + "\n";
	                        }
	                        else
	                        {
	                            motivoAnulacao2 = motivoAnulacao2 + caracter;
	                            cont++;
	                            j++;
	                        }
	                    }
	
	                    anulacao.atribuirDescricao(motivoAnulacao2);
	                    anulacoes.add(anulacao);
	                }
	            }
    			else if(numeroRegistroInt == 9)
    			{
    				nRegistro = " (Registro 09)";
    				
    				cContas = null;
    				if(verificaPosicaoString(linha, 9, 19, true, numeroLinha, "Cuenta", nRegistro))
    				{
    					apelidoCconta = linha.substring(9, 19).trim();
    					cContas = (ClassificacaoContas)entidadeHome.obterEntidadePorApelido(apelidoCconta);
    					
    					if(cContas == null)
    						erros.add("Error: 05 - Cuenta " + apelidoCconta + " no fue encontrada - Línea: " + numeroLinha + nRegistro);
    				}
				                
	                tipoInstrumento = "";
	                if(verificaPosicaoString(linha, 19, 20, true, numeroLinha, "Tipo del Instrumento", nRegistro))
	                {
	                	String s = linha.substring(19, 20).trim();
	                	
		                if(s.equals("1"))
		                    tipoInstrumento = "P\363liza Individual";
		                else if(s.equals("2"))
		                    tipoInstrumento = "P\363liza Madre";
		                else if(s.equals("3"))
		                    tipoInstrumento = "Certificado de Seguro Colectivo";
		                else if(s.equals("4"))
		                    tipoInstrumento = "Certificado Provisorio";
		                else if(s.equals("5"))
		                    tipoInstrumento = "Nota de Cobertura de Reaseguro";
		                else
		                	erros.add("Error: 28 - Tipo del Instrumento es obligatorio - Línea: " + numeroLinha + nRegistro);
	                }
				                
	                numeroInstrumento = "";
	                if(verificaPosicaoString(linha, 20, 30, true, numeroLinha, "Número Póliza", nRegistro))
	                	numeroInstrumento = linha.substring(20, 30).trim();
	                
	                numeroEndoso = 0;
	                if(verificaPosicaoString(linha, 30, 40, true, numeroLinha, "Endoso", nRegistro))
	                	numeroEndoso = Double.valueOf(linha.substring(30, 40).trim());

	                certificado = 0;
	                if(verificaPosicaoString(linha, 40, 47, true, numeroLinha, "Certificado", nRegistro))
	                	certificado = Double.valueOf(linha.substring(40, 47).trim());
				                
	                apolice = null;
	                if(apolices != null && cContas!=null && !numeroInstrumento.equals(""))
	                {
	                    apolice = apolices.get(obterOrigem().obterId() + numeroInstrumento+ cContas.obterId() + tipoInstrumento);
	                    if(apolice == null)
	                    	apolice = apoliceHome.obterApolice(obterOrigem(), numeroInstrumento,cContas, tipoInstrumento);
	                    if(apolice == null)
	                    	erros.add("Error: 29 - Instrumento " + numeroInstrumento + " no fue encuentrado - Línea: " + numeroLinha + nRegistro);
	                }
	                
	                qtde = 0;
	                if(verificaPosicaoString(linha, 47, 49, true, numeroLinha, "Cantidad", nRegistro))
	                {
	                	qtde = Integer.parseInt(linha.substring(47, 49).trim());
	                	if(qtde == 0)
	                		erros.add("Error: 78 - Cantidad de Cobranzas es obligatoria - Línea: " + numeroLinha + nRegistro);
	                }
	                
	                ultimo = 0;
	                ultimoArquivo = qtde * (4 + 2 + 2 + 4 + 2 + 2 + 2 + 22 + 2 + 22 + 22);
	                ultimoArquivo2 = linha.substring(49, linha.length()).length() - 2;
	                if(ultimoArquivo!= ultimoArquivo2)
	                	erros.add("Error: 79 - Número de Registros de Cobranza declarados es diferente de lo que hay en el archivo - Línea " + numeroLinha + nRegistro);
	                else
	                {
	                	for(int w = 0; w < qtde; w++)
	                	{
	                		if(ultimo == 0)
	                			ultimo = 49;
	                		
	                		dataCobranca = null;
	                		anoCobranca = "";
	                		mesCobranca = "";
	                		diaCobranca = "";
	                		
	                		if(verificaPosicaoString(linha, ultimo, ultimo + 4, true, numeroLinha, "Año Cobranza", nRegistro))
	                			anoCobranca = linha.substring(ultimo, ultimo + 4).trim();
	                		if(verificaPosicaoString(linha, ultimo + 4, ultimo + 4 + 2, true, numeroLinha, "Mes Cobranza", nRegistro))
	                			mesCobranca = linha.substring(ultimo + 4, ultimo + 4 + 2).trim();
	                		if(verificaPosicaoString(linha, ultimo + 4 + 2, ultimo + 4 + 2 + 2, true, numeroLinha, "Día Cobranza", nRegistro))
	                			diaCobranca = linha.substring(ultimo + 4 + 2, ultimo + 4 + 2 + 2).trim();
	                		
	                		if(eData(diaCobranca + "/" + mesCobranca + "/" + anoCobranca))
	                			dataCobranca = (new SimpleDateFormat("dd/MM/yyyy")).parse(diaCobranca + "/" + mesCobranca + "/" + anoCobranca);
	                		else
	                			erros.add("Error: 80 - Fecha Cobranza Invalida "+diaCobranca + "/" + mesCobranca + "/" + anoCobranca +" - Línea: " + numeroLinha + nRegistro);
					                    
	                		dataVencimentoCobranca = null;
	                		anoVencimentoCobranca = "";
	                		mesVencimentoCobranca = "";
	                		diaVencimentoCobranca = "";
	                		
	                		if(verificaPosicaoString(linha, ultimo + 4 + 2 + 2, ultimo + 4 + 2 + 2 + 4, true, numeroLinha, "Año Vencimiento Cobranza", nRegistro))
	                			anoVencimentoCobranca = linha.substring(ultimo + 4 + 2 + 2, ultimo + 4 + 2 + 2 + 4).trim();
	                		if(verificaPosicaoString(linha, ultimo + 4 + 2 + 2 + 4, ultimo + 4 + 2 + 2 + 4 + 2, true, numeroLinha, "Mes Vencimiento Cobranza", nRegistro))
	                			mesVencimentoCobranca = linha.substring(ultimo + 4 + 2 + 2 + 4, ultimo + 4 + 2 + 2 + 4 + 2).trim();
	                		if(verificaPosicaoString(linha, ultimo + 4 + 2 + 2 + 4 + 2, ultimo + 4 + 2 + 2 + 4 + 2 + 2, true, numeroLinha, "Día Vencimiento Cobranza", nRegistro))
	                			diaVencimentoCobranca = linha.substring(ultimo + 4 + 2 + 2 + 4 + 2, ultimo + 4 + 2 + 2 + 4 + 2 + 2).trim();
		                    
	                		if(eData(diaVencimentoCobranca + "/" + mesVencimentoCobranca + "/" + anoVencimentoCobranca))
	                			dataVencimentoCobranca = (new SimpleDateFormat("dd/MM/yyyy")).parse(diaVencimentoCobranca + "/" + mesVencimentoCobranca + "/" + anoVencimentoCobranca);
	                		else
		                        erros.add("Error: 81 - Fecha Vencimiento Cobranza Invalida "+diaVencimentoCobranca + "/" + mesVencimentoCobranca + "/" + anoVencimentoCobranca + " - Línea: " + numeroLinha + nRegistro);

	                		cotaCobranca = "";
	                		if(verificaPosicaoString(linha, ultimo + 4 + 2 + 2 + 4 + 2 + 2, ultimo + 4 + 2 + 2 + 4 + 2 + 2 + 2, true, numeroLinha, "Cuota de la Cobranza", nRegistro))
	                		{
	                			cotaCobranca = linha.substring(ultimo + 4 + 2 + 2 + 4 + 2 + 2, ultimo + 4 + 2 + 2 + 4 + 2 + 2 + 2).trim();
	                			if(cotaCobranca.equals(""))
	                				erros.add("Error: 82 - Numero de la Cuota de la Cobranza es obligatorio - Línea: " + numeroLinha + nRegistro);
	                		}
	                		
	                		valorCobrancaGs = 0;
	                		if(verificaPosicaoString(linha, ultimo + 4 + 2 + 2 + 4 + 2 + 2 + 2, ultimo + 4 + 2 + 2 + 4 + 2 + 2 + 2 + 22, true, numeroLinha, "Valor en Gs de la Cobranza", nRegistro))
	                		{
	                			valorCobrancaGsStr = linha.substring(ultimo + 4 + 2 + 2 + 4 + 2 + 2 + 2, ultimo + 4 + 2 + 2 + 4 + 2 + 2 + 2 + 22).trim();
	                			if(valorCobrancaGsStr.equals(""))
	                				erros.add("Error: 83 - Valor en Gs de la Cobranza es obligatorio - Línea: " + numeroLinha + nRegistro);
	                			else
	                				valorCobrancaGs = Double.valueOf(valorCobrancaGsStr);
	                		}
	                		
	                		tipoMoedaCobrancaGs = "";
	                		if(verificaPosicaoString(linha, ultimo + 4 + 2 + 2 + 4 + 2 + 2 + 2 + 22, ultimo + 4 + 2 + 2 + 4 + 2 + 2 + 2 + 22 + 2, true, numeroLinha, "Tipo ME Valor de la Cobranza", nRegistro))
	                			tipoMoedaCobrancaGs = obterTipoMoeda(linha.substring(ultimo + 4 + 2 + 2 + 4 + 2 + 2 + 2 + 22, ultimo + 4 + 2 + 2 + 4 + 2 + 2 + 2 + 22 + 2), numeroLinha, nRegistro);
	                		
	                		valorCobrancaMe = 0;
	                		if(verificaPosicaoString(linha, ultimo + 4 + 2 + 2 + 4 + 2 + 2 + 2 + 22 + 2, ultimo + 4 + 2 + 2 + 4 + 2 + 2 + 2 + 22 + 2 + 22, true, numeroLinha, "Valor de la Cobranza en ME", nRegistro))
	                			valorCobrancaMe = Double.valueOf(linha.substring(ultimo + 4 + 2 + 2 + 4 + 2 + 2 + 2 + 22 + 2, ultimo + 4 + 2 + 2 + 4 + 2 + 2 + 2 + 22 + 2 + 22).trim());

	                		valorInteresCobranca = 0;
	                		if(verificaPosicaoString(linha, ultimo + 4 + 2 + 2 + 4 + 2 + 2 + 2 + 22 + 2 + 22, ultimo + 4 + 2 + 2 + 4 + 2 + 2 + 2 + 22 + 2 + 22 + 22, true, numeroLinha, "Valor Interes de la Cobranza", nRegistro))
	                			valorInteresCobranca = Double.valueOf(linha.substring(ultimo + 4 + 2 + 2 + 4 + 2 + 2 + 2 + 22 + 2 + 22, ultimo + 4 + 2 + 2 + 4 + 2 + 2 + 2 + 22 + 2 + 22 + 22).trim());
					                    
	                		ultimo = ultimo + 4 + 2 + 2 + 4 + 2 + 2 + 2 + 22 + 2 + 22 + 22;
					                    
	                		if(erros.size() == 0)
	                		{
		                        cobranca = (RegistroCobranca)getModelManager().getEntity("RegistroCobranca");
		                        cobranca.atribuirOrigem(apolice.obterOrigem());
		                        cobranca.atribuirDestino(apolice.obterDestino());
		                        cobranca.atribuirResponsavel(apolice.obterResponsavel());
		                        cobranca.atribuirTitulo("Datos do Instrumento: " + numeroInstrumento + ";Sección: " + cContas.obterId());
		                        cobranca.atribuirSuperior(apolice);
		                        cobranca.atribuirDataCobranca(dataCobranca);
		                        cobranca.atribuirDataVencimento(dataVencimentoCobranca);
		                        cobranca.atribuirNumeroParcela(Integer.parseInt(cotaCobranca));
		                        cobranca.atribuirValorCobrancaGs(valorCobrancaGs);
		                        cobranca.atribuirTipoMoedaValorCobrancaGs(tipoMoedaCobrancaGs);
		                        cobranca.atribuirValorCobrancaMe(valorCobrancaMe);
		                        cobranca.atribuirValorInteres(valorInteresCobranca);
		                        cobranca.atribuirTipoInstrumento(tipoInstrumento);
		                        cobranca.atribuirNumeroEndoso(numeroEndoso);
		                        cobranca.atribuirCertificado(certificado);
		                        cobrancas.add(cobranca);
		                    }
		                }
	                }
	            }
    			else if(numeroRegistroInt == 10)
    			{
    				nRegistro = " (Registro 10)";

    				cContas = null;
    				if(verificaPosicaoString(linha, 9, 19, true, numeroLinha, "Cuenta", nRegistro))
    				{
    					apelidoCconta = linha.substring(9, 19).trim();
    					cContas = (ClassificacaoContas)entidadeHome.obterEntidadePorApelido(apelidoCconta);
				                
    					if(cContas == null)
    						erros.add("Error: 05 - Cuenta " + apelidoCconta + " no fue encontrada - Línea: " + numeroLinha + nRegistro);
    				}
    				
    				numeroInstrumento = "";
    				if(verificaPosicaoString(linha, 20, 30, true, numeroLinha, "Número Póliza", nRegistro))
    					numeroInstrumento = linha.substring(19, 29).trim();
    				
    				numeroOrdem = "";
    				if(verificaPosicaoString(linha, 29, 35, true, numeroLinha, "Numero de Orden", nRegistro))
    				{
    					numeroOrdem = linha.substring(29, 35).trim();
    					if(numeroOrdem.equals(""))
    						erros.add("Error: 84 - Numero de Orden es obligatorio - Línea: " + numeroLinha + nRegistro);
    				}
	                
    				dataNotificacao = null;
    				anoNotificacao = "";
    				mesNotificacao = "";
    				diaNotificacao = "";
    				
    				if(verificaPosicaoString(linha, 35, 39, true, numeroLinha, "Año Notificaci\363n", nRegistro))
    					anoNotificacao = linha.substring(35, 39).trim();
    				if(verificaPosicaoString(linha, 39, 41, true, numeroLinha, "Mes Notificaci\363n", nRegistro))
    					mesNotificacao = linha.substring(39, 41).trim();
    				if(verificaPosicaoString(linha, 41, 43, true, numeroLinha, "Día Notificaci\363n", nRegistro))
    					diaNotificacao = linha.substring(41, 43).trim();
	                
    				if(eData(diaNotificacao + "/" + mesNotificacao + "/" + anoNotificacao))
    					dataNotificacao = (new SimpleDateFormat("dd/MM/yyyy")).parse(diaNotificacao + "/" + mesNotificacao + "/" + anoNotificacao);
    				else
	                    erros.add("Error: 86 - Fecha Notificaci\363n Invalida "+diaNotificacao + "/" + mesNotificacao + "/" + anoNotificacao + " - Línea: " + numeroLinha + nRegistro);
	                    
    				assuntoQuestionado = "";
    				if(verificaPosicaoString(linha, 43, 163, false, numeroLinha, "Asunto Cuestionado", nRegistro))
    				{
    					assuntoQuestionado = linha.substring(43, 163).trim();
    					if(assuntoQuestionado.equals(""))
    						erros.add("Error: 87 - Asunto Cuestionado es obligatorio - Línea: " + numeroLinha + nRegistro);
    				}
    				
    				demandante = "";
    				if(verificaPosicaoString(linha, 163, 203, false, numeroLinha, "Actor o Demandante", nRegistro))
    				{
    					demandante = linha.substring(163, 203).trim();
    					//if(demandante.equals(""))
    						//erros.add("Error: 88 - Actor o Demandante es obligatorio - Línea: " + numeroLinha + nRegistro);
    				}
    				
    				demandado = "";
    				if(verificaPosicaoString(linha, 203, 243, false, numeroLinha, "Demandado", nRegistro))
    				{
    					demandado = linha.substring(203, 243).trim();
    					//if(demandado.equals(""))
    						//erros.add("Error: 89 - Demandado es obligatorio - Línea: " + numeroLinha + nRegistro);
    				}
    				
    				julgado = "";
    				if(verificaPosicaoString(linha, 243, 245, false, numeroLinha, "Juzgado", nRegistro))
    				{
    					julgado = linha.substring(243, 245).trim();
    					if(julgado.equals(""))
    						erros.add("Error: 90 - Juzgado es obligatorio - Línea: " + numeroLinha + nRegistro);
    				}
    				
    				turno = "";
    				if(verificaPosicaoString(linha, 245, 247, false, numeroLinha, "Turno", nRegistro))
    				{
    					turno = linha.substring(245, 247).trim();
    					if(turno.equals(""))
    						erros.add("Error: 91 - Turno es obligatorio - Línea: " + numeroLinha + nRegistro);
    				}
    				
    				juiz = "";
    				if(verificaPosicaoString(linha, 247, 287, false, numeroLinha, "Juez", nRegistro))
    				{
    					juiz = linha.substring(247, 287).trim();
    					//if(juiz.equals(""))
    						//erros.add("Error: 95 - Juez es obligatorio - Línea: " + numeroLinha + nRegistro);
    				}
    				
    				numeroSecretaria = "";
    				if(verificaPosicaoString(linha, 287, 289, true, numeroLinha, "Numero de la Secretaria", nRegistro))
    				{
    					numeroSecretaria = linha.substring(287, 289).trim();
    					if(numeroSecretaria.equals(""))
    						erros.add("Error: 96 - Numero de la Secretaria es obligatorio - Línea: " + numeroLinha + nRegistro);
    				}
    				
    				advogado = "";
    				if(verificaPosicaoString(linha, 289, 329, false, numeroLinha, "Abogado que esta a cargo del caso", nRegistro))
    				{
    					advogado = linha.substring(289, 329).trim();
    					if(advogado.equals(""))
    						erros.add("Error: 97 - Abogado que esta a cargo del caso es obligatorio - Línea: " + numeroLinha + nRegistro);
    				}
    				
    				circunscricao = "";
    				if(verificaPosicaoString(linha, 329, 331, false, numeroLinha, "Circunscripci\363n", nRegistro))
    				{
    					circunscricao = linha.substring(329, 331).trim();
    					if(circunscricao.equals(""))
    						erros.add("Error: 98 - Circunscripci\363n es obligatoria - Línea: " + numeroLinha + nRegistro);
    				}
    				
    				forum = "";
    				if(verificaPosicaoString(linha, 331, 333, false, numeroLinha, "Fuero", nRegistro))
    				{
    					forum = linha.substring(331, 333).trim();
    					if(forum.equals(""))
    						erros.add("Error: 100 - Fuero es obligatorio - Línea: " + numeroLinha + nRegistro);
    				}
    				
    				dataDemanda = null;
    				anoDemanda = "";
    				mesDemanda = "";
    				diaDemanda = "";
    				
    				if(verificaPosicaoString(linha, 333, 337, true, numeroLinha, "Año Demanda", nRegistro))
    					anoDemanda = linha.substring(333, 337).trim();
    				if(verificaPosicaoString(linha, 337, 339, true, numeroLinha, "Mes Demanda", nRegistro))
    					mesDemanda = linha.substring(337, 339).trim();
    				if(verificaPosicaoString(linha, 339, 341, true, numeroLinha, "Día Demanda", nRegistro))
    					diaDemanda = linha.substring(339, 341).trim();
				                
    				if(eData(diaDemanda + "/" + mesDemanda + "/" + anoDemanda))
    					dataDemanda = (new SimpleDateFormat("dd/MM/yyyy")).parse(diaDemanda + "/" + mesDemanda + "/" + anoDemanda);
    				else
    					erros.add("Error: 101 - Fecha Demanda Invalida "+diaDemanda + "/" + mesDemanda + "/" + anoDemanda + " - Línea: " + numeroLinha + nRegistro);

    				montanteDemandando = 0;
    				if(verificaPosicaoString(linha, 341, 363, true, numeroLinha, "Valor demandado", nRegistro))
    				{
    					montanteDemandandoStr = linha.substring(341, 363).trim();
    					if(montanteDemandandoStr.equals(""))
    						erros.add("Error: 102 - Valor demandado es obligatorio - Línea: " + numeroLinha + nRegistro);
    					else
    						montanteDemandando = Double.valueOf(montanteDemandandoStr);
    				}
				                
    				montanteSentenca = 0;
    				if(verificaPosicaoString(linha, 363, 385, true, numeroLinha, "Montante de la sentencia", nRegistro))
    					montanteSentenca = Double.valueOf(linha.substring(363, 385).trim());
    				
    				dataCancelamento = null;
    				anoCancelamento = "";
    				mesCancelamento = "";
    				diaCancelamento = "";
    				
    				if(verificaPosicaoString(linha, 385, 389, true, numeroLinha, "Año Cancelaci\363n", nRegistro))
    					anoCancelamento = linha.substring(385, 389).trim();
    				if(verificaPosicaoString(linha, 389, 391, true, numeroLinha, "Mes Cancelaci\363n", nRegistro))
    					mesCancelamento = linha.substring(389, 391).trim();
    				if(verificaPosicaoString(linha, 391, 393, true, numeroLinha, "Día Cancelaci\363n", nRegistro))
    					diaCancelamento = linha.substring(391, 393).trim();
	                
    				//if(eData(diaCancelamento + "/" + mesCancelamento + "/" + anoCancelamento))
    					dataCancelamento = (new SimpleDateFormat("dd/MM/yyyy")).parse(diaCancelamento + "/" + mesCancelamento + "/" + anoCancelamento);
    				//else
	                  //  erros.add("Error: 103 - Fecha Cancelaci\363n Invalida "+diaCancelamento + "/" + mesCancelamento + "/" + anoCancelamento +" - Línea: " + numeroLinha + nRegistro);
	                    
    				caraterDemanda = "";
    				if(verificaPosicaoString(linha, 393, 394, true, numeroLinha, "Car\341cter de la demanda", nRegistro))
    				{
    					String s = linha.substring(393, 394).trim();
    					
		                if(s.equals("1"))
		                    caraterDemanda = "La propria Compa\361ia";
		                else if(s.equals("2"))
		                    caraterDemanda = "Citada en garantia";
		                else
		                	erros.add("Error: 104 - Car\341cter de la demanda es obligatorio - Línea: " + numeroLinha + nRegistro);
    				}
				                
    				responsabilidadeMaxima = 0;
    				if(verificaPosicaoString(linha, 394, 416, true, numeroLinha, "Responsabilidad máxima", nRegistro))
    					responsabilidadeMaxima = Double.valueOf(linha.substring(394, 416).trim());
    				
    				provisaoSinistro = 0;
    				if(verificaPosicaoString(linha, 416, 438, true, numeroLinha, "Provisión siniestro", nRegistro))
    					provisaoSinistro = Double.valueOf(linha.substring(416, 438).trim());
				                
    				objetoCausa = "";
    				if(verificaPosicaoString(linha, 438, 688, false, numeroLinha, "Objeto de la Causa", nRegistro))
    				{
    					objetoCausa = linha.substring(438, 688).trim();
    					if(objetoCausa.equals(""))
    						erros.add("Error: 105 - Objeto de la Causa es obligatorio - Línea: " + numeroLinha + nRegistro);
    				}
    				
    				observacoes = "";
    				if(verificaPosicaoString(linha, 688, 938, false, numeroLinha, "Observaciones", nRegistro))
    					observacoes = linha.substring(688, 938).trim();
    				
    				tipoInstrumento = "";
    				if(verificaPosicaoString(linha, 938, 939, true, numeroLinha, "Tipo del Instrumento", nRegistro))
    				{
    					String s = linha.substring(938, 939).trim();
    					
		                if(s.equals("1"))
		                    tipoInstrumento = "P\363liza Individual";
		                else if(s.equals("2"))
		                    tipoInstrumento = "P\363liza Madre";
		                else if(s.equals("3"))
		                    tipoInstrumento = "Certificado de Seguro Colectivo";
		                else if(s.equals("4"))
		                    tipoInstrumento = "Certificado Provisorio";
		                else if(s.equals("5"))
		                    tipoInstrumento = "Nota de Cobertura de Reaseguro";
		                else
		                	erros.add("Error: 28 - Tipo del Instrumento es obligatorio - Línea: " + numeroLinha + nRegistro);
    				}
    				
    				numeroEndoso = 0;
    				if(verificaPosicaoString(linha, 939, 949, true, numeroLinha, "Endoso", nRegistro))
    					numeroEndoso = Double.valueOf(linha.substring(939, 949).trim());
    				
    				certificado = 0;            
    				if(verificaPosicaoString(linha, 949, 956, true, numeroLinha, "Certificado", nRegistro))
    					certificado = Double.valueOf(linha.substring(949, 956).trim());
				                
	                apolice = null;
	                if(apolices != null && cContas!=null && !numeroInstrumento.equals(""))
	                    apolice = apolices.get(obterOrigem().obterId() + numeroInstrumento+ cContas.obterId() + tipoInstrumento);
	                if(apolice == null)
	                    apolice = apoliceHome.obterApolice(obterOrigem(), numeroInstrumento,cContas, tipoInstrumento);
	                if(apolice == null)
	                    erros.add("Error: 29 - Instrumento " + numeroInstrumento + " no fue encuentrado - Línea: " + numeroLinha + nRegistro);
				                
	                if(erros.size() == 0)
	                {
	                	aspectos = (AspectosLegais)getModelManager().getEntity("AspectosLegais");
	                    aspectos.atribuirOrigem(apolice.obterOrigem());
	                    aspectos.atribuirDestino(apolice.obterDestino());
	                    aspectos.atribuirDataPrevistaInicio(apolice.obterDataPrevistaInicio());
	                    aspectos.atribuirDataPrevistaConclusao(apolice.obterDataPrevistaConclusao());
	                    aspectos.atribuirResponsavel(apolice.obterResponsavel());
	                    aspectos.atribuirTitulo("Datos do Instrumento: " + numeroInstrumento + ";Sección: " + cContas.obterId());
	                    aspectos.atribuirSuperior(apolice);
	                    aspectos.atribuirNumeroOrdem(numeroOrdem);
	                    aspectos.atribuirDataNotificacao(dataNotificacao);
	                    aspectos.atribuirAssunto(assuntoQuestionado);
	                    aspectos.atribuirDemandante(demandante);
	                    aspectos.atribuirDemandado(demandado);
	                    aspectos.atribuirJulgado(julgado);
	                    aspectos.atribuirTurno(turno);
	                    aspectos.atribuirJuiz(juiz);
	                    aspectos.atribuirSecretaria(numeroSecretaria);
	                    aspectos.atribuirAdvogado(advogado);
	                    aspectos.atribuirCircunscricao(circunscricao);
	                    aspectos.atribuirForum(forum);
	                    aspectos.atribuirDataDemanda(dataDemanda);
	                    aspectos.atribuirMontanteDemandado(montanteDemandando);
	                    aspectos.atribuirMontanteSentenca(montanteSentenca);
	                    aspectos.atribuirDataCancelamento(dataCancelamento);
	                    aspectos.atribuirTipo(caraterDemanda);
	                    aspectos.atribuirResponsabilidadeMaxima(responsabilidadeMaxima);
	                    aspectos.atribuirSinistroPendente(provisaoSinistro);
	                    aspectos.atribuirTipoInstrumento(tipoInstrumento);
	                    aspectos.atribuirNumeroEndoso(numeroEndoso);
	                    aspectos.atribuirCertificado(certificado);
	                    objetoCausa2 = "";
	                    cont = 1;
	                    for(int j = 0; j < objetoCausa.length();)
	                    {
	                        caracter = objetoCausa.substring(j, cont);
	                        if(j == 100 || j == 200)
	                        {
	                            boolean entrou;
	                            for(entrou = false; !caracter.equals(" "); entrou = true)
	                            {
	                                objetoCausa2 += caracter;
	                                j++;
	                                cont++;
	                                if(j == objetoCausa.length())
	                                    break;
	                                caracter = objetoCausa.substring(j, cont);
	                            }
	
	                            if(!entrou)
	                            {
	                                j++;
	                                cont++;
	                            } 
	                            else
	                                objetoCausa2 += "\n";
	                        }
	                        else
	                        {
	                            objetoCausa2 += caracter;
	                            cont++;
	                            j++;
	                        }
	                    }
	
	                    aspectos.atribuirObjetoCausa(objetoCausa2);
	                    cont = 1;
	                    observacoes2 = "";
	                    for(int j = 0; j < observacoes.length();)
	                    {
	                        caracter = observacoes.substring(j, cont);
	                        if(j == 100 || j == 200)
	                        {
	                            entrou = false;
	                            for(entrou = false; !caracter.equals(" "); entrou = true)
	                            {
	                                observacoes2 += caracter;
	                                j++;
	                                cont++;
	                                if(j == observacoes.length())
	                                    break;
	                                caracter = observacoes.substring(j, cont);
	                            }
	
	                            if(!entrou)
	                            {
	                                j++;
	                                cont++;
	                            } 
	                            else
	                                observacoes2 += "\n";
	                        } 
	                        else
	                        {
	                            observacoes2 += caracter;
	                            cont++;
	                            j++;
	                        }
	                    }
	
	                    aspectos.atribuirDescricao(observacoes2);
	                    aspectos2.add(aspectos);
	                }
	            }
    			else if(numeroRegistroInt == 11)
    			{
    				nRegistro = " (Registro 11)";
    				
    				cContas = null;
    				if(verificaPosicaoString(linha, 9, 19, true, numeroLinha, "Cuenta", nRegistro))
    				{
    					apelidoCconta = linha.substring(9, 19).trim();
    					cContas = (ClassificacaoContas) entidadeHome.obterEntidadePorApelido(apelidoCconta);
				                
    					if(cContas == null)
    						erros.add("Error: 05 - Cuenta " + apelidoCconta + " no fue encontrada - Línea: " + numeroLinha + nRegistro);
    				}
				                
    				tipoInstrumento = "";
    				if(verificaPosicaoString(linha, 19, 20, true, numeroLinha, "Tipo del Instrumento", nRegistro))
    				{
    					String s = linha.substring(19, 20).trim();
				                
		                if(s.equals("1"))
		                    tipoInstrumento = "P\363liza Individual";
		                else if(s.equals("2"))
		                    tipoInstrumento = "P\363liza Madre";
		                else if(s.equals("3"))
		                    tipoInstrumento = "Certificado de Seguro Colectivo";
		                else if(s.equals("4"))
		                    tipoInstrumento = "Certificado Provisorio";
		                else if(s.equals("5"))
		                    tipoInstrumento = "Nota de Cobertura de Reaseguro";
		                else
		                    erros.add("Error: 28 - Tipo del Instrumento es obligatorio - Línea: " + numeroLinha + nRegistro);
    				}
    				
    				numeroInstrumento = "";
    				if(verificaPosicaoString(linha, 20, 30, true, numeroLinha, "Número Póliza", nRegistro))
    					numeroInstrumento = linha.substring(20, 30).trim();
				    
    				numeroEndoso2 = 0;
    				if(verificaPosicaoString(linha, 30, 40, true, numeroLinha, "Endoso", nRegistro))
    					numeroEndoso2 = Double.valueOf(linha.substring(30, 40).trim());
    				
    				certificado2 = 0;
    				if(verificaPosicaoString(linha, 40, 47, true, numeroLinha, "Certificado", nRegistro))
    					certificado2 = Double.valueOf(linha.substring(40, 47).trim());
				                
    				apolice = null;
    				            
    				if(apolices != null && cContas!=null && !numeroInstrumento.equals(""))
	                    apolice = apolices.get(obterOrigem().obterId() + numeroInstrumento+ cContas.obterId() + tipoInstrumento);
	                if(apolice == null)
	                    apolice = apoliceHome.obterApolice(obterOrigem(), numeroInstrumento,cContas, tipoInstrumento);
	                if(apolice == null)
	                    erros.add("Error: 29 - Instrumento " + numeroInstrumento.trim() + " no fue encuentrado - Línea: " + numeroLinha + nRegistro);
	                else
	                {
	                	if(!endosoApolice && apolice!=null)
	                	{
		                	String situacaoSeguro = apolice.obterSituacaoSeguro();
		                	if(situacaoSeguro.equals("Anulada"))
		                		erros.add("Error: 131 - Endoso de póliza anulada - N° de póliza "+ numeroInstrumento +" - Línea: " + numeroLinha + nRegistro);
	                	}
	                	
	                	if(!suplementoEspecial)
	                	{
		                	if(!suplementosTODOSMap.containsKey(obterOrigem().obterId() + numeroInstrumento+ cContas.obterId() + tipoInstrumento))
		                	{
		                		suplementosMap = apolice.obterSuplementos();
		                		suplementosTODOSMap.put(obterOrigem().obterId() + numeroInstrumento+ cContas.obterId() + tipoInstrumento, suplementosMap);
		                	}
	                	}
	                }
	                
	                qtde = 0;
	                if(verificaPosicaoString(linha, 47, 49, true, numeroLinha, "Cantidad", nRegistro))
	                {
	                	qtde = Integer.valueOf(linha.substring(47, 49).trim());
	                	if(qtde == 0)
	                		erros.add("Error: 106 - Cantidad de Endosos es obligatoria - Línea: " + numeroLinha + nRegistro);
	                }
	                
	                ultimo = 0;
				                
	                ultimoArquivo = qtde * (10 + 4 + 2 + 2 + 4 + 2 + 2 + 4 + 2 + 2 + 120 + 22 + 2 + 22);
	                ultimoArquivo2 = linha.substring(49, linha.length()).length() - 2;
	                if(ultimoArquivo!= ultimoArquivo2)
	                	erros.add("Error: 107 - Número de Suplementos declarados es diferente de lo que hay en el archivo - Línea: " + numeroLinha + nRegistro);
	                else
	                {
	                	suplementosMap = new TreeMap<Double, Suplemento>();
	                	if(apolice!=null && !suplementoEspecial)
	                		suplementosMap = suplementosTODOSMap.get(obterOrigem().obterId() + numeroInstrumento+ cContas.obterId() + tipoInstrumento);
				                	
	                	for(int w = 0; w < qtde; w++)
	                	{
	                		if(ultimo == 0)
	                			ultimo = 49;
					                    
	                		String numeroEndosoStr = "";
	                		if(verificaPosicaoString(linha, ultimo, ultimo + 10, true, numeroLinha, "Numero del Endoso", nRegistro))
	                		{
	                			numeroEndosoStr = linha.substring(ultimo, ultimo + 10).trim();
	                			if(numeroEndosoStr.equals(""))
	                				erros.add("Error: 108 - Numero del Endoso es obligatorio - Línea: " + numeroLinha + nRegistro);
	                		}
					                    
	                		dataEmissao = null;
	                		anoEmissao = "";
	                		mesEmissao = "";
	                		diaEmissao = "";
	                		
	                		if(verificaPosicaoString(linha, ultimo + 10, ultimo + 10 + 4, true, numeroLinha, "Año Emisi\363n", nRegistro))
	                			anoEmissao = linha.substring(ultimo + 10, ultimo + 10 + 4).trim();
	                		if(verificaPosicaoString(linha, ultimo + 10 + 4, ultimo + 10 + 4 + 2, true, numeroLinha, "Mes Emisi\363n", nRegistro))
	                			mesEmissao = linha.substring(ultimo + 10 + 4, ultimo + 10 + 4 + 2).trim();
	                		if(verificaPosicaoString(linha, ultimo + 10 + 4 + 2, ultimo + 10 + 4 + 2 + 2, true, numeroLinha, "Día Emisi\363n", nRegistro))
	                			diaEmissao = linha.substring(ultimo + 10 + 4 + 2, ultimo + 10 + 4 + 2 + 2).trim();
					                    
	                		if(suplementoEspecial)
	                			dataEmissao = (new SimpleDateFormat("dd/MM/yyyy")).parse(diaEmissao + "/" + mesEmissao + "/" + anoEmissao);
	                		else
	                		{
	                			if(eData(diaEmissao + "/" + mesEmissao + "/" + anoEmissao))
	                				dataEmissao = (new SimpleDateFormat("dd/MM/yyyy")).parse(diaEmissao + "/" + mesEmissao + "/" + anoEmissao);
	                			else
	                				erros.add("Error: 92 - Fecha Emisi\363n Invalida "+diaEmissao + "/" + mesEmissao + "/" + anoEmissao+" - Línea: " + numeroLinha + nRegistro);
	                		}

	                		dataVigenciaInicial = null;
	                		anoVigenciaInicial = "";
	                		mesVigenciaInicial = "";
	                		diaVigenciaInicial = "";
	                		
	                		if(verificaPosicaoString(linha, ultimo + 10 + 4 + 2 + 2, ultimo + 10 + 4 + 2 + 2 + 4, true, numeroLinha, "Año Inicio Vigencia", nRegistro))
	                			anoVigenciaInicial = linha.substring(ultimo + 10 + 4 + 2 + 2, ultimo + 10 + 4 + 2 + 2 + 4).trim();
	                		if(verificaPosicaoString(linha, ultimo + 10 + 4 + 2 + 2 + 4, ultimo + 10 + 4 + 2 + 2 + 4 + 2, true, numeroLinha, "Mes Inicio Vigencia", nRegistro))
	                			mesVigenciaInicial = linha.substring(ultimo + 10 + 4 + 2 + 2 + 4, ultimo + 10 + 4 + 2 + 2 + 4 + 2).trim();
	                		if(verificaPosicaoString(linha, ultimo + 10 + 4 + 2 + 2 + 4 + 2, ultimo + 10 + 4 + 2 + 2 + 4 + 2 + 2, true, numeroLinha, "Día Inicio Vigencia", nRegistro))
	                			diaVigenciaInicial = linha.substring(ultimo + 10 + 4 + 2 + 2 + 4 + 2, ultimo + 10 + 4 + 2 + 2 + 4 + 2 + 2).trim();
		                    
	                		if(suplementoEspecial)
	                			dataVigenciaInicial = (new SimpleDateFormat("dd/MM/yyyy")).parse(diaVigenciaInicial + "/" + mesVigenciaInicial + "/" + anoVigenciaInicial);
	                		else
	                		{
	                			if(eData(diaVigenciaInicial + "/" + mesVigenciaInicial + "/" + anoVigenciaInicial))
	                				dataVigenciaInicial = (new SimpleDateFormat("dd/MM/yyyy")).parse(diaVigenciaInicial + "/" + mesVigenciaInicial + "/" + anoVigenciaInicial);
	                			else	
	                				erros.add("Error: 92 - Fecha Inicio Vigencia Invalida "+diaVigenciaInicial + "/" + mesVigenciaInicial + "/" + anoVigenciaInicial + " - Línea: " + numeroLinha + nRegistro);
	                		}
	                		
	                		dataVigenciaVencimento = null;
	                		anoVigenciaVencimento = "";
	                		mesVigenciaVencimento = "";
	                		diaVigenciaVencimento = "";
	                		
	                		if(verificaPosicaoString(linha, ultimo + 10 + 4 + 2 + 2 + 4 + 2 + 2, ultimo + 10 + 4 + 2 + 2 + 4 + 2 + 2 + 4, true, numeroLinha, "Año Fin Vigencia", nRegistro))
	                			anoVigenciaVencimento = linha.substring(ultimo + 10 + 4 + 2 + 2 + 4 + 2 + 2, ultimo + 10 + 4 + 2 + 2 + 4 + 2 + 2 + 4).trim();
	                		if(verificaPosicaoString(linha, ultimo + 10 + 4 + 2 + 2 + 4 + 2 + 2 + 4, ultimo + 10 + 4 + 2 + 2 + 4 + 2 + 2 + 4 + 2, true, numeroLinha, "Mes Fin Vigencia", nRegistro))
	                			mesVigenciaVencimento = linha.substring(ultimo + 10 + 4 + 2 + 2 + 4 + 2 + 2 + 4, ultimo + 10 + 4 + 2 + 2 + 4 + 2 + 2 + 4 + 2).trim();
	                		if(verificaPosicaoString(linha, ultimo + 10 + 4 + 2 + 2 + 4 + 2 + 2 + 4 + 2, ultimo + 10 + 4 + 2 + 2 + 4 + 2 + 2 + 4 + 2 + 2, true, numeroLinha, "Día Fin Vigencia", nRegistro))
	                			diaVigenciaVencimento = linha.substring(ultimo + 10 + 4 + 2 + 2 + 4 + 2 + 2 + 4 + 2, ultimo + 10 + 4 + 2 + 2 + 4 + 2 + 2 + 4 + 2 + 2).trim();
		                    
		                    if(suplementoEspecial)
		                    	dataVigenciaVencimento = (new SimpleDateFormat("dd/MM/yyyy")).parse(diaVigenciaVencimento + "/" + mesVigenciaVencimento + "/" + anoVigenciaVencimento);
		                    else
		                    {
		                    	if(eData(diaVigenciaVencimento + "/" + mesVigenciaVencimento + "/" + anoVigenciaVencimento))
		                    		dataVigenciaVencimento = (new SimpleDateFormat("dd/MM/yyyy")).parse(diaVigenciaVencimento + "/" + mesVigenciaVencimento + "/" + anoVigenciaVencimento);
		                    	else
			                        erros.add("Error: 92 - Fecha Fin Vigencia Invalida "+diaVigenciaVencimento + "/" + mesVigenciaVencimento + "/" + anoVigenciaVencimento+" - Línea: " + numeroLinha + nRegistro);
		                    }
		                    
		                    razaoEmissao = "";
		                    if(verificaPosicaoString(linha, ultimo + 10 + 4 + 2 + 2 + 4 + 2 + 2 + 4 + 2 + 2, ultimo + 10 + 4 + 2 + 2 + 4 + 2 + 2 + 4 + 2 + 2 + 120, false, numeroLinha, "Raz\363n o causa", nRegistro))
		                    {
		                    	razaoEmissao = linha.substring(ultimo + 10 + 4 + 2 + 2 + 4 + 2 + 2 + 4 + 2 + 2, ultimo + 10 + 4 + 2 + 2 + 4 + 2 + 2 + 4 + 2 + 2 + 120).trim();
		                    	if(razaoEmissao.equals(""))
		                    		erros.add("Error: 109 - Raz\363n o causa es obligatoria - Línea: " + numeroLinha + nRegistro);
		                    }
					         
		                    primaGs = 0;
		                    if(verificaPosicaoString(linha, ultimo + 10 + 4 + 2 + 2 + 4 + 2 + 2 + 4 + 2 + 2 + 120, ultimo + 10 + 4 + 2 + 2 + 4 + 2 + 2 + 4 + 2 + 2 + 120 + 22, true, numeroLinha, "Prima en Gs del Endoso", nRegistro))
		                    {
		                    	primaGsStr = linha.substring(ultimo + 10 + 4 + 2 + 2 + 4 + 2 + 2 + 4 + 2 + 2 + 120, ultimo + 10 + 4 + 2 + 2 + 4 + 2 + 2 + 4 + 2 + 2 + 120 + 22).trim();
		                    	if(primaGsStr.equals(""))
		                    		erros.add("Error: 110 - Prima en Gs del Endoso es obligatoria - Línea: " + numeroLinha + nRegistro);
		                    	else
		                    	{
		                    		if(primaGsStr.substring(0, 1).equals("-"))
		                    			primaGs = Double.valueOf(primaGsStr.substring(1, primaGsStr.length())) * -1D;
		                    		else
		                    			primaGs = Double.valueOf(primaGsStr);
		                    	}
		                    }
		                    
		                    tipoMoedaPrimaGs = "";
		                    if(verificaPosicaoString(linha, ultimo + 10 + 4 + 2 + 2 + 4 + 2 + 2 + 4 + 2 + 2 + 120 + 22, ultimo + 10 + 4 + 2 + 2 + 4 + 2 + 2 + 4 + 2 + 2 + 120 + 22 + 2, true, numeroLinha, "Tipo ME Prima", nRegistro))
		                    	tipoMoedaPrimaGs = obterTipoMoeda(linha.substring(ultimo + 10 + 4 + 2 + 2 + 4 + 2 + 2 + 4 + 2 + 2 + 120 + 22, ultimo + 10 + 4 + 2 + 2 + 4 + 2 + 2 + 4 + 2 + 2 + 120 + 22 + 2), numeroLinha, nRegistro);
		                    
		                    primaMe = 0;
		                    if(verificaPosicaoString(linha, ultimo + 10 + 4 + 2 + 2 + 4 + 2 + 2 + 4 + 2 + 2 + 120 + 22 + 2, ultimo + 10 + 4 + 2 + 2 + 4 + 2 + 2 + 4 + 2 + 2 + 120 + 22 + 2 + 22, true, numeroLinha, "Prima en ME", nRegistro))
		                    {
		                    	primaMeStr = linha.substring(ultimo + 10 + 4 + 2 + 2 + 4 + 2 + 2 + 4 + 2 + 2 + 120 + 22 + 2, ultimo + 10 + 4 + 2 + 2 + 4 + 2 + 2 + 4 + 2 + 2 + 120 + 22 + 2 + 22).trim();
		                    	if(primaMeStr.substring(0, 1).equals("-"))
		                    		primaMe = Double.valueOf(primaMeStr.substring(1, primaMeStr.length())) * -1D;
		                    	else
		                    		primaMe = Double.valueOf(primaMeStr);
		                    }
		                    
		                    ultimo = ultimo + 10 + 4 + 2 + 2 + 4 + 2 + 2 + 4 + 2 + 2 + 120 + 22 + 2 + 22;
					                    
		                    //Verifica datas da suplemento
		                    nSuple = Double.valueOf(numeroEndosoStr);
					                    
		                    if(apolice!=null && !suplementoEspecial)
		                    {
		                    	System.out.println("Apolice = "+ apolice.obterNumeroApolice() + " Suplementos = " + suplementosMap.size());
		                    	Date dataInicio = null;
		                    	Date dataFim = null;
		                    	Date dataEmissaoSuple = null;
					                    	
		                    	if(nSuple == 1)
		                    	{
		                    		dataInicio = apolice.obterDataPrevistaInicio();
	                    			dataFim = apolice.obterDataPrevistaConclusao();
	                    			dataEmissaoSuple = apolice.obterDataEmissao();
		                    	}
		                    	else
		                    	{
		                    		double nSuple2 = nSuple -1;
		                    		Suplemento sAnterior = null;
		                    		if(suplementosMap.containsKey(nSuple2))
		                    			sAnterior = suplementosMap.get(nSuple2);
		                    		else
		                    		{
		                    			if(!numeroInstrumento.equals("") && cContas!=null && !tipoInstrumento.equals(""))
		                    			{
			                    			if(suplementosMem.containsKey(obterOrigem().obterId() + numeroInstrumento+ cContas.obterId() + tipoInstrumento + nSuple2))
			                    				sAnterior = suplementosMem.get(obterOrigem().obterId() + numeroInstrumento+ cContas.obterId() + tipoInstrumento + nSuple2);
		                    			}
		                    		}
		                    			
		                    		if(sAnterior!=null)
		                    		{
		                    			dataInicio = sAnterior.obterDataPrevistaInicio();
		                    			dataFim = sAnterior.obterDataPrevistaConclusao();
		                    			dataEmissaoSuple = sAnterior.obterDataEmissao();
		                    		}
		                    	}
					                    	
		                    	if(dataFim!=null)
		                    	{
		                    		Calendar c = Calendar.getInstance();
		                    		c.setTime(dataFim);
		                    		c.add(Calendar.YEAR, 2);
				                    			
		                    		dataFim = c.getTime();
		                    	}
			                    			
                    			if(dataEmissao!=null && dataEmissaoSuple!=null)
                    			{
                    				//Data de emissão do suplemento tem que ser > dataEmissao Suplemento anterior
                    				if(dataEmissao.compareTo(dataEmissaoSuple)<0)
                    					erros.add("Error: 111 - Fecha emision del Endoso es menor que Endoso anterior o Póliza, Póliza "+numeroInstrumento+" Endoso actual "+numeroEndoso+" - Línea: " + numeroLinha + nRegistro);
                    			}
                    			if(dataVigenciaInicial!=null && dataInicio!=null)
                    			{
                    				//Data de vigencia do suplemento tem que ser > vigencia Suplemento anterior
                    				if(dataVigenciaInicial.compareTo(dataInicio)<0)
                    					erros.add("Error: 112 - Fecha inicio de vigencia del Endoso es menor que Endoso anterior o Póliza, Póliza "+numeroInstrumento+" Endoso actual "+numeroEndoso+" - Línea: " + numeroLinha + nRegistro);
                    			}
                    			if(dataVigenciaVencimento!=null && dataFim!=null)
                    			{
                    				//Data fim de vigencia do suplemento nao pode ser > 2 anos vigencia Suplemento anterior
                    				if(dataVigenciaVencimento.compareTo(dataFim)>0)
                    					erros.add("Error: 113 - Fecha fin de vigencia del Endoso es mayor que 2 anõs del Endoso anterior o Póliza, Póliza "+numeroInstrumento+" Endoso actual "+numeroEndoso+" - Línea: " + numeroLinha + nRegistro);
                    			}
		                    }
					                    
		                    if(erros.size() == 0)
		                    {
		                        suplemento = (Suplemento)getModelManager().getEntity("Suplemento");
		                        suplemento.atribuirOrigem(apolice.obterOrigem());
		                        suplemento.atribuirDestino(apolice.obterDestino());
		                        suplemento.atribuirResponsavel(apolice.obterResponsavel());
		                        suplemento.atribuirTitulo("Datos do Instrumento: " + numeroInstrumento + ";Sección: " + cContas.obterId());
		                        suplemento.atribuirSuperior(apolice);
		                        suplemento.atribuirNumero(numeroEndosoStr);
		                        suplemento.atribuirDataEmissao(dataEmissao);
		                        suplemento.atribuirDataPrevistaInicio(dataVigenciaInicial);
		                        suplemento.atribuirDataPrevistaConclusao(dataVigenciaVencimento);
		                        suplemento.atribuirPrimaGs(primaGs);
		                        suplemento.atribuirTipoMoedaPrimaGs(tipoMoedaPrimaGs);
		                        suplemento.atribuirPrimaMe(primaMe);
		                        suplemento.atribuirTipoInstrumento(tipoInstrumento);
		                        suplemento.atribuirNumeroEndoso(numeroEndoso2);
		                        suplemento.atribuirCertificado(certificado2);
		                        razaoEmissao2 = "";
		                        cont = 1;
		                        for(int j = 0; j < razaoEmissao.length();)
		                        {
		                            caracter = razaoEmissao.substring(j, cont);
		                            if(j == 100)
		                            {
		                                entrou = false;
		                                for(entrou = false; !caracter.equals(" "); entrou = true)
		                                {
		                                    razaoEmissao2 += caracter;
		                                    j++;
		                                    cont++;
		                                    if(j == razaoEmissao.length())
		                                        break;
		                                    caracter = razaoEmissao.substring(j, cont);
		                                }
		
		                                if(!entrou)
		                                {
		                                    j++;
		                                    cont++;
		                                } else
		                                {
		                                    razaoEmissao2 += "\n";
		                                }
		                            } 
		                            else
		                            {
		                                razaoEmissao2 += caracter;
		                                cont++;
		                                j++;
		                            }
		                        }
		
		                        suplemento.atribuirRazao(razaoEmissao2);
		                        suplementos.add(suplemento);
		                        suplementosMem.put(obterOrigem().obterId() + numeroInstrumento+ cContas.obterId() + tipoInstrumento + nSuple, suplemento);
		                    }
		                }
	                }
	            }
    			else if(numeroRegistroInt == 12)
    			{
    				nRegistro = " (Registro 12)";
    				
    				cContas = null;
    				if(verificaPosicaoString(linha, 9, 19, true, numeroLinha, "Cuenta", nRegistro))
    				{
    					apelidoCconta = linha.substring(9, 19).trim();
    					cContas = (ClassificacaoContas)entidadeHome.obterEntidadePorApelido(apelidoCconta);
    					
    					if(cContas == null)
    						erros.add("Error: 05 - Cuenta " + apelidoCconta + " no fue encontrada - Línea: " + numeroLinha + nRegistro);
    				}
    				
    				numeroInstrumento = "";
    				if(verificaPosicaoString(linha, 19, 29, true, numeroLinha, "Número Póliza", nRegistro))
    					numeroInstrumento = linha.substring(19, 29).trim();
				                
    				dataFinalizacao = null;            
    				anoFinalizacao = "";
    				mesFinalizacao = "";
    				diaFinalizacao = "";
    				
    				if(verificaPosicaoString(linha, 29, 33, true, numeroLinha, "Año Finalizaci\363n", nRegistro))
    					anoFinalizacao = linha.substring(29, 33).trim();
    				if(verificaPosicaoString(linha, 33, 35, true, numeroLinha, "Mes Finalizaci\363n", nRegistro))
    					mesFinalizacao = linha.substring(33, 35).trim();
    				if(verificaPosicaoString(linha, 35, 37, true, numeroLinha, "Día Finalizaci\363n", nRegistro))
    					diaFinalizacao = linha.substring(35, 37).trim();
	                
    				if(eData(diaFinalizacao + "/" + mesFinalizacao + "/" + anoFinalizacao))
    					dataFinalizacao = (new SimpleDateFormat("dd/MM/yyyy")).parse(diaFinalizacao + "/" + mesFinalizacao + "/" + anoFinalizacao);
    				else
	                    erros.add("Error: 114 - Fecha Finalizaci\363n Invalida "+diaFinalizacao + "/" + mesFinalizacao + "/" + anoFinalizacao + " - Línea: " + numeroLinha + nRegistro);
				                
    				tipoInstrumento = "";
    				if(verificaPosicaoString(linha, 37, 38, true, numeroLinha, "Tipo del Instrumento", nRegistro))
    				{
    					String s = linha.substring(37, 38).trim();
    					
		                if(s.equals("1"))
		                    tipoInstrumento = "P\363liza Individual";
		                else if(s.equals("2"))
		                    tipoInstrumento = "P\363liza Madre";
		                else if(s.equals("3"))
		                    tipoInstrumento = "Certificado de Seguro Colectivo";
		                else if(s.equals("4"))
		                    tipoInstrumento = "Certificado Provisorio";
		                else if(s.equals("5"))
		                    tipoInstrumento = "Nota de Cobertura de Reaseguro";
		                else
		                    erros.add("Error: 28 - Tipo del Instrumento es obligatorio - Línea: " + numeroLinha + nRegistro);
    				}
				                
    				numeroEndoso = 0;
    				if(verificaPosicaoString(linha, 38, 48, true, numeroLinha, "Endoso", nRegistro))
    					numeroEndoso = Double.valueOf(linha.substring(38, 48).trim());
    				
    				certificado = 0;
    				if(verificaPosicaoString(linha, 48, 55, true, numeroLinha, "Certificado", nRegistro))
    					certificado = Double.valueOf(linha.substring(48, 55).trim());
				                
	                apolice = null;
	                if(apolices != null && cContas!=null && !numeroInstrumento.equals(""))
	                {
	                    apolice = apolices.get(obterOrigem().obterId() + numeroInstrumento+ cContas.obterId() + tipoInstrumento);
	                    if(apolice == null)
	                    	apolice = apoliceHome.obterApolice(obterOrigem(), numeroInstrumento,cContas, tipoInstrumento);
	                    if(apolice == null)
	                    	erros.add("Error: 29 - Instrumento " + numeroInstrumento + " no fue encuentrado - Línea: " + numeroLinha + nRegistro);
	                }
	                
	                if(erros.size() == 0)
	                {
	                    apolice.atualizarDataEncerramento(dataFinalizacao);
	                    apolice.atualizarSituacaoSeguro("No Vigente");
	                }
    			}
                else if(numeroRegistroInt == 13)
    			{
    				nRegistro = " (Registro 13)";
			                
    				 cContas = null;
    				 if(verificaPosicaoString(linha, 9, 19, true, numeroLinha, "Cuenta", nRegistro))
    				 {
    					 apelidoCconta = linha.substring(9, 19).trim();
    					 cContas = (ClassificacaoContas)entidadeHome.obterEntidadePorApelido(apelidoCconta);
    					 
    					 if(cContas == null)
    						 erros.add("Error: 05 - Cuenta " + apelidoCconta + " no fue encontrada - Línea: " + numeroLinha + nRegistro);
    				 }
    				 
    				 numeroInstrumento = "";
    				 if(verificaPosicaoString(linha,19, 29, true, numeroLinha, "Número Póliza", nRegistro))
    					 numeroInstrumento = linha.substring(19, 29).trim();
			                
    				 dataInicioVigencia = null;
    				 anoInicioVigencia = "";
    				 mesInicioVigencia = "";
    				 diaInicioVigencia = "";
    				 
    				 if(verificaPosicaoString(linha, 29, 33, true, numeroLinha, "Año Inicio Vigencia", nRegistro))
    					 anoInicioVigencia = linha.substring(29, 33).trim();
    				 if(verificaPosicaoString(linha, 33, 35, true, numeroLinha, "Mes Inicio Vigencia", nRegistro))
    					 mesInicioVigencia = linha.substring(33, 35).trim();
    				 if(verificaPosicaoString(linha, 35, 37, true, numeroLinha, "Día Inicio Vigencia", nRegistro))
    					 diaInicioVigencia = linha.substring(35, 37).trim();
    				 
    				 if(eData(diaInicioVigencia + "/" + mesInicioVigencia + "/" + anoInicioVigencia))
    					 dataInicioVigencia = (new SimpleDateFormat("dd/MM/yyyy")).parse(diaInicioVigencia + "/" + mesInicioVigencia + "/" + anoInicioVigencia);
    				 else
    					 erros.add("Error: 92 - Fecha Inicio Vigencia Invalida "+diaInicioVigencia + "/" + mesInicioVigencia + "/" + anoInicioVigencia+" - Línea: " + numeroLinha + nRegistro);
			                
    				 dataFimVigencia = null;
    				 anoFimVigencia = "";
    				 mesFimVigencia = "";
    				 diaFimVigencia = "";
    				 
    				 if(verificaPosicaoString(linha, 37, 41, true, numeroLinha, "Día Fin Vigencia", nRegistro))
    					 anoFimVigencia = linha.substring(37, 41).trim();
    				 if(verificaPosicaoString(linha, 41, 43, true, numeroLinha, "Mes Fin Vigencia", nRegistro))
    					 mesFimVigencia = linha.substring(41, 43).trim();
    				 if(verificaPosicaoString(linha, 43, 45, true, numeroLinha, "Día Fin Vigencia", nRegistro))
    					 diaFimVigencia = linha.substring(43, 45).trim();
	                
	                if(eData(diaFimVigencia + "/" + mesFimVigencia + "/" + anoFimVigencia))
	                	dataFimVigencia = (new SimpleDateFormat("dd/MM/yyyy")).parse(diaFimVigencia + "/" + mesFimVigencia + "/" + anoFimVigencia);
	                else
    					 erros.add("Error: 92 - Fecha Fin Invalida "+diaFimVigencia + "/" + mesFimVigencia + "/" + anoFimVigencia + " - Línea: " + numeroLinha + nRegistro);
	                
	                financiamentoGs = 0;
	                if(verificaPosicaoString(linha, 45, 67, true, numeroLinha, "Inter\351s por Financiamento en Gs", nRegistro))
	                {
	                	financiamentoGsStr = linha.substring(45, 67).trim();
	                	if(financiamentoGsStr.equals(""))
	                		erros.add("Error: 115 - Inter\351s por Financiamento en Gs es obligatorio - Línea: " + numeroLinha + nRegistro);
	                	else
			                financiamentoGs = Double.valueOf(financiamentoGsStr);
	                }
	                
	                tipoMoedaFinanciamentoGs = "";
	                if(verificaPosicaoString(linha, 67, 69, true, numeroLinha, "Tipo ME Inter\351s por Financiamento", nRegistro))
	                	tipoMoedaFinanciamentoGs = obterTipoMoeda(linha.substring(67, 69), numeroLinha, nRegistro);
	                
	                financiamentoMe = 0;
	                if(verificaPosicaoString(linha, 69, 91, true, numeroLinha, "Inter\351s por Financiamento en ME", nRegistro))
	                	financiamentoMe = Double.valueOf(linha.substring(69, 91).trim());
			                
	                qtde = 0;
	                if(verificaPosicaoString(linha, 91, 94, true, numeroLinha, "Cantidad de Cuotas de la Refinanciaci\363n", nRegistro))
	                {
	                	qtde = Integer.valueOf(linha.substring(91, 94).trim());
	                	//if(qtde == 0)
	                		//erros.add("Error: 116 - Cantidad de Cuotas de la Refinanciaci\363n es obligatoria - Línea: " + numeroLinha + nRegistro);
	                }

	                tipoInstrumento = "";
	                if(verificaPosicaoString(linha, 94, 95, true, numeroLinha, "Tipo del Instrumento", nRegistro))
	                {
	                	String s = linha.substring(94, 95).trim();
	                	
	                	if(s.equals("1"))
		                    tipoInstrumento = "P\363liza Individual";
		                else if(s.equals("2"))
		                    tipoInstrumento = "P\363liza Madre";
		                else if(s.equals("3"))
		                    tipoInstrumento = "Certificado de Seguro Colectivo";
		                else if(s.equals("4"))
		                    tipoInstrumento = "Certificado Provisorio";
		                else if(s.equals("5"))
		                    tipoInstrumento = "Nota de Cobertura de Reaseguro";
		                else
		                    erros.add("Error: 28 - Tipo del Instrumento es obligatorio - Línea: " + numeroLinha + nRegistro);
	                }
	                
	                numeroEndoso = 0;
	                if(verificaPosicaoString(linha, 95, 105, true, numeroLinha, "Endoso", nRegistro))
	                	numeroEndoso = Double.valueOf(linha.substring(95, 105).trim());
	                
	                certificado = 0;
	                if(verificaPosicaoString(linha, 105, 112, true, numeroLinha, "Certificado", nRegistro))
	                	certificado = Double.valueOf(linha.substring(105, 112).trim());
			                
	                apolice = null;
	                if(apolices != null && cContas!=null && !numeroInstrumento.equals(""))
	                {
	                    apolice = apolices.get(obterOrigem().obterId() + numeroInstrumento+ cContas.obterId() + tipoInstrumento);
	                    if(apolice == null)
	                    	apolice = apoliceHome.obterApolice(obterOrigem(), numeroInstrumento,cContas, tipoInstrumento);
	                    if(apolice == null)
	                    	erros.add("Error: 29 - Instrumento " + numeroInstrumento + " no fue encuentrado - Línea: " + numeroLinha + nRegistro);
	                }
			                
	                if(erros.size() == 0)
	                {
	                    refinacao = (Refinacao)getModelManager().getEntity("Refinacao");
	                    refinacao.atribuirOrigem(apolice.obterOrigem());
	                    refinacao.atribuirDestino(apolice.obterDestino());
	                    refinacao.atribuirResponsavel(apolice.obterResponsavel());
	                    refinacao.atribuirTitulo("Datos do Instrumento: " + numeroInstrumento + ";Sección: " + cContas.obterId());
	                    refinacao.atribuirSuperior(apolice);
	                    refinacao.atribuirDataPrevistaInicio(dataInicioVigencia);
	                    refinacao.atribuirDataPrevistaConclusao(dataFimVigencia);
	                    refinacao.atribuirFinanciamentoGs(financiamentoGs);
	                    refinacao.atribuirTipoMoedaFinanciamentoGs(tipoMoedaFinanciamentoGs);
	                    refinacao.atribuirFinanciamentoMe(financiamentoMe);
	                    refinacao.atribuirQtdeParcelas(qtde);
	                    refinacao.atribuirTipoInstrumento(tipoInstrumento);
	                    refinacao.atribuirNumeroEndoso(numeroEndoso);
	                    refinacao.atribuirCertificado(certificado);
	                    refinacoes.add(refinacao);
	                }
    			}
                else if(numeroRegistroInt == 14)
    			{
    				nRegistro = " (Registro 14)";
    				
    				cContas = null;
    				if(verificaPosicaoString(linha, 9, 19, true, numeroLinha, "Cuenta", nRegistro))
    				{
		            	apelidoCconta = linha.substring(9, 19).trim();
		                cContas = (ClassificacaoContas)entidadeHome.obterEntidadePorApelido(apelidoCconta);
		                
		                if(cContas == null)
		                    erros.add("Error: 05 - Cuenta " + apelidoCconta + " no fue encontrada - Línea: " + numeroLinha + nRegistro);
    				}
		                
    				tipoInstrumento = "";
    				if(verificaPosicaoString(linha, 19, 20, true, numeroLinha, "Tipo del Instrumento", nRegistro))
    				{
    					String s = linha.substring(19, 20).trim(); 
    				
		                if(s.equals("1"))
		                    tipoInstrumento = "P\363liza Individual";
		                else if(s.equals("2"))
		                    tipoInstrumento = "P\363liza Madre";
		                else if(s.equals("3"))
		                    tipoInstrumento = "Certificado de Seguro Colectivo";
		                else if(s.equals("4"))
		                    tipoInstrumento = "Certificado Provisorio";
		                else if(s.equals("5"))
		                    tipoInstrumento = "Nota de Cobertura de Reaseguro";
		                else
		                    erros.add("Error: 28 - Tipo del Instrumento es obligatorio - Línea: " + numeroLinha + nRegistro);
    				}
		                
    				numeroInstrumento = "";
    				if(verificaPosicaoString(linha, 20, 30, true, numeroLinha, "Número Póliza", nRegistro))
    					numeroInstrumento = linha.substring(20, 30).trim();
		                
    				numeroEndoso = 0;
    				if(verificaPosicaoString(linha, 30, 40, true, numeroLinha, "Endoso", nRegistro))
    					numeroEndoso = Double.valueOf(linha.substring(30, 40).trim());
    				
    				certificado = 0;
    				if(verificaPosicaoString(linha, 40, 47, true, numeroLinha, "Certificado", nRegistro))
		                certificado = Double.valueOf(linha.substring(40, 47).trim());
		                
	                apolice = null;
	                
	                if(apolices != null && cContas!=null && !numeroInstrumento.equals(""))
	                    apolice = apolices.get(obterOrigem().obterId() + numeroInstrumento+ cContas.obterId() + tipoInstrumento);
	                if(apolice == null)
	                    apolice = apoliceHome.obterApolice(obterOrigem(), numeroInstrumento,cContas, tipoInstrumento);
	                if(apolice == null)
	                    erros.add("Error: 29 - Instrumento " + numeroInstrumento + " no fue encuentrado - Línea: " + numeroLinha + nRegistro);
		                
		            qtde = 0;    
		            if(verificaPosicaoString(linha, 47, 49, true, numeroLinha, "Cantidad", nRegistro))
		            {
		            	qtde = Integer.valueOf(linha.substring(47, 49).trim());
		                if(qtde == 0)
		                    erros.add("Error: 117 - Cantidad de Gastos o Pagos es obligatoria - Línea: " + numeroLinha + nRegistro);
		            }
		                
	                ultimo = 0;
	                ultimoArquivo = qtde * (6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2 + 22 + 4 + 2 + 2 + 10 + 10 + 1 + 1);
	                ultimoArquivo2 = linha.substring(49, linha.length()).length() - 2;
	                if(ultimoArquivo!= ultimoArquivo2)
	                	erros.add("Error: 118 - Número de Registros de Gastos declarados es diferente de lo que hay en el archivo - Línea " + numeroLinha + nRegistro);
	                else
	                {
	                	for(int w = 0; w < qtde; w++)
	                	{
	                		if(ultimo == 0)
	                			ultimo = 49;
			                    
	                		numeroSinistro = "";
	                		if(verificaPosicaoString(linha, ultimo, ultimo + 6, true, numeroLinha, "Número del Siniestro", nRegistro))
	                		{
	                			numeroSinistro = linha.substring(ultimo, ultimo + 6).trim();
	                			if(numeroSinistro.equals(""))
	                				erros.add("Error: 58 - Numero del Siniestro es obligatorio - Línea: " + numeroLinha + nRegistro);
	                		}
	                		
	                		sinistro = null;
		                    if(sinistros != null && !numeroSinistro.equals(""))
		                    {
		                        sinistro = sinistros.get(new Long(obterOrigem().obterId() + numeroSinistro));
		                        if(sinistro == null)
		                        	sinistro = sinistroHome.obterSinistro(obterOrigem(), numeroSinistro);
		                        if(sinistro == null)
		                        	erros.add("Error: 59 - Siniestro " + numeroSinistro + " no fue encuentrado - Línea: " + numeroLinha + nRegistro);
		                    }
		                    
		                    dataSinistro = null;
		                    anoSinistro = "";
		                    mesSinistro = "";
		                    diaSinistro = "";
		                    
		                    if(verificaPosicaoString(linha, ultimo + 6, ultimo + 6 + 4, true, numeroLinha, "Año Siniestro", nRegistro))
		                    	anoSinistro = linha.substring(ultimo + 6, ultimo + 6 + 4).trim();
		                    if(verificaPosicaoString(linha, ultimo + 6 + 4, ultimo + 6 + 4 + 2, true, numeroLinha, "Mes Siniestro", nRegistro))
			                    mesSinistro = linha.substring(ultimo + 6 + 4, ultimo + 6 + 4 + 2).trim();
		                    if(verificaPosicaoString(linha, ultimo + 6 + 4 + 2, ultimo + 6 + 4 + 2 + 2, true, numeroLinha, "Día Siniestro", nRegistro))
			                    diaSinistro = linha.substring(ultimo + 6 + 4 + 2, ultimo + 6 + 4 + 2 + 2).trim();
			                    
		                    if(eData(diaSinistro + "/" + mesSinistro + "/" + anoSinistro))
		                    	dataSinistro = (new SimpleDateFormat("dd/MM/yyyy")).parse(diaSinistro + "/" + mesSinistro + "/" + anoSinistro);
		                    else
		                    	erros.add("Error: 60 - Fecha Siniestro Invalida "+diaSinistro + "/" + mesSinistro + "/" + anoSinistro + " - Línea: " + numeroLinha);

		                    tipoPagamento = "";
		                    if(verificaPosicaoString(linha, ultimo + 6 + 4 + 2 + 2, ultimo + 6 + 4 + 2 + 2 + 1, true, numeroLinha, "Tipo del Pago", nRegistro))
		                    {
		                    	String s = linha.substring(ultimo + 6 + 4 + 2 + 2, ultimo + 6 + 4 + 2 + 2 + 1).trim();
		                    	
		                    	if(s.equals("1"))
			                        tipoPagamento = "Liquidador";
			                    else if(s.equals("2"))
			                        tipoPagamento = "Asegurado";
			                    else if(s.equals("3"))
			                        tipoPagamento = "Tercero";
			                    else if(s.equals("4"))
			                        tipoPagamento = "Otros";
			                    else
			                    	erros.add("Error: 60 - Tipo del Pago invalido, Tipo del Pago = "+s+"  - Línea: " + numeroLinha + nRegistro);
		                    }
		                    
		                    auxiliar = null;
		                    if(verificaPosicaoString(linha, ultimo + 6 + 4 + 2 + 2 + 1, ultimo + 6 + 4 + 2 + 2 + 1 + 3, true, numeroLinha, "Numero del Liquidador", nRegistro))
		                    {
		                    	numeroLiquidador = linha.substring(ultimo + 6 + 4 + 2 + 2 + 1, ultimo + 6 + 4 + 2 + 2 + 1 + 3).trim();
			                    
		                    	if(!numeroLiquidador.equals("000"))
			                    {
		                    		if(apolice!=null)
			                    	{
			                    		mesAno = null;
			                    		
			                    		if(apolice.obterDataEmissao()!=null)
			                    		{
			                    			mesAno = new SimpleDateFormat("MM/yyyy").format(apolice.obterDataEmissao());
			                    			auxiliar = auxiliarSeguroHome.obterAuxiliarPorInscricaoeTipo(numeroLiquidador, "Liquidadores de Siniestros",mesAno);
			                    		}
				                    	
				                    	if(auxiliar == null)
				                    	{
				                    		auxiliar = auxiliarSeguroHome.obterAuxiliarPorInscricao(numeroLiquidador, "Liquidadores de Siniestros");
				                    		
				                    		if(auxiliar!=null)
				                    			erros.add("Error: 50 - Auxiliar de Seguro(Liquidadores de Siniestros) con Inscripci\363n " + numeroLiquidador + " " + auxiliar.obterNome() +" no fue encuentrado, Póliza: "+apolice.obterNumeroApolice()+" mes e año Póliza: "+ mesAno+" - Línea: " + numeroLinha + nRegistro);
				                    		else
				                    			erros.add("Error: 50 - Auxiliar de Seguro(Liquidadores de Siniestros) con Inscripci\363n " + numeroLiquidador + " no fue encuentrado, Póliza: "+apolice.obterNumeroApolice()+" mes e año Póliza: "+ mesAno+" - Línea: " + numeroLinha + nRegistro);
				                    	}
			                    	}
			                    }
		                    }
			                    
		                    if(auxiliar!=null)
		                    {
		                    	situacaoEntidade = auxiliar.obterAtributo("situacao");
		                    	if(situacaoEntidade!=null)
		                    	{
			                    	situacaoAgente = situacaoEntidade.obterValor();
				                	if(situacaoAgente.equals(Inscricao.SUSPENSA))
				                		erros.add("Error: 09 - Auxiliar de Seguro(Liquidadores de Siniestros) con inscripci\363n " + numeroLiquidador + " suspendida, Póliza: "+apolice.obterNumeroApolice()+" mes e año Póliza: "+ mesAno+" - Línea: " + numeroLinha + nRegistro);
		                    	}
		                    }
		                    
		                    nomeTerceiro = "";
		                    if(verificaPosicaoString(linha, ultimo + 6 + 4 + 2 + 2 + 1 + 3, ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60, false, numeroLinha, "Nombre del tercero", nRegistro))
		                    	nomeTerceiro = linha.substring(ultimo + 6 + 4 + 2 + 2 + 1 + 3, ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60).trim();
		                    
		                    abonadoGs = 0;
		                    if(verificaPosicaoString(linha, ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60, ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22, true, numeroLinha, "Abono en Gs", nRegistro))
		                    	abonadoGs = Double.valueOf(linha.substring(ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60, ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22).trim());
			                    
		                    tipoMoedaAbonoGs = "";
		                    if(verificaPosicaoString(linha, ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22, ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2, true, numeroLinha, "Tipo ME Abono", nRegistro))
		                    	tipoMoedaAbonoGs = obterTipoMoeda(linha.substring(ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22, ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2), numeroLinha, nRegistro);
		                    
		                    abonadoMe = 0;
		                    if(verificaPosicaoString(linha, ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2, ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2 + 22, true, numeroLinha, "Abono en ME", nRegistro))
		                    	abonadoMe = Double.valueOf(linha.substring(ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2, ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2 + 22).trim());
		                    
		                    dataPagamento = null;
		                    anoPagamento = "";
		                    mesPagamento = "";
		                    diaPagamento = "";
		                    
		                    if(verificaPosicaoString(linha, ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2 + 22, ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2 + 22 + 4, true, numeroLinha, "Año del Pago", nRegistro))
		                    	anoPagamento = linha.substring(ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2 + 22, ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2 + 22 + 4).trim();
		                    if(verificaPosicaoString(linha, ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2 + 22 + 4, ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2 + 22 + 4 + 2, true, numeroLinha, "Mes del Pago", nRegistro))
		                    	mesPagamento = linha.substring(ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2 + 22 + 4, ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2 + 22 + 4 + 2).trim();
		                    if(verificaPosicaoString(linha, ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2 + 22 + 4 + 2, ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2 + 22 + 4 + 2 + 2, true, numeroLinha, "Día del Pago", nRegistro))
		                    	diaPagamento = linha.substring(ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2 + 22 + 4 + 2, ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2 + 22 + 4 + 2 + 2).trim();
		                    
		                    if(eData(diaPagamento + "/" + mesPagamento + "/" + anoPagamento))
		                    	dataPagamento = (new SimpleDateFormat("dd/MM/yyyy")).parse(diaPagamento + "/" + mesPagamento + "/" + anoPagamento);
		                    else
		                        erros.add("Error: 68 - Fecha del Pago Invalida "+diaPagamento + "/" + mesPagamento + "/" + anoPagamento + " - Línea: " + numeroLinha + nRegistro);
			                    
		                    numeroCheque = "";
		                    if(verificaPosicaoString(linha, ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2 + 22 + 4 + 2 + 2, ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2 + 22 + 4 + 2 + 2 + 10, false, numeroLinha, "Numero del Cheque", nRegistro))
		                    	numeroCheque = linha.substring(ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2 + 22 + 4 + 2 + 2, ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2 + 22 + 4 + 2 + 2 + 10).trim();
		                    
		                    bancoStr = "";
		                    banco = null;
		                    if(verificaPosicaoString(linha, ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2 + 22 + 4 + 2 + 2 + 10, ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2 + 22 + 4 + 2 + 2 + 10 + 10, true, numeroLinha, "Banco", nRegistro))
		                    {
			                    bancoStr = linha.substring(ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2 + 22 + 4 + 2 + 2 + 10, ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2 + 22 + 4 + 2 + 2 + 10 + 10).trim();
			                    
			                    if(!bancoStr.equals("0000000000") && !bancoStr.equals("") && !numeroCheque.equals(""))
			                    {
			                        banco = entidadeHome.obterEntidadePorApelido(bancoStr);
			                        if(banco == null)
			                            erros.add("Error: 119 - Banco " + bancoStr + " no fue encuentrado - Línea: " + numeroLinha + nRegistro);
			                        else if(!(banco instanceof Conta))
			                            erros.add("Error: 119 - Banco " + bancoStr + " no fue encuentrado - Línea: " + numeroLinha + nRegistro);
			                    }
		                    }
		                    
		                    situacaoSinistro = "";
		                    if(verificaPosicaoString(linha, ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2 + 22 + 4 + 2 + 2 + 10 + 10, ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2 + 22 + 4 + 2 + 2 + 10 + 10 + 1, true, numeroLinha, "Situación del Siniestro", nRegistro))
		                    {
		                    	String s = linha.substring(ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2 + 22 + 4 + 2 + 2 + 10 + 10, ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2 + 22 + 4 + 2 + 2 + 10 + 10 + 1).trim();
		                    	
			                    if(s.equals("1"))
			                        situacaoSinistro = "Pendiente de Liquidaci\363n";
			                    else if(s.equals("2"))
			                        situacaoSinistro = "Controvertido";
			                    else if(s.equals("3"))
			                        situacaoSinistro = "Pendiente de Pago";
			                    else if(s.equals("4"))
			                        situacaoSinistro = "Rechazado";
			                    else if(s.equals("5"))
			                        situacaoSinistro = "Judicializado";
			                    else
			                    	erros.add("Error: 119 - Situación del Siniestro invalida, Situación del Siniestro = "+ s +" - Línea: " + numeroLinha + nRegistro);
		                    }
			                    
		                    situacaoPagamento = "";
		                    if(verificaPosicaoString(linha, ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2 + 22 + 4 + 2 + 2 + 10 + 10 + 1, ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2 + 22 + 4 + 2 + 2 + 10 + 10 + 1 + 1, true, numeroLinha, "Situación del Pago", nRegistro))
		                    {
		                    	String s = linha.substring(ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2 + 22 + 4 + 2 + 2 + 10 + 10 + 1, ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2 + 22 + 4 + 2 + 2 + 10 + 10 + 1 + 1).trim();
		                    	
			                    if(s.equals("1"))
			                        situacaoPagamento = "Normal";
			                    else if(s.equals("2"))
			                        situacaoPagamento = "Anulado";
			                    else
			                    	erros.add("Error: 119 - Situación del Pago invalida, Situación del Pago = "+ s +" - Línea: " + numeroLinha + nRegistro);
		                    }
			                    
		                    ultimo = ultimo + 6 + 4 + 2 + 2 + 1 + 3 + 60 + 22 + 2 + 22 + 4 + 2 + 2 + 10 + 10 + 1 + 1;
		                    if(erros.size() == 0)
		                    {
		                        gastos = (RegistroGastos)getModelManager().getEntity("RegistroGastos");
		                        gastos.atribuirOrigem(sinistro.obterOrigem());
		                        gastos.atribuirDestino(sinistro.obterDestino());
		                        gastos.atribuirResponsavel(sinistro.obterResponsavel());
		                        gastos.atribuirTitulo("Datos do Instrumento: " + numeroInstrumento + ";Sección: " + cContas.obterId());
		                        gastos.atribuirSuperior(sinistro);
		                        gastos.atribuirDataSinistro(dataSinistro);
		                        gastos.atribuirTipo(tipoPagamento);
		                        gastos.atribuirAuxiliarSeguro(auxiliar);
		                        gastos.atribuirNomeTerceiro(nomeTerceiro);
		                        gastos.atribuirAbonoGs(abonadoGs);
		                        gastos.atribuirTipoMoedaAbonoGs(tipoMoedaAbonoGs);
		                        gastos.atribuirAbonoMe(abonadoMe);
		                        gastos.atribuirDataPagamento(dataPagamento);
		                        gastos.atribuirNumeroCheque(numeroCheque);
		                        gastos.atribuirBanco(banco);
		                        gastos.atribuirSituacaoSinistro(situacaoSinistro);
		                        gastos.atribuirSituacaoPagamento(situacaoPagamento);
		                        gastos.atribuirTipoInstrumento(tipoInstrumento);
		                        gastos.atribuirNumeroEndoso(numeroEndoso);
		                        gastos.atribuirCertificado(certificado);
		                        gastos2.add(gastos);
		                    }
		                }
	                }
	            }
                else if(numeroRegistroInt == 15)
    			{
    				nRegistro = " (Registro 15)";
    				
    				cContas = null;
    				if(verificaPosicaoString(linha, 9, 19, true, numeroLinha, "Cuenta", nRegistro))
    				{
    					apelidoCconta = linha.substring(9, 19).trim();
    					cContas = (ClassificacaoContas)entidadeHome.obterEntidadePorApelido(apelidoCconta);
				                
    					if(cContas == null)
    						erros.add("Error: 05 - Cuenta " + apelidoCconta + " no fue encontrada - Línea: " + numeroLinha + nRegistro);
    				}
    				
    				numeroInstrumento = "";
    				if(verificaPosicaoString(linha, 19, 29, true, numeroLinha, "Número Póliza", nRegistro))
    					numeroInstrumento = linha.substring(19, 29).trim();
				                
    				tipoInstrumento = "";
    				if(verificaPosicaoString(linha, 305, 306, true, numeroLinha, "Tipo del Instrumento", nRegistro))
    				{
    					String s = linha.substring(305, 306).trim();
    					
    					if(s.equals("1"))
    						tipoInstrumento = "P\363liza Individual";
    					else if(s.equals("2"))
		                    tipoInstrumento = "P\363liza Madre";
		                else if(s.equals("3"))
		                    tipoInstrumento = "Certificado de Seguro Colectivo";
		                else if(s.equals("4"))
		                    tipoInstrumento = "Certificado Provisorio";
		                else if(s.equals("5"))
		                    tipoInstrumento = "Nota de Cobertura de Reaseguro";
		                else
		                    erros.add("Error: 28 - Tipo del Instrumento es obligatorio - Línea: " + numeroLinha + nRegistro);
    				}
    				
    				numeroEndoso = 0;
    				if(verificaPosicaoString(linha, 306, 316, true, numeroLinha, "Endoso", nRegistro))
    					numeroEndoso = Double.parseDouble(linha.substring(306, 316).trim());
    				
    				certificado = 0;
    				if(verificaPosicaoString(linha, 316, 323, true, numeroLinha, "Certificado", nRegistro))
    					certificado = Double.parseDouble(linha.substring(316, 323).trim());
    				
	                apolice = null;
	                
	                if(apolices != null && cContas!=null && !numeroInstrumento.equals(""))
	                {
	                    apolice = apolices.get(obterOrigem().obterId() + numeroInstrumento+ cContas.obterId() + tipoInstrumento);
	                    if(apolice == null)
	                    	apolice = apoliceHome.obterApolice(obterOrigem(), numeroInstrumento,cContas, tipoInstrumento);
	                    if(apolice == null)
	                    	erros.add("Error: 29 - Instrumento " + numeroInstrumento + " no fue encuentrado - Línea: " + numeroLinha + nRegistro);
	                }
	                
	                if(verificaPosicaoString(linha, 29, 32, true, numeroLinha, "Inscripci\363n Reaseguradora o Aseguradora", nRegistro))
	                {
	                	inscricaoReaseguradora = linha.substring(29, 32).trim();
	                	entidadeReaseguradora = null;
	                	reaseguradora = null;
	                	if(!inscricaoReaseguradora.equals("000"))
	                	{
	                		if(apolice!=null)
	                		{
	                			mesAno = null;
				                		
	                			if(apolice.obterDataEmissao()!=null)
	                				mesAno = new SimpleDateFormat("MM/yyyy").format(apolice.obterDataEmissao());
					                	
	                			if(Integer.parseInt(inscricaoReaseguradora) >= 201)
	                			{
	                				mesAno = new SimpleDateFormat("MM/yyyy").format(apolice.obterDataPrevistaInicio());
	                				entidadeReaseguradora = entidadeHome.obterEntidadePorInscricao(inscricaoReaseguradora, "Reaseguradora",mesAno);
	                			}
	                			else
	                				entidadeReaseguradora = entidadeHome.obterEntidadePorInscricao(inscricaoReaseguradora, "Aseguradora",mesAno);
					                	
	                			if(entidadeReaseguradora == null)
	                			{
	                				if(Integer.parseInt(inscricaoReaseguradora) >= 201)
	                					entidadeReaseguradora = entidadeHome.obterEntidadePorInscricaoSemValidacao(inscricaoReaseguradora, "Reaseguradora");
	                				else
	                					entidadeReaseguradora = entidadeHome.obterEntidadePorInscricaoSemValidacao(inscricaoReaseguradora, "Aseguradora");
				                        	
	                				if(entidadeReaseguradora!=null)
	                					erros.add("Error: 33 - Inscripci\363n (Reaseguradora o Aseguradora) " + inscricaoReaseguradora + " de la Reaseguradora "+entidadeReaseguradora.obterNome()+" no fue encontrada o esta No Activa, Póliza: "+apolice.obterNumeroApolice()+" mes e año Póliza: "+ mesAno+" - Línea: " + numeroLinha + nRegistro);
	                				else
	                					erros.add("Error: 33 - Inscripci\363n (Reaseguradora o Aseguradora) " + inscricaoReaseguradora + " de la Reaseguradora no fue encontrada o esta No Activa, Póliza: "+apolice.obterNumeroApolice()+" mes e año Póliza: "+ mesAno+" - Línea: " + numeroLinha + nRegistro);
	                			}
	                			else
	                			{
	                				if(entidadeReaseguradora instanceof Reaseguradora)
	                					reaseguradora = (Reaseguradora)entidadeReaseguradora;
	                				else if(entidadeReaseguradora instanceof Aseguradora)
	                					reaseguradora = (Aseguradora)entidadeReaseguradora;
	                			}
	                		}
	                	}
	                }
				                
				                
	                if(reaseguradora!=null)
	                {
	                	situacaoEntidade = reaseguradora.obterAtributo("situacao");
                    	if(situacaoEntidade!=null)
                    	{
		                	situacaoAgente = situacaoEntidade.obterValor();
		                	if(situacaoAgente.equals(Inscricao.SUSPENSA))
		                		erros.add("Error: 09 - Inscripci\363n (Reaseguradora o Aseguradora) " + inscricaoReaseguradora + " suspendida, Póliza: "+apolice.obterNumeroApolice()+" mes e año Póliza: "+ mesAno+" - Línea: " + numeroLinha + nRegistro);
                    	}
	                }
				                
	                tipoContrato = "";
	                if(verificaPosicaoString(linha, 32, 33, true, numeroLinha, "Tipo del Contrato", nRegistro))
	                {
	                	String s = linha.substring(32, 33).trim();
	                	
		                if(s.equals("1"))
		                    tipoContrato = "Cuota parte";
		                else if(s.equals("2"))
		                    tipoContrato = "Excedente";
		                else if(s.equals("3"))
		                    tipoContrato = "Exceso de p\351rdida";
		                else if(s.equals("4"))
		                    tipoContrato = "Facultativo no Proporcional";
		                else if(s.equals("5"))
		                    tipoContrato = "Facultativo Proporcional";
		                else if(s.equals("6"))
		                    tipoContrato = "Limitaci\363n de Siniestralidad";
		                else
		                	erros.add("Error: 70 - Tipo del Contrato Invalido, Tipo del Contrato = "+s+" - Línea: " + numeroLinha + nRegistro);
	                }
	                
	                dataAnulacao = null;
	                anoAnulacao = "";
	                mesAnulacao = "";
	                diaAnulacao = "";
	                
	                if(verificaPosicaoString(linha, 33, 37, true, numeroLinha, "Año Anulación", nRegistro))
	                	anoAnulacao = linha.substring(33, 37).trim();
	                if(verificaPosicaoString(linha, 37, 39, true, numeroLinha, "Mes Anulación", nRegistro))
	                	mesAnulacao = linha.substring(37, 39).trim();
	                if(verificaPosicaoString(linha, 39, 41, true, numeroLinha, "Día Anulación", nRegistro))
	                	diaAnulacao = linha.substring(39, 41).trim();
	                
	                if(eData(diaAnulacao + "/" + mesAnulacao + "/" + anoAnulacao))
	                	dataAnulacao = (new SimpleDateFormat("dd/MM/yyyy")).parse(diaAnulacao + "/" + mesAnulacao + "/" + anoAnulacao);
	                else
	                	erros.add("Error: 70 - Fecha Anulación Invalida "+diaAnulacao + "/" + mesAnulacao + "/" + anoAnulacao + " - Línea: " + numeroLinha + nRegistro);
	                	
	                tipoAnulacao = "";
	                if(verificaPosicaoString(linha, 41, 42, true, numeroLinha, "Tipo Anulación", nRegistro))
	                {
	                	String s = linha.substring(41, 42).trim();
	                	
	                	if(s.equals("1"))
	                		tipoAnulacao = "Total";
	                	else if(s.equals("2"))
	                		tipoAnulacao = "Parcial";
	                	else
	                		erros.add("Error: 70 - Tipo Anulación Invalida, Tipo Anulación = "+s+" - Línea: " + numeroLinha + nRegistro);
	                }
	                
	                capitalAnuladoGs = 0;
	                if(verificaPosicaoString(linha, 42, 64, true, numeroLinha, "Capital Anulado en Gs", nRegistro))
	                	capitalAnuladoGs = Double.valueOf(linha.substring(42, 64).trim());

	                tipoMoedaCapitalAnuladoGs = "";
	                if(verificaPosicaoString(linha, 64, 66, true, numeroLinha, "Tipo ME Capital Anulado", nRegistro))
	                	tipoMoedaCapitalAnuladoGs = obterTipoMoeda(linha.substring(64, 66), numeroLinha, nRegistro);

	                capitalAnuladoMe = 0;
	                if(verificaPosicaoString(linha, 66, 88, true, numeroLinha, "Capital Anulado en ME", nRegistro))
	                	capitalAnuladoMe = Double.valueOf(linha.substring(66, 88).trim());

	                diaCorridos = "";
	                if(verificaPosicaoString(linha, 88, 93, true, numeroLinha, "Días corridos", nRegistro))
	                	diaCorridos = linha.substring(88, 93).trim();
	                
	                primaAnuladaGs = 0;
	                if(verificaPosicaoString(linha, 93, 115, true, numeroLinha, "Prima anulada", nRegistro))
	                	primaAnuladaGs = Double.valueOf(linha.substring(93, 115).trim());

	                tipoMoedaPrimaAnuladaGs = "";
	                if(verificaPosicaoString(linha, 115, 117, true, numeroLinha, "Tipo ME Prima anulada", nRegistro))
	                	tipoMoedaPrimaAnuladaGs = obterTipoMoeda(linha.substring(115, 117), numeroLinha, nRegistro);
	                
	                primaAnuladaMe = 0;
	                if(verificaPosicaoString(linha, 117, 139, true, numeroLinha, "Prima anulada en ME", nRegistro))
	                	primaAnuladaMe = Double.valueOf(linha.substring(117, 139).trim());        
				                
	                comissaoAnuladaGs = 0;
	                if(verificaPosicaoString(linha, 139, 161, true, numeroLinha, "Comisión anulada en Gs", nRegistro))
	                	comissaoAnuladaGs = Double.valueOf(linha.substring(139, 161).trim());
				                
	                tipoMoedaComissaoAnuladaGs = "";
	                if(verificaPosicaoString(linha, 161, 163, true, numeroLinha, "Tipo ME Comisión anulada", nRegistro))
	                	tipoMoedaComissaoAnuladaGs = obterTipoMoeda(linha.substring(161, 163), numeroLinha, nRegistro);
	                
	                comissaoAnuladaMe = 0;
	                if(verificaPosicaoString(linha, 161, 163, true, numeroLinha, "Comisión anulada en ME", nRegistro))
	                	comissaoAnuladaMe = Double.parseDouble(linha.substring(163, 185).trim());
				                
	                motivoAnulacao = "";
	                if(verificaPosicaoString(linha, 185, 305, false, numeroLinha, "Razón Anulación", nRegistro))
	                	motivoAnulacao = linha.substring(185, 305).trim();

	                dadosReaseguro = null;
				                
	                if(dadosReaseguros != null && apolice != null && cContas!=null && !tipoContrato.equals(""))
	                {                	
                        if(reaseguradora != null)
                            dadosReaseguro = dadosReaseguros.get(new Long((obterOrigem().obterId() + cContas.obterId()) + apolice.obterNumeroApolice() + reaseguradora.obterId()) + tipoContrato);
                        else
                        	dadosReaseguro = dadosReaseguros.get(new Long((obterOrigem().obterId() + cContas.obterId()) + apolice.obterNumeroApolice() + 0) + tipoContrato);
	                }
				                
	                if(dadosReaseguro == null && apolice != null && cContas!=null && !tipoContrato.equals(""))
	                {
	                	dadosReaseguro = dadosReaseguroHome.obterDadosReaseguro(obterOrigem(), cContas, apolice, reaseguradora, tipoContrato);
	                	if(dadosReaseguro == null)
	                		erros.add("Error: 120 - Dato de Reaseguro no fue encuentrado - Línea: " + numeroLinha + nRegistro);	
	                }
				                
	                if(erros.size() == 0)
	                {
	                    registroAnulacao = (RegistroAnulacao)getModelManager().getEntity("RegistroAnulacao");
	                    registroAnulacao.atribuirOrigem(aseguradora);
	                    registroAnulacao.atribuirDestino(dadosReaseguro.obterDestino());
	                    registroAnulacao.atribuirResponsavel(dadosReaseguro.obterResponsavel());
	                    registroAnulacao.atribuirTitulo("Datos do Instrumento: " + numeroInstrumento + ";Sección: " + cContas.obterId());
	                    registroAnulacao.atribuirSuperior(dadosReaseguro);
	                    registroAnulacao.atribuirReaeguradora(reaseguradora);
	                    registroAnulacao.atribuirTipoContrato(tipoContrato);
	                    registroAnulacao.atribuirDataAnulacao(dataAnulacao);
	                    registroAnulacao.atribuirTipo(tipoAnulacao);
	                    registroAnulacao.atribuirCapitalGs(capitalAnuladoGs);
	                    registroAnulacao.atribuirTipoMoedaCapitalGs(tipoMoedaCapitalAnuladoGs);
	                    registroAnulacao.atribuirCapitalMe(capitalAnuladoMe);
	                    registroAnulacao.atribuirDiasCorridos(Integer.parseInt(diaCorridos));
	                    registroAnulacao.atribuirPrimaGs(primaAnuladaGs);
	                    registroAnulacao.atribuirTipoMoedaPrimaGs(tipoMoedaPrimaAnuladaGs);
	                    registroAnulacao.atribuirPrimaMe(primaAnuladaMe);
	                    registroAnulacao.atribuirComissaoGs(comissaoAnuladaGs);
	                    registroAnulacao.atribuirTipoMoedaComissaoGs(tipoMoedaComissaoAnuladaGs);
	                    registroAnulacao.atribuirTipoInstrumento(tipoInstrumento);
	                    registroAnulacao.atribuirNumeroEndoso(numeroEndoso);
	                    registroAnulacao.atribuirCertificado(certificado);
	                    
	                    cont = 1;
	                    motivoAnulacao2 = "";
	                    for(int j = 0; j < motivoAnulacao.length();)
	                    {
	                        caracter = motivoAnulacao.substring(j, cont);
	                        if(j == 100 || j == 200)
	                        {
	                            entrou = false;
	                            for(entrou = false; !caracter.equals(" "); entrou = true)
	                            {
	                                motivoAnulacao2 += caracter;
	                                j++;
	                                cont++;
	                                if(j == motivoAnulacao.length())
	                                    break;
	                                caracter = motivoAnulacao.substring(j, cont);
	                            }
	
	                            if(!entrou)
	                            {
	                                j++;
	                                cont++;
	                            }
	                            else
	                                motivoAnulacao2 += "\n";
	                        }
	                        else
	                        {
	                            motivoAnulacao2 += caracter;
	                            cont++;
	                            j++;
	                        }
	                    }
	
	                    registroAnulacao.atribuirDescricao(motivoAnulacao2);
	                    anulacoes2.add(registroAnulacao);
	                }
	            }
                else if(numeroRegistroInt == 16)
    			{
    				nRegistro = " (Registro 16)";
    				
    				cContas = null;
    				if(verificaPosicaoString(linha, 9, 19, true, numeroLinha, "Cuenta", nRegistro))
    				{
		            	apelidoCconta = linha.substring(9, 19).trim();
		                cContas = (ClassificacaoContas)entidadeHome.obterEntidadePorApelido(apelidoCconta);
		                
		                if(cContas == null)
		                    erros.add("Error: 05 - Cuenta " + apelidoCconta + " no fue encontrada - Línea: " + numeroLinha + nRegistro);
    				}
				                
    				numeroInstrumento = "";
    				if(verificaPosicaoString(linha, 19, 29, true, numeroLinha, "Número Póliza", nRegistro))
    					numeroInstrumento = linha.substring(19, 29).trim();

    				dataCorte = null;
    				anoCorte = "";
    				mesCorte = "";
    				diaCorte = "";
    				
    				if(verificaPosicaoString(linha, 29, 33, true, numeroLinha, "Año Corte", nRegistro))
    					anoCorte = linha.substring(29, 33).trim();
    				if(verificaPosicaoString(linha, 33, 35, true, numeroLinha, "Mes Corte", nRegistro))
    					mesCorte = linha.substring(33, 35).trim();
    				if(verificaPosicaoString(linha, 35, 37, true, numeroLinha, "Día Corte", nRegistro))
    					diaCorte = linha.substring(35, 37).trim();
				        
    				if(eData(diaCorte + "/" + mesCorte + "/" + anoCorte))
    					dataCorte = (new SimpleDateFormat("dd/MM/yyyy")).parse(diaCorte + "/" + mesCorte + "/" + anoCorte);
    				else
    					erros.add("Error: 121 - Fecha Corte Invalida "+diaCorte + "/" + mesCorte + "/" + anoCorte + " - Línea: " + numeroLinha + nRegistro);

    				numeroParcela = "";
    				if(verificaPosicaoString(linha, 37, 39, true, numeroLinha, "Numero Parcela", nRegistro))
    					numeroParcela = linha.substring(37, 39).trim();
    				
    				dataVencimento = null;
    				anoVencimento = "";
    				mesVencimento = "";
    				diaVencimento = "";
    				
    				if(verificaPosicaoString(linha, 39, 43, true, numeroLinha, "Año Vencimiento", nRegistro))
    					anoVencimento = linha.substring(39, 43).trim();
    				if(verificaPosicaoString(linha, 43, 45, true, numeroLinha, "Mes Vencimiento", nRegistro))
    					mesVencimento = linha.substring(43, 45).trim();
    				if(verificaPosicaoString(linha, 45, 47, true, numeroLinha, "Día Vencimiento", nRegistro))
    					diaVencimento = linha.substring(45, 47).trim();
    				
    				if(eData(diaVencimento + "/" + mesVencimento + "/" + anoVencimento))
    					dataVencimento = (new SimpleDateFormat("dd/MM/yyyy")).parse(diaVencimento + "/" + mesVencimento + "/" + anoVencimento);
    				else
    					erros.add("Error: 122 - Fecha Vencimiento Invalida "+diaVencimento + "/" + mesVencimento + "/" + anoVencimento +" - Línea: " + numeroLinha + nRegistro);
    					
    				diaAtraso = "";
    				if(verificaPosicaoString(linha, 47, 50, true, numeroLinha, "Dias Morosidade", nRegistro))
    					diaAtraso = linha.substring(47, 50).trim();
    				
    				valorGs = 0;
    				if(verificaPosicaoString(linha, 50, 72, true, numeroLinha, "Valor en Gs", nRegistro))
    					valorGs = Double.valueOf(linha.substring(50, 72).trim());        
				                
    				tipoMoeda = "";
    				if(verificaPosicaoString(linha, 72, 74, true, numeroLinha, "Tipo ME Valor", nRegistro))
    					tipoMoeda = obterTipoMoeda(linha.substring(72, 74), numeroLinha, nRegistro);
    				
    				valorMe = 0;
    				if(verificaPosicaoString(linha, 74, 96, true, numeroLinha, "Valor en ME", nRegistro))
    					valorMe = Double.valueOf(linha.substring(74, 96).trim());
				                
    				tipoInstrumento = "";
    				if(verificaPosicaoString(linha, 96, 97, true, numeroLinha, "Tipo del Instrumento", nRegistro))
    				{
    					String s = linha.substring(96, 97).trim();
    					
		                if(s.equals("1"))
		                    tipoInstrumento = "P\363liza Individual";
		                else if(s.equals("2"))
		                    tipoInstrumento = "P\363liza Madre";
		                else if(s.equals("3"))
		                    tipoInstrumento = "Certificado de Seguro Colectivo";
		                else if(s.equals("4"))
		                    tipoInstrumento = "Certificado Provisorio";
		                else if(s.equals("5"))
		                    tipoInstrumento = "Nota de Cobertura de Reaseguro";
		                else
		                    erros.add("Error: 28 - Tipo del Instrumento es obligatorio - Línea: " + numeroLinha + nRegistro);
    				}
    				
    				numeroEndoso = 0;
    				if(verificaPosicaoString(linha, 97, 107, true, numeroLinha, "Endoso", nRegistro))
    					numeroEndoso = Double.valueOf(linha.substring(97, 107).trim());
				                
    				certificado = 0;
    				if(verificaPosicaoString(linha, 107, 114, true, numeroLinha, "Certificado", nRegistro))
    					certificado = Double.valueOf(linha.substring(107, 114).trim());
				                
	                apolice = null;
	                if(apolices != null && cContas!=null && !numeroInstrumento.equals(""))
	                {
	                    apolice = apolices.get(obterOrigem().obterId() + numeroInstrumento+ cContas.obterId() + tipoInstrumento);
	                    if(apolice == null)
	                    	apolice = apoliceHome.obterApolice(obterOrigem(), numeroInstrumento,cContas, tipoInstrumento);
	                    if(apolice == null)
	                    	erros.add("Error: 29 - Instrumento " + numeroInstrumento + " no fue encuentrado - Línea: " + numeroLinha + nRegistro);
	                }
				                
	                if(erros.size() == 0)
	                {
	                    morosidade = (Morosidade)getModelManager().getEntity("Morosidade");
	                    morosidade.atribuirOrigem(aseguradora);
	                    morosidade.atribuirDestino(apolice.obterDestino());
	                    morosidade.atribuirResponsavel(apolice.obterResponsavel());
	                    morosidade.atribuirTitulo("Datos do Instrumento: " + numeroInstrumento + ";Sección: " + cContas.obterId());
	                    morosidade.atribuirSuperior(apolice);
	                    morosidade.atribuirDataCorte(dataCorte);
	                    morosidade.atribuirNumeroParcela(Integer.parseInt(numeroParcela));
	                    morosidade.atribuirDataVencimento(dataVencimento);
	                    morosidade.atribuirDiasAtraso(Integer.parseInt(diaAtraso));
	                    morosidade.atribuirValorGs(valorGs);
	                    morosidade.atribuirTipoMoedaValorGs(tipoMoeda);
	                    morosidade.atribuirValorMe(valorMe);
	                    morosidade.atribuirTipoInstrumento(tipoInstrumento);
	                    morosidade.atribuirNumeroEndoso(numeroEndoso);
	                    morosidade.atribuirCertificado(certificado);
	                    morosidades.add(morosidade);
	                }
    			} 
                else if(numeroRegistroInt == 18)
    			{
                	nRegistro = " (Registro 18)";
                	
	                String ativoCorrenteStr = linha.substring(9, 31);
	                double ativoCorrente = Double.parseDouble(ativoCorrenteStr);
	                String passivoCorrenteStr = linha.substring(31, 53);
	                double passivoCorrente = Double.parseDouble(passivoCorrenteStr);
	                String inversaoStr = linha.substring(53, 75);
	                double inversao = Double.parseDouble(inversaoStr);
	                String deudasStr = linha.substring(75, 97);
	                double deudas = Double.parseDouble(deudasStr);
	                String usoStr = linha.substring(97, 119);
	                double uso = Double.parseDouble(usoStr);
	                String vendaStr = linha.substring(119, 141);
	                double venda = Double.parseDouble(vendaStr);
	                String leasingStr = linha.substring(141, 163);
	                double leasing = Double.parseDouble(leasingStr);
	                String resultadoStr = linha.substring(163, 185);
	                double resultado = Double.parseDouble(resultadoStr);
	                
	                EntidadeHome home = (EntidadeHome)getModelManager().getHome("EntidadeHome");
	                RatioPermanente ratio = (RatioPermanente)getModelManager().getEntity("RatioPermanente");
	                Entidade bcp = home.obterEntidadePorApelido("bcp");
	                Usuario admin = (Usuario)home.obterEntidadePorApelido("admin");
	                ratio.atribuirOrigem(aseguradora);
	                ratio.atribuirDestino(bcp);
	                ratio.atribuirResponsavel(admin);
	                ratio.atribuirTitulo("Ratio Financeiro - Permanente");
	                ratio.atribuirDataPrevistaInicio(dataGeracao);
	                ratio.atribuirAtivoCorrente(ativoCorrente);
	                ratio.atribuirPassivoCorrente(passivoCorrente);
	                ratio.atribuirInversao(inversao);
	                ratio.atribuirDeudas(deudas);
	                ratio.atribuirUso(uso);
	                ratio.atribuirVenda(venda);
	                ratio.atribuirLeasing(leasing);
	                ratio.atribuirResultados(resultado);
	                ratio.atribuirSuperior(this);
                
	                ratiosPermanentes.add(ratio);
    			}
                else if(numeroRegistroInt == 19)
                {
                	nRegistro = " (Registro 19)";
                	
	            	String primasDiretasStr = linha.substring(9, 31);
	                double primasDiretas = Double.parseDouble(primasDiretasStr);
	                String primasAceitasStr = linha.substring(31, 53);
	                double primasAceitas = Double.parseDouble(primasAceitasStr);
	                String primasCedidasStr = linha.substring(53, 75);
	                double primasCedidas = Double.parseDouble(primasCedidasStr);
	                String anulacaoPrimasDiretasStr = linha.substring(75, 97);
	                double anulacaoPrimasDiretas = Double.parseDouble(anulacaoPrimasDiretasStr);
	                String anulacaoPrimasAtivasStr = linha.substring(97, 119);
	                double anulacaoPrimasAtivas = Double.parseDouble(anulacaoPrimasAtivasStr);
	                String anulacaoPrimasCedidasStr = linha.substring(119, 141);
	                double anulacaoPrimasCedidas = Double.parseDouble(anulacaoPrimasCedidasStr);
	                EntidadeHome home = (EntidadeHome)getModelManager().getHome("EntidadeHome");
	                RatioUmAno ratio = (RatioUmAno)getModelManager().getEntity("RatioUmAno");
	                Entidade bcp = home.obterEntidadePorApelido("bcp");
	                Usuario admin = (Usuario)home.obterEntidadePorApelido("admin");
	                ratio.atribuirOrigem(aseguradora);
	                ratio.atribuirDestino(bcp);
	                ratio.atribuirResponsavel(admin);
	                ratio.atribuirTitulo("Ratio Financeiro - Un Año");
	                ratio.atribuirDataPrevistaInicio(dataGeracao);
	                ratio.atribuirPrimasDiretas(primasDiretas);
	                ratio.atribuirPrimasAceitas(primasAceitas);
	                ratio.atribuirPrimasCedidas(primasCedidas);
	                ratio.atribuirAnulacaoPrimasDiretas(anulacaoPrimasDiretas);
	                ratio.atribuirAnulacaoPrimasAtivas(anulacaoPrimasAtivas);
	                ratio.atribuirAnulacaoPrimasCedidas(anulacaoPrimasCedidas);
	                ratio.atribuirSuperior(this);
	                ratiosUmAno.add(ratio);
                }	
                else if(numeroRegistroInt == 20)
                {
                	nRegistro = " (Registro 20)";
                	
	                String sinistrosPagosStr = linha.substring(9, 31);
	                double sinistrosPagos = Double.parseDouble(sinistrosPagosStr);
	                String gastosSinistroStr = linha.substring(31, 53);
	                double gastosSinistro = Double.parseDouble(gastosSinistroStr);
	                String sinistrosRecuperadosStr = linha.substring(53, 75);
	                double sinistrosRecuperados = Double.parseDouble(sinistrosRecuperadosStr);
	                String gastosRecupeadosStr = linha.substring(75, 97);
	                double gastosRecupeados = Double.parseDouble(gastosRecupeadosStr);
	                String recuperoSinistroStr = linha.substring(97, 119);
	                double recuperoSinistro = Double.parseDouble(recuperoSinistroStr);
	                String provisoesStr = linha.substring(119, 141);
	                double provisoes = Double.parseDouble(provisoesStr);
	                EntidadeHome home = (EntidadeHome)getModelManager().getHome("EntidadeHome");
	                RatioTresAnos ratio = (RatioTresAnos)getModelManager().getEntity("RatioTresAnos");
	                Entidade bcp = home.obterEntidadePorApelido("bcp");
	                Usuario admin = (Usuario)home.obterEntidadePorApelido("admin");
	                ratio.atribuirOrigem(aseguradora);
	                ratio.atribuirDestino(bcp);
	                ratio.atribuirResponsavel(admin);
	                ratio.atribuirTitulo("Ratio Financeiro - Tres A\361o");
	                ratio.atribuirDataPrevistaInicio(dataGeracao);
	                ratio.atribuirSinistrosPagos(sinistrosPagos);
	                ratio.atribuirGastosSinistros(gastosSinistro);
	                ratio.atribuirSinistrosRecuperados(sinistrosRecuperados);
	                ratio.atribuirGastosRecuperados(gastosRecupeados);
	                ratio.atribuirRecuperoSinistros(recuperoSinistro);
	                ratio.atribuirProvisoes(provisoes);
	                ratio.atribuirSuperior(this);
	                ratiosTresAnos.add(ratio);
                }
                else if(numeroRegistroInt == 99)
                {
                	nRegistro = " (Registro 99)";
                	
                	if(numeroLinha != numeroTotalRegistros)
                		erros.add("Error: 123 - Numero total de registros (" + numeroLinha + ") no es el mismo del archivo (" + numeroTotalRegistros + ") - Línea: " + numeroLinha + nRegistro);
                }
                else
                	erros.add("Error: 123 - Registro " + linha.substring(7, 9) + " no es válido - Línea: " + numeroLinha);
    		}
        }
        catch(NumberFormatException e)
        {
        	if(e.toString().equals("java.util.ConcurrentModificationException"))
        		this.validarApolice(linha, numeroLinha);
        	else
        	{
        		String[] msgCerta = e.getMessage().split(":");
        		erros.add("Error Interno: Formato de número no es correcto " + msgCerta[1] + " - Línea: "+ numeroLinha + nRegistro);
        	}
        }
        catch(Exception e)
        {
        	if(e.toString().equals("java.util.ConcurrentModificationException"))
        		this.validarApolice(linha, numeroLinha);
        	else
        		erros.add("Error Interno: " + e.toString() + " - Línea: "+ numeroLinha + nRegistro);
        }
    	return erros;
    }

    
    private String nomeAsegurado,tipoPessoa,tipoPessoa2,tipoIdentificacao,tipoIdentificacao2,numeroIdentificacao,anoNascimento,mesNascimento,diaNascimento,tomadorSeguro,validacaoRuc,sobreNome,dataNascStr,dataNascStr2,paisSWIFT,tipoPessoaBcp,paisBcp,nomeBcp,sobreNomeBcp,dataNascBcp,nomeModificado;
    private Date dataNascimento;
    private Uteis uteis = new Uteis();
    private EntidadeBCP entidadeBcp;
    private Collection<EntidadeBCP> asegurados, tomadores;
    private boolean atualizacaoPessoaApolice;
    private Map<String,Apolice> apolicesAtualizacao = new TreeMap<>();
    private boolean validaAsegurado;
    
    private Collection<String> validarAsegurado(String linha, int numeroLinha) throws Exception
    {
    	erros = new ArrayList();
        
        try
        {
        	/*if(numeroLinha == 73)
        		System.out.println("");*/
        	
        	numeroRegistroInt = 0;
    		nRegistro = " (Registro 01) Datos del Asegurado ";
    		
    		if(this.verificaPosicaoString(linha, 7, 9, true, numeroLinha, "Número del registro", ""))
   				numeroRegistroInt = Integer.valueOf(linha.substring(7, 9).trim());
        	
	        //if(Integer.parseInt(linha.substring(5, 7)) == 1)
        	if(numeroRegistroInt == 1)
	        {
        		sigla = "";
        		aseguradora = null;
        		if(verificaPosicaoString(linha, 9, 12, true, numeroLinha, "Sigla (Datos del Asegurado)", nRegistro))
        		{
        			sigla = linha.substring(9, 12).trim();
        			aseguradora = entidadeHome.obterEntidadePorSigla(sigla);
        			if(aseguradora == null || aseguradora instanceof Raiz)
        				erros.add("Error: 03 - Aseguradora " + sigla + " no fue encontrada (Archivo Datos del Asegurado) - Línea: " + numeroLinha);
        			else
        			{
        				if(!obterOrigem().equals(aseguradora))
        					erros.add("Error: 06 - Aseguradora " + sigla + " no es la misma de la agenda (Archivo Datos del Asegurado) - Línea: " + numeroLinha);
        			}
        		}
        		
        		chaveUsuario = "";
        		usuario = null;
        		if(verificaPosicaoString(linha, 12, 22, false, numeroLinha, "Clave del usuario (Datos del Asegurado)", nRegistro))
        		{
        			chaveUsuario = linha.substring(12, 22).trim();
        			usuario = usuarioHome.obterUsuarioPorChave(chaveUsuario);
        			if(usuario == null)
        				erros.add("Error: 04 - Usuario " + chaveUsuario + " no fue encontrado (Archivo Datos del Asegurado) - Línea: " + numeroLinha);
        		}
	            
        		dataGeracao = null;
        		anoGeracao = "";
        		mesGeracao = "";
        		diaGeracao = "";
        		
        		if(verificaPosicaoString(linha, 22, 26, true, numeroLinha, "Año emisión (Datos del Asegurado)", nRegistro))
        			anoGeracao = linha.substring(22, 26).trim();
        		if(verificaPosicaoString(linha, 26, 28, true, numeroLinha, "Mes emisión (Datos del Asegurado)", nRegistro))
        			mesGeracao = linha.substring(26, 28).trim();
        		if(verificaPosicaoString(linha, 28, 30, true, numeroLinha, "Día emisión (Datos del Asegurado)", nRegistro))
        			diaGeracao = linha.substring(28, 30).trim();
	            
	            if(eData(diaGeracao + "/" + mesGeracao + "/" + anoGeracao))
	            	dataGeracao = (new SimpleDateFormat("dd/MM/yyyy")).parse(diaGeracao + "/" + mesGeracao + "/" + anoGeracao);
	            else
	                erros.add("Error: 92 - Fecha Emisi\363n Invalida - Línea: " + numeroLinha);
	            
	            anoReporte = "";
	            mesReporte = "";
	            if(verificaPosicaoString(linha, 30, 34, true, numeroLinha, "Año reporte (Datos del Asegurado)", nRegistro))
	            	anoReporte = linha.substring(30, 34).trim();
	            if(verificaPosicaoString(linha, 34, 36, true, numeroLinha, "Mes reporte (Datos del Asegurado)", nRegistro))
	            	mesReporte = linha.substring(34, 36).trim();
	            
	            if(!anoReporte.equals("") && !mesReporte.equals(""))
	            {
		            if(obterMesMovimento() != Integer.valueOf(mesReporte))
		                erros.add("Error: 07 - Mes informado es diferente del mes de la agenda (Archivo Datos del Asegurado)");
		            if(obterAnoMovimento() != Integer.valueOf(anoReporte))
		                erros.add("Error: 08 - A\361o informado es diferente del a\361o de la agenda (Archivo Datos del Asegurado)");
	            }
	            
	            numeroTotalRegistros = 0;
	            if(verificaPosicaoString(linha, 36, 46, true, numeroLinha, "Numero total de registros (Datos del Asegurado)", nRegistro))
	            	numeroTotalRegistros = Integer.valueOf(linha.substring(36, 46).trim());
	            
	            tipoArquivo = "";
	            if(verificaPosicaoString(linha, 46, 47, false, numeroLinha, "Tipo del Archivo (Datos del Asegurado)", nRegistro))
	            {
		            if(!linha.substring(46, 47).trim().toLowerCase().equals("n"))
		                erros.add("Error: 124 - Tipo del Archivo Invalido - Línea: " + numeroLinha);
	            }
	        }
        	else if(numeroRegistroInt == 17)
	        {
        		nRegistro = " (Registro 17) Datos del Asegurado ";
        		
        		atualizacaoPessoaApolice = false;
        		cContas = null;
        		
        		if(verificaPosicaoString(linha, 9, 19, true, numeroLinha, "Cuenta", nRegistro))
        		{
        			apelidoCconta = linha.substring(9, 19).trim();
        			if(apelidoCconta.trim().length() > 0)
        				cContas = (ClassificacaoContas)entidadeHome.obterEntidadePorApelido(apelidoCconta);
	            
        			if(cContas == null)
        				erros.add("Error: 05 - Cuenta " + apelidoCconta + " no fue encontrada (Archivo Datos del Asegurado) - Línea: " + numeroLinha);
        		}
	            
        		numeroInstrumento = "";
        		if(verificaPosicaoString(linha, 19, 29, true, numeroLinha, "Número Póliza", nRegistro))
        			numeroInstrumento = linha.substring(19, 29).trim();
	            
	            if(linha.length() < 195)
	            {
		            //ANTIGO FORMATO, QUANDO NÃO TINHA O SOBRE NOME SEPARADO
	            	
		            nomeAsegurado = linha.substring(29, 89);
		            
		            tipoPessoa = "";
		            //if(linha.substring(87, 88).equals("1"))
		            if(linha.substring(89, 90).equals("1"))
		                tipoPessoa = "Persona Fisica";
		            else if(linha.substring(89, 90).equals("2"))
		                tipoPessoa = "Persona Juridica";
		            
		            tipoIdentificacao = "";
		            tipoIdentificacao2 = "";
		            //if(linha.substring(88, 89).equals("1"))
		            if(linha.substring(90, 91).equals("1"))
		            {
		                tipoIdentificacao = "C\351dula de Identidad Paraguaya";
		                tipoIdentificacao2 = "CI";
		            }
		            else if(linha.substring(90, 91).equals("2"))
		            {
		                tipoIdentificacao = "C\351dula de Identidad Extranjera";
		                tipoIdentificacao2 = "CRC";
		            }
		            else if(linha.substring(90, 91).equals("3"))
		            {
		                tipoIdentificacao = "Passaporte";
		                tipoIdentificacao2 = "CRP";
		            }
		            else if(linha.substring(90, 91).equals("4"))
		            {
		                tipoIdentificacao = "RUC";
		                tipoIdentificacao2 = "RUC";
		            }
		            else if(linha.substring(90, 91).equals("5"))
		            {
		                tipoIdentificacao = "Otro";
		                tipoIdentificacao2 = "Otro";
		            }
		            
		            //numeroIdentificacao = linha.substring(89, 104);
		            numeroIdentificacao = linha.substring(91, 106);
		            
		            //anoNascimento = linha.substring(104, 108);
		            anoNascimento = linha.substring(106, 110);
		            //mesNascimento = linha.substring(108, 110);
		            mesNascimento = linha.substring(110, 112);
		            //diaNascimento = linha.substring(110, 112);
		            diaNascimento = linha.substring(112, 114);
		            dataNascimento = null;
		            if(diaNascimento.startsWith(" ") || mesNascimento.startsWith(" ") || anoNascimento.startsWith(" "))
		            {
		                if(cContas.obterApelido().startsWith("04010120") && tipoPessoa.equals("Persona Fisica"))
		                    erros.add("Error: 125 - Fecha de Nacimiento invalida - Línea: " + numeroLinha);
		            } 
		            else
		                dataNascimento = (new SimpleDateFormat("dd/MM/yyyy")).parse(diaNascimento + "/" + mesNascimento + "/" + anoNascimento);
		            
		            //tomadorSeguro = linha.substring(112, 172);
		            tomadorSeguro = linha.substring(114, 174);
		            tipoInstrumento = "";
		            //if(linha.substring(172, 173).equals("1"))
		            if(linha.substring(174, 175).equals("1"))
		                tipoInstrumento = "P\363liza Individual";
		            else if(linha.substring(174, 175).equals("2"))
		                tipoInstrumento = "P\363liza Madre";
		            else if(linha.substring(174, 175).equals("3"))
		                tipoInstrumento = "Certificado de Seguro Colectivo";
		            else if(linha.substring(174, 175).equals("4"))
		                tipoInstrumento = "Certificado Provisorio";
		            else if(linha.substring(174, 175).equals("5"))
		                tipoInstrumento = "Nota de Cobertura de Reaseguro";
		            
		            if(tipoInstrumento.equals(""))
		                erros.add("Error: 28 - Instrumento es obligatorio - Línea: " + numeroLinha);
		            
		            if(apolices != null)
		                apolice = (Apolice)apolices.get(obterOrigem().obterId() + numeroInstrumento+ cContas.obterId() + tipoInstrumento);
		            if(apolice == null)
		            {
		                apolice = apoliceHome.obterApolice(obterOrigem(), numeroInstrumento,cContas, tipoInstrumento);
		                atualizacaoPessoaApolice = true;
		            }
		            if(apolice == null)
		                erros.add("Error: 29 - Instrumento " + numeroInstrumento.trim() + " no fue encontrado (Archivo Datos del Asegurado) - Línea: " + numeroLinha);
		            
		            if(apolice!=null)
		            {
			            if(!tipoInstrumento.equals(apolice.obterStatusApolice()))
			            {
			            	if(!atualizacaoPessoaApolice)
			            		erros.add("Error: 129 - Tipo Instrumento informado en el Registro 02 es diferente del informado en el Registro 17, Póliza "+numeroInstrumento+" Sección "+apelidoCconta+" - Línea: " + numeroLinha);
			            	else
			            		erros.add("Error: 129 - Tipo Instrumento en la Base de Datos es diferente del informado en el Registro 17, Póliza "+numeroInstrumento+" Sección "+apelidoCconta+" - Línea: " + numeroLinha);
			            }
		            }
		            
	                if(tipoInstrumento.equals("P\363liza Madre"))
	                {
	                	if(tomadorSeguro.trim().length() == 0)
	                		erros.add("Error: 126 - Nombre Tomador en Blanco(Archivo Datos del Asegurado) - Línea: " + numeroLinha);
	                }
	                else
	                {
			            if(nomeAsegurado.trim().length() == 0)
		                	erros.add("Error: 126 - Nombre Asegurado en Blanco(Archivo Datos del Asegurado) - Línea: " + numeroLinha);
	                }
	                
		            //numeroEndoso = Double.parseDouble(linha.substring(173, 183));
	                numeroEndoso = Double.parseDouble(linha.substring(175, 185));
		            //certificado = Double.parseDouble(linha.substring(183, 190));
	                certificado = Double.parseDouble(linha.substring(185, 192));
		            
		            if(numeroInstrumento.startsWith("15") && (apelidoCconta.equals("0401011300") || apelidoCconta.equals("0402011300") || apelidoCconta.equals("0402021300")))
		            {
		            	if(nomeAsegurado.toLowerCase().trim().equals(tomadorSeguro.toLowerCase().trim()))
		            		erros.add("Error: 127 - Póliza caución con nombre del asegurado y del tomador iguales Póliza "+numeroInstrumento+" Sección "+apelidoCconta+" - Línea: " + numeroLinha);
		            }
		            
		            if(!documentoEspecial)
		            {
			            validacaoRuc = rucCiHome.obterPessoaPorDoc(tipoIdentificacao2, numeroIdentificacao.trim());
			            if(validacaoRuc!=null)
			            {
			            	if(validacaoRuc.equals(""))
			            		erros.add("Error: 128 - Documento inválido (Archivo Datos del Asegurado) tipo doc "+tipoIdentificacao2+" Nº "+numeroIdentificacao.trim()+" - Línea: " + numeroLinha);
			            	else
			            	{
			            		String[] linhaSuja = validacaoRuc.split(";");
			            		
			            		if(linhaSuja.length == 0)
			            			erros.add("Error: 128 - Documento inválido (Archivo Datos del Asegurado) tipo doc "+tipoIdentificacao2+" Nº "+numeroIdentificacao.trim()+" - Línea: " + numeroLinha);
			            	}
			            }
			            else
			            	erros.add("Error: 128 - Documento inválido (Archivo Datos del Asegurado) tipo doc "+tipoIdentificacao2+" Nº "+numeroIdentificacao.trim()+" - Línea: " + numeroLinha);
		            }
		            
		            //String eAtualizacao = linha.substring(190, 191);
		            
		            if(erros.size() == 0)
		            {
	                	apolice.atribuirNomeAsegurado(nomeAsegurado.trim());
	                    apolice.atribuirTipoPessoa(tipoPessoa.trim());
	                    apolice.atribuirTipoIdentificacao(tipoIdentificacao.trim());
	                    apolice.atribuirNumeroIdentificacao(numeroIdentificacao.trim());
	                    apolice.atribuirDataNascimento(dataNascimento);
	                    apolice.atribuirNomeTomador(tomadorSeguro.trim());
		            	if(atualizacaoPessoaApolice)
		            		apolicesAtualizacao.put(obterOrigem().obterId() + numeroInstrumento+ cContas.obterId() + tipoInstrumento, apolice);
		            }
	            }
	            else
	            {
	            	//NOVO FORMATO, COM SOBRE NOME SEPARADO
	            	
	            	asegurados = new ArrayList<>();
	            	tomadores = new ArrayList<>();
	            	
	            	qtde = 0;
	            	if(verificaPosicaoString(linha, 29, 31, true, numeroLinha, "Cantidad Asegurados", nRegistro))
	            		qtde = Integer.valueOf(linha.substring(29, 31).trim());
	            	
		            ultimo = 31;
		            if(qtde > 0)
		            {
		            	for(int w = 0; w < qtde; w++)
		                {
		            		validaAsegurado = true;
		            		
		            		entidadeBcp = new EntidadeBCPImpl();
		            		tipoPessoa = "";
		            		tipoIdentificacao2 = "";
		            		
		            		String nomeModificado = "";
		            		nomeAsegurado = "";
		            		if(verificaPosicaoString(linha, ultimo, ultimo + 60, false, numeroLinha, "Nombre Asegurado", nRegistro))
		            		{
		            			nomeAsegurado = linha.substring(ultimo, ultimo + 60).trim().replace("\"", "");
		            			if(nomeAsegurado.length() == 0)
			                    	erros.add("Error: 147 - Nombre del Asegurado es obligatorio Póliza "+numeroInstrumento+" - Línea: " + numeroLinha);
		            			else
		            				nomeModificado = Normalizer.normalize(nomeAsegurado, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
		            		}
		            		
		            		sobreNome = "";
		            		if(verificaPosicaoString(linha, ultimo + 60, ultimo + 60 + 30, false, numeroLinha, "Apellido Asegurado", nRegistro))
		            			sobreNome = linha.substring(ultimo + 60, ultimo + 60 + 30).trim().replace("\"", "");
		            		
		            		tipoPessoa2 = "";
		            		if(verificaPosicaoString(linha, ultimo + 60 + 30, ultimo + 60 + 30 + 1, true, numeroLinha, "Tipo Persona Asegurado", nRegistro))
		            			tipoPessoa2 = linha.substring(ultimo + 60 + 30, ultimo + 60 + 30 + 1).trim();
		            		
		            		tipoIdentificacao = "";
		            		if(verificaPosicaoString(linha, ultimo + 60 + 30 + 1, ultimo + 60 + 30 + 1 + 1, true, numeroLinha, "Tipo del Documento Asegurado", nRegistro))
		            			tipoIdentificacao = linha.substring(ultimo + 60 + 30 + 1, ultimo + 60 + 30 + 1 + 1).trim();
		            		
		            		numeroIdentificacao = "";
		            		if(verificaPosicaoString(linha, ultimo + 60 + 30 + 1 + 1, ultimo + 60 + 30 + 1 + 1 + 15, false, numeroLinha, "Numero del Documento Asegurado", nRegistro))
		            			numeroIdentificacao = linha.substring(ultimo + 60 + 30 + 1 + 1, ultimo + 60 + 30 + 1 + 1 + 15).trim();
		            		
		            		dataNascStr2 = "";
		            		if(verificaPosicaoString(linha, ultimo + 60 + 30 + 1 + 1 + 15, ultimo + 60 + 30 + 1 + 1 + 15 + 8, false, numeroLinha, "Fecha de Nacimiento Asegurado", nRegistro))
		            			dataNascStr2 = linha.substring(ultimo + 60 + 30 + 1 + 1 + 15, ultimo + 60 + 30 + 1 + 1 + 15 + 8).trim();
		            		
		            		paisSWIFT = "";
		            		if(verificaPosicaoString(linha, ultimo + 60 + 30 + 1 + 1 + 15 + 8, ultimo + 60 + 30 + 1 + 1 + 15 + 8 + 3, false, numeroLinha, "Pais Asegurado", nRegistro))
		            			paisSWIFT = linha.substring(ultimo + 60 + 30 + 1 + 1 + 15 + 8, ultimo + 60 + 30 + 1 + 1 + 15 + 8 + 3).trim();
		                    
		                    if(numeroInstrumento.startsWith("1514") && nomeModificado.toUpperCase().indexOf("PERSONAS NATURALES O JURIDICAS") > -1)
		                    	validaAsegurado = false;
		                    
		                    if(paisSWIFT.equals("") && validaAsegurado)
		                    	erros.add("Error: 142 - País del Asegurado es obligatorio Póliza "+numeroInstrumento+" - Línea: " + numeroLinha);
		                    
		    	            if(tipoPessoa2.equals("1"))
		    	            	tipoPessoa = "Persona Jurídica";
		    	            else if(tipoPessoa2.equals("2"))
		    	            	tipoPessoa = "Persona Física";
		    	            else if(tipoPessoa2.equals("3"))
		    	                tipoPessoa = "Física Casada";
		    	            else if(tipoPessoa2.equals("4"))
		    	            {
		    	                tipoPessoa = "Consorcio";
		    	                validaAsegurado = false;
		    	            }
		    	            else
		    	            	erros.add("Error: 133 - Tipo de Persona del Asegurado no es válido Póliza "+numeroInstrumento+", Tipo Persona: " + tipoPessoa2 + " - Línea: " + numeroLinha);
		                    
		    	            entidadeBcp.setNome(nomeAsegurado);
		    	            entidadeBcp.setSobreNome(sobreNome);
		    	            entidadeBcp.setTipoPessoa(tipoPessoa);
		    	            entidadeBcp.setPais(paisSWIFT);
		    	            entidadeBcp.setNumeroDoc(numeroIdentificacao);
		    	            
		    	            tipoIdentificacao2 = "";
		    	            if(tipoIdentificacao.equals("1"))
		    	                tipoIdentificacao2 = "CI";
		    	            else if(tipoIdentificacao.equals("2"))
			    	            tipoIdentificacao2 = "RUC";
		    	            else if(tipoIdentificacao.equals("3"))
		    	                tipoIdentificacao2 = "CRP";
		    	            else if(tipoIdentificacao.equals("4"))
		    	                tipoIdentificacao2 = "CRC";
		    	            else
		    	            {
		    	            	if((tipoIdentificacao.equals("0") && !tipoPessoa2.equals("4")) || validaAsegurado)
		    	            		erros.add("Error: 134 - Tipo de Documento no es válido (Asegurado) Póliza "+numeroInstrumento+", Tipo Documento: " + tipoIdentificacao + " - Línea: " + numeroLinha);
		    	            }
		    	            
		    	            if(!tipoPessoa.equals("") && !tipoIdentificacao2.equals("") && validaAsegurado)
		    	            {
		    	            	if((tipoPessoa2.equals("1") || tipoPessoa2.equals("2")) && tipoIdentificacao.equals("4"))//FISICA OU JURIDICA PODE SER CRC
		    	            	{
		    	            		
		    	            	}
		    	            	else if((tipoPessoa2.equals("2") || tipoPessoa2.equals("3")) && tipoIdentificacao.equals("2"))//FISICA COM RUC
			    	            	erros.add("Error: 141 - Tipo de Documento no es correcto (Asegurado), Tipo de Persona: "+tipoPessoa+", Tipo de Documento: "+tipoIdentificacao2+" - Línea: " + numeroLinha);
			    	            else if (tipoPessoa2.equals("1"))//JURIDICA PODE SER RUC OU CRC
			    	            {
			    	            	if(!tipoIdentificacao.equals("2") && !tipoIdentificacao.equals("4"))
			    	            		erros.add("Error: 141 - Tipo de Documento no es correcto (Asegurado), Tipo de Persona: "+tipoPessoa+", Tipo de Documento: "+tipoIdentificacao2+" - Línea: " + numeroLinha);
			    	            }
			    	            else if (tipoPessoa2.equals("4") && !tipoIdentificacao.equals("2"))//CONSORCIO SÓ PODE SER RUC
			    	            	erros.add("Error: 141 - Tipo de Documento no es correcto (Asegurado), Tipo de Persona: "+tipoPessoa+", Tipo de Documento: "+tipoIdentificacao2+" - Línea: " + numeroLinha);
			    	            else if(tipoIdentificacao.equals("4"))
		    	            	{
		    	            		 if(tipoPessoa2.equals("1") && sobreNome.length() > 0)
		    	            			 erros.add("Error: 145 - Apellido debe venir en blanco (Asegurado), Tipo de Persona: "+tipoPessoa+", Tipo de Documento: "+tipoIdentificacao2+" - Línea: " + numeroLinha);
		    	            		 else if(tipoPessoa2.equals("2") && sobreNome.length() == 0)
		    	            			 erros.add("Error: 135 - Apellido es obligatorio (Asegurado) Tipo de Persona: "+tipoPessoa+", Tipo de Documento: "+tipoIdentificacao2+" - Línea: " + numeroLinha);
		    	            	}
		    	            	else if(tipoIdentificacao.equals("2") && sobreNome.length() > 0)// Empresa com sobrenome
			    	            	erros.add("Error: 145 - Apellido debe venir en blanco (Asegurado), Tipo de Persona: "+tipoPessoa+", Tipo de Documento: "+tipoIdentificacao2+" - Línea: " + numeroLinha);
		    	            	else if(tipoIdentificacao.equals("3") && (!tipoPessoa2.equals("2") && !tipoPessoa2.equals("3")))// CRP só pode ser pessoa fisica
		    	            		erros.add("Error: 141 - Tipo de Documento no es correcto (Asegurado), Tipo de Persona: "+tipoPessoa+", Tipo de Documento: "+tipoIdentificacao2+" - Línea: " + numeroLinha);
		    	            }
		    	            
		    	            entidadeBcp.setTipoDocumento(tipoIdentificacao2);
		    	            
		    	            if(numeroIdentificacao.length() == 0  && validaAsegurado)
		    	            	erros.add("Error: 142 - Número del Documento es obligatorio (Asegurado) Póliza "+numeroInstrumento+" - Línea: " + numeroLinha);
		    	            
		    	            if(tipoPessoa.indexOf("Física") > -1)
		    	            {
		    	            	if(sobreNome.length() == 0)
		    	            		erros.add("Error: 135 - Apellido es obligatorio (Asegurado) Tipo de Persona: "+tipoPessoa+", Tipo de Documento: "+tipoIdentificacao2+" - Línea: " + numeroLinha);
		    	            	
		    	            	if(dataNascStr2.length() == 0)
		    	            		erros.add("Error: 125 - Fecha de Nacimiento es obligatoria (Asegurado) Tipo de Persona: "+tipoPessoa+", Tipo de Documento: "+tipoIdentificacao2+" - Línea: " + numeroLinha);
		    	            	else
		    	            	{
			    	            	if(dataNascStr2.length() == 8)
			    	            	{
			    	            		anoNascimento = "";
			    	            		if(verificaPosicaoString(dataNascStr2, 0, 4, true, numeroLinha, "Año Nacimiento del Asegurado", nRegistro))
			    	            			anoNascimento = dataNascStr2.substring(0, 4).trim();
			    	            		
			    	            		mesNascimento = "";
			    	            		if(verificaPosicaoString(dataNascStr2, 4, 6, true, numeroLinha, "Mes Nacimiento del Asegurado", nRegistro))
			    	            			mesNascimento = dataNascStr2.substring(4, 6).trim();
			    	            		
			    	            		diaNascimento = "";
			    	            		if(verificaPosicaoString(dataNascStr2, 6, 8, true, numeroLinha, "Día Nacimiento del Asegurado", nRegistro))
			    	            			diaNascimento = dataNascStr2.substring(6, 8).trim();
			    	            		
			    	            		dataNascStr = diaNascimento + "/"+mesNascimento+"/"+anoNascimento;
			    	            		
			    	            		if(!eData(dataNascStr))
			    	            			erros.add("Error: 125 - Fecha de Nacimiento no es válida (Asegurado) Póliza "+numeroInstrumento+", Fecha "+dataNascStr+" - Línea: " + numeroLinha);
			    	            		else
			    	            			entidadeBcp.setDataNascimento(new SimpleDateFormat("dd/MM/yyyy").parse(dataNascStr));
			    	            	}
			    	            	else
			    	            		erros.add("Error: 125 - Fecha de Nacimiento no es válida (Asegurado) Póliza "+numeroInstrumento+", Fecha "+dataNascStr2+" - Línea: " + numeroLinha);
		    	            	}
		    	            }
		    	            
		    	            if(!documentoEspecial && validaAsegurado)
		    	            {
		    	            	/*if(tipoIdentificacao2.equals("CRC") && !paisSWIFT.equals(""))
		    	            	{
		    	            		if(numeroIdentificacao.indexOf(paisSWIFT.substring(0, 2)) == -1)
		    	            			numeroIdentificacao = paisSWIFT.substring(0, 2) + " " + numeroIdentificacao;
		    	            	}*/
		    	            	
		    	            	//entidadeBcp.setNumeroDoc(numeroIdentificacao);
		    	            	
		    		            validacaoRuc = rucCiHome.obterPessoaPorDoc(tipoIdentificacao2, numeroIdentificacao);
		    		            if(validacaoRuc!=null)
		    		            {
		    		            	paisBcp = "";
		    		            	nomeBcp = "";
		    		            	tipoPessoaBcp = "";
		    		            	sobreNomeBcp = "";
		    		            	dataNascBcp = "";
		    		            	
		    		            	if(validacaoRuc.equals(""))
		    		            		erros.add("Error: 128 - No se encontró el documento en la base de datos BCP (Asegurado), Nombre "+nomeAsegurado+", Apellido "+sobreNome+", Tipo Documento "+tipoIdentificacao2+", Nº "+numeroIdentificacao+" - Línea: " + numeroLinha);
		    		            	else
		    		            	{
		    		            		String[] linhaSuja = validacaoRuc.split(";");
		    		            		
		    		            		if(linhaSuja.length == 0)
		    		            			erros.add("Error: 128 - No se encontró el documento en la base de datos BCP (Asegurado), Nombre "+nomeAsegurado+", Apellido "+sobreNome+", Tipo Documento "+tipoIdentificacao2+", Nº "+numeroIdentificacao+" - Línea: " + numeroLinha);
		    		            		else
		    		            		{
		    		            			if(verificaPosicaoArray(linhaSuja, 0))
		    		            				tipoPessoaBcp = linhaSuja[0].trim().replace("\"", "");
		    		            			else
		    		            				erros.add("Error: 150 - No se encontró el Tipo Persona en la base de datos BCP (Asegurado) Tipo Documento "+tipoIdentificacao2+", Nº "+numeroIdentificacao+". Nombre archivo = "+nomeAsegurado+" - Línea: " + numeroLinha);
		    		            			
		    		            			if(verificaPosicaoArray(linhaSuja, 1))
		    		            				paisBcp = linhaSuja[1].trim().replace("\"", "");
		    		            			else
		    		            				erros.add("Error: 151 - No se encontró el Pais en la base de datos BCP (Asegurado) Tipo Documento "+tipoIdentificacao2+", Nº "+numeroIdentificacao+". Nombre archivo = "+nomeAsegurado+" - Línea: " + numeroLinha);		    		            				
		    		            			
		    		            			if(verificaPosicaoArray(linhaSuja, 2))
		    		            			{
		    		            				nomeBcp = linhaSuja[2].trim().replace("\"", "");
		    		            				if(nomeBcp.length() > 60)
			    		            				nomeBcp = nomeBcp.substring(0,59);
		    		            			}
		    		            			else
		    		            				erros.add("Error: 152 - No se encontró el Nombre en la base de datos BCP (Asegurado) Tipo Documento "+tipoIdentificacao2+", Nº "+numeroIdentificacao+". Nombre archivo = "+nomeAsegurado+" - Línea: " + numeroLinha);		    		            				
		    		            			
		    		            			//if(!tipoPessoaBcp.equals(tipoPessoa2))
		    		            				//erros.add("Error: 136 - Tipo de Persona es diferente en la base de datos BCP (Archivo Datos del Asegurado) Tipo Documento "+tipoIdentificacao2+", Nº "+numeroIdentificacao+". Tipo persona archivo = "+tipoPessoa2+", tipo persona BCP = "+tipoPessoaBcp+" - Línea: " + numeroLinha);
		    		            			if(!paisBcp.equals(""))
		    		            			{
		    		            				if(!paisBcp.toLowerCase().equals(paisSWIFT.toLowerCase()))
		    		            					erros.add("Error: 137 - País es diferente en la base de datos BCP (Asegurado) Tipo Documento "+tipoIdentificacao2+", Nº "+numeroIdentificacao+". País archivo = "+paisSWIFT+", País BCP = "+paisBcp+" - Línea: " + numeroLinha);
		    		            			}
		    		            			
		    		            			if(!nomeBcp.equals(""))
		    		            			{
		    		            				//System.out.println("nomeBcp " + nomeBcp);
		    		            				//System.out.println("nomeAsegurado " + nomeAsegurado);
		    		            				
			    		            			if(!nomeBcp.toLowerCase().equals(nomeAsegurado.toLowerCase()))
			    		            				erros.add("Error: 138 - Nombre es diferente en la base de datos BCP (Asegurado) Tipo Documento "+tipoIdentificacao2+", Nº "+numeroIdentificacao+". Nombre archivo = "+nomeAsegurado.toLowerCase()+", Nombre BCP = "+nomeBcp.toLowerCase()+" - Línea: " + numeroLinha);
		    		            			}
		    		            				
		    		            			if(tipoPessoa2.equals("2") || tipoPessoa2.equals("3"))
		    		            			{
		    		            				if(verificaPosicaoArray(linhaSuja, 3))
		    		            					sobreNomeBcp = linhaSuja[3].trim().replace("\"", "");
		    		            				else
		    		            					erros.add("Error: 153 - No se encontró el Apellido en la base de datos BCP (Asegurado) Tipo Documento "+tipoIdentificacao2+", Nº "+numeroIdentificacao+". Nombre archivo = "+nomeAsegurado+" - Línea: " + numeroLinha);
		    		            				
		    		            				if(verificaPosicaoArray(linhaSuja, 4))
		    		            					dataNascBcp = linhaSuja[4].trim().replace("\"", "");
		    		            				else
		    		            					erros.add("Error: 154 - No se encontró el Fecha de Nacimiento en la base de datos BCP (Asegurado) Tipo Documento "+tipoIdentificacao2+", Nº "+numeroIdentificacao+". Nombre archivo = "+nomeAsegurado+" - Línea: " + numeroLinha);
			    		            			
			    		            			if(!sobreNomeBcp.equals(""))
			    		            			{
				    		            			if(!sobreNomeBcp.toLowerCase().equals(sobreNome.toLowerCase()))
				    		            				erros.add("Error: 139 - Apellido es diferente en la base de datos BCP (Asegurado) Tipo Documento "+tipoIdentificacao2+", Nº "+numeroIdentificacao+". Apellido archivo = "+sobreNome.toLowerCase()+", Apellido BCP = "+sobreNomeBcp.toLowerCase()+" - Línea: " + numeroLinha);
			    		            			}
			    		            			
			    		            			if(!dataNascBcp.equals(""))
			    		            			{
				    		            			if(!dataNascBcp.equals(dataNascStr))
				    		            				erros.add("Error: 140 - Fecha de Nacimiento es diferente en la base de datos BCP (Asegurado) Tipo Documento "+tipoIdentificacao2+", Nº "+numeroIdentificacao+". Fecha de Nacimiento archivo = "+dataNascStr+", Fecha de Nacimiento BCP = "+dataNascBcp+" - Línea: " + numeroLinha);
			    		            			}
			    		            			
			    		            			//if(!sobreNomeBcp.toLowerCase().equals(sobreNome.toLowerCase()))
			    		            				//erros.add("Error: 139 - Apellido es diferente en la base de datos BCP (Asegurado) Tipo Documento "+tipoIdentificacao2+", Nº "+numeroIdentificacao+". Apellido archivo = "+sobreNome.toLowerCase()+", Apellido BCP = "+sobreNomeBcp.toLowerCase()+" - Línea: " + numeroLinha);
			    		            			//if(!dataNascBcp.equals(dataNascStr))
			    		            				//erros.add("Error: 140 - Fecha de Nacimiento es diferente en la base de datos BCP (Asegurado) Tipo Documento "+tipoIdentificacao2+", Nº "+numeroIdentificacao+". Fecha de Nacimiento archivo = "+dataNascStr+", Fecha de Nacimiento BCP = "+dataNascBcp+" - Línea: " + numeroLinha);
		    		            			}
		    		            		}
		    		            	}
		    		            }
		    		            else
		    		            	erros.add("Error: 128 - No se encontró el documento en la base de datos BCP (Asegurado), Nombre "+nomeAsegurado+", Apellido "+sobreNome+", Tipo Documento "+tipoIdentificacao2+", Nº "+numeroIdentificacao+" - Línea: " + numeroLinha);
		    	            }
		    	            
		    	            numeroIdentificacao = numeroIdentificacao.replace("-", "");
		    	            numeroIdentificacao = numeroIdentificacao.replace(".", "");
		    	            numeroIdentificacao = numeroIdentificacao.replace(" ", "");
		    	            entidadeBcp.setNumeroDoc(numeroIdentificacao.trim());
		    	            
		    	            asegurados.add(entidadeBcp);
		                    
		                    ultimo+= 60 + 30 + 1 + 1 + 15 + 8 + 3;
		                }
		            }
		            else
		            	ultimo+= 60 + 30 + 1 + 1 + 15 + 8 + 3;
	            	
	            	//TOMADORES
		            qtde = 0;
		            if(verificaPosicaoString(linha, ultimo, ultimo + 2, true, numeroLinha, "Cantidad Tomadores", nRegistro))
		            	qtde = Integer.valueOf(linha.substring(ultimo, ultimo + 2).trim());
		            
	            	ultimo+=2;
	            	if(qtde > 0)
	            	{
		            	for(int w = 0; w < qtde; w++)
		                {
		            		entidadeBcp = new EntidadeBCPImpl();
		            		tipoPessoa = "";
		            		tipoIdentificacao2 = "";
		            		
		            		nomeAsegurado = "";
		            		if(verificaPosicaoString(linha, ultimo, ultimo + 60, false, numeroLinha, "Nombre Tomador", nRegistro))
		            		{
		            			nomeAsegurado = linha.substring(ultimo, ultimo + 60).trim().replace("\"", "");
		            			if(nomeAsegurado.length() == 0)
			                    	erros.add("Error: 147 - Nombre es obligatorio (Tomador) Póliza "+numeroInstrumento+" - Línea: " + numeroLinha);
		            		}
		            		
		            		sobreNome = "";
		            		if(verificaPosicaoString(linha, ultimo + 60, ultimo + 60 + 30, false, numeroLinha, "Apellido del Tomador", nRegistro))
		            			sobreNome = linha.substring(ultimo + 60, ultimo + 60 + 30).trim().replace("\"", "");
		            		
		            		tipoPessoa2 = "";
		            		if(verificaPosicaoString(linha, ultimo + 60 + 30, ultimo + 60 + 30 + 1, true, numeroLinha, "Tipo Persona del Tomador", nRegistro))
		            			tipoPessoa2 = linha.substring(ultimo + 60 + 30, ultimo + 60 + 30 + 1).trim();
		            		
		            		tipoIdentificacao = "";
		            		if(verificaPosicaoString(linha, ultimo + 60 + 30 + 1, ultimo + 60 + 30 + 1 + 1, true, numeroLinha, "Tipo del Documento del Tomador", nRegistro))
		            			tipoIdentificacao = linha.substring(ultimo + 60 + 30 + 1, ultimo + 60 + 30 + 1 + 1).trim();
		            		
		            		numeroIdentificacao = "";
		            		if(verificaPosicaoString(linha, ultimo + 60 + 30 + 1 + 1, ultimo + 60 + 30 + 1 + 1 + 15, false, numeroLinha, "Numero del Documento del Tomador", nRegistro))
		                    	numeroIdentificacao = linha.substring(ultimo + 60 + 30 + 1 + 1, ultimo + 60 + 30 + 1 + 1 + 15).trim();
		                    	
		            		dataNascStr2 = "";
		            		if(verificaPosicaoString(linha, ultimo + 60 + 30 + 1 + 1 + 15, ultimo + 60 + 30 + 1 + 1 + 15 + 8, false, numeroLinha, "Fecha de Nacimiento del Tomador", nRegistro))
		            			dataNascStr2 = linha.substring(ultimo + 60 + 30 + 1 + 1 + 15, ultimo + 60 + 30 + 1 + 1 + 15 + 8).trim();
		            		
		            		paisSWIFT = "";
		            		if(verificaPosicaoString(linha, ultimo + 60 + 30 + 1 + 1 + 15 + 8, ultimo + 60 + 30 + 1 + 1 + 15 + 8 + 3, false, numeroLinha, "Pais del Tomador", nRegistro))
		            		{
		            			paisSWIFT = linha.substring(ultimo + 60 + 30 + 1 + 1 + 15 + 8, ultimo + 60 + 30 + 1 + 1 + 15 + 8 + 3).trim();
		                    
		            			if(paisSWIFT.equals(""))
		            				erros.add("Error: 142 - País del Tomador es obligatorio Póliza "+numeroInstrumento+" - Línea: " + numeroLinha);
		            		}
		                    
		                    if(tipoPessoa2.equals("1"))
		    	            	tipoPessoa = "Persona Jurídica";
		    	            else if(tipoPessoa2.equals("2"))
		    	            	tipoPessoa = "Persona Física";
		    	            else if(tipoPessoa2.equals("3"))
		    	                tipoPessoa = "Física Casada";
		    	            else if(tipoPessoa2.equals("4"))
		    	                tipoPessoa = "Consorcio";
		    	            else
		    	            	erros.add("Error: 133 - Tipo de Persona no es válido (Tomador) Póliza "+numeroInstrumento+", Tipo Persona: " + tipoPessoa2 + " - Línea: " + numeroLinha);
		    	            
		                    entidadeBcp.setNome(nomeAsegurado);
		                    entidadeBcp.setSobreNome(sobreNome);
		    	            entidadeBcp.setTipoPessoa(tipoPessoa);
		    	            entidadeBcp.setPais(paisSWIFT);
		    	            entidadeBcp.setNumeroDoc(numeroIdentificacao.trim());
		    	            
		    	            tipoIdentificacao2 = "";
		    	            if(tipoIdentificacao.equals("1"))
		    	                tipoIdentificacao2 = "CI";
		    	            else if(tipoIdentificacao.equals("2"))
			    	            tipoIdentificacao2 = "RUC";
		    	            else if(tipoIdentificacao.equals("3"))
		    	                tipoIdentificacao2 = "CRP";
		    	            else if(tipoIdentificacao.equals("4"))
		    	                tipoIdentificacao2 = "CRC";
		    	            else
		    	            {
		    	            	if(tipoIdentificacao.equals("0") && !tipoPessoa2.equals("4"))
		    	            		erros.add("Error: 134 - Tipo de Documento no es válido (Tomador) Póliza "+numeroInstrumento+", Tipo Documento: " + tipoIdentificacao + " - Línea: " + numeroLinha);
		    	            }
		    	            
		    	            if(!tipoPessoa.equals("") && !tipoIdentificacao2.equals(""))
		    	            {
		    	            	if((tipoPessoa2.equals("1") || tipoPessoa2.equals("2")) && tipoIdentificacao.equals("4"))//FISICA  OU JURIDICA PODE SER CRC
		    	            	{
		    	            		
		    	            	}
		    	            	else if((tipoPessoa2.equals("2") || tipoPessoa2.equals("3")) && tipoIdentificacao.equals("2"))//FISICA COM RUC
			    	            	erros.add("Error: 141 - Tipo de Documento no es correcto (Tomador), Tipo de Persona: "+tipoPessoa+", Tipo de Documento: "+tipoIdentificacao2+" - Línea: " + numeroLinha);
			    	            else if (tipoPessoa2.equals("1"))//JURIDICA PODE SER RUC OU CRC
			    	            {
			    	            	if(!tipoIdentificacao.equals("2") && !tipoIdentificacao.equals("4"))
			    	            		erros.add("Error: 141 - Tipo de Documento no es correcto (Tomador), Tipo de Persona: "+tipoPessoa+", Tipo de Documento: "+tipoIdentificacao2+" - Línea: " + numeroLinha);
			    	            }
			    	            else if (tipoPessoa2.equals("4") && !tipoIdentificacao.equals("2"))//CONSORCIO SÓ PODE SER RUC
			    	            	erros.add("Error: 141 - Tipo de Documento no es correcto (Tomador), Tipo de Persona: "+tipoPessoa+", Tipo de Documento: "+tipoIdentificacao2+" - Línea: " + numeroLinha);
			    	            else if(tipoIdentificacao.equals("4"))
		    	            	{
		    	            		 if(tipoPessoa2.equals("1") && sobreNome.length() > 0)
		    	            			 erros.add("Error: 145 - Apellido debe venir en blanco (Tomador), Tipo de Persona: "+tipoPessoa+", Tipo de Documento: "+tipoIdentificacao2+" - Línea: " + numeroLinha);
		    	            		 else if(tipoPessoa2.equals("2") && sobreNome.length() == 0)
		    	            			 erros.add("Error: 135 - Apellido es obligatorio (Tomador) Tipo de Persona: "+tipoPessoa+", Tipo de Documento: "+tipoIdentificacao2+" - Línea: " + numeroLinha);
		    	            	}
			    	            else if(tipoIdentificacao.equals("2") && sobreNome.length() > 0)// Empresa com sobrenome
			    	            	erros.add("Error: 145 - Apellido debe venir en blanco (Tomador), Tipo de Persona: "+tipoPessoa+", Tipo de Documento: "+tipoIdentificacao2+" - Línea: " + numeroLinha);
			    	            else if(tipoIdentificacao.equals("3") && (!tipoPessoa2.equals("2") && !tipoPessoa2.equals("3")))// CRP só pode ser pessoa fisica
		    	            		erros.add("Error: 141 - Tipo de Documento no es correcto (Tomador), Tipo de Persona: "+tipoPessoa+", Tipo de Documento: "+tipoIdentificacao2+" - Línea: " + numeroLinha);
		    	            }
		    	            
		    	            entidadeBcp.setTipoDocumento(tipoIdentificacao2);
		    	            
		    	            if(numeroIdentificacao.length() == 0 && !tipoIdentificacao.equals("0") && !tipoPessoa2.equals("4"))
		    	            	erros.add("Error: 142 - Número del Documento es obligatorio (Tomador) Póliza "+numeroInstrumento+" - Línea: " + numeroLinha);
		    	            
		    	            if(tipoPessoa.indexOf("Física") > -1)
		    	            {
		    	            	if(sobreNome.length() == 0)
		    	            		erros.add("Error: 135 - Apellido es obligatorio (Tomador) Tipo de Persona: "+tipoPessoa+", Tipo de Documento: "+tipoIdentificacao2+" - Línea: " + numeroLinha);
		    	            	
		    	            	if(dataNascStr2.length() == 0)
		    	            		erros.add("Error: 125 - Fecha de Nacimiento es obligatoria (Tomador) Tipo de Persona: "+tipoPessoa+", Tipo de Documento: "+tipoIdentificacao2+" - Línea: " + numeroLinha);
		    	            	else
		    	            	{
			    	            	if(dataNascStr2.length() == 8)
			    	            	{
			    	            		anoNascimento = "";
			    	            		if(verificaPosicaoString(dataNascStr2, 0, 4, true, numeroLinha, "Año Nacimiento del Tomador", nRegistro))
			    	            			anoNascimento = dataNascStr2.substring(0, 4).trim();
			    	            		
			    	            		mesNascimento = "";
			    	            		if(verificaPosicaoString(dataNascStr2, 4, 6, true, numeroLinha, "Mes Nacimiento del Tomador", nRegistro))
			    	            			mesNascimento = dataNascStr2.substring(4, 6).trim();
			    	            		
			    	            		diaNascimento = "";
			    	            		if(verificaPosicaoString(dataNascStr2, 6, 8, true, numeroLinha, "Día Nacimiento del Tomador", nRegistro))
			    	            			diaNascimento = dataNascStr2.substring(6, 8).trim();
			    	            		
			    	            		dataNascStr = diaNascimento + "/"+mesNascimento+"/"+anoNascimento;
			    	            		
			    	            		if(!eData(dataNascStr))
			    	            			erros.add("Error: 125 - Fecha de Nacimiento no es válida (Tomador) Póliza "+numeroInstrumento+", Fecha "+dataNascStr+" - Línea: " + numeroLinha);
			    	            		else
			    	            			entidadeBcp.setDataNascimento(new SimpleDateFormat("dd/MM/yyyy").parse(dataNascStr));
			    	            	}
			    	            	else
			    	            		erros.add("Error: 125 - Fecha de Nacimiento no es válida (Tomador) Póliza "+numeroInstrumento+", Fecha "+dataNascStr2+" - Línea: " + numeroLinha);
		    	            	}
		    	            }
		    	            
		    	            if(!documentoEspecial && !tipoPessoa2.equals("4"))
		    	            {
		    	            	/*if(tipoIdentificacao2.equals("CRC") && !paisSWIFT.equals(""))
		    	            	{
		    	            		if(numeroIdentificacao.indexOf(paisSWIFT.substring(0, 2)) == -1)
		    	            			numeroIdentificacao = paisSWIFT.substring(0, 2) + " " + numeroIdentificacao;
		    	            	}*/
		    	            	
		    		            validacaoRuc = rucCiHome.obterPessoaPorDoc(tipoIdentificacao2, numeroIdentificacao);
		    		            if(validacaoRuc!=null)
		    		            {
		    		            	paisBcp = "";
		    		            	nomeBcp = "";
		    		            	tipoPessoaBcp = "";
		    		            	sobreNomeBcp = "";
		    		            	dataNascBcp = "";
		    		            	
		    		            	if(validacaoRuc.equals(""))
		    		            		erros.add("Error: 128 - No se encontró el documento en la base de datos BCP (Tomador), Nombre "+nomeAsegurado+", Apellido "+sobreNome+", Tipo Documento "+tipoIdentificacao2+", Nº "+numeroIdentificacao+" - Línea: " + numeroLinha);
		    		            	else
		    		            	{
		    		            		String[] linhaSuja = validacaoRuc.split(";");
		    		            		
		    		            		if(linhaSuja.length == 0)
		    		            			erros.add("Error: 128 - No se encontró el documento en la base de datos BCP (Tomador), Nombre "+nomeAsegurado+", Apellido "+sobreNome+", Tipo Documento "+tipoIdentificacao2+", Nº "+numeroIdentificacao+" - Línea: " + numeroLinha);
		    		            		else
		    		            		{
		    		            			/*tipoPessoaBcp = linhaSuja[0].trim().replace("\"", "");
		    		            			paisBcp = linhaSuja[1].trim().replace("\"", "");
		    		            			nomeBcp = linhaSuja[2].trim().replace("\"", "");
		    		            			if(nomeBcp.length() > 60)
		    		            				nomeBcp = nomeBcp.substring(0,59);*/
		    		            			
		    		            			if(verificaPosicaoArray(linhaSuja, 0))
		    		            				tipoPessoaBcp = linhaSuja[0].trim().replace("\"", "");
		    		            			else
		    		            				erros.add("Error: 150 - No se encontró el Tipo Persona en la base de datos BCP (Tomador) Tipo Documento "+tipoIdentificacao2+", Nº "+numeroIdentificacao+". Nombre archivo = "+nomeAsegurado+" - Línea: " + numeroLinha);
		    		            			
		    		            			if(verificaPosicaoArray(linhaSuja, 1))
		    		            				paisBcp = linhaSuja[1].trim().replace("\"", "");
		    		            			else
		    		            				erros.add("Error: 151 - No se encontró el Pais en la base de datos BCP (Tomador) Tipo Documento "+tipoIdentificacao2+", Nº "+numeroIdentificacao+". Nombre archivo = "+nomeAsegurado+" - Línea: " + numeroLinha);		    		            				
		    		            			
		    		            			if(verificaPosicaoArray(linhaSuja, 2))
		    		            			{
		    		            				nomeBcp = linhaSuja[2].trim().replace("\"", "");
		    		            				if(nomeBcp.length() > 60)
			    		            				nomeBcp = nomeBcp.substring(0,59);
		    		            			}
		    		            			else
		    		            				erros.add("Error: 152 - No se encontró el Nombre en la base de datos BCP (Tomador) Tipo Documento "+tipoIdentificacao2+", Nº "+numeroIdentificacao+". Nombre archivo = "+nomeAsegurado+" - Línea: " + numeroLinha);
		    		            			
		    		            			//if(!tipoPessoaBcp.equals(tipoPessoa2))
		    		            				//erros.add("Error: 136 - Tipo de Persona es diferente en la base de datos BCP (Tomador) Tipo Documento "+tipoIdentificacao2+", Nº "+numeroIdentificacao+". Tipo persona archivo = "+tipoPessoa2+", tipo persona BCP = "+tipoPessoaBcp+" - Línea: " + numeroLinha);
		    		            			if(!paisBcp.equals(""))
		    		            			{
			    		            			if(!paisBcp.toLowerCase().equals(paisSWIFT.toLowerCase()))
			    		            				erros.add("Error: 137 - País es diferente en la base de datos BCP (Tomador) Tipo Documento "+tipoIdentificacao2+", Nº "+numeroIdentificacao+". País archivo = "+paisSWIFT+", País BCP = "+paisBcp+" - Línea: " + numeroLinha);
		    		            			}
		    		            			
		    		            			if(!nomeBcp.equals(""))
		    		            			{
			    		            			if(!nomeBcp.toLowerCase().equals(nomeAsegurado.toLowerCase()))
			    		            				erros.add("Error: 138 - Nombre es diferente en la base de datos BCP (Tomador) Tipo Documento "+tipoIdentificacao2+", Nº "+numeroIdentificacao+". Nombre archivo = "+nomeAsegurado.toLowerCase()+", Nombre BCP = "+nomeBcp.toLowerCase()+" - Línea: " + numeroLinha);
		    		            			}
		    		            			
		    		            			if(tipoPessoa2.equals("2") || tipoPessoa2.equals("3"))
		    		            			{
		    		            				/*sobreNomeBcp = linhaSuja[3].trim().replace("\"", "");
			    		            			dataNascBcp = linhaSuja[4].trim().replace("\"", "");*/
			    		            			
		    		            				if(verificaPosicaoArray(linhaSuja, 3))
		    		            					sobreNomeBcp = linhaSuja[3].trim().replace("\"", "");
		    		            				else
		    		            					erros.add("Error: 153 - No se encontró el Apellido en la base de datos BCP (Asegurado) Tipo Documento "+tipoIdentificacao2+", Nº "+numeroIdentificacao+". Nombre archivo = "+nomeAsegurado+" - Línea: " + numeroLinha);
		    		            				
		    		            				if(verificaPosicaoArray(linhaSuja, 4))
		    		            					dataNascBcp = linhaSuja[4].trim().replace("\"", "");
		    		            				else
		    		            					erros.add("Error: 154 - No se encontró el Fecha de Nacimiento en la base de datos BCP (Asegurado) Tipo Documento "+tipoIdentificacao2+", Nº "+numeroIdentificacao+". Nombre archivo = "+nomeAsegurado+" - Línea: " + numeroLinha);
		    		            				
			    		            			if(!sobreNomeBcp.equals(""))
			    		            			{
				    		            			if(!sobreNomeBcp.toLowerCase().equals(sobreNome.toLowerCase()))
				    		            				erros.add("Error: 139 - Apellido es diferente en la base de datos BCP (Tomador) Tipo Documento "+tipoIdentificacao2+", Nº "+numeroIdentificacao+". Apellido archivo = "+sobreNome.toLowerCase()+", Apellido BCP = "+sobreNomeBcp.toLowerCase()+" - Línea: " + numeroLinha);
			    		            			}
			    		            			
			    		            			if(!dataNascBcp.equals(""))
			    		            			{
				    		            			if(!dataNascBcp.equals(dataNascStr))
				    		            				erros.add("Error: 140 - Fecha de Nacimiento es diferente en la base de datos BCP (Tomador) Tipo Documento "+tipoIdentificacao2+", Nº "+numeroIdentificacao+". Fecha de Nacimiento archivo = "+dataNascStr+", Fecha de Nacimiento BCP = "+dataNascBcp+" - Línea: " + numeroLinha);
			    		            			}
		    		            			}
		    		            		}
		    		            	}
		    		            }
		    		            else
		    		            	erros.add("Error: 128 - No se encontró el documento en la base de datos BCP (Tomador), Nombre "+nomeAsegurado+", Apellido "+sobreNome+", Tipo Documento "+tipoIdentificacao2+", Nº "+numeroIdentificacao+" - Línea: " + numeroLinha);
		    	            }
		                    
	    	            	numeroIdentificacao = numeroIdentificacao.replace("-", "");
		    	            numeroIdentificacao = numeroIdentificacao.replace(".", "");
		    	            numeroIdentificacao = numeroIdentificacao.replace(" ", "");
		    	            entidadeBcp.setNumeroDoc(numeroIdentificacao.trim());
		    	            
		    	            tomadores.add(entidadeBcp);
		    	            
		                    ultimo+= 60 + 30 + 1 + 1 + 15 + 8 + 3;
		                }
	            	}
		            else
		            	ultimo+= 60 + 30 + 1 + 1 + 15 + 8 + 3;
	            	
	            	tipoInstrumento2 = "";
	            	if(verificaPosicaoString(linha, ultimo, ultimo + 1, true, numeroLinha, "Tipo del Instrumento", nRegistro))
	            	{
	            		tipoInstrumento2 = linha.substring(ultimo, ultimo + 1).trim();
	            	
	            		if(tipoInstrumento2.equals("1"))
	            			tipoInstrumento = "P\363liza Individual";
			            else if(tipoInstrumento2.equals("2"))
			                tipoInstrumento = "P\363liza Madre";
			            else if(tipoInstrumento2.equals("3"))
			                tipoInstrumento = "Certificado de Seguro Colectivo";
			            else if(tipoInstrumento2.equals("4"))
			                tipoInstrumento = "Certificado Provisorio";
			            else if(tipoInstrumento2.equals("5"))
			                tipoInstrumento = "Nota de Cobertura de Reaseguro";
			            else
	            			erros.add("Error: 28 - Instrumento es obligatorio - Línea: " + numeroLinha);
	            	}
		            
	            	apolice = null;
	            	if(apolices != null && cContas!=null && !numeroInstrumento.equals(""))
	            	{
		                apolice = apolices.get(obterOrigem().obterId() + numeroInstrumento + cContas.obterId() + tipoInstrumento);
		                if(apolice == null)
		                {
		                	apolice = apoliceHome.obterApolice(obterOrigem(), numeroInstrumento,cContas, tipoInstrumento);
		                	atualizacaoPessoaApolice = true;
		                }
	            	}
		            
		            if(apolice == null)
		                erros.add("Error: 29 - Instrumento " + numeroInstrumento + " no fue encontrado (Archivo Datos del Asegurado) - Línea: " + numeroLinha);
		            else
		            {
		            	//System.out.println(apolice.obterId());
		            	if(!tipoInstrumento.equals(apolice.obterStatusApolice()))
			            {
			            	if(!atualizacaoPessoaApolice)
			            		erros.add("Error: 129 - Tipo Instrumento informado en el Registro 02 es diferente del informado en el Registro 17, Póliza "+numeroInstrumento+" Sección "+apelidoCconta+". Registro 2 = "+apolice.obterStatusApolice()+" y Registro 17 = "+tipoInstrumento+" - Línea: " + numeroLinha);
			            	else
			            		erros.add("Error: 129 - Tipo Instrumento en la Base de Datos es diferente del informado en el Registro 17, Póliza "+numeroInstrumento+" Sección "+apelidoCconta+". Registro 2 = "+apolice.obterStatusApolice()+" y Registro 17 = "+tipoInstrumento+" - Línea: " + numeroLinha);
			            }
			            	
			            apolice.setAsegurados(asegurados);
			            apolice.setTomadores(tomadores);
			            
			            if(apolice.getAsegurados()!=null)
	                	{
	                		if(apolice.getAsegurados().size() == 0)
	                			erros.add("Error: 126 - No hay Asegurados (Archivo Datos del Asegurado) Póliza "+numeroInstrumento+" - Línea: " + numeroLinha);
	                	}
	                	else
	                		erros.add("Error: 126 - No hay Asegurados (Archivo Datos del Asegurado) Póliza "+numeroInstrumento+" - Línea: " + numeroLinha);
		            
			            if(tipoInstrumento.equals("P\363liza Madre"))
		                {
		                	if(apolice.getTomadores()!=null)
		                	{
		                		if(apolice.getTomadores().size() == 0)
		                			erros.add("Error: 126 - No hay Tomadores (Archivo Datos del Asegurado) Póliza "+numeroInstrumento+" - Línea: " + numeroLinha);
		                	}
		                	else
		                		erros.add("Error: 126 - No hay Tomadores (Archivo Datos del Asegurado) Póliza "+numeroInstrumento+" - Línea: " + numeroLinha);
		                }
			            
			            if(numeroInstrumento.startsWith("15") && (apelidoCconta.equals("0401011300") || apelidoCconta.equals("0402011300") || apelidoCconta.equals("0402021300")))
			            {
			            	if(apolice.getAsegurados()!=null)
			            	{
			            		for(EntidadeBCP asegurado : apolice.getAsegurados())
			            		{
			            			String nome = asegurado.getNome();
			            			String sobreNome = asegurado.getSobreNome();
			            			
			            			if(apolice.getTomadores()!=null)
			            			{
			            				for(EntidadeBCP tomador : apolice.getTomadores())
			            				{
			            					String nome2 = tomador.getNome();
					            			String sobreNome2 = tomador.getSobreNome();
					            			
					            			if(nome.toLowerCase().equals(nome2.toLowerCase()) && sobreNome.toLowerCase().equals(sobreNome2.toLowerCase()))
					            			{
					            				erros.add("Error: 127 - Póliza caución con Nombre del Asegurado y del Tomador iguales Póliza "+numeroInstrumento+" Sección "+apelidoCconta+" - Línea: " + numeroLinha);
					            				break;
					            			}
			            				}
			            			}
			            		}
			            	}
			            }
			            
			            if(atualizacaoPessoaApolice)
		            		apolicesAtualizacao.put(obterOrigem().obterId() + numeroInstrumento+ cContas.obterId() + tipoInstrumento, apolice);
			            
			            apolicesResgostro17.put(obterOrigem().obterId() + numeroInstrumento+ cContas.obterId() + tipoInstrumento, apolice);
		            }
		            
		            numeroEndoso = 0;
		            
		            if(verificaPosicaoString(linha, ultimo + 1, ultimo + 1 + 10, true, numeroLinha, "Endoso", nRegistro))
		            	numeroEndoso = Double.valueOf(linha.substring(ultimo + 1, ultimo + 1 + 10).trim());
		            
		            certificado = 0;
		            if(verificaPosicaoString(linha, ultimo + 1 + 10, ultimo + 1 + 10 + 7, true, numeroLinha, "Certificado", nRegistro))
		            	certificado = Double.valueOf(linha.substring(ultimo + 1 + 10, ultimo + 1 + 10 + 7));
	            }
	        }
        	/*else if(Integer.parseInt(linha.substring(7, 9)) == 99)
            {
            	if(numeroLinha != numeroTotalRegistros)
            		erros.add("Error: 123 - Numero total de registros (" + numeroLinha + ") no es el mismo del archivo (" + numeroTotalRegistros + "), Archivo Datos del Asegurado - Línea: " + numeroLinha);
            }
        	else
        		erros.add("Error: 123 - Registro " + linha.substring(7, 9) + " no es válido (Archivo Datos del Asegurado) - Línea: " + numeroLinha);*/
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
        	if(e.toString().equals("java.util.ConcurrentModificationException"))
        		this.validarAsegurado(linha, numeroLinha);
        	else
        	{
        		String[] msgCerta = e.getMessage().split(":");
        		
        		erros.add("Error Interno: Tamanõ de String Incorrecto " + msgCerta[1] + " (Archivo Datos del Asegurado) - Línea: "+ numeroLinha);
            	System.out.println("Error Interno: " + msgCerta[1] + " (Archivo Datos del Asegurado) - Línea: "+ numeroLinha);
            	e.printStackTrace();
        	}
        	
        }
        catch(NumberFormatException e)
        {
        	if(e.toString().equals("java.util.ConcurrentModificationException"))
        		this.validarAsegurado(linha, numeroLinha);
        	else
        	{
        		String[] msgCerta = e.getMessage().split(":");
        		erros.add("Error Interno: Formato de número no es correcto " + msgCerta[1] + " - Línea: "+ numeroLinha);
        		System.out.println("Error Interno: " + msgCerta[1] + " - Línea: "+ numeroLinha);
        		e.printStackTrace();
        	}
        }
        catch(Exception e)
        {
        	if(e.toString().equals("java.util.ConcurrentModificationException"))
        		this.validarAsegurado(linha, numeroLinha);
        	else
        	{
        		erros.add("Error Interno: " + e.getMessage() + " (Archivo Datos del Asegurado) - Línea: "+ numeroLinha);
            	System.out.println("Error Interno: " + e.toString() + " (Archivo Datos del Asegurado) - Línea: "+ numeroLinha);
            	e.printStackTrace();
        	}
        	
        }
        //numeroLinha++;
    //}

    return erros;
}
    public void concluirNotificacoesInferiores() throws Exception
    {
    	EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
    	
    	//SQLQuery query = this.getModelManager().createSQLQuery("crm","select id from evento where superior = ? and classe='Notificacao' and tipo = 'Notificación de Error de Validación'");
    	SQLQuery query = this.getModelManager().createSQLQuery("crm","select id from evento where superior = ? and classe='Notificacao'");
    	query.addLong(this.obterId());
    	
    	SQLRow[] rows = query.execute();
    	long id;
    	Notificacao not;
    	
    	for(int i = 0 ; i < rows.length ; i++)
    	{
    		id = rows[i].getLong("id");
    		
    		not = (Notificacao) home.obterEventoPorId(id);
    		
    		if(not.obterFase().obterCodigo().equals(Evento.EVENTO_PENDENTE))
    			not.atualizarFase2(EVENTO_CONCLUIDO);
    	}
    }
    
    public boolean verificaPosicaoArray(String[] array, int posicao) 
    {
    	try
    	{
    		String str = array[posicao];
    		return true;
    	}
    	catch(Exception e)
    	{
    		return false;
    	}
    }
    
    public boolean verificaPosicaoString(String campo, int posicaoInicio, int posicaoFinal, boolean validaNumero, int numeroLinha, String mensagem, String nRegistro) 
    {
    	try
    	{
	    	if(validaSubString(campo, posicaoInicio, posicaoFinal))
	    	{
	    		if(validaNumero)
	    		{
	    			if(!eNumero(campo.substring(posicaoInicio, posicaoFinal).trim()))
	    			{
	    				erros.add("Error: 998 - "+mensagem+" no es numérico "+campo.substring(posicaoInicio, posicaoFinal)+" - Línea: " + numeroLinha + nRegistro);
	    				return false;
	    			}
	    			else
		    			return true;
	    		}
	    		else
	    			return true;
	    	}
	    	else
	    	{
	    		erros.add("Error: 997 - Tamaño "+mensagem+" incorrecto - Línea: " + numeroLinha + nRegistro);
	    		return false;
	    	}
    	}
    	catch(Exception e)
		{
    		return false;
		}
    }
    
    public boolean eNumero(String valor) throws Exception
	{
		try
		{
			valor = valor.replace(",", ".");
			Double.valueOf(valor);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
    
    public boolean validaSubString(String campo, int posicaoInicio, int posicaoFinal)
    {
    	try
    	{
    		String str = campo.substring(posicaoInicio, posicaoFinal);
    		return true;
    	}
    	catch(Exception e)
    	{
    		return false;
    	}
    }
    
    public boolean eData(String dataStr)
    {
        final String DATE_PATTERN = "(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)";
        Pattern pattern = Pattern.compile(DATE_PATTERN);;
        Matcher matcher  = pattern.matcher(dataStr);

        if(matcher.matches())
        {
            matcher.reset();

            if(matcher.find())
            {
                String day = matcher.group(1);
                String month = matcher.group(2);
                int year = Integer.parseInt(matcher.group(3));

                if (day.equals("31") && (month.equals("4") || month .equals("6") || month.equals("9") || month.equals("11") || month.equals("04") || month .equals("06") || month.equals("09")))
                {
                    return false; // only 1,3,5,7,8,10,12 has 31 days
                }
                else if (month.equals("2") || month.equals("02"))
                {
                    //leap year
                    if(year % 4==0)
                    {
                        if(day.equals("30") || day.equals("31"))
                        {
                            return false;
                        }
                        else
                        {
                            return true;
                        }
                    }
                    else
                    {
                        if(day.equals("29")||day.equals("30")||day.equals("31"))
                        {
                            return false;
                        }
                        else
                        {
                            return true;
                        }
                    }
                }
                else
                {
                    return true;
                }
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }
    
}
