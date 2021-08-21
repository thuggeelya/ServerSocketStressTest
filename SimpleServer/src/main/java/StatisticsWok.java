
public class StatisticsWok implements Statistics {

    private int orders;
    private int access;
    private String name;
    private int sumAccess;

    public StatisticsWok(){
        this.orders = 0;
        this.access = 0;
        this.sumAccess = 0;
    }

    @Override
    public void orderInfo(Dish dish) {
        this.name = dish.getName();
        this.sumAccess = this.access * dish.getCost();
    }

    public int getOrders() {
        return orders;
    }

    public void setOrders(int orders) {
        this.orders = orders;
    }

    public int getAccess() {
        return access;
    }

    public void setAccess(int access) {
        this.access = access;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSumAccess() {
        return sumAccess;
    }

    public void setSumAccess(int sumAccess) {
        this.sumAccess = sumAccess;
    }
}