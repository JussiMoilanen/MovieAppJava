/*
 * Jussi Moilanen
 * 8.6.2018
 * and open the template in the editor.
 */
package viikko9;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author n1682
 */
public class Mainclass {

    private List<Theatre> theatre;
    private List<Movie> movies;
    private List<String> moviesbyname;
    private Document doc;
    private ObservableList<String> obtheatre;
    static private Mainclass m;

    private Mainclass() {

        theatre = new ArrayList<Theatre>();
        movies = new ArrayList<Movie>();
        moviesbyname = new ArrayList<String>();
        
        obtheatre = FXCollections.observableArrayList();
        
    }

    public static Mainclass getInstance() {
        if (m == null) {
            m = new Mainclass();
        }
        return m;
    }

    public void mainclass(String content) {

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            doc = dBuilder.parse(new InputSource(new StringReader(content)));
            doc.getDocumentElement().normalize();
            parseContent();

            
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(Mainclass.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void parseContent() {
        NodeList theatrenodes = doc.getElementsByTagName("TheatreArea");

        for (int i = 0; i < theatrenodes.getLength(); i++) {
            Node node = theatrenodes.item(i);
            Element e = (Element) node;
            if (node.getNodeType() == Node.ELEMENT_NODE) {

                Node idlist = e.getElementsByTagName("ID").item(0);
                idlist.getTextContent();
                Node namelist = e.getElementsByTagName("Name").item(0);
                namelist.getTextContent();
                theatre.add(new Theatre(idlist.getTextContent(), namelist.getTextContent()));
            }
        }

    }
    
    public void parseContentlisting(String content1) {

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            doc = dBuilder.parse(new InputSource(new StringReader(content1)));
            doc.getDocumentElement().normalize();
            
            NodeList theatrenodes = doc.getElementsByTagName("Show");
            movies.clear();
            for (int i = 0; i < theatrenodes.getLength(); i++) {

                Node node = theatrenodes.item(i);
                Element e = (Element) node;
            
                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    Node titlelist = e.getElementsByTagName("Title").item(0);
                    titlelist.getTextContent();
                    
                    Node movielist = e.getElementsByTagName("dttmShowStart").item(0);
                    String alkuaika = movielist.getTextContent().substring(11, 16);

                    movies.add(new Movie(alkuaika,titlelist.getTextContent()));
                    
                }
            }
            
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(Mainclass.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

    public String getContentAction() {

        try {
            URL url = new URL("https://www.finnkino.fi/xml/TheatreAreas/");

            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "utf-8"));

            String content = "";
            String rivi;

            while ((rivi = br.readLine()) != null) {

                content += rivi + "\n";

            }
            return content;
        } catch (IOException ex) {
            Logger.getLogger(Mainclass.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "virhe";
    }
    
    public void getMovieByName(String haku){
        
            for(int i = 0; i < movies.size(); i++){
                if (movies.get(i).getMovieName().toLowerCase().trim().contains(haku)){
                    moviesbyname.add(movies.get(i).getAlkamisaika() + " \t" + movies.get(i).getMovieName());
            }
        }
    }
    
    private String getTheatherAndDate(String valinta,String date){

        try {
            
            String path = String.format("http://www.finnkino.fi/xml/Schedule/?area=%s&dt=%s", valinta,date);
            URL url = new URL(path);
            
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "utf-8"));
            
            String rivi;
            String content1 = "";
            
            while ((rivi = br.readLine()) != null){
                content1 += rivi + "\n";
            }

            return content1;
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Mainclass.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "virhe";
    }
    
    public void getMovieByTime(String startingaika, String endingaika){
        
        try {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            if (startingaika.isEmpty()){
                startingaika = "00:00";
                
            }
            if (endingaika.isEmpty()){
                endingaika = "23:59";
            }
            
            Date startingtime = format.parse(startingaika);
            Date endingtime = format.parse(endingaika);
            Date movietime;
            
            
                obtheatre.clear();
                for (int i = 0; i < movies.size(); i++){   
                    movietime = format.parse(movies.get(i).getAlkamisaika());
                 
            if ((movietime.after(startingtime) && movietime.before(endingtime)) || movietime.equals(startingtime) || movietime.equals(endingtime)){
                    obtheatre.add(movies.get(i).getAlkamisaika() + "\t" + movies.get(i).getMovieName());
                    
                    //System.out.println(movies.get(i).getAlkamisaika() + movies.get(i).getMovieName());
                }
            }  
                
                
                    } catch (ParseException ex) {
            Logger.getLogger(Mainclass.class.getName()).log(Level.SEVERE, null, ex);
        }
  
    }

    public List<Theatre> getTheatre(){
        return theatre;
    }
    public ObservableList<String> getMovies(String valinta,String date){
        parseContentlisting(getTheatherAndDate(valinta,date));
        return obtheatre;
    }
    public List<String> getMovieListByName(){
        return moviesbyname;
    }

    
    /*public ObservableList listToOblist(){

       ObservableList<Theatre> obtheatre = FXCollections.observableArrayList(theatre);
       
       return obtheatre;   
    }*/
    

}
