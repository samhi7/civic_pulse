import java.util.Scanner;

public class Calc {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        double a = sc.nextDouble();
        char op = sc.next().charAt(0);
        double b = sc.nextDouble();

        switch (op) {
            case '+':
                System.out.println(a + b);
                break;
            case '-':
                System.out.println(a - b);
                break;
            case '*':
                System.out.println(a * b);
                break;
            case '/':
                if (b != 0)
                    System.out.println(a / b);
                else
                    System.out.println("Cannot divide by zero");
                break;
            default:
                System.out.println("Invalid Operator");
        }
    }
} 
