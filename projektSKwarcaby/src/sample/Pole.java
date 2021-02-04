package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Pole extends Rectangle {  //graficzna reprezentacja pola

    private Pionek pionek;

    public boolean poleZajete(){
        if(pionek != null) {return true;}
        else return false;
    }
    public Pionek getPionek(){
        return pionek;
    }
    public void setPionek(Pionek pionek){
        this.pionek = pionek;
    }

    public Pole(boolean kolor_pola, int x, int y){
        setWidth(Main.rozmiar_pola);
        setHeight(Main.rozmiar_pola);

        relocate(x * Main.rozmiar_pola, y * Main.rozmiar_pola);
        if(kolor_pola == true){
            setFill(Color.valueOf("#F0D9B5"));  //nadanie kolorow pol
        }
        else{
            setFill(Color.valueOf("#B58863"));
        }
    }
}
