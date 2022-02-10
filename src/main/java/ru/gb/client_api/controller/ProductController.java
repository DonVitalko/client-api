package ru.gb.client_api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.gb.api.product.api.ProductGateway;
import ru.gb.api.product.dto.ProductDto;


import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductGateway productGateway;

    @GetMapping
    public String showForm(Model model, @RequestParam(name = "id", required = false) Long id){
        ProductDto productDto;
        if (id != null) {
            productDto = productGateway.getProduct(id).getBody();
        } else {
            productDto = ProductDto.builder().build();
        }
        model.addAttribute("product", productDto);
        return "product-form";
    }


    @GetMapping("/all")
    public String getProductList(Model model){
        model.addAttribute("products", productGateway.getProductList());
        return "product-list";
    }

    @GetMapping("/{productId}")
    public String info(Model model, @PathVariable("productId") Long id) {
        ProductDto productDto;
        if (id != null) {
            productDto = productGateway.getProduct(id).getBody();
        } else {
            return "redirect:/product/all";
        }
        model.addAttribute("product", productDto);
        return "product-info";
    }

    @PostMapping
    public String saveProduct(ProductDto productDto) {
        productDto.setManufactureDate(LocalDate.now());
        productGateway.handlePost(productDto);
        return "redirect:/product/all";
    }

    @GetMapping("/delete")
    public String deleteById(@RequestParam(name = "id") Long id) {
        productGateway.deleteById(id);
        return "redirect:/product/all";
    }

}
