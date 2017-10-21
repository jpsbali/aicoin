/**
@function Escape Special Characters in JSON strings
@param {string} input - Input String
@returns {string} the same string as the input but now escaped
*/
function escapeSpecialChars(input) {
          if(typeof(input) != 'undefined' && input != null)
          {
          return input
    .replace(/[\\]/g, '\\\\')
    .replace(/[\"]/g, '\\\"')
    .replace(/[\/]/g, '\\/')
    .replace(/[\b]/g, '\\b')
    .replace(/[\f]/g, '\\f')
    .replace(/[\n]/g, '\\n')
    .replace(/[\r]/g, '\\r')
    .replace(/[\t]/g, '\\t'); }
          else{
                    return "";
          }
}

/**
@function Converts any XSJS RecordSet object to a JSON Object
@param {object} rs - XSJS Record Set object
@param {optional String} rsName - name of the record set object in the JSON
@returns {object} JSON representation of the record set data
*/
function recordSetToJSON(rs,rsName){
          rsName = typeof rsName !== 'undefined' ? rsName : 'entries';
 
          var meta = rs.getMetaData();
          var colCount = meta.getColumnCount();
          var values=[];
          var table=[];
          var value="";
          while (rs.next()) {
          for (var i=1; i<=colCount; i++) {
                    value = '"'+meta.getColumnLabel(i)+'" : ';
               switch(meta.getColumnType(i)) {
               case $.db.types.VARCHAR:
               case $.db.types.CHAR:
                    value += '"'+ escapeSpecialChars(rs.getString(i))+'"';
                    break;
               case $.db.types.NVARCHAR:
               case $.db.types.NCHAR:
               case $.db.types.SHORTTEXT:
                    value += '"'+escapeSpecialChars(rs.getNString(i))+'"';
                    break;
               case $.db.types.TINYINT:
               case $.db.types.SMALLINT:
               case $.db.types.INT:
               case $.db.types.BIGINT:
                    value += rs.getInteger(i);
                    break;
               case $.db.types.DOUBLE:
                    value += rs.getDouble(i);
                    break;
               case $.db.types.DECIMAL:
                    value += rs.getDecimal(i);
                    break;
               case $.db.types.REAL:
                    value += rs.getReal(i);
                    break;
               case $.db.types.NCLOB:
               case $.db.types.TEXT:
                    value += '"'+ escapeSpecialChars(rs.getNClob(i))+'"';
                    break;
               case $.db.types.CLOB:
                    value += '"'+ escapeSpecialChars(rs.getClob(i))+'"';
                    break;                   
               case $.db.types.BLOB:
                          value += '"'+ $.util.convert.encodeBase64(rs.getBlob(i))+'"';
                    break;                   
               case $.db.types.DATE:
                    value += '"'+rs.getDate(i)+'"';
                    break;
               case $.db.types.TIME:
                    value += '"'+rs.getTime(i)+'"';
                    break;
               case $.db.types.TIMESTAMP:
                    value += '"' + (rs.getTimestamp(i)).getTime() + '"';
                    break;
               case $.db.types.SECONDDATE:
                    value += '"'+rs.getSeconddate(i)+'"';
                    break;
               default:
                    value += '"'+escapeSpecialChars(rs.getString(i))+'"';
               }
               values.push(value);
               }
             table.push('{'+values+'}');
          }
          return           JSON.parse('{"'+ rsName +'" : [' + table          +']}');
 
}

/**
 * @param {connection} Connection - The SQL connection used in the OData request
 * @param {beforeTableName} String - The name of a temporary table with the single entyr before the operation  (UPDATE and DELETE events only)
 * @param {afterTableName} String - The name of a temporary table with the single entry after the operation (CREATE and UPDATE events only)
**/
function diamondCreate(param) {
    var after = param.afterTableName;
    
    console.log(after);
    
    //Get Input New Record Values
    var pStmt = param.connection.prepareStatement('select * from "' + after + '"');
    var Data = recordSetToJSON(pStmt.executeQuery(), 'Details');
    pStmt.close();

	// 01 FAULT_ID
	// 02 ASSET_ID
	// 03 FAULT_TEXT
	// 04 FAULT_SIGNATURE
	// 05 FAULT_STATUS
	// 06 FAULT_AIBC_STATUS

	// 01 DIAMOND_ID" INTEGER CS_INT NOT NULL,
	// 02 "DESCRIPTION" NVARCHAR(45),
	// 03 "CUT" NVARCHAR(45),
	// 04 "COLOR" NVARCHAR(45),
	// 05 "CLARITY" NVARCHAR(45),
	// 06 "CARAT" NVARCHAR(45),
	// 07 "SHAPE" NVARCHAR(45),
	// 08 "CERTIFICATION" NVARCHAR(45),
	// 09 "QUALITY" NVARCHAR(45),
	// 10 "WEIGHT" NVARCHAR(45),
	// 11 "MEASUREMENTS" NVARCHAR(45),
	// 12 "ITEMID" NVARCHAR(45),
	// 13 "EMAIL" NVARCHAR(45),
	// 14 "AIBC_TRANS" NVARCHAR(45),
	// 15 "ROWHASH" NVARCHAR(45),
	// 16 "STATUS" VARCHAR(1) DEFAULT 'I',
	// 17 "ACCTID" 
	// 18 "AIBC_STATUS"
    
    // Model after diamond table
    var DIAMOND_ID = parseInt(Data.Details[0].DIAMOND_ID);
    var DESCRIPTION = Data.Details[0].DESCRIPTION;
    var CUT = Data.Details[0].CUT;
    var COLOR = Data.Details[0].COLOR;
    var CLARITY = Data.Details[0].CLARITY;
    var CARAT = Data.Details[0].CARAT;
    var SHAPE = Data.Details[0].SHAPE;
    var CERTIFICATION = Data.Details[0].CERTIFICATION;
    var QUALITY = Data.Details[0].QUALITY;
    var WEIGHT = Data.Details[0].WEIGHT;
    var MEASUREMENTS = Data.Details[0].MEASUREMENTS;
    var ITEMID = Data.Details[0].ITEMID;
    var EMAIL = Data.Details[0].EMAIL;
    var AIBC_TRANS = Data.Details[0].AIBC_TRANS;
    var ROWHASH = Data.Details[0].ROWHASH;
    var STATUS = Data.Details[0].STATUS;
	var ACCTID = Data.Details[0].ACCTID;
    var AIBC_STATUS = Data.Details[0].AIBC_STATUS;
    
    // If needed Validate Parameters
    // if (!validateField2Empty(ASSET_ID)) {
    //     throw 'Invalid Asset ID : ' + ASSET_ID + '';
    // }

    console.log("Processing Request ...");
    
    //Get Next Sequence Number
    pStmt = param.connection.prepareStatement('select "aicoin.db::diamondId".NEXTVAL from dummy');
    var rs = pStmt.executeQuery();
    var nextDiamondId = '';
    while (rs.next()) {
        nextDiamondId = rs.getString(1);
    }
    pStmt.close();
    
    // Calculate New Fault Signature
	ROWHASH = nextDiamondId + "_" + ROWHASH;    
	
	// Set DIAMOND_ID
	DIAMOND_ID = nextDiamondId;
	
	// Log incoming values
    console.log(DIAMOND_ID);
    console.log(DESCRIPTION);
    console.log(CUT);
    console.log(COLOR);
    console.log(CLARITY);
    console.log(CARAT);
    console.log(SHAPE);
    console.log(CERTIFICATION);
    console.log(QUALITY);
    console.log(WEIGHT);
    console.log(MEASUREMENTS);
    console.log(ITEMID);
    console.log(EMAIL);
    console.log(AIBC_TRANS);
    console.log(ROWHASH);
    console.log(STATUS);
    console.log(ACCTID);
    console.log(AIBC_STATUS);
    	
    for (var i=0; i<2; i++) {
        if (i<1) {
            pStmt = param.connection.prepareStatement('insert into "aicoin.db::SAP_HANA_BLOCKCHAIN.DIAMOND" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)');
        }
        else {
            pStmt = param.connection.prepareStatement('TRUNCATE TABLE "' + after + '" ');
            pStmt.executeUpdate();
            pStmt.close();
            
            pStmt = param.connection.prepareStatement('insert into "' + after + '" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)');
        }
 
        pStmt.setInteger(1, parseInt(DIAMOND_ID));
        pStmt.setString (2, DESCRIPTION);
        pStmt.setString (3, CUT);
        pStmt.setString (4, COLOR);
        pStmt.setString (5, CLARITY);
        pStmt.setString (6, CARAT);
        pStmt.setString (7, SHAPE);
        pStmt.setString (8, CERTIFICATION);
        pStmt.setString (9, QUALITY);
        pStmt.setString (10, WEIGHT);
        pStmt.setString (11, MEASUREMENTS);
        pStmt.setString (12, ITEMID);
        pStmt.setString (13, EMAIL);
        pStmt.setString (14, AIBC_TRANS);
        pStmt.setString (15, ROWHASH);
        pStmt.setString (16, STATUS);
        pStmt.setString (17, ACCTID);
        pStmt.setString (18, AIBC_STATUS);
        pStmt.executeUpdate();
        pStmt.close();
    }
}

function validateField2Empty(the_field) {
    if (the_field === '') {
        return false;
    }
    else {
        return true;
    }
}