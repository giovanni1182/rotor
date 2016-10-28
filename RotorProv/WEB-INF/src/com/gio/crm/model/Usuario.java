// FrontEnd Plus GUI for JAD
// DeCompiled : Usuario.class

package com.gio.crm.model;

import java.util.Collection;

// Referenced classes of package com.gio.crm.model:
//            Entidade

public interface Usuario extends Entidade
{
    public abstract void atribuirChave(String s)
        throws Exception;

    public abstract void atribuirAlcada(double d)
        throws Exception;

    public abstract void atualizarAlcada(double d)
        throws Exception;

    public abstract void atualizarSenha(String s, String s1, String s2)
        throws Exception;

    public abstract void converterEmPessoa()
        throws Exception;

    public abstract double obterAlcada()
        throws Exception;

    public abstract String obterChave()
        throws Exception;

    public abstract boolean permiteAtualizarSenha()
        throws Exception;

    public abstract boolean permiteConverterParaPessoa()
        throws Exception;

    public abstract boolean possuiResponsabilidades()
        throws Exception;

    public abstract boolean possuiSenha()
        throws Exception;

    public abstract boolean verificarSenha(String s)
        throws Exception;

    public abstract Collection verificarAgendas()
        throws Exception;
}
