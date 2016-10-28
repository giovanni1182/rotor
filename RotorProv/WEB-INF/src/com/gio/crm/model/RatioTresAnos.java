// FrontEnd Plus GUI for JAD
// DeCompiled : RatioTresAnos.class

package com.gio.crm.model;


// Referenced classes of package com.gio.crm.model:
//            Evento

public interface RatioTresAnos
    extends Evento
{

    public abstract void atribuirSinistrosPagos(double d)
        throws Exception;

    public abstract void atribuirGastosSinistros(double d)
        throws Exception;

    public abstract void atribuirSinistrosRecuperados(double d)
        throws Exception;

    public abstract void atribuirGastosRecuperados(double d)
        throws Exception;

    public abstract void atribuirRecuperoSinistros(double d)
        throws Exception;

    public abstract void atribuirProvisoes(double d)
        throws Exception;

    public abstract void atualizarSinistrosPagos(double d)
        throws Exception;

    public abstract void atualizarGastosSinistros(double d)
        throws Exception;

    public abstract void atualizarSinistrosRecuperados(double d)
        throws Exception;

    public abstract void atualizarGastosRecuperados(double d)
        throws Exception;

    public abstract void atualizarRecuperoSinistros(double d)
        throws Exception;

    public abstract void atualizarProvisoes(double d)
        throws Exception;

    public abstract void incluir()
        throws Exception;

    public abstract double obterSinistrosPagos()
        throws Exception;

    public abstract double obterGastosSinistros()
        throws Exception;

    public abstract double obterSinistrosRecuperados()
        throws Exception;

    public abstract double obterGastosRecuperados()
        throws Exception;

    public abstract double obterRecuperoSinistros()
        throws Exception;

    public abstract double obterProvisoes()
        throws Exception;
}
