// FrontEnd Plus GUI for JAD
// DeCompiled : Renovacao.class

package com.gio.crm.model;


// Referenced classes of package com.gio.crm.model:
//            Evento

public interface Renovacao
    extends Evento
{

    public abstract void atualizarMatriculaAnterior(int i)
        throws Exception;

    public abstract void atualizarCertificadoAntecedentes(int i)
        throws Exception;

    public abstract void atualizarCertificadoJudicial(int i)
        throws Exception;

    public abstract void atualizarCertificadoTributario(int i)
        throws Exception;

    public abstract void atualizarDeclaracao(int i)
        throws Exception;

    public abstract void atualizarComprovanteMatricula(int i)
        throws Exception;

    public abstract void atualizarApoliceSeguro(int i)
        throws Exception;

    public abstract void atualizarLivro(int i)
        throws Exception;

    public abstract void incluir()
        throws Exception;

    public abstract boolean obterMatriculaAnterior()
        throws Exception;

    public abstract boolean obterCertificadoAntecedentes()
        throws Exception;

    public abstract boolean obterCertificadoJudicial()
        throws Exception;

    public abstract boolean obterCertificadoTributario()
        throws Exception;

    public abstract boolean obterDeclaracao()
        throws Exception;

    public abstract boolean obterComprovanteMatricula()
        throws Exception;

    public abstract boolean obterApoliceSeguro()
        throws Exception;

    public abstract boolean obterLivro()
        throws Exception;
}
