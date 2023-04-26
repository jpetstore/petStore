package org.csu.mypetstore.domain;

import lombok.Data;

@Data
public class ImageOfProduct
{
    private String productId;
    private byte[] image;
}
