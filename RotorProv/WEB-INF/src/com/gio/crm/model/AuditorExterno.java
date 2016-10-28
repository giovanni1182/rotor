// FrontEnd Plus GUI for JAD
// DeCompiled : AuditorExterno.class

package com.gio.crm.model;

import java.util.Collection;
import java.util.Date;

// Referenced classes of package com.gio.crm.model:
//            Entidade, Aseguradora

public interface AuditorExterno
    extends Entidade
{
    public static interface Cliente
    {

        public abstract void atualizar(Aseguradora aseguradora, double d, Date date, Date date1, String s)
            throws Exception;

        public abstract AuditorExterno obterAuditor()
            throws Exception;

        public abstract int obterId()
            throws Exception;

        public abstract Aseguradora obterAseguradora()
            throws Exception;

        public abstract double obterHonorarios()
            throws Exception;

        public abstract Date obterDataInicio()
            throws Exception;

        public abstract Date obterDataFim()
            throws Exception;

        public abstract String obterOutrosServicos()
            throws Exception;
    }

    public static interface Servico
    {

        public abstract void atualizar(String s, Date date, double d, String s1)
            throws Exception;

        public abstract AuditorExterno obterAuditor()
            throws Exception;

        public abstract Aseguradora obterAseguradora()
            throws Exception;

        public abstract int obterId()
            throws Exception;

        public abstract String obterServico()
            throws Exception;

        public abstract Date obterDataContrato()
            throws Exception;

        public abstract double obterHonorarios()
            throws Exception;

        public abstract String obterPeriodo()
            throws Exception;
    }


    public abstract void adicionarCliente(Aseguradora aseguradora, double d, Date date, Date date1, String s)
        throws Exception;

    public abstract Cliente obterCliente(int i)
        throws Exception;

    public abstract Collection obterClientes()
        throws Exception;

    public abstract void removerCliente(Cliente cliente)
        throws Exception;

    public abstract void adicionarServico(Aseguradora aseguradora, String s, Date date, double d, String s1)
        throws Exception;

    public abstract Servico obterServico(Aseguradora aseguradora, int i)
        throws Exception;

    public abstract Collection obterServicos(Aseguradora aseguradora)
        throws Exception;

    public abstract void removerServico(Servico servico)
        throws Exception;

    public abstract void atualizarAseguradora(Aseguradora aseguradora)
        throws Exception;

    public abstract void atualizarRamo(String s)
        throws Exception;

    public abstract void adicionarNovoRamo(String s)
        throws Exception;

    public abstract Aseguradora obterAseguradora()
        throws Exception;

    public abstract String obterRamo()
        throws Exception;

    public abstract void incluir()
        throws Exception;

    public abstract Collection obterNomeRamos()
        throws Exception;
}
