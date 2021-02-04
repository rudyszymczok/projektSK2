package sample;

import java.io.*;
import java.net.Socket;

public class Polaczenie extends Plansza implements Runnable{ // indeksy kolejno: 0:x0 1:y0 2:newX 3:newY 4:x_zbitego
    // 5:y_zbitego 6:czy_zmiana_tury 7:czy bicie 8:czy_promocja 9:czy_podanie_koloru

    @Override
    public void run() {
        Socket client_socket;
        {
            try {//utworzenie gniazda sieciowego
                client_socket = new Socket(Setup.setup.adresIP, Setup.setup.numerPortu);
                BufferedReader reader = new BufferedReader(new InputStreamReader(client_socket.getInputStream())); //utworzenie strumieni przesyłu danych
                PrintWriter writer = new PrintWriter(client_socket.getOutputStream(), true);
                client_socket.setTcpNoDelay(true);
                System.out.println("Polaczono z serwerem");

                while(true){
                    String data;
                    while(reader.ready() == false){} //oczekiwanie na dane
                    data = reader.readLine();
                    if(data.charAt(9) == '1'){  //sprawdzenie czy dane dotycza konfiguracji gry (wybor stron)
                        Setup.setup.setKolor(1); // 1 -> biale bierki
                        tura = 1;
                        continue;
                    }
                    else if(data.charAt(9) == '2'){
                        Setup.setup.setKolor(2);   // 2 -> czarne bierki
                        tura = 2;
                        continue;
                    }
                    if (data.charAt(6) == '0') { // sprawdzenie czy nastapila zmiana tury (przeciwnik nie ma juz zadnych kolejnych legalnych zbic)
                        do {

                            int x0, y0, newX, newY, zbityX, zbityY;  // przypisanie koordynat poszczegolnych pionkow
                            x0 = Integer.parseInt(String.valueOf(data.charAt(0)));
                            y0 = Integer.parseInt(String.valueOf(data.charAt(1)));
                            newX = Integer.parseInt(String.valueOf(data.charAt(2)));
                            newY = Integer.parseInt(String.valueOf(data.charAt(3)));
                            zbityX = Integer.parseInt(String.valueOf(data.charAt(4)));
                            zbityY = Integer.parseInt(String.valueOf(data.charAt(5)));

                            Pionek pionek = plansza[x0][y0].getPionek();

                            TypPionka typ = pionek.getType();

                            if (data.charAt(8) == '0') { //brak promocji
                                if (data.charAt(7) == '0') { //brak bicia
                                    plansza[x0][y0].setPionek(null);
                                    plansza[newX][newY].setPionek(pionek);
                                } else { //bicie
                                    Pionek zbity = plansza[zbityX][zbityY].getPionek();
                                    plansza[x0][y0].setPionek(null);
                                    plansza[newX][newY].setPionek(pionek);
                                    plansza[zbityX][zbityY].setPionek(null);
                                    grupaPionki.getChildren().remove(zbity);

                                }
                            } else { //promocja
                                if (data.charAt(7) == '0') { //brak bicia
                                    plansza[x0][y0].setPionek(null);
                                    grupaPionki.getChildren().remove(pionek);
                                    if (typ == TypPionka.bialy) {
                                        Pionek damka = new Pionek(TypPionka.bialy_hetman, newX, newY);
                                        plansza[newX][newY].setPionek(damka);
                                        grupaPionki.getChildren().add(damka);
                                    } else {
                                        Pionek damka = new Pionek(TypPionka.czarny_hetman, newX, newY);
                                        plansza[newX][newY].setPionek(damka);
                                        grupaPionki.getChildren().add(damka);
                                    }
                                } else { //bicie
                                    Pionek zbity = plansza[zbityX][zbityY].getPionek();
                                    plansza[x0][y0].setPionek(null);
                                    plansza[newX][newY].setPionek(pionek);
                                    plansza[zbityX][zbityY].setPionek(null);
                                    grupaPionki.getChildren().remove(zbity);
                                    if (typ == TypPionka.bialy) {
                                        Pionek damka = new Pionek(TypPionka.bialy_hetman, newX, newY);
                                        plansza[newX][newY].setPionek(damka);
                                        grupaPionki.getChildren().add(damka);
                                    } else {
                                        Pionek damka = new Pionek(TypPionka.czarny_hetman, newX, newY);
                                        plansza[newX][newY].setPionek(damka);
                                        grupaPionki.getChildren().add(damka);
                                    }

                                }
                            }
                            while (reader.ready() == false) {}
                            data = reader.readLine();

                        } while (data.charAt(6) == '0');
                    }
                    if(data.charAt(6) == '1'){  //analogicznie w przypadku zmiany tury
                        tura = tura + 1;
                        int x0, y0, newX, newY, zbityX, zbityY;
                        x0 = Integer.parseInt(String.valueOf(data.charAt(0)));
                        y0 = Integer.parseInt(String.valueOf(data.charAt(1)));
                        newX = Integer.parseInt(String.valueOf(data.charAt(2)));
                        newY = Integer.parseInt(String.valueOf(data.charAt(3)));
                        zbityX = Integer.parseInt(String.valueOf(data.charAt(4)));
                        zbityY = Integer.parseInt(String.valueOf(data.charAt(5)));

                        Pionek pionek = plansza[x0][y0].getPionek();

                        TypPionka typ = pionek.getType();

                        if (data.charAt(8) == '0') { //brak promocji
                            if (data.charAt(7) == '0') { //brak bicia
                                plansza[x0][y0].setPionek(null);
                                plansza[newX][newY].setPionek(pionek);
                            } else { //bicie
                                Pionek zbity = plansza[zbityX][zbityY].getPionek();
                                plansza[x0][y0].setPionek(null);
                                plansza[newX][newY].setPionek(pionek);
                                plansza[zbityX][zbityY].setPionek(null);
                                grupaPionki.getChildren().remove(zbity);

                            }
                        } else { //promocja
                            if (data.charAt(7) == '0') { //brak bicia
                                plansza[x0][y0].setPionek(null);
                                grupaPionki.getChildren().remove(pionek);
                                if (typ == TypPionka.bialy) {
                                    Pionek damka = new Pionek(TypPionka.bialy_hetman, newX, newY);
                                    plansza[newX][newY].setPionek(damka);
                                    grupaPionki.getChildren().add(damka);
                                } else {
                                    Pionek damka = new Pionek(TypPionka.czarny_hetman, newX, newY);
                                    plansza[newX][newY].setPionek(damka);
                                    grupaPionki.getChildren().add(damka);
                                }
                            } else { //bicie
                                Pionek zbity = plansza[zbityX][zbityY].getPionek();
                                plansza[x0][y0].setPionek(null);
                                plansza[newX][newY].setPionek(pionek);
                                plansza[zbityX][zbityY].setPionek(null);
                                grupaPionki.getChildren().remove(zbity);
                                if (typ == TypPionka.bialy) {
                                    Pionek damka = new Pionek(TypPionka.bialy_hetman, newX, newY);
                                    plansza[newX][newY].setPionek(damka);
                                    grupaPionki.getChildren().add(damka);
                                } else {
                                    Pionek damka = new Pionek(TypPionka.czarny_hetman, newX, newY);
                                    plansza[newX][newY].setPionek(damka);
                                    grupaPionki.getChildren().add(damka);
                                }
                            }
                        }
                    }
                    while(move.length() < 9){} //oczekiwanie az aplikacja zwroci wykonany ruch
                    writer.println(move);
                    tura = tura + 1;  //inkrementacja licznika tur w celu zapobiegniecia ruszania pionkami w nie swojej turze
                    move = "";
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
