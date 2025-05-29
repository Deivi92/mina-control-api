package com.minacontrol.empleado.repository;

import com.minacontrol.empleado.model.Empleado;
import java.util.List;
import java.util.Optional;

public interface EmpleadoRepository {
    Empleado save(Empleado empleado);
    Optional<Empleado> findById(Long id);
    List<Empleado> findAll();
    void deleteById(Long id);
}
