// FrontEnd Plus GUI for JAD
// DeCompiled : AuxiliarSeguro.class

package com.gio.crm.model;

import java.util.Collection;

// Referenced classes of package com.gio.crm.model:
//            Entidade, Aseguradora

public interface AuxiliarSeguro
    extends Entidade
{
    public static interface Ramo
    {

        public abstract AuxiliarSeguro obterAuxiliarSeguro()
            throws Exception;

        public abstract int obterSeq()
            throws Exception;

        public abstract String obterRamo()
            throws Exception;
    }


    public abstract void atualizarAseguradora(Aseguradora aseguradora)
        throws Exception;

    public abstract Aseguradora obterAseguradora()
        throws Exception;

    public abstract Collection obterAseguradoras()
        throws Exception;

    public abstract void adicionarNovoRamo(String s)
        throws Exception;

    public abstract Collection obterNomeRamos()
        throws Exception;

    public abstract Ramo obterRamo(int i)
        throws Exception;

    public abstract Collection obterRamos()
        throws Exception;

    public abstract void excluirRamo(Ramo ramo)
        throws Exception;
}
