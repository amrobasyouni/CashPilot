package in.amrobasyouni.CashPilot.service;

import in.amrobasyouni.CashPilot.dto.ExpenseDTO;
import in.amrobasyouni.CashPilot.entity.ProfileEntity;
import in.amrobasyouni.CashPilot.repository.ProfileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final ProfileRepository profileRepository;
    private final EmailService emailService;
    private final ExpenseService expenseService;

    @Value("${money.manger.frontend.url}")
    private String frontendUrl;

    @Scheduled(cron = "0 0 22 * * *",zone = "Asia/Beirut")
    public void sendDailyIncomeExpenseReminder(){
        log.info("Job Started: sendDailyIncomeExpenseReminder");
        List<ProfileEntity> profiles = profileRepository.findAll();
        for (ProfileEntity profile : profiles){
            String body = "Hi " + profile.getFullName() + "<br><br>"
                    + "This is a friendly reminder to add your expenses and incomes in Cash Pilot.<br><br>"
                    + "<a href=\"" + frontendUrl + "\" style=\"display:inline-block; padding:10px 20px; background-color:#4CAF50; color:#fff; text-decoration:none; border-radius:5px; font-weight:bold;\">Go to Cash Pilot</a>"
                    + "<br><br>Best Regards, <br>Cash Pilot";
            emailService.sendEmail(profile.getEmail(),"Daily Reminder: Add incomes and expenses",body);
        }
        log.info("Job Completed: sendDailyIncomeExpenseReminder");
    }

    @Scheduled(cron = "0 0 22 * * *",zone = "Asia/Beirut")
    @Transactional
    public void sendDailyExpenseSummary(){
        log.info("Job Started: sendDailyExpenseSummary");
        List<ProfileEntity> profiles = profileRepository.findAll();

        for(ProfileEntity profile: profiles){
           List<ExpenseDTO> todayExpenses =  expenseService.getExpensesForUserOnDate(profile.getId(), LocalDate.now());
            if(!todayExpenses.isEmpty()){
                StringBuilder table = new StringBuilder();
                table.append("<table style='border-collapse:collapse;width:100%;'>");
                table.append("<tr style='background-color:#f2f2f2;'>")
                        .append("<th style='border:1px solid #ddd; padding:8px; text-align:left;'>SN</th>")
                        .append("<th style='border:1px solid #ddd; padding:8px; text-align:left;'>Amount</th>")
                        .append("<th style='border:1px solid #ddd; padding:8px; text-align:left;'>Name</th>")
                        .append("<th style='border:1px solid #ddd; padding:8px; text-align:left;'>Category Name</th>")
                        .append("</tr>");
                int i = 1;
                for(ExpenseDTO expenseDTO : todayExpenses){
                    table.append("<tr>");
                    table.append("<td style='border:1px solid #ddd; padding:8px;'>").append(i++).append("</td>");
                    table.append("<td style='border:1px solid #ddd; padding:8px;'>").append(expenseDTO.getAmount()).append("</td>");
                    table.append("<td style='border:1px solid #ddd; padding:8px;'>").append(expenseDTO.getName()).append("</td>");
                    table.append("<td style='border:1px solid #ddd; padding:8px;'>").append(expenseDTO.getCategoryId()!= null? expenseDTO.getCategoryName():"N/A").append("</td>");
                    table.append("</tr>");
                }
                table.append("</table>");
                String body = "Hi " + profile.getFullName() + "<br><br>"+ table+"<br><br>"
                        + "<br><br>Best Regards, <br>Cash Pilot";
                emailService.sendEmail(profile.getEmail(),"Your Daily Expenses",body);
            }
        }
        log.info("Job Completed: sendDailyExpenseSummary");
    }

}
