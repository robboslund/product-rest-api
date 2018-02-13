package products;
import com.google.gson.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import products.dataClasses.Pricing;
import products.dataClasses.PricingRepository;
import products.returnClasses.CurrentPrice;
import products.returnClasses.Product;
import java.util.Random;

@RestController
public class ProductController {

    @Autowired
    PricingRepository pricingRepository;

    private String[] excludePropertiesList = {"taxonomy", "price", "promotion", "bulk_ship", "rating_and_review_reviews",
            "rating_and_review_statistics", "question_answer_statistics"};

    @RequestMapping(value = "/products/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> testRestApi(@PathVariable("id") long id) {
        createMockDataWithId(id);
        String productInfoUrl = getItemInfoUrl(id);
        return getProductDetails(productInfoUrl);
    }

    private String getItemInfoUrl(long id) {
        return "http://redsky.target.com/v2/pdp/tcin/" + id + getExcludedPropertiesString();
    }

    private String getExcludedPropertiesString() {
        return "?excludes=" + String.join(",", excludePropertiesList);
    }

    private void createMockDataWithId(long id) {
        Pricing pricing = pricingRepository.findOne(String.valueOf(id));
        if (pricing == null){
            Pricing newPricing = new Pricing(id,generateDollarAmount(),"USD");
            pricingRepository.save(newPricing);
        }
    }

    private double generateDollarAmount() {
        double min = 0.00;
        double max = 999.99;
        double precision = 100.00; //for creating a 2 decimal float
        Random r = new Random();
        return (r.nextInt((int) ((max - min) * precision + 1)) + min * precision) / precision;
    }

    private ResponseEntity<?> getProductDetails(String productInfoUrl) {
        RestTemplate restTemplate = new RestTemplate();
        String responseString;
        Product responseProduct;
        try {
            responseString = restTemplate.getForObject(productInfoUrl, String.class);
            responseProduct = convertResponseStringToResponseProduct(responseString);
            return new ResponseEntity<>(responseProduct, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
        }
    }

    private Product convertResponseStringToResponseProduct(String responseString) {
        Product product = new Product();
        CurrentPrice currentPrice = new CurrentPrice();
        JsonObject responseJson = responseStringToResponseJson(responseString);
        JsonObject productJson = responseJson.getAsJsonObject("product");

        setProductDetails(product, productJson);
        setPriceDetails(currentPrice, product.getId());
        product.setCurrent_price(currentPrice);

        return product;
    }

    private JsonObject responseStringToResponseJson(String responseString) {
        JsonParser parser = new JsonParser();
        return parser.parse(responseString).getAsJsonObject();
    }

    private void setProductDetails(Product product, JsonObject productJson) {
        JsonObject itemJson = productJson.getAsJsonObject("item");
        JsonObject productDescriptionJson = itemJson.getAsJsonObject("product_description");
        product.setId(itemJson.get("tcin").getAsLong());
        product.setName(productDescriptionJson.get("title").getAsString());
    }

    private void setPriceDetails(CurrentPrice currentPrice, long productId) {
        Pricing pricing = pricingRepository.findOne(String.valueOf(productId));
        currentPrice.setCurrency_code(pricing.getCurrencyCode());
        currentPrice.setValue(pricing.getPrice());
    }
}