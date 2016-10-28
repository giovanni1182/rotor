// FrontEnd Plus GUI for JAD
// DeCompiled : RatioUmAno.class

package com.gio.crm.model;


// Referenced classes of package com.gio.crm.model:
//            Evento

public interface RatioUmAno
    extends Evento
{

    public abstract void atribuirPrimasDiretas(double d)
        throws Exception;

    public abstract void atribuirPrimasAceitas(double d)
        throws Exception;

    public abstract void atribuirPrimasCedidas(double d)
        throws Exception;

    public abstract void atribuirAnulacaoPrimasDiretas(double d)
        throws Exception;

    public abstract void atribuirAnulacaoPrimasAtivas(double d)
        throws Exception;

    public abstract void atribuirAnulacaoPrimasCedidas(double d)
        throws Exception;

    public abstract void atualizarPrimasDiretas(double d)
        throws Exception;

    public abstract void atualizarPrimasAceitas(double d)
        throws Exception;

    public abstract void atualizarPrimasCedidas(double d)
        throws Exception;

    public abstract void atualizarAnulacaoPrimasDiretas(double d)
        throws Exception;

    public abstract void atualizarAnulacaoPrimasAtivas(double d)
        throws Exception;

    public abstract void atualizarAnulacaoPrimasCedidas(double d)
        throws Exception;

    public abstract void incluir()
        throws Exception;

    public abstract double obterPrimasDiretas()
        throws Exception;

    public abstract double obterPrimasAceitas()
        throws Exception;

    public abstract double obterPrimasCedidas()
        throws Exception;

    public abstract double obterAnulacaoPrimasDiretas()
        throws Exception;

    public abstract double obterAnulacaoPrimasAtivas()
        throws Exception;

    public abstract double obterAnulacaoPrimasCedidas()
        throws Exception;
}
