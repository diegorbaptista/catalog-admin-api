package com.codemagic.catalog.admin.infrastructure.video.persistence;

import com.codemagic.catalog.admin.domain.video.VideoPreview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface VideoRepository extends JpaRepository<VideoJpaEntity, String> {

    @Query("""
            select distinct new com.codemagic.catalog.admin.domain.video.VideoPreview(
            	v.id as id,
            	v.title as title,
            	v.description as description,
            	v.createdAt as createdAt,
            	v.updatedAt as updatedAt
            ) from Video v
                left join v.categories categories
                left join v.genres genres
                left join v.members members
            where
                ( :terms is null or UPPER(v.title) like :terms ) and
                ( :members is  null or members.id.castMemberId in :members ) and
            	( :categories is null or categories.id.categoryId in :categories ) and
            	( :genres is null or genres.id.genreId in :genres )
            """)
    Page<VideoPreview> findAll(
            @Param("terms") String terms,
            @Param("members") Set<String> members,
            @Param("categories") Set<String> categories,
            @Param("genres") Set<String> genres,
            Pageable page
    );

}
