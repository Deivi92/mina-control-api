package com.minacontrol.empleado.repository;

import com.minacontrol.empleado.model.Empleado;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository("empleadoRepository") // Se añade un calificador único
public class InMemoryEmpleadoRepositoryImpl implements EmpleadoRepository {

    private final Map<Long, Empleado> empleados = new ConcurrentHashMap<>();
    private final AtomicLong counter = new AtomicLong();

    @Override
    public Empleado save(Empleado empleado) {
        if (empleado.getId() == null) {
            empleado.setId(counter.incrementAndGet());
        }
        empleados.put(empleado.getId(), empleado);
        return empleado;
    }

    @Override
    public Optional<Empleado> findById(Long id) {
        return Optional.ofNullable(empleados.get(id));
    }

    @Override
    public List<Empleado> findAll() {
        return new ArrayList<>(empleados.values());
    }

    @Override
    public void deleteById(Long id) {
        empleados.remove(id);
    }
}
