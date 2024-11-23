package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * ClassName: AddressBookServiceImpl
 * Description:
 *
 * @Author: 陈杰
 * @Create: 2024/11/20 - 下午5:41
 * @Version: v1.0
 */
@Service
public class AddressBookServiceImpl implements AddressBookService {

    @Autowired
    private AddressBookMapper addressBookMapper;

    @Override
    public void addAddressBook(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBook.setIsDefault(0);
        addressBookMapper.addAddressBook(addressBook);
    }

    @Override
    public List<AddressBook> list() {
        return addressBookMapper.list(BaseContext.getCurrentId());
    }

    @Override
    public AddressBook getDefaultAddress() {
        return addressBookMapper.getDefaultAddress(BaseContext.getCurrentId());
    }

    @Override
    public void updateAddress(AddressBook addressBook) {
        addressBookMapper.updateAddress(addressBook);
    }

    @Override
    public void delAddressById(Long id) {
        addressBookMapper.delAddressById(id);
    }

    @Override
    public AddressBook getAddressById(Long id) {
        return addressBookMapper.getAddressById(id);
    }

    @Override
    @Transactional
    public void setDefaultAddress(AddressBook addressBook) {
        AddressBook newDefaultAddress = addressBookMapper.getAddressById(addressBook.getId());
        List<AddressBook> list = addressBookMapper.list(newDefaultAddress.getUserId());
        for (AddressBook address : list) {
            if (address.getIsDefault() == 1) {
                address.setIsDefault(0);
                addressBookMapper.updateAddress(address);
                break;
            }
        }
        newDefaultAddress.setIsDefault(1);
        addressBookMapper.updateAddress(newDefaultAddress);
    }
}
