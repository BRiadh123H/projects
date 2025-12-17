package poo;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        vehicle tab[] = {
            new car("Toyota", "Corolla", 2020),
            new truck("Volvo", "FH16", 2019, 20000),
            new car("Ford", "Mustang", 2021),
            new truck("Scania", "R500", 2018, 18000),
            new car("Honda", "Civic", 2022)
        };

        for (vehicle v : tab) {
            v.demarrer();
            try { 
                v.setVitesse(50);
            } catch (invalidspeedexception e) {
                System.out.println("Erreur vitesse " + v.marque);
            }
            v.afficherInfos();
            System.out.println();
        }

        if (tab[1] instanceof reparable) {
            ((reparable) tab[1]).repare();
        }

        // Lecture utilisateur
        Scanner sc = new Scanner(System.in);
        car maVoiture = new car();

        int vitesse;

        while (true) {
            try {
                System.out.print("Entrez la vitesse de la voiture : ");
                vitesse = sc.nextInt();
                maVoiture.setVitesse(vitesse);
                break;
            } catch (invalidspeedexception e) {
                System.out.println("Erreur : " + e.getMessage());
            } catch (InputMismatchException e) {
                System.out.println("Veuillez entrer un nombre !");
            }
            sc.nextLine();
        }

        sc.nextLine();

        System.out.print("Entrez la marque : ");
        maVoiture.marque = sc.nextLine();

        System.out.print("Entrez le modèle : ");
        maVoiture.modele = sc.nextLine();

        try {
            System.out.print("Entrez l'année : ");
            maVoiture.annee = sc.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Année invalide !");
        }

        maVoiture.afficherInfos();
        sc.close();
    }
}

