package com.sinse.electroshop.controller.store;

import com.sinse.electroshop.controller.dto.ProductDTO;
import com.sinse.electroshop.domain.Product;
import com.sinse.electroshop.domain.Store;
import com.sinse.electroshop.exception.StoreNotFoundException;
import com.sinse.electroshop.model.product.ProductService;
import com.sinse.electroshop.model.store.StoreRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@Controller
@RequestMapping("/store")
@RequiredArgsConstructor
public class StoreProductController {
    private final ProductService productService;
    private final StoreRepository storeRepository;

    @GetMapping("/product/registform")
    public String registForm() {
        return "store/product/regist";
    }

    @PostMapping("/product/regist")
    @ResponseBody
    public ResponseEntity<String> regist(ProductDTO productDTO) {

        Product product = new Product();
        product.setProductId(productDTO.getProduct_id());
        product.setBrand(productDTO.getBrand());
        product.setPrice(productDTO.getPrice());
        product.setProductName(productDTO.getProduct_name());

        Store store = new Store();
        store.setStoreId(productDTO.getStore_id());
        product.setStore(store);

        productService.regist(product);
        return ResponseEntity.status(HttpStatus.CREATED).body("등록성공");
    }

    @GetMapping("/product/list")
    public String getList() {
        return "/store/product/list";
    }

    @GetMapping("/product/listbystore")
    public String getListByStore(Model model, @RequestParam(name = "storeId", required = false, defaultValue = "0") int storeId, HttpSession session) {
        log.debug("list by store 진입 storeId=" + storeId);
        if (storeId == 0) {
            Store store = (Store) session.getAttribute("store");
            storeId = store.getStoreId();
            log.debug("바뀐 storeId=" + storeId);
        }

        List productList = productService.getListByStoreId(storeId);
        model.addAttribute("productList", productList);
        log.debug("productList size=" + productList.size());
        return "store/product/list";
    }

    @GetMapping("/product/detail")
    public String getDetail(Model model, @RequestParam(name="product_id", required = false, defaultValue = "0") int productId) {

        Product product = productService.getDetail(productId);
        model.addAttribute("product", product);
        return "store/product/detail";
    }
}