# Varexp Interface

The Varexp Interface is currently a tool that allows a user to compare between several Varexp.txt config files and a Matrikon config file and conduct a series of tests.

## Prerequities 

### Required Software Installation:

- Mysql
- Java 8 (or higher)

### Additional Requirements 

- A production account called "Production" created on MySQL. You can
  technically call it something else, but if you do, the properties.xml file
(more on this in ABOUT_PROPERTIES) needs
to be updated as such

- Make sure the MySQL installation has write access to an output folder. To
  double check this, do the following:

## Basic Usage

- Copy the Folder "VarexpComparison" from "\\files.wosrpt.us\NCC-ACC\Stephen C\varexp\" to anywhere on
  PT1-SV-GEPORA (or any other machine on WOSRPT). This is where the latest
version should be until further notice
- Update the properties.xml file if needed (More on this in ABOUT_PROPERTIES)
- Double click on the VarexpInterface.jar file 
- Click on the Old Config button and open up the PcVue 9 varexp file of your
  choosing
- Click on the New Config button and open up the PcVue 11 varexp file of your
  choosing
- Click on the Matrikon Config button and open up the Matrikon config file of your
  choosing

- Click the Compare! button and let it run

## Tests Columns in the Result File

 The tests are created based on Victor's requirements and so the name of the
tests may seem somewhat cryptic. This section will go into more detail of the
test conducted.

  In general, all tests conducted are shown with a PASS or a FAIL. However,
certain tests are not applicable to certain tags, for example, digital tests
should never be conducted on analog OPC tags and vice versa. More details will
be given in the tests respective sections.

  If a test has failed, then a remark will be made in the Comments section on
the far right

  With the exception of the Commandable Rnage Test and the Internal Type Check
test, if a column is completely empty (excluding the header), then that may
imply something happened and the test was not executed. At this point, let
Stephen know so he can investigate

  In addition, both the new config file is used as the comparison file.
Meaning, the old config or the matrikon config is always being compared to the
new config and an old config is **never** compared to a matrikon file

   Nomenclature wise, numbered columns refer to the columns in a PcVue config
while letters (columns I,J, K, etc.) refers to the columns in a matrikon
config

### Tests

  * Tag Name Test: This tests matches the tag name between the three config
    files. 
	* PASS: If the concatenated OPC tag name matches between the old, new
	  and matrikon config
	* FAIL: If the concatenated OPC tag is missing in matrikon or the old
	  config

  * Description Test: Test to check the description (columns 10,11) between the old config and
    new config
	* PASS: If the description matches (word for word)
	* FAIL: If the description doesn't match

  * Digitals Test: **Used for digital tags only**. Test to see if columns 40,41,42,43,45 and 46
    matches between the old config and the new config
	* PASS: If the content in the noted columns matches between the old
	  and new config for an OPC tag
	* FAIL: Otherwise

  * Units Test: **Used for analog tags only**. Test to see if the units
    (column 60) matches between the new config and the matrikon config matches
	* PASS: If the units matches between the new and old config
	* FAIL: Otherwise

  * Analogs Minimum Ratio Test: **Used for analog tags only**. Test to see if the ratio of min equip value and min display values in PcVue is equal to the same ratio in the matrikon config (columns 65/62 = columns J/L).
	**Note that min equap value (col 62) can be zero, which may cause some
ambiguity**
	* PASS: If the ratio matches
	* FAIL: Otherwise

  * Analogs Maximum  Ratio Test: **Used for analog tags only**: Test to see if
    the ratio of max equip value and max display values in PcVue is equal to
the same ratio in the matrikon config (columns 66/63 = columns I/K).
	* PASS: If the ratio matches
	* FAIL: Otherwise

  * Type Test: **Used only for OPC/DNP3 Tags**. 
	Test to see if tags with source 'O' in old config have been changed to
'3' in the new config
	* PASS: If a tag with source 'O' in the old config have been updated
	  with source '3' in the new config
	* FAIL: Otherwise

  * OPC DNP3 Source Test: **Used only for OPC tags**
	Test  if the full SEL path in the new config (column 208,209,210,211) matches with the SEL path
in the matrikon config (Column C)
	* PASS: if the OPC tag is found in the matrikon file and the
	  concatenated SEL path (PcVue) matches the SEL path in matrikon
config
	* FAIL: Otherwise

  * Commandable Range Test: **Used for CTV Tags only and only in new config**
	Checks the commandable range in column 70,71 matches with
columns 62 and 63 in the new config file
	* PASS: For a CTV tag If columns 70 matches with 62 and column 71 matches with 63.
	* FAIL: Otherwise

  * Internal Type Check Test: **Used for Internal tags only**
	Checks to see if the interanl tags between the new config and the old
config still matches
	* PASS: If there has been no change in internal tag
	* FAIL: Otherwise

  * Producer Test: **used only for internal tags** Test to see if the Producer matches between the new config
    and old config (Column 28). However, because the station this field varies between field and
FEs, what this test actually does is compare to see if the station matches and
if the station number matches. (Ex: ST08 compare with WF08 will result in a
pass)
	* PASS: If the sources between the new config and old config are
	  internal and the 'station' matches
	* FAIL: Otherwise

## ABOUT_PROPERTIES

The properties file is a basic configuration file for the VarexpInterface.jar
file. More importantly, the VarexpInterface.jar might not work correctly
depending on certain configurations. 

If the properties.xml is not in the same direction as the VarexpInterface.jar
(missing), then the VarexpInterface.jar will generate one with the
default parameters. However, the default parameters might not be what the user
needs. See section below

### Important Parameters

The properties.xml file comes with seven parameters as of this writing.
However, the only ones that are of important are as follows:

* User. This is the user for MySQL. If the user doesn't exist on the MySQL
  server, then the program will fail. The default is "production"
* Password. This is the password for the production account on MySQL. If the
  passsword doesn't match, then the program will not work as intented. The password is not hashed and the default is ZAQ!xsw2 

* databaseIP: This is the IP or Hostname of where MySQL is installed. If this
  is changed, to a different IP where there is no MYSQL installed, then the
  program will throw an error. Default is 'localhost'. 

* defaultFileName: This is the filename of the csv file that contains all the
  pass/fail tests. The default is "output.csv"

* defaultFilePath: This is the location the pass/fail test will be saved at.
  **Important**: You need to add the '\' at the end of the path
