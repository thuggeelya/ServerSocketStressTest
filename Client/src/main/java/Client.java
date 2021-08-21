import ivf6.Help;

import java.io.IOException;

class ClientThread extends Thread {

/**
 * Request means the client's desire.
 * PIZZA (flour, eggs, cheese, sausage);
 * SUSHI (rice, fish, wasabi);
 * WOK (noodles, chicken);
 * CHEESEBURGER (bread, cutlet, cheese, salad).
 */

    final int MAX_DENY = 50;
    String[][] dishes = new String[4][2];
    Dish dishCost = new Dish();
    final int pizzaCost = dishCost.ingredientsPizza().getCost();
    final int sushiCost = dishCost.ingredientsSushi().getCost();
    final int wokCost = dishCost.ingredientsWok().getCost();
    final int cheeseburgerCost = dishCost.ingredientsCheeseburger().getCost();

    // interval between one client's current and next order
    final int clientInterval = 1500;

    @Override
    public void run(){

        dishes[0][0] = "pizza";
        dishes[1][0] = "sushi";
        dishes[2][0] = "wok";
        dishes[3][0] = "cheeseburger";
        dishes[0][1] = String.valueOf(pizzaCost);
        dishes[1][1] = String.valueOf(sushiCost);
        dishes[2][1] = String.valueOf(wokCost);
        dishes[3][1] = String.valueOf(cheeseburgerCost);

        int denys = 0;
        final int userID = (int) (Math.random()*100000) + 1;
        int budget = 5000;

        boolean payable = true;

        while (payable) {
            try (Help help = new Help("127.0.0.1", 8000)) {

                try {
                    Thread.sleep(clientInterval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // get random dish
                int index = (int) (Math.random() * dishes.length);
                String dish = dishes[index][0];

                int quantity = (int) (Math.random() * 2 + 1);
                int currentCost = Integer.parseInt(dishes[index][1]);

                // is client payable
                int cost = currentCost * quantity;
                payable = budget > cost;
                String request = payable ? dish + " " + quantity + " " + userID : "stop"; //dish 'space' quantity 'space' ID (to parse on server)

                help.writeLine(request);
                String response = help.readLine();
                if (response == null){
                    break;
                }

                denys += (response.startsWith("Sorry")) ? 1 : 0;
                int balance = budget-currentCost;
                budget = (response.startsWith("Sorry")) ? budget : balance;

                payable = (denys <= MAX_DENY) && (!request.startsWith("stop"));
                if (denys > MAX_DENY) {
                    System.out.println("Max denies ..");
                    break;
                } else if (request.startsWith("stop")) {
                    System.out.println("I have no money ..");
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

public class Client {

    public static final int clientCount = Server.clientCount;
    static final int clientInterval = 150;

    public static void main(String[] args) {

        for(int i=0; i<clientCount; i++) {

            ClientThread clientThread = new ClientThread();
            try {
                Thread.sleep(clientInterval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            clientThread.start();
            clientThread.setName(String.valueOf(i+1));
        }
    }
}