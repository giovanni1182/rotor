// FrontEnd Plus GUI for JAD
// DeCompiled : RaizHomeImpl.class

package com.gio.crm.model;

import infra.model.Home;

// Referenced classes of package com.gio.crm.model:
//            RaizHome, EntidadeHomeImpl, Raiz

public class RaizHomeImpl extends Home
    implements RaizHome
{

    private Raiz raiz;

    public RaizHomeImpl()
    {
    }

    public Raiz obterRaiz()
        throws Exception
    {
        if(raiz == null)
        {
            EntidadeHomeImpl entidadeHome = (EntidadeHomeImpl)getModelManager().getHome("EntidadeHome");
            raiz = (Raiz)entidadeHome.instanciarEntidade(0L, "Raiz");
        }
        return raiz;
    }
}
