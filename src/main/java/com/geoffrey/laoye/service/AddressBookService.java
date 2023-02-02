package com.geoffrey.laoye.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.geoffrey.laoye.entity.AddressBook;
import org.springframework.transaction.annotation.Transactional;

public interface AddressBookService extends IService<AddressBook> {
    //设置默认地址
    @Transactional
    public void setDefault(AddressBook addressBook);
}
