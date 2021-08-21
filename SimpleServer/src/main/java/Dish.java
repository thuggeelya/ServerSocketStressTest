
import java.util.ArrayList;

public class Dish implements DishesAvailable {

    private int cost;
    private String name;
    private ArrayList<String> ingredients;

    public Dish(String name, ArrayList<String> ingredients, int cost) {
        this.name = name;
        this.ingredients = ingredients;
        this.cost = cost;
    }

    public Dish(){}

    @Override
    public Dish ingredientsPizza() {
        Ingredient ingredient = new Ingredient();
        return new Dish("pizza", new ArrayList<String>(){
             {
                add("flour");
                add("eggs");
                add("cheese");
                add("sausage");
            }
        }, ingredient.costFlour().getCost()+ingredient.costEggs().getCost()+ingredient.costCheese().getCost()+ingredient.costSausage().getCost());
    }

    @Override
    public Dish ingredientsWok() {
        Ingredient ingredient = new Ingredient();
        return new Dish("wok", new ArrayList<String>(){
            {
                add("noodles");
                add("chicken");
            }
        }, ingredient.costNoodles().getCost()+ingredient.costChicken().getCost());
    }

    @Override
    public Dish ingredientsSushi() {
        Ingredient ingredient = new Ingredient();
        return new Dish("sushi", new ArrayList<String>(){
            {
                add("rice");
                add("fish");
                add("wasabi");
            }
        }, ingredient.costRice().getCost()+ingredient.costFish().getCost()+ingredient.costWasabi().getCost());
    }

    @Override
    public Dish ingredientsCheeseburger() {
        Ingredient ingredient = new Ingredient();
        return new Dish("cheeseburger",new ArrayList<String>(){
            {
                add("bread");
                add("cutlet");
                add("cheese");
                add("salad");
            }
        }, ingredient.costBread().getCost()+ingredient.costCutlet().getCost()+ingredient.costCheese().getCost()+ingredient.costSalad().getCost());
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public int getCost() {
        return cost;
    }

    public String getName() {
        return name;
    }
}