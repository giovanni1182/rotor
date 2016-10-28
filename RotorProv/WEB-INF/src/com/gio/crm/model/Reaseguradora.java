// FrontEnd Plus GUI for JAD
// DeCompiled : Reaseguradora.class

package com.gio.crm.model;

import java.util.Collection;
import java.util.Date;

// Referenced classes of package com.gio.crm.model:
//            Entidade

public interface Reaseguradora
    extends Entidade
{
    public static interface Classificacao
    {

        public abstract void atualizar(String s, String s1, String s2, String s3, Date date)
            throws Exception;

        public abstract Reaseguradora obterReaseguradora()
            throws Exception;

        public abstract int obterId()
            throws Exception;

        public abstract String obterClassificacao()
            throws Exception;

        public abstract String obterNivel()
            throws Exception;

        public abstract String obterCodigo()
            throws Exception;

        public abstract String obterQualificacao()
            throws Exception;

        public abstract Date obterData()
            throws Exception;
    }


    public abstract void adicionarClassificacao(String s, String s1, String s2, String s3, Date date)
        throws Exception;

    public abstract void adicionarClassificacaoNivel(String s)
        throws Exception;

    public abstract Classificacao obterClassificacao(int i)
        throws Exception;

    public abstract Collection obterClassificacoes()
        throws Exception;

    public abstract Collection obterNiveis()
        throws Exception;

    public abstract void removerClassificacao(Classificacao classificacao)
        throws Exception;
}
