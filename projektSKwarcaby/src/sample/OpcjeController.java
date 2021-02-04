package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class OpcjeController implements Initializable {

    public Setup setup = Setup.getInstance();

    @FXML private TextArea adresIP;
    @FXML private TextArea numerPortu;  //pobranie danych zadeklarowanych w opcjach
    @FXML
    private void okPushed(ActionEvent actionEvent) throws IOException{
        setup.setAdresIP(adresIP.getText());
        setup.setNumerPortu(Integer.parseInt(numerPortu.getText()));
        Parent menuParent = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene menuScene = new Scene(menuParent);
        Stage menuWindow = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        menuWindow.setScene(menuScene);
        menuWindow.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
class Setup{
    String adresIP;
    int numerPortu;
    int kolor;
    public static final Setup setup = new Setup("localhost", 54000, 1);
    private Setup(String adresIP, int numerPortu, int kolor){
        this.adresIP = adresIP;
        this.numerPortu = numerPortu;
        this.kolor = kolor;
    }
    public static final Setup getInstance(){return setup;}
    public void setAdresIP(String adresIP){this.adresIP = adresIP;}
    public void setNumerPortu(int numerPortu){this.numerPortu = numerPortu;}
    public void setKolor(int kolor){this.kolor = kolor;}
}
