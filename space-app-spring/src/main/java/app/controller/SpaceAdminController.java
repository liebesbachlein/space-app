package app.controller;

import app.dto.request.CreateSpaceRequest;
import app.dto.request.EditSpaceRequest;
import app.dto.response.*;
import app.service.SpaceService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@Validated
@Valid
public class SpaceAdminController {
    private final SpaceService spaceService;

    @Autowired
    public SpaceAdminController(SpaceService spaceService) {
        this.spaceService = spaceService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/spaces")
    @ResponseStatus(HttpStatus.CREATED)
    public void createSpace(
            @RequestBody @Valid CreateSpaceRequest request) {
        spaceService.create(
                request.getName(),
                request.getPrice(),
                request.getTypeId()
        );
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/space/form")
    public CreateSpaceFormResponse createSpaceForm() {
        var list = spaceService.getAllTypes().stream()
                .map(CreateSpaceFormResponse.SpaceType::mapFromSpaceTypeEntity)
                .toList();
        return new CreateSpaceFormResponse(list);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/space/{id}")
    public void deleteSpace(
            @PathVariable("id")
            @NotNull(message = "Space id must be provided")
            @Min(value = 1, message = "Space id must be positive non-zero integer")
            int id) {
        spaceService.delete(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/space/{id}")
    public void createSpace(
            @PathVariable("id")
            @NotNull(message = "Space id must be provided")
            @Min(value = 1, message = "Space id must be positive non-zero integer")
            int id,
            @RequestBody @Valid EditSpaceRequest request
    ) {
        spaceService.edit(
                id,
                request.getName(),
                request.getPrice(),
                request.getTypeId()
        );
    }
}
