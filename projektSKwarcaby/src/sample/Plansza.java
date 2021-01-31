package sample;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

public class Plansza {

    private Pole[][] plansza = new Pole[Main.rozmiar_planszy][Main.rozmiar_planszy];

    private Group grupaPola = new Group();
    private Group grupaPionki = new Group();

    public Parent init(){
        Pane okno_planszy = new Pane();
        okno_planszy.setPrefSize(Main.rozmiar_okna, Main.rozmiar_okna);
        okno_planszy.getChildren().addAll(grupaPola, grupaPionki);

        for (int y = 0; y < Main.rozmiar_planszy; y++){
            for (int x = 0; x < Main.rozmiar_planszy; x++){
                Pole pole = new Pole(((x + y) % 2 == 0), x, y);
                plansza[x][y] = pole;
                grupaPola.getChildren().add(pole);

                Pionek pionek;

                if(y == 3 && (x+y)%2 == 1){
                    pionek = dodajPionek(TypPionka.czarny, x, y);
                }
                else if (y == 4 && (x+y)%2 == 1){
                    pionek = dodajPionek(TypPionka.bialy_hetman, x, y);
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

    private boolean LegalnyRuch(Pionek pionek){
        int x = planszaKoord(pionek.getOldX());
        int y = planszaKoord(pionek.getOldY());
        if(pionek.getType() == TypPionka.bialy){
            if(x + 2 < 8 && y + 2 < 8 && (plansza[x+1][y+1].getPionek().getType() == TypPionka.czarny || plansza[x+1][y+1].getPionek().getType() == TypPionka.czarny_hetman)){
                return true;
            }
            else if(x + 2 < 8 && y - 2 >= 0 && (plansza[x+1][y-1].getPionek().getType() == TypPionka.czarny || plansza[x+1][y-1].getPionek().getType() == TypPionka.czarny_hetman)){
                return true;
            }
            else if(x - 2 >= 0 && y - 2 >= 0 && (plansza[x-1][y-1].getPionek().getType() == TypPionka.czarny || plansza[x-1][y-1].getPionek().getType() == TypPionka.czarny_hetman)){
                return true;
            }
            else if(x - 2 >= 0 && y + 2 < 8 && (plansza[x-1][y+1].getPionek().getType() == TypPionka.czarny || plansza[x-1][y+1].getPionek().getType() == TypPionka.czarny_hetman)){
                return true;
            }
            else return false;
        }
        else if(pionek.getType() == TypPionka.czarny){
            if(x + 2 < 8 && y + 2 < 8 && (plansza[x+1][y+1].getPionek().getType() == TypPionka.bialy || plansza[x+1][y+1].getPionek().getType() == TypPionka.bialy_hetman)){
                return true;
            }
            else if(x + 2 < 8 && y - 2 >= 0 && (plansza[x+1][y-1].getPionek().getType() == TypPionka.bialy || plansza[x+1][y-1].getPionek().getType() == TypPionka.bialy_hetman)){
                return true;
            }
            else if(x - 2 >= 0 && y - 2 >= 0 && (plansza[x-1][y-1].getPionek().getType() == TypPionka.bialy || plansza[x-1][y-1].getPionek().getType() == TypPionka.bialy_hetman)){
                return true;
            }
            else if(x - 2 >= 0 && y + 2 < 8 && (plansza[x-1][y+1].getPionek().getType() == TypPionka.bialy || plansza[x-1][y+1].getPionek().getType() == TypPionka.bialy_hetman)){
                return true;
            }
            else return false;
        }
        else if (pionek.getType() == TypPionka.czarny_hetman){
            if(6 - x > 0 && 6 - y > 0){
                int tresh = Math.min(8-x, 8-y);
                for(int i=1; i<tresh; i++){
                    if ((plansza[x+i][y+i].getPionek().getType() == TypPionka.bialy || plansza[x+i][y+i].getPionek().getType() == TypPionka.bialy_hetman) && plansza[x+i+1][y+i+1].getPionek() == null){
                        if(i == 1) {return true;}
                        else{
                            for(int j=1; j<i; j++){
                                if(plansza[x+j][y+j].getPionek() != null){
                                    break;
                                }
                                return true;
                            }
                        }
                        break;
                    }
                }
            }
            if(6 - x > 0 && y - 2 >= 0){
                int tresh = Math.min(8-x, y+1);
                for(int i=1; i<tresh; i++){
                    if ((plansza[x+i][y-i].getPionek().getType() == TypPionka.bialy || plansza[x+i][y-i].getPionek().getType() == TypPionka.bialy_hetman) && plansza[x+i+1][y-i+1].getPionek() == null){
                        if(i == 1) {return true;}
                        else{
                            for(int j=1; j<i; j++){
                                if(plansza[x+j][y-j].getPionek() != null){
                                    break;
                                }
                                return true;
                            }
                        }
                        break;
                    }
                }
            }
            if(6 - y > 0 && x - 2 >= 0){
                int tresh = Math.min(8-y, x+1);
                for(int i=1; i<tresh; i++){
                    if ((plansza[x-i][y+i].getPionek().getType() == TypPionka.bialy || plansza[x-i][y+i].getPionek().getType() == TypPionka.bialy_hetman) && plansza[x-i+1][y+i+1].getPionek() == null){
                        if(i == 1) {return true;}
                        else{
                            for(int j=1; j<i; j++){
                                if(plansza[x-j][y+j].getPionek() != null){
                                    break;
                                }
                                return true;
                            }
                        }
                        break;
                    }
                }
            }
            if(x - 2 >= 0 && y - 2 >= 0){
                int tresh = Math.min(x+1, y+1);
                for(int i=1; i<tresh; i++){
                    if ((plansza[x-i][y-i].getPionek().getType() == TypPionka.bialy || plansza[x-i][y-i].getPionek().getType() == TypPionka.bialy_hetman) && plansza[x-i+1][y-i+1].getPionek() == null){
                        if(i == 1) {return true;}
                        else{
                            for(int j=1; j<i; j++){
                                if(plansza[x-j][y-j].getPionek() != null){
                                    break;
                                }
                                return true;
                            }
                        }
                        break;
                    }
                }
            }
            return false;
        }
        else if (pionek.getType() == TypPionka.bialy_hetman){
            if(6 - x > 0 && 6 - y > 0){
                int tresh = Math.min(8-x, 8-y);
                for(int i=1; i<tresh; i++){
                    if ((plansza[x+i][y+i].getPionek().getType() == TypPionka.czarny || plansza[x+i][y+i].getPionek().getType() == TypPionka.czarny_hetman) && plansza[x+i+1][y+i+1].getPionek() == null){
                        if(i == 1) {return true;}
                        else{
                            for(int j=1; j<i; j++){
                                if(plansza[x+j][y+j].getPionek() != null){
                                    break;
                                }
                                return true;
                            }
                        }
                        break;
                    }
                }
            }
            if(6 - x > 0 && y - 2 >= 0){
                int tresh = Math.min(8-x, y+1);
                for(int i=1; i<tresh; i++){
                    if ((plansza[x+i][y-i].getPionek().getType() == TypPionka.czarny || plansza[x+i][y-i].getPionek().getType() == TypPionka.czarny_hetman) && plansza[x+i+1][y-i+1].getPionek() == null){
                        if(i == 1) {return true;}
                        else{
                            for(int j=1; j<i; j++){
                                if(plansza[x+j][y-j].getPionek() != null){
                                    break;
                                }
                                return true;
                            }
                        }
                        break;
                    }
                }
            }
            if(6 - y > 0 && x - 2 >= 0){
                int tresh = Math.min(8-y, x+1);
                for(int i=1; i<tresh; i++){
                    if ((plansza[x-i][y+i].getPionek().getType() == TypPionka.czarny || plansza[x-i][y+i].getPionek().getType() == TypPionka.czarny_hetman) && plansza[x-i+1][y+i+1].getPionek() == null){
                        if(i == 1) {return true;}
                        else{
                            for(int j=1; j<i; j++){
                                if(plansza[x-j][y+j].getPionek() != null){
                                    break;
                                }
                                return true;
                            }
                        }
                        break;
                    }
                }
            }
            if(x - 2 >= 0 && y - 2 >= 0){
                int tresh = Math.min(x+1, y+1);
                for(int i=1; i<tresh; i++){
                    if ((plansza[x-i][y-i].getPionek().getType() == TypPionka.czarny || plansza[x-i][y-i].getPionek().getType() == TypPionka.czarny_hetman) && plansza[x-i+1][y-i+1].getPionek() == null){
                        if(i == 1) {return true;}
                        else{
                            for(int j=1; j<i; j++){
                                if(plansza[x-j][y-j].getPionek() != null){
                                    break;
                                }
                                return true;
                            }
                        }
                        break;
                    }
                }
            }
            return false;
        }
        else return false;
    }

    private WynikRuchu tryMove(Pionek pionek, int newX, int newY){

        int x0 = planszaKoord(pionek.getOldX());
        int y0 = planszaKoord(pionek.getOldY());

        if(plansza[newX][newY].poleZajete() || (newX + newY)%2 == 0){
            return new WynikRuchu(TypRuchu.brak);
        }
        else if(pionek.getType() == TypPionka.czarny){
            if(x0 - newX == -2 && y0 - newY == -2 && (plansza[x0+1][y0+1].getPionek().getType() == TypPionka.bialy || plansza[x0+1][y0+1].getPionek().getType() == TypPionka.bialy_hetman) && newY != 7){
                return new WynikRuchu(TypRuchu.normalne_zbicie, plansza[x0+1][y0+1].getPionek());
            }
            else if(x0 - newX == -2 && y0 - newY == 2 && (plansza[x0+1][y0-1].getPionek().getType() == TypPionka.bialy || plansza[x0+1][y0-1].getPionek().getType() == TypPionka.bialy_hetman)){
                return new WynikRuchu(TypRuchu.normalne_zbicie, plansza[x0+1][y0-1].getPionek());
            }
            else if(x0 - newX == 2 && y0 - newY == 2 && (plansza[x0-1][y0-1].getPionek().getType() == TypPionka.bialy || plansza[x0+1][y0-1].getPionek().getType() == TypPionka.bialy_hetman)){
                return new WynikRuchu(TypRuchu.normalne_zbicie, plansza[x0-1][y0-1].getPionek());
            }
            else if(x0 - newX == 2 && y0 - newY == -2 && (plansza[x0-1][y0+1].getPionek().getType() == TypPionka.bialy || plansza[x0-1][y0+1].getPionek().getType() == TypPionka.bialy_hetman) && newY != 7){
                return new WynikRuchu(TypRuchu.normalne_zbicie, plansza[x0-1][y0+1].getPionek());
            }
            else if(x0 - newX == 2 && y0 - newY == -2 && (plansza[x0-1][y0+1].getPionek().getType() == TypPionka.bialy || plansza[x0-1][y0+1].getPionek().getType() == TypPionka.bialy_hetman) && newY == 7){
                return new WynikRuchu(TypRuchu.zbicie_promocja, plansza[x0-1][y0+1].getPionek());
            }
            else if(x0 - newX == -2 && y0 - newY == -2 && (plansza[x0+1][y0+1].getPionek().getType() == TypPionka.bialy || plansza[x0+1][y0+1].getPionek().getType() == TypPionka.bialy_hetman) && newY == 7){
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
            if(x0 - newX == -2 && y0 - newY == 2 && (plansza[x0+1][y0-1].getPionek().getType() == TypPionka.czarny || plansza[x0+1][y0-1].getPionek().getType() == TypPionka.czarny_hetman) && newY != 0){
                return new WynikRuchu(TypRuchu.normalne_zbicie, plansza[x0+1][y0-1].getPionek());
            }
            else if(x0 - newX == -2 && y0 - newY == -2 && (plansza[x0+1][y0+1].getPionek().getType() == TypPionka.czarny || plansza[x0+1][y0+1].getPionek().getType() == TypPionka.czarny_hetman)){
                return new WynikRuchu(TypRuchu.normalne_zbicie, plansza[x0+1][y0+1].getPionek());
            }
            else if(x0 - newX == 2 && y0 - newY == -2 && (plansza[x0-1][y0+1].getPionek().getType() == TypPionka.czarny || plansza[x0+1][y0+1].getPionek().getType() == TypPionka.czarny_hetman)){
                return new WynikRuchu(TypRuchu.normalne_zbicie, plansza[x0-1][y0+1].getPionek());
            }
            else if(x0 - newX == 2 && y0 - newY == 2 && (plansza[x0-1][y0-1].getPionek().getType() == TypPionka.czarny || plansza[x0-1][y0-1].getPionek().getType() == TypPionka.czarny_hetman) && newY != 0){
                return new WynikRuchu(TypRuchu.normalne_zbicie, plansza[x0-1][y0-1].getPionek());
            }
            else if(x0 - newX == 2 && y0 - newY == 2 && (plansza[x0-1][y0-1].getPionek().getType() == TypPionka.czarny || plansza[x0-1][y0-1].getPionek().getType() == TypPionka.czarny_hetman) && newY == 0){
                return new WynikRuchu(TypRuchu.zbicie_promocja, plansza[x0-1][y0-1].getPionek());
            }
            else if(x0 - newX == -2 && y0 - newY == 2 && (plansza[x0+1][y0-1].getPionek().getType() == TypPionka.czarny || plansza[x0+1][y0-1].getPionek().getType() == TypPionka.czarny_hetman) && newY == 0){
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
            if(Math.abs(y0 - newY) == Math.abs(x0 - newX)){
                int offsetX = (newX > x0) ? -1 : 1;
                int offsetY = (newY > y0) ? -1 : 1;
                if(plansza[newX + offsetX][newY + offsetY].getPionek().getType() == TypPionka.czarny_hetman || plansza[newX + offsetX][newY + offsetY].getPionek().getType() == TypPionka.czarny){
                    for(int i=0; i<Math.abs(x0 - newX)-1; i++){
                        int currentX = x0 - i * offsetX;
                        int currentY = x0 - i * offsetY;
                        if(plansza[currentX][currentY].poleZajete() == true){
                            return new WynikRuchu(TypRuchu.brak);
                        }
                    }
                    return new WynikRuchu(TypRuchu.normalne_zbicie);
                }
                else if(plansza[newX + offsetX][newY + offsetY].getPionek().getType() == TypPionka.bialy_hetman || plansza[newX + offsetX][newY + offsetY].getPionek().getType() == TypPionka.bialy){
                    return new WynikRuchu(TypRuchu.brak);
                }
                else return new WynikRuchu(TypRuchu.ruch);
            }
            else return new WynikRuchu(TypRuchu.brak);
        }
        else if(pionek.getType() == TypPionka.czarny_hetman){
            if(Math.abs(y0 - newY) == Math.abs(x0 - newX)){
                int offsetX = (newX > x0) ? -1 : 1;
                int offsetY = (newY > y0) ? -1 : 1;
                if(plansza[newX + offsetX][newY + offsetY].getPionek().getType() == TypPionka.bialy_hetman || plansza[newX + offsetX][newY + offsetY].getPionek().getType() == TypPionka.bialy){
                    for(int i=0; i<Math.abs(x0 - newX)-1; i++){
                        int currentX = x0 - i * offsetX;
                        int currentY = x0 - i * offsetY;
                        if(plansza[currentX][currentY].poleZajete() == true){
                            return new WynikRuchu(TypRuchu.brak);
                        }
                    }
                    return new WynikRuchu(TypRuchu.normalne_zbicie);
                }
                else if(plansza[newX + offsetX][newY + offsetY].getPionek().getType() == TypPionka.czarny_hetman || plansza[newX + offsetX][newY + offsetY].getPionek().getType() == TypPionka.czarny){
                    return new WynikRuchu(TypRuchu.brak);
                }
                else return new WynikRuchu(TypRuchu.ruch);
            }
            else return new WynikRuchu(TypRuchu.brak);
        }
        else return new WynikRuchu(TypRuchu.brak);
    }

    private int planszaKoord(double pixel){
        return (int)(pixel + Main.rozmiar_pola / 2) / Main.rozmiar_pola;
    }

    private Pionek dodajPionek(TypPionka typ, int x, int y){

        Pionek pionek = new Pionek(typ, x, y);

        pionek.setOnMouseReleased(mouseEvent -> {
            int newX = planszaKoord(pionek.getLayoutX());
            int newY = planszaKoord(pionek.getLayoutY());

            WynikRuchu wynik = tryMove(pionek, newX, newY);

            int x0 = planszaKoord(pionek.getOldX());
            int y0 = planszaKoord(pionek.getOldY());

            switch (wynik.getTyp()){
                case brak:
                    pionek.cofnijRuch();
                    break;
                case ruch:
                    pionek.ruch(newX, newY);
                    plansza[x0][y0].setPionek(null);
                    plansza[newX][newY].setPionek(pionek);
                case normalne_zbicie:
                    pionek.ruch(newX, newY);
                    plansza[x0][y0].setPionek(null);
                    plansza[newX][newY].setPionek(pionek);
                    Pionek zbijanyPionek = wynik.getPionek();
                    plansza[planszaKoord(zbijanyPionek.getOldX())][planszaKoord((zbijanyPionek.getOldY()))].setPionek(null);
                    grupaPionki.getChildren().remove(zbijanyPionek);
                    break;
                case ruch_promocja:
                    /*if(pionek.getType() == TypPionka.bialy){
                        pionek.setTyp(TypPionka.bialy_hetman);
                    }
                    else if(pionek.getType() == TypPionka.czarny){
                        pionek.setTyp(TypPionka.czarny_hetman);
                    }*/
                    pionek.ruch(newX, newY);
                    plansza[x0][y0].setPionek(null);
                    grupaPionki.getChildren().remove(pionek);
                    //plansza[newX][newY].setPionek(pionek);
                    if(pionek.getType() == TypPionka.bialy){
                        Pionek damka = new Pionek(TypPionka.bialy_hetman, newX, newY);
                        plansza[newX][newY].setPionek(damka);
                        grupaPionki.getChildren().add(damka);
                    }
                    else if(pionek.getType() == TypPionka.czarny){
                        Pionek damka = new Pionek(TypPionka.czarny_hetman, newX, newY);
                        plansza[newX][newY].setPionek(damka);
                        grupaPionki.getChildren().add(damka);
                    }
                    break;
                case zbicie_promocja:
                    if(pionek.getType() == TypPionka.bialy){
                        pionek.setTyp(TypPionka.bialy_hetman);
                    }
                    else if(pionek.getType() == TypPionka.czarny){
                        pionek.setTyp(TypPionka.czarny_hetman);
                    }
                    pionek.ruch(newX, newY);
                    plansza[x0][y0].setPionek(null);
                    plansza[newX][newY].setPionek(pionek);
                    zbijanyPionek = wynik.getPionek();
                    plansza[planszaKoord(zbijanyPionek.getOldX())][planszaKoord((zbijanyPionek.getOldY()))].setPionek(null);
                    grupaPionki.getChildren().remove(zbijanyPionek);
                    break;
            }
        });

        return pionek;
    }
}
