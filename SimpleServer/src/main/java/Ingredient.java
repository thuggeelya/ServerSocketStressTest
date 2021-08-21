
public class Ingredient implements IngredientsAvailable{

    public String name;
    public int cost;
    public boolean available;
    public int quantity;

    public Ingredient(String name, int cost, boolean available, int quantity){
        this.name = name;
        this.cost = cost;
        this.available = available;
        this.quantity = quantity;
    }

    public Ingredient(){}

    //portions for 1 dish
    @Override
    public Ingredient costBread() {
        return new Ingredient("bread", 15, true, 100);
    }

    @Override
    public Ingredient costCheese() {
        return new Ingredient("cheese", 50, true, 150);
    }

    @Override
    public Ingredient costChicken() {
        return new Ingredient("chicken", 100, true, 100);
    }

    @Override
    public Ingredient costCutlet() {
        return new Ingredient("cutlet", 80, true, 100);
    }

    @Override
    public Ingredient costEggs() {
        return new Ingredient("eggs", 15, true, 100);
    }

    @Override
    public Ingredient costFish() {
        return new Ingredient("fish", 75, true, 100);
    }

    @Override
    public Ingredient costFlour() {
        return new Ingredient("flour", 20, true, 100);
    }

    @Override
    public Ingredient costNoodles() {
        return new Ingredient("noodles", 50, true, 100);
    }

    @Override
    public Ingredient costRice() {
        return new Ingredient("rice", 60, true, 100);
    }

    @Override
    public Ingredient costSalad() {
        return new Ingredient("salad", 15, true, 100);
    }

    @Override
    public Ingredient costSausage() {
        return new Ingredient("sausage", 80, true, 100);
    }

    @Override
    public Ingredient costWasabi() {
        return new Ingredient("wasabi", 60, true, 100);
    }

    public String getName() {
        return name;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isAvailable() {
        return available;
    }

    public int getQuantity() {
        return quantity;
    }

    public void decQuantity(int c) {
        if (this.getQuantity()>0) {
            this.quantity -= c;
        }
    }

    public void incQuantity() {
        this.quantity += 1;
    }

    public int getCost() {
        return cost;
    }
}