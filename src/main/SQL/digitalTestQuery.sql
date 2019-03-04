 /*
 This is for testing the digitals

This matches the digital columns between the new configuration and hte old configureation. Specifically, coluns 40,41,42,43,45 and 46 must match between the old configuration and the new configuration
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
	group by tagName asc
) as oldConfigTable
inner join
(
	select variable_id, desc_1st_lang, desc_2nd_lang, 
		trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th))  as tagName
	from newvarexpdb.common as common
	group by tagName asc	
) as newConfigTable
on oldConfigTable.tagName = newConfigTable.tagName;

#Test 
select * from oldvarexpdb.cmd;


select t2.tagName as oldTagName, t2.variable_id, t1.bit_log_bit_0_to_1 as bitLog01_1, t1.bit_log_bit_1_to_0 as bitLog10_1, t1.bit_reserved bitReserved1, t1.authorisation_level as authorisationLevel1, t1.alarm_level as alarmLevel1, 
	t2.tagName as newTagName, t2.bit_log_bit_0_to_1 as bitLog01_2, t2.bit_log_bit_1_to_0 as bitLog10_2, t2.bit_reserved as bitReserved2, t2.authorisation_level authorisationLevel2, t2.alarm_level as alarmLevel2,
    t2.bit_log_bit_0_to_1 <=> t1.bit_log_bit_0_to_1
	from

(
	select * from
	(
		select trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as tagName,variable_id, bit.bit_log_bit_0_to_1, bit.bit_log_bit_1_to_0,bit.bit_reserved, null as authorisation_level, null as alarm_level
		from oldvarexpdb.common as common
			right join oldvarexpdb.bit as bit on bit_variable_id
		    where 
			bit.bit_variable_id = common.variable_id

		union all

		select trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as tagName,variable_id, cmd.cmd_log_bit_0_to_1,cmd.cmd_log_bit_1_to_0,cmd.cmd_reserved, null as authorisation_level, null as alarm_level
		from oldvarexpdb.common as common
			right join oldvarexpdb.cmd as cmd on cmd_variable_id
		    where 
			cmd.cmd_variable_id = common.variable_id
		union all

		select trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as tagName,variable_id, ala.ala_log_bit_0_to_1,ala.ala_log_bit_1_to_0,ala.ala_reserved,ala.ala_alarm_level,ala_alarm 
		from oldvarexpdb.common as common
			right join oldvarexpdb.ala as ala on ala_variable_id
		    where 
			ala.ala_variable_id = common.variable_id

		union all

		select trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as tagName,variable_id, acm.acm_log_bit_0_to_1,acm.acm_log_bit_1_to_0,acm.acm_reserved,acm.acm_alarm_level,acm_alarm 
		from oldvarexpdb.common as common
			right join oldvarexpdb.acm as acm on acm_variable_id
		    where 
			acm.acm_variable_id = common.variable_id

		union all

		select trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as tagName,variable_id, tsh.tsh_log_bit_0_to_1,tsh.tsh_log_bit_1_to_0,tsh.tsh_reserved,null as tsh_alarm_level, null as tsh_alarm 
		from oldvarexpdb.common as common
			right join oldvarexpdb.tsh as tsh on tsh_variable_id
		    where 
			tsh.tsh_variable_id = common.variable_id
		
		union all

		select trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as tagName,variable_id, ats.ats_log_bit_0_to_1,ats.ats_log_bit_1_to_0,ats.ats_reserved,null as ats_alarm_level, null as ats_alarm 
		from oldvarexpdb.common as common
			right join oldvarexpdb.ats as ats on ats_variable_id
		    where 
			ats.ats_variable_id = common.variable_id

	) as oldConfigTable
) as t1
right join
(
	select * from
	(
		select trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as tagName,variable_id, bit.bit_log_bit_0_to_1, bit.bit_log_bit_1_to_0,bit.bit_reserved, null as authorisation_level, null as alarm_level
		from newvarexpdb.common as common
			right join newvarexpdb.bit as bit on bit_variable_id
		    where 
			bit.bit_variable_id = common.variable_id

		union all

		select trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as tagName,variable_id, cmd.cmd_log_bit_0_to_1,cmd.cmd_log_bit_1_to_0,cmd.cmd_reserved, null as authorisation_level, null as alarm_level
		from newvarexpdb.common as common
			right join newvarexpdb.cmd as cmd on cmd_variable_id
		    where 
			cmd.cmd_variable_id = common.variable_id
		union all

		select trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as tagName,variable_id, ala.ala_log_bit_0_to_1,ala.ala_log_bit_1_to_0,ala.ala_reserved,ala.ala_alarm_level,ala_alarm 
		from newvarexpdb.common as common
			right join newvarexpdb.ala as ala on ala_variable_id
		    where 
			ala.ala_variable_id = common.variable_id

		union all

		select trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as tagName,variable_id, acm.acm_log_bit_0_to_1,acm.acm_log_bit_1_to_0,acm.acm_reserved,acm.acm_alarm_level,acm_alarm 
		from newvarexpdb.common as common
			right join newvarexpdb.acm as acm on acm_variable_id
		    where 
			acm.acm_variable_id = common.variable_id

		union all

		select trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as tagName,variable_id, tsh.tsh_log_bit_0_to_1,tsh.tsh_log_bit_1_to_0,tsh.tsh_reserved,null as tsh_alarm_level, null as tsh_alarm 
		from newvarexpdb.common as common
			right join newvarexpdb.tsh as tsh on tsh_variable_id
		    where 
			tsh.tsh_variable_id = common.variable_id
		
		union all

		select trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as tagName,variable_id, ats.ats_log_bit_0_to_1,ats.ats_log_bit_1_to_0,ats.ats_reserved,null as ats_alarm_level, null as ats_alarm 
		from newvarexpdb.common as common
			right join newvarexpdb.ats as ats on ats_variable_id
		    where 
			ats.ats_variable_id = common.variable_id

	) as newConfigTable
) as t2 
on t2.tagName = t1.tagName; 

#Update the resultOutput DB's result table's DescriptionTest Column
UPDATE resultOutput.resultTable result,
    (SELECT 
        t1.variable_type AS newVariableType,
            t1.bit_log_bit_0_to_1 AS bitLog01_1,
            t1.bit_log_bit_1_to_0 AS bitLog10_1,
            t1.bit_reserved bitReserved1,
            t1.authorisation_level AS authorisationLevel1,
            t1.alarm_level AS alarmLevel1,
            t2.tagName AS oldTagName,
            t2.variable_id,
            t2.variable_type AS oldVariableType,
            t2.tagName AS newTagName,
            t2.bit_log_bit_0_to_1 AS bitLog01_2,
            t2.bit_log_bit_1_to_0 AS bitLog10_2,
            t2.bit_reserved AS bitReserved2,
            t2.authorisation_level authorisationLevel2,
            t2.alarm_level AS alarmLevel2
    FROM
        (SELECT 
        *
    FROM
        (SELECT 
        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,
            variable_id,
            variable_type,
            bit.bit_log_bit_0_to_1,
            bit.bit_log_bit_1_to_0,
            bit.bit_reserved,
            NULL AS authorisation_level,
            NULL AS alarm_level
    FROM
        oldvarexpdb.common AS common
    RIGHT JOIN oldvarexpdb.bit AS bit ON bit_variable_id = common.variable_id
    WHERE
        bit.bit_variable_id = common.variable_id UNION ALL SELECT 
        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,
            variable_id,
            variable_type,
            cmd.cmd_log_bit_0_to_1,
            cmd.cmd_log_bit_1_to_0,
            cmd.cmd_reserved,
            NULL AS authorisation_level,
            NULL AS alarm_level
    FROM
        oldvarexpdb.common AS common
    RIGHT JOIN oldvarexpdb.cmd AS cmd ON cmd_variable_id = common.variable_id
    WHERE
        cmd.cmd_variable_id = common.variable_id UNION ALL SELECT 
        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,
            variable_id,
            variable_type,
            ala.ala_log_bit_0_to_1,
            ala.ala_log_bit_1_to_0,
            ala.ala_reserved,
            ala.ala_alarm_level,
            ala_alarm
    FROM
        oldvarexpdb.common AS common
    RIGHT JOIN oldvarexpdb.ala AS ala ON ala_variable_id = common.variable_id
    WHERE
        ala.ala_variable_id = common.variable_id UNION ALL SELECT 
        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,
            variable_id,
            variable_type,
            acm.acm_log_bit_0_to_1,
            acm.acm_log_bit_1_to_0,
            acm.acm_reserved,
            acm.acm_alarm_level,
            acm_alarm
    FROM
        oldvarexpdb.common AS common
    RIGHT JOIN oldvarexpdb.acm AS acm ON acm_variable_id = common.variable_id
    WHERE
        acm.acm_variable_id = common.variable_id UNION ALL SELECT 
        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,
            variable_id,
            variable_type,
            tsh.tsh_log_bit_0_to_1,
            tsh.tsh_log_bit_1_to_0,
            tsh.tsh_reserved,
            NULL AS tsh_alarm_level,
            NULL AS tsh_alarm
    FROM
        oldvarexpdb.common AS common
    RIGHT JOIN oldvarexpdb.tsh AS tsh ON tsh_variable_id = common.variable_id
    WHERE
        tsh.tsh_variable_id = common.variable_id UNION ALL SELECT 
        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,
            variable_id,
            variable_type,
            ats.ats_log_bit_0_to_1,
            ats.ats_log_bit_1_to_0,
            ats.ats_reserved,
            NULL AS ats_alarm_level,
            NULL AS ats_alarm
    FROM
        oldvarexpdb.common AS common
    RIGHT JOIN oldvarexpdb.ats AS ats ON ats_variable_id = common.variable_id
    WHERE
        ats.ats_variable_id = common.variable_id) AS oldConfigTable) AS t1
    RIGHT JOIN (SELECT 
        *
    FROM
        (SELECT 
        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,
            variable_id,
            variable_type,
            bit.bit_log_bit_0_to_1,
            bit.bit_log_bit_1_to_0,
            bit.bit_reserved,
            NULL AS authorisation_level,
            NULL AS alarm_level
    FROM
        newvarexpdb.common AS common
    RIGHT JOIN newvarexpdb.bit AS bit ON bit_variable_id = common.variable_id
    WHERE
        bit.bit_variable_id = common.variable_id UNION ALL SELECT 
        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,
            variable_id,
            variable_type,
            cmd.cmd_log_bit_0_to_1,
            cmd.cmd_log_bit_1_to_0,
            cmd.cmd_reserved,
            NULL AS authorisation_level,
            NULL AS alarm_level
    FROM
        newvarexpdb.common AS common
    RIGHT JOIN newvarexpdb.cmd AS cmd ON cmd_variable_id = common.variable_id
    WHERE
        cmd.cmd_variable_id = common.variable_id UNION ALL SELECT 
        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,
            variable_id,
            variable_type,
            ala.ala_log_bit_0_to_1,
            ala.ala_log_bit_1_to_0,
            ala.ala_reserved,
            ala.ala_alarm_level,
            ala_alarm
    FROM
        newvarexpdb.common AS common
    RIGHT JOIN newvarexpdb.ala AS ala ON ala_variable_id = common.variable_id
    WHERE
        ala.ala_variable_id = common.variable_id UNION ALL SELECT 
        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,
            variable_id,
            variable_type,
            acm.acm_log_bit_0_to_1,
            acm.acm_log_bit_1_to_0,
            acm.acm_reserved,
            acm.acm_alarm_level,
            acm_alarm
    FROM
        newvarexpdb.common AS common
    RIGHT JOIN newvarexpdb.acm AS acm ON acm_variable_id = common.variable_id
    WHERE
        acm.acm_variable_id = common.variable_id UNION ALL SELECT 
        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,
            variable_id,
            variable_type,
            tsh.tsh_log_bit_0_to_1,
            tsh.tsh_log_bit_1_to_0,
            tsh.tsh_reserved,
            NULL AS tsh_alarm_level,
            NULL AS tsh_alarm
    FROM
        newvarexpdb.common AS common
    RIGHT JOIN newvarexpdb.tsh AS tsh ON tsh_variable_id = common.variable_id
    WHERE
        tsh.tsh_variable_id = common.variable_id UNION ALL SELECT 
        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,
            variable_id,
            variable_type,
            ats.ats_log_bit_0_to_1,
            ats.ats_log_bit_1_to_0,
            ats.ats_reserved,
            NULL AS ats_alarm_level,
            NULL AS ats_alarm
    FROM
        newvarexpdb.common AS common
    RIGHT JOIN newvarexpdb.ats AS ats ON ats_variable_id = common.variable_id
    WHERE
        ats.ats_variable_id = common.variable_id) AS newConfigTable) AS t2 ON t2.tagName = t1.tagName) DigitalTable 
SET 
    result.`Digitals Test` = IF(DigitalTable.bitLog01_1 <=> DigitalTable.bitLog01_2
            AND DigitalTable.bitReserved1 <=> DigitalTable.bitReserved2
            AND DigitalTable.authorisationLevel1 <=> DigitalTable.authorisationLevel2
            AND DigitalTable.alarmLevel1 <=> DigitalTable.alarmLevel2,
        'PASS',
        IF((DigitalTable.oldVariableType LIKE 'CTV'
                AND DigitalTable.newVariableType LIKE 'CMD'
                AND DigitalTable.bitReserved2 LIKE 'HI')
                OR (DigitalTable.oldTagName LIKE 'ALA'
                AND DigitalTable.newTagName LIKE 'ACM'
                AND DigitalTable.authorisationLevel2 = 0),
            'PASS',
            'FAIL')),
    result.`Comment` = CONCAT(result.`Comment`,
            IF((DigitalTable.bitLog10_1 <=> DigitalTable.bitLog10_2) = 0,
                '
                                                                 bitlog10 (42) does not match between old and new config',
                ''),
            IF((DigitalTable.bitReserved1 <=> DigitalTable.bitReserved2) = 0
                    AND NOT (DigitalTable.oldVariableType LIKE 'CTV'
                    AND DigitalTable.newVariableType LIKE 'CMD'
                    AND DigitalTable.bitReserved2 LIKE 'HI'),
                '
                                                                 bitReserved (43)does not match between old and new config',
                ''),
            IF((DigitalTable.authorisationLevel1 <=> DigitalTable.authorisationLevel2) = 0
                    AND NOT (DigitalTable.oldTagName LIKE 'ALA'
                    AND DigitalTable.newTagName LIKE 'ACM'
                    AND DigitalTable.authorisationLevel2 = 0),
                '
                                                                 authorization level (44) does not match between old and new config',
                ''),
            IF((DigitalTable.alarmLevel1 <=> DigitalTable.alarmLevel2) = 0,
                '
                                                                 alarm level (45) does not match between old and new config',
                ''))
WHERE
    result.tagName = DigitalTable.newTagName;






SELECT 
    TagName, `Digitals Test`, Comment
FROM
    resultOutput.resultTable;

select * from resultOutput.resultTable;
select * from resultOutput.resultTable order by `Digitals Test`;
select * from oldvarexpdb.common
where
	trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',
    common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th))='SHILO.ST.AS.ZACS1.AtsAlm';

select * from newvarexpdb.common
where
	trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',
    common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th))='SHILO.ST.AS.ZACS1.AtsAlm';
