import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Select mode: \n1. Send File \n2. Receive File");
        System.out.print("Enter choice (1 or 2): ");
        String choice = sc.nextLine();

        try {
            if (choice.equals("1")) {

                System.out.print("Enter path of file to send: ");
                String filePath = sc.nextLine();

                if (!FileManager.isValidFile(filePath)) {
                    System.err.println("❌ Invalid file path or file not readable.");
                    return;
                }

                System.out.print("Enter recipient's IP address: ");
                String ip = sc.nextLine();

                System.out.print("Enter port number: ");
                int port = Integer.parseInt(sc.nextLine());

                System.out.print("Enter a decryption key (share securely with receiver): ");
                String key = sc.nextLine();

                Client.sendFile(filePath, ip, port, key);
                Logger.log(" File sent successfully to " + ip + " on port " + port);

            } else if (choice.equals("2")) {

                System.out.print("Enter port number to listen on: ");
                int port = Integer.parseInt(sc.nextLine());

                System.out.print("Enter directory to save the received file (default: received): ");
                String saveDirectory = sc.nextLine().trim();
                if (saveDirectory.isEmpty()) {
                    saveDirectory = "received";
                }

                System.out.print("Enter decryption key: ");
                String key = sc.nextLine();

                Server server = new Server();
                server.receiveFile(port, saveDirectory, key);

            } else {
                System.out.println(" Invalid option. Please select 1 or 2.");
            }

        } catch (Exception e) {
            System.err.println(" Error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            sc.close();
        }
    }
}
