// FrontEnd Plus GUI for JAD
// DeCompiled : Aseguradora.class

package com.gio.crm.model;

import java.util.Collection;
import java.util.Date;

// Referenced classes of package com.gio.crm.model:
//            Entidade, RatioPermanente

public interface Aseguradora
    extends Entidade
{
    public static interface Reaseguradora
    {

    	public abstract void atualizar(Entidade entidade, Entidade entidade1, String s, Date date, int i, String s1)
            throws Exception;

        public abstract Aseguradora obterAseguradora()
            throws Exception;

        public abstract int obterId()
            throws Exception;

        public abstract Entidade obterReaseguradora()
            throws Exception;

        public abstract Entidade obterCorretora()
            throws Exception;

        public abstract String obterTipoContrato()
            throws Exception;

        public abstract Date obterDataVencimento()
            throws Exception;

        public abstract int obterParticipacao()
            throws Exception;

        public abstract String obterObservacao()
            throws Exception;
    }

    public static interface Acionista
    {

        public abstract void atualizar(String s, int i, String s1)
            throws Exception;

        public abstract Aseguradora obterAseguradora()
            throws Exception;

        public abstract int obterId()
            throws Exception;

        public abstract String obterAcionista()
            throws Exception;

        public abstract int obterquantidade()
            throws Exception;

        public abstract String obtertipo()
            throws Exception;
    }

    public static interface Filial
    {

        public abstract void atualizar(String s, String s1, String s2, String s3, String s4, String s5)
            throws Exception;

        public abstract Aseguradora obterAseguradora()
            throws Exception;

        public abstract int obterId()
            throws Exception;

        public abstract String obterFilial()
            throws Exception;

        public abstract String obterTipo()
            throws Exception;

        public abstract String obterTelefone()
            throws Exception;

        public abstract String obterCidade()
            throws Exception;

        public abstract String obterEndereco()
            throws Exception;

        public abstract String obterEmail()
            throws Exception;
    }

    public static interface Fusao
    {

        public abstract void atualizar(String s, Date date)
            throws Exception;

        public abstract Aseguradora obterAseguradora()
            throws Exception;

        public abstract int obterId()
            throws Exception;

        public abstract String obterEmpresa()
            throws Exception;

        public abstract Date obterDatausao()
            throws Exception;
    }

    public static interface Coasegurador
    {

        public abstract void atualizar(String s)
            throws Exception;

        public abstract Aseguradora obterAseguradora()
            throws Exception;

        public abstract int obterId()
            throws Exception;

        public abstract String obterCodigo()
            throws Exception;
    }


    boolean existeAgendaNoPeriodo(int mes, int ano, String tipo) throws Exception;
    
    public abstract Collection obterMeicos(String s)
        throws Exception;

    public abstract void adicionarReaseguradora(Entidade entidade, Entidade entidade1, String s, Date date, int i, String s1)
        throws Exception;

    public abstract Reaseguradora obterReaseguradora(int i)
        throws Exception;

    public abstract Collection obterReaseguradoras()
        throws Exception;

    public abstract void removerReaseguradora(Reaseguradora reaseguradora)
        throws Exception;

    public abstract Collection obterCalculosMeicos()
        throws Exception;

    public abstract Collection obterAgentes()
        throws Exception;

    public abstract void adicionarAcionista(String s, int i, String s1)
        throws Exception;

    public abstract Acionista obterAcionista(int i)
        throws Exception;

    public abstract Collection obterAcionistas()
        throws Exception;

    public abstract void removerAcionista(Acionista acionista)
        throws Exception;

    public abstract void adicionarFilial(String s, String s1, String s2, String s3, String s4, String s5)
        throws Exception;

    public abstract Filial obterFilial(int i)
        throws Exception;

    public abstract Collection obterFiliais()
        throws Exception;

    public abstract void removerFilial(Filial filial)
        throws Exception;

    public abstract void adicionarFusao(String s, Date date)
        throws Exception;

    public abstract Fusao obterFusao(int i)
        throws Exception;

    public abstract Collection obterFusoes()
        throws Exception;

    public abstract void removerFusao(Fusao fusao)
        throws Exception;

    public abstract RatioPermanente obterRatioPermanente()
        throws Exception;

    public abstract Collection obterRatiosPermanentes()
        throws Exception;

    public abstract Collection obterRatiosUmAno()
        throws Exception;

    public abstract Collection obterRatiosTresAnos()
        throws Exception;

    public abstract Collection obterPlanos()
        throws Exception;

    public abstract void adicionarCoasegurador(String s)
        throws Exception;

    public abstract Coasegurador obterCoasegurador(int i)
        throws Exception;

    public abstract Collection obterCoaseguradores()
        throws Exception;

    public abstract void removerCoasegurador(Coasegurador coasegurador)
        throws Exception;

    public abstract Collection obterCoaseguradorasPorGrupo(String s)
        throws Exception;
    
    Collection obterAgendas() throws Exception;
    
    Collection obterAgendasIntrumentoPendentes() throws Exception;
    
    AgendaMovimentacao obterUltimaAgendaMCO() throws Exception;
    AgendaMovimentacao obterUltimaAgendaMCI() throws Exception;
}
