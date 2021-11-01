package portfolio.shopapi.service.item.clothes;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portfolio.shopapi.entity.item.Item;
import portfolio.shopapi.entity.item.clothes.Clothes;
import portfolio.shopapi.entity.mapping.ItemCategory;
import portfolio.shopapi.repository.item.clothes.ClothesRepository;
import portfolio.shopapi.request.item.StockItemRequest;
import portfolio.shopapi.request.item.clothes.SaveClothesRequest;
import portfolio.shopapi.response.Response;
import portfolio.shopapi.response.item.book.BookResponse;
import portfolio.shopapi.response.item.clothes.ClothesResponse;
import portfolio.shopapi.service.item.ItemService;
import portfolio.shopapi.service.itemcategory.ItemCategoryService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClothesService implements ItemService<SaveClothesRequest, Clothes> {

    private final ItemCategoryService categoryService;
    private final ClothesRepository clothesRepository;

    @Transactional
    @Override
    public Clothes findById(Long id) {
        return clothesRepository.findById(id)
                .orElseThrow(() -> {
                    throw new IllegalStateException("의류가 조회되지 않습니다.");
                });
    }

    /**
     * 의류 저장
     * @param request
     * @return
     */
    @Transactional
    @Override
    public Response saveItem(SaveClothesRequest request) {

        // 카테고리 리스트 생성
        List<ItemCategory> itemCategories = categoryService.createItemCategories(
                request.getCategoryCodes()
        );

        Clothes clothes = Clothes.createClothes(request, itemCategories);

        clothesRepository.save(clothes);

        return new Response<ClothesResponse>(
                ClothesResponse.createClothesResponse(clothes)
        );
    }

    /**
     * Item LOCK 조회
     * @param id
     * @return
     */
    @Override
    public Clothes findWithItemForUpdate(Long id) {
        return clothesRepository.findWithItemForUpdateById(id);
    }

    /**
     * 수량 차감
     * @param request
     * @return
     */
    @Transactional
    @Override
    public Response discountQuantity(StockItemRequest request) {
        Clothes findClothes = clothesRepository.findWithItemForUpdateById(request.getId());
        findClothes.removeStock(request.getStockQuantity());

        return new Response<ClothesResponse>(
                ClothesResponse.createClothesResponse(findClothes)
        );
    }

    /**
     * 수량 증가
     * @param request
     * @return
     */
    @Transactional
    @Override
    public Response addQuantity(StockItemRequest request) {
        Clothes findClothes = clothesRepository.findWithItemForUpdateById(request.getId());
        findClothes.addStock(request.getStockQuantity());

        return new Response<ClothesResponse>(
                ClothesResponse.createClothesResponse(findClothes)
        );
    }


}
