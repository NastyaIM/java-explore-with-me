package ru.practicum.compilations.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.compilations.model.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    @Query(value = "select c from Compilation as c " +
            "where :pinned is null or c.pinned = :pinned")
    Page<Compilation> findAll(@Param("pinned") Boolean pinned, Pageable pageable);
}