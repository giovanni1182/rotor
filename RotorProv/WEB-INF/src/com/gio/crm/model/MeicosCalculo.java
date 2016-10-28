// FrontEnd Plus GUI for JAD
// DeCompiled : MeicosCalculo.class

package com.gio.crm.model;


// Referenced classes of package com.gio.crm.model:
//            Evento

public interface MeicosCalculo
    extends Evento
{

    public abstract void atribuirValor(double d)
        throws Exception;

    public abstract void atualizarValorIndicador(double d)
        throws Exception;

    public abstract void incluir()
        throws Exception;

    public abstract double obterValorIndicador()
        throws Exception;
}
