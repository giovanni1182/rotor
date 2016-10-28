// FrontEnd Plus GUI for JAD
// DeCompiled : Util.class

package com.gio.crm.model;


public class Util
{

    public Util()
    {
    }

    public static String translateException(Exception exception)
    {
        String message = exception.getMessage();
        if(message == null)
            message = "Erro Interno: " + exception.getClass().getName();
        return message;
    }
}
