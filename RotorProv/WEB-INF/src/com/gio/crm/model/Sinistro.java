// FrontEnd Plus GUI for JAD
// DeCompiled : Sinistro.class

package com.gio.crm.model;

import java.util.Date;

// Referenced classes of package com.gio.crm.model:
//            Evento, AuxiliarSeguro, Aseguradora, Apolice, 
//            ClassificacaoContas

public interface Sinistro
    extends Evento
{

    public abstract void atribuirNumero(String s)
        throws Exception;

    public abstract void atribuirDataSinistro(Date date)
        throws Exception;

    public abstract void atribuirDataDenuncia(Date date)
        throws Exception;

    public abstract void atribuirAgente(AuxiliarSeguro auxiliarseguro)
        throws Exception;

    public abstract void atribuirMontanteGs(double d)
        throws Exception;

    public abstract void atribuirTipoMoedaMontanteGs(String s)
        throws Exception;

    public abstract void atribuirMontanteMe(double d)
        throws Exception;

    public abstract void atribuirSituacao(String s)
        throws Exception;

    public abstract void atribuirDataPagamento(Date date)
        throws Exception;

    public abstract void atribuirDataRecuperacao(Date date)
        throws Exception;

    public abstract void atribuirValorRecuperacao(double d)
        throws Exception;

    public abstract void atribuirValorRecuperacaoTerceiro(double d)
        throws Exception;

    public abstract void atribuirParticipacao(double d)
        throws Exception;

    public abstract void atribuirTipoInstrumento(String s)
        throws Exception;

    public abstract void atribuirNumeroEndoso(double d)
        throws Exception;

    public abstract void atribuirCertificado(double d)
        throws Exception;

    public abstract void atualizarNumero(String s)
        throws Exception;

    public abstract void atualizarDataSinistro(Date date)
        throws Exception;

    public abstract void atualizarDataDenuncia(Date date)
        throws Exception;

    public abstract void atualizarAgente(AuxiliarSeguro auxiliarseguro)
        throws Exception;

    public abstract void atualizarMontanteGs(double d)
        throws Exception;

    public abstract void atualizarTipoMoedaMontanteGs(String s)
        throws Exception;

    public abstract void atualizarMontanteMe(double d)
        throws Exception;

    public abstract void atualizarSituacao(String s)
        throws Exception;

    public abstract void atualizarDataPagamento(Date date)
        throws Exception;

    public abstract void atualizarDataRecuperacao(Date date)
        throws Exception;

    public abstract void atualizarValorRecuperacao(double d)
        throws Exception;

    public abstract void atualizarValorRecuperacaoTerceiro(double d)
        throws Exception;

    public abstract void atualizarParticipacao(double d)
        throws Exception;

    public abstract void incluir()
        throws Exception;

    public abstract String obterNumero()
        throws Exception;

    public abstract Date obterDataSinistro()
        throws Exception;

    public abstract Date obterDataDenuncia()
        throws Exception;

    public abstract AuxiliarSeguro obterAuxiliar()
        throws Exception;

    public abstract double obterMontanteGs()
        throws Exception;

    public abstract String obterTipoMoedaMontanteGs()
        throws Exception;

    public abstract double obterMontanteMe()
        throws Exception;

    public abstract String obterSituacao()
        throws Exception;

    public abstract Date obterDataPagamento()
        throws Exception;

    public abstract Date obterDataRecuperacao()
        throws Exception;

    public abstract double obterValorRecuperacao()
        throws Exception;

    public abstract double obterValorRecuperacaoTerceiro()
        throws Exception;

    public abstract double obterParticipacao()
        throws Exception;

    public abstract String obterTipoInstrumento()
        throws Exception;

    public abstract double obterNumeroEndoso()
        throws Exception;

    public abstract double obterCertificado()
        throws Exception;

    public abstract void verificarDuplicidade(Entidade aseguradora, Apolice apolice, ClassificacaoContas classificacaocontas, String s, double d, double d1, Date date)
        throws Exception;
    
    void atribuirCodificacaoPlano(CodificacaoPlano plano) throws Exception;
    void atribuirCodificacaoCobertura(CodificacaoCobertura cobertura) throws Exception;
    void atribuirCodificacaoRisco(CodificacaoRisco risco) throws Exception;
    void atribuirCodificacaoDetalhe(CodificacaoDetalhe detalhe) throws Exception;
    
}
