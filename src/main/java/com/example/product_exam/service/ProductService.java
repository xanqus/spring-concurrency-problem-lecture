package com.example.product_exam.service;

import com.example.product_exam.dao.ProductRepository;
import com.example.product_exam.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public synchronized void decrease(Long id, Long quantity) {
        Product product = productRepository.findById(id).orElseThrow();

        product.decrease(quantity);

        productRepository.saveAndFlush(product);
    }



}
