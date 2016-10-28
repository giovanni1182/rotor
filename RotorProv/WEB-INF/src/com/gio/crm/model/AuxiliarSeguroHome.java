// FrontEnd Plus GUI for JAD
// DeCompiled : AuxiliarSeguroHome.class

package com.gio.crm.model;


// Referenced classes of package com.gio.crm.model:
//            AuxiliarSeguro

public interface AuxiliarSeguroHome
{

    public abstract AuxiliarSeguro obterAuxiliarPorInscricao(String s) throws Exception;
    AuxiliarSeguro obterAuxiliarPorInscricao(String inscricao, String tipo) throws Exception;

    public abstract AuxiliarSeguro obterAuxiliarPorInscricaoeTipo(String s, String s1, String mesAno)
        throws Exception;
}
