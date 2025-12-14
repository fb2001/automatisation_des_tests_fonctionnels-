package hai702.tp4.model;
public class Product {
    private int id;

    private java.lang.String name;

    private double price;

    private java.lang.String expirationDate;

    public Product(int id, java.lang.String name, double price, java.lang.String expirationDate) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.expirationDate = expirationDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public java.lang.String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(java.lang.String expirationDate) {
        this.expirationDate = expirationDate;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (((((id + " - ") + name) + " - ") + price) + "€ - exp: ") + expirationDate;
    }
}