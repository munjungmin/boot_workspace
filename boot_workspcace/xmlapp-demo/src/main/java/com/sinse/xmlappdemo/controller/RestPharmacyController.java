package com.sinse.xmlappdemo.controller;

import com.sinse.xmlappdemo.domain.Pharmacy;
import com.sinse.xmlappdemo.exception.PharmacyException;
import com.sinse.xmlappdemo.model.PharmacyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class RestPharmacyController {

    private PharmacyService pharmacyService;

    public RestPharmacyController(PharmacyService pharmacyService) {
        this.pharmacyService = pharmacyService;
    }

    @GetMapping("/pharmacies")
    public List<Pharmacy> getList() {
        try {
            return pharmacyService.parse();
        } catch (Exception e) {
            throw new PharmacyException("parse 실패", e);
        }
    }

    @ExceptionHandler(PharmacyException.class)
    public ResponseEntity<String> handle(PharmacyException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("응답 파싱 실패");
    }
}


