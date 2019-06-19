package app.productosfirebase;

public class Product {
    private String ProductoId;
    private String Name;
    private String Description;
    private String Price;

    public Product(){

    }

    public Product(String ProductId , String Name , String Description , String Price){
        this.ProductoId=ProductId;
        this.Name=Name;
        this.Price=Price;
        this.Description=Description;
    }


    public String getProductoId() {
        return ProductoId;
    }

    public void setProductoId(String productoId) {
        ProductoId = productoId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }




}
