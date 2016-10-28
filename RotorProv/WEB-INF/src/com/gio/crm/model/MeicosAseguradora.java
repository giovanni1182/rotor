// FrontEnd Plus GUI for JAD
// DeCompiled : MeicosAseguradora.class

package com.gio.crm.model;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

// Referenced classes of package com.gio.crm.model:
//            Evento

public interface MeicosAseguradora
    extends Evento
{
    public static interface Indicador
    {

        public abstract void atualizar(String s)
            throws Exception;

        public abstract MeicosAseguradora obterMeicosAseguradora()
            throws Exception;

        public abstract int obterSequencial()
            throws Exception;

        public abstract String obterDescricao()
            throws Exception;

        public abstract int obterPeso()
            throws Exception;

        public abstract boolean estaMarcado()
            throws Exception;

        public abstract boolean eExcludente()
            throws Exception;
    }

    public static interface ControleDocumento
    {

        public abstract void atualizar(Date date)
            throws Exception;

        public abstract MeicosAseguradora obterMeicosAseguradora()
            throws Exception;

        public abstract int obterSequencial()
            throws Exception;

        public abstract String obterDescricao()
            throws Exception;

        public abstract Date obterDataEntrega()
            throws Exception;

        public abstract Date obterDataLimite()
            throws Exception;
    }


    public abstract Map obterIndicadores()
        throws Exception;

    public abstract Indicador obterIndicador(int i)
        throws Exception;

    public abstract void adicionarIndicador(String s, int i, boolean flag)
        throws Exception;

    public abstract Map obterDocumentos()
        throws Exception;

    public abstract ControleDocumento obterDocumento(int i)
        throws Exception;

    public abstract void adicionarDocumento(String s, Date date)
        throws Exception;

    public abstract Collection obterMeicosCalculos()
        throws Exception;
}
