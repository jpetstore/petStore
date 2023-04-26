package org.csu.mypetstore.controller;

import com.alibaba.fastjson.JSON;
import org.csu.mypetstore.domain.*;
import org.csu.mypetstore.service.CatalogService;
import org.csu.mypetstore.service.InventoryService;
import org.csu.mypetstore.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/catalog")
@SessionAttributes("allCategoryList")
public class CatalogController {
    public static byte[] BlobToBytes(Blob blob){
        BufferedInputStream bis = null;

        try {
            bis = new BufferedInputStream(blob.getBinaryStream());
            byte[] bytes = new byte[(int) blob.length()];
            int len = bytes.length;
            int offset = 0;
            int read = 0;
            while(offset < len && (read = bis.read(bytes, offset, len - offset)) > 0){
                offset += read;
            }
            return bytes;
        } catch (SQLException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } finally {
            if(bis != null){
                try {
                    bis.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    @Autowired
    private CatalogService catalogService;
@Autowired
private ReviewService reviewService;
@Autowired
private InventoryService inventoryService;

    @RequestMapping(value = "/showImage.do")
    public  void showImage(String productId, HttpServletResponse response, HttpServletRequest request) throws IOException, SQLException {
        byte[] bb=inventoryService.getimage(productId);
            //从数据库读取出二进制数据……
//         byte[] bb= BlobToBytes(data);
            // 将图像输出到Servlet输出流中。
            ServletOutputStream sos = response.getOutputStream();
            sos.write(bb, 0, bb.length);
            sos.close();
        // 禁止图像缓存。
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/gif/png");
    }


    @GetMapping ("/review")
    public String review(HttpServletRequest request, Review review, Model model){
        boolean result=reviewService.insertReview(review);//插入评论的数据库

        return "/catalog/Main";
    }
    @GetMapping ("/main")
    public String loginForm(Model model){
        List<Category> allCategoryList = catalogService.getAllCategoryList();
        model.addAttribute("allCategoryList",allCategoryList);

        return "/catalog/Main";
    }

    @GetMapping ("/viewCategory")
    public String viewCatalog(Category category, Model model){
        String categoryId=category.getCategoryId();
        Category categoryByCategoryId=catalogService.getCategory(categoryId);
        List<Product> productList=catalogService.getProductListByCategory(categoryId);

        model.addAttribute("productList",productList);
        model.addAttribute("category",categoryByCategoryId);
        return "catalog/Category";
    }
    @GetMapping ("/ReviewForm")
    public String reviewForm(Item item_post,Product product, Model model){
        Item item=catalogService.getItem(item_post.getItemId());
        model.addAttribute("product",product);
        //库存量
        int item1=catalogService.getInventoryQuantity(item.getItemId());
        List<Review> reviewList =reviewService.getCategoryList(item_post.getItemId());

        model.addAttribute("reviewList",reviewList);//查找相应的评论
        model.addAttribute("item",item);
        model.addAttribute("item1",item1);
        return "catalog/Review";
    }

    @GetMapping("/viewProduct")
    public String viewProduct(Product product, Model model){
        List<Item> itemList=catalogService.getItemListByProduct(product.getProductId());
        model.addAttribute("product",product);
        model.addAttribute("itemList",itemList);
        return "/catalog/Product";
    }

    @GetMapping("/viewItem")
    public String viewItem(Item item_post,Product product,Model model){
        Item item=catalogService.getItem(item_post.getItemId());
        //库存量
        int item1=catalogService.getInventoryQuantity(item.getItemId());
        List<Review> reviewList =reviewService.getCategoryList(item_post.getItemId());
        model.addAttribute("reviewList",reviewList);//查找相应的评论

        model.addAttribute("item",item);
        model.addAttribute("item1",item1);
        model.addAttribute("product",product);
        return "/catalog/Item";
    }

    @PostMapping("/search")
    public String searchProduct(String keyword,Model model){
        List<Product> productList=new ArrayList<Product>();
        productList=catalogService.searchProductList(keyword);

        model.addAttribute("productList",productList);
        return "/catalog/SearchProduct";
    }

    @GetMapping("/searchThis")
    public void searchAutoComplete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String keyword;
        keyword=request.getParameter("keyword");
        Product product=new Product();
        Item item=new Item();
        List<Product> productList=new ArrayList<Product>();
        productList=catalogService.searchProductList(keyword);

        StringBuffer sb = new StringBuffer("[");

        for(int i=0;i<productList.size();i++){
            if(i== productList.size()-1) {
                sb.append("\"" + productList.get(i).getName() + "\"]");
            }else{
                sb.append("\"" + productList.get(i).getName() + "\",");
            }
        }
        response.getWriter().write(sb.toString());
    }

    @GetMapping("/fwItem")
    public void floatingWindow(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String itemId;
        itemId=request.getParameter("itemId");

        Item item=catalogService.getItem(itemId);
        String jsonstr = JSON.toJSONString(item);
        response.getWriter().write(jsonstr);

    }

}
