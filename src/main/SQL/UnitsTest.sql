/*
 This is for testing the digitals

This matches the digital columns between the new configuration and hte old configureation. Specifically, coluns 40,41,42,43,45 and 46 must match between the old configuration and the new configuration
 */         
SELECT 
    *
FROM
    resultOutput.resultTable;

SELECT 
    *
FROM
    (SELECT 
        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,
            variable_id,
            reg.reg_Measurement_Units
    FROM
        oldvarexpdb.common AS common
    RIGHT JOIN oldvarexpdb.reg AS reg ON reg_variable_id
    WHERE
        reg.reg_variable_id = common.variable_id UNION ALL SELECT 
        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,
            variable_id,
            ctv.ctv_Measurement_Units
    FROM
        oldvarexpdb.common AS common
    RIGHT JOIN oldvarexpdb.ctv AS ctv ON ctv_variable_id
    WHERE
        ctv.ctv_variable_id = common.variable_id UNION ALL SELECT 
        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,
            variable_id,
            cnt.cnt_Measurement_Units
    FROM
        oldvarexpdb.common AS common
    RIGHT JOIN oldvarexpdb.cnt AS cnt ON cnt_variable_id
    WHERE
        cnt.cnt_variable_id = common.variable_id UNION ALL SELECT 
        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,
            variable_id,
            chr.chr_Measurement_Units
    FROM
        oldvarexpdb.common AS common
    RIGHT JOIN oldvarexpdb.chr AS chr ON chr_variable_id
    WHERE
        chr.chr_variable_id = common.variable_id) AS old_table
        RIGHT JOIN
    (SELECT 
        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,
            variable_id,
            reg.reg_Measurement_Units
    FROM
        newvarexpdb.common AS common
    RIGHT JOIN newvarexpdb.reg AS reg ON reg_variable_id
    WHERE
        reg.reg_variable_id = common.variable_id UNION ALL SELECT 
        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,
            variable_id,
            ctv.ctv_Measurement_Units
    FROM
        newvarexpdb.common AS common
    RIGHT JOIN newvarexpdb.ctv AS ctv ON ctv_variable_id
    WHERE
        ctv.ctv_variable_id = common.variable_id UNION ALL SELECT 
        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,
            variable_id,
            cnt.cnt_Measurement_Units
    FROM
        newvarexpdb.common AS common
    RIGHT JOIN newvarexpdb.cnt AS cnt ON cnt_variable_id
    WHERE
        cnt.cnt_variable_id = common.variable_id UNION ALL SELECT 
        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,
            variable_id,
            chr.chr_Measurement_Units
    FROM
        newvarexpdb.common AS common
    RIGHT JOIN newvarexpdb.chr AS chr ON chr_variable_id
    WHERE
        chr.chr_variable_id = common.variable_id) AS new_table ON old_table.tagName = new_table.tagName;

UPDATE resultOutput.resultTable result,
    (SELECT 
        new_table.tagName AS tagName,
            old_table.reg_Measurement_Units AS MeasurementUnits1,
            new_table.reg_Measurement_Units AS MeasurementUnits2
    FROM
        (SELECT 
        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,
            variable_id,
            reg.reg_Measurement_Units
    FROM
        oldvarexpdb.common AS common
    RIGHT JOIN oldvarexpdb.reg AS reg ON reg_variable_id
    WHERE
        reg.reg_variable_id = common.variable_id UNION ALL SELECT 
        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,
            variable_id,
            ctv.ctv_Measurement_Units
    FROM
        oldvarexpdb.common AS common
    RIGHT JOIN oldvarexpdb.ctv AS ctv ON ctv_variable_id
    WHERE
        ctv.ctv_variable_id = common.variable_id UNION ALL SELECT 
        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,
            variable_id,
            cnt.cnt_Measurement_Units
    FROM
        oldvarexpdb.common AS common
    RIGHT JOIN oldvarexpdb.cnt AS cnt ON cnt_variable_id
    WHERE
        cnt.cnt_variable_id = common.variable_id UNION ALL SELECT 
        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,
            variable_id,
            chr.chr_Measurement_Units
    FROM
        oldvarexpdb.common AS common
    RIGHT JOIN oldvarexpdb.chr AS chr ON chr_variable_id
    WHERE
        chr.chr_variable_id = common.variable_id) AS old_table
    RIGHT JOIN (SELECT 
        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,
            variable_id,
            reg.reg_Measurement_Units
    FROM
        newvarexpdb.common AS common
    RIGHT JOIN newvarexpdb.reg AS reg ON reg_variable_id
    WHERE
        reg.reg_variable_id = common.variable_id UNION ALL SELECT 
        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,
            variable_id,
            ctv.ctv_Measurement_Units
    FROM
        newvarexpdb.common AS common
    RIGHT JOIN newvarexpdb.ctv AS ctv ON ctv_variable_id
    WHERE
        ctv.ctv_variable_id = common.variable_id UNION ALL SELECT 
        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,
            variable_id,
            cnt.cnt_Measurement_Units
    FROM
        newvarexpdb.common AS common
    RIGHT JOIN newvarexpdb.cnt AS cnt ON cnt_variable_id
    WHERE
        cnt.cnt_variable_id = common.variable_id UNION ALL SELECT 
        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,
            variable_id,
            chr.chr_Measurement_Units
    FROM
        newvarexpdb.common AS common
    RIGHT JOIN newvarexpdb.chr AS chr ON chr_variable_id
    WHERE
        chr.chr_variable_id = common.variable_id) AS new_table ON old_table.tagName = new_table.tagName) UnitsTable 
SET 
    result.`Units Test` = IF(UnitsTable.MeasurementUnits1 <=> UnitsTable.MeasurementUnits2,
        'PASS',
        'FAIL'),
    result.`Comment` = CONCAT(result.`Comment`,
            IF(UnitsTable.MeasurementUnits1 <> UnitsTable.MeasurementUnits2,
                '
                 Units do not match between new and old configs',
                ''))
WHERE
    result.tagName = UnitsTable.tagName;


SELECT 
    *
FROM
    resultOutput.resultTable;
