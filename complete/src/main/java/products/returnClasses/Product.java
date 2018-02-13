package products.returnClasses;

import products.returnClasses.CurrentPrice;

public class Product {
    private long id;
    private String name;
    private CurrentPrice current_price;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CurrentPrice getCurrent_price() {
        return current_price;
    }

    public void setCurrent_price(CurrentPrice current_price) {
        this.current_price = current_price;
    }
}
