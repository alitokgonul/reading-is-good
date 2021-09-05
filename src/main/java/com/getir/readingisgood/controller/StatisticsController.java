package com.getir.readingisgood.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.getir.readingisgood.controller.model.MonthlyOrderDTO;
import com.getir.readingisgood.service.StatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/statistics")
@Api(tags = { "Statistics" })
@SwaggerDefinition(tags = { @Tag(name = "Statistics", description = "Statistics management") })
public class StatisticsController {

    private final StatisticsService statisticsService;

    @ApiOperation(value = "List customer monthly order")
    @ApiResponses(
        value = { @ApiResponse(code = 200, message = "Monthly orders of the  customer are successfully retrieved."),
                  @ApiResponse(code = 400, message = "Please check your request"),
                  @ApiResponse(code = 401, message = "Authorization error"),
                  @ApiResponse(code = 500, message = "Unexpected server error") })
    @GetMapping("/customer-monthly-order")
    public ResponseEntity<List<MonthlyOrderDTO>> listMonthlyOrder(HttpServletRequest req) {

        return ResponseEntity.ok(statisticsService.listMonthlyOrder(req));
    }
}
