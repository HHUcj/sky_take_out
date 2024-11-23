package com.sky.controller.user;

import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ClassName: AddressBookController
 * Description:
 *
 * @Author: 陈杰
 * @Create: 2024/11/20 - 下午5:40
 * @Version: v1.0
 */
@RestController
@RequestMapping("/user/addressBook")
@Slf4j
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    @PostMapping
    public Result addAddressBook(@RequestBody AddressBook addressBook) {
        addressBookService.addAddressBook(addressBook);
        return Result.success();
    }

    @GetMapping("/list")
    public Result<List<AddressBook>> list() {
        List<AddressBook> addressBookList = addressBookService.list();
        return Result.success(addressBookList);
    }

    @GetMapping("/default")
    public Result<AddressBook> getDefaultAddress() {
        AddressBook addressBook = addressBookService.getDefaultAddress();
        return Result.success(addressBook);
    }

    @PutMapping
    public Result updateAddress(@RequestBody AddressBook addressBook) {
        addressBookService.updateAddress(addressBook);
        return Result.success();
    }

    @DeleteMapping
    public Result delAddressById(Long id) {
        addressBookService.delAddressById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<AddressBook> getAddressById(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getAddressById(id);
        return Result.success(addressBook);
    }

    @PutMapping("/default")
    public Result setDefaultAddress(@RequestBody AddressBook addressBook) {
        addressBookService.setDefaultAddress(addressBook);
        return Result.success();
    }
}
