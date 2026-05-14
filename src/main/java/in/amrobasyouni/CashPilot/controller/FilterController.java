package in.amrobasyouni.CashPilot.controller;

import in.amrobasyouni.CashPilot.dto.FilterDTO;
import in.amrobasyouni.CashPilot.service.ExpenseService;
import in.amrobasyouni.CashPilot.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/filter")
public class FilterController {

    private final ExpenseService expenseService;
    private final IncomeService incomeService;

    @PostMapping
    public ResponseEntity<?> filterTransactions(@RequestBody FilterDTO filterDTO){
        // preparing the data for validation
        LocalDate startDate = filterDTO.getStartDate()!= null ? filterDTO.getStartDate() : LocalDate.of(1900,1,1);
        LocalDate endDate = filterDTO.getEndDate()!= null ? filterDTO.getEndDate(): LocalDate.now();
        String keyword = filterDTO.getKeyword()!= null ? filterDTO.getKeyword() : "";
        String sortField = filterDTO.getSortField()!= null ? filterDTO.getSortField():"date";
        Sort.Direction direction = "desc".equalsIgnoreCase(filterDTO.getSortOrder())? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction,sortField);

        if ("income".equalsIgnoreCase(filterDTO.getType())){
         return ResponseEntity.ok(incomeService.filterIncomes(startDate,endDate,keyword,sort));
        }else if("expense".equalsIgnoreCase(filterDTO.getType())){
            return ResponseEntity.ok(expenseService.filterExpenses(startDate,endDate,keyword,sort));
        }else{
         return ResponseEntity.badRequest().body("Invalid Type");
        }}

    }

