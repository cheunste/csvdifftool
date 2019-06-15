/*
This SQL query finds tags that are not in the newconfig file, but do exist in either the old config file or the matrikon config file.

It then inserts them into the result table database.

THIS MUST BE DONE AFTER ALL OTHER TESTS

*/

insert into resultOutput.resultTable (TagName,
	`Tag Name Test`,`Description Test`,`Digitals Test`,
    `Units Test`,`Analogs Minimum Ratio Test`,`Analogs Maximum Ratio Test`,
    `Type Test`,`OPC DNP3 Source Test`,`Commandable Range Test`,
    `Internal Type Check Test`,`Producer Test`,`Comment`)
Select if(oldTagName<=>null,matrikonTagName,oldTagName),'FAIL','','','','','','','','','','',IF(oldTagName <=> NULL,'Tag only exists in Matrikon config. No further tests conducted','Tag only exists in old config. No further tests conducted') from
(	
    SELECT 
    *
    FROM
    (SELECT 
        newConfigTable.newVariableId,
            oldConfigTable.oldVariableId,
            newConfigTable.newTagName,
            oldConfigTable.oldTagName
    FROM
        (SELECT 
        variable_id AS newVariableId,
            TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS newTagName
    FROM
        newvarexpdb.common AS common
    ORDER BY newTagName) AS newConfigTable
    LEFT JOIN (SELECT 
        variable_id AS oldVariableId,
            TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS oldTagName
    FROM
        oldvarexpdb.common AS common
    ORDER BY oldTagName) AS oldConfigTable ON oldConfigTable.oldTagName = newConfigTable.newTagName UNION SELECT 
        newConfigTableR.newVariableId,
            oldConfigTableR.oldVariableId,
            newConfigTableR.newTagName,
            oldConfigTableR.oldTagName
    FROM
        (SELECT 
        variable_id AS newVariableId,
            TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS newTagName
    FROM
        newvarexpdb.common AS common
    ORDER BY newTagName) AS newConfigTableR
    RIGHT JOIN (SELECT 
        variable_id AS oldVariableId,
            TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS oldTagName
    FROM
        oldvarexpdb.common AS common
    ORDER BY oldTagName) AS oldConfigTableR ON oldConfigTableR.oldTagName = newConfigTableR.newTagName) AS tempTable1
        LEFT JOIN
    (SELECT 
        matrikon.matrikon_id AS matrikonID,
            CONCAT(matrikon.matrikon_group_name, '.', matrikon_name) AS matrikonTagName
    FROM
        matrikondb.matrikon AS matrikon) AS matrikonTable ON tempTable1.newTagName = matrikonTagName
        WHERE newTagName<=>null
) as missingTagTable;