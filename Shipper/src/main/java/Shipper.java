import ivf6.Help;

import java.io.IOException;
import java.util.ArrayList;

class ShipperThread extends Thread {

    /**
     * Request means the shipper's offer.  One ingredient
     * used once to prepare only one dish by the kitchen.
     *
     * FLOUR;
     * EGGS;
     * CHEESE;
     * SAUSAGE;
     * RICE;
     * FISH;
     * WASABI;
     * NOODLES;
     * CHICKEN;
     * BREAD;
     * CUTLET;
     * SALAD.
     */

    private static final ArrayList<String> ingredients = new ArrayList<String>(){{
        add("flour");
        add("eggs");
        add("cheese");
        add("sausage");
        add("rice");
        add("fish");
        add("wasabi");
        add("noodles");
        add("chicken");
        add("bread");
        add("cutlet");
        add("salad");
    }};

    // interval between one shipper's current and next order
    final int shipperInterval = 2000;

    @Override
    public void run() {

        boolean firmPayable = true;
        while (firmPayable) {
            try {
                Thread.sleep(shipperInterval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try (Help help = new Help("127.0.0.1", 8000)) {

                String request = ingredients.get((int) (Math.random() * ingredients.size())) + " 1 0";
                help.writeLine(request);

                String response = help.readLine();
                if (response == null){
                    break;
                }
                //firmPayable = !response.startsWith("stop");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

public class Shipper {

    static final int shipperCount = Server.shipperCount;
    static final int shipperInterval = 150;

    public static void main(String[] args) {

        for(int i=0; i<shipperCount; i++) {

            ShipperThread shipperThread = new ShipperThread();
            try {
                Thread.sleep(shipperInterval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            shipperThread.start();
            shipperThread.setName(String.valueOf(i+1));
        }
    }
}