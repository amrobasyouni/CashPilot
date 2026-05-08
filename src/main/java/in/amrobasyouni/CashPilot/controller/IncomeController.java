package in.amrobasyouni.CashPilot.controller;

import in.amrobasyouni.CashPilot.dto.ExpenseDTO;
import in.amrobasyouni.CashPilot.dto.IncomeDTO;
import in.amrobasyouni.CashPilot.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/incomes")
public class IncomeController {
    private final IncomeService incomeService;

    @PostMapping
    public ResponseEntity<IncomeDTO> addIncome(@RequestBody IncomeDTO dto){
        IncomeDTO newIncome = incomeService.addIncome(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newIncome);
    }

    @GetMapping
    public ResponseEntity<List<IncomeDTO>> getCurrentProfileExpensesForCurrentMonth(){
        List<IncomeDTO> incomes = incomeService.getCurrentMonthIncomes();
        return ResponseEntity.ok(incomes);
    }

    @DeleteMapping("/{incomeId}")
    public ResponseEntity<String> deleteExpenseById(@PathVariable Long incomeId){
        incomeService.deleteIncome(incomeId);
        return ResponseEntity.ok("Income Deleted");
    }
}
