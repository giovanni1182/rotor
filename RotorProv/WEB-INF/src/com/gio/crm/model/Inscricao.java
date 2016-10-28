// FrontEnd Plus GUI for JAD
// DeCompiled : Inscricao.class

package com.gio.crm.model;

import java.util.Collection;
import java.util.Date;

// Referenced classes of package com.gio.crm.model:
//            Evento, Aseguradora, Entidade, Apolice

public interface Inscricao
    extends Evento
{
    public static interface Ramo
    {

        public abstract Inscricao obterEvento()
            throws Exception;

        public abstract int obterSeq()
            throws Exception;

        public abstract String obterRamo()
            throws Exception;
    }


    public static final String SUPERINTENDENTE = "superintendente";
    public static final String APROVADA = "aprovado";
    public static final String REJEITADA = "rejeitado";
    public static final String SUSPENSA = "suspenso";
    public static final String CANCELADA = "cancelado";

    public abstract void atualizarInscricao(String s)
        throws Exception;

    public abstract void atualizarNumeroResolucao(String s)
        throws Exception;

    public abstract void atualizarDataResolucao(Date date)
        throws Exception;

    public abstract void atualizarDataValidade(Date date)
        throws Exception;

    public abstract void atualizarSituacao(String s)
        throws Exception;

    public abstract void atualizarDataEmissao(Date date)
        throws Exception;

    public abstract void atualizarDataVencimento(Date date)
        throws Exception;

    public abstract void atualizarAseguradora(Aseguradora aseguradora)
        throws Exception;

    public abstract void atualizarAgente(Entidade entidade)
        throws Exception;

    public abstract void atualizarApolice(Apolice apolice)
        throws Exception;

    public abstract void atualizarNumeroApolice(String s)
        throws Exception;

    public abstract void atualizarNumeroSecao(String s)
        throws Exception;

    public abstract void incluir()
        throws Exception;

    public abstract boolean verificarInscricao(String s)
        throws Exception;

    public abstract boolean validarResolucao(String s)
        throws Exception;

    public abstract Collection obterDocumentosVinculados()
        throws Exception;

    public abstract String obterInscricao()
        throws Exception;

    public abstract String obterNumeroResolucao()
        throws Exception;

    public abstract Date obterDataResolucao()
        throws Exception;

    public abstract Date obterDataValidade()
        throws Exception;

    public abstract String obterSituacao()
        throws Exception;

    public abstract Aseguradora obterAseguradora()
        throws Exception;

    public abstract Date obterDataVencimento()
        throws Exception;

    public abstract Date obterDataEmissao()
        throws Exception;

    public abstract Entidade obterAgente()
        throws Exception;

    public abstract Apolice obterApolice()
        throws Exception;

    public abstract String obterNumeroApolice()
        throws Exception;

    public abstract String obterNumeroSecao()
        throws Exception;

    public abstract void atualizarRamo(String s)
        throws Exception;

    public abstract void adicionarNovoRamo(String s)
        throws Exception;

    public abstract String obterRamo()
        throws Exception;

    public abstract Collection obterNomeRamos()
        throws Exception;

    public abstract Collection obterRamos()
        throws Exception;

    public abstract Ramo obterRamo(int i)
        throws Exception;

    public abstract void excluirRamo(Ramo ramo)
        throws Exception;
}
