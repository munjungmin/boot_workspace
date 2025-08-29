package com.sinse.electroshop.model.product;

import com.sinse.electroshop.domain.Product;
import com.sinse.electroshop.exception.ProductRegistException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductDAO productDAO;

    @Override
    public List getList() {
        return productDAO.selectAll();
    }

    @Override
    public List getListByStoreId(int storeId) {
        return productDAO.findByStore_storeId(storeId);
    }

    @Override
    public Product getDetail(int productId) {
        return productDAO.selectById(productId);
    }

    @Override
    public Product regist(Product product) throws ProductRegistException{
        try{
            return productDAO.save(product);
        }catch(DataAccessException e){
            throw new ProductRegistException("상품 등록 중 예외 발생", e);
        }
    }
}
