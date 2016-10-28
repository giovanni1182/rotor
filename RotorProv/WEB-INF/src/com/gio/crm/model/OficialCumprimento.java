// FrontEnd Plus GUI for JAD
// DeCompiled : OficialCumprimento.class

package com.gio.crm.model;


// Referenced classes of package com.gio.crm.model:
//            Entidade, Aseguradora

public interface OficialCumprimento
    extends Entidade
{

    public abstract void atualizarAseguradora(Aseguradora aseguradora)
        throws Exception;

    public abstract void incluir()
        throws Exception;

    public abstract Aseguradora obterAseguradora()
        throws Exception;
}
