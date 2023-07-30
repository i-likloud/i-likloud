package com.backend.domain.store.repository;
import com.backend.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {

    Store findByStoreId(Long storeId);
}
