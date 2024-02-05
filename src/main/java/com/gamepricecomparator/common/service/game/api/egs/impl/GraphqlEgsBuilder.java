package com.gamepricecomparator.common.service.game.api.egs.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GraphqlEgsBuilder {
    private String keywords;
    private int start = 0;
    private int count = 100;
    private String country = "DE";

    public GraphqlEgsBuilder setKeywords(String keywords) {
        this.keywords = keywords;
        return this;
    }

    public GraphqlEgsBuilder setStart(int start) {
        this.start = start;
        return this;
    }

    public GraphqlEgsBuilder setCount(int count) {
        this.count = count;
        return this;
    }

    public GraphqlEgsBuilder setCountry(String country) {
        this.country = country;
        return this;
    }
    public String build() {
        return String.format("{\"query\": \"{" +
                "Catalog {searchStore(keywords: \\\"%s\\\", start: %d, count: %d) {" +
                "paging {count total} elements {" +
                "id keyImages{url} title urlSlug description price (country: \\\"%s\\\"){" +
                "totalPrice {originalPrice discountPrice currencyCode}}}}}}\"}",
                keywords, start, count, country);
    }
}