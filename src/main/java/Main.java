import java.time.LocalDateTime;
import java.time.Month;

public class Main
{
    public static void main(String[] args) {

        LocalDateTime createdAt = LocalDateTime.of(2026,Month.AUGUST,20,0,0);
        System.out.println(createdAt.isBefore(LocalDateTime.now().minusMonths(5)));
    }
}