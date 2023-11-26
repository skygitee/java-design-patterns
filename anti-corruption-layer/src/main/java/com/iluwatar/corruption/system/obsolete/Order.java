package com.iluwatar.corruption.system.obsolete;

import java.util.Objects;

public class Order {
    private String id;
    private String customer;

    private String item;
    private String qty;
    private String price;

    public Order(String id, String customer, String item, String qty, String price) {
        this.id = id;
        this.customer = customer;
        this.item = item;
        this.qty = qty;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", customer='" + customer + '\'' +
                ", item='" + item + '\'' +
                ", qty='" + qty + '\'' +
                ", price='" + price + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (!Objects.equals(id, order.id)) return false;
        if (!Objects.equals(customer, order.customer)) return false;
        if (!Objects.equals(item, order.item)) return false;
        if (!Objects.equals(qty, order.qty)) return false;
        return Objects.equals(price, order.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customer, item, qty, price);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}