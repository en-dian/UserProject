import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class AdminDataGeneratorCsv {

    // 保存路径（Windows 路径用双反斜杠或正斜杠）
    private static final String OUTPUT_PATH = "D:/desk/File/MyProject/admin_data.csv";
    // 生成条数
    private static final int TOTAL = 10000;

    // 姓氏池
    private static final String[] SURNAMES = {
            "李", "王", "张", "刘", "陈", "杨", "赵", "黄", "周", "吴",
            "徐", "孙", "胡", "朱", "高", "林", "何", "郭", "马", "罗",
            "梁", "宋", "郑", "谢", "韩", "唐", "冯", "于", "董", "萧"
    };
    // 名字池
    private static final String[] GIVEN_NAMES = {
            "明", "华", "伟", "芳", "娜", "敏", "静", "强", "磊", "洋",
            "艳", "勇", "军", "杰", "娟", "涛", "超", "秀兰", "霞", "平",
            "刚", "桂英", "文", "辉", "玲", "健", "俊", "峰", "雪", "丽",
            "鹏", "博", "婷", "欣", "宇", "浩", "凯", "悦", "璐", "瑶"
    };
    // 手机号号段
    private static final String[] PHONE_PREFIX = {
            "130", "131", "132", "133", "134", "135", "136", "137", "138", "139",
            "150", "151", "152", "153", "155", "156", "157", "158", "159",
            "180", "181", "182", "183", "184", "185", "186", "187", "188", "189"
    };
    // 邮箱域名
    private static final String[] EMAIL_DOMAINS = {
            "qq.com", "163.com", "gmail.com", "outlook.com", "yahoo.com",
            "foxmail.com", "126.com", "aliyun.com", "sina.com", "hotmail.com"
    };
    // 省份城市 + 街道样例
    private static final String[] CITIES = {
            "陕西省西安市未央区某某街道", "陕西省西安市雁塔区科技路88号",
            "北京市海淀区中关村大街1号", "上海市静安区南京西路1266号",
            "广东省深圳市南山区科技园路8号", "浙江省杭州市西湖区文二路188号",
            "四川省成都市高新区天府五街200号", "湖北省武汉市洪山区珞瑜路726号",
            "江苏省南京市建邺区江东中路229号", "山东省青岛市市南区香港中路11号",
            "湖南省长沙市雨花区劳动西路102号", "河南省郑州市二七区大学路80号",
            "福建省厦门市思明区中山路400号", "重庆市渝北区金开大道68号",
            "天津市南开区卫津路94号", "安徽省合肥市政务区潜山路111号",
            "江西省南昌市青山湖区北京东路456号", "辽宁省大连市中山区人民路55号",
            "吉林省长春市南关区人民大街123号", "黑龙江省哈尔滨市道里区中央大街99号"
    };

    public static void main(String[] args) {
        Random random = new Random();
        Set<String> phoneSet = new HashSet<>(TOTAL * 2);
        List<String[]> rows = new ArrayList<>(TOTAL);

        // 表头（新增 password 列）
        rows.add(new String[] { "name", "password", "phone", "status", "email", "address" });

        while (rows.size() <= TOTAL) {
            String name = randomName(random);
            String phone = randomPhone(random);

            // 手机号去重
            if (phoneSet.contains(phone))
                continue;
            phoneSet.add(phone);

            // 生成六位数密码
            String password = String.format("%06d", random.nextInt(1000000));

            int status = random.nextInt(2);
            String email = randomEmail(name, random);
            String address = CITIES[random.nextInt(CITIES.length)]
                    + (random.nextInt(999) + 1) + "号";

            rows.add(new String[] {
                    name, password, phone, String.valueOf(status), email, address
            });
        }

        // 写入 CSV
        File file = new File(OUTPUT_PATH);
        file.getParentFile().mkdirs();

        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            // 写入 BOM，避免 Excel 打开中文乱码
            bw.write('\uFEFF');
            for (String[] row : rows) {
                bw.write(String.join(",", row));
                bw.newLine();
            }
            System.out.println("生成完成，共 " + (rows.size() - 1) + " 条数据");
            System.out.println("文件路径：" + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String randomName(Random r) {
        return SURNAMES[r.nextInt(SURNAMES.length)]
                + GIVEN_NAMES[r.nextInt(GIVEN_NAMES.length)];
    }

    private static String randomPhone(Random r) {
        String prefix = PHONE_PREFIX[r.nextInt(PHONE_PREFIX.length)];
        StringBuilder sb = new StringBuilder(prefix);
        for (int i = 0; i < 8; i++) {
            sb.append(r.nextInt(10));
        }
        return sb.toString();
    }

    private static String randomEmail(String name, Random r) {
        return name.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-zA-Z0-9]", "")
                + r.nextInt(10000)
                + "@" + EMAIL_DOMAINS[r.nextInt(EMAIL_DOMAINS.length)];
    }
}