package ru.practicum;

import java.util.*;
import java.util.stream.Collectors;

public class ProductService {

    public static Map<String, List<Product>> groupByType(List<Product> products) {
        return products.stream()
                .collect(Collectors.groupingBy(Product::getType));
    }

    public static List<Product> sortByPrice(List<Product> products) {
        return products.stream()
                .sorted(Comparator.comparing(Product::getPrice))
                .collect(Collectors.toList());
    }

    public static List<Product> sortBySize(List<Product> products) {
        return products.stream()
                .sorted(Comparator.comparing(
                        Product::getSize,
                        Comparator.nullsLast(Comparator.naturalOrder())
                ))
                .collect(Collectors.toList());
    }

    public static List<Product> filterNikeBlack(List<Product> products) {
        ProductPredicate filter = p ->
                p.getBrand().equals("Nike") && p.getColor().equals("Черный");
        return products.stream()
                .filter(filter::test)
                .collect(Collectors.toList());
    }

    public static Optional<Product> findMaxPrice(List<Product> products) {
        return products.stream()
                .max(Comparator.comparing(Product::getPrice));
    }

    public static Optional<Product> findMinPrice(List<Product> products) {
        return products.stream()
                .min(Comparator.comparing(Product::getPrice));
    }
}