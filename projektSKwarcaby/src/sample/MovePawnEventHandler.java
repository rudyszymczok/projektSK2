package sample;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class MovePawnEventHandler implements EventHandler<MouseEvent> {

    Plansza plansza;
    Pionek pionek;
    public MovePawnEventHandler(Plansza plansza, Pionek pionek){
        this.plansza = plansza;
        this.pionek = pionek;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        int newX = plansza.planszaKoord(pionek.getLayoutX());
        int newY = plansza.planszaKoord(pionek.getLayoutY());

        WynikRuchu wynik = plansza.tryMove(pionek, newX, newY);

        int x0 = plansza.planszaKoord(pionek.getOldX());
        int y0 = plansza.planszaKoord(pionek.getOldY());

        int legalMove;

        switch (wynik.getTyp()){  //w zaleznosci od typu ruchu zaktualizowanie stanu rozgrywki na planszy
            case brak:
                pionek.cofnijRuch();
                plansza.move = "";
                break;
            case ruch:
                plansza.move = "";
                pionek.ruch(newX, newY);
                plansza.plansza[x0][y0].setPionek(null);
                plansza.plansza[newX][newY].setPionek(pionek);
                plansza.move = plansza.move + x0 + y0 + newX + newY + 99 + 0 + 0 + 0 + 0;
                System.out.println(plansza.move);
                break;
            case normalne_zbicie:
                plansza.move = "";
                pionek.ruch(newX, newY);
                plansza.plansza[x0][y0].setPionek(null);
                plansza.plansza[newX][newY].setPionek(pionek);
                Pionek zbijanyPionek = wynik.getPionek();
                plansza.plansza[plansza.planszaKoord(zbijanyPionek.getOldX())][plansza.planszaKoord((zbijanyPionek.getOldY()))].setPionek(null);
                plansza.grupaPionki.getChildren().remove(zbijanyPionek);
                legalMove = plansza.LegalnyRuch(pionek) ? 1 : 0;
                plansza.move = plansza.move + x0 + y0 + newX + newY + plansza.planszaKoord(zbijanyPionek.getOldX()) + plansza.planszaKoord(zbijanyPionek.getOldY()) + legalMove + 1 + 0 + 0;
                System.out.println(plansza.move);
                break;
            case ruch_promocja:
                plansza.move = "";
                pionek.ruch(newX, newY);
                plansza.plansza[x0][y0].setPionek(null);
                plansza.grupaPionki.getChildren().remove(pionek);
                if(pionek.getType() == TypPionka.bialy){
                    Pionek damka = new Pionek(TypPionka.bialy_hetman, newX, newY);
                    plansza.plansza[newX][newY].setPionek(damka);
                    damka.ruch(newX, newY);
                    plansza.grupaPionki.getChildren().add(damka);
                    damka.setOnMouseReleased(new MovePawnEventHandler(plansza, damka));
                }
                else if(pionek.getType() == TypPionka.czarny){
                    Pionek damka = new Pionek(TypPionka.czarny_hetman, newX, newY);
                    plansza.plansza[newX][newY].setPionek(damka);
                    damka.ruch(newX, newY);
                    plansza.grupaPionki.getChildren().add(damka);
                    damka.setOnMouseReleased(new MovePawnEventHandler(plansza, damka));
                }
                plansza.move = plansza.move + x0 + y0 + newX + newY + 99 + 0 + 0 + 1 + 0;
                System.out.println(plansza.move);
                break;
            case zbicie_promocja:
                plansza.move = "";
                pionek.ruch(newX, newY);
                plansza.plansza[x0][y0].setPionek(null);
                zbijanyPionek = wynik.getPionek();
                plansza.plansza[plansza.planszaKoord(zbijanyPionek.getOldX())][plansza.planszaKoord((zbijanyPionek.getOldY()))].setPionek(null);
                plansza.grupaPionki.getChildren().remove(zbijanyPionek);
                plansza.grupaPionki.getChildren().remove(pionek);
                if(pionek.getType() == TypPionka.bialy){
                    Pionek damka = new Pionek(TypPionka.bialy_hetman, newX, newY);
                    plansza.plansza[newX][newY].setPionek(damka);
                    damka.ruch(newX, newY);
                    plansza.grupaPionki.getChildren().add(damka);
                    damka.setOnMouseReleased(new MovePawnEventHandler(plansza, damka));
                }
                else if(pionek.getType() == TypPionka.czarny){
                    Pionek damka = new Pionek(TypPionka.czarny_hetman, newX, newY);
                    plansza.plansza[newX][newY].setPionek(damka);
                    damka.ruch(newX, newY);
                    plansza.grupaPionki.getChildren().add(damka);
                    damka.setOnMouseReleased(new MovePawnEventHandler(plansza, damka));
                }
                legalMove = plansza.LegalnyRuch(pionek) ? 1 : 0;
                plansza.move = plansza.move + x0 + y0 + newX + newY + plansza.planszaKoord(zbijanyPionek.getOldX()) + plansza.planszaKoord(zbijanyPionek.getOldY()) + legalMove + 1 + 1 + 0;
                System.out.println(plansza.move);
                break;
        }
    }
}
