// FrontEnd Plus GUI for JAD
// DeCompiled : Refinacao.class

package com.gio.crm.model;

import java.util.Date;

// Referenced classes of package com.gio.crm.model:
//            Evento, Aseguradora, Apolice, ClassificacaoContas

public interface Refinacao
    extends Evento
{

    public abstract void atribuirFinanciamentoGs(double d)
        throws Exception;

    public abstract void atribuirTipoMoedaFinanciamentoGs(String s)
        throws Exception;

    public abstract void atribuirFinanciamentoMe(double d)
        throws Exception;

    public abstract void atribuirQtdeParcelas(int i)
        throws Exception;

    public abstract void atribuirTipoInstrumento(String s)
        throws Exception;

    public abstract void atribuirNumeroEndoso(double d)
        throws Exception;

    public abstract void atribuirCertificado(double d)
        throws Exception;

    public abstract void atualizarFinanciamentoGs(double d)
        throws Exception;

    public abstract void atualizarTipoMoedaFinanciamentoGs(String s)
        throws Exception;

    public abstract void atualizarFinanciamentoMe(double d)
        throws Exception;

    public abstract void atualizarQtdeParcelas(int i)
        throws Exception;

    public abstract void incluir()
        throws Exception;

    public abstract double obterFinanciamentoGs()
        throws Exception;

    public abstract String obterTipoMoedaFinanciamentoGs()
        throws Exception;

    public abstract double obterFinanciamentoMe()
        throws Exception;

    public abstract int obterQtdeParcelas()
        throws Exception;

    public abstract String obterTipoInstrumento()
        throws Exception;

    public abstract double obterNumeroEndoso()
        throws Exception;

    public abstract double obterCertificado()
        throws Exception;

    public abstract void verificarDuplicidade(Aseguradora aseguradora, Apolice apolice, ClassificacaoContas classificacaocontas, double d, double d1, 
            Date date)
        throws Exception;
}
