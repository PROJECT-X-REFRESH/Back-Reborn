package com.reborn.back.board.repository;

import com.reborn.back.domain.board.BoardView;
import com.reborn.back.domain.entity.BoardType;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardViewRepository extends JpaRepository<BoardView, Long> {

}