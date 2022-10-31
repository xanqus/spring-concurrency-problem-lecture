package com.example.product_exam;

import com.example.product_exam.dao.ProductRepository;
import com.example.product_exam.domain.Product;
import com.example.product_exam.service.ProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class ProductExamApplicationTests {
	@Autowired
	private ProductService productService;

	@Autowired
	private ProductRepository productRepository;


	@BeforeEach
	public void before() {
		Product product = new Product(1L, 100L);

		productRepository.saveAndFlush(product);
	}

	@AfterEach
	public void after() {
		productRepository.deleteAll();
	}


	@Test
	void test1() {
		for(int i = 0; i < 100; i++) {
			productService.decrease(1L, 1L);
		}
		Product product = productRepository.findById(1L).orElseThrow();
		assertThat(product.getQuantity())
				.isEqualTo(0L);

	}

	@Test
	void test2() throws InterruptedException {
		int threadCount = 100;
		ExecutorService executorService = Executors.newFixedThreadPool(32);
		CountDownLatch latch = new CountDownLatch(threadCount);

		for(int i = 0; i < threadCount; i++) {
			executorService.submit(() -> {
				try {
					productService.decrease(1L, 1L);
				} finally {
					latch.countDown();
				}
			});
		}
		latch.await();

		Product product = productRepository.findById(1L).orElseThrow();

		assertThat(product.getQuantity())
				.isEqualTo(0L);
	}


}
