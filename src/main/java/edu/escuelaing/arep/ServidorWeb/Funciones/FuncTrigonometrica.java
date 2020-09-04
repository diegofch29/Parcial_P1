package edu.escuelaing.arep.ServidorWeb.Funciones;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author diego
 */
public class FuncTrigonometrica {
    
    private final Map<String, String> resp;
    
    public FuncTrigonometrica(){
        resp = new HashMap<String, String>();
    }
    
    public Map<String, String> function(String num, String function){
        System.out.println(function+" "+num);
        if (function.contains("sin")){
            return sen(Integer.valueOf(num));
        }
        else if(function.contains("cos")){
            return cos(Integer.valueOf(num));
        }
        else if(function.contains("tan")){
            return tan(Integer.valueOf(num));
        }
        return null;
    }

    
    private  Map<String, String> sen(double num) {
        resp.clear();
        resp.put("sin",String.valueOf(Math.sin(num)));
        return resp;
    }

    
    private Map<String, String> cos(double num) {
        resp.clear();
        resp.put("cos",String.valueOf(Math.cos(num)));
        return resp;
    }

    
    private Map<String, String> tan(double num) {
        resp.clear();
        resp.put("tan",String.valueOf(Math.tan(num)));
        return resp;
    }
    
}
