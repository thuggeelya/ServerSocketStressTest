
import dnl.utils.text.table.TextTable;
import ivf6.Help;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 1.	Статистика заказов по клиентам с суммарным итогом:
 * Идентификатор | Сумма заказов | Остаток средств
 * 2.	Статистика заказов по товарам с суммарным итогом:
 * Наименование | Кол-во заказов | Кол-во проданного товара | Сумма проданного.
 * 3.	Остатки материалов на складе с итоговой стоимостью:
 * Наименование | Кол-во материалов | Стоимость.
 * 4.	Остаток на счету фирмы.
 */

public class Server implements ClientData {

    private static int access_quantity = 0;
    private static int deny_quantity = 0;
    private static int FIRM_BUDGET = 55000;
    private static final int CLIENT_START_BUDGET = 5000;

    public Server(){}

    public static int clientCount = 300;
    public static int shipperCount = 100;

    // userID, orderSum
    public static HashMap<Integer, Integer> userIDs = new HashMap<>();

    public static String[] clientColumns = {"№", "userID", "orderSum", "balance"};
    public static String[] dishColumns = {"name", "orderQuantity", "soldQuantity", "soldSum"};
    public static Object[][] clientData = null;
    public static Object[][] dishData = new Object[4][4]; // 4 dishes, 4 columns in statistics
    public static String[] ingredientColumns = {"name", "materialsQuantity", "cost"};
    public static Object[][] ingredientData = {};

    /*

    public static int resistance(){
        return getClientsNumber()+getShippersNumber();
    }

    public static void orderProcessing(){
        int access = access_quantity;
        int deny = deny_quantity;
        float processing = access/deny;
    }

    */

    public static void main(String[] args) {

        AtomicBoolean clientConnected = new AtomicBoolean(false);
        AtomicBoolean shipperConnected = new AtomicBoolean(false);

        try (ServerSocket server = new ServerSocket(8000)) {
            //System.setProperty("java.net.preferIPv4Stack" , "true");
            Ingredient temp = new Ingredient();

            // register ingredients
            Ingredient flour = temp.costFlour();
            Ingredient eggs = temp.costEggs();
            Ingredient cheese = temp.costCheese();
            Ingredient sausage = temp.costSausage();
            Ingredient rice = temp.costRice();
            Ingredient fish = temp.costFish();
            Ingredient wasabi = temp.costWasabi();
            Ingredient noodles = temp.costNoodles();
            Ingredient chicken = temp.costChicken();
            Ingredient bread = temp.costBread();
            Ingredient cutlet = temp.costCutlet();
            Ingredient salad = temp.costSalad();

            // hashmap to find ingredients by name
            HashMap<String, Ingredient> ingredients_available = new HashMap<>();
            ingredients_available.put(flour.getName(), flour);
            ingredients_available.put(eggs.getName(), eggs);
            ingredients_available.put(sausage.getName(), sausage);
            ingredients_available.put(cheese.getName(), cheese);
            ingredients_available.put(rice.getName(), rice);
            ingredients_available.put(fish.getName(), fish);
            ingredients_available.put(wasabi.getName(), wasabi);
            ingredients_available.put(noodles.getName(), noodles);
            ingredients_available.put(chicken.getName(), chicken);
            ingredients_available.put(bread.getName(), bread);
            ingredients_available.put(cutlet.getName(), cutlet);
            ingredients_available.put(salad.getName(), salad);

            // stress test stats
            StatisticsPizza pizza = new StatisticsPizza();
            StatisticsSushi sushi = new StatisticsSushi();
            StatisticsWok wok = new StatisticsWok();
            StatisticsCheeseburger cheeseburger = new StatisticsCheeseburger();

            // condition for server to continue
            AtomicBoolean go_on = new AtomicBoolean(true);

            while (go_on.get()) {
                Help help = new Help(server);

                new Thread(() -> { //run()
                    String request = help.readLine();

                    // for the current dish
                    Dish dish = new Dish();

                    // for the current ingredients
                    ArrayList<String> ingredients;

                    String response = "";
                    // order or delivery or 'stop'
                    String definition = "stop";
                    int quantity = 0;
                    int userID = 0;

                    // decode the request
                    if (!request.startsWith("stop")) {

                        definition = request.substring(0, request.indexOf(' '));
                        quantity = Integer.parseInt(request.substring(request.indexOf(' ') + 1, request.indexOf(' ') + 2));
                        userID = Integer.parseInt(request.substring(request.indexOf(' ') + 3));

                        // register user by id
                        if (userID!=0)
                            userIDs.putIfAbsent(userID, 0);
                    }

                    // define it's a client or a shipper
                    switch (definition) {
                        case "pizza" -> {
                            clientConnected.set(true);
                            // register dish as a pizza
                            dish = dish.ingredientsPizza();
                            pizza.setOrders(pizza.getOrders() + 1);
                        }
                        case "sushi" -> {
                            clientConnected.set(true);
                            dish = dish.ingredientsSushi();
                            // register dish as sushi
                            sushi.setOrders(sushi.getOrders() + 1);
                        }
                        case "wok" -> {
                            clientConnected.set(true);
                            // register dish as a wok
                            dish = dish.ingredientsWok();
                            wok.setOrders(wok.getOrders() + 1);
                        }
                        case "cheeseburger" -> {
                            clientConnected.set(true);
                            // register dish as a cheeseburger
                            dish = dish.ingredientsCheeseburger();
                            cheeseburger.setOrders(cheeseburger.getOrders() + 1);
                        }
                        case "stop" -> {
                            // register null dish
                            dish = null;
                            // only client can say 'stop'
                            clientConnected.set(true);
                        }
                        default -> { // shipper
                            shipperConnected.set(true);
                            // register null dish
                            dish = null;
                            // if firm is payable
                            if (FIRM_BUDGET >= ingredients_available.get(definition).getCost()) {
                                response = "recd";
                                FIRM_BUDGET -= ingredients_available.get(definition).getCost();
                                // got fresh ingredient
                                ingredients_available.get(definition).incQuantity();
                            } else {
                                response = "stop";
                            }
                        }
                    }

                    // if client connected
                    // check whether dish can be cooked (all its ingredients must be available)
                    if ((dish != null)/* && (go_on.get())*/) {
                        ingredients = dish.getIngredients();
                        response = null;
                        boolean available = true;

                        for (String name : ingredients) {
                            if (ingredients_available.get(name).getQuantity() - quantity < 0) {
                                available = false;
                                //ingredients_available.get(name).setAvailable(false);
                                break;
                            }
                        }

                        if (available) {
                            FIRM_BUDGET += dish.getCost();
                            switch (dish.getName()) {
                                case "pizza" -> {
                                    // inc pizza sales number
                                    pizza.setAccess(pizza.getAccess() + 1);
                                    // save changes
                                    pizza.orderInfo(dish);
                                }
                                case "sushi" -> {
                                    // inc sushi sales number
                                    sushi.setAccess(sushi.getAccess() + 1);
                                    // save changes
                                    sushi.orderInfo(dish);
                                }
                                case "wok" -> {
                                    // inc wok sales number
                                    wok.setAccess(wok.getAccess() + 1);
                                    // save changes
                                    wok.orderInfo(dish);
                                }
                                case "cheeseburger" -> {
                                    // inc cheeseburger sales number
                                    cheeseburger.setAccess(cheeseburger.getAccess() + 1);
                                    // save changes
                                    cheeseburger.orderInfo(dish);
                                }
                            }
                            for (String name : ingredients) {
                                ingredients_available.get(name).decQuantity(quantity);
                            }
                            // inc whole sales number
                            access_quantity++;
                            // count user's order sum
                            userIDs.replace(userID, userIDs.get(userID) + dish.getCost());
                        } else {
                            response = "oops";
                            // inc whole denies number
                            deny_quantity++;
                        }

                        response = (response == null) ? "OK, " + dish.getName() :
                                       "Sorry, we can't cook " + dish.getName() ;
                    }
                    else if (shipperConnected.get()){
                        //System.out.println("Shipper - " + request);

                        // fill in ingredient data
                        Ingredient[] ingredient_values = ingredients_available.values().toArray(new Ingredient[0]);
                        ingredientData = new Object[ingredient_values.length][3]; // 3 columns

                        for (int i=0; i<ingredient_values.length; i++){
                            ingredientData[i] = new Object[]{ingredient_values[i].getName(), ingredient_values[i].getQuantity(),
                                    ingredient_values[i].getCost() * ingredient_values[i].getQuantity()};
                        }
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // fill in dish data
                    dishData[0] = new Object[]{pizza.getName(), pizza.getOrders(), pizza.getAccess(), pizza.getSumAccess()};
                    dishData[1] = new Object[]{wok.getName(), wok.getOrders(), wok.getAccess(), wok.getSumAccess()};
                    dishData[2] = new Object[]{sushi.getName(), sushi.getOrders(), sushi.getAccess(), sushi.getSumAccess()};
                    dishData[3] = new Object[]{cheeseburger.getName(), cheeseburger.getOrders(), cheeseburger.getAccess(), cheeseburger.getSumAccess()};

                    // fill in client data
                    Integer[] users;
                    /*

                      ClientData userData;

                      if (userIDs.keySet().size() == clientCount){

                          userData = new ServerProxy();

                      }

                      else{

                          userData = new Server();

                      }

                      users = userData.setClientData();

                      */
                    users = userIDs.keySet().toArray(new Integer[0]);
                    Integer[] orderSum = userIDs.values().toArray(new Integer[0]);
                    ArrayList<Object[]> clients = new ArrayList<>();
                    for (int i=0; i<users.length; i++){
                        int sum = orderSum[i];
                        clients.add(new Object[]{i+1, users[i], sum, CLIENT_START_BUDGET - sum});
                    }
                    int clientsSize = clients.size();
                    clientData = new Object[clientsSize][4]; // 4 columns
                    for (int i=0; i<clientsSize; i++){
                        clientData[i] = clients.get(i);
                    }

                    // fill in statistics tables
                    TextTable clientTable = new TextTable(clientColumns, clientData);
                    TextTable dishTable = new TextTable(dishColumns, dishData);
                    TextTable ingredientTable = new TextTable(ingredientColumns, ingredientData);

                    System.out.println("\n--------CLIENTS STATISTICS--------");
                    clientTable.printTable();
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("\n--------DISH STATISTICS--------");
                    dishTable.printTable();
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("\n--------INGREDIENTS STATISTICS--------");
                    ingredientTable.printTable();
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("\n--------FIRM BALANCE--------\n" + FIRM_BUDGET);

                    help.writeLine(response);

                    // closing current connection
                    try {
                        help.close();
                        clientConnected.set(false);
                        shipperConnected.set(false);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
                //
                go_on.set(true);
            }
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Integer[] setClientData(){
        return userIDs.keySet().toArray(new Integer[0]);
    }
}