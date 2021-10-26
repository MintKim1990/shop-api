package portfolio.shopapi.entity;


import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseEntity extends BaseTimeEntity {

    // Bean으로 등록한 AuditorAware 클래스에서 등록이 일어날때마다 사용자 ID정보를 받아와서 INSERT
    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    // Bean으로 등록한 AuditorAware 클래스에서 등록이 일어날때마다 사용자 ID정보를 받아와서 INSERT
    @LastModifiedBy
    private String lastModifiedBy;

}
