drop table resultoutput.resulttable;

CREATE TABLE resultoutput.`resulttable` (
   `id` int(11) NOT NULL AUTO_INCREMENT,
   `TagName` varchar(45) NOT NULL,
   `Tag Name Test` varchar(45) DEFAULT NULL,
   `Description Test` varchar(45) DEFAULT NULL,
   `Digitals Test` varchar(45) DEFAULT NULL,
   `Units Test` varchar(45) DEFAULT NULL,
   `Analogs Minimum Ratio Test` varchar(45) DEFAULT NULL,
   `Analogs Maximum Ratio Test` varchar(45) DEFAULT NULL,
   `Type Test` varchar(45) DEFAULT NULL,
   `OPC DNP3 Source Test` varchar(45) DEFAULT NULL,
   `Commandable Range Test` varchar(45) DEFAULT NULL,
   `Internal Type Check Test` varchar(45) DEFAULT NULL,
   `Producer Test` varchar(45) DEFAULT NULL,
   `Comment` longtext,
   PRIMARY KEY (`id`)
 );
 
 select * from resultoutput.resulttable;
 #To update comments do something like
 #update table SET credit = credit + 7 where id =X
 
 # Insert all new varexp config into hte result database
 insert into resultoutput.resulttable (TagName) 
 select trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as newTagName
 from newvarexpdb.common;
 
 
 
 insert into resultOutput.resultTable (TagName,`Tag Name Test`)
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
		left join  
		(
			select variable_id as oldVariableId,trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as oldTagName
			from oldvarexpdb.common as common
		order by oldTagName
		) as oldConfigTable
		on oldConfigTable.oldTagName = newConfigTable.newTagName
	order by oldConfigTable.oldTagName asc
	) as tempTable1
	left  join
	(
		select matrikon.matrikon_id as matrikonID,concat(matrikon.matrikon_group_name,".",matrikon_name) as matrikonTagName
	from matrikondb.matrikon as matrikon
	) as matrikonTable
	on tempTable1.newTagName = matrikonTagName
) as tempTable2;


select * from matrikondb.matrikon;
select * from oldvarexpdb.common;
select * from resultoutput.resulttable;

SHOW VARIABLES LIKE "secure_file_priv";
update resulttable set
resulttable = (
	select * from resulttable 
    order by resulttable.TagName asc
);

#This query outputs the result output to a csv file
select "Tag Name","Tag Name Test", "Description Test", "Digitals Test", "Units Test","Analogs Minimum Ratio Test","Analogs Maximum Ratio Test","Type Test" ,"OPC DNP3 Source Test","Commandable Range Test",
	"Internal Type Check Test","Producer Test","Comment"
union all
(
Select `TagName`,`Tag Name Test`, `Description Test`, `Digitals Test`, `Units Test`,`Analogs Minimum Ratio Test`,`Analogs Maximum Ratio Test`,`Type Test` ,`OPC DNP3 Source Test`,`Commandable Range Test`,
	`Internal Type Check Test`,`Producer Test`,`Comment`
into outfile 'C:\\Users\\Stephen\\Documents\\ComparisonTool\\derp.csv'
fields terminated by ','
enclosed by '"'
escaped by '\\'
lines terminated by "\r"
from resultoutput.resulttable
);

select *
from oldvarexpdb.common as common
where
	trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',
    common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) = 'SHILO.ST.DG1.MMXU2.A'
;