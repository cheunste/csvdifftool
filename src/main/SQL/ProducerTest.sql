 /*
 This is for testing the digitals

This matches the digital columns between the new configuration and hte old configureation. Specifically, coluns 40,41,42,43,45 and 46 must match between the old configuration and the new configuration
 */         
#update outputvarexpdb.result set Comment=concat(Comment, "[Comment]") where id =480;
select * from resultOutput.resultTable;
select regexp_substr(common.topology_server, '[:alpha:]+') as temp, common.source, trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) 
				as tagName from newvarexpdb.common as common;
select * from newvarexpdb.common as common;
-- select newTable.tagName as TagName, 
-- 	newTable.topology_server as newProducer, newTable.source as newSource,
-- 	oldTable.topology_server as oldProducer, oldTable.source as oldSource

select *
from  
(
	select
		regexp_substr(common.topology_server, '[:alpha:]+') as temp,
        regexp_substr(common.topology_client, '[:digit:]+') as temp2,
		common.topology_server, 
        common.topology_client,
		common.variable_id, 
		common.source, trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) 
				as tagName
			from newvarexpdb.common as common
) as newTable
left join
(
	select common.topology_server, 
		common.topology_client,
		common.variable_id, 
		common.source, trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) 
				as tagName 
			from oldvarexpdb.common as common
) as oldTable
on newTable.tagName = oldTable.tagName;

select common.*, regexp_substr(common.topology_server, '[[:digit:]]+') as temp from newvarexpdb.common as common;

/*
Update the resultOutput DB's result table's analog (commandable range) column 

Note that this only works for CTV variables and there could be a chance that they do not exist in the newvarexp
*/
UPDATE resultOutput.resultTable result,
    (SELECT 
        newTable.tagName AS TagName,
            newTable.topology_server AS newProducer,
            newTable.source AS newSource,
            newTable.topology_client AS newClient,
            oldTable.topology_server AS oldProducer,
            oldTable.source AS oldSource,
            oldTable.topology_client AS oldClient
    FROM
        (SELECT 
        REGEXP_SUBSTR(common.topology_server, '[[:digit:]]+') AS temp,
            common.topology_client,
            common.topology_server,
            common.variable_id,
            common.source,
            TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName
    FROM
        newvarexpdb.common AS common) AS newTable
    LEFT JOIN (SELECT 
        common.topology_server,
            common.topology_client,
            common.variable_id,
            common.source,
            TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName
    FROM
        oldvarexpdb.common AS common) AS oldTable ON newTable.tagName = oldTable.tagName) ProducerTable 
SET 
    result.`Producer Test` = IF(newSource = 'I',
        IF(newSource = oldSource,
            'PASS',
            'FAIL'),
        IF(REGEXP_SUBSTR(newProducer, '[:alpha:]+') = 'ST'
                AND REGEXP_SUBSTR(newClient, '[:digit:]+') = REGEXP_SUBSTR(oldClient, '[:digit:]+'),
            'PASS',
            'FAIL')),
    result.`Comment` = CONCAT(result.`Comment`,
            IF(REGEXP_SUBSTR(newClient, '[:digit:]+') <> REGEXP_SUBSTR(oldClient, '[:digit:]+'),
                '
                 Check Producer. Stations does not match ',
                ''),
            IF(newSource = 'I'
                    AND newSource <> oldSource,
                '
                 Source types does not match',
                ''))
WHERE
    result.tagName = ProducerTable.tagName;


select * from resultOutput.resultTable;