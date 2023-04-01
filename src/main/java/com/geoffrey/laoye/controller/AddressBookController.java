package com.geoffrey.laoye.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.geoffrey.laoye.common.BaseContext;
import com.geoffrey.laoye.common.R;
import com.geoffrey.laoye.entity.AddressBook;
import com.geoffrey.laoye.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 地址薄管理
 */
@RestController
@RequestMapping("/addressBook")
@Slf4j
@SuppressWarnings("all")

public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;

    /**
     * 新增地址信息
     * @param addressBook
     * @return
     */
    @PostMapping
    private R<String> save(@RequestBody AddressBook addressBook) {
        addressBookService.saveUserInfo(addressBook);
        return R.success("新增地址成功");
    }

    ///**
    // * 查询指定用户的全部地址
    // * @return
    // */
    //@GetMapping("/list")
    //private R<List<AddressBook>> list() {
    //    LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
    //    queryWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
    //    queryWrapper.orderByDesc(AddressBook::getUpdateTime);
    //    List<AddressBook> list = addressBookService.list(queryWrapper);
    //    return R.success(list);
    //}

    /**
     * 查询指定用户的信息
     * @return
     */
    @GetMapping("/list")
    private R<AddressBook> list() {
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        //queryWrapper.orderByDesc(AddressBook::getUpdateTime);
        AddressBook addressBook = addressBookService.getOne(queryWrapper);
        return R.success(addressBook);
    }

    ///**
    // * 设置默认
    // * @param addressBook
    // * @return
    // */
    //@PutMapping("/default")
    //private R<String> setDefault(@RequestBody AddressBook addressBook){
    //    addressBookService.setDefault(addressBook);
    //    return R.success("设置默认地址成功");
    //}

    ///**
    // * 获得默认地址
    // * @return
    // */
    //@GetMapping("/default")
    //private R<AddressBook> getDefault(){
    //    LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
    //    queryWrapper.eq(AddressBook::getUserId,BaseContext.getCurrentId());
    //    queryWrapper.eq(AddressBook::getIsDefault,1);
    //    AddressBook addressBook = addressBookService.getOne(queryWrapper);
    //    return R.success(addressBook);
    //}

    /**
     * 根据id查询地址
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    private R<AddressBook> getAddressBook(@PathVariable String id){
        Long userId = Long.valueOf(id);
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId,userId);
        AddressBook addressBook = addressBookService.getOne(queryWrapper);
        if (addressBook!=null){
            return R.success(addressBook);
        }else {
            return R.error("没有找到该对象");
        }
    }

    /**
     * 修改收获地址
     * @param addressBook
     * @return
     */
    @PutMapping
    private R<String> update(@RequestBody AddressBook addressBook){
        log.warn("头像地址：{}",addressBook.getImage());
        addressBookService.updateById(addressBook);
        return R.success("保存地址成功");
    }

    /**
     * 根据id删除地址
     * @param id
     * @return
     */
    @DeleteMapping
    private R<String> deleteById(Long id){
        addressBookService.removeById(id);
        return R.success("删除地址成功");
    }

}
