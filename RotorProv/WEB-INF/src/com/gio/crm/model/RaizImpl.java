// FrontEnd Plus GUI for JAD
// DeCompiled : RaizImpl.class

package com.gio.crm.model;

import java.util.Date;

// Referenced classes of package com.gio.crm.model:
//            EntidadeImpl, Raiz, UsuarioHome, Usuario, 
//            Entidade

public class RaizImpl extends EntidadeImpl
    implements Raiz
{

    public RaizImpl()
    {
    }

    public void atribuirNome(String s)
        throws Exception
    {
    }

    public void atribuirResponsavel(Usuario usuario1)
        throws Exception
    {
    }

    public void atribuirSuperior(Entidade entidade1)
        throws Exception
    {
    }

    public Date obterAtualizacao()
        throws Exception
    {
        return new Date(0L);
    }

    public Date obterCriacao()
        throws Exception
    {
        return new Date(0L);
    }

    public String obterNome()
    {
        return "Raiz";
    }

    public Usuario obterResponsavel()
        throws Exception
    {
        return null;
    }

    public boolean permiteIncluirEntidadesInferiores()
        throws Exception
    {
        UsuarioHome home = (UsuarioHome)getModelManager().getHome("UsuarioHome");
        Usuario usuarioAtual = home.obterUsuarioPorUser(getModelManager().getUser());
        return usuarioAtual.obterId() == 1L;
    }
}
