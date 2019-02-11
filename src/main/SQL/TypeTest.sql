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
    newvarexpdb.dnp3_master;
SELECT 
    *
FROM
    matrikondb.matrikon;

SELECT 
    newConfigTable.variable_id,
    newTagName,
    newConfigTable.source AS newSource,
    oldConfigTable.source AS oldSource
FROM
    (SELECT 
        *,
            TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS newTagName
    FROM
        newvarexpdb.common AS common
    WHERE
        source = '3') AS newConfigTable
        LEFT JOIN
    (SELECT 
        *,
            TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS oldTagName
    FROM
        oldvarexpdb.common AS common
    WHERE
        source = 'O') AS oldConfigTable ON newTagName = oldTagName;

UPDATE resultOutput.resultTable result,
    (SELECT 
        newConfigTable.variable_id,
            newTagName AS tagName,
            newConfigTable.source AS newSource,
            oldConfigTable.source AS oldSource
    FROM
        (SELECT 
        *,
            TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS newTagName
    FROM
        newvarexpdb.common AS common
    WHERE
        source = '3') AS newConfigTable
    LEFT JOIN (SELECT 
        *,
            TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS oldTagName
    FROM
        oldvarexpdb.common AS common
    WHERE
        source = 'O') AS oldConfigTable ON newTagName = oldTagName) Dnp3TypeTable 
SET 
    result.`Type Test` = IF(oldSource = 'O' AND newSource = '3',
        'PASS',
        'FAIL'),
    result.`Comment` = CONCAT(result.`Comment`,
            IF(oldSource = 'O' AND newSource <> '3',
                '
                 Source hasn\'t been updated to \'3\' in new config',
                ''))
WHERE
    result.tagName = Dnp3TypeTable.tagName;


SELECT 
    *
FROM
    resultOutput.resultTable;
