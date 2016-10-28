// FrontEnd Plus GUI for JAD
// DeCompiled : SampleModelManager.class

package com.gio.crm.model;

import java.util.HashMap;

import infra.model.Home;
import infra.model.ModelManager;

public class SampleModelManager extends ModelManager
{

    private HashMap homes;

    public SampleModelManager()
    {
        homes = new HashMap();
    }

    public String getModelClassName(String classAlias)
    {
    	if(classAlias.toLowerCase().equals("uploadedfilehome") || classAlias.toLowerCase().equals("uploadedfile"))
        	return "com.gio.crm.model."+ classAlias;
    	else if(classAlias.toLowerCase().equals("departamento"))
    		return "com.gio.crm.model.EntidadeImpl";
    	else
    	{
	    	if(classAlias.toLowerCase().equals("empresa"))
	            classAlias = "Entidade";
	        if(classAlias.toLowerCase().equals("parametro"))
	            classAlias = "Parametro";
        	
	        return "com.gio.crm.model." + classAlias + "Impl";
    	}
    }

    public Home getHome(String homeAlias)
        throws Exception
    {
        Home home = (Home)homes.get(homeAlias);
        if(home == null)
        {
            home = super.getHome(homeAlias);
            homes.put(homeAlias, home);
        }
        return home;
    }

    public void beginTransaction()
        throws Exception
    {
        beginTransaction("crm");
    }
}
