package com.ws181.gepollo_rapsing.ecommerceapi.dto;

public record ProductListingEntry(
    Long prodId,
    String prodName,
    Double prodPrice
) {}