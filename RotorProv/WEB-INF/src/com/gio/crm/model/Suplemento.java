// FrontEnd Plus GUI for JAD
// DeCompiled : Suplemento.class

package com.gio.crm.model;

import java.util.Date;

// Referenced classes of package com.gio.crm.model:
//            Evento, Aseguradora, Apolice, ClassificacaoContas

public interface Suplemento
    extends Evento
{

    public abstract void atribuirNumero(String s)
        throws Exception;

    public abstract void atribuirDataEmissao(Date date)
        throws Exception;

    public abstract void atribuirRazao(String s)
        throws Exception;

    public abstract void atribuirPrimaGs(double d)
        throws Exception;

    public abstract void atribuirTipoMoedaPrimaGs(String s)
        throws Exception;

    public abstract void atribuirPrimaMe(double d)
        throws Exception;

    public abstract void atribuirTipoInstrumento(String s)
        throws Exception;

    public abstract void atribuirNumeroEndoso(double d)
        throws Exception;

    public abstract void atribuirCertificado(double d)
        throws Exception;

    public abstract void atualizarNumero(String s)
        throws Exception;

    public abstract void atualizarDataEmissao(Date date)
        throws Exception;

    public abstract void atualizarRazao(String s)
        throws Exception;

    public abstract void atualizarPrimaGs(double d)
        throws Exception;

    public abstract void atualizarTipoMoedaPrimaGs(String s)
        throws Exception;

    public abstract void atualizarPrimaMe(double d)
        throws Exception;

    public abstract void incluir()
        throws Exception;

    public abstract String obterNumero()
        throws Exception;

    public abstract Date obterDataEmissao()
        throws Exception;

    public abstract String obterRazao()
        throws Exception;

    public abstract double obterPrimaGs()
        throws Exception;

    public abstract String obterTipoMoedaPrimaGs()
        throws Exception;

    public abstract double obterPrimaMe()
        throws Exception;

    public abstract String obterTipoInstrumento()
        throws Exception;

    public abstract double obterNumeroEndoso()
        throws Exception;

    public abstract double obterCertificado()
        throws Exception;

    public abstract void verificarDuplicidade(Aseguradora aseguradora, Apolice apolice, ClassificacaoContas classificacaocontas, double d, double d1)
        throws Exception;
}
