// FrontEnd Plus GUI for JAD
// DeCompiled : Evento.class

package com.gio.crm.model;

import java.util.Collection;
import java.util.Date;

// Referenced classes of package com.gio.crm.model:
//            Entidade, Usuario

public interface Evento
{
    public static interface Comentario
    {

        public abstract String obterComentario();

        public abstract Date obterCriacao();

        public abstract String obterTitulo();
    }

    public static interface Fase
    {

        public abstract boolean equals(Object obj);

        public abstract String obterCodigo();

        public abstract Collection obterFasesAnteriores()
            throws Exception;

        public abstract Date obterInicio();

        public abstract String obterNome()
            throws Exception;

        public abstract Collection obterProximasFases()
            throws Exception;

        public abstract Date obterTermino();
    }


    public static final String EVENTO_CONCLUIDO = "concluido";
    public static final String EVENTO_PENDENTE = "pendente";
    public static final int PRIORIDADE_ALTA = 1;
    public static final int PRIORIDADE_BAIXA = 3;
    public static final int PRIORIDADE_NORMAL = 2;

    public abstract void adicionarComentario(String s, String s1)
        throws Exception;

    public abstract void atribuirDataPrevistaConclusao(Date date)
        throws Exception;

    public abstract void atribuirDataPrevistaInicio(Date date)
        throws Exception;

    public abstract void atribuirDescricao(String s)
        throws Exception;

    public abstract void atribuirDestino(Entidade entidade)
        throws Exception;

    public abstract void atribuirDuracao(Long long1)
        throws Exception;

    public abstract void atribuirId(long l)
        throws Exception;

    public abstract void atribuirOrigem(Entidade entidade)
        throws Exception;

    public abstract void atribuirPrioridade(int i)
        throws Exception;

    public abstract void atribuirResponsavel(Entidade entidade)
        throws Exception;

    public abstract void atribuirSuperior(Evento evento)
        throws Exception;

    public abstract void atribuirTipo(String s)
        throws Exception;

    public abstract void atribuirTitulo(String s)
        throws Exception;

    public abstract void atualizarComoLido()
        throws Exception;

    public abstract void atualizarComoNaoLido()
        throws Exception;

    public abstract void atualizarDataPrevistaConclusao(Date date)
        throws Exception;

    public abstract void atualizarDataPrevistaInicio(Date date)
        throws Exception;

    public abstract void atualizarDescricao(String s)
        throws Exception;

    public abstract void atualizarDestino(Entidade entidade)
        throws Exception;

    public abstract void atualizarDuracao(Long long1)
        throws Exception;

    public abstract void atualizarFase(String s)
        throws Exception;

    public abstract void atualizarOrigem(Entidade entidade)
        throws Exception;

    public abstract void atualizarPrioridade(int i)
        throws Exception;

    public abstract void atualizarResponsavel(Entidade entidade)
        throws Exception;

    public abstract void atualizarSuperior(Evento evento)
        throws Exception;

    public abstract void atualizarTipo(String s)
        throws Exception;

    public abstract void atualizarTitulo(String s)
        throws Exception;
    
    public abstract void atualizarDataPrevistaConclusao2(Date date)
    throws Exception;

public abstract void atualizarDataPrevistaInicio2(Date date)
    throws Exception;

public abstract void atualizarDescricao2(String s)
    throws Exception;

public abstract void atualizarFase2(String s)
    throws Exception;

public abstract void atualizarOrigem2(Entidade entidade)
    throws Exception;

public abstract void atualizarPrioridade2(int i)
    throws Exception;

public abstract void atualizarResponsavel2(Entidade entidade)
    throws Exception;

public abstract void atualizarSuperior2(Evento evento)
    throws Exception;

public abstract void atualizarTipo2(String s)
    throws Exception;

public abstract void atualizarTitulo2(String s)
    throws Exception;

    public abstract void calcularPrevisoes()
        throws Exception;

    public abstract void concluir(String s)
        throws Exception;

    public abstract void encaminhar(Entidade entidade, String s)
        throws Exception;

    public abstract void excluir()
        throws Exception;
    
    void excluir2()throws Exception;

    public abstract boolean foiLido()
        throws Exception;

    public abstract void incluir()
        throws Exception;

    public abstract Date obterAtualizacao()
        throws Exception;

    public abstract String obterClasse()
        throws Exception;

    public abstract String obterClasseDescricao()
        throws Exception;

    public abstract Collection obterComentarios()
        throws Exception;

    public abstract Date obterCriacao()
        throws Exception;

    public abstract Usuario obterCriador()
        throws Exception;

    public abstract Date obterDataPrevistaConclusao()
        throws Exception;

    public abstract Date obterDataPrevistaInicio()
        throws Exception;

    public abstract String obterDescricao()
        throws Exception;

    public abstract Entidade obterDestino()
        throws Exception;

    public abstract long obterDuracao()
        throws Exception;

    public abstract Fase obterFase()
        throws Exception;

    public abstract Fase obterFaseAnterior()
        throws Exception;

    public abstract String obterIcone()
        throws Exception;

    public abstract long obterId();

    public abstract Collection<Evento> obterInferiores()
        throws Exception;

    public abstract Entidade obterOrigem()
        throws Exception;

    public abstract int obterPrioridade()
        throws Exception;

    public abstract int obterQuantidadeComentarios()
        throws Exception;

    public abstract Entidade obterResponsavel()
        throws Exception;

    public abstract Entidade obterResponsavelAnterior()
        throws Exception;

    public abstract Evento obterSuperior()
        throws Exception;

    public abstract Collection obterSuperiores()
        throws Exception;

    public abstract String obterTipo()
        throws Exception;

    public abstract String obterTitulo()
        throws Exception;

    public abstract boolean permiteAdicionarComentario()
        throws Exception;

    public abstract boolean permiteAtualizar()
        throws Exception;

    public abstract boolean permiteConcluir()
        throws Exception;

    public abstract boolean permiteDevolver()
        throws Exception;

    public abstract boolean permiteEncaminhar()
        throws Exception;

    public abstract boolean permiteExcluir()
        throws Exception;

    public abstract boolean permiteIncluirEventoInferior()
        throws Exception;

    public abstract boolean permiteResponder()
        throws Exception;

    public abstract void responder(String s)
        throws Exception;

    public abstract void ordenar()
        throws Exception;

    public abstract void ordenarParaCima(long l)
        throws Exception;

    public abstract void ordenarParaBaixo(long l)
        throws Exception;

    public abstract void atualizarOrdem(long l)
        throws Exception;

    public abstract long obterOrdem()
        throws Exception;

    public abstract void atribuirClassesParaOrdenar(Collection collection)
        throws Exception;
}
