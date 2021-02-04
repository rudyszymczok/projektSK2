package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.System.exit;

public class Controller implements Initializable {
    public void nowaGraPushed(ActionEvent event) throws IOException
    {
        //utworzenie okna gry
        Plansza root = new Plansza();
        Pane board = (Pane) root.init();
        Scene nowaGraScene = new Scene(board);
        Stage nowaGraWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
        nowaGraWindow.setTitle("Warcaby");
        nowaGraWindow.setScene(nowaGraScene);
        nowaGraWindow.show();

        //utworzenie watku do przesylania danych do serwera
        Polaczenie polaczenie = new Polaczenie();
        Thread thread = new Thread(polaczenie);
        thread.start();
    }
    public void opcjePushed(ActionEvent actionEvent) throws IOException{
        //przejscie do widoku opcji
        Parent opcje = FXMLLoader.load(getClass().getResource("opcje.fxml"));
        Scene opcjeScene = new Scene(opcje);
        Stage opcjeWindow = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        opcjeWindow.setScene(opcjeScene);
        opcjeWindow.show();
    }
    public void wyjsciePushed()
    {
        exit(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

}
