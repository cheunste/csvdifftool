/*
This query is for Units.

Match the analog units between the new varexp config and the old varexp config. 

1) Merge the common table with the respective table in either the REG , CTV, NT or CHR fields
2) find the "Measurement Units" field
3) Insert the result set into the outputvarexp DB
*/
select *
from
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
) as oldTable

right join 
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
) as newTable

on oldTable.tagName = newTable.tagName;