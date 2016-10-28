// FrontEnd Plus GUI for JAD
// DeCompiled : UsuarioImpl.class

package com.gio.crm.model;

import java.util.Collection;

import infra.sql.SQLQuery;
import infra.sql.SQLUpdate;

// Referenced classes of package com.gio.crm.model:
//            EntidadeImpl, Usuario, UsuarioHome, UsuarioHomeImpl, 
//            EventoHome

public class UsuarioImpl extends EntidadeImpl
    implements Usuario
{

    private String chave;
    private Boolean possuiSenha;
    private double alcada;

    public UsuarioImpl()
    {
    }

    public void atribuirAlcada(double alcada)
        throws Exception
    {
        this.alcada = alcada;
    }

    public void atribuirChave(String chave)
        throws Exception
    {
        this.chave = chave;
    }

    public void atualizar()
        throws Exception
    {
        super.atualizar();
        if(chave != null)
        {
            UsuarioHome usuarioHome = (UsuarioHome)getModelManager().getHome("UsuarioHome");
            Usuario usuario = usuarioHome.obterUsuarioPorChave(chave);
            if(usuario != null && usuario.obterId() != obterId())
                throw new Exception("A chave " + chave + " j\341 est\341 sendo utilizada pelo usu\341rio " + usuario.obterNome());
            SQLUpdate update = getModelManager().createSQLUpdate("update usuario set chave=? where id=?");
            update.addString(chave);
            update.addLong(obterId());
            update.execute();
        }
    }

    public void atualizarAlcada(double alcada)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("update usuario set alcada=? where id=?");
        update.addDouble(alcada);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarSenha(String senhaAtual, String novaSenha1, String novaSenha2)
        throws Exception
    {
        if(!verificarSenha(senhaAtual))
            throw new Exception("A senha atual n\343o confere");
        if(!novaSenha1.equals(novaSenha2))
        {
            throw new Exception("As duas novas senhas n\343o conferem");
        } else
        {
            SQLUpdate update = getModelManager().createSQLUpdate("update usuario set senha=? where id=?");
            update.addString(novaSenha1);
            update.addLong(obterId());
            update.execute();
            return;
        }
    }

    public void converterEmPessoa()
        throws Exception
    {
        atualizarClasse("Pessoa");
        SQLUpdate update = getModelManager().createSQLUpdate("delete from usuario where id=?");
        update.addLong(obterId());
        update.execute();
    }

    public void excluir()
        throws Exception
    {
        super.excluir();
        SQLUpdate update1 = getModelManager().createSQLUpdate("delete from usuario where id=?");
        update1.addLong(obterId());
        update1.execute();
    }

    public void incluir()
        throws Exception
    {
        super.incluir();
        UsuarioHome usuarioHome = (UsuarioHome)getModelManager().getHome("UsuarioHome");
        Usuario usuario = usuarioHome.obterUsuarioPorChave(chave);
        if(usuario != null && usuario.obterId() != obterId())
        {
            throw new Exception("A chave '" + chave + "' j\341 est\341 sendo utilizada pelo usu\341rio '" + usuario.obterNome() + "'");
        } else
        {
            SQLUpdate update1 = getModelManager().createSQLUpdate("insert into usuario (id, chave, senha) values (?, ?, '')");
            update1.addLong(obterId());
            update1.addString(chave);
            update1.execute();
            return;
        }
    }

    public double obterAlcada()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select alcada from usuario where id=?");
        query.addLong(obterId());
        alcada = query.executeAndGetFirstRow().getDouble("alcada");
        return alcada;
    }

    public String obterChave()
        throws Exception
    {
        if(this.chave == null)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select chave from usuario where id=?");
            query.addLong(obterId());
            String chave = query.executeAndGetFirstRow().getString("chave");
            if(chave != null)
                this.chave = chave.toLowerCase();
        }
        return this.chave;
    }

    public boolean permiteAtualizarSenha()
        throws Exception
    {
        UsuarioHome usuarioHome = (UsuarioHome)getModelManager().getHome("UsuarioHome");
        Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(getModelManager().getUser());
        return usuarioAtual.obterId() == obterId();
    }

    public boolean permiteConverterParaPessoa()
        throws Exception
    {
        UsuarioHome usuarioHome = (UsuarioHome)getModelManager().getHome("UsuarioHome");
        Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(getModelManager().getUser());
        return usuarioAtual.obterId() == obterResponsavel().obterId() && obterId() != usuarioAtual.obterId();
    }

    public boolean possuiResponsabilidades()
        throws Exception
    {
        UsuarioHomeImpl home = (UsuarioHomeImpl)getModelManager().getHome("UsuarioHome");
        return home.possuiResponsabilidades(this);
    }

    public boolean possuiSenha()
        throws Exception
    {
        if(possuiSenha == null)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select senha from usuario where id=?");
            query.addLong(obterId());
            String senha = query.executeAndGetFirstRow().getString("senha");
            possuiSenha = new Boolean(senha != null && !senha.equals(""));
        }
        return possuiSenha.booleanValue();
    }

    public boolean verificarSenha(String senha)
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select senha from usuario where id=?");
        query.addLong(obterId());
        String s = query.executeAndGetFirstRow().getString("senha");
        return senha.equals(s);
    }

    public Collection verificarAgendas()
        throws Exception
    {
        EventoHome eventoHome = (EventoHome)getModelManager().getHome("EventoHome");
        return eventoHome.obterAgendas(true);
    }
}
