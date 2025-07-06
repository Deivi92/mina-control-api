
package com.minacontrol.turnos.repository;

import com.minacontrol.turnos.entity.TipoTurno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TipoTurnoRepository extends JpaRepository<TipoTurno, Long> {
    Optional<TipoTurno> findByNombre(String nombre);
    List<TipoTurno> findByActivo(boolean activo);
    Optional<TipoTurno> findByIdAndActivo(Long id, boolean activo);
}
