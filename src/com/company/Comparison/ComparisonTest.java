package com.company.Comparison;

import com.company.Database.dbConnector;

import java.util.ArrayList;
import java.util.List;

public class ComparisonTest {

    private static List<String> testList = new ArrayList<String>();

    private static String oldConfigDB;
    private static String newConfigDB;
    private static String matrikonDB;
    private static String resultDB;
    private static List<String> databaseList;


    //This is an initial insert. All it does is insert all the tags from the new config to the output DB.
    //Must be executed before all the others
    private String initialInsert = "insert into resultOutput.resultTable (TagName,\n" +
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
    private String tagMatchTest = "Update resultOutput.resultTable result,\n" +
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
    private String descriptionTest = "Update resultOutput.resultTable result,\n" +
            "(\n" +
            "\tselect newConfigTable.tagName, newConfigTable.desc_1st_lang as newConfigDesc1, newConfigTable.desc_2nd_lang as newConfigDesc2, oldConfigTable.desc_1st_lang as oldConfigDesc1,oldConfigTable.desc_2nd_lang as oldConfigDesc2 from\n" +
            "\t(\n" +
            "\tselect variable_id, desc_1st_lang, desc_2nd_lang, \n" +
            "\t\t\ttrim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th))  as tagName\n" +
            "\t\tfrom oldvarexpdb.common as common\n" +
            "\t\tgroup by tagName \n" +
            "        order by tagName asc\n" +
            "\t) as oldConfigTable\n" +
            "\tinner join\n" +
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
            "    result.`Comment` = if(newConfigDesc1 != oldConfigDesc1, concat(result.`Comment`,\"\\n New Tag description doesn't match old config's description\"), result.`Comment`),\n" +
            "    result.`Comment` = if(newConfigDesc2 != oldConfigDesc2, concat(result.`Comment`,\"\\n New Tag description (lang2) doesn't match old config's description (lang2)\"), result.`Comment`)\n" +
            "where result.tagName = DescriptionTable.tagName;";


    //Test for the digital bits. Only for digital tags
    private String digitalsTest = "Update " + Result.resultDatabaseName() + " result,\n" +
            "(\n" +
            "select t2.tagName, t2.variable_id, t1.bit_log_bit_0_to_1 as bitLog01_1, t1.bit_log_bit_1_to_0 as bitLog10_1, t1.bit_reserved bitReserved1, t1.authorisation_level as authorisationLevel1, t1.alarm_level as alarmLevel1, \n" +
            "\tt2.bit_log_bit_0_to_1 as bitLog01_2, t2.bit_log_bit_1_to_0 as bitLog10_2, t2.bit_reserved as bitReserved2, t2.authorisation_level authorisationLevel2, t2.alarm_level as alarmLevel2 from\n" +
            "(\n" +
            "\tselect * from\n" +
            "\t(\n" +
            "\t\tselect trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as tagName,variable_id, bit.bit_log_bit_0_to_1, bit.bit_log_bit_1_to_0,bit.bit_reserved, null as authorisation_level, null as alarm_level\n" +
            "\t\tfrom oldvarexpdb.common as common\n" +
            "\t\t\tright join oldvarexpdb.bit as bit on bit_variable_id\n" +
            "\t\t    where \n" +
            "\t\t\tbit.bit_variable_id = common.variable_id\n" +
            "\n" +
            "\t\tunion all\n" +
            "\n" +
            "\t\tselect trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as tagName,variable_id, cmd.cmd_log_bit_0_to_1,cmd.cmd_log_bit_1_to_0,cmd.cmd_reserved, null as authorisation_level, null as alarm_level\n" +
            "\t\tfrom oldvarexpdb.common as common\n" +
            "\t\t\tright join oldvarexpdb.cmd as cmd on cmd_variable_id\n" +
            "\t\t    where \n" +
            "\t\t\tcmd.cmd_variable_id = common.variable_id\n" +
            "\t\tunion all\n" +
            "\n" +
            "\t\tselect trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as tagName,variable_id, ala.ala_log_bit_0_to_1,ala.ala_log_bit_1_to_0,ala.ala_reserved,ala.ala_alarm_level,ala_alarm \n" +
            "\t\tfrom oldvarexpdb.common as common\n" +
            "\t\t\tright join oldvarexpdb.ala as ala on ala_variable_id\n" +
            "\t\t    where \n" +
            "\t\t\tala.ala_variable_id = common.variable_id\n" +
            "\n" +
            "\t\tunion all\n" +
            "\n" +
            "\t\tselect trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as tagName,variable_id, acm.acm_log_bit_0_to_1,acm.acm_log_bit_1_to_0,acm.acm_reserved,acm.acm_alarm_level,acm_alarm \n" +
            "\t\tfrom oldvarexpdb.common as common\n" +
            "\t\t\tright join oldvarexpdb.acm as acm on acm_variable_id\n" +
            "\t\t    where \n" +
            "\t\t\tacm.acm_variable_id = common.variable_id\n" +
            "\n" +
            "\t\tunion all\n" +
            "\n" +
            "\t\tselect trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as tagName,variable_id, tsh.tsh_log_bit_0_to_1,tsh.tsh_log_bit_1_to_0,tsh.tsh_reserved,null as tsh_alarm_level, null as tsh_alarm \n" +
            "\t\tfrom oldvarexpdb.common as common\n" +
            "\t\t\tright join oldvarexpdb.tsh as tsh on tsh_variable_id\n" +
            "\t\t    where \n" +
            "\t\t\ttsh.tsh_variable_id = common.variable_id\n" +
            "\t\t\n" +
            "\t\tunion all\n" +
            "\n" +
            "\t\tselect trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as tagName,variable_id, ats.ats_log_bit_0_to_1,ats.ats_log_bit_1_to_0,ats.ats_reserved,null as ats_alarm_level, null as ats_alarm \n" +
            "\t\tfrom oldvarexpdb.common as common\n" +
            "\t\t\tright join oldvarexpdb.ats as ats on ats_variable_id\n" +
            "\t\t    where \n" +
            "\t\t\tats.ats_variable_id = common.variable_id\n" +
            "\n" +
            "\t\t) as oldConfigTable\n" +
            "\t) as t1\n" +
            "\tinner join\n" +
            "\t(\n" +
            "\t\tselect * from\n" +
            "\t\t(\n" +
            "\t\t\tselect trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as tagName,variable_id, bit.bit_log_bit_0_to_1, bit.bit_log_bit_1_to_0,bit.bit_reserved, null as authorisation_level, null as alarm_level\n" +
            "\t\t\tfrom newvarexpdb.common as common\n" +
            "\t\t\t\tright join newvarexpdb.bit as bit on bit_variable_id\n" +
            "\t\t\t\twhere \n" +
            "\t\t\t\tbit.bit_variable_id = common.variable_id\n" +
            "\n" +
            "\t\t\tunion all\n" +
            "\n" +
            "\t\t\tselect trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as tagName,variable_id, cmd.cmd_log_bit_0_to_1,cmd.cmd_log_bit_1_to_0,cmd.cmd_reserved, null as authorisation_level, null as alarm_level\n" +
            "\t\t\tfrom newvarexpdb.common as common\n" +
            "\t\t\t\tright join newvarexpdb.cmd as cmd on cmd_variable_id\n" +
            "\t\t\t\twhere \n" +
            "\t\t\t\tcmd.cmd_variable_id = common.variable_id\n" +
            "\t\t\tunion all\n" +
            "\n" +
            "\t\t\tselect trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as tagName,variable_id, ala.ala_log_bit_0_to_1,ala.ala_log_bit_1_to_0,ala.ala_reserved,ala.ala_alarm_level,ala_alarm \n" +
            "\t\t\tfrom newvarexpdb.common as common\n" +
            "\t\t\t\tright join newvarexpdb.ala as ala on ala_variable_id\n" +
            "\t\t\t\twhere \n" +
            "\t\t\t\tala.ala_variable_id = common.variable_id\n" +
            "\n" +
            "\t\t\tunion all\n" +
            "\n" +
            "\t\t\tselect trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as tagName,variable_id, acm.acm_log_bit_0_to_1,acm.acm_log_bit_1_to_0,acm.acm_reserved,acm.acm_alarm_level,acm_alarm \n" +
            "\t\t\tfrom newvarexpdb.common as common\n" +
            "\t\t\t\tright join newvarexpdb.acm as acm on acm_variable_id\n" +
            "\t\t\t\twhere \n" +
            "\t\t\t\tacm.acm_variable_id = common.variable_id\n" +
            "\n" +
            "\t\t\tunion all\n" +
            "\n" +
            "\t\t\tselect trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as tagName,variable_id, tsh.tsh_log_bit_0_to_1,tsh.tsh_log_bit_1_to_0,tsh.tsh_reserved,null as tsh_alarm_level, null as tsh_alarm \n" +
            "\t\t\tfrom newvarexpdb.common as common\n" +
            "\t\t\t\tright join newvarexpdb.tsh as tsh on tsh_variable_id\n" +
            "\t\t\t\twhere \n" +
            "\t\t\t\ttsh.tsh_variable_id = common.variable_id\n" +
            "\t\t\t\n" +
            "\t\t\tunion all\n" +
            "\n" +
            "\t\t\tselect trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as tagName,variable_id, ats.ats_log_bit_0_to_1,ats.ats_log_bit_1_to_0,ats.ats_reserved,null as ats_alarm_level, null as ats_alarm \n" +
            "\t\t\tfrom newvarexpdb.common as common\n" +
            "\t\t\t\tright join newvarexpdb.ats as ats on ats_variable_id\n" +
            "\t\t\t\twhere \n" +
            "\t\t\t\tats.ats_variable_id = common.variable_id\n" +
            "\n" +
            "\t\t) as newConfigTable\n" +
            "\t) as t2 \n" +
            "\ton t2.tagName = t1.tagName\n" +
            ") DigitalTable\n" +
            "set result.`Digitals Test` = if(\n" +
            "\n" +
            "\tDigitalTable.bitLog01_2          <=> DigitalTable.bitLog01_2  and \n" +
            "\tDigitalTable.bitLog10_2          <=> DigitalTable.bitLog10_2  and \n" +
            "\tDigitalTable.bitReserved2        <=> DigitalTable.bitReserved2 and \n" +
            "\tDigitalTable.authorisationLevel2 <=> DigitalTable.authorisationLevel2 and\n" +
            "\tDigitalTable.alarmLevel2         <=> DigitalTable.alarmLevel2 \n" +
            "\t,'PASS','FAIL')\n" +
            "    result.`Comment` = if(DigitalTable.bitLog01_2          <> DigitalTable.bitLog01_2, concat(result.`Comment`,\"\\n bitlog01 does not match between old and new config\"), result.`Comment`),\n" +
            "    result.`Comment` = if(DigitalTable.bitLog10_2          <> DigitalTable.bitLog10_2, concat(result.`Comment`,\"\\n bitlog10 does not match between old and new config\"), result.`Comment`),\n" +
            "    result.`Comment` = if(DigitalTable.bitReserved2        <> DigitalTable.bitReserved2, concat(result.`Comment`,\"\\n bitReserved does not match between old and new config\"), result.`Comment`),\n" +
            "    result.`Comment` = if(DigitalTable.authorisationLevel2 <> DigitalTable.authorisationLevel2, concat(result.`Comment`,\"\\n authorization level does not match between old and new config\"), result.`Comment`),\n" +
            "    result.`Comment` = if(DigitalTable.alarmLevel2         <> DigitalTable.alarmLevel2 , concat(result.`Comment`,\"\\n alarm leveldoes not match between old and new config\"), result.`Comment`)\n" +
            "where result.tagName = DigitalTable.tagName;";

    //Matches the units column for analog tags
    private String unitsTest = "Update resultOutput.resultTable result,\n" +
            "(\n" +
            "\tselect new_table.tagName as tagName, old_table.reg_Measurement_Units as MeasurementUnits1, new_table.reg_Measurement_Units as MeasurementUnits2 from\n" +
            "\t(\n" +
            "\t\tselect trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as tagName,variable_id, reg.reg_Measurement_Units\n" +
            "\t\tfrom oldvarexpdb.common as common\n" +
            "\t\t\tright join oldvarexpdb.reg as reg on reg_variable_id\n" +
            "\t\t\twhere \n" +
            "\t\t\treg.reg_variable_id = common.variable_id\n" +
            "\n" +
            "\t\tunion all\n" +
            "\n" +
            "\t\tselect trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as tagName,variable_id, ctv.ctv_Measurement_Units\n" +
            "\t\tfrom oldvarexpdb.common as common\n" +
            "\t\t\tright join oldvarexpdb.ctv as ctv on ctv_variable_id\n" +
            "\t\t\twhere \n" +
            "\t\t\tctv.ctv_variable_id = common.variable_id\n" +
            "\n" +
            "\t\tunion all\n" +
            "\n" +
            "\t\tselect trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as tagName,variable_id, cnt.cnt_Measurement_Units\n" +
            "\t\tfrom oldvarexpdb.common as common\n" +
            "\t\t\tright join oldvarexpdb.cnt as cnt on cnt_variable_id\n" +
            "\t\t\twhere \n" +
            "\t\t\tcnt.cnt_variable_id = common.variable_id\n" +
            "\n" +
            "\t\tunion all\n" +
            "\n" +
            "\t\tselect trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as tagName,variable_id, chr.chr_Measurement_Units\n" +
            "\t\tfrom oldvarexpdb.common as common\n" +
            "\t\t\tright join oldvarexpdb.chr as chr on chr_variable_id\n" +
            "\t\t\twhere \n" +
            "\t\t\tchr.chr_variable_id = common.variable_id\n" +
            "\t) as old_table\n" +
            "\n" +
            "\tinner join \n" +
            "\t(\n" +
            "\t\tselect trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as tagName,variable_id, reg.reg_Measurement_Units\n" +
            "\t\tfrom newvarexpdb.common as common\n" +
            "\t\t\tright join newvarexpdb.reg as reg on reg_variable_id\n" +
            "\t\t\twhere \n" +
            "\t\t\treg.reg_variable_id = common.variable_id\n" +
            "\n" +
            "\t\tunion all\n" +
            "\n" +
            "\t\tselect trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as tagName,variable_id, ctv.ctv_Measurement_Units\n" +
            "\t\tfrom newvarexpdb.common as common\n" +
            "\t\t\tright join newvarexpdb.ctv as ctv on ctv_variable_id\n" +
            "\t\t\twhere \n" +
            "\t\t\tctv.ctv_variable_id = common.variable_id\n" +
            "\n" +
            "\t\tunion all\n" +
            "\n" +
            "\t\tselect trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as tagName,variable_id, cnt.cnt_Measurement_Units\n" +
            "\t\tfrom newvarexpdb.common as common\n" +
            "\t\t\tright join newvarexpdb.cnt as cnt on cnt_variable_id\n" +
            "\t\t\twhere \n" +
            "\t\t\tcnt.cnt_variable_id = common.variable_id\n" +
            "\n" +
            "\t\tunion all\n" +
            "\n" +
            "\t\tselect trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as tagName,variable_id, chr.chr_Measurement_Units\n" +
            "\t\tfrom newvarexpdb.common as common\n" +
            "\t\t\tright join newvarexpdb.chr as chr on chr_variable_id\n" +
            "\t\t\twhere \n" +
            "\t\t\tchr.chr_variable_id = common.variable_id\n" +
            "\t) as new_table\n" +
            "\ton old_table.tagName = new_table.tagName\n" +
            "\t\n" +
            ") UnitsTable\n" +
            "set result.`Units Test` = if(\n" +
            "\n" +
            "\tUnitsTable.MeasurementUnits1          <=> UnitsTable.MeasurementUnits2\n" +
            "result.`Comment` = if(UnitsTable.MeasurementUnits1 <> UnitsTable.MeasurementUnits2, concat(result.`Comment`,\"\\n Units do not match between new and old configs\"), result.`Comment`)\n" +
            "\t,'PASS','FAIL')\n" +
            "where result.tagName = UnitsTable.tagName;";

    //This tests the analog max and min ratios
    private String analogRatioTest = "Update resultOutput.resultTable result,\n" +
            "(\n" +
            "select matrikonTag as tagName, matrikonLowRatio,matrikonHiRatio,round(minEquipmentVal/minDisplayVal,2) as newConfigMinRatio, round(maxEquipmentVal/maxDisplayVal,2) as newConfigMaxRatio from\n" +
            "#select matrikonTag as tagName, matrikonLowRatio,matrikonHiRatio,round(maxEquipmentVal/maxDisplayVal,2) as newConfigMaxRatio from\n" +
            "#select * from\n" +
            "(\n" +
            "\tselect matrikon_id,concat(matrikon_group_name,'.',matrikon_name) as matrikonTag, matrikon_hiRaw,matrikon_lowRaw,matrikon_hiScaled,matrikon_loScaled, round(matrikon_lowRaw / matrikon_loScaled,2) as matrikonLowRatio, round(matrikon_hiRaw/matrikon_hiScaled,2) as matrikonHiRatio \n" +
            "    from matrikondb.matrikon\n" +
            ") as matrikonTable\n" +
            "\n" +
            "inner join\n" +
            "(\n" +
            "\tselect trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as tagName,variable_id, reg_Minimium_display_value as minDisplayVal,reg_Minimum_equipment_value as minEquipmentVal, reg_Maximum_display_value as maxDisplayVal,reg_Maximum_equipment_value as maxEquipmentVal\n" +
            "\t\tfrom newvarexpdb.common as common\n" +
            "\t\t\tright join newvarexpdb.reg as reg on reg_variable_id\n" +
            "\t\t\twhere \n" +
            "\t\t\treg.reg_variable_id = common.variable_id\n" +
            "\n" +
            "\t\tunion all\n" +
            "\n" +
            "\t\tselect trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as tagName,variable_id, ctv_Minimium_display_value as minDisplayVal,ctv_Minimum_equipment_value as minEquipmentVal, ctv_Maximum_display_value as maxDisplayVal,ctv_Maximum_equipment_value as maxEquipmentVal\n" +
            "\t\tfrom newvarexpdb.common as common\n" +
            "\t\t\tright join newvarexpdb.ctv as ctv on ctv_variable_id\n" +
            "\t\t\twhere \n" +
            "\t\t\tctv.ctv_variable_id = common.variable_id\n" +
            "\n" +
            "\t\tunion all\n" +
            "\n" +
            "\t\tselect trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as tagName,variable_id, cnt_Minimium_display_value as minDisplayVal,cnt_Minimum_equipment_value as minEquipmentVal, cnt_Maximum_display_value as maxDisplayVal,cnt_Maximum_equipment_value as maxEquipmentVal\n" +
            "\t\tfrom newvarexpdb.common as common\n" +
            "\t\t\tright join newvarexpdb.cnt as cnt on cnt_variable_id\n" +
            "\t\t\twhere \n" +
            "\t\t\tcnt.cnt_variable_id = common.variable_id\n" +
            "\n" +
            "\t\tunion all\n" +
            "\n" +
            "\t\tselect trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as tagName,variable_id, chr_Minimium_display_value as minDisplayVal,chr_Minimum_equipment_value as minEquipmentVal, chr_Maximum_display_value as maxDisplayVal,chr_Maximum_equipment_value as maxEquipmentVal\n" +
            "\t\tfrom newvarexpdb.common as common\n" +
            "\t\t\tright join newvarexpdb.chr as chr on chr_variable_id\n" +
            "\t\t\twhere \n" +
            "\t\t\tchr.chr_variable_id = common.variable_id\n" +
            "\t) as new_config\n" +
            "\ton matrikonTable.matrikonTag = new_config.tagName\n" +
            "\n" +
            "\t\n" +
            ") AnalogsRatioTable\n" +
            "set result.`Analogs Minimum Ratio Test` = \n" +
            "\tif(\n" +
            "\t\tif(newConfigMinRatio is null or newConfigMinRatio =0,true,\n" +
            "\t\t\t\tmatrikonLowRatio <=> newConfigMinRatio\n" +
            "        ),'PASS','FAIL'\n" +
            "    ),\n" +
            "result.`Analogs Maximum Ratio Test` =\n" +
            "    if(\n" +
            "\t\t(AnalogsRatioTable.matrikonHiRatio <=> AnalogsRatioTable.newConfigMaxRatio),'PASS','FAIL'\n" +
            "    )\n" +
            "\n" +
            "    result.`Comment` = if(AnalogsRatioTable.newConfigMinRatio is null, concat(result.`Comment`,\"\\n min display value (62) is zero\"), result.`Comment`),\n" +
            "    result.`Comment` = if(AnalogsRatioTable.newConfigMinRatio = 0, concat(result.`Comment`,\"\\n min equipment value (65) is zero\"), result.`Comment`),\n" +
            "    result.`Comment` = if(AnalogsRatioTable.newConfigMinRatio <> AnalogsRatioTable.matrikonLowRatio, concat(result.`Comment`,\"\\n min equipment val and min display val does not match between new nad matrikon config\"), result.`Comment`),\n" +
            "    result.`Comment` = if(AnalogsRatioTable.matrikonHiRatio <> AnalogsRatioTable.newConfigMaxRatio, concat(result.`Comment`,\"\\n max equipment val and max display val does not match between new and matrikon config\"), result.`Comment`)\n" +
            "where result.tagName = AnalogsRatioTable.tagName;";

    //This test is to update DNP3 Type. It checks to see if the oldSource is 'O' and for the same tags, check if the new source in the new config is '3'
    private String dnp3TypeTest = "Update resultOutput.resultTable result,\n" +
            "(\n" +
            "\tselect newConfigTable.variable_id, newTagName as tagName, newConfigTable.source as newSource, oldConfigTable.source as oldSource\n" +
            "\tfrom\n" +
            "\t\t(\n" +
            "\t\t\tselect *,trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as newTagName\n" +
            "\t\tfrom newvarexpdb.common as common\n" +
            "\t\twhere source = \"3\"\n" +
            "\t    \n" +
            "\t    ) as newConfigTable\n" +
            "\n" +
            "\tinner join\n" +
            "\n" +
            "\t(\n" +
            "\t\t\tselect *,trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as oldTagName\n" +
            "\t\tfrom oldvarexpdb.common as common\n" +
            "\t\twhere source = \"O\"\n" +
            "\t) as oldConfigTable\n" +
            "\ton newTagName = oldTagName\n" +
            ") Dnp3TypeTable\n" +
            "set result.`Type Test` = if(\n" +
            "\toldSource = 'O' and\n" +
            "    newSource = '3'\n" +
            "\t,'PASS','FAIL'),\n" +
            "result.`Comment` = if(oldSource ='O' and newSource <> '3', concat(result.`Comment`,\"\\n Source hasn't been updated to '3' in new config\"), result.`Comment`)\n" +
            "where result.tagName = Dnp3TypeTable.tagName;";

    //This tests to see if  the item paths for DNP3 is the same (This is the SEL path)
    private String sourceTest = "Update resultOutput.resultTable result,\n" +
            "(\n" +
            "\tselect * from\n" +
            "\t(\n" +
            "\t\tselect commonTable.variable_id, commonTable.source, commonTable.tagName, concat(dnp3_master_device_name,'.',lpad(dnp3_master_type,3,'0'), '.',dnp3_master_AOB_PointVariation,\".\",dnp3_master_point_address) as newConfigItemPath from\n" +
            "\t\t(\n" +
            "\t\t\tselect source,variable_id,trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as tagName \n" +
            "\t\t\t\tfrom newvarexpdb.common as common\n" +
            "\t\t\t\twhere \n" +
            "\t\t\t\t\tsource = \"3\" or source=\"O\"\n" +
            "\t\t) as commonTable\n" +
            "\t\tinner join\n" +
            "\t\t(\n" +
            "\t\t\tselect *\n" +
            "\t\t\t\tfrom newvarexpdb.dnp3_master\n" +
            "\t\t) as dnp3Table\n" +
            "\t\ton commonTable.variable_id = dnp3Table.dnp3_master_variable_id\n" +
            "\t) as newConfigTable\n" +
            "\n" +
            "\tinner join\n" +
            "\t(\n" +
            "\t\tselect concat(matrikon_group_name,'.',matrikon_name) as matrikonTag, matrikon_item_path as itemPath from matrikondb.matrikon\n" +
            "\t) as matrikonTable\n" +
            "\ton newConfigTable.tagName = matrikonTable.matrikonTag\n" +
            "\t\n" +
            ") SourceTable\n" +
            "set result.`OPC DNP3 Source Test` = if(\n" +
            "\tnewConfigItemPath = itemPath\n" +
            "\t,'PASS','FAIL'),\n" +
            "result.`Comment` = if(newConfigItemPath <> itemPath, concat(result.`Comment`,\"\\n SEL item path does not match for tag\"), result.`Comment`)\n" +
            "where result.tagName = SourceTable.tagName;";


    //This tests for the commandable range. This should only work for CTV tags
    private String ctvRangeTest = "Update resultOutput.resultTable result,\n" +
            "(\n" +
            "\tSelect commonTable.tagName,commonTable.variable_id, ctvTable.ctv_Minimium_display_value as minDisplayValue, ctvTable.ctv_Maximum_display_value as maxDisplayValue, \n" +
            "\t\tctvTable.ctv_Minimum_control_value as minControlValue, ctvTable.ctv_maximum_control_value as maxControlValue\n" +
            "    from\n" +
            "\t(\n" +
            "\t\tselect trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as tagName, common.* \n" +
            "\t\t\tfrom newvarexpdb.common as common\n" +
            "\t\t\twhere source = \"CTV\"\n" +
            "\t) as commonTable\n" +
            "\tinner join\n" +
            "\t(\n" +
            "\t\tselect *\n" +
            "\t\t\tfrom newvarexpdb.ctv\n" +
            "\t) as ctvTable\n" +
            "\ton ctvTable.ctv_variable_id = commonTable.variable_id\n" +
            "\t\n" +
            ") RangeTable\n" +
            "set result.`Commandable Range Test` = if(\n" +
            "\t\tminControlValue = minDisplayValue and \n" +
            "        maxDisplayValue = maxControlValue\n" +
            "\t,'PASS','FAIL'),\n" +
            "    result.`Comment` = if( minControlValue <> minDisplayValue , concat(result.`Comment`,\"\\n min commandable ranges does not match\"), result.`Comment`),\n" +
            "    result.`Comment` = if( maxDisplayValue <> maxControlValue , concat(result.`Comment`,\"\\n max commandable ranges does not match\"), result.`Comment`)\n" +
            "where result.tagName = RangeTable.tagName;";

    private String internalTest = "Update resultOutput.resultTable result,\n" +
            "(\n" +
            "\tSelect newTagName, oldTagName, newCommon.source as newSource, oldCommon.source as oldSource\n" +
            "\tfrom \n" +
            "\t(\n" +
            "\t\tselect common.variable_id, common.source, trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as newTagName \n" +
            "\t\t\tfrom newvarexpdb.common as common\n" +
            "\t\t\twhere source=\"I\"\n" +
            "\t) as newCommon\n" +
            "\tinner join\n" +
            "\t(\n" +
            "\t\tselect common.variable_id, common.source, trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) as oldTagName \n" +
            "\t\t\tfrom oldvarexpdb.common as common\n" +
            "\t\t\twhere source=\"I\"\n" +
            "\t) as oldCommon\n" +
            "\ton oldTagName = newTagName\n" +
            "\t\n" +
            ") InternalTable\n" +
            "set result.`Internal Type Check Test` = 'PASS'\n" +
            "where result.tagName = InternalTable.newTagName;\n";

    private String producerTest = "Update resultOutput.resultTable result,\n" +
            "(\n" +
            "\tselect newTable.tagName as TagName, \n" +
            "\tnewTable.topology_server as newProducer, newTable.source as newSource,\n" +
            "\toldTable.topology_server as oldProducer, oldTable.source as oldSource\n" +
            "\tfrom  \n" +
            "\t(\n" +
            "\t\tselect\n" +
            "\t\t\tregexp_substr(common.topology_server, '[[:digit:]]+') as temp,\n" +
            "\t\t\tcommon.topology_server, \n" +
            "\t\t\tcommon.variable_id, \n" +
            "\t\t\tcommon.source, trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) \n" +
            "\t\t\t\t\tas tagName\n" +
            "\t\t\t\tfrom newvarexpdb.common as common\n" +
            "\t) as newTable\n" +
            "\tinner join\n" +
            "\t(\n" +
            "\t\tselect common.topology_server, \n" +
            "\t\t\tcommon.variable_id, \n" +
            "\t\t\tcommon.source, trim(TRAILING '.' FROM concat(common.1st_element,'.',common.2nd_element,'.',common.3rd_element,'.',common.4th_element,'.',common.5th_element,'.',common.6th_element,'.',common.7th_to_12th)) \n" +
            "\t\t\t\t\tas tagName \n" +
            "\t\t\t\tfrom oldvarexpdb.common as common\n" +
            "\t) as oldTable\n" +
            "\n" +
            "\ton newTable.tagName = oldTable.tagName\n" +
            ") ProducerTable\n" +
            "set result.`Producer Test` = if(newSource=\"I\",\n" +
            "\tif(newSource = oldSource,'PASS','FAIL'),\n" +
            "    if(regexp_substr(newProducer, '[:alpha:]+') = 'ST','PASS','FAIL')\n" +
            "\t),\n" +
            "\tresult.`Comment` = if(regexp_substr(newClient,'[:digit:]+') <> regexp_substr(oldClient,'[:digit:]+'), concat(result.`Comment`,\"\\n Check Producer. Stations does not match \"), result.`Comment`),\n" +
            "    result.`Comment` = if( newSource = \"I\" and newSource <> oldSource , concat(result.`Comment`,\"\\n Source types does not match\"), result.`Comment`)" +
            "where result.tagName = ProducerTable.tagName;";


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

        testList.add(initialInsert);
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
        dbConnector dbConnector = new dbConnector();
        for (String test : testList) {
            dbConnector.sqlExecute(resultDB, test);
        }
    }

    /*
    Checks to see if the databases in this class (as in set up in the constructuor) exists or not
     */
    public boolean checkDBExistance() {
        boolean databaseRequirement = true;
        dbConnector dbConnector = new dbConnector();
        for (String database : databaseList) {
            databaseRequirement = databaseRequirement && dbConnector.verifyDBExists(database);
        }
        return databaseRequirement;
    }
}
