package portfolio.shopapi.service.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portfolio.shopapi.entity.category.Category;
import portfolio.shopapi.entity.item.Item;
import portfolio.shopapi.entity.item.book.Autobiography;
import portfolio.shopapi.entity.mapping.ItemCategory;
import portfolio.shopapi.repository.category.CategoryRepository;
import portfolio.shopapi.repository.item.ItemRepository;
import portfolio.shopapi.request.item.SaveAutobiographyRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public Optional<Item> findById(Long id) {
        return itemRepository.findById(id);
    }

    /**
     * 자서전 저장
     * @param request
     * @return
     */
    @Transactional
    public Long saveAutobiography(SaveAutobiographyRequest request) {

        List<ItemCategory> itemCategories = new ArrayList<>();

        // Item 을 생성하여 처리하는동안 Category 가 변경될경우 데이터 정합성에 문제가 생길수있어 비관적 Lock 처리
        request.getCategoryCodes().stream()
                .forEach(code -> {

                    Optional<Category> category = categoryRepository.findWithCategoryForUpdate(code);

                    if(category.isPresent()) {
                        itemCategories.add(
                                ItemCategory.createItemCategory(
                                        category.get()
                                )
                        );
                    } else {
                        throw new RuntimeException("유효하지 않은 카테고리 코드입니다.");
                    }
                });

        Item item = Autobiography.builder()
                .name(request.getName())
                .stockQuantity(request.getStockQuantity())
                .price(request.getPrice())
                .itemCategories(itemCategories)
                .auther(request.getAuther())
                .isbn(request.getIsbn())
                .build();

        return itemRepository
                .save(item)
                .getId();

    }

    /**
     * Item LOCK 조회
     * @param id
     * @return
     */
    public Item findWithItemForUpdate (Long id) {
        return itemRepository.findWithItemForUpdate(id);
    }

    /**
     * 수량차감 LOCK 테스트용
     * @param id
     * @param quantity
     * @return
     */
    @Transactional
    public Integer discountQuantity(Long id, int quantity) {

        Item findItem = itemRepository.findWithItemForUpdate(id);

        if(Objects.nonNull(findItem)) {
            findItem.removeStock(quantity);
            return findItem.getStockQuantity();
        }

        return null;
    }

}
