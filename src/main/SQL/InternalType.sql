 /*
 This is for testing the digitals

This matches the digital columns between the new configuration and hte old configureation. Specifically, coluns 40,41,42,43,45 and 46 must match between the old configuration and the new configuration
 */         
#update outputvarexpdb.result set Comment=concat(Comment, "[Comment]") where id =480;
select * from resultOutput.resultTable;
select * from newvarexpdb.ctv;
select * from oldvarexpdb.ctv;
select * from oldvarexpdb.common inner join oldvarexpdb.ctv on common.variable_id = ctv.ctv_variable_id;

Select newCommon.tagName as tagName, oldCommon.tagName as oldTagName, newCommon.source as newSource, oldCommon.source as oldSource, if(newCOmmon.source = oldCommon.source,"PASS","FAIL") as result
from 
(
	select common.variable_id, common.source, trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as tagName 
		from newvarexpdb.common as common
		where source="I"
) as newCommon
inner join
(
	select common.variable_id, common.source, trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as tagName 
		from oldvarexpdb.common as common
		where source="I"
) as oldCommon
on oldCommon.tagName = newCommon.tagName;

/*
Update the resultOutput DB's result table's analog (commandable range) column 

Note that this only works for CTV variables and there could be a chance that they do not exist in the newvarexp
*/
Update resultOutput.resultTable result,
(
	Select newTagName, oldTagName, newCommon.source as newSource, oldCommon.source as oldSource
	from 
	(
		select common.variable_id, common.source, trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as newTagName 
			from newvarexpdb.common as common
			where source="I"
	) as newCommon
	left join
	(
		select common.variable_id, common.source, trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as oldTagName 
			from oldvarexpdb.common as common
			where source="I"
	) as oldCommon
	on oldTagName = newTagName
	
) InternalTable
set result.`Internal Type Check Test` = if((newTagName is not null) and (oldTagName is not null),'PASS','FAIL'),
	result.`Comment`= concat(result.`Comment`,
		if( (newTagName is null) or (oldTagName is null), "\n The internal tags don't match",'')
)
where result.tagName = InternalTable.newTagName;


select * from resultOutput.resultTable;

