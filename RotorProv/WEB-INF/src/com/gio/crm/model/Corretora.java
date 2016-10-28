// FrontEnd Plus GUI for JAD
// DeCompiled : Corretora.class

package com.gio.crm.model;

import java.util.Collection;

// Referenced classes of package com.gio.crm.model:
//            Entidade, Aseguradora

public interface Corretora
    extends Entidade
{

    public abstract void atualizarAseguradora(Aseguradora aseguradora)
        throws Exception;

    public abstract Aseguradora obterAseguradora()
        throws Exception;

    public abstract void atualizarRamo(String s)
        throws Exception;

    public abstract void adicionarNovoRamo(String s)
        throws Exception;

    public abstract String obterRamo()
        throws Exception;

    public abstract void incluir()
        throws Exception;

    public abstract Collection obterNomeRamos()
        throws Exception;
}
