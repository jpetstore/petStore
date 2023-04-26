package org.csu.mypetstore.persistence;

import org.csu.mypetstore.domain.ImageOfProduct;
import org.springframework.stereotype.Repository;

import java.awt.*;
import java.sql.Blob;

@Repository
public interface ImageMapper {
ImageOfProduct getImage(String productId);
}
