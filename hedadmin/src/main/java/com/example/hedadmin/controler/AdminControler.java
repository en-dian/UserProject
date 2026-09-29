package com.example.hedadmin.controler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.hedadmin.entity.Admin;
import com.example.hedadmin.exception.BussinessException;
import com.example.hedadmin.response.R;
import com.example.hedadmin.response.ResponseCode;
import com.example.hedadmin.service.AdminService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//import org.springframework.web.bind.annotation.RequestMapping;
@Tag(name = "信息管理")
@RestController
public class AdminControler {
    @Resource
    public AdminService adminService;
//    @RequestMapping("/hedadmin")
    @Operation(summary = "新增用户")
    @PostMapping("/admin/add")
    @CrossOrigin
    public R add(@RequestBody Admin admin){
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Admin::getPhone,admin.getPhone());
        long count= adminService.count(wrapper);
        if(count > 0){
            throw new BussinessException(ResponseCode.NAME_EXIST);
        }
        adminService.save(admin);
        return R.success();
    }

    @Operation(summary = "用户列表")
//    @GetMapping("/admin/list")
    @PostMapping("/admin/list")
    @CrossOrigin
//    @PostMapping("/list")   // 明确为 POST
    public R<PageInfo<Admin>> list(
            @RequestBody(required = false) Admin admin,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize
    ) {
        if (admin == null) admin = new Admin();

        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();

        wrapper.like(admin.getName() != null, Admin::getName, admin.getName());
        wrapper.like(admin.getEmail() != null, Admin::getEmail, admin.getEmail());
        wrapper.like(admin.getPhone() != null, Admin::getPhone, admin.getPhone());
        wrapper.ne(Admin::getStatus, 2);
        wrapper.orderByDesc(Admin::getId);

        PageHelper.startPage(pageNum, pageSize);
        List<Admin> list = adminService.list(wrapper);
        PageInfo<Admin> pageInfo = new PageInfo<>(list);
        return R.data(pageInfo);
    }
    @Operation(summary = "删除用户")
    @PostMapping("/admin/remove")
    @CrossOrigin
    public R remove(@RequestParam List<Long> ids){
        adminService.removeByIds(ids);
        return R.success();
    }
    @Operation(summary = "修改用户")
    @PostMapping("admin/update")
    public R update(@RequestBody Admin admin){
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Admin::getPhone,admin.getPhone());
        long count= adminService.count(wrapper);
        if(count > 0){
            throw new BussinessException(ResponseCode.NAME_EXIST);
        }
        adminService.updateById(admin);
        return R.success();
    }
    private static final Logger log = LoggerFactory.getLogger(AdminControler.class);
    @Operation(summary = "软删除用户")
    @PostMapping("/admin/softRemove")
    @CrossOrigin
    public R softRemove(@RequestParam List<Long> ids) {
        System.out.println("=== 软删除请求，传入的 ids: " + ids);

        if (ids == null || ids.isEmpty()) {
            throw new BussinessException(ResponseCode.PARAM_ERROR);
        }

        // 查询更新前数据
        List<Admin> beforeList = adminService.listByIds(ids);
        System.out.println("=== 更新前数据: " + beforeList);

        // 执行软删除
        LambdaUpdateWrapper<Admin> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(Admin::getId, ids)
                .set(Admin::getStatus, 2);
        boolean success = adminService.update(wrapper);
        if (!success) {
            throw new BussinessException(ResponseCode.ERROR);
        }

        // 查询更新后数据
        List<Admin> afterList = adminService.listByIds(ids);
        System.out.println("=== 更新后数据: " + afterList);

        // 输出状态变化对比
        System.out.println("=== 状态变更完成，所有记录 status 已设为 2");

        return R.success();
    }

//    修改代码在调用这个接口的的时候输出，传入的 ids值，同时将寻找到的ids对应的数据输出，再将改过的数据进行输出，两则对比一下

}
