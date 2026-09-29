import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Random;

public class AttendanceMock {

    public static void main(String[] args) {
        Random random = new Random();
        LocalDate startDate = LocalDate.of(2026, 6, 1);
        LocalDate endDate = LocalDate.of(2026, 8, 5);
        long[] userIds = { 3, 4 };
        double lateProbability = 0.10; // 10%迟到概率

        StringBuilder recordValues = new StringBuilder();
        StringBuilder summaryValues = new StringBuilder();

        for (long userId : userIds) {
            LocalDate current = startDate;
            while (!current.isAfter(endDate)) {
                // 跳过周六、周日
                DayOfWeek week = current.getDayOfWeek();
                if (week == DayOfWeek.SATURDAY || week == DayOfWeek.SUNDAY) {
                    current = current.plusDays(1);
                    continue;
                }

                boolean isLate = random.nextDouble() < lateProbability;
                LocalDateTime clockIn;
                int clockInStatus;
                String remark = "";

                if (isLate) {
                    // 迟到 09:05 ~ 09:40
                    int lateMin = 5 + random.nextInt(35);
                    clockIn = LocalDateTime.of(current, LocalTime.of(9, lateMin));
                    if (lateMin > 30) {
                        clockInStatus = 4; // 严重迟到
                        remark = "上班严重迟到";
                    } else {
                        clockInStatus = 2; // 普通迟到
                        remark = "上班迟到";
                    }
                } else {
                    // 正常上班 08:10‑08:58
                    int inMin = 10 + random.nextInt(48);
                    clockIn = LocalDateTime.of(current, LocalTime.of(8, inMin));
                    clockInStatus = 1;
                    remark = "";
                }

                // 下班 18:05‑18:45，全部正常
                int outMin = 5 + random.nextInt(40);
                LocalDateTime clockOut = LocalDateTime.of(current, LocalTime.of(18, outMin));

                int recordYear = current.getYear();
                int recordMonth = current.getMonthValue();
                int recordDay = current.getDayOfMonth();
                int inHour = clockIn.getHour();
                int outHour = clockOut.getHour();

                // ---------------- attendance_record 上班打卡 ----------------
                recordValues.append(String.format(
                        "('%s',%d,'%s',1,%d,%d,%d,%d,%d,1,108.8996420,34.2401950,'陕西省西安市雁塔区高新三路8号橙仕空间','dev-uu-%04d','192.168.1.10%d','橙仕空间','',0,'%s'),\n",
                        current,
                        userId,
                        clockIn,
                        recordYear, recordMonth, recordDay, inHour,
                        clockInStatus,
                        userId,
                        (int) userId,
                        remark));

                // ---------------- attendance_record 下班打卡 ----------------
                recordValues.append(String.format(
                        "('%s',%d,'%s',2,%d,%d,%d,%d,1,1,108.8996420,34.2401950,'陕西省西安市雁塔区高新三路8号橙仕空间','dev-uu-%04d','192.168.1.10%d','橙仕空间','橙仕空间',0,''),\n",
                        current,
                        userId,
                        clockOut,
                        recordYear, recordMonth, recordDay, outHour,
                        userId,
                        (int) userId));

                // ---------------- attendance_daily_summary ----------------
                long workSeconds = Duration.between(clockIn, clockOut).getSeconds();
                int isAbnormal = (clockInStatus == 2 || clockInStatus == 4) ? 1 : 0;

                summaryValues.append(String.format(
                        "(%d,'%s','%s','%s',%d,1,%d,0,%d),\n",
                        userId,
                        current,
                        clockIn,
                        clockOut,
                        clockInStatus,
                        workSeconds,
                        isAbnormal));

                current = current.plusDays(1);
            }
        }

        // 输出完整SQL
        System.out.println("==================== attendance_record INSERT ====================");
        System.out.println("INSERT INTO `attendance_record` (\n" +
                "`record_date`, `user_id`, `record_time`, `punch_type`,\n" +
                "`record_year`, `record_month`, `record_day`, `record_hour`,\n" +
                "`status`, `source_type`, `longitude`, `latitude`, `location_address`,\n" +
                "`device_id`, `ip_address`, `check_in_location`, `check_out_location`, `is_backup`, `remark`\n" +
                ") VALUES");
        System.out.println(recordValues);
        System.out.println(";");

        System.out.println("\n==================== attendance_daily_summary INSERT ====================");
        System.out.println("INSERT INTO `attendance_daily_summary` (\n" +
                "`user_id`, `record_date`, `clock_in_time`, `clock_out_time`,\n" +
                "`clock_in_status`, `clock_out_status`, `work_seconds`, `is_missing_card`, `is_abnormal`\n" +
                ") VALUES");
        System.out.println(summaryValues);
        System.out.println(";");
    }
}