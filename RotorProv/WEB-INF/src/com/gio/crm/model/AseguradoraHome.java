// FrontEnd Plus GUI for JAD
// DeCompiled : AseguradoraHome.class

package com.gio.crm.model;

import java.util.Collection;

public interface AseguradoraHome
{

    public abstract Collection obterAseguradoras()
        throws Exception;
    
    String obterMesAnoGEE() throws Exception;
    Collection obterAseguradorasPorMenor80OrdenadoPorNome()throws Exception;
    
    
   // Entidade obterAseguradoraPorSigla(String sigla) throws Exception ;
    
    
}
