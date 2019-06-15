/*
Divide analogs

In this sql query, you are making sure the varexp variable ratios matches with the ones in matrikon. Speicifcally, the following needs to match

(matrikon_lowRaw/matrikon_loScaled) ==  (minEquipmentVal/minDisplayVal)
(matrikon_hiRaw/matrikon_hiScaled) == (maxEquipmentVal/maxDisplayVal)
Then you insert them into the outputvarexp DB

Note that there can be cases where the newConfigMinRatio can be null or 0. Mainly because the minDisplayVal can be 0 and maxEquipmentVal can be empty
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

