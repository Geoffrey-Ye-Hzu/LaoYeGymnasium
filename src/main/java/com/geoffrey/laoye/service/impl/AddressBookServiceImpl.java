package com.geoffrey.laoye.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoffrey.laoye.common.BaseContext;
import com.geoffrey.laoye.entity.AddressBook;
import com.geoffrey.laoye.entity.User;
import com.geoffrey.laoye.mapper.AddressBookMapper;
import com.geoffrey.laoye.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {


    //设置默认地址
    @Override
    public void setDefault(AddressBook addressBook) {
        LambdaUpdateWrapper<AddressBook> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        updateWrapper.set(AddressBook::getIsDefault,0);
        this.update(updateWrapper); //UPDATE address_book SET is_default=? WHERE (user_id = ?)

        addressBook.setIsDefault(1);
        this.updateById(addressBook); // UPDATE address_book SET is_default=?, update_time=?, update_user=? WHERE id=?
    }

    @Override
    public void saveUserInfo(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        log.info("addressBook:{}", addressBook);
        this.save(addressBook);
    }
    @Override
    public void saveNewUserInfo(User user) {
        String phone = user.getPhone();
        Long userId = user.getId();
        AddressBook addressBook = new AddressBook();
        addressBook.setSex("1"); //默认设置为男性
        addressBook.setPhone(phone);
        addressBook.setNickname("新用户" + phone);
        addressBook.setConsignee("新用户" + phone);
        addressBook.setUserId(userId);
        addressBook.setImage("avatarDefault.jpg");


        log.info("addressBook:{}", addressBook);
        this.save(addressBook);
    }
}
