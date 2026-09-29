import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Random;


public class GenerateUserData {

    // 中文姓氏池
    private static final String[] SURNAMES = {
            "赵", "钱", "孙", "李", "周", "吴", "郑", "王", "冯", "陈", "褚", "卫",
            "蒋", "沈", "韩", "杨", "朱", "秦", "尤", "许", "何", "吕", "施", "张",
            "孔", "曹", "严", "华", "金", "魏", "陶", "姜", "戚", "谢", "邹", "喻",
            "柏", "水", "窦", "章", "云", "苏", "潘", "葛", "奚", "范", "彭", "郎",
            "鲁", "韦", "昌", "马", "苗", "凤", "花", "方", "俞", "任", "袁", "柳"
    };

    // 中文名（单字/双字共用）
    private static final String[] GIVEN_NAMES = {
            "伟", "芳", "娜", "秀英", "敏", "静", "丽", "强", "磊", "洋",
            "勇", "艳", "杰", "倩", "涛", "明", "超", "秀兰", "霞", "平",
            "刚", "桂英", "涛", "慧", "建", "文", "华", "飞", "玉兰", "斌",
            "宇", "鑫", "浩", "然", "博", "文", "轩", "宇", "辰", "怡",
            "彤", "萱", "妍", "琦", "珂", "玥", "帅", "岚", "鸣"
    };

    // 城市列表
    private static final String[] CITIES = {
            "北京市", "上海市", "广州市", "深圳市", "杭州市", "南京市",
            "武汉市", "成都市", "重庆市", "西安市", "天津市", "苏州市",
            "郑州市", "长沙市", "东莞市", "青岛市", "沈阳市", "大连市",
            "昆明市", "贵阳市"
    };

    // 街道列表
    private static final String[] STREETS = {
            "中山路", "人民路", "解放路", "和平路", "建设路", "文化路",
            "花园路", "东风路", "黄河路", "长江路", "中华路", "迎宾路",
            "朝阳路", "光明路", "幸福路", "团结路", "友谊路", "青年路"
    };

    public static void main(String[] args) {
        int count = 10000;
        String outputPath = "D:\\desk\\File\\MyProject\\users.csv";
        File outputFile = new File(outputPath);

        // 确保父目录存在
        File parentDir = outputFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            if (parentDir.mkdirs()) {
                System.out.println("创建目录: " + parentDir.getAbsolutePath());
            }
        }

        Random random = new Random();

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(outputFile), StandardCharsets.UTF_8))) {

            // 写入 CSV 表头（不含 id，id 由数据库自增）
            writer.write("name,password,phone,status,email,address");
            writer.newLine();

            for (int i = 0; i < count; i++) {
                String name = generateName(random);
                String password = String.format("%06d", random.nextInt(1000000)); // 六位数字密码
                String phone = generatePhone(random);
                int status = random.nextInt(2); // 0-禁用，1-启用
                String email = generateEmail(random);
                String address = generateAddress(random);

                // 拼接 CSV 行（字段含逗号时自动转义）
                String line = String.format("%s,%s,%s,%d,%s,%s",
                        escapeCsv(name),
                        password,
                        phone,
                        status,
                        escapeCsv(email),
                        escapeCsv(address));
                writer.write(line);
                writer.newLine();

                // 每 1000 条打印进度
                if ((i + 1) % 1000 == 0) {
                    System.out.println("已生成 " + (i + 1) + " 条数据");
                }
            }

            System.out.println("✅ 数据生成完成，文件保存在: " + outputPath);

        } catch (IOException e) {
            System.err.println("❌ 写入文件失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 生成中文姓名（2~3个字）
     */
    private static String generateName(Random random) {
        String surname = SURNAMES[random.nextInt(SURNAMES.length)];
        String given = GIVEN_NAMES[random.nextInt(GIVEN_NAMES.length)];
        // 随机决定是单名还是双名
        if (random.nextBoolean()) {
            String given2 = GIVEN_NAMES[random.nextInt(GIVEN_NAMES.length)];
            return surname + given + given2;
        } else {
            return surname + given;
        }
    }

    /**
     * 生成 11 位手机号（1 开头，第二位 3~9）
     */
    private static String generatePhone(Random random) {
        int second = 3 + random.nextInt(7); // 3~9
        // 后 9 位：100000000 ~ 999999999
        long rest = 100000000L + (long) (random.nextDouble() * 900000000L);
        return "1" + second + rest;
    }

    /**
     * 生成随机邮箱（格式：字母+数字@example.com）
     */
    private static String generateEmail(Random random) {
        int len = 5 + random.nextInt(6); // 5~10 位字母
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            char c = (char) ('a' + random.nextInt(26));
            sb.append(c);
        }
        sb.append(random.nextInt(1000)); // 追加数字后缀
        sb.append("@example.com");
        return sb.toString();
    }

    /**
     * 生成地址（城市 + 街道 + 门牌号）
     */
    private static String generateAddress(Random random) {
        String city = CITIES[random.nextInt(CITIES.length)];
        String street = STREETS[random.nextInt(STREETS.length)];
        int door = 1 + random.nextInt(200);
        return city + " " + street + " " + door + "号";
    }

    /**
     * CSV 字段转义（处理逗号、引号、换行）
     */
    private static String escapeCsv(String field) {
        if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }
        return field;
    }
}