package com.github.blazeblack2reduxwikiapi.repository.moves;

import com.github.blazeblack2reduxwikiapi.model.moves.Move;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface MoveRepository extends JpaRepository<Move, Long> {
    Move findByName(@RequestParam("name") String name);
    Move findByIdentifier(@RequestParam("identifier") String identifier);
    @Query("select m from Move m where m.machine like 'TM%'")
    List<Move> findAllTMs();
    @Query("select m from Move m where m.machine like 'HM%'")
    List<Move> findAllHMs();
    @Query("select m from Move m where m.name like %:query% and m.type.name like %:type% order by m.name")
    Page<Move> findMoves(String query, String type, Pageable pageable);
}
