import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CsvImporter {

    // ========== 数据库连接配置（请根据实际情况修改） ==========
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/usersdb?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "123456";

    // ========== CSV 文件路径 ==========
    private static final String CSV_FILE_PATH = "D:/desk/File/MyProject/admin_data.csv";

    // ========== 批量插入大小 ==========
    private static final int BATCH_SIZE = 10000;

    public static void main(String[] args) {
        // 1. 注册 JDBC 驱动（可选，高版本无需显式注册）
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC 驱动未找到，请检查依赖");
            e.printStackTrace();
            return;
        }

        // 2. 读取 CSV 并导入
        List<String[]> rows = readCsv(CSV_FILE_PATH);
        if (rows.isEmpty()) {
            System.out.println("CSV 文件为空或读取失败，程序退出");
            return;
        }

        // 打印总行数（不含表头）
        System.out.println("待导入数据行数：" + rows.size());

        // 3. 批量插入
        int insertedCount = insertBatch(rows);
        System.out.println("成功导入 " + insertedCount + " 条记录");
    }

    /**
     * 读取 CSV 文件，跳过表头，返回数据行列表（每行为 String[]）
     */
    private static List<String[]> readCsv(String filePath) {
        List<String[]> dataRows = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            System.err.println("CSV 文件不存在：" + filePath);
            return dataRows;
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

            // ---- 处理 UTF-8 BOM（可选） ----
            br.mark(1);
            int firstChar = br.read();
            if (firstChar != '\uFEFF') {
                br.reset(); // 不是 BOM，回退
            } // 否则跳过 BOM

            String line;
            boolean isHeader = true;
            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false; // 跳过表头
                    continue;
                }
                // 用逗号分割（CSV 中字段不含逗号，直接 split）
                String[] fields = line.split(",", -1); // -1 保留空字段
                if (fields.length < 6) {
                    System.err.println("跳过无效行（字段数不足6）：" + line);
                    continue;
                }
                dataRows.add(fields);
            }
        } catch (IOException e) {
            System.err.println("读取 CSV 文件出错：");
            e.printStackTrace();
        }
        return dataRows;
    }

    /**
     * 使用 JDBC 批量插入
     */
    private static int insertBatch(List<String[]> rows) {
        String sql = "INSERT INTO user (name, password, phone, status, email, address) VALUES (?, ?, ?, ?, ?, ?)";
        int totalInserted = 0;

        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false); // 手动控制事务

            int count = 0;
            for (String[] row : rows) {
                pstmt.setString(1, row[0]); // name
                pstmt.setString(2, row[1]); // password
                pstmt.setString(3, row[2]); // phone
                pstmt.setInt(4, Integer.parseInt(row[3])); // status (0/1)
                pstmt.setString(5, row[4]); // email
                pstmt.setString(6, row[5]); // address
                pstmt.addBatch();
                count++;

                if (count % BATCH_SIZE == 0) {
                    int[] results = pstmt.executeBatch();
                    totalInserted += results.length;
                    conn.commit();
                    System.out.println("已提交 " + totalInserted + " 条");
                }
            }
            // 提交剩余
            if (count % BATCH_SIZE != 0) {
                int[] results = pstmt.executeBatch();
                totalInserted += results.length;
                conn.commit();
            }

        } catch (SQLException e) {
            System.err.println("数据库操作失败：");
            e.printStackTrace();
            // 可考虑回滚
        }
        return totalInserted;
    }
}