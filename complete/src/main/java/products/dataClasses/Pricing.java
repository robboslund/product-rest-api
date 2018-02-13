package products.dataClasses;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("Student")
public class Pricing implements Serializable {

    private long productId;
    private double price;
    private String currencyCode;

    public Pricing(long productId, double price, String currencyCode){
        this.productId = productId;
        this.price = price;
        this.currencyCode = currencyCode;
    }

    @Id
    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
}
