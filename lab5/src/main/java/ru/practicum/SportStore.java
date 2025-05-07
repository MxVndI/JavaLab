package ru.practicum;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class SportStore {
    public static void main(String[] args) {
        List<Product> products = Arrays.asList(
                new Product("Мяч", "Nike", 5, "Красный", 25.99),
                new Product("Ракетка", "Adidas", null, "Синий", 89.99),
                new Product("Кроссовки", "Nike", 42, "Черный", 120.50),
                new Product("Мяч", "Adidas", 4, "Синий", 22.50),
                new Product("Спортивная одежда", "Puma", null, "Черный", 75.00)
        );

        Map<String, List<Product>> productsByType = products.stream()
                .collect(Collectors.groupingBy(Product::getType));

        productsByType.forEach((type, productList) -> {
            System.out.println("\nТип: " + type);

            List<Product> sortedByPrice = productList.stream()
                    .sorted(Comparator.comparing(Product::getPrice))
                    .collect(Collectors.toList());
            System.out.println("Сортировка по цене: " + sortedByPrice);

            List<Product> sortedBySize = productList.stream()
                    .sorted(Comparator.comparing(
                            Product::getSize,
                            Comparator.nullsLast(Comparator.naturalOrder())
                    ))
                    .collect(Collectors.toList());
            System.out.println("Сортировка по размеру: " + sortedBySize);
        });

        ProductPredicate filter = p ->
                p.getBrand().equals("Nike") && p.getColor().equals("Черный");
        List<Product> filteredProducts = products.stream()
                .filter(filter::test)
                .collect(Collectors.toList());
        System.out.println("\nТовары Nike черного цвета: " + filteredProducts);

        productsByType.forEach((type, productList) -> {
            Optional<Product> maxProduct = productList.stream()
                    .max(Comparator.comparing(Product::getPrice));
            Optional<Product> minProduct = productList.stream()
                    .min(Comparator.comparing(Product::getPrice));

            System.out.printf("\nТип: %s%nСамый дорогой: %s%nСамый дешевый: %s%n",
                    type,
                    maxProduct.orElse(null),
                    minProduct.orElse(null));
        });
    }
}