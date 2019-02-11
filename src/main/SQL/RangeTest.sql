 /*
 This is for testing the digitals

This matches the digital columns between the new configuration and hte old configureation. Specifically, coluns 40,41,42,43,45 and 46 must match between the old configuration and the new configuration
 */         
#update outputvarexpdb.result set Comment=concat(Comment, "[Comment]") where id =480;
select * from resultOutput.resultTable;
select * from newvarexpdb.ctv;
select * from oldvarexpdb.ctv;
select * from oldvarexpdb.common inner join oldvarexpdb.ctv on common.variable_id = ctv.ctv_variable_id;

Select commonTable.variable_id, ctvTable.ctv_Minimium_display_value, ctvTable.ctv_Maximum_display_value, ctvTable.ctv_Minimum_control_value, ctvTable.ctv_maximum_control_value 
from
(
	select * 
		from newvarexpdb.common
		where source = "CTV"
) as commonTable

inner join

(
	select *
		from newvarexpdb.ctv
) as ctvTable
on ctvTable.ctv_variable_id = commonTable.variable_id;

/*
Update the resultOutput DB's result table's analog (commandable range) column 

Note that this only works for CTV variables and there could be a chance that they do not exist in the newvarexp
*/
UPDATE resultOutput.resultTable result,
    (SELECT 
        commonTable.tagName,
            commonTable.variable_id,
            ctvTable.ctv_Minimium_display_value AS minDisplayValue,
            ctvTable.ctv_Maximum_display_value AS maxDisplayValue,
            ctvTable.ctv_Minimum_control_value AS minControlValue,
            ctvTable.ctv_maximum_control_value AS maxControlValue
    FROM
        (SELECT 
        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,
            common.*
    FROM
        newvarexpdb.common AS common
    WHERE
        source = 'CTV') AS commonTable
    INNER JOIN (SELECT 
        *
    FROM
        newvarexpdb.ctv) AS ctvTable ON ctvTable.ctv_variable_id = commonTable.variable_id) RangeTable 
SET 
    result.`Commandable Range Test` = IF(minControlValue = minDisplayValue
            AND maxDisplayValue = maxControlValue,
        'PASS',
        'FAIL'),
    result.`Comment` = CONCAT(result.`Comment`,
            IF(minControlValue <> minDisplayValue,
                '
                 min commandable ranges does not match',
                ''),
            IF(maxDisplayValue <> maxControlValue,
                '
                 max commandable ranges does not match',
                ''))
WHERE
    result.tagName = RangeTable.tagName;


select * from resultOutput.resultTable;
