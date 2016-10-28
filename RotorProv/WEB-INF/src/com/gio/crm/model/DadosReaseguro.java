// FrontEnd Plus GUI for JAD
// DeCompiled : DadosReaseguro.class

package com.gio.crm.model;

import java.util.Date;


// Referenced classes of package com.gio.crm.model:
//            Evento, Entidade, Aseguradora, Apolice, 
//            ClassificacaoContas

public interface DadosReaseguro
    extends Evento
{

	void atribuirDataIniApo(Date data) throws Exception;
	void atribuirDataFimApo(Date data) throws Exception;
	
	public abstract void atualizarReaseguradora(Entidade entidade)
        throws Exception;

    public abstract void atualizarTipoContrato(String s)
        throws Exception;

    public abstract void atualizarCorredora(Entidade entidade)
        throws Exception;

    public abstract void atualizarCapitalGs(double d)
        throws Exception;

    public abstract void atualizarTipoMoedaCapitalGs(String s)
        throws Exception;

    public abstract void atualizarCapitalMe(double d)
        throws Exception;

    public abstract void atualizarPrimaGs(double d)
        throws Exception;

    public abstract void atualizarTipoMoedaPrimaGs(String s)
        throws Exception;

    public abstract void atualizarPrimaMe(double d)
        throws Exception;

    public abstract void atualizarComissaoGs(double d)
        throws Exception;

    public abstract void atualizarTipoMoedaComissaoGs(String s)
        throws Exception;

    public abstract void atualizarComissaoMe(double d)
        throws Exception;

    public abstract void atualizarSituacao(String s)
        throws Exception;

    public abstract void atribuirReaseguradora(Entidade entidade)
        throws Exception;

    public abstract void atribuirTipoContrato(String s)
        throws Exception;

    public abstract void atribuirCorredora(Entidade entidade)
        throws Exception;

    public abstract void atribuirCapitalGs(double d)
        throws Exception;

    public abstract void atribuirTipoMoedaCapitalGs(String s)
        throws Exception;

    public abstract void atribuirCapitalMe(double d)
        throws Exception;

    public abstract void atribuirPrimaGs(double d)
        throws Exception;

    public abstract void atribuirTipoMoedaPrimaGs(String s)
        throws Exception;

    public abstract void atribuirPrimaMe(double d)
        throws Exception;

    public abstract void atribuirComissaoGs(double d)
        throws Exception;

    public abstract void atribuirTipoMoedaComissaoGs(String s)
        throws Exception;

    public abstract void atribuirComissaoMe(double d)
        throws Exception;

    public abstract void atribuirSituacao(String s)
        throws Exception;

    public abstract void atribuirValorEndoso(double d)
        throws Exception;

    public abstract void atribuirTipoInstrumento(String s)
        throws Exception;

    public abstract void atribuirCertificado(double d)
        throws Exception;

    public abstract void incluir()
        throws Exception;

    public abstract Entidade obterReaseguradora()
        throws Exception;

    public abstract String obterTipoContrato()
        throws Exception;

    public abstract Entidade obterCorredora()
        throws Exception;

    public abstract double obterCapitalGs()
        throws Exception;

    public abstract String obterTipoMoedaCapitalGs()
        throws Exception;

    public abstract double obterCapitalMe()
        throws Exception;

    public abstract double obterPrimaGs()
        throws Exception;

    public abstract String obterTipoMoedaPrimaGs()
        throws Exception;

    public abstract double obterPrimaMe()
        throws Exception;

    public abstract double obterComissaoGs()
        throws Exception;

    public abstract String obterTipoMoedaComissaoGs()
        throws Exception;

    public abstract double obterComissaoMe()
        throws Exception;

    public abstract String obterSituacao()
        throws Exception;

    public abstract double obterValorEndoso()
        throws Exception;

    public abstract String obterTipoInstrumento()
        throws Exception;

    public abstract double obterCertificado()
        throws Exception;

    public abstract void verificarDuplicidade(Aseguradora aseguradora, Apolice apolice, ClassificacaoContas classificacaocontas, Entidade entidade, String s, double d, 
            double d1)
        throws Exception;
}
