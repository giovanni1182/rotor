// FrontEnd Plus GUI for JAD
// DeCompiled : RegistroCobranca.class

package com.gio.crm.model;

import java.util.Date;

// Referenced classes of package com.gio.crm.model:
//            Evento, Aseguradora, Apolice, ClassificacaoContas

public interface RegistroCobranca
    extends Evento
{

    public abstract void atribuirDataCobranca(Date date)
        throws Exception;

    public abstract void atribuirDataVencimento(Date date)
        throws Exception;

    public abstract void atribuirNumeroParcela(int i)
        throws Exception;

    public abstract void atribuirValorCobrancaGs(double d)
        throws Exception;

    public abstract void atribuirTipoMoedaValorCobrancaGs(String s)
        throws Exception;

    public abstract void atribuirValorCobrancaMe(double d)
        throws Exception;

    public abstract void atribuirValorInteres(double d)
        throws Exception;

    public abstract void atribuirTipoInstrumento(String s)
        throws Exception;

    public abstract void atribuirNumeroEndoso(double d)
        throws Exception;

    public abstract void atribuirCertificado(double d)
        throws Exception;

    public abstract void atualizarDataCobranca(Date date)
        throws Exception;

    public abstract void atualizarDataVencimento(Date date)
        throws Exception;

    public abstract void atualizarNumeroParcela(int i)
        throws Exception;

    public abstract void atualizarValorCobrancaGs(double d)
        throws Exception;

    public abstract void atualizarTipoMoedaValorCobrancaGs(String s)
        throws Exception;

    public abstract void atualizarValorCobrancaMe(double d)
        throws Exception;

    public abstract void atualizarValorInteres(double d)
        throws Exception;

    public abstract void incluir()
        throws Exception;

    public abstract Date obterDataCobranca()
        throws Exception;

    public abstract Date obterDataVencimento()
        throws Exception;

    public abstract int obterNumeroParcela()
        throws Exception;

    public abstract double obterValorCobrancaGs()
        throws Exception;

    public abstract String obterTipoMoedaValorCobrancaGs()
        throws Exception;

    public abstract double obterValorCobrancaMe()
        throws Exception;

    public abstract double obterValorInteres()
        throws Exception;

    public abstract String obterTipoInstrumento()
        throws Exception;

    public abstract double obterNumeroEndoso()
        throws Exception;

    public abstract double obterCertificado()
        throws Exception;

    public abstract void verificarDuplicidade(Aseguradora aseguradora, Apolice apolice, ClassificacaoContas classificacaocontas, Date date, int i, double d, 
            double d1)
        throws Exception;
}
