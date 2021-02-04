package sample;

import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

import java.awt.event.MouseEvent;

public class Plansza {

    public int tura = 1;
    public String move = "";
    public int kolor = 1;

    public Pole[][] plansza = new Pole[Main.rozmiar_planszy][Main.rozmiar_planszy];

    public Group grupaPola = new Group();
    public Group grupaPionki = new Group();

    Pane okno_planszy = new Pane();

    public Parent init(){  //inicjalizacja planszy -> dodanie do Pane grup pionkow oraz pol

        okno_planszy.setPrefSize(Main.rozmiar_okna, Main.rozmiar_okna);
        okno_planszy.getChildren().addAll(grupaPola, grupaPionki);

        for (int y = 0; y < Main.rozmiar_planszy; y++){
            for (int x = 0; x < Main.rozmiar_planszy; x++){
                Pole pole = new Pole(((x + y) % 2 == 0), x, y);
                plansza[x][y] = pole;
                grupaPola.getChildren().add(pole);

                Pionek pionek;

                //wyjsciowe rozmieszczenie pionkow
                if(y < 3 && (x+y)%2 == 1){
                    pionek = dodajPionek(TypPionka.czarny, x, y);
                }
                else if (y > 4 && (x+y)%2 == 1){
                    pionek = dodajPionek(TypPionka.bialy, x, y);
                }
                else{
                    pionek = null;
                }
                if(pionek != null){
                    pole.setPionek(pionek);
                    grupaPionki.getChildren().add(pionek);
                }
            }
        }

        return okno_planszy;
    }

    public boolean LegalnyRuch(Pionek pionek){  //sprawdzenie czy po wykonaniu bicia jest jeszcze jakies legalne bicia w celu zmaksymalizowania bicia
        int x = planszaKoord(pionek.getOldX());
        int y = planszaKoord(pionek.getOldY());
        if(pionek.getType() == TypPionka.bialy){
            if(x + 2 < 8 && y + 2 < 8 && plansza[x+1][y+1].poleZajete() && !plansza[x+2][y+2].poleZajete() && (plansza[x+1][y+1].getPionek().getType() == TypPionka.czarny || plansza[x+1][y+1].getPionek().getType() == TypPionka.czarny_hetman)){
                return true;
            }
            else if(x + 2 < 8 && y - 2 >= 0 && plansza[x+1][y-1].poleZajete() && !plansza[x+2][y-2].poleZajete() && (plansza[x+1][y-1].getPionek().getType() == TypPionka.czarny || plansza[x+1][y-1].getPionek().getType() == TypPionka.czarny_hetman)){
                return true;
            }
            else if(x - 2 >= 0 && y - 2 >= 0 && plansza[x-1][y-1].poleZajete() && !plansza[x-2][y-2].poleZajete() && (plansza[x-1][y-1].getPionek().getType() == TypPionka.czarny || plansza[x-1][y-1].getPionek().getType() == TypPionka.czarny_hetman)){
                return true;
            }
            else if(x - 2 >= 0 && y + 2 < 8 && plansza[x-1][y+1].poleZajete() && !plansza[x-2][y+2].poleZajete() && (plansza[x-1][y+1].getPionek().getType() == TypPionka.czarny || plansza[x-1][y+1].getPionek().getType() == TypPionka.czarny_hetman)){
                return true;
            }
            else return false;
        }
        else if(pionek.getType() == TypPionka.czarny){
            if(x + 2 < 8 && y + 2 < 8 && plansza[x+1][y+1].poleZajete() && !plansza[x+2][y+2].poleZajete() && (plansza[x+1][y+1].getPionek().getType() == TypPionka.bialy || plansza[x+1][y+1].getPionek().getType() == TypPionka.bialy_hetman)){
                return true;
            }
            else if(x + 2 < 8 && y - 2 >= 0 && plansza[x+1][y-1].poleZajete() && !plansza[x+2][y-2].poleZajete() && (plansza[x+1][y-1].getPionek().getType() == TypPionka.bialy || plansza[x+1][y-1].getPionek().getType() == TypPionka.bialy_hetman)){
                return true;
            }
            else if(x - 2 >= 0 && y - 2 >= 0 && plansza[x-1][y-1].poleZajete() && !plansza[x-2][y-2].poleZajete() && (plansza[x-1][y-1].getPionek().getType() == TypPionka.bialy || plansza[x-1][y-1].getPionek().getType() == TypPionka.bialy_hetman)){
                return true;
            }
            else if(x - 2 >= 0 && y + 2 < 8 && plansza[x-1][y+1].poleZajete() && !plansza[x-2][y+2].poleZajete() && (plansza[x-1][y+1].getPionek().getType() == TypPionka.bialy || plansza[x-1][y+1].getPionek().getType() == TypPionka.bialy_hetman)){
                return true;
            }
            else return false;
        }
        else if (pionek.getType() == TypPionka.czarny_hetman){
            if(6 - x > 0 && 6 - y > 0){
                int tresh = Math.min(7-x, 7-y);
                for(int i=1; i<tresh; i++){
                    boolean abort = false;
                    if (plansza[x+i][y+i].poleZajete() && (plansza[x+i][y+i].getPionek().getType() == TypPionka.bialy || plansza[x+i][y+i].getPionek().getType() == TypPionka.bialy_hetman) && !plansza[x+i+1][y+i+1].poleZajete()){
                        if(i == 1) {return true;}
                        else{
                            for(int j=1; j<i; j++){
                                if(plansza[x+j][y+j].poleZajete()){
                                    abort = true;
                                    break;
                                }
                            }
                            if(abort){break;}
                            else return true;
                        }
                    }
                }
            }
            if(6 - x > 0 && y - 2 >= 0){
                int tresh = Math.min(7-x, y);
                for(int i=1; i<tresh; i++){
                    boolean abort = false;
                    if (plansza[x+i][y-i].poleZajete() && (plansza[x+i][y-i].getPionek().getType() == TypPionka.bialy || plansza[x+i][y-i].getPionek().getType() == TypPionka.bialy_hetman) && !plansza[x+i+1][y-i-1].poleZajete()){
                        if(i == 1) {return true;}
                        else{
                            for(int j=1; j<i; j++){
                                if(plansza[x+j][y-j].poleZajete()){
                                    abort = true;
                                    break;
                                }
                            }
                            if(abort){break;}
                            else return true;
                        }
                    }
                }
            }
            if(6 - y > 0 && x - 2 >= 0){
                int tresh = Math.min(7-y, x);
                for(int i=1; i<tresh; i++){
                    boolean abort = false;
                    if (plansza[x-1][y+1].poleZajete() && (plansza[x-i][y+i].getPionek().getType() == TypPionka.bialy || plansza[x-i][y+i].getPionek().getType() == TypPionka.bialy_hetman) && !plansza[x-i-1][y+i+1].poleZajete()){
                        if(i == 1) {return true;}
                        else{
                            for(int j=1; j<i; j++){
                                if(plansza[x-j][y+j].poleZajete()){
                                    abort = true;
                                    break;
                                }
                            }
                            if(abort){break;}
                            else return true;
                        }
                    }
                }
            }
            if(x - 2 >= 0 && y - 2 >= 0){
                int tresh = Math.min(x, y);
                for(int i=1; i<tresh; i++){
                    boolean abort = false;
                    if (plansza[x-i][y-1].poleZajete() && (plansza[x-i][y-i].getPionek().getType() == TypPionka.bialy || plansza[x-i][y-i].getPionek().getType() == TypPionka.bialy_hetman) && !plansza[x-i-1][y-i-1].poleZajete()){
                        if(i == 1) {return true;}
                        else{
                            for(int j=1; j<i; j++){
                                if(plansza[x-j][y-j].poleZajete()){
                                    abort = true;
                                    break;
                                }
                            }
                            if(abort){break;}
                            else return true;
                        }
                    }
                }
            }
            return false;
        }
        else if (pionek.getType() == TypPionka.bialy_hetman){
            if(6 - x > 0 && 6 - y > 0){
                int tresh = Math.min(7-x, 7-y);
                for(int i=1; i<tresh; i++){
                    boolean abort = false;
                    if (plansza[x+i][y+i].poleZajete() && (plansza[x+i][y+i].getPionek().getType() == TypPionka.czarny || plansza[x+i][y+i].getPionek().getType() == TypPionka.czarny_hetman) && !plansza[x+i+1][y+i+1].poleZajete()){
                        if(i == 1) {return true;}
                        else{
                            for(int j=1; j<i; j++){
                                if(plansza[x+j][y+j].poleZajete()){
                                    abort = true;
                                    break;
                                }
                            }
                            if(abort){break;}
                            else return true;
                        }
                    }
                }
            }
            if(6 - x > 0 && y - 2 >= 0){
                int tresh = Math.min(7-x, y);
                for(int i=1; i<tresh; i++){
                    boolean abort = false;
                    if (plansza[x+i][y-i].poleZajete() && (plansza[x+i][y-i].getPionek().getType() == TypPionka.czarny || plansza[x+i][y-i].getPionek().getType() == TypPionka.czarny_hetman) && !plansza[x+i+1][y-i-1].poleZajete()){
                        if(i == 1) {return true;}
                        else{
                            for(int j=1; j<i; j++){
                                if(plansza[x+j][y-j].poleZajete()){
                                    abort = true;
                                    break;
                                }
                            }
                            if(abort){break;}
                            else return true;
                        }
                    }
                }
            }
            if(6 - y > 0 && x - 2 >= 0){
                int tresh = Math.min(7-y, x);
                for(int i=1; i<tresh; i++){
                    boolean abort = false;
                    if (plansza[x-i][y+i].poleZajete() && (plansza[x-i][y+i].getPionek().getType() == TypPionka.czarny || plansza[x-i][y+i].getPionek().getType() == TypPionka.czarny_hetman) && !plansza[x-i-1][y+i+1].poleZajete()){
                        if(i == 1) {return true;}
                        else{
                            for(int j=1; j<i; j++){
                                if(plansza[x-j][y+j].poleZajete()){
                                    abort = true;
                                    break;
                                }
                            }
                            if(abort){break;}
                            else return true;
                        }
                    }
                }
            }
            if(x - 2 >= 0 && y - 2 >= 0){
                int tresh = Math.min(x, y);
                for(int i=1; i<tresh; i++){
                    boolean abort = false;
                    if (plansza[x-i][y-i].poleZajete() && (plansza[x-i][y-i].getPionek().getType() == TypPionka.czarny || plansza[x-i][y-i].getPionek().getType() == TypPionka.czarny_hetman) && !plansza[x-i-1][y-i-1].poleZajete()){
                        if(i == 1) {return true;}
                        else{
                            for(int j=1; j<i; j++){
                                if(plansza[x-j][y-j].poleZajete()){
                                    abort = true;
                                    break;
                                }
                            }
                            if(abort){break;}
                            else return true;
                        }
                    }
                }
            }
            return false;
        }
        else return false;
    }

    public WynikRuchu tryMove(Pionek pionek, int newX, int newY){  //sprawdzenie jaki logiczny ruch zostal wykonany

        int x0 = planszaKoord(pionek.getOldX());
        int y0 = planszaKoord(pionek.getOldY());

        if(tura % 2 != kolor % 2){
            return new WynikRuchu(TypRuchu.brak);
        }

        if(kolor == 1 && (pionek.getType() == TypPionka.czarny || pionek.getType() == TypPionka.czarny_hetman)){
            return new WynikRuchu(TypRuchu.brak);
        }

        if(kolor == 2 && (pionek.getType() == TypPionka.bialy || pionek.getType() == TypPionka.bialy_hetman)){
            return new WynikRuchu(TypRuchu.brak);
        }

        if(plansza[newX][newY].poleZajete() || (newX + newY)%2 == 0){
            return new WynikRuchu(TypRuchu.brak);
        }
        if(kolor != tura){
            return new WynikRuchu(TypRuchu.brak);
        }
        else if(pionek.getType() == TypPionka.czarny){
            if(x0 - newX == -2 && y0 - newY == -2 && plansza[x0+1][y0+1].poleZajete() && (plansza[x0+1][y0+1].getPionek().getType() == TypPionka.bialy || plansza[x0+1][y0+1].getPionek().getType() == TypPionka.bialy_hetman) && newY != 7){
                return new WynikRuchu(TypRuchu.normalne_zbicie, plansza[x0+1][y0+1].getPionek());
            }
            else if(x0 - newX == -2 && y0 - newY == 2 && plansza[x0+1][y0-1].poleZajete() && (plansza[x0+1][y0-1].getPionek().getType() == TypPionka.bialy || plansza[x0+1][y0-1].getPionek().getType() == TypPionka.bialy_hetman)){
                return new WynikRuchu(TypRuchu.normalne_zbicie, plansza[x0+1][y0-1].getPionek());
            }
            else if(x0 - newX == 2 && y0 - newY == 2 && plansza[x0-1][y0-1].poleZajete() && (plansza[x0-1][y0-1].getPionek().getType() == TypPionka.bialy || plansza[x0+1][y0-1].getPionek().getType() == TypPionka.bialy_hetman)){
                return new WynikRuchu(TypRuchu.normalne_zbicie, plansza[x0-1][y0-1].getPionek());
            }
            else if(x0 - newX == 2 && y0 - newY == -2 && plansza[x0-1][y0+1].poleZajete() && (plansza[x0-1][y0+1].getPionek().getType() == TypPionka.bialy || plansza[x0-1][y0+1].getPionek().getType() == TypPionka.bialy_hetman) && newY != 7){
                return new WynikRuchu(TypRuchu.normalne_zbicie, plansza[x0-1][y0+1].getPionek());
            }
            else if(x0 - newX == 2 && y0 - newY == -2 && plansza[x0-1][y0+1].poleZajete() && (plansza[x0-1][y0+1].getPionek().getType() == TypPionka.bialy || plansza[x0-1][y0+1].getPionek().getType() == TypPionka.bialy_hetman) && newY == 7){
                return new WynikRuchu(TypRuchu.zbicie_promocja, plansza[x0-1][y0+1].getPionek());
            }
            else if(x0 - newX == -2 && y0 - newY == -2 && plansza[x0+1][y0+1].poleZajete() && (plansza[x0+1][y0+1].getPionek().getType() == TypPionka.bialy || plansza[x0+1][y0+1].getPionek().getType() == TypPionka.bialy_hetman) && newY == 7){
                return new WynikRuchu(TypRuchu.zbicie_promocja, plansza[x0+1][y0+1].getPionek());
            }
            else if(y0 - newY == 1){
                return new WynikRuchu(TypRuchu.brak);
            }
            else if(y0 - newY == -1 && Math.abs(x0 - newX) == 1 && newY != 7){
                return new WynikRuchu(TypRuchu.ruch);
            }
            else if(y0 - newY == -1 && Math.abs(x0 - newX) == 1 && newY == 7){
                return new WynikRuchu(TypRuchu.ruch_promocja);
            }
            else return new WynikRuchu(TypRuchu.brak);
        }
        else if(pionek.getType() == TypPionka.bialy){
            if(x0 - newX == -2 && y0 - newY == 2 && plansza[x0+1][y0-1].poleZajete() && (plansza[x0+1][y0-1].getPionek().getType() == TypPionka.czarny || plansza[x0+1][y0-1].getPionek().getType() == TypPionka.czarny_hetman) && newY != 0){
                return new WynikRuchu(TypRuchu.normalne_zbicie, plansza[x0+1][y0-1].getPionek());
            }
            else if(x0 - newX == -2 && y0 - newY == -2 && plansza[x0+1][y0+1].poleZajete() && (plansza[x0+1][y0+1].getPionek().getType() == TypPionka.czarny || plansza[x0+1][y0+1].getPionek().getType() == TypPionka.czarny_hetman)){
                return new WynikRuchu(TypRuchu.normalne_zbicie, plansza[x0+1][y0+1].getPionek());
            }
            else if(x0 - newX == 2 && y0 - newY == -2 && plansza[x0-1][y0+1].poleZajete() && (plansza[x0-1][y0+1].getPionek().getType() == TypPionka.czarny || plansza[x0+1][y0+1].getPionek().getType() == TypPionka.czarny_hetman)){
                return new WynikRuchu(TypRuchu.normalne_zbicie, plansza[x0-1][y0+1].getPionek());
            }
            else if(x0 - newX == 2 && y0 - newY == 2 && plansza[x0-1][y0-1].poleZajete() && (plansza[x0-1][y0-1].getPionek().getType() == TypPionka.czarny || plansza[x0-1][y0-1].getPionek().getType() == TypPionka.czarny_hetman) && newY != 0){
                return new WynikRuchu(TypRuchu.normalne_zbicie, plansza[x0-1][y0-1].getPionek());
            }
            else if(x0 - newX == 2 && y0 - newY == 2 && plansza[x0-1][y0-1].poleZajete() && (plansza[x0-1][y0-1].getPionek().getType() == TypPionka.czarny || plansza[x0-1][y0-1].getPionek().getType() == TypPionka.czarny_hetman) && newY == 0){
                return new WynikRuchu(TypRuchu.zbicie_promocja, plansza[x0-1][y0-1].getPionek());
            }
            else if(x0 - newX == -2 && y0 - newY == 2 && plansza[x0+1][y0-1].poleZajete() && (plansza[x0+1][y0-1].getPionek().getType() == TypPionka.czarny || plansza[x0+1][y0-1].getPionek().getType() == TypPionka.czarny_hetman) && newY == 0){
                return new WynikRuchu(TypRuchu.zbicie_promocja, plansza[x0+1][y0-1].getPionek());
            }
            else if(y0 - newY == -1){
                return new WynikRuchu(TypRuchu.brak);
            }
            else if(y0 - newY == 1 && Math.abs(x0 - newX) == 1 && newY != 0){
                return new WynikRuchu(TypRuchu.ruch);
            }
            else if(y0 - newY == 1 && Math.abs(x0 - newX) == 1 && newY == 0){
                return new WynikRuchu(TypRuchu.ruch_promocja);
            }
            else return new WynikRuchu(TypRuchu.brak);
        }
        else if(pionek.getType() == TypPionka.bialy_hetman){
            if(Math.abs(x0 - newX) == 1 && Math.abs(y0 - newY) == 1){
                return new WynikRuchu(TypRuchu.ruch);
            }
            if(Math.abs(y0 - newY) == Math.abs(x0 - newX)){
                int offsetX = (newX > x0) ? -1 : 1;
                int offsetY = (newY > y0) ? -1 : 1;
                if(plansza[newX + offsetX][newY + offsetY].poleZajete() && (plansza[newX + offsetX][newY + offsetY].getPionek().getType() == TypPionka.czarny_hetman || plansza[newX + offsetX][newY + offsetY].getPionek().getType() == TypPionka.czarny)){
                    for(int i=1; i<Math.abs(x0 - newX)-1; i++){
                        int currentX = x0 - i * offsetX;
                        int currentY = y0 - i * offsetY;
                        if(plansza[currentX][currentY].poleZajete() == true){
                            return new WynikRuchu(TypRuchu.brak);
                        }
                    }
                    return new WynikRuchu(TypRuchu.normalne_zbicie, plansza[newX + offsetX][newY + offsetY].getPionek());
                }
                else if(plansza[newX + offsetX][newY + offsetY].poleZajete() && (plansza[newX + offsetX][newY + offsetY].getPionek().getType() == TypPionka.bialy_hetman || plansza[newX + offsetX][newY + offsetY].getPionek().getType() == TypPionka.bialy)){
                    return new WynikRuchu(TypRuchu.brak);
                }
                else return new WynikRuchu(TypRuchu.ruch);
            }
            else return new WynikRuchu(TypRuchu.brak);
        }
        else if(pionek.getType() == TypPionka.czarny_hetman){
            if(Math.abs(x0 - newX) == 1 && Math.abs(y0 - newY) == 1){
                return new WynikRuchu(TypRuchu.ruch);
            }
            if(Math.abs(y0 - newY) == Math.abs(x0 - newX)){
                int offsetX = (newX > x0) ? -1 : 1;
                int offsetY = (newY > y0) ? -1 : 1;
                if(plansza[newX + offsetX][newY + offsetY].poleZajete() && (plansza[newX + offsetX][newY + offsetY].getPionek().getType() == TypPionka.bialy_hetman || plansza[newX + offsetX][newY + offsetY].getPionek().getType() == TypPionka.bialy)){
                    for(int i=1; i<Math.abs(x0 - newX)-1; i++){
                        int currentX = x0 - i * offsetX;
                        int currentY = y0 - i * offsetY;
                        if(plansza[currentX][currentY].poleZajete() == true){
                            return new WynikRuchu(TypRuchu.brak);
                        }
                    }
                    return new WynikRuchu(TypRuchu.normalne_zbicie, plansza[newX + offsetX][newY + offsetY].getPionek());
                }
                else if(plansza[newX + offsetX][newY + offsetY].poleZajete() && (plansza[newX + offsetX][newY + offsetY].getPionek().getType() == TypPionka.czarny_hetman || plansza[newX + offsetX][newY + offsetY].getPionek().getType() == TypPionka.czarny)){
                    return new WynikRuchu(TypRuchu.brak);
                }
                else return new WynikRuchu(TypRuchu.ruch);
            }
            else return new WynikRuchu(TypRuchu.brak);
        }
        else return new WynikRuchu(TypRuchu.brak);
    }

    public int planszaKoord(double pixel){  //zamiana koordynat okna (w pikselach) na koordynaty planszy (0-7)
        return (int)(pixel + Main.rozmiar_pola / 2) / Main.rozmiar_pola;
    }

    public Pionek dodajPionek(TypPionka typ, int x, int y){  //dodanie do planszy pionka

        Pionek pionek = new Pionek(typ, x, y);

        pionek.setOnMouseReleased(new MovePawnEventHandler(this, pionek));  //obsluga zdarzenia puszczenia myszy

        return pionek;
    }
}
