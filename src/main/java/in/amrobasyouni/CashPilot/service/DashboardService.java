package in.amrobasyouni.CashPilot.service;

import in.amrobasyouni.CashPilot.dto.ExpenseDTO;
import in.amrobasyouni.CashPilot.dto.IncomeDTO;
import in.amrobasyouni.CashPilot.dto.RecentTransactionDTO;
import in.amrobasyouni.CashPilot.entity.ProfileEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Stream.concat;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ExpenseService expenseService;
    private final IncomeService incomeService;
    private final ProfileService profileService;


    public Map<String ,Object> getDashboardData(){
        ProfileEntity profile = profileService.getCurrentProfile();
        Map<String,Object> returnValue = new LinkedHashMap<>();
        List<IncomeDTO> lastestIncomes = incomeService.getLatest5IncomesForCurrentUser();
        List<ExpenseDTO> lastestExpenses = expenseService.getLatest5ExpensesForCurrentUser();
        List<RecentTransactionDTO> recentTransaction = concat(lastestIncomes.stream().map(income->
                RecentTransactionDTO.builder()
                        .id(income.getId())
                        .name(income.getName())
                        .icon(income.getIcon())
                        .date(income.getDate())
                        .profileId(profile.getId())
                        .createdAt(income.getCreatedAt())
                        .updatedAt(income.getUpdatedAt())
                        .type("income")
                        .amount(income.getAmount())
                        .build()),
lastestExpenses.stream().map(expense->
        RecentTransactionDTO.builder()
                .id(expense.getId())
                .name(expense.getName())
                .icon(expense.getIcon())
                .date(expense.getDate())
                .profileId(profile.getId())
                .createdAt(expense.getCreatedAt())
                .updatedAt(expense.getUpdatedAt())
                .type("Expense")
                .amount(expense.getAmount())
        .build())
        .sorted((a,b)->{
            int cmp = b.getDate().compareTo(a.getDate());
            if (cmp == 0 && a.getCreatedAt()!=null && b.getCreatedAt()!=null){
              return b.getCreatedAt().compareTo(a.getCreatedAt());
            }
            return cmp;
        })).toList();

        returnValue.put("totalBalance",incomeService.totalIncomes().subtract(expenseService.totalExpenses()));
        returnValue.put("totalIncome",incomeService.totalIncomes());
        returnValue.put("totalExpense",expenseService.totalExpenses());
        returnValue.put("recent5Expenses",lastestExpenses);
        returnValue.put("recent5Incomes",lastestIncomes);
        returnValue.put("recnetTransactions",recentTransaction);
        return returnValue;


    }

}
