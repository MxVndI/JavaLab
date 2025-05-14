package ru.practicum;

import java.util.*;

public class SportStore {
    public static void main(String[] args) {
        List<Product> products = Arrays.asList(
                new Product("Мяч", "Nike", 5, "Красный", 25.99),
                new Product("Ракетка", "Adidas", null, "Синий", 89.99),
                new Product("Кроссовки", "Nike", 42, "Черный", 120.50),
                new Product("Мяч", "Adidas", 4, "Синий", 22.50),
                new Product("Спортивная одежда", "Puma", null, "Черный", 75.00)
        );

        Map<String, List<Product>> groupedProducts = ProductService.groupByType(products);

        // Сортировка и вывод по типам
        for (Map.Entry<String, List<Product>> entry : groupedProducts.entrySet()) {
            String type = entry.getKey();
            List<Product> productList = entry.getValue();

            List<Product> sortedByPrice = ProductService.sortByPrice(productList);
            List<Product> sortedBySize = ProductService.sortBySize(productList);

            System.out.println("\nТип: " + type);
            System.out.println("Сортировка по цене: " + sortedByPrice);
            System.out.println("Сортировка по размеру: " + sortedBySize);
        }

        // Использование универсального фильтра
        List<Product> nikeBlackProducts = ProductService.filterByPredicate(
                products,
                p -> p.getBrand().equals("Nike") && p.getColor().equals("Черный")
        );
        System.out.println("\nТовары Nike черного цвета: " + nikeBlackProducts);

        // Поиск самых дорогих и дешевых товаров
        for (Map.Entry<String, List<Product>> entry : groupedProducts.entrySet()) {
            String type = entry.getKey();
            Optional<Product> maxProduct = ProductService.findMaxPrice(entry.getValue());
            Optional<Product> minProduct = ProductService.findMinPrice(entry.getValue());

            System.out.printf("\nТип: %s%nСамый дорогой: %s%nСамый дешевый: %s%n",
                    type,
                    maxProduct.map(Object::toString).orElse("null"),
                    minProduct.map(Object::toString).orElse("null"));
        }
    }
}