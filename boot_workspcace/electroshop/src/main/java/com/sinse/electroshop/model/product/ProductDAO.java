package com.sinse.electroshop.model.product;

import com.sinse.electroshop.domain.Product;

import java.util.List;

public interface ProductDAO {
    public List selectAll();
    public List findByStore_storeId(int storeId);
    public Product selectById(int productId);
    public Product save(Product product);

}
