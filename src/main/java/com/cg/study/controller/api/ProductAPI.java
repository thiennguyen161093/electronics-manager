package com.cg.study.controller.api;

import com.cg.study.model.Customer;
import com.cg.study.model.Product;
import com.cg.study.service.customer.ICustomerService;
import com.cg.study.service.product.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductAPI {
    @Autowired
    private ICustomerService customerService;

    @Autowired
    private IProductService productService;

    @GetMapping
    public ResponseEntity<Iterable<Product>> allProducts(@RequestParam("search") Optional<String> serialNumber) {
        Iterable<Product> products;
        if (serialNumber.isPresent()){
            products = productService.findProductBySerialNumber(serialNumber.get());
        } else {
            products = productService.findAll();
            if (((List) products).isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }
        return new ResponseEntity<>(products,HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Iterable<Product>> findAllProductsByCustomerId(@PathVariable Long id) {
        Iterable<Product> products = productService.findAllByCustomerId(id);
        if (((List) products).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/getProducts/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.findById(id);
        if (product.isPresent()) {
            return new ResponseEntity<>(product.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Product> saveProduct(@RequestBody Product product) {
        if (product.getId() != null) {
            return new ResponseEntity<>(productService.save(product), HttpStatus.OK);
        }

        Optional<Customer> customer = customerService.findById(product.getCustomer().getId());

        if (customer.isPresent()) {
            product.setCustomer(customer.get());
            product.setPurchaseDay(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            return new ResponseEntity<>(productService.save(product), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Product> deleteProvince(@PathVariable Long id) {
        Optional<Product> productOptional = productService.findById(id);
        if (!productOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        productService.remove(id);
        return new ResponseEntity<>(productOptional.get(), HttpStatus.NO_CONTENT);
    }

//    @GetMapping
//    public ResponseEntity<Product> getProductBySerialNumber(@RequestParam("search") String serialNumber){
//        Optional<Product> product = productService.findBySerialNumber(serialNumber);
//        if(product.isPresent()){
//            return new ResponseEntity<>(product.get(),HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
}
