// FrontEnd Plus GUI for JAD
// DeCompiled : UsuarioHome.class

package com.gio.crm.model;

import java.util.Collection;

import infra.security.User;

// Referenced classes of package com.gio.crm.model:
//            Usuario

public interface UsuarioHome
{

    public abstract Usuario obterUsuarioPorChave(String s)
        throws Exception;

    public abstract Usuario obterUsuarioPorUser(User user)
        throws Exception;

    public abstract Collection obterUsuarios()
        throws Exception;

    public abstract boolean possuiResponsabilidades(Usuario usuario)
        throws Exception;
    
    Usuario obterUsuarioPorNivel(String nivel) throws Exception;
}
