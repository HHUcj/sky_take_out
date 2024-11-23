package com.sky.mapper;

import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * ClassName: AddressBookMapper
 * Description:
 *
 * @Author: 陈杰
 * @Create: 2024/11/20 - 下午5:42
 * @Version: v1.0
 */
@Mapper
public interface AddressBookMapper {
    void addAddressBook(AddressBook addressBook);

    List<AddressBook> list(Long userId);

    AddressBook getDefaultAddress(Long currentId);

    void updateAddress(AddressBook addressBook);

    void delAddressById(Long id);

    AddressBook getAddressById(Long id);
}
