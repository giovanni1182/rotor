// FrontEnd Plus GUI for JAD
// DeCompiled : AnulacaoInstrumento.class

package com.gio.crm.model;

import java.util.Date;

// Referenced classes of package com.gio.crm.model:
//            Evento, Aseguradora, Apolice, ClassificacaoContas

public interface AnulacaoInstrumento
    extends Evento
{

    public abstract void atribuirDataAnulacao(Date date)
        throws Exception;

    public abstract void atribuirCapitalGs(double d)
        throws Exception;

    public abstract void atribuirTipoMoedaCapitalGs(String s)
        throws Exception;

    public abstract void atribuirCapitalMe(double d)
        throws Exception;

    public abstract void atribuirSolicitadoPor(String s)
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

    public abstract void atribuirComissaoRecuperarGs(double d)
        throws Exception;

    public abstract void atribuirTipoMoedaComissaoRecuperarGs(String s)
        throws Exception;

    public abstract void atribuirComissaoRecuperarMe(double d)
        throws Exception;

    public abstract void atribuirSaldoAnulacaoGs(double d)
        throws Exception;

    public abstract void atribuirTipoMoedaSaldoAnulacaoGs(String s)
        throws Exception;

    public abstract void atribuirSaldoAnulacaoMe(double d)
        throws Exception;

    public abstract void atribuirDestinoSaldoAnulacao(String s)
        throws Exception;

    public abstract void atribuirTipoInstrumento(String s)
        throws Exception;

    public abstract void atribuirNumeroEndoso(double d)
        throws Exception;

    public abstract void atribuirCertificado(double d)
        throws Exception;

    public abstract void atualizarDataAnulacao(Date date)
        throws Exception;

    public abstract void atualizarCapitalGs(double d)
        throws Exception;

    public abstract void atualizarTipoMoedaCapitalGs(String s)
        throws Exception;

    public abstract void atualizarCapitalMe(double d)
        throws Exception;

    public abstract void atualizarSolicitadoPor(String s)
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

    public abstract void atualizarComissaoRecuperarGs(double d)
        throws Exception;

    public abstract void atualizarTipoMoedaComissaoRecuperarGs(String s)
        throws Exception;

    public abstract void atualizarComissaoRecuperarMe(double d)
        throws Exception;

    public abstract void atualizarSaldoAnulacaoGs(double d)
        throws Exception;

    public abstract void atualizarTipoMoedaSaldoAnulacaoGs(String s)
        throws Exception;

    public abstract void atualizarSaldoAnulacaoMe(double d)
        throws Exception;

    public abstract void atualizarDestinoSaldoAnulacao(String s)
        throws Exception;

    public abstract void incluir()
        throws Exception;

    public abstract Date obterDataAnulacao()
        throws Exception;

    public abstract double obterCapitalGs()
        throws Exception;

    public abstract String obterTipoMoedaCapitalGs()
        throws Exception;

    public abstract double obterCapitalMe()
        throws Exception;

    public abstract String obterSolicitadoPor()
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

    public abstract double obterComissaoRecuperarGs()
        throws Exception;

    public abstract String obterTipoMoedaComissaoRecuperarGs()
        throws Exception;

    public abstract double obterComissaoRecuperarMe()
        throws Exception;

    public abstract double obterSaldoAnulacaoGs()
        throws Exception;

    public abstract String obterTipoMoedaSaldoAnulacaoGs()
        throws Exception;

    public abstract double obterSaldoAnulacaoMe()
        throws Exception;

    public abstract String obterDestinoSaldoAnulacao()
        throws Exception;

    public abstract String obterTipoInstrumento()
        throws Exception;

    public abstract double obterNumeroEndoso()
        throws Exception;

    public abstract double obterCertificado()
        throws Exception;

    public abstract void verificarDuplicidade(Aseguradora aseguradora, Apolice apolice, ClassificacaoContas classificacaocontas, Date date, double d, double d1)
        throws Exception;
}
