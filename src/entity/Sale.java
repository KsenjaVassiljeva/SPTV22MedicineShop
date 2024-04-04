/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import entity.Customer;
import entity.Medicine;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
public class Sale implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Medicine medicine;

    @OneToOne
    private Customer customer;

    @Temporal(TemporalType.DATE)
    private Date saleDate;

    private int unitsSold;
    private int quantity;

    public Sale() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public int getUnitsSold() {
        return unitsSold;
    }

    public void setUnitsSold(int unitsSold) {
        this.unitsSold = unitsSold;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public double getTotal() {
        if (medicine != null) {
            return unitsSold * medicine.getPrice();
        }
        return 0.0; // or throw an exception indicating missing medicine
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.medicine);
        hash = 83 * hash + Objects.hashCode(this.customer);
        hash = 83 * hash + Objects.hashCode(this.saleDate);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Sale other = (Sale) obj;
        return Objects.equals(this.medicine, other.medicine)
                && Objects.equals(this.customer, other.customer)
                && Objects.equals(this.saleDate, other.saleDate);
    }

    @Override
    public String toString() {
        if (medicine == null || customer == null || saleDate == null) {
            return "Sale{id=" +
                    id + ", medicine=null, customer=null, saleDate=null, unitsSold=" + 
                    unitsSold + ", quantity=" + 
                    quantity + '}';
        }
        String medicineName = (medicine.getName() != null) ? medicine.getName() : "Unknown";
        String customerName = (customer.getFirstName() != null && customer.getLastName() != null) ? 
            customer.getFirstName() + " " + customer.getLastName() : "Unknown";
        return "Sale{" +
                "id=" + id +
                ", medicine=" + medicineName +
                ", customer=" + customerName +
                ", saleDate=" + saleDate +
                ", unitsSold=" + unitsSold +
                ", quantity=" + quantity +
                '}';
    }
}
