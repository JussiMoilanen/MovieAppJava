/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viikko9;


public class Theatre {
    
    private String nimi;
    private String ID;
    
    
    public Theatre(){
        
    }
    
    public Theatre(String a, String b){
        ID = a;
        nimi = b;
    }

    public String getName(){
        return nimi;
    }
    public String getID(){
        return ID;
    }
    
    @Override
    public String toString(){
        return nimi;
    }
}
