package com.example.demo.Controller;


import com.example.demo.Service.ProductosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin (origins = "http://localhost:3000")
@CrossOrigin(
        origins = "*",
        methods = {RequestMethod.GET, RequestMethod.HEAD, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
        allowedHeaders = "*")
@RestController
public class ProductosController {
    @Autowired
    ProductosService productosService;
    @RequestMapping(value="/items/search/", method=RequestMethod.GET)
    public String productos(@RequestHeader("search") String search) throws Exception {
        return productosService.getProductos(search);
    }

    @RequestMapping(value="/item/{search}", method=RequestMethod.GET)
    public String details(@RequestHeader("search") String search) throws Exception {
        return productosService.getDetails(search);
    }

    @RequestMapping(value="/items/{search}/description", method=RequestMethod.GET)
    public String description(@RequestHeader("search") String search) throws Exception {
        return productosService.getDescription(search);
    }
}