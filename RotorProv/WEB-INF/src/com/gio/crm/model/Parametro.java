// FrontEnd Plus GUI for JAD
// DeCompiled : Parametro.class

package com.gio.crm.model;

import java.util.Collection;
import java.util.Date;

// Referenced classes of package com.gio.crm.model:
//            Entidade

public interface Parametro
    extends Entidade
{
    public static interface Feriado
    {

        public abstract void atualizarValor(String s, Date date)
            throws Exception;

        public abstract String obterDescricaoFeriado()
            throws Exception;

        public abstract Date obterDataFeriado()
            throws Exception;

        public abstract Entidade obterEntidade()
            throws Exception;

        public abstract int obterId()
            throws Exception;
    }

    public static interface Consistencia
    {

        public abstract void atualizarValor(String s, String s1, String s2, String s3, int i)
            throws Exception;

        public abstract Entidade obterEntidade()
            throws Exception;

        public abstract int obterSequencial()
            throws Exception;

        public abstract int obterRegra()
            throws Exception;

        public abstract String obterOperando1()
            throws Exception;

        public abstract String obterOperando2()
            throws Exception;

        public abstract String obterOperador()
            throws Exception;

        public abstract String obterMensagem()
            throws Exception;
    }


    public abstract void adicionarConsistencia(String s, String s1, String s2, String s3, int i)
        throws Exception;

    public abstract void adicionarFeriado(String s, Date date)
        throws Exception;

    public abstract Collection obterFeriados()
        throws Exception;

    public abstract Collection<Consistencia> obterConsistencias()
        throws Exception;

    public abstract Feriado obterFeriado(int i)
        throws Exception;

    public abstract Consistencia obterConsistencia(int i, int j)
        throws Exception;

    public abstract void removerFeriado(Feriado feriado)
        throws Exception;

    public abstract void removerConsistencia(Consistencia consistencia, int i)
        throws Exception;
}
