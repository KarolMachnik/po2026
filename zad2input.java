import java.util.Scanner;

// robie scannerem bo chciałem żeby był print przed wybraniem a metoda z zajęć nie działała jak ja chcialem

public class zad2input {
    public static void main (String[] args){
        System.out.print("Podaj ilosc linii: ");
        Scanner num = new Scanner(System.in);
        int n = num.nextInt();
        for (int i=0; i<n+1; i++){
            System.out.println("*".repeat(i));
        }
    }
}