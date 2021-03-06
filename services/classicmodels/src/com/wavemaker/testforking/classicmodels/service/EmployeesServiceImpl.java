/*Copyright (c) 2022-2023 wavemaker.com All Rights Reserved.
 This software is the confidential and proprietary information of wavemaker.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with wavemaker.com*/
package com.wavemaker.testforking.classicmodels.service;

/*This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.*/

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.wavemaker.commons.InvalidInputException;
import com.wavemaker.commons.MessageResource;
import com.wavemaker.runtime.data.annotations.EntityService;
import com.wavemaker.runtime.data.dao.WMGenericDao;
import com.wavemaker.runtime.data.exception.EntityNotFoundException;
import com.wavemaker.runtime.data.export.DataExportOptions;
import com.wavemaker.runtime.data.export.ExportType;
import com.wavemaker.runtime.data.expression.QueryFilter;
import com.wavemaker.runtime.data.model.AggregationInfo;
import com.wavemaker.runtime.file.model.Downloadable;

import com.wavemaker.testforking.classicmodels.Customers;
import com.wavemaker.testforking.classicmodels.Employees;


/**
 * ServiceImpl object for domain model class Employees.
 *
 * @see Employees
 */
@Service("classicmodels.EmployeesService")
@Validated
@EntityService(entityClass = Employees.class, serviceId = "classicmodels")
public class EmployeesServiceImpl implements EmployeesService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeesServiceImpl.class);

    @Lazy
    @Autowired
    @Qualifier("classicmodels.CustomersService")
    private CustomersService customersService;

    @Autowired
    @Qualifier("classicmodels.EmployeesDao")
    private WMGenericDao<Employees, Integer> wmGenericDao;

    @Autowired
    @Qualifier("wmAppObjectMapper")
    private ObjectMapper objectMapper;


    public void setWMGenericDao(WMGenericDao<Employees, Integer> wmGenericDao) {
        this.wmGenericDao = wmGenericDao;
    }

    @Transactional(value = "classicmodelsTransactionManager")
    @Override
    public Employees create(Employees employees) {
        LOGGER.debug("Creating a new Employees with information: {}", employees);

        Employees employeesCreated = this.wmGenericDao.create(employees);
        // reloading object from database to get database defined & server defined values.
        return this.wmGenericDao.refresh(employeesCreated);
    }

    @Transactional(readOnly = true, value = "classicmodelsTransactionManager")
    @Override
    public Employees getById(Integer employeesId) {
        LOGGER.debug("Finding Employees by id: {}", employeesId);
        return this.wmGenericDao.findById(employeesId);
    }

    @Transactional(readOnly = true, value = "classicmodelsTransactionManager")
    @Override
    public Employees findById(Integer employeesId) {
        LOGGER.debug("Finding Employees by id: {}", employeesId);
        try {
            return this.wmGenericDao.findById(employeesId);
        } catch (EntityNotFoundException ex) {
            LOGGER.debug("No Employees found with id: {}", employeesId, ex);
            return null;
        }
    }

    @Transactional(readOnly = true, value = "classicmodelsTransactionManager")
    @Override
    public List<Employees> findByMultipleIds(List<Integer> employeesIds, boolean orderedReturn) {
        LOGGER.debug("Finding Employees by ids: {}", employeesIds);

        return this.wmGenericDao.findByMultipleIds(employeesIds, orderedReturn);
    }


    @Transactional(rollbackFor = EntityNotFoundException.class, value = "classicmodelsTransactionManager")
    @Override
    public Employees update(Employees employees) {
        LOGGER.debug("Updating Employees with information: {}", employees);

        this.wmGenericDao.update(employees);
        this.wmGenericDao.refresh(employees);

        return employees;
    }

    @Transactional(value = "classicmodelsTransactionManager")
    @Override
    public Employees partialUpdate(Integer employeesId, Map<String, Object>employeesPatch) {
        LOGGER.debug("Partially Updating the Employees with id: {}", employeesId);

        Employees employees = getById(employeesId);

        try {
            ObjectReader employeesReader = this.objectMapper.reader().forType(Employees.class).withValueToUpdate(employees);
            employees = employeesReader.readValue(this.objectMapper.writeValueAsString(employeesPatch));
        } catch (IOException ex) {
            LOGGER.debug("There was a problem in applying the patch: {}", employeesPatch, ex);
            throw new InvalidInputException("Could not apply patch",ex);
        }

        employees = update(employees);

        return employees;
    }

    @Transactional(value = "classicmodelsTransactionManager")
    @Override
    public Employees delete(Integer employeesId) {
        LOGGER.debug("Deleting Employees with id: {}", employeesId);
        Employees deleted = this.wmGenericDao.findById(employeesId);
        if (deleted == null) {
            LOGGER.debug("No Employees found with id: {}", employeesId);
            throw new EntityNotFoundException(MessageResource.create("com.wavemaker.runtime.entity.not.found"), Employees.class.getSimpleName(), employeesId);
        }
        this.wmGenericDao.delete(deleted);
        return deleted;
    }

    @Transactional(value = "classicmodelsTransactionManager")
    @Override
    public void delete(Employees employees) {
        LOGGER.debug("Deleting Employees with {}", employees);
        this.wmGenericDao.delete(employees);
    }

    @Transactional(readOnly = true, value = "classicmodelsTransactionManager")
    @Override
    public Page<Employees> findAll(QueryFilter[] queryFilters, Pageable pageable) {
        LOGGER.debug("Finding all Employees");
        return this.wmGenericDao.search(queryFilters, pageable);
    }

    @Transactional(readOnly = true, value = "classicmodelsTransactionManager")
    @Override
    public Page<Employees> findAll(String query, Pageable pageable) {
        LOGGER.debug("Finding all Employees");
        return this.wmGenericDao.searchByQuery(query, pageable);
    }

    @Transactional(readOnly = true, value = "classicmodelsTransactionManager", timeout = 300)
    @Override
    public Downloadable export(ExportType exportType, String query, Pageable pageable) {
        LOGGER.debug("exporting data in the service classicmodels for table Employees to {} format", exportType);
        return this.wmGenericDao.export(exportType, query, pageable);
    }

    @Transactional(readOnly = true, value = "classicmodelsTransactionManager", timeout = 300)
    @Override
    public void export(DataExportOptions options, Pageable pageable, OutputStream outputStream) {
        LOGGER.debug("exporting data in the service classicmodels for table Employees to {} format", options.getExportType());
        this.wmGenericDao.export(options, pageable, outputStream);
    }

    @Transactional(readOnly = true, value = "classicmodelsTransactionManager")
    @Override
    public long count(String query) {
        return this.wmGenericDao.count(query);
    }

    @Transactional(readOnly = true, value = "classicmodelsTransactionManager")
    @Override
    public Page<Map<String, Object>> getAggregatedValues(AggregationInfo aggregationInfo, Pageable pageable) {
        return this.wmGenericDao.getAggregatedValues(aggregationInfo, pageable);
    }

    @Transactional(readOnly = true, value = "classicmodelsTransactionManager")
    @Override
    public Page<Customers> findAssociatedCustomerses(Integer employeeNumber, Pageable pageable) {
        LOGGER.debug("Fetching all associated customerses");

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("employees.employeeNumber = '" + employeeNumber + "'");

        return customersService.findAll(queryBuilder.toString(), pageable);
    }

    @Transactional(readOnly = true, value = "classicmodelsTransactionManager")
    @Override
    public Page<Employees> findAssociatedEmployeesesForReportsTo(Integer employeeNumber, Pageable pageable) {
        LOGGER.debug("Fetching all associated employeesesForReportsTo");

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("employeesByReportsTo.employeeNumber = '" + employeeNumber + "'");

        return findAll(queryBuilder.toString(), pageable);
    }

    /**
     * This setter method should only be used by unit tests
     *
     * @param service CustomersService instance
     */
    protected void setCustomersService(CustomersService service) {
        this.customersService = service;
    }

}