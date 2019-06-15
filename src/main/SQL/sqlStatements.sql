CREATE DATABASE matrikondb;
drop database matrikondb;
drop table matrikon;

CREATE TABLE statDist(
id int unsigned primary key auto_increment,
date text(50) not null,
dist21 int not null,
dist32 int not null,
dist43 int not null,
dist54 int not null,
megaball int not null
);

CREATE TABLE matrikon( 
                matrikon_id int unsigned primary key auto_increment not null, 
                matrikon_group_name TEXT(50) NULL,
                matrikon_name TEXT(50) NULL,
                matrikon_item_path TEXT(50) NULL,

                matrikon_dataType TINYINT NULL,
                matrikon_readOnly TINYINT NULL,
                matrikon_pollAlways TINYINT NULL,
                matrikon_updateRate SMALLINT NULL,
                
                matrikon_scaling TINYINT NULL,
                matrikon_hiRaw   SMALLINT NULL,
                matrikon_lowRaw  SMALLINT NULL,
                matrikon_hiScaled  SMALLINT NULL,
                
                matrikon_loScaled  SMALLINT NULL
                );
                
                select * from matrikon;
                select * from matrikondb;
				INSERT INTO matrikon VALUES(null,'SHILO.ST.DB1.MMXU1','TotVAr','SEL.030.0.1',5,1,1,3000,1,32767,-32767,327.67,-327.67);
				INSERT INTO matrikon VALUES(0,'SHILO.ST.DB1.MMXU1','A','SEL.030.0.2',5,1,1,3000,1,32767,-32767,32767,-32767);
                
                select * from matrikondb.matrikon group by matrikon_group_name asc;
                select concat(matrikon_group_name, '.',matrikon_name) as matrikon_tag from matrikondb.matrikon;
                select * from oldvarexpdb.common;
                select old_variable_id,concat(1st_element,'.',2nd_element,'.',3rd_element,'.',4th_element,'.',5th_element,'.',6th_element,'.',7th_to_12th) as oldVarexpTagName from oldvarexpdb.common;
                select new_ variable_id,concat(1st_element,'.',2nd_element,'.',3rd_element,'.',4th_element,'.',5th_element,'.',6th_element,'.',7th_to_12th) as newVarexpTagName from newVarexpdb.common;
                
                
                select * from newvarexpdb.common;
                 select 1st_element,2nd_element,3rd_element,4th_element,5th_element,6th_element,7th_to_12th from newvarexpdb.common;

/*
Step 0: Matching Lines in the Spreadsheet (aka Number of entries in the DB). They must be equal...if not, throw a warning
*/

Select
(
	select count(*) from newvarexpdb.common
)as newTableEntries,
(
	select count(*) from oldvarexpdb.common
) as oldTableEntries,
(
	select count(*) from matrikondb.matrikon
) as matrikonEntries;

/*
This is for any update operation on the outputvarexpdb
*/
#  An example on how to update
update outputvarexpdb.common output
join newvarexpdb.common newConfig
on output.variable_id = newConfig.variable_id
set output.variable_id = newConfig.variable_id,
output.1st_element = newConfig.1st_element,
output.2nd_element = newConfig.2nd_element,
output.3rd_element = newConfig.3rd_element,
output.4th_element = newConfig.4th_element,
output.5th_element = newConfig.5th_element,
output.6th_element = newConfig.6th_element,
output.7th_to_12th= newConfig.7th_to_12th
;
/*
This is for matching tag names
*/                 

	#select tempTable1.tagName, matrikonTable.matrikonTagName,tempTable1.oldVariableId, tempTable1.newVariableId as newVariableID, matrikonTable.matrikonID from
    alter table  outputvarexpdb.common
    add column fullTagName varchar(50) AFTER reserved5;
    
    insert into
		outputvarexpdb.common
    select
		tempTable1.*
    from
    (
		select newConfigTable.*
		from
		(
			select *,trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as newTagName
			from newvarexpdb.common as common
            group by newTagName
		) as newConfigTable
		inner join  
		(
			select *,trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as oldTagName
			from oldvarexpdb.common as common
            group by oldTagName
		) as oldConfigTable
		on oldConfigTable.oldTagName = newConfigTable.newTagName
        group by oldConfigTable.oldTagName 
    ) as tempTable1
    inner join
    (
		select matrikon.matrikon_id as matrikonID,concat(matrikon.matrikon_group_name,".",matrikon_name) as matrikonTagName
        from matrikondb.matrikon as matrikon
    ) as matrikonTable
    on tempTable1.newTagName = matrikonTable.matrikonTagName
    group by tempTable1.newTagName;

 /*
 This is for matching description
 TODO: This is non unique. you need to match tags as well
 */         
update outputvarexpdb.common output
join 
(
	select oldConfigTable.tagName, oldConfigTable.desc_1st_lang as oldDescLang1, newConfigTable.desc_1st_lang as newDescLang1,
		oldConfigTable.desc_2nd_lang as oldDescLang2, newConfigTable.desc_2nd_lang as newDescLang2 
	from
	(
		select variable_id, desc_1st_lang, desc_2nd_lang, 
			trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th))  as tagName
		from oldvarexpdb.common as common
		group by tagName asc
	) as oldConfigTable
	inner join
	(
		select variable_id, desc_1st_lang, desc_2nd_lang, 
			trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th))  as tagName
		from newvarexpdb.common as common
		group by tagName asc	
	) as newConfigTable
	on oldConfigTable.tagName = newConfigTable.tagName
    group by oldConfigTable.tagName
) as derp
on derp.tagName = output.fullTagName
set
	output.desc_1st_lang = derp.oldDescLang1,
    output.desc_2nd_lang = derp.oldDescLang2
;

select * from outputvarexpdb.bit;
select * from newvarexpdb.ala;


/*
This query is for the Digital items (ALA, BIT, CMD, etc)
*/


/*
This query is for Units
1) Merge the common table with the respective table in either the REG , CTV, NT or CHR fields
2) find the "Measurement Units" field
*/

select * from
(
	select trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as tagName,variable_id, reg.reg_Measurement_Units
	from oldvarexpdb.common as common
		right join oldvarexpdb.reg as reg on reg_variable_id
	    where 
		reg.reg_variable_id = common.variable_id

	union all

	select trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as tagName,variable_id, ctv.ctv_Measurement_Units
	from oldvarexpdb.common as common
		right join oldvarexpdb.ctv as ctv on ctv_variable_id
	    where 
		ctv.ctv_variable_id = common.variable_id

	union all

	select trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as tagName,variable_id, cnt.cnt_Measurement_Units
	from oldvarexpdb.common as common
		right join oldvarexpdb.cnt as cnt on cnt_variable_id
	    where 
		cnt.cnt_variable_id = common.variable_id

	union all

	select trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as tagName,variable_id, chr.chr_Measurement_Units
	from oldvarexpdb.common as common
		right join oldvarexpdb.chr as chr on chr_variable_id
	    where 
		chr.chr_variable_id = common.variable_id
) as old_table

inner join 
(
	select trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as tagName,variable_id, reg.reg_Measurement_Units
	from newvarexpdb.common as common
		right join newvarexpdb.reg as reg on reg_variable_id
	    where 
		reg.reg_variable_id = common.variable_id

	union all

	select trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as tagName,variable_id, ctv.ctv_Measurement_Units
	from newvarexpdb.common as common
		right join newvarexpdb.ctv as ctv on ctv_variable_id
	    where 
		ctv.ctv_variable_id = common.variable_id

	union all

	select trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as tagName,variable_id, cnt.cnt_Measurement_Units
	from newvarexpdb.common as common
		right join newvarexpdb.cnt as cnt on cnt_variable_id
	    where 
		cnt.cnt_variable_id = common.variable_id

	union all

	select trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as tagName,variable_id, chr.chr_Measurement_Units
	from newvarexpdb.common as common
		right join newvarexpdb.chr as chr on chr_variable_id
	    where 
		chr.chr_variable_id = common.variable_id
) as new_table
on old_table.tagName = new_table.tagName;

/*
This is the part where you divide the analog variables
*/

select matrikonTag, matrikonLowRatio,matrikonHiRatio,round(minEquipmentVal/minDisplayVal,2) as newConfigMinRatio, round(maxEquipmentVal/maxDisplayVal,2) as newConfigMaxRatio from
(
	select matrikon_id,concat(matrikon_group_name,'.',matrikon_name) as matrikonTag, matrikon_hiRaw,matrikon_lowRaw,matrikon_hiScaled,matrikon_loScaled, round(matrikon_lowRaw / matrikon_loScaled,2) as matrikonLowRatio, round(matrikon_hiRaw/matrikon_hiScaled,2) as matrikonHiRatio 
    from matrikondb.matrikon
) as matrikonTable

inner join
(
	select trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as tagName,variable_id, reg_Minimium_display_value as minDisplayVal,reg_Minimum_equipment_value as minEquipmentVal, reg_Maximum_display_value as maxDisplayVal,reg_Maximum_equipment_value as maxEquipmentVal
	from newvarexpdb.common as common
		right join newvarexpdb.reg as reg on reg_variable_id
	    where 
		reg.reg_variable_id = common.variable_id

	union all

	select trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as tagName,variable_id, ctv_Minimium_display_value as minDisplayVal,ctv_Minimum_equipment_value as minEquipmentVal, ctv_Maximum_display_value as maxDisplayVal,ctv_Maximum_equipment_value as maxEquipmentVal
	from newvarexpdb.common as common
		right join newvarexpdb.ctv as ctv on ctv_variable_id
	    where 
		ctv.ctv_variable_id = common.variable_id

	union all

	select trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as tagName,variable_id, cnt_Minimium_display_value as minDisplayVal,cnt_Minimum_equipment_value as minEquipmentVal, cnt_Maximum_display_value as maxDisplayVal,cnt_Maximum_equipment_value as maxEquipmentVal
	from newvarexpdb.common as common
		right join newvarexpdb.cnt as cnt on cnt_variable_id
	    where 
		cnt.cnt_variable_id = common.variable_id

	union all

	select trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as tagName,variable_id, chr_Minimium_display_value as minDisplayVal,chr_Minimum_equipment_value as minEquipmentVal, chr_Maximum_display_value as maxDisplayVal,chr_Maximum_equipment_value as maxEquipmentVal
	from newvarexpdb.common as common
		right join newvarexpdb.chr as chr on chr_variable_id
	    where 
		chr.chr_variable_id = common.variable_id
) as new_config
on matrikonTable.matrikonTag = new_config.tagName;

/*
Part 2: DNP3 Variables
Type
*/

select newConfigTable.variable_id, newTagName, newConfigTable.source as newSource, oldConfigTable.source as oldSource
from
	(
		select *,trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as newTagName
        from newvarexpdb.common as common
        where source = "3"
    
    ) as newConfigTable

inner join

(
		select *,trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as oldTagName
        from oldvarexpdb.common as common
        where source = "O"
) as oldConfigTable
on newTagName = oldTagName;

/*
OPC/DNP3
*/
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

inner join
(
	select concat(matrikon_group_name,'.',matrikon_name) as matrikonTag, matrikon_item_path as itemPath from matrikondb.matrikon
) as matrikonTable
on newConfigTable.tagName = matrikonTable.matrikonTag;


/*
Analog Matching (Commandable Range)
*/
Select * from
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
Part 3: INternal Variables

Internal Type Check
*/

Select newCommon.variable_id, oldCommon.variable_id, newCommon.tagName, oldCommon.tagName, newCommon.source
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
Producer check
*/
select * from
(
	select common.topology_server, 
		common.variable_id, 
		common.source, trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) 
				as tagName 
			from newvarexpdb.common as common
) as newTable
inner join
(
	select common.topology_server, 
		common.variable_id, 
		common.source, trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) 
				as tagName 
			from oldvarexpdb.common as common
) as oldTable
on newTable.tagName = oldTable.tagName
group by newTable.tagName;

select * from resultoutput.resulttable;
select * from matrikondb.matrikon;
select * from newvarexpdb.common;
select * from oldvarexpdb.common;