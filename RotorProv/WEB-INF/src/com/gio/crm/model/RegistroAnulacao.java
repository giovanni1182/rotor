// FrontEnd Plus GUI for JAD
// DeCompiled : RegistroAnulacao.class

package com.gio.crm.model;

import java.util.Date;

// Referenced classes of package com.gio.crm.model:
//            Evento, Entidade, Reaseguradora, Aseguradora, 
//            Apolice, ClassificacaoContas

public interface RegistroAnulacao
    extends Evento
{

    public abstract void atribuirReaeguradora(Entidade entidade)
        throws Exception;

    public abstract void atribuirTipoContrato(String s)
        throws Exception;

    public abstract void atribuirDataAnulacao(Date date)
        throws Exception;

    public abstract void atribuirCapitalGs(double d)
        throws Exception;

    public abstract void atribuirTipoMoedaCapitalGs(String s)
        throws Exception;

    public abstract void atribuirCapitalMe(double d)
        throws Exception;

    public abstract void atribuirDiasCorridos(int i)
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

    public abstract void atribuirTipoInstrumento(String s)
        throws Exception;

    public abstract void atribuirNumeroEndoso(double d)
        throws Exception;

    public abstract void atribuirCertificado(double d)
        throws Exception;

    public abstract void atualizarReaeguradora(Reaseguradora reaseguradora)
        throws Exception;

    public abstract void atualizarTipoContrato(String s)
        throws Exception;

    public abstract void atualizarDataAnulacao(Date date)
        throws Exception;

    public abstract void atualizarCapitalGs(double d)
        throws Exception;

    public abstract void atualizarTipoMoedaCapitalGs(String s)
        throws Exception;

    public abstract void atualizarCapitalMe(double d)
        throws Exception;

    public abstract void atualizarDiasCorridos(int i)
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

    public abstract void incluir()
        throws Exception;

    public abstract Entidade obterReaeguradora()
        throws Exception;

    public abstract String obterTipoContrato()
        throws Exception;

    public abstract Date obterDataAnulacao()
        throws Exception;

    public abstract double obterCapitalGs()
        throws Exception;

    public abstract String obterTipoMoedaCapitalGs()
        throws Exception;

    public abstract double obterCapitalMe()
        throws Exception;

    public abstract int obterDiasCorridos()
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

    public abstract String obterTipoInstrumento()
        throws Exception;

    public abstract double obterNumeroEndoso()
        throws Exception;

    public abstract double obterCertificado()
        throws Exception;

    public abstract void verificarDuplicidade(Aseguradora aseguradora, Apolice apolice, DadosReaseguro dados, ClassificacaoContas cContas, Entidade reaseguradora, String tipo, double numeroEndoso, double certificado)
        throws Exception;
}
