package com.codemagic.catalog.admin.infrastructure.category.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<CategoryJpaEntity, String> {

   @Query("select c.id from Category c where c.id in :ids")
   List<String> existsByIds(@Param("ids") final List<String> ids);

   Page<CategoryJpaEntity> findAll(Specification<CategoryJpaEntity> where, Pageable page);
}
