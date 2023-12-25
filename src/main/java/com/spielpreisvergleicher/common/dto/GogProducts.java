package com.spielpreisvergleicher.common.dto;

public record GogProducts(
        Price price,
        Integer id,
        String title,
        String url,
        Platforms worksOn
) {
    public record Price(
            Double baseAmount,
            Double finalAmount,
            Integer discountPercentage,
            String currency,
            Boolean isFree
    ) {
    }

    public record Platforms(
            String Windows,
            String Mac,
            String Linux
    ) {
    }
}
