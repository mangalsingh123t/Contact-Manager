package com.contactmanager.contanctmanager.Entites;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class MyOrder {

    @Id
    String myOrder_Id = UUID.randomUUID().toString();

    String orderId;

    Integer amount;

    // int amount_paid;

    // String currency;

    String status;

    // String created_at;

    @ManyToOne
    private User user;

    String paymentd;

    public String getMyOrder_Id() {
        return myOrder_Id;
    }

    public void setMyOrder_Id(String myOrder_Id) {
        this.myOrder_Id = myOrder_Id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPaymentd() {
        return paymentd;
    }

    public void setPaymentd(String paymentd) {
        this.paymentd = paymentd;
    }

}
// {
// "id": "order_EKwxwAgItmmXdp",
// "entity": "order",
// "amount": 50000,
// "amount_paid": 0,
// "amount_due": 50000,
// "currency": "INR",
// "receipt": "receipt#1",
// "offer_id": null,
// "status": "created",
// "attempts": 0,
// "notes": [],
// "created_at": 1582628071
// }