package in.amrobasyouni.CashPilot.controller;

import in.amrobasyouni.CashPilot.dto.ExpenseDTO;
import in.amrobasyouni.CashPilot.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/expenses")
public class ExpenseController {

        private final ExpenseService expenseService;

        @PostMapping
        public ResponseEntity<ExpenseDTO> addExpense(@RequestBody ExpenseDTO dto){
            ExpenseDTO newExpense = expenseService.addExpense(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(newExpense);
        }

        @GetMapping
    public ResponseEntity<List<ExpenseDTO>> getCurrentProfileExpensesForCurrentMonth(){
            List<ExpenseDTO> expenses = expenseService.getCurrentMonthExpenses();
            return ResponseEntity.ok(expenses);
        }

        @DeleteMapping("/{expenseId}")
    public ResponseEntity<String> deleteExpenseById(@PathVariable Long expenseId){
            expenseService.deleteExpense(expenseId);
            return ResponseEntity.ok("Expense Deleted");
        }

       @GetMapping("/top")
    public ResponseEntity<List<ExpenseDTO>>get5LatestExpenses(){
            List<ExpenseDTO> latest = expenseService.getLatest5ExpensesForCurrentUser();
            return ResponseEntity.ok(latest);

           }
    @GetMapping("/sum")
    public ResponseEntity<BigDecimal> totalExpense(){
          return  ResponseEntity.ok(expenseService.totalExpenses());
       }
}
