/*Copyright (c) 2022-2023 wavemaker.com All Rights Reserved.
 This software is the confidential and proprietary information of wavemaker.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with wavemaker.com*/
package com.wavemaker.testforking.classicmodels;

/*This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.*/

import java.io.Serializable;
import java.util.Objects;

public class OrderdetailsId implements Serializable {

    private Integer orderNumber;
    private String productCode;

    public Integer getOrderNumber() {
        return this.orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getProductCode() {
        return this.productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Orderdetails)) return false;
        final Orderdetails orderdetails = (Orderdetails) o;
        return Objects.equals(getOrderNumber(), orderdetails.getOrderNumber()) &&
                Objects.equals(getProductCode(), orderdetails.getProductCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrderNumber(),
                getProductCode());
    }
}