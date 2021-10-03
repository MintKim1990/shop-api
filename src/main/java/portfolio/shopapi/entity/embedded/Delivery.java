package portfolio.shopapi.entity.embedded;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import portfolio.shopapi.entity.constant.DeliveryStatus;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
@Getter
@AllArgsConstructor
public class Delivery {

    @Embedded
    private Address deliveryAddress;

    /*
        Enumerated Default 타입 : EnumType.ORDINAL
        ORDINAL 일경우 DB에 숫자타입으로 저장되기때문에 나중에 enum 이 변경될경우 숫자가 변경되어 에러발생
        STRING 일경우에 DB에 문자타입으로 저장되기때문에 나중에 enum 이 변경되어도 안전
     */
    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

}
