
import controller.Controller;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CommandHandler handler = new CommandHandler();

        while (scanner.hasNextLine()) {
            String comando = scanner.nextLine().trim();
            handler.executar(comando);
        }
        scanner.close();
    }
}