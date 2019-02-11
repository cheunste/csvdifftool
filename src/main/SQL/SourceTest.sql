 /*
 This is for testing the digitals

This matches the digital columns between the new configuration and hte old configureation. Specifically, coluns 40,41,42,43,45 and 46 must match between the old configuration and the new configuration
 */         
#update outputvarexpdb.result set Comment=concat(Comment, "[Comment]") where id =480;
select * from resultOutput.resultTable;


select * from
(
	select commonTable.variable_id, commonTable.source, commonTable.tagName, concat(dnp3_master_device_name,'.',lpad(dnp3_master_type,3,'0'), '.',dnp3_master_AOB_PointVariation,".",dnp3_master_point_address) as newConfigItemPath from
	(
		select source,variable_id,trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as tagName 
			from newvarexpdb.common as common
			where 
				source = "3" or source="O"
	) as commonTable
	inner join
	(
		select *
			from newvarexpdb.dnp3_master
	) as dnp3Table
	on commonTable.variable_id = dnp3Table.dnp3_master_variable_id
) as newConfigTable

left join
(
	select concat(matrikon_group_name,'.',matrikon_name) as matrikonTag, matrikon_item_path as itemPath from matrikondb.matrikon
) as matrikonTable
on newConfigTable.tagName = matrikonTable.matrikonTag;


#Update the resultOutput DB's result table's DescriptionTest Column
UPDATE resultOutput.resultTable result,
    (SELECT 
        *
    FROM
        (SELECT 
        commonTable.variable_id,
            commonTable.source,
            commonTable.tagName,
            CONCAT(dnp3_master_device_name, '.', LPAD(dnp3_master_type, 3, '0'), '.', dnp3_master_AOB_PointVariation, '.', dnp3_master_point_address) AS newConfigItemPath
    FROM
        (SELECT 
        source,
            variable_id,
            TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName
    FROM
        newvarexpdb.common AS common
    WHERE
        source = '3' OR source = 'O') AS commonTable
    INNER JOIN (SELECT 
        *
    FROM
        newvarexpdb.dnp3_master) AS dnp3Table ON commonTable.variable_id = dnp3Table.dnp3_master_variable_id) AS newConfigTable
    INNER JOIN (SELECT 
        CONCAT(matrikon_group_name, '.', matrikon_name) AS matrikonTag,
            matrikon_item_path AS itemPath
    FROM
        matrikondb.matrikon) AS matrikonTable ON newConfigTable.tagName = matrikonTable.matrikonTag) SourceTable 
SET 
    result.`OPC DNP3 Source Test` = IF(newConfigItemPath = itemPath,
        'PASS',
        'FAIL'),
    result.`Comment` = IF(newConfigItemPath <> itemPath,
        CONCAT(result.`Comment`,
                '
                 SEL item path does not match for tag'),
        result.`Comment`)
WHERE
    result.tagName = SourceTable.tagName;

select * from resultOutput.resultTable;
