package br.edu.unifip.ecommerceapi.controllers;

import br.edu.unifip.ecommerceapi.dtos.RoleDto;
import br.edu.unifip.ecommerceapi.models.Role;
import br.edu.unifip.ecommerceapi.services.RoleService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/roles")
@CrossOrigin(origins = "*", maxAge = 3600)
public class RoleController {
    final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.status(HttpStatus.OK).body(roleService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getRoleById(@PathVariable(value = "id") UUID id) {
        Optional<Role> roleOptional = roleService.findById(id);
        return roleOptional.<ResponseEntity<Object>>map(role -> ResponseEntity.status(HttpStatus.OK).body(role)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role not found."));
    }

    @PostMapping
    public ResponseEntity<Object> saveRole(@RequestBody @Valid RoleDto roleDto) {
        try {
            var role = new Role();
            BeanUtils.copyProperties(roleDto, role);
            return ResponseEntity.status(HttpStatus.CREATED).body(roleService.save(role));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Role already exists.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> softDeleteProduct(@PathVariable(value = "id") UUID id) {
        Optional<Role> roleOptional = roleService.findById(id);
        if (roleOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role not found.");
        }

        Role instance = roleOptional.get();

        // Verificar se o registro está ativo
        if (!instance.isActive()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Role is not active.");
        }

        roleService.softDelete(instance);
        return ResponseEntity.status(HttpStatus.OK).body("Role deleted successfully.");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateRole(@PathVariable(value = "id") UUID id, @RequestBody Map<Object, Object> objectMap) {
        Optional<Role> roleOptional = roleService.findById(id);
        if (roleOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role not found.");
        }

        Role instance = roleOptional.get();

        // Verificar se o registro está ativo
        if (!instance.isActive()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Role is not active.");
        }

        try {
            roleService.partialUpdate(instance, objectMap);
            return ResponseEntity.status(HttpStatus.OK).body(roleOptional.get());
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Role name already exists.");
        }

    }
}
