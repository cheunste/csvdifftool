package VarexpInterface.Comparison;

import VarexpInterface.Database.dbConnector;
import VarexpInterface.PropertyManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ComparisonTest {

    private static List<String> testList = new ArrayList<String>();

    private static String oldConfigDB;
    private static String newConfigDB;
    private static String matrikonDB;
    private static String resultDB;
    private static List<String> databaseList;

    private static final int MAX_THREADS = 2;


    //This is an initial insert. All it does is insert all the tags from the new config to the output DB.
    //Must be executed before all the others
    private String initialInsert = "insert into " + Result.resultDatabaseName() + " (TagName,\n" +
            "\t`Tag Name Test`,`Description Test`,`Digitals Test`,\n" +
            "    `Units Test`,`Analogs Minimum Ratio Test`,`Analogs Maximum Ratio Test`,\n" +
            "    `Type Test`,`OPC DNP3 Source Test`,`Commandable Range Test`,\n" +
            "    `Internal Type Check Test`,`Producer Test`,`Comment`)\n" +
            "\n" +
            "select newTagName,'','','','','','','','','','','','' from\n" +
            "(\n" +
            "\tselect *\n" +
            "\tfrom\n" +
            "\t(\n" +
            "\t\tselect newConfigTable.newVariableId, oldConfigTable.oldVariableId,newConfigTable.newTagName, oldConfigTable.oldTagName\n" +
            "\t\tfrom\n" +
            "\t\t(\n" +
            "\t\t\tselect variable_id as newVariableId,trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as newTagName\n" +
            "\t\t\tfrom newvarexpdb.common as common\n" +
            "\t\torder by newTagName\n" +
            "\t\t) as newConfigTable\n" +
            "\t\tleft join  \n" +
            "\t\t(\n" +
            "\t\t\tselect variable_id as oldVariableId,trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as oldTagName\n" +
            "\t\t\tfrom oldvarexpdb.common as common\n" +
            "\t\torder by oldTagName\n" +
            "\t\t) as oldConfigTable\n" +
            "\t\ton oldConfigTable.oldTagName = newConfigTable.newTagName\n" +
            "\torder by oldConfigTable.oldTagName asc\n" +
            "\t) as tempTable1\n" +
            "\tleft join\n" +
            "\t(\n" +
            "\t\tselect matrikon.matrikon_id as matrikonID,concat(matrikon.matrikon_group_name,\".\",matrikon_name) as matrikonTagName\n" +
            "\tfrom matrikondb.matrikon as matrikon\n" +
            "\t) as matrikonTable\n" +
            "\ton tempTable1.newTagName = matrikonTagName\n" +
            ") as tempTable2;";

    //This matches the tags from the new config, old config and matrkon config and PASSES or fails them accordingly
    private String tagMatchTest = "Update " + Result.resultDatabaseName() + " result,\n" +
            "(\n" +
            "\tselect *\n" +
            "\tfrom\n" +
            "\t(\n" +
            "\t\tselect newConfigTable.newVariableId, oldConfigTable.oldVariableId,newConfigTable.newTagName, oldConfigTable.oldTagName\n" +
            "\t\tfrom\n" +
            "\t\t(\n" +
            "\t\t\tselect variable_id as newVariableId,trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as newTagName\n" +
            "\t\t\tfrom newvarexpdb.common as common\n" +
            "\t\torder by newTagName\n" +
            "\t\t) as newConfigTable\n" +
            "\t\tleft join  \n" +
            "\t\t(\n" +
            "\t\t\tselect variable_id as oldVariableId,trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as oldTagName\n" +
            "\t\t\tfrom oldvarexpdb.common as common\n" +
            "\t\torder by oldTagName\n" +
            "\t\t) as oldConfigTable\n" +
            "\t\ton oldConfigTable.oldTagName = newConfigTable.newTagName\n" +
            "\torder by oldConfigTable.oldTagName asc\n" +
            "\t) as tempTable1\n" +
            "\tleft join\n" +
            "\t(\n" +
            "\t\tselect matrikon.matrikon_id as matrikonID,concat(matrikon.matrikon_group_name,\".\",matrikon_name) as matrikonTagName\n" +
            "\tfrom matrikondb.matrikon as matrikon\n" +
            "\t) as matrikonTable\n" +
            "\ton tempTable1.newTagName = matrikonTagName\n" +
            ") as TagNameTable\n" +
            "set \n" +
            "\tresult.`Tag Name Test` = if(newTagName = oldTagName and newTagName = matrikonTagName ,'PASS','FAIL'),\t\n" +
            "    result.`Comment` = if(matrikonTagName is null, concat(result.`Comment`,\"\\nTag doesn't exist in matrikon config\"), result.`Comment`),\n" +
            "    result.`Comment` = if(oldTagName is null, concat(result.`Comment`,\"\\nTag doesn't exist in old config\"), result.`Comment`),\n" +
            "    result.`Comment` = if(newTagName != oldTagName and oldTagName is not null,concat(result.`Comment`,\"\\nNew Tag doesn't match with old config\"), result.`Comment`),\n" +
            "    result.`Comment` = if(newTagName != matrikonTagName and matrikonTagName is not null,concat(result.`Comment`,\"\\nNew Tag doesn't match with matrikon config\"), result.`Comment`)\n" +
            "where result.tagName = TagNameTable.newTagName;";

    //Matches the description between the old config and the new config
    private String descriptionTest = "Update " + Result.resultDatabaseName() + " result,\n" +
            "(\n" +
            "\tselect newConfigTable.tagName, newConfigTable.desc_1st_lang as newConfigDesc1, newConfigTable.desc_2nd_lang as newConfigDesc2, oldConfigTable.desc_1st_lang as oldConfigDesc1,oldConfigTable.desc_2nd_lang as oldConfigDesc2 from\n" +
            "\t(\n" +
            "\tselect variable_id, desc_1st_lang, desc_2nd_lang, \n" +
            "\t\t\ttrim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th))  as tagName\n" +
            "\t\tfrom oldvarexpdb.common as common\n" +
            "\t\tgroup by tagName \n" +
            "        order by tagName asc\n" +
            "\t) as oldConfigTable\n" +
            "\tright join\n" +
            "\t(\n" +
            "\t\tselect variable_id, desc_1st_lang, desc_2nd_lang, \n" +
            "\t\t\ttrim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th))  as tagName\n" +
            "\t\tfrom newvarexpdb.common as common\n" +
            "\t\tgroup by tagName \n" +
            "        order by tagName asc\t\n" +
            "\t) as newConfigTable\n" +
            "\ton oldConfigTable.tagName = newConfigTable.tagName\n" +
            ") DescriptionTable\n" +
            "set result.`Description Test` = if(DescriptionTable.newConfigDesc1 =DescriptionTable.oldConfigDesc1 and DescriptionTable.newConfigDesc2 =DescriptionTable.oldConfigDesc2,'PASS','FAIL'),\n" +
            "    result.`Comment` = if(newConfigDesc1 != oldConfigDesc1, concat(result.`Comment`,\"\\n New Tag description (10) doesn't match old config's description\"), result.`Comment`),\n" +
            "    result.`Comment` = if(newConfigDesc2 != oldConfigDesc2, concat(result.`Comment`,\"\\n New Tag description lang2 (11) doesn't match old config's description (lang2)\"), result.`Comment`)\n" +
            "where result.tagName = DescriptionTable.tagName;";

    //Test for the digital bits. Only for digital tags
    private String digitalsTest =
            "Update " + Result.resultDatabaseName() + " result,\n" +
                    "    (SELECT \n" +
                    "        t2.tagName AS oldTagName,\n" +
                    "            t2.variable_id,\n" +
                    "            t1.bit_log_bit_0_to_1 AS bitLog01_1,\n" +
                    "            t1.bit_log_bit_1_to_0 AS bitLog10_1,\n" +
                    "            t1.bit_reserved bitReserved1,\n" +
                    "            t1.authorisation_level AS authorisationLevel1,\n" +
                    "            t1.alarm_level AS alarmLevel1,\n" +
                    "            t2.tagName AS newTagName,\n" +
                    "            t2.bit_log_bit_0_to_1 AS bitLog01_2,\n" +
                    "            t2.bit_log_bit_1_to_0 AS bitLog10_2,\n" +
                    "            t2.bit_reserved AS bitReserved2,\n" +
                    "            t2.authorisation_level authorisationLevel2,\n" +
                    "            t2.alarm_level AS alarmLevel2\n" +
                    "    FROM\n" +
                    "        (SELECT \n" +
                    "        *\n" +
                    "    FROM\n" +
                    "        (SELECT \n" +
                    "        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,\n" +
                    "            variable_id,\n" +
                    "            bit.bit_log_bit_0_to_1,\n" +
                    "            bit.bit_log_bit_1_to_0,\n" +
                    "            bit.bit_reserved,\n" +
                    "            NULL AS authorisation_level,\n" +
                    "            NULL AS alarm_level\n" +
                    "    FROM\n" +
                    "        oldvarexpdb.common AS common\n" +
                    "    RIGHT JOIN oldvarexpdb.bit AS bit ON bit_variable_id = common.variable_id\n" +
                    "    WHERE\n" +
                    "        bit.bit_variable_id = common.variable_id UNION ALL SELECT \n" +
                    "        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,\n" +
                    "            variable_id,\n" +
                    "            cmd.cmd_log_bit_0_to_1,\n" +
                    "            cmd.cmd_log_bit_1_to_0,\n" +
                    "            cmd.cmd_reserved,\n" +
                    "            NULL AS authorisation_level,\n" +
                    "            NULL AS alarm_level\n" +
                    "    FROM\n" +
                    "        oldvarexpdb.common AS common\n" +
                    "    RIGHT JOIN oldvarexpdb.cmd AS cmd ON cmd_variable_id = common.variable_id\n" +
                    "    WHERE\n" +
                    "        cmd.cmd_variable_id = common.variable_id UNION ALL SELECT \n" +
                    "        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,\n" +
                    "            variable_id,\n" +
                    "            ala.ala_log_bit_0_to_1,\n" +
                    "            ala.ala_log_bit_1_to_0,\n" +
                    "            ala.ala_reserved,\n" +
                    "            ala.ala_alarm_level,\n" +
                    "            ala_alarm\n" +
                    "    FROM\n" +
                    "        oldvarexpdb.common AS common\n" +
                    "    RIGHT JOIN oldvarexpdb.ala AS ala ON ala_variable_id = common.variable_id\n" +
                    "    WHERE\n" +
                    "        ala.ala_variable_id = common.variable_id UNION ALL SELECT \n" +
                    "        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,\n" +
                    "            variable_id,\n" +
                    "            acm.acm_log_bit_0_to_1,\n" +
                    "            acm.acm_log_bit_1_to_0,\n" +
                    "            acm.acm_reserved,\n" +
                    "            acm.acm_alarm_level,\n" +
                    "            acm_alarm\n" +
                    "    FROM\n" +
                    "        oldvarexpdb.common AS common\n" +
                    "    RIGHT JOIN oldvarexpdb.acm AS acm ON acm_variable_id = common.variable_id\n" +
                    "    WHERE\n" +
                    "        acm.acm_variable_id = common.variable_id UNION ALL SELECT \n" +
                    "        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,\n" +
                    "            variable_id,\n" +
                    "            tsh.tsh_log_bit_0_to_1,\n" +
                    "            tsh.tsh_log_bit_1_to_0,\n" +
                    "            tsh.tsh_reserved,\n" +
                    "            NULL AS tsh_alarm_level,\n" +
                    "            NULL AS tsh_alarm\n" +
                    "    FROM\n" +
                    "        oldvarexpdb.common AS common\n" +
                    "    RIGHT JOIN oldvarexpdb.tsh AS tsh ON tsh_variable_id = common.variable_id\n" +
                    "    WHERE\n" +
                    "        tsh.tsh_variable_id = common.variable_id UNION ALL SELECT \n" +
                    "        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,\n" +
                    "            variable_id,\n" +
                    "            ats.ats_log_bit_0_to_1,\n" +
                    "            ats.ats_log_bit_1_to_0,\n" +
                    "            ats.ats_reserved,\n" +
                    "            NULL AS ats_alarm_level,\n" +
                    "            NULL AS ats_alarm\n" +
                    "    FROM\n" +
                    "        oldvarexpdb.common AS common\n" +
                    "    RIGHT JOIN oldvarexpdb.ats AS ats ON ats_variable_id = common.variable_id\n" +
                    "    WHERE\n" +
                    "        ats.ats_variable_id = common.variable_id) AS oldConfigTable) AS t1\n" +
                    "    RIGHT JOIN (SELECT \n" +
                    "        *\n" +
                    "    FROM\n" +
                    "        (SELECT \n" +
                    "        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,\n" +
                    "            variable_id,\n" +
                    "            bit.bit_log_bit_0_to_1,\n" +
                    "            bit.bit_log_bit_1_to_0,\n" +
                    "            bit.bit_reserved,\n" +
                    "            NULL AS authorisation_level,\n" +
                    "            NULL AS alarm_level\n" +
                    "    FROM\n" +
                    "        newvarexpdb.common AS common\n" +
                    "    RIGHT JOIN newvarexpdb.bit AS bit ON bit_variable_id = common.variable_id\n" +
                    "    WHERE\n" +
                    "        bit.bit_variable_id = common.variable_id UNION ALL SELECT \n" +
                    "        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,\n" +
                    "            variable_id,\n" +
                    "            cmd.cmd_log_bit_0_to_1,\n" +
                    "            cmd.cmd_log_bit_1_to_0,\n" +
                    "            cmd.cmd_reserved,\n" +
                    "            NULL AS authorisation_level,\n" +
                    "            NULL AS alarm_level\n" +
                    "    FROM\n" +
                    "        newvarexpdb.common AS common\n" +
                    "    RIGHT JOIN newvarexpdb.cmd AS cmd ON cmd_variable_id = common.variable_id\n" +
                    "    WHERE\n" +
                    "        cmd.cmd_variable_id = common.variable_id UNION ALL SELECT \n" +
                    "        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,\n" +
                    "            variable_id,\n" +
                    "            ala.ala_log_bit_0_to_1,\n" +
                    "            ala.ala_log_bit_1_to_0,\n" +
                    "            ala.ala_reserved,\n" +
                    "            ala.ala_alarm_level,\n" +
                    "            ala_alarm\n" +
                    "    FROM\n" +
                    "        newvarexpdb.common AS common\n" +
                    "    RIGHT JOIN newvarexpdb.ala AS ala ON ala_variable_id = common.variable_id\n" +
                    "    WHERE\n" +
                    "        ala.ala_variable_id = common.variable_id UNION ALL SELECT \n" +
                    "        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,\n" +
                    "            variable_id,\n" +
                    "            acm.acm_log_bit_0_to_1,\n" +
                    "            acm.acm_log_bit_1_to_0,\n" +
                    "            acm.acm_reserved,\n" +
                    "            acm.acm_alarm_level,\n" +
                    "            acm_alarm\n" +
                    "    FROM\n" +
                    "        newvarexpdb.common AS common\n" +
                    "    RIGHT JOIN newvarexpdb.acm AS acm ON acm_variable_id = common.variable_id\n" +
                    "    WHERE\n" +
                    "        acm.acm_variable_id = common.variable_id UNION ALL SELECT \n" +
                    "        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,\n" +
                    "            variable_id,\n" +
                    "            tsh.tsh_log_bit_0_to_1,\n" +
                    "            tsh.tsh_log_bit_1_to_0,\n" +
                    "            tsh.tsh_reserved,\n" +
                    "            NULL AS tsh_alarm_level,\n" +
                    "            NULL AS tsh_alarm\n" +
                    "    FROM\n" +
                    "        newvarexpdb.common AS common\n" +
                    "    RIGHT JOIN newvarexpdb.tsh AS tsh ON tsh_variable_id = common.variable_id\n" +
                    "    WHERE\n" +
                    "        tsh.tsh_variable_id = common.variable_id UNION ALL SELECT \n" +
                    "        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,\n" +
                    "            variable_id,\n" +
                    "            ats.ats_log_bit_0_to_1,\n" +
                    "            ats.ats_log_bit_1_to_0,\n" +
                    "            ats.ats_reserved,\n" +
                    "            NULL AS ats_alarm_level,\n" +
                    "            NULL AS ats_alarm\n" +
                    "    FROM\n" +
                    "        newvarexpdb.common AS common\n" +
                    "    RIGHT JOIN newvarexpdb.ats AS ats ON ats_variable_id = common.variable_id\n" +
                    "    WHERE\n" +
                    "        ats.ats_variable_id = common.variable_id) AS newConfigTable) AS t2 ON t2.tagName = t1.tagName) DigitalTable \n" +
                    "SET \n" +
                    "    result.`Digitals Test` = IF(" +
                    "            DigitalTable.bitReserved1 <=> DigitalTable.bitReserved2\n" +
                    "            AND DigitalTable.authorisationLevel1 <=> DigitalTable.authorisationLevel2\n" +
                    "            AND DigitalTable.alarmLevel1 <=> DigitalTable.alarmLevel2,\n" +
                    "        'PASS',\n" +
                    "        'FAIL'),\n" +
                    "    result.`Comment` = CONCAT(result.`Comment`,\n" +
                    "            IF((DigitalTable.bitReserved1 <=> DigitalTable.bitReserved2) = 0,\n" +
                    "                '\n" +
                    "                 bitReserved (43) does not match between old and new config',\n" +
                    "                ''),\n" +
                    "            IF((DigitalTable.authorisationLevel1 <=> DigitalTable.authorisationLevel2) = 0,\n" +
                    "                '\n" +
                    "                 authorization level (44) does not match between old and new config',\n" +
                    "                ''),\n" +
                    "            IF((DigitalTable.alarmLevel1 <=> DigitalTable.alarmLevel2) = 0,\n" +
                    "                '\n" +
                    "                 alarm level (45) does not match between old and new config',\n" +
                    "                ''))\n" +
                    "WHERE\n" +
                    "    result.tagName = DigitalTable.newTagName;\n";

    //Matches the units column for analog tags
    private String unitsTest =
            "Update " + Result.resultDatabaseName() + " result,\n" +
            "    (SELECT \n" +
            "        new_table.tagName AS tagName,\n" +
            "            old_table.reg_Measurement_Units AS MeasurementUnits1,\n" +
            "            new_table.reg_Measurement_Units AS MeasurementUnits2\n" +
            "    FROM\n" +
            "        (SELECT \n" +
            "        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,\n" +
            "            variable_id,\n" +
            "            reg.reg_Measurement_Units\n" +
            "    FROM\n" +
            "        oldvarexpdb.common AS common\n" +
            "    RIGHT JOIN oldvarexpdb.reg AS reg ON reg_variable_id\n" +
            "    WHERE\n" +
            "        reg.reg_variable_id = common.variable_id UNION ALL SELECT \n" +
            "        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,\n" +
            "            variable_id,\n" +
            "            ctv.ctv_Measurement_Units\n" +
            "    FROM\n" +
            "        oldvarexpdb.common AS common\n" +
            "    RIGHT JOIN oldvarexpdb.ctv AS ctv ON ctv_variable_id\n" +
            "    WHERE\n" +
            "        ctv.ctv_variable_id = common.variable_id UNION ALL SELECT \n" +
            "        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,\n" +
            "            variable_id,\n" +
            "            cnt.cnt_Measurement_Units\n" +
            "    FROM\n" +
            "        oldvarexpdb.common AS common\n" +
            "    RIGHT JOIN oldvarexpdb.cnt AS cnt ON cnt_variable_id\n" +
            "    WHERE\n" +
            "        cnt.cnt_variable_id = common.variable_id UNION ALL SELECT \n" +
            "        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,\n" +
            "            variable_id,\n" +
            "            chr.chr_Measurement_Units\n" +
            "    FROM\n" +
            "        oldvarexpdb.common AS common\n" +
            "    RIGHT JOIN oldvarexpdb.chr AS chr ON chr_variable_id\n" +
            "    WHERE\n" +
            "        chr.chr_variable_id = common.variable_id) AS old_table\n" +
            "    RIGHT JOIN (SELECT \n" +
            "        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,\n" +
            "            variable_id,\n" +
            "            reg.reg_Measurement_Units\n" +
            "    FROM\n" +
            "        newvarexpdb.common AS common\n" +
            "    RIGHT JOIN newvarexpdb.reg AS reg ON reg_variable_id\n" +
            "    WHERE\n" +
            "        reg.reg_variable_id = common.variable_id UNION ALL SELECT \n" +
            "        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,\n" +
            "            variable_id,\n" +
            "            ctv.ctv_Measurement_Units\n" +
            "    FROM\n" +
            "        newvarexpdb.common AS common\n" +
            "    RIGHT JOIN newvarexpdb.ctv AS ctv ON ctv_variable_id\n" +
            "    WHERE\n" +
            "        ctv.ctv_variable_id = common.variable_id UNION ALL SELECT \n" +
            "        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,\n" +
            "            variable_id,\n" +
            "            cnt.cnt_Measurement_Units\n" +
            "    FROM\n" +
            "        newvarexpdb.common AS common\n" +
            "    RIGHT JOIN newvarexpdb.cnt AS cnt ON cnt_variable_id\n" +
            "    WHERE\n" +
            "        cnt.cnt_variable_id = common.variable_id UNION ALL SELECT \n" +
            "        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,\n" +
            "            variable_id,\n" +
            "            chr.chr_Measurement_Units\n" +
            "    FROM\n" +
            "        newvarexpdb.common AS common\n" +
            "    RIGHT JOIN newvarexpdb.chr AS chr ON chr_variable_id\n" +
            "    WHERE\n" +
            "        chr.chr_variable_id = common.variable_id) AS new_table ON old_table.tagName = new_table.tagName) UnitsTable \n" +
            "SET \n" +
            "    result.`Units Test` = IF(UnitsTable.MeasurementUnits1 <=> UnitsTable.MeasurementUnits2,\n" +
            "        'PASS',\n" +
            "        'FAIL'),\n" +
            "    result.`Comment` = CONCAT(result.`Comment`,\n" +
            "            IF(UnitsTable.MeasurementUnits1 <> UnitsTable.MeasurementUnits2,\n" +
            "                '\n" +
                    "                 Units (60) do not match between new and old configs',\n" +
            "                ''))\n" +
            "WHERE\n" +
            "    result.tagName = UnitsTable.tagName;";

    //This tests the analog max and min ratios
    private String analogRatioTest =
            "Update " + Result.resultDatabaseName() + " result,\n" +
                    "    (SELECT \n" +
                    "        tagName AS newTagName,\n" +
                    "            matrikonTag AS matrikonTagName,\n" +
                    "            matrikonLowRatio,\n" +
                    "            matrikonHiRatio,\n" +
                    "            ROUND(minEquipmentVal / minDisplayVal, 2) AS newConfigMinRatio,\n" +
                    "            ROUND(maxEquipmentVal / maxDisplayVal, 2) AS newConfigMaxRatio\n" +
                    "    FROM\n" +
                    "        (SELECT \n" +
                    "        matrikon_id,\n" +
                    "            CONCAT(matrikon_group_name, '.', matrikon_name) AS matrikonTag,\n" +
                    "            matrikon_hiRaw,\n" +
                    "            matrikon_lowRaw,\n" +
                    "            matrikon_hiScaled,\n" +
                    "            matrikon_loScaled,\n" +
                    "            ROUND(matrikon_lowRaw / matrikon_loScaled, 2) AS matrikonLowRatio,\n" +
                    "            ROUND(matrikon_hiRaw / matrikon_hiScaled, 2) AS matrikonHiRatio\n" +
                    "    FROM\n" +
                    "        matrikondb.matrikon) AS matrikonTable\n" +
                    "    RIGHT JOIN (SELECT \n" +
                    "        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,\n" +
                    "            variable_id,\n" +
                    "            reg_Minimium_display_value AS minDisplayVal,\n" +
                    "            reg_Minimum_equipment_value AS minEquipmentVal,\n" +
                    "            reg_Maximum_display_value AS maxDisplayVal,\n" +
                    "            reg_Maximum_equipment_value AS maxEquipmentVal\n" +
                    "    FROM\n" +
                    "        newvarexpdb.common AS common\n" +
                    "    RIGHT JOIN newvarexpdb.reg AS reg ON reg_variable_id\n" +
                    "    WHERE\n" +
                    "        reg.reg_variable_id = common.variable_id UNION ALL SELECT \n" +
                    "        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,\n" +
                    "            variable_id,\n" +
                    "            ctv_Minimium_display_value AS minDisplayVal,\n" +
                    "            ctv_Minimum_equipment_value AS minEquipmentVal,\n" +
                    "            ctv_Maximum_display_value AS maxDisplayVal,\n" +
                    "            ctv_Maximum_equipment_value AS maxEquipmentVal\n" +
                    "    FROM\n" +
                    "        newvarexpdb.common AS common\n" +
                    "    RIGHT JOIN newvarexpdb.ctv AS ctv ON ctv_variable_id\n" +
                    "    WHERE\n" +
                    "        ctv.ctv_variable_id = common.variable_id UNION ALL SELECT \n" +
                    "        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,\n" +
                    "            variable_id,\n" +
                    "            cnt_Minimium_display_value AS minDisplayVal,\n" +
                    "            cnt_Minimum_equipment_value AS minEquipmentVal,\n" +
                    "            cnt_Maximum_display_value AS maxDisplayVal,\n" +
                    "            cnt_Maximum_equipment_value AS maxEquipmentVal\n" +
                    "    FROM\n" +
                    "        newvarexpdb.common AS common\n" +
                    "    RIGHT JOIN newvarexpdb.cnt AS cnt ON cnt_variable_id\n" +
                    "    WHERE\n" +
                    "        cnt.cnt_variable_id = common.variable_id UNION ALL SELECT \n" +
                    "        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,\n" +
                    "            variable_id,\n" +
                    "            chr_Minimium_display_value AS minDisplayVal,\n" +
                    "            chr_Minimum_equipment_value AS minEquipmentVal,\n" +
                    "            chr_Maximum_display_value AS maxDisplayVal,\n" +
                    "            chr_Maximum_equipment_value AS maxEquipmentVal\n" +
                    "    FROM\n" +
                    "        newvarexpdb.common AS common\n" +
                    "    RIGHT JOIN newvarexpdb.chr AS chr ON chr_variable_id\n" +
                    "    WHERE\n" +
                    "        chr.chr_variable_id = common.variable_id) AS new_config ON matrikonTable.matrikonTag = new_config.tagName) AnalogsRatioTable \n" +
                    "SET \n" +
                    "    result.`Analogs Minimum Ratio Test` = IF(IF((newConfigMinRatio IS NULL\n" +
                    "                OR newConfigMinRatio = 0)\n" +
                    "                AND (AnalogsRatioTable.matrikonTagName IS NOT NULL),\n" +
                    "            TRUE,\n" +
                    "            (matrikonLowRatio <=> newConfigMinRatio)\n" +
                    "                AND (AnalogsRatioTable.newTagName IS NOT NULL)),\n" +
                    "        'PASS',\n" +
                    "        'FAIL'),\n" +
                    "    result.`Analogs Maximum Ratio Test` = IF((AnalogsRatioTable.matrikonHiRatio <=> AnalogsRatioTable.newConfigMaxRatio)\n" +
                    "            AND (AnalogsRatioTable.matrikonTagName IS NOT NULL),\n" +
                    "        'PASS',\n" +
                    "        'FAIL'),\n" +
                    "    result.`Comment` = CONCAT(result.`Comment`,\n" +
                    "            IF(AnalogsRatioTable.newConfigMinRatio IS NULL,\n" +
                    "                '\n" +
                    "                 min display value (62) is zero',\n" +
                    "                ''),\n" +
                    "            IF(AnalogsRatioTable.newConfigMinRatio = 0,\n" +
                    "                '\n" +
                    "                 min equipment value (65) is zero',\n" +
                    "                ''),\n" +
                    "            IF(AnalogsRatioTable.newConfigMinRatio <> AnalogsRatioTable.matrikonLowRatio,\n" +
                    "                '\n" +
                    "                 min equipment val (65) and min display val (62) does not match between new nad matrikon config',\n" +
                    "                ''),\n" +
                    "            IF(AnalogsRatioTable.matrikonHiRatio <> AnalogsRatioTable.newConfigMaxRatio,\n" +
                    "                '\n" +
                    "                 max equipment val (66) and max display val (63) does not match between new and matrikon config',\n" +
                    "                ''),\n" +
                    "            IF(AnalogsRatioTable.matrikonTagName IS NULL,\n" +
                    "                '\n" +
                    "                 tag doesn\\'t exist in matrikon config',\n" +
                    "                ''))\n" +
                    "WHERE\n" +
                    "    result.tagName = AnalogsRatioTable.newTagName;";

    //This test is to update DNP3 Type. It checks to see if the oldSource is 'O' and for the same tags, check if the new source in the new config is '3'
    private String dnp3TypeTest =
            "Update " + Result.resultDatabaseName() + " result,\n" +
            "    (SELECT \n" +
            "        newConfigTable.variable_id,\n" +
            "            newTagName AS tagName,\n" +
            "            newConfigTable.source AS newSource,\n" +
            "            oldConfigTable.source AS oldSource\n" +
            "    FROM\n" +
            "        (SELECT \n" +
            "        *,\n" +
            "            TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS newTagName\n" +
            "    FROM\n" +
            "        newvarexpdb.common AS common\n" +
            "    WHERE\n" +
            "        source = '3') AS newConfigTable\n" +
            "    LEFT JOIN (SELECT \n" +
            "        *,\n" +
            "            TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS oldTagName\n" +
            "    FROM\n" +
            "        oldvarexpdb.common AS common\n" +
            "    WHERE\n" +
            "        source = 'O') AS oldConfigTable ON newTagName = oldTagName) Dnp3TypeTable \n" +
            "SET \n" +
            "    result.`Type Test` = IF(oldSource = 'O' AND newSource = '3',\n" +
            "        'PASS',\n" +
            "        'FAIL'),\n" +
            "    result.`Comment` = CONCAT(result.`Comment`,\n" +
            "            IF(oldSource = 'O' AND newSource <> '3',\n" +
            "                '\n" +
                    "                 Source (17) hasn\\'t been updated to \\'3\\' in new config',\n" +
            "                ''))\n" +
            "WHERE\n" +
            "    result.tagName = Dnp3TypeTable.tagName;";

    //This tests to see if  the item paths for DNP3 is the same (This is the SEL path)
    private String sourceTest =
            "Update " + Result.resultDatabaseName() + " result,\n" +
            "    (SELECT \n" +
            "        *\n" +
            "    FROM\n" +
            "        (SELECT \n" +
            "        commonTable.variable_id,\n" +
            "            commonTable.source,\n" +
            "            commonTable.tagName,\n" +
            "            CONCAT(dnp3_master_device_name, '.', LPAD(dnp3_master_type, 3, '0'), '.', dnp3_master_AOB_PointVariation, '.', dnp3_master_point_address) AS newConfigItemPath\n" +
            "    FROM\n" +
            "        (SELECT \n" +
            "        source,\n" +
            "            variable_id,\n" +
            "            TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName\n" +
            "    FROM\n" +
            "        newvarexpdb.common AS common\n" +
            "    WHERE\n" +
            "        source = '3' OR source = 'O') AS commonTable\n" +
            "    INNER JOIN (SELECT \n" +
            "        *\n" +
            "    FROM\n" +
            "        newvarexpdb.dnp3_master) AS dnp3Table ON commonTable.variable_id = dnp3Table.dnp3_master_variable_id) AS newConfigTable\n" +
            "    INNER JOIN (SELECT \n" +
            "        CONCAT(matrikon_group_name, '.', matrikon_name) AS matrikonTag,\n" +
            "            matrikon_item_path AS itemPath\n" +
            "    FROM\n" +
            "        matrikondb.matrikon) AS matrikonTable ON newConfigTable.tagName = matrikonTable.matrikonTag) SourceTable \n" +
            "SET \n" +
            "    result.`OPC DNP3 Source Test` = IF(newConfigItemPath = itemPath,\n" +
            "        'PASS',\n" +
            "        'FAIL'),\n" +
            "    result.`Comment` = IF(newConfigItemPath <> itemPath,\n" +
            "        CONCAT(result.`Comment`,\n" +
            "                '\n" +
            "                 SEL item path does not match for tag'),\n" +
            "        result.`Comment`)";


    //This tests for the commandable range. This should only work for CTV tags

    private String ctvRangeTest =
            "Update " + Result.resultDatabaseName() + " result,\n" +
            "    (SELECT \n" +
            "        commonTable.tagName,\n" +
            "            commonTable.variable_id,\n" +
            "            ctvTable.ctv_Minimium_display_value AS minDisplayValue,\n" +
            "            ctvTable.ctv_Maximum_display_value AS maxDisplayValue,\n" +
            "            ctvTable.ctv_Minimum_control_value AS minControlValue,\n" +
            "            ctvTable.ctv_maximum_control_value AS maxControlValue\n" +
            "    FROM\n" +
            "        (SELECT \n" +
            "        TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName,\n" +
            "            common.*\n" +
            "    FROM\n" +
            "        newvarexpdb.common AS common\n" +
            "    WHERE\n" +
            "        source = 'CTV') AS commonTable\n" +
            "    INNER JOIN (SELECT \n" +
            "        *\n" +
            "    FROM\n" +
            "        newvarexpdb.ctv) AS ctvTable ON ctvTable.ctv_variable_id = commonTable.variable_id) RangeTable \n" +
            "SET \n" +
            "    result.`Commandable Range Test` = IF(minControlValue = minDisplayValue\n" +
            "            AND maxDisplayValue = maxControlValue,\n" +
            "        'PASS',\n" +
            "        'FAIL'),\n" +
            "    result.`Comment` = CONCAT(result.`Comment`,\n" +
            "            IF(minControlValue <> minDisplayValue,\n" +
            "                '\n" +
                    "                 min commandable ranges (70) does not match with min display value (62)',\n" +
            "                ''),\n" +
            "            IF(maxDisplayValue <> maxControlValue,\n" +
            "                '\n" +
                    "                 max commandable ranges (71) does not match with max display value (63)',\n" +
            "                ''))\n" +
            "WHERE\n" +
            "    result.tagName = RangeTable.tagName;";

    private String internalTest =
            "Update " + Result.resultDatabaseName() + " result,\n" +
            "(\n" +
            "\tSelect newTagName, oldTagName, newCommon.source as newSource, oldCommon.source as oldSource\n" +
            "\tfrom \n" +
            "\t(\n" +
            "\t\tselect common.variable_id, common.source, trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as newTagName \n" +
            "\t\t\tfrom newvarexpdb.common as common\n" +
            "\t\t\twhere source=\"I\"\n" +
            "\t) as newCommon\n" +
            "\tleft join\n" +
            "\t(\n" +
            "\t\tselect common.variable_id, common.source, trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as oldTagName \n" +
            "\t\t\tfrom oldvarexpdb.common as common\n" +
            "\t\t\twhere source=\"I\"\n" +
            "\t) as oldCommon\n" +
            "\ton oldTagName = newTagName\n" +
            "\t\n" +
            ") InternalTable\n" +
            "set result.`Internal Type Check Test` = if((newTagName is not null) and (oldTagName is not null),'PASS','FAIL'),\n" +
            "\tresult.`Comment`= concat(result.`Comment`,\n" +
            "\t\tif( (newTagName is null) or (oldTagName is null), \"\\n The internal tags don't match\",'')\n" +
            ")\n" +
            "where result.tagName = InternalTable.newTagName;\n";

    private String producerTest =
            "Update " + Result.resultDatabaseName() + " result,\n" +
            "    (SELECT \n" +
            "        newTable.tagName AS TagName,\n" +
            "            newTable.topology_server AS newProducer,\n" +
            "            newTable.source AS newSource,\n" +
            "            newTable.topology_client AS newClient,\n" +
            "            oldTable.topology_server AS oldProducer,\n" +
            "            oldTable.source AS oldSource,\n" +
            "            oldTable.topology_client AS oldClient\n" +
            "    FROM\n" +
            "        (SELECT \n" +
            "        REGEXP_SUBSTR(common.topology_server, '[[:digit:]]+') AS temp,\n" +
            "            common.topology_client,\n" +
            "            common.topology_server,\n" +
            "            common.variable_id,\n" +
            "            common.source,\n" +
            "            TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName\n" +
            "    FROM\n" +
            "        newvarexpdb.common AS common) AS newTable\n" +
            "    LEFT JOIN (SELECT \n" +
            "        common.topology_server,\n" +
            "            common.topology_client,\n" +
            "            common.variable_id,\n" +
            "            common.source,\n" +
            "            TRIM(TRAILING '.' FROM CONCAT(common.1st_element, '.', common.2nd_element, '.', common.3rd_element, '.', common.4th_element, '.', common.5th_element, '.', common.6th_element, '.', common.7th_to_12th)) AS tagName\n" +
            "    FROM\n" +
            "        oldvarexpdb.common AS common) AS oldTable ON newTable.tagName = oldTable.tagName) ProducerTable \n" +
            "SET \n" +
            "    result.`Producer Test` = IF(newSource = 'I',\n" +
            "        IF(newSource = oldSource,\n" +
            "            'PASS',\n" +
            "            'FAIL'),\n" +
            "        IF(REGEXP_SUBSTR(newProducer, '[:alpha:]+') = 'ST'\n" +
            "                AND REGEXP_SUBSTR(newClient, '[:digit:]+') = REGEXP_SUBSTR(oldClient, '[:digit:]+'),\n" +
            "            'PASS',\n" +
            "            'FAIL')),\n" +
            "    result.`Comment` = CONCAT(result.`Comment`,\n" +
            "            IF(REGEXP_SUBSTR(newClient, '[:digit:]+') <> REGEXP_SUBSTR(oldClient, '[:digit:]+'),\n" +
            "                '\n" +
            "                 Check Producer. Stations does not match ',\n" +
            "                ''),\n" +
            "            IF(newSource = 'I'\n" +
            "                    AND newSource <> oldSource,\n" +
            "                '\n" +
            "                 Source types does not match',\n" +
            "                ''))\n" +
            "WHERE\n" +
            "    result.tagName = ProducerTable.tagName;";


    private static final Logger log = LogManager.getLogger(ComparisonTest.class);
    public ComparisonTest(String matrikonDB, String newConfigDB, String oldConfigDB, String resultDB) {

        ComparisonTest.matrikonDB = matrikonDB;
        ComparisonTest.newConfigDB = newConfigDB;
        ComparisonTest.oldConfigDB = oldConfigDB;
        ComparisonTest.resultDB = resultDB;

        databaseList = new ArrayList<>();

        databaseList.add(matrikonDB);
        databaseList.add(newConfigDB);
        databaseList.add(oldConfigDB);
        databaseList.add(resultDB);

        dbConnector.sqlExecute(resultDB, initialInsert);
        testList.add(tagMatchTest);
        testList.add(descriptionTest);
        testList.add(digitalsTest);
        testList.add(unitsTest);
        testList.add(analogRatioTest);
        testList.add(dnp3TypeTest);
        testList.add(sourceTest);
        testList.add(ctvRangeTest);
        testList.add(internalTest);
        testList.add(producerTest);

    }

    /*
    This calls the dbConnector class and executes all the statements in the testList array
     */
    public static void executeTest() {
        ExecutorService testService;
        try {
            PropertyManager.getPropertyValues();
            testService = Executors.newFixedThreadPool(PropertyManager.getResultThreads());
        } catch (IOException e) {

            testService = Executors.newFixedThreadPool(MAX_THREADS);
        }

        for (String test : testList) {
            log.info("Executing test: " + test);
            testService.submit(new testExecutorHelper(resultDB, test));
            log.info("Finished test:" + test);
        }
        testService.shutdown();

        //Wait until all tests are completed
        while (!testService.isTerminated()) {
        }

        //This clears up the testList list. This is so if user needs to run it again, it will prevent double
        //entries. Holy shit, did I program myself into a ditch.
        testList.clear();

        //Close the dbConnection
        dbConnector.close();
    }

    private static class testExecutorHelper implements Callable<String> {

        private String resultDB;
        private String testName;

        public testExecutorHelper(String resultDB, String test) {
            this.resultDB = resultDB;
            this.testName = test;
        }

        @Override
        public String call() throws Exception {
            dbConnector.sqlExecute(resultDB, testName);
            return "";
        }
    }


    /*
    Checks to see if the databases in this class (as in set up in the constructuor) exists or not
     */
    public boolean checkDBExistance() {
        boolean databaseRequirement = true;
        for (String database : databaseList) {
            databaseRequirement = databaseRequirement && dbConnector.verifyDBExists(database);
        }
        return databaseRequirement;
    }
}
