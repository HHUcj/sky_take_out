package com.sky.service;

import com.sky.entity.AddressBook;

import java.util.List;

/**
 * ClassName: AddressBook
 * Description:
 *
 * @Author: 陈杰
 * @Create: 2024/11/20 - 下午5:41
 * @Version: v1.0
 */
public interface AddressBookService {
    void addAddressBook(AddressBook addressBook);

    List<AddressBook> list();

    AddressBook getDefaultAddress();

    void updateAddress(AddressBook addressBook);

    void delAddressById(Long id);

    AddressBook getAddressById(Long id);

    void setDefaultAddress(AddressBook addressBook);
}
