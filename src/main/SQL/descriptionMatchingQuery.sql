 /*
 This is for matching description

This matches the description between the new config, old config 

Uses the following columns:
PcVue:  10,11
 */         
#update outputvarexpdb.result set Comment=concat(Comment, "[Comment]") where id =480;
select * from resultOutput.resultTable;

#select newConfigTable.tagName, oldConfigTable.desc_1st_lang as oldDescLang1, newConfigTable.desc_1st_lang as newDescLang1,
#	oldConfigTable.desc_2nd_lang as oldDescLang2, newConfigTable.desc_2nd_lang as newDescLang2 ;


select newConfigTable.tagName, newConfigTable.desc_1st_lang, newConfigTable.desc_2nd_lang, oldConfigTable.desc_1st_lang,oldConfigTable.desc_2nd_lang from
(
	select variable_id, desc_1st_lang, desc_2nd_lang, 
		trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th))  as tagName
	from oldvarexpdb.common as common
	group by tagName 
) as oldConfigTable
right join
(
	select variable_id, desc_1st_lang, desc_2nd_lang, 
		trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th))  as tagName
	from newvarexpdb.common as common
	group by tagName 	
) as newConfigTable
on oldConfigTable.tagName = newConfigTable.tagName;


#Update the resultOutput DB's result table's DescriptionTest Column
Update resultOutput.resultTable result,
(
	select newConfigTable.tagName, newConfigTable.desc_1st_lang as newConfigDesc1, newConfigTable.desc_2nd_lang as newConfigDesc2, oldConfigTable.desc_1st_lang as oldConfigDesc1,oldConfigTable.desc_2nd_lang as oldConfigDesc2 from
	(
	select variable_id, desc_1st_lang, desc_2nd_lang, 
			trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th))  as tagName
		from oldvarexpdb.common as common
		group by tagName 
        order by tagName asc
	) as oldConfigTable
	right join
	(
		select variable_id, desc_1st_lang, desc_2nd_lang, 
			trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th))  as tagName
		from newvarexpdb.common as common
		group by tagName 
        order by tagName asc	
	) as newConfigTable
	on oldConfigTable.tagName = newConfigTable.tagName
) DescriptionTable
set result.`Description Test` = if(DescriptionTable.newConfigDesc1 =DescriptionTable.oldConfigDesc1 and DescriptionTable.newConfigDesc2 =DescriptionTable.oldConfigDesc2,'PASS','FAIL'),
    result.`Comment` = if(newConfigDesc1 != oldConfigDesc1, concat(result.`Comment`,"\n New Tag description (10) doesn't match old config's description"), result.`Comment`),
    result.`Comment` = if(newConfigDesc2 != oldConfigDesc2, concat(result.`Comment`,"\n New Tag description lang2 (11) doesn't match old config's description (lang2)"), result.`Comment`)
where result.tagName = DescriptionTable.tagName;


select * from resultOutput.resultTable;
