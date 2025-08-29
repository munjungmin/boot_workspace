package com.sinse.electroshop.model.product;

import com.sinse.electroshop.domain.Product;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class JpaProductDAO implements ProductDAO {

    private final JpaProductRepository repository;

    @Override
    public List selectAll() {
        return repository.findAll();
    }

    @Override
    public Product selectById(int productId) {
        return repository.findById(productId);
    }

    @Override
    public Product save(Product product) throws DataAccessException {
        return repository.save(product);
    }

    public List findByStore_storeId(int storeId){
        return repository.findByStore_storeId(storeId);
    }
}
