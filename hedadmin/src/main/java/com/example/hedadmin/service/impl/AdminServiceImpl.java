package com.example.hedadmin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.hedadmin.entity.Admin;
import com.example.hedadmin.mapper.AdminMapper;
import com.example.hedadmin.service.AdminService;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
}
