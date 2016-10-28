// FrontEnd Plus GUI for JAD
// DeCompiled : UsuarioHomeImpl.class

package com.gio.crm.model;

import java.util.Collection;
import java.util.HashMap;

import infra.model.Home;
import infra.security.User;
import infra.sql.SQLQuery;
import infra.sql.SQLRow;

// Referenced classes of package com.gio.crm.model:
//            UsuarioHome, Usuario, EntidadeHome, EntidadeHomeImpl

public class UsuarioHomeImpl extends Home
    implements UsuarioHome
{

    private HashMap usuariosPorChave;

    public UsuarioHomeImpl()
    {
        usuariosPorChave = new HashMap();
    }

    public Usuario obterUsuarioPorChave(String chave)
        throws Exception
    {
        Usuario usuario = (Usuario)usuariosPorChave.get(chave);
        if(usuario == null)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select id from usuario where chave=?");
            query.addString(chave);
            long id = query.executeAndGetFirstRow().getLong("id");
            if(id > 0L)
            {
                EntidadeHome entidadeHome = (EntidadeHome)getModelManager().getHome("EntidadeHome");
                usuario = (Usuario)entidadeHome.obterEntidadePorId(id);
                usuariosPorChave.put(chave, usuario);
            }
        }
        return usuario;
    }

    public Usuario obterUsuarioPorUser(User user)
        throws Exception
    {
        return obterUsuarioPorChave(user.getName());
    }

    public Collection obterUsuarios()
        throws Exception
    {
        EntidadeHomeImpl entidadeHome = (EntidadeHomeImpl)getModelManager().getHome("EntidadeHome");
        SQLQuery query = getModelManager().createSQLQuery("crm", "select entidade.id,entidade.classe from usuario,entidade where usuario.id=entidade.id order by entidade.nome");
        return entidadeHome.instanciarEntidades(query.execute());
    }

    public boolean possuiResponsabilidades(Usuario usuario)
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select count(*) as quantidade from entidade where responsavel=?");
        query.addLong(usuario.obterId());
        SQLRow row = query.executeAndGetFirstRow();
        long quantidade = row.getLong("quantidade");
        return quantidade > 0L;
    }
    
    public Usuario obterUsuarioPorNivel(String nivel) throws Exception
    {
    	EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
    	Usuario usuario = null;
    	
    	SQLQuery query = this.getModelManager().createSQLQuery("crm","select id from usuario where nivel = ?");
    	query.addString(nivel);
    	
    	long id = query.executeAndGetFirstRow().getLong("id");
    	
    	if(id > 0)
    		usuario = (Usuario) home.obterEntidadePorId(id);
    	
    	return usuario;
    	
    }
}
