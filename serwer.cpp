#include <iostream>
#include <vector>
#include <sys/types.h>
#include <unistd.h>
#include <sys/socket.h>
#include <netdb.h>
#include <arpa/inet.h>
#include <string.h>
#include <string>
#include <thread>

using namespace std;

class Gra{  //klasa przechowujaca stan danej rozgrywki
    private:
        int id_gry;
        int id_graczaB;
        int id_graczaC;
        int tura;
    public:
        int plansza[8][8];  // 0 -> puste pole; 1-> bialy pionek; 2 -> biala damka; -1 -> czarny pion; -2 -> czarna damka

        void setIdGry(int id){
            id_gry = id;
        }
        int getIdGry(){
            return id_gry;
        }
        void setTura(int tur){
            tura = tur;
        }
        int getTura(){
            return tura;
        }
        void setIdGraczaB(int id){
            id_graczaB = id;
        }
        int getIdGraczaB(){
            return id_graczaB;
        }
        void setIdGraczaC(int id){
            id_graczaC = id;
        }
        int getIdGraczaC(){
            return id_graczaC;
        }
        void init(){  //inicjalizacja startowej planszy dla danej rozgrywki
            for(int i=0; i<8; i++)
                for(int j=0; j<8; j++){
                    if(i<3 && (i+j)%2 == 1) plansza[i][j] = -1;
                    else if(i>4 && (i+j)%2 == 1) plansza[i][j] = 1;
                    else plansza[i][j] = 0;
                }
        }
};

void handleConnection(int client_socket, vector <int> deskryptory, vector <Gra> gry){  //funkcja opisujaca zachowanie watku
    bool firstMessage = true;
    int kolor = deskryptory.size() % 2;
    if(firstMessage == true){  //przydzielenie koloru bierek dla uzytkownika (kto pierwszy w danej grze ten bialy)
        if(kolor == 1){
            Gra gra;
            gra.setTura(1);
            gra.setIdGry(gry.size()-1);
            gra.setIdGraczaB(client_socket);
            gra.init();
            gry.push_back(gra);
            char buffer[11] = "0000000001"; //przeslanie informacji o kolorze
            send(client_socket, buffer, strlen(buffer), 0);
            
        }
        else{
            gry[(deskryptory.size()-1)/2].setIdGraczaC(client_socket);
            gry[(deskryptory.size()-1)/2].setTura(gry[(deskryptory.size()-1)/2].getTura() + 1);
            char buffer[11] = "0000000002";   //przeslanie informacji o kolorze
            send(client_socket, buffer, strlen(buffer), 0); 
        }
        firstMessage = false;
    }
    else{
        while(true){
            char buffer[11];
            read(client_socket, buffer, sizeof(buffer));  //odczytanie informacji od klienta
            int x0, y0, newX, newY, zbityX, zbityY, czy_zmiana, czy_bicie, czy_promocja;
            x0 = (int) buffer[0] - 48;
            y0 = (int) buffer[1] - 48;
            newX = (int) buffer[2] - 48;
            newY = (int) buffer[3] - 48;        //interpretacja zakodowanej informacji
            zbityX = (int) buffer[4] - 48;
            zbityY = (int) buffer[5] - 48;
            czy_zmiana = (int) buffer[6] - 48;
            czy_bicie = (int) buffer[7] - 48;
            czy_promocja = (int) buffer[8] - 48;

            int socket_przeciwnika;         
            int id_gry;
            for(int i=0; i < (int)gry.size(); i++){             //zachowanie watku dla roznych przypadkow ruchow
                if(gry[i].getIdGraczaB() == client_socket){
                    socket_przeciwnika = gry[i].getIdGraczaC();
                    id_gry = i;
                    break;
                }
                if(gry[i].getIdGraczaC() == client_socket){
                    socket_przeciwnika = gry[i].getIdGraczaB();
                    id_gry = i;
                    break;
                } 
            }
            if(czy_bicie == 1){
                int typ = gry[id_gry].plansza[x0][y0];
                gry[id_gry].plansza[x0][y0] = 0;
                gry[id_gry].plansza[newX][newY] = typ;
                gry[id_gry].plansza[zbityX][zbityY] = 0;
            }
            if(czy_bicie == 0){
                int typ = gry[id_gry].plansza[x0][y0];
                gry[id_gry].plansza[x0][y0] = 0;
                gry[id_gry].plansza[newX][newY] = typ;
            }
            if(czy_promocja == 1){
                if(gry[id_gry].plansza[newX][newY] == 1) gry[id_gry].plansza[newX][newY] = 2;
                if(gry[id_gry].plansza[newX][newY] == -1) gry[id_gry].plansza[newX][newY] = -2;
            }
            if(czy_zmiana == 1){
                send(socket_przeciwnika, buffer, sizeof(buffer),0);
                gry[id_gry].setTura(gry[id_gry].getTura() + 1);
            }

        }
    }
}

int main(){

    vector <int> deskryptory;  //wektor przechowujacy deskryptory gniazd dla kolejnych klientow
    vector <Gra> gry;   //wektor przechowujacy stany rozgrywek oraz informacje o grze

    int port = 54000;   //deklaracja portu serwera
    int max_clients = SOMAXCONN;
    int optval = 1;

    int server_socket = socket(AF_INET, SOCK_STREAM, 0); //utworzenie gniazda serwera

    if(server_socket == -1){
        cerr << "Blad przy tworzeniu socketu" << endl;
        return -1;
    }

    if(setsockopt(server_socket, SOL_SOCKET, SO_REUSEADDR, (char *)&optval, sizeof(optval)) == -1){
        cerr << "Blad w ustawieniu setsockopt" << endl;
        return -2;
    }

    sockaddr_in address;
    address.sin_family = AF_INET;
    address.sin_port = htons(port);
    inet_pton(AF_INET, "0.0.0.0", &address.sin_addr);
    //socklen_t address_len = sizeof(address);

    if(bind(server_socket, (sockaddr*)&address, sizeof(address)) == -1){
        cerr << "Blad podczas wykonywania bind" << endl;
        return -3;
    }

    if(listen(server_socket, SOMAXCONN) == 0){
        cout << "Serwer wlaczony na porcie: " << port << endl;  //przejscie serwera w tryb nasluchu
    }

    while(true){  //nieskonczona petla w ktorej serwer akceptuje polaczenie od klienta oraz tworzy nowy watek do obslugi go

        if((int)deskryptory.size() == max_clients){
            cout << "osiagnieto maksymalna liczbe klientow" << endl;
        }
        int client_socket = accept(server_socket, NULL, NULL);

        deskryptory.push_back(client_socket);

        thread t(handleConnection, client_socket, deskryptory, gry);
        t.join();
    }


    return 0;
}