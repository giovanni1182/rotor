// FrontEnd Plus GUI for JAD
// DeCompiled : DadosCoaseguro.class

package com.gio.crm.model;


// Referenced classes of package com.gio.crm.model:
//            Evento, Entidade, Aseguradora, Apolice, 
//            ClassificacaoContas

public interface DadosCoaseguro
    extends Evento
{

    public abstract void atualizarAseguradora(Entidade entidade)
        throws Exception;

    public abstract void atualizarCapitalGs(double d)
        throws Exception;

    public abstract void atualizarTipoMoedaCapitalGs(String s)
        throws Exception;

    public abstract void atualizarCapitalMe(double d)
        throws Exception;

    public abstract void atualizarParticipacao(double d)
        throws Exception;

    public abstract void atualizarPrimaGs(double d)
        throws Exception;

    public abstract void atualizarTipoMoedaPrimaGs(String s)
        throws Exception;

    public abstract void atualizarPrimaMe(double d)
        throws Exception;

    public abstract void atualizarGrupo(String s)
        throws Exception;

    public abstract void atribuirAseguradora(Entidade entidade)
        throws Exception;

    public abstract void atribuirCapitalGs(double d)
        throws Exception;

    public abstract void atribuirTipoMoedaCapitalGs(String s)
        throws Exception;

    public abstract void atribuirCapitalMe(double d)
        throws Exception;

    public abstract void atribuirParticipacao(double d)
        throws Exception;

    public abstract void atribuirPrimaGs(double d)
        throws Exception;

    public abstract void atribuirTipoMoedaPrimaGs(String s)
        throws Exception;

    public abstract void atribuirPrimaMe(double d)
        throws Exception;

    public abstract void atribuirGrupo(String s)
        throws Exception;

    public abstract void atribuirTipoInstrumento(String s)
        throws Exception;

    public abstract void atribuirNumeroEndoso(double d)
        throws Exception;

    public abstract void atribuirCertificado(double d)
        throws Exception;

    public abstract void incluir()
        throws Exception;

    public abstract Entidade obterAseguradora()
        throws Exception;

    public abstract double obterCapitalGs()
        throws Exception;

    public abstract String obterTipoMoedaCapitalGs()
        throws Exception;

    public abstract double obterCapitalMe()
        throws Exception;

    public abstract double obterParticipacao()
        throws Exception;

    public abstract double obterPrimaGs()
        throws Exception;

    public abstract String obterTipoMoedaPrimaGs()
        throws Exception;

    public abstract double obterPrimaMe()
        throws Exception;

    public abstract String obterGrupo()
        throws Exception;

    public abstract String obterTipoInstrumento()
        throws Exception;

    public abstract double obterNumeroEndoso()
        throws Exception;

    public abstract double obterCertificado()
        throws Exception;

    public abstract void verificarDuplicidade(Aseguradora aseguradora, Entidade entidade, Apolice apolice, ClassificacaoContas classificacaocontas, double d, double d1)
        throws Exception;
}
