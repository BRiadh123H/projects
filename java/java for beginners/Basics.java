import java.util.Scanner;
public class Basics {
    public static void main(String[] args) {
        Scanner sc =new Scanner(System.in);
        System.out.print("Enter your age: ");
        int age =sc.nextInt();

        double height = 1.82;
        String name = "Riadh";
        System.out.println(name + " is "+ age + " years old and his height is " + height + " meters.");
        sc.close();
    }
    
}
