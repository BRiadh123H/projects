package poo1;

public class Main {
    
    public static void main(String[] args) {
        vehicle tab []= new vehicle[5];
        tab[0]= new car("Toyota","Corolla",2020);
        tab[1]= new truck("Volvo","FH16",2019,20000);
        tab[2]= new car("Ford","Mustang",2021);
        tab[3]= new truck("Scania","R500",2018,18000);
        tab[4]= new car("Honda","Civic",2022);
        for (int i=0;i<tab.length;i++) {
            tab[i].demarrer();
            tab[i].setVitesse((i+1)*10);
            tab[i].afficherInfos();
            System.out.println();
        }
    }
}
