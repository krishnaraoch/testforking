/*Copyright (c) 2022-2023 wavemaker.com All Rights Reserved.
 This software is the confidential and proprietary information of wavemaker.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with wavemaker.com*/
package com.wavemaker.testforking.classicmodels.controller;

/*This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.*/

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wavemaker.commons.wrapper.StringWrapper;
import com.wavemaker.runtime.data.export.DataExportOptions;
import com.wavemaker.runtime.data.export.ExportType;
import com.wavemaker.runtime.data.expression.QueryFilter;
import com.wavemaker.runtime.data.model.AggregationInfo;
import com.wavemaker.runtime.file.manager.ExportedFileManager;
import com.wavemaker.runtime.file.model.Downloadable;
import com.wavemaker.tools.api.core.annotations.MapTo;
import com.wavemaker.tools.api.core.annotations.WMAccessVisibility;
import com.wavemaker.tools.api.core.models.AccessSpecifier;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import com.wavemaker.testforking.classicmodels.Employees;
import com.wavemaker.testforking.classicmodels.Offices;
import com.wavemaker.testforking.classicmodels.service.OfficesService;


/**
 * Controller object for domain model class Offices.
 * @see Offices
 */
@RestController("classicmodels.OfficesController")
@Api(value = "OfficesController", description = "Exposes APIs to work with Offices resource.")
@RequestMapping("/classicmodels/Offices")
public class OfficesController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OfficesController.class);

    @Autowired
	@Qualifier("classicmodels.OfficesService")
	private OfficesService officesService;

	@Autowired
	private ExportedFileManager exportedFileManager;

	@ApiOperation(value = "Creates a new Offices instance.")
    @RequestMapping(method = RequestMethod.POST)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public Offices createOffices(@RequestBody Offices offices) {
		LOGGER.debug("Create Offices with information: {}" , offices);

		offices = officesService.create(offices);
		LOGGER.debug("Created Offices with information: {}" , offices);

	    return offices;
	}

    @ApiOperation(value = "Returns the Offices instance associated with the given id.")
    @RequestMapping(value = "/{officeCode:.+}", method = RequestMethod.GET)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public Offices getOffices(@PathVariable("officeCode") String officeCode) {
        LOGGER.debug("Getting Offices with id: {}" , officeCode);

        Offices foundOffices = officesService.getById(officeCode);
        LOGGER.debug("Offices details with id: {}" , foundOffices);

        return foundOffices;
    }

    @ApiOperation(value = "Updates the Offices instance associated with the given id.")
    @RequestMapping(value = "/{officeCode:.+}", method = RequestMethod.PUT)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public Offices editOffices(@PathVariable("officeCode") String officeCode, @RequestBody Offices offices) {
        LOGGER.debug("Editing Offices with id: {}" , offices.getOfficeCode());

        offices.setOfficeCode(officeCode);
        offices = officesService.update(offices);
        LOGGER.debug("Offices details with id: {}" , offices);

        return offices;
    }
    
    @ApiOperation(value = "Partially updates the Offices instance associated with the given id.")
    @RequestMapping(value = "/{officeCode:.+}", method = RequestMethod.PATCH)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public Offices patchOffices(@PathVariable("officeCode") String officeCode, @RequestBody @MapTo(Offices.class) Map<String, Object> officesPatch) {
        LOGGER.debug("Partially updating Offices with id: {}" , officeCode);

        Offices offices = officesService.partialUpdate(officeCode, officesPatch);
        LOGGER.debug("Offices details after partial update: {}" , offices);

        return offices;
    }

    @ApiOperation(value = "Deletes the Offices instance associated with the given id.")
    @RequestMapping(value = "/{officeCode:.+}", method = RequestMethod.DELETE)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public boolean deleteOffices(@PathVariable("officeCode") String officeCode) {
        LOGGER.debug("Deleting Offices with id: {}" , officeCode);

        Offices deletedOffices = officesService.delete(officeCode);

        return deletedOffices != null;
    }

    /**
     * @deprecated Use {@link #findOffices(String, Pageable)} instead.
     */
    @Deprecated
    @ApiOperation(value = "Returns the list of Offices instances matching the search criteria.")
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public Page<Offices> searchOfficesByQueryFilters( Pageable pageable, @RequestBody QueryFilter[] queryFilters) {
        LOGGER.debug("Rendering Offices list by query filter:{}", (Object) queryFilters);
        return officesService.findAll(queryFilters, pageable);
    }

    @ApiOperation(value = "Returns the paginated list of Offices instances matching the optional query (q) request param. If there is no query provided, it returns all the instances. Pagination & Sorting parameters such as page& size, sort can be sent as request parameters. The sort value should be a comma separated list of field names & optional sort order to sort the data on. eg: field1 asc, field2 desc etc ")
    @RequestMapping(method = RequestMethod.GET)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public Page<Offices> findOffices(@ApiParam("conditions to filter the results") @RequestParam(value = "q", required = false) String query, Pageable pageable) {
        LOGGER.debug("Rendering Offices list by filter:", query);
        return officesService.findAll(query, pageable);
    }

    @ApiOperation(value = "Returns the paginated list of Offices instances matching the optional query (q) request param. This API should be used only if the query string is too big to fit in GET request with request param. The request has to made in application/x-www-form-urlencoded format.")
    @RequestMapping(value="/filter", method = RequestMethod.POST, consumes= "application/x-www-form-urlencoded")
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public Page<Offices> filterOffices(@ApiParam("conditions to filter the results") @RequestParam(value = "q", required = false) String query, Pageable pageable) {
        LOGGER.debug("Rendering Offices list by filter", query);
        return officesService.findAll(query, pageable);
    }

    @ApiOperation(value = "Returns downloadable file for the data matching the optional query (q) request param.")
    @RequestMapping(value = "/export/{exportType}", method = RequestMethod.GET, produces = "application/octet-stream")
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public Downloadable exportOffices(@PathVariable("exportType") ExportType exportType, @ApiParam("conditions to filter the results") @RequestParam(value = "q", required = false) String query, Pageable pageable) {
         return officesService.export(exportType, query, pageable);
    }

    @ApiOperation(value = "Returns a URL to download a file for the data matching the optional query (q) request param and the required fields provided in the Export Options.") 
    @RequestMapping(value = "/export", method = {RequestMethod.POST}, consumes = "application/json")
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public StringWrapper exportOfficesAndGetURL(@RequestBody DataExportOptions exportOptions, Pageable pageable) {
        String exportedFileName = exportOptions.getFileName();
        if(exportedFileName == null || exportedFileName.isEmpty()) {
            exportedFileName = Offices.class.getSimpleName();
        }
        exportedFileName += exportOptions.getExportType().getExtension();
        String exportedUrl = exportedFileManager.registerAndGetURL(exportedFileName, outputStream -> officesService.export(exportOptions, pageable, outputStream));
        return new StringWrapper(exportedUrl);
    }

	@ApiOperation(value = "Returns the total count of Offices instances matching the optional query (q) request param.")
	@RequestMapping(value = "/count", method = RequestMethod.GET)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
	public Long countOffices( @ApiParam("conditions to filter the results") @RequestParam(value = "q", required = false) String query) {
		LOGGER.debug("counting Offices");
		return officesService.count(query);
	}

    @ApiOperation(value = "Returns aggregated result with given aggregation info")
	@RequestMapping(value = "/aggregations", method = RequestMethod.POST)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
	public Page<Map<String, Object>> getOfficesAggregatedValues(@RequestBody AggregationInfo aggregationInfo, Pageable pageable) {
        LOGGER.debug("Fetching aggregated results for {}", aggregationInfo);
        return officesService.getAggregatedValues(aggregationInfo, pageable);
    }

    @RequestMapping(value="/{officeCode:.+}/employeeses", method=RequestMethod.GET)
    @ApiOperation(value = "Gets the employeeses instance associated with the given id.")
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public Page<Employees> findAssociatedEmployeeses(@PathVariable("officeCode") String officeCode, Pageable pageable) {

        LOGGER.debug("Fetching all associated employeeses");
        return officesService.findAssociatedEmployeeses(officeCode, pageable);
    }

    /**
	 * This setter method should only be used by unit tests
	 *
	 * @param service OfficesService instance
	 */
	protected void setOfficesService(OfficesService service) {
		this.officesService = service;
	}

}