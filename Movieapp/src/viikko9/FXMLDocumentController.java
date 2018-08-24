/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viikko9;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author n1682
 */
public class FXMLDocumentController implements Initializable {

    Mainclass m = Mainclass.getInstance();

    private Label label;
    @FXML
    private ChoiceBox<Theatre> picktheatre;
    @FXML
    private Font x1;
    @FXML
    private TextField actday;
    @FXML
    private TextField startingtime;
    @FXML
    private TextField endingtime;
    @FXML
    private Button listfilms;
    @FXML
    private Font x2;
    @FXML
    private TextField givemoviename;
    @FXML
    private Button namesearch;

    private TextArea textarea;
    @FXML
    private ListView<String> textarea1;
    @FXML
    private TextField selectedMovie;
    @FXML
    private TextField imdbRating;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        m.mainclass(m.getContentAction());

        List<Theatre> lista = m.getTheatre();
        picktheatre.getItems().addAll(lista);
        //ObservableList<Theatre> oblist = m.listToOblist();
        // picktheatre.setItems(oblist);
        picktheatre.getSelectionModel().selectFirst();

    }

    public String getMovieID() {

        String movieName = selectedMovie.getText();
        String imdburl = String.format("https://www.imdb.com/find?q=%s&s=tt&ttype=ft&ref_=fn_ft", movieName);

        try {
            Document doc = Jsoup.connect(imdburl).get();

            /*for (Element e : doc.select("table.findList tr")) {
                String title = e.select(".result_text").text();
                System.out.println(title);
            }*/
            Elements link = doc.select("table.findList tr");

            if (!link.isEmpty()) {
                String title = link.select(".result_text").get(0).html();
                String[] url = title.split("title/");
                String imdbID = url[1].trim().substring(0, 9);
                getMovieRating(imdbID);
                return imdbID;
            } else {
                System.out.println("Elokuvaa ei löytynyt Imdb:stä.");
                imdbRating.setText("-");
            }

        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    private void getMovieRating(String imdbID) {
        String movieurl = String.format("https://www.imdb.com/title/%s", imdbID);
        Document document;

        try {
            document = Jsoup.connect(movieurl).get();
            Elements e = document.select("div.ratingValue");
            if (!e.isEmpty()) {
                String rating = e.text();
                //System.out.println(rating.substring(0, 3));
                imdbRating.setText(rating.substring(0, 3));
            } else{
                System.out.println("Elokuvalla ei ole vielä arvosanaa.");
                imdbRating.setText("-");
            }

        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void listFilmsAction(ActionEvent event) {
        String valinta = picktheatre.getSelectionModel().getSelectedItem().getID();
        String date = actday.getText();
        String startingaika = startingtime.getText();
        String endingaika = endingtime.getText();

        //ObservableList<String> obmovies = FXCollections.observableArrayList(m.getTheatre());
        //textarea1.setItems(obmovies);
        //m.getTheatherAndDate(valinta, date);
        textarea1.setItems(m.getMovies(valinta, date));
        m.getMovieByTime(startingaika, endingaika);

        //picktheatre.getSelectionModel().selectFirst();
    }

    @FXML
    private void nameSearchAction(ActionEvent event) {

        String haku = givemoviename.getText();
        m.getMovieByName(haku);
        ObservableList<String> obmoviesbyname = FXCollections.observableArrayList(m.getMovieListByName());
        textarea1.setItems(obmoviesbyname);

    }

    @FXML
    private void empty(ActionEvent event) {
        // korjaa että tyhjentää listan -----
        textarea1.getItems().clear();

    }

    @FXML
    private void setMovieName(MouseEvent event) {
        String mname = textarea1.getSelectionModel().getSelectedItems().get(0);

        if (mname.toLowerCase().contains("(2d)") || mname.toLowerCase().contains("(3d)") || mname.toLowerCase().contains("(dub)")) {
            mname = mname.substring(0, mname.length() - 5);
            System.out.println(mname);
        }
        if (mname.toLowerCase().contains("(2d dub)") || mname.toLowerCase().contains("(3d dub)")) {
            mname = mname.substring(0, mname.length() - 8);
            System.out.println(mname);
        }

        if (mname.length() > 0) {
            String testname = mname.substring(6);
            selectedMovie.setText(testname.trim());
            getMovieID();
        }
    }

    @FXML
    private void openImdbSite(ActionEvent event) {
        String imdbID = getMovieID();
        String moviesite = String.format("https://www.imdb.com/title/%s",imdbID);
            try {
                Desktop.getDesktop().browse(new URL(moviesite).toURI());
            } catch (IOException | URISyntaxException e) {
                System.out.println("Cant open site or something");
            }
    }

}
