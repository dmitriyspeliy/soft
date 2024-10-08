package com.comfortablesoft.test.task.controller;

import com.comfortablesoft.test.task.service.MainService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Task controller")
public class MainController {

    private final MainService mainService;

    @Operation(summary = "Find the n-th largest value")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Get value",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = String.class)))}),
            @ApiResponse(
                    responseCode = "400",
                    description = "Значение N больше, чем строк. Неправильный формат чисел. Отсутствует файл",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = IllegalArgumentException.class)))}),
            @ApiResponse(
                    responseCode = "5XX",
                    description = "Не удалось получить ответ. Причина в сообщении",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = IllegalArgumentException.class)))})})
    @GetMapping
    public Integer getNMax(@RequestParam(name = "n", required = false, defaultValue = "50") String n,
                           @RequestParam(name = "path", required = false,
                                   defaultValue = "src/main/resources/test.xlsx") String path) {
        return mainService.findNum(n, path);
    }

}
