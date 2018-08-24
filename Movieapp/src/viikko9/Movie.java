/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viikko9;

/**
 *
 * @author n1682
 */
public class Movie {

    private String alkamisaika;
    private String moviename;
    
    public Movie(String a, String b) {
        alkamisaika = a;
        moviename = b;
    }
    
    public String getAlkamisaika(){
        return alkamisaika;
    }
    public String getMovieName(){
        return moviename;
    }
    
}
