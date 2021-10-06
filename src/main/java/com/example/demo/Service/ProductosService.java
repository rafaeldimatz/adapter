package com.example.demo.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.HashMap;

@Service
public class ProductosService {
    @Value("${urlProducts}")
    String urlProducts;
    @Value("${urlDetails}")
    String urlDetails;
    @Value("${urlDescription}")
    String urlDescription;
    public String getProductos(String search) throws Exception {

        //return "Obtener Productos";
       com.example.demo.utils.HTTPConnection getProducto = new com.example.demo.utils.HTTPConnection();
       String resp = getProducto.executeRequest("GET",urlProducts,search,null,new HashMap<>(),"results");
       return resp;
    }

    public String getDetails(String search) throws Exception {

        //return "Obtener Productos";
        com.example.demo.utils.HTTPConnection getDetails = new com.example.demo.utils.HTTPConnection();
        String resp = getDetails.executeRequest("GET",urlDetails,search,null,new HashMap<>(),"details");
        return resp;
    }

    public String getDescription(String search) throws Exception {

        //return "Obtener Productos";
        com.example.demo.utils.HTTPConnection getDetails = new com.example.demo.utils.HTTPConnection();
        String urlDesc = urlDescription + search + "/description";
        String resp = getDetails.executeRequest("GET",urlDesc,"",null,new HashMap<>(),"description");
        return resp;
    }
}
