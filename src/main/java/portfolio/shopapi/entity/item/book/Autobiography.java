package portfolio.shopapi.entity.item.book;

import lombok.Getter;
import portfolio.shopapi.entity.item.Item;

import javax.persistence.Entity;

@Entity
@Getter
public class Autobiography extends Item {

    private String auther;
    private String isbn;

    public Autobiography(String name, int price, int stockQuantity, String auther, String isbn) {
        super(name, price, stockQuantity);
        setAuther(auther);
        setIsbn(isbn);
    }

    private void setAuther(String auther) {
        this.auther = auther;
    }

    private void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
