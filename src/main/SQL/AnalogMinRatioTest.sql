 /*
 This is for testing the digitals

This matches the digital columns between the new configuration and hte old configureation. Specifically, coluns 40,41,42,43,45 and 46 must match between the old configuration and the new configuration
 */         
#update outputvarexpdb.result set Comment=concat(Comment, "[Comment]") where id =480;
select * from resultOutput.resultTable;

select tagName, matrikonLowRatio,matrikonHiRatio,round(minEquipmentVal/minDisplayVal,2) as newConfigMinRatio, round(maxEquipmentVal/maxDisplayVal,2) as newConfigMaxRatio, if(matrikonTag is null, 'meh','whatever')
	
 from
#select * from
(
	select matrikon_id,concat(matrikon_group_name,'.',matrikon_name) as matrikonTag, matrikon_hiRaw,matrikon_lowRaw,matrikon_hiScaled,matrikon_loScaled, round(matrikon_lowRaw / matrikon_loScaled,2) as matrikonLowRatio, round(matrikon_hiRaw/matrikon_hiScaled,2) as matrikonHiRatio 
    from matrikondb.matrikon
) as matrikonTable

right join
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


#Update the resultOutput DB's result table's DescriptionTest Column
UPDATE resultOutput.resultTable result,
    (SELECT 
        tagName AS newTagName,
			units,
            matrikonTag AS matrikonTagName,
            matrikonLowRatio,
            matrikonHiRatio,
            ROUND(minEquipmentVal / minDisplayVal, 2) AS newConfigMinRatio,
            ROUND(maxEquipmentVal / maxDisplayVal, 2) AS newConfigMaxRatio
    FROM
        (SELECT 
        matrikon_id,
            CONCAT(matrikon_group_name, '.', matrikon_name) AS matrikonTag,
            matrikon_hiRaw,
            matrikon_lowRaw,
            matrikon_hiScaled,
            matrikon_loScaled,
            ROUND(matrikon_lowRaw / matrikon_loScaled, 2) AS matrikonLowRatio,
            ROUND(matrikon_hiRaw / matrikon_hiScaled, 2) AS matrikonHiRatio
    FROM
        matrikondb.matrikon) AS matrikonTable
    RIGHT JOIN (SELECT 
        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,
            variable_id,
            reg_Measurement_Units AS units,
            reg_Minimium_display_value AS minDisplayVal,
            reg_Minimum_equipment_value AS minEquipmentVal,
            reg_Maximum_display_value AS maxDisplayVal,
            reg_Maximum_equipment_value AS maxEquipmentVal
    FROM
        newvarexpdb.common AS common
    RIGHT JOIN newvarexpdb.reg AS reg ON reg_variable_id
    WHERE
        reg.reg_variable_id = common.variable_id UNION ALL SELECT 
        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,
            variable_id,
            ctv_Measurement_Units AS units,
            ctv_Minimium_display_value AS minDisplayVal,
            ctv_Minimum_equipment_value AS minEquipmentVal,
            ctv_Maximum_display_value AS maxDisplayVal,
            ctv_Maximum_equipment_value AS maxEquipmentVal
    FROM
        newvarexpdb.common AS common
    RIGHT JOIN newvarexpdb.ctv AS ctv ON ctv_variable_id
    WHERE
        ctv.ctv_variable_id = common.variable_id UNION ALL SELECT 
        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,
            variable_id,
            cnt_Measurement_Units AS units,
            cnt_Minimium_display_value AS minDisplayVal,
            cnt_Minimum_equipment_value AS minEquipmentVal,
            cnt_Maximum_display_value AS maxDisplayVal,
            cnt_Maximum_equipment_value AS maxEquipmentVal
    FROM
        newvarexpdb.common AS common
    RIGHT JOIN newvarexpdb.cnt AS cnt ON cnt_variable_id
    WHERE
        cnt.cnt_variable_id = common.variable_id UNION ALL SELECT 
        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,
            variable_id,
            chr_Measurement_Units AS units,
            chr_Minimium_display_value AS minDisplayVal,
            chr_Minimum_equipment_value AS minEquipmentVal,
            chr_Maximum_display_value AS maxDisplayVal,
            chr_Maximum_equipment_value AS maxEquipmentVal
    FROM
        newvarexpdb.common AS common
    RIGHT JOIN newvarexpdb.chr AS chr ON chr_variable_id
    WHERE
        chr.chr_variable_id = common.variable_id) AS new_config ON matrikonTable.matrikonTag = new_config.tagName) AnalogsRatioTable 
SET 
    result.`Analogs Minimum Ratio Test` = IF(IF((newConfigMinRatio IS NULL
                OR newConfigMinRatio = 0)
                AND (AnalogsRatioTable.matrikonTagName IS NOT NULL),
            TRUE,
            (matrikonLowRatio <=> newConfigMinRatio)
                AND (AnalogsRatioTable.newTagName IS NOT NULL)),
        'PASS',
        IF(units LIKE "A" or units LIKE "kV",'PASS','FAIL')
        ),
    result.`Analogs Maximum Ratio Test` = IF((AnalogsRatioTable.matrikonHiRatio <=> AnalogsRatioTable.newConfigMaxRatio)
            AND (AnalogsRatioTable.matrikonTagName IS NOT NULL),
        'PASS',
        'FAIL'),
    result.`Comment` = CONCAT(result.`Comment`,
            IF(AnalogsRatioTable.newConfigMinRatio IS NULL and NOT (units LIKE "A" or units LIKE "kV"),
                '
                 min display value (62) is zero',
                ''),
            IF(AnalogsRatioTable.newConfigMinRatio = 0,
                '
                 min equipment value (65) is zero',
                ''),
            IF(AnalogsRatioTable.newConfigMinRatio <> AnalogsRatioTable.matrikonLowRatio,
                '
                 min equipment val (65) and min display val (63) does not match between new and matrikon config',
                ''),
            IF(AnalogsRatioTable.matrikonHiRatio <> AnalogsRatioTable.newConfigMaxRatio,
                '
                 max equipment val (66) and max display val (64) does not match between new and matrikon config',
                '')
WHERE
    result.tagName = AnalogsRatioTable.newTagName;

#  round(matrikon_lowRaw / matrikon_loScaled,2) as matrikonLowRatio, round(matrikon_hiRaw/matrikon_hiScaled,2) as matrikonHiRatio
#  round(minEquipmentVal/minDisplayVal,2) as newConfigMinRatio, round(maxEquipmentVal/maxDisplayVal,2) as newConfigMaxRatio
select * from resultOutput.resultTable;

select * from matrikondb.matrikon where concat(matrikon_group_name,matrikon_name)="SHILO.ST.DG1.MMXU2.A";
select * from newvarexpdb.common
where
	trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',
    common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th))='SHILO.ST.DG1.MMXU2.A';

select * from resultOUtput.resultTable where `Analogs Minimum Ratio Test` = "FAIL" or "Analogs Maximum Ratio Test" ="FAIL"
