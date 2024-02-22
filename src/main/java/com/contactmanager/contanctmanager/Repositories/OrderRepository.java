package com.contactmanager.contanctmanager.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.contactmanager.contanctmanager.Entites.MyOrder;

public interface OrderRepository extends JpaRepository<MyOrder,String> {
      
      MyOrder findByOrderId(String orderId);
}
