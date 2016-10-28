// FrontEnd Plus GUI for JAD
// DeCompiled : Plano.class

package com.gio.crm.model;

import java.util.Collection;
import java.util.Date;

// Referenced classes of package com.gio.crm.model:
//            Evento

public interface Plano
    extends Evento
{

    public static final String CHEFE_DIVISAO = "chefedivisao";
    public static final String INTENDENTE = "intendente";
    public static final String SUPERINTENDENTE = "superintendente";
    public static final String APROVADA = "aprovado";
    public static final String REJEITADA = "rejeitado";
    public static final String SUSPENSA = "suspenso";
    public static final String CANCELADA = "cancelado";

    public abstract void atualizarRamo(String s)
        throws Exception;

    public abstract void atualizarSecao(String s)
        throws Exception;

    public abstract void atualizarPlano(String s)
        throws Exception;

    public abstract void atualizarIdentificador(String s)
        throws Exception;

    public abstract void atualizarResolucao(String s)
        throws Exception;

    public abstract void atualizarDataResolucao(Date date)
        throws Exception;

    public abstract void atualizarSituacao(String s)
        throws Exception;

    public abstract void atualizarDenominacao(String s)
        throws Exception;

    public abstract void incluir()
        throws Exception;

    public abstract String obterRamo()
        throws Exception;

    public abstract String obterSecao()
        throws Exception;

    public abstract String obterPlano()
        throws Exception;

    public abstract String obterIdentificador()
        throws Exception;

    public abstract String obterResolucao()
        throws Exception;

    public abstract Date obterDataResolucao()
        throws Exception;

    public abstract String obterSituacao()
        throws Exception;

    public abstract String obterDenominacao()
        throws Exception;

    public abstract void adicionarNovaSecao(String s)
        throws Exception;

    public abstract Collection obterNomeSecoes()
        throws Exception;

    public abstract void adicionarNovoPlano(String s)
        throws Exception;

    public abstract Collection obterNomePlanos()
        throws Exception;

    public abstract void adicionarNovoRamo(String s)
        throws Exception;

    public abstract Collection obterNomeRamos()
        throws Exception;

    public abstract void verificarPlano(String s)
        throws Exception;

    public abstract boolean validarResolucao(String s)
        throws Exception;
}
