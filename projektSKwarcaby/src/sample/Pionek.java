package sample;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

public class Pionek extends StackPane {
    private TypPionka typ;
    private double mouseX, mouseY, oldX, oldY;

    public TypPionka getType(){
        return typ;
    }

    public void setTyp(TypPionka typ){
        this.typ = typ;
    }

    public double getOldX() {
        return oldX;
    }

    public double getOldY() {
        return oldY;
    }

    public Pionek(TypPionka typ, int x, int y){  //utworzenie grafifcznej postaci pionka oraz rozmieszczenie go na planszy

        this.typ = typ;

        ruch(x, y);

        Ellipse pion = new Ellipse(Main.rozmiar_pola * 0.3125, Main.rozmiar_pola * 0.26);
        if(typ == TypPionka.czarny || typ == TypPionka.czarny_hetman){
            pion.setFill(Color.valueOf("#272727"));
        }
        else{
            pion.setFill(Color.WHITESMOKE);
        }
        pion.setStroke(Color.BLACK);
        pion.setStrokeWidth(Main.rozmiar_pola * 0.02);
        pion.setTranslateX((Main.rozmiar_pola - Main.rozmiar_pola * 0.3125 * 2)/2);
        pion.setTranslateY((Main.rozmiar_pola - Main.rozmiar_pola * 0.26 * 2)/2 - Main.rozmiar_pola * 0.07);

        Ellipse tlo = new Ellipse(Main.rozmiar_pola * 0.3125, Main.rozmiar_pola * 0.26);
        if(typ == TypPionka.czarny_hetman || typ == TypPionka.bialy_hetman){
            tlo.setFill(Color.valueOf("#FFD700"));
        }
        else{
            tlo.setFill(Color.valueOf("#22B14C"));
        }
        tlo.setStroke(Color.BLACK);
        tlo.setStrokeWidth(Main.rozmiar_pola * 0.02);
        tlo.setTranslateX((Main.rozmiar_pola - Main.rozmiar_pola * 0.3125 * 2)/2);
        tlo.setTranslateY((Main.rozmiar_pola - Main.rozmiar_pola * 0.26 * 2)/2);

        getChildren().addAll(tlo, pion);

        setOnMousePressed(mouseEvent -> {
            mouseX = mouseEvent.getSceneX();
            mouseY = mouseEvent.getSceneY();
        });

        setOnMouseDragged(mouseEvent -> {
            relocate(mouseEvent.getSceneX() - mouseX + oldX, mouseEvent.getSceneY() - mouseY + oldY);
        });
    }
    public void ruch(int x, int y){
        oldX = x * Main.rozmiar_pola;
        oldY = y * Main.rozmiar_pola;
        relocate(oldX, oldY);
    }
    public void cofnijRuch(){
        relocate(oldX, oldY);
    }
}
