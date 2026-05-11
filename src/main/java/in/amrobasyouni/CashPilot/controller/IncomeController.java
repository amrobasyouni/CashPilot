package in.amrobasyouni.CashPilot.controller;

import in.amrobasyouni.CashPilot.dto.ExpenseDTO;
import in.amrobasyouni.CashPilot.dto.IncomeDTO;
import in.amrobasyouni.CashPilot.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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
    public ResponseEntity<List<IncomeDTO>> getCurrentProfileIncomesForCurrentMonth(){
        List<IncomeDTO> incomes = incomeService.getCurrentMonthIncomes();
        return ResponseEntity.ok(incomes);
    }

    @DeleteMapping("/{incomeId}")
    public ResponseEntity<String> deleteIncomeById(@PathVariable Long incomeId){
        incomeService.deleteIncome(incomeId);
        return ResponseEntity.ok("Income Deleted");
    }

    @GetMapping("/top")
    public ResponseEntity<List<IncomeDTO>>get5LatestIncomes(){
        List<IncomeDTO> latest = incomeService.getLatest5IncomesForCurrentUser();
        return ResponseEntity.ok(latest);

    }
    @GetMapping("/sum")
    public ResponseEntity<BigDecimal> totalIncomes(){
        return  ResponseEntity.ok(incomeService.totalIncomes());
    }
}
