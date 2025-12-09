public class ControlFlow {

    public static void main(String[] args) {

        int x = 7;

        if (x > 5) {
            System.out.println("x is greater than 5");
        } else {
            System.out.println("x is less than or equal to 5");
        }

        for (int i = 1; i <= 5; i++) {
            System.out.println("Iteration: " + i);
        }

        switch (x) {
            case 7 -> System.out.println("x is seven");
            default -> System.out.println("x is not seven");
        }
    }
}
