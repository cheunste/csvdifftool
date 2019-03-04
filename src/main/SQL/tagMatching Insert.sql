/*
This is the query for matching tag names as well as inserting the tag names into the Result Database

Checks to see if the tagName matches between all three databases, then the result is thrown into the Result database.

That being said, it really should only check the matrikon tag if the PcVue tag is DNP3 or OPC

Update rule exceptions:

Excludes checking Internal and Equipment variables
*/                 
select * from outputvarexpdb.result;


select * from
(
	select *
	from
	(
		select newConfigTable.newVariableId, oldConfigTable.oldVariableId,newConfigTable.newTagName, oldConfigTable.oldTagName
		from
		(
			select variable_id as newVariableId,trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as newTagName
			from newvarexpdb.common as common
		order by newTagName
		) as newConfigTable
		left join  
		(
			select variable_id as oldVariableId,trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as oldTagName
			from oldvarexpdb.common as common
		order by oldTagName
		) as oldConfigTable
		on oldConfigTable.oldTagName = newConfigTable.newTagName
	order by oldConfigTable.oldTagName asc
	) as tempTable1
	left join
	(
		select matrikon.matrikon_id as matrikonID,concat(matrikon.matrikon_group_name,".",matrikon_name) as matrikonTagName
	from matrikondb.matrikon as matrikon
	) as matrikonTable
	on tempTable1.newTagName = matrikonTagName

) as tempTable2
;



#Insert into result
insert into resultoutput.resulttable ( tagName)
#select newTagName,if(newTagName=oldTagName and newTagName=matrikonTagName,'PASS','FAIL') as remark, "" from
select newTagName,if(newTagName=oldTagName and newTagName=matrikonTagName,'PASS','FAIL') as remark  from
(
	select *
	from
	(
		select newConfigTable.newVariableId, oldConfigTable.oldVariableId,newConfigTable.newTagName, oldConfigTable.oldTagName
		from
		(
			select variable_id as newVariableId,trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as newTagName
			from newvarexpdb.common as common
		order by newTagName
		) as newConfigTable
		inner join  
		(
			select variable_id as oldVariableId,trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as oldTagName
			from oldvarexpdb.common as common
		order by oldTagName
		) as oldConfigTable
		on oldConfigTable.oldTagName = newConfigTable.newTagName
	order by oldConfigTable.oldTagName asc
	) as tempTable1
	inner join
	(
		select matrikon.matrikon_id as matrikonID,concat(matrikon.matrikon_group_name,".",matrikon_name) as matrikonTagName
	from matrikondb.matrikon as matrikon
	) as matrikonTable
	on tempTable1.newTagName = matrikonTagName
) as tempTable2
;


#This query fills up the Tag Name column with values from the new varexpdb config. SHOULD ONLY BE USED ON AN EMPTY TABLE
insert into resultOutput.resultTable (TagName,
	`Tag Name Test`,`Description Test`,`Digitals Test`,
    `Units Test`,`Analogs Minimum Ratio Test`,`Analogs Maximum Ratio Test`,
    `Type Test`,`OPC DNP3 Source Test`,`Commandable Range Test`,
    `Internal Type Check Test`,`Producer Test`,`Comment`)

select newTagName,'','','','','','','','','','','','' from
(
	select *
	from
	(
		select newConfigTable.newVariableId, oldConfigTable.oldVariableId,newConfigTable.newTagName, oldConfigTable.oldTagName
		from
		(
			select variable_id as newVariableId,trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as newTagName
			from newvarexpdb.common as common
		order by newTagName
		) as newConfigTable
		left join  
		(
			select variable_id as oldVariableId,trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as oldTagName
			from oldvarexpdb.common as common
		order by oldTagName
		) as oldConfigTable
		on oldConfigTable.oldTagName = newConfigTable.newTagName
	order by oldConfigTable.oldTagName asc
	) as tempTable1
	left join
	(
		select matrikon.matrikon_id as matrikonID,concat(matrikon.matrikon_group_name,".",matrikon_name) as matrikonTagName
	from matrikondb.matrikon as matrikon
	) as matrikonTable
	on tempTable1.newTagName = matrikonTagName
) as tempTable2;
    
select * from resulttable;

# This inserts both TagName and fills out the tagName test
insert into resultOutput.resultTable (TagName,`Tag Name Test`)
select newTagName,if(newTagName=oldTagName and newTagName=matrikonTagName,'PASS','FAIL') as remark  from
(
	select *
	from
	(
		select newConfigTable.newVariableId, oldConfigTable.oldVariableId,newConfigTable.newTagName, oldConfigTable.oldTagName
		from
		(
			select variable_id as newVariableId,trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as newTagName
			from newvarexpdb.common as common
		order by newTagName
		) as newConfigTable
		left join  
		(
			select variable_id as oldVariableId,trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as oldTagName
			from oldvarexpdb.common as common
		order by oldTagName
		) as oldConfigTable
		on oldConfigTable.oldTagName = newConfigTable.newTagName
	order by oldConfigTable.oldTagName asc
	) as tempTable1
	left join
	(
		select matrikon.matrikon_id as matrikonID,concat(matrikon.matrikon_group_name,".",matrikon_name) as matrikonTagName
	from matrikondb.matrikon as matrikon
	) as matrikonTable
	on tempTable1.newTagName = matrikonTagName
) as tempTable2;

#This is the Tag Name Test. This tests to see if all three variables exists and fill out the comment column if test fails
	select *
	from
	(
		select newConfigTable.newVariableId, oldConfigTable.oldVariableId,newConfigTable.newTagName, oldConfigTable.oldTagName
		from
		(
			select variable_id as newVariableId,trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as newTagName
			from newvarexpdb.common as common
		order by newTagName
		) as newConfigTable
		left join  
		(
			select variable_id as oldVariableId,trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as oldTagName
			from oldvarexpdb.common as common
		order by oldTagName
		) as oldConfigTable
		on oldConfigTable.oldTagName = newConfigTable.newTagName
	order by oldConfigTable.oldTagName asc
	) as tempTable1
	left join
	(
		select matrikon.matrikon_id as matrikonID,concat(matrikon.matrikon_group_name,".",matrikon_name) as matrikonTagName
	from matrikondb.matrikon as matrikon
	) as matrikonTable
	on tempTable1.newTagName = matrikonTagName;
    
Update resultOutput.resultTable result,
(
	select * 
    from
	(
		select newConfigTable.newVariableId, oldConfigTable.oldVariableId, newConfigTable.newSource, oldConfigTable.oldSource, newConfigTable.newTagName, oldConfigTable.oldTagName
		from
		(
			select variable_id as newVariableId, source as newSource, trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as newTagName
			from newvarexpdb.common as common
		order by newTagName
		) as newConfigTable
		left join  
		(
			select variable_id as oldVariableId, source as oldSource, trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as oldTagName
			from oldvarexpdb.common as common
		order by oldTagName
		) as oldConfigTable
		on oldConfigTable.oldTagName = newConfigTable.newTagName
	order by oldConfigTable.oldTagName asc
	) as tempTable1
	left join
	(
		select matrikon.matrikon_id as matrikonID,concat(matrikon.matrikon_group_name,".",matrikon_name) as matrikonTagName
	from matrikondb.matrikon as matrikon
	) as matrikonTable
	on tempTable1.newTagName = matrikonTagName
) as TagNameTable
set 
	result.`Tag Name Test` = if((newSource like "E" OR newSource like "I") OR (newTagName = oldTagName and newTagName = matrikonTagName) ,'PASS','FAIL'),	
    result.`Comment` = if(matrikonTagName is null, concat(result.`Comment`,"\nTag doesn't exist in matrikon config"), result.`Comment`),
    result.`Comment` = if(oldTagName is null, concat(result.`Comment`,"\nTag doesn't exist in old config"), result.`Comment`),
    result.`Comment` = if(newTagName != oldTagName and oldTagName is not null,concat(result.`Comment`,"\nNew Tag doesn't match with old config"), result.`Comment`),
    result.`Comment` = if(newTagName != matrikonTagName and matrikonTagName is not null,concat(result.`Comment`,"\nNew Tag doesn't match with matrikon config"), result.`Comment`)
where result.tagName = TagNameTable.newTagName;

select * from resultoutput.resulttable;

#Comment command
#result.`Comment` = if(, concat(result.`Comment`,"\n"), result.`Comment`),
#set result.`Tag Name Test` = if(newTagName = oldTagName and newTagName = matrikonTagName ,'PASS','FAIL'),
#	result.`Comment` = concat (result.`Comment`, if (oldTagName is null, '\ntag doesn\'t exist in old config', 'N/A'),
#    result.`Comment` = concat (result.`Comment`, if (matrikonTagName is null, ']ntag doesn\'t exist in matrikon config', 'N/A')
