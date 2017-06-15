// $.import("mta_iot.js","crudCommon");
//$.import("crudCommon.xsjslib");


// Following logic from Thomas Jung
// http://scn.sap.com/thread/3447784

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
function faultCreate(param) {
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
    
    var FAULT_ID = parseInt(Data.Details[0].FAULT_ID);
    var ASSET_ID = parseInt(Data.Details[0].ASSET_ID);
    var FAULT_TEXT = Data.Details[0].FAULT_TEXT;
    var FAULT_SIGNATURE = Data.Details[0].FAULT_SIGNATURE;
    var FAULT_STATUS = Data.Details[0].FAULT_STATUS;
    var FAULT_AIBC_STATUS = Data.Details[0].FAULT_AIBC_STATUS;
        
    // If needed Validate Parameters
    // if (!validateField2Empty(ASSET_ID)) {
    //     throw 'Invalid Asset ID : ' + ASSET_ID + '';
    // }

    //Get Next Sequence Number
    pStmt = param.connection.prepareStatement('select "aicoin.db::faultId".NEXTVAL from dummy');
    var rs = pStmt.executeQuery();
    var nextFaultId = '';
    while (rs.next()) {
        nextFaultId = rs.getString(1);
    }
    pStmt.close();
    
    // Calculate New Fault Signature
	var faultSignature = nextFaultId + "_" + FAULT_SIGNATURE;    
	
	// Log incoming values
    console.log(nextFaultId);
    console.log(ASSET_ID);
    console.log(FAULT_TEXT);
    console.log(faultSignature);
    console.log(FAULT_STATUS);
    console.log(FAULT_AIBC_STATUS);
    	
    for (var i=0; i<2; i++) {
        if (i<1) {
            pStmt = param.connection.prepareStatement('insert into "aicoin.db::SAP_HANA_BLOCKCHAIN.FAULT" values(?,?,?,CURRENT_UTCTIMESTAMP,?,?,?)');
        }
        else {
            pStmt = param.connection.prepareStatement('TRUNCATE TABLE "' + after + '" ');
            pStmt.executeUpdate();
            pStmt.close();
            
            pStmt = param.connection.prepareStatement('insert into "' + after + '" values(?,?,?,CURRENT_UTCTIMESTAMP,?,?,?)');
        }
        
        pStmt.setInteger(1, parseInt(nextFaultId));
        pStmt.setInteger(2, ASSET_ID);
        pStmt.setString (3, FAULT_TEXT);
        pStmt.setString (4, faultSignature);
        pStmt.setString (5, FAULT_STATUS);
        pStmt.setString (6, FAULT_AIBC_STATUS);
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