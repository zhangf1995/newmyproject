package com.myproject.service.impl;

import org.springframework.stereotype.Service;

import com.myproject.domain.Order;
import com.myproject.service.IOrderService;

@Service
public class OrderServiceImpl extends BaseServiceImpl<Order> implements IOrderService{
}
