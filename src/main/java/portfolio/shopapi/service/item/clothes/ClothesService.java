package portfolio.shopapi.service.item.clothes;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portfolio.shopapi.entity.category.Category;
import portfolio.shopapi.entity.item.Item;
import portfolio.shopapi.entity.item.book.Book;
import portfolio.shopapi.entity.item.clothes.Clothes;
import portfolio.shopapi.entity.mapping.ItemCategory;
import portfolio.shopapi.repository.category.CategoryRepository;
import portfolio.shopapi.repository.item.ItemRepository;
import portfolio.shopapi.request.item.book.SaveBookRequest;
import portfolio.shopapi.request.item.clothes.SaveClothesRequest;
import portfolio.shopapi.service.item.ItemService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClothesService implements ItemService<SaveClothesRequest> {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    @Override
    public Optional<Item> findById(Long id) {
        return itemRepository.findById(id);
    }

    @Transactional
    @Override
    public Long saveItem(SaveClothesRequest request) {

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

        Item item = Clothes.builder()
                .name(request.getName())
                .stockQuantity(request.getStockQuantity())
                .price(request.getPrice())
                .itemCategories(itemCategories)
                .material(request.getMaterial())
                .size(request.getSize())
                .build();

        return itemRepository
                .save(item)
                .getId();
    }

    @Override
    public Item findWithItemForUpdate(Long id) {
        return null;
    }
}
