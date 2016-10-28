// FrontEnd Plus GUI for JAD
// DeCompiled : Morosidade.class

package com.gio.crm.model;

import java.util.Date;

// Referenced classes of package com.gio.crm.model:
//            Evento, Aseguradora, Apolice, ClassificacaoContas

public interface Morosidade
    extends Evento
{

    public abstract void atribuirDataCorte(Date date)
        throws Exception;

    public abstract void atribuirNumeroParcela(int i)
        throws Exception;

    public abstract void atribuirDataVencimento(Date date)
        throws Exception;

    public abstract void atribuirDiasAtraso(int i)
        throws Exception;

    public abstract void atribuirValorGs(double d)
        throws Exception;

    public abstract void atribuirTipoMoedaValorGs(String s)
        throws Exception;

    public abstract void atribuirValorMe(double d)
        throws Exception;

    public abstract void atribuirTipoInstrumento(String s)
        throws Exception;

    public abstract void atribuirNumeroEndoso(double d)
        throws Exception;

    public abstract void atribuirCertificado(double d)
        throws Exception;

    public abstract void atualizarDataCorte(Date date)
        throws Exception;

    public abstract void atualizarNumeroParcela(int i)
        throws Exception;

    public abstract void atualizarDataVencimento(Date date)
        throws Exception;

    public abstract void atualizarDiasAtraso(int i)
        throws Exception;

    public abstract void atualizarValorGs(double d)
        throws Exception;

    public abstract void atualizarTipoMoedaValorGs(String s)
        throws Exception;

    public abstract void atualizarValorMe(double d)
        throws Exception;

    public abstract void incluir()
        throws Exception;

    public abstract Date obterDataCorte()
        throws Exception;

    public abstract int obterNumeroParcela()
        throws Exception;

    public abstract Date obterDataVencimento()
        throws Exception;

    public abstract int obterDiasAtraso()
        throws Exception;

    public abstract double obterValorGs()
        throws Exception;

    public abstract String obterTipoMoedaValorGs()
        throws Exception;

    public abstract double obterValorMe()
        throws Exception;

    public abstract String obterTipoInstrumento()
        throws Exception;

    public abstract double obterNumeroEndoso()
        throws Exception;

    public abstract double obterCertificado()
        throws Exception;

    public abstract void verificarDuplicidade(Aseguradora aseguradora, Apolice apolice, ClassificacaoContas classificacaocontas, Date date, double d, double d1, int i)
        throws Exception;
}
