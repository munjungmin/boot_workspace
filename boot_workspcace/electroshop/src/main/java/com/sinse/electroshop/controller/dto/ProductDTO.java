package com.sinse.electroshop.controller.dto;

import lombok.Data;

@Data
public class ProductDTO {
    private int store_id;
    private int product_id;
    private String product_name;
    private int price;
    private String brand;
}
