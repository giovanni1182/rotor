// FrontEnd Plus GUI for JAD
// DeCompiled : DadosPrevisao.class

package com.gio.crm.model;

import java.util.Date;

// Referenced classes of package com.gio.crm.model:
//            Evento, Aseguradora, Apolice, ClassificacaoContas

public interface DadosPrevisao
    extends Evento
{

    public abstract void atualizarDataCorte(Date date)
        throws Exception;

    public abstract void atualizarCurso(double d)
        throws Exception;

    public abstract void atualizarSinistroPendente(double d)
        throws Exception;

    public abstract void atualizarReservasMatematicas(double d)
        throws Exception;

    public abstract void atualizarFundosAcumulados(double d)
        throws Exception;

    public abstract void atualizarPremios(double d)
        throws Exception;

    public abstract void atribuirDataCorte(Date date)
        throws Exception;

    public abstract void atribuirCurso(double d)
        throws Exception;

    public abstract void atribuirSinistroPendente(double d)
        throws Exception;

    public abstract void atribuirReservasMatematicas(double d)
        throws Exception;

    public abstract void atribuirFundosAcumulados(double d)
        throws Exception;

    public abstract void atribuirPremios(double d)
        throws Exception;

    public abstract void atribuirTipoInstrumento(String s)
        throws Exception;

    public abstract void atribuirNumeroEndoso(double d)
        throws Exception;

    public abstract void atribuirCertificado(double d)
        throws Exception;

    public abstract void incluir()
        throws Exception;

    public abstract Date obterDataCorte()
        throws Exception;

    public abstract double obterCurso()
        throws Exception;

    public abstract double obterSinistroPendente()
        throws Exception;

    public abstract double obterReservasMatematicas()
        throws Exception;

    public abstract double obterFundosAcumulados()
        throws Exception;

    public abstract double obterPremios()
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
