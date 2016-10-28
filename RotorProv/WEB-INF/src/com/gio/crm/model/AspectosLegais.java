// FrontEnd Plus GUI for JAD
// DeCompiled : AspectosLegais.class

package com.gio.crm.model;

import java.util.Date;

// Referenced classes of package com.gio.crm.model:
//            Evento, Aseguradora, Apolice, ClassificacaoContas

public interface AspectosLegais
    extends Evento
{

    public abstract void atribuirNumeroOrdem(String s)
        throws Exception;

    public abstract void atribuirDataNotificacao(Date date)
        throws Exception;

    public abstract void atribuirAssunto(String s)
        throws Exception;

    public abstract void atribuirDemandante(String s)
        throws Exception;

    public abstract void atribuirDemandado(String s)
        throws Exception;

    public abstract void atribuirJulgado(String s)
        throws Exception;

    public abstract void atribuirTurno(String s)
        throws Exception;

    public abstract void atribuirJuiz(String s)
        throws Exception;

    public abstract void atribuirSecretaria(String s)
        throws Exception;

    public abstract void atribuirAdvogado(String s)
        throws Exception;

    public abstract void atribuirCircunscricao(String s)
        throws Exception;

    public abstract void atribuirForum(String s)
        throws Exception;

    public abstract void atribuirDataDemanda(Date date)
        throws Exception;

    public abstract void atribuirMontanteDemandado(double d)
        throws Exception;

    public abstract void atribuirMontanteSentenca(double d)
        throws Exception;

    public abstract void atribuirDataCancelamento(Date date)
        throws Exception;

    public abstract void atribuirResponsabilidadeMaxima(double d)
        throws Exception;

    public abstract void atribuirSinistroPendente(double d)
        throws Exception;

    public abstract void atribuirObjetoCausa(String s)
        throws Exception;

    public abstract void atribuirTipoInstrumento(String s)
        throws Exception;

    public abstract void atribuirNumeroEndoso(double d)
        throws Exception;

    public abstract void atribuirCertificado(double d)
        throws Exception;

    public abstract void atualizarNumeroOrdem(String s)
        throws Exception;

    public abstract void atualizarDataNotificacao(Date date)
        throws Exception;

    public abstract void atualizarAssunto(String s)
        throws Exception;

    public abstract void atualizarDemandante(String s)
        throws Exception;

    public abstract void atualizarDemandado(String s)
        throws Exception;

    public abstract void atualizarJulgado(String s)
        throws Exception;

    public abstract void atualizarTurno(String s)
        throws Exception;

    public abstract void atualizarJuiz(String s)
        throws Exception;

    public abstract void atualizarSecretaria(String s)
        throws Exception;

    public abstract void atualizarAdvogado(String s)
        throws Exception;

    public abstract void atualizarCircunscricao(String s)
        throws Exception;

    public abstract void atualizarForum(String s)
        throws Exception;

    public abstract void atualizarDataDemanda(Date date)
        throws Exception;

    public abstract void atualizarMontanteDemandado(double d)
        throws Exception;

    public abstract void atualizarMontanteSentenca(double d)
        throws Exception;

    public abstract void atualizarDataCancelamento(Date date)
        throws Exception;

    public abstract void atualizarResponsabilidadeMaxima(double d)
        throws Exception;

    public abstract void atualizarSinistroPendente(double d)
        throws Exception;

    public abstract void atualizarObjetoCausa(String s)
        throws Exception;

    public abstract void incluir()
        throws Exception;

    public abstract String obterNumeroOrdem()
        throws Exception;

    public abstract Date obterDataNotificacao()
        throws Exception;

    public abstract String obterAssunto()
        throws Exception;

    public abstract String obterDemandante()
        throws Exception;

    public abstract String obterDemandado()
        throws Exception;

    public abstract String obterJulgado()
        throws Exception;

    public abstract String obterTurno()
        throws Exception;

    public abstract String obterJuiz()
        throws Exception;

    public abstract String obterSecretaria()
        throws Exception;

    public abstract String obterAdvogado()
        throws Exception;

    public abstract String obterCircunscricao()
        throws Exception;

    public abstract String obterForum()
        throws Exception;

    public abstract Date obterDataDemanda()
        throws Exception;

    public abstract double obterMontanteDemandado()
        throws Exception;

    public abstract double obterMontanteSentenca()
        throws Exception;

    public abstract Date obterDataCancelamento()
        throws Exception;

    public abstract double obterResponsabilidadeMaxima()
        throws Exception;

    public abstract double obterSinistroPendente()
        throws Exception;

    public abstract String obterObjetoCausa()
        throws Exception;

    public abstract String obterTipoInstrumento()
        throws Exception;

    public abstract double obterNumeroEndoso()
        throws Exception;

    public abstract double obterCertificado()
        throws Exception;

    public abstract void verificarDuplicidade(Aseguradora aseguradora, Apolice apolice, ClassificacaoContas classificacaocontas, String s, double d, double d1)
        throws Exception;
}
