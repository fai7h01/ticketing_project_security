package com.cydeo.entity;

import com.cydeo.entity.common.UserPrincipal;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class BaseEntityListener extends AuditingEntityListener {

    private void onPrePersist(BaseEntity baseEntity){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        baseEntity.setInsertDateTime(LocalDateTime.now());
        baseEntity.setLastUpdateDateTime(LocalDateTime.now());

        if (authentication != null && !authentication.getName().equals("anonymousUser")){
            Object principal = authentication.getPrincipal();
            baseEntity.setInsertUserId(((UserPrincipal) principal).getId());
            baseEntity.setLastUpdateUserId(((UserPrincipal) principal).getId());
        }
    }

    private void onPreUpdate(BaseEntity baseEntity){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        baseEntity.setLastUpdateDateTime(LocalDateTime.now());

        if(authentication != null && !authentication.getName().equals("anonymousUser")){
            Object principal = authentication.getPrincipal();
            baseEntity.setLastUpdateUserId( ((UserPrincipal) principal).getId());
        }
    }

}
