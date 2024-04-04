package entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Medicine implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String manufacturer;
    
    @Column(nullable = false)
    private int productionYear;
    
    @Column(nullable = false)
    private int price;
    
    private int quantity;
    private int count;
    private int yearlySales;
    private int monthlySales;
    private int dailySales;
    
    

    public Medicine() {
    }

    public Medicine(String name, String manufacturer, int productionYear, int quantity, int count) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.productionYear = productionYear;
        this.quantity = quantity;
        this.count = count;
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public int getProductionYear() {
        return productionYear;
    }

    public void setProductionYear(int productionYear) {
        this.productionYear = productionYear;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public int getYearlySales() {
        return yearlySales;
    }

    public void setYearlySales(int yearlySales) {
        this.yearlySales = yearlySales;
    }

    public int getMonthlySales() {
        return monthlySales;
    }

    public void setMonthlySales(int monthlySales) {
        this.monthlySales = monthlySales;
    }

    public int getDailySales() {
        return dailySales;
    }

    public void setDailySales(int dailySales) {
        this.dailySales = dailySales;
    }


    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.name);
        hash = 29 * hash + Objects.hashCode(this.manufacturer);
        hash = 29 * hash + this.productionYear;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Medicine other = (Medicine) obj;
        if (this.productionYear != other.productionYear) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.manufacturer, other.manufacturer)) {
            return false;
        }
        return true;
    }
    
    public void updateSales(int quantitySold) {
        yearlySales += quantitySold;
        monthlySales += quantitySold;
        dailySales += quantitySold;
    }

    @Override
    public String toString() {
        return "Medicine{" +
                "name='" + name + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", productionYear=" + productionYear +
                ", quantity=" + quantity +
                ", count=" + count +
                ", price=" + price +
                '}';
    }
    
}
