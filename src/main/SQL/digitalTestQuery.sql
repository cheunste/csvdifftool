/*
 This is for testing the digitals

This matches the digital columns between the new configuration and hte old configureation. Specifically, coluns 40,41,42,43,45 and 46 must match between the old configuration and the new configuration
 */         


UPDATE resultOutput.resultTable result,
    (SELECT 
        t1.variable_type AS oldVariableType,
            t1.bit_log_bit_0_to_1 AS bitLog01_1,
            t1.bit_log_bit_1_to_0 AS bitLog10_1,
            t1.bit_reserved bitReserved1,
            t1.authorisation_level AS authorisationLevel1,
            t1.alarm_level AS alarmLevel1,
            t1.tagName AS oldTagName,
            t2.variable_id,
            t2.variable_type AS newVariableType,
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
        ats.ats_variable_id = common.variable_id UNION ALL SELECT 
        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,
            variable_id,
            variable_type,
            NULL,
            NULL,
            NULL,
            NULL,
            NULL
    FROM
        oldvarexpdb.common AS common
    RIGHT JOIN oldvarexpdb.ctv AS ctv ON ctv_variable_id = common.variable_id) AS oldConfigTable) AS t1
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
                OR (DigitalTable.oldVariableType LIKE 'ACM'
                AND DigitalTable.newVariableType LIKE 'ALA'),
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
				bitReserved (43) does not match between old and new config',
                ''),
            IF((DigitalTable.authorisationLevel1 <=> DigitalTable.authorisationLevel2) = 0
                    AND NOT (DigitalTable.oldVariableType LIKE 'ACM'
                    AND DigitalTable.newVariableType LIKE 'ALA'),
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