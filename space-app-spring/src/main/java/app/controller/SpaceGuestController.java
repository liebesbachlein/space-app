package app.controller;

import app.dto.request.CreateSpaceRequest;
import app.dto.request.EditSpaceRequest;
import app.dto.response.CreateSpaceFormResponse;
import app.dto.response.GetSpaceResponse;
import app.dto.response.GetSpacesResponse;
import app.service.SpaceService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Validated
@Valid
public class SpaceGuestController {
    private final SpaceService spaceService;

    @Autowired
    public SpaceGuestController(SpaceService spaceService) {
        this.spaceService = spaceService;
    }

    @GetMapping("/spaces")
    public GetSpacesResponse getSpaces() {
        return new GetSpacesResponse(spaceService.getAll().stream()
                .map(GetSpacesResponse.Space::mapFromSpaceEntity)
                .toList(),
                spaceService.getAllTypes().stream()
                        .map(GetSpacesResponse.SpaceType::mapFromSpaceTypeEntity
                        ).toList());
    }

    @GetMapping("/space/{id}")
    public GetSpaceResponse getSpace(
            @PathVariable("id")
            @NotNull(message = "Space id must be provided")
            @Min(value = 1, message = "Space id must be positive non-zero integer")
            int id
    ) {
        return new GetSpaceResponse(
                GetSpaceResponse.Space.mapFromSpaceEntity(spaceService.getById(id)),
                spaceService.getAllTypes().stream()
                        .map(GetSpaceResponse.SpaceType::mapFromSpaceTypeEntity
                ).toList());
    }
}
