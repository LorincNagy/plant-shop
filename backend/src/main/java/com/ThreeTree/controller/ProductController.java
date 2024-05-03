package com.ThreeTree.controller;

import com.ThreeTree.dto.NewProductResponse;
import com.ThreeTree.model.Product;
import com.ThreeTree.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> missingProduct(NoSuchElementException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

//    @GetMapping
//    public List<NewProductResponse> getProductsInRange(@RequestParam int start, @RequestParam int end) {
//        return productService.findProductsInRange(start + 1, end);
//    }

    @GetMapping
    public List<NewProductResponse> getAllProducts() {
        return productService.getProducts();
    }

    @GetMapping("/allProductsSize")
    public int getAllProductsSize() {
        return productService.getProducts().size();
    }

    @GetMapping("/{productId}")
    public ResponseEntity<NewProductResponse> getProductById(@PathVariable("productId") Long id) {
        NewProductResponse product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping("/products")
    public void addProduct(@RequestBody Product product) {
        productService.saveProduct(product);
    }

}


//    A Spring keretrendszer megkülönbözteti a Path Variable és a Request Parameter közötti különbséget az URL szerkezetének elemzése alapján.
//
//        Path Variable: Ha az URL-ben a változó az útvonal része, és az URL útvonalát számos elem és szöveg után követi a változó, akkor az egy Path Variable. Például: /api/products/123, ahol a 123 az productId lesz.
//        Request Parameter: Ha az URL-ben a változók kulcs-érték párokként vannak megadva a ? után, akkor azok Request Parameterek. Például: /api/products?start=1&end=10, ahol a start és az end a keresési paraméterek lesznek.
//        A Spring figyel az URL struktúrájára és automatikusan felismeri, hogy melyik része a Path Variable és melyik a Request Parameter. Ha a Spring vezérlője Path Variable-t vár, de a kérésben Request Parameter van megadva, akkor hibát fog dobni, mivel nem találja meg a megfelelő útvonalat. Ha a vezérlő a Request Parameter-t várja, de a kérésben Path Variable van megadva, akkor a Spring nem fogja tudni értelmezni az értéket.
//
//        Fontos megjegyezni, hogy a nevek (@PathVariable, @RequestParam) csak az annotációk nevei, amelyeket a Spring konténer használ a megfelelő értékek azonosításához. A neveket nem muszáj ugyanolyannak vagy különbözőnek is megadni a vezérlő metódus paramétereinek nevével. Az annotáció neve alapján a Spring tudja, hogy hol keresse az értéket az URL-ben.
