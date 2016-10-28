// FrontEnd Plus GUI for JAD
// DeCompiled : RatioPermanente.class

package com.gio.crm.model;


// Referenced classes of package com.gio.crm.model:
//            Evento

public interface RatioPermanente
    extends Evento
{

    public abstract void atribuirAtivoCorrente(double d)
        throws Exception;

    public abstract void atribuirPassivoCorrente(double d)
        throws Exception;

    public abstract void atribuirInversao(double d)
        throws Exception;

    public abstract void atribuirDeudas(double d)
        throws Exception;

    public abstract void atribuirUso(double d)
        throws Exception;

    public abstract void atribuirVenda(double d)
        throws Exception;

    public abstract void atribuirLeasing(double d)
        throws Exception;

    public abstract void atribuirResultados(double d)
        throws Exception;

    public abstract void atualizarAtivoCorrente(double d)
        throws Exception;

    public abstract void atualizarPassivoCorrente(double d)
        throws Exception;

    public abstract void atualizarInversao(double d)
        throws Exception;

    public abstract void atualizarDeudas(double d)
        throws Exception;

    public abstract void atualizarUso(double d)
        throws Exception;

    public abstract void atualizarVenda(double d)
        throws Exception;

    public abstract void atualizarLeasing(double d)
        throws Exception;

    public abstract void atualizarResultados(double d)
        throws Exception;

    public abstract void incluir()
        throws Exception;

    public abstract double obterAtivoCorrente()
        throws Exception;

    public abstract double obterPassivoCorrente()
        throws Exception;

    public abstract double obterInversao()
        throws Exception;

    public abstract double obterDeudas()
        throws Exception;

    public abstract double obterUso()
        throws Exception;

    public abstract double obterVenda()
        throws Exception;

    public abstract double obterLeasing()
        throws Exception;

    public abstract double obterResultados()
        throws Exception;
}
