package com.sinse.xmlapp.controller;

import com.sinse.xmlapp.domain.Item;
import com.sinse.xmlapp.model.bus.BusService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RestBusController {
    private BusService busService;

    public RestBusController(BusService busService) {
        this.busService = busService;
    }

    //@GetMapping(value= "/buses", produces = MediaType.APPLICATION_XML_VALUE)
    @GetMapping(value= "/buses")
    public List<Item> getList() throws Exception{
        return busService.parse();
    }
}
